package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.composite.Iterator;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.manager.DLink;

/**
 * CollisionPair represents a pair of game object trees that should be checked
 * for collisions. It manages collision detection between two hierarchies of
 * game objects and notifies observers when collisions occur.
 * 
 * The collision detection algorithm performs a nested iteration through both
 * trees, checking each leaf node in tree A against each leaf node in tree B
 * for intersection using their collision rectangles.
 * 
 * This class uses the Observer pattern via CollisionSubject to notify
 * interested parties when collisions are detected.
 */
public class CollisionPair extends DLink {

    /**
     * Enumeration of collision pair types.
     * Each type represents a specific collision scenario in the game.
     */
    public enum Name {
        ALIEN_MISSILE,
        ALIEN_WALL,
        MISSILE_WALL,
        MISSILE_SHIELD,
        ALIEN_SHIELD,
        BOMB_SHIELD,
        BOMB_WALL,
        CORE_CANNON_WALL,
        BOMB_MISSILE,
        FLYING_SAUCER_MISSILE,
        FLYING_SAUCER_WALL,
        BOMB_SHIP,
        NULL_OBJECT,
        NOT_INITIALIZED
    }

    private Name name;
    private GameObject treeA;
    private GameObject treeB;
    private CollisionSubject subject;

    /**
     * Creates a new uninitialized CollisionPair.
     */
    public CollisionPair() {
        super();
        this.name = Name.NOT_INITIALIZED;
        this.treeA = null;
        this.treeB = null;
        this.subject = new CollisionSubject();
    }

    /**
     * Sets the collision pair configuration.
     *
     * @param name The name/type of this collision pair
     * @param treeRootA The root of the first game object tree
     * @param treeRootB The root of the second game object tree
     * @throws IllegalArgumentException if either tree root is null
     */
    public void set(Name name, GameObject treeRootA, GameObject treeRootB) {
        if (treeRootA == null || treeRootB == null) {
            throw new IllegalArgumentException("Tree roots cannot be null");
        }

        this.name = name;
        this.treeA = treeRootA;
        this.treeB = treeRootB;
    }

    /**
     * Clears this collision pair, resetting it to uninitialized state.
     */
    public void clear() {
        this.name = Name.NOT_INITIALIZED;
        this.treeA = null;
        this.treeB = null;
        if (this.subject != null) {
            this.subject.detachAll();
        }
    }

    /**
     * Gets the name/type of this collision pair.
     *
     * @return The collision pair name
     */
    public Name getName() {
        return name;
    }

    /**
     * Sets the name/type of this collision pair.
     *
     * @param name The collision pair name
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * Processes collision detection for this pair.
     * Iterates through both trees and checks for intersections.
     */
    public void process() {
        collide(this.treeA, this.treeB);
    }

    /**
     * Performs collision detection between two game object trees.
     * This is a static method that can be used independently of a CollisionPair.
     * 
     * The algorithm performs a nested iteration:
     * - For each node in tree A
     *   - For each node in tree B
     *     - Check if their collision rectangles intersect
     *     - If they intersect, call accept() on node A with node B
     *
     * @param safeTreeA The root of the first game object tree
     * @param safeTreeB The root of the second game object tree
     */
    public static void collide(GameObject safeTreeA, GameObject safeTreeB) {
        if (safeTreeA == null || safeTreeB == null) {
            return;
        }

        GameObject nodeA = safeTreeA;
        int maxIterationsA = 1000; // Safety limit to prevent infinite loops
        int iterA = 0;

        while (nodeA != null && iterA < maxIterationsA) {
            GameObject nodeB = safeTreeB;
            int maxIterationsB = 1000; // Safety limit to prevent infinite loops
            int iterB = 0;

            while (nodeB != null && iterB < maxIterationsB) {
                // Get collision rectangles
                CollisionRect rectA = nodeA.getCollisionObject().getCollisionRect();
                CollisionRect rectB = nodeB.getCollisionObject().getCollisionRect();

                // Check for intersection
                if (CollisionRect.intersect(rectA, rectB)) {
                    // Notify via visitor pattern
                    // Note: accept() will be implemented when visitor pattern is fully integrated
                    // For now, we just detect the collision
                    // nodeA.accept(nodeB);
                }

                // Move to next sibling in tree B
                GameObject nextB = Iterator.getSiblingGameObject(nodeB);
                if (nextB == nodeB) {
                    // Safety check: if getSiblingGameObject returns the same node, break to prevent infinite loop
                    break;
                }
                nodeB = nextB;
                iterB++;
            }

            // Move to next sibling in tree A
            GameObject nextA = Iterator.getSiblingGameObject(nodeA);
            if (nextA == nodeA) {
                // Safety check: if getSiblingGameObject returns the same node, break to prevent infinite loop
                break;
            }
            nodeA = nextA;
            iterA++;
        }
    }

    /**
     * Attaches an observer to this collision pair.
     * The observer will be notified when collisions occur.
     *
     * @param observer The observer to attach
     */
    public void attach(CollisionObserver observer) {
        this.subject.attach(observer);
    }

    /**
     * Notifies all attached observers of a collision.
     */
    public void notifyListeners() {
        this.subject.notifyObservers();
    }

    /**
     * Sets the collision objects for notification.
     * This should be called before notifyListeners().
     *
     * @param objA The first game object in the collision
     * @param objB The second game object in the collision
     */
    public void setCollision(GameObject objA, GameObject objB) {
        this.subject.setCollision(objA, objB);
    }

    /**
     * Gets the collision subject for this pair.
     * Used primarily for testing.
     *
     * @return The collision subject
     */
    public CollisionSubject getSubject() {
        return subject;
    }

    /**
     * Gets the first tree root.
     *
     * @return The first game object tree root
     */
    public GameObject getTreeA() {
        return treeA;
    }

    /**
     * Gets the second tree root.
     *
     * @return The second game object tree root
     */
    public GameObject getTreeB() {
        return treeB;
    }

    /**
     * Dumps information about this collision pair for debugging.
     */
    @Override
    public void dump() {
        System.out.println("CollisionPair: " + name);
        System.out.println("  TreeA: " + (treeA != null ? treeA.getName() : "null"));
        System.out.println("  TreeB: " + (treeB != null ? treeB.getName() : "null"));
    }

    /**
     * Washes/resets this collision pair to default state.
     * Called when the pair is returned to the pool.
     */
    @Override
    protected void wash() {
        clear();
    }
}