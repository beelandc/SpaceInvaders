package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.sprite.SpriteProxy;

/**
 * CollisionObject manages collision detection for game objects.
 * It maintains a collision rectangle and provides methods to update position.
 * 
 * Note: Visual collision box rendering (SpriteBox) will be added in a future phase.
 */
public class CollisionObject {
    private final CollisionRect collisionRect;
    // TODO: Add SpriteBox for visual debugging in future phase

    /**
     * Creates a collision object based on a sprite proxy.
     * The collision rectangle is initialized to match the sprite's dimensions.
     *
     * @param spriteProxy The sprite proxy to base collision bounds on
     */
    public CollisionObject(SpriteProxy spriteProxy) {
        if (spriteProxy == null) {
            throw new IllegalArgumentException("SpriteProxy cannot be null");
        }

        // Initialize collision rectangle based on sprite dimensions
        // For now, we'll use default dimensions - will be updated when sprite system is complete
        this.collisionRect = new CollisionRect(0.0f, 0.0f, 32.0f, 32.0f);
        
        // TODO: Extract actual dimensions from sprite when sprite system is complete
        // Sprite sprite = spriteProxy.getSprite();
        // this.collisionRect = new CollisionRect(sprite.getScreenRect());
    }

    /**
     * Creates a collision object with specified dimensions.
     *
     * @param x      Center X coordinate
     * @param y      Center Y coordinate
     * @param width  Width of collision rectangle
     * @param height Height of collision rectangle
     */
    public CollisionObject(float x, float y, float width, float height) {
        this.collisionRect = new CollisionRect(x, y, width, height);
    }

    /**
     * Updates the position of the collision rectangle.
     *
     * @param x New center X coordinate
     * @param y New center Y coordinate
     */
    public void updatePos(float x, float y) {
        this.collisionRect.x = x;
        this.collisionRect.y = y;
        
        // TODO: Update visual collision box when SpriteBox is implemented
    }

    /**
     * Gets the collision rectangle.
     *
     * @return The collision rectangle
     */
    public CollisionRect getCollisionRect() {
        return this.collisionRect;
    }

    /**
     * Sets the dimensions of the collision rectangle.
     *
     * @param width  New width
     * @param height New height
     */
    public void setDimensions(float width, float height) {
        this.collisionRect.width = width;
        this.collisionRect.height = height;
    }

    @Override
    public String toString() {
        return "CollisionObject[rect=" + collisionRect + "]";
    }
}