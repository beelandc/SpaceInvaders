package net.beeland.spaceinvaders.composite;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.manager.DLink;

/**
 * Composite is an abstract class that extends GameObject and can contain children.
 * This implements the Composite pattern, allowing game objects to form hierarchies.
 * 
 * Examples include:
 * - AlienGrid containing AlienColumns
 * - AlienColumn containing individual Aliens
 * - ShieldGroup containing Shield structures
 */
public abstract class Composite extends GameObject {
    
    protected CompositeName compositeName;
    protected DLink head;
    protected int numChildren;

    /**
     * Creates a composite game object with the specified name.
     *
     * @param gameName The game object name
     */
    protected Composite(GameObjectName gameName) {
        super(gameName);
        this.numChildren = 0;
        this.head = null;
        this.containerType = ContainerType.COMPOSITE;
        this.compositeName = CompositeName.UNINITIALIZED;
    }

    /**
     * Sets the composite name for this composite.
     *
     * @param name The composite name
     */
    public void setCompositeName(CompositeName name) {
        this.compositeName = name;
    }

    /**
     * Gets the composite name.
     *
     * @return The composite name
     */
    public CompositeName getCompositeName() {
        return this.compositeName;
    }

    /**
     * Adds a child component to this composite.
     * The child is added to the front of the list.
     *
     * @param component The component to add
     * @throws IllegalArgumentException if component is null
     */
    @Override
    public void add(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Cannot add null component");
        }

        // Add to front of list
        if (this.head == null) {
            this.head = component;
            component.clear();
        } else {
            component.setNext(this.head);
            this.head.setPrev(component);
            component.setPrev(null);
            this.head = component;
        }

        this.numChildren++;
        component.setParent(this);
    }

    /**
     * Removes a child component from this composite.
     *
     * @param component The component to remove
     * @throws IllegalArgumentException if component is null
     */
    @Override
    public void remove(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Cannot remove null component");
        }

        // Remove from list
        if (component == this.head) {
            this.head = component.getNext();
            if (this.head != null) {
                this.head.setPrev(null);
            }
        } else {
            component.remove();
        }

        this.numChildren--;
    }

    /**
     * Gets the first child component.
     *
     * @return The first child, or null if no children
     */
    @Override
    public Component getFirstChild() {
        return (Component) this.head;
    }

    /**
     * Gets the number of children in this composite.
     *
     * @return The number of children
     */
    public int getNumChildren() {
        return this.numChildren;
    }

    /**
     * Gets a child by index.
     *
     * @param index The index (0-based)
     * @return The child at the specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Component getChildByIndex(int index) {
        if (index < 0 || index >= numChildren) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for " + numChildren + " children");
        }

        DLink node = this.head;
        int count = 0;

        while (count < index && node != null) {
            node = node.getNext();
            count++;
        }

        return (Component) node;
    }

    /**
     * Gets the head of the component list.
     *
     * @return The head of the list
     */
    public DLink getComponentList() {
        return this.head;
    }

    /**
     * Moves this composite and all its children by the specified delta.
     *
     * @param xDelta The amount to move in the X direction
     * @param yDelta The amount to move in the Y direction
     */
    @Override
    public void move(float xDelta, float yDelta) {
        DLink node = this.head;

        while (node != null) {
            Component component = (Component) node;
            component.move(xDelta, yDelta);
            node = node.getNext();
        }
    }

    /**
     * Updates this composite and all its children.
     * Recalculates the bounding box based on children.
     */
    @Override
    public void update() {
        if (getFirstChild() != null) {
            baseUpdateBoundingBox();
            super.update();
        }
    }

    /**
     * Prints debug information about this composite and its children.
     */
    public void print() {
        System.out.println("Composite: " + getName() + " (" + compositeName + ") - " + numChildren + " children");
        
        DLink node = head;
        while (node != null) {
            if (node instanceof Component) {
                Component comp = (Component) node;
                if (comp instanceof GameObject) {
                    System.out.println("  Child: " + ((GameObject) comp).getName());
                }
            }
            node = node.getNext();
        }
    }

    /**
     * Enum defining all possible composite names in the game.
     */
    public enum CompositeName {
        ALIEN_GRID,
        ALIEN_COL_1,
        ALIEN_COL_2,
        ALIEN_COL_3,
        ALIEN_COL_4,
        ALIEN_COL_5,
        ALIEN_COL_6,
        ALIEN_COL_7,
        ALIEN_COL_8,
        ALIEN_COL_9,
        ALIEN_COL_10,
        ALIEN_COL_11,
        MISSILE_GROUP,
        CORE_CANNON_GROUP,
        SHIELD_GROUP,
        BOMB_ROOT,
        FLYING_SAUCER_ROOT,
        UNINITIALIZED
    }
}