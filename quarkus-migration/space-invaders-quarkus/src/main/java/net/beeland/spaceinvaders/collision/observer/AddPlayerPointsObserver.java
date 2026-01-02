package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.gameobject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AddPlayerPointsObserver - Adds points to player score when aliens are destroyed
 * 
 * Point values:
 * - Squid Alien: 30 points
 * - Crab Alien: 20 points
 * - Jellyfish Alien: 10 points
 * - Flying Saucer: 50-300 points (random)
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class AddPlayerPointsObserver extends CollisionObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(AddPlayerPointsObserver.class);
    
    // Point values for each alien type
    private static final int SQUID_POINTS = 30;
    private static final int CRAB_POINTS = 20;
    private static final int JELLYFISH_POINTS = 10;
    private static final int FLYING_SAUCER_MIN_POINTS = 50;
    private static final int FLYING_SAUCER_MAX_POINTS = 300;
    
    /**
     * Default constructor
     */
    public AddPlayerPointsObserver() {
        super();
    }
    
    /**
     * Called when a collision is detected
     * Adds appropriate points based on alien type
     */
    @Override
    public void notifyCollision() {
        if (subject == null) {
            return;
        }
        
        // Determine which object is the alien
        GameObject alien = null;
        if (subject.getObjA() instanceof Alien || subject.getObjA() instanceof FlyingSaucer) {
            alien = subject.getObjA();
        } else if (subject.getObjB() instanceof Alien || subject.getObjB() instanceof FlyingSaucer) {
            alien = subject.getObjB();
        }
        
        if (alien == null) {
            logger.warn("AddPlayerPointsObserver: No alien found in collision");
            return;
        }
        
        // Calculate points based on alien type
        int points = calculatePoints(alien);
        
        if (points > 0) {
            logger.info("Player earned {} points for destroying {}", points, alien.getName());
            
            // TODO: Add points to player score through PlayState
            // PlayState should have a method like addScore(int points)
            // For now, just log the points
        }
    }
    
    /**
     * Calculate points based on alien type
     * 
     * @param alien The alien that was destroyed
     * @return The point value
     */
    private int calculatePoints(GameObject alien) {
        if (alien instanceof SquidAlien) {
            return SQUID_POINTS;
        } else if (alien instanceof CrabAlien) {
            return CRAB_POINTS;
        } else if (alien instanceof JellyfishAlien) {
            return JELLYFISH_POINTS;
        } else if (alien instanceof FlyingSaucer) {
            // Flying saucer gives random points
            return FLYING_SAUCER_MIN_POINTS + 
                   (int)(Math.random() * (FLYING_SAUCER_MAX_POINTS - FLYING_SAUCER_MIN_POINTS + 1));
        }
        
        logger.warn("Unknown alien type: {}", alien.getClass().getSimpleName());
        return 0;
    }
    
    @Override
    public void dump() {
        System.out.println("AddPlayerPointsObserver");
    }
}