package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;
import net.beeland.spaceinvaders.composite.Composite;

/**
 * ShieldRoot - Composite representing a single shield structure
 * Contains multiple ShieldColumn objects that form the shield shape
 */
public class ShieldRoot extends Composite {
    
    /**
     * Constructor for ShieldRoot
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     */
    public ShieldRoot(GameObjectName name, float posX, float posY) {
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
        return String.format("ShieldRoot[name=%s, pos=(%.2f, %.2f), children=%d]",
                getName(), getX(), getY(), getNumChildren());
    }
}