package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * GameObjectManager manages all game objects in the game using the Object Pool pattern.
 * It maintains active and reserved lists of GameObjectRef instances and provides
 * methods to attach, find, detach, and update game objects.
 * 
 * This class is a CDI-managed singleton (@ApplicationScoped) that can be injected
 * throughout the application.
 */
@ApplicationScoped
public class GameObjectManager extends Manager {
    
    private final GameObjectRef compareRef;
    private final NullGameObject nullGameObject;

    /**
     * Creates a game object manager with default pool settings.
     * Default: 10 initial objects, grow by 5 when needed.
     */
    public GameObjectManager() {
        super();
        this.compareRef = new GameObjectRef();
        this.nullGameObject = new NullGameObject();
        this.compareRef.setGameObject(this.nullGameObject);
        
        // Initialize the pool
        initialize(10, 5);
    }

    /**
     * Attaches a game object to the manager.
     * The game object is added to the active list and can be updated/rendered.
     *
     * @param gameObject The game object to attach
     * @return A reference to the attached game object
     * @throws IllegalArgumentException if gameObject is null
     */
    public GameObjectRef attach(GameObject gameObject) {
        if (gameObject == null) {
            throw new IllegalArgumentException("Cannot attach null GameObject");
        }

        GameObjectRef ref = (GameObjectRef) getFromPool();
        if (ref == null) {
            throw new IllegalStateException("Failed to allocate GameObjectRef from pool");
        }

        ref.set(gameObject);
        return ref;
    }

    /**
     * Finds a game object by its name.
     * Returns the first game object with the specified name.
     *
     * @param name The name to search for
     * @return The game object with the specified name, or null if not found
     */
    public GameObject find(GameObject.GameObjectName name) {
        if (name == null) {
            return null;
        }

        // Search through active list
        DLink current = getActiveHead();
        while (current != null) {
            GameObjectRef ref = (GameObjectRef) current;
            GameObject obj = ref.getGameObject();
            if (obj != null && obj.getName() == name) {
                return obj;
            }
            current = current.getNext();
        }

        return null;
    }

    /**
     * Detaches a game object from the manager.
     * The game object is removed from the active list and returned to the pool.
     *
     * @param gameObject The game object to detach
     * @throws IllegalArgumentException if gameObject is null
     */
    public void detach(GameObject gameObject) {
        if (gameObject == null) {
            throw new IllegalArgumentException("Cannot detach null GameObject");
        }

        // Find the reference by hash code
        DLink current = getActiveHead();
        while (current != null) {
            GameObjectRef ref = (GameObjectRef) current;
            GameObject obj = ref.getGameObject();
            if (obj != null && obj.hashCode() == gameObject.hashCode()) {
                returnToPool(ref);
                return;
            }
            current = current.getNext();
        }

        throw new IllegalStateException("GameObject not found in manager: " + gameObject);
    }

    /**
     * Updates all active game objects.
     * This should be called once per frame to update game object states.
     */
    public void update() {
        DLink current = getActiveHead();

        while (current != null) {
            GameObjectRef ref = (GameObjectRef) current;
            GameObject gameObject = ref.getGameObject();
            if (gameObject != null) {
                gameObject.update();
            }
            current = current.getNext();
        }
    }

    /**
     * Prints debug information about the manager state.
     * Shows active and reserved lists with their contents.
     */
    public void print() {
        System.out.println("******** GAME OBJECT MANAGER ****************");
        System.out.println(getStats());

        // Print Active List
        System.out.println("Active List:");
        DLink current = getActiveHead();
        int nodePos = 0;

        if (current != null) {
            while (current != null) {
                GameObjectRef ref = (GameObjectRef) current;
                GameObject gameObject = ref.getGameObject();
                System.out.printf("  Node %d - %s%n", nodePos, 
                        gameObject != null ? gameObject.toString() : "null");
                current = current.getNext();
                nodePos++;
            }
        } else {
            System.out.println("  *** Empty ***");
        }

        System.out.println();
    }

    //----------------------------------------------------------------------
    // Override Abstract Methods from Manager
    //----------------------------------------------------------------------

    @Override
    protected DLink createNode() {
        return new GameObjectRef();
    }
}