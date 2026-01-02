package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;
import net.beeland.spaceinvaders.composite.Composite;

/**
 * ShieldGroup - Composite representing a group of shields
 * Manages multiple ShieldRoot objects (typically 4 shields in Space Invaders)
 */
public class ShieldGroup extends Composite {
    
    /**
     * Constructor for ShieldGroup
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     */
    public ShieldGroup(GameObjectName name, float posX, float posY) {
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
        return String.format("ShieldGroup[name=%s, pos=(%.2f, %.2f), children=%d]",
                getName(), getX(), getY(), getNumChildren());
    }
}