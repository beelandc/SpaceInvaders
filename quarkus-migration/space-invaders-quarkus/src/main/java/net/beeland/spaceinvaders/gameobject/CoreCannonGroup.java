package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;

/**
 * CoreCannonGroup is a composite that manages the player's ships (lives).
 * In Space Invaders, the player starts with 3 lives (3 ships).
 * 
 * The group is responsible for:
 * - Managing multiple player ships (lives)
 * - Tracking the active ship
 * - Handling ship respawn after death
 * - Managing the lives count
 */
public class CoreCannonGroup extends Composite {
    
    private int livesRemaining;
    private CoreCannon activeShip;

    /**
     * Creates a core cannon group with the specified number of lives.
     *
     * @param lives The number of lives (ships) to start with
     */
    public CoreCannonGroup(int lives) {
        super(GameObjectName.CORE_CANNON_GROUP);
        this.setCompositeName(CompositeName.CORE_CANNON_GROUP);
        this.livesRemaining = lives;
        this.activeShip = null;
    }

    /**
     * Accepts a collision visitor for double-dispatch collision handling.
     *
     * @param other The collision visitor
     */
    @Override
    public void accept(CollisionVisitor other) {
        other.visitCoreCannonGroup(this);
    }

    /**
     * Gets the active player ship.
     *
     * @return The active ship, or null if none exists
     */
    public CoreCannon getActiveShip() {
        if (activeShip != null && !activeShip.isMarkedForDeath()) {
            return activeShip;
        }
        return null;
    }

    /**
     * Sets the active player ship.
     *
     * @param ship The ship to set as active
     */
    public void setActiveShip(CoreCannon ship) {
        this.activeShip = ship;
    }

    /**
     * Gets the number of lives remaining.
     *
     * @return The number of lives
     */
    public int getLivesRemaining() {
        return this.livesRemaining;
    }

    /**
     * Sets the number of lives remaining.
     *
     * @param lives The number of lives
     */
    public void setLivesRemaining(int lives) {
        this.livesRemaining = lives;
    }

    /**
     * Decrements the lives count.
     */
    public void loseLife() {
        if (livesRemaining > 0) {
            livesRemaining--;
        }
    }

    /**
     * Checks if the player has any lives remaining.
     *
     * @return true if lives > 0, false otherwise
     */
    public boolean hasLivesRemaining() {
        return livesRemaining > 0;
    }

    /**
     * Checks if the player is currently alive (has an active ship).
     *
     * @return true if active ship exists and is not dead, false otherwise
     */
    public boolean isAlive() {
        return getActiveShip() != null;
    }

    /**
     * Respawns the player ship at the starting position.
     * This is typically called after the player dies and has lives remaining.
     *
     * @param x The X position to spawn at
     * @param y The Y position to spawn at
     */
    public void respawn(float x, float y) {
        if (hasLivesRemaining() && !isAlive()) {
            // Create new ship at spawn position
            // This would typically be done by the game state
            System.out.println("Respawning player at (" + x + ", " + y + ")");
        }
    }

    @Override
    public String toString() {
        return String.format("CoreCannonGroup[name=%s, lives=%d, alive=%b, children=%d]",
                getName(), livesRemaining, isAlive(), getNumChildren());
    }
}