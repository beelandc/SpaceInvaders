package net.beeland.spaceinvaders.gameobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShieldFactory class.
 * Tests shield creation, structure validation, and factory methods.
 */
class ShieldFactoryTest {

    @Test
    @DisplayName("Test createShield creates valid ShieldRoot")
    void testCreateShield_CreatesValidShieldRoot() {
        // Arrange
        float startX = 100.0f;
        float startY = 150.0f;
        
        // Act
        ShieldRoot shield = ShieldFactory.createShield(startX, startY);
        
        // Assert
        assertNotNull(shield, "Shield should not be null");
        assertEquals(GameObject.GameObjectName.SHIELD_ROOT, shield.getName(), 
            "Shield should have SHIELD_ROOT name");
        assertEquals(startX, shield.getX(), 0.001f, "Shield X position should match");
        assertEquals(startY, shield.getY(), 0.001f, "Shield Y position should match");
    }
    
    @Test
    @DisplayName("Test createShield creates 7 columns")
    void testCreateShield_Creates7Columns() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        
        // Assert
        assertEquals(7, shield.getNumChildren(), "Shield should have 7 columns");
    }
    
    @Test
    @DisplayName("Test createShield brick count per column")
    void testCreateShield_BrickCountPerColumn() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        
        // Assert - Expected brick counts: 10, 10, 8, 7, 8, 10, 10 = 63 total
        int[] expectedCounts = {10, 10, 8, 7, 8, 10, 10};
        int totalBricks = 0;
        
        for (int i = 0; i < shield.getNumChildren(); i++) {
            ShieldColumn column = (ShieldColumn) shield.getChildByIndex(i);
            int brickCount = column.getNumChildren();
            assertEquals(expectedCounts[i], brickCount,
                "Column " + i + " should have " + expectedCounts[i] + " bricks");
            totalBricks += brickCount;
        }
        
        assertEquals(63, totalBricks, "Shield should have 63 total bricks");
    }
    
    @Test
    @DisplayName("Test column 0 has 10 bricks with special top pieces")
    void testCreateShield_Column0Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column0 = (ShieldColumn) shield.getChildByIndex(0);
        
        // Assert
        assertEquals(10, column0.getNumChildren(), "Column 0 should have 10 bricks");
        
        // Check last two bricks are special types
        ShieldBrick brick8 = (ShieldBrick) column0.getChildByIndex(8);
        ShieldBrick brick9 = (ShieldBrick) column0.getChildByIndex(9);
        
        assertEquals(ShieldBrick.BrickType.LEFT_TOP_1, brick8.getBrickType(),
            "Brick 8 should be LEFT_TOP_1");
        assertEquals(ShieldBrick.BrickType.LEFT_TOP_0, brick9.getBrickType(),
            "Brick 9 should be LEFT_TOP_0");
    }
    
    @Test
    @DisplayName("Test column 1 has 10 regular bricks")
    void testCreateShield_Column1Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column1 = (ShieldColumn) shield.getChildByIndex(1);
        
        // Assert
        assertEquals(10, column1.getNumChildren(), "Column 1 should have 10 bricks");
        
        // All should be regular bricks
        for (int i = 0; i < column1.getNumChildren(); i++) {
            ShieldBrick brick = (ShieldBrick) column1.getChildByIndex(i);
            assertEquals(ShieldBrick.BrickType.BRICK, brick.getBrickType(),
                "Column 1 brick " + i + " should be regular BRICK");
        }
    }
    
    @Test
    @DisplayName("Test column 2 has 8 bricks with LEFT_BOTTOM")
    void testCreateShield_Column2Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column2 = (ShieldColumn) shield.getChildByIndex(2);
        
        // Assert
        assertEquals(8, column2.getNumChildren(), "Column 2 should have 8 bricks");
        
        // First brick should be LEFT_BOTTOM
        ShieldBrick brick0 = (ShieldBrick) column2.getChildByIndex(0);
        assertEquals(ShieldBrick.BrickType.LEFT_BOTTOM, brick0.getBrickType(),
            "First brick should be LEFT_BOTTOM");
    }
    
    @Test
    @DisplayName("Test column 3 has 7 bricks (creates gap)")
    void testCreateShield_Column3Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column3 = (ShieldColumn) shield.getChildByIndex(3);
        
        // Assert
        assertEquals(7, column3.getNumChildren(), 
            "Column 3 should have 7 bricks (shortest column)");
    }
    
    @Test
    @DisplayName("Test column 4 has 8 bricks with RIGHT_BOTTOM")
    void testCreateShield_Column4Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column4 = (ShieldColumn) shield.getChildByIndex(4);
        
        // Assert
        assertEquals(8, column4.getNumChildren(), "Column 4 should have 8 bricks");
        
        // First brick should be RIGHT_BOTTOM
        ShieldBrick brick0 = (ShieldBrick) column4.getChildByIndex(0);
        assertEquals(ShieldBrick.BrickType.RIGHT_BOTTOM, brick0.getBrickType(),
            "First brick should be RIGHT_BOTTOM");
    }
    
    @Test
    @DisplayName("Test column 5 has 10 regular bricks")
    void testCreateShield_Column5Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column5 = (ShieldColumn) shield.getChildByIndex(5);
        
        // Assert
        assertEquals(10, column5.getNumChildren(), "Column 5 should have 10 bricks");
    }
    
    @Test
    @DisplayName("Test column 6 has 10 bricks with special top pieces")
    void testCreateShield_Column6Structure() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        ShieldColumn column6 = (ShieldColumn) shield.getChildByIndex(6);
        
        // Assert
        assertEquals(10, column6.getNumChildren(), "Column 6 should have 10 bricks");
        
        // Check last two bricks are special types
        ShieldBrick brick8 = (ShieldBrick) column6.getChildByIndex(8);
        ShieldBrick brick9 = (ShieldBrick) column6.getChildByIndex(9);
        
        assertEquals(ShieldBrick.BrickType.RIGHT_TOP_1, brick8.getBrickType(),
            "Brick 8 should be RIGHT_TOP_1");
        assertEquals(ShieldBrick.BrickType.RIGHT_TOP_0, brick9.getBrickType(),
            "Brick 9 should be RIGHT_TOP_0");
    }
    
    @Test
    @DisplayName("Test createAllShields creates ShieldGroup")
    void testCreateAllShields_CreatesShieldGroup() {
        // Act
        ShieldGroup shieldGroup = ShieldFactory.createAllShields();
        
        // Assert
        assertNotNull(shieldGroup, "ShieldGroup should not be null");
        assertEquals(GameObject.GameObjectName.SHIELD_GROUP, shieldGroup.getName(),
            "ShieldGroup should have SHIELD_GROUP name");
    }
    
    @Test
    @DisplayName("Test createAllShields creates 4 shields")
    void testCreateAllShields_Creates4Shields() {
        // Act
        ShieldGroup shieldGroup = ShieldFactory.createAllShields();
        
        // Assert
        assertEquals(4, shieldGroup.getNumChildren(), 
            "ShieldGroup should contain 4 shields");
    }
    
    @Test
    @DisplayName("Test createAllShields creates shields at standard Y position")
    void testCreateAllShields_StandardYPosition() {
        // Act
        ShieldGroup shieldGroup = ShieldFactory.createAllShields();
        
        // Assert - All shields should be at Y=150
        float expectedY = 150.0f;
        
        for (int i = 0; i < 4; i++) {
            ShieldRoot shield = (ShieldRoot) shieldGroup.getChildByIndex(i);
            assertEquals(expectedY, shield.getY(), 0.001f,
                "Shield " + i + " Y position should be " + expectedY);
        }
    }
    
    @Test
    @DisplayName("Test getShieldWidth returns correct value")
    void testGetShieldWidth() {
        // Act
        float width = ShieldFactory.getShieldWidth();
        
        // Assert
        assertEquals(84.0f, width, 0.001f, 
            "Shield width should be 84 pixels (7 columns × 12px)");
    }
    
    @Test
    @DisplayName("Test getShieldHeight returns correct value")
    void testGetShieldHeight() {
        // Act
        float height = ShieldFactory.getShieldHeight();
        
        // Assert
        assertEquals(60.0f, height, 0.001f,
            "Shield height should be 60 pixels (10 bricks × 6px)");
    }
    
    @Test
    @DisplayName("Test getBrickWidth returns correct value")
    void testGetBrickWidth() {
        // Act
        float width = ShieldFactory.getBrickWidth();
        
        // Assert
        assertEquals(12.0f, width, 0.001f, "Brick width should be 12 pixels");
    }
    
    @Test
    @DisplayName("Test getBrickHeight returns correct value")
    void testGetBrickHeight() {
        // Act
        float height = ShieldFactory.getBrickHeight();
        
        // Assert
        assertEquals(6.0f, height, 0.001f, "Brick height should be 6 pixels");
    }
    
    
    @Test
    @DisplayName("Test all bricks have correct GameObject name")
    void testCreateShield_AllBricksHaveCorrectName() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        
        // Assert
        for (int col = 0; col < shield.getNumChildren(); col++) {
            ShieldColumn column = (ShieldColumn) shield.getChildByIndex(col);
            for (int row = 0; row < column.getNumChildren(); row++) {
                ShieldBrick brick = (ShieldBrick) column.getChildByIndex(row);
                assertEquals(GameObject.GameObjectName.SHIELD_BRICK, brick.getName(),
                    "Brick at column " + col + ", row " + row + " should have SHIELD_BRICK name");
            }
        }
    }
    
    @Test
    @DisplayName("Test all columns have correct GameObject name")
    void testCreateShield_AllColumnsHaveCorrectName() {
        // Arrange & Act
        ShieldRoot shield = ShieldFactory.createShield(100.0f, 150.0f);
        
        // Assert
        for (int col = 0; col < shield.getNumChildren(); col++) {
            ShieldColumn column = (ShieldColumn) shield.getChildByIndex(col);
            assertEquals(GameObject.GameObjectName.SHIELD_COLUMN, column.getName(),
                "Column " + col + " should have SHIELD_COLUMN name");
        }
    }
    
    @Test
    @DisplayName("Test createShield with zero position")
    void testCreateShield_WithZeroPosition() {
        // Act
        ShieldRoot shield = ShieldFactory.createShield(0.0f, 0.0f);
        
        // Assert
        assertNotNull(shield, "Shield should be created at origin");
        assertEquals(0.0f, shield.getX(), 0.001f, "Shield X should be 0");
        assertEquals(0.0f, shield.getY(), 0.001f, "Shield Y should be 0");
        assertEquals(7, shield.getNumChildren(), "Shield should still have 7 columns");
    }
    
    @Test
    @DisplayName("Test createShield with negative position")
    void testCreateShield_WithNegativePosition() {
        // Act
        ShieldRoot shield = ShieldFactory.createShield(-50.0f, -100.0f);
        
        // Assert
        assertNotNull(shield, "Shield should be created at negative position");
        assertEquals(-50.0f, shield.getX(), 0.001f, "Shield X should be -50");
        assertEquals(-100.0f, shield.getY(), 0.001f, "Shield Y should be -100");
    }
    
    @Test
    @DisplayName("Test createShield with large position values")
    void testCreateShield_WithLargePosition() {
        // Act
        ShieldRoot shield = ShieldFactory.createShield(10000.0f, 10000.0f);
        
        // Assert
        assertNotNull(shield, "Shield should be created at large position");
        assertEquals(10000.0f, shield.getX(), 0.001f, "Shield X should be 10000");
        assertEquals(10000.0f, shield.getY(), 0.001f, "Shield Y should be 10000");
    }
}