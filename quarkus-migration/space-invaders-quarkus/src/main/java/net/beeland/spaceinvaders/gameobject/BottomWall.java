package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * BottomWall - Bottom boundary wall of the play area
 * Prevents bombs from traveling off the bottom of the screen
 * Also triggers game over if aliens reach this wall
 */
public class BottomWall extends Wall {
    
    /**
     * Constructor for BottomWall
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    public BottomWall(GameObjectName name, float posX, float posY, float width, float height) {
        super(name, WallType.BOTTOM, posX, posY, width, height);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("BottomWall[name=%s, pos=(%.2f, %.2f)]",
                getName(), getX(), getY());
    }
}