package net.beeland.spaceinvaders.sprite;

import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jboss.logging.Logger;

/**
 * SpriteManager - CDI-managed singleton for sprites
 * Manages sprite instances using Object Pool pattern
 */
@ApplicationScoped
public class SpriteManager extends Manager {
    
    private static final Logger LOG = Logger.getLogger(SpriteManager.class);
    
    private static final int INITIAL_RESERVE = 100;
    private static final int GROW_SIZE = 20;
    
    /**
     * Initialize the sprite manager
     * Called automatically by CDI after construction
     */
    @PostConstruct
    public void init() {
        LOG.info("Initializing SpriteManager");
        super.initialize(INITIAL_RESERVE, GROW_SIZE);
    }
    
    /**
     * Add a sprite to the manager
     * @param name Sprite name
     * @param image Image to render
     * @param x X position
     * @param y Y position
     * @return The created sprite
     */
    public Sprite add(Sprite.Name name, Image image, float x, float y) {
        Sprite sprite = (Sprite) getFromPool();
        
        if (sprite != null) {
            sprite.set(name, image, x, y);
            LOG.debug("Added sprite: " + name);
        } else {
            LOG.error("Failed to get sprite from pool");
        }
        
        return sprite;
    }
    
    /**
     * Find a sprite by name
     * @param name Sprite name to find
     * @return Sprite if found, null otherwise
     */
    public Sprite find(Sprite.Name name) {
        DLink current = getActiveHead();
        
        while (current != null) {
            Sprite sprite = (Sprite) current;
            if (sprite.getName().equals(name.toString())) {
                return sprite;
            }
            current = current.getNext();
        }
        
        LOG.warn("Sprite not found: " + name);
        return null;
    }
    
    /**
     * Remove a sprite from the manager
     * @param sprite Sprite to remove
     */
    public void remove(Sprite sprite) {
        if (sprite != null) {
            returnToPool(sprite);
            LOG.debug("Removed sprite: " + sprite.getName());
        }
    }
    
    /**
     * Remove a sprite by name
     * @param name Name of sprite to remove
     */
    public void remove(Sprite.Name name) {
        Sprite sprite = find(name);
        if (sprite != null) {
            remove(sprite);
        }
    }
    
    /**
     * Render all active sprites
     * This will be replaced by SpriteBatch for efficiency
     */
    public void renderAll() {
        DLink current = getActiveHead();
        
        while (current != null) {
            Sprite sprite = (Sprite) current;
            sprite.render();
            current = current.getNext();
        }
    }
    
    @Override
    protected DLink createNode() {
        return new Sprite();
    }
    
    /**
     * Cleanup all sprites
     * Called automatically by CDI before destruction
     */
    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up SpriteManager");
        super.destroy();
    }
}
