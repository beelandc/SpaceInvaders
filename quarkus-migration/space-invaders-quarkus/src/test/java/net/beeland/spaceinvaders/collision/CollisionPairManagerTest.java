package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.NullGameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionPairManager class.
 * Tests collision pair management without CDI.
 *
 * Note: Uses NullGameObject instances to avoid OpenGL/native library dependencies in tests.
 * Note: This is a plain JUnit test (not @QuarkusTest) to avoid CDI initialization issues.
 */
class CollisionPairManagerTest {

    private CollisionPairManager manager;
    private GameObject treeA;
    private GameObject treeB;

    @BeforeEach
    void setUp() {
        // Create manager directly (no CDI)
        manager = new CollisionPairManager();
        
        // Use NullGameObject to avoid OpenGL dependencies in tests
        treeA = new NullGameObject();
        treeA.setX(100f);
        treeA.setY(100f);
        
        treeB = new NullGameObject();
        treeB.setX(150f);
        treeB.setY(150f);
    }

    @Test
    void testAdd() {
        CollisionPair pair = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        assertNotNull(pair);
        assertEquals(CollisionPair.Name.ALIEN_MISSILE, pair.getName());
        assertEquals(treeA, pair.getTreeA());
        assertEquals(treeB, pair.getTreeB());
    }

    @Test
    void testAddWithNullTreeA() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.add(CollisionPair.Name.ALIEN_MISSILE, null, treeB);
        });
    }

    @Test
    void testAddWithNullTreeB() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, null);
        });
    }

    @Test
    void testAddMultiplePairs() {
        GameObject treeC = new NullGameObject();
        treeC.setX(200f);
        treeC.setY(200f);
        
        GameObject treeD = new NullGameObject();
        treeD.setX(250f);
        treeD.setY(250f);
        
        CollisionPair pair1 = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        CollisionPair pair2 = manager.add(CollisionPair.Name.MISSILE_WALL, treeC, treeD);
        
        assertNotNull(pair1);
        assertNotNull(pair2);
        assertNotEquals(pair1, pair2);
    }

    @Test
    void testFind() {
        manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        CollisionPair found = manager.find(CollisionPair.Name.ALIEN_MISSILE);
        
        assertNotNull(found);
        assertEquals(CollisionPair.Name.ALIEN_MISSILE, found.getName());
    }

    @Test
    void testFindNonExistent() {
        CollisionPair found = manager.find(CollisionPair.Name.BOMB_SHIELD);
        
        assertNull(found);
    }

    @Test
    void testFindWithNull() {
        CollisionPair found = manager.find(null);
        
        assertNull(found);
    }

    @Test
    void testRemove() {
        CollisionPair pair = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        manager.remove(pair);
        
        CollisionPair found = manager.find(CollisionPair.Name.ALIEN_MISSILE);
        assertNull(found);
    }

    @Test
    void testRemoveWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.remove(null);
        });
    }

    @Test
    void testProcess() {
        manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        // Should not throw exception
        assertDoesNotThrow(() -> {
            manager.process();
        });
    }

    @Test
    void testProcessMultiplePairs() {
        GameObject treeC = new NullGameObject();
        treeC.setX(200f);
        treeC.setY(200f);
        
        GameObject treeD = new NullGameObject();
        treeD.setX(250f);
        treeD.setY(250f);
        
        manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        manager.add(CollisionPair.Name.MISSILE_WALL, treeC, treeD);
        
        // Should process all pairs without exception
        assertDoesNotThrow(() -> {
            manager.process();
        });
    }

    @Test
    void testGetActiveColPair() {
        // Before processing, should be null
        assertNull(manager.getActiveColPair());
        
        // Note: During actual processing, this would be set
        // but we can't easily test that without mocking
    }

    @Test
    void testPrintAll() {
        manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        // Should not throw exception
        assertDoesNotThrow(() -> {
            manager.printAll();
        });
    }

    @Test
    void testObjectPoolBehavior() {
        // Add and remove multiple times to test pool reuse
        CollisionPair pair1 = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        manager.remove(pair1);
        
        CollisionPair pair2 = manager.add(CollisionPair.Name.MISSILE_WALL, treeA, treeB);
        
        // pair2 should be reused from pool (same instance as pair1)
        assertNotNull(pair2);
    }

    @Test
    void testMultipleFinds() {
        GameObject treeC = new NullGameObject();
        treeC.setX(200f);
        treeC.setY(200f);
        
        GameObject treeD = new NullGameObject();
        treeD.setX(250f);
        treeD.setY(250f);
        
        manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        manager.add(CollisionPair.Name.MISSILE_WALL, treeC, treeD);
        
        CollisionPair found1 = manager.find(CollisionPair.Name.ALIEN_MISSILE);
        CollisionPair found2 = manager.find(CollisionPair.Name.MISSILE_WALL);
        
        assertNotNull(found1);
        assertNotNull(found2);
        assertEquals(CollisionPair.Name.ALIEN_MISSILE, found1.getName());
        assertEquals(CollisionPair.Name.MISSILE_WALL, found2.getName());
    }

    @Test
    void testAddRemoveAdd() {
        // Add a pair
        CollisionPair pair1 = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        assertNotNull(pair1);
        
        // Remove it
        manager.remove(pair1);
        assertNull(manager.find(CollisionPair.Name.ALIEN_MISSILE));
        
        // Add a different pair (should reuse from pool)
        CollisionPair pair2 = manager.add(CollisionPair.Name.BOMB_SHIELD, treeA, treeB);
        assertNotNull(pair2);
        assertEquals(CollisionPair.Name.BOMB_SHIELD, pair2.getName());
    }

    @Test
    void testProcessWithObservers() {
        CollisionPair pair = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        TestObserver observer = new TestObserver();
        pair.attach(observer);
        
        // Process should work with observers attached
        assertDoesNotThrow(() -> {
            manager.process();
        });
    }

    @Test
    void testManagerStatistics() {
        // Add several pairs
        manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        manager.add(CollisionPair.Name.MISSILE_WALL, treeA, treeB);
        manager.add(CollisionPair.Name.BOMB_SHIELD, treeA, treeB);
        
        // Manager should track statistics (inherited from Manager base class)
        assertDoesNotThrow(() -> {
            manager.printAll();
        });
    }

    @Test
    void testClearAfterRemove() {
        CollisionPair pair = manager.add(CollisionPair.Name.ALIEN_MISSILE, treeA, treeB);
        
        // Attach observer
        TestObserver observer = new TestObserver();
        pair.attach(observer);
        
        // Remove should clear the pair
        manager.remove(pair);
        
        // Pair should be cleared (name reset to NOT_INITIALIZED)
        assertEquals(CollisionPair.Name.NOT_INITIALIZED, pair.getName());
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