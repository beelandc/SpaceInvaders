package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Alien subclasses
 */
class AlienTest {

    // ========== CrabAlien Tests ==========

    @Test
    @DisplayName("CrabAlien should initialize with correct position")
    void testCrabAlienInitialization() {
        CrabAlien crab = new CrabAlien(100.0f, 200.0f);
        
        assertEquals(100.0f, crab.getX());
        assertEquals(200.0f, crab.getY());
        assertEquals(GameObject.GameObjectName.CRAB_ALIEN, crab.getName());
    }

    @Test
    @DisplayName("CrabAlien should return correct points")
    void testCrabAlienPoints() {
        CrabAlien crab = new CrabAlien(0.0f, 0.0f);
        assertEquals(20, crab.getPoints());
    }

    @Test
    @DisplayName("CrabAlien should return correct type")
    void testCrabAlienType() {
        CrabAlien crab = new CrabAlien(0.0f, 0.0f);
        assertEquals(Alien.Type.CRAB, crab.getType());
    }

    @Test
    @DisplayName("CrabAlien should update without error")
    void testCrabAlienUpdate() {
        CrabAlien crab = new CrabAlien(0.0f, 0.0f);
        assertDoesNotThrow(() -> crab.update());
    }

    @Test
    @DisplayName("CrabAlien should handle position changes")
    void testCrabAlienPositionChange() {
        CrabAlien crab = new CrabAlien(100.0f, 200.0f);
        
        crab.setX(150.0f);
        crab.setY(250.0f);
        
        assertEquals(150.0f, crab.getX());
        assertEquals(250.0f, crab.getY());
    }

    // ========== SquidAlien Tests ==========

    @Test
    @DisplayName("SquidAlien should initialize with correct position")
    void testSquidAlienInitialization() {
        SquidAlien squid = new SquidAlien(100.0f, 200.0f);
        
        assertEquals(100.0f, squid.getX());
        assertEquals(200.0f, squid.getY());
        assertEquals(GameObject.GameObjectName.SQUID_ALIEN, squid.getName());
    }

    @Test
    @DisplayName("SquidAlien should return correct points")
    void testSquidAlienPoints() {
        SquidAlien squid = new SquidAlien(0.0f, 0.0f);
        assertEquals(30, squid.getPoints());
    }

    @Test
    @DisplayName("SquidAlien should return correct type")
    void testSquidAlienType() {
        SquidAlien squid = new SquidAlien(0.0f, 0.0f);
        assertEquals(Alien.Type.SQUID, squid.getType());
    }

    @Test
    @DisplayName("SquidAlien should update without error")
    void testSquidAlienUpdate() {
        SquidAlien squid = new SquidAlien(0.0f, 0.0f);
        assertDoesNotThrow(() -> squid.update());
    }

    @Test
    @DisplayName("SquidAlien should handle position changes")
    void testSquidAlienPositionChange() {
        SquidAlien squid = new SquidAlien(100.0f, 200.0f);
        
        squid.setX(150.0f);
        squid.setY(250.0f);
        
        assertEquals(150.0f, squid.getX());
        assertEquals(250.0f, squid.getY());
    }

    // ========== JellyfishAlien Tests ==========

    @Test
    @DisplayName("JellyfishAlien should initialize with correct position")
    void testJellyfishAlienInitialization() {
        JellyfishAlien jellyfish = new JellyfishAlien(100.0f, 200.0f);
        
        assertEquals(100.0f, jellyfish.getX());
        assertEquals(200.0f, jellyfish.getY());
        assertEquals(GameObject.GameObjectName.JELLYFISH_ALIEN, jellyfish.getName());
    }

    @Test
    @DisplayName("JellyfishAlien should return correct points")
    void testJellyfishAlienPoints() {
        JellyfishAlien jellyfish = new JellyfishAlien(0.0f, 0.0f);
        assertEquals(10, jellyfish.getPoints());
    }

