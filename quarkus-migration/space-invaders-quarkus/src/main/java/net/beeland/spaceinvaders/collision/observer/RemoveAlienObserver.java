package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.composite.Component;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.composite.ForwardIterator;
import net.beeland.spaceinvaders.gameobject.Alien;
import net.beeland.spaceinvaders.gameobject.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RemoveAlienObserver - Handles alien removal after being hit
 * 
 * When an alien is hit by a missile, this observer:
 * 1. Marks the alien for death
 * 2. Removes it from its column in the alien grid
 * 3. Removes empty columns from the grid
 * 4. Increases alien grid speed as aliens are destroyed
 * 5. Triggers level reset when all aliens are destroyed
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class RemoveAlienObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveAlienObserver.class);
    private static final float SPEED_INCREMENT = 0.013f;
    
    private Alien alien;
    private GameObject columnComposite;
    
    /**
     * Default constructor
     */
    public RemoveAlienObserver() {
        super();
        this.alien = null;
        this.columnComposite = null;
    }
    
    /**
     * Copy constructor for delayed execution
     * 
     * @param other The observer to copy
     */
    public RemoveAlienObserver(RemoveAlienObserver other) {
        super();
        this.alien = other.alien;
        this.columnComposite = other.columnComposite;
    }
    
    /**
     * Called when a collision is detected
     * Marks alien for death and schedules cleanup
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Get the alien from the collision
        if (subject.getObjA() instanceof Alien) {
            this.alien = (Alien) subject.getObjA();
        } else {
            logger.warn("RemoveAlienObserver: Object A is not an Alien");
            return;
        }
        
        // Disable collision by setting collision rect to zero
        if (alien.getCollisionObject() != null) {
            alien.getCollisionObject().getCollisionRect().set(0, 0, 0, 0);
            alien.update();
        }
        
        // Mark for death if not already marked
        if (!alien.isMarkedForDeath()) {
            alien.markForDeath();
            logger.debug("Alien marked for death: {}", alien.getName());
            
            // Remove alien from its column composite
            removeAlienFromGrid();
            
            // Schedule delayed removal
            // TODO: Use DelayedObjectManager when implemented
            // For now, execute immediately
            executeDelayed();
        }
    }
    
    /**
     * Remove the alien from the alien grid composite structure
     */
    private void removeAlienFromGrid() {
        if (alien == null) {
            return;
        }
        
        // Note: In the state-based architecture, we need to access the alien grid
        // from the current game state. This is a simplified version.
        // The actual implementation will need to get the alien grid from PlayState
        
        // Remove alien from its parent column
        Component parent = alien.getParent();
        if (parent != null) {
            parent.remove(alien);
            logger.debug("Removed alien from column");
            
            // Check if column is now empty
            if (parent.getFirstChild() == null) {
                // Mark empty column for removal
                if (parent instanceof GameObject) {
                    GameObject columnGameObject = (GameObject) parent;
                    if (!columnGameObject.isMarkedForDeath()) {
                        columnGameObject.markForDeath();
                        this.columnComposite = columnGameObject;
                        logger.debug("Column is empty, marked for death");
                        
                        // Remove column from grid
                        Component gridParent = parent.getParent();
                        if (gridParent != null) {
                            gridParent.remove(parent);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Execute delayed removal of the alien
     * Also handles speed increase and level reset check
     */
    @Override
    public void executeDelayed() {
        if (alien == null) {
            return;
        }
        
        // TODO: Remove from sprite batches when sprite batch system is integrated
        // alien.remove(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox);
        
        logger.debug("Alien removed: {}", alien.getName());
        
        // Remove empty column if needed
        if (columnComposite != null) {
            // TODO: Remove from sprite batches
            // columnComposite.remove(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox);
            logger.debug("Empty column removed");
        }
        
        // TODO: Increase alien grid speed
        // This will need to be done through the PlayState
        // PlayState should have a method to increase speed
        
        // TODO: Check if all aliens are destroyed
        // If alien grid is empty, trigger level reset
        // This will need to be done through the PlayState
        
        alien = null;
        columnComposite = null;
    }
    
    @Override
    public void dump() {
        System.out.println("RemoveAlienObserver:");
        if (alien != null) {
            System.out.println("  Alien: " + alien.getName());
        }
        if (columnComposite != null) {
            System.out.println("  Column: " + columnComposite.getName());
        }
    }
}