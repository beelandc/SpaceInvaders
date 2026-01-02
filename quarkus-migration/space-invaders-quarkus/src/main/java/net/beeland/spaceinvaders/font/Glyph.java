package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.texture.Texture;
import net.beeland.spaceinvaders.texture.TextureManager;

/**
 * Glyph - Represents a single character in a font
 * 
 * Contains texture coordinates and dimensions for rendering a character.
 * Glyphs are typically loaded from a font atlas texture.
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
public class Glyph extends DLink {
    
    /**
     * Glyph font name enumeration
     */
    public enum Name {
        CONSOLAS_20PT,
        CONSOLAS_36PT,
        NULL_OBJECT,
        UNINITIALIZED
    }
    
    // Glyph properties
    private Name name;
    private int key;              // ASCII code of the character
    private Image image;          // Image region for this glyph
    private float x;              // Texture X coordinate
    private float y;              // Texture Y coordinate
    private float width;          // Glyph width in pixels
    private float height;         // Glyph height in pixels
    
    /**
     * Default constructor
     */
    public Glyph() {
        super();
        this.name = Name.UNINITIALIZED;
        this.key = 0;
        this.image = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.width = 0.0f;
        this.height = 0.0f;
    }
    
    /**
     * Set glyph properties
     * 
     * @param name Glyph font name
     * @param key ASCII code of character
     * @param textureName Name of texture containing the glyph
     * @param x X coordinate in texture
     * @param y Y coordinate in texture
     * @param width Width of glyph
     * @param height Height of glyph
     * @param textureManager TextureManager instance for finding textures
     */
    public void set(Name name, int key, Texture.Name textureName, 
                   float x, float y, float width, float height,
                   TextureManager textureManager) {
        this.name = name;
        this.key = key;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Find the texture
        if (textureManager != null) {
            Texture texture = textureManager.find(textureName);
            if (texture != null) {
                // Create an image for this glyph region
                // Note: We need to create a corresponding Image.Name for glyphs
                // For now, we'll store the image reference directly
                this.image = new Image();
                this.image.set(Image.Name.UNINITIALIZED, texture,
                             (int)x, (int)y, (int)width, (int)height);
            }
        }
    }
    
    /**
     * Reset glyph to default state
     */
    @Override
    public void wash() {
        this.name = Name.UNINITIALIZED;
        this.key = 0;
        this.image = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.width = 1.0f;
        this.height = 1.0f;
    }
    
    /**
     * Dump glyph information for debugging
     */
    @Override
    public void dump() {
        System.out.println("Glyph: " + name + " (key: " + key + " '" + (char)key + "')");
        System.out.println("  Position: (" + x + ", " + y + ")");
        System.out.println("  Size: " + width + " x " + height);
        System.out.println("  Image: " + (image != null ? "present" : "null"));
    }
    
    // Getters
    
    public Name getName() {
        return name;
    }
    
    public int getKey() {
        return key;
    }
    
    public Image getImage() {
        return image;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    // Setters
    
    public void setName(Name name) {
        this.name = name;
    }
    
    public void setKey(int key) {
        this.key = key;
    }
}