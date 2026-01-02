package net.beeland.spaceinvaders.composite;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.Leaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for ReverseIterator class.
 * Tests reverse traversal of composite structures.
 */
@DisplayName("ReverseIterator Tests")
class ReverseIteratorTest {

    // Test implementations
    private static class TestComposite extends Composite {
        public TestComposite(GameObjectName name) {
            super(name);
        }

        @Override
        public void accept(CollisionVisitor other) {
            // Test implementation
        }
    }

    private static class TestLeaf extends Leaf {
        public TestLeaf(GameObjectName name) {
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

    private TestComposite root;

    @BeforeEach
    void setUp() {
        root = new TestComposite(GameObject.GameObjectName.ALIEN_GRID);
    }

    @Test
    @DisplayName("Constructor throws exception for null component")
    void testConstructorNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReverseIterator(null);
        });
    }

    @Test
    @DisplayName("Constructor throws exception for LEAF component")
    void testConstructorLeafThrowsException() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ReverseIterator(leaf);
        });
    }

    @Test
    @DisplayName("First returns last node in forward order")
    void testFirstReturnsLastNode() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        ReverseIterator iterator = new ReverseIterator(root);
        Component first = iterator.first();
        
        // In reverse iteration, first should be the last node (the leaf)
        assertEquals(child, first);
    }

    @Test
    @DisplayName("IsDone is false initially after first()")
    void testIsDoneAfterFirst() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        ReverseIterator iterator = new ReverseIterator(root);
        iterator.first();
        
        assertFalse(iterator.isDone());
    }

    @Test
    @DisplayName("Iterate in reverse over composite with one child")
    void testIterateReverseWithOneChild() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        ReverseIterator iterator = new ReverseIterator(root);
        List<Component> visited = new ArrayList<>();
        
        Component node = iterator.first();
        visited.add(node);
        
        while (!iterator.isDone()) {
            node = iterator.next();
            if (node != null) {
                visited.add(node);
            }
        }
        
        // Should visit in reverse: child, root
        assertEquals(2, visited.size());
        assertEquals(child, visited.get(0));
        assertEquals(root, visited.get(1));
    }

    @Test
    @DisplayName("Iterate in reverse over composite with multiple children")
    void testIterateReverseWithMultipleChildren() {
        TestLeaf child1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf child2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        TestLeaf child3 = new TestLeaf(GameObject.GameObjectName.JELLYFISH_ALIEN);
        
        root.add(child1);
        root.add(child2);
        root.add(child3);
        
        ReverseIterator iterator = new ReverseIterator(root);
        List<Component> visited = new ArrayList<>();
        
        Component node = iterator.first();
        visited.add(node);
        
        while (!iterator.isDone()) {
            node = iterator.next();
            if (node != null) {
                visited.add(node);
            }
        }
        
        assertEquals(4, visited.size());
        // Last element should be root
        assertEquals(root, visited.get(visited.size() - 1));
        // All children should be visited
        assertTrue(visited.contains(child1));
        assertTrue(visited.contains(child2));
        assertTrue(visited.contains(child3));
    }

    @Test
    @DisplayName("Iterate in reverse over nested composites")
    void testIterateReverseNestedComposites() {
        TestComposite child1 = new TestComposite(GameObject.GameObjectName.ALIEN_GRID_COLUMN);
        TestComposite child2 = new TestComposite(GameObject.GameObjectName.ALIEN_GRID_COLUMN);
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        child1.add(leaf1);
        child2.add(leaf2);
        root.add(child1);
        root.add(child2);
        
        ReverseIterator iterator = new ReverseIterator(root);
        List<Component> visited = new ArrayList<>();
        
        Component node = iterator.first();
        visited.add(node);
        
        while (!iterator.isDone()) {
            node = iterator.next();
            if (node != null) {
                visited.add(node);
            }
        }
        
        // Should visit all 5 nodes in reverse order
        assertEquals(5, visited.size());
        // Root should be last
        assertEquals(root, visited.get(visited.size() - 1));
        // All nodes should be visited
        assertTrue(visited.contains(child1));
        assertTrue(visited.contains(child2));
        assertTrue(visited.contains(leaf1));
        assertTrue(visited.contains(leaf2));
    }

    @Test
    @DisplayName("Reverse iteration is opposite of forward iteration")
    void testReverseIsOppositeOfForward() {
        TestLeaf child1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf child2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        root.add(child1);
        root.add(child2);
        
        // Forward iteration
        ForwardIterator forward = new ForwardIterator(root);
        List<Component> forwardList = new ArrayList<>();
        Component node = forward.first();
        forwardList.add(node);
        while (!forward.isDone()) {
            node = forward.next();
            if (node != null) {
                forwardList.add(node);
            }
        }
        
        // Reverse iteration
        ReverseIterator reverse = new ReverseIterator(root);
        List<Component> reverseList = new ArrayList<>();
        node = reverse.first();
        reverseList.add(node);
        while (!reverse.isDone()) {
            node = reverse.next();
            if (node != null) {
                reverseList.add(node);
            }
        }
        
        // Lists should be same size
        assertEquals(forwardList.size(), reverseList.size());
        
        // First element of forward should be last of reverse
        assertEquals(forwardList.get(0), reverseList.get(reverseList.size() - 1));
        
        // Last element of forward should be first of reverse
        assertEquals(forwardList.get(forwardList.size() - 1), reverseList.get(0));
    }

    @Test
    @DisplayName("IsDone returns true when iteration complete")
    void testIsDoneWhenComplete() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        ReverseIterator iterator = new ReverseIterator(root);
        iterator.first();
        
        while (!iterator.isDone()) {
            iterator.next();
        }
        
        assertTrue(iterator.isDone());
    }
}