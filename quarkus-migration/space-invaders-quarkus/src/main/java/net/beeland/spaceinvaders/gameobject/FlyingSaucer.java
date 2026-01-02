package net.beeland.spaceinvaders.gameobject;

import java.util.Random;

/**
 * FlyingSaucer - Special alien that appears periodically
 * 
 * The flying saucer moves horizontally across the top of the screen
 * and awards random point values (50, 100, or 150) when destroyed.
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2024-12-23
 */
public class FlyingSaucer extends Alien {
    
    private static final int LOW_POINTS = 50;
    private static final int MEDIUM_POINTS = 100;
    private static final int HIGH_POINTS = 150;
    
    private final Random random;
    
    /**
     * Default constructor
     */
    public FlyingSaucer() {
        super(GameObject.GameObjectName.FLYING_SAUCER);
        this.x = 0.0f;
        this.y = 0.0f;
        this.random = new Random();
    }
    
    /**
     * Constructor
     *
     * @param name GameObject name
     * @param x Initial X position
     * @param y Initial Y position
     */
    public FlyingSaucer(GameObject.GameObjectName name, float x, float y) {
        super(name);
        this.x = x;
        this.y = y;
        this.random = new Random();
    }
    
    /**
     * Get random point value for destroying the flying saucer
     * Returns 50, 100, or 150 points randomly
     * 
     * @return Point value (50, 100, or 150)
     */
    @Override
    public int getPoints() {
        int choice = random.nextInt(3);
        
        switch (choice) {
            case 0:
                return LOW_POINTS;
            case 1:
                return MEDIUM_POINTS;
            case 2:
                return HIGH_POINTS;
            default:
                return LOW_POINTS;
        }
    }
    
    /**
     * Get the alien type
     * 
     * @return FLYING_SAUCER type
     */
    @Override
    public Type getType() {
        return Type.FLYING_SAUCER;
    }
    
    /**
     * Update the flying saucer
     * Calls parent update for sprite animation
     */
    @Override
    public void update() {
        super.update();
    }
    
    /**
     * Accept collision visitor
     *
     * @param visitor Collision visitor
     */
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visitFlyingSaucer(this);
    }
    
    /**
     * Dump flying saucer information for debugging
     */
    @Override
    public void dump() {
        System.out.println("FlyingSaucer:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Position: (" + this.x + ", " + this.y + ")");
        System.out.println("  Point Range: " + LOW_POINTS + "-" + HIGH_POINTS);
    }
}