package net.beeland.spaceinvaders.state;

/**
 * Enumeration of all possible game states.
 * Used by the State pattern to manage different game modes.
 */
public enum GameStateName {
    /**
     * Attract mode - title screen showing game instructions
     */
    ATTRACT,
    
    /**
     * Active gameplay state
     */
    PLAY,
    
    /**
     * Game over state - shown when player loses all lives
     */
    GAME_OVER
}