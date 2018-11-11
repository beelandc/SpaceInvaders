using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class GameStateManager
    {
        private static GameStateManager instance = null;

        // Active
        private SpaceInvaders pGame;

        // Game States 
        private GameAttractState pAttractState;
        private GamePlayer1State pPlayer1State;
        private GamePlayer2State pPlayer2State;
        private GameEndState pEndState;

        public enum GameState
        {
            Attract,
            Player1,
            Player2,
            End
        }

        private GameStateManager()
        {
            // Store the states
            this.pAttractState = new GameAttractState(GameStateManager.GameState.Attract);
            this.pPlayer1State = new GamePlayer1State(GameStateManager.GameState.Player1);
            this.pPlayer2State = new GamePlayer2State(GameStateManager.GameState.Player2);
            this.pEndState = new GameEndState(GameStateManager.GameState.End);

            // set active
            this.pGame = null;
        }

        public static void Create()
        {
            // make sure its the first time
            Debug.Assert(instance == null);

            // Do the initialization
            if (instance == null)
            {
                instance = new GameStateManager();
            }

            Debug.Assert(instance != null);
        }

        public static void SetGame(SpaceInvaders inGame)
        {
            Debug.Assert(inGame != null);

            GameStateManager pInstance = GameStateManager.PrivInstance();

            pInstance.pGame = inGame;
        }

        private static GameStateManager PrivInstance()
        {
            Debug.Assert(instance != null);

            return instance;
        }

        public static SpaceInvaders GetGame()
        {
            GameStateManager pInstance = GameStateManager.PrivInstance();

            Debug.Assert(pInstance != null);
            Debug.Assert(pInstance.pGame != null);

            return pInstance.pGame;
        }

        public static SpaceInvadersGameState GetState(GameState state)
        {
            GameStateManager pInstance = GameStateManager.PrivInstance();
            Debug.Assert(pInstance != null);

            SpaceInvadersGameState pSpaceInvadersGameState = null;

            switch (state)
            {
                case GameStateManager.GameState.Attract:
                    pSpaceInvadersGameState = pInstance.pAttractState;
                    break;

                case GameStateManager.GameState.Player1:
                    pSpaceInvadersGameState = pInstance.pPlayer1State;
                    break;

                case GameStateManager.GameState.Player2:
                    pSpaceInvadersGameState = pInstance.pPlayer2State;
                    break;
                case GameStateManager.GameState.End:
                    pSpaceInvadersGameState = pInstance.pEndState;
                    break;
                default:
                    Debug.Assert(false);
                    break;
            }

            return pSpaceInvadersGameState;
        }

    }
}
