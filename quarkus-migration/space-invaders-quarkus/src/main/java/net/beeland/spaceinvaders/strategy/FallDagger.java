package net.beeland.spaceinvaders.strategy;

import net.beeland.spaceinvaders.gameobject.Bomb;

/**
 * FallDagger - Bomb falls in a dagger pattern by flipping vertically
 * Flips the bomb's Y scale when it reaches certain Y thresholds
 */
public class FallDagger implements FallStrategy {
    
    private float oldPosY;
    
    /**
     * Constructor
     */
    public FallDagger() {
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
        
        // If bomb has passed the target, flip vertically and update old position
        if (bomb.getY() < targetY) {
            bomb.multiplyScale(1.0f, -1.0f);
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