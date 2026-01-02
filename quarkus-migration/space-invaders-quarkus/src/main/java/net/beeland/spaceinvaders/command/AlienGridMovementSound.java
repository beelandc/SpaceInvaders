package net.beeland.spaceinvaders.command;

import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.sound.Sound;
import net.beeland.spaceinvaders.sound.SoundManager;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import jakarta.inject.Inject;

/**
 * AlienGridMovementSound - Command that plays alien movement sounds
 * 
 * This command cycles through 4 different movement sounds (fastInvader1-4)
 * and plays them in sequence. It reschedules itself based on the alien grid
 * speed, creating the characteristic Space Invaders sound that speeds up
 * as aliens are destroyed.
 * 
 * Design Pattern: Command Pattern
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2026-01-02
 */
public class AlienGridMovementSound extends Command {
    
    // Sound names for the 4 movement sounds
    private static final Sound.SoundName[] MOVEMENT_SOUNDS = {
        Sound.SoundName.FAST_INVADER_1,
        Sound.SoundName.FAST_INVADER_2,
        Sound.SoundName.FAST_INVADER_3,
        Sound.SoundName.FAST_INVADER_4
    };
    
    // Current sound index (0-3)
    private int currentSoundIndex;
    
    // Managers (injected via CDI)
    @Inject
    private SoundManager soundManager;
    
    @Inject
    private GameObjectManager gameObjectManager;
    
    @Inject
    private TimerManager timerManager;
    
    // Alien grid speed (time between movements)
    private float alienGridSpeed;
    
    /**
     * Default constructor
     */
    public AlienGridMovementSound() {
        super();
        this.currentSoundIndex = 0;
        this.alienGridSpeed = 1.0f; // Default speed
    }
    
    /**
     * Set the alien grid speed
     * 
     * @param speed Time between alien movements (in seconds)
     */
    public void setAlienGridSpeed(float speed) {
        this.alienGridSpeed = speed;
    }
    
    /**
     * Get the alien grid speed
     * 
     * @return Time between alien movements (in seconds)
     */
    public float getAlienGridSpeed() {
        return this.alienGridSpeed;
    }
    
    /**
     * Execute the command - play current sound and reschedule if aliens exist
     * 
     * @param deltaTime The time delta since the command was scheduled
     */
    @Override
    public void execute(float deltaTime) {
        // Select and play current sound
        Sound.SoundName currentSound = MOVEMENT_SOUNDS[currentSoundIndex];
        
        // Play the sound (use SoundManager if available)
        if (soundManager != null) {
            soundManager.play(currentSound);
        }
        
        // Increment sound index (cycle 0-3)
        currentSoundIndex = (currentSoundIndex + 1) % MOVEMENT_SOUNDS.length;
        
        // Check if alien grid still exists and has aliens
        if (shouldReschedule()) {
            // Reschedule this command with current alien grid speed
            if (timerManager != null) {
                timerManager.add(
                    TimeEvent.Name.ALIEN_GRID_MOVEMENT_SOUND,
                    this,
                    alienGridSpeed
                );
            }
        }
    }
    
    /**
     * Check if the command should reschedule itself
     * Returns true if the alien grid exists and has at least one alien
     * 
     * @return true if should reschedule, false otherwise
     */
    private boolean shouldReschedule() {
        if (gameObjectManager == null) {
            return false;
        }
        
        // Find the alien grid
        GameObject alienGrid = gameObjectManager.find(GameObject.GameObjectName.ALIEN_GRID);
        
        // Check if grid exists and has children (aliens)
        if (alienGrid != null && alienGrid.getFirstChild() != null) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Reset the sound index to start from the beginning
     */
    public void reset() {
        this.currentSoundIndex = 0;
    }
    
    /**
     * Get the current sound index
     * 
     * @return Current sound index (0-3)
     */
    public int getCurrentSoundIndex() {
        return this.currentSoundIndex;
    }
    
    /**
     * Set the current sound index
     * 
     * @param index Sound index (0-3)
     */
    public void setCurrentSoundIndex(int index) {
        if (index >= 0 && index < MOVEMENT_SOUNDS.length) {
            this.currentSoundIndex = index;
        }
    }
    
    @Override
    protected void wash() {
        super.wash();
        this.currentSoundIndex = 0;
        this.alienGridSpeed = 1.0f;
    }
    
    @Override
    public void dump() {
        System.out.println("AlienGridMovementSound:");
        System.out.println("  Current Sound Index: " + currentSoundIndex);
        System.out.println("  Current Sound: " + MOVEMENT_SOUNDS[currentSoundIndex]);
        System.out.println("  Alien Grid Speed: " + alienGridSpeed);
    }
}