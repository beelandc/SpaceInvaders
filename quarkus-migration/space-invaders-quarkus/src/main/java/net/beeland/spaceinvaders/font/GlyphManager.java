package net.beeland.spaceinvaders.font;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import net.beeland.spaceinvaders.texture.Texture;
import net.beeland.spaceinvaders.texture.TextureManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jboss.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;

/**
 * GlyphManager - CDI-managed singleton for font glyphs
 * 
 * Manages glyph instances using Object Pool pattern.
 * Supports loading glyphs from XML font definition files.
 * 
 * Design Pattern: Singleton (via CDI @ApplicationScoped)
 * Design Pattern: Object Pool (via Manager base class)
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@ApplicationScoped
public class GlyphManager extends Manager {
    
    private static final Logger LOG = Logger.getLogger(GlyphManager.class);
    
    private static final int INITIAL_RESERVE = 100;  // ASCII characters
    private static final int GROW_SIZE = 20;
    
    @Inject
    TextureManager textureManager;
    
    /**
     * Initialize the glyph manager
     * Called automatically by CDI after construction
     */
    @PostConstruct
    public void init() {
        LOG.info("Initializing GlyphManager");
        super.initialize(INITIAL_RESERVE, GROW_SIZE);
    }
    
    /**
     * Clean up resources
     * Called automatically by CDI before destruction
     */
    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up GlyphManager");
        super.destroy();
    }
    
    /**
     * Add a glyph to the manager
     * 
     * @param name Glyph font name
     * @param key ASCII code of character
     * @param textureName Name of texture containing the glyph
     * @param x X coordinate in texture
     * @param y Y coordinate in texture
     * @param width Width of glyph
     * @param height Height of glyph
     * @return The created glyph
     */
    public Glyph add(Glyph.Name name, int key, Texture.Name textureName,
                    float x, float y, float width, float height) {
        Glyph glyph = (Glyph) getFromPool();
        
        if (glyph != null) {
            glyph.set(name, key, textureName, x, y, width, height, textureManager);
            LOG.info("Added glyph: " + name + " key=" + key + " '" + (char)key + "' at (" + x + "," + y + ") size=" + width + "x" + height);
        } else {
            LOG.error("Failed to get glyph from pool");
        }
        
        return glyph;
    }
    
    /**
     * Load glyphs from an XML font definition file
     * 
     * Expected XML format:
     * <font>
     *   <character key="65">
     *     <x>10</x>
     *     <y>20</y>
     *     <width>15</width>
     *     <height>20</height>
     *   </character>
     *   ...
     * </font>
     * 
     * @param glyphName Glyph font name
     * @param xmlPath Path to XML file (relative to resources)
     * @param textureName Name of texture containing the font
     */
    public void loadFromXml(Glyph.Name glyphName, String xmlPath, Texture.Name textureName) {
        try {
            // Load XML file from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xmlPath);
            if (inputStream == null) {
                LOG.error("Could not find XML file: " + xmlPath);
                return;
            }
            
            // Parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            
            // Get all character elements
            NodeList characterList = doc.getElementsByTagName("character");
            
            for (int i = 0; i < characterList.getLength(); i++) {
                Element charElement = (Element) characterList.item(i);
                
                // Extract character data
                int key = Integer.parseInt(charElement.getAttribute("key"));
                int x = Integer.parseInt(getElementText(charElement, "x"));
                int y = Integer.parseInt(getElementText(charElement, "y"));
                int width = Integer.parseInt(getElementText(charElement, "width"));
                int height = Integer.parseInt(getElementText(charElement, "height"));
                
                // Add glyph
                add(glyphName, key, textureName, x, y, width, height);
            }
            
            LOG.info("Loaded " + characterList.getLength() + " glyphs from " + xmlPath);
            
        } catch (Exception e) {
            LOG.error("Error loading glyphs from XML: " + xmlPath, e);
        }
    }
    
    /**
     * Helper method to get text content of an XML element
     */
    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
    
    /**
     * Find a glyph by name and key
     * 
     * @param name Glyph font name
     * @param key ASCII code of character
     * @return Glyph if found, null otherwise
     */
    public Glyph find(Glyph.Name name, int key) {
        DLink current = getActiveHead();
        
        while (current != null) {
            Glyph glyph = (Glyph) current;
            if (glyph.getName() == name && glyph.getKey() == key) {
                return glyph;
            }
            current = current.getNext();
        }
        
        LOG.warn("Glyph not found: " + name + " key=" + key + " '" + (char)key + "'");
        return null;
    }
    
    /**
     * Remove a glyph from the manager
     * 
     * @param glyph Glyph to remove
     */
    public void remove(Glyph glyph) {
        if (glyph != null) {
            returnToPool(glyph);
            LOG.debug("Removed glyph: " + glyph.getName() + " key=" + glyph.getKey());
        }
    }
    
    /**
     * Create a new glyph node for the pool
     * 
     * @return New Glyph instance
     */
    @Override
    protected DLink createNode() {
        return new Glyph();
    }
    
    /**
     * Print manager statistics (for debugging)
     */
    public void printStats() {
        LOG.info("========== GLYPH MANAGER ==========");
        LOG.info(getStats());
        LOG.info("===================================");
    }
}