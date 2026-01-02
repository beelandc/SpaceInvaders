package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.sprite.SpriteBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FontSprite class
 *
 * @author Cecil Beeland
 * @version 1.0
 * @since 2024-12-23
 */
@DisplayName("FontSprite Tests")
class FontSpriteTest {
    
    private FontSprite fontSprite;
    
    @Mock
    private GlyphManager mockGlyphManager;
    
    @Mock
    private Glyph mockGlyph;
    
    @Mock
    private SpriteBatch mockSpriteBatch;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fontSprite = new FontSprite();
    }
    
    @Test
    @DisplayName("Constructor initializes with default values")
    void testConstructor() {
        assertEquals(Font.Name.UNINITIALIZED, fontSprite.getName());
        assertEquals("", fontSprite.getMessage());
        assertEquals(Glyph.Name.UNINITIALIZED, fontSprite.getGlyphName());
        assertEquals(0.0f, fontSprite.getX());
        assertEquals(0.0f, fontSprite.getY());
        assertEquals(1.0f, fontSprite.getRed());
        assertEquals(1.0f, fontSprite.getGreen());
        assertEquals(1.0f, fontSprite.getBlue());
        assertEquals(1.0f, fontSprite.getAlpha());
    }
    
    @Test
    @DisplayName("Set method configures font sprite properties")
    void testSet() {
        fontSprite.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertEquals(Font.Name.PLAYER1_SCORE, fontSprite.getName());
        assertEquals("12345", fontSprite.getMessage());
        assertEquals(Glyph.Name.CONSOLAS_20PT, fontSprite.getGlyphName());
        assertEquals(100.0f, fontSprite.getX());
        assertEquals(50.0f, fontSprite.getY());
        // Color should reset to white
        assertEquals(1.0f, fontSprite.getRed());
        assertEquals(1.0f, fontSprite.getGreen());
        assertEquals(1.0f, fontSprite.getBlue());
        assertEquals(1.0f, fontSprite.getAlpha());
    }
    
    @Test
    @DisplayName("Set method throws exception for null message")
    void testSetWithNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            fontSprite.set(Font.Name.PLAYER1_SCORE, null, Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        });
    }
    
    @Test
    @DisplayName("UpdateMessage changes displayed text")
    void testUpdateMessage() {
        fontSprite.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        fontSprite.updateMessage("67890");
        
        assertEquals("67890", fontSprite.getMessage());
    }
    
    @Test
    @DisplayName("UpdateMessage throws exception for null")
    void testUpdateMessageWithNull() {
        fontSprite.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertThrows(IllegalArgumentException.class, () -> {
            fontSprite.updateMessage(null);
        });
    }
    
    @Test
    @DisplayName("SetColor updates RGBA values")
    void testSetColor() {
        fontSprite.setColor(1.0f, 0.0f, 0.0f, 0.5f);
        
        assertEquals(1.0f, fontSprite.getRed());
        assertEquals(0.0f, fontSprite.getGreen());
        assertEquals(0.0f, fontSprite.getBlue());
        assertEquals(0.5f, fontSprite.getAlpha());
    }
    
    @Test
    @DisplayName("SetPosition updates coordinates")
    void testSetPosition() {
        fontSprite.setPosition(200.0f, 150.0f);
        
        assertEquals(200.0f, fontSprite.getX());
        assertEquals(150.0f, fontSprite.getY());
    }
    
    @Test
    @DisplayName("SetGlyphManager sets manager reference")
    void testSetGlyphManager() {
        fontSprite.setGlyphManager(mockGlyphManager);
        
        // No direct getter, but we can test via render
        assertDoesNotThrow(() -> fontSprite.setGlyphManager(mockGlyphManager));
    }
    
    @Test
    @DisplayName("Render handles empty message")
    void testRenderEmptyMessage() {
        fontSprite.set(Font.Name.PLAYER1_SCORE, "", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        fontSprite.setGlyphManager(mockGlyphManager);
        
        assertDoesNotThrow(() -> fontSprite.render());
        
        // Should not call find for empty message
        verify(mockGlyphManager, never()).find(any(), anyInt());
    }
    
    @Test
    @DisplayName("Render handles null glyph manager")
    void testRenderNullGlyphManager() {
        fontSprite.set(Font.Name.PLAYER1_SCORE, "ABC", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertDoesNotThrow(() -> fontSprite.render());
    }
    
    @Test
    @DisplayName("Render iterates through message characters")
    void testRenderIteratesCharacters() {
        when(mockGlyph.getWidth()).thenReturn(12.0f);
        when(mockGlyphManager.find(any(), anyInt())).thenReturn(mockGlyph);
        
        fontSprite.set(Font.Name.PLAYER1_SCORE, "ABC", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        fontSprite.setGlyphManager(mockGlyphManager);
        
        fontSprite.render(mockSpriteBatch);
        
        // Should call find for each character
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, 'A');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, 'B');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, 'C');
    }
    
    @Test
    @DisplayName("Render handles missing glyphs")
    void testRenderMissingGlyphs() {
        when(mockGlyphManager.find(any(), anyInt())).thenReturn(null);
        
        fontSprite.set(Font.Name.PLAYER1_SCORE, "ABC", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        fontSprite.setGlyphManager(mockGlyphManager);
        
        assertDoesNotThrow(() -> fontSprite.render());
    }
    
    @Test
    @DisplayName("Update method does not throw exception")
    void testUpdate() {
        assertDoesNotThrow(() -> fontSprite.update());
    }
    
    @Test
    @DisplayName("FontSprite supports white color")
    void testWhiteColor() {
        fontSprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        assertEquals(1.0f, fontSprite.getRed());
        assertEquals(1.0f, fontSprite.getGreen());
        assertEquals(1.0f, fontSprite.getBlue());
        assertEquals(1.0f, fontSprite.getAlpha());
    }
    
    @Test
    @DisplayName("FontSprite supports red color")
    void testRedColor() {
        fontSprite.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        
        assertEquals(1.0f, fontSprite.getRed());
        assertEquals(0.0f, fontSprite.getGreen());
        assertEquals(0.0f, fontSprite.getBlue());
    }
    
    @Test
    @DisplayName("FontSprite supports green color")
    void testGreenColor() {
        fontSprite.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        
        assertEquals(0.0f, fontSprite.getRed());
        assertEquals(1.0f, fontSprite.getGreen());
        assertEquals(0.0f, fontSprite.getBlue());
    }
    
    @Test
    @DisplayName("FontSprite supports transparency")
    void testTransparency() {
        fontSprite.setColor(1.0f, 1.0f, 1.0f, 0.5f);
        
        assertEquals(0.5f, fontSprite.getAlpha());
    }
    
    @Test
    @DisplayName("FontSprite can render numbers")
    void testRenderNumbers() {
        when(mockGlyph.getWidth()).thenReturn(12.0f);
        when(mockGlyphManager.find(any(), anyInt())).thenReturn(mockGlyph);
        
        fontSprite.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        fontSprite.setGlyphManager(mockGlyphManager);
        fontSprite.render(mockSpriteBatch);
        
        
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, '1');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, '2');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, '3');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, '4');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, '5');
    }
    
    @Test
    @DisplayName("FontSprite can render mixed characters")
    void testRenderMixedCharacters() {
        when(mockGlyph.getWidth()).thenReturn(12.0f);
        when(mockGlyphManager.find(any(), anyInt())).thenReturn(mockGlyph);
        
        fontSprite.set(Font.Name.ATTRACT_TITLE, "Score: 100", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        fontSprite.setGlyphManager(mockGlyphManager);
        fontSprite.render(mockSpriteBatch);
        
        
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, 'S');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, ':');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, ' ');
        verify(mockGlyphManager).find(Glyph.Name.CONSOLAS_20PT, '1');
    }
    
    @Test
    @DisplayName("Multiple FontSprites can be created independently")
    void testMultipleFontSprites() {
        FontSprite sprite1 = new FontSprite();
        FontSprite sprite2 = new FontSprite();
        
        sprite1.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 50.0f, 20.0f);
        sprite2.set(Font.Name.PLAYER2_SCORE, "67890", Glyph.Name.CONSOLAS_20PT, 50.0f, 40.0f);
        
        assertEquals("12345", sprite1.getMessage());
        assertEquals("67890", sprite2.getMessage());
        assertEquals(20.0f, sprite1.getY());
        assertEquals(40.0f, sprite2.getY());
    }
}