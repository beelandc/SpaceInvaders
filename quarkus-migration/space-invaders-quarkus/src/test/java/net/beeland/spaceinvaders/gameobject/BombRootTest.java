package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.strategy.FallStraight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BombRoot class
 */
@DisplayName("BombRoot Tests")
class BombRootTest {
    
    private BombRoot bombRoot;
    
    @BeforeEach
    void setUp() {
        bombRoot = new BombRoot(Composite.CompositeName.BOMB_ROOT, 100.0f, 200.0f);
    }
    
    @Test
    @DisplayName("Constructor with position should initialize with correct name")
    void testConstructorWithPosition() {
        assertEquals(GameObject.GameObjectName.BOMB_ROOT, bombRoot.getName());
    }
    
    @Test
    @DisplayName("Constructor with position should set composite name")
    void testConstructorCompositeName() {
        assertEquals(Composite.CompositeName.BOMB_ROOT, bombRoot.getCompositeName());
    }
    
    @Test
    @DisplayName("Constructor with position should set initial position")
    void testConstructorPosition() {
        assertEquals(100.0f, bombRoot.getX(), 0.001f);
        assertEquals(200.0f, bombRoot.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Constructor without position should default to origin")
    void testConstructorWithoutPosition() {
        BombRoot root = new BombRoot(Composite.CompositeName.BOMB_ROOT);
        assertEquals(0.0f, root.getX(), 0.001f);
        assertEquals(0.0f, root.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Constructor without position should set composite name")
    void testConstructorWithoutPositionCompositeName() {
        BombRoot root = new BombRoot(Composite.CompositeName.BOMB_ROOT);
        assertEquals(Composite.CompositeName.BOMB_ROOT, root.getCompositeName());
    }
    
    @Test
    @DisplayName("Should start with no children")
    void testInitiallyNoChildren() {
        assertEquals(0, bombRoot.getNumChildren());
    }
    
    @Test
    @DisplayName("Can add bomb children")
    void testAddBomb() {
        FallStraight strategy = new FallStraight();
        Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 50.0f, 100.0f);
        
        bombRoot.add(bomb);
        
        assertEquals(1, bombRoot.getNumChildren());
    }
    
    @Test
    @DisplayName("Can add multiple bombs")
    void testAddMultipleBombs() {
        FallStraight strategy = new FallStraight();
        Bomb bomb1 = new Bomb(GameObject.GameObjectName.BOMB, strategy, 50.0f, 100.0f);
        Bomb bomb2 = new Bomb(GameObject.GameObjectName.BOMB, strategy, 60.0f, 110.0f);
        Bomb bomb3 = new Bomb(GameObject.GameObjectName.BOMB, strategy, 70.0f, 120.0f);
        
        bombRoot.add(bomb1);
        bombRoot.add(bomb2);
        bombRoot.add(bomb3);
        
        assertEquals(3, bombRoot.getNumChildren());
    }
    
    @Test
    @DisplayName("Can remove bomb children")
    void testRemoveBomb() {
        FallStraight strategy = new FallStraight();
        Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 50.0f, 100.0f);
        
        bombRoot.add(bomb);
        assertEquals(1, bombRoot.getNumChildren());
        
        bombRoot.remove(bomb);
        assertEquals(0, bombRoot.getNumChildren());
    }
    
    @Test
    @DisplayName("GetFirstChild should return first bomb")
    void testGetFirstChild() {
        FallStraight strategy = new FallStraight();
        Bomb bomb1 = new Bomb(GameObject.GameObjectName.BOMB, strategy, 50.0f, 100.0f);
        Bomb bomb2 = new Bomb(GameObject.GameObjectName.BOMB, strategy, 60.0f, 110.0f);
        
        bombRoot.add(bomb1);
        bombRoot.add(bomb2);
        
        // Last added is first (added to front)
        assertSame(bomb2, bombRoot.getFirstChild());
    }
    
    @Test
    @DisplayName("GetFirstChild should return null when no children")
    void testGetFirstChildEmpty() {
        assertNull(bombRoot.getFirstChild());
    }
    
    @Test
    @DisplayName("Accept should call visitor's visit method")
    void testAccept() {
        TestVisitor visitor = new TestVisitor();
        
        bombRoot.accept(visitor);
        
        assertTrue(visitor.visitCalled);
        assertSame(bombRoot, visitor.visitedObject);
    }
    
    @Test
    @DisplayName("Update should not throw exception")
    void testUpdate() {
        assertDoesNotThrow(() -> bombRoot.update());
    }
    
    @Test
    @DisplayName("Update with children should not throw exception")
    void testUpdateWithChildren() {
        FallStraight strategy = new FallStraight();
        Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 50.0f, 100.0f);
        bombRoot.add(bomb);
        
        assertDoesNotThrow(() -> bombRoot.update());
    }
    
    @Test
    @DisplayName("Dump should not throw exception")
    void testDump() {
        assertDoesNotThrow(() -> bombRoot.dump());
    }
    
    @Test
    @DisplayName("Dump with children should not throw exception")
    void testDumpWithChildren() {
        FallStraight strategy = new FallStraight();
        Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 50.0f, 100.0f);
        bombRoot.add(bomb);
        
        assertDoesNotThrow(() -> bombRoot.dump());
    }
    
    @Test
    @DisplayName("Can create multiple BombRoots with different names")
    void testMultipleBombRoots() {
        BombRoot root1 = new BombRoot(Composite.CompositeName.BOMB_ROOT);
        BombRoot root2 = new BombRoot(Composite.CompositeName.FLYING_SAUCER_ROOT);
        
        assertEquals(Composite.CompositeName.BOMB_ROOT, root1.getCompositeName());
        assertEquals(Composite.CompositeName.FLYING_SAUCER_ROOT, root2.getCompositeName());
    }
    
    @Test
    @DisplayName("BombRoot should be a Composite")
    void testIsComposite() {
        assertTrue(bombRoot instanceof Composite);
    }
    
    @Test
    @DisplayName("BombRoot should be a GameObject")
    void testIsGameObject() {
        assertTrue(bombRoot instanceof GameObject);
    }
    
    /**
     * Test visitor implementation
     */
    private static class TestVisitor implements GameObject.CollisionVisitor {
        boolean visitCalled = false;
        GameObject visitedObject = null;
        
        @Override
        public void visit(GameObject other) {
            visitCalled = true;
            visitedObject = other;
        }
    }
}