package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * ShieldBrick - Individual brick component of a shield
 * Extends Leaf to represent a single destructible shield piece
 */
public class ShieldBrick extends Leaf {
    
    private BrickType brickType;
    
    /**
     * Enum defining different brick types for shield construction
     */
    public enum BrickType {
        BRICK,           // Standard brick
        LEFT_TOP_0,      // Left top corner (outer)
        LEFT_TOP_1,      // Left top corner (inner)
        LEFT_BOTTOM,     // Left bottom corner
        RIGHT_TOP_0,     // Right top corner (outer)
        RIGHT_TOP_1,     // Right top corner (inner)
        RIGHT_BOTTOM,    // Right bottom corner
        UNINITIALIZED
    }
    
    /**
     * Constructor for ShieldBrick
     * 
     * @param name GameObject name
     * @param brickType Type of brick (standard or corner piece)
     * @param posX X position
     * @param posY Y position
     */
    public ShieldBrick(GameObjectName name, BrickType brickType, float posX, float posY) {
        super(name);
        this.brickType = brickType;
        setX(posX);
        setY(posY);
    }
    
    /**
     * Get the brick type
     * 
     * @return BrickType of this brick
     */
    public BrickType getBrickType() {
        return this.brickType;
    }
    
    /**
     * Set the brick type
     * 
     * @param brickType New brick type
     */
    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
    }
    
    @Override
    public void update() {
        // Shield bricks are static - no update logic needed
        super.update();
    }
    
    @Override
    public void move(float xDelta, float yDelta) {
        // Shield bricks are static - no movement
        // Position changes only through explicit setX/setY calls
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        // Visitor pattern for collision detection
        // Will be implemented when collision system is added
        visitor.visit(this);
    }
    
    @Override
    protected void wash() {
        super.wash();
        this.brickType = BrickType.UNINITIALIZED;
    }
    
    @Override
    public String toString() {
        return String.format("ShieldBrick[name=%s, type=%s, pos=(%.2f, %.2f)]",
                getName(), brickType, getX(), getY());
    }
}