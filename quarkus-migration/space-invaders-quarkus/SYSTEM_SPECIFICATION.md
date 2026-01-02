# Space Invaders Quarkus - System Specification

**Version:** 1.0  
**Date:** December 22, 2024  
**Status:** Living Document

---

## 1. Executive Summary

### 1.1 Project Overview
Space Invaders Quarkus is a Java-based reimplementation of the classic Space Invaders arcade game, migrated from C# to Java using the Quarkus framework and LWJGL for graphics rendering. The project demonstrates modern software architecture patterns while maintaining the classic gameplay experience.

### 1.2 Goals
- Migrate C# Space Invaders to Java/Quarkus
- Implement 10+ design patterns
- Achieve 60 FPS performance
- Maintain 80%+ test coverage
- Create maintainable, well-documented code

### 1.3 Technology Stack
- **Framework:** Quarkus 3.30.4
- **Language:** Java 21 (LTS)
- **Graphics:** LWJGL 3.3.3 (OpenGL 3.3 Core)
- **Math:** JOML 1.10.5
- **Build:** Maven
- **Testing:** JUnit 5, Mockito

---

## 2. Functional Requirements

### 2.1 Game Mechanics

#### 2.1.1 Player Controls
- **FR-001:** Player shall control a cannon at the bottom of the screen
- **FR-002:** Cannon shall move left/right using arrow keys or A/D keys
- **FR-003:** Cannon shall fire missiles using spacebar
- **FR-004:** Only one missile can be active at a time
- **FR-005:** Cannon has 3 lives per game

#### 2.1.2 Alien Behavior
- **FR-006:** 55 aliens arranged in 11 columns × 5 rows
- **FR-007:** Three alien types: Squid (top), Crab (middle), Jellyfish (bottom)
- **FR-008:** Aliens move as a synchronized grid
- **FR-009:** Grid moves horizontally, drops down at edges, reverses direction
- **FR-010:** Movement speed increases as aliens are destroyed
- **FR-011:** Aliens drop bombs randomly
- **FR-012:** Three bomb types with different fall patterns (straight, zigzag, dagger)

#### 2.1.3 Shields
- **FR-013:** Four shields positioned between player and aliens
- **FR-014:** Shields composed of destructible bricks
- **FR-015:** Shields degrade when hit by missiles or bombs
- **FR-016:** Shield bricks have multiple damage states

#### 2.1.4 UFO (Flying Saucer)
- **FR-017:** UFO appears periodically at top of screen
- **FR-018:** UFO moves horizontally across screen
- **FR-019:** UFO awards bonus points when destroyed
- **FR-020:** UFO has unique sound effect

#### 2.1.5 Scoring
- **FR-021:** Squid alien: 30 points
- **FR-022:** Crab alien: 20 points
- **FR-023:** Jellyfish alien: 10 points
- **FR-024:** UFO: 50-300 points (variable)
- **FR-025:** High score persists between games

#### 2.1.6 Game States
- **FR-026:** Attract mode displays title and high score
- **FR-027:** Player 1 mode for single player
- **FR-028:** Player 2 mode for two-player alternating
- **FR-029:** Game Over state displays final score
- **FR-030:** Level progression increases difficulty

### 2.2 Audio Requirements
- **FR-031:** Background music during gameplay
- **FR-032:** Sound effects for: missile fire, alien death, explosion, UFO
- **FR-033:** Alien movement sound increases in tempo
- **FR-034:** Audio volume control

### 2.3 Visual Requirements
- **FR-035:** 800×600 pixel game window
- **FR-036:** 60 FPS target frame rate
- **FR-037:** Sprite-based rendering
- **FR-038:** Smooth animations for all game objects
- **FR-039:** Visual feedback for collisions
- **FR-040:** Score and lives display

---

## 3. Non-Functional Requirements

### 3.1 Performance
- **NFR-001:** Maintain 60 FPS during normal gameplay
- **NFR-002:** Frame time variance < 2ms
- **NFR-003:** Memory usage < 256MB
- **NFR-004:** Startup time < 3 seconds
- **NFR-005:** No garbage collection pauses > 5ms

### 3.2 Quality
- **NFR-006:** Code coverage ≥ 80%
- **NFR-007:** Zero critical bugs in production
- **NFR-008:** All public APIs documented
- **NFR-009:** Cyclomatic complexity < 10 per method
- **NFR-010:** Technical debt ratio < 5%

### 3.3 Maintainability
- **NFR-011:** Follow Java coding conventions
- **NFR-012:** Maximum method length: 50 lines
- **NFR-013:** Maximum class length: 500 lines
- **NFR-014:** Clear separation of concerns
- **NFR-015:** Comprehensive inline documentation

### 3.4 Portability
- **NFR-016:** Run on Windows, macOS, Linux
- **NFR-017:** Support OpenGL 3.3+
- **NFR-018:** No platform-specific code in game logic
- **NFR-019:** Configurable key bindings

