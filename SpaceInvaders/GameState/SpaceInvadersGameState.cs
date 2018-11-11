using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract public class SpaceInvadersGameState
    {
        protected GameStateManager.GameState stateName;

        // State flags
        protected bool changeState = false;
        protected bool resetLevel = false;
        
        // State-scoped managers
        protected SpriteBatchManager pSpriteBatchManager;
        protected GameObjectManager pGameObjectManager;
        protected QueuedTimeEventManager pQueuedTimeEventManager;
        protected GhostManager pGhostManager;
        protected DelayedObjectManager pDelayedObjectManager;
        protected CoreCannonManager pCoreCannonManager;
        protected CompositeManager pCompositeManager;

        // Alien Grid Speed -- Only relevant for Player states
        protected const float gameInitialAlienGridSpeed = 0.8f;
        protected float levelInitialAlienGridSpeed;
        protected float currAlienGridSpeed;

        // Alien Grid Location -- Only relevant for Player states
        protected const float gameInitialAlienGridX = 100.0f;
        protected const float gameInitialAlienGridY = 700.0f;
        protected float currLevelInitialAlienGridY;

        public SpaceInvadersGameState(GameStateManager.GameState inStateName)
        {
            this.stateName = inStateName;
        }

        // state()
        public abstract void Handle(SpaceInvaders pGame);

        // strategy()
        public abstract void LoadContent();
        public abstract void Update();
        public abstract void Draw();
        public abstract void AttachInputObservers();
        public abstract void DetachInputObservers();

        public GameStateManager.GameState GetStateName()
        {
            return this.stateName;
        }

        public void SetCurrAlienGridSpeed(float newGridSpeed)
        {
            if ((stateName == GameStateManager.GameState.Player1)
                || (stateName == GameStateManager.GameState.Player2))
            {
                this.currAlienGridSpeed = newGridSpeed;
            }
            else
            {
                Debug.Assert(false);
            }
        }

        public float GetCurrAlienGridSpeed()
        {
            float gridSpeed = -1.0f;

            if ((stateName == GameStateManager.GameState.Player1)
                || (stateName == GameStateManager.GameState.Player2))
            {
                gridSpeed = currAlienGridSpeed;
            }
            else
            {
                Debug.Assert(false);
            }

            return gridSpeed;
        }

        public void SetLevelInitialAlienGridSpeed(float newGridSpeed)
        {
            if ((stateName == GameStateManager.GameState.Player1)
                || (stateName == GameStateManager.GameState.Player2))
            {
                this.levelInitialAlienGridSpeed = newGridSpeed;
            }
            else
            {
                Debug.Assert(false);
            }
        }

        public float GetLevelInitialAlienGridSpeed()
        {
            float gridSpeed = -1.0f;

            if ((stateName == GameStateManager.GameState.Player1)
                || (stateName == GameStateManager.GameState.Player2))
            {
                gridSpeed = this.levelInitialAlienGridSpeed;
            }
            else
            {
                Debug.Assert(false);
            }

            return gridSpeed;
        }

        public float GetGameInitialGridSpeed()
        {
            return gameInitialAlienGridSpeed;
        }

        public float GetGameInitialGridXPosition()
        {
            return gameInitialAlienGridX;
        }

        public float GetGameInitialGridYPosition()
        {
            return gameInitialAlienGridY;
        }

        public void SetLevelInitialAlienGridYPos(float newYPos)
        {
            if ((stateName == GameStateManager.GameState.Player1)
                || (stateName == GameStateManager.GameState.Player2))
            {
                this.currLevelInitialAlienGridY = newYPos;
            }
            else
            {
                Debug.Assert(false);
            }
        }

        public float GetLevelInitialAlienGridYPos()
        {
            float yPos = -1.0f;

            if ((stateName == GameStateManager.GameState.Player1)
                || (stateName == GameStateManager.GameState.Player2))
            {
                yPos = this.currLevelInitialAlienGridY;
            }
            else
            {
                Debug.Assert(false);
            }

            return yPos;
        }

        public bool IsStateChangePending()
        {
            return this.changeState;
        }

        public void SetStateChangePending(bool inChangeState)
        {
            this.changeState = inChangeState;
        }

        public bool IsLevelResetPending()
        {
            return this.resetLevel;
        }

        public void SetLevelResetPending(bool inLevelReset)
        {
            this.resetLevel = inLevelReset;
        }

        public SpriteBatchManager GetSpriteBatchManager()
        {
            return pSpriteBatchManager;
        }

        public GameObjectManager GetGameObjectManager()
        {
            return pGameObjectManager;
        }

        public QueuedTimeEventManager GetQueuedTimeEventManager()
        {
            return pQueuedTimeEventManager;
        }

        public GhostManager GetGhostManager()
        {
            return pGhostManager;
        }

        public DelayedObjectManager GetDelayedObjectManager()
        {
            return pDelayedObjectManager;
        }

        public CoreCannonManager GetCoreCannonManager()
        {
            return pCoreCannonManager;
        }

        public CompositeManager GetCompositeManager()
        {
            return pCompositeManager;
        }

        public void SwitchState(SpaceInvaders pGame, GameStateManager.GameState nextState)
        {
            // Detach current state input observers
            pGame.DetachStateInputObservers();

            // Queue any Time Events for later
            TimeEvent pTimeEvent = TimerManager.Pop();
            while (pTimeEvent != null)
            {
                this.GetQueuedTimeEventManager().Enqueue(pTimeEvent.GetName(), pTimeEvent.GetCommand(), pTimeEvent.GetDeltaTime());
                pTimeEvent = TimerManager.Pop();
            }

            // Change game state
            pGame.SetGameState(nextState);

            // Load up TimerManager with next state's Time Events
            QueuedTimeEvent qte = pGame.GetStateQueuedTimeEventManager().Dequeue();
            while (qte != null)
            {

                TimerManager.Add(qte.GetTimeEventName(), qte.GetCommand(), qte.GetTimeEventDelta());

                // Get next queued event
                qte = pGame.GetStateQueuedTimeEventManager().Dequeue();
            }

            // Attach next state input observers
            pGame.AttachStateInputObservers();
        }

        public void UpdateHiScore(SpaceInvaders pGame, int inScore)
        {
            if(pGame.GetHiScore() < inScore)
            {
                pGame.SetHiScore(inScore);

                Font pHiScoreFont = FontManager.Find(Font.Name.HighScore);
                Debug.Assert(pHiScoreFont != null);
                pHiScoreFont.UpdateMessage(inScore.ToString("D4"));
            }
        }

    }
}
