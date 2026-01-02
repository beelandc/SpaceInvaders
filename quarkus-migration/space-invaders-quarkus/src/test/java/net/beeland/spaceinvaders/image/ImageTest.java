package net.beeland.spaceinvaders.image;

import net.beeland.spaceinvaders.texture.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Image class
 */
@DisplayName("Image Tests")
class ImageTest {
    
    private Image image;
    
    @Mock
    private Texture mockTexture;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        image = new Image();
        
        // Setup mock texture
        when(mockTexture.getWidth()).thenReturn(256);
        when(mockTexture.getHeight()).thenReturn(256);
        when(mockTexture.getName()).thenReturn("TestTexture");
    }
    
    @Test
    @DisplayName("New image should be uninitialized")
    void testNewImageIsUninitialized() {
        assertEquals("UNINITIALIZED", image.getName());
        assertNull(image.getTexture());
        assertEquals(0.0f, image.getX());
        assertEquals(0.0f, image.getY());
        assertEquals(0.0f, image.getWidth());
        assertEquals(0.0f, image.getHeight());
    }
    
    @Test
    @DisplayName("Should set image with pixel coordinates")
    void testSetWithPixelCoordinates() {
        image.set(Image.Name.SQUID_ALIEN, mockTexture, 0, 0, 32, 32);
        
        assertEquals("SQUID_ALIEN", image.getName());
        assertEquals(mockTexture, image.getTexture());
        assertEquals(0, image.getPixelX());
        assertEquals(0, image.getPixelY());
        assertEquals(32, image.getPixelWidth());
        assertEquals(32, image.getPixelHeight());
    }
    
    @Test
    @DisplayName("Should calculate normalized coordinates correctly")
    void testNormalizedCoordinates() {
        // Set image at (64, 64) with size 32x32 in a 256x256 texture
        image.set(Image.Name.CRAB_ALIEN, mockTexture, 64, 64, 32, 32);
        
        // Normalized coordinates should be:
        // x = 64/256 = 0.25
        // y = 1.0 - (64 + 32)/256 = 1.0 - 0.375 = 0.625 (Y-flipped to OpenGL space)
        // width = 32/256 = 0.125
        // height = 32/256 = 0.125
        
        assertEquals(0.25f, image.getX(), 0.001f);
        assertEquals(0.625f, image.getY(), 0.001f);
        assertEquals(0.125f, image.getWidth(), 0.001f);
        assertEquals(0.125f, image.getHeight(), 0.001f);
    }
    
    @Test
    @DisplayName("Should set image using full texture")
    void testSetWithFullTexture() {
        image.set(Image.Name.CORE_CANNON, mockTexture);
        
        assertEquals("CORE_CANNON", image.getName());
        assertEquals(mockTexture, image.getTexture());
        assertEquals(0, image.getPixelX());
        assertEquals(0, image.getPixelY());
        assertEquals(256, image.getPixelWidth());
        assertEquals(256, image.getPixelHeight());
        
        // Normalized coordinates: x=0, y=1.0-(0+256)/256=0.0, w=1.0, h=1.0
        assertEquals(0.0f, image.getX());
        assertEquals(0.0f, image.getY());  // Full texture starts at bottom in OpenGL
        assertEquals(1.0f, image.getWidth());
        assertEquals(1.0f, image.getHeight());
    }
    
    @Test
    @DisplayName("Should get texture coordinates array")
    void testGetTexCoords() {
        image.set(Image.Name.MISSILE, mockTexture, 64, 64, 32, 32);
        
        float[] texCoords = image.getTexCoords();
        
        assertNotNull(texCoords);
        assertEquals(4, texCoords.length);
        
        // [minX, minY, maxX, maxY]
        // Y is flipped: 1.0 - (64 + 32)/256 = 0.625
        assertEquals(0.25f, texCoords[0], 0.001f);   // minX
        assertEquals(0.625f, texCoords[1], 0.001f);  // minY (Y-flipped)
        assertEquals(0.375f, texCoords[2], 0.001f);  // maxX (0.25 + 0.125)
        assertEquals(0.75f, texCoords[3], 0.001f);   // maxY (0.625 + 0.125)
    }
    
    @Test
    @DisplayName("Should wash image correctly")
    void testWash() {
        image.set(Image.Name.BOMB, mockTexture, 0, 0, 16, 16);
        
        image.wash();
        
        assertEquals("UNINITIALIZED", image.getName());
        assertNull(image.getTexture());
        assertEquals(0.0f, image.getX());
        assertEquals(0.0f, image.getY());
        assertEquals(0.0f, image.getWidth());
        assertEquals(0.0f, image.getHeight());
        assertEquals(0, image.getPixelX());
        assertEquals(0, image.getPixelY());
        assertEquals(0, image.getPixelWidth());
        assertEquals(0, image.getPixelHeight());
    }
    
    @Test
    @DisplayName("Should handle null texture gracefully")
    void testSetWithNullTexture() {
        image.set(Image.Name.SHIELD_BRICK, null, 0, 0, 16, 16);
        
        assertEquals("SHIELD_BRICK", image.getName());
        assertNull(image.getTexture());
        
        // Coordinates should still be set
        assertEquals(0, image.getPixelX());
        assertEquals(0, image.getPixelY());
        assertEquals(16, image.getPixelWidth());
        assertEquals(16, image.getPixelHeight());
    }
    
    @Test
    @DisplayName("Should handle edge of texture coordinates")
    void testEdgeCoordinates() {
        // Image at bottom-right corner (in image space, top-left origin)
        image.set(Image.Name.UFO, mockTexture, 224, 224, 32, 32);
        
        assertEquals(224, image.getPixelX());
        assertEquals(224, image.getPixelY());
        assertEquals(32, image.getPixelWidth());
        assertEquals(32, image.getPixelHeight());
        
        // Normalized: x=224/256=0.875, y=1.0-(224+32)/256=0.0 (Y-flipped to OpenGL)
        assertEquals(0.875f, image.getX(), 0.001f);
        assertEquals(0.0f, image.getY(), 0.001f);  // Bottom of texture in OpenGL space
        assertEquals(0.125f, image.getWidth(), 0.001f);
        assertEquals(0.125f, image.getHeight(), 0.001f);
    }
    
    @Test
    @DisplayName("Should dump image information")
    void testDump() {
        image.set(Image.Name.EXPLOSION, mockTexture, 0, 0, 64, 64);
        
        // Should not throw exception
        assertDoesNotThrow(() -> image.dump());
    }
}
