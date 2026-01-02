package net.beeland.spaceinvaders.command;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.FlyingSaucer;
import net.beeland.spaceinvaders.gameobject.FlyingSaucerRoot;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.sprite.SpriteProxy;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Flying Saucer Command classes
 */
@QuarkusTest
class FlyingSaucerCommandTest {

    @Inject
    DeployFlyingSaucerCommand deployCommand;

    @Inject
    FlyingSaucerMovement movementCommand;

    @Inject
    DropFlyingSaucerBombCommand dropBombCommand;

    @InjectMock
    GameObjectManager gameObjectManager;

    @InjectMock
    TimerManager timerManager;

    private FlyingSaucerRoot mockRoot;
    private FlyingSaucer mockSaucer;

    @BeforeEach
    void setUp() {
        // Create mock flying saucer root
        mockRoot = mock(FlyingSaucerRoot.class);
        mockSaucer = mock(FlyingSaucer.class);
        
        // Reset mocks
        reset(gameObjectManager, timerManager, mockRoot, mockSaucer);
    }

    // ========== DeployFlyingSaucerCommand Tests ==========

    @Test
    @DisplayName("DeployFlyingSaucerCommand should deploy saucer when root is empty")
    void testDeploySaucerWhenRootEmpty() {
        // Setup: root exists but has no children
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(null);

        // Execute
        deployCommand.execute(1.0f);

        // Verify: saucer was attached to game object manager
        verify(gameObjectManager).attach(any(FlyingSaucer.class));
        
        // Verify: saucer was added to root composite
        verify(mockRoot).add(any(FlyingSaucer.class));
        
        // Verify: movement command was scheduled
        verify(timerManager).add(
            eq(TimeEvent.Name.SPRITE_MOVEMENT),
            any(FlyingSaucerMovement.class),
            eq(0.1f)
        );
        
        // Verify: bomb drop command was scheduled
        verify(timerManager).add(
            eq(TimeEvent.Name.DROP_BOMB),
            any(DropFlyingSaucerBombCommand.class),
            anyFloat()
        );
    }

    @Test
    @DisplayName("DeployFlyingSaucerCommand should not deploy when saucer already exists")
    void testDeploySaucerWhenAlreadyExists() {
        // Setup: root exists and has a child
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);

        // Execute
        deployCommand.execute(1.0f);

