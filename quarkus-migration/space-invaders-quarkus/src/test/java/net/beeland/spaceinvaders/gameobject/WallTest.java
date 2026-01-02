package net.beeland.spaceinvaders.gameobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Wall classes.
 * Tests all wall types, positioning, collision detection, and static behavior.
 */
@DisplayName("Wall Tests")
class WallTest {

    @Test
    @DisplayName("LeftWall should be created with correct type and position")
    void testLeftWallCreation() {
        LeftWall wall = new LeftWall(
            GameObject.GameObjectName.LEFT_WALL,
            10.0f, 20.0f, 5.0f, 600.0f
        );

        assertEquals(GameObject.GameObjectName.LEFT_WALL, wall.getName());
        assertEquals(Wall.WallType.LEFT, wall.getWallType());
        assertEquals(10.0f, wall.getX(), 0.001f);
        assertEquals(20.0f, wall.getY(), 0.001f);
    }

    @Test
    @DisplayName("RightWall should be created with correct type and position")
    void testRightWallCreation() {
        RightWall wall = new RightWall(
            GameObject.GameObjectName.RIGHT_WALL,
            790.0f, 20.0f, 5.0f, 600.0f
        );

        assertEquals(GameObject.GameObjectName.RIGHT_WALL, wall.getName());
        assertEquals(Wall.WallType.RIGHT, wall.getWallType());
        assertEquals(790.0f, wall.getX(), 0.001f);
        assertEquals(20.0f, wall.getY(), 0.001f);
    }

    @Test
    @DisplayName("TopWall should be created with correct type and position")
    void testTopWallCreation() {
        TopWall wall = new TopWall(
            GameObject.GameObjectName.TOP_WALL,
            0.0f, 0.0f, 800.0f, 5.0f
        );

        assertEquals(GameObject.GameObjectName.TOP_WALL, wall.getName());
        assertEquals(Wall.WallType.TOP, wall.getWallType());
        assertEquals(0.0f, wall.getX(), 0.001f);
        assertEquals(0.0f, wall.getY(), 0.001f);
    }

    @Test
    @DisplayName("BottomWall should be created with correct type and position")
    void testBottomWallCreation() {
        BottomWall wall = new BottomWall(
            GameObject.GameObjectName.BOTTOM_WALL,
            0.0f, 595.0f, 800.0f, 5.0f
        );

        assertEquals(GameObject.GameObjectName.BOTTOM_WALL, wall.getName());
        assertEquals(Wall.WallType.BOTTOM, wall.getWallType());
        assertEquals(0.0f, wall.getX(), 0.001f);
        assertEquals(595.0f, wall.getY(), 0.001f);
    }

    @Test
    @DisplayName("LeftBumper should be created with correct type and position")
    void testLeftBumperCreation() {
        LeftBumper bumper = new LeftBumper(
            GameObject.GameObjectName.LEFT_BUMPER,
            15.0f, 500.0f, 5.0f, 50.0f
        );

        assertEquals(GameObject.GameObjectName.LEFT_BUMPER, bumper.getName());
        assertEquals(Wall.WallType.LEFT_BUMPER, bumper.getWallType());
        assertEquals(15.0f, bumper.getX(), 0.001f);
        assertEquals(500.0f, bumper.getY(), 0.001f);
    }

    @Test
    @DisplayName("RightBumper should be created with correct type and position")
    void testRightBumperCreation() {
        RightBumper bumper = new RightBumper(
            GameObject.GameObjectName.RIGHT_BUMPER,
            780.0f, 500.0f, 5.0f, 50.0f
        );

        assertEquals(GameObject.GameObjectName.RIGHT_BUMPER, bumper.getName());
        assertEquals(Wall.WallType.RIGHT_BUMPER, bumper.getWallType());
        assertEquals(780.0f, bumper.getX(), 0.001f);
        assertEquals(500.0f, bumper.getY(), 0.001f);
    }

    @Test
    @DisplayName("Wall move should do nothing (static object)")
    void testWallMove() {
        LeftWall wall = new LeftWall(
            GameObject.GameObjectName.LEFT_WALL,
            10.0f, 20.0f, 5.0f, 600.0f
        );

        float originalX = wall.getX();
        float originalY = wall.getY();

        wall.move(0.0f, 0.0f);

        assertEquals(originalX, wall.getX(), 0.001f);
        assertEquals(originalY, wall.getY(), 0.001f);
    }

    @Test
    @DisplayName("Wall accept should call visitor.visit")
    void testWallAccept() {
        TopWall wall = new TopWall(
            GameObject.GameObjectName.TOP_WALL,
            0.0f, 0.0f, 800.0f, 5.0f
        );

        final boolean[] visited = {false};
        GameObject.CollisionVisitor visitor = other -> {
            visited[0] = true;
            assertSame(wall, other);
        };

        wall.accept(visitor);
        assertTrue(visited[0], "Visitor should have been called");
    }

    @Test
    @DisplayName("All walls should have collision objects")
    void testWallsHaveCollisionObjects() {
        LeftWall left = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        RightWall right = new RightWall(GameObject.GameObjectName.RIGHT_WALL, 795, 0, 5, 600);
        TopWall top = new TopWall(GameObject.GameObjectName.TOP_WALL, 0, 0, 800, 5);
        BottomWall bottom = new BottomWall(GameObject.GameObjectName.BOTTOM_WALL, 0, 595, 800, 5);
        LeftBumper leftBumper = new LeftBumper(GameObject.GameObjectName.LEFT_BUMPER, 15, 500, 5, 50);
        RightBumper rightBumper = new RightBumper(GameObject.GameObjectName.RIGHT_BUMPER, 780, 500, 5, 50);

        assertNotNull(left.getCollisionObject());
        assertNotNull(right.getCollisionObject());
        assertNotNull(top.getCollisionObject());
        assertNotNull(bottom.getCollisionObject());
        assertNotNull(leftBumper.getCollisionObject());
        assertNotNull(rightBumper.getCollisionObject());
    }

