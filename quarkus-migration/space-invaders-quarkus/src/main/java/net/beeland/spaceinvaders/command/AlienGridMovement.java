package net.beeland.spaceinvaders.command;

import net.beeland.spaceinvaders.composite.Component;
import net.beeland.spaceinvaders.gameobject.AlienGrid;
import net.beeland.spaceinvaders.timer.TimerManager;

/**
 * AlienGridMovement is a command that moves the alien grid.
 * 
 * This command is scheduled by the TimerManager and executes periodically
 * to move the entire alien formation. The movement logic is handled by
 * the AlienGrid.move() method, which implements the classic Space Invaders
 * movement pattern (horizontal until hitting a wall, then down and reverse).
 * 
 * After executing, the command reschedules itself to create continuous movement.
 * The movement speed can be adjusted by changing the timer interval.
 */
public class AlienGridMovement extends Command {
    
    private Component alienGrid;
    private float movementSpeed;  // Time interval between movements (in seconds)

    /**
     * Creates an alien grid movement command for the specified component.
     *
     * @param alienGrid The alien grid component to move
     * @param movementSpeed The time interval between movements (in seconds)
     */
    public AlienGridMovement(Component alienGrid, float movementSpeed) {
        if (alienGrid == null) {
            throw new IllegalArgumentException("AlienGrid cannot be null");
        }
        this.alienGrid = alienGrid;
        this.movementSpeed = movementSpeed;
    }

    /**
     * Creates an alien grid movement command with default speed.
     *
     * @param alienGrid The alien grid component to move
     */
    public AlienGridMovement(Component alienGrid) {
        this(alienGrid, 1.0f);  // Default: 1 second between movements
    }

    /**
     * Executes the movement command.
     * 
     * This method:
     * 1. Calls the alien grid's move() method (which handles the actual movement logic)
     * 2. Reschedules itself for the next movement
     * 
     * The movement deltas are managed internally by the AlienGrid class,
     * so we pass 0.0f for both parameters.
     *
     * @param deltaTime The time delta since the command was scheduled (unused)
     */
    @Override
    public void execute(float deltaTime) {
        // Move the alien grid
        // The AlienGrid.move() method handles all movement logic internally
        alienGrid.move(0.0f, 0.0f);
        
        // TODO: Reschedule this command for the next movement
        // This will be implemented once TimerManager integration is complete
        // TimerManager.add(TimeEvent.Name.ALIEN_GRID_MOVEMENT, this, movementSpeed);
        
        System.out.println("AlienGridMovement executed - Speed: " + movementSpeed + "s");
    }

    /**
     * Gets the alien grid component being moved.
     *
     * @return The alien grid component
     */
    public Component getAlienGrid() {
        return this.alienGrid;
    }

    /**
     * Gets the current movement speed (time interval between movements).
     *
     * @return The movement speed in seconds
     */
    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    /**
     * Sets the movement speed (time interval between movements).
     * This allows the game to speed up as aliens are destroyed.
     *
     * @param movementSpeed The new movement speed in seconds
     */
    public void setMovementSpeed(float movementSpeed) {
        if (movementSpeed <= 0) {
            throw new IllegalArgumentException("Movement speed must be positive");
        }
        this.movementSpeed = movementSpeed;
    }

    /**
     * Increases the movement speed by reducing the time interval.
     * This is typically called when aliens are destroyed.
     *
     * @param speedIncrease The amount to decrease the interval by (in seconds)
     */
    public void increaseSpeed(float speedIncrease) {
        float newSpeed = this.movementSpeed - speedIncrease;
        if (newSpeed < 0.1f) {
            newSpeed = 0.1f;  // Minimum speed cap
        }
        this.movementSpeed = newSpeed;
    }

    @Override
    protected void wash() {
        super.wash();
        this.alienGrid = null;
        this.movementSpeed = 1.0f;
    }

    @Override
    public void dump() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String gridInfo = (alienGrid instanceof AlienGrid) 
            ? ((AlienGrid) alienGrid).getName().toString()
            : "Unknown";
        return String.format("AlienGridMovement[grid=%s, speed=%.2fs]", 
                gridInfo, movementSpeed);
    }
}