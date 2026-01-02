package net.beeland.spaceinvaders.state;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.beeland.spaceinvaders.font.FontManager;
import net.beeland.spaceinvaders.font.GlyphManager;
import net.beeland.spaceinvaders.sprite.SpriteBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameStateManager manages game state transitions using the State pattern.
 * This is a CDI-managed singleton that maintains all game states and handles
 * transitions between them.
 * 
 * Design Pattern: State Pattern
 * - Context: GameStateManager (this class)
 * - State: GameState (abstract base)
 * - ConcreteStates: AttractState, PlayState, GameOverState
 * 
 * The manager maintains references to all states and delegates behavior
 * to the current active state.
 */
@ApplicationScoped
public class GameStateManager {
    
    private static final Logger logger = LoggerFactory.getLogger(GameStateManager.class);
    
    // All possible game states
    private final AttractState attractState;
    private final PlayState playState;
    private final GameOverState gameOverState;
    
    // Current active state
    private GameState currentState;
    
    // High score tracking (persists across states)
    private int highScore;
    
    // Window handle for input processing
    private long window;
    
    // CDI-injected managers
    @Inject
    FontManager fontManager;
    
    @Inject
    GlyphManager glyphManager;
    
    /**
     * Constructor - initializes all game states.
     * CDI will create this as a singleton.
     */
    public GameStateManager() {
        logger.info("Initializing GameStateManager");
        
        // Create all state instances
        this.attractState = new AttractState();
        this.playState = new PlayState();
        this.gameOverState = new GameOverState();
        
        // Start in attract mode
        this.currentState = null;
        this.highScore = 0;
        
        logger.debug("GameStateManager initialized with all states");
    }
    
    /**
     * Initialize the manager and enter the initial state.
     * Should be called once at game startup.
     */
    public void initialize() {
        logger.info("Starting game in Attract State");
        changeState(GameStateName.ATTRACT);
    }
    
    /**
     * Set the window handle for all states.
     * Must be called after initialize() to enable input processing.
     *
     * @param window The GLFW window handle
     */
    public void setWindow(long window) {
        this.window = window;
        logger.info("Setting window handle for all states: {}", window);
        
        // Pass window to all states
        this.attractState.setWindow(window);
        this.playState.setWindow(window);
        this.gameOverState.setWindow(window);
        
        // Pass managers to all states
        this.attractState.setFontManager(this.fontManager);
        this.attractState.setGlyphManager(this.glyphManager);
        this.playState.setFontManager(this.fontManager);
        this.playState.setGlyphManager(this.glyphManager);
        this.gameOverState.setFontManager(this.fontManager);
        this.gameOverState.setGlyphManager(this.glyphManager);
        
        logger.debug("Window handle and managers set for all states");
    }
    
    /**
     * Update the current game state.
     * Should be called every frame.
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    public void update(float deltaTime) {
        if (this.currentState == null) {
            logger.warn("No current state - initializing to Attract");
            initialize();
            return;
        }
        
        // Update the current state
        this.currentState.update(deltaTime);
        
        // Check if state wants to transition
        if (this.currentState.isChangeStatePending()) {
            GameStateName nextState = this.currentState.getNextState();
            if (nextState != null) {
                changeState(nextState);
            }
        }
    }
    
    /**
     * Render the current game state.
     * Should be called every frame after update.
     *
     * @param spriteBatch The sprite batch to use for rendering
     */
    public void draw(SpriteBatch spriteBatch) {
        if (this.currentState != null) {
            this.currentState.draw(spriteBatch);
        }
    }
    
    /**
     * Handle input for the current game state.
     * Should be called every frame before update.
     */
    public void handleInput() {
        if (this.currentState != null) {
            this.currentState.handleInput();
        }
    }
    
    /**
     * Change to a different game state.
     * Exits the current state and enters the new state.
     * 
     * @param newStateName The state to transition to
     */
    public void changeState(GameStateName newStateName) {
        if (newStateName == null) {
            logger.error("Cannot change to null state");
            return;
        }
        
        // Exit current state and reset its transition flag
        if (this.currentState != null) {
            logger.info("Exiting state: {}", this.currentState.getStateName());
            this.currentState.exit();
            // CRITICAL: Reset the state transition flag to prevent infinite loops
            this.currentState.setChangeStatePending(false);
        }
        
        // Get the new state instance
        GameState newState = getState(newStateName);
        if (newState == null) {
            logger.error("Failed to get state: {}", newStateName);
            return;
        }
        
        // Transfer data between states if needed
        transferStateData(this.currentState, newState);
        
        // Set new state
        this.currentState = newState;
        
        // Enter new state
        logger.info("Entering state: {}", newStateName);
        this.currentState.enter();
    }
    
    /**
     * Get a state instance by name.
     * 
     * @param stateName The name of the state to retrieve
     * @return The state instance, or null if not found
     */
    private GameState getState(GameStateName stateName) {
        switch (stateName) {
            case ATTRACT:
                return this.attractState;
            case PLAY:
                return this.playState;
            case GAME_OVER:
                return this.gameOverState;
            default:
                logger.error("Unknown state name: {}", stateName);
                return null;
        }
    }
    
    /**
     * Transfer data between states during transitions.
     * For example, pass final score from PlayState to GameOverState.
     * 
     * @param fromState The state being exited (may be null)
     * @param toState The state being entered
     */
    private void transferStateData(GameState fromState, GameState toState) {
        if (fromState == null || toState == null) {
            return;
        }
        
        // Transfer score from Play to GameOver
        if (fromState instanceof PlayState && toState instanceof GameOverState) {
            PlayState playState = (PlayState) fromState;
            GameOverState gameOverState = (GameOverState) toState;
            
            int finalScore = playState.getPlayerScore();
            gameOverState.setFinalScore(finalScore);
            gameOverState.setHighScore(this.highScore);
            
            // Update high score if needed
            if (finalScore > this.highScore) {
                this.highScore = finalScore;
                logger.info("New high score: {}", this.highScore);
            }
            
            logger.debug("Transferred score {} from Play to GameOver", finalScore);
        }
    }
    
    /**
     * Get the current active state.
     * 
     * @return The current state, or null if not initialized
     */
    public GameState getCurrentState() {
        return this.currentState;
    }
    
    /**
     * Get the current state name.
     * 
     * @return The name of the current state, or null if not initialized
     */
    public GameStateName getCurrentStateName() {
        return this.currentState != null ? this.currentState.getStateName() : null;
    }
    
    /**
     * Get the high score.
     * 
     * @return The current high score
     */
    public int getHighScore() {
        return this.highScore;
    }
    
    /**
     * Set the high score.
     * 
     * @param score The new high score
     */
    public void setHighScore(int score) {
        this.highScore = score;
        logger.info("High score set to: {}", score);
    }
    
    /**
     * Reset the game to initial state.
     * Useful for restarting the game.
     */
    public void reset() {
        logger.info("Resetting game");
        changeState(GameStateName.ATTRACT);
    }
    
    /**
     * Clean up the manager.
     * Should be called when shutting down the game.
     */
    public void destroy() {
        logger.info("Destroying GameStateManager");
        
        if (this.currentState != null) {
            this.currentState.exit();
            this.currentState = null;
        }
        
        logger.debug("GameStateManager destroyed");
    }
}