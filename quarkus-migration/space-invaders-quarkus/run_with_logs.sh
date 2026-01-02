#!/bin/bash

# Build and run Space Invaders with debug logging

echo "Building project..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo ""
echo "Starting Space Invaders with debug logging..."
echo "Game will run for 30 seconds, then logs will be saved to game_output.log"
echo ""

# Run the game in background and capture output
./run.sh 2>&1 | tee game_output.log &
GAME_PID=$!

# Wait 30 seconds
sleep 30

# Kill the game
echo ""
echo "Stopping game..."
kill $GAME_PID 2>/dev/null

echo ""
echo "Logs saved to game_output.log"
echo ""
echo "Searching for glyph-related log entries..."
grep -i "glyph\|char\|font" game_output.log | head -50