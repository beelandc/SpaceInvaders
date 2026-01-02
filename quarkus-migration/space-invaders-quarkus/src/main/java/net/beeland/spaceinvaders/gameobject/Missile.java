package net.beeland.spaceinvaders.gameobject;

/**
 * Missile - Projectile fired by the player's CoreCannon
 * Moves upward on the screen to destroy aliens
 */
public class Missile extends Leaf {
    
    private boolean active;
    private float deltaY;
    
    /**
     * Constructor
     *
     * @param name GameObject name
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public Missile(GameObject.GameObjectName name, float posX, float posY) {
        super(name);
        this.x = posX;
        this.y = posY;
        this.active = false;
        this.deltaY = 10.0f; // Moves upward (positive Y)
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
        if (this.active) {
            this.y += this.deltaY;
        }
    }
    
    /**
     * Set the missile active/inactive state
     * 
     * @param state true to activate, false to deactivate
     */
    public void setActive(boolean state) {
        this.active = state;
    }
    
    /**
     * Check if missile is active
     * 
     * @return true if active
     */
    public boolean isActive() {
        return this.active;
    }
    
    /**
     * Get the vertical movement delta
     * 
     * @return Delta Y value
     */
    public float getDeltaY() {
        return this.deltaY;
    }
    
    /**
     * Set the vertical movement delta
     * 
     * @param delta New delta Y value
     */
    public void setDeltaY(float delta) {
        this.deltaY = delta;
    }
    
    /**
     * Launch the missile from a specific position
     * 
     * @param posX Launch X position
     * @param posY Launch Y position
     */
    public void launch(float posX, float posY) {
        this.x = posX;
        this.y = posY;
        this.active = true;
    }
    
    /**
     * Reset the missile (deactivate and move off-screen)
     */
    public void reset() {
        this.active = false;
        this.y = -100.0f; // Move off-screen
    }
    
    @Override
    public void dump() {
        System.out.println("Missile:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Position: (" + this.x + ", " + this.y + ")");
        System.out.println("  Active: " + this.active);
        System.out.println("  Delta Y: " + this.deltaY);
    }
}