package net.beeland.spaceinvaders.animation;

import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.sprite.Sprite;
import net.beeland.spaceinvaders.sprite.SpriteManager;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Animation class
 */
class AnimationTest {
    
    private Animation animation;
    
    @Mock
    private SpriteManager spriteManager;
    
    @Mock
    private TimerManager timerManager;
    
    @Mock
    private Sprite mockSprite;
    
    @Mock
    private Image mockImage1;
    
    @Mock
    private Image mockImage2;
    
    @Mock
    private Image mockImage3;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        animation = new Animation();
        
        // Setup mock sprite manager to return mock sprite
        when(spriteManager.find(any(Sprite.Name.class))).thenReturn(mockSprite);
    }
    
    @Test
    void testDefaultConstructor() {
        Animation anim = new Animation();
        
        assertEquals(Animation.Name.UNINITIALIZED, anim.getName());
        assertNull(anim.getSprite());
        assertEquals(0, anim.getFrameCount());
        assertEquals(0, anim.getCurrentFrameIndex());
        assertEquals(0.5f, anim.getFrameDuration(), 0.001f);
        assertTrue(anim.isLoop());
        assertFalse(anim.isActive());
    }
    
    @Test
    void testSet() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        
        assertEquals(Animation.Name.CRAB_ALIEN, animation.getName());
        assertEquals(mockSprite, animation.getSprite());
        assertEquals(0.3f, animation.getFrameDuration(), 0.001f);
        assertTrue(animation.isLoop());
        assertFalse(animation.isActive());
        
        verify(spriteManager).find(Sprite.Name.CRAB_ALIEN);
    }
    
    @Test
    void testSetWithNullSpriteManager() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, null, timerManager);
        
        assertEquals(Animation.Name.CRAB_ALIEN, animation.getName());
        assertNull(animation.getSprite());
    }
    
    @Test
    void testSetThrowsExceptionWhenSpriteNotFound() {
        when(spriteManager.find(any(Sprite.Name.class))).thenReturn(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                         0.3f, true, spriteManager, timerManager);
        });
    }
    
    @Test
    void testAddFrame() {
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        animation.addFrame(mockImage3);
        
        assertEquals(3, animation.getFrameCount());
    }
    
    @Test
    void testAddFrameThrowsExceptionForNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            animation.addFrame(null);
        });
    }
    
    @Test
    void testStart() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        
        animation.start();
        
        assertTrue(animation.isActive());
        assertEquals(0, animation.getCurrentFrameIndex());
        verify(mockSprite).swapImage(mockImage1);
        verify(timerManager).add(any(), eq(animation), eq(0.3f));
    }
    
    @Test
    void testStartThrowsExceptionWithNoFrames() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        
        assertThrows(IllegalStateException.class, () -> {
            animation.start();
        });
    }
    
    @Test
    void testStop() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.start();
        
        assertTrue(animation.isActive());
        
        animation.stop();
        
        assertFalse(animation.isActive());
    }
    
    @Test
    void testReset() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        animation.start();
        
        // Simulate advancing to next frame
        animation.execute(0.3f);
        assertEquals(1, animation.getCurrentFrameIndex());
        
        animation.reset();
        
        assertEquals(0, animation.getCurrentFrameIndex());
        verify(mockSprite, times(2)).swapImage(mockImage1); // Once on start, once on reset
    }
    
    @Test
    void testExecuteAdvancesFrame() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        animation.addFrame(mockImage3);
        animation.start();
        
        // First frame is set on start
        verify(mockSprite).swapImage(mockImage1);
        
        // Execute should advance to frame 1
        animation.execute(0.3f);
        assertEquals(1, animation.getCurrentFrameIndex());
        verify(mockSprite).swapImage(mockImage2);
        
        // Execute again should advance to frame 2
        animation.execute(0.3f);
        assertEquals(2, animation.getCurrentFrameIndex());
        verify(mockSprite).swapImage(mockImage3);
    }
    
    @Test
    void testExecuteLoopsBackToStart() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        animation.start();
        
        // Advance to last frame
        animation.execute(0.3f);
        assertEquals(1, animation.getCurrentFrameIndex());
        
        // Execute should loop back to frame 0
        animation.execute(0.3f);
        assertEquals(0, animation.getCurrentFrameIndex());
        verify(mockSprite, times(2)).swapImage(mockImage1); // Once on start, once on loop
        assertTrue(animation.isActive());
    }
    
    @Test
    void testExecuteStopsWhenNotLooping() {
        animation.set(Animation.Name.EXPLOSION, Sprite.Name.EXPLOSION, 
                     0.1f, false, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        animation.start();
        
        // Advance to last frame
        animation.execute(0.1f);
        assertEquals(1, animation.getCurrentFrameIndex());
        assertTrue(animation.isActive());
        
        // Execute should stop at last frame
        animation.execute(0.1f);
        assertEquals(1, animation.getCurrentFrameIndex());
        assertFalse(animation.isActive());
        
        // Timer should not be rescheduled
        verify(timerManager, times(2)).add(any(), eq(animation), anyFloat()); // Only start + first execute
    }
    
    @Test
    void testExecuteDoesNothingWhenInactive() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.addFrame(mockImage2);
        
        // Don't start the animation
        animation.execute(0.3f);
        
        // Should not advance frame
        assertEquals(0, animation.getCurrentFrameIndex());
        verify(mockSprite, never()).swapImage(any());
    }
    
    @Test
    void testDestroy() {
        animation.set(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 
                     0.3f, true, spriteManager, timerManager);
        animation.addFrame(mockImage1);
        animation.start();
        
        animation.destroy();
        
        assertNull(animation.getSprite());
        assertEquals(0, animation.getFrameCount());
        assertFalse(animation.isActive());
    }
    
    @Test
    void testSetFrameDuration() {
        animation.setFrameDuration(0.5f);
        assertEquals(0.5f, animation.getFrameDuration(), 0.001f);
    }
    
    @Test
    void testSetFrameDurationThrowsExceptionForNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            animation.setFrameDuration(-0.1f);
        });
    }
    
    @Test
    void testSetFrameDurationThrowsExceptionForZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            animation.setFrameDuration(0.0f);
        });
    }
    
    @Test
    void testSetLoop() {
        animation.setLoop(false);
        assertFalse(animation.isLoop());
        
        animation.setLoop(true);
        assertTrue(animation.isLoop());
    }
    
    @Test
    void testSetName() {
        animation.setName(Animation.Name.SQUID_ALIEN);
        assertEquals(Animation.Name.SQUID_ALIEN, animation.getName());
    }
}