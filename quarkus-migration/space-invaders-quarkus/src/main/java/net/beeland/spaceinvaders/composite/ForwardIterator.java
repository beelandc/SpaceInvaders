package net.beeland.spaceinvaders.composite;

/**
 * ForwardIterator implements non-recursive depth-first traversal of composite structures.
 * 
 * Traversal order:
 * 1. If node has children, visit first child
 * 2. Else if node has siblings, visit next sibling
 * 3. Else go up to parent and repeat from step 2
 * 
 * This allows efficient traversal of game object hierarchies without recursion.
 */
public class ForwardIterator extends Iterator {
    
    private Component current;
    private final Component root;
    private boolean done;

    /**
     * Creates a forward iterator starting at the specified component.
     *
     * @param start The starting component (must be a COMPOSITE)
     * @throws IllegalArgumentException if start is null or not a COMPOSITE
     */
    public ForwardIterator(Component start) {
        if (start == null) {
            throw new IllegalArgumentException("Start component cannot be null");
        }
        if (start.getContainerType() != Component.ContainerType.COMPOSITE) {
            throw new IllegalArgumentException("Start component must be a COMPOSITE");
        }

        this.current = start;
        this.root = start;
        this.done = false;
    }

    /**
     * Gets the parent of the specified component.
     *
     * @param node The component
     * @return The parent component, or null if none exists
     */
    public static Component getParent(Component node) {
        return (node != null) ? node.getParent() : null;
    }

    /**
     * Gets the first child of the specified component.
     *
     * @param node The component
     * @return The first child component, or null if none exists
     */
    public static Component getChild(Component node) {
        return (node != null) ? node.getFirstChild() : null;
    }

    /**
     * Gets the next sibling of the specified component.
     *
     * @param node The component
     * @return The next sibling component, or null if none exists
     */
    public static Component getSibling(Component node) {
        return (node != null) ? (Component) node.getNext() : null;
    }

    /**
     * Gets the first component in the iteration (the root).
     *
     * @return The root component
     */
    @Override
    public Component first() {
        this.current = this.root;
        this.done = false;
        return this.current;
    }

    /**
     * Gets the next component in the depth-first traversal.
     *
     * @return The next component, or null if iteration is complete
     */
    @Override
    public Component next() {
        if (this.current == null) {
            return null;
        }

        Component node = this.current;
        Component child = getChild(node);
        Component sibling = getSibling(node);
        Component parent = getParent(node);

        // Perform depth-first iteration
        node = nextStep(node, parent, sibling);

        this.current = node;
        return this.current;
    }

    /**
     * Checks if the iteration is complete.
     *
     * @return true if done, false otherwise
     */
    @Override
    public boolean isDone() {
        return this.done;
    }

    /**
     * Performs one step of the depth-first traversal.
     * 
     * Algorithm:
     * 1. If node has child, return child
     * 2. Else if node has sibling, return sibling
     * 3. Else go up to parent and look for sibling
     * 4. Repeat step 3 until sibling found or root reached
     *
     * @param node The current node
     * @param parent The parent of the current node
     * @param sibling The sibling of the current node
     * @return The next node in traversal, or null if done
     */
    private Component nextStep(Component node, Component parent, Component sibling) {
        // Try to get child or sibling
        Component childOrSibling = returnChildOrSibling(node, sibling);

        // If node has children or siblings, return it
        if (childOrSibling != null) {
            return childOrSibling;
        } else {
            // Go up the tree looking for a sibling
            while (parent != null) {
                node = parent;

                // If parent has sibling, return it
                if (node.getNext() != null) {
                    return (Component) node.getNext();
                } else {
                    // Go up another level
                    parent = getParent(node);
                }
            }
        }

        // Check if iteration is complete
        if (node.getContainerType() != Component.ContainerType.LEAF && parent == null) {
            node = null;
            this.done = true;
        }

        return node;
    }

    /**
     * Returns the child if it exists, otherwise returns the sibling.
     *
     * @param node The current node
     * @param sibling The sibling of the current node
     * @return The child or sibling, or null if neither exists
     */
    private Component returnChildOrSibling(Component node, Component sibling) {
        Component firstChild = node.getFirstChild();

        // If has children, return first child
        if (firstChild != null) {
            return firstChild;
        }
        // Else if node has sibling, return sibling
        else if (sibling != null) {
            return sibling;
        }

        // If node has neither child nor sibling
        return null;
    }
}