package net.beeland.spaceinvaders.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedStatic;
import org.lwjgl.opengl.GL11;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GameState classes (AttractState, PlayState, GameOverState).
 * Tests state initialization, lifecycle, and transitions.
 */
class GameStateTest {
    
    private MockedStatic<GL11> gl11Mock;
    
    @BeforeEach
    void setUp() {
        // Mock all GL11 static methods to prevent LWJGL native library loading
        gl11Mock = mockStatic(GL11.class);
    }
    
    @AfterEach
    void tearDown() {
        if (gl11Mock != null) {
            gl11Mock.close();
        }
    }
    
    @Test
    @DisplayName("AttractState should initialize with correct state name")
    void testAttractStateInitialization() {
        AttractState state = new AttractState();
        
        assertEquals(GameStateName.ATTRACT, state.getStateName());
        assertFalse(state.isChangeStatePending());
        assertNull(state.getNextState());
    }
    
    @Test
    @DisplayName("AttractState should create managers on enter")
    void testAttractStateEnter() {
        AttractState state = new AttractState();
        
        state.enter();
        
        assertNotNull(state.getGameObjectManager());
        assertNotNull(state.getTimerManager());
    }
    
    @Test
    @DisplayName("AttractState should clean up managers on exit")
    void testAttractStateExit() {
        AttractState state = new AttractState();
        state.enter();
        
        assertNotNull(state.getGameObjectManager());
        assertNotNull(state.getTimerManager());
        
        state.exit();
        
        assertNull(state.getGameObjectManager());
        assertNull(state.getTimerManager());
    }
    
    @Test
    @DisplayName("AttractState update should not crash with null managers")
    void testAttractStateUpdateWithoutEnter() {
        AttractState state = new AttractState();
        
        // Should not throw exception
        assertDoesNotThrow(() -> state.update(0.016f));
    }
    
    @Test
    @DisplayName("AttractState should handle state transition request")
    void testAttractStateTransition() {
        AttractState state = new AttractState();
        
        state.setNextState(GameStateName.PLAY);
        
        assertTrue(state.isChangeStatePending());
        assertEquals(GameStateName.PLAY, state.getNextState());
    }
    
    @Test
    @DisplayName("PlayState should initialize with correct state name")
    void testPlayStateInitialization() {
        PlayState state = new PlayState();
        
        assertEquals(GameStateName.PLAY, state.getStateName());
        assertFalse(state.isChangeStatePending());
        assertNull(state.getNextState());
    }
    
    @Test
    @DisplayName("PlayState should initialize game variables on enter")
    void testPlayStateEnter() {
        PlayState state = new PlayState();
        
        state.enter();
        
        assertNotNull(state.getGameObjectManager());
        assertNotNull(state.getTimerManager());
        assertEquals(3, state.getPlayerLives());
        assertEquals(0, state.getPlayerScore());
        assertTrue(state.getAlienGridSpeed() > 0);
    }
    
    @Test
    @DisplayName("PlayState should clean up managers on exit")
    void testPlayStateExit() {
        PlayState state = new PlayState();
        state.enter();
        
        assertNotNull(state.getGameObjectManager());
        assertNotNull(state.getTimerManager());
        
        state.exit();
        
        assertNull(state.getGameObjectManager());
        assertNull(state.getTimerManager());
    }
    
    @Test
    @DisplayName("PlayState should track player lives")
    void testPlayStateLives() {
        PlayState state = new PlayState();
        state.enter();
        
        assertEquals(3, state.getPlayerLives());
        
        state.setPlayerLives(2);
        assertEquals(2, state.getPlayerLives());
        
        state.setPlayerLives(1);
        assertEquals(1, state.getPlayerLives());
    }
    
    @Test
    @DisplayName("PlayState should transition to game over when lives reach zero")
    void testPlayStateGameOverTransition() {
        PlayState state = new PlayState();
        state.enter();
        
        state.setPlayerLives(0);
        
        assertTrue(state.isChangeStatePending());
        assertEquals(GameStateName.GAME_OVER, state.getNextState());
    }
    
    @Test
    @DisplayName("PlayState should track player score")
    void testPlayStateScore() {
        PlayState state = new PlayState();
        state.enter();
        
        assertEquals(0, state.getPlayerScore());
        
        state.addScore(10);
        assertEquals(10, state.getPlayerScore());
        
        state.addScore(20);
        assertEquals(30, state.getPlayerScore());
        
        state.addScore(30);
        assertEquals(60, state.getPlayerScore());
    }
    
    @Test
    @DisplayName("PlayState should allow alien speed adjustment")
    void testPlayStateAlienSpeed() {
        PlayState state = new PlayState();
        state.enter();
        
        float initialSpeed = state.getAlienGridSpeed();
        assertTrue(initialSpeed > 0);
        
        state.setAlienGridSpeed(1.5f);
        assertEquals(1.5f, state.getAlienGridSpeed(), 0.001f);
        
        state.setAlienGridSpeed(2.0f);
        assertEquals(2.0f, state.getAlienGridSpeed(), 0.001f);
    }
    
