package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Component;
import net.beeland.spaceinvaders.sprite.SpriteProxy;

/**
 * Leaf represents a game object that cannot have children.
 * This is part of the Composite pattern implementation where Leaf nodes
 * are terminal nodes in the game object hierarchy.
 * 
 * Leaf objects throw exceptions if attempts are made to add or remove children.
 */
public abstract class Leaf extends GameObject {

    /**
     * Creates a leaf game object with the specified name.
     *
     * @param gameName The name identifier for this game object
     */
    protected Leaf(GameObjectName gameName) {
        super(gameName);
        this.containerType = ContainerType.LEAF;
    }

    /**
     * Attempts to add a child component.
     * This operation is not supported for leaf nodes.
     *
     * @param component The component to add
     * @throws UnsupportedOperationException Always thrown as leaves cannot have children
     */
    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException(
                "Cannot add children to a Leaf node: " + this.getName());
    }

    /**
     * Attempts to remove a child component.
     * This operation is not supported for leaf nodes.
     *
     * @param component The component to remove
     * @throws UnsupportedOperationException Always thrown as leaves cannot have children
     */
    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException(
                "Cannot remove children from a Leaf node: " + this.getName());
    }

    /**
     * Gets the first child component.
     * Leaf nodes have no children, so this always returns null.
     *
     * @return null, as leaf nodes have no children
     */
    @Override
    public Component getFirstChild() {
        return null;
    }

    /**
     * Moves this leaf object by the specified delta values.
     * Subclasses must implement this method to define movement behavior.
     *
     * @param xDelta The amount to move in the X direction
     * @param yDelta The amount to move in the Y direction
     */
    @Override
    public abstract void move(float xDelta, float yDelta);

    /**
     * Prints information about this leaf node.
     * Useful for debugging the game object hierarchy.
     */
    public void print() {
        System.out.println(this.toString());
    }
}