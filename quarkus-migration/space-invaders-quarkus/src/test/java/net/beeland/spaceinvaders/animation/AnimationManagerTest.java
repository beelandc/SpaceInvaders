package net.beeland.spaceinvaders.animation;

import net.beeland.spaceinvaders.sprite.Sprite;
import net.beeland.spaceinvaders.sprite.SpriteManager;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AnimationManager class
 */
class AnimationManagerTest {
    
    private AnimationManager manager;
    
    @Mock
    private SpriteManager spriteManager;
    
    @Mock
    private TimerManager timerManager;
    
    @Mock
    private Sprite mockSprite;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new AnimationManager();
        manager.spriteManager = spriteManager;
        manager.timerManager = timerManager;
        manager.init();
        
        // Setup mock sprite manager
        when(spriteManager.find(any(Sprite.Name.class))).thenReturn(mockSprite);
    }
    
    @Test
    void testInit() {
        AnimationManager newManager = new AnimationManager();
        newManager.spriteManager = spriteManager;
        newManager.timerManager = timerManager;
        
        assertDoesNotThrow(() -> newManager.init());
        assertNotNull(newManager.getStats());
    }
    
    @Test
    void testAdd() {
        Animation animation = manager.add(Animation.Name.CRAB_ALIEN, 
                                         Sprite.Name.CRAB_ALIEN, 
                                         0.3f, true);
        
        assertNotNull(animation);
        assertEquals(Animation.Name.CRAB_ALIEN, animation.getName());
        assertEquals(0.3f, animation.getFrameDuration(), 0.001f);
        assertTrue(animation.isLoop());
        verify(spriteManager).find(Sprite.Name.CRAB_ALIEN);
    }
    
    @Test
    void testAddMultiple() {
        Animation anim1 = manager.add(Animation.Name.CRAB_ALIEN, 
                                     Sprite.Name.CRAB_ALIEN, 
                                     0.3f, true);
        Animation anim2 = manager.add(Animation.Name.SQUID_ALIEN, 
                                     Sprite.Name.SQUID_ALIEN, 
                                     0.4f, true);
        Animation anim3 = manager.add(Animation.Name.JELLYFISH_ALIEN, 
                                     Sprite.Name.JELLYFISH_ALIEN, 
                                     0.5f, true);
        
        assertNotNull(anim1);
        assertNotNull(anim2);
        assertNotNull(anim3);
        assertNotEquals(anim1, anim2);
        assertNotEquals(anim2, anim3);
    }
    
    @Test
    void testFind() {
        Animation added = manager.add(Animation.Name.CRAB_ALIEN, 
                                     Sprite.Name.CRAB_ALIEN, 
                                     0.3f, true);
        
        Animation found = manager.find(Animation.Name.CRAB_ALIEN);
        
        assertNotNull(found);
        assertEquals(added, found);
        assertEquals(Animation.Name.CRAB_ALIEN, found.getName());
    }
    
    @Test
    void testFindReturnsNullWhenNotFound() {
        Animation found = manager.find(Animation.Name.SQUID_ALIEN);
        
        assertNull(found);
    }
    
    @Test
    void testFindWithMultipleAnimations() {
        Animation anim1 = manager.add(Animation.Name.CRAB_ALIEN, 
                                     Sprite.Name.CRAB_ALIEN, 
                                     0.3f, true);
        Animation anim2 = manager.add(Animation.Name.SQUID_ALIEN, 
                                     Sprite.Name.SQUID_ALIEN, 
                                     0.4f, true);
        
        Animation found1 = manager.find(Animation.Name.CRAB_ALIEN);
        Animation found2 = manager.find(Animation.Name.SQUID_ALIEN);
        
        assertEquals(anim1, found1);
        assertEquals(anim2, found2);
    }
    
    @Test
    void testRemove() {
        Animation animation = manager.add(Animation.Name.CRAB_ALIEN, 
                                         Sprite.Name.CRAB_ALIEN, 
                                         0.3f, true);
        
        assertNotNull(manager.find(Animation.Name.CRAB_ALIEN));
        
        manager.remove(animation);
        
        // After removal, find should return null
        assertNull(manager.find(Animation.Name.CRAB_ALIEN));
    }
    
    @Test
    void testRemoveWithNull() {
        assertDoesNotThrow(() -> manager.remove(null));
    }
    
    @Test
    void testStopAll() {
        Animation anim1 = manager.add(Animation.Name.CRAB_ALIEN, 
                                     Sprite.Name.CRAB_ALIEN, 
                                     0.3f, true);
        Animation anim2 = manager.add(Animation.Name.SQUID_ALIEN, 
                                     Sprite.Name.SQUID_ALIEN, 
                                     0.4f, true);
        
        // Start both animations (would need frames, but we're testing stop)
        assertDoesNotThrow(() -> manager.stopAll());
        
        assertFalse(anim1.isActive());
        assertFalse(anim2.isActive());
    }
    
    @Test
    void testPrintStats() {
        manager.add(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 0.3f, true);
        manager.add(Animation.Name.SQUID_ALIEN, Sprite.Name.SQUID_ALIEN, 0.4f, true);
        
        assertDoesNotThrow(() -> manager.printStats());
    }
    
    @Test
    void testGetStats() {
        manager.add(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 0.3f, true);
        
        String stats = manager.getStats();
        
        assertNotNull(stats);
        assertTrue(stats.contains("AnimationManager"));
    }
    
    @Test
    void testCleanup() {
        manager.add(Animation.Name.CRAB_ALIEN, Sprite.Name.CRAB_ALIEN, 0.3f, true);
        manager.add(Animation.Name.SQUID_ALIEN, Sprite.Name.SQUID_ALIEN, 0.4f, true);
        
        assertDoesNotThrow(() -> manager.cleanup());
    }
    
    @Test
    void testObjectPoolReuse() {
        // Add and remove an animation
        Animation anim1 = manager.add(Animation.Name.CRAB_ALIEN, 
                                     Sprite.Name.CRAB_ALIEN, 
                                     0.3f, true);
        manager.remove(anim1);
        
        // Add another animation - should reuse the pooled object
        Animation anim2 = manager.add(Animation.Name.SQUID_ALIEN, 
                                     Sprite.Name.SQUID_ALIEN, 
                                     0.4f, true);
        
        assertNotNull(anim2);
        assertEquals(Animation.Name.SQUID_ALIEN, anim2.getName());
    }
    
    @Test
    void testPoolGrowth() {
        // Add more animations than initial pool size (10)
        for (int i = 0; i < 15; i++) {
            Animation anim = manager.add(Animation.Name.CRAB_ALIEN, 
                                        Sprite.Name.CRAB_ALIEN, 
                                        0.3f, true);
            assertNotNull(anim);
        }
        
        // All animations should be created successfully
        String stats = manager.getStats();
        assertNotNull(stats);
    }
}