### 3.5 Scalability
- **NFR-020:** Support for additional game modes
- **NFR-021:** Extensible enemy types
- **NFR-022:** Pluggable rendering backends
- **NFR-023:** Modular audio system

---

## 4. System Architecture

### 4.1 Architectural Patterns

#### 4.1.1 Object Pool Pattern
**Purpose:** Efficient memory management  
**Implementation:** Manager base class  
**Components:**
- Active list (in-use objects)
- Reserved list (available objects)
- Automatic growth mechanism

#### 4.1.2 Singleton Pattern (via CDI)
**Purpose:** Single instance managers  
**Implementation:** @ApplicationScoped beans  
**Components:**
- TextureManager
- ImageManager
- SpriteManager
- GameObjectManager
- CollisionPairManager

#### 4.1.3 Factory Pattern
**Purpose:** Object creation abstraction  
**Implementation:** AlienFactory, ShieldFactory  
**Components:**
- Create methods for different types
- Centralized object configuration

#### 4.1.4 Proxy Pattern
**Purpose:** Lightweight sprite instances  
**Implementation:** SpriteProxy  
**Components:**
- Shared sprite data
- Unique position/state per proxy

#### 4.1.5 Composite Pattern
**Purpose:** Hierarchical object structures  
**Implementation:** AlienGrid, ShieldGroup  
**Components:**
- Component interface
- Leaf nodes (individual objects)
- Composite nodes (groups)

#### 4.1.6 Iterator Pattern
**Purpose:** Collection traversal  
**Implementation:** Non-recursive tree traversal  
**Components:**
- ForwardIterator
- ReverseIterator
- Custom iteration logic

#### 4.1.7 Strategy Pattern
**Purpose:** Interchangeable algorithms  
**Implementation:** Bomb fall strategies  
**Components:**
- FallStrategy interface
- FallStraight, FallZigZag, FallDagger

#### 4.1.8 State Pattern
**Purpose:** State-based behavior  
**Implementation:** Game states, player states  
**Components:**
- State interface
- Concrete state classes
- State transitions

#### 4.1.9 Observer Pattern
**Purpose:** Event notification  
**Implementation:** Collision system  
**Components:**
- Subject (CollisionSubject)
- Observers (collision handlers)
- Notification mechanism

#### 4.1.10 Command Pattern
**Purpose:** Action encapsulation  
**Implementation:** Timer events, input commands  
**Components:**
- Command interface
- Concrete commands
- Command queue

### 4.2 Layer Architecture

```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│  (Game Loop, Input, Rendering)      │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Game Logic Layer                │
│  (GameObjects, Collision, State)    │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Resource Management Layer       │
│  (Managers, Sprites, Textures)      │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Infrastructure Layer            │
│  (LWJGL, OpenGL, CDI, Quarkus)      │
└─────────────────────────────────────┘
```

### 4.3 Component Diagram

```
Game
  ├── GameStateManager
  │     ├── AttractState
  │     ├── Player1State
  │     ├── Player2State
  │     └── EndState
  │
  ├── TextureManager
  │     └── Texture[]
  │
  ├── ImageManager
  │     └── Image[]
  │
  ├── SpriteManager
  │     ├── Sprite[]
  │     └── SpriteProxy[]
  │
  ├── GameObjectManager
  │     ├── AlienGrid
  │     │     └── Alien[]
  │     ├── CoreCannon
  │     ├── Missile
  │     ├── Bomb[]
  │     ├── ShieldGroup[]
  │     └── UFO
  │
  ├── CollisionPairManager
  │     └── CollisionPair[]
  │           └── CollisionObserver[]
  │
  ├── TimerManager
  │     └── TimeEvent[]
  │           └── Command[]
  │
  └── SoundEngineManager
        └── Sound[]
```

---

## 5. Data Models

### 5.1 Core Classes

#### 5.1.1 DLink
```java
abstract class DLink {
    - DLink next
    - DLink prev
    + getNext(): DLink
    + getPrev(): DLink
    + insertAfter(DLink): void
    + insertBefore(DLink): void
    + remove(): void
    + abstract wash(): void
}
```

#### 5.1.2 Manager
```java
abstract class Manager {
    - DLink activeHead
    - DLink reservedHead
    - int totalActive
    - int totalReserved
    - int peakActive
    + initialize(int, int): void
    # getFromPool(): DLink
    # returnToPool(DLink): void
    + abstract createNode(): DLink
}
```

#### 5.1.3 GameObject
```java
abstract class GameObject extends DLink {
    - float x, y
    - float width, height
    - Sprite sprite
    - CollisionRect collisionRect
    + update(float): void
    + render(): void
    + abstract accept(CollisionVisitor): void
}
```

### 5.2 Game State

```java
enum GameState {
    ATTRACT,
    PLAYER_1,
    PLAYER_2,
    GAME_OVER
}

class GameContext {
    - GameState currentState
    - int player1Score
    - int player2Score
    - int highScore
    - int currentPlayer
    - int level
}
```

---

## 6. Interface Specifications

### 6.1 Manager Interface
All managers implement:
- `initialize(reserveSize, growSize)`
- `add(...): T`
- `find(name): T`
- `remove(T): void`
- `getStats(): String`
- `destroy(): void`

### 6.2 GameObject Interface
All game objects implement:
- `update(deltaTime): void`
- `render(): void`
- `getCollisionRect(): CollisionRect`
- `accept(CollisionVisitor): void`

### 6.3 Collision Interface
```java
interface CollisionVisitor {
    void visitAlien(Alien alien);
    void visitMissile(Missile missile);
    void visitBomb(Bomb bomb);
    void visitCannon(CoreCannon cannon);
    void visitShield(ShieldBrick brick);
}
```

---

## 7. Performance Specifications

### 7.1 Frame Budget (60 FPS = 16.67ms per frame)
- **Update Logic:** ≤ 5ms
- **Collision Detection:** ≤ 2ms
- **Rendering:** ≤ 8ms
- **Audio:** ≤ 1ms
- **Buffer:** 0.67ms

### 7.2 Memory Budget
- **Textures:** ≤ 50MB
- **Game Objects:** ≤ 10MB
- **Object Pools:** ≤ 5MB
- **Audio Buffers:** ≤ 20MB
- **Other:** ≤ 15MB
- **Total:** ≤ 100MB

### 7.3 Object Pool Sizes
- **Sprites:** 200 (reserve: 100)
- **GameObjects:** 100 (reserve: 50)
- **Collision Pairs:** 50 (reserve: 25)
- **Time Events:** 50 (reserve: 25)
- **Images:** 100 (reserve: 50)

---

## 8. Testing Specifications

### 8.1 Unit Test Coverage
- **Manager Classes:** ≥ 90%
- **Game Objects:** ≥ 85%
- **Collision System:** ≥ 90%
- **Rendering:** ≥ 70% (OpenGL limitations)
- **Overall:** ≥ 80%

### 8.2 Test Categories
1. **Unit Tests:** Individual class testing
2. **Integration Tests:** Component interaction
3. **Performance Tests:** Frame rate validation
4. **Regression Tests:** Bug prevention
5. **Manual Tests:** Gameplay validation

---

## 9. Security Specifications

### 9.1 Input Validation
- **SR-001:** Validate all user input
- **SR-002:** Sanitize file paths
- **SR-003:** Bounds checking on arrays
- **SR-004:** No buffer overflows

### 9.2 Resource Management
- **SR-005:** Proper cleanup of OpenGL resources
- **SR-006:** No resource leaks
- **SR-007:** Graceful handling of missing files
- **SR-008:** Error recovery mechanisms

---

## 10. Deployment Specifications

### 10.1 Build Artifacts
- **JAR:** Uber JAR with all dependencies
- **Native:** GraalVM native image (optional)
- **Size:** < 50MB (JAR), < 30MB (native)

### 10.2 System Requirements
- **OS:** Windows 10+, macOS 10.14+, Linux (kernel 4.x+)
- **CPU:** 2 GHz dual-core
- **RAM:** 512MB minimum, 1GB recommended
- **GPU:** OpenGL 3.3+ support
- **Disk:** 100MB free space

---

## 11. Future Enhancements

### 11.1 Planned Features
- Multiple difficulty levels
- Leaderboard system
- Replay system
- Custom key bindings UI
- Gamepad support
- Network multiplayer
- Level editor

### 11.2 Technical Improvements
- Shader-based effects
- Particle systems
- Advanced audio mixing
- Save/load game state
- Configuration UI
- Performance profiling tools

---

## 12. Glossary

- **CDI:** Contexts and Dependency Injection
- **DLink:** Doubly-Linked list node
- **FPS:** Frames Per Second
- **GLFW:** Graphics Library Framework
- **JOML:** Java OpenGL Math Library
- **LWJGL:** Lightweight Java Game Library
- **OpenGL:** Open Graphics Library
- **STB:** Sean Barrett's public domain libraries
- **UFO:** Unidentified Flying Object (bonus enemy)

---

## 13. References

- Original C# Implementation: `/Users/cbeeland/repositories/SpaceInvaders/`
- Migration Plan: `quarkus-migration.txt`
- Design Document: `CBeeland_Design_Doc.pdf`
- LWJGL Documentation: https://www.lwjgl.org/guide
- Quarkus Documentation: https://quarkus.io/guides/

---

**Document Control:**
- **Author:** Migration Team
- **Reviewers:** TBD
- **Approval:** TBD
- **Next Review:** After Phase 2 completion
