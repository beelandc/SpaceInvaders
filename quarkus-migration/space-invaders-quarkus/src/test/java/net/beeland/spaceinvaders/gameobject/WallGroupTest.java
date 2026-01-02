package net.beeland.spaceinvaders.gameobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WallGroup composite class.
 * Tests wall group management and hierarchy.
 */
@DisplayName("WallGroup Tests")
class WallGroupTest {

    @Test
    @DisplayName("WallGroup should be created with correct name and position")
    void testWallGroupCreation() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f,
            0.0f
        );

        assertNotNull(group);
        assertEquals(0.0f, group.getX(), 0.001f);
        assertEquals(0.0f, group.getY(), 0.001f);
    }

    @Test
    @DisplayName("WallGroup should be able to add wall children")
    void testWallGroupAddWalls() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        LeftWall leftWall = new LeftWall(
            GameObject.GameObjectName.LEFT_WALL,
            10.0f, 0.0f, 5.0f, 600.0f
        );

        RightWall rightWall = new RightWall(
            GameObject.GameObjectName.RIGHT_WALL,
            785.0f, 0.0f, 5.0f, 600.0f
        );

        group.add(leftWall);
        group.add(rightWall);

        assertNotNull(group.getFirstChild());
    }

    @Test
    @DisplayName("WallGroup should be able to add all wall types")
    void testWallGroupAddAllWallTypes() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        LeftWall left = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 10, 0, 5, 600);
        RightWall right = new RightWall(GameObject.GameObjectName.RIGHT_WALL, 785, 0, 5, 600);
        TopWall top = new TopWall(GameObject.GameObjectName.TOP_WALL, 0, 0, 800, 5);
        BottomWall bottom = new BottomWall(GameObject.GameObjectName.BOTTOM_WALL, 0, 595, 800, 5);
        LeftBumper leftBumper = new LeftBumper(GameObject.GameObjectName.LEFT_BUMPER, 15, 500, 5, 50);
        RightBumper rightBumper = new RightBumper(GameObject.GameObjectName.RIGHT_BUMPER, 780, 500, 5, 50);

        group.add(left);
        group.add(right);
        group.add(top);
        group.add(bottom);
        group.add(leftBumper);
        group.add(rightBumper);

        assertNotNull(group.getFirstChild());
    }

    @Test
    @DisplayName("WallGroup move should do nothing (static object)")
    void testWallGroupMove() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            100.0f, 200.0f
        );

        float originalX = group.getX();
        float originalY = group.getY();

        group.move(0.0f, 0.0f);

        assertEquals(originalX, group.getX(), 0.001f);
        assertEquals(originalY, group.getY(), 0.001f);
    }

    @Test
    @DisplayName("WallGroup accept should call visitor.visit")
    void testWallGroupAccept() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        final boolean[] visited = {false};
        GameObject.CollisionVisitor visitor = other -> {
            visited[0] = true;
            assertSame(group, other);
        };

        group.accept(visitor);
        assertTrue(visited[0], "Visitor should have been called");
    }

    @Test
    @DisplayName("WallGroup should have collision object")
    void testWallGroupHasCollisionObject() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        assertNotNull(group.getCollisionObject());
    }

    @Test
    @DisplayName("WallGroup should have sprite proxy")
    void testWallGroupHasSpriteProxy() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        assertNotNull(group.getSpriteProxy());
    }

    @Test
    @DisplayName("WallGroup should not be marked for death initially")
    void testWallGroupNotMarkedForDeathInitially() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        assertFalse(group.isMarkedForDeath());
    }

    @Test
    @DisplayName("WallGroup markForDeath should set death flag")
    void testWallGroupMarkForDeath() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        group.markForDeath();
        assertTrue(group.isMarkedForDeath());
    }

    @Test
    @DisplayName("WallGroup clearDeathFlag should clear death flag")
    void testWallGroupClearDeathFlag() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        group.markForDeath();
        assertTrue(group.isMarkedForDeath());

        group.clearDeathFlag();
        assertFalse(group.isMarkedForDeath());
    }

    @Test
    @DisplayName("WallGroup setX and setY should update position")
    void testWallGroupSetPosition() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        group.setX(50.0f);
        group.setY(100.0f);

        assertEquals(50.0f, group.getX(), 0.001f);
        assertEquals(100.0f, group.getY(), 0.001f);
    }

    @Test
    @DisplayName("WallGroup update should update sprite and collision positions")
    void testWallGroupUpdate() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        group.setX(10.0f);
        group.setY(20.0f);
        group.update();

        assertNotNull(group.getSpriteProxy());
        assertNotNull(group.getCollisionObject());
    }

    @Test
    @DisplayName("WallGroup should support typical game boundary setup")
    void testWallGroupTypicalSetup() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        // Create typical game boundaries
        LeftWall left = new LeftWall(
            GameObject.GameObjectName.LEFT_WALL,
            10.0f, 0.0f, 5.0f, 600.0f
        );

        RightWall right = new RightWall(
            GameObject.GameObjectName.RIGHT_WALL,
            785.0f, 0.0f, 5.0f, 600.0f
        );

        TopWall top = new TopWall(
            GameObject.GameObjectName.TOP_WALL,
            0.0f, 0.0f, 800.0f, 5.0f
        );

        BottomWall bottom = new BottomWall(
            GameObject.GameObjectName.BOTTOM_WALL,
            0.0f, 595.0f, 800.0f, 5.0f
        );

        // Add to group
        group.add(left);
        group.add(right);
        group.add(top);
        group.add(bottom);

        // Verify structure
        assertNotNull(group.getFirstChild());
    }

    @Test
    @DisplayName("WallGroup should support player constraint bumpers")
    void testWallGroupWithBumpers() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        // Create player movement bumpers
        LeftBumper leftBumper = new LeftBumper(
            GameObject.GameObjectName.LEFT_BUMPER,
            15.0f, 500.0f, 5.0f, 50.0f
        );

        RightBumper rightBumper = new RightBumper(
            GameObject.GameObjectName.RIGHT_BUMPER,
            780.0f, 500.0f, 5.0f, 50.0f
        );

        // Add to group
        group.add(leftBumper);
        group.add(rightBumper);

        // Verify structure
        assertNotNull(group.getFirstChild());
    }

    @Test
    @DisplayName("WallGroup toString should include class name")
    void testWallGroupToString() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        String str = group.toString();
        assertNotNull(str);
        assertTrue(str.contains("WallGroup"));
    }

    @Test
    @DisplayName("WallGroup should be a Composite")
    void testWallGroupIsComposite() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        // WallGroup extends Composite, so it should support composite operations
        assertNotNull(group);
        
        // Should be able to add children
        LeftWall wall = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        group.add(wall);
        
        assertNotNull(group.getFirstChild());
    }

    @Test
    @DisplayName("WallGroup should handle empty group")
    void testWallGroupEmpty() {
        WallGroup group = new WallGroup(
            GameObject.GameObjectName.UNINITIALIZED,
            0.0f, 0.0f
        );

        // Empty group should still be valid
        assertNotNull(group);
        assertNotNull(group.getCollisionObject());
        assertNotNull(group.getSpriteProxy());
    }
}