    @Test
    @DisplayName("JellyfishAlien should return correct type")
    void testJellyfishAlienType() {
        JellyfishAlien jellyfish = new JellyfishAlien(0.0f, 0.0f);
        assertEquals(Alien.Type.JELLYFISH, jellyfish.getType());
    }

    @Test
    @DisplayName("JellyfishAlien should update without error")
    void testJellyfishAlienUpdate() {
        JellyfishAlien jellyfish = new JellyfishAlien(0.0f, 0.0f);
        assertDoesNotThrow(() -> jellyfish.update());
    }

    @Test
    @DisplayName("JellyfishAlien should handle position changes")
    void testJellyfishAlienPositionChange() {
        JellyfishAlien jellyfish = new JellyfishAlien(100.0f, 200.0f);
        
        jellyfish.setX(150.0f);
        jellyfish.setY(250.0f);
        
        assertEquals(150.0f, jellyfish.getX());
        assertEquals(250.0f, jellyfish.getY());
    }

    // ========== FlyingSaucer Tests ==========

    @Test
    @DisplayName("FlyingSaucer should initialize with default constructor")
    void testFlyingSaucerDefaultConstructor() {
        FlyingSaucer saucer = new FlyingSaucer();
        
        assertEquals(0.0f, saucer.getX());
        assertEquals(0.0f, saucer.getY());
        assertEquals(GameObject.GameObjectName.FLYING_SAUCER, saucer.getName());
    }

    @Test
    @DisplayName("FlyingSaucer should initialize with position")
    void testFlyingSaucerInitialization() {
        FlyingSaucer saucer = new FlyingSaucer(GameObject.GameObjectName.FLYING_SAUCER, 100.0f, 200.0f);
        
        assertEquals(100.0f, saucer.getX());
        assertEquals(200.0f, saucer.getY());
        assertEquals(GameObject.GameObjectName.FLYING_SAUCER, saucer.getName());
    }

    @Test
    @DisplayName("FlyingSaucer should return random points in valid range")
    void testFlyingSaucerPoints() {
        FlyingSaucer saucer = new FlyingSaucer();
        
        // Test multiple times to ensure randomness
        for (int i = 0; i < 20; i++) {
            int points = saucer.getPoints();
            assertTrue(points == 50 || points == 100 || points == 150,
                "Points should be 50, 100, or 150, but was: " + points);
        }
    }

    @Test
    @DisplayName("FlyingSaucer should return correct type")
    void testFlyingSaucerType() {
        FlyingSaucer saucer = new FlyingSaucer();
        assertEquals(Alien.Type.FLYING_SAUCER, saucer.getType());
    }

    @Test
    @DisplayName("FlyingSaucer should update without error")
    void testFlyingSaucerUpdate() {
        FlyingSaucer saucer = new FlyingSaucer();
        assertDoesNotThrow(() -> saucer.update());
    }

    @Test
    @DisplayName("FlyingSaucer should handle position changes")
    void testFlyingSaucerPositionChange() {
        FlyingSaucer saucer = new FlyingSaucer(GameObject.GameObjectName.FLYING_SAUCER, 100.0f, 200.0f);
        
        saucer.setX(150.0f);
        saucer.setY(250.0f);
        
        assertEquals(150.0f, saucer.getX());
        assertEquals(250.0f, saucer.getY());
    }

    @Test
    @DisplayName("FlyingSaucer should dump without error")
    void testFlyingSaucerDump() {
        FlyingSaucer saucer = new FlyingSaucer(GameObject.GameObjectName.FLYING_SAUCER, 100.0f, 200.0f);
        assertDoesNotThrow(() -> saucer.dump());
    }

    @Test
    @DisplayName("FlyingSaucer should accept collision visitor")
    void testFlyingSaucerAcceptVisitor() {
        FlyingSaucer saucer = new FlyingSaucer();
        assertDoesNotThrow(() -> saucer.accept(new TestCollisionVisitor()));
    }
    