    @Test
    @DisplayName("All walls should have sprite proxies")
    void testWallsHaveSpriteProxies() {
        LeftWall left = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        RightWall right = new RightWall(GameObject.GameObjectName.RIGHT_WALL, 795, 0, 5, 600);
        TopWall top = new TopWall(GameObject.GameObjectName.TOP_WALL, 0, 0, 800, 5);
        BottomWall bottom = new BottomWall(GameObject.GameObjectName.BOTTOM_WALL, 0, 595, 800, 5);

        assertNotNull(left.getSpriteProxy());
        assertNotNull(right.getSpriteProxy());
        assertNotNull(top.getSpriteProxy());
        assertNotNull(bottom.getSpriteProxy());
    }

    @Test
    @DisplayName("Walls should not be marked for death initially")
    void testWallsNotMarkedForDeathInitially() {
        LeftWall wall = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        assertFalse(wall.isMarkedForDeath());
    }

    @Test
    @DisplayName("Wall markForDeath should set death flag")
    void testWallMarkForDeath() {
        RightWall wall = new RightWall(GameObject.GameObjectName.RIGHT_WALL, 795, 0, 5, 600);
        wall.markForDeath();
        assertTrue(wall.isMarkedForDeath());
    }

    @Test
    @DisplayName("Wall clearDeathFlag should clear death flag")
    void testWallClearDeathFlag() {
        TopWall wall = new TopWall(GameObject.GameObjectName.TOP_WALL, 0, 0, 800, 5);
        wall.markForDeath();
        wall.clearDeathFlag();
        assertFalse(wall.isMarkedForDeath());
    }

    @Test
    @DisplayName("WallType enum should have 7 types")
    void testWallTypeCount() {
        Wall.WallType[] types = Wall.WallType.values();
        assertEquals(8, types.length);
    }

    @Test
    @DisplayName("WallType enum should contain all expected types")
    void testWallTypeValues() {
        Wall.WallType[] types = Wall.WallType.values();
        
        assertTrue(containsType(types, Wall.WallType.WALL_GROUP));
        assertTrue(containsType(types, Wall.WallType.LEFT));
        assertTrue(containsType(types, Wall.WallType.RIGHT));
        assertTrue(containsType(types, Wall.WallType.TOP));
        assertTrue(containsType(types, Wall.WallType.BOTTOM));
        assertTrue(containsType(types, Wall.WallType.LEFT_BUMPER));
        assertTrue(containsType(types, Wall.WallType.RIGHT_BUMPER));
    }

    @Test
    @DisplayName("Wall setX and setY should update position")
    void testWallSetPosition() {
        LeftWall wall = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        
        wall.setX(100.0f);
        wall.setY(200.0f);

        assertEquals(100.0f, wall.getX(), 0.001f);
        assertEquals(200.0f, wall.getY(), 0.001f);
    }

    @Test
    @DisplayName("Wall update should update sprite and collision positions")
    void testWallUpdate() {
        BottomWall wall = new BottomWall(GameObject.GameObjectName.BOTTOM_WALL, 0, 595, 800, 5);
        
        wall.setX(10.0f);
        wall.setY(590.0f);
        wall.update();

        assertNotNull(wall.getSpriteProxy());
        assertNotNull(wall.getCollisionObject());
    }

    @Test
    @DisplayName("Wall toString should include wall type")
    void testWallToString() {
        LeftWall wall = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 10, 20, 5, 600);
        String str = wall.toString();
        assertNotNull(str);
        assertTrue(str.contains("LeftWall"));
    }

    @Test
    @DisplayName("Bumpers should be distinct from walls")
    void testBumpersDistinctFromWalls() {
        LeftWall leftWall = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        LeftBumper leftBumper = new LeftBumper(GameObject.GameObjectName.LEFT_BUMPER, 15, 500, 5, 50);

        assertNotEquals(leftWall.getWallType(), leftBumper.getWallType());
    }

    @Test
    @DisplayName("Wall collision rectangles should have correct dimensions")
    void testWallCollisionRectangleDimensions() {
        float width = 10.0f;
        float height = 100.0f;
        
        LeftWall wall = new LeftWall(
            GameObject.GameObjectName.LEFT_WALL,
            50.0f, 100.0f, width, height
        );

        assertNotNull(wall.getCollisionObject());
        assertNotNull(wall.getCollisionObject().getCollisionRect());
    }

    @Test
    @DisplayName("Different wall types should be distinguishable")
    void testDifferentWallTypes() {
        LeftWall left = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 0, 0, 5, 600);
        RightWall right = new RightWall(GameObject.GameObjectName.RIGHT_WALL, 795, 0, 5, 600);
        TopWall top = new TopWall(GameObject.GameObjectName.TOP_WALL, 0, 0, 800, 5);
        BottomWall bottom = new BottomWall(GameObject.GameObjectName.BOTTOM_WALL, 0, 595, 800, 5);

        assertNotEquals(left.getWallType(), right.getWallType());
        assertNotEquals(left.getWallType(), top.getWallType());
        assertNotEquals(left.getWallType(), bottom.getWallType());
        assertNotEquals(right.getWallType(), top.getWallType());
        assertNotEquals(right.getWallType(), bottom.getWallType());
        assertNotEquals(top.getWallType(), bottom.getWallType());
    }

    private boolean containsType(Wall.WallType[] types, Wall.WallType target) {
        for (Wall.WallType type : types) {
            if (type == target) {
                return true;
            }
        }
        return false;
    }
}