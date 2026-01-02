package net.beeland.spaceinvaders.font;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Font class
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2024-12-23
 */
@DisplayName("Font Tests")
class FontTest {
    
    private Font font;
    
    @BeforeEach
    void setUp() {
        font = new Font();
    }
    
    @Test
    @DisplayName("Constructor initializes with default values")
    void testConstructor() {
        assertEquals(Font.Name.UNINITIALIZED, font.getName());
        assertEquals("", font.getMessage());
        assertEquals(Glyph.Name.UNINITIALIZED, font.getGlyphName());
        assertEquals(0.0f, font.getX());
        assertEquals(0.0f, font.getY());
        assertNull(font.getFontSprite());
    }
    
    @Test
    @DisplayName("Set method configures font properties")
    void testSet() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertEquals(Font.Name.PLAYER1_SCORE, font.getName());
        assertEquals("12345", font.getMessage());
        assertEquals(Glyph.Name.CONSOLAS_20PT, font.getGlyphName());
        assertEquals(100.0f, font.getX());
        assertEquals(50.0f, font.getY());
        assertNotNull(font.getFontSprite());
    }
    
    @Test
    @DisplayName("Set method throws exception for null message")
    void testSetWithNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            font.set(Font.Name.PLAYER1_SCORE, null, Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        });
    }
    
    @Test
    @DisplayName("UpdateMessage changes displayed text")
    void testUpdateMessage() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        font.updateMessage("67890");
        
        assertEquals("67890", font.getMessage());
    }
    
    @Test
    @DisplayName("UpdateMessage throws exception for null")
    void testUpdateMessageWithNull() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertThrows(IllegalArgumentException.class, () -> {
            font.updateMessage(null);
        });
    }
    
    @Test
    @DisplayName("UpdateMessage updates FontSprite")
    void testUpdateMessageUpdatesFontSprite() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        font.updateMessage("99999");
        
        assertEquals("99999", font.getFontSprite().getMessage());
    }
    
    @Test
    @DisplayName("Wash resets font to default state")
    void testWash() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        font.wash();
        
        assertEquals(Font.Name.UNINITIALIZED, font.getName());
        assertEquals("", font.getMessage());
        assertEquals(Glyph.Name.UNINITIALIZED, font.getGlyphName());
        assertEquals(0.0f, font.getX());
        assertEquals(0.0f, font.getY());
    }
    
    @Test
    @DisplayName("Dump prints font information")
    void testDump() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertDoesNotThrow(() -> font.dump());
    }
    
    @Test
    @DisplayName("SetName updates font name")
    void testSetName() {
        font.setName(Font.Name.HIGH_SCORE);
        assertEquals(Font.Name.HIGH_SCORE, font.getName());
    }
    
    @Test
    @DisplayName("SetPosition updates coordinates")
    void testSetPosition() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        font.setPosition(200.0f, 150.0f);
        
        assertEquals(200.0f, font.getX());
        assertEquals(150.0f, font.getY());
    }
    
    @Test
    @DisplayName("SetPosition updates FontSprite position")
    void testSetPositionUpdatesFontSprite() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        font.setPosition(200.0f, 150.0f);
        
        assertEquals(200.0f, font.getFontSprite().getX());
        assertEquals(150.0f, font.getFontSprite().getY());
    }
    
    @Test
    @DisplayName("SetColor updates FontSprite color")
    void testSetColor() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        
        assertEquals(1.0f, font.getFontSprite().getRed());
        assertEquals(0.0f, font.getFontSprite().getGreen());
        assertEquals(0.0f, font.getFontSprite().getBlue());
        assertEquals(1.0f, font.getFontSprite().getAlpha());
    }
    
    @Test
    @DisplayName("SetColor handles null FontSprite gracefully")
    void testSetColorWithNullFontSprite() {
        assertDoesNotThrow(() -> font.setColor(1.0f, 0.0f, 0.0f, 1.0f));
    }
    
    @Test
    @DisplayName("SetPosition handles null FontSprite gracefully")
    void testSetPositionWithNullFontSprite() {
        assertDoesNotThrow(() -> font.setPosition(100.0f, 50.0f));
    }
    
    @Test
    @DisplayName("Font can display score")
    void testScoreDisplay() {
        font.set(Font.Name.PLAYER1_SCORE, "00000", Glyph.Name.CONSOLAS_20PT, 50.0f, 20.0f);
        
        assertEquals("00000", font.getMessage());
        
        font.updateMessage("01234");
        assertEquals("01234", font.getMessage());
    }
    
    @Test
    @DisplayName("Font can display text messages")
    void testTextDisplay() {
        font.set(Font.Name.ATTRACT_TITLE, "SPACE INVADERS", Glyph.Name.CONSOLAS_36PT, 
                200.0f, 100.0f);
        
        assertEquals("SPACE INVADERS", font.getMessage());
    }
    
    @Test
    @DisplayName("Font supports empty message")
    void testEmptyMessage() {
        font.set(Font.Name.TEST_MESSAGE, "", Glyph.Name.CONSOLAS_20PT, 0.0f, 0.0f);
        
        assertEquals("", font.getMessage());
    }
    
    @Test
    @DisplayName("Multiple fonts can be created independently")
    void testMultipleFonts() {
        Font font1 = new Font();
        Font font2 = new Font();
        
        font1.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 50.0f, 20.0f);
        font2.set(Font.Name.PLAYER2_SCORE, "67890", Glyph.Name.CONSOLAS_20PT, 50.0f, 40.0f);
        
        assertEquals("12345", font1.getMessage());
        assertEquals("67890", font2.getMessage());
        assertNotEquals(font1.getFontSprite(), font2.getFontSprite());
    }
    
    @Test
    @DisplayName("Font creates FontSprite on first set")
    void testFontSpriteCreation() {
        assertNull(font.getFontSprite());
        
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertNotNull(font.getFontSprite());
    }
    
    @Test
    @DisplayName("Font reuses FontSprite on subsequent sets")
    void testFontSpriteReuse() {
        font.set(Font.Name.PLAYER1_SCORE, "12345", Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        FontSprite firstSprite = font.getFontSprite();
        
        font.set(Font.Name.PLAYER1_SCORE, "67890", Glyph.Name.CONSOLAS_20PT, 200.0f, 100.0f);
        FontSprite secondSprite = font.getFontSprite();
        
        assertSame(firstSprite, secondSprite);
    }
}