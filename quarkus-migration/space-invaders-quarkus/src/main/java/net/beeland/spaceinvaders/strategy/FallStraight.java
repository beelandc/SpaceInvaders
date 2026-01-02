package net.beeland.spaceinvaders.strategy;

import net.beeland.spaceinvaders.gameobject.Bomb;

/**
 * FallStraight - Bomb falls straight down without any horizontal movement
 */
public class FallStraight implements FallStrategy {
    
    private float oldPosY;
    
    /**
     * Constructor
     */
    public FallStraight() {
        this.oldPosY = 0.0f;
    }
    
    @Override
    public void reset(float posY) {
        this.oldPosY = posY;
    }
    
    @Override
    public void fall(Bomb bomb) {
        // Do nothing - bomb falls straight down naturally
        // No horizontal movement or scale changes
    }
    
    /**
     * Get the old Y position
     * 
     * @return Old Y position
     */
    public float getOldPosY() {
        return this.oldPosY;
    }
}