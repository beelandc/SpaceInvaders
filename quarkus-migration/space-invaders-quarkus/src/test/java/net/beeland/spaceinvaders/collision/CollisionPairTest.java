package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.gameobject.GameObject;

import net.beeland.spaceinvaders.gameobject.Missile;
import net.beeland.spaceinvaders.gameobject.SquidAlien;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionPair class.
 * Tests collision pair management and collision detection logic.
 */
class CollisionPairTest {

    private CollisionPair pair;
    private GameObject treeA;
    private GameObject treeB;

    @BeforeEach
    void setUp() {
        pair = new CollisionPair();
        treeA = new Missile(GameObject.GameObjectName.MISSILE, 100f, 100f);
        treeB = new SquidAlien(150f, 150f);
    }

    @Test
    void testConstructor() {
        assertEquals(CollisionPair.Name.NOT_INITIALIZED, pair.getName());
        assertNull(pair.getTreeA());
        assertNull(pair.getTreeB());
        assertNotNull(pair.getSubject());
    }

    @Test
    void testSet() {
        pair.set(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        assertEquals(CollisionPair.Name.ALIEN_MISSILE, pair.getName());
        assertEquals(treeA, pair.getTreeA());
        assertEquals(treeB, pair.getTreeB());
    }

    @Test
    void testSetWithNullTreeA() {
        assertThrows(IllegalArgumentException.class, () -> {
            pair.set(CollisionPair.Name.ALIEN_MISSILE, null, treeB);
        });
    }

    @Test
    void testSetWithNullTreeB() {
        assertThrows(IllegalArgumentException.class, () -> {
            pair.set(CollisionPair.Name.ALIEN_MISSILE, treeA, null);
        });
    }

    @Test
    void testClear() {
        pair.set(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        pair.clear();
        
        assertEquals(CollisionPair.Name.NOT_INITIALIZED, pair.getName());
        assertNull(pair.getTreeA());
        assertNull(pair.getTreeB());
    }

    @Test
    void testSetName() {
        pair.setName(CollisionPair.Name.MISSILE_WALL);
        assertEquals(CollisionPair.Name.MISSILE_WALL, pair.getName());
    }

    @Test
    void testGetName() {
        pair.set(CollisionPair.Name.BOMB_SHIELD, treeA, treeB);
        assertEquals(CollisionPair.Name.BOMB_SHIELD, pair.getName());
    }

    @Test
    void testAttachObserver() {
        TestObserver observer = new TestObserver();
        
        pair.attach(observer);
        
        assertEquals(pair.getSubject(), observer.getSubject());
    }

    @Test
    void testSetCollision() {
        GameObject objA = new Missile(GameObject.GameObjectName.MISSILE, 50f, 50f);
        GameObject objB = new SquidAlien(60f, 60f);
        
        pair.setCollision(objA, objB);
        
        assertEquals(objA, pair.getSubject().getObjA());
        assertEquals(objB, pair.getSubject().getObjB());
    }

    @Test
    void testNotifyListeners() {
        TestObserver observer = new TestObserver();
        pair.attach(observer);
        
        GameObject objA = new Missile(GameObject.GameObjectName.MISSILE, 50f, 50f);
        GameObject objB = new SquidAlien(60f, 60f);
        pair.setCollision(objA, objB);
        
        pair.notifyListeners();
        
        assertTrue(observer.wasNotified());
    }

    @Test
    void testProcess() {
        // Create overlapping objects
        GameObject missile = new Missile(GameObject.GameObjectName.MISSILE, 100f, 100f);
        GameObject alien = new SquidAlien(105f, 105f);
        
        pair.set(CollisionPair.Name.ALIEN_MISSILE, missile, alien);
        
        // Process should detect collision (if rectangles overlap)
        assertDoesNotThrow(() -> {
            pair.process();
        });
    }

    @Test
    void testCollideStaticMethod() {
        GameObject missile = new Missile(GameObject.GameObjectName.MISSILE, 100f, 100f);
        GameObject alien = new SquidAlien(105f, 105f);
        
        // Should not throw exception
        assertDoesNotThrow(() -> {
            CollisionPair.collide(missile, alien);
        });
    }

    @Test
    void testCollideWithNullTreeA() {
        GameObject alien = new SquidAlien(105f, 105f);
        
        // Should handle null gracefully
        assertDoesNotThrow(() -> {
            CollisionPair.collide(null, alien);
        });
    }

    @Test
    void testCollideWithNullTreeB() {
        GameObject missile = new Missile(GameObject.GameObjectName.MISSILE, 100f, 100f);
        
        // Should handle null gracefully
        assertDoesNotThrow(() -> {
            CollisionPair.collide(missile, null);
        });
    }

    @Test
    void testCollideWithBothNull() {
        // Should handle null gracefully
        assertDoesNotThrow(() -> {
            CollisionPair.collide(null, null);
        });
    }

    @Test
    void testPrint() {
        pair.set(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        // Should not throw exception
        assertDoesNotThrow(() -> {
            pair.dump();
        });
    }

    @Test
    void testAllCollisionPairNames() {
        // Verify all enum values are accessible
        CollisionPair.Name[] names = CollisionPair.Name.values();
        
        assertTrue(names.length > 0);
        assertNotNull(CollisionPair.Name.ALIEN_MISSILE);
        assertNotNull(CollisionPair.Name.ALIEN_WALL);
        assertNotNull(CollisionPair.Name.MISSILE_WALL);
        assertNotNull(CollisionPair.Name.MISSILE_SHIELD);
        assertNotNull(CollisionPair.Name.ALIEN_SHIELD);
        assertNotNull(CollisionPair.Name.BOMB_SHIELD);
        assertNotNull(CollisionPair.Name.BOMB_WALL);
        assertNotNull(CollisionPair.Name.CORE_CANNON_WALL);
        assertNotNull(CollisionPair.Name.BOMB_MISSILE);
        assertNotNull(CollisionPair.Name.FLYING_SAUCER_MISSILE);
        assertNotNull(CollisionPair.Name.FLYING_SAUCER_WALL);
        assertNotNull(CollisionPair.Name.BOMB_SHIP);
        assertNotNull(CollisionPair.Name.NULL_OBJECT);
        assertNotNull(CollisionPair.Name.NOT_INITIALIZED);
    }

    @Test
    void testMultipleObservers() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        TestObserver observer3 = new TestObserver();
        
        pair.attach(observer1);
        pair.attach(observer2);
        pair.attach(observer3);
        
        GameObject objA = new Missile(GameObject.GameObjectName.MISSILE, 50f, 50f);
        GameObject objB = new SquidAlien(60f, 60f);
        pair.setCollision(objA, objB);
        
        pair.notifyListeners();
        
        assertTrue(observer1.wasNotified());
        assertTrue(observer2.wasNotified());
        assertTrue(observer3.wasNotified());
    }

    @Test
    void testClearDetachesObservers() {
        TestObserver observer = new TestObserver();
        pair.attach(observer);
        
        pair.set(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        pair.clear();
        
        // After clear, subject should have no observers
        assertNull(pair.getSubject().getHead());
    }

    /**
     * Test observer implementation for testing purposes.
     */
    private static class TestObserver extends CollisionObserver {
        private boolean notified = false;

        @Override
        public void notifyCollision() {
            notified = true;
        }

        public boolean wasNotified() {
            return notified;
        }
    }
}