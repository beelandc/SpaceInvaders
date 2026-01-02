package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.composite.Component;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.ShieldBrick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RemoveShieldBrickObserver - Handles shield brick removal after collision
 * 
 * When a shield brick is hit (by missile, bomb, or alien), this observer:
 * 1. Marks the shield brick for death
 * 2. Removes it from its column in the shield
 * 3. Removes empty columns from the shield
 * 4. Removes empty shields from the shield group
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class RemoveShieldBrickObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveShieldBrickObserver.class);
    
    private ShieldBrick shieldBrick;
    private GameObject columnComposite;
    private GameObject shieldComposite;
    
    /**
     * Default constructor
     */
    public RemoveShieldBrickObserver() {
        super();
        this.shieldBrick = null;
        this.columnComposite = null;
        this.shieldComposite = null;
    }
    
    /**
     * Copy constructor for delayed execution
     * 
     * @param other The observer to copy
     */
    public RemoveShieldBrickObserver(RemoveShieldBrickObserver other) {
        super();
        this.shieldBrick = other.shieldBrick;
        this.columnComposite = other.columnComposite;
        this.shieldComposite = other.shieldComposite;
    }
    
    /**
     * Called when a collision is detected
     * Marks shield brick for death and schedules cleanup
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Get the shield brick from the collision
        if (subject.getObjA() instanceof ShieldBrick) {
            this.shieldBrick = (ShieldBrick) subject.getObjA();
        } else {
            logger.warn("RemoveShieldBrickObserver: Object A is not a ShieldBrick");
            return;
        }
        
        // Disable collision by setting collision rect to zero
        if (shieldBrick.getCollisionObject() != null) {
            shieldBrick.getCollisionObject().getCollisionRect().set(0, 0, 0, 0);
            shieldBrick.update();
        }
        
        // Mark for death if not already marked
        if (!shieldBrick.isMarkedForDeath()) {
            shieldBrick.markForDeath();
            logger.debug("Shield brick marked for death: {}", shieldBrick.getName());
            
            // Remove shield brick from its column composite
            removeShieldBrickFromStructure();
            
            // Schedule delayed removal
            // TODO: Use DelayedObjectManager when implemented
            // For now, execute immediately
            executeDelayed();
        }
    }
    
    /**
     * Remove the shield brick from the shield composite structure
     */
    private void removeShieldBrickFromStructure() {
        if (shieldBrick == null) {
            return;
        }
        
        // Remove shield brick from its parent column
        Component columnParent = shieldBrick.getParent();
        if (columnParent != null) {
            columnParent.remove(shieldBrick);
            logger.debug("Removed shield brick from column");
            
            // Check if column is now empty
            if (columnParent.getFirstChild() == null) {
                // Mark empty column for removal
                if (columnParent instanceof GameObject) {
                    GameObject columnGameObject = (GameObject) columnParent;
                    if (!columnGameObject.isMarkedForDeath()) {
                        columnGameObject.markForDeath();
                        this.columnComposite = columnGameObject;
                        logger.debug("Shield column is empty, marked for death");
                        
                        // Get the shield parent
                        Component shieldParent = columnParent.getParent();
                        if (shieldParent != null) {
                            // Remove column from shield
                            shieldParent.remove(columnParent);
                            
                            // Check if shield is now empty
                            if (shieldParent.getFirstChild() == null) {
                                if (shieldParent instanceof GameObject) {
                                    GameObject shieldGameObject = (GameObject) shieldParent;
                                    if (!shieldGameObject.isMarkedForDeath()) {
                                        shieldGameObject.markForDeath();
                                        this.shieldComposite = shieldGameObject;
                                        logger.debug("Shield is empty, marked for death");
                                        
                                        // Remove shield from shield group
                                        Component shieldGroupParent = shieldParent.getParent();
                                        if (shieldGroupParent != null) {
                                            shieldGroupParent.remove(shieldParent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Execute delayed removal of the shield brick
     */
    @Override
    public void executeDelayed() {
        if (shieldBrick == null) {
            return;
        }
        
        // TODO: Remove from sprite batches when sprite batch system is integrated
        // shieldBrick.remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
        
        logger.debug("Shield brick removed: {}", shieldBrick.getName());
        
        // Remove empty column if needed
        if (columnComposite != null) {
            // TODO: Remove from sprite batches
            // columnComposite.remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
            logger.debug("Empty shield column removed");
        }
        
        // Remove empty shield if needed
        if (shieldComposite != null) {
            // TODO: Remove from sprite batches
            // shieldComposite.remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
            logger.debug("Empty shield removed");
        }
        
        shieldBrick = null;
        columnComposite = null;
        shieldComposite = null;
    }
    
    @Override
    public void dump() {
        System.out.println("RemoveShieldBrickObserver:");
        if (shieldBrick != null) {
            System.out.println("  Shield Brick: " + shieldBrick.getName());
        }
        if (columnComposite != null) {
            System.out.println("  Column: " + columnComposite.getName());
        }
        if (shieldComposite != null) {
            System.out.println("  Shield: " + shieldComposite.getName());
        }
    }
}