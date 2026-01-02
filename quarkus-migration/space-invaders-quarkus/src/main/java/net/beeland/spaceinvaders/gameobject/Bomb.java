package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.strategy.FallStrategy;

/**
 * Bomb - Projectile dropped by aliens
 * Uses Strategy pattern for different falling behaviors
 */
public class Bomb extends Leaf {
    
    private float delta;
    private FallStrategy strategy;
    private float scaleX;
    private float scaleY;
    
    /**
     * Constructor
     * 
     * @param name GameObject name
     * @param strategy Fall strategy for this bomb
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public Bomb(GameObject.GameObjectName name, FallStrategy strategy, float posX, float posY) {
        super(name);
        this.x = posX;
        this.y = posY;
        this.delta = 4.0f; // Downward speed
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
        this.strategy.reset(this.y);
    }
    
    @Override
    public void move(float deltaX, float deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor other) {
        other.visit(this);
    }
    
    @Override
    public void update() {
        super.update();
        
        // Move bomb downward (negative Y is down in screen coordinates)
        this.y -= this.delta;
        
        // Apply strategy pattern for special movement
        this.strategy.fall(this);
    }
    
    /**
     * Reset the bomb to off-screen position
     */
    public void reset() {
        this.y = 700.0f; // Off-screen at top
        this.strategy.reset(this.y);
    }
    
    /**
     * Get the bounding box height
     * For now, returns a default value since collision system isn't fully implemented
     * 
     * @return Bounding box height
     */
    public float getBoundingBoxHeight() {
        // TODO: Get actual height from collision object when collision system is complete
        return 16.0f; // Default bomb height
    }
    
    /**
     * Multiply the scale factors
     * Used by strategies to flip the bomb
     * 
     * @param sx X scale multiplier
     * @param sy Y scale multiplier
     */
    public void multiplyScale(float sx, float sy) {
        this.scaleX *= sx;
        this.scaleY *= sy;
    }
    
    /**
     * Get X scale
     * 
     * @return X scale factor
     */
    public float getScaleX() {
        return this.scaleX;
    }
    
    /**
     * Get Y scale
     * 
     * @return Y scale factor
     */
    public float getScaleY() {
        return this.scaleY;
    }
    
    /**
     * Set X scale
     * 
     * @param scaleX New X scale factor
     */
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }
    
    /**
     * Set Y scale
     * 
     * @param scaleY New Y scale factor
     */
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }
    
    /**
     * Get the fall speed delta
     * 
     * @return Delta value
     */
    public float getDelta() {
        return this.delta;
    }
    
    /**
     * Set the fall speed delta
     * 
     * @param delta New delta value
     */
    public void setDelta(float delta) {
        this.delta = delta;
    }
    
    /**
     * Get the fall strategy
     * 
     * @return Current fall strategy
     */
    public FallStrategy getStrategy() {
        return this.strategy;
    }
    
    /**
     * Set a new fall strategy
     * 
     * @param strategy New fall strategy
     */
    public void setStrategy(FallStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
        this.strategy.reset(this.y);
    }
    
    @Override
    public void dump() {
        System.out.println("Bomb:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Position: (" + this.x + ", " + this.y + ")");
        System.out.println("  Delta: " + this.delta);
        System.out.println("  Scale: (" + this.scaleX + ", " + this.scaleY + ")");
        System.out.println("  Strategy: " + this.strategy.getClass().getSimpleName());
    }
}