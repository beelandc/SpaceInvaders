package net.beeland.spaceinvaders.gameobject;

/**
 * NullGameObject is a concrete implementation of the Null Object pattern.
 * It provides a safe default object that can be used instead of null references,
 * preventing null pointer exceptions and simplifying code.
 *
 * This object does nothing when its methods are called, making it safe to use
 * as a placeholder or default value.
 */
public class NullGameObject extends Leaf {

    /**
     * Creates a null game object with NULL_OBJECT name.
     */
    public NullGameObject() {
        super(GameObjectName.NULL_OBJECT);
    }

    /**
     * Accepts a collision visitor.
     * This implementation does nothing, as null objects don't participate in collisions.
     *
     * @param other The collision visitor (ignored)
     */
    @Override
    public void accept(CollisionVisitor other) {
        // Null object does nothing
    }

    /**
     * Updates the null game object.
     * This implementation does nothing, as null objects don't need updates.
     */
    @Override
    public void update() {
        // Null object does nothing
    }

    /**
     * Moves the null game object.
     * This implementation does nothing, as null objects don't move.
     *
     * @param xDelta The amount to move in the X direction (ignored)
     * @param yDelta The amount to move in the Y direction (ignored)
     */
    @Override
    public void move(float xDelta, float yDelta) {
        // Null object does nothing
    }

    @Override
    public String toString() {
        return "NullGameObject[]";
    }
}