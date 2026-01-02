# Running Space Invaders - Quarkus Edition

## Quick Start

### Method 1: Using the Run Script (Recommended)

```bash
# Navigate to the project directory
cd quarkus-migration/space-invaders-quarkus

# Build the project
mvn clean package

# Run the game
./run.sh
```

### Method 2: Using Maven Quarkus Plugin

**⚠️ IMPORTANT:** Due to a limitation with the Quarkus Maven plugin, you need to set the JVM arguments as an environment variable:

```bash
# Navigate to the project directory
cd quarkus-migration/space-invaders-quarkus

# Set JVM arguments (required for macOS)
export MAVEN_OPTS="-XstartOnFirstThread --enable-native-access=ALL-UNNAMED"

# Run in dev mode
mvn quarkus:dev
```

### Method 3: Direct Java Execution

```bash
# Navigate to the project directory
cd quarkus-migration/space-invaders-quarkus

# Build the project
mvn clean package

# Run with proper JVM arguments
java -XstartOnFirstThread \
     --enable-native-access=ALL-UNNAMED \
     -jar target/quarkus-app/quarkus-run.jar
```

---

## Understanding the JVM Arguments

### `-XstartOnFirstThread`
**Required for:** macOS only  
**Purpose:** GLFW (the windowing library) on macOS requires that all window operations happen on the main thread. This JVM argument ensures that the Java main thread is the first thread started by the JVM.

**Without this argument on macOS:**
- The application will crash silently during `glfwInit()`
- No window will appear
- No error message will be displayed

### `--enable-native-access=ALL-UNNAMED`
**Required for:** Java 21+  
**Purpose:** LWJGL uses native libraries that need to access restricted methods in the JDK. This flag grants permission for native access.

**Without this argument:**
- You'll see warnings about restricted method access
- The application may still work but with warnings

---

## Troubleshooting

### Issue: No window appears

**Symptoms:**
- Application starts
- Logs show "Starting Space Invaders..." and "Initializing game..."
- Application stops without showing "Starting game loop..."
- No window appears

**Solution:**
Ensure you're using the `-XstartOnFirstThread` JVM argument. Use one of the methods above.

### Issue: "Unable to initialize GLFW" error

**Possible causes:**
1. Missing `-XstartOnFirstThread` on macOS
2. GLFW native libraries not loaded correctly
3. Display/graphics driver issues

**Solution:**
1. Verify JVM arguments are being applied
2. Check that LWJGL native libraries are in the classpath
3. Try running with `LIBGL_ALWAYS_SOFTWARE=1` for software rendering

### Issue: Warnings about restricted method access

**Symptoms:**
```
WARNING: A restricted method in java.lang.System has been called
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning
```

**Solution:**
Add `--enable-native-access=ALL-UNNAMED` to your JVM arguments (already included in the run script).

### Issue: HTTP server still starts

**Symptoms:**
```
INFO: Listening on: http://0.0.0.0:xxxxx
```

**Note:** This is expected behavior with the current Quarkus configuration. The HTTP server is disabled but Quarkus still reports a port. This doesn't affect the game functionality.

---

## Expected Output

When the application runs successfully, you should see:

```
INFO: Starting Space Invaders...
INFO: Initializing game...
INFO: Game initialized successfully
INFO: OpenGL Version: 4.1 INTEL-...
INFO: GLSL Version: 4.10
INFO: Starting game loop...
```

And a **black window (800x600)** should appear with the title "Space Invaders - Quarkus Edition".

**Controls:**
- `ESC` - Exit the game

---

## Building for Distribution

### Create an Uber JAR

```bash
mvn clean package -Dquarkus.package.jar.type=uber-jar
```

Then distribute the JAR with instructions to run:
```bash
java -XstartOnFirstThread --enable-native-access=ALL-UNNAMED \
     -jar space-invaders-quarkus-1.0.0-SNAPSHOT-runner.jar
```

### Platform-Specific Notes

**macOS:**
- Must use `-XstartOnFirstThread`
- Native libraries for `natives-macos-arm64` are included

**Linux:**
- Remove `-XstartOnFirstThread` (not needed)
- Update `pom.xml` to use `natives-linux` classifier

**Windows:**
- Remove `-XstartOnFirstThread` (not needed)
- Update `pom.xml` to use `natives-windows` classifier

---

## Development Mode

For development with hot reload:

```bash
# Set environment variable
export MAVEN_OPTS="-XstartOnFirstThread --enable-native-access=ALL-UNNAMED"

# Run in dev mode
mvn quarkus:dev
```

**Note:** Hot reload may not work perfectly with LWJGL/GLFW. You may need to restart the application for some changes.

---

## Validation Checklist

After applying the fixes, verify:

- [ ] Application builds without errors: `mvn clean package`
- [ ] Run script is executable: `ls -l run.sh` shows `x` permission
- [ ] Window appears when running: `./run.sh`
- [ ] Window title shows "Space Invaders - Quarkus Edition"
- [ ] Window size is 800x600 pixels
- [ ] Window has black background
- [ ] ESC key closes the window
- [ ] Logs show "Starting game loop..." message
- [ ] No GLFW initialization errors in logs

---

## Additional Resources

- [LWJGL Documentation](https://www.lwjgl.org/guide)
- [GLFW Documentation](https://www.glfw.org/documentation.html)
- [Quarkus Documentation](https://quarkus.io/guides/)