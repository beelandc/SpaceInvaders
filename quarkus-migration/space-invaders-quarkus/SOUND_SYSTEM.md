# Sound System Documentation

**Author:** Cecil Beeland  
**Date:** December 23, 2024  
**Version:** 1.0

## Overview

The Space Invaders sound system uses OpenAL for cross-platform audio playback. It implements the Object Pool pattern for efficient sound buffer management and integrates with Quarkus CDI for dependency injection.

## Architecture

### Components

1. **Sound** - Represents an OpenAL sound buffer
2. **SoundManager** - Manages sound instances using Object Pool pattern
3. **Sound Observers** - Respond to game events by playing sounds

### Design Patterns

- **Object Pool**: Reuses Sound objects to minimize memory allocation
- **Singleton**: SoundManager is application-scoped via CDI
- **Observer**: Sound observers respond to collision events

## Class Descriptions

### Sound Class

Located: `net.beeland.spaceinvaders.sound.Sound`

Manages individual sound buffers using OpenAL.

**Key Features:**
- Loads OGG Vorbis audio files using STB Vorbis
- Stores OpenAL buffer ID for playback
- Extends DLink for use in object pools
- Supports wash/dump lifecycle methods

**Sound Names:**
```java
public enum SoundName {
    SHOOT,              // Player missile fire
    INVADER_KILLED,     // Alien destruction
    EXPLOSION,          // General explosion
    UFO_LOWPITCH,       // Flying saucer movement
    FAST_INVADER_1,     // Alien movement sound 1
    FAST_INVADER_2,     // Alien movement sound 2
    FAST_INVADER_3,     // Alien movement sound 3
    FAST_INVADER_4,     // Alien movement sound 4
    UNINITIALIZED       // Default state
}
```

### SoundManager Class

Located: `net.beeland.spaceinvaders.sound.SoundManager`

Centralized manager for all game sounds.

**Key Features:**
- CDI-managed singleton (@ApplicationScoped)
- Object Pool pattern for Sound instances
- OpenAL context initialization and cleanup
- Volume control (master volume)
- Up to 32 simultaneous sound sources

**Public Methods:**

```java
// Initialize the sound system
void initialize(int initialReserve, int growthSize)

// Add a sound to the pool
Sound add(SoundName name, String filePath)

// Find a loaded sound
Sound find(SoundName name)

// Play a sound at default volume
boolean play(SoundName name)

// Play a sound at specific volume
boolean play(SoundName name, float volume)

// Stop all playing sounds
void stopAll()

// Set master volume (0.0 to 1.0)
void setMasterVolume(float volume)

// Get current master volume
float getMasterVolume()

// Update (call once per frame)
void update()

// Cleanup resources
void cleanup()
```

### Sound Observers

Located: `net.beeland.spaceinvaders.collision.observer.*`

#### InvaderKilledSoundObserver
Plays the "invader killed" sound when an alien is destroyed.

#### ExplosionSoundObserver
Plays explosion sound for various collision events (bomb-ship, missile-saucer).

## Usage Examples

### Basic Setup

```java
import net.beeland.spaceinvaders.sound.Sound;
import net.beeland.spaceinvaders.sound.SoundManager;
import jakarta.inject.Inject;

@ApplicationScoped
public class GameAudioSetup {
    
    @Inject
    SoundManager soundManager;
    
    public void initializeAudio() {
        // Initialize with 10 initial sounds, grow by 5 when needed
        soundManager.initialize(10, 5);
        
        // Load all game sounds
        soundManager.add(Sound.SoundName.SHOOT, "sounds/shoot.ogg");
        soundManager.add(Sound.SoundName.INVADER_KILLED, "sounds/invaderkilled.ogg");
        soundManager.add(Sound.SoundName.EXPLOSION, "sounds/explosion.ogg");
        soundManager.add(Sound.SoundName.UFO_LOWPITCH, "sounds/ufo_lowpitch.ogg");
        soundManager.add(Sound.SoundName.FAST_INVADER_1, "sounds/fastinvader1.ogg");
        soundManager.add(Sound.SoundName.FAST_INVADER_2, "sounds/fastinvader2.ogg");
        soundManager.add(Sound.SoundName.FAST_INVADER_3, "sounds/fastinvader3.ogg");
        soundManager.add(Sound.SoundName.FAST_INVADER_4, "sounds/fastinvader4.ogg");
        
        // Set initial volume
        soundManager.setMasterVolume(0.2f);
    }
}
```

### Playing Sounds

```java
// Play at default volume
soundManager.play(Sound.SoundName.SHOOT);

// Play at specific volume (0.0 to 1.0)
soundManager.play(Sound.SoundName.EXPLOSION, 0.5f);

// Play in response to game event
public void onPlayerShoot() {
    soundManager.play(Sound.SoundName.SHOOT);
}
```

