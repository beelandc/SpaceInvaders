package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.gameobject.CoreCannon;
import net.beeland.spaceinvaders.gameobject.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PlayerDeathObserver - Handles player death when hit by a bomb
 * 
 * When the player's ship is hit by a bomb, this observer:
 * 1. Marks the player ship for death
 * 2. Decrements player lives
 * 3. Triggers respawn or game over
 * 4. Plays explosion sound/animation
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class PlayerDeathObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(PlayerDeathObserver.class);
    
    private CoreCannon coreCannon;
    
    /**
     * Default constructor
     */
    public PlayerDeathObserver() {
        super();
        this.coreCannon = null;
    }
    
    /**
     * Copy constructor for delayed execution
     * 
     * @param other The observer to copy
     */
    public PlayerDeathObserver(PlayerDeathObserver other) {
        super();
        this.coreCannon = other.coreCannon;
    }
    
    /**
     * Called when a collision is detected
     * Marks player for death and triggers respawn logic
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Get the core cannon from the collision
        if (subject.getObjA() instanceof CoreCannon) {
            this.coreCannon = (CoreCannon) subject.getObjA();
        } else if (subject.getObjB() instanceof CoreCannon) {
            this.coreCannon = (CoreCannon) subject.getObjB();
        }
        
        if (coreCannon == null) {
            logger.warn("PlayerDeathObserver: No CoreCannon found in collision");
            return;
        }
        
        // Disable collision by setting collision rect to zero
        if (coreCannon.getCollisionObject() != null) {
            coreCannon.getCollisionObject().getCollisionRect().set(0, 0, 0, 0);
            coreCannon.update();
        }
        
        // Mark for death if not already marked
        if (!coreCannon.isMarkedForDeath()) {
            coreCannon.markForDeath();
            logger.info("Player ship destroyed!");
            
            // TODO: Decrement player lives through PlayState
            // TODO: Trigger explosion animation/sound
            // TODO: Schedule respawn or game over
            
            // Schedule delayed removal
            // TODO: Use DelayedObjectManager when implemented
            // For now, execute immediately
            executeDelayed();
        }
    }
    
    /**
     * Execute delayed removal of the player ship
     * Handles respawn or game over logic
     */
    @Override
    public void executeDelayed() {
        if (coreCannon == null) {
            return;
        }
        
        // TODO: Remove from sprite batches when sprite batch system is integrated
        // coreCannon.remove(SpriteBatch.Name.CoreCannon, SpriteBatch.Name.CollisionBox);
        
        logger.info("Player ship removed from game");
        
        // TODO: Decrement lives and check for game over
        // This needs to be done through the PlayState
        // PlayState should have methods like:
        // - decrementLives()
        // - respawnPlayer()
        // - triggerGameOver()
        
        coreCannon = null;
    }
    
    @Override
    public void dump() {
        System.out.println("PlayerDeathObserver:");
        if (coreCannon != null) {
            System.out.println("  CoreCannon: " + coreCannon.getName());
        }
    }
}