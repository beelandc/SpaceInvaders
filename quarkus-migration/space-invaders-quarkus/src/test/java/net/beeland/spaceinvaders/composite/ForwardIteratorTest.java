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
 * Unit tests for ForwardIterator class.
 * Tests depth-first traversal of composite structures.
 */
@DisplayName("ForwardIterator Tests")
class ForwardIteratorTest {

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
            new ForwardIterator(null);
        });
    }

    @Test
    @DisplayName("Constructor throws exception for LEAF component")
    void testConstructorLeafThrowsException() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ForwardIterator(leaf);
        });
    }

    @Test
    @DisplayName("First returns root component")
    void testFirstReturnsRoot() {
        ForwardIterator iterator = new ForwardIterator(root);
        
        Component first = iterator.first();
        
        assertEquals(root, first);
    }

    @Test
    @DisplayName("IsDone is false initially")
    void testIsDoneInitially() {
        ForwardIterator iterator = new ForwardIterator(root);
        
        assertFalse(iterator.isDone());
    }

    @Test
    @DisplayName("Iterate over single composite")
    void testIterateSingleComposite() {
        ForwardIterator iterator = new ForwardIterator(root);
        
        Component first = iterator.first();
        assertEquals(root, first);
        
        Component next = iterator.next();
        assertNull(next);
        assertTrue(iterator.isDone());
    }

    @Test
    @DisplayName("Iterate over composite with one child")
    void testIterateWithOneChild() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        ForwardIterator iterator = new ForwardIterator(root);
        List<Component> visited = new ArrayList<>();
        
        Component node = iterator.first();
        visited.add(node);
        
        while (!iterator.isDone()) {
            node = iterator.next();
            if (node != null) {
                visited.add(node);
            }
        }
        
        assertEquals(2, visited.size());
        assertEquals(root, visited.get(0));
        assertEquals(child, visited.get(1));
    }

    @Test
    @DisplayName("Iterate over composite with multiple children")
    void testIterateWithMultipleChildren() {
        TestLeaf child1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf child2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        TestLeaf child3 = new TestLeaf(GameObject.GameObjectName.JELLYFISH_ALIEN);
        
        root.add(child1);
        root.add(child2);
        root.add(child3);
        
        ForwardIterator iterator = new ForwardIterator(root);
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
        assertEquals(root, visited.get(0));
        // Children are added to front, so order is child3, child2, child1
        assertTrue(visited.contains(child1));
        assertTrue(visited.contains(child2));
        assertTrue(visited.contains(child3));
    }

    @Test
    @DisplayName("Iterate over nested composites")
    void testIterateNestedComposites() {
        TestComposite child1 = new TestComposite(GameObject.GameObjectName.ALIEN_GRID_COLUMN);
        TestComposite child2 = new TestComposite(GameObject.GameObjectName.ALIEN_GRID_COLUMN);
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        child1.add(leaf1);
        child2.add(leaf2);
        root.add(child1);
        root.add(child2);
        
        ForwardIterator iterator = new ForwardIterator(root);
        List<Component> visited = new ArrayList<>();
        
        Component node = iterator.first();
        visited.add(node);
        
        while (!iterator.isDone()) {
            node = iterator.next();
            if (node != null) {
                visited.add(node);
            }
        }
        
        // Should visit: root, child2, leaf2, child1, leaf1 (depth-first, children added to front)
        assertEquals(5, visited.size());
        assertEquals(root, visited.get(0));
        assertTrue(visited.contains(child1));
        assertTrue(visited.contains(child2));
        assertTrue(visited.contains(leaf1));
        assertTrue(visited.contains(leaf2));
    }

    @Test
    @DisplayName("GetParent returns parent component")
    void testGetParent() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        Component parent = ForwardIterator.getParent(child);
        
        assertEquals(root, parent);
    }

    @Test
    @DisplayName("GetChild returns first child")
    void testGetChild() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        Component firstChild = ForwardIterator.getChild(root);
        
        assertEquals(child, firstChild);
    }

    @Test
    @DisplayName("GetSibling returns next sibling")
    void testGetSibling() {
        TestLeaf child1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf child2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        root.add(child1);
        root.add(child2);
        
        // child2 is added last, so it's first. child1 is its sibling
        Component sibling = ForwardIterator.getSibling(child2);
        
        assertEquals(child1, sibling);
    }

    @Test
    @DisplayName("Multiple iterations work correctly")
    void testMultipleIterations() {
        TestLeaf child = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        root.add(child);
        
        ForwardIterator iterator = new ForwardIterator(root);
        
        // First iteration
        iterator.first();
        while (!iterator.isDone()) {
            iterator.next();
        }
        
        // Second iteration
        Component first = iterator.first();
        assertEquals(root, first);
        assertFalse(iterator.isDone());
    }
}