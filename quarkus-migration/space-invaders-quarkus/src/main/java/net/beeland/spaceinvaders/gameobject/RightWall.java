package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * RightWall - Right boundary wall of the play area
 * Prevents aliens from moving off the right side of the screen
 */
public class RightWall extends Wall {
    
    /**
     * Constructor for RightWall
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    public RightWall(GameObjectName name, float posX, float posY, float width, float height) {
        super(name, WallType.RIGHT, posX, posY, width, height);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("RightWall[name=%s, pos=(%.2f, %.2f)]",
                getName(), getX(), getY());
    }
}