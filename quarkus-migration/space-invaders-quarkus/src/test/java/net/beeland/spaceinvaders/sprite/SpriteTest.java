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
 * Unit tests for Sprite class
 */
@DisplayName("Sprite Tests")
class SpriteTest {
    
    private Sprite sprite;
    
    @Mock
    private Image mockImage;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sprite = new Sprite();
        
        // Setup mock image
        when(mockImage.getPixelWidth()).thenReturn(32);
        when(mockImage.getPixelHeight()).thenReturn(32);
        when(mockImage.getName()).thenReturn("TestImage");
    }
    
    @Test
    @DisplayName("New sprite should be uninitialized")
    void testNewSpriteIsUninitialized() {
        assertEquals("UNINITIALIZED", sprite.getName());
        assertNull(sprite.getImage());
        assertEquals(0.0f, sprite.getX());
        assertEquals(0.0f, sprite.getY());
        assertEquals(1.0f, sprite.getScaleX());
        assertEquals(1.0f, sprite.getScaleY());
    }
    
    @Test
    @DisplayName("Should set sprite properties")
    void testSetSpriteProperties() {
        sprite.set(Sprite.Name.SQUID_ALIEN, mockImage, 100.0f, 200.0f);
        
        assertEquals("SQUID_ALIEN", sprite.getName());
        assertEquals(mockImage, sprite.getImage());
        assertEquals(100.0f, sprite.getX());
        assertEquals(200.0f, sprite.getY());
        assertEquals(32.0f, sprite.getWidth());
        assertEquals(32.0f, sprite.getHeight());
    }
    
    @Test
    @DisplayName("Should set position")
    void testSetPosition() {
        sprite.set(Sprite.Name.CORE_CANNON, mockImage, 0.0f, 0.0f);
        
        sprite.setPosition(150.0f, 250.0f);
        
        assertEquals(150.0f, sprite.getX());
        assertEquals(250.0f, sprite.getY());
    }
    
    @Test
    @DisplayName("Should set scale")
    void testSetScale() {
        sprite.set(Sprite.Name.MISSILE, mockImage, 0.0f, 0.0f);
        
        sprite.setScale(2.0f, 3.0f);
        
        assertEquals(2.0f, sprite.getScaleX());
        assertEquals(3.0f, sprite.getScaleY());
    }
    
    @Test
    @DisplayName("Should calculate scaled width and height")
    void testScaledDimensions() {
        sprite.set(Sprite.Name.BOMB, mockImage, 0.0f, 0.0f);
        sprite.setScale(2.0f, 3.0f);
        
        assertEquals(64.0f, sprite.getWidth()); // 32 * 2
        assertEquals(96.0f, sprite.getHeight()); // 32 * 3
    }
    
    @Test
    @DisplayName("Should set rotation angle")
    void testSetAngle() {
        sprite.set(Sprite.Name.UFO, mockImage, 0.0f, 0.0f);
        
        sprite.setAngle(1.57f); // ~90 degrees
        
        assertEquals(1.57f, sprite.getAngle(), 0.01f);
    }
    
    @Test
    @DisplayName("Should set color tint")
    void testSetColor() {
        sprite.set(Sprite.Name.EXPLOSION, mockImage, 0.0f, 0.0f);
        
        sprite.setColor(1.0f, 0.5f, 0.0f, 0.8f);
        
        assertEquals(1.0f, sprite.getRed());
        assertEquals(0.5f, sprite.getGreen());
        assertEquals(0.0f, sprite.getBlue());
        assertEquals(0.8f, sprite.getAlpha());
    }
    
    @Test
    @DisplayName("Should have default white color")
    void testDefaultColor() {
        sprite.set(Sprite.Name.SHIELD_BRICK, mockImage, 0.0f, 0.0f);
        
        assertEquals(1.0f, sprite.getRed());
        assertEquals(1.0f, sprite.getGreen());
        assertEquals(1.0f, sprite.getBlue());
        assertEquals(1.0f, sprite.getAlpha());
    }
    
    @Test
    @DisplayName("Should wash sprite correctly")
    void testWash() {
        sprite.set(Sprite.Name.CRAB_ALIEN, mockImage, 100.0f, 200.0f);
        sprite.setScale(2.0f, 2.0f);
        sprite.setAngle(1.0f);
        sprite.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        
        sprite.wash();
        
        assertEquals("UNINITIALIZED", sprite.getName());
        assertNull(sprite.getImage());
        assertEquals(0.0f, sprite.getX());
        assertEquals(0.0f, sprite.getY());
        assertEquals(1.0f, sprite.getScaleX());
        assertEquals(1.0f, sprite.getScaleY());
        assertEquals(0.0f, sprite.getAngle());
        assertEquals(1.0f, sprite.getRed());
        assertEquals(1.0f, sprite.getGreen());
        assertEquals(1.0f, sprite.getBlue());
        assertEquals(1.0f, sprite.getAlpha());
    }
    
    @Test
    @DisplayName("Should handle null image")
    void testNullImage() {
        sprite.set(Sprite.Name.BACKGROUND, null, 0.0f, 0.0f);
        
        assertEquals("BACKGROUND", sprite.getName());
        assertNull(sprite.getImage());
        assertEquals(0.0f, sprite.getWidth());
        assertEquals(0.0f, sprite.getHeight());
    }
    
    @Test
    @DisplayName("Should dump sprite information")
    void testDump() {
        sprite.set(Sprite.Name.JELLYFISH_ALIEN, mockImage, 50.0f, 75.0f);
        
        assertDoesNotThrow(() -> sprite.dump());
    }
    
    @Test
    @DisplayName("Should render without errors")
    void testRender() {
        sprite.set(Sprite.Name.CORE_CANNON, mockImage, 0.0f, 0.0f);
        
        assertDoesNotThrow(() -> sprite.render());
    }
}
