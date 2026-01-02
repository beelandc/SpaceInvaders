package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.manager.DLink;

/**
 * CollisionObserver is an abstract base class implementing the Observer pattern
 * for collision detection responses. Concrete observers implement specific
 * collision responses (e.g., removing objects, playing sounds, updating scores).
 * 
 * This class extends DLink to allow observers to be organized in linked lists
 * within a CollisionSubject.
 * 
 * Concrete implementations should override:
 * - notifyCollision(): Immediate response to collision
 * - executeDelayed(): Optional delayed execution (default: no-op)
 */
public abstract class CollisionObserver extends DLink {
    
    protected CollisionSubject subject;

    /**
     * Creates a new CollisionObserver with no subject.
     */
    public CollisionObserver() {
        super();
        this.subject = null;
    }

    /**
     * Called when a collision is detected.
     * Concrete observers must implement this to define their collision response.
     * The observer can access the colliding objects via getSubject().getObjA()
     * and getSubject().getObjB().
     */
    public abstract void notifyCollision();

    /**
     * Optional method for delayed execution of collision responses.
     * Some collision responses need to be deferred to avoid modifying
     * collections during iteration. Default implementation does nothing.
     */
    public void executeDelayed() {
        // Default implementation - override if delayed execution is needed
    }

    /**
     * Sets the subject this observer is attached to.
     *
     * @param subject The collision subject
     */
    public void setSubject(CollisionSubject subject) {
        this.subject = subject;
    }

    /**
     * Gets the subject this observer is attached to.
     *
     * @return The collision subject
     */
    public CollisionSubject getSubject() {
        return subject;
    }

    /**
     * Dumps information about this observer for debugging.
     * Override to provide specific observer details.
     */
    @Override
    public void dump() {
        System.out.println("CollisionObserver: " + this.getClass().getSimpleName());
    }

    /**
     * Washes/resets this observer to default state.
     * Override if the observer has state that needs resetting.
     */
    @Override
    protected void wash() {
        // Default implementation - observers typically don't need washing
    }
}