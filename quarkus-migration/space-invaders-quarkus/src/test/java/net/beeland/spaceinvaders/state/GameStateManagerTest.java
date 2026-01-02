package net.beeland.spaceinvaders.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedStatic;
import org.lwjgl.opengl.GL11;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GameStateManager.
 * Tests state management, transitions, and data transfer between states.
 */
class GameStateManagerTest {
    
    private GameStateManager manager;
    private MockedStatic<GL11> gl11Mock;
    
    @BeforeEach
    void setUp() {
        // Mock all GL11 static methods to prevent LWJGL native library loading
        gl11Mock = mockStatic(GL11.class);
        manager = new GameStateManager();
    }
    
    @AfterEach
    void tearDown() {
        if (gl11Mock != null) {
            gl11Mock.close();
        }
    }
    
    @Test
    @DisplayName("GameStateManager should initialize with no current state")
    void testInitialState() {
        assertNull(manager.getCurrentState());
        assertNull(manager.getCurrentStateName());
        assertEquals(0, manager.getHighScore());
    }
    
    @Test
    @DisplayName("GameStateManager should initialize to Attract state")
    void testInitialize() {
        manager.initialize();
        
        assertNotNull(manager.getCurrentState());
        assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
    }
    
    @Test
    @DisplayName("GameStateManager should change to Play state")
    void testChangeToPlayState() {
        manager.initialize();
        
        manager.changeState(GameStateName.PLAY);
        
        assertEquals(GameStateName.PLAY, manager.getCurrentStateName());
        assertInstanceOf(PlayState.class, manager.getCurrentState());
    }
    
    @Test
    @DisplayName("GameStateManager should change to GameOver state")
    void testChangeToGameOverState() {
        manager.initialize();
        
        manager.changeState(GameStateName.GAME_OVER);
        
        assertEquals(GameStateName.GAME_OVER, manager.getCurrentStateName());
        assertInstanceOf(GameOverState.class, manager.getCurrentState());
    }
    
    @Test
    @DisplayName("GameStateManager should handle null state change gracefully")
    void testChangeToNullState() {
        manager.initialize();
        GameStateName initialState = manager.getCurrentStateName();
        
        manager.changeState(null);
        
        // Should remain in current state
        assertEquals(initialState, manager.getCurrentStateName());
    }
    
    @Test
    @DisplayName("GameStateManager should exit old state when changing")
    void testStateExitOnChange() {
        manager.initialize();
        GameState attractState = manager.getCurrentState();
        attractState.enter();
        
        assertNotNull(attractState.getGameObjectManager());
        assertNotNull(attractState.getTimerManager());
        
        manager.changeState(GameStateName.PLAY);
        
        // Old state should be cleaned up
        assertNull(attractState.getGameObjectManager());
        assertNull(attractState.getTimerManager());
    }
    
    @Test
    @DisplayName("GameStateManager should enter new state when changing")
    void testStateEnterOnChange() {
        manager.initialize();
        
        manager.changeState(GameStateName.PLAY);
        
        GameState playState = manager.getCurrentState();
        assertNotNull(playState.getGameObjectManager());
        assertNotNull(playState.getTimerManager());
    }
    
    @Test
    @DisplayName("GameStateManager should transfer score from Play to GameOver")
    void testScoreTransferToGameOver() {
        manager.initialize();
        manager.changeState(GameStateName.PLAY);
        
        PlayState playState = (PlayState) manager.getCurrentState();
        playState.addScore(1000);
        
        manager.changeState(GameStateName.GAME_OVER);
        
        GameOverState gameOverState = (GameOverState) manager.getCurrentState();
        assertEquals(1000, gameOverState.getFinalScore());
    }
    
    @Test
    @DisplayName("GameStateManager should update high score when exceeded")
    void testHighScoreUpdate() {
        manager.initialize();
        manager.setHighScore(500);
        
        manager.changeState(GameStateName.PLAY);
        PlayState playState = (PlayState) manager.getCurrentState();
        playState.addScore(1000);
        
        manager.changeState(GameStateName.GAME_OVER);
        
        assertEquals(1000, manager.getHighScore());
    }
    
    @Test
    @DisplayName("GameStateManager should not update high score when not exceeded")
    void testHighScoreNotUpdated() {
        manager.initialize();
        manager.setHighScore(2000);
        
        manager.changeState(GameStateName.PLAY);
        PlayState playState = (PlayState) manager.getCurrentState();
        playState.addScore(1000);
        
        manager.changeState(GameStateName.GAME_OVER);
        
        assertEquals(2000, manager.getHighScore());
    }
    
