# Sound Resources

This directory contains audio files for the Space Invaders game.

## Required Sound Files

The following sound files should be placed in this directory in OGG Vorbis format:

### Game Sounds
- **shoot.ogg** - Player missile firing sound
- **invaderkilled.ogg** - Alien destruction sound
- **explosion.ogg** - General explosion sound (bomb hits, flying saucer destruction)
- **ufo_lowpitch.ogg** - Flying saucer movement sound (looping)

### Alien Movement Sounds
- **fastinvader1.ogg** - Alien grid movement sound 1
- **fastinvader2.ogg** - Alien grid movement sound 2
- **fastinvader3.ogg** - Alien grid movement sound 3
- **fastinvader4.ogg** - Alien grid movement sound 4

## File Format

All sound files must be in **OGG Vorbis** format (.ogg extension).

The Sound class uses STB Vorbis to decode OGG files at runtime.

## Sound Mapping

The sounds are mapped to `Sound.SoundName` enum values:

| Enum Value | File Name | Usage |
|------------|-----------|-------|
| SHOOT | shoot.ogg | Player fires missile |
| INVADER_KILLED | invaderkilled.ogg | Alien is destroyed |
| EXPLOSION | explosion.ogg | Bomb/saucer explosion |
| UFO_LOWPITCH | ufo_lowpitch.ogg | Flying saucer movement |
| FAST_INVADER_1 | fastinvader1.ogg | Alien grid movement |
| FAST_INVADER_2 | fastinvader2.ogg | Alien grid movement |
| FAST_INVADER_3 | fastinvader3.ogg | Alien grid movement |
| FAST_INVADER_4 | fastinvader4.ogg | Alien grid movement |

## Loading Sounds

Sounds are loaded through the SoundManager:

```java
@Inject
SoundManager soundManager;

// Initialize the sound system
soundManager.initialize(10, 5);

// Load sounds
soundManager.add(Sound.SoundName.SHOOT, "sounds/shoot.ogg");
soundManager.add(Sound.SoundName.INVADER_KILLED, "sounds/invaderkilled.ogg");
// ... load other sounds
```

## Playing Sounds

```java
// Play at default volume
soundManager.play(Sound.SoundName.SHOOT);

// Play at specific volume (0.0 to 1.0)
soundManager.play(Sound.SoundName.EXPLOSION, 0.5f);
```

## Volume Control

```java
// Set master volume (0.0 to 1.0)
soundManager.setMasterVolume(0.3f);

// Get current master volume
float volume = soundManager.getMasterVolume();
```

## Notes

- The original C# game used WAV files with IrrKlang
- This Java version uses OGG Vorbis with OpenAL for better compression and cross-platform support
- You may need to convert the original WAV files to OGG format
- Recommended tool for conversion: Audacity or ffmpeg

### Converting WAV to OGG with ffmpeg:
```bash
ffmpeg -i input.wav -c:a libvorbis -q:a 4 output.ogg
```

## Copyright

Ensure you have the rights to use any sound files placed in this directory.