### Using Sound Observers

```java
import net.beeland.spaceinvaders.collision.CollisionPair;
import net.beeland.spaceinvaders.collision.observer.InvaderKilledSoundObserver;
import jakarta.inject.Inject;

@ApplicationScoped
public class CollisionSetup {
    
    @Inject
    CollisionPairManager collisionPairManager;
    
    @Inject
    InvaderKilledSoundObserver invaderKilledSound;
    
    public void setupCollisions() {
        // Create collision pair for alien-missile collisions
        CollisionPair alienMissilePair = collisionPairManager.add(
            CollisionPair.CollisionPairName.ALIEN_MISSILE,
            gameObjectManager.find(GameObject.GameObjectName.ALIEN_ROOT),
            gameObjectManager.find(GameObject.GameObjectName.MISSILE_ROOT)
        );
        
        // Attach sound observer
        alienMissilePair.attach(invaderKilledSound);
    }
}
```

### Game Loop Integration

```java
public void gameLoop() {
    while (running) {
        // ... game update logic ...
        
        // Update sound system (cleans up finished sounds)
        soundManager.update();
        
        // ... rendering ...
    }
}
```

### Cleanup

```java
public void shutdown() {
    // Stop all sounds and release OpenAL resources
    soundManager.cleanup();
}
```

## Audio File Requirements

### Format
- **Container:** OGG Vorbis (.ogg)
- **Codec:** Vorbis
- **Channels:** Mono or Stereo
- **Sample Rate:** 44100 Hz recommended

### File Locations
All sound files should be placed in: `src/main/resources/sounds/`

### Converting from WAV to OGG

Using ffmpeg:
```bash
ffmpeg -i input.wav -c:a libvorbis -q:a 4 output.ogg
```

Using Audacity:
1. Open WAV file
2. File → Export → Export as OGG Vorbis
3. Set quality to 4-6
4. Export

## Performance Considerations

### Object Pooling
- Sounds are pooled to avoid garbage collection
- Initial pool size: 10 sounds
- Growth increment: 5 sounds
- Sounds are reused via wash() method

### Concurrent Playback
- Maximum 32 simultaneous sounds
- Oldest sounds are stopped when limit is reached
- Use appropriate volume levels to avoid clipping

### Memory Usage
- Each sound buffer is loaded once
- Multiple playback instances share the same buffer
- Typical sound file: 10-50 KB (OGG compressed)

## Testing

### Unit Tests
Located: `net.beeland.spaceinvaders.sound.SoundTest`
- 12 tests for Sound class
- Tests state management and lifecycle

Located: `net.beeland.spaceinvaders.sound.SoundManagerTest`
- 8 active tests for SoundManager
- 6 disabled tests requiring OpenAL context

### Integration Tests
OpenAL-dependent tests should be run with:
```bash
mvn verify -P integration-tests
```

## Troubleshooting

### No Sound Output
1. Verify OpenAL is initialized: `soundManager.initialize(10, 5)`
2. Check sound files exist in `src/main/resources/sounds/`
3. Verify file format is OGG Vorbis
4. Check master volume: `soundManager.getMasterVolume()`

### Crackling or Distortion
1. Reduce master volume: `soundManager.setMasterVolume(0.2f)`
2. Check for too many simultaneous sounds (>32)
3. Verify sound file quality

### Memory Leaks
1. Ensure `cleanup()` is called on shutdown
2. Check that sounds are properly returned to pool
3. Monitor pool statistics: `soundManager.getStats()`

## Future Enhancements

Potential improvements for future versions:

1. **3D Positional Audio**
   - Add position/velocity to sound sources
   - Implement distance attenuation

2. **Sound Categories**
   - Music vs. SFX volume controls
   - Category-based mixing

3. **Streaming Audio**
   - For longer music tracks
   - Reduce memory footprint

4. **Sound Effects**
   - Pitch variation
   - Reverb/echo effects
   - Doppler effect

5. **Audio Compression**
   - Support for additional formats (MP3, FLAC)
   - Runtime decompression

## References

- [OpenAL Specification](https://www.openal.org/documentation/)
- [LWJGL OpenAL Documentation](https://www.lwjgl.org/guide)
- [STB Vorbis](https://github.com/nothings/stb/blob/master/stb_vorbis.c)
- [OGG Vorbis](https://xiph.org/vorbis/)

## Version History

- **1.0** (2024-12-23): Initial implementation
  - Sound and SoundManager classes
  - OpenAL integration
  - Object Pool pattern
  - CDI integration
  - Sound observers
  - Unit tests