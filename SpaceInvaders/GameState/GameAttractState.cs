using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class GameAttractState : SpaceInvadersGameState
    {
        bool firstGame = true;

        public GameAttractState(GameStateManager.GameState stateName)
            : base(stateName)
        {

        }

        public override void Handle(SpaceInvaders pGame)
        {
            if (!firstGame)
            {
                // Clean Player States for new game
                this.CleanPlayerState(Player.Name.Player1);
                this.CleanPlayerState(Player.Name.Player2);

                // Clean Game States for new game 
                ResetGameCommand resetGame = new ResetGameCommand();
                ResetCoreCannonCommand resetCannon = new ResetCoreCannonCommand();

                // Reset Player1 State
                this.SwitchState(pGame, GameStateManager.GameState.Player1);
                resetGame.Execute(1.0f);
                GameStateManager.GetGame().SetStateLevelInitialAlienGridSpeed(GameStateManager.GetGame().GetGameInitialAlienGridSpeed());
                GameStateManager.GetGame().SetStateLevelInitialAlienGridYPosition(GameStateManager.GetGame().GetGameInitialGridYPosition());
                GameObject pCoreCannon = GameStateManager.GetGame().GetStateGhostManager().Find(GameObject.Name.CoreCannon);
                if (pCoreCannon != null)
                {
                    resetCannon.Execute(1.0f);
                }

                // Reset Player2 State
                this.SwitchState(pGame, GameStateManager.GameState.Player2);
                resetGame.Execute(1.0f);
                GameStateManager.GetGame().SetStateLevelInitialAlienGridSpeed(GameStateManager.GetGame().GetGameInitialAlienGridSpeed());
                GameStateManager.GetGame().SetStateLevelInitialAlienGridYPosition(GameStateManager.GetGame().GetGameInitialGridYPosition());
                pCoreCannon = GameStateManager.GetGame().GetStateGhostManager().Find(GameObject.Name.CoreCannon);
                if (pCoreCannon != null)
                {
                    resetCannon.Execute(1.0f);
                }

            } else
            {
                // Set new game flag to false
                this.firstGame = false;
            }
            
            this.SwitchState(pGame, GameStateManager.GameState.Player1);
        }

        public override void LoadContent()
        {
            // Set to current state for initialization
            GameStateManager.GetGame().SetGameState(GameStateManager.GameState.Attract);

            //---------------------------------------------------------------------------------------------------------
            // Initialize State-Scoped Managers
            //---------------------------------------------------------------------------------------------------------

            this.pSpriteBatchManager = new SpriteBatchManager(3, 1);
            this.pGameObjectManager = new GameObjectManager(10, 5);
            this.pQueuedTimeEventManager = new QueuedTimeEventManager(20, 5);
            this.pDelayedObjectManager = new DelayedObjectManager();

            //---------------------------------------------------------------------------------------------------------
            // Create Colors
            //---------------------------------------------------------------------------------------------------------

            //pGreenColor = new Azul.Color(0.1137f, 0.8196f, 0.2667f, 1.0f);

            //---------------------------------------------------------------------------------------------------------
            // Create SpriteBatches
            //---------------------------------------------------------------------------------------------------------
            SpriteBatch pTexts_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.AttractTexts);
            SpriteBatch pAlien_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Attract_Aliens);

            //---------------------------------------------------------------------------------------------------------
            // Aliens
            //---------------------------------------------------------------------------------------------------------
            FlyingSaucer pFlyingSaucer = new FlyingSaucer(GameObject.Name.FlyingSaucer, Sprite.Name.FlyingSaucer, 300, 400);
            this.pGameObjectManager.Attach(pFlyingSaucer);
            pFlyingSaucer.ActivateSprite(pAlien_SpriteBatch);

            SquidAlien pSquidAlien = new SquidAlien(GameObject.Name.SquidAlien, Sprite.Name.SquidAlien, 300, 350);
            this.pGameObjectManager.Attach(pSquidAlien);
            pSquidAlien.ActivateSprite(pAlien_SpriteBatch);

            CrabAlien pCrabAlien = new CrabAlien(GameObject.Name.CrabAlien, Sprite.Name.CrabAlien, 300, 300);
            this.pGameObjectManager.Attach(pCrabAlien);
            pCrabAlien.ActivateSprite(pAlien_SpriteBatch);

            JellyfishAlien pJellyfishAlien = new JellyfishAlien(GameObject.Name.JellyFishAlien, Sprite.Name.JellyfishAlien, 300, 250);
            this.pGameObjectManager.Attach(pJellyfishAlien);
            pJellyfishAlien.ActivateSprite(pAlien_SpriteBatch);

            //---------------------------------------------------------------------------------------------------------
            // Fonts
            //---------------------------------------------------------------------------------------------------------
            FontManager.Add(Font.Name.Attract_Play, pTexts_SpriteBatch, "PLAY", Glyph.Name.Consolas36pt, 385, 650);
            FontManager.Add(Font.Name.Attract_Title, pTexts_SpriteBatch, "SPACE INVADERS", Glyph.Name.Consolas36pt, 290, 550);
            FontManager.Add(Font.Name.Attract_ScoreAdvanceTable, pTexts_SpriteBatch, "* SCORE ADVANCE TABLE *", Glyph.Name.Consolas36pt, 225, 450);
            FontManager.Add(Font.Name.Attract_FlyingSaucerPoints, pTexts_SpriteBatch, "= ?  MYSTERY", Glyph.Name.Consolas36pt, 350, 400);
            FontManager.Add(Font.Name.Attract_SquidPoints, pTexts_SpriteBatch, "= 30  POINTS", Glyph.Name.Consolas36pt, 350, 350);
            FontManager.Add(Font.Name.Attract_CrabPoints, pTexts_SpriteBatch, "= 20  POINTS", Glyph.Name.Consolas36pt, 350, 300);
            FontManager.Add(Font.Name.Attract_JellyfishPoints, pTexts_SpriteBatch, "= 10  POINTS", Glyph.Name.Consolas36pt, 350, 250);

            FontManager.Add(Font.Name.Attract_Instruction_1P, pTexts_SpriteBatch, "PRESS 1 FOR 1-PLAYER MODE", Glyph.Name.Consolas36pt, 200, 150);
            FontManager.Add(Font.Name.Attract_Instruction_2P, pTexts_SpriteBatch, "PRESS 2 FOR 2-PLAYER MODE", Glyph.Name.Consolas36pt, 200, 115);
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

            pInputSubject = InputManager.GetKey1Subject();
            pInputSubject.Attach(new StartNewGameObserver(false));

            pInputSubject = InputManager.GetKey2Subject();
            pInputSubject.Attach(new StartNewGameObserver(true));
        }

        public override void DetachInputObservers()
        {
            InputSubject pInputSubject;

            pInputSubject = InputManager.GetKey1Subject();
            pInputSubject.DetachAllObservers();

            pInputSubject = InputManager.GetKey2Subject();
            pInputSubject.DetachAllObservers();
        }

        private void CleanPlayerState(Player.Name playerToClean)
        {
            // Clean player
            Player cleanPlayer = new Player(playerToClean);
            GameStateManager.GetGame().SetPlayer(playerToClean, cleanPlayer);

            //Find Player Points Font
            Font.Name pointsFont = Font.Name.Uninitialized;
            Font.Name livesFont = Font.Name.Uninitialized;
            switch (playerToClean)
            {
                case Player.Name.Player1:
                    pointsFont = Font.Name.Player1Score;
                    livesFont = Font.Name.Player1Lives;
                    break;
                case Player.Name.Player2:
                    pointsFont = Font.Name.Player2Score;
                    livesFont = Font.Name.Player2Lives;
                    break;
            }

            // Update Points display
            Font pPlayerScoreFont = FontManager.Find(pointsFont);
            Debug.Assert(pPlayerScoreFont != null);
            pPlayerScoreFont.UpdateMessage(cleanPlayer.GetPoints().ToString("D4"));

            // Update Lives display
            Font pPlayerLivesFont = FontManager.Find(livesFont);
            Debug.Assert(pPlayerLivesFont != null);
            pPlayerLivesFont.UpdateMessage("LIVES "+cleanPlayer.GetNumLives().ToString());
        }
    }
}
