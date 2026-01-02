package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * TopWall - Top boundary wall of the play area
 * Prevents missiles from traveling off the top of the screen
 */
public class TopWall extends Wall {
    
    /**
     * Constructor for TopWall
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    public TopWall(GameObjectName name, float posX, float posY, float width, float height) {
        super(name, WallType.TOP, posX, posY, width, height);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("TopWall[name=%s, pos=(%.2f, %.2f)]",
                getName(), getX(), getY());
    }
}