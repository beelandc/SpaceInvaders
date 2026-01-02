package net.beeland.spaceinvaders.composite;

import net.beeland.spaceinvaders.manager.DLink;

/**
 * Component is the base class for the Composite pattern implementation.
 * It extends DLink to support linked list management and provides
 * the interface for composite structure operations.
 * 
 * This class supports both LEAF and COMPOSITE container types.
 */
public abstract class Component extends DLink {
    
    /**
     * Reference to the parent component in the hierarchy.
     */
    protected Component parent;
    
    /**
     * Reference used for reverse iteration through the composite structure.
     */
    protected Component reverse;
    
    /**
     * The type of container (LEAF or COMPOSITE).
     */
    protected ContainerType containerType = ContainerType.UNKNOWN;

    /**
     * Adds a child component to this component.
     * Implementation depends on whether this is a LEAF or COMPOSITE.
     *
     * @param component The component to add
     */
    public abstract void add(Component component);

    /**
     * Removes a child component from this component.
     * Implementation depends on whether this is a LEAF or COMPOSITE.
     *
     * @param component The component to remove
     */
    public abstract void remove(Component component);

    /**
     * Moves this component by the specified delta values.
     *
     * @param xDelta The amount to move in the X direction
     * @param yDelta The amount to move in the Y direction
     */
    public abstract void move(float xDelta, float yDelta);

    /**
     * Gets the first child component.
     * Returns null for LEAF nodes.
     *
     * @return The first child component, or null if none exists
     */
    public abstract Component getFirstChild();

    /**
     * Gets the parent component.
     *
     * @return The parent component
     */
    public Component getParent() {
        return parent;
    }

    /**
     * Sets the parent component.
     *
     * @param parent The parent component
     */
    public void setParent(Component parent) {
        this.parent = parent;
    }

    /**
     * Gets the reverse reference component.
     *
     * @return The reverse reference component
     */
    public Component getReverse() {
        return reverse;
    }

    /**
     * Sets the reverse reference component.
     *
     * @param reverse The reverse reference component
     */
    public void setReverse(Component reverse) {
        this.reverse = reverse;
    }

    /**
     * Gets the container type.
     *
     * @return The container type
     */
    public ContainerType getContainerType() {
        return containerType;
    }

    /**
     * Sets the container type.
     *
     * @param containerType The container type
     */
    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }

    /**
     * Enum defining the types of containers in the composite structure.
     */
    public enum ContainerType {
        /**
         * A leaf node with no children.
         */
        LEAF,
        
        /**
         * A composite node that can contain children.
         */
        COMPOSITE,
        
        /**
         * Unknown or uninitialized container type.
         */
        UNKNOWN
    }
}