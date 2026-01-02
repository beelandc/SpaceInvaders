package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.command.DeployFlyingSaucerCommand;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.FlyingSaucer;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Random;

/**
 * RemoveFlyingSaucerObserver - Handles flying saucer destruction
 * 
 * When a flying saucer is hit, this observer:
 * 1. Marks the flying saucer for death
 * 2. Disables its collision
 * 3. Schedules removal from the game
 * 4. Schedules deployment of next flying saucer
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2024-12-23
 */
@ApplicationScoped
public class RemoveFlyingSaucerObserver extends CollisionObserver {
    
    @Inject
    GameObjectManager gameObjectManager;
    
    @Inject
    TimerManager timerManager;
    
    @Inject
    DeployFlyingSaucerCommand deployFlyingSaucerCommand;
    
    private FlyingSaucer flyingSaucer;
    private Random random;
    
    /**
     * Default constructor required by CDI
     */
    public RemoveFlyingSaucerObserver() {
        super();
        this.flyingSaucer = null;
        this.random = new Random();
    }
    
    /**
     * Called when a collision is detected
     * Marks flying saucer for death and schedules cleanup
     */
    @Override
    public void notifyCollision() {
        // Get the flying saucer from the collision subject
        if (subject != null && subject.getObjA() instanceof FlyingSaucer) {
            this.flyingSaucer = (FlyingSaucer) subject.getObjA();
            
            // Disable collision by setting collision rect to zero
            if (flyingSaucer.getCollisionObject() != null) {
                flyingSaucer.getCollisionObject().getCollisionRect().set(0, 0, 0, 0);
                flyingSaucer.update();
            }
            
            // Mark for death if not already marked
            if (!flyingSaucer.isMarkedForDeath()) {
                flyingSaucer.markForDeath();
                
                // TODO: Schedule delayed removal when DelayedObjectManager is implemented
                // For now, remove immediately
                removeFlyingSaucer();
            }
            
            // Schedule next flying saucer deployment (30-60 seconds)
            float deployDelay = 30.0f + random.nextFloat() * 31.0f;
            timerManager.add(TimeEvent.Name.DEPLOY_FLYING_SAUCER, deployFlyingSaucerCommand, deployDelay);
        }
    }
    
    /**
     * Remove the flying saucer from the game
     */
    private void removeFlyingSaucer() {
        if (flyingSaucer != null) {
            // TODO: Remove from sprite batches when sprite batch system is integrated
            // flyingSaucer.remove(SpriteBatch.Name.FlyingSaucer, SpriteBatch.Name.CollisionBox);
            
            // Remove from flying saucer root composite
            Composite flyingSaucerRoot = (Composite) gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT);
            if (flyingSaucerRoot != null) {
                flyingSaucerRoot.remove(flyingSaucer);
            }
        }
    }
    
    @Override
    public void dump() {
        System.out.println("RemoveFlyingSaucerObserver:");
        if (flyingSaucer != null) {
            System.out.println("  Flying Saucer: " + flyingSaucer.getName());
        }
    }
}