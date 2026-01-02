package net.beeland.spaceinvaders.font;

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
 * Unit tests for GlyphManager class
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@DisplayName("GlyphManager Tests")
class GlyphManagerTest {
    
    private GlyphManager glyphManager;
    
    @Mock
    private TextureManager mockTextureManager;
    
    @Mock
    private Texture mockTexture;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        glyphManager = new GlyphManager();
        glyphManager.textureManager = mockTextureManager;
        glyphManager.init();
    }
    
    @Test
    @DisplayName("Initialization creates reserve pool")
    void testInit() {
        assertNotNull(glyphManager);
        String stats = glyphManager.getStats();
        assertTrue(stats.contains("Reserved:"));
    }
    
    @Test
    @DisplayName("Add creates and configures glyph")
    void testAdd() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        Glyph glyph = glyphManager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                                       10.0f, 20.0f, 15.0f, 20.0f);
        
        assertNotNull(glyph);
        assertEquals(Glyph.Name.CONSOLAS_20PT, glyph.getName());
        assertEquals(65, glyph.getKey());
        assertEquals(10.0f, glyph.getX());
        assertEquals(20.0f, glyph.getY());
        assertEquals(15.0f, glyph.getWidth());
        assertEquals(20.0f, glyph.getHeight());
        
        verify(mockTextureManager).find(Texture.Name.ALIENS);
    }
    
    @Test
    @DisplayName("Find locates glyph by name and key")
    void testFind() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyphManager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                        10.0f, 20.0f, 15.0f, 20.0f);
        
        Glyph found = glyphManager.find(Glyph.Name.CONSOLAS_20PT, 65);
        
        assertNotNull(found);
        assertEquals(Glyph.Name.CONSOLAS_20PT, found.getName());
        assertEquals(65, found.getKey());
    }
    
    @Test
    @DisplayName("Find returns null for non-existent glyph")
    void testFindNotFound() {
        Glyph found = glyphManager.find(Glyph.Name.CONSOLAS_20PT, 99);
        assertNull(found);
    }
    
    @Test
    @DisplayName("Remove returns glyph to pool")
    void testRemove() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        Glyph glyph = glyphManager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                                       10.0f, 20.0f, 15.0f, 20.0f);
        
        String statsBefore = glyphManager.getStats();
        glyphManager.remove(glyph);
        String statsAfter = glyphManager.getStats();
        
        assertTrue(statsBefore.contains("Active: 1"));
        assertTrue(statsAfter.contains("Active: 0"));
    }
    
    @Test
    @DisplayName("Multiple glyphs can be added")
    void testMultipleGlyphs() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        Glyph glyph1 = glyphManager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                                        0.0f, 0.0f, 12.0f, 20.0f);
        Glyph glyph2 = glyphManager.add(Glyph.Name.CONSOLAS_20PT, 66, Texture.Name.ALIENS,
                                        12.0f, 0.0f, 12.0f, 20.0f);
        Glyph glyph3 = glyphManager.add(Glyph.Name.CONSOLAS_20PT, 67, Texture.Name.ALIENS,
                                        24.0f, 0.0f, 12.0f, 20.0f);
        
        assertNotNull(glyph1);
        assertNotNull(glyph2);
        assertNotNull(glyph3);
        String stats = glyphManager.getStats();
        assertTrue(stats.contains("Active: 3"));
    }
    
    @Test
    @DisplayName("Find distinguishes between different keys")
    void testFindDifferentKeys() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyphManager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                        0.0f, 0.0f, 12.0f, 20.0f);
        glyphManager.add(Glyph.Name.CONSOLAS_20PT, 66, Texture.Name.ALIENS,
                        12.0f, 0.0f, 12.0f, 20.0f);
        
        Glyph foundA = glyphManager.find(Glyph.Name.CONSOLAS_20PT, 65);
        Glyph foundB = glyphManager.find(Glyph.Name.CONSOLAS_20PT, 66);
        
        assertNotNull(foundA);
        assertNotNull(foundB);
        assertEquals(65, foundA.getKey());
        assertEquals(66, foundB.getKey());
        assertNotEquals(foundA, foundB);
    }
    
    @Test
    @DisplayName("Find distinguishes between different font names")
    void testFindDifferentFonts() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        glyphManager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                        0.0f, 0.0f, 12.0f, 20.0f);
        glyphManager.add(Glyph.Name.CONSOLAS_36PT, 65, Texture.Name.ALIENS,
                        0.0f, 0.0f, 20.0f, 36.0f);
        
        Glyph found20pt = glyphManager.find(Glyph.Name.CONSOLAS_20PT, 65);
        Glyph found36pt = glyphManager.find(Glyph.Name.CONSOLAS_36PT, 65);
        
        assertNotNull(found20pt);
        assertNotNull(found36pt);
        assertEquals(12.0f, found20pt.getWidth());
        assertEquals(20.0f, found36pt.getWidth());
    }
    
    @Test
    @DisplayName("Pool grows when reserve is exhausted")
    void testPoolGrowth() {
        when(mockTextureManager.find(any())).thenReturn(mockTexture);
        when(mockTexture.getTextureId()).thenReturn(1);
        when(mockTexture.getWidth()).thenReturn(512);
        when(mockTexture.getHeight()).thenReturn(512);
        
        // Add more glyphs than initial reserve (100)
        for (int i = 0; i < 110; i++) {
            glyphManager.add(Glyph.Name.CONSOLAS_20PT, i, Texture.Name.ALIENS,
                           0.0f, 0.0f, 12.0f, 20.0f);
        }
        
        String stats = glyphManager.getStats();
        assertTrue(stats.contains("Active: 110"));
    }
    
    @Test
    @DisplayName("PrintStats does not throw exception")
    void testPrintStats() {
        assertDoesNotThrow(() -> glyphManager.printStats());
    }
    
    @Test
    @DisplayName("Cleanup destroys manager")
    void testCleanup() {
        assertDoesNotThrow(() -> glyphManager.cleanup());
    }
    
    @Test
    @DisplayName("Remove handles null glyph gracefully")
    void testRemoveNull() {
        assertDoesNotThrow(() -> glyphManager.remove(null));
    }
    
    @Test
    @DisplayName("Add handles null texture manager")
    void testAddWithNullTextureManager() {
        GlyphManager manager = new GlyphManager();
        manager.textureManager = null;
        manager.init();
        
        Glyph glyph = manager.add(Glyph.Name.CONSOLAS_20PT, 65, Texture.Name.ALIENS,
                                  10.0f, 20.0f, 15.0f, 20.0f);
        
        assertNotNull(glyph);
        assertNull(glyph.getImage());
    }
}