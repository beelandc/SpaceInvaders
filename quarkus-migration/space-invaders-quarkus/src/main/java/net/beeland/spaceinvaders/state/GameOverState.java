package net.beeland.spaceinvaders.state;

import net.beeland.spaceinvaders.font.Font;
import net.beeland.spaceinvaders.font.FontManager;
import net.beeland.spaceinvaders.font.Glyph;
import net.beeland.spaceinvaders.font.GlyphManager;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Game Over State - Displayed when the player loses all lives.
 * This state shows the final score and allows the player to restart.
 *
 * Responsibilities:
 * - Display "GAME OVER" message
 * - Show final score
 * - Show high score if achieved
 * - Wait for player input to restart or return to attract mode
 * - Transition to AttractState when player presses space
 */
public class GameOverState extends GameState {
    
    private static final Logger logger = LoggerFactory.getLogger(GameOverState.class);
    
    // Font manager for text rendering
    private FontManager fontManager;
    private GlyphManager glyphManager;
    
    // Window handle for input
    private long window;
    
    // Key state tracking for debouncing
    private boolean spaceWasPressed = false;
    
    private int finalScore;
    private int highScore;
    private boolean isNewHighScore;
    
    /**
     * Constructor for GameOverState.
     */
    public GameOverState() {
        super(GameStateName.GAME_OVER);
        this.finalScore = 0;
        this.highScore = 0;
        this.isNewHighScore = false;
        this.fontManager = null;
        this.glyphManager = null;
    }
    
    /**
     * Set the font manager for this state.
     *
     * @param fontManager The font manager instance
     */
    public void setFontManager(FontManager fontManager) {
        this.fontManager = fontManager;
    }
    
    /**
     * Set the glyph manager for this state.
     *
     * @param glyphManager The glyph manager instance
     */
    public void setGlyphManager(GlyphManager glyphManager) {
        this.glyphManager = glyphManager;
    }
    
    /**
     * Set the window handle for input processing
     *
     * @param window GLFW window handle
     */
    public void setWindow(long window) {
        this.window = window;
    }
    
    /**
     * Initialize the game over state.
     * Sets up the game over screen with score display.
     */
    @Override
    public void enter() {
        logger.info("Entering Game Over State - Final Score: {}", finalScore);
        
        // Reset key debouncing flag to ensure clean state
        this.spaceWasPressed = false;
        
        // Verify managers are set (warn only for testing compatibility)
        if (this.fontManager == null) {
            logger.warn("FontManager not set - skipping font creation (test mode?)");
        }
        
        if (this.glyphManager == null) {
            logger.warn("GlyphManager not set - fonts will not render (test mode?)");
        }
        
        // Create state-scoped managers (not CDI-managed, state-specific instances)
        this.gameObjectManager = new GameObjectManager();
        this.timerManager = new TimerManager();
        this.timerManager.init();
        
        // Check if new high score
        if (this.finalScore > this.highScore) {
            this.highScore = this.finalScore;
            this.isNewHighScore = true;
            logger.info("New High Score: {}", this.highScore);
        }
        
        //---------------------------------------------------------------------------------------------------------
        // Create Fonts for Game Over Screen (Y coordinates converted to bottom-left origin)
        //---------------------------------------------------------------------------------------------------------
        if (this.fontManager != null && this.glyphManager != null) {
            logger.info("Creating game over screen fonts with GlyphManager");
            
            // "GAME OVER" title - Original Y=500 -> new Y=100 (near bottom)
            Font gameOverFont = fontManager.add(Font.Name.END_GAME_OVER, "GAME OVER",
                           Glyph.Name.CONSOLAS_36PT, 350.0f, 100.0f);
            setGlyphManagerOnFont(gameOverFont);
            
            // Final score display - Original Y=400 -> new Y=200
            String scoreText = "SCORE: " + String.format("%04d", finalScore);
            Font scoreFont = fontManager.add(Font.Name.END_GAME, scoreText,
                           Glyph.Name.CONSOLAS_36PT, 320.0f, 200.0f);
            setGlyphManagerOnFont(scoreFont);
            
            // High score display - Original Y=350 -> new Y=250
            String highScoreText = isNewHighScore ? "NEW HIGH SCORE!" : "HIGH SCORE: " + String.format("%04d", highScore);
            Font highScoreFont = fontManager.add(Font.Name.HIGH_SCORE, highScoreText,
                           Glyph.Name.CONSOLAS_36PT, isNewHighScore ? 280.0f : 300.0f, 250.0f);
            setGlyphManagerOnFont(highScoreFont);
            
            // Instructions - Original Y=200 -> new Y=400 (near top)
            Font instructionsFont = fontManager.add(Font.Name.END_INSTRUCTIONS, "PRESS SPACE TO CONTINUE",
                           Glyph.Name.CONSOLAS_36PT, 240.0f, 400.0f);
            setGlyphManagerOnFont(instructionsFont);
            
            logger.debug("Game Over State initialized with {} fonts", 4);
        } else {
            logger.info("Skipping font creation - managers not available (test mode)");
        }
    }
    
