package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.Missile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RemoveMissileObserver - Handles missile removal after collision
 * 
 * When a missile hits something (alien, shield, wall), this observer:
 * 1. Marks the missile for death
 * 2. Disables its collision
 * 3. Removes it from the missile group
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class RemoveMissileObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveMissileObserver.class);
    
    private Missile missile;
    
    /**
     * Default constructor
     */
    public RemoveMissileObserver() {
        super();
        this.missile = null;
    }
    
    /**
     * Copy constructor for delayed execution
     * 
     * @param other The observer to copy
     */
    public RemoveMissileObserver(RemoveMissileObserver other) {
        super();
        this.missile = other.missile;
    }
    
    /**
     * Called when a collision is detected
     * Marks missile for death and schedules cleanup
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Determine which object is the missile
        if (subject.getObjA() instanceof Missile) {
            this.missile = (Missile) subject.getObjA();
        } else if (subject.getObjB() instanceof Missile) {
            this.missile = (Missile) subject.getObjB();
        }
        
        if (missile == null) {
            logger.warn("RemoveMissileObserver: No missile found in collision");
            return;
        }
        
        // Disable collision by setting collision rect to zero
        if (missile.getCollisionObject() != null) {
            missile.getCollisionObject().getCollisionRect().set(0, 0, 0, 0);
            missile.update();
        }
        
        // Mark for death if not already marked
        if (!missile.isMarkedForDeath()) {
            missile.markForDeath();
            logger.debug("Missile marked for death: {}", missile.getName());
            
            // Schedule delayed removal
            // TODO: Use DelayedObjectManager when implemented
            // For now, execute immediately
            executeDelayed();
        }
    }
    
    /**
     * Execute delayed removal of the missile
     */
    @Override
    public void executeDelayed() {
        if (missile == null) {
            return;
        }
        
        // TODO: Remove from sprite batches when sprite batch system is integrated
        // missile.remove(SpriteBatch.Name.Missile, SpriteBatch.Name.CollisionBox);
        
        // Remove from missile group
        // Note: In the state-based architecture, we need to get the missile group from the current state
        // For now, we'll just mark it as removed
        logger.debug("Missile removed: {}", missile.getName());
        
        // The missile will be cleaned up by the game object manager
        missile = null;
    }
    
    @Override
    public void dump() {
        System.out.println("RemoveMissileObserver:");
        if (missile != null) {
            System.out.println("  Missile: " + missile.getName());
        }
    }
}