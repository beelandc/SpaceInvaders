package net.beeland.spaceinvaders.collision.observer;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import net.beeland.spaceinvaders.collision.CollisionObject;
import net.beeland.spaceinvaders.collision.CollisionRect;
import net.beeland.spaceinvaders.collision.CollisionSubject;
import net.beeland.spaceinvaders.command.DeployFlyingSaucerCommand;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.FlyingSaucer;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.sound.Sound;
import net.beeland.spaceinvaders.sound.SoundManager;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for Collision Observer classes
 * Tests the Observer pattern implementation for collision responses
 */
@QuarkusTest
@DisplayName("Collision Observer Tests")
class CollisionObserverTest {

    @Inject
    ExplosionSoundObserver explosionObserver;
    
    @Inject
    InvaderKilledSoundObserver invaderKilledObserver;
    
    @Inject
    RemoveFlyingSaucerObserver removeFlyingSaucerObserver;
    
    @InjectMock
    SoundManager soundManager;
    
    @InjectMock
    GameObjectManager gameObjectManager;
    
    @InjectMock
    TimerManager timerManager;
    
    @InjectMock
    DeployFlyingSaucerCommand deployFlyingSaucerCommand;
    
    // ExplosionSoundObserver Tests
    
    @Test
    @DisplayName("ExplosionSoundObserver should play explosion sound on collision notification")
    void testExplosionObserverNotifyCollision() {
        // When
        explosionObserver.notifyCollision();
        
        // Then
        verify(soundManager).play(Sound.SoundName.EXPLOSION);
    }
    
