package net.beeland.spaceinvaders.sound;

import net.beeland.spaceinvaders.manager.DLink;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Sound class - Represents an OpenAL sound buffer
 * 
 * Manages loading and storing audio data using OpenAL.
 * Supports WAV and OGG file formats via STB Vorbis.
 * Uses Object Pool pattern via Manager base class.
 * 
 * Design Pattern: Object Pool (via Manager)
 * 
 * @author Cecil Beeland
 * @version 1.0
 * @since 2025-12-23
 */
public class Sound extends DLink {
    
    // Sound identification
    private SoundName name;
    
    // OpenAL buffer ID
    private int bufferId;
    
    // File path for loading
    private String filePath;
    
    // Sound properties
    private int channels;
    private int sampleRate;
    
    /**
     * Sound name enumeration
     */
    public enum SoundName {
        SHOOT,
        INVADER_KILLED,
        EXPLOSION,
        UFO_LOWPITCH,
        FAST_INVADER_1,
        FAST_INVADER_2,
        FAST_INVADER_3,
        FAST_INVADER_4,
        UNINITIALIZED
    }
    
    /**
     * Default constructor
     */
    public Sound() {
        this.name = SoundName.UNINITIALIZED;
        this.bufferId = -1;
        this.filePath = null;
        this.channels = 0;
        this.sampleRate = 0;
    }
    
    /**
     * Set sound properties and load audio data
     * 
     * @param name Sound identifier
     * @param filePath Path to audio file (WAV or OGG)
     * @return true if successful, false otherwise
     */
    public boolean set(SoundName name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        
        // Load the audio file
        return loadSound();
    }
    
    /**
     * Load sound from file using STB Vorbis
     * 
     * @return true if successful, false otherwise
     */
    private boolean loadSound() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Allocate space for channels and sample rate
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);
            
            // Decode the OGG file
            ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(
                filePath, channelsBuffer, sampleRateBuffer);
            
            if (rawAudioBuffer == null) {
                System.err.println("Failed to load sound: " + filePath);
                return false;
            }
            
            // Get audio properties
            this.channels = channelsBuffer.get(0);
            this.sampleRate = sampleRateBuffer.get(0);
            
            // Determine OpenAL format
            int format = (channels == 1) ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
            
            // Generate OpenAL buffer
            this.bufferId = AL10.alGenBuffers();
            
            // Upload audio data to OpenAL
            AL10.alBufferData(bufferId, format, rawAudioBuffer, sampleRate);
            
            // Free the decoded audio buffer
            MemoryUtil.memFree(rawAudioBuffer);
            
            // Check for OpenAL errors
            int error = AL10.alGetError();
            if (error != AL10.AL_NO_ERROR) {
                System.err.println("OpenAL error loading sound: " + error);
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Exception loading sound: " + filePath);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the sound name
     * 
     * @return Sound identifier
     */
    public SoundName getName() {
        return name;
    }
    
    /**
     * Get the OpenAL buffer ID
     * 
     * @return Buffer ID
     */
    public int getBufferId() {
        return bufferId;
    }
    
    /**
     * Get the file path
     * 
     * @return File path
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * Get number of channels
     * 
     * @return 1 for mono, 2 for stereo
     */
    public int getChannels() {
        return channels;
    }
    
    /**
     * Get sample rate
     * 
     * @return Sample rate in Hz
     */
    public int getSampleRate() {
        return sampleRate;
    }
    
    /**
     * Wash - Reset sound to default state
     * Called when returning to pool
     */
    public void wash() {
        // Don't delete the OpenAL buffer - it's reusable
        // Just reset the name
        this.name = SoundName.UNINITIALIZED;
    }
    
    /**
     * Dump - Clean up OpenAL resources
     * Called when permanently removing from pool
     */
    public void dump() {
        if (bufferId != -1) {
            AL10.alDeleteBuffers(bufferId);
            bufferId = -1;
        }
        this.name = SoundName.UNINITIALIZED;
        this.filePath = null;
        this.channels = 0;
        this.sampleRate = 0;
    }
    
    @Override
    public String toString() {
        return "Sound{" +
                "name=" + name +
                ", bufferId=" + bufferId +
                ", filePath='" + filePath + '\'' +
                ", channels=" + channels +
                ", sampleRate=" + sampleRate +
                '}';
    }
}