# Testing Guidelines for Space Invaders Quarkus Migration

## Overview
This document outlines the testing strategy and guidelines for the Space Invaders Quarkus migration project. We aim for **80%+ code coverage** across all modules.

## Testing Framework
- **JUnit 5**: Primary testing framework
- **Quarkus Test**: For CDI and integration testing
- **Mockito**: For mocking dependencies
- **AssertJ**: For fluent assertions (optional)

## Test Structure

### Unit Tests
Located in: `src/test/java/com/depaul/spaceinvaders/`

#### Naming Convention
- Test class: `<ClassName>Test.java`
- Test method: `test<MethodName>_<Scenario>_<ExpectedResult>()`
- Use `@DisplayName` for readable test descriptions

#### Example:
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

### Integration Tests
For components requiring OpenGL context or full application context:
- Mark with `@QuarkusTest`
- Use `@Disabled` for tests requiring graphics context
- Document why tests are disabled

### Test Categories

#### 1. Manager Classes (DLink, Manager, *Manager)
**Coverage Target: 90%+**
- Test object pool operations (get, return, grow)
- Test lifecycle (initialize, destroy)
- Test statistics tracking
- Test edge cases (null handling, empty pools)
- Test CDI injection

#### 2. Game Objects (Texture, Image, Sprite, etc.)
**Coverage Target: 85%+**
- Test property setters/getters
- Test wash() method
- Test coordinate calculations
- Test null handling
- Mock dependencies (e.g., Texture in Image tests)

#### 3. Rendering Components
**Coverage Target: 70%+** (due to OpenGL dependencies)
- Test non-OpenGL logic
- Mock OpenGL calls where possible
- Integration tests for actual rendering

#### 4. Game Logic
**Coverage Target: 90%+**
- Test collision detection
- Test game state transitions
- Test input handling
- Test scoring logic

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=DLinkTest
```

### Run with Coverage Report
```bash
mvn clean test jacoco:report
```
Coverage report will be in: `target/site/jacoco/index.html`

### Run in Quarkus Dev Mode
```bash
mvn quarkus:dev
```
Then press `r` to run tests

## Test Development Workflow

### For Each New Feature:
1. **Write tests first** (TDD approach recommended)
2. Implement the feature
3. Ensure all tests pass
4. Check coverage: `mvn jacoco:report`
5. Add additional tests if coverage < 80%

### For Bug Fixes:
1. Write a failing test that reproduces the bug
2. Fix the bug
3. Ensure the test passes
4. Verify no regression in other tests

## Mocking Guidelines

### When to Mock:
- External dependencies (OpenGL, file system)
- Complex dependencies that are tested separately
- Slow operations (database, network)

### When NOT to Mock:
- Simple POJOs
- Classes under test
- Value objects

### Example with Mockito:
```java
@Mock
private Texture mockTexture;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
    when(mockTexture.getWidth()).thenReturn(256);
    when(mockTexture.getHeight()).thenReturn(256);
}
```

## Coverage Targets by Module

| Module | Target Coverage | Notes |
|--------|----------------|-------|
| manager | 90% | Core infrastructure |
| texture | 70% | OpenGL dependencies |
| image | 85% | Mostly pure logic |
| sprite | 75% | Some OpenGL calls |
| gameobject | 85% | Game logic |
| collision | 90% | Critical for gameplay |
| input | 85% | Event handling |
| sound | 70% | Audio dependencies |

## Continuous Integration

### Pre-commit Checks:
```bash
# Run before committing
mvn clean test
```

### CI Pipeline (Future):
1. Build project
2. Run all tests
3. Generate coverage report
4. Fail if coverage < 80%
5. Fail if any test fails

## Test Data

### Test Resources:
Located in: `src/test/resources/`
- Mock textures (small test images)
- Test configuration files
- Sample game data

## Common Test Patterns

### Testing Object Pools:
```java
@Test
void testPoolGrowth() {
    manager.initialize(2, 5);
    
    // Exhaust pool
    Node n1 = manager.get();
    Node n2 = manager.get();
    
    // Should trigger growth
    Node n3 = manager.get();
    
    assertNotNull(n3);
}
```

### Testing CDI Beans:
```java
@QuarkusTest
class MyManagerTest {
    @Inject
    MyManager manager;
    
    @Test
    void testInjection() {
        assertNotNull(manager);
    }
}
```

### Testing with OpenGL (Disabled):
```java
@Test
@Disabled("Requires OpenGL context")
void testTextureLoading() {
    // This would need GLFW window
}
```

## Best Practices

1. **Keep tests independent**: Each test should be able to run alone
2. **Use descriptive names**: Test names should explain what they test
3. **One assertion per test**: Focus on testing one thing
4. **Clean up resources**: Use `@AfterEach` for cleanup
5. **Test edge cases**: null, empty, boundary values
6. **Test error conditions**: Not just happy paths
7. **Keep tests fast**: Mock slow operations
8. **Maintain tests**: Update tests when code changes

## Future Enhancements

- [ ] Add integration tests with actual OpenGL context
- [ ] Add performance benchmarks
- [ ] Add mutation testing
- [ ] Add property-based testing for complex logic
- [ ] Set up CI/CD pipeline
- [ ] Add visual regression tests for rendering

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Quarkus Testing Guide](https://quarkus.io/guides/getting-started-testing)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Coverage Tool](https://www.jacoco.org/jacoco/)
