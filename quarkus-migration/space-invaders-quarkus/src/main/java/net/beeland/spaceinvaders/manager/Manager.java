package net.beeland.spaceinvaders.manager;

import org.jboss.logging.Logger;

/**
 * Abstract Manager base class implementing Object Pool pattern
 * Manages a pool of reusable objects to avoid garbage collection overhead
 * All manager classes inherit from this base class
 */
public abstract class Manager {
    
    private static final Logger LOG = Logger.getLogger(Manager.class);
    
    // Head of active list (objects currently in use)
    protected DLink activeHead;
    
    // Head of reserved list (objects available for reuse)
    protected DLink reservedHead;
    
    // Statistics
    protected int totalActive;
    protected int totalReserved;
    protected int peakActive;
    
    /**
     * Constructor
     * Initializes the manager with empty lists
     */
    protected Manager() {
        this.activeHead = null;
        this.reservedHead = null;
        this.totalActive = 0;
        this.totalReserved = 0;
        this.peakActive = 0;
    }
    
    /**
     * Initialize the manager
     * Preallocates objects to the reserved pool
     * @param reserveSize Number of objects to preallocate
     * @param growSize Number of objects to add when pool is exhausted
     */
    protected void initialize(int reserveSize, int growSize) {
        LOG.info("Initializing " + this.getClass().getSimpleName() + 
                 " with reserve: " + reserveSize + ", grow: " + growSize);
        
        // Preallocate objects to reserved pool
        for (int i = 0; i < reserveSize; i++) {
            DLink node = createNode();
            addToReserved(node);
        }
    }
    
    /**
     * Get an object from the pool
     * If reserved pool is empty, creates new objects
     * @return Object from pool
     */
    protected DLink getFromPool() {
        DLink node;
        
        if (reservedHead == null) {
            // Pool exhausted, grow it
            LOG.debug("Pool exhausted for " + this.getClass().getSimpleName() + ", growing...");
            grow();
        }
        
        // Get from reserved list
        node = reservedHead;
        if (node != null) {
            reservedHead = node.getNext();
            if (reservedHead != null) {
                reservedHead.setPrev(null);
            }
            totalReserved--;
            
            // Add to active list
            addToActive(node);
        }
        
        return node;
    }
    
    /**
     * Return an object to the pool
     * @param node Object to return
     */
    protected void returnToPool(DLink node) {
        if (node == null) {
            return;
        }
        
        // Remove from active list
        removeFromActive(node);
        
        // Wash the node (reset its state)
        node.wash();
        
        // Add to reserved list
        addToReserved(node);
    }
    
    /**
     * Add a node to the active list
     * @param node Node to add
     */
    private void addToActive(DLink node) {
        if (node == null) {
            return;
        }
        
        node.clear();
        
        if (activeHead == null) {
            activeHead = node;
            node.setPrev(null);
            node.setNext(null);
        } else {
            node.setNext(activeHead);
            node.setPrev(null);  // New head has no previous node
            activeHead.setPrev(node);
            activeHead = node;
        }
        
        totalActive++;
        if (totalActive > peakActive) {
            peakActive = totalActive;
        }
    }
    
    /**
     * Remove a node from the active list
     * @param node Node to remove
     */
    private void removeFromActive(DLink node) {
        if (node == null) {
            return;
        }
        
        if (node == activeHead) {
            activeHead = node.getNext();
            if (activeHead != null) {
                activeHead.setPrev(null);
            }
        } else {
            node.remove();
        }
        
        totalActive--;
    }
    
    /**
     * Add a node to the reserved list
     * @param node Node to add
     */
    private void addToReserved(DLink node) {
        if (node == null) {
            return;
        }
        
        node.clear();
        
        if (reservedHead == null) {
            reservedHead = node;
        } else {
            node.setNext(reservedHead);
            reservedHead.setPrev(node);
            reservedHead = node;
        }
        
        totalReserved++;
    }
    
    /**
     * Grow the reserved pool
     * Creates additional objects when pool is exhausted
     */
    private void grow() {
        int growSize = 5; // Default grow size
        for (int i = 0; i < growSize; i++) {
            DLink node = createNode();
            addToReserved(node);
        }
        LOG.debug("Grew pool by " + growSize + " objects");
    }
    
    /**
     * Get the head of the active list
     * @return Head of active list
     */
    public DLink getActiveHead() {
        return activeHead;
    }
    
    /**
     * Get statistics about the pool
     * @return String with pool statistics
     */
    public String getStats() {
        return String.format("%s - Active: %d, Reserved: %d, Peak: %d",
                           this.getClass().getSimpleName(),
                           totalActive, totalReserved, peakActive);
    }
    
    /**
     * Dump all active objects (for debugging)
     */
    public void dumpActive() {
        LOG.info("=== " + this.getClass().getSimpleName() + " Active List ===");
        DLink current = activeHead;
        int count = 0;
        while (current != null) {
            current.dump();
            current = current.getNext();
            count++;
        }
        LOG.info("Total active objects: " + count);
    }
    
    /**
     * Abstract method to create a new node
     * Must be implemented by subclasses
     * @return New DLink node
     */
    protected abstract DLink createNode();
    
    /**
     * Destroy the manager and cleanup resources
     */
    public void destroy() {
        LOG.info("Destroying " + this.getClass().getSimpleName());
        LOG.info("Final stats: " + getStats());
        
        // Clear active list
        activeHead = null;
        
        // Clear reserved list
        reservedHead = null;
        
        totalActive = 0;
        totalReserved = 0;
        peakActive = 0;
    }
}
