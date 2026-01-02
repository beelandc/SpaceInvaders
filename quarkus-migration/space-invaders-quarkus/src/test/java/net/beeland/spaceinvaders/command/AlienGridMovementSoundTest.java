package net.beeland.spaceinvaders.command;

import net.beeland.spaceinvaders.gameobject.AlienGrid;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.sound.Sound;
import net.beeland.spaceinvaders.sound.SoundManager;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AlienGridMovementSound command
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
class AlienGridMovementSoundTest {
    
    private AlienGridMovementSound command;
    private SoundManager mockSoundManager;
    private GameObjectManager mockGameObjectManager;
    private TimerManager mockTimerManager;
    
    @BeforeEach
    void setUp() {
        command = new AlienGridMovementSound();
        
        // Create mocks
        mockSoundManager = mock(SoundManager.class);
        mockGameObjectManager = mock(GameObjectManager.class);
        mockTimerManager = mock(TimerManager.class);
        
        // Inject mocks using reflection
        injectMock(command, "soundManager", mockSoundManager);
        injectMock(command, "gameObjectManager", mockGameObjectManager);
        injectMock(command, "timerManager", mockTimerManager);
    }
    
    /**
     * Helper method to inject mocks via reflection
     */
    private void injectMock(Object target, String fieldName, Object mock) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, mock);
        } catch (Exception e) {
            fail("Failed to inject mock: " + e.getMessage());
        }
    }
    
    @Test
    void testConstructor() {
        AlienGridMovementSound cmd = new AlienGridMovementSound();
        assertEquals(0, cmd.getCurrentSoundIndex());
        assertEquals(1.0f, cmd.getAlienGridSpeed());
    }
    
    @Test
    void testSetAndGetAlienGridSpeed() {
        command.setAlienGridSpeed(0.5f);
        assertEquals(0.5f, command.getAlienGridSpeed());
        
        command.setAlienGridSpeed(2.0f);
        assertEquals(2.0f, command.getAlienGridSpeed());
    }
    
    @Test
    void testExecutePlaysFirstSound() {
        // Setup: alien grid exists with children
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        // Execute
        command.execute(0.0f);
        
        // Verify first sound was played
        verify(mockSoundManager).play(Sound.SoundName.FAST_INVADER_1);
    }
    
    @Test
    void testExecuteCyclesThroughSounds() {
        // Setup: alien grid exists with children
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        // Execute 4 times to cycle through all sounds
        command.execute(0.0f);
        verify(mockSoundManager).play(Sound.SoundName.FAST_INVADER_1);
        
        command.execute(0.0f);
        verify(mockSoundManager).play(Sound.SoundName.FAST_INVADER_2);
        
        command.execute(0.0f);
        verify(mockSoundManager).play(Sound.SoundName.FAST_INVADER_3);
        
        command.execute(0.0f);
        verify(mockSoundManager).play(Sound.SoundName.FAST_INVADER_4);
        
        // Fifth execution should cycle back to first sound
        command.execute(0.0f);
        verify(mockSoundManager, times(2)).play(Sound.SoundName.FAST_INVADER_1);
    }
    
    @Test
    void testExecuteReschedulesWhenAliensExist() {
        // Setup: alien grid exists with children
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        command.setAlienGridSpeed(0.75f);
        
        // Execute
        command.execute(0.0f);
        
        // Verify command was rescheduled
        ArgumentCaptor<TimeEvent.Name> nameCaptor = ArgumentCaptor.forClass(TimeEvent.Name.class);
        ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);
        ArgumentCaptor<Float> deltaCaptor = ArgumentCaptor.forClass(Float.class);
        
        verify(mockTimerManager).add(nameCaptor.capture(), commandCaptor.capture(), deltaCaptor.capture());
        
        assertEquals(TimeEvent.Name.ALIEN_GRID_MOVEMENT_SOUND, nameCaptor.getValue());
        assertSame(command, commandCaptor.getValue());
        assertEquals(0.75f, deltaCaptor.getValue());
    }
    
    @Test
    void testExecuteDoesNotRescheduleWhenNoAlienGrid() {
        // Setup: no alien grid
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(null);
        
        // Execute
        command.execute(0.0f);
        
        // Verify command was NOT rescheduled
        verify(mockTimerManager, never()).add(any(), any(), anyFloat());
    }
    
    @Test
    void testExecuteDoesNotRescheduleWhenAlienGridEmpty() {
        // Setup: alien grid exists but has no children
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(null);
        
        // Execute
        command.execute(0.0f);
        
        // Verify command was NOT rescheduled
        verify(mockTimerManager, never()).add(any(), any(), anyFloat());
    }
    
    @Test
    void testExecuteHandlesNullSoundManager() {
        // Setup: null sound manager
        injectMock(command, "soundManager", null);
        
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        // Execute should not throw exception
        assertDoesNotThrow(() -> command.execute(0.0f));
    }
    
    @Test
    void testExecuteHandlesNullTimerManager() {
        // Setup: null timer manager
        injectMock(command, "timerManager", null);
        
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        // Execute should not throw exception
        assertDoesNotThrow(() -> command.execute(0.0f));
    }
    
    @Test
    void testExecuteHandlesNullGameObjectManager() {
        // Setup: null game object manager
        injectMock(command, "gameObjectManager", null);
        
        // Execute should not throw exception and should not reschedule
        assertDoesNotThrow(() -> command.execute(0.0f));
        verify(mockTimerManager, never()).add(any(), any(), anyFloat());
    }
    
    @Test
    void testReset() {
        // Set to non-zero index
        command.setCurrentSoundIndex(2);
        assertEquals(2, command.getCurrentSoundIndex());
        
        // Reset
        command.reset();
        
        // Verify index is back to 0
        assertEquals(0, command.getCurrentSoundIndex());
    }
    
    @Test
    void testSetCurrentSoundIndex() {
        command.setCurrentSoundIndex(0);
        assertEquals(0, command.getCurrentSoundIndex());
        
        command.setCurrentSoundIndex(1);
        assertEquals(1, command.getCurrentSoundIndex());
        
        command.setCurrentSoundIndex(2);
        assertEquals(2, command.getCurrentSoundIndex());
        
        command.setCurrentSoundIndex(3);
        assertEquals(3, command.getCurrentSoundIndex());
    }
    
    @Test
    void testSetCurrentSoundIndexIgnoresInvalidValues() {
        command.setCurrentSoundIndex(2);
        assertEquals(2, command.getCurrentSoundIndex());
        
        // Try to set invalid values
        command.setCurrentSoundIndex(-1);
        assertEquals(2, command.getCurrentSoundIndex()); // Should not change
        
        command.setCurrentSoundIndex(4);
        assertEquals(2, command.getCurrentSoundIndex()); // Should not change
        
        command.setCurrentSoundIndex(100);
        assertEquals(2, command.getCurrentSoundIndex()); // Should not change
    }
    
    @Test
    void testWash() {
        // Set non-default values
        command.setCurrentSoundIndex(3);
        command.setAlienGridSpeed(2.5f);
        
        // Wash
        command.wash();
        
        // Verify reset to defaults
        assertEquals(0, command.getCurrentSoundIndex());
        assertEquals(1.0f, command.getAlienGridSpeed());
    }
    
    @Test
    void testDump() {
        command.setCurrentSoundIndex(2);
        command.setAlienGridSpeed(1.5f);
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.dump());
    }
    
    @Test
    void testSoundIndexWrapsAround() {
        // Setup: alien grid exists with children
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        // Start at index 3 (last sound)
        command.setCurrentSoundIndex(3);
        
        // Execute should play sound 4 and wrap to index 0
        command.execute(0.0f);
        verify(mockSoundManager).play(Sound.SoundName.FAST_INVADER_4);
        assertEquals(0, command.getCurrentSoundIndex());
    }
    
    @Test
    void testMultipleExecutionsWithDifferentSpeeds() {
        // Setup: alien grid exists with children
        AlienGrid mockAlienGrid = mock(AlienGrid.class);
        GameObject mockChild = mock(GameObject.class);
        when(mockGameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID)).thenReturn(mockAlienGrid);
        when(mockAlienGrid.getFirstChild()).thenReturn(mockChild);
        
        // Execute with speed 1.0
        command.setAlienGridSpeed(1.0f);
        command.execute(0.0f);
        
        // Execute with speed 0.5 (faster)
        command.setAlienGridSpeed(0.5f);
        command.execute(0.0f);
        
        // Execute with speed 2.0 (slower)
        command.setAlienGridSpeed(2.0f);
        command.execute(0.0f);
        
        // Verify all three reschedules used correct speeds
        ArgumentCaptor<Float> deltaCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockTimerManager, times(3)).add(any(), any(), deltaCaptor.capture());
        
        assertEquals(1.0f, deltaCaptor.getAllValues().get(0));
        assertEquals(0.5f, deltaCaptor.getAllValues().get(1));
        assertEquals(2.0f, deltaCaptor.getAllValues().get(2));
    }
}