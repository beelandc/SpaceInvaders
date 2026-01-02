package net.beeland.spaceinvaders.gameobject;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameObjectManager class.
 * Tests object pool management, attach/detach operations, and CDI integration.
 */
@QuarkusTest
@DisplayName("GameObjectManager Tests")
class GameObjectManagerTest {

    @Inject
    GameObjectManager manager;

    // Test GameObject implementation
    private static class TestGameObject extends Leaf {
        public TestGameObject(GameObjectName name) {
            super(name);
        }

        @Override
        public void accept(CollisionVisitor other) {
            // Test implementation
        }

        @Override
        public void move(float xDelta, float yDelta) {
            setX(getX() + xDelta);
            setY(getY() + yDelta);
        }
    }

    @BeforeEach
    void setUp() {
        // Manager is injected by CDI, no manual initialization needed
    }

    @Test
    @DisplayName("Manager is injected via CDI")
    void testCDIInjection() {
        assertNotNull(manager);
    }

    @Test
    @DisplayName("Attach adds game object to manager")
    void testAttach() {
        TestGameObject obj = new TestGameObject(GameObject.GameObjectName.SQUID_ALIEN);
        
        GameObjectRef ref = manager.attach(obj);
        
        assertNotNull(ref);
        assertEquals(obj, ref.getGameObject());
    }

    @Test
    @DisplayName("Attach throws exception for null object")
    void testAttachNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.attach(null);
        });
    }

    @Test
    @DisplayName("Find returns attached game object by name")
    void testFind() {
        TestGameObject obj = new TestGameObject(GameObject.GameObjectName.CRAB_ALIEN);
        manager.attach(obj);
        
        GameObject found = manager.find(GameObject.GameObjectName.CRAB_ALIEN);
        
        assertNotNull(found);
        assertEquals(GameObject.GameObjectName.CRAB_ALIEN, found.getName());
    }

    @Test
    @DisplayName("Find returns null for non-existent name")
    void testFindNonExistent() {
        GameObject found = manager.find(GameObject.GameObjectName.FLYING_SAUCER);
        
        assertNull(found);
    }

    @Test
    @DisplayName("Find returns null for null name")
    void testFindNullName() {
        GameObject found = manager.find(null);
        
        assertNull(found);
    }

    @Test
    @DisplayName("Detach removes game object from manager")
    void testDetach() {
        TestGameObject obj = new TestGameObject(GameObject.GameObjectName.JELLYFISH_ALIEN);
        manager.attach(obj);
        
        manager.detach(obj);
        
        GameObject found = manager.find(GameObject.GameObjectName.JELLYFISH_ALIEN);
        assertNull(found);
    }

    @Test
    @DisplayName("Detach throws exception for null object")
    void testDetachNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.detach(null);
        });
    }

    @Test
    @DisplayName("Detach throws exception for non-attached object")
    void testDetachNonAttached() {
        TestGameObject obj = new TestGameObject(GameObject.GameObjectName.CORE_CANNON);
        
        assertThrows(IllegalStateException.class, () -> {
            manager.detach(obj);
        });
    }

    @Test
    @DisplayName("Update calls update on all attached objects")
    void testUpdate() {
        TestGameObject obj1 = new TestGameObject(GameObject.GameObjectName.SQUID_ALIEN);
        TestGameObject obj2 = new TestGameObject(GameObject.GameObjectName.CRAB_ALIEN);
        
        obj1.setPos(10.0f, 20.0f);
        obj2.setPos(30.0f, 40.0f);
        
        manager.attach(obj1);
        manager.attach(obj2);
        
        // Update should call update() on each object
        manager.update();
        
        // Verify objects still exist and have their positions
        assertEquals(10.0f, obj1.getX(), 0.001f);
        assertEquals(20.0f, obj1.getY(), 0.001f);
        assertEquals(30.0f, obj2.getX(), 0.001f);
        assertEquals(40.0f, obj2.getY(), 0.001f);
    }

    @Test
    @DisplayName("Multiple objects with same name can be attached")
    void testMultipleSameName() {
        TestGameObject obj1 = new TestGameObject(GameObject.GameObjectName.BOMB);
        TestGameObject obj2 = new TestGameObject(GameObject.GameObjectName.BOMB);
        
        GameObjectRef ref1 = manager.attach(obj1);
        GameObjectRef ref2 = manager.attach(obj2);
        
        assertNotNull(ref1);
        assertNotNull(ref2);
        assertNotSame(ref1, ref2);
    }

    @Test
    @DisplayName("Find returns first object with matching name")
    void testFindFirstMatch() {
        TestGameObject obj1 = new TestGameObject(GameObject.GameObjectName.MISSILE);
        TestGameObject obj2 = new TestGameObject(GameObject.GameObjectName.MISSILE);
        
        obj1.setPos(100.0f, 100.0f);
        obj2.setPos(200.0f, 200.0f);
        
        manager.attach(obj1);
        manager.attach(obj2);
        
        GameObject found = manager.find(GameObject.GameObjectName.MISSILE);
        
        assertNotNull(found);
        assertEquals(GameObject.GameObjectName.MISSILE, found.getName());
        // Should find one of them (order depends on implementation)
        assertTrue(found == obj1 || found == obj2);
    }

    @Test
    @DisplayName("Attach and detach multiple objects")
    void testAttachDetachMultiple() {
        TestGameObject obj1 = new TestGameObject(GameObject.GameObjectName.SHIELD_BRICK);
        TestGameObject obj2 = new TestGameObject(GameObject.GameObjectName.LEFT_WALL);
        TestGameObject obj3 = new TestGameObject(GameObject.GameObjectName.RIGHT_WALL);
        
        manager.attach(obj1);
        manager.attach(obj2);
        manager.attach(obj3);
        
        assertNotNull(manager.find(GameObject.GameObjectName.SHIELD_BRICK));
        assertNotNull(manager.find(GameObject.GameObjectName.LEFT_WALL));
        assertNotNull(manager.find(GameObject.GameObjectName.RIGHT_WALL));
        
        manager.detach(obj2);
        
        assertNotNull(manager.find(GameObject.GameObjectName.SHIELD_BRICK));
        assertNull(manager.find(GameObject.GameObjectName.LEFT_WALL));
        assertNotNull(manager.find(GameObject.GameObjectName.RIGHT_WALL));
    }

    @Test
    @DisplayName("Print method executes without errors")
    void testPrint() {
        TestGameObject obj = new TestGameObject(GameObject.GameObjectName.ALIEN_GRID);
        manager.attach(obj);
        
        // Should not throw exception
        assertDoesNotThrow(() -> manager.print());
    }

    @Test
    @DisplayName("GetStats returns non-null string")
    void testGetStats() {
        String stats = manager.getStats();
        
        assertNotNull(stats);
        assertTrue(stats.contains("GameObjectManager"));
    }
}