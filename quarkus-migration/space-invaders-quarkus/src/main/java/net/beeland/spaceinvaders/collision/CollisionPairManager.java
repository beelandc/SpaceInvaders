package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import net.beeland.spaceinvaders.gameobject.GameObject;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * CollisionPairManager manages all collision pairs in the game using the
 * Object Pool pattern. It extends the Manager base class to provide
 * efficient allocation and reuse of CollisionPair objects.
 * 
 * This manager is a CDI-managed singleton (@ApplicationScoped) that handles:
 * - Creating and managing collision pairs
 * - Processing all active collision pairs each frame
 * - Finding collision pairs by name
 * - Tracking the currently active collision pair during processing
 * 
 * Usage:
 * <pre>
 * {@code
 * CollisionPairManager manager = CDI.current().select(CollisionPairManager.class).get();
 * CollisionPair pair = manager.add(CollisionPair.Name.ALIEN_MISSILE, alienRoot, missileRoot);
 * manager.process(); // Check all collisions
 * }
 * </pre>
 */
@ApplicationScoped
public class CollisionPairManager extends Manager {

    private CollisionPair activeColPair;
    private boolean initialized = false;

    /**
     * Creates a new CollisionPairManager with default pool settings.
     * Called by CDI container.
     */
    public CollisionPairManager() {
        super();
        this.activeColPair = null;
    }
    
    /**
     * Ensures the manager is initialized before use.
     * Uses lazy initialization to avoid issues during CDI startup.
     */
    private void ensureInitialized() {
        if (!initialized) {
            initialize(3, 1);
            initialized = true;
        }
    }

    /**
     * Adds a new collision pair to the manager.
     * Gets a collision pair from the pool, initializes it, and adds it to the active list.
     *
     * @param name The name/type of the collision pair
     * @param treeRootA The root of the first game object tree
     * @param treeRootB The root of the second game object tree
     * @return The initialized collision pair
     * @throws IllegalArgumentException if either tree root is null
     */
    public CollisionPair add(CollisionPair.Name name, GameObject treeRootA, GameObject treeRootB) {
        ensureInitialized();
        
        if (treeRootA == null || treeRootB == null) {
            throw new IllegalArgumentException("Tree roots cannot be null");
        }

        CollisionPair colPair = (CollisionPair) getFromPool();
        colPair.set(name, treeRootA, treeRootB);
        return colPair;
    }

    /**
     * Processes all active collision pairs.
     * Iterates through the active list and calls process() on each pair.
     * Tracks the currently active pair for observer access.
     */
    public void process() {
        ensureInitialized();
        
        CollisionPair colPair = (CollisionPair) getActiveHead();

        while (colPair != null) {
            // Set the current active pair
            this.activeColPair = colPair;

            // Process collision detection for this pair
            colPair.process();

            // Move to next pair
            colPair = (CollisionPair) colPair.getNext();
        }

        // Clear active pair after processing
        this.activeColPair = null;
    }

    /**
     * Finds a collision pair by name.
     *
     * @param name The name of the collision pair to find
     * @return The collision pair, or null if not found
     */
    public CollisionPair find(CollisionPair.Name name) {
        ensureInitialized();
        
        if (name == null) {
            return null;
        }
        
        // Search through active list
        CollisionPair current = (CollisionPair) getActiveHead();
        while (current != null) {
            if (current.getName() == name) {
                return current;
            }
            current = (CollisionPair) current.getNext();
        }
        
        return null;
    }

    /**
     * Removes a collision pair from the active list and returns it to the pool.
     *
     * @param node The collision pair to remove
     * @throws IllegalArgumentException if node is null
     */
    public void remove(CollisionPair node) {
        ensureInitialized();
        
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        returnToPool(node);
    }

    /**
     * Gets the currently active collision pair being processed.
     * This is useful for observers that need to know which pair triggered them.
     *
     * @return The active collision pair, or null if not currently processing
     */
    public CollisionPair getActiveColPair() {
        return activeColPair;
    }

    /**
     * Prints all collision pairs (active and reserved).
     * Useful for debugging.
     */
    public void printAll() {
        ensureInitialized();
        
        dumpActive();
    }

    /**
     * Creates a new CollisionPair node for the pool.
     *
     * @return A new CollisionPair instance
     */
    @Override
    protected DLink createNode() {
        return new CollisionPair();
    }
}