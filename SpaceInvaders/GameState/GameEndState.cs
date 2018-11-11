using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class GameEndState : SpaceInvadersGameState
    {
        public GameEndState(GameStateManager.GameState stateName)
            : base(stateName)
        {

        }

        public override void Handle(SpaceInvaders pGame)
        {
            this.SwitchState(pGame, GameStateManager.GameState.Attract);
        }

        public override void LoadContent()
        {
            // Set to current state for initialization
            GameStateManager.GetGame().SetGameState(GameStateManager.GameState.End);

            //---------------------------------------------------------------------------------------------------------
            // Initialize State-Scoped Managers
            //---------------------------------------------------------------------------------------------------------

            this.pSpriteBatchManager = new SpriteBatchManager(3, 1);
            this.pGameObjectManager = new GameObjectManager(10, 5);
            this.pQueuedTimeEventManager = new QueuedTimeEventManager(20, 5);
            this.pDelayedObjectManager = new DelayedObjectManager();

            //---------------------------------------------------------------------------------------------------------
            // Create SpriteBatches
            //---------------------------------------------------------------------------------------------------------
            SpriteBatch pTexts_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.EndTexts);
            SpriteBatch pAlien_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Attract_Aliens);

            //---------------------------------------------------------------------------------------------------------
            // Aliens
            //---------------------------------------------------------------------------------------------------------
            CrabAlien pCrabAlien = new CrabAlien(GameObject.Name.CrabAlien, Sprite.Name.GiantCrabAlien, 450, 450);
            this.pGameObjectManager.Attach(pCrabAlien);
            pCrabAlien.ActivateSprite(pAlien_SpriteBatch);

            //---------------------------------------------------------------------------------------------------------
            // Fonts
            //---------------------------------------------------------------------------------------------------------

            

            FontManager.Add(Font.Name.End_GameOver, pTexts_SpriteBatch, "GAME OVER", Glyph.Name.Consolas36pt, 355, 350);
            FontManager.Add(Font.Name.End_Instructions, pTexts_SpriteBatch, "PRESS SPACE TO RESTART", Glyph.Name.Consolas36pt, 225, 250);
            
        }

        //-----------------------------------------------------------------------------
        // Game::Update()
        //      Called once per frame, update data, tranformations, etc
        //      Use this function to control process order
        //      Input, AI, Physics, Animation, and Graphics
        //-----------------------------------------------------------------------------
        public override void Update()
        {
            // Sound Engine update - keeps everything moving and updating smoothly
            SoundEngineManager.GetSoundEngine().Update();

            // Input
            InputManager.Update();

            // Fire off the timer events
            TimerManager.Update(GameStateManager.GetGame().GetTime());

            // Update all objects
            this.pGameObjectManager.Update();

            // Process Collisions
            CollisionPairManager.Process();

            // Delete any objects here
            this.pDelayedObjectManager.Process();

            // Chage State if flag is true
            if (this.changeState)
            {
                this.Handle(GameStateManager.GetGame());
                this.changeState = false;
            }
        }

        //-----------------------------------------------------------------------------
        // Game::Draw()
        //		This function is called once per frame
        //	    Use this for draw graphics to the screen.
        //      Only do rendering here
        //-----------------------------------------------------------------------------
        public override void Draw()
        {
            // draw all objects
            this.pSpriteBatchManager.Draw();

        }

        public override void AttachInputObservers()
        {
            InputSubject pInputSubject;

            pInputSubject = InputManager.GetSpaceSubject();
            pInputSubject.Attach(new ChangeGameStateObserver());

        }

        public override void DetachInputObservers()
        {
            InputSubject pInputSubject;

            pInputSubject = InputManager.GetSpaceSubject();
            pInputSubject.DetachAllObservers();
        }
    }
}
