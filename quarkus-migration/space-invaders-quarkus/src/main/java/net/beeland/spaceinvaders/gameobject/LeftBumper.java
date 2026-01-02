package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;

/**
 * LeftBumper - Left boundary for player cannon movement
 * Prevents the player's cannon from moving off the left side
 */
public class LeftBumper extends Wall {
    
    /**
     * Constructor for LeftBumper
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     * @param width Wall width
     * @param height Wall height
     */
    public LeftBumper(GameObjectName name, float posX, float posY, float width, float height) {
        super(name, WallType.LEFT_BUMPER, posX, posY, width, height);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("LeftBumper[name=%s, pos=(%.2f, %.2f)]",
                getName(), getX(), getY());
    }
}