package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.texture.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FontManager class
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2024-12-23
 */
@DisplayName("FontManager Tests")
class FontManagerTest {
    
    private FontManager fontManager;
    
    @Mock
    private GlyphManager mockGlyphManager;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fontManager = new FontManager();
        fontManager.glyphManager = mockGlyphManager;
        fontManager.init();
    }
    
    @Test
    @DisplayName("Initialization creates reserve pool")
    void testInit() {
        assertNotNull(fontManager);
        String stats = fontManager.getStats();
        assertTrue(stats.contains("Reserved:"));
    }
    
    @Test
    @DisplayName("Add creates and configures font")
    void testAdd() {
        Font font = fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                                    Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertNotNull(font);
        assertEquals(Font.Name.PLAYER1_SCORE, font.getName());
        assertEquals("12345", font.getMessage());
        assertEquals(Glyph.Name.CONSOLAS_20PT, font.getGlyphName());
        assertEquals(100.0f, font.getX());
        assertEquals(50.0f, font.getY());
        assertNotNull(font.getFontSprite());
    }
    
    @Test
    @DisplayName("Add sets glyph manager on font sprite")
    void testAddSetsGlyphManager() {
        Font font = fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                                    Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        assertNotNull(font.getFontSprite());
        // GlyphManager should be set (we can't directly verify, but render won't error)
        assertDoesNotThrow(() -> font.getFontSprite().render());
    }
    
    @Test
    @DisplayName("Find locates font by name")
    void testFind() {
        fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                       Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        Font found = fontManager.find(Font.Name.PLAYER1_SCORE);
        
        assertNotNull(found);
        assertEquals(Font.Name.PLAYER1_SCORE, found.getName());
        assertEquals("12345", found.getMessage());
    }
    
    @Test
    @DisplayName("Find returns null for non-existent font")
    void testFindNotFound() {
        Font found = fontManager.find(Font.Name.HIGH_SCORE);
        assertNull(found);
    }
    
    @Test
    @DisplayName("Remove returns font to pool")
    void testRemove() {
        Font font = fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                                    Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        String statsBefore = fontManager.getStats();
        fontManager.remove(font);
        String statsAfter = fontManager.getStats();
        
        assertTrue(statsBefore.contains("Active: 1"));
        assertTrue(statsAfter.contains("Active: 0"));
    }
    
    @Test
    @DisplayName("UpdateMessage updates font by name")
    void testUpdateMessage() {
        fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                       Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        fontManager.updateMessage(Font.Name.PLAYER1_SCORE, "67890");
        
        Font font = fontManager.find(Font.Name.PLAYER1_SCORE);
        assertEquals("67890", font.getMessage());
    }
    
    @Test
    @DisplayName("UpdateMessage handles non-existent font")
    void testUpdateMessageNotFound() {
        assertDoesNotThrow(() -> {
            fontManager.updateMessage(Font.Name.HIGH_SCORE, "99999");
        });
    }
    
    @Test
    @DisplayName("Multiple fonts can be added")
    void testMultipleFonts() {
        Font font1 = fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                                     Glyph.Name.CONSOLAS_20PT, 50.0f, 20.0f);
        Font font2 = fontManager.add(Font.Name.PLAYER2_SCORE, "67890", 
                                     Glyph.Name.CONSOLAS_20PT, 50.0f, 40.0f);
        Font font3 = fontManager.add(Font.Name.HIGH_SCORE, "99999", 
                                     Glyph.Name.CONSOLAS_20PT, 200.0f, 20.0f);
        
        assertNotNull(font1);
        assertNotNull(font2);
        assertNotNull(font3);
        String stats = fontManager.getStats();
        assertTrue(stats.contains("Active: 3"));
    }
    
    @Test
    @DisplayName("Find distinguishes between different font names")
    void testFindDifferentFonts() {
        fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                       Glyph.Name.CONSOLAS_20PT, 50.0f, 20.0f);
        fontManager.add(Font.Name.PLAYER2_SCORE, "67890", 
                       Glyph.Name.CONSOLAS_20PT, 50.0f, 40.0f);
        
        Font font1 = fontManager.find(Font.Name.PLAYER1_SCORE);
        Font font2 = fontManager.find(Font.Name.PLAYER2_SCORE);
        
        assertNotNull(font1);
        assertNotNull(font2);
        assertEquals("12345", font1.getMessage());
        assertEquals("67890", font2.getMessage());
        assertNotEquals(font1, font2);
    }
    
    @Test
    @DisplayName("LoadGlyphsFromXml delegates to GlyphManager")
    void testLoadGlyphsFromXml() {
        fontManager.loadGlyphsFromXml(Glyph.Name.CONSOLAS_20PT, 
                                     "fonts/consolas20.xml", 
                                     Texture.Name.ALIENS);
        
        verify(mockGlyphManager).loadFromXml(Glyph.Name.CONSOLAS_20PT, 
                                            "fonts/consolas20.xml", 
                                            Texture.Name.ALIENS);
    }
    
    @Test
    @DisplayName("LoadGlyphsFromXml handles null glyph manager")
    void testLoadGlyphsFromXmlNullManager() {
        FontManager manager = new FontManager();
        manager.glyphManager = null;
        manager.init();
        
        assertDoesNotThrow(() -> {
            manager.loadGlyphsFromXml(Glyph.Name.CONSOLAS_20PT, 
                                     "fonts/consolas20.xml", 
                                     Texture.Name.ALIENS);
        });
    }
    
    @Test
    @DisplayName("RenderAll iterates through active fonts")
    void testRenderAll() {
        fontManager.add(Font.Name.PLAYER1_SCORE, "12345", 
                       Glyph.Name.CONSOLAS_20PT, 50.0f, 20.0f);
        fontManager.add(Font.Name.PLAYER2_SCORE, "67890", 
                       Glyph.Name.CONSOLAS_20PT, 50.0f, 40.0f);
        
        assertDoesNotThrow(() -> fontManager.renderAll());
    }
    
    @Test
    @DisplayName("RenderAll handles empty font list")
    void testRenderAllEmpty() {
        assertDoesNotThrow(() -> fontManager.renderAll());
    }
    
    @Test
    @DisplayName("Pool grows when reserve is exhausted")
    void testPoolGrowth() {
        // Add more fonts than initial reserve (20)
        for (int i = 0; i < 25; i++) {
            fontManager.add(Font.Name.TEST_MESSAGE, "Test" + i, 
                          Glyph.Name.CONSOLAS_20PT, 0.0f, 0.0f);
        }
        
        String stats = fontManager.getStats();
        assertTrue(stats.contains("Active: 25"));
    }
    
    @Test
    @DisplayName("PrintStats does not throw exception")
    void testPrintStats() {
        assertDoesNotThrow(() -> fontManager.printStats());
    }
    
    @Test
    @DisplayName("Cleanup destroys manager")
    void testCleanup() {
        assertDoesNotThrow(() -> fontManager.cleanup());
    }
    
    @Test
    @DisplayName("Remove handles null font gracefully")
    void testRemoveNull() {
        assertDoesNotThrow(() -> fontManager.remove(null));
    }
    
    @Test
    @DisplayName("Font can be updated multiple times")
    void testMultipleUpdates() {
        fontManager.add(Font.Name.PLAYER1_SCORE, "00000", 
                       Glyph.Name.CONSOLAS_20PT, 100.0f, 50.0f);
        
        fontManager.updateMessage(Font.Name.PLAYER1_SCORE, "00100");
        Font font = fontManager.find(Font.Name.PLAYER1_SCORE);
        assertEquals("00100", font.getMessage());
        
        fontManager.updateMessage(Font.Name.PLAYER1_SCORE, "00200");
        assertEquals("00200", font.getMessage());
        
        fontManager.updateMessage(Font.Name.PLAYER1_SCORE, "00300");
        assertEquals("00300", font.getMessage());
    }
    
    @Test
    @DisplayName("Font supports score display use case")
    void testScoreDisplayUseCase() {
        // Add score title
        Font scoreTitle = fontManager.add(Font.Name.PLAYER1_SCORE_TITLE, "SCORE", 
                                         Glyph.Name.CONSOLAS_20PT, 50.0f, 10.0f);
        
        // Add score value
        Font scoreValue = fontManager.add(Font.Name.PLAYER1_SCORE, "00000", 
                                         Glyph.Name.CONSOLAS_20PT, 50.0f, 30.0f);
        
        assertNotNull(scoreTitle);
        assertNotNull(scoreValue);
        
        // Update score
        fontManager.updateMessage(Font.Name.PLAYER1_SCORE, "01234");
        
        Font updatedScore = fontManager.find(Font.Name.PLAYER1_SCORE);
        assertEquals("01234", updatedScore.getMessage());
    }
    
    @Test
    @DisplayName("Font supports lives display use case")
    void testLivesDisplayUseCase() {
        Font lives = fontManager.add(Font.Name.PLAYER1_LIVES, "3", 
                                    Glyph.Name.CONSOLAS_20PT, 20.0f, 550.0f);
        
        assertNotNull(lives);
        assertEquals("3", lives.getMessage());
        
        // Decrease lives
        fontManager.updateMessage(Font.Name.PLAYER1_LIVES, "2");
        Font updatedLives = fontManager.find(Font.Name.PLAYER1_LIVES);
        assertEquals("2", updatedLives.getMessage());
    }
    
    @Test
    @DisplayName("Font supports attract mode use case")
    void testAttractModeUseCase() {
        Font title = fontManager.add(Font.Name.ATTRACT_TITLE, "SPACE INVADERS", 
                                    Glyph.Name.CONSOLAS_36PT, 200.0f, 100.0f);
        Font play = fontManager.add(Font.Name.ATTRACT_PLAY, "PLAY", 
                                   Glyph.Name.CONSOLAS_20PT, 350.0f, 300.0f);
        
        assertNotNull(title);
        assertNotNull(play);
        assertEquals("SPACE INVADERS", title.getMessage());
        assertEquals("PLAY", play.getMessage());
    }
}