package net.beeland.spaceinvaders.command;

import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * FlyingSaucerMovement - Command for moving the flying saucer across the screen
 * Handles horizontal movement with edge detection
 *
 * TODO: Add sound playback when Sound.play() API is implemented
 */
@ApplicationScoped
public class FlyingSaucerMovement extends Command {
    
    @Inject
    GameObjectManager gameObjectManager;
    
    @Inject
    TimerManager timerManager;
    
    private GameObject.GameObjectName gameObjectName;
    private float deltaX;
    private float deltaY;
    private float currX;
    private float currY;
    
    /**
     * Default constructor for CDI
     */
    public FlyingSaucerMovement() {
        this.deltaX = 0.0f;
        this.deltaY = 0.0f;
        this.currX = 0.0f;
        this.currY = 0.0f;
    }
    
    /**
     * Set the movement parameters
     * 
     * @param gameObjectName The game object to move
     * @param deltaX Horizontal movement delta
     * @param deltaY Vertical movement delta
     */
    public void set(GameObject.GameObjectName gameObjectName, float deltaX, float deltaY) {
        this.gameObjectName = gameObjectName;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
    
    /**
     * Update the X delta
     * 
     * @param newDeltaX New horizontal movement delta
     */
    public void updateXDelta(float newDeltaX) {
        this.deltaX = newDeltaX;
    }
    
    /**
     * Update the Y delta
     * 
     * @param newDeltaY New vertical movement delta
     */
    public void updateYDelta(float newDeltaY) {
        this.deltaY = newDeltaY;
    }
    
    @Override
    public void execute(float deltaTime) {
        // Find the flying saucer root composite
        Composite flyingSaucerRoot = (Composite) gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT);
        
        // If flying saucer is active (has children)
        if (flyingSaucerRoot != null && flyingSaucerRoot.getFirstChild() != null) {
            // TODO: Play UFO sound when Sound.play() API is implemented
            
            // Find the actual flying saucer game object
            GameObject flyingSaucer = gameObjectManager.find(this.gameObjectName);
            if (flyingSaucer != null) {
                // Calculate new position
                this.currX = flyingSaucer.getX() + this.deltaX;
                this.currY = flyingSaucer.getY() + this.deltaY;
                
                // Get sprite width for edge detection
                float spriteHalfWidth = 0.0f;
                if (flyingSaucer.getSpriteProxy() != null) {
                    // Assuming sprite width is available from sprite proxy
                    spriteHalfWidth = 16.0f; // Default half-width, adjust as needed
                }
                
                // Check window boundaries (0 to 896 pixels)
                if ((this.currX + spriteHalfWidth) > 896.0f) {
                    // Hit right edge, reverse direction
                    this.deltaX = -this.deltaX;
                    this.currX = 896.0f - spriteHalfWidth;
                } else if ((this.currX - spriteHalfWidth) < 0.0f) {
                    // Hit left edge, reverse direction
                    this.deltaX = -this.deltaX;
                    this.currX = 0.0f + spriteHalfWidth;
                }
                
                // Update position
                flyingSaucer.setX(this.currX);
                flyingSaucer.setY(this.currY);
            }
            
            // Re-schedule this command for continuous movement
            timerManager.add(TimeEvent.Name.SPRITE_MOVEMENT, this, deltaTime);
        }
    }
    
    @Override
    protected void wash() {
        super.wash();
        this.gameObjectName = null;
        this.deltaX = 0.0f;
        this.deltaY = 0.0f;
        this.currX = 0.0f;
        this.currY = 0.0f;
    }
    
    @Override
    public void dump() {
        System.out.println("FlyingSaucerMovement:");
        System.out.println("  GameObject: " + this.gameObjectName);
        System.out.println("  Delta: (" + this.deltaX + ", " + this.deltaY + ")");
        System.out.println("  Current: (" + this.currX + ", " + this.currY + ")");
    }
}