package net.beeland.spaceinvaders.command;

import net.beeland.spaceinvaders.manager.DLink;

/**
 * Command Pattern - Abstract base class for all commands.
 * Commands encapsulate actions that can be executed at specific times
 * or in response to events.
 *
 * This is part of the Command design pattern implementation.
 */
public abstract class Command extends DLink {
    
    /**
     * Execute the command with the given delta time.
     *
     * @param deltaTime The time delta since the command was scheduled
     */
    public abstract void execute(float deltaTime);
    
    @Override
    protected void wash() {
        // Reset command state if needed
        this.clear();
    }
    
    @Override
    public void dump() {
        System.out.println("Command: " + this.getClass().getSimpleName());
    }
}