        // Verify: no new saucer was attached
        verify(gameObjectManager, never()).attach(any(FlyingSaucer.class));
        verify(mockRoot, never()).add(any(FlyingSaucer.class));
    }

    @Test
    @DisplayName("DeployFlyingSaucerCommand should not deploy when root is null")
    void testDeploySaucerWhenRootNull() {
        // Setup: root doesn't exist
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(null);

        // Execute
        deployCommand.execute(1.0f);

        // Verify: no saucer was attached
        verify(gameObjectManager, never()).attach(any(FlyingSaucer.class));
    }

    @Test
    @DisplayName("DeployFlyingSaucerCommand should handle wash")
    void testDeployCommandWash() {
        // Should not throw exception
        assertDoesNotThrow(() -> deployCommand.wash());
    }

    @Test
    @DisplayName("DeployFlyingSaucerCommand should handle dump")
    void testDeployCommandDump() {
        // Should not throw exception
        assertDoesNotThrow(() -> deployCommand.dump());
    }

    // ========== FlyingSaucerMovement Tests ==========

    @Test
    @DisplayName("FlyingSaucerMovement should move saucer when active")
    void testMoveSaucerWhenActive() {
        // Setup: root has a child and saucer exists
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER))
            .thenReturn(mockSaucer);
        when(mockSaucer.getX()).thenReturn(100.0f);
        when(mockSaucer.getY()).thenReturn(735.0f);
        when(mockSaucer.getSpriteProxy()).thenReturn(mock(SpriteProxy.class));

        // Set movement parameters
        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 0.0f);

        // Execute
        movementCommand.execute(0.1f);

        // Verify: position was updated
        verify(mockSaucer).setX(110.0f);
        verify(mockSaucer).setY(735.0f);
        
        // Verify: command was rescheduled
        verify(timerManager).add(
            eq(TimeEvent.Name.SPRITE_MOVEMENT),
            any(FlyingSaucerMovement.class),
            eq(0.1f)
        );
    }

    @Test
    @DisplayName("FlyingSaucerMovement should not move when root is empty")
    void testMoveSaucerWhenRootEmpty() {
        // Setup: root exists but has no children
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(null);

        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 0.0f);

        // Execute
        movementCommand.execute(0.1f);

        // Verify: no position updates
        verify(mockSaucer, never()).setX(anyFloat());
        verify(mockSaucer, never()).setY(anyFloat());
        
        // Verify: command was not rescheduled
        verify(timerManager, never()).add(any(), any(), anyFloat());
    }

    @Test
    @DisplayName("FlyingSaucerMovement should reverse at right edge")
    void testMoveSaucerReverseAtRightEdge() {
        // Setup: saucer near right edge
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER))
            .thenReturn(mockSaucer);
        when(mockSaucer.getX()).thenReturn(890.0f); // Near right edge (896 - 16 half-width)
        when(mockSaucer.getY()).thenReturn(735.0f);
        when(mockSaucer.getSpriteProxy()).thenReturn(mock(SpriteProxy.class));

        // Set movement parameters (moving right)
        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 0.0f);

        // Execute
        movementCommand.execute(0.1f);

        // Verify: X position was clamped to edge
        verify(mockSaucer).setX(880.0f); // 896 - 16 half-width
    }

    @Test
    @DisplayName("FlyingSaucerMovement should reverse at left edge")
    void testMoveSaucerReverseAtLeftEdge() {
        // Setup: saucer near left edge
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER))
            .thenReturn(mockSaucer);
        when(mockSaucer.getX()).thenReturn(10.0f); // Near left edge
        when(mockSaucer.getY()).thenReturn(735.0f);
        when(mockSaucer.getSpriteProxy()).thenReturn(mock(SpriteProxy.class));

        // Set movement parameters (moving left)
        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, -10.0f, 0.0f);

        // Execute
        movementCommand.execute(0.1f);

        // Verify: X position was clamped to edge
        verify(mockSaucer).setX(16.0f); // 0 + 16 half-width
    }

    @Test
    @DisplayName("FlyingSaucerMovement should handle null saucer")
    void testMoveSaucerWhenSaucerNull() {
        // Setup: root has child but saucer not found
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER))
            .thenReturn(null);

        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 0.0f);

        // Execute - should not throw exception
        assertDoesNotThrow(() -> movementCommand.execute(0.1f));
    }

    @Test
    @DisplayName("FlyingSaucerMovement should update X delta")
    void testUpdateXDelta() {
        movementCommand.updateXDelta(15.0f);
        
        // Verify by executing and checking behavior
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER))
            .thenReturn(mockSaucer);
        when(mockSaucer.getX()).thenReturn(100.0f);
        when(mockSaucer.getY()).thenReturn(735.0f);
        when(mockSaucer.getSpriteProxy()).thenReturn(mock(SpriteProxy.class));

        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 0.0f);
        movementCommand.updateXDelta(15.0f);
        movementCommand.execute(0.1f);

        // Should move by 15 instead of 10
        verify(mockSaucer).setX(115.0f);
    }

    @Test
    @DisplayName("FlyingSaucerMovement should update Y delta")
    void testUpdateYDelta() {
        movementCommand.updateYDelta(5.0f);
        
        // Verify by executing and checking behavior
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockRoot);
        when(mockRoot.getFirstChild()).thenReturn(mockSaucer);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER))
            .thenReturn(mockSaucer);
        when(mockSaucer.getX()).thenReturn(100.0f);
        when(mockSaucer.getY()).thenReturn(735.0f);
        when(mockSaucer.getSpriteProxy()).thenReturn(mock(SpriteProxy.class));

        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 0.0f);
        movementCommand.updateYDelta(5.0f);
        movementCommand.execute(0.1f);

        // Should move Y by 5
        verify(mockSaucer).setY(740.0f);
    }

    @Test
    @DisplayName("FlyingSaucerMovement should wash properly")
    void testMovementWash() {
        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 5.0f);
        movementCommand.wash();
        
        // After wash, should not crash when executing
        assertDoesNotThrow(() -> movementCommand.execute(0.1f));
    }

    @Test
    @DisplayName("FlyingSaucerMovement should dump properly")
    void testMovementDump() {
        movementCommand.set(GameObject.GameObjectName.FLYING_SAUCER, 10.0f, 5.0f);
        assertDoesNotThrow(() -> movementCommand.dump());
    }

    // ========== DropFlyingSaucerBombCommand Tests ==========

    @Test
    @DisplayName("DropFlyingSaucerBombCommand should execute without error")
    void testDropBombExecute() {
        // Currently a stub, should not throw exception
        assertDoesNotThrow(() -> dropBombCommand.execute(1.0f));
    }

    @Test
    @DisplayName("DropFlyingSaucerBombCommand should wash properly")
    void testDropBombWash() {
        assertDoesNotThrow(() -> dropBombCommand.wash());
    }

    @Test
    @DisplayName("DropFlyingSaucerBombCommand should dump properly")
    void testDropBombDump() {
        assertDoesNotThrow(() -> dropBombCommand.dump());
    }

    @Test
    @DisplayName("DropFlyingSaucerBombCommand should handle multiple executions")
    void testDropBombMultipleExecutions() {
        assertDoesNotThrow(() -> {
            dropBombCommand.execute(0.5f);
            dropBombCommand.execute(1.0f);
            dropBombCommand.execute(1.5f);
        });
    }
}