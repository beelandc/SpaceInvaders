package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * Wall - Abstract base class for boundary walls
 * Walls define the play area boundaries and interact with game objects
 */
public abstract class Wall extends Leaf {
    
    private WallType wallType;
    
    /**
     * Enum defining different wall types
     */
    public enum WallType {
        WALL_GROUP,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        LEFT_BUMPER,
        RIGHT_BUMPER,
        UNINITIALIZED
    }
    
    /**
     * Constructor for Wall
     * 
     * @param name GameObject name
     * @param wallType Type of wall
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    protected Wall(GameObjectName name, WallType wallType, float posX, float posY, float width, float height) {
        super(name);
        this.wallType = wallType;
        setX(posX);
        setY(posY);
        
        // Set collision rectangle dimensions
        if (getCollisionObject() != null && getCollisionObject().getCollisionRect() != null) {
            getCollisionObject().getCollisionRect().set(posX, posY, width, height);
        }
    }
    
    /**
     * Get the wall type
     * 
     * @return WallType of this wall
     */
    public WallType getWallType() {
        return this.wallType;
    }
    
    /**
     * Set the wall type
     * 
     * @param wallType New wall type
     */
    public void setWallType(WallType wallType) {
        this.wallType = wallType;
    }
    
    @Override
    public void move(float xDelta, float yDelta) {
        // Walls are static - no movement
        // Position changes only through explicit setX/setY calls
    }
    
    @Override
    public void update() {
        // Walls are static - no update logic needed
        super.update();
    }
    
    @Override
    public String toString() {
        return String.format("Wall[name=%s, type=%s, pos=(%.2f, %.2f)]",
                getName(), wallType, getX(), getY());
    }
}