    @Test
    @DisplayName("GameStateManager should handle update with no state")
    void testUpdateWithNoState() {
        // Should not crash
        assertDoesNotThrow(() -> manager.update(0.016f));
    }
    
    @Test
    @DisplayName("GameStateManager should update current state")
    void testUpdateCurrentState() {
        manager.initialize();
        
        // Should not crash
        assertDoesNotThrow(() -> manager.update(0.016f));
    }
    
    // NOTE: draw() tests removed because OpenGL native methods cannot be mocked
    // and require an actual graphics context. Drawing functionality will be
    // tested through integration tests when running the actual game.
    
    @Test
    @DisplayName("GameStateManager should handle input with no state")
    void testHandleInputWithNoState() {
        // Should not crash
        assertDoesNotThrow(() -> manager.handleInput());
    }
    
    @Test
    @DisplayName("GameStateManager should handle input for current state")
    void testHandleInputCurrentState() {
        manager.initialize();
        
        // Should not crash
        assertDoesNotThrow(() -> manager.handleInput());
    }
    
    @Test
    @DisplayName("GameStateManager should process automatic state transitions")
    void testAutomaticStateTransition() {
        manager.initialize();
        manager.changeState(GameStateName.PLAY);
        
        PlayState playState = (PlayState) manager.getCurrentState();
        playState.setPlayerLives(0); // Triggers transition to GAME_OVER
        
        manager.update(0.016f);
        
        assertEquals(GameStateName.GAME_OVER, manager.getCurrentStateName());
    }
    
    @Test
    @DisplayName("GameStateManager should reset to Attract state")
    void testReset() {
        manager.initialize();
        manager.changeState(GameStateName.PLAY);
        
        assertEquals(GameStateName.PLAY, manager.getCurrentStateName());
        
        manager.reset();
        
        assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
    }
    
    @Test
    @DisplayName("GameStateManager should clean up on destroy")
    void testDestroy() {
        manager.initialize();
        GameState state = manager.getCurrentState();
        state.enter();
        
        assertNotNull(manager.getCurrentState());
        assertNotNull(state.getGameObjectManager());
        
        manager.destroy();
        
        assertNull(manager.getCurrentState());
        assertNull(state.getGameObjectManager());
    }
    
    @Test
    @DisplayName("GameStateManager should allow setting high score")
    void testSetHighScore() {
        assertEquals(0, manager.getHighScore());
        
        manager.setHighScore(1000);
        assertEquals(1000, manager.getHighScore());
        
        manager.setHighScore(2500);
        assertEquals(2500, manager.getHighScore());
    }
    
    @Test
    @DisplayName("GameStateManager should maintain high score across state changes")
    void testHighScorePersistence() {
        manager.initialize();
        manager.setHighScore(1500);
        
        manager.changeState(GameStateName.PLAY);
        assertEquals(1500, manager.getHighScore());
        
        manager.changeState(GameStateName.GAME_OVER);
        assertEquals(1500, manager.getHighScore());
        
        manager.changeState(GameStateName.ATTRACT);
        assertEquals(1500, manager.getHighScore());
    }
    
    @Test
    @DisplayName("GameStateManager should cycle through all states")
    void testStateCycle() {
        manager.initialize();
        assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
        
        manager.changeState(GameStateName.PLAY);
        assertEquals(GameStateName.PLAY, manager.getCurrentStateName());
        
        manager.changeState(GameStateName.GAME_OVER);
        assertEquals(GameStateName.GAME_OVER, manager.getCurrentStateName());
        
        manager.changeState(GameStateName.ATTRACT);
        assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
    }
    
    @Test
    @DisplayName("GameStateManager should handle rapid state changes")
    void testRapidStateChanges() {
        manager.initialize();
        
        for (int i = 0; i < 10; i++) {
            manager.changeState(GameStateName.PLAY);
            assertEquals(GameStateName.PLAY, manager.getCurrentStateName());
            
            manager.changeState(GameStateName.GAME_OVER);
            assertEquals(GameStateName.GAME_OVER, manager.getCurrentStateName());
            
            manager.changeState(GameStateName.ATTRACT);
            assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
        }
    }
    
    @Test
    @DisplayName("GameStateManager should handle same state transition")
    void testSameStateTransition() {
        manager.initialize();
        assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
        
        GameState firstState = manager.getCurrentState();
        
        manager.changeState(GameStateName.ATTRACT);
        
        // Should still be in attract state
        assertEquals(GameStateName.ATTRACT, manager.getCurrentStateName());
        // Should be the same state instance
        assertSame(firstState, manager.getCurrentState());
    }
}