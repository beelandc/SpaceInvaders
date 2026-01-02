#!/bin/bash

# Space Invaders - Run Script
# This script runs the Space Invaders game with the correct JVM arguments for macOS

echo "Starting Space Invaders..."
echo ""

# Check if the JAR file exists
JAR_FILE="target/quarkus-app/quarkus-run.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Please build the project first with: mvn clean package"
    exit 1
fi

# Run the application with required JVM arguments
# -XstartOnFirstThread: Required for GLFW on macOS
# --enable-native-access=ALL-UNNAMED: Required for LWJGL native library access in Java 21+
# -Dsun.java2d.metal=true: Enable Metal rendering backend on macOS
# -Dorg.lwjgl.util.DebugLoader=true: Enable LWJGL debug output
# -Dorg.lwjgl.util.Debug=true: Enable LWJGL debug mode
java -XstartOnFirstThread \
     --enable-native-access=ALL-UNNAMED \
     -Dsun.java2d.metal=true \
     -Dorg.lwjgl.util.DebugLoader=true \
     -Dorg.lwjgl.util.Debug=true \
     -jar "$JAR_FILE"