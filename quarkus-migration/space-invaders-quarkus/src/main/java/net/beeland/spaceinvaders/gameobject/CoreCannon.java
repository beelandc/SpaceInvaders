package net.beeland.spaceinvaders.gameobject;

/**
 * CoreCannon - The player's ship
 * Can move left/right and fire missiles
 */
public class CoreCannon extends Leaf {
    
    private float speed;
    private float minX;
    private float maxX;
    
    /**
     * Constructor
     *
     * @param name GameObject name
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public CoreCannon(GameObject.GameObjectName name, float posX, float posY) {
        super(name);
        this.x = posX;
        this.y = posY;
        this.speed = 3.0f;
        this.minX = 50.0f;  // Left boundary
        this.maxX = 750.0f; // Right boundary
    }
    
    @Override
    public void move(float deltaX, float deltaY) {
        this.x += deltaX;
        this.y += deltaY;
        
        // Clamp to boundaries
        if (this.x < this.minX) {
            this.x = this.minX;
        } else if (this.x > this.maxX) {
            this.x = this.maxX;
        }
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor other) {
        other.visit(this);
    }
    
    @Override
    public void update() {
        super.update();
    }
    
    /**
     * Move the cannon to the right
     */
    public void moveRight() {
        this.x += this.speed;
        
        // Clamp to right boundary
        if (this.x > this.maxX) {
            this.x = this.maxX;
        }
    }
    
    /**
     * Move the cannon to the left
     */
    public void moveLeft() {
        this.x -= this.speed;
        
        // Clamp to left boundary
        if (this.x < this.minX) {
            this.x = this.minX;
        }
    }
    
    /**
     * Get the cannon's movement speed
     * 
     * @return Speed value
     */
    public float getSpeed() {
        return this.speed;
    }
    
    /**
     * Set the cannon's movement speed
     * 
     * @param speed New speed value
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    /**
     * Set the movement boundaries
     * 
     * @param minX Left boundary
     * @param maxX Right boundary
     */
    public void setBoundaries(float minX, float maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }
    
    /**
     * Get the left boundary
     * 
     * @return Minimum X value
     */
    public float getMinX() {
        return this.minX;
    }
    
    /**
     * Get the right boundary
     * 
     * @return Maximum X value
     */
    public float getMaxX() {
        return this.maxX;
    }
    
    /**
     * Check if cannon can move right
     * 
     * @return true if not at right boundary
     */
    public boolean canMoveRight() {
        return this.x < this.maxX;
    }
    
    /**
     * Check if cannon can move left
     * 
     * @return true if not at left boundary
     */
    public boolean canMoveLeft() {
        return this.x > this.minX;
    }
    
    /**
     * Reset cannon to starting position
     * 
     * @param posX Starting X position
     * @param posY Starting Y position
     */
    public void reset(float posX, float posY) {
        this.x = posX;
        this.y = posY;
    }
    
    @Override
    public void dump() {
        System.out.println("CoreCannon:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Position: (" + this.x + ", " + this.y + ")");
        System.out.println("  Speed: " + this.speed);
        System.out.println("  Boundaries: [" + this.minX + ", " + this.maxX + "]");
    }
}