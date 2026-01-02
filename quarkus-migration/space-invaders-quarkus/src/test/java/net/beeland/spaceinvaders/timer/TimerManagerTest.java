package net.beeland.spaceinvaders.timer;

import net.beeland.spaceinvaders.command.DebugCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimerManager class
 */
class TimerManagerTest {
    
    private TimerManager timerManager;
    
    @BeforeEach
    void setUp() {
        timerManager = new TimerManager();
    }
    
    @Test
    @DisplayName("TimerManager should initialize with zero current time")
    void testInitialization() {
        assertEquals(0.0f, timerManager.getCurrentTime());
    }
    
    @Test
    @DisplayName("TimerManager should add time event successfully")
    void testAddTimeEvent() {
        DebugCommand command = new DebugCommand("Test");
        
        TimeEvent event = timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 5.0f);
        
        assertNotNull(event);
        assertEquals(TimeEvent.Name.DEBUG_COMMAND, event.getName());
        assertSame(command, event.getCommand());
        assertEquals(5.0f, event.getDeltaTime());
    }
    
    @Test
    @DisplayName("TimerManager should find event by name")
    void testFindEvent() {
        DebugCommand command = new DebugCommand("Test");
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command, 1.0f);
        
        TimeEvent found = timerManager.find(TimeEvent.Name.SPRITE_ANIMATION);
        
        assertNotNull(found);
        assertEquals(TimeEvent.Name.SPRITE_ANIMATION, found.getName());
    }
    
    @Test
    @DisplayName("TimerManager should return null for non-existent event")
    void testFindNonExistentEvent() {
        TimeEvent found = timerManager.find(TimeEvent.Name.MISSILE_MOVEMENT);
        assertNull(found);
    }
    
    @Test
    @DisplayName("TimerManager should remove event")
    void testRemoveEvent() {
        DebugCommand command = new DebugCommand("Test");
        TimeEvent event = timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 1.0f);
        
        assertNotNull(timerManager.find(TimeEvent.Name.DEBUG_COMMAND));
        
        timerManager.remove(event);
        
        assertNull(timerManager.find(TimeEvent.Name.DEBUG_COMMAND));
    }
    
    @Test
    @DisplayName("TimerManager should pop first event")
    void testPopEvent() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 5.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 10.0f);
        
        TimeEvent popped = timerManager.pop();
        
        assertNotNull(popped);
        assertEquals(TimeEvent.Name.SPRITE_ANIMATION, popped.getName());
    }
    
    @Test
    @DisplayName("TimerManager should return null when popping empty list")
    void testPopEmptyList() {
        TimeEvent popped = timerManager.pop();
        assertNull(popped);
    }
    
    @Test
    @DisplayName("TimerManager should update current time")
    void testUpdateCurrentTime() {
        timerManager.update(10.5f);
        assertEquals(10.5f, timerManager.getCurrentTime());
        
        timerManager.update(25.0f);
        assertEquals(25.0f, timerManager.getCurrentTime());
    }
    
    @Test
    @DisplayName("TimerManager should execute events when time reached")
    void testExecuteEventsOnUpdate() {
        DebugCommand command = new DebugCommand("Test");
        timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 5.0f);
        
        assertEquals(0, command.getExecutionCount());
        
        // Update to time before trigger
        timerManager.update(4.0f);
        assertEquals(0, command.getExecutionCount());
        
        // Update to trigger time
        timerManager.update(5.0f);
        assertEquals(1, command.getExecutionCount());
    }
    
    @Test
    @DisplayName("TimerManager should execute multiple events in order")
    void testExecuteMultipleEvents() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        DebugCommand command3 = new DebugCommand("Command 3");
        
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 2.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 5.0f);
        timerManager.add(TimeEvent.Name.DROP_BOMB, command3, 3.0f);
        
        // Update to time that triggers all events
        timerManager.update(10.0f);
        
        assertEquals(1, command1.getExecutionCount());
        assertEquals(1, command2.getExecutionCount());
        assertEquals(1, command3.getExecutionCount());
    }
    
    @Test
    @DisplayName("TimerManager should maintain sorted order by trigger time")
    void testSortedOrderByTriggerTime() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        DebugCommand command3 = new DebugCommand("Command 3");
        
        // Add in non-sorted order
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 10.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 2.0f);
        timerManager.add(TimeEvent.Name.DROP_BOMB, command3, 5.0f);
        
        // Pop should return events in sorted order
        TimeEvent first = timerManager.pop();
        assertEquals(TimeEvent.Name.MISSILE_MOVEMENT, first.getName());
        
        TimeEvent second = timerManager.pop();
        assertEquals(TimeEvent.Name.DROP_BOMB, second.getName());
        
        TimeEvent third = timerManager.pop();
        assertEquals(TimeEvent.Name.SPRITE_ANIMATION, third.getName());
    }
    
    @Test
    @DisplayName("TimerManager should handle events with same trigger time")
    void testSameTriggerTime() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 5.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 5.0f);
        
        timerManager.update(5.0f);
        
        assertEquals(1, command1.getExecutionCount());
        assertEquals(1, command2.getExecutionCount());
    }
    
    @Test
    @DisplayName("TimerManager should not execute future events")
    void testDoNotExecuteFutureEvents() {
        DebugCommand command = new DebugCommand("Test");
        timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 10.0f);
        
        timerManager.update(5.0f);
        
        assertEquals(0, command.getExecutionCount());
        assertNotNull(timerManager.find(TimeEvent.Name.DEBUG_COMMAND));
    }
    
    @Test
    @DisplayName("TimerManager should remove events after execution")
    void testRemoveEventsAfterExecution() {
        DebugCommand command = new DebugCommand("Test");
        timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 5.0f);
        
        assertNotNull(timerManager.find(TimeEvent.Name.DEBUG_COMMAND));
        
        timerManager.update(5.0f);
        
        assertNull(timerManager.find(TimeEvent.Name.DEBUG_COMMAND));
    }
    
    @Test
    @DisplayName("TimerManager should handle zero delta time")
    void testZeroDeltaTime() {
        DebugCommand command = new DebugCommand("Test");
        timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 0.0f);
        
        timerManager.update(0.0f);
        
        assertEquals(1, command.getExecutionCount());
    }
    
    @Test
    @DisplayName("TimerManager should handle adding event at head of list")
    void testAddEventAtHead() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 10.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 5.0f);
        
        TimeEvent first = timerManager.pop();
        assertEquals(TimeEvent.Name.MISSILE_MOVEMENT, first.getName());
    }
    
    @Test
    @DisplayName("TimerManager should handle adding event at tail of list")
    void testAddEventAtTail() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 5.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 10.0f);
        
        timerManager.pop(); // Remove first
        TimeEvent second = timerManager.pop();
        assertEquals(TimeEvent.Name.MISSILE_MOVEMENT, second.getName());
    }
    
    @Test
    @DisplayName("TimerManager should handle adding event in middle of list")
    void testAddEventInMiddle() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        DebugCommand command3 = new DebugCommand("Command 3");
        
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command1, 5.0f);
        timerManager.add(TimeEvent.Name.DROP_BOMB, command3, 15.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command2, 10.0f);
        
        timerManager.pop(); // 5.0f
        TimeEvent middle = timerManager.pop();
        assertEquals(TimeEvent.Name.MISSILE_MOVEMENT, middle.getName());
    }
    
    @Test
    @DisplayName("TimerManager should support pool growth")
    void testPoolGrowth() {
        DebugCommand command = new DebugCommand("Test");
        
        // Add more events than initial pool size (3)
        timerManager.add(TimeEvent.Name.SPRITE_ANIMATION, command, 1.0f);
        timerManager.add(TimeEvent.Name.MISSILE_MOVEMENT, command, 2.0f);
        timerManager.add(TimeEvent.Name.DROP_BOMB, command, 3.0f);
        timerManager.add(TimeEvent.Name.ALIEN_GRID_MOVEMENT, command, 4.0f);
        
        // Should not throw exception
        assertNotNull(timerManager.find(TimeEvent.Name.ALIEN_GRID_MOVEMENT));
    }
    
    @Test
    @DisplayName("TimerManager should handle large time values")
    void testLargeTimeValues() {
        DebugCommand command = new DebugCommand("Test");
        timerManager.add(TimeEvent.Name.DEBUG_COMMAND, command, 999999.0f);
        
        timerManager.update(1000000.0f);
        
        assertEquals(1, command.getExecutionCount());
    }
}