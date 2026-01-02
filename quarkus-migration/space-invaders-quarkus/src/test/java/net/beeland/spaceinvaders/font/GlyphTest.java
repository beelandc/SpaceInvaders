package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.texture.Texture;
import net.beeland.spaceinvaders.texture.TextureManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Glyph class
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@DisplayName("Glyph Tests")
class GlyphTest {
    
    private Glyph glyph;
    
    @Mock
    private TextureManager mockTextureManager;
    
    @Mock
    private Texture mockTexture;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        glyph = new Glyph();
    }
    
    @Test
    @DisplayName("Constructor initializes with default values")
    void testConstructor() {
        assertEquals(Glyph.Name.UNINITIALIZED, glyph.getName());
        assertEquals(0, glyph.getKey());
        assertNull(glyph.getImage());
        assertEquals(0.0f, glyph.getX());
        assertEquals(0.0f, glyph.getY());
        assertEquals(0.0f, glyph.getWidth());
        assertEquals(0.0f, glyph.getHeight());
    }
    
    @Test
    @DisplayName("Set method configures glyph properties")
    void testSet() {
        // Setup mock
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        // Set glyph
        glyph.set(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                 10.0f, 20.0f, 15.0f, 20.0f, mockTextureManager);
        
        assertEquals(Glyph.Name.CONSOLAS_20PT, glyph.getName());
        assertEquals(65, glyph.getKey());
        assertEquals(10.0f, glyph.getX());
        assertEquals(20.0f, glyph.getY());
        assertEquals(15.0f, glyph.getWidth());
        assertEquals(20.0f, glyph.getHeight());
        assertNotNull(glyph.getImage());
        
        verify(mockTextureManager).find(Texture.Name.ALIENS);
    }
    
    @Test
    @DisplayName("Set method handles null texture manager")
    void testSetWithNullTextureManager() {
        glyph.set(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                 10.0f, 20.0f, 15.0f, 20.0f, null);
        
        assertEquals(Glyph.Name.CONSOLAS_20PT, glyph.getName());
        assertEquals(65, glyph.getKey());
        assertNull(glyph.getImage());
    }
    
    @Test
    @DisplayName("Set method handles texture not found")
    void testSetWithTextureNotFound() {
        when(mockTextureManager.find(any())).thenReturn(null);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                 10.0f, 20.0f, 15.0f, 20.0f, mockTextureManager);
        
        assertEquals(Glyph.Name.CONSOLAS_20PT, glyph.getName());
        assertNull(glyph.getImage());
    }
    
    @Test
    @DisplayName("Wash resets glyph to default state")
    void testWash() {
        // Setup mock
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        // Set glyph
        glyph.set(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                 10.0f, 20.0f, 15.0f, 20.0f, mockTextureManager);
        
        // Wash
        glyph.wash();
        
        assertEquals(Glyph.Name.UNINITIALIZED, glyph.getName());
        assertEquals(0, glyph.getKey());
        assertNull(glyph.getImage());
        assertEquals(0.0f, glyph.getX());
        assertEquals(0.0f, glyph.getY());
        assertEquals(1.0f, glyph.getWidth());
        assertEquals(1.0f, glyph.getHeight());
    }
    
    @Test
    @DisplayName("Dump prints glyph information")
    void testDump() {
        // Setup mock
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                 10.0f, 20.0f, 15.0f, 20.0f, mockTextureManager);
        
        // Should not throw exception
        assertDoesNotThrow(() -> glyph.dump());
    }
    
    @Test
    @DisplayName("SetName updates glyph name")
    void testSetName() {
        glyph.setName(Glyph.Name.CONSOLAS_36PT);
        assertEquals(Glyph.Name.CONSOLAS_36PT, glyph.getName());
    }
    
    @Test
    @DisplayName("SetKey updates glyph key")
    void testSetKey() {
        glyph.setKey(97);
        assertEquals(97, glyph.getKey());
    }
    
    @Test
    @DisplayName("Glyph can represent uppercase letter")
    void testUppercaseLetter() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, 'A', Texture.Name.ALIENS,
                 0.0f, 0.0f, 12.0f, 20.0f, mockTextureManager);
        
        assertEquals('A', glyph.getKey());
    }
    
    @Test
    @DisplayName("Glyph can represent lowercase letter")
    void testLowercaseLetter() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, 'a', Texture.Name.ALIENS,
                 0.0f, 0.0f, 12.0f, 20.0f, mockTextureManager);
        
        assertEquals('a', glyph.getKey());
    }
    
    @Test
    @DisplayName("Glyph can represent digit")
    void testDigit() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, '5', Texture.Name.ALIENS,
                 0.0f, 0.0f, 12.0f, 20.0f, mockTextureManager);
        
        assertEquals('5', glyph.getKey());
    }
    
    @Test
    @DisplayName("Glyph can represent special character")
    void testSpecialCharacter() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, '!', Texture.Name.ALIENS,
                 0.0f, 0.0f, 8.0f, 20.0f, mockTextureManager);
        
        assertEquals('!', glyph.getKey());
    }
    
    @Test
    @DisplayName("Glyph supports space character")
    void testSpaceCharacter() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyph.set(Glyph.Name.CONSOLAS_20PT, ' ', Texture.Name.ALIENS,
                 0.0f, 0.0f, 6.0f, 20.0f, mockTextureManager);
        
        assertEquals(' ', glyph.getKey());
    }
    
    @Test
    @DisplayName("Multiple glyphs can be created independently")
    void testMultipleGlyphs() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        Glyph glyph1 = new Glyph();
        Glyph glyph2 = new Glyph();
        
        glyph1.set(Glyph.Name.CONSOLAS_20PT, 'A', Texture.Name.ALIENS,
                  0.0f, 0.0f, 12.0f, 20.0f, mockTextureManager);
        glyph2.set(Glyph.Name.CONSOLAS_20PT, 'B', Texture.Name.ALIENS,
                  12.0f, 0.0f, 12.0f, 20.0f, mockTextureManager);
        
        assertEquals('A', glyph1.getKey());
        assertEquals('B', glyph2.getKey());
        assertEquals(0.0f, glyph1.getX());
        assertEquals(12.0f, glyph2.getX());
    }
}