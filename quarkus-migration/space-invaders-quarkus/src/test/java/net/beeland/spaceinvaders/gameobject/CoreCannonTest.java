package net.beeland.spaceinvaders.gameobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CoreCannon class
 */
@DisplayName("CoreCannon Tests")
class CoreCannonTest {
    
    private CoreCannon cannon;
    private static final float MIN_X = 50.0f;
    private static final float MAX_X = 750.0f;
    private static final float SPEED = 3.0f;
    
    @BeforeEach
    void setUp() {
        cannon = new CoreCannon(GameObject.GameObjectName.CORE_CANNON, 400.0f, 50.0f);
    }
    
    @Test
    @DisplayName("Constructor should initialize with correct name")
    void testConstructor() {
        assertEquals(GameObject.GameObjectName.CORE_CANNON, cannon.getName());
    }
    
    @Test
    @DisplayName("Constructor should set initial position")
    void testConstructorPosition() {
        assertEquals(400.0f, cannon.getX(), 0.001f);
        assertEquals(50.0f, cannon.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("MoveLeft should decrease X position")
    void testMoveLeft() {
        float initialX = cannon.getX();
        
        cannon.moveLeft();
        
        assertEquals(initialX - SPEED, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("MoveRight should increase X position")
    void testMoveRight() {
        float initialX = cannon.getX();
        
        cannon.moveRight();
        
        assertEquals(initialX + SPEED, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("MoveLeft should not go below minimum X")
    void testMoveLeftBoundary() {
        cannon.setX(MIN_X);
        
        cannon.moveLeft();
        
        assertEquals(MIN_X, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("MoveRight should not go above maximum X")
    void testMoveRightBoundary() {
        cannon.setX(MAX_X);
        
        cannon.moveRight();
        
        assertEquals(MAX_X, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("Multiple moveLeft calls should respect boundary")
    void testMultipleMoveLeftAtBoundary() {
        cannon.setX(MIN_X + 1.0f);
        
        cannon.moveLeft();
        cannon.moveLeft();
        cannon.moveLeft();
        
        assertEquals(MIN_X, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("Multiple moveRight calls should respect boundary")
    void testMultipleMoveRightAtBoundary() {
        cannon.setX(MAX_X - 1.0f);
        
        cannon.moveRight();
        cannon.moveRight();
        cannon.moveRight();
        
        assertEquals(MAX_X, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("Y position should not change when moving left")
    void testMoveLeftPreservesY() {
        float initialY = cannon.getY();
        
        cannon.moveLeft();
        
        assertEquals(initialY, cannon.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Y position should not change when moving right")
    void testMoveRightPreservesY() {
        float initialY = cannon.getY();
        
        cannon.moveRight();
        
        assertEquals(initialY, cannon.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Move should update position")
    void testMove() {
        cannon.setX(100.0f);
        cannon.setY(200.0f);
        
        cannon.move(10.0f, 20.0f);
        
        assertEquals(110.0f, cannon.getX(), 0.001f);
        assertEquals(220.0f, cannon.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Move with negative deltas should work")
    void testMoveNegative() {
        cannon.setX(100.0f);
        cannon.setY(200.0f);
        
        cannon.move(-10.0f, -20.0f);
        
        assertEquals(90.0f, cannon.getX(), 0.001f);
        assertEquals(180.0f, cannon.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Accept should call visitor's visit method")
    void testAccept() {
        TestVisitor visitor = new TestVisitor();
        
        cannon.accept(visitor);
        
        assertTrue(visitor.visitCalled);
        assertSame(cannon, visitor.visitedObject);
    }
    
    @Test
    @DisplayName("Can move from center to left boundary")
    void testMoveFromCenterToLeft() {
        cannon.setX(400.0f);
        
        // Move left until boundary
        for (int i = 0; i < 200; i++) {
            cannon.moveLeft();
        }
        
        assertEquals(MIN_X, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("Can move from center to right boundary")
    void testMoveFromCenterToRight() {
        cannon.setX(400.0f);
        
        // Move right until boundary
        for (int i = 0; i < 200; i++) {
            cannon.moveRight();
        }
        
        assertEquals(MAX_X, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("Can move back and forth")
    void testMoveBackAndForth() {
        float startX = cannon.getX();
        
        cannon.moveRight();
        cannon.moveRight();
        cannon.moveLeft();
        cannon.moveLeft();
        
        assertEquals(startX, cannon.getX(), 0.001f);
    }
    
    @Test
    @DisplayName("Constructor with different positions")
    void testConstructorVariousPositions() {
        CoreCannon cannon1 = new CoreCannon(GameObject.GameObjectName.CORE_CANNON, 100.0f, 50.0f);
        assertEquals(100.0f, cannon1.getX(), 0.001f);
        assertEquals(50.0f, cannon1.getY(), 0.001f);
        
        CoreCannon cannon2 = new CoreCannon(GameObject.GameObjectName.CORE_CANNON, 700.0f, 100.0f);
        assertEquals(700.0f, cannon2.getX(), 0.001f);
        assertEquals(100.0f, cannon2.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Update should not change position")
    void testUpdate() {
        float initialX = cannon.getX();
        float initialY = cannon.getY();
        
        cannon.update();
        
        assertEquals(initialX, cannon.getX(), 0.001f);
        assertEquals(initialY, cannon.getY(), 0.001f);
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