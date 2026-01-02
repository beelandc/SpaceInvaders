package net.beeland.spaceinvaders.manager;

/**
 * Doubly-linked list node base class
 * Used for efficient object pooling and management
 * All managed objects inherit from this class
 */
public abstract class DLink {
    
    protected DLink next;
    protected DLink prev;
    
    /**
     * Default constructor
     */
    protected DLink() {
        this.next = null;
        this.prev = null;
    }
    
    /**
     * Clear the node's links
     */
    public void clear() {
        this.next = null;
        this.prev = null;
    }
    
    /**
     * Get the next node in the list
     * @return Next DLink node
     */
    public DLink getNext() {
        return this.next;
    }
    
    /**
     * Set the next node in the list
     * @param node Next DLink node
     */
    public void setNext(DLink node) {
        this.next = node;
    }
    
    /**
     * Get the previous node in the list
     * @return Previous DLink node
     */
    public DLink getPrev() {
        return this.prev;
    }
    
    /**
     * Set the previous node in the list
     * @param node Previous DLink node
     */
    public void setPrev(DLink node) {
        this.prev = node;
    }
    
    /**
     * Check if this node has a next node
     * @return true if next node exists
     */
    public boolean hasNext() {
        return this.next != null;
    }
    
    /**
     * Check if this node has a previous node
     * @return true if previous node exists
     */
    public boolean hasPrev() {
        return this.prev != null;
    }
    
    /**
     * Remove this node from the list
     * Updates the links of adjacent nodes
     */
    public void remove() {
        if (this.prev != null) {
            this.prev.next = this.next;
        }
        if (this.next != null) {
            this.next.prev = this.prev;
        }
        this.clear();
    }
    
    /**
     * Insert this node after the specified node
     * @param node Node to insert after
     */
    public void insertAfter(DLink node) {
        if (node == null) {
            return;
        }
        
        this.next = node.next;
        this.prev = node;
        
        if (node.next != null) {
            node.next.prev = this;
        }
        node.next = this;
    }
    
    /**
     * Insert this node before the specified node
     * @param node Node to insert before
     */
    public void insertBefore(DLink node) {
        if (node == null) {
            return;
        }
        
        this.prev = node.prev;
        this.next = node;
        
        if (node.prev != null) {
            node.prev.next = this;
        }
        node.prev = this;
    }
    
    /**
     * Abstract method to wash/reset the node
     * Must be implemented by subclasses to reset their state
     */
    protected abstract void wash();
    
    /**
     * Abstract method to dump node information
     * Used for debugging
     */
    public abstract void dump();
}
