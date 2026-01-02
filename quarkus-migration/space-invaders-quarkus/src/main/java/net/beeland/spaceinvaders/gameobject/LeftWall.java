package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * LeftWall - Left boundary wall of the play area
 * Prevents aliens from moving off the left side of the screen
 */
public class LeftWall extends Wall {
    
    /**
     * Constructor for LeftWall
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    public LeftWall(GameObjectName name, float posX, float posY, float width, float height) {
        super(name, WallType.LEFT, posX, posY, width, height);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("LeftWall[name=%s, pos=(%.2f, %.2f)]",
                getName(), getX(), getY());
    }
}