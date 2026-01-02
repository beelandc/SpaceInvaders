package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.gameobject.AlienGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GridWallCollisionObserver - Handles alien grid collision with walls
 * 
 * When the alien grid hits a wall (left or right), this observer:
 * 1. Reverses the grid's horizontal direction
 * 2. Moves the grid down one row
 * 3. Checks if aliens have reached the bottom (game over condition)
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class GridWallCollisionObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(GridWallCollisionObserver.class);
    private static final float GRID_DROP_DISTANCE = 20.0f;
    
    /**
     * Default constructor
     */
    public GridWallCollisionObserver() {
        super();
    }
    
    /**
     * Called when a collision is detected
     * Reverses grid direction and moves it down
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Get the alien grid from the collision
        AlienGrid alienGrid = null;
        if (subject.getObjA() instanceof AlienGrid) {
            alienGrid = (AlienGrid) subject.getObjA();
        } else if (subject.getObjB() instanceof AlienGrid) {
            alienGrid = (AlienGrid) subject.getObjB();
        }
        
        if (alienGrid == null) {
            logger.warn("GridWallCollisionObserver: No alien grid found in collision");
            return;
        }
        
        // Trigger the grid's direction switch mechanism
        // The AlienGrid handles the direction reversal and downward movement internally
        alienGrid.setSwitchXDirection(true);
        logger.debug("Alien grid direction switch triggered");
        
        // TODO: Check if aliens have reached the bottom (game over condition)
        // This should trigger a game over if the grid is too low
        // The exact threshold depends on the game design
        float currentY = alienGrid.getY();
        if (currentY < 150.0f) {
            logger.warn("Alien grid has reached critical low position - potential game over");
            // TODO: Trigger game over through PlayState
        }
    }
    
    @Override
    public void dump() {
        System.out.println("GridWallCollisionObserver");
    }
}