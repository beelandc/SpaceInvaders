package net.beeland.spaceinvaders.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Command pattern implementation
 */
class CommandTest {
    
    private DebugCommand debugCommand;
    
    @BeforeEach
    void setUp() {
        debugCommand = new DebugCommand("Test Command");
    }
    
    @Test
    @DisplayName("Command should execute successfully")
    void testCommandExecution() {
        // Execute the command
        debugCommand.execute(1.0f);
        
        // Verify execution count increased
        assertEquals(1, debugCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("Command should track multiple executions")
    void testMultipleExecutions() {
        // Execute multiple times
        debugCommand.execute(0.5f);
        debugCommand.execute(1.0f);
        debugCommand.execute(1.5f);
        
        // Verify execution count
        assertEquals(3, debugCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("Command execution count should reset")
    void testResetExecutionCount() {
        // Execute and reset
        debugCommand.execute(1.0f);
        debugCommand.execute(1.0f);
        assertEquals(2, debugCommand.getExecutionCount());
        
        debugCommand.resetExecutionCount();
        assertEquals(0, debugCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("Command should support DLink operations")
    void testDLinkOperations() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        // Link commands
        command1.setNext(command2);
        command2.setPrev(command1);
        
        // Verify links
        assertSame(command2, command1.getNext());
        assertSame(command1, command2.getPrev());
    }
    
    @Test
    @DisplayName("Command should wash properly")
    void testWash() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        command1.setNext(command2);
        command1.setPrev(command2);
        
        // Wash the command
        command1.wash();
        
        // Verify links are cleared
        assertNull(command1.getNext());
        assertNull(command1.getPrev());
    }
    
    @Test
    @DisplayName("Default constructor should work")
    void testDefaultConstructor() {
        DebugCommand defaultCommand = new DebugCommand();
        
        // Should be able to execute
        assertDoesNotThrow(() -> defaultCommand.execute(1.0f));
        assertEquals(1, defaultCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("Command should handle zero delta time")
    void testZeroDeltaTime() {
        assertDoesNotThrow(() -> debugCommand.execute(0.0f));
        assertEquals(1, debugCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("Command should handle negative delta time")
    void testNegativeDeltaTime() {
        // Should still execute (validation is in TimerManager)
        assertDoesNotThrow(() -> debugCommand.execute(-1.0f));
        assertEquals(1, debugCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("Command dump should not throw exception")
    void testDump() {
        assertDoesNotThrow(() -> debugCommand.dump());
    }
    
    @Test
    @DisplayName("Multiple commands should be independent")
    void testCommandIndependence() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        command1.execute(1.0f);
        command1.execute(1.0f);
        command2.execute(1.0f);
        
        assertEquals(2, command1.getExecutionCount());
        assertEquals(1, command2.getExecutionCount());
    }
}