package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * RightBumper - Right boundary for player cannon movement
 * Prevents the player's cannon from moving off the right side
 */
public class RightBumper extends Wall {
    
    /**
     * Constructor for RightBumper
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    public RightBumper(GameObjectName name, float posX, float posY, float width, float height) {
        super(name, WallType.RIGHT_BUMPER, posX, posY, width, height);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("RightBumper[name=%s, pos=(%.2f, %.2f)]",
                getName(), getX(), getY());
    }
}