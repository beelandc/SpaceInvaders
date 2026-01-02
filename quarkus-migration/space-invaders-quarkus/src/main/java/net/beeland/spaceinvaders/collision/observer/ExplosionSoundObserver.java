package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.sound.Sound;
import net.beeland.spaceinvaders.sound.SoundManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * ExplosionSoundObserver - Plays explosion sound on collision
 * 
 * Implements the Observer pattern to respond to various collisions
 * (e.g., bomb-ship, missile-flying saucer) by playing an explosion sound.
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@ApplicationScoped
public class ExplosionSoundObserver extends CollisionObserver {
    
    @Inject
    SoundManager soundManager;
    
    /**
     * Default constructor required by CDI
     */
    public ExplosionSoundObserver() {
        super();
    }
    
    /**
     * Called when a collision is detected
     * Plays the explosion sound
     */
    @Override
    public void notifyCollision() {
        if (soundManager != null) {
            soundManager.play(Sound.SoundName.EXPLOSION);
        }
    }
    
    @Override
    public void dump() {
        System.out.println("ExplosionSoundObserver");
    }
}