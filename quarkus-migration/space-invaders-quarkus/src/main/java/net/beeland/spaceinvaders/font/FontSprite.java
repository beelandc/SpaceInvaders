package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.sprite.SpriteBatch;
import net.beeland.spaceinvaders.image.Image;

/**
 * FontSprite - Renders text strings using bitmap fonts
 * 
 * Iterates through a text string and renders each character using
 * glyphs from a font atlas texture.
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
public class FontSprite {
    
    // Font sprite properties
    private Font.Name name;
    private String message;
    private Glyph.Name glyphName;
    private float x;
    private float y;
    
    // Color (RGBA)
    private float red;
    private float green;
    private float blue;
    private float alpha;
    
    // Reference to glyph manager (will be injected when rendering)
    private GlyphManager glyphManager;
    
    /**
     * Default constructor
     */
    public FontSprite() {
        this.name = Font.Name.UNINITIALIZED;
        this.message = "";
        this.glyphName = Glyph.Name.UNINITIALIZED;
        this.x = 0.0f;
        this.y = 0.0f;
        
        // Default to white color
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
        
        this.glyphManager = null;
    }
    
    /**
     * Set font sprite properties
     * 
     * @param name Font identifier
     * @param message Text to display
     * @param glyphName Glyph font to use
     * @param x X position
     * @param y Y position
     */
    public void set(Font.Name name, String message, Glyph.Name glyphName, float x, float y) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        this.name = name;
        this.message = message;
        this.glyphName = glyphName;
        this.x = x;
        this.y = y;
        
        // Reset color to white
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    /**
     * Set the glyph manager for rendering
     * 
     * @param glyphManager GlyphManager instance
     */
    public void setGlyphManager(GlyphManager glyphManager) {
        this.glyphManager = glyphManager;
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
    }
    
    /**
     * Set the color for text rendering
     * 
     * @param red Red component (0-1)
     * @param green Green component (0-1)
     * @param blue Blue component (0-1)
     * @param alpha Alpha component (0-1)
     */
    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    /**
     * Set the position
     * 
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Render the text string using SpriteBatch
     *
     * This method iterates through each character in the message,
     * finds the corresponding glyph, and renders it using the sprite batch.
     *
     * @param spriteBatch SpriteBatch to use for rendering
     */
    public void render(SpriteBatch spriteBatch) {
        
        if (message == null || message.isEmpty()) {
            System.err.println("  Message is null or empty, returning");
            return;
        }
        
        if (glyphManager == null) {
            System.err.println("  GlyphManager not set, returning");
            return;
        }
        
        if (spriteBatch == null) {
            System.err.println("  SpriteBatch is null, returning");
            return;
        }
        
        float xOffset = x;
        
        // Iterate through each character
        int renderedCount = 0;
        int skippedCount = 0;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            int key = (int) c;
            
            
            // Find the glyph for this character
            Glyph glyph = glyphManager.find(glyphName, key);
            
            
            if (glyph != null && glyph.getImage() != null) {
                Image image = glyph.getImage();
                
                // Get texture ID
                int textureId = image.getTexture().getTextureId();
                
                // Image coordinates are ALREADY normalized (0-1 range)
                // Do NOT divide by texture dimensions again!
                float u0 = image.getX();
                float v0 = image.getY();
                float u1 = image.getX() + image.getWidth();
                float v1 = image.getY() + image.getHeight();
                
                // Draw this character
                spriteBatch.drawTexture(
                    textureId,
                    xOffset, y,
                    glyph.getWidth(), glyph.getHeight(),
                    u0, v0, u1, v1,
                    red, green, blue, alpha
                );
                
                renderedCount++;
                // Move to next character position
                xOffset += glyph.getWidth();
            } else {
                // Character not found, skip it or use a default width
                System.err.println("  WARNING: Glyph not found for char '" + c + "' (key=" + key + ")");
                skippedCount++;
                xOffset += 10.0f; // Default spacing
            }
        }
    }
    
    /**
     * Render the text string (deprecated - use render(SpriteBatch) instead)
     * Kept for backwards compatibility
     */
    @Deprecated
    public void render() {
        // This method is deprecated and does nothing
        // Use render(SpriteBatch) instead
    }
    
    /**
     * Update method (placeholder for future animation support)
     */
    public void update() {
        // Currently no per-frame updates needed
        // This is here for compatibility with the sprite system
    }
    
    // Getters
    
    public Font.Name getName() {
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
    
    public float getRed() {
        return red;
    }
    
    public float getGreen() {
        return green;
    }
    
    public float getBlue() {
        return blue;
    }
    
    public float getAlpha() {
        return alpha;
    }
}