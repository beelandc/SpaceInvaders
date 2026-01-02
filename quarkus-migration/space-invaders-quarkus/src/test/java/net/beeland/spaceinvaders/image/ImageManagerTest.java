package net.beeland.spaceinvaders.image;

import net.beeland.spaceinvaders.texture.Texture;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ImageManager
 */
@QuarkusTest
@DisplayName("ImageManager Tests")
class ImageManagerTest {
    
    @Inject
    ImageManager imageManager;
    
    @Mock
    private Texture mockTexture;
    
    @Test
    @DisplayName("Should inject ImageManager via CDI")
    void testCDIInjection() {
        assertNotNull(imageManager);
    }
    
    @Test
    @DisplayName("Should get statistics")
    void testGetStatistics() {
        String stats = imageManager.getStats();
        
        assertNotNull(stats);
        assertTrue(stats.contains("ImageManager"));
    }
    
    @Test
    @DisplayName("Should add image with pixel coordinates")
    void testAddImageWithPixelCoordinates() {
        MockitoAnnotations.openMocks(this);
        when(mockTexture.getWidth()).thenReturn(256);
        when(mockTexture.getHeight()).thenReturn(256);
        when(mockTexture.getName()).thenReturn("TestTexture");
        
        Image image = imageManager.add(Image.Name.SQUID_ALIEN, mockTexture, 0, 0, 32, 32);
        
        assertNotNull(image);
        assertEquals("SQUID_ALIEN", image.getName());
        assertEquals(mockTexture, image.getTexture());
    }
    
    @Test
    @DisplayName("Should add image with full texture")
    void testAddImageWithFullTexture() {
        MockitoAnnotations.openMocks(this);
        when(mockTexture.getWidth()).thenReturn(256);
        when(mockTexture.getHeight()).thenReturn(256);
        
        Image image = imageManager.add(Image.Name.CORE_CANNON, mockTexture);
        
        assertNotNull(image);
        assertEquals("CORE_CANNON", image.getName());
    }
    
    @Test
    @DisplayName("Should find image by name")
    void testFindImage() {
        MockitoAnnotations.openMocks(this);
        when(mockTexture.getWidth()).thenReturn(256);
        when(mockTexture.getHeight()).thenReturn(256);
        
        imageManager.add(Image.Name.CRAB_ALIEN, mockTexture, 0, 0, 32, 32);
        
        Image found = imageManager.find(Image.Name.CRAB_ALIEN);
        
        assertNotNull(found);
        assertEquals("CRAB_ALIEN", found.getName());
    }
    
    @Test
    @DisplayName("Should return null when image not found")
    void testFindNonExistentImage() {
        Image found = imageManager.find(Image.Name.UFO);
        
        assertNull(found);
    }
    
    @Test
    @DisplayName("Should remove image")
    void testRemoveImage() {
        MockitoAnnotations.openMocks(this);
        when(mockTexture.getWidth()).thenReturn(256);
        when(mockTexture.getHeight()).thenReturn(256);
        
        Image image = imageManager.add(Image.Name.MISSILE, mockTexture, 0, 0, 16, 16);
        
        imageManager.remove(image);
        
        Image found = imageManager.find(Image.Name.MISSILE);
        assertNull(found);
    }
    
    @Test
    @DisplayName("Should remove image by name")
    void testRemoveImageByName() {
        MockitoAnnotations.openMocks(this);
        when(mockTexture.getWidth()).thenReturn(256);
        when(mockTexture.getHeight()).thenReturn(256);
        
        imageManager.add(Image.Name.BOMB, mockTexture, 0, 0, 16, 16);
        
        imageManager.remove(Image.Name.BOMB);
        
        Image found = imageManager.find(Image.Name.BOMB);
        assertNull(found);
    }
    
    @Test
    @DisplayName("Should handle remove null image gracefully")
    void testRemoveNullImage() {
        assertDoesNotThrow(() -> imageManager.remove((Image) null));
    }
    
    @Test
    @DisplayName("Should handle remove non-existent image by name")
    void testRemoveNonExistentImageByName() {
        assertDoesNotThrow(() -> imageManager.remove(Image.Name.EXPLOSION));
    }
}
