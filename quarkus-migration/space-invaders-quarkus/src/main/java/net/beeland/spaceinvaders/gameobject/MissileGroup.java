package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;

/**
 * MissileGroup is a composite that manages player missiles.
 * In the original Space Invaders, only one missile can be on screen at a time.
 * 
 * The group is responsible for:
 * - Managing active missiles
 * - Enforcing the one-missile-at-a-time rule
 * - Handling collision with aliens, shields, and walls
 */
public class MissileGroup extends Composite {

    /**
     * Creates a missile group.
     */
    public MissileGroup() {
        super(GameObjectName.MISSILE_GROUP);
        this.setCompositeName(CompositeName.MISSILE_GROUP);
    }

    /**
     * Accepts a collision visitor for double-dispatch collision handling.
     *
     * @param other The collision visitor
     */
    @Override
    public void accept(CollisionVisitor other) {
        other.visitMissileGroup(this);
    }

    /**
     * Checks if a missile is currently active.
     *
     * @return true if at least one missile exists, false otherwise
     */
    public boolean hasMissile() {
        return getFirstChild() != null && !((GameObject) getFirstChild()).isMarkedForDeath();
    }

    /**
     * Gets the active missile.
     *
     * @return The active missile, or null if none exists
     */
    public Missile getActiveMissile() {
        GameObject child = (GameObject) getFirstChild();
        if (child != null && !child.isMarkedForDeath()) {
            return (Missile) child;
        }
        return null;
    }

    /**
     * Removes all missiles from the group.
     */
    public void clearMissiles() {
        GameObject child = (GameObject) getFirstChild();
        while (child != null) {
            GameObject next = (GameObject) child.getNext();
            child.markForDeath();
            child = next;
        }
    }

    @Override
    public String toString() {
        return String.format("MissileGroup[name=%s, children=%d, hasMissile=%b]",
                getName(), getNumChildren(), hasMissile());
    }
}