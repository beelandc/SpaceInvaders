package net.beeland.spaceinvaders.gameobject;

/**
 * Alien - Abstract base class for all alien types
 * Aliens move in formation and can be destroyed for points
 */
public abstract class Alien extends Leaf {
    
    /**
     * Alien type enumeration
     */
    public enum Type {
        SQUID,
        CRAB,
        JELLYFISH,
        FLYING_SAUCER
    }
    
    /**
     * Constructor
     *
     * @param name GameObject name
     */
    protected Alien(GameObject.GameObjectName name) {
        super(name);
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
    
    /**
     * Get the point value for destroying this alien
     * 
     * @return Point value
     */
    public abstract int getPoints();
    
    /**
     * Get the alien type
     * 
     * @return Alien type
     */
    public abstract Type getType();
    
    
    @Override
    public void update() {
        super.update();
    }
    
    @Override
    public void dump() {
        System.out.println("Alien:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Type: " + getType());
        System.out.println("  Position: (" + this.x + ", " + this.y + ")");
        System.out.println("  Points: " + getPoints());
    }
}