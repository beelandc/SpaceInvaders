package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;

/**
 * AlienGridColumn is a composite that represents a vertical column of aliens.
 * Each column contains 5 aliens stacked vertically.
 * 
 * The column is responsible for:
 * - Managing the 5 aliens in the column
 * - Determining which alien is at the bottom (for bomb dropping)
 * - Handling collision with shields and other objects
 */
public class AlienGridColumn extends Composite {

    /**
     * Creates an alien grid column with the specified composite name.
     *
     * @param compositeName The composite name for this column (e.g., ALIEN_COL_1)
     */
    public AlienGridColumn(CompositeName compositeName) {
        super(GameObjectName.ALIEN_GRID_COLUMN);
        this.setCompositeName(compositeName);
    }

    /**
     * Accepts a collision visitor for double-dispatch collision handling.
     *
     * @param other The collision visitor
     */
    @Override
    public void accept(CollisionVisitor other) {
        other.visitAlienGridColumn(this);
    }

    /**
     * Visits a shield brick for collision handling.
     * Delegates collision to individual aliens in the column.
     *
     * @param shieldBrick The shield brick to check collision with
     */
    public void visitShieldBrick(ShieldBrick shieldBrick) {
        // AlienGridColumn vs ShieldBrick
        System.out.println("         collide:  " + shieldBrick.getName() + " <-> " + this.getName());

        // Alien vs ShieldBrick - delegate to children
        GameObject child = (GameObject) getFirstChild();
        while (child != null) {
            // Collision check would happen here via CollisionPair
            child = (GameObject) child.getNext();
        }
    }

    /**
     * Gets the bottom-most alien in this column.
     * This is used to determine which alien can drop bombs.
     *
     * @return The bottom alien, or null if column is empty
     */
    public GameObject getBottomAlien() {
        GameObject bottomAlien = null;
        GameObject child = (GameObject) getFirstChild();
        
        while (child != null) {
            if (!child.isMarkedForDeath()) {
                if (bottomAlien == null || child.getY() < bottomAlien.getY()) {
                    bottomAlien = child;
                }
            }
            child = (GameObject) child.getNext();
        }
        
        return bottomAlien;
    }

    /**
     * Checks if this column has any living aliens.
     *
     * @return true if at least one alien is alive, false otherwise
     */
    public boolean hasLivingAliens() {
        GameObject child = (GameObject) getFirstChild();
        
        while (child != null) {
            if (!child.isMarkedForDeath()) {
                return true;
            }
            child = (GameObject) child.getNext();
        }
        
        return false;
    }

    @Override
    public String toString() {
        return String.format("AlienGridColumn[name=%s, compositeName=%s, children=%d]",
                getName(), getCompositeName(), getNumChildren());
    }
}