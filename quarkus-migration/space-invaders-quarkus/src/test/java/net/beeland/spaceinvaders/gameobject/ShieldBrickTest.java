package net.beeland.spaceinvaders.gameobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShieldBrick class.
 * Tests brick types, positioning, collision detection, and static behavior.
 */
@DisplayName("ShieldBrick Tests")
class ShieldBrickTest {

    @Test
    @DisplayName("Constructor should create brick with correct name and type")
    void testConstructor() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            100.0f,
            200.0f
        );

        assertEquals(GameObject.GameObjectName.SHIELD_BRICK, brick.getName());
        assertEquals(ShieldBrick.BrickType.BRICK, brick.getBrickType());
        assertEquals(100.0f, brick.getX(), 0.001f);
        assertEquals(200.0f, brick.getY(), 0.001f);
    }

    @Test
    @DisplayName("Constructor should handle all brick types")
    void testAllBrickTypes() {
        ShieldBrick.BrickType[] types = ShieldBrick.BrickType.values();
        
        for (ShieldBrick.BrickType type : types) {
            ShieldBrick brick = new ShieldBrick(
                GameObject.GameObjectName.SHIELD_BRICK,
                type,
                0.0f,
                0.0f
            );
            
            assertEquals(type, brick.getBrickType());
        }
    }

    @Test
    @DisplayName("getBrickType should return correct type")
    void testGetBrickType() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.LEFT_TOP_0,
            0.0f,
            0.0f
        );

        assertEquals(ShieldBrick.BrickType.LEFT_TOP_0, brick.getBrickType());
    }

    @Test
    @DisplayName("setBrickType should update brick type")
    void testSetBrickType() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        brick.setBrickType(ShieldBrick.BrickType.RIGHT_BOTTOM);
        assertEquals(ShieldBrick.BrickType.RIGHT_BOTTOM, brick.getBrickType());
    }

    @Test
    @DisplayName("Position should be set correctly via constructor")
    void testPositionFromConstructor() {
        float x = 150.5f;
        float y = 250.75f;
        
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            x,
            y
        );

        assertEquals(x, brick.getX(), 0.001f);
        assertEquals(y, brick.getY(), 0.001f);
    }

    @Test
    @DisplayName("setX and setY should update position")
    void testSetPosition() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        brick.setX(300.0f);
        brick.setY(400.0f);

        assertEquals(300.0f, brick.getX(), 0.001f);
        assertEquals(400.0f, brick.getY(), 0.001f);
    }

    @Test
    @DisplayName("move should do nothing (static object)")
    void testMove() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            100.0f,
            200.0f
        );

        float originalX = brick.getX();
        float originalY = brick.getY();

        brick.move(0.0f, 0.0f);

        assertEquals(originalX, brick.getX(), 0.001f);
        assertEquals(originalY, brick.getY(), 0.001f);
    }

    @Test
    @DisplayName("accept should call visitor.visit with this brick")
    void testAccept() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        final boolean[] visited = {false};
        GameObject.CollisionVisitor visitor = other -> {
            visited[0] = true;
            assertSame(brick, other);
        };

        brick.accept(visitor);
        assertTrue(visited[0], "Visitor should have been called");
    }

    @Test
    @DisplayName("Should have collision object")
    void testHasCollisionObject() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        assertNotNull(brick.getCollisionObject());
    }

    @Test
    @DisplayName("Should have sprite proxy")
    void testHasSpriteProxy() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        assertNotNull(brick.getSpriteProxy());
    }

    @Test
    @DisplayName("Should not be marked for death initially")
    void testNotMarkedForDeathInitially() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        assertFalse(brick.isMarkedForDeath());
    }

    @Test
    @DisplayName("markForDeath should set death flag")
    void testMarkForDeath() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        brick.markForDeath();
        assertTrue(brick.isMarkedForDeath());
    }

    @Test
    @DisplayName("clearDeathFlag should clear death flag")
    void testClearDeathFlag() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f,
            0.0f
        );

        brick.markForDeath();
        assertTrue(brick.isMarkedForDeath());

        brick.clearDeathFlag();
        assertFalse(brick.isMarkedForDeath());
    }

    @Test
    @DisplayName("Corner brick types should be distinct")
    void testCornerBrickTypes() {
        ShieldBrick leftTop0 = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.LEFT_TOP_0,
            0.0f, 0.0f
        );
        
        ShieldBrick leftTop1 = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.LEFT_TOP_1,
            0.0f, 0.0f
        );
        
        ShieldBrick leftBottom = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.LEFT_BOTTOM,
            0.0f, 0.0f
        );

        assertNotEquals(leftTop0.getBrickType(), leftTop1.getBrickType());
        assertNotEquals(leftTop0.getBrickType(), leftBottom.getBrickType());
        assertNotEquals(leftTop1.getBrickType(), leftBottom.getBrickType());
    }

    @Test
    @DisplayName("toString should include brick type and position")
    void testToString() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.LEFT_TOP_0,
            100.0f,
            200.0f
        );

        String str = brick.toString();
        assertNotNull(str);
        assertTrue(str.contains("ShieldBrick"));
    }

    @Test
    @DisplayName("update should update sprite and collision positions")
    void testUpdate() {
        ShieldBrick brick = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            100.0f,
            200.0f
        );

        brick.setX(150.0f);
        brick.setY(250.0f);
        brick.update();

        // Verify sprite proxy position is updated
        assertNotNull(brick.getSpriteProxy());
        // Verify collision object position is updated
        assertNotNull(brick.getCollisionObject());
    }

    @Test
    @DisplayName("BrickType enum should have 7 types")
    void testBrickTypeCount() {
        ShieldBrick.BrickType[] types = ShieldBrick.BrickType.values();
        assertEquals(8, types.length);
    }

    @Test
    @DisplayName("BrickType enum should contain all expected types")
    void testBrickTypeValues() {
        ShieldBrick.BrickType[] types = ShieldBrick.BrickType.values();
        
        assertTrue(containsType(types, ShieldBrick.BrickType.BRICK));
        assertTrue(containsType(types, ShieldBrick.BrickType.LEFT_TOP_0));
        assertTrue(containsType(types, ShieldBrick.BrickType.LEFT_TOP_1));
        assertTrue(containsType(types, ShieldBrick.BrickType.LEFT_BOTTOM));
        assertTrue(containsType(types, ShieldBrick.BrickType.RIGHT_TOP_0));
        assertTrue(containsType(types, ShieldBrick.BrickType.RIGHT_TOP_1));
        assertTrue(containsType(types, ShieldBrick.BrickType.RIGHT_BOTTOM));
    }

    private boolean containsType(ShieldBrick.BrickType[] types, ShieldBrick.BrickType target) {
        for (ShieldBrick.BrickType type : types) {
            if (type == target) {
                return true;
            }
        }
        return false;
    }
}