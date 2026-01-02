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
 * Unit tests for SpriteProxy class
 */
@DisplayName("SpriteProxy Tests")
class SpriteProxyTest {
    
    private SpriteProxy proxy;
    
    @Mock
    private Sprite mockSprite;
    
    @Mock
    private Image mockImage;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proxy = new SpriteProxy();
        
        // Setup mock sprite
        when(mockSprite.getX()).thenReturn(100.0f);
        when(mockSprite.getY()).thenReturn(200.0f);
        when(mockSprite.getScaleX()).thenReturn(1.0f);
        when(mockSprite.getScaleY()).thenReturn(1.0f);
        when(mockSprite.getAngle()).thenReturn(0.0f);
        when(mockSprite.getRed()).thenReturn(1.0f);
        when(mockSprite.getGreen()).thenReturn(1.0f);
        when(mockSprite.getBlue()).thenReturn(1.0f);
        when(mockSprite.getAlpha()).thenReturn(1.0f);
        when(mockSprite.getImage()).thenReturn(mockImage);
        when(mockSprite.getName()).thenReturn("SQUID_ALIEN");
        
        // Setup mock image
        when(mockImage.getPixelWidth()).thenReturn(32);
        when(mockImage.getPixelHeight()).thenReturn(32);
    }
    
    @Test
    @DisplayName("New proxy should be initialized with defaults")
    void testNewProxyDefaults() {
        assertNull(proxy.getRealSprite());
        assertEquals(0.0f, proxy.getX());
        assertEquals(0.0f, proxy.getY());
        assertEquals(1.0f, proxy.getScaleX());
        assertEquals(1.0f, proxy.getScaleY());
        assertEquals(0.0f, proxy.getAngle());
        assertEquals(1.0f, proxy.getRed());
        assertEquals(1.0f, proxy.getGreen());
        assertEquals(1.0f, proxy.getBlue());
        assertEquals(1.0f, proxy.getAlpha());
    }
    
    @Test
    @DisplayName("Should set real sprite and copy properties")
    void testSetRealSprite() {
        proxy.set(mockSprite);
        
        assertEquals(mockSprite, proxy.getRealSprite());
        assertEquals(100.0f, proxy.getX());
        assertEquals(200.0f, proxy.getY());
        assertEquals(1.0f, proxy.getScaleX());
        assertEquals(1.0f, proxy.getScaleY());
    }
    
    @Test
    @DisplayName("Should set position independently")
    void testSetPosition() {
        proxy.set(mockSprite);
        
        proxy.setPosition(300.0f, 400.0f);
        
        assertEquals(300.0f, proxy.getX());
        assertEquals(400.0f, proxy.getY());
        // Original sprite position should not change
        verify(mockSprite, never()).setPosition(anyFloat(), anyFloat());
    }
    
    @Test
    @DisplayName("Should set scale independently")
    void testSetScale() {
        proxy.set(mockSprite);
        
        proxy.setScale(2.0f, 3.0f);
        
        assertEquals(2.0f, proxy.getScaleX());
        assertEquals(3.0f, proxy.getScaleY());
    }
    
    @Test
    @DisplayName("Should set angle independently")
    void testSetAngle() {
        proxy.set(mockSprite);
        
        proxy.setAngle(1.57f);
        
        assertEquals(1.57f, proxy.getAngle(), 0.01f);
    }
    
    @Test
    @DisplayName("Should set color independently")
    void testSetColor() {
        proxy.set(mockSprite);
        
        proxy.setColor(0.5f, 0.6f, 0.7f, 0.8f);
        
        assertEquals(0.5f, proxy.getRed());
        assertEquals(0.6f, proxy.getGreen());
        assertEquals(0.7f, proxy.getBlue());
        assertEquals(0.8f, proxy.getAlpha());
    }
    
    @Test
    @DisplayName("Should get image from real sprite")
    void testGetImage() {
        proxy.set(mockSprite);
        
        assertEquals(mockImage, proxy.getImage());
    }
    
    @Test
    @DisplayName("Should calculate width with scale")
    void testGetWidth() {
        proxy.set(mockSprite);
        proxy.setScale(2.0f, 1.0f);
        
        assertEquals(64.0f, proxy.getWidth()); // 32 * 2
    }
    
    @Test
    @DisplayName("Should calculate height with scale")
    void testGetHeight() {
        proxy.set(mockSprite);
        proxy.setScale(1.0f, 3.0f);
        
        assertEquals(96.0f, proxy.getHeight()); // 32 * 3
    }
    
    @Test
    @DisplayName("Should return zero dimensions with null sprite")
    void testDimensionsWithNullSprite() {
        assertEquals(0.0f, proxy.getWidth());
        assertEquals(0.0f, proxy.getHeight());
    }
    
    @Test
    @DisplayName("Should return null image with null sprite")
    void testGetImageWithNullSprite() {
        assertNull(proxy.getImage());
    }
    
    @Test
    @DisplayName("Should push values to real sprite on render")
    void testRender() {
        proxy.set(mockSprite);
        proxy.setPosition(150.0f, 250.0f);
        proxy.setScale(2.0f, 2.0f);
        proxy.setAngle(1.0f);
        proxy.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        
        proxy.render();
        
        verify(mockSprite).setPosition(150.0f, 250.0f);
        verify(mockSprite).setScale(2.0f, 2.0f);
        verify(mockSprite).setAngle(1.0f);
        verify(mockSprite).setColor(0.5f, 0.5f, 0.5f, 0.5f);
        verify(mockSprite).render();
    }
    
    @Test
    @DisplayName("Should not crash on render with null sprite")
    void testRenderWithNullSprite() {
        assertDoesNotThrow(() -> proxy.render());
    }
    
    @Test
    @DisplayName("Should wash proxy correctly")
    void testWash() {
        proxy.set(mockSprite);
        proxy.setPosition(100.0f, 200.0f);
        proxy.setScale(2.0f, 3.0f);
        proxy.setAngle(1.5f);
        proxy.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        
        proxy.wash();
        
        assertNull(proxy.getRealSprite());
        assertEquals(0.0f, proxy.getX());
        assertEquals(0.0f, proxy.getY());
        assertEquals(1.0f, proxy.getScaleX());
        assertEquals(1.0f, proxy.getScaleY());
        assertEquals(0.0f, proxy.getAngle());
        assertEquals(1.0f, proxy.getRed());
        assertEquals(1.0f, proxy.getGreen());
        assertEquals(1.0f, proxy.getBlue());
        assertEquals(1.0f, proxy.getAlpha());
    }
    
    @Test
    @DisplayName("Should dump proxy information")
    void testDump() {
        proxy.set(mockSprite);
        
        assertDoesNotThrow(() -> proxy.dump());
    }
    
    @Test
    @DisplayName("Multiple proxies can share same sprite")
    void testMultipleProxiesShareSprite() {
        SpriteProxy proxy1 = new SpriteProxy();
        SpriteProxy proxy2 = new SpriteProxy();
        
        proxy1.set(mockSprite);
        proxy2.set(mockSprite);
        
        proxy1.setPosition(100.0f, 100.0f);
        proxy2.setPosition(200.0f, 200.0f);
        
        assertEquals(mockSprite, proxy1.getRealSprite());
        assertEquals(mockSprite, proxy2.getRealSprite());
        assertEquals(100.0f, proxy1.getX());
        assertEquals(200.0f, proxy2.getX());
    }
}
