package net.beeland.spaceinvaders.state;

import net.beeland.spaceinvaders.font.Font;
import net.beeland.spaceinvaders.font.FontManager;
import net.beeland.spaceinvaders.font.Glyph;
import net.beeland.spaceinvaders.font.GlyphManager;
import net.beeland.spaceinvaders.gameobject.*;
import net.beeland.spaceinvaders.sprite.SpriteBatch;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attract State - Title screen and game instructions.
 * This state is shown when the game starts and after game over.
 *
 * Responsibilities:
 * - Display game title and instructions
 * - Show alien point values
 * - Wait for player input to start game
 * - Transition to PlayState when player presses start
 */
public class AttractState extends GameState {
    
    private static final Logger logger = LoggerFactory.getLogger(AttractState.class);
    
    // Font manager for text rendering (will be injected)
    private FontManager fontManager;
    private GlyphManager glyphManager;
    
    // Sprite batch for rendering
    private SpriteBatch textSpriteBatch;
    private SpriteBatch alienSpriteBatch;
    
    // Alien sprites for score table
    private FlyingSaucer flyingSaucer;
    private SquidAlien squidAlien;
    private CrabAlien crabAlien;
    private JellyfishAlien jellyfishAlien;
    
    // Window handle for input
    private long window;
    
    // Key state tracking for debouncing
    private boolean key1WasPressed = false;
    private boolean key2WasPressed = false;
    
    /**
     * Constructor for AttractState.
     */
    public AttractState() {
        super(GameStateName.ATTRACT);
        // FontManager and GlyphManager will be set by GameStateManager
        this.fontManager = null;
        this.glyphManager = null;
    }
    
    /**
     * Set the font manager for this state.
     * Must be called before enter().
     *
     * @param fontManager The font manager instance
     */
    public void setFontManager(FontManager fontManager) {
        this.fontManager = fontManager;
    }
    
