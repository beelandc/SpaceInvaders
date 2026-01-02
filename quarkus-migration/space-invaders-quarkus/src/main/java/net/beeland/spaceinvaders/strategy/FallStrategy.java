package net.beeland.spaceinvaders.strategy;

import net.beeland.spaceinvaders.gameobject.Bomb;

/**
 * FallStrategy - Strategy pattern interface for bomb falling behavior
 * Different strategies create different bomb movement patterns
 */
public interface FallStrategy {
    
    /**
     * Apply the fall strategy to the bomb
     * 
     * @param bomb The bomb to apply the strategy to
     */
    void fall(Bomb bomb);
    
    /**
     * Reset the strategy state
     * 
     * @param posY Initial Y position
     */
    void reset(float posY);
}