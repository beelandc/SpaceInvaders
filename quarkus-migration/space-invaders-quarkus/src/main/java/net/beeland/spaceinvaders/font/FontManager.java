package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jboss.logging.Logger;

/**
 * FontManager - CDI-managed singleton for fonts
 * 
 * Manages font instances using Object Pool pattern.
 * Integrates with GlyphManager for character rendering.
 * 
 * Design Pattern: Singleton (via CDI @ApplicationScoped)
 * Design Pattern: Object Pool (via Manager base class)
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@ApplicationScoped
public class FontManager extends Manager {
    
    private static final Logger LOG = Logger.getLogger(FontManager.class);
    
    private static final int INITIAL_RESERVE = 20;
    private static final int GROW_SIZE = 5;
    
    @Inject
    GlyphManager glyphManager;
    
    /**
     * Initialize the font manager
     * Called automatically by CDI after construction
     */
    @PostConstruct
    public void init() {
        LOG.info("Initializing FontManager");
        super.initialize(INITIAL_RESERVE, GROW_SIZE);
    }
    
    /**
     * Clean up resources
     * Called automatically by CDI before destruction
     */
    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up FontManager");
        super.destroy();
    }
    
    /**
     * Add a font to the manager
     * 
     * @param name Font identifier
     * @param message Text to display
     * @param glyphName Glyph font to use
     * @param x X position
     * @param y Y position
     * @return The created font
     */
    public Font add(Font.Name name, String message, Glyph.Name glyphName, float x, float y) {
        Font font = (Font) getFromPool();
        
        if (font != null) {
            font.set(name, message, glyphName, x, y);
            
            // Set the glyph manager on the font sprite
            if (font.getFontSprite() != null) {
                font.getFontSprite().setGlyphManager(glyphManager);
            }
            
            LOG.debug("Added font: " + name + " message=\"" + message + "\"");
        } else {
            LOG.error("Failed to get font from pool");
        }
        
        return font;
    }
    
    /**
     * Load glyphs from XML (delegates to GlyphManager)
     * 
     * @param glyphName Glyph font name
     * @param xmlPath Path to XML file
     * @param textureName Name of texture containing the font
     */
    public void loadGlyphsFromXml(Glyph.Name glyphName, String xmlPath, 
                                   net.beeland.spaceinvaders.texture.Texture.Name textureName) {
        if (glyphManager != null) {
            glyphManager.loadFromXml(glyphName, xmlPath, textureName);
        } else {
            LOG.error("GlyphManager not available");
        }
    }
    
    /**
     * Find a font by name
     * 
     * @param name Font name to find
     * @return Font if found, null otherwise
     */
    public Font find(Font.Name name) {
        DLink current = getActiveHead();
        
        while (current != null) {
            Font font = (Font) current;
            if (font.getName() == name) {
                return font;
            }
            current = current.getNext();
        }
        
        LOG.warn("Font not found: " + name);
        return null;
    }
    
    /**
     * Remove a font from the manager
     *
     * @param font Font to remove
     */
    public void remove(Font font) {
        if (font != null) {
            returnToPool(font);
            LOG.debug("Removed font: " + font.getName());
        }
    }
    
    /**
     * Remove all fonts from the manager
     * Useful when transitioning between game states
     */
    public void removeAll() {
        DLink current = getActiveHead();
        int removedCount = 0;
        
        while (current != null) {
            DLink next = current.getNext();
            Font font = (Font) current;
            returnToPool(font);
            removedCount++;
            current = next;
        }
        
        LOG.debug("Removed all fonts: " + removedCount + " fonts cleared");
    }
    
    /**
     * Update a font's message
     * 
     * @param name Font name
     * @param message New message
     */
    public void updateMessage(Font.Name name, String message) {
        Font font = find(name);
        if (font != null) {
            font.updateMessage(message);
            LOG.debug("Updated font " + name + " message to: \"" + message + "\"");
        } else {
            LOG.warn("Cannot update message - font not found: " + name);
        }
    }
    
    /**
     * Create a new font node for the pool
     * 
     * @return New Font instance
     */
    @Override
    protected DLink createNode() {
        return new Font();
    }
    
    /**
     * Print manager statistics (for debugging)
     */
    public void printStats() {
        LOG.info("========== FONT MANAGER ==========");
        LOG.info(getStats());
        LOG.info("==================================");
    }
    
    /**
     * Render all active fonts using SpriteBatch
     * This method iterates through all fonts and calls their render method
     *
     * @param spriteBatch SpriteBatch to use for rendering
     */
    public void renderAll(net.beeland.spaceinvaders.sprite.SpriteBatch spriteBatch) {
        if (spriteBatch == null) {
            LOG.warn("Cannot render fonts - SpriteBatch is null");
            return;
        }
        
        DLink current = getActiveHead();
        int fontCount = 0;
        
        LOG.debug("FontManager.renderAll() starting");
        
        while (current != null) {
            Font font = (Font) current;
            fontCount++;
            LOG.debug("Rendering font: " + font.getName());
            
            FontSprite fontSprite = font.getFontSprite();
            if (fontSprite != null) {
                String message = fontSprite.getMessage();
                LOG.debug("  FontSprite message: \"" + message + "\" (length=" + (message != null ? message.length() : "null") + ")");
                fontSprite.render(spriteBatch);
            } else {
                LOG.warn("  Font " + font.getName() + " has null FontSprite!");
            }
            current = current.getNext();
        }
        
        LOG.debug("FontManager.renderAll() completed - rendered " + fontCount + " fonts");
    }
    
    /**
     * Render all active fonts (deprecated - use renderAll(SpriteBatch) instead)
     * Kept for backwards compatibility
     */
    @Deprecated
    public void renderAll() {
        // This method is deprecated and does nothing
        // Use renderAll(SpriteBatch) instead
    }
}