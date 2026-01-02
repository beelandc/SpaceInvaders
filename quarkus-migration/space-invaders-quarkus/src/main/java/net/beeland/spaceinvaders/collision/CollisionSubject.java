package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.manager.DLink;

/**
 * CollisionSubject - Observer pattern subject for collision events.
 * Maintains a list of observers and notifies them when collisions occur.
 */
public class CollisionSubject {
    private CollisionObserver head;
    private GameObject objA;
    private GameObject objB;

    public CollisionSubject() {
        this.head = null;
        this.objA = null;
        this.objB = null;
    }

    /**
     * Attach an observer to this subject.
     *
     * @param observer The observer to attach
     * @throws IllegalArgumentException if observer is null
     */
    public void attach(CollisionObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }

        // Set the subject reference in the observer
        observer.setSubject(this);

        // Add to front of list
        if (this.head == null) {
            this.head = observer;
            observer.setNext(null);
            observer.setPrev(null);
        } else {
            observer.setNext(this.head);
            observer.setPrev(null);
            this.head.setPrev(observer);
            this.head = observer;
        }
    }

    /**
     * Detach an observer from this subject.
     *
     * @param observer The observer to detach
     */
    public void detach(CollisionObserver observer) {
        if (observer == null) {
            return;
        }

        if (observer == this.head) {
            this.head = (CollisionObserver) observer.getNext();
            if (this.head != null) {
                this.head.setPrev(null);
            }
        } else {
            DLink prev = observer.getPrev();
            DLink next = observer.getNext();
            
            if (prev != null) {
                prev.setNext(next);
            }
            if (next != null) {
                next.setPrev(prev);
            }
        }

        observer.setNext(null);
        observer.setPrev(null);
        observer.setSubject(null);
    }

    /**
     * Detach all observers from this subject.
     */
    public void detachAll() {
        CollisionObserver observer = this.head;
        while (observer != null) {
            CollisionObserver next = (CollisionObserver) observer.getNext();
            observer.setNext(null);
            observer.setPrev(null);
            observer.setSubject(null);
            observer = next;
        }
        this.head = null;
    }

    /**
     * Set the collision objects.
     *
     * @param objA The first game object
     * @param objB The second game object
     * @throws IllegalArgumentException if either object is null
     */
    public void setCollision(GameObject objA, GameObject objB) {
        if (objA == null) {
            throw new IllegalArgumentException("objA cannot be null");
        }
        if (objB == null) {
            throw new IllegalArgumentException("objB cannot be null");
        }
        this.objA = objA;
        this.objB = objB;
    }

    /**
     * Notify all attached observers of a collision event.
     */
    public void notifyObservers() {
        CollisionObserver observer = this.head;
        while (observer != null) {
            observer.notifyCollision();
            observer = (CollisionObserver) observer.getNext();
        }
    }

    /**
     * Get the first game object in the collision.
     *
     * @return The first game object
     */
    public GameObject getObjA() {
        return this.objA;
    }

    /**
     * Get the second game object in the collision.
     *
     * @return The second game object
     */
    public GameObject getObjB() {
        return this.objB;
    }

    /**
     * Get the head of the observer list.
     * Used primarily for testing.
     *
     * @return The first observer in the list
     */
    public CollisionObserver getHead() {
        return this.head;
    }

    /**
     * Clear all observers and reset state.
     */
    public void clear() {
        detachAll();
        this.objA = null;
        this.objB = null;
    }
}