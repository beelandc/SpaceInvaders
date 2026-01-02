package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.manager.DLink;

/**
 * GameObjectRef is a wrapper class that extends DLink to allow GameObjects
 * to be managed in linked lists by the GameObjectManager.
 * 
 * This class follows the Object Pool pattern, allowing efficient reuse
 * of GameObject references without constant allocation/deallocation.
 */
public class GameObjectRef extends DLink {
    
    private GameObject gameObject;

    /**
     * Creates an empty game object reference.
     */
    public GameObjectRef() {
        super();
        this.gameObject = null;
    }

    /**
     * Sets the game object for this reference.
     *
     * @param gameObject The game object to reference
     * @throws IllegalArgumentException if gameObject is null
     */
    public void set(GameObject gameObject) {
        if (gameObject == null) {
            throw new IllegalArgumentException("GameObject cannot be null");
        }
        this.gameObject = gameObject;
    }

    /**
     * Gets the referenced game object.
     *
     * @return The game object, or null if not set
     */
    public GameObject getGameObject() {
        return this.gameObject;
    }

    /**
     * Sets the game object for this reference.
     * This is an alternative to set() for consistency with the original C# code.
     *
     * @param gameObject The game object to reference
     */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     * Gets the name of the referenced game object.
     *
     * @return The game object name, or null if no game object is set
     */
    public GameObject.GameObjectName getName() {
        return (this.gameObject != null) ? this.gameObject.getName() : null;
    }

    /**
     * Washes this reference by clearing the game object.
     * This is called when the reference is returned to the pool.
     */
    @Override
    protected void wash() {
        this.gameObject = null;
    }

    /**
     * Dumps debug information about this reference.
     */
    @Override
    public void dump() {
        if (gameObject != null) {
            System.out.println("GameObjectRef[" + gameObject.toString() + "]");
        } else {
            System.out.println("GameObjectRef[empty]");
        }
    }

    @Override
    public String toString() {
        if (gameObject != null) {
            return "GameObjectRef[" + gameObject.toString() + "]";
        }
        return "GameObjectRef[empty]";
    }
}