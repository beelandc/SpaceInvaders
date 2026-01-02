package net.beeland.spaceinvaders.timer;

import net.beeland.spaceinvaders.command.Command;
import net.beeland.spaceinvaders.manager.DLink;

/**
 * TimeEvent represents a scheduled command execution.
 * Events are stored in a sorted list by trigger time and executed
 * when the current time reaches or exceeds the trigger time.
 */
public class TimeEvent extends DLink {
    
    /**
     * Event name enumeration for identifying different types of timer events
     */
    public enum Name {
        SPRITE_ANIMATION,
        SPRITE_MOVEMENT,
        MISSILE_MOVEMENT,
        ALIEN_GRID_MOVEMENT_SOUND,
        DROP_BOMB,
        DEBUG_COMMAND,
        DEPLOY_FLYING_SAUCER,
        RESET_LEVEL,
        ALIEN_GRID_MOVEMENT,
        RESET_CORE_CANNON,
        ANIMATION,
        UNINITIALIZED
    }
    
    private Name name;
    private Command command;
    private float triggerTime;
    private float deltaTime;
    
    /**
     * Default constructor
     */
    public TimeEvent() {
        super();
        this.name = Name.UNINITIALIZED;
        this.command = null;
        this.triggerTime = 0.0f;
        this.deltaTime = 0.0f;
    }
    
    /**
     * Set the time event properties
     * 
     * @param eventName The name/type of this event
     * @param command The command to execute when triggered
     * @param deltaTimeToTrigger Time from now until trigger
     * @param currentTime The current game time
     */
    public void set(Name eventName, Command command, float deltaTimeToTrigger, float currentTime) {
        assert command != null : "Command cannot be null";
        assert deltaTimeToTrigger >= 0.0f : "Delta time must be non-negative";
        
        this.name = eventName;
        this.command = command;
        this.deltaTime = deltaTimeToTrigger;
        this.triggerTime = currentTime + deltaTimeToTrigger;
    }
    
    /**
     * Process/execute this time event
     */
    public void process() {
        assert this.command != null : "Cannot process event with null command";
        this.command.execute(this.deltaTime);
    }
    
    @Override
    protected void wash() {
        this.clear();
        this.name = Name.UNINITIALIZED;
        this.command = null;
        this.triggerTime = 0.0f;
        this.deltaTime = 0.0f;
    }
    
    @Override
    public void dump() {
        System.out.println("TimeEvent:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Command: " + (this.command != null ? this.command.getClass().getSimpleName() : "null"));
        System.out.println("  Trigger Time: " + this.triggerTime);
        System.out.println("  Delta Time: " + this.deltaTime);
    }
    
    // Getters and Setters
    
    public Name getName() {
        return this.name;
    }
    
    public void setName(Name name) {
        this.name = name;
    }
    
    public Command getCommand() {
        return this.command;
    }
    
    public void setCommand(Command command) {
        this.command = command;
    }
    
    public float getTriggerTime() {
        return this.triggerTime;
    }
    
    public void setTriggerTime(float triggerTime) {
        this.triggerTime = triggerTime;
    }
    
    public float getDeltaTime() {
        return this.deltaTime;
    }
    
    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }
}