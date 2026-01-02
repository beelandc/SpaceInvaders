package net.beeland.spaceinvaders.strategy;

import net.beeland.spaceinvaders.gameobject.Bomb;

/**
 * FallZigZag - Bomb falls in a zigzag pattern by flipping horizontally
 * Flips the bomb's X scale when it reaches certain Y thresholds
 */
public class FallZigZag implements FallStrategy {
    
    private float oldPosY;
    
    /**
     * Constructor
     */
    public FallZigZag() {
        this.oldPosY = 0.0f;
    }
    
    @Override
    public void reset(float posY) {
        this.oldPosY = posY;
    }
    
    @Override
    public void fall(Bomb bomb) {
        if (bomb == null) {
            return;
        }
        
        // Calculate target Y position (one bounding box height below old position)
        float targetY = oldPosY - 1.0f * bomb.getBoundingBoxHeight();
        
        // If bomb has passed the target, flip horizontally and update old position
        if (bomb.getY() < targetY) {
            bomb.multiplyScale(-1.0f, 1.0f);
            oldPosY = targetY;
        }
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