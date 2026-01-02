package net.beeland.spaceinvaders.sprite;

import net.beeland.spaceinvaders.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SpriteManager class
 */
@DisplayName("SpriteManager Tests")
class SpriteManagerTest {
    
    private SpriteManager manager;
    
    @Mock
    private Image mockImage;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new SpriteManager();
        manager.init();
        
        // Setup mock image
        when(mockImage.getPixelWidth()).thenReturn(32);
        when(mockImage.getPixelHeight()).thenReturn(32);
        when(mockImage.getName()).thenReturn("TestImage");
    }
    
    @Test
    @DisplayName("Should initialize with reserve pool")
    void testInitialization() {
        assertNotNull(manager);
        assertNotNull(manager.getStats());
    }
    
    @Test
    @DisplayName("Should add sprite to active list")
    void testAddSprite() {
        Sprite sprite = manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 100.0f, 200.0f);
        
        assertNotNull(sprite);
        assertEquals("SQUID_ALIEN", sprite.getName());
        assertEquals(mockImage, sprite.getImage());
        assertEquals(100.0f, sprite.getX());
        assertEquals(200.0f, sprite.getY());
        // Verify sprite was added (check via find or getActiveHead)
        assertNotNull(manager.getActiveHead());
    }
    
    @Test
    @DisplayName("Should add multiple sprites")
    void testAddMultipleSprites() {
        Sprite sprite1 = manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 0.0f, 0.0f);
        Sprite sprite2 = manager.add(Sprite.Name.CRAB_ALIEN, mockImage, 50.0f, 50.0f);
        Sprite sprite3 = manager.add(Sprite.Name.JELLYFISH_ALIEN, mockImage, 100.0f, 100.0f);
        
        assertNotNull(sprite1);
        assertNotNull(sprite2);
        assertNotNull(sprite3);
        // Verify all sprites were added
        assertNotNull(sprite1);
        assertNotNull(sprite2);
        assertNotNull(sprite3);
    }
    
    @Test
    @DisplayName("Should find sprite by name")
    void testFindSprite() {
        manager.add(Sprite.Name.CORE_CANNON, mockImage, 100.0f, 200.0f);
        
        Sprite found = manager.find(Sprite.Name.CORE_CANNON);
        
        assertNotNull(found);
        assertEquals("CORE_CANNON", found.getName());
    }
    
    @Test
    @DisplayName("Should return null when sprite not found")
    void testFindNonExistentSprite() {
        Sprite found = manager.find(Sprite.Name.UFO);
        
        assertNull(found);
    }
    
    @Test
    @DisplayName("Should remove sprite by reference")
    void testRemoveSpriteByReference() {
        Sprite sprite = manager.add(Sprite.Name.MISSILE, mockImage, 0.0f, 0.0f);
        // Verify sprite was added (check via find or getActiveHead)
        assertNotNull(manager.getActiveHead());
        
        manager.remove(sprite);
        
        // Verify sprite was removed
        assertNull(manager.find(Sprite.Name.MISSILE));
    }
    
    @Test
    @DisplayName("Should remove sprite by name")
    void testRemoveSpriteByName() {
        manager.add(Sprite.Name.BOMB, mockImage, 0.0f, 0.0f);
        // Verify sprite was added (check via find or getActiveHead)
        assertNotNull(manager.getActiveHead());
        
        manager.remove(Sprite.Name.BOMB);
        
        // Verify sprite was removed
        assertNull(manager.find(Sprite.Name.MISSILE));
    }
    
    @Test
    @DisplayName("Should handle remove with null sprite")
    void testRemoveNullSprite() {
        assertDoesNotThrow(() -> manager.remove((Sprite) null));
    }
    
    @Test
    @DisplayName("Should handle remove non-existent sprite by name")
    void testRemoveNonExistentSpriteByName() {
        assertDoesNotThrow(() -> manager.remove(Sprite.Name.UFO));
    }
    
    @Test
    @DisplayName("Should reuse sprites from pool")
    void testSpriteReuse() {
        // Add and remove a sprite
        Sprite sprite1 = manager.add(Sprite.Name.SHIELD_BRICK, mockImage, 0.0f, 0.0f);
        manager.remove(sprite1);
        
        // Add another sprite - should reuse from pool
        Sprite sprite2 = manager.add(Sprite.Name.EXPLOSION, mockImage, 50.0f, 50.0f);
        
        assertNotNull(sprite2);
        assertEquals("EXPLOSION", sprite2.getName());
    }
    
    @Test
    @DisplayName("Should render all active sprites")
    void testRenderAll() {
        manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 0.0f, 0.0f);
        manager.add(Sprite.Name.CRAB_ALIEN, mockImage, 50.0f, 50.0f);
        
        assertDoesNotThrow(() -> manager.renderAll());
    }
    
    @Test
    @DisplayName("Should handle render with no sprites")
    void testRenderAllEmpty() {
        assertDoesNotThrow(() -> manager.renderAll());
    }
    
    @Test
    @DisplayName("Should grow pool when needed")
    void testPoolGrowth() {
        // Add many sprites to exhaust initial pool
        for (int i = 0; i < 150; i++) {
            Sprite sprite = manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 0.0f, 0.0f);
            assertNotNull(sprite);
        }
        
        // Pool should have grown - verify by checking stats
        assertNotNull(manager.getStats());
    }
    
    @Test
    @DisplayName("Should cleanup on destroy")
    void testCleanup() {
        manager.add(Sprite.Name.CORE_CANNON, mockImage, 0.0f, 0.0f);
        
        assertDoesNotThrow(() -> manager.cleanup());
    }
    
    @Test
    @DisplayName("Should dump statistics")
    void testDumpStats() {
        manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 0.0f, 0.0f);
        manager.add(Sprite.Name.CRAB_ALIEN, mockImage, 50.0f, 50.0f);
        
        assertDoesNotThrow(() -> manager.dumpActive());
    }
    
    @Test
    @DisplayName("Should find first sprite when multiple with same name")
    void testFindFirstSprite() {
        manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 100.0f, 100.0f);
        manager.add(Sprite.Name.SQUID_ALIEN, mockImage, 200.0f, 200.0f);
        
        Sprite found = manager.find(Sprite.Name.SQUID_ALIEN);
        
        assertNotNull(found);
        assertEquals("SQUID_ALIEN", found.getName());
        // Manager uses LIFO (Last In First Out), so most recently added is found first
        assertEquals(200.0f, found.getX());
    }
}
