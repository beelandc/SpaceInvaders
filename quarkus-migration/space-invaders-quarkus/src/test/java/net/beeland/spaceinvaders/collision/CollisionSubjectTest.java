package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.NullGameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionSubject class.
 * Tests the Observer pattern implementation for collision notifications.
 */
class CollisionSubjectTest {

    private CollisionSubject subject;
    private GameObject objA;
    private GameObject objB;

    @BeforeEach
    void setUp() {
        subject = new CollisionSubject();
        objA = new NullGameObject();
        objB = new NullGameObject();
    }

    @Test
    void testConstructor() {
        assertNull(subject.getObjA());
        assertNull(subject.getObjB());
        assertNull(subject.getHead());
    }

    @Test
    void testSetCollision() {
        subject.setCollision(objA, objB);
        
        assertEquals(objA, subject.getObjA());
        assertEquals(objB, subject.getObjB());
    }

    @Test
    void testSetCollisionWithNullObjA() {
        assertThrows(IllegalArgumentException.class, () -> {
            subject.setCollision(null, objB);
        });
    }

    @Test
    void testSetCollisionWithNullObjB() {
        assertThrows(IllegalArgumentException.class, () -> {
            subject.setCollision(objA, null);
        });
    }

    @Test
    void testAttachSingleObserver() {
        TestObserver observer = new TestObserver();
        
        subject.attach(observer);
        
        assertEquals(subject, observer.getSubject());
        assertEquals(observer, subject.getHead());
        assertNull(observer.getNext());
        assertNull(observer.getPrev());
    }

    @Test
    void testAttachMultipleObservers() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        TestObserver observer3 = new TestObserver();
        
        subject.attach(observer1);
        subject.attach(observer2);
        subject.attach(observer3);
        
        // Observers are added to front, so order is 3, 2, 1
        assertEquals(observer3, subject.getHead());
        assertEquals(observer2, observer3.getNext());
        assertEquals(observer1, observer2.getNext());
        assertNull(observer1.getNext());
    }

    @Test
    void testAttachNullObserver() {
        assertThrows(IllegalArgumentException.class, () -> {
            subject.attach(null);
        });
    }

    @Test
    void testNotifyObservers() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        TestObserver observer3 = new TestObserver();
        
        subject.attach(observer1);
        subject.attach(observer2);
        subject.attach(observer3);
        
        subject.setCollision(objA, objB);
        subject.notifyObservers();
        
        assertTrue(observer1.wasNotified());
        assertTrue(observer2.wasNotified());
        assertTrue(observer3.wasNotified());
    }

    @Test
    void testNotifyObserversWithNoObservers() {
        // Should not throw exception
        assertDoesNotThrow(() -> {
            subject.notifyObservers();
        });
    }

    @Test
    void testDetachAll() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        
        subject.attach(observer1);
        subject.attach(observer2);
        
        subject.detachAll();
        
        assertNull(subject.getHead());
        assertNull(observer1.getSubject());
        assertNull(observer2.getSubject());
        assertNull(observer1.getNext());
        assertNull(observer2.getNext());
    }

    @Test
    void testDetachAllWithNoObservers() {
        // Should not throw exception
        assertDoesNotThrow(() -> {
            subject.detachAll();
        });
    }

    @Test
    void testObserverChainIntegrity() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        TestObserver observer3 = new TestObserver();
        
        subject.attach(observer1);
        subject.attach(observer2);
        subject.attach(observer3);
        
        // Verify forward chain
        assertEquals(observer3, subject.getHead());
        assertEquals(observer2, observer3.getNext());
        assertEquals(observer1, observer2.getNext());
        assertNull(observer1.getNext());
        
        // Verify backward chain
        assertNull(observer3.getPrev());
        assertEquals(observer3, observer2.getPrev());
        assertEquals(observer2, observer1.getPrev());
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