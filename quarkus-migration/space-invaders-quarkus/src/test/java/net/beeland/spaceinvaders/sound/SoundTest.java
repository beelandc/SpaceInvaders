package net.beeland.spaceinvaders.sound;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Sound class
 * 
 * Note: These tests focus on the Sound object's state management
 * and do not test actual OpenAL functionality (which requires a context).
 * Integration tests should be used for full audio testing.
 * 
 * @author Cecil Beeland
 */
@DisplayName("Sound Tests")
class SoundTest {
    
    private Sound sound;
    
    @BeforeEach
    void setUp() {
        sound = new Sound();
    }
    
    @Test
    @DisplayName("Constructor initializes with default values")
    void testConstructor() {
        assertEquals(Sound.SoundName.UNINITIALIZED, sound.getName());
        assertEquals(-1, sound.getBufferId());
        assertNull(sound.getFilePath());
        assertEquals(0, sound.getChannels());
        assertEquals(0, sound.getSampleRate());
    }
    
    @Test
    @DisplayName("getName returns correct sound name")
    void testGetName() {
        assertEquals(Sound.SoundName.UNINITIALIZED, sound.getName());
    }
    
    @Test
    @DisplayName("getBufferId returns initial buffer ID")
    void testGetBufferId() {
        assertEquals(-1, sound.getBufferId());
    }
    
    @Test
    @DisplayName("getFilePath returns null initially")
    void testGetFilePath() {
        assertNull(sound.getFilePath());
    }
    
    @Test
    @DisplayName("getChannels returns 0 initially")
    void testGetChannels() {
        assertEquals(0, sound.getChannels());
    }
    
    @Test
    @DisplayName("getSampleRate returns 0 initially")
    void testGetSampleRate() {
        assertEquals(0, sound.getSampleRate());
    }
    
    @Test
    @DisplayName("wash resets sound name to UNINITIALIZED")
    void testWash() {
        // Note: We can't actually load a sound without OpenAL context
        // So we just test that wash resets the name
        sound.wash();
        assertEquals(Sound.SoundName.UNINITIALIZED, sound.getName());
    }
    
    @Test
    @DisplayName("dump resets all properties")
    void testDump() {
        sound.dump();
        assertEquals(Sound.SoundName.UNINITIALIZED, sound.getName());
        assertEquals(-1, sound.getBufferId());
        assertNull(sound.getFilePath());
        assertEquals(0, sound.getChannels());
        assertEquals(0, sound.getSampleRate());
    }
    
    @Test
    @DisplayName("toString returns formatted string")
    void testToString() {
        String result = sound.toString();
        assertNotNull(result);
        assertTrue(result.contains("Sound{"));
        assertTrue(result.contains("name="));
        assertTrue(result.contains("bufferId="));
    }
    
    @Test
    @DisplayName("All SoundName enum values are defined")
    void testSoundNameEnum() {
        Sound.SoundName[] names = Sound.SoundName.values();
        assertTrue(names.length >= 9);
        
        // Verify specific sound names exist
        assertNotNull(Sound.SoundName.valueOf("SHOOT"));
        assertNotNull(Sound.SoundName.valueOf("INVADER_KILLED"));
        assertNotNull(Sound.SoundName.valueOf("EXPLOSION"));
        assertNotNull(Sound.SoundName.valueOf("UFO_LOWPITCH"));
        assertNotNull(Sound.SoundName.valueOf("FAST_INVADER_1"));
        assertNotNull(Sound.SoundName.valueOf("FAST_INVADER_2"));
        assertNotNull(Sound.SoundName.valueOf("FAST_INVADER_3"));
        assertNotNull(Sound.SoundName.valueOf("FAST_INVADER_4"));
        assertNotNull(Sound.SoundName.valueOf("UNINITIALIZED"));
    }
    
    @Test
    @DisplayName("Sound extends DLink")
    void testExtendsLink() {
        // Verify Sound can be used in linked lists
        // Initially, next and prev are null
        assertNull(sound.getNext());
        assertNull(sound.getPrev());
        
        // Verify the methods exist and can be called
        assertDoesNotThrow(() -> sound.getNext());
        assertDoesNotThrow(() -> sound.getPrev());
    }
    
    @Test
    @DisplayName("Multiple Sound instances are independent")
    void testMultipleInstances() {
        Sound sound1 = new Sound();
        Sound sound2 = new Sound();
        
        assertNotSame(sound1, sound2);
        assertEquals(sound1.getName(), sound2.getName());
    }
}