    // Simple test visitor for collision testing
    private static class TestCollisionVisitor implements GameObject.CollisionVisitor {
        @Override
        public void visit(GameObject gameObject) {
            // Test implementation
        }
        
        @Override
        public void visitFlyingSaucer(FlyingSaucer flyingSaucer) {
            // Test implementation
        }
    }

    // ========== FlyingSaucerRoot Tests ==========

    @Test
    @DisplayName("FlyingSaucerRoot should initialize with composite name and position")
    void testFlyingSaucerRootInitialization() {
        FlyingSaucerRoot root = new FlyingSaucerRoot(Composite.CompositeName.FLYING_SAUCER_ROOT, 100.0f, 200.0f);
        
        assertEquals(100.0f, root.getX());
        assertEquals(200.0f, root.getY());
        assertEquals(GameObject.GameObjectName.FLYING_SAUCER_ROOT, root.getName());
    }

    @Test
    @DisplayName("FlyingSaucerRoot should initialize with just composite name")
    void testFlyingSaucerRootDefaultPosition() {
        FlyingSaucerRoot root = new FlyingSaucerRoot(Composite.CompositeName.FLYING_SAUCER_ROOT);
        
        assertEquals(0.0f, root.getX());
        assertEquals(0.0f, root.getY());
        assertEquals(GameObject.GameObjectName.FLYING_SAUCER_ROOT, root.getName());
    }

    @Test
    @DisplayName("FlyingSaucerRoot should update without error")
    void testFlyingSaucerRootUpdate() {
        FlyingSaucerRoot root = new FlyingSaucerRoot(Composite.CompositeName.FLYING_SAUCER_ROOT);
        assertDoesNotThrow(() -> root.update());
    }

    @Test
    @DisplayName("FlyingSaucerRoot should dump without error")
    void testFlyingSaucerRootDump() {
        FlyingSaucerRoot root = new FlyingSaucerRoot(Composite.CompositeName.FLYING_SAUCER_ROOT);
        assertDoesNotThrow(() -> root.dump());
    }

    @Test
    @DisplayName("FlyingSaucerRoot should manage children")
    void testFlyingSaucerRootChildren() {
        FlyingSaucerRoot root = new FlyingSaucerRoot(Composite.CompositeName.FLYING_SAUCER_ROOT);
        FlyingSaucer saucer = new FlyingSaucer(GameObject.GameObjectName.FLYING_SAUCER, 100.0f, 200.0f);
        
        // Initially should have no children
        assertNull(root.getFirstChild());
        
        // Add child
        root.add(saucer);
        
        // Should now have a child
        assertNotNull(root.getFirstChild());
    }

    // ========== Alien Type Comparison Tests ==========

    @Test
    @DisplayName("Aliens should have different point values")
    void testAlienPointDifferences() {
        JellyfishAlien jellyfish = new JellyfishAlien(0.0f, 0.0f);
        CrabAlien crab = new CrabAlien(0.0f, 0.0f);
        SquidAlien squid = new SquidAlien(0.0f, 0.0f);
        
        assertTrue(jellyfish.getPoints() < crab.getPoints());
        assertTrue(crab.getPoints() < squid.getPoints());
        assertEquals(10, jellyfish.getPoints());
        assertEquals(20, crab.getPoints());
        assertEquals(30, squid.getPoints());
    }

    @Test
    @DisplayName("Aliens should have different types")
    void testAlienTypeDifferences() {
        JellyfishAlien jellyfish = new JellyfishAlien(0.0f, 0.0f);
        CrabAlien crab = new CrabAlien(0.0f, 0.0f);
        SquidAlien squid = new SquidAlien(0.0f, 0.0f);
        FlyingSaucer saucer = new FlyingSaucer();
        
        assertNotEquals(jellyfish.getType(), crab.getType());
        assertNotEquals(crab.getType(), squid.getType());
        assertNotEquals(squid.getType(), saucer.getType());
    }
}