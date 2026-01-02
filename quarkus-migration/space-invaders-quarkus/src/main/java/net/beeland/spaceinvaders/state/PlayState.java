package net.beeland.spaceinvaders.state;

import net.beeland.spaceinvaders.collision.CollisionPair;
import net.beeland.spaceinvaders.collision.CollisionPairManager;
import net.beeland.spaceinvaders.collision.observer.*;
import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.font.Font;
import net.beeland.spaceinvaders.font.FontManager;
import net.beeland.spaceinvaders.font.Glyph;
import net.beeland.spaceinvaders.font.GlyphManager;
import net.beeland.spaceinvaders.gameobject.*;
import net.beeland.spaceinvaders.timer.TimerManager;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Play State - Active gameplay.
 * This state manages the main game loop where the player fights aliens.
 *
 * Responsibilities:
 * - Manage player ship, aliens, missiles, bombs, and shields
 * - Process collisions between game objects
 * - Handle player input (movement, shooting)
 * - Update game score and lives
 * - Transition to GameOverState when player loses all lives
 * - Transition to next level when all aliens are destroyed
 */
public class PlayState extends GameState {
    
    private static final Logger logger = LoggerFactory.getLogger(PlayState.class);
    
    // Game configuration
    private static final float INITIAL_ALIEN_SPEED = 0.8f;
    private static final float INITIAL_ALIEN_GRID_X = 100.0f;
    private static final float INITIAL_ALIEN_GRID_Y = 700.0f;
    private static final float ALIEN_X_DELTA = 10.0f;
    private static final float ALIEN_Y_DELTA = 0.0f;
    
    // Font manager for UI text
    private FontManager fontManager;
    private GlyphManager glyphManager;
    
    // Collision pair manager for collision detection
    private CollisionPairManager collisionPairManager;
    
    // Window handle for input
    private long window;
    
    // Game state variables
    private int playerLives;
    private int playerScore;
    private int highScore;
    private float alienGridSpeed;
    private float alienGridX;
    private float alienGridY;
    
    // Game object composites
    private AlienGrid alienGrid;
    private CoreCannonGroup coreCannonGroup;
    private MissileGroup missileGroup;
    private BombRoot bombRoot;
    private FlyingSaucerRoot flyingSaucerRoot;
    private ShieldGroup shieldGroup;
    private WallGroup wallGroup;
    
    // MVP: Timer for auto-transition to game over
    private float playTimer;
    private static final float PLAY_DURATION = 5.0f; // 5 seconds for MVP demo
    
