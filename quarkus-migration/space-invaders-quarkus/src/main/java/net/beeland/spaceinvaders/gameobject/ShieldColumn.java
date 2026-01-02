package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionVisitor;
import net.beeland.spaceinvaders.composite.Composite;

/**
 * ShieldColumn - Composite representing a vertical column of shield bricks
 * Part of the shield hierarchy: ShieldRoot -> ShieldColumn -> ShieldBrick
 */
public class ShieldColumn extends Composite {
    
    /**
     * Constructor for ShieldColumn
     * 
     * @param name GameObject name
     * @param posX X position
     * @param posY Y position
     */
    public ShieldColumn(GameObjectName name, float posX, float posY) {
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
        return String.format("ShieldColumn[name=%s, pos=(%.2f, %.2f), children=%d]",
                getName(), getX(), getY(), getNumChildren());
    }
}