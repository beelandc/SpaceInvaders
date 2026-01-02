package net.beeland.spaceinvaders.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DLink doubly-linked list node
 */
@DisplayName("DLink Tests")
class DLinkTest {
    
    private TestDLink node1;
    private TestDLink node2;
    private TestDLink node3;
    
    /**
     * Concrete implementation of DLink for testing
     */
    private static class TestDLink extends DLink {
        private String data;
        private boolean wasWashed = false;
        
        public TestDLink(String data) {
            this.data = data;
        }
        
        public String getData() {
            return data;
        }
        
        @Override
        protected void wash() {
            this.data = null;
            this.wasWashed = true;
        }
        
        public boolean wasWashed() {
            return wasWashed;
        }
        
        @Override
        public void dump() {
            System.out.println("TestDLink: " + data);
        }
    }
    
    @BeforeEach
    void setUp() {
        node1 = new TestDLink("Node1");
        node2 = new TestDLink("Node2");
        node3 = new TestDLink("Node3");
    }
    
    @Test
    @DisplayName("New node should have null links")
    void testNewNodeHasNullLinks() {
        assertNull(node1.getNext());
        assertNull(node1.getPrev());
        assertFalse(node1.hasNext());
        assertFalse(node1.hasPrev());
    }
    
    @Test
    @DisplayName("Should set and get next node")
    void testSetAndGetNext() {
        node1.setNext(node2);
        
        assertEquals(node2, node1.getNext());
        assertTrue(node1.hasNext());
    }
    
    @Test
    @DisplayName("Should set and get previous node")
    void testSetAndGetPrev() {
        node2.setPrev(node1);
        
        assertEquals(node1, node2.getPrev());
        assertTrue(node2.hasPrev());
    }
    
    @Test
    @DisplayName("Should clear node links")
    void testClear() {
        node1.setNext(node2);
        node1.setPrev(node3);
        
        node1.clear();
        
        assertNull(node1.getNext());
        assertNull(node1.getPrev());
        assertFalse(node1.hasNext());
        assertFalse(node1.hasPrev());
    }
    
    @Test
    @DisplayName("Should insert node after another node")
    void testInsertAfter() {
        // Create chain: node1 -> node3
        node1.setNext(node3);
        node3.setPrev(node1);
        
        // Insert node2 after node1: node1 -> node2 -> node3
        node2.insertAfter(node1);
        
        assertEquals(node2, node1.getNext());
        assertEquals(node1, node2.getPrev());
        assertEquals(node3, node2.getNext());
        assertEquals(node2, node3.getPrev());
    }
    
    @Test
    @DisplayName("Should insert node before another node")
    void testInsertBefore() {
        // Create chain: node1 -> node3
        node1.setNext(node3);
        node3.setPrev(node1);
        
        // Insert node2 before node3: node1 -> node2 -> node3
        node2.insertBefore(node3);
        
        assertEquals(node2, node1.getNext());
        assertEquals(node1, node2.getPrev());
        assertEquals(node3, node2.getNext());
        assertEquals(node2, node3.getPrev());
    }
    
    @Test
    @DisplayName("Should remove node from list")
    void testRemove() {
        // Create chain: node1 -> node2 -> node3
        node1.setNext(node2);
        node2.setPrev(node1);
        node2.setNext(node3);
        node3.setPrev(node2);
        
        // Remove node2
        node2.remove();
        
        // Should connect node1 -> node3
        assertEquals(node3, node1.getNext());
        assertEquals(node1, node3.getPrev());
        
        // node2 should be cleared
        assertNull(node2.getNext());
        assertNull(node2.getPrev());
    }
    
    @Test
    @DisplayName("Should handle insert after null node")
    void testInsertAfterNull() {
        node1.insertAfter(null);
        
        // Should not crash, node should remain unchanged
        assertNull(node1.getNext());
        assertNull(node1.getPrev());
    }
    
    @Test
    @DisplayName("Should handle insert before null node")
    void testInsertBeforeNull() {
        node1.insertBefore(null);
        
        // Should not crash, node should remain unchanged
        assertNull(node1.getNext());
        assertNull(node1.getPrev());
    }
    
    @Test
    @DisplayName("Should call wash method")
    void testWash() {
        node1.wash();
        
        assertTrue(node1.wasWashed());
        assertNull(node1.getData());
    }
    
    @Test
    @DisplayName("Should handle remove on isolated node")
    void testRemoveIsolatedNode() {
        node1.remove();
        
        // Should not crash
        assertNull(node1.getNext());
        assertNull(node1.getPrev());
    }
    
    @Test
    @DisplayName("Should handle remove on head node")
    void testRemoveHeadNode() {
        // Create chain: node1 -> node2
        node1.setNext(node2);
        node2.setPrev(node1);
        
        node1.remove();
        
        assertNull(node2.getPrev());
        assertNull(node1.getNext());
    }
    
    @Test
    @DisplayName("Should handle remove on tail node")
    void testRemoveTailNode() {
        // Create chain: node1 -> node2
        node1.setNext(node2);
        node2.setPrev(node1);
        
        node2.remove();
        
        assertNull(node1.getNext());
        assertNull(node2.getPrev());
    }
}
