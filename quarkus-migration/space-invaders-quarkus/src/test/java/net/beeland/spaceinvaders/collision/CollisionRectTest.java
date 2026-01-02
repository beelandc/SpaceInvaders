package net.beeland.spaceinvaders.collision;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollisionRect class.
 * Tests rectangle creation, intersection detection, and union operations.
 */
@DisplayName("CollisionRect Tests")
class CollisionRectTest {

    @Test
    @DisplayName("Constructor with parameters creates rect with correct values")
    void testConstructorWithParameters() {
        CollisionRect rect = new CollisionRect(100.0f, 200.0f, 50.0f, 75.0f);
        
        assertEquals(100.0f, rect.x, 0.001f);
        assertEquals(200.0f, rect.y, 0.001f);
        assertEquals(50.0f, rect.width, 0.001f);
        assertEquals(75.0f, rect.height, 0.001f);
    }

    @Test
    @DisplayName("Default constructor creates rect with zero values")
    void testDefaultConstructor() {
        CollisionRect rect = new CollisionRect();
        
        assertEquals(0.0f, rect.x, 0.001f);
        assertEquals(0.0f, rect.y, 0.001f);
        assertEquals(0.0f, rect.width, 0.001f);
        assertEquals(0.0f, rect.height, 0.001f);
    }

    @Test
    @DisplayName("Copy constructor creates identical rect")
    void testCopyConstructor() {
        CollisionRect original = new CollisionRect(10.0f, 20.0f, 30.0f, 40.0f);
        CollisionRect copy = new CollisionRect(original);
        
        assertEquals(original.x, copy.x, 0.001f);
        assertEquals(original.y, copy.y, 0.001f);
        assertEquals(original.width, copy.width, 0.001f);
        assertEquals(original.height, copy.height, 0.001f);
    }

    @Test
    @DisplayName("Set method updates all values")
    void testSetWithParameters() {
        CollisionRect rect = new CollisionRect();
        rect.set(50.0f, 60.0f, 70.0f, 80.0f);
        
        assertEquals(50.0f, rect.x, 0.001f);
        assertEquals(60.0f, rect.y, 0.001f);
        assertEquals(70.0f, rect.width, 0.001f);
        assertEquals(80.0f, rect.height, 0.001f);
    }

    @Test
    @DisplayName("Set method with rect parameter copies values")
    void testSetWithRect() {
        CollisionRect source = new CollisionRect(15.0f, 25.0f, 35.0f, 45.0f);
        CollisionRect target = new CollisionRect();
        target.set(source);
        
        assertEquals(source.x, target.x, 0.001f);
        assertEquals(source.y, target.y, 0.001f);
        assertEquals(source.width, target.width, 0.001f);
        assertEquals(source.height, target.height, 0.001f);
    }

    @Test
    @DisplayName("Intersect returns true for overlapping rectangles")
    void testIntersectOverlapping() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(110.0f, 110.0f, 50.0f, 50.0f);
        
        assertTrue(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Intersect returns false for non-overlapping rectangles")
    void testIntersectNonOverlapping() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(200.0f, 200.0f, 50.0f, 50.0f);
        
        assertFalse(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Intersect returns true for touching rectangles")
    void testIntersectTouching() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        // rectA edges: left=75, right=125, bottom=75, top=125
        CollisionRect rectB = new CollisionRect(150.0f, 100.0f, 50.0f, 50.0f);
        // rectB edges: left=125, right=175, bottom=75, top=125
        
        assertTrue(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Intersect returns false when rect B is to the right of rect A")
    void testIntersectRightSide() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(200.0f, 100.0f, 50.0f, 50.0f);
        
        assertFalse(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Intersect returns false when rect B is to the left of rect A")
    void testIntersectLeftSide() {
        CollisionRect rectA = new CollisionRect(200.0f, 100.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        
        assertFalse(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Intersect returns false when rect B is above rect A")
    void testIntersectAbove() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(100.0f, 200.0f, 50.0f, 50.0f);
        
        assertFalse(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Intersect returns false when rect B is below rect A")
    void testIntersectBelow() {
        CollisionRect rectA = new CollisionRect(100.0f, 200.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        
        assertFalse(CollisionRect.intersect(rectA, rectB));
    }

    @Test
    @DisplayName("Union combines two rectangles correctly")
    void testUnion() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        // rectA: center=(100,100), edges: left=75, right=125, bottom=75, top=125
        
        CollisionRect rectB = new CollisionRect(150.0f, 150.0f, 50.0f, 50.0f);
        // rectB: center=(150,150), edges: left=125, right=175, bottom=125, top=175
        
        rectA.union(rectB);
        
        // Union should contain both rectangles
        // Expected: left=75, right=175, bottom=75, top=175
        // Width = 175-75 = 100, Height = 175-75 = 100
        // Center = (75+100/2, 75+100/2) = (125, 125)
        
        assertEquals(125.0f, rectA.x, 0.001f);
        assertEquals(125.0f, rectA.y, 0.001f);
        assertEquals(100.0f, rectA.width, 0.001f);
        assertEquals(100.0f, rectA.height, 0.001f);
    }

    @Test
    @DisplayName("Union with identical rect doesn't change dimensions")
    void testUnionIdentical() {
        CollisionRect rectA = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        CollisionRect rectB = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        
        rectA.union(rectB);
        
        assertEquals(100.0f, rectA.x, 0.001f);
        assertEquals(100.0f, rectA.y, 0.001f);
        assertEquals(50.0f, rectA.width, 0.001f);
        assertEquals(50.0f, rectA.height, 0.001f);
    }

    @Test
    @DisplayName("GetMinX returns correct left edge")
    void testGetMinX() {
        CollisionRect rect = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        assertEquals(75.0f, rect.getMinX(), 0.001f);
    }

    @Test
    @DisplayName("GetMaxX returns correct right edge")
    void testGetMaxX() {
        CollisionRect rect = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        assertEquals(125.0f, rect.getMaxX(), 0.001f);
    }

    @Test
    @DisplayName("GetMinY returns correct bottom edge")
    void testGetMinY() {
        CollisionRect rect = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        assertEquals(75.0f, rect.getMinY(), 0.001f);
    }

    @Test
    @DisplayName("GetMaxY returns correct top edge")
    void testGetMaxY() {
        CollisionRect rect = new CollisionRect(100.0f, 100.0f, 50.0f, 50.0f);
        assertEquals(125.0f, rect.getMaxY(), 0.001f);
    }

    @Test
    @DisplayName("ToString returns formatted string")
    void testToString() {
        CollisionRect rect = new CollisionRect(10.5f, 20.5f, 30.5f, 40.5f);
        String result = rect.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("10.50"));
        assertTrue(result.contains("20.50"));
        assertTrue(result.contains("30.50"));
        assertTrue(result.contains("40.50"));
    }
}