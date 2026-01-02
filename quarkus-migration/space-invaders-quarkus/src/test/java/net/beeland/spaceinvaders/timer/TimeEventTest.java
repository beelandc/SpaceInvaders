package net.beeland.spaceinvaders.timer;

import net.beeland.spaceinvaders.command.DebugCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimeEvent class
 */
class TimeEventTest {
    
    private TimeEvent timeEvent;
    private DebugCommand testCommand;
    
    @BeforeEach
    void setUp() {
        timeEvent = new TimeEvent();
        testCommand = new DebugCommand("Test Command");
    }
    
    @Test
    @DisplayName("TimeEvent should initialize with default values")
    void testDefaultInitialization() {
        assertEquals(TimeEvent.Name.UNINITIALIZED, timeEvent.getName());
        assertNull(timeEvent.getCommand());
        assertEquals(0.0f, timeEvent.getTriggerTime());
        assertEquals(0.0f, timeEvent.getDeltaTime());
    }
    
    @Test
    @DisplayName("TimeEvent should set properties correctly")
    void testSetProperties() {
        float currentTime = 10.0f;
        float deltaTime = 5.0f;
        
        timeEvent.set(TimeEvent.Name.DEBUG_COMMAND, testCommand, deltaTime, currentTime);
        
        assertEquals(TimeEvent.Name.DEBUG_COMMAND, timeEvent.getName());
        assertSame(testCommand, timeEvent.getCommand());
        assertEquals(deltaTime, timeEvent.getDeltaTime());
        assertEquals(currentTime + deltaTime, timeEvent.getTriggerTime());
    }
    
    @Test
    @DisplayName("TimeEvent should calculate trigger time correctly")
    void testTriggerTimeCalculation() {
        float currentTime = 100.0f;
        float deltaTime = 25.0f;
        
        timeEvent.set(TimeEvent.Name.SPRITE_ANIMATION, testCommand, deltaTime, currentTime);
        
        assertEquals(125.0f, timeEvent.getTriggerTime());
    }
    
    @Test
    @DisplayName("TimeEvent should process command")
    void testProcess() {
        timeEvent.set(TimeEvent.Name.DEBUG_COMMAND, testCommand, 1.0f, 0.0f);
        
        assertEquals(0, testCommand.getExecutionCount());
        
        timeEvent.process();
        
        assertEquals(1, testCommand.getExecutionCount());
    }
    
    @Test
    @DisplayName("TimeEvent should wash properly")
    void testWash() {
        timeEvent.set(TimeEvent.Name.MISSILE_MOVEMENT, testCommand, 2.0f, 5.0f);
        
        timeEvent.wash();
        
        assertEquals(TimeEvent.Name.UNINITIALIZED, timeEvent.getName());
        assertNull(timeEvent.getCommand());
        assertEquals(0.0f, timeEvent.getTriggerTime());
        assertEquals(0.0f, timeEvent.getDeltaTime());
    }
    
    @Test
    @DisplayName("TimeEvent should support all event names")
    void testAllEventNames() {
        TimeEvent.Name[] names = {
            TimeEvent.Name.SPRITE_ANIMATION,
            TimeEvent.Name.SPRITE_MOVEMENT,
            TimeEvent.Name.MISSILE_MOVEMENT,
            TimeEvent.Name.ALIEN_GRID_MOVEMENT_SOUND,
            TimeEvent.Name.DROP_BOMB,
            TimeEvent.Name.DEBUG_COMMAND,
            TimeEvent.Name.DEPLOY_FLYING_SAUCER,
            TimeEvent.Name.RESET_LEVEL,
            TimeEvent.Name.ALIEN_GRID_MOVEMENT,
            TimeEvent.Name.RESET_CORE_CANNON,
            TimeEvent.Name.UNINITIALIZED
        };
        
        for (TimeEvent.Name name : names) {
            timeEvent.setName(name);
            assertEquals(name, timeEvent.getName());
        }
    }
    
    @Test
    @DisplayName("TimeEvent should handle zero delta time")
    void testZeroDeltaTime() {
        float currentTime = 50.0f;
        
        timeEvent.set(TimeEvent.Name.DEBUG_COMMAND, testCommand, 0.0f, currentTime);
        
        assertEquals(0.0f, timeEvent.getDeltaTime());
        assertEquals(currentTime, timeEvent.getTriggerTime());
    }
    
    @Test
    @DisplayName("TimeEvent should support DLink operations")
    void testDLinkOperations() {
        TimeEvent event1 = new TimeEvent();
        TimeEvent event2 = new TimeEvent();
        
        event1.setNext(event2);
        event2.setPrev(event1);
        
        assertSame(event2, event1.getNext());
        assertSame(event1, event2.getPrev());
    }
    
    @Test
    @DisplayName("TimeEvent should allow manual trigger time setting")
    void testManualTriggerTimeSetting() {
        timeEvent.setTriggerTime(123.45f);
        assertEquals(123.45f, timeEvent.getTriggerTime());
    }
    
    @Test
    @DisplayName("TimeEvent should allow manual delta time setting")
    void testManualDeltaTimeSetting() {
        timeEvent.setDeltaTime(67.89f);
        assertEquals(67.89f, timeEvent.getDeltaTime());
    }
    
    @Test
    @DisplayName("TimeEvent should allow command replacement")
    void testCommandReplacement() {
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        timeEvent.setCommand(command1);
        assertSame(command1, timeEvent.getCommand());
        
        timeEvent.setCommand(command2);
        assertSame(command2, timeEvent.getCommand());
    }
    
    @Test
    @DisplayName("TimeEvent dump should not throw exception")
    void testDump() {
        timeEvent.set(TimeEvent.Name.DEBUG_COMMAND, testCommand, 1.0f, 0.0f);
        assertDoesNotThrow(() -> timeEvent.dump());
    }
    
    @Test
    @DisplayName("TimeEvent should handle large time values")
    void testLargeTimeValues() {
        float currentTime = 999999.0f;
        float deltaTime = 100000.0f;
        
        timeEvent.set(TimeEvent.Name.DEBUG_COMMAND, testCommand, deltaTime, currentTime);
        
        assertEquals(1099999.0f, timeEvent.getTriggerTime());
    }
    
    @Test
    @DisplayName("Multiple TimeEvents should be independent")
    void testEventIndependence() {
        TimeEvent event1 = new TimeEvent();
        TimeEvent event2 = new TimeEvent();
        DebugCommand command1 = new DebugCommand("Command 1");
        DebugCommand command2 = new DebugCommand("Command 2");
        
        event1.set(TimeEvent.Name.SPRITE_ANIMATION, command1, 1.0f, 0.0f);
        event2.set(TimeEvent.Name.MISSILE_MOVEMENT, command2, 2.0f, 5.0f);
        
        assertEquals(TimeEvent.Name.SPRITE_ANIMATION, event1.getName());
        assertEquals(TimeEvent.Name.MISSILE_MOVEMENT, event2.getName());
        assertEquals(1.0f, event1.getTriggerTime());
        assertEquals(7.0f, event2.getTriggerTime());
    }
}