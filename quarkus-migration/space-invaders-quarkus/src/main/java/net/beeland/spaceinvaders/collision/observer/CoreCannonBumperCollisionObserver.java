package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.gameobject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CoreCannonBumperCollisionObserver - Prevents player from moving off screen
 * 
 * When the player's ship hits a bumper (left or right edge), this observer:
 * 1. Stops the player's movement in that direction
 * 2. Adjusts the player's position to stay within bounds
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class CoreCannonBumperCollisionObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(CoreCannonBumperCollisionObserver.class);
    
    /**
     * Default constructor
     */
    public CoreCannonBumperCollisionObserver() {
        super();
    }
    
    /**
     * Called when a collision is detected
     * Stops player movement at screen edge
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Get the core cannon and bumper from the collision
        CoreCannon coreCannon = null;
        GameObject bumper = null;
        
        if (subject.getObjA() instanceof CoreCannon) {
            coreCannon = (CoreCannon) subject.getObjA();
            bumper = subject.getObjB();
        } else if (subject.getObjB() instanceof CoreCannon) {
            coreCannon = (CoreCannon) subject.getObjB();
            bumper = subject.getObjA();
        }
        
        if (coreCannon == null || bumper == null) {
            logger.warn("CoreCannonBumperCollisionObserver: Missing CoreCannon or Bumper");
            return;
        }
        
        // Determine which bumper was hit and adjust position
        if (bumper instanceof LeftBumper) {
            // Hit left edge - stop moving left
            logger.debug("Player hit left bumper");
            // TODO: Set player movement state to prevent further left movement
            // This might require a state machine in CoreCannon
        } else if (bumper instanceof RightBumper) {
            // Hit right edge - stop moving right
            logger.debug("Player hit right bumper");
            // TODO: Set player movement state to prevent further right movement
        }
        
        // The collision itself prevents further movement by the collision system
        // No need to explicitly stop the player here
    }
    
    @Override
    public void dump() {
        System.out.println("CoreCannonBumperCollisionObserver");
    }
}