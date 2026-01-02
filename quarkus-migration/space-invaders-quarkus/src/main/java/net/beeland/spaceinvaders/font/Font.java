package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.manager.DLink;

/**
 * Font - Represents a text string with rendering properties
 * 
 * Contains the text message, position, and glyph reference for rendering.
 * Fonts are managed by FontManager and rendered via FontSprite.
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
public class Font extends DLink {
    
    /**
     * Font name enumeration for identification
     */
    public enum Name {
        // Score displays
        PLAYER1_SCORE,
        PLAYER2_SCORE,
        HIGH_SCORE,
        PLAYER1_SCORE_TITLE,
        PLAYER2_SCORE_TITLE,
        HIGH_SCORE_TITLE,
        
        // Lives display
        PLAYER1_LIVES,
        PLAYER2_LIVES,
        
        // Game credits
        GAME_CREDITS,
        
        // Attract mode
        ATTRACT_PLAY,
        ATTRACT_TITLE,
        ATTRACT_SCORE_ADVANCE_TABLE,
        ATTRACT_FLYING_SAUCER_POINTS,
        ATTRACT_SQUID_POINTS,
        ATTRACT_CRAB_POINTS,
        ATTRACT_JELLYFISH_POINTS,
        ATTRACT_INSTRUCTION_1P,
        ATTRACT_INSTRUCTION_2P,
        
        // End game
        END_GAME,
        END_GAME_OVER,
        END_INSTRUCTIONS,
        
        // Testing
        TEST_MESSAGE,
        TEST_ONE_OFF,
        
        NULL_OBJECT,
        UNINITIALIZED
    }
    
    // Font properties
    private Name name;
    private String message;
    private Glyph.Name glyphName;
    private float x;
    private float y;
    private FontSprite fontSprite;
    
    /**
     * Default constructor
     */
    public Font() {
        super();
        this.name = Name.UNINITIALIZED;
        this.message = "";
        this.glyphName = Glyph.Name.UNINITIALIZED;
        this.x = 0.0f;
        this.y = 0.0f;
        this.fontSprite = null;
    }
    
    /**
     * Set font properties
     * 
     * @param name Font identifier
     * @param message Text to display
     * @param glyphName Glyph font to use
     * @param x X position
     * @param y Y position
     */
    public void set(Name name, String message, Glyph.Name glyphName, float x, float y) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        this.name = name;
        this.message = message;
        this.glyphName = glyphName;
        this.x = x;
        this.y = y;
        
        // Create font sprite if it doesn't exist
        if (this.fontSprite == null) {
            this.fontSprite = new FontSprite();
        }
        
        // Configure the font sprite
        this.fontSprite.set(name, message, glyphName, x, y);
    }
    
    /**
     * Update the displayed message
     * 
     * @param message New text to display
     */
    public void updateMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        this.message = message;
        
        if (this.fontSprite != null) {
            this.fontSprite.updateMessage(message);
        }
    }
    
    /**
     * Reset font to default state
     */
    @Override
    public void wash() {
        this.name = Name.UNINITIALIZED;
        this.message = "";
        this.glyphName = Glyph.Name.UNINITIALIZED;
        this.x = 0.0f;
        this.y = 0.0f;
        
        if (this.fontSprite != null) {
            this.fontSprite.set(Name.NULL_OBJECT, "", Glyph.Name.NULL_OBJECT, 0.0f, 0.0f);
        }
    }
    
    /**
     * Dump font information for debugging
     */
    @Override
    public void dump() {
        System.out.println("Font: " + name);
        System.out.println("  Message: \"" + message + "\"");
        System.out.println("  Glyph: " + glyphName);
        System.out.println("  Position: (" + x + ", " + y + ")");
        System.out.println("  FontSprite: " + (fontSprite != null ? "present" : "null"));
    }
    
    // Getters
    
    public Name getName() {
        return name;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Glyph.Name getGlyphName() {
        return glyphName;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public FontSprite getFontSprite() {
        return fontSprite;
    }
    
    // Setters
    
    public void setName(Name name) {
        this.name = name;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        if (this.fontSprite != null) {
            this.fontSprite.setPosition(x, y);
        }
    }
    
    public void setColor(float red, float green, float blue, float alpha) {
        if (this.fontSprite != null) {
            this.fontSprite.setColor(red, green, blue, alpha);
        }
    }
}