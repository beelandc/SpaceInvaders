package net.beeland.spaceinvaders.state;

import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.sprite.SpriteBatch;
import net.beeland.spaceinvaders.timer.TimerManager;

/**
 * Abstract base class for game states using the State pattern.
 * Each concrete state manages its own game objects, rendering, and input handling.
 * 
 * Design Pattern: State Pattern
 * - Context: GameStateManager
 * - State: GameState (this class)
 * - ConcreteStates: AttractState, PlayState, GameOverState
 */
public abstract class GameState {
    
    protected final GameStateName stateName;
    protected boolean changeStatePending;
    protected GameStateName nextState;
    
    // State-scoped managers (each state has its own instances)
    protected GameObjectManager gameObjectManager;
    protected TimerManager timerManager;
    
    /**
     * Constructor for GameState.
     * 
     * @param stateName The name/type of this state
     */
    protected GameState(GameStateName stateName) {
        this.stateName = stateName;
        this.changeStatePending = false;
        this.nextState = null;
    }
    
    /**
     * Get the name of this state.
     * 
     * @return The state name
     */
    public GameStateName getStateName() {
        return this.stateName;
    }
    
    /**
     * Check if a state change is pending.
     * 
     * @return true if state should change, false otherwise
     */
    public boolean isChangeStatePending() {
        return this.changeStatePending;
    }
    
    /**
     * Set the state change flag.
     * 
     * @param pending true to request state change, false otherwise
     */
    public void setChangeStatePending(boolean pending) {
        this.changeStatePending = pending;
    }
    
    /**
     * Get the next state to transition to.
     * 
     * @return The next state name, or null if no transition pending
     */
    public GameStateName getNextState() {
        return this.nextState;
    }
    
    /**
     * Set the next state to transition to.
     * 
     * @param nextState The state to transition to
     */
    public void setNextState(GameStateName nextState) {
        this.nextState = nextState;
        this.changeStatePending = true;
    }
    
    /**
     * Get the state's GameObject manager.
     * 
     * @return The GameObject manager for this state
     */
    public GameObjectManager getGameObjectManager() {
        return this.gameObjectManager;
    }
    
    /**
     * Get the state's Timer manager.
     * 
     * @return The Timer manager for this state
     */
    public TimerManager getTimerManager() {
        return this.timerManager;
    }
    
    /**
     * Initialize the state.
     * Called when entering this state.
     * Should create managers, load resources, and set up game objects.
     */
    public abstract void enter();
    
    /**
     * Update the state.
     * Called every frame while this state is active.
     * Should update game logic, process input, and handle collisions.
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    public abstract void update(float deltaTime);
    
    /**
     * Render the state.
     * Called every frame to draw the state's visuals.
     *
     * @param spriteBatch The sprite batch to use for rendering
     */
    public abstract void draw(SpriteBatch spriteBatch);
    
    /**
     * Clean up the state.
     * Called when exiting this state.
     * Should release resources and clean up managers.
     */
    public abstract void exit();
    
    /**
     * Handle input for this state.
     * Called to process keyboard/mouse input specific to this state.
     */
    public abstract void handleInput();
}