    /**
     * Constructor for PlayState.
     */
    public PlayState() {
        super(GameStateName.PLAY);
        this.fontManager = null;
        this.glyphManager = null;
        this.highScore = 0;
        this.playTimer = 0.0f;
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
     * Initialize the play state.
     * Sets up the game world with all game objects.
     */
    @Override
    public void enter() {
        logger.info("Entering Play State");
        
        // Verify managers are set (warn only for testing compatibility)
        if (this.fontManager == null) {
            logger.warn("FontManager not set - skipping font creation (test mode?)");
        }
        
        if (this.glyphManager == null) {
            logger.warn("GlyphManager not set - fonts will not render (test mode?)");
        }
        
        // Initialize game variables
        this.playerLives = 3;
        this.playerScore = 0;
        this.alienGridSpeed = INITIAL_ALIEN_SPEED;
        this.alienGridX = INITIAL_ALIEN_GRID_X;
        this.alienGridY = INITIAL_ALIEN_GRID_Y;
        this.playTimer = 0.0f;
        
        // Create state-scoped managers (not CDI-managed, state-specific instances)
        this.gameObjectManager = new GameObjectManager();
        this.timerManager = new TimerManager();
        this.timerManager.init();
        
        //---------------------------------------------------------------------------------------------------------
        // MVP: Create Placeholder UI (Y coordinates converted to bottom-left origin)
        //---------------------------------------------------------------------------------------------------------
        if (this.fontManager != null && this.glyphManager != null) {
            logger.info("Creating MVP placeholder UI for PlayState with GlyphManager");
            
            // Large "PLAY STATE" title - Original Y=500 -> new Y=100 (near bottom)
            Font titleFont = fontManager.add(Font.Name.ATTRACT_TITLE, "PLAY STATE",
                           Glyph.Name.CONSOLAS_36PT, 320.0f, 100.0f);
            setGlyphManagerOnFont(titleFont);
            
            // Instructions - Original Y=400 -> new Y=200
            Font instructionFont = fontManager.add(Font.Name.ATTRACT_PLAY, "Press ESC to return to Attract",
                           Glyph.Name.CONSOLAS_36PT, 200.0f, 200.0f);
            setGlyphManagerOnFont(instructionFont);
            
            // Timer - Original Y=350 -> new Y=250
            Font timerFont = fontManager.add(Font.Name.ATTRACT_INSTRUCTION_1P, "Auto-transition in 5 sec",
                           Glyph.Name.CONSOLAS_36PT, 250.0f, 250.0f);
            setGlyphManagerOnFont(timerFont);
        } else {
            logger.info("Skipping font creation - managers not available (test mode)");
        }
        
        // Skip full game object creation for MVP
        logger.info("MVP: PlayState initialized");
    }
    
    /* FULL IMPLEMENTATION COMMENTED OUT FOR MVP - uncomment when ready for full game
    
    private void enterFullImplementation() {
        
        //---------------------------------------------------------------------------------------------------------
        // Create Walls (Boundaries)
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating walls");
        wallGroup = new WallGroup(GameObject.GameObjectName.SHIELD_GROUP, 0.0f, 0.0f);
        
        LeftWall leftWall = new LeftWall(GameObject.GameObjectName.LEFT_WALL, 20.0f, 425.0f, 15.0f, 800.0f);
        RightWall rightWall = new RightWall(GameObject.GameObjectName.RIGHT_WALL, 875.0f, 425.0f, 15.0f, 800.0f);
        TopWall topWall = new TopWall(GameObject.GameObjectName.TOP_WALL, 450.0f, 825.0f, 870.0f, 15.0f);
        BottomWall bottomWall = new BottomWall(GameObject.GameObjectName.BOTTOM_WALL, 450.0f, 55.0f, 870.0f, 15.0f);
        LeftBumper leftBumper = new LeftBumper(GameObject.GameObjectName.LEFT_BUMPER, 45.0f, 100.0f, 30.0f, 50.0f);
        RightBumper rightBumper = new RightBumper(GameObject.GameObjectName.RIGHT_BUMPER, 850.0f, 100.0f, 30.0f, 50.0f);
        
        wallGroup.add(leftWall);
        wallGroup.add(rightWall);
        wallGroup.add(topWall);
        wallGroup.add(bottomWall);
        wallGroup.add(leftBumper);
        wallGroup.add(rightBumper);
        
        gameObjectManager.attach(wallGroup);
        gameObjectManager.attach(leftWall);
        gameObjectManager.attach(rightWall);
        gameObjectManager.attach(topWall);
        gameObjectManager.attach(bottomWall);
        gameObjectManager.attach(leftBumper);
        gameObjectManager.attach(rightBumper);
        
        //---------------------------------------------------------------------------------------------------------
        // Create Shields (4 shields)
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating shields");
        shieldGroup = ShieldFactory.createAllShields();
        gameObjectManager.attach(shieldGroup);
        
        //---------------------------------------------------------------------------------------------------------
        // Create Alien Grid (11 columns x 5 rows = 55 aliens)
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating alien grid");
        alienGrid = new AlienGrid(Composite.CompositeName.ALIEN_GRID, ALIEN_X_DELTA, ALIEN_Y_DELTA);
        
        // Create 11 columns of aliens
        float alienStartX = INITIAL_ALIEN_GRID_X;
        float alienStartY = INITIAL_ALIEN_GRID_Y;
        float alienSpacingX = 50.0f;
        float alienSpacingY = 40.0f;
        
        Composite.CompositeName[] columnNames = {
            Composite.CompositeName.ALIEN_COL_1, Composite.CompositeName.ALIEN_COL_2,
            Composite.CompositeName.ALIEN_COL_3, Composite.CompositeName.ALIEN_COL_4,
            Composite.CompositeName.ALIEN_COL_5, Composite.CompositeName.ALIEN_COL_6,
            Composite.CompositeName.ALIEN_COL_7, Composite.CompositeName.ALIEN_COL_8,
            Composite.CompositeName.ALIEN_COL_9, Composite.CompositeName.ALIEN_COL_10,
            Composite.CompositeName.ALIEN_COL_11
        };
        
        for (int col = 0; col < 11; col++) {
            AlienGridColumn column = new AlienGridColumn(columnNames[col]);
            
            float colX = alienStartX + (col * alienSpacingX);
            
            // Row 0: Squid (top row)
            SquidAlien squid = new SquidAlien(colX, alienStartY);
            column.add(squid);
            gameObjectManager.attach(squid);
            
            // Rows 1-2: Crab (middle rows)
            for (int row = 1; row <= 2; row++) {
                CrabAlien crab = new CrabAlien(colX, alienStartY - (row * alienSpacingY));
                column.add(crab);
                gameObjectManager.attach(crab);
            }
            
            // Rows 3-4: Jellyfish (bottom rows)
            for (int row = 3; row <= 4; row++) {
                JellyfishAlien jellyfish = new JellyfishAlien(colX, alienStartY - (row * alienSpacingY));
                column.add(jellyfish);
                gameObjectManager.attach(jellyfish);
            }
            
            alienGrid.add(column);
            gameObjectManager.attach(column);
        }
        
        gameObjectManager.attach(alienGrid);
        
        //---------------------------------------------------------------------------------------------------------
        // Create Core Cannon (Player Ship)
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating core cannon");
        coreCannonGroup = new CoreCannonGroup(3); // 3 lives
        
        CoreCannon coreCannon = new CoreCannon(GameObject.GameObjectName.CORE_CANNON, 400.0f, 100.0f);
        coreCannonGroup.add(coreCannon);
        gameObjectManager.attach(coreCannon);
        gameObjectManager.attach(coreCannonGroup);
        
        //---------------------------------------------------------------------------------------------------------
        
        // TODO: Schedule timer events (alien movement, bomb drops, flying saucer)
        
        logger.debug("Play State initialized - Lives: {}, Score: {}", playerLives, playerScore);
    }
    */ // END FULL IMPLEMENTATION
    
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
    
    /* CONTINUATION OF FULL IMPLEMENTATION - COMMENTED OUT
    private void continueFullImplementation() {
        //---------------------------------------------------------------------------------------------------------
        // Create Missile Group
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating missile group");
        missileGroup = new MissileGroup();
        gameObjectManager.attach(missileGroup);
        
        //---------------------------------------------------------------------------------------------------------
        // Create Bomb Root
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating bomb root");
        bombRoot = new BombRoot(Composite.CompositeName.BOMB_ROOT);
        gameObjectManager.attach(bombRoot);
        
        //---------------------------------------------------------------------------------------------------------
        // Create Flying Saucer Root
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating flying saucer root");
        flyingSaucerRoot = new FlyingSaucerRoot(Composite.CompositeName.FLYING_SAUCER_ROOT);
        gameObjectManager.attach(flyingSaucerRoot);
        
        //---------------------------------------------------------------------------------------------------------
        // Create UI Fonts
        //---------------------------------------------------------------------------------------------------------
        logger.info("Creating UI fonts");
        
        // Player score
        fontManager.add(Font.Name.PLAYER1_SCORE, String.format("%04d", playerScore),
                       Glyph.Name.CONSOLAS_36PT, 50.0f, 750.0f);
        
        // High score
        fontManager.add(Font.Name.HIGH_SCORE, String.format("%04d", highScore),
                       Glyph.Name.CONSOLAS_36PT, 400.0f, 750.0f);
        
        // Lives
        fontManager.add(Font.Name.PLAYER1_LIVES, "LIVES " + playerLives,
                       Glyph.Name.CONSOLAS_36PT, 50.0f, 50.0f);
        
        // Credits
        fontManager.add(Font.Name.GAME_CREDITS, "CREDIT 00",
                       Glyph.Name.CONSOLAS_36PT, 700.0f, 50.0f);
        
        //---------------------------------------------------------------------------------------------------------
        // Set up Collision Pairs
        //---------------------------------------------------------------------------------------------------------
        logger.info("Setting up collision pairs");
        setupCollisionPairs();
        
        // TODO: Schedule timer events (alien movement, bomb drops, flying saucer)
        
        logger.debug("Play State initialized - Lives: {}, Score: {}", playerLives, playerScore);
    }
    */ // END FULL IMPLEMENTATION
    
    /**
     * Update the play state.
     * Processes game logic, collisions, and state transitions.
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    @Override
    public void update(float deltaTime) {
        // Handle input
        handleInput();
        
        // MVP: Auto-transition to game over after timer expires
        playTimer += deltaTime;
        if (playTimer >= PLAY_DURATION) {
            logger.info("MVP: Play timer expired, transitioning to Game Over");
            setNextState(GameStateName.GAME_OVER);
        }
        
        // Update timers (alien movement, bomb drops, etc.)
        if (this.timerManager != null) {
            this.timerManager.update(deltaTime);
        }
        
        // Update all game objects
        if (this.gameObjectManager != null) {
            this.gameObjectManager.update();
        }
        
        // Process collisions (disabled for MVP)
        // if (this.collisionPairManager != null) {
        //     this.collisionPairManager.process();
        // }
        
        // Check for state change
        if (this.changeStatePending) {
            logger.info("State change pending from Play to {}", this.nextState);
        }
    }
    
    /**
     * Render the play state.
     * Draws all game objects and UI elements.
     */
    @Override
    public void draw(net.beeland.spaceinvaders.sprite.SpriteBatch spriteBatch) {
        // Render all fonts (score, lives, etc.) using the sprite batch
        if (fontManager != null && spriteBatch != null) {
            fontManager.renderAll(spriteBatch);
        }
        
        // Game objects will be drawn by sprite batches
        // TODO: Integrate with sprite batch rendering system
        if (this.gameObjectManager != null) {
            // Sprite batches handle rendering
        }
    }
    
    /**
     * Clean up the play state.
     * Releases resources when exiting this state.
     */
    @Override
    public void exit() {
        logger.info("Exiting Play State - Final Score: {}", playerScore);
        
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
        
        // Clean up collision pair manager
        if (this.collisionPairManager != null) {
            this.collisionPairManager = null;
        }
        
    }
    
    /**
     * Handle input for the play state.
     * Processes player movement and shooting.
     */
    @Override
    public void handleInput() {
        if (window == 0 || coreCannonGroup == null) {
            return;
        }
        
        // Get the core cannon from the group
        CoreCannon coreCannon = (CoreCannon) coreCannonGroup.getFirstChild();
        if (coreCannon == null) {
            return;
        }
        
        // Left arrow: move player left
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            coreCannon.moveLeft();
        }
        
        // Right arrow: move player right
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            coreCannon.moveRight();
        }
        
