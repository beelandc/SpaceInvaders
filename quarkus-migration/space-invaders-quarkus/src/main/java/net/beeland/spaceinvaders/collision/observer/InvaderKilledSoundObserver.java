package net.beeland.spaceinvaders.collision.observer;

import net.beeland.spaceinvaders.collision.CollisionObserver;
import net.beeland.spaceinvaders.sound.Sound;
import net.beeland.spaceinvaders.sound.SoundManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * InvaderKilledSoundObserver - Plays sound when an alien is killed
 * 
 * Implements the Observer pattern to respond to alien-missile collisions
 * by playing the "invader killed" sound effect.
 * 
 * Design Pattern: Observer
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@ApplicationScoped
public class InvaderKilledSoundObserver extends CollisionObserver {
    
    @Inject
    SoundManager soundManager;
    
    /**
     * Default constructor required by CDI
     */
    public InvaderKilledSoundObserver() {
        super();
    }
    
    /**
     * Called when a collision is detected
     * Plays the invader killed sound
     */
    @Override
    public void notifyCollision() {
        if (soundManager != null) {
            soundManager.play(Sound.SoundName.INVADER_KILLED);
        }
    }
    
    @Override
    public void dump() {
        System.out.println("InvaderKilledSoundObserver");
    }
}