    /**
     * Helper method to set GlyphManager on a font's sprite.
     *
     * @param font The font to configure
     */
    private void setGlyphManagerOnFont(Font font) {
        if (font != null && font.getFontSprite() != null) {
            font.getFontSprite().setGlyphManager(this.glyphManager);
        } else {
            logger.warn("Could not set GlyphManager - font or sprite is null");
        }
    }
    
    /**
     * Update the game over state.
     * Processes input and checks for state transitions.
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    @Override
    public void update(float deltaTime) {
        // Handle input
        handleInput();
        
        // Update timers (for any animations)
        if (this.timerManager != null) {
            this.timerManager.update(deltaTime);
        }
        
        // Update game objects (for animations)
        if (this.gameObjectManager != null) {
            this.gameObjectManager.update();
        }
        
        // Check for state change
        if (this.changeStatePending) {
            logger.info("State change pending from GameOver to {}", this.nextState);
        }
    }
    
    /**
     * Render the game over state.
     * Draws the game over screen and score information.
     */
    @Override
    public void draw(net.beeland.spaceinvaders.sprite.SpriteBatch spriteBatch) {
        // Render all fonts using the sprite batch
        if (fontManager != null && spriteBatch != null) {
            fontManager.renderAll(spriteBatch);
        }
    }
    
    /**
     * Clean up the game over state.
     * Releases resources when exiting this state.
     */
    @Override
    public void exit() {
        logger.info("Exiting Game Over State");
        
        // Clear all fonts from FontManager
        if (this.fontManager != null) {
            this.fontManager.removeAll();
            logger.debug("Cleared all fonts from FontManager");
        }
        
        // Clean up managers
        if (this.gameObjectManager != null) {
            this.gameObjectManager.destroy();
            this.gameObjectManager = null;
        }
        
        if (this.timerManager != null) {
            this.timerManager.destroy();
            this.timerManager = null;
        }
        
        // Reset flags
        this.isNewHighScore = false;
        
        logger.debug("Game Over State cleaned up");
    }
    
    /**
     * Handle input for the game over state.
     * Listens for space bar to return to attract mode.
     * Uses key debouncing to prevent multiple triggers.
     */
    @Override
    public void handleInput() {
        if (window == 0) {
            return;
        }
        
        // Check for space bar press with debouncing
        boolean spaceIsPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;
        if (spaceIsPressed && !spaceWasPressed) {
            logger.info("Returning to attract state");
            setNextState(GameStateName.ATTRACT);
        }
        spaceWasPressed = spaceIsPressed;
    }
    
    /**
     * Set the final score to display.
     * Should be called before entering this state.
     * 
     * @param score The player's final score
     */
    public void setFinalScore(int score) {
        this.finalScore = score;
    }
    
    /**
     * Get the final score.
     * 
     * @return The final score
     */
    public int getFinalScore() {
        return this.finalScore;
    }
    
    /**
     * Set the high score.
     * 
     * @param score The high score
     */
    public void setHighScore(int score) {
        this.highScore = score;
    }
    
    /**
     * Get the high score.
     * 
     * @return The high score
     */
    public int getHighScore() {
        return this.highScore;
    }
    
    /**
     * Check if the player achieved a new high score.
     * 
     * @return true if new high score, false otherwise
     */
    public boolean isNewHighScore() {
        return this.isNewHighScore;
    }
}