        // Space bar: fire missile
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            fireMissile();
        }
        
        // ESC: return to attract mode
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            logger.info("ESC pressed - returning to attract state");
            setNextState(GameStateName.ATTRACT);
        }
    }
    
    /**
     * Fire a missile from the player's ship
     */
    private void fireMissile() {
        if (coreCannonGroup == null || missileGroup == null) {
            return;
        }
        
        CoreCannon coreCannon = (CoreCannon) coreCannonGroup.getFirstChild();
        if (coreCannon == null) {
            return;
        }
        
        // Create missile at cannon position
        float missileX = coreCannon.getX();
        float missileY = coreCannon.getY() + 20.0f;  // Offset above cannon
        
        Missile missile = new Missile(GameObject.GameObjectName.MISSILE, missileX, missileY);
        missileGroup.add(missile);
        gameObjectManager.attach(missile);
        
    }
    
    /**
     * Get the player's current lives.
     * 
     * @return Number of lives remaining
     */
    public int getPlayerLives() {
        return this.playerLives;
    }
    
    /**
     * Set the player's lives.
     * 
     * @param lives Number of lives
     */
    public void setPlayerLives(int lives) {
        this.playerLives = lives;
        if (this.playerLives <= 0) {
            // Trigger game over
            setNextState(GameStateName.GAME_OVER);
        }
    }
    
    /**
     * Get the player's current score.
     * 
     * @return Current score
     */
    public int getPlayerScore() {
        return this.playerScore;
    }
    
    /**
     * Add points to the player's score.
     *
     * @param points Points to add
     */
    public void addScore(int points) {
        this.playerScore += points;
        
        // Update score display
        if (fontManager != null) {
            fontManager.updateMessage(Font.Name.PLAYER1_SCORE, String.format("%04d", playerScore));
        }
        
        // Check for new high score
        if (playerScore > highScore) {
            highScore = playerScore;
            if (fontManager != null) {
                fontManager.updateMessage(Font.Name.HIGH_SCORE, String.format("%04d", highScore));
            }
        }
        
    }
    
    /**
     * Get the current alien grid speed.
     * 
     * @return Alien movement speed
     */
    public float getAlienGridSpeed() {
        return this.alienGridSpeed;
    }
    
    /**
     * Set the alien grid speed.
     * Speed increases as aliens are destroyed.
     *
     * @param speed New movement speed
     */
    public void setAlienGridSpeed(float speed) {
        this.alienGridSpeed = speed;
    }
    
    /**
     * Set up all collision pairs for the game.
     * This creates collision detection between different game object groups
     * and attaches appropriate observers to handle collision responses.
     */
    private void setupCollisionPairs() {
        // Create collision pair manager for this state
        this.collisionPairManager = new CollisionPairManager();
        
        //---------------------------------------------------------------------------------------------------------
        // 1. Alien vs Missile
        //---------------------------------------------------------------------------------------------------------
        CollisionPair alienMissilePair = collisionPairManager.add(
            CollisionPair.Name.ALIEN_MISSILE, alienGrid, missileGroup);
        alienMissilePair.attach(new RemoveMissileObserver());
        alienMissilePair.attach(new RemoveAlienObserver());
        alienMissilePair.attach(new AddPlayerPointsObserver());
        logger.debug("Collision pair added: Alien vs Missile");
        
        //---------------------------------------------------------------------------------------------------------
        // 2. FlyingSaucer vs Missile
        //---------------------------------------------------------------------------------------------------------
        CollisionPair flyingSaucerMissilePair = collisionPairManager.add(
            CollisionPair.Name.FLYING_SAUCER_MISSILE, flyingSaucerRoot, missileGroup);
        flyingSaucerMissilePair.attach(new RemoveMissileObserver());
        flyingSaucerMissilePair.attach(new RemoveFlyingSaucerObserver());
        flyingSaucerMissilePair.attach(new AddPlayerPointsObserver());
        flyingSaucerMissilePair.attach(new ExplosionSoundObserver());
        logger.debug("Collision pair added: FlyingSaucer vs Missile");
        
        //---------------------------------------------------------------------------------------------------------
        // 3. FlyingSaucer vs Wall
        //---------------------------------------------------------------------------------------------------------
        CollisionPair flyingSaucerWallPair = collisionPairManager.add(
            CollisionPair.Name.FLYING_SAUCER_WALL, flyingSaucerRoot, wallGroup);
        flyingSaucerWallPair.attach(new RemoveFlyingSaucerObserver());
        logger.debug("Collision pair added: FlyingSaucer vs Wall");
        
        //---------------------------------------------------------------------------------------------------------
        // 4. Alien vs Wall
        //---------------------------------------------------------------------------------------------------------
        CollisionPair alienWallPair = collisionPairManager.add(
            CollisionPair.Name.ALIEN_WALL, alienGrid, wallGroup);
        alienWallPair.attach(new GridWallCollisionObserver());
        logger.debug("Collision pair added: Alien vs Wall");
        
        //---------------------------------------------------------------------------------------------------------
        // 5. Alien vs Shield
        //---------------------------------------------------------------------------------------------------------
        CollisionPair alienShieldPair = collisionPairManager.add(
            CollisionPair.Name.ALIEN_SHIELD, alienGrid, shieldGroup);
        alienShieldPair.attach(new RemoveShieldBrickObserver());
        logger.debug("Collision pair added: Alien vs Shield");
        
        //---------------------------------------------------------------------------------------------------------
        // 6. Missile vs Wall
        //---------------------------------------------------------------------------------------------------------
        CollisionPair missileWallPair = collisionPairManager.add(
            CollisionPair.Name.MISSILE_WALL, missileGroup, wallGroup);
        missileWallPair.attach(new RemoveMissileObserver());
        logger.debug("Collision pair added: Missile vs Wall");
        
        //---------------------------------------------------------------------------------------------------------
        // 7. Missile vs Shield
        //---------------------------------------------------------------------------------------------------------
        CollisionPair missileShieldPair = collisionPairManager.add(
            CollisionPair.Name.MISSILE_SHIELD, missileGroup, shieldGroup);
        missileShieldPair.attach(new RemoveMissileObserver());
        missileShieldPair.attach(new RemoveShieldBrickObserver());
        logger.debug("Collision pair added: Missile vs Shield");
        
        //---------------------------------------------------------------------------------------------------------
        // 8. Bomb vs Shield
        //---------------------------------------------------------------------------------------------------------
        CollisionPair bombShieldPair = collisionPairManager.add(
            CollisionPair.Name.BOMB_SHIELD, bombRoot, shieldGroup);
        bombShieldPair.attach(new RemoveBombObserver());
        bombShieldPair.attach(new RemoveShieldBrickObserver());
        logger.debug("Collision pair added: Bomb vs Shield");
        
        //---------------------------------------------------------------------------------------------------------
        // 9. Bomb vs Wall
        //---------------------------------------------------------------------------------------------------------
        CollisionPair bombWallPair = collisionPairManager.add(
            CollisionPair.Name.BOMB_WALL, bombRoot, wallGroup);
        bombWallPair.attach(new RemoveBombObserver());
        logger.debug("Collision pair added: Bomb vs Wall");
        
        //---------------------------------------------------------------------------------------------------------
        // 10. Bomb vs Missile
        //---------------------------------------------------------------------------------------------------------
        CollisionPair bombMissilePair = collisionPairManager.add(
            CollisionPair.Name.BOMB_MISSILE, bombRoot, missileGroup);
        bombMissilePair.attach(new RemoveBombObserver());
        bombMissilePair.attach(new RemoveMissileObserver());
        logger.debug("Collision pair added: Bomb vs Missile");
        
        //---------------------------------------------------------------------------------------------------------
        // 11. Bomb vs CoreCannon (Player Death)
        //---------------------------------------------------------------------------------------------------------
        CollisionPair bombShipPair = collisionPairManager.add(
            CollisionPair.Name.BOMB_SHIP, bombRoot, coreCannonGroup);
        bombShipPair.attach(new PlayerDeathObserver());
        bombShipPair.attach(new RemoveBombObserver());
        bombShipPair.attach(new ExplosionSoundObserver());
        logger.debug("Collision pair added: Bomb vs CoreCannon");
        
        //---------------------------------------------------------------------------------------------------------
        // 12. CoreCannon vs Bumpers (Screen Edge)
        //---------------------------------------------------------------------------------------------------------
        CollisionPair coreCannonWallPair = collisionPairManager.add(
            CollisionPair.Name.CORE_CANNON_WALL, coreCannonGroup, wallGroup);
        coreCannonWallPair.attach(new CoreCannonBumperCollisionObserver());
        logger.debug("Collision pair added: CoreCannon vs Wall");
        
        logger.info("All collision pairs set up successfully - {} pairs total", 12);
    }
}