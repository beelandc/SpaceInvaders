package net.beeland.spaceinvaders.gameobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Missile class
 */
@DisplayName("Missile Tests")
class MissileTest {
    
    private Missile missile;
    
    @BeforeEach
    void setUp() {
        missile = new Missile(GameObject.GameObjectName.MISSILE, 0.0f, 0.0f);
    }
    
    @Test
    @DisplayName("Constructor should initialize with correct name")
    void testConstructor() {
        assertEquals(GameObject.GameObjectName.MISSILE, missile.getName());
    }
    
    @Test
    @DisplayName("Constructor should initialize as inactive")
    void testConstructorInactive() {
        assertFalse(missile.isActive());
    }
    
    @Test
    @DisplayName("Launch should activate missile and set position")
    void testLaunch() {
        float startX = 100.0f;
        float startY = 200.0f;
        
        missile.launch(startX, startY);
        
        assertTrue(missile.isActive());
        assertEquals(startX, missile.getX(), 0.001f);
        assertEquals(startY, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Update should move missile upward when active")
    void testUpdateMovesUpward() {
        float startX = 100.0f;
        float startY = 200.0f;
        missile.launch(startX, startY);
        
        missile.update();
        
        assertEquals(startX, missile.getX(), 0.001f);
        assertEquals(startY + 10.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Update should not move missile when inactive")
    void testUpdateWhenInactive() {
        missile.setX(100.0f);
        missile.setY(200.0f);
        
        missile.update();
        
        assertEquals(100.0f, missile.getX(), 0.001f);
        assertEquals(200.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Multiple updates should continue moving upward")
    void testMultipleUpdates() {
        float startX = 100.0f;
        float startY = 200.0f;
        missile.launch(startX, startY);
        
        missile.update();
        missile.update();
        missile.update();
        
        assertEquals(startX, missile.getX(), 0.001f);
        assertEquals(startY + 30.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Reset should deactivate missile")
    void testReset() {
        missile.launch(100.0f, 200.0f);
        assertTrue(missile.isActive());
        
        missile.reset();
        
        assertFalse(missile.isActive());
    }
    
    @Test
    @DisplayName("Reset should move missile off-screen")
    void testResetMovesOffScreen() {
        float x = 100.0f;
        float y = 200.0f;
        missile.launch(x, y);
        
        missile.reset();
        
        assertEquals(-100.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Move should update position")
    void testMove() {
        missile.setX(100.0f);
        missile.setY(200.0f);
        
        missile.move(10.0f, 20.0f);
        
        assertEquals(110.0f, missile.getX(), 0.001f);
        assertEquals(220.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Move with negative deltas should work")
    void testMoveNegative() {
        missile.setX(100.0f);
        missile.setY(200.0f);
        
        missile.move(-10.0f, -20.0f);
        
        assertEquals(90.0f, missile.getX(), 0.001f);
        assertEquals(180.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Accept should call visitor's visit method")
    void testAccept() {
        TestVisitor visitor = new TestVisitor();
        
        missile.accept(visitor);
        
        assertTrue(visitor.visitCalled);
        assertSame(missile, visitor.visitedObject);
    }
    
    @Test
    @DisplayName("Launch after reset should reactivate missile")
    void testLaunchAfterReset() {
        missile.launch(100.0f, 200.0f);
        missile.reset();
        assertFalse(missile.isActive());
        
        missile.launch(150.0f, 250.0f);
        
        assertTrue(missile.isActive());
        assertEquals(150.0f, missile.getX(), 0.001f);
        assertEquals(250.0f, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Update after reset should not move missile")
    void testUpdateAfterReset() {
        missile.launch(100.0f, 200.0f);
        missile.update();
        
        missile.reset();
        float yAfterReset = missile.getY();
        missile.update();
        
        // After reset, missile is inactive so update should not change position
        assertEquals(yAfterReset, missile.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("IsActive should return correct state")
    void testIsActive() {
        assertFalse(missile.isActive());
        
        missile.launch(100.0f, 200.0f);
        assertTrue(missile.isActive());
        
        missile.reset();
        assertFalse(missile.isActive());
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