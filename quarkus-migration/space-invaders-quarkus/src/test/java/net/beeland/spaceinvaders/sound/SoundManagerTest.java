package net.beeland.spaceinvaders.sound;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SoundManager class
 * 
 * Note: Tests that require OpenAL context are disabled as they need
 * a full graphics environment. These should be run as integration tests.
 * 
 * @author Cecil Beeland
 */
@QuarkusTest
@DisplayName("SoundManager Tests")
class SoundManagerTest {
    
    @Inject
    SoundManager soundManager;
    
    @BeforeEach
    void setUp() {
        // Note: OpenAL initialization happens in SoundManager.initialize()
        // We don't call it here to avoid requiring audio hardware in unit tests
    }
    
    @Test
    @DisplayName("SoundManager is injected via CDI")
    void testCDIInjection() {
        assertNotNull(soundManager);
    }
    
    @Test
    @DisplayName("getMasterVolume returns default volume")
    void testGetMasterVolume() {
        assertEquals(0.2f, soundManager.getMasterVolume(), 0.001f);
    }
    
    @Test
    @DisplayName("setMasterVolume updates volume")
    void testSetMasterVolume() {
        soundManager.setMasterVolume(0.5f);
        assertEquals(0.5f, soundManager.getMasterVolume(), 0.001f);
        
        // Reset to default
        soundManager.setMasterVolume(0.2f);
    }
    
    @Test
    @DisplayName("setMasterVolume clamps to valid range")
    void testSetMasterVolumeClamps() {
        // Test upper bound
        soundManager.setMasterVolume(1.5f);
        assertEquals(1.0f, soundManager.getMasterVolume(), 0.001f);
        
        // Test lower bound
        soundManager.setMasterVolume(-0.5f);
        assertEquals(0.0f, soundManager.getMasterVolume(), 0.001f);
        
        // Reset to default
        soundManager.setMasterVolume(0.2f);
    }
    
    @Test
    @DisplayName("update method executes without error")
    void testUpdate() {
        assertDoesNotThrow(() -> soundManager.update());
    }
    
    @Test
    @DisplayName("stopAll executes without error")
    void testStopAll() {
        assertDoesNotThrow(() -> soundManager.stopAll());
    }
    
    @Test
    @DisplayName("getStats returns manager statistics")
    void testGetStats() {
        String stats = soundManager.getStats();
        assertNotNull(stats);
        assertTrue(stats.contains("SoundManager"));
    }
    
    @Test
    @DisplayName("getActiveHead returns active list head")
    void testGetActiveHead() {
        // Should not throw, even if null
        assertDoesNotThrow(() -> soundManager.getActiveHead());
    }
    
    // The following tests are disabled because they require OpenAL context
    // They should be run as integration tests with proper audio setup
    
    @Test
    @Disabled("Requires OpenAL context - run as integration test")
    @DisplayName("initialize creates OpenAL context")
    void testInitialize() {
        assertDoesNotThrow(() -> soundManager.initialize(5, 3));
    }
    
    @Test
    @Disabled("Requires OpenAL context and audio files")
    @DisplayName("add creates and loads sound")
    void testAdd() {
        Sound sound = soundManager.add(Sound.SoundName.SHOOT, "sounds/shoot.ogg");
        assertNotNull(sound);
        assertEquals(Sound.SoundName.SHOOT, sound.getName());
    }
    
    @Test
    @Disabled("Requires OpenAL context and loaded sounds")
    @DisplayName("find returns sound by name")
    void testFind() {
        soundManager.add(Sound.SoundName.EXPLOSION, "sounds/explosion.ogg");
        Sound found = soundManager.find(Sound.SoundName.EXPLOSION);
        assertNotNull(found);
        assertEquals(Sound.SoundName.EXPLOSION, found.getName());
    }
    
    @Test
    @Disabled("Requires OpenAL context and loaded sounds")
    @DisplayName("play plays sound by name")
    void testPlay() {
        soundManager.add(Sound.SoundName.INVADER_KILLED, "sounds/invaderkilled.ogg");
        assertTrue(soundManager.play(Sound.SoundName.INVADER_KILLED));
    }
    
    @Test
    @Disabled("Requires OpenAL context and loaded sounds")
    @DisplayName("play with volume plays sound at specified volume")
    void testPlayWithVolume() {
        soundManager.add(Sound.SoundName.UFO_LOWPITCH, "sounds/ufo_lowpitch.ogg");
        assertTrue(soundManager.play(Sound.SoundName.UFO_LOWPITCH, 0.5f));
    }
    
    @Test
    @Disabled("Requires OpenAL context")
    @DisplayName("cleanup releases OpenAL resources")
    void testCleanup() {
        assertDoesNotThrow(() -> soundManager.cleanup());
    }
}