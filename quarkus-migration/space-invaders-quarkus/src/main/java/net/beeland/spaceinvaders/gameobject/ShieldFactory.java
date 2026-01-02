package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;

/**
 * ShieldFactory creates shield structures using the Factory pattern.
 *
 * Each shield consists of:
 * - 1 ShieldRoot (composite container)
 * - 7 ShieldColumn composites (vertical columns)
 * - 63 ShieldBrick objects (individual bricks)
 *
 * Shield structure (7 columns × varying heights):
 * Column 0: 10 bricks (8 regular + LeftTop1 + LeftTop0)
 * Column 1: 10 bricks (all regular)
 * Column 2: 8 bricks (LeftBottom + 7 regular, starts at row 2)
 * Column 3: 7 bricks (all regular, starts at row 3)
 * Column 4: 8 bricks (RightBottom + 7 regular, starts at row 2)
 * Column 5: 10 bricks (all regular)
 * Column 6: 10 bricks (8 regular + RightTop1 + RightTop0)
 *
 * Total: 63 bricks per shield (10+10+8+7+8+10+10)
 */
public class ShieldFactory {
    
    // Shield brick dimensions (from C# original)
    private static final float BRICK_WIDTH = 12.0f;
    private static final float BRICK_HEIGHT = 6.0f;
    
    /**
     * Creates a single shield at the specified position.
     * 
     * @param startX The x-coordinate for the shield's left edge
     * @param startY The y-coordinate for the shield's bottom edge
     * @return The ShieldRoot composite containing the complete shield structure
     */
    public static ShieldRoot createShield(float startX, float startY) {
        // Create the root composite for this shield
        ShieldRoot shieldRoot = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            startX,
            startY
        );
        
        // NOTE: Composite.add() adds to front of list, so we must add columns in reverse order (6 to 0)
        // and bricks within each column in reverse order (9 to 0)
        float offsetX = 6 * BRICK_WIDTH;  // Start from rightmost column
        
        // Column 6: Right edge with special top bricks (add in reverse order)
        ShieldColumn column6 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column6, ShieldBrick.BrickType.RIGHT_TOP_0, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.RIGHT_TOP_1, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 2 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 1 * BRICK_HEIGHT);
        addBrick(column6, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 0 * BRICK_HEIGHT);
        shieldRoot.add(column6);
        
        // Column 5: Full height (add in reverse order)
        offsetX -= BRICK_WIDTH;
        ShieldColumn column5 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 2 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 1 * BRICK_HEIGHT);
        addBrick(column5, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 0 * BRICK_HEIGHT);
        shieldRoot.add(column5);
        
        // Column 4: Right inner edge with bottom cutout (add in reverse order)
        offsetX -= BRICK_WIDTH;
        ShieldColumn column4 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        addBrick(column4, ShieldBrick.BrickType.RIGHT_BOTTOM, startX + offsetX, startY + 2 * BRICK_HEIGHT);
        shieldRoot.add(column4);
        
        // Column 3: Center column (shortest, creates the gap) (add in reverse order)
        offsetX -= BRICK_WIDTH;
        ShieldColumn column3 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column3, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        shieldRoot.add(column3);
        
        // Column 2: Left inner edge with bottom cutout (add in reverse order)
        offsetX -= BRICK_WIDTH;
        ShieldColumn column2 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        addBrick(column2, ShieldBrick.BrickType.LEFT_BOTTOM, startX + offsetX, startY + 2 * BRICK_HEIGHT);
        shieldRoot.add(column2);
        
        // Column 1: Full height (add in reverse order)
        offsetX -= BRICK_WIDTH;
        ShieldColumn column1 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 2 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 1 * BRICK_HEIGHT);
        addBrick(column1, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 0 * BRICK_HEIGHT);
        shieldRoot.add(column1);
        
        // Column 0: Left edge with special top bricks (add in reverse order)
        offsetX -= BRICK_WIDTH;
        ShieldColumn column0 = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0.0f, 0.0f);
        addBrick(column0, ShieldBrick.BrickType.LEFT_TOP_0, startX + offsetX, startY + 9 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.LEFT_TOP_1, startX + offsetX, startY + 8 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 7 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 6 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 5 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 4 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 3 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 2 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 1 * BRICK_HEIGHT);
        addBrick(column0, ShieldBrick.BrickType.BRICK, startX + offsetX, startY + 0 * BRICK_HEIGHT);
        shieldRoot.add(column0);
        
        return shieldRoot;
    }
    
    /**
     * Creates all 4 shields for the game at standard positions.
     * 
     * Standard shield positions (from C# original):
     * - Shield 1: x=60, y=150
     * - Shield 2: x=200, y=150
     * - Shield 3: x=340, y=150
     * - Shield 4: x=480, y=150
     * 
     * @return A ShieldGroup composite containing all 4 shields
     */
    public static ShieldGroup createAllShields() {
        ShieldGroup shieldGroup = new ShieldGroup(GameObject.GameObjectName.SHIELD_GROUP, 0.0f, 0.0f);
        
        // Create 4 shields at standard positions
        // Spacing: 140 pixels between shields
        float shieldY = 150.0f;
        float[] shieldXPositions = {60.0f, 200.0f, 340.0f, 480.0f};
        
        for (float shieldX : shieldXPositions) {
            ShieldRoot shield = createShield(shieldX, shieldY);
            shieldGroup.add(shield);
        }
        
        return shieldGroup;
    }
    
    /**
     * Helper method to create and add a brick to a column.
     * 
     * @param column The column to add the brick to
     * @param brickType The type of brick to create
     * @param x The x-coordinate of the brick
     * @param y The y-coordinate of the brick
     */
    private static void addBrick(ShieldColumn column, ShieldBrick.BrickType brickType, float x, float y) {
        ShieldBrick brick = new ShieldBrick(GameObject.GameObjectName.SHIELD_BRICK, brickType, x, y);
        column.add(brick);
    }
    
    /**
     * Gets the width of a single shield.
     * 
     * @return The width in pixels (7 columns × 12 pixels = 84 pixels)
     */
    public static float getShieldWidth() {
        return 7 * BRICK_WIDTH;  // 84 pixels
    }
    
    /**
     * Gets the height of a single shield.
     * 
     * @return The height in pixels (10 bricks × 6 pixels = 60 pixels)
     */
    public static float getShieldHeight() {
        return 10 * BRICK_HEIGHT;  // 60 pixels
    }
    
    /**
     * Gets the brick width constant.
     * 
     * @return The brick width in pixels
     */
    public static float getBrickWidth() {
        return BRICK_WIDTH;
    }
    
    /**
     * Gets the brick height constant.
     * 
     * @return The brick height in pixels
     */
    public static float getBrickHeight() {
        return BRICK_HEIGHT;
    }
}