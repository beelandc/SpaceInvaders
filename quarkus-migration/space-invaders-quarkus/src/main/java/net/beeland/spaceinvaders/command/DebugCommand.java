package net.beeland.spaceinvaders.command;

/**
 * DebugCommand - A simple command for testing and debugging the timer system.
 * This command prints debug information when executed.
 */
public class DebugCommand extends Command {
    
    private String message;
    private int executionCount;
    
    /**
     * Constructor with custom message
     * 
     * @param message The message to print when executed
     */
    public DebugCommand(String message) {
        this.message = message;
        this.executionCount = 0;
    }
    
    /**
     * Default constructor
     */
    public DebugCommand() {
        this("Debug Command Executed");
    }
    
    @Override
    public void execute(float deltaTime) {
        this.executionCount++;
        System.out.println("******************************************");
        System.out.println(this.message);
        System.out.println("Delta Time: " + deltaTime);
        System.out.println("Execution Count: " + this.executionCount);
        System.out.println("******************************************");
    }
    
    /**
     * Get the number of times this command has been executed
     * 
     * @return The execution count
     */
    public int getExecutionCount() {
        return this.executionCount;
    }
    
    /**
     * Reset the execution count
     */
    public void resetExecutionCount() {
        this.executionCount = 0;
    }
    
    @Override
    public void dump() {
        System.out.println("DebugCommand:");
        System.out.println("  Message: " + this.message);
        System.out.println("  Execution Count: " + this.executionCount);
    }
}