    @Test
    @DisplayName("PlayState update should not crash with null managers")
    void testPlayStateUpdateWithoutEnter() {
        PlayState state = new PlayState();
        
        // Should not throw exception
        assertDoesNotThrow(() -> state.update(0.016f));
    }
    
    @Test
    @DisplayName("GameOverState should initialize with correct state name")
    void testGameOverStateInitialization() {
        GameOverState state = new GameOverState();
        
        assertEquals(GameStateName.GAME_OVER, state.getStateName());
        assertFalse(state.isChangeStatePending());
        assertNull(state.getNextState());
        assertEquals(0, state.getFinalScore());
        assertEquals(0, state.getHighScore());
        assertFalse(state.isNewHighScore());
    }
    
    @Test
    @DisplayName("GameOverState should create managers on enter")
    void testGameOverStateEnter() {
        GameOverState state = new GameOverState();
        
        state.enter();
        
        assertNotNull(state.getGameObjectManager());
        assertNotNull(state.getTimerManager());
    }
    
    @Test
    @DisplayName("GameOverState should clean up managers on exit")
    void testGameOverStateExit() {
        GameOverState state = new GameOverState();
        state.enter();
        
        assertNotNull(state.getGameObjectManager());
        assertNotNull(state.getTimerManager());
        
        state.exit();
        
        assertNull(state.getGameObjectManager());
        assertNull(state.getTimerManager());
    }
    
    @Test
    @DisplayName("GameOverState should track final score")
    void testGameOverStateFinalScore() {
        GameOverState state = new GameOverState();
        
        state.setFinalScore(1000);
        assertEquals(1000, state.getFinalScore());
        
        state.setFinalScore(2500);
        assertEquals(2500, state.getFinalScore());
    }
    
    @Test
    @DisplayName("GameOverState should track high score")
    void testGameOverStateHighScore() {
        GameOverState state = new GameOverState();
        
        state.setHighScore(500);
        assertEquals(500, state.getHighScore());
        
        state.setHighScore(1500);
        assertEquals(1500, state.getHighScore());
    }
    
    @Test
    @DisplayName("GameOverState should detect new high score on enter")
    void testGameOverStateNewHighScore() {
        GameOverState state = new GameOverState();
        
        state.setHighScore(1000);
        state.setFinalScore(1500);
        
        state.enter();
        
        assertTrue(state.isNewHighScore());
        assertEquals(1500, state.getHighScore());
    }
    
    @Test
    @DisplayName("GameOverState should not flag new high score when score is lower")
    void testGameOverStateNoNewHighScore() {
        GameOverState state = new GameOverState();
        
        state.setHighScore(2000);
        state.setFinalScore(1500);
        
        state.enter();
        
        assertFalse(state.isNewHighScore());
        assertEquals(2000, state.getHighScore());
    }
    
    @Test
    @DisplayName("GameOverState should reset new high score flag on exit")
    void testGameOverStateResetHighScoreFlag() {
        GameOverState state = new GameOverState();
        
        state.setHighScore(1000);
        state.setFinalScore(1500);
        state.enter();
        
        assertTrue(state.isNewHighScore());
        
        state.exit();
        
        assertFalse(state.isNewHighScore());
    }
    
    @Test
    @DisplayName("GameOverState update should not crash with null managers")
    void testGameOverStateUpdateWithoutEnter() {
        GameOverState state = new GameOverState();
        
        // Should not throw exception
        assertDoesNotThrow(() -> state.update(0.016f));
    }
    
    @Test
    @DisplayName("All states should support change state pending flag")
    void testChangeStatePendingFlag() {
        AttractState attract = new AttractState();
        PlayState play = new PlayState();
        GameOverState gameOver = new GameOverState();
        
        // Initially false
        assertFalse(attract.isChangeStatePending());
        assertFalse(play.isChangeStatePending());
        assertFalse(gameOver.isChangeStatePending());
        
        // Can be set
        attract.setChangeStatePending(true);
        play.setChangeStatePending(true);
        gameOver.setChangeStatePending(true);
        
        assertTrue(attract.isChangeStatePending());
        assertTrue(play.isChangeStatePending());
        assertTrue(gameOver.isChangeStatePending());
        
        // Can be cleared
        attract.setChangeStatePending(false);
        play.setChangeStatePending(false);
        gameOver.setChangeStatePending(false);
        
        assertFalse(attract.isChangeStatePending());
        assertFalse(play.isChangeStatePending());
        assertFalse(gameOver.isChangeStatePending());
    }
}