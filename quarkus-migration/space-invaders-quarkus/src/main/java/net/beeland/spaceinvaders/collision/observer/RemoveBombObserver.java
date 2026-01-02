package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.Bomb;
import net.beeland.spaceinvaders.gameobject.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RemoveBombObserver - Handles bomb removal after collision
 * 
 * When a bomb hits something (shield, wall, missile, player), this observer:
 * 1. Marks the bomb for death
 * 2. Disables its collision
 * 3. Removes it from the bomb root
 * 4. Schedules the next bomb drop
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class RemoveBombObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveBombObserver.class);
    
    private Bomb bomb;
    
    /**
     * Default constructor
     */
    public RemoveBombObserver() {
        super();
        this.bomb = null;
    }
    
    /**
     * Copy constructor for delayed execution
     * 
     * @param other The observer to copy
     */
    public RemoveBombObserver(RemoveBombObserver other) {
        super();
        this.bomb = other.bomb;
    }
    
    /**
     * Called when a collision is detected
     * Marks bomb for death and schedules cleanup
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Determine which object is the bomb
        if (subject.getObjA() instanceof Bomb) {
            this.bomb = (Bomb) subject.getObjA();
        } else if (subject.getObjB() instanceof Bomb) {
            this.bomb = (Bomb) subject.getObjB();
        }
        
        if (bomb == null) {
            logger.warn("RemoveBombObserver: No bomb found in collision");
            return;
        }
        
        // Disable collision by setting collision rect to zero
        if (bomb.getCollisionObject() != null) {
            bomb.getCollisionObject().getCollisionRect().set(0, 0, 0, 0);
            bomb.update();
        }
        
        // Mark for death if not already marked
        if (!bomb.isMarkedForDeath()) {
            bomb.markForDeath();
            logger.debug("Bomb marked for death: {}", bomb.getName());
            
            // Schedule delayed removal
            // TODO: Use DelayedObjectManager when implemented
            // For now, execute immediately
            executeDelayed();
        }
    }
    
    /**
     * Execute delayed removal of the bomb
     */
    @Override
    public void executeDelayed() {
        if (bomb == null) {
            return;
        }
        
        // TODO: Remove from sprite batches when sprite batch system is integrated
        // bomb.remove(SpriteBatch.Name.Bomb, SpriteBatch.Name.CollisionBox);
        
        logger.debug("Bomb removed: {}", bomb.getName());
        
        // TODO: Schedule next bomb drop
        // This will need to be done through the PlayState or a BombManager
        // The timing depends on whether it's a regular bomb or flying saucer bomb
        
        bomb = null;
    }
    
    @Override
    public void dump() {
        System.out.println("RemoveBombObserver:");
        if (bomb != null) {
            System.out.println("  Bomb: " + bomb.getName());
        }
    }
}