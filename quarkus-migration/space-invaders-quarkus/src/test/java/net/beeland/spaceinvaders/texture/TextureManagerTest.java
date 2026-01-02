package net.beeland.spaceinvaders.texture;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TextureManager
 * Note: Some tests are disabled as they require OpenGL context
 */
@QuarkusTest
@DisplayName("TextureManager Tests")
class TextureManagerTest {
    
    @Inject
    TextureManager textureManager;
    
    @Test
    @DisplayName("Should inject TextureManager via CDI")
    void testCDIInjection() {
        assertNotNull(textureManager);
    }
    
    @Test
    @DisplayName("Should get statistics")
    void testGetStatistics() {
        String stats = textureManager.getStats();
        
        assertNotNull(stats);
        assertTrue(stats.contains("TextureManager"));
    }
    
    @Test
    @Disabled("Requires OpenGL context")
    @DisplayName("Should add texture")
    void testAddTexture() {
        // This test requires an OpenGL context which is not available in unit tests
        // Integration tests with GLFW window would be needed
    }
    
    @Test
    @Disabled("Requires OpenGL context")
    @DisplayName("Should find texture by name")
    void testFindTexture() {
        // This test requires an OpenGL context
    }
    
    @Test
    @Disabled("Requires OpenGL context")
    @DisplayName("Should remove texture")
    void testRemoveTexture() {
        // This test requires an OpenGL context
    }
    
    @Test
    @DisplayName("Should handle find on empty manager")
    void testFindOnEmpty() {
        Texture texture = textureManager.find(Texture.Name.ALIENS);
        
        // Should return null if not found
        assertNull(texture);
    }
}