    /**
     * Set the glyph manager for this state.
     * Must be called before enter().
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
     * Initialize the attract state.
     * Sets up the title screen with game instructions.
     */
    @Override
    public void enter() {
        logger.info("Entering Attract State");
        
        // Reset key debouncing flags to ensure clean state
        this.key1WasPressed = false;
        this.key2WasPressed = false;
        
        // Create state-scoped managers (not CDI-managed, state-specific instances)
        this.gameObjectManager = new GameObjectManager();
        this.timerManager = new TimerManager();
        this.timerManager.init();
        
        // Verify font manager is set (warn only for testing compatibility)
        if (this.fontManager == null) {
            logger.warn("FontManager not set - skipping font creation (test mode?)");
        }
        
        if (this.glyphManager == null) {
            logger.warn("GlyphManager not set - fonts will not render (test mode?)");
        }
        
        // Initialize sprite batches (will be set by Game class)
        // For now, we'll create placeholder batches
        // TODO: Get these from a SpriteBatchManager
        
        //---------------------------------------------------------------------------------------------------------
        // Create Fonts for Attract Screen
        //---------------------------------------------------------------------------------------------------------
        if (this.fontManager != null && this.glyphManager != null) {
            logger.info("Creating attract screen fonts with GlyphManager");
            
            // High Score (top of screen) - converted from top-left to bottom-left origin
            // Original Y=750 (near top in 896px window) -> scaled and flipped for 600px window
            Font highScoreFont = fontManager.add(Font.Name.HIGH_SCORE, "HIGH SCORE: 0000",
                           Glyph.Name.CONSOLAS_36PT, 300.0f, 550.0f);  // 600 - 50
            setGlyphManagerOnFont(highScoreFont);
            
            // Title - Original Y=600 -> new Y=400
            Font titleFont = fontManager.add(Font.Name.ATTRACT_TITLE, "SPACE INVADERS",
                           Glyph.Name.CONSOLAS_36PT, 290.0f, 500.0f);  // 600 - 100
            setGlyphManagerOnFont(titleFont);
            
            // Play instruction - Original Y=550 -> new Y=450
            Font playFont = fontManager.add(Font.Name.ATTRACT_PLAY, "PLAY",
                           Glyph.Name.CONSOLAS_36PT, 385.0f, 450.0f);  // 600 - 150
            setGlyphManagerOnFont(playFont);
            
            // Score advance table header - Original Y=450 -> new Y=350
            Font tableFont = fontManager.add(Font.Name.ATTRACT_SCORE_ADVANCE_TABLE, "* SCORE ADVANCE TABLE *",
                           Glyph.Name.CONSOLAS_36PT, 225.0f, 350.0f);  // 600 - 250
            setGlyphManagerOnFont(tableFont);
            
            // Point values for each alien type - Original Y=400 -> new Y=300
            Font saucerFont = fontManager.add(Font.Name.ATTRACT_FLYING_SAUCER_POINTS, "= ?  MYSTERY",
                           Glyph.Name.CONSOLAS_36PT, 350.0f, 300.0f);  // 600 - 300
            setGlyphManagerOnFont(saucerFont);
            
            // Original Y=350 -> new Y=250
            Font squidFont = fontManager.add(Font.Name.ATTRACT_SQUID_POINTS, "= 30  POINTS",
                           Glyph.Name.CONSOLAS_36PT, 350.0f, 250.0f);  // 600 - 350
            setGlyphManagerOnFont(squidFont);
            
            // Original Y=300 -> new Y=200
            Font crabFont = fontManager.add(Font.Name.ATTRACT_CRAB_POINTS, "= 20  POINTS",
                           Glyph.Name.CONSOLAS_36PT, 350.0f, 200.0f);  // 600 - 400
            setGlyphManagerOnFont(crabFont);
            
            // Original Y=250 -> new Y=150
            Font jellyFont = fontManager.add(Font.Name.ATTRACT_JELLYFISH_POINTS, "= 10  POINTS",
                           Glyph.Name.CONSOLAS_36PT, 350.0f, 150.0f);  // 600 - 450
            setGlyphManagerOnFont(jellyFont);
            
            // Player mode instructions - Original Y=150 -> new Y=100
            Font instruction1Font = fontManager.add(Font.Name.ATTRACT_INSTRUCTION_1P, "PRESS 1 FOR 1-PLAYER MODE",
                           Glyph.Name.CONSOLAS_36PT, 200.0f, 100.0f);  // 600 - 500
            setGlyphManagerOnFont(instruction1Font);
            
            // Original Y=115 -> new Y=50
            Font instruction2Font = fontManager.add(Font.Name.ATTRACT_INSTRUCTION_2P, "PRESS 2 FOR 2-PLAYER MODE",
                           Glyph.Name.CONSOLAS_36PT, 200.0f, 50.0f);  // 600 - 550
            setGlyphManagerOnFont(instruction2Font);
        } else {
            logger.info("Skipping font creation - managers not available (test mode)");
        }
        
        //---------------------------------------------------------------------------------------------------------
        // Create Alien Sprites for Score Table (Y coordinates converted to bottom-left origin)
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating alien sprites for score table");
        
        // Flying Saucer (mystery points) - Original Y=400 -> new Y=300
        flyingSaucer = new FlyingSaucer(GameObject.GameObjectName.FLYING_SAUCER, 300.0f, 300.0f);
        gameObjectManager.attach(flyingSaucer);
        
        // Squid Alien (30 points) - Original Y=350 -> new Y=250
        squidAlien = new SquidAlien(300.0f, 250.0f);
        gameObjectManager.attach(squidAlien);
        
        // Crab Alien (20 points) - Original Y=300 -> new Y=200
        crabAlien = new CrabAlien(300.0f, 200.0f);
        gameObjectManager.attach(crabAlien);
        
        // Jellyfish Alien (10 points) - Original Y=250 -> new Y=150
        jellyfishAlien = new JellyfishAlien(300.0f, 150.0f);
        gameObjectManager.attach(jellyfishAlien);
        
        logger.info("Attract State initialized with {} fonts and 4 alien sprites", 10);
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
     * Update the attract state.
     * Processes input and checks for state transitions.
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    @Override
    public void update(float deltaTime) {
        // Handle input
        handleInput();
        
        // Update timers
        if (this.timerManager != null) {
            this.timerManager.update(deltaTime);
        }
        
        // Update game objects (for animations)
        if (this.gameObjectManager != null) {
            this.gameObjectManager.update();
        }
        
        // Check for state change
        if (this.changeStatePending) {
            logger.info("State change pending from Attract to {}", this.nextState);
        }
    }
    
    /**
     * Render the attract state.
     * Draws the title screen and instructions.
     */
    @Override
    public void draw(net.beeland.spaceinvaders.sprite.SpriteBatch spriteBatch) {
        // Render all fonts using the sprite batch
        if (fontManager != null && spriteBatch != null) {
            fontManager.renderAll(spriteBatch);
        }
        
        // Draw alien sprites for score table
        if (spriteBatch != null && gameObjectManager != null) {
            // Aliens will be drawn through their sprite proxies
            // This requires the sprite system to be fully integrated
        }
    }
    
    /**
     * Clean up the attract state.
     * Releases resources when exiting this state.
     */
    @Override
    public void exit() {
        logger.info("Exiting Attract State");
        
        // Clear all fonts from FontManager
        if (this.fontManager != null) {
            this.fontManager.removeAll();
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
    }
    
    /**
     * Handle input for the attract state.
     * Listens for 1 or 2 key to start the game.
     * Uses key debouncing to prevent multiple triggers.
     */
    @Override
    public void handleInput() {
        if (window == 0) {
            return;
        }
        
        // Check for '1' key press (1-player mode) with debouncing
        boolean key1IsPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_1) == GLFW.GLFW_PRESS;
        if (key1IsPressed && !key1WasPressed) {
            logger.info("1-Player mode selected");
            setNextState(GameStateName.PLAY);
        }
        key1WasPressed = key1IsPressed;
        
        // Check for '2' key press (2-player mode) with debouncing
        boolean key2IsPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_2) == GLFW.GLFW_PRESS;
        if (key2IsPressed && !key2WasPressed) {
            logger.info("2-Player mode selected");
            // TODO: Set two-player mode flag
            setNextState(GameStateName.PLAY);
        }
        key2WasPressed = key2IsPressed;
    }
}