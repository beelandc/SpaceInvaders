package net.beeland.spaceinvaders.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Manager base class
 */
@DisplayName("Manager Tests")
class ManagerTest {
    
    private TestManager manager;
    
    /**
     * Concrete implementation of Manager for testing
     */
    private static class TestNode extends DLink {
        private String data;
        
        public TestNode(String data) {
            this.data = data;
        }
        
        public String getData() {
            return data;
        }
        
        @Override
        protected void wash() {
            this.data = "washed";
        }
        
        @Override
        public void dump() {
            System.out.println("TestNode: " + data);
        }
    }
    
    private static class TestManager extends Manager {
        @Override
        protected DLink createNode() {
            return new TestNode("test");
        }
        
        public TestNode getNode() {
            return (TestNode) getFromPool();
        }
        
        public void returnNode(TestNode node) {
            returnToPool(node);
        }
    }
    
    @BeforeEach
    void setUp() {
        manager = new TestManager();
    }
    
    @Test
    @DisplayName("Should initialize with specified reserve size")
    void testInitialize() {
        manager.initialize(5, 3);
        
        String stats = manager.getStats();
        assertTrue(stats.contains("Reserved: 5"));
        assertTrue(stats.contains("Active: 0"));
    }
    
    @Test
    @DisplayName("Should get node from pool")
    void testGetFromPool() {
        manager.initialize(5, 3);
        
        TestNode node = manager.getNode();
        
        assertNotNull(node);
        String stats = manager.getStats();
        assertTrue(stats.contains("Active: 1"));
        assertTrue(stats.contains("Reserved: 4"));
    }
    
    @Test
    @DisplayName("Should return node to pool")
    void testReturnToPool() {
        manager.initialize(5, 3);
        
        TestNode node = manager.getNode();
        manager.returnNode(node);
        
        String stats = manager.getStats();
        assertTrue(stats.contains("Active: 0"));
        assertTrue(stats.contains("Reserved: 5"));
    }
    
    @Test
    @DisplayName("Should wash node when returning to pool")
    void testWashOnReturn() {
        manager.initialize(5, 3);
        
        TestNode node = manager.getNode();
        String originalData = node.getData();
        
        manager.returnNode(node);
        
        assertEquals("washed", node.getData());
    }
    
    @Test
    @DisplayName("Should grow pool when exhausted")
    void testPoolGrowth() {
        manager.initialize(2, 5);
        
        // Exhaust the pool
        TestNode node1 = manager.getNode();
        TestNode node2 = manager.getNode();
        
        // This should trigger growth
        TestNode node3 = manager.getNode();
        
        assertNotNull(node3);
        String stats = manager.getStats();
        assertTrue(stats.contains("Active: 3"));
    }
    
    @Test
    @DisplayName("Should track peak active count")
    void testPeakTracking() {
        manager.initialize(10, 5);
        
        TestNode node1 = manager.getNode();
        TestNode node2 = manager.getNode();
        TestNode node3 = manager.getNode();
        
        manager.returnNode(node1);
        
        String stats = manager.getStats();
        assertTrue(stats.contains("Peak: 3"));
        assertTrue(stats.contains("Active: 2"));
    }
    
    @Test
    @DisplayName("Should get active head")
    void testGetActiveHead() {
        manager.initialize(5, 3);
        
        assertNull(manager.getActiveHead());
        
        TestNode node = manager.getNode();
        
        assertEquals(node, manager.getActiveHead());
    }
    
    @Test
    @DisplayName("Should handle multiple nodes in active list")
    void testMultipleActiveNodes() {
        manager.initialize(10, 5);
        
        TestNode node1 = manager.getNode();
        TestNode node2 = manager.getNode();
        TestNode node3 = manager.getNode();
        
        // Verify all are in active list
        DLink current = manager.getActiveHead();
        int count = 0;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        
        assertEquals(3, count);
    }
    
    @Test
    @DisplayName("Should destroy manager and cleanup")
    void testDestroy() {
        manager.initialize(5, 3);
        
        TestNode node1 = manager.getNode();
        TestNode node2 = manager.getNode();
        
        manager.destroy();
        
        String stats = manager.getStats();
        assertTrue(stats.contains("Active: 0"));
        assertTrue(stats.contains("Reserved: 0"));
    }
    
    @Test
    @DisplayName("Should handle return null node gracefully")
    void testReturnNullNode() {
        manager.initialize(5, 3);
        
        // Should not crash
        assertDoesNotThrow(() -> manager.returnNode(null));
    }
    
    @Test
    @DisplayName("Should maintain correct statistics")
    void testStatistics() {
        manager.initialize(10, 5);
        
        TestNode node1 = manager.getNode();
        TestNode node2 = manager.getNode();
        TestNode node3 = manager.getNode();
        
        manager.returnNode(node1);
        
        String stats = manager.getStats();
        assertTrue(stats.contains("TestManager"));
        assertTrue(stats.contains("Active: 2"));
        assertTrue(stats.contains("Reserved: 8"));
        assertTrue(stats.contains("Peak: 3"));
    }
    
    @Test
    @DisplayName("Should handle getting all nodes from pool")
    void testExhaustPool() {
        manager.initialize(3, 5);
        
        TestNode node1 = manager.getNode();
        TestNode node2 = manager.getNode();
        TestNode node3 = manager.getNode();
        
        // Pool should be exhausted, but growth should provide more
        TestNode node4 = manager.getNode();
        
        assertNotNull(node4);
    }
}
