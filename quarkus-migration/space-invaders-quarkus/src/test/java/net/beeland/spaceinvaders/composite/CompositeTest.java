package net.beeland.spaceinvaders.composite;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.Leaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Composite class.
 * Tests composite pattern functionality including add, remove, and traversal.
 */
@DisplayName("Composite Tests")
class CompositeTest {

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

    private TestComposite composite;

    @BeforeEach
    void setUp() {
        composite = new TestComposite(GameObject.GameObjectName.ALIEN_GRID);
    }

    @Test
    @DisplayName("Constructor initializes composite correctly")
    void testConstructor() {
        assertNotNull(composite);
        assertEquals(GameObject.GameObjectName.ALIEN_GRID, composite.getName());
        assertEquals(Component.ContainerType.COMPOSITE, composite.getContainerType());
        assertEquals(0, composite.getNumChildren());
        assertNull(composite.getFirstChild());
    }

    @Test
    @DisplayName("SetCompositeName sets the name")
    void testSetCompositeName() {
        composite.setCompositeName(Composite.CompositeName.ALIEN_GRID);
        assertEquals(Composite.CompositeName.ALIEN_GRID, composite.getCompositeName());
    }

    @Test
    @DisplayName("Add increases child count")
    void testAddIncreasesChildCount() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        
        composite.add(leaf);
        
        assertEquals(1, composite.getNumChildren());
    }

    @Test
    @DisplayName("Add sets parent reference")
    void testAddSetsParent() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        
        composite.add(leaf);
        
        assertEquals(composite, leaf.getParent());
    }

    @Test
    @DisplayName("Add makes child the first child")
    void testAddMakesFirstChild() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        
        composite.add(leaf);
        
        assertEquals(leaf, composite.getFirstChild());
    }

    @Test
    @DisplayName("Add multiple children maintains order")
    void testAddMultipleChildren() {
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        TestLeaf leaf3 = new TestLeaf(GameObject.GameObjectName.JELLYFISH_ALIEN);
        
        composite.add(leaf1);
        composite.add(leaf2);
        composite.add(leaf3);
        
        assertEquals(3, composite.getNumChildren());
        // Last added should be first child (added to front)
        assertEquals(leaf3, composite.getFirstChild());
    }

    @Test
    @DisplayName("Add throws exception for null component")
    void testAddNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            composite.add(null);
        });
    }

    @Test
    @DisplayName("Remove decreases child count")
    void testRemoveDecreasesChildCount() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        composite.add(leaf);
        
        composite.remove(leaf);
        
        assertEquals(0, composite.getNumChildren());
    }

    @Test
    @DisplayName("Remove first child updates head")
    void testRemoveFirstChild() {
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        composite.add(leaf1);
        composite.add(leaf2);
        
        composite.remove(leaf2); // Remove first child
        
        assertEquals(1, composite.getNumChildren());
        assertEquals(leaf1, composite.getFirstChild());
    }

    @Test
    @DisplayName("Remove throws exception for null component")
    void testRemoveNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            composite.remove(null);
        });
    }

    @Test
    @DisplayName("GetChildByIndex returns correct child")
    void testGetChildByIndex() {
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        TestLeaf leaf3 = new TestLeaf(GameObject.GameObjectName.JELLYFISH_ALIEN);
        
        composite.add(leaf1);
        composite.add(leaf2);
        composite.add(leaf3);
        
        // Remember: added to front, so order is leaf3, leaf2, leaf1
        assertEquals(leaf3, composite.getChildByIndex(0));
        assertEquals(leaf2, composite.getChildByIndex(1));
        assertEquals(leaf1, composite.getChildByIndex(2));
    }

    @Test
    @DisplayName("GetChildByIndex throws exception for negative index")
    void testGetChildByIndexNegative() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            composite.getChildByIndex(-1);
        });
    }

    @Test
    @DisplayName("GetChildByIndex throws exception for index >= numChildren")
    void testGetChildByIndexTooLarge() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        composite.add(leaf);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            composite.getChildByIndex(1);
        });
    }

    @Test
    @DisplayName("Move moves all children")
    void testMoveMovesAllChildren() {
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        leaf1.setPos(10.0f, 20.0f);
        leaf2.setPos(30.0f, 40.0f);
        
        composite.add(leaf1);
        composite.add(leaf2);
        
        composite.move(5.0f, 10.0f);
        
        assertEquals(15.0f, leaf1.getX(), 0.001f);
        assertEquals(30.0f, leaf1.getY(), 0.001f);
        assertEquals(35.0f, leaf2.getX(), 0.001f);
        assertEquals(50.0f, leaf2.getY(), 0.001f);
    }

    @Test
    @DisplayName("GetComponentList returns head")
    void testGetComponentList() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        composite.add(leaf);
        
        assertEquals(leaf, composite.getComponentList());
    }

    @Test
    @DisplayName("Nested composites work correctly")
    void testNestedComposites() {
        TestComposite child1 = new TestComposite(GameObject.GameObjectName.ALIEN_GRID_COLUMN);
        TestComposite child2 = new TestComposite(GameObject.GameObjectName.ALIEN_GRID_COLUMN);
        TestLeaf leaf1 = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        TestLeaf leaf2 = new TestLeaf(GameObject.GameObjectName.CRAB_ALIEN);
        
        child1.add(leaf1);
        child2.add(leaf2);
        composite.add(child1);
        composite.add(child2);
        
        assertEquals(2, composite.getNumChildren());
        assertEquals(1, child1.getNumChildren());
        assertEquals(1, child2.getNumChildren());
    }

    @Test
    @DisplayName("Print executes without errors")
    void testPrint() {
        TestLeaf leaf = new TestLeaf(GameObject.GameObjectName.SQUID_ALIEN);
        composite.add(leaf);
        
        assertDoesNotThrow(() -> composite.print());
    }
}