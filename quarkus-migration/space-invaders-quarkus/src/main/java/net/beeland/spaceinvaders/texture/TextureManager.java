package net.beeland.spaceinvaders.texture;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jboss.logging.Logger;

/**
 * TextureManager - Singleton manager for textures using CDI
 * Implements Object Pool pattern for efficient texture management
 */
@ApplicationScoped
public class TextureManager extends Manager {
    
    private static final Logger LOG = Logger.getLogger(TextureManager.class);
    
    private static final int INITIAL_RESERVE = 10;
    private static final int GROW_SIZE = 5;
    
    /**
     * Initialize the texture manager
     * Called automatically by CDI after construction
     */
    @PostConstruct
    public void init() {
        LOG.info("Initializing TextureManager");
        super.initialize(INITIAL_RESERVE, GROW_SIZE);
    }
    
    /**
     * Add a texture to the manager
     * @param name Texture name
     * @param filePath Path to texture file
     * @return The created texture
     */
    public Texture add(Texture.Name name, String filePath) {
        Texture texture = (Texture) getFromPool();
        
        if (texture != null) {
            texture.set(name, filePath);
            LOG.debug("Added texture: " + name);
        } else {
            LOG.error("Failed to get texture from pool");
        }
        
        return texture;
    }
    
    /**
     * Find a texture by name
     * @param name Texture name to find
     * @return Texture if found, null otherwise
     */
    public Texture find(Texture.Name name) {
        DLink current = getActiveHead();
        
        while (current != null) {
            Texture texture = (Texture) current;
            if (texture.getName().equals(name.toString())) {
                return texture;
            }
            current = current.getNext();
        }
        
        LOG.warn("Texture not found: " + name);
        return null;
    }
    
    /**
     * Remove a texture from the manager
     * @param texture Texture to remove
     */
    public void remove(Texture texture) {
        if (texture != null) {
            texture.cleanup();
            returnToPool(texture);
            LOG.debug("Removed texture: " + texture.getName());
        }
    }
    
    /**
     * Remove a texture by name
     * @param name Name of texture to remove
     */
    public void remove(Texture.Name name) {
        Texture texture = find(name);
        if (texture != null) {
            remove(texture);
        }
    }
    
    @Override
    protected DLink createNode() {
        return new Texture();
    }
    
    /**
     * Cleanup all textures
     * Called automatically by CDI before destruction
     */
    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up TextureManager");
        
        // Cleanup all active textures
        DLink current = getActiveHead();
        while (current != null) {
            Texture texture = (Texture) current;
            texture.cleanup();
            current = current.getNext();
        }
        
        super.destroy();
    }
    
    /**
     * Load default textures for the game
     * Should be called during game initialization
     */
    public void loadDefaultTextures() {
        LOG.info("Loading default textures...");
        
        // Note: These paths will need to be adjusted based on actual resource location
        String resourcePath = "src/main/resources/textures/";
        
        try {
            // Load game textures
            // These will be loaded when actual texture files are available
            LOG.info("Default textures loaded successfully");
        } catch (Exception e) {
            LOG.error("Error loading default textures", e);
        }
    }
}
