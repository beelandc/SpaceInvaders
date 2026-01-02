package net.beeland.spaceinvaders.timer;

import net.beeland.spaceinvaders.command.Command;
import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;

/**
 * TimerManager manages scheduled time events using the Object Pool pattern.
 * Events are stored in a sorted list by trigger time for efficient processing.
 * 
 * This manager uses CDI (@ApplicationScoped) for singleton behavior in Quarkus.
 */
@ApplicationScoped
public class TimerManager extends Manager {
    
    private TimeEvent compareNode;
    private float currentTime;
    
    /**
     * Default constructor for CDI
     */
    public TimerManager() {
        super();
        this.compareNode = new TimeEvent();
        this.currentTime = 0.0f;
    }
    
    /**
     * Initialize the manager after construction
     */
    @PostConstruct
    public void init() {
        initialize(3, 1);
    }
    
    /**
     * Add a new time event to the manager
     * 
     * @param eventName The name/type of the event
     * @param command The command to execute
     * @param deltaTimeToTrigger Time from now until trigger
     * @return The created TimeEvent
     */
    public TimeEvent add(TimeEvent.Name eventName, Command command, float deltaTimeToTrigger) {
        assert command != null : "Command cannot be null";
        assert deltaTimeToTrigger >= 0.0f : "Delta time must be non-negative";
        
        // Get a node from the pool
        TimeEvent newEvent = (TimeEvent) this.getFromPool();
        assert newEvent != null : "Failed to get TimeEvent from pool";
        
        // Set the event properties
        newEvent.set(eventName, command, deltaTimeToTrigger, this.currentTime);
        
        // Remove from active list head (getFromPool adds it at head)
        if (this.activeHead == newEvent) {
            this.activeHead = newEvent.getNext();
            if (this.activeHead != null) {
                this.activeHead.setPrev(null);
            }
        }
        
        // Add to active list in sorted order by trigger time
        addToActiveListInSortedOrder(newEvent);
        
        return newEvent;
    }
    
    /**
     * Find a time event by name
     * 
     * @param name The event name to search for
     * @return The found TimeEvent or null
     */
    public TimeEvent find(TimeEvent.Name name) {
        DLink current = this.activeHead;
        while (current != null) {
            TimeEvent event = (TimeEvent) current;
            if (event.getName() == name) {
                return event;
            }
            current = current.getNext();
        }
        return null;
    }
    
    /**
     * Remove a time event from the manager
     * 
     * @param event The event to remove
     */
    public void remove(TimeEvent event) {
        assert event != null : "Cannot remove null event";
        this.returnToPool(event);
    }
    
    /**
     * Pop the first event from the active list
     * Note: The returned event is removed from active list but NOT returned to pool
     * Caller is responsible for managing the event after popping
     *
     * @return The first TimeEvent or null if list is empty
     */
    public TimeEvent pop() {
        if (this.activeHead == null) {
            return null;
        }
        
        TimeEvent event = (TimeEvent) this.activeHead;
        
        // Remove from active list without washing
        this.activeHead = event.getNext();
        if (this.activeHead != null) {
            this.activeHead.setPrev(null);
        }
        event.clear();
        
        return event;
    }
    
    /**
     * Update the timer manager - process all events that should trigger
     * 
     * @param totalTime The current total game time
     */
    public void update(float totalTime) {
        // Latch the current time
        this.currentTime = totalTime;
        
        TimeEvent event = (TimeEvent) this.activeHead;
        
        // Process all events whose trigger time has been reached
        while (event != null && this.currentTime >= event.getTriggerTime()) {
            TimeEvent nextEvent = (TimeEvent) event.getNext();
            
            // Execute the event's command
            event.process();
            
            // Remove from active list
            this.returnToPool(event);
            
            // Move to next event
            event = nextEvent;
        }
    }
    
    /**
     * Get the current time
     * 
     * @return The current time
     */
    public float getCurrentTime() {
        return this.currentTime;
    }
    
    /**
     * Add a time event to the active list in sorted order by trigger time
     * 
     * @param newEvent The event to add
     */
    private void addToActiveListInSortedOrder(TimeEvent newEvent) {
        newEvent.clear();
        
        // If list is empty or new event should be first
        if (this.activeHead == null) {
            this.activeHead = newEvent;
            return;
        }
        
        TimeEvent current = (TimeEvent) this.activeHead;
        
        // Check if should be new head
        if (newEvent.getTriggerTime() <= current.getTriggerTime()) {
            newEvent.setNext(this.activeHead);
            this.activeHead.setPrev(newEvent);
            this.activeHead = newEvent;
            return;
        }
        
        // Find the correct position in the sorted list
        while (current != null) {
            TimeEvent next = (TimeEvent) current.getNext();
            
            // If we're at the end or found the insertion point
            if (next == null || newEvent.getTriggerTime() <= next.getTriggerTime()) {
                newEvent.setNext(current.getNext());
                newEvent.setPrev(current);
                
                if (current.getNext() != null) {
                    current.getNext().setPrev(newEvent);
                }
                current.setNext(newEvent);
                break;
            }
            
            current = next;
        }
    }
    
    @Override
    protected DLink createNode() {
        return new TimeEvent();
    }
}