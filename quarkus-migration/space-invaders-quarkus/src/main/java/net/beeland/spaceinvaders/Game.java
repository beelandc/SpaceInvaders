package net.beeland.spaceinvaders;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.beeland.spaceinvaders.font.Glyph;
import net.beeland.spaceinvaders.font.GlyphManager;
import net.beeland.spaceinvaders.shader.ShaderProgram;
import net.beeland.spaceinvaders.sprite.SpriteBatch;
import net.beeland.spaceinvaders.state.GameStateManager;
import net.beeland.spaceinvaders.texture.Texture;
import org.jboss.logging.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryUtil;

/**
 * Main Game class for Space Invaders
 * Implements the game loop using LWJGL and GLFW
 */
@QuarkusMain
@ApplicationScoped
public class Game implements QuarkusApplication {
    
    private static final Logger LOG = Logger.getLogger(Game.class);
    
    // Window dimensions
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String WINDOW_TITLE = "Space Invaders - Quarkus Edition";
    
    // Target frame rate (60 FPS)
    private static final double TARGET_FPS = 60.0;
    private static final double FRAME_TIME = 1.0 / TARGET_FPS;
    
    private long window;
    private boolean running = false;
    
    // Rendering components
    private ShaderProgram shaderProgram;
    private SpriteBatch spriteBatch;
    
    // Inject managers via CDI
    @Inject
    GameStateManager gameStateManager;
    
    @Inject
    GlyphManager glyphManager;
    
    @Inject
    net.beeland.spaceinvaders.texture.TextureManager textureManager;
    
    public static void main(String[] args) {
        Quarkus.run(Game.class, args);
    }
    
    @Override
    public int run(String... args) {
        LOG.info("Starting Space Invaders...");
        
        try {
            init();
            gameLoop();
            cleanup();
            return 0;
        } catch (Exception e) {
            LOG.error("Error running game", e);
            return 1;
        }
    }
    
    /**
     * Initialize GLFW, create window, and set up OpenGL context
     */
    private void init() {
        LOG.info("Initializing game...");
        
        // Disable GLFW thread check for Quarkus compatibility
        Configuration.GLFW_CHECK_THREAD0.set(false);
        
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();
        
        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        
        // Create window
        window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, 
                                       MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        // Setup key callback
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });
        
        // Make OpenGL context current
        GLFW.glfwMakeContextCurrent(window);
        
        // Enable v-sync
        GLFW.glfwSwapInterval(1);
        
        // Make window visible
        GLFW.glfwShowWindow(window);
        
        // Initialize OpenGL bindings
        GL.createCapabilities();
        
        // Set clear color (black background)
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        // Enable blending for transparency
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        LOG.info("Game initialized successfully");
        LOG.info("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
        LOG.info("GLSL Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
        
        // Initialize shader program
        LOG.info("Loading shader program...");
        shaderProgram = new ShaderProgram();
        if (!shaderProgram.createFromResources("shaders/sprite.vert", "shaders/sprite.frag")) {
            throw new RuntimeException("Failed to load shader program");
        }
        LOG.info("Shader program loaded successfully");
        
        // Initialize sprite batch
        LOG.info("Initializing SpriteBatch...");
        spriteBatch = new SpriteBatch();
        spriteBatch.initialize(shaderProgram.getProgramId(), WINDOW_WIDTH, WINDOW_HEIGHT);
        LOG.info("SpriteBatch initialized successfully");
        
        // Load textures and glyphs BEFORE initializing game states
        // States need these resources to be available when they create fonts
        LOG.info("Loading textures...");
        loadTextures();
        LOG.info("Textures loaded successfully");
        
        LOG.info("Loading font glyphs...");
        loadGlyphs();
        LOG.info("Font glyphs loaded successfully");
        
        // Initialize GameStateManager and start in Attract state
        if (gameStateManager != null) {
            LOG.info("Initializing GameStateManager...");
            
            // Pass window handle and managers to states BEFORE initializing
            // This ensures managers are available when enter() is called
            LOG.info("Setting window handle and managers for GameStateManager...");
            gameStateManager.setWindow(window);
            
            // Now initialize and enter the first state
            gameStateManager.initialize();
            
            LOG.info("GameStateManager initialized - Current state: " + gameStateManager.getCurrentStateName());
        } else {
            LOG.error("GameStateManager is null - CDI injection failed!");
        }
    }
    
    /**
     * Main game loop
     * Implements fixed timestep for consistent game logic updates
     */
    private void gameLoop() {
        LOG.info("Starting game loop...");
        running = true;
        
        double lastTime = GLFW.glfwGetTime();
        double accumulator = 0.0;
        int frames = 0;
        double fpsTimer = lastTime;
        
        while (running && !GLFW.glfwWindowShouldClose(window)) {
            double currentTime = GLFW.glfwGetTime();
            double deltaTime = currentTime - lastTime;
            lastTime = currentTime;
            
            accumulator += deltaTime;
            
            // Fixed timestep update
            while (accumulator >= FRAME_TIME) {
                update(FRAME_TIME);
                accumulator -= FRAME_TIME;
            }
            
            // Render
            render();
            
            // Swap buffers and poll events
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
            
            // FPS counter
            frames++;
            if (currentTime - fpsTimer >= 1.0) {
                LOG.info("FPS: " + frames + " | Game loop running");
                frames = 0;
                fpsTimer = currentTime;
            }
        }
        
        LOG.info("Game loop ended");
    }
    
    /**
     * Update game logic
     * @param deltaTime Time elapsed since last update (in seconds)
     */
    private void update(double deltaTime) {
        // Update game state manager
        if (gameStateManager != null) {
            // Handle input first
            gameStateManager.handleInput();
            
            // Update game logic (convert double to float for state manager)
            gameStateManager.update((float) deltaTime);
        }
    }
    
    /**
     * Render the game
     */
    private void render() {
        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Draw current game state using sprite batch
        if (gameStateManager != null && spriteBatch != null) {
            spriteBatch.begin();
            gameStateManager.draw(spriteBatch);
            spriteBatch.end();
        }
    }
    
    /**
     * Load texture files for fonts and sprites
     * Must be called before loading glyphs
     */
    private void loadTextures() {
        if (textureManager == null) {
            LOG.error("TextureManager is null - cannot load textures!");
            return;
        }
        
        try {
            // Load font texture atlases
            LOG.info("Loading Consolas 36pt texture atlas...");
            textureManager.add(Texture.Name.CONSOLAS_36PT, "graphics/Consolas36pt.tga");
            
            LOG.info("Loading Consolas 20pt texture atlas...");
            textureManager.add(Texture.Name.CONSOLAS_20PT, "graphics/Consolas20pt.tga");
            
            LOG.info("All textures loaded successfully");
            
        } catch (Exception e) {
            LOG.error("Failed to load textures", e);
            throw new RuntimeException("Texture loading failed", e);
        }
    }
    
    /**
     * Load font glyphs from XML definition files
     * Must be called AFTER loading textures and before initializing game states
     */
    private void loadGlyphs() {
        if (glyphManager == null) {
            LOG.error("GlyphManager is null - cannot load glyphs!");
            return;
        }
        
        try {
            // Load Consolas 20pt font glyphs FIRST
            // (Manager adds to head, so load less-used font first)
            LOG.info("Loading Consolas 20pt glyphs from graphics/Consolas20pt.xml...");
            glyphManager.loadFromXml(
                Glyph.Name.CONSOLAS_20PT,
                "graphics/Consolas20pt.xml",
                Texture.Name.CONSOLAS_20PT
            );
            LOG.info("Consolas 20pt glyphs loaded successfully");
            
            // Load Consolas 36pt font glyphs SECOND
            // (This will be at head of list, searched first)
            LOG.info("Loading Consolas 36pt glyphs from graphics/Consolas36pt.xml...");
            glyphManager.loadFromXml(
                Glyph.Name.CONSOLAS_36PT,
                "graphics/Consolas36pt.xml",
                Texture.Name.CONSOLAS_36PT
            );
            LOG.info("Consolas 36pt glyphs loaded successfully");
            
        } catch (Exception e) {
            LOG.error("Failed to load font glyphs", e);
            throw new RuntimeException("Font glyph loading failed", e);
        }
    }
    
    /**
     * Cleanup resources
     */
    private void cleanup() {
        LOG.info("Cleaning up resources...");
        
        // Cleanup GameStateManager
        if (gameStateManager != null) {
            LOG.info("Destroying GameStateManager...");
            gameStateManager.destroy();
        }
        
        // Cleanup rendering resources
        if (shaderProgram != null) {
            LOG.info("Disposing shader program...");
            shaderProgram.dispose();
        }
        
        // Note: SpriteBatch doesn't have a dispose method in current implementation
        // OpenGL resources will be cleaned up when context is destroyed
        
        // Destroy window
        if (window != MemoryUtil.NULL) {
            GLFW.glfwDestroyWindow(window);
        }
        
        // Terminate GLFW
        GLFW.glfwTerminate();
        
        // Free error callback
        GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
        
        LOG.info("Cleanup complete");
    }
}