    @Test
    @DisplayName("ExplosionSoundObserver should handle null sound manager gracefully")
    void testExplosionObserverWithNullSoundManager() {
        // Given
        ExplosionSoundObserver observer = new ExplosionSoundObserver();
        
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> observer.notifyCollision());
    }
    
    @Test
    @DisplayName("ExplosionSoundObserver should output correct dump information")
    void testExplosionObserverDump() {
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> explosionObserver.dump());
    }
    
    @Test
    @DisplayName("ExplosionSoundObserver should create with default constructor")
    void testExplosionObserverConstructor() {
        // When
        ExplosionSoundObserver observer = new ExplosionSoundObserver();
        
        // Then
        assertNotNull(observer);
    }
    
    // InvaderKilledSoundObserver Tests
    
    @Test
    @DisplayName("InvaderKilledSoundObserver should play invader killed sound on collision notification")
    void testInvaderKilledObserverNotifyCollision() {
        // When
        invaderKilledObserver.notifyCollision();
        
        // Then
        verify(soundManager).play(Sound.SoundName.INVADER_KILLED);
    }
    
    @Test
    @DisplayName("InvaderKilledSoundObserver should handle null sound manager gracefully")
    void testInvaderKilledObserverWithNullSoundManager() {
        // Given
        InvaderKilledSoundObserver observer = new InvaderKilledSoundObserver();
        
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> observer.notifyCollision());
    }
    
    @Test
    @DisplayName("InvaderKilledSoundObserver should output correct dump information")
    void testInvaderKilledObserverDump() {
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> invaderKilledObserver.dump());
    }
    
    @Test
    @DisplayName("InvaderKilledSoundObserver should create with default constructor")
    void testInvaderKilledObserverConstructor() {
        // When
        InvaderKilledSoundObserver observer = new InvaderKilledSoundObserver();
        
        // Then
        assertNotNull(observer);
    }
    
    // RemoveFlyingSaucerObserver Tests
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should mark flying saucer for death on collision")
    void testRemoveFlyingSaucerObserverMarkForDeath() {
        // Given
        FlyingSaucer mockFlyingSaucer = mock(FlyingSaucer.class);
        CollisionSubject mockSubject = mock(CollisionSubject.class);
        Composite mockFlyingSaucerRoot = mock(Composite.class);
        
        // Setup collision object
        CollisionObject collisionObject = mock(CollisionObject.class);
        CollisionRect collisionRect = mock(CollisionRect.class);
        when(mockFlyingSaucer.getCollisionObject()).thenReturn(collisionObject);
        when(collisionObject.getCollisionRect()).thenReturn(collisionRect);
        
        // Setup subject
        when(mockSubject.getObjA()).thenReturn(mockFlyingSaucer);
        when(mockFlyingSaucer.isMarkedForDeath()).thenReturn(false);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockFlyingSaucerRoot);
        
        removeFlyingSaucerObserver.setSubject(mockSubject);
        
        // When
        removeFlyingSaucerObserver.notifyCollision();
        
        // Then
        verify(mockFlyingSaucer).markForDeath();
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should disable collision on flying saucer")
    void testRemoveFlyingSaucerObserverDisablesCollision() {
        // Given
        FlyingSaucer mockFlyingSaucer = mock(FlyingSaucer.class);
        CollisionSubject mockSubject = mock(CollisionSubject.class);
        Composite mockFlyingSaucerRoot = mock(Composite.class);
        
        CollisionObject collisionObject = mock(CollisionObject.class);
        CollisionRect collisionRect = mock(CollisionRect.class);
        when(mockFlyingSaucer.getCollisionObject()).thenReturn(collisionObject);
        when(collisionObject.getCollisionRect()).thenReturn(collisionRect);
        
        when(mockSubject.getObjA()).thenReturn(mockFlyingSaucer);
        when(mockFlyingSaucer.isMarkedForDeath()).thenReturn(false);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockFlyingSaucerRoot);
        
        removeFlyingSaucerObserver.setSubject(mockSubject);
        
        // When
        removeFlyingSaucerObserver.notifyCollision();
        
        // Then
        verify(collisionRect).set(0, 0, 0, 0);
        verify(mockFlyingSaucer).update();
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should remove flying saucer from composite")
    void testRemoveFlyingSaucerObserverRemovesFromComposite() {
        // Given
        FlyingSaucer mockFlyingSaucer = mock(FlyingSaucer.class);
        CollisionSubject mockSubject = mock(CollisionSubject.class);
        Composite mockFlyingSaucerRoot = mock(Composite.class);
        
        CollisionObject collisionObject = mock(CollisionObject.class);
        CollisionRect collisionRect = mock(CollisionRect.class);
        when(mockFlyingSaucer.getCollisionObject()).thenReturn(collisionObject);
        when(collisionObject.getCollisionRect()).thenReturn(collisionRect);
        
        when(mockSubject.getObjA()).thenReturn(mockFlyingSaucer);
        when(mockFlyingSaucer.isMarkedForDeath()).thenReturn(false);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockFlyingSaucerRoot);
        
        removeFlyingSaucerObserver.setSubject(mockSubject);
        
        // When
        removeFlyingSaucerObserver.notifyCollision();
        
        // Then
        verify(mockFlyingSaucerRoot).remove(mockFlyingSaucer);
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should schedule next flying saucer deployment")
    void testRemoveFlyingSaucerObserverSchedulesNextDeployment() {
        // Given
        FlyingSaucer mockFlyingSaucer = mock(FlyingSaucer.class);
        CollisionSubject mockSubject = mock(CollisionSubject.class);
        Composite mockFlyingSaucerRoot = mock(Composite.class);
        
        CollisionObject collisionObject = mock(CollisionObject.class);
        CollisionRect collisionRect = mock(CollisionRect.class);
        when(mockFlyingSaucer.getCollisionObject()).thenReturn(collisionObject);
        when(collisionObject.getCollisionRect()).thenReturn(collisionRect);
        
        when(mockSubject.getObjA()).thenReturn(mockFlyingSaucer);
        when(mockFlyingSaucer.isMarkedForDeath()).thenReturn(false);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(mockFlyingSaucerRoot);
        
        removeFlyingSaucerObserver.setSubject(mockSubject);
        
        // When
        removeFlyingSaucerObserver.notifyCollision();
        
        // Then
        ArgumentCaptor<Float> delayCaptor = ArgumentCaptor.forClass(Float.class);
        verify(timerManager).add(
            eq(TimeEvent.Name.DEPLOY_FLYING_SAUCER),
            any(DeployFlyingSaucerCommand.class),
            delayCaptor.capture()
        );
        
        // Verify delay is between 30 and 61 seconds
        float delay = delayCaptor.getValue();
        assertTrue(delay >= 30.0f && delay <= 61.0f,
            "Delay should be between 30 and 61 seconds, was: " + delay);
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should not mark for death if already marked")
    void testRemoveFlyingSaucerObserverAlreadyMarked() {
        // Given
        FlyingSaucer mockFlyingSaucer = mock(FlyingSaucer.class);
        CollisionSubject mockSubject = mock(CollisionSubject.class);
        Composite mockFlyingSaucerRoot = mock(Composite.class);
        
        CollisionObject collisionObject = mock(CollisionObject.class);
        CollisionRect collisionRect = mock(CollisionRect.class);
        when(mockFlyingSaucer.getCollisionObject()).thenReturn(collisionObject);
        when(collisionObject.getCollisionRect()).thenReturn(collisionRect);
        
        when(mockSubject.getObjA()).thenReturn(mockFlyingSaucer);
        when(mockFlyingSaucer.isMarkedForDeath()).thenReturn(true);
        
        removeFlyingSaucerObserver.setSubject(mockSubject);
        
        // When
        removeFlyingSaucerObserver.notifyCollision();
        
        // Then
        verify(mockFlyingSaucer, never()).markForDeath();
        verify(mockFlyingSaucerRoot, never()).remove(any());
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should handle null subject gracefully")
    void testRemoveFlyingSaucerObserverWithNullSubject() {
        // Given
        RemoveFlyingSaucerObserver observer = new RemoveFlyingSaucerObserver();
        
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> observer.notifyCollision());
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should handle null flying saucer root gracefully")
    void testRemoveFlyingSaucerObserverWithNullRoot() {
        // Given
        FlyingSaucer mockFlyingSaucer = mock(FlyingSaucer.class);
        CollisionSubject mockSubject = mock(CollisionSubject.class);
        
        CollisionObject collisionObject = mock(CollisionObject.class);
        CollisionRect collisionRect = mock(CollisionRect.class);
        when(mockFlyingSaucer.getCollisionObject()).thenReturn(collisionObject);
        when(collisionObject.getCollisionRect()).thenReturn(collisionRect);
        
        when(mockSubject.getObjA()).thenReturn(mockFlyingSaucer);
        when(mockFlyingSaucer.isMarkedForDeath()).thenReturn(false);
        when(gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT))
            .thenReturn(null);
        
        removeFlyingSaucerObserver.setSubject(mockSubject);
        
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> removeFlyingSaucerObserver.notifyCollision());
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should output correct dump information")
    void testRemoveFlyingSaucerObserverDump() {
        // When/Then - should not throw exception
        assertDoesNotThrow(() -> removeFlyingSaucerObserver.dump());
    }
    
    @Test
    @DisplayName("RemoveFlyingSaucerObserver should create with default constructor")
    void testRemoveFlyingSaucerObserverConstructor() {
        // When
        RemoveFlyingSaucerObserver observer = new RemoveFlyingSaucerObserver();
        
        // Then
        assertNotNull(observer);
    }
}