package net.beeland.spaceinvaders.image;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import net.beeland.spaceinvaders.texture.Texture;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jboss.logging.Logger;

/**
 * ImageManager - CDI-managed singleton for images
 * Manages rectangular regions within textures for sprite rendering
 */
@ApplicationScoped
public class ImageManager extends Manager {
    
    private static final Logger LOG = Logger.getLogger(ImageManager.class);
    
    private static final int INITIAL_RESERVE = 50;
    private static final int GROW_SIZE = 10;
    
    /**
     * Initialize the image manager
     * Called automatically by CDI after construction
     */
    @PostConstruct
    public void init() {
        LOG.info("Initializing ImageManager");
        super.initialize(INITIAL_RESERVE, GROW_SIZE);
    }
    
    /**
     * Add an image using pixel coordinates
     * @param name Image name
     * @param texture Source texture
     * @param x X position in pixels
     * @param y Y position in pixels
     * @param width Width in pixels
     * @param height Height in pixels
     * @return The created image
     */
    public Image add(Image.Name name, Texture texture, int x, int y, int width, int height) {
        Image image = (Image) getFromPool();
        
        if (image != null) {
            image.set(name, texture, x, y, width, height);
            LOG.debug("Added image: " + name);
        } else {
            LOG.error("Failed to get image from pool");
        }
        
        return image;
    }
    
    /**
     * Add an image using the entire texture
     * @param name Image name
     * @param texture Source texture
     * @return The created image
     */
    public Image add(Image.Name name, Texture texture) {
        Image image = (Image) getFromPool();
        
        if (image != null) {
            image.set(name, texture);
            LOG.debug("Added full texture image: " + name);
        } else {
            LOG.error("Failed to get image from pool");
        }
        
        return image;
    }
    
    /**
     * Find an image by name
     * @param name Image name to find
     * @return Image if found, null otherwise
     */
    public Image find(Image.Name name) {
        DLink current = getActiveHead();
        
        while (current != null) {
            Image image = (Image) current;
            if (image.getName().equals(name.toString())) {
                return image;
            }
            current = current.getNext();
        }
        
        LOG.warn("Image not found: " + name);
        return null;
    }
    
    /**
     * Remove an image from the manager
     * @param image Image to remove
     */
    public void remove(Image image) {
        if (image != null) {
            returnToPool(image);
            LOG.debug("Removed image: " + image.getName());
        }
    }
    
    /**
     * Remove an image by name
     * @param name Name of image to remove
     */
    public void remove(Image.Name name) {
        Image image = find(name);
        if (image != null) {
            remove(image);
        }
    }
    
    @Override
    protected DLink createNode() {
        return new Image();
    }
    
    /**
     * Cleanup all images
     * Called automatically by CDI before destruction
     */
    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up ImageManager");
        super.destroy();
    }
}
