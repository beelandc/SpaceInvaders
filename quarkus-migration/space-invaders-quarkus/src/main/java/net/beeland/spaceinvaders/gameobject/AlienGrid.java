package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.manager.DLink;

/**
 * AlienGrid is a composite that manages the entire alien formation.
 * It contains 11 AlienGridColumn composites, each containing 5 aliens.
 * 
 * The grid handles:
 * - Horizontal movement (left/right)
 * - Vertical movement (down when hitting walls)
 * - Direction reversal when hitting boundaries
 * - Movement speed that increases as aliens are destroyed
 * 
 * Movement pattern:
 * 1. Move horizontally until hitting a wall
 * 2. Move down one row
 * 3. Reverse horizontal direction
 * 4. Repeat
 */
public class AlienGrid extends Composite {
    
    private boolean switchXDirection;
    private boolean directionChangePending;
    
    private float xDelta;
    private float yDelta;
    private float prevXDelta;

    /**
     * Creates an alien grid with the specified movement deltas.
     *
     * @param compositeName The composite name for this grid
     * @param xDelta The horizontal movement delta per update
     * @param yDelta The vertical movement delta when changing direction
     */
    public AlienGrid(CompositeName compositeName, float xDelta, float yDelta) {
        super(GameObjectName.ALIEN_GRID);
        this.setCompositeName(compositeName);
        this.xDelta = xDelta;
        this.yDelta = yDelta;
        this.prevXDelta = -1.0f;
        this.switchXDirection = false;
        this.directionChangePending = false;
    }

    /**
     * Accepts a collision visitor for double-dispatch collision handling.
     *
     * @param other The collision visitor
     */
    @Override
    public void accept(CollisionVisitor other) {
        other.visitAlienGrid(this);
    }

    /**
     * Visits a missile group for collision handling.
     * Delegates collision to individual columns.
     *
     * @param missileGroup The missile group to check collision with
     */
    public void visitMissileGroup(MissileGroup missileGroup) {
        // AlienGrid vs MissileGroup
        System.out.println("         collide:  " + missileGroup.getName() + " <-> " + this.getName());

        // MissileGroup vs Columns - delegate to children
        GameObject child = (GameObject) getFirstChild();
        while (child != null) {
            // Collision check would happen here via CollisionPair
            child = (GameObject) child.getNext();
        }
    }

    /**
     * Moves the entire alien grid and all its children.
     * Implements the three-step movement pattern:
     * 1. Initial movement after wall collision - move down
     * 2. Move all children with current deltas
     * 3. After moving down, switch to horizontal movement in opposite direction
     *
     * @param dummyX Unused - grid uses internal xDelta
     * @param dummyY Unused - grid uses internal yDelta
     */
    @Override
    public void move(float dummyX, float dummyY) {
        // Step 1: Initial movement since wall collision - Move down
        if (switchXDirection && !directionChangePending) {
            setXDelta(0.0f);
            setYDelta(-25.0f);  // Move down 25 pixels
            this.directionChangePending = true;
        }

        // Step 2: Move all children with current deltas
        DLink node = this.head;
        while (node != null) {
            net.beeland.spaceinvaders.composite.Component component = 
                (net.beeland.spaceinvaders.composite.Component) node;
            component.move(this.xDelta, this.yDelta);
            node = node.getNext();
        }

        // Step 3: Finally, grid has moved off wall, reset direction change flag
        if (directionChangePending && !switchXDirection) {
            this.directionChangePending = false;
        }

        // Step 2 (continued): Movement down complete, switch X direction
        if (switchXDirection) {
            // Reset deltas to horizontal movement in opposite direction
            this.yDelta = 0.0f;
            this.xDelta = (this.prevXDelta * -1.0f);
            this.switchXDirection = false;
        }
    }

    /**
     * Sets the flag to switch horizontal direction.
     * This is typically called when the grid hits a wall.
     *
     * @param switchDirection true to trigger direction switch
     */
    public void setSwitchXDirection(boolean switchDirection) {
        this.switchXDirection = switchDirection;
    }

    /**
     * Gets the switch direction flag.
     *
     * @return true if direction switch is pending
     */
    public boolean getSwitchXDirection() {
        return this.switchXDirection;
    }

    /**
     * Sets the horizontal movement delta.
     * Stores the previous delta for direction reversal.
     *
     * @param xDelta The new horizontal delta
     */
    public void setXDelta(float xDelta) {
        this.prevXDelta = this.xDelta;
        this.xDelta = xDelta;
    }

    /**
     * Sets the vertical movement delta.
     *
     * @param yDelta The new vertical delta
     */
    public void setYDelta(float yDelta) {
        this.yDelta = yDelta;
    }

    /**
     * Gets the current horizontal delta.
     *
     * @return The horizontal movement delta
     */
    public float getXDelta() {
        return this.xDelta;
    }

    /**
     * Gets the current vertical delta.
     *
     * @return The vertical movement delta
     */
    public float getYDelta() {
        return this.yDelta;
    }

    /**
     * Gets the previous horizontal delta.
     * Used for direction reversal.
     *
     * @return The previous horizontal delta
     */
    public float getPrevXDelta() {
        return this.prevXDelta;
    }

    /**
     * Sets the previous horizontal delta.
     *
     * @param prevXDelta The previous horizontal delta
     */
    public void setPrevXDelta(float prevXDelta) {
        this.prevXDelta = prevXDelta;
    }

    /**
     * Checks if a direction change is pending.
     *
     * @return true if the grid is in the process of changing direction
     */
    public boolean isDirectionChangePending() {
        return this.directionChangePending;
    }

    @Override
    public String toString() {
        return String.format("AlienGrid[name=%s, children=%d, xDelta=%.2f, yDelta=%.2f, switchDir=%b]",
                getName(), getNumChildren(), xDelta, yDelta, switchXDirection);
    }
}