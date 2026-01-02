package net.beeland.spaceinvaders.sound;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.manager.Manager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

/**
 * SoundManager - Manages Sound objects using Object Pool pattern
 * 
 * Provides centralized management of OpenAL sound buffers.
 * Handles OpenAL context initialization and cleanup.
 * Supports sound playback with volume control.
 * 
 * Design Patterns:
 * - Object Pool (via Manager base class)
 * - Singleton (via CDI @ApplicationScoped)
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
@ApplicationScoped
public class SoundManager extends Manager {
    
    // OpenAL context
    private long device;
    private long context;
    private int[] sources;
    private static final int MAX_SOURCES = 32;
    
    // Volume control
    private float masterVolume = 0.2f;
    
    /**
     * Default constructor required by CDI
     */
    @Inject
    public SoundManager() {
        super();
    }
    
    /**
     * Initialize the SoundManager with OpenAL context
     * 
     * @param initialReserve Initial pool size
     * @param growthSize Pool growth increment
     */
    public void initialize(int initialReserve, int growthSize) {
        super.initialize(initialReserve, growthSize);
        
        // Initialize OpenAL
        initializeOpenAL();
    }
    
    /**
     * Initialize OpenAL context and sources
     */
    private void initializeOpenAL() {
        // Open default audio device
        device = ALC10.alcOpenDevice((CharSequence) null);
        if (device == 0) {
            throw new IllegalStateException("Failed to open OpenAL device");
        }
        
        // Create OpenAL context
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = ALC10.alcCreateContext(device, (int[]) null);
        if (context == 0) {
            throw new IllegalStateException("Failed to create OpenAL context");
        }
        
        // Make context current
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
        
        // Generate audio sources
        sources = new int[MAX_SOURCES];
        for (int i = 0; i < MAX_SOURCES; i++) {
            sources[i] = AL10.alGenSources();
        }
        
        // Set listener properties (default position at origin)
        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }
    
    /**
     * Create a new Sound instance
     *
     * @return New Sound object
     */
    @Override
    protected DLink createNode() {
        return new Sound();
    }
    
    /**
     * Add a sound to the manager
     *
     * @param name Sound identifier
     * @param filePath Path to audio file
     * @return Sound object or null if failed
     */
    public Sound add(Sound.SoundName name, String filePath) {
        Sound sound = (Sound) super.getFromPool();
        
        if (sound != null) {
            if (sound.set(name, filePath)) {
                return sound;
            } else {
                // Failed to load, return to pool
                super.returnToPool(sound);
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Find a sound by name
     *
     * @param name Sound identifier
     * @return Sound object or null if not found
     */
    public Sound find(Sound.SoundName name) {
        DLink current = super.getActiveHead();
        while (current != null) {
            Sound sound = (Sound) current;
            if (sound.getName() == name) {
                return sound;
            }
            current = current.getNext();
        }
        return null;
    }
    
    /**
     * Play a sound
     * 
     * @param name Sound identifier
     * @return true if played successfully
     */
    public boolean play(Sound.SoundName name) {
        Sound sound = find(name);
        if (sound == null) {
            return false;
        }
        
        return playSound(sound);
    }
    
    /**
     * Play a sound with specified volume
     * 
     * @param name Sound identifier
     * @param volume Volume (0.0 to 1.0)
     * @return true if played successfully
     */
    public boolean play(Sound.SoundName name, float volume) {
        Sound sound = find(name);
        if (sound == null) {
            return false;
        }
        
        return playSound(sound, volume);
    }
    
    /**
     * Play a sound using an available source
     * 
     * @param sound Sound to play
     * @return true if played successfully
     */
    private boolean playSound(Sound sound) {
        return playSound(sound, masterVolume);
    }
    
    /**
     * Play a sound with specified volume
     * 
     * @param sound Sound to play
     * @param volume Volume (0.0 to 1.0)
     * @return true if played successfully
     */
    private boolean playSound(Sound sound, float volume) {
        // Find an available source
        int sourceId = findAvailableSource();
        if (sourceId == -1) {
            return false;
        }
        
        // Configure source
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, sound.getBufferId());
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.0f);
        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_FALSE);
        
        // Play the sound
        AL10.alSourcePlay(sourceId);
        
        return true;
    }
    
    /**
     * Find an available audio source
     * 
     * @return Source ID or -1 if none available
     */
    private int findAvailableSource() {
        for (int sourceId : sources) {
            int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
            if (state != AL10.AL_PLAYING) {
                return sourceId;
            }
        }
        return -1;
    }
    
    /**
     * Stop all playing sounds
     */
    public void stopAll() {
        if (sources != null) {
            for (int sourceId : sources) {
                AL10.alSourceStop(sourceId);
            }
        }
    }
    
    /**
     * Set master volume
     * 
     * @param volume Volume (0.0 to 1.0)
     */
    public void setMasterVolume(float volume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    /**
     * Get master volume
     * 
     * @return Current master volume
     */
    public float getMasterVolume() {
        return masterVolume;
    }
    
    /**
     * Update OpenAL (call once per frame)
     * Cleans up finished sources
     */
    public void update() {
        // OpenAL handles most updates automatically
        // This method is here for future extensions
    }
    
    /**
     * Cleanup OpenAL resources
     */
    public void cleanup() {
        // Stop all sounds
        stopAll();
        
        // Delete sources
        if (sources != null) {
            for (int sourceId : sources) {
                AL10.alDeleteSources(sourceId);
            }
        }
        
        // Cleanup all sounds in pool
        super.destroy();
        
        // Destroy OpenAL context
        if (context != 0) {
            ALC10.alcDestroyContext(context);
            context = 0;
        }
        
        // Close device
        if (device != 0) {
            ALC10.alcCloseDevice(device);
            device = 0;
        }
    }
    
}