package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;
import net.beeland.spaceinvaders.composite.Composite;

/**
 * WallGroup - Composite representing all boundary walls
 * Manages all walls (left, right, top, bottom) and bumpers
 */
public class WallGroup extends Composite {
    
    /**
     * Constructor for WallGroup
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     */
    public WallGroup(GameObjectName name, float posX, float posY) {
        super(name);
        setX(posX);
        setY(posY);
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor visitor) {
        // Visitor pattern for collision detection
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return String.format("WallGroup[name=%s, pos=(%.2f, %.2f), children=%d]",
                getName(), getX(), getY(), getNumChildren());
    }
}