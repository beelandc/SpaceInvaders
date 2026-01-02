package net.beeland.spaceinvaders.animation;

import net.beeland.spaceinvaders.command.Command;
import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.sprite.Sprite;
import net.beeland.spaceinvaders.sprite.SpriteManager;
import net.beeland.spaceinvaders.timer.TimerManager;
import net.beeland.spaceinvaders.timer.TimeEvent;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;

/**
 * Animation - Command pattern for sprite frame animation
 * 
 * Cycles through a sequence of images to create sprite animations.
 * Integrates with TimerManager to schedule frame updates.
 * 
 * Design Pattern: Command
 * - Execute() advances to next frame and reschedules itself
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
public class Animation extends Command {
    
    /**
     * Animation type enumeration
     */
    public enum Name {
        CRAB_ALIEN,
        SQUID_ALIEN,
        JELLYFISH_ALIEN,
        EXPLOSION,
        UNINITIALIZED
    }
    
    // Animation properties
    private Name name;
    private Sprite sprite;
    private List<Image> frames;
    private int currentFrameIndex;
    private float frameDuration;
    private boolean loop;
    private boolean active;
    
    // Manager references (set by AnimationManager)
    private SpriteManager spriteManager;
    private TimerManager timerManager;
    
    /**
     * Default constructor
     */
    public Animation() {
        super();
        this.name = Name.UNINITIALIZED;
        this.sprite = null;
        this.frames = new ArrayList<>();
        this.currentFrameIndex = 0;
        this.frameDuration = 0.5f; // Default 0.5 seconds per frame
        this.loop = true;
        this.active = false;
    }
    
    /**
     * Set animation properties
     * 
     * @param name Animation identifier
     * @param spriteName Name of sprite to animate
     * @param frameDuration Time per frame in seconds
     * @param loop Whether animation should loop
     */
    public void set(Name name, Sprite.Name spriteName, float frameDuration, boolean loop,
                    SpriteManager spriteManager, TimerManager timerManager) {
        this.name = name;
        this.frameDuration = frameDuration;
        this.loop = loop;
        this.currentFrameIndex = 0;
        this.active = false;
        this.spriteManager = spriteManager;
        this.timerManager = timerManager;
        
        // Find the sprite to animate
        if (spriteManager != null) {
            this.sprite = spriteManager.find(spriteName);
            if (this.sprite == null) {
                throw new IllegalArgumentException("Sprite not found: " + spriteName);
            }
        }
    }
    
    /**
     * Add a frame to the animation sequence
     * 
     * @param image Image to add as animation frame
     */
    public void addFrame(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        this.frames.add(image);
    }
    
    /**
     * Start the animation
     * Schedules first frame update via TimerManager
     */
    public void start() {
        if (frames.isEmpty()) {
            throw new IllegalStateException("Cannot start animation with no frames");
        }
        
        this.active = true;
        this.currentFrameIndex = 0;
        
        // Set initial frame
        if (sprite != null && !frames.isEmpty()) {
            sprite.swapImage(frames.get(0));
        }
        
        // Schedule next frame update
        if (timerManager != null) {
            timerManager.add(TimeEvent.Name.ANIMATION, this, frameDuration);
        }
    }
    
    /**
     * Stop the animation
     */
    public void stop() {
        this.active = false;
    }
    
    /**
     * Reset animation to first frame
     */
    public void reset() {
        this.currentFrameIndex = 0;
        if (sprite != null && !frames.isEmpty()) {
            sprite.swapImage(frames.get(0));
        }
    }
    
    /**
     * Execute - Advance to next frame
     * Called by TimerManager when frame duration expires
     * 
     * @param deltaTime Time since last update (unused, frame duration is fixed)
     */
    @Override
    public void execute(float deltaTime) {
        if (!active || frames.isEmpty()) {
            return;
        }
        
        // Advance to next frame
        currentFrameIndex++;
        
        // Handle end of animation
        if (currentFrameIndex >= frames.size()) {
            if (loop) {
                // Loop back to first frame
                currentFrameIndex = 0;
            } else {
                // Stop at last frame
                currentFrameIndex = frames.size() - 1;
                active = false;
                return; // Don't reschedule
            }
        }
        
        // Update sprite with new frame
        if (sprite != null) {
            sprite.swapImage(frames.get(currentFrameIndex));
        }
        
        // Reschedule for next frame
        if (active && timerManager != null) {
            timerManager.add(TimeEvent.Name.ANIMATION, this, frameDuration);
        }
    }
    
    /**
     * Clean up animation resources
     */
    public void destroy() {
        this.sprite = null;
        this.frames.clear();
        this.active = false;
    }
    
    // Getters
    
    public Name getName() {
        return name;
    }
    
    public Sprite getSprite() {
        return sprite;
    }
    
    public int getFrameCount() {
        return frames.size();
    }
    
    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }
    
    public float getFrameDuration() {
        return frameDuration;
    }
    
    public boolean isLoop() {
        return loop;
    }
    
    public boolean isActive() {
        return active;
    }
    
    // Setters
    
    public void setName(Name name) {
        this.name = name;
    }
    
    public void setFrameDuration(float frameDuration) {
        if (frameDuration <= 0) {
            throw new IllegalArgumentException("Frame duration must be positive");
        }
        this.frameDuration = frameDuration;
    }
    
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}