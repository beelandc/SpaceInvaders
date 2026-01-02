package net.beeland.spaceinvaders.collision;

/**
 * CollisionRect represents a rectangular collision boundary.
 * The rectangle is defined by a center point (x, y) and dimensions (width, height).
 * This class provides methods for intersection testing and union operations.
 */
public class CollisionRect {
    public float x;
    public float y;
    public float width;
    public float height;

    /**
     * Creates a collision rectangle with specified parameters.
     *
     * @param x      Center X coordinate
     * @param y      Center Y coordinate
     * @param width  Width of the rectangle
     * @param height Height of the rectangle
     */
    public CollisionRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a collision rectangle by copying another rectangle.
     *
     * @param other The rectangle to copy
     */
    public CollisionRect(CollisionRect other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

    /**
     * Creates a collision rectangle with default values (0, 0, 0, 0).
     */
    public CollisionRect() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * Sets the rectangle's properties.
     *
     * @param x      Center X coordinate
     * @param y      Center Y coordinate
     * @param width  Width of the rectangle
     * @param height Height of the rectangle
     */
    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the rectangle's properties by copying another rectangle.
     *
     * @param other The rectangle to copy
     */
    public void set(CollisionRect other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

    /**
     * Tests if two collision rectangles intersect.
     *
     * @param rectA First rectangle
     * @param rectB Second rectangle
     * @return true if the rectangles intersect, false otherwise
     */
    public static boolean intersect(CollisionRect rectA, CollisionRect rectB) {
        // Calculate bounds for rectangle A
        float aMinX = rectA.x - rectA.width / 2;
        float aMaxX = rectA.x + rectA.width / 2;
        float aMinY = rectA.y - rectA.height / 2;
        float aMaxY = rectA.y + rectA.height / 2;

        // Calculate bounds for rectangle B
        float bMinX = rectB.x - rectB.width / 2;
        float bMaxX = rectB.x + rectB.width / 2;
        float bMinY = rectB.y - rectB.height / 2;
        float bMaxY = rectB.y + rectB.height / 2;

        // Trivial reject - if any of these conditions are true, no intersection
        if (bMaxX < aMinX || bMinX > aMaxX || bMaxY < aMinY || bMinY > aMaxY) {
            return false;
        }

        return true;
    }

    /**
     * Computes the union of this rectangle with another rectangle.
     * This rectangle is modified to contain both rectangles.
     *
     * @param other The rectangle to union with
     */
    public void union(CollisionRect other) {
        // Calculate minimum X (leftmost edge)
        float minX = Math.min(this.x - this.width / 2, other.x - other.width / 2);

        // Calculate maximum X (rightmost edge)
        float maxX = Math.max(this.x + this.width / 2, other.x + other.width / 2);

        // Calculate maximum Y (topmost edge)
        float maxY = Math.max(this.y + this.height / 2, other.y + other.height / 2);

        // Calculate minimum Y (bottommost edge)
        float minY = Math.min(this.y - this.height / 2, other.y - other.height / 2);

        // Update this rectangle to contain both
        this.width = maxX - minX;
        this.height = maxY - minY;
        this.x = minX + this.width / 2;
        this.y = minY + this.height / 2;
    }

    /**
     * Gets the minimum X coordinate (left edge).
     *
     * @return The minimum X coordinate
     */
    public float getMinX() {
        return x - width / 2;
    }

    /**
     * Gets the maximum X coordinate (right edge).
     *
     * @return The maximum X coordinate
     */
    public float getMaxX() {
        return x + width / 2;
    }

    /**
     * Gets the minimum Y coordinate (bottom edge).
     *
     * @return The minimum Y coordinate
     */
    public float getMinY() {
        return y - height / 2;
    }

    /**
     * Gets the maximum Y coordinate (top edge).
     *
     * @return The maximum Y coordinate
     */
    public float getMaxY() {
        return y + height / 2;
    }

    @Override
    public String toString() {
        return String.format("CollisionRect[x=%.2f, y=%.2f, width=%.2f, height=%.2f]",
                x, y, width, height);
    }
}