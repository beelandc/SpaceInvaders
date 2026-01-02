package net.beeland.spaceinvaders.animation;

import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import net.beeland.spaceinvaders.sprite.Sprite;
import net.beeland.spaceinvaders.sprite.SpriteManager;
import net.beeland.spaceinvaders.timer.TimerManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jboss.logging.Logger;

/**
 * AnimationManager - CDI-managed singleton for sprite animations
 * 
 * Manages animation instances using Object Pool pattern.
 * Integrates with SpriteManager and TimerManager for animation playback.
 * 
 * Design Pattern: Singleton (via CDI @ApplicationScoped)
 * Design Pattern: Object Pool (via Manager base class)
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@ApplicationScoped
public class AnimationManager extends Manager {
    
    private static final Logger LOG = Logger.getLogger(AnimationManager.class);
    
    private static final int INITIAL_RESERVE = 10;
    private static final int GROW_SIZE = 5;
    
    @Inject
    SpriteManager spriteManager;
    
    @Inject
    TimerManager timerManager;
    
    /**
     * Initialize the animation manager
     * Called automatically by CDI after construction
     */
    @PostConstruct
    public void init() {
        LOG.info("Initializing AnimationManager");
        super.initialize(INITIAL_RESERVE, GROW_SIZE);
    }
    
    /**
     * Clean up resources
     * Called automatically by CDI before destruction
     */
    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up AnimationManager");
        super.destroy();
    }
    
    /**
     * Add an animation to the manager
     * 
     * @param name Animation identifier
     * @param spriteName Name of sprite to animate
     * @param frameDuration Time per frame in seconds
     * @param loop Whether animation should loop
     * @return The created animation
     */
    public Animation add(Animation.Name name, Sprite.Name spriteName, 
                        float frameDuration, boolean loop) {
        Animation animation = (Animation) getFromPool();
        
        if (animation != null) {
            animation.set(name, spriteName, frameDuration, loop, spriteManager, timerManager);
            LOG.debug("Added animation: " + name);
        } else {
            LOG.error("Failed to get animation from pool");
        }
        
        return animation;
    }
    
    /**
     * Find an animation by name
     * 
     * @param name Animation name to find
     * @return Animation if found, null otherwise
     */
    public Animation find(Animation.Name name) {
        DLink current = getActiveHead();
        
        while (current != null) {
            Animation animation = (Animation) current;
            if (animation.getName() == name) {
                return animation;
            }
            current = current.getNext();
        }
        
        LOG.warn("Animation not found: " + name);
        return null;
    }
    
    /**
     * Remove an animation from the manager
     * 
     * @param animation Animation to remove
     */
    public void remove(Animation animation) {
        if (animation != null) {
            animation.stop();
            animation.destroy();
            returnToPool(animation);
            LOG.debug("Removed animation: " + animation.getName());
        }
    }
    
    /**
     * Stop all active animations
     */
    public void stopAll() {
        DLink current = getActiveHead();
        
        while (current != null) {
            Animation animation = (Animation) current;
            animation.stop();
            current = current.getNext();
        }
        
        LOG.debug("Stopped all animations");
    }
    
    /**
     * Create a new animation node for the pool
     * 
     * @return New Animation instance
     */
    @Override
    protected DLink createNode() {
        return new Animation();
    }
    
    /**
     * Print manager statistics (for debugging)
     */
    public void printStats() {
        LOG.info("========== ANIMATION MANAGER ==========");
        LOG.info(getStats());
        LOG.info("=======================================");
    }
}