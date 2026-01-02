package net.beeland.spaceinvaders.composite;

/**
 * ReverseIterator implements reverse traversal of composite structures.
 * 
 * This iterator uses the reverse pointers set up by a forward iteration
 * to traverse the structure in reverse order. It's useful for operations
 * that need to process nodes from bottom-up or right-to-left.
 */
public class ReverseIterator extends Iterator {
    
    private final Component root;
    private Component current;

    /**
     * Creates a reverse iterator starting at the specified component.
     * This constructor sets up the reverse pointers by doing a forward
     * traversal first.
     *
     * @param start The starting component (must be a COMPOSITE)
     * @throws IllegalArgumentException if start is null or not a COMPOSITE
     */
    public ReverseIterator(Component start) {
        if (start == null) {
            throw new IllegalArgumentException("Start component cannot be null");
        }
        if (start.getContainerType() != Component.ContainerType.COMPOSITE) {
            throw new IllegalArgumentException("Start component must be a COMPOSITE");
        }

        this.root = start;
        this.current = start;

        // Initialize reverse pointers using forward iteration
        setupReversePointers();
    }

    /**
     * Sets up the reverse pointers by doing a forward traversal.
     * Each node's reverse pointer is set to the previous node in the traversal.
     */
    private void setupReversePointers() {
        ForwardIterator forward = new ForwardIterator(root);

        Component prevNode = root;
        Component node = forward.first();

        while (!forward.isDone()) {
            // Save previous node
            prevNode = node;

            // Advance to next node
            node = forward.next();

            if (node != null) {
                // Set reverse pointer
                node.setReverse(prevNode);
            }
        }

        // Set root's reverse pointer to the last node
        root.setReverse(prevNode);
    }

    /**
     * Gets the first component in the reverse iteration (the last node).
     *
     * @return The last component in forward order
     */
    @Override
    public Component first() {
        if (root == null) {
            return null;
        }

        this.current = root.getReverse();

        // Clear root's reverse pointer to avoid infinite loop
        root.setReverse(null);

        return this.current;
    }

    /**
     * Gets the next component in the reverse traversal.
     *
     * @return The next component in reverse order, or null if done
     */
    @Override
    public Component next() {
        if (this.current == null) {
            return null;
        }

        this.current = this.current.getReverse();
        return this.current;
    }

    /**
     * Checks if the iteration is complete.
     *
     * @return true if current is null, false otherwise
     */
    @Override
    public boolean isDone() {
        return (this.current == null);
    }
}