# Development Policies & Implementation Rules

**Version:** 1.0  
**Effective Date:** December 22, 2024  
**Status:** Mandatory for all contributors

---

## 1. Code Quality Standards

### 1.1 Testing Requirements

#### 1.1.1 Unit Test Coverage
**MANDATORY:** All new code must include unit tests.

- **Minimum Coverage:** 80% overall, 90% for critical components
- **Test-First Approach:** Write tests before or alongside implementation
- **Test Naming:** Use descriptive names with `@DisplayName` annotations
- **Test Structure:** Follow Arrange-Act-Assert pattern

**Example:**
```java
@Test
@DisplayName("Should return node from pool when available")
void testGetFromPool_WhenPoolHasNodes_ReturnsNode() {
    // Arrange
    manager.initialize(5, 3);
    
    // Act
    TestNode node = manager.getNode();
    
    // Assert
    assertNotNull(node);
}
```

#### 1.1.2 Coverage by Module
| Module | Minimum Coverage | Rationale |
|--------|-----------------|-----------|
| manager | 90% | Core infrastructure |
| gameobject | 85% | Game logic critical |
| collision | 90% | Gameplay critical |
| texture/image | 85% | Resource management |
| sprite | 75% | Some OpenGL dependencies |
| rendering | 70% | OpenGL limitations |
| sound | 70% | Audio dependencies |

#### 1.1.3 Test Execution
- **Pre-commit:** All tests must pass before committing
- **CI/CD:** Automated test execution on all branches
- **Coverage Reports:** Generated with every build
- **Failing Tests:** Block merges to main branch

**Commands:**
```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### 1.2 Code Style

#### 1.2.1 Java Conventions
- **Naming:**
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase`

- **Formatting:**
  - Indentation: 4 spaces (no tabs)
  - Line length: 120 characters maximum
  - Braces: K&R style (opening brace on same line)
  - Blank lines: One between methods, two between classes

#### 1.2.2 Documentation
**MANDATORY:** All public APIs must be documented.

```java
/**
 * Manages a pool of reusable objects to avoid garbage collection overhead.
 * 
 * @param <T> The type of objects managed by this pool
 */
public abstract class Manager<T extends DLink> {
    /**
     * Initializes the manager with specified pool sizes.
     * 
     * @param reserveSize Number of objects to preallocate
     * @param growSize Number of objects to add when pool is exhausted
     * @throws IllegalArgumentException if sizes are negative
     */
    protected void initialize(int reserveSize, int growSize) {
        // Implementation
    }
}
```

#### 1.2.3 Code Complexity
- **Cyclomatic Complexity:** ≤ 10 per method
- **Method Length:** ≤ 50 lines
- **Class Length:** ≤ 500 lines
- **Parameters:** ≤ 5 per method

**Enforcement:** Use static analysis tools (SonarQube, Checkstyle)

### 1.3 Error Handling

#### 1.3.1 Exception Policy
- **Checked Exceptions:** For recoverable errors
- **Unchecked Exceptions:** For programming errors
- **Never:** Catch and ignore exceptions
- **Always:** Log exceptions with context

```java
// GOOD
try {
    texture.load(filePath);
} catch (IOException e) {
    LOG.error("Failed to load texture: " + filePath, e);
    throw new TextureLoadException("Cannot load texture", e);
}

// BAD
try {
    texture.load(filePath);
} catch (Exception e) {
    // Silent failure - NEVER DO THIS
}
```

#### 1.3.2 Null Safety
- **Prefer:** Optional<T> over null returns
- **Validate:** All method parameters
- **Document:** When null is acceptable
- **Use:** @Nullable and @NonNull annotations

```java
public Optional<Texture> findTexture(Texture.Name name) {
    Objects.requireNonNull(name, "Texture name cannot be null");
    // Implementation
}
```

---

## 2. Design Patterns & Architecture

### 2.1 Pattern Usage

#### 2.1.1 Mandatory Patterns
These patterns MUST be used in specified contexts:

1. **Object Pool:** For all frequently created/destroyed objects
2. **Singleton (CDI):** For all manager classes
3. **Factory:** For complex object creation
4. **Observer:** For event notification
5. **State:** For state-based behavior

#### 2.1.2 Pattern Implementation Rules
- **Document:** Which pattern is being used and why
- **Consistency:** Use patterns consistently across codebase
- **Simplicity:** Don't over-engineer with unnecessary patterns

```java
/**
 * TextureManager implements Singleton pattern via CDI.
 * Uses Object Pool pattern for efficient texture management.
 */
@ApplicationScoped
public class TextureManager extends Manager {
    // Implementation
}
```

### 2.2 Dependency Injection

#### 2.2.1 CDI Usage
**MANDATORY:** Use CDI for all singleton managers.

```java
// GOOD - CDI managed
@ApplicationScoped
public class TextureManager extends Manager {
    @Inject
    Logger logger;
}

// BAD - Manual singleton
public class TextureManager {
    private static TextureManager instance;
    private TextureManager() {}
    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }
}
```

#### 2.2.2 Injection Rules
- **Constructor Injection:** Preferred for required dependencies
- **Field Injection:** Acceptable for optional dependencies
- **Setter Injection:** Avoid unless necessary
- **@Inject:** Use on constructors, not fields when possible

### 2.3 Separation of Concerns

#### 2.3.1 Layer Responsibilities
- **Presentation:** UI, input handling, rendering
- **Game Logic:** Game rules, state management
- **Resource Management:** Loading, caching, pooling
- **Infrastructure:** LWJGL, OpenGL, Quarkus

**Rule:** No layer should depend on layers above it.

#### 2.3.2 Class Responsibilities
- **Single Responsibility:** Each class has one reason to change
- **Interface Segregation:** Small, focused interfaces
- **Dependency Inversion:** Depend on abstractions, not concretions

---

## 3. Performance Requirements

### 3.1 Frame Rate

#### 3.1.1 Target Performance
**MANDATORY:** Maintain 60 FPS during normal gameplay.

- **Frame Time:** ≤ 16.67ms per frame
- **Update Logic:** ≤ 5ms
- **Rendering:** ≤ 8ms
- **Variance:** < 2ms frame-to-frame

#### 3.1.2 Performance Testing
- **Profile:** Use JProfiler or VisualVM regularly
- **Measure:** Frame times in production builds
- **Optimize:** Hot paths identified by profiling
- **Document:** Performance-critical sections

```java
// Mark performance-critical code
/**
 * PERFORMANCE CRITICAL: Called every frame for all active sprites.
 * Keep execution time < 0.1ms per sprite.
 */
public void update(float deltaTime) {
    // Implementation
}
```

### 3.2 Memory Management

#### 3.2.1 Object Pooling
**MANDATORY:** Use object pools for frequently created objects.

- **Sprites:** Pool all sprite instances
- **GameObjects:** Pool all game objects
- **Collision Data:** Pool collision rectangles
- **Events:** Pool timer events

#### 3.2.2 Memory Budget
- **Total:** ≤ 256MB during gameplay
- **Textures:** ≤ 50MB
- **Pools:** ≤ 10MB
- **Monitor:** Use JConsole to track memory usage

#### 3.2.3 Garbage Collection
- **Minimize:** Object allocation in game loop
- **Reuse:** Objects from pools
- **Avoid:** Creating temporary objects in hot paths
- **Target:** < 5ms GC pauses

---

## 4. Version Control

### 4.1 Git Workflow

#### 4.1.1 Branch Strategy
- **main:** Production-ready code only
- **develop:** Integration branch for features
- **feature/*:** Individual feature branches
- **bugfix/*:** Bug fix branches
- **hotfix/*:** Emergency production fixes

#### 4.1.2 Commit Messages
**Format:**
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Formatting, missing semicolons, etc.
- `refactor`: Code restructuring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Example:**
```
feat(sprite): implement sprite batching for performance

- Add SpriteBatch class for efficient rendering
- Reduce draw calls from 100 to 5 per frame
- Add unit tests for batch operations

Closes #123
```

#### 4.1.3 Pull Request Requirements
**MANDATORY:** All changes must go through PR review.

- **Tests:** All tests must pass
- **Coverage:** Must maintain or improve coverage
- **Review:** At least one approval required
- **CI:** All CI checks must pass
- **Documentation:** Update relevant docs

### 4.2 Code Review

#### 4.2.1 Review Checklist
- [ ] Code follows style guidelines
- [ ] Tests are included and passing
- [ ] Documentation is updated
- [ ] No obvious bugs or security issues
- [ ] Performance impact considered
- [ ] Design patterns used appropriately

#### 4.2.2 Review Standards
- **Timely:** Review within 24 hours
- **Constructive:** Provide helpful feedback
- **Thorough:** Check logic, not just style
- **Educational:** Explain reasoning

---

## 5. Security Policies

### 5.1 Input Validation

#### 5.1.1 User Input
**MANDATORY:** Validate all user input.

```java
public void setPosition(float x, float y) {
    if (Float.isNaN(x) || Float.isNaN(y)) {
        throw new IllegalArgumentException("Position cannot be NaN");
    }
    if (Float.isInfinite(x) || Float.isInfinite(y)) {
        throw new IllegalArgumentException("Position cannot be infinite");
    }
    this.x = x;
    this.y = y;
}
```

#### 5.1.2 File Operations
- **Validate:** All file paths
- **Sanitize:** User-provided paths
- **Restrict:** Access to game directory only
- **Check:** File existence before operations

### 5.2 Resource Management

#### 5.2.1 OpenGL Resources
**MANDATORY:** Clean up all OpenGL resources.

```java
@PreDestroy
public void cleanup() {
    if (textureId != 0) {
        GL11.glDeleteTextures(textureId);
        textureId = 0;
    }
}
```

#### 5.2.2 Memory Leaks
- **Monitor:** Resource allocation/deallocation
- **Test:** For leaks in long-running tests
- **Fix:** Immediately upon discovery
- **Document:** Resource ownership

---

## 6. Documentation Requirements

### 6.1 Code Documentation

#### 6.1.1 Required Documentation
**MANDATORY:** Document all public APIs.

- **Classes:** Purpose, usage, patterns
- **Methods:** Parameters, return values, exceptions
- **Complex Logic:** Inline comments explaining why
- **TODOs:** Track with issue numbers

#### 6.1.2 Documentation Standards
```java
/**
 * Manages texture resources using Object Pool pattern.
 * 
 * <p>This class is thread-safe and uses CDI for singleton management.
 * Textures are loaded using STB Image and cached for reuse.
 * 
 * <p>Example usage:
 * <pre>{@code
 * @Inject
 * TextureManager textureManager;
 * 
 * Texture texture = textureManager.add(
 *     Texture.Name.ALIENS, 
 *     "resources/aliens.png"
 * );
 * }</pre>
 * 
 * @see Texture
 * @see Manager
 * @since 1.0
 */
@ApplicationScoped
public class TextureManager extends Manager {
    // Implementation
}
```

### 6.2 Project Documentation

#### 6.2.1 Required Documents
- **README.md:** Project overview, setup instructions
- **MIGRATION_STATUS.md:** Current progress tracking
- **SYSTEM_SPECIFICATION.md:** Technical specifications
- **TESTING_GUIDELINES.md:** Testing standards
- **DEVELOPMENT_POLICIES.md:** This document

#### 6.2.2 Update Requirements
- **Code Changes:** Update relevant docs
- **New Features:** Document in specification
- **Bug Fixes:** Update known issues
- **Performance:** Update benchmarks

---

## 7. Continuous Integration

### 7.1 CI Pipeline

#### 7.1.1 Build Steps
1. Compile code
2. Run unit tests
3. Generate coverage report
4. Run static analysis
5. Build artifacts
6. Run integration tests

#### 7.1.2 Quality Gates
**MANDATORY:** All gates must pass.

- **Tests:** 100% passing
- **Coverage:** ≥ 80%
- **Complexity:** ≤ 10 per method
- **Duplications:** < 3%
- **Security:** No critical vulnerabilities

### 7.2 Deployment

#### 7.2.1 Release Process
1. Create release branch
2. Update version numbers
3. Run full test suite
4. Generate release notes
5. Tag release in Git
6. Build release artifacts
7. Deploy to distribution

---

## 8. Enforcement

### 8.1 Automated Checks

#### 8.1.1 Pre-commit Hooks
```bash
# .git/hooks/pre-commit
#!/bin/bash
mvn test
if [ $? -ne 0 ]; then
    echo "Tests failed. Commit aborted."
    exit 1
fi
```

#### 8.1.2 CI Checks
- **Build:** Must succeed
- **Tests:** Must pass
- **Coverage:** Must meet threshold
- **Style:** Must pass checkstyle

### 8.2 Manual Review

#### 8.2.1 Code Review
- **Required:** For all PRs
- **Checklist:** Use review checklist
- **Approval:** At least one reviewer

#### 8.2.2 Architecture Review
- **Required:** For major changes
- **Participants:** Lead developer + team
- **Documentation:** Design decisions recorded

---

## 9. Exceptions & Waivers

### 9.1 Policy Exceptions

#### 9.1.1 Requesting Exceptions
- **Document:** Reason for exception
- **Justify:** Why policy doesn't apply
- **Approve:** Requires lead developer approval
- **Track:** In issue tracker

#### 9.1.2 Temporary Waivers
- **OpenGL Tests:** May be disabled with @Disabled
- **Coverage:** May be lower for experimental code
- **Performance:** May be relaxed during prototyping

**Example:**
```java
@Test
@Disabled("Requires OpenGL context - waived per POLICY-001")
void testTextureRendering() {
    // Test implementation
}
```

---

## 10. Policy Updates

### 10.1 Amendment Process

#### 10.1.1 Proposing Changes
1. Create issue describing change
2. Discuss with team
3. Update document
4. Get approval
5. Communicate to team

#### 10.1.2 Version History
| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2024-12-22 | Initial version | Migration Team |

---

## 11. Resources

### 11.1 Tools
- **IDE:** IntelliJ IDEA or VS Code
- **Build:** Maven
- **Testing:** JUnit 5, Mockito
- **Coverage:** JaCoCo
- **Static Analysis:** SonarQube, Checkstyle
- **Profiling:** JProfiler, VisualVM

### 11.2 References
- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [Effective Java](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Clean Code](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [Quarkus Best Practices](https://quarkus.io/guides/)

---

**Acknowledgment:**
By contributing to this project, you agree to follow these policies and standards.

**Questions:**
Contact the lead developer or create an issue for clarification.
