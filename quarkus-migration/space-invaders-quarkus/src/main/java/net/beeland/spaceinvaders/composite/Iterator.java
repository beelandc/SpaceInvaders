package net.beeland.spaceinvaders.composite;

import net.beeland.spaceinvaders.gameobject.GameObject;

/**
 * Iterator is an abstract base class for traversing composite structures.
 * It implements the Iterator pattern for non-recursive traversal of
 * game object hierarchies.
 * 
 * Concrete implementations include:
 * - ForwardIterator: Depth-first forward traversal
 * - ReverseIterator: Reverse traversal
 */
public abstract class Iterator {

    /**
     * Gets the first component in the iteration.
     *
     * @return The first component
     */
    public abstract Component first();

    /**
     * Gets the next component in the iteration.
     *
     * @return The next component, or null if iteration is complete
     */
    public abstract Component next();

    /**
     * Checks if the iteration is complete.
     *
     * @return true if iteration is done, false otherwise
     */
    public abstract boolean isDone();

    /**
     * Gets the sibling game object of the specified node.
     * This is a convenience method that wraps the Component sibling access.
     *
     * @param node The game object node
     * @return The sibling game object, or null if none exists
     */
    public static GameObject getSiblingGameObject(GameObject node) {
        if (node == null) {
            return null;
        }

        Component sibling = (Component) node.getNext();
        return (GameObject) sibling;
    }

    /**
     * Gets the child game object of the specified node.
     * This is a convenience method that wraps the Component child access.
     *
     * @param node The game object node
     * @return The child game object, or null if none exists
     */
    public static GameObject getChildGameObject(GameObject node) {
        if (node == null) {
            return null;
        }

        Component child = node.getFirstChild();
        return (GameObject) child;
    }

    /**
     * Gets the parent game object of the specified node.
     *
     * @param node The game object node
     * @return The parent game object, or null if none exists
     */
    public static GameObject getParentGameObject(GameObject node) {
        if (node == null) {
            return null;
        }

        Component parent = node.getParent();
        return (GameObject) parent;
    }
}