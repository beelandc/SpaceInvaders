package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Shield composite classes.
 * Tests ShieldColumn, ShieldGroup, and ShieldRoot.
 */
@DisplayName("Shield Composite Tests")
class ShieldCompositeTest {

    // ========== ShieldColumn Tests ==========

    @Test
    @DisplayName("ShieldColumn should be created with correct name and position")
    void testShieldColumnCreation() {
        ShieldColumn column = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            100.0f,
            200.0f
        );

        assertEquals(GameObject.GameObjectName.SHIELD_COLUMN, column.getName());
        assertEquals(100.0f, column.getX(), 0.001f);
        assertEquals(200.0f, column.getY(), 0.001f);
    }

    @Test
    @DisplayName("ShieldColumn should be able to add children")
    void testShieldColumnAddChildren() {
        ShieldColumn column = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            0.0f, 0.0f
        );

        ShieldBrick brick1 = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f, 0.0f
        );

        ShieldBrick brick2 = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            0.0f, 10.0f
        );

        column.add(brick1);
        column.add(brick2);

        assertNotNull(column.getFirstChild());
    }

    @Test
    @DisplayName("ShieldColumn move should do nothing (static object)")
    void testShieldColumnMove() {
        ShieldColumn column = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            100.0f, 200.0f
        );

        float originalX = column.getX();
        float originalY = column.getY();

        column.move(0.0f, 0.0f);

        assertEquals(originalX, column.getX(), 0.001f);
        assertEquals(originalY, column.getY(), 0.001f);
    }

    @Test
    @DisplayName("ShieldColumn accept should call visitor.visit")
    void testShieldColumnAccept() {
        ShieldColumn column = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            0.0f, 0.0f
        );

        final boolean[] visited = {false};
        GameObject.CollisionVisitor visitor = other -> {
            visited[0] = true;
            assertSame(column, other);
        };

        column.accept(visitor);
        assertTrue(visited[0], "Visitor should have been called");
    }

    @Test
    @DisplayName("ShieldColumn should have collision object")
    void testShieldColumnHasCollisionObject() {
        ShieldColumn column = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            0.0f, 0.0f
        );

        assertNotNull(column.getCollisionObject());
    }

    // ========== ShieldRoot Tests ==========

    @Test
    @DisplayName("ShieldRoot should be created with correct name and position")
    void testShieldRootCreation() {
        ShieldRoot root = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            150.0f,
            300.0f
        );

        assertEquals(GameObject.GameObjectName.SHIELD_ROOT, root.getName());
        assertEquals(150.0f, root.getX(), 0.001f);
        assertEquals(300.0f, root.getY(), 0.001f);
    }

    @Test
    @DisplayName("ShieldRoot should be able to add ShieldColumn children")
    void testShieldRootAddColumns() {
        ShieldRoot root = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            0.0f, 0.0f
        );

        ShieldColumn column1 = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            0.0f, 0.0f
        );

        ShieldColumn column2 = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            10.0f, 0.0f
        );

        root.add(column1);
        root.add(column2);

        assertNotNull(root.getFirstChild());
    }

    @Test
    @DisplayName("ShieldRoot move should do nothing (static object)")
    void testShieldRootMove() {
        ShieldRoot root = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            150.0f, 300.0f
        );

        float originalX = root.getX();
        float originalY = root.getY();

        root.move(0.0f, 0.0f);

        assertEquals(originalX, root.getX(), 0.001f);
        assertEquals(originalY, root.getY(), 0.001f);
    }

    @Test
    @DisplayName("ShieldRoot accept should call visitor.visit")
    void testShieldRootAccept() {
        ShieldRoot root = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            0.0f, 0.0f
        );

        final boolean[] visited = {false};
        GameObject.CollisionVisitor visitor = other -> {
            visited[0] = true;
            assertSame(root, other);
        };

        root.accept(visitor);
        assertTrue(visited[0], "Visitor should have been called");
    }

    @Test
    @DisplayName("ShieldRoot should have collision object")
    void testShieldRootHasCollisionObject() {
        ShieldRoot root = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            0.0f, 0.0f
        );

        assertNotNull(root.getCollisionObject());
    }

    // ========== ShieldGroup Tests ==========

    @Test
    @DisplayName("ShieldGroup should be created with correct name and position")
    void testShieldGroupCreation() {
        ShieldGroup group = new ShieldGroup(
            GameObject.GameObjectName.SHIELD_GROUP,
            0.0f,
            400.0f
        );

        assertEquals(GameObject.GameObjectName.SHIELD_GROUP, group.getName());
        assertEquals(0.0f, group.getX(), 0.001f);
        assertEquals(400.0f, group.getY(), 0.001f);
    }

    @Test
    @DisplayName("ShieldGroup should be able to add ShieldRoot children")
    void testShieldGroupAddRoots() {
        ShieldGroup group = new ShieldGroup(
            GameObject.GameObjectName.SHIELD_GROUP,
            0.0f, 0.0f
        );

        ShieldRoot root1 = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            100.0f, 400.0f
        );

        ShieldRoot root2 = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            300.0f, 400.0f
        );

        group.add(root1);
        group.add(root2);

        assertNotNull(group.getFirstChild());
    }

    @Test
    @DisplayName("ShieldGroup move should do nothing (static object)")
    void testShieldGroupMove() {
        ShieldGroup group = new ShieldGroup(
            GameObject.GameObjectName.SHIELD_GROUP,
            0.0f, 400.0f
        );

        float originalX = group.getX();
        float originalY = group.getY();

        group.move(0.0f, 0.0f);

        assertEquals(originalX, group.getX(), 0.001f);
        assertEquals(originalY, group.getY(), 0.001f);
    }

    @Test
    @DisplayName("ShieldGroup accept should call visitor.visit")
    void testShieldGroupAccept() {
        ShieldGroup group = new ShieldGroup(
            GameObject.GameObjectName.SHIELD_GROUP,
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
    @DisplayName("ShieldGroup should have collision object")
    void testShieldGroupHasCollisionObject() {
        ShieldGroup group = new ShieldGroup(
            GameObject.GameObjectName.SHIELD_GROUP,
            0.0f, 0.0f
        );

        assertNotNull(group.getCollisionObject());
    }

    // ========== Hierarchy Tests ==========

    @Test
    @DisplayName("Should be able to build complete shield hierarchy")
    void testCompleteShieldHierarchy() {
        // Create group
        ShieldGroup group = new ShieldGroup(
            GameObject.GameObjectName.SHIELD_GROUP,
            0.0f, 400.0f
        );

        // Create root
        ShieldRoot root = new ShieldRoot(
            GameObject.GameObjectName.SHIELD_ROOT,
            100.0f, 400.0f
        );

        // Create column
        ShieldColumn column = new ShieldColumn(
            GameObject.GameObjectName.SHIELD_COLUMN,
            100.0f, 400.0f
        );

        // Create bricks
        ShieldBrick brick1 = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            100.0f, 400.0f
        );

        ShieldBrick brick2 = new ShieldBrick(
            GameObject.GameObjectName.SHIELD_BRICK,
            ShieldBrick.BrickType.BRICK,
            100.0f, 410.0f
        );

        // Build hierarchy
        column.add(brick1);
        column.add(brick2);
        root.add(column);
        group.add(root);

        // Verify structure
        assertNotNull(group.getFirstChild());
        assertEquals(root, group.getFirstChild());
    }

    @Test
    @DisplayName("Shield composites should not be marked for death initially")
    void testShieldCompositesNotMarkedForDeathInitially() {
        ShieldColumn column = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0, 0);
        ShieldRoot root = new ShieldRoot(GameObject.GameObjectName.SHIELD_ROOT, 0, 0);
        ShieldGroup group = new ShieldGroup(GameObject.GameObjectName.SHIELD_GROUP, 0, 0);

        assertFalse(column.isMarkedForDeath());
        assertFalse(root.isMarkedForDeath());
        assertFalse(group.isMarkedForDeath());
    }

    @Test
    @DisplayName("Shield composites should support markForDeath")
    void testShieldCompositesMarkForDeath() {
        ShieldColumn column = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 0, 0);
        column.markForDeath();
        assertTrue(column.isMarkedForDeath());
    }

    @Test
    @DisplayName("Shield composites should support clearDeathFlag")
    void testShieldCompositesClearDeathFlag() {
        ShieldRoot root = new ShieldRoot(GameObject.GameObjectName.SHIELD_ROOT, 0, 0);
        root.markForDeath();
        root.clearDeathFlag();
        assertFalse(root.isMarkedForDeath());
    }

    @Test
    @DisplayName("Shield composites should support setX and setY")
    void testShieldCompositesSetPosition() {
        ShieldGroup group = new ShieldGroup(GameObject.GameObjectName.SHIELD_GROUP, 0, 0);
        
        group.setX(250.0f);
        group.setY(450.0f);

        assertEquals(250.0f, group.getX(), 0.001f);
        assertEquals(450.0f, group.getY(), 0.001f);
    }

    @Test
    @DisplayName("Shield composites should support update")
    void testShieldCompositesUpdate() {
        ShieldColumn column = new ShieldColumn(GameObject.GameObjectName.SHIELD_COLUMN, 100, 200);
        
        column.setX(150.0f);
        column.setY(250.0f);
        column.update();

        assertNotNull(column.getSpriteProxy());
        assertNotNull(column.getCollisionObject());
    }
}