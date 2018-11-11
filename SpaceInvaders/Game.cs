using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpaceInvaders : Azul.Game
    {
        // Game State Attributes
        private SpaceInvadersGameState pGameState;
        private int hiScore;
        private int numCredits;
        private bool twoPlayerMode;
        private Player player1;
        private Player player2;
        private SpriteBatchManager pGameSpriteBatchManager;
        private SpriteBatch pGameTexts_SpriteBatch;

        // Colors
        Azul.Color pRedColor;
        Azul.Color pGreenColor;
        
        //-----------------------------------------------------------------------------
        // Game::Initialize()
        //		Allows the engine to perform any initialization it needs to before 
        //      starting to run.  This is where it can query for any required services 
        //      and load any non-graphic related content. 
        //-----------------------------------------------------------------------------
        public override void Initialize()
        {
            // Game Window Device setup
            this.SetWindowName("Cecil Beeland - Space Invaders");
            this.SetWidthHeight(896, 1024);

            // Set Background Color to Black
            this.SetClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }

        //-----------------------------------------------------------------------------
        // Game::LoadContent()
        //		Allows you to load all content needed for your engine,
        //	    such as objects, graphics, etc.
        //-----------------------------------------------------------------------------
        public override void LoadContent()
        {
            // Initialize Game State Attributes
            hiScore = 0;
            numCredits = 0 ;
            twoPlayerMode = false;
            player1 = new Player(Player.Name.Player1);
            player2 = new Player(Player.Name.Player2);

            //---------------------------------------------------------------------------------------------------------
            // Manager initialization
            //---------------------------------------------------------------------------------------------------------
            TextureManager.Create(3, 1);
            ImageManager.Create(10, 2);
            SpriteManager.Create(10, 2);
            SpriteBoxManager.Create(20, 2);
            SpriteProxyManager.Create(50, 5);
            SpriteAnimationManager.Create(10, 2);
            TimerManager.Create(50, 10);
            CollisionPairManager.Create(10, 2);
            GlyphManager.Create(3, 1);
            FontManager.Create(5, 1);
            SoundEngineManager.Create();
            //---------------------------------------------------------------------------------------------------------
            // Initialize Game-Scoped Non-Singleton Managers
            //---------------------------------------------------------------------------------------------------------

            this.pGameSpriteBatchManager = new SpriteBatchManager(3, 1);

            //---------------------------------------------------------------------------------------------------------
            // Create Colors
            //---------------------------------------------------------------------------------------------------------

            pRedColor = new Azul.Color(0.9019f, 0.0784f, 0.0784f, 1.0f);
            pGreenColor = new Azul.Color(0.1137f, 0.8196f, 0.2667f, 1.0f);

            //---------------------------------------------------------------------------------------------------------
            // Load the Textures
            //---------------------------------------------------------------------------------------------------------

            TextureManager.Add(Texture.Name.Aliens, "Aliens.tga");
            TextureManager.Add(Texture.Name.Shield, "Shield.tga");
            TextureManager.Add(Texture.Name.Consolas20pt, "Consolas20pt.tga");
            TextureManager.Add(Texture.Name.Consolas36pt, "Consolas36pt.tga");

            //---------------------------------------------------------------------------------------------------------
            // Load the Fonts 
            //---------------------------------------------------------------------------------------------------------

            FontManager.AddXml(Glyph.Name.Consolas20pt, "Consolas20pt.xml", Texture.Name.Consolas20pt);
            FontManager.AddXml(Glyph.Name.Consolas36pt, "Consolas36pt.xml", Texture.Name.Consolas36pt);

            //---------------------------------------------------------------------------------------------------------
            // Create Images
            //---------------------------------------------------------------------------------------------------------

            // Aliens
            ImageManager.Add(Image.Name.OpenJellyfishAlien, Texture.Name.Aliens, 47.0f, 23.0f, 185.0f, 121.0f);
            ImageManager.Add(Image.Name.ClosedJellyfishAlien, Texture.Name.Aliens, 51.0f, 178.0f, 178.0f, 120.0f);
            ImageManager.Add(Image.Name.OpenCrabAlien, Texture.Name.Aliens, 316.0f, 24.0f, 167.0f, 119.0f);
            ImageManager.Add(Image.Name.ClosedCrabAlien, Texture.Name.Aliens, 316.0f, 179.0f, 167.0f, 119.0f);
            ImageManager.Add(Image.Name.OpenSquidAlien, Texture.Name.Aliens, 608.0f, 25.0f, 127.0f, 120.0f);
            ImageManager.Add(Image.Name.ClosedSquidAlien, Texture.Name.Aliens, 611.0f, 178.0f, 122.0f, 119.0f);
            ImageManager.Add(Image.Name.FlyingSaucer, Texture.Name.Aliens, 80.0f, 500.0f, 223.0f, 105.0f);

            // Shields
            ImageManager.Add(Image.Name.Brick, Texture.Name.Shield, 174, 110, 10, 5);
            ImageManager.Add(Image.Name.BrickLeft_Top0, Texture.Name.Shield, 153, 94, 10, 5);
            ImageManager.Add(Image.Name.BrickLeft_Top1, Texture.Name.Shield, 153, 99, 10, 5);
            ImageManager.Add(Image.Name.BrickLeft_Bottom, Texture.Name.Shield, 173, 129, 10, 5);
            ImageManager.Add(Image.Name.BrickRight_Top0, Texture.Name.Shield, 213, 94, 10, 5);
            ImageManager.Add(Image.Name.BrickRight_Top1, Texture.Name.Shield, 213, 99, 10, 5);
            ImageManager.Add(Image.Name.BrickRight_Bottom, Texture.Name.Shield, 193, 129, 10, 5);

            // Bombs
            ImageManager.Add(Image.Name.BombStraight, Texture.Name.Aliens, 378, 798, 15, 98);
            ImageManager.Add(Image.Name.BombZigZag, Texture.Name.Aliens, 574, 643, 42, 99);
            ImageManager.Add(Image.Name.BombCross, Texture.Name.Aliens, 112, 798, 42, 84);

            // Other
            ImageManager.Add(Image.Name.CoreCannon, Texture.Name.Aliens, 51.0f, 332.0f, 192.0f, 120.0f);
            ImageManager.Add(Image.Name.Missle, Texture.Name.Aliens, 419.0f, 699.0f, 16.0f, 58.0f);

            //---------------------------------------------------------------------------------------------------------
            // Create Sprites
            //---------------------------------------------------------------------------------------------------------

            // Aliens
            SpriteManager.Add(Sprite.Name.JellyfishAlien, Image.Name.OpenJellyfishAlien, 100.0f, 0.0f, 48.0f, 33.0f);
            SpriteManager.Add(Sprite.Name.CrabAlien, Image.Name.OpenCrabAlien, 100.0f, 200.0f, 45.0f, 30.0f);
            SpriteManager.Add(Sprite.Name.SquidAlien, Image.Name.OpenSquidAlien, 300.0f, 200.0f, 33.0f, 30.0f);
            SpriteManager.Add(Sprite.Name.FlyingSaucer, Image.Name.FlyingSaucer, 400.0f, 500.0f, 58.0f, 28.0f, pRedColor);

            SpriteManager.Add(Sprite.Name.GiantCrabAlien, Image.Name.OpenCrabAlien, 100.0f, 200.0f, 180.0f, 120.0f, pGreenColor);

            // Shields
            SpriteManager.Add(Sprite.Name.Brick, Image.Name.Brick, 50, 25, 12, 6);
            SpriteManager.Add(Sprite.Name.Brick_LeftTop0, Image.Name.BrickLeft_Top0, 50, 25, 12, 6);
            SpriteManager.Add(Sprite.Name.Brick_LeftTop1, Image.Name.BrickLeft_Top1, 50, 25, 12, 6);
            SpriteManager.Add(Sprite.Name.Brick_LeftBottom, Image.Name.BrickLeft_Bottom, 50, 25, 12, 6);
            SpriteManager.Add(Sprite.Name.Brick_RightTop0, Image.Name.BrickRight_Top0, 50, 25, 12, 6);
            SpriteManager.Add(Sprite.Name.Brick_RightTop1, Image.Name.BrickRight_Top1, 50, 25, 12, 6);
            SpriteManager.Add(Sprite.Name.Brick_RightBottom, Image.Name.BrickRight_Bottom, 50, 25, 12, 6);

            // Bombs
            SpriteManager.Add(Sprite.Name.BombZigZag, Image.Name.BombZigZag, 200, 200, 10, 20);
            SpriteManager.Add(Sprite.Name.BombStraight, Image.Name.BombStraight, 100, 100, 5, 20);
            SpriteManager.Add(Sprite.Name.BombCross, Image.Name.BombCross, 100, 100, 10, 20);

            // Other
            SpriteManager.Add(Sprite.Name.CoreCannon, Image.Name.CoreCannon, 400.0f, 40.0f, 50.0f, 30.0f, pGreenColor);
            SpriteManager.Add(Sprite.Name.Missile, Image.Name.Missle, 400.0f, 70.0f, 4.0f, 14.0f);

            //---------------------------------------------------------------------------------------------------------
            // Sprite Animations
            //---------------------------------------------------------------------------------------------------------

            // Squid Alien
            SpriteAnimation pSquidAnimation = SpriteAnimationManager.Add(SpriteAnimation.Name.SquidAlien, Sprite.Name.SquidAlien);
            pSquidAnimation.Attach(Image.Name.ClosedSquidAlien);
            pSquidAnimation.Attach(Image.Name.OpenSquidAlien);

            // Crab Alien
            SpriteAnimation pCrabAnimation = SpriteAnimationManager.Add(SpriteAnimation.Name.CrabAlien, Sprite.Name.CrabAlien);
            pCrabAnimation.Attach(Image.Name.ClosedCrabAlien);
            pCrabAnimation.Attach(Image.Name.OpenCrabAlien);

            // JellyFish Alien
            SpriteAnimation pJellyfishAnimation = SpriteAnimationManager.Add(SpriteAnimation.Name.JellyfishAlien, Sprite.Name.JellyfishAlien);
            pJellyfishAnimation.Attach(Image.Name.ClosedJellyfishAlien);
            pJellyfishAnimation.Attach(Image.Name.OpenJellyfishAlien);

            //---------------------------------------------------------------------------------------------------------
            // Create Game-Scoped SpriteBatches
            //---------------------------------------------------------------------------------------------------------
            pGameTexts_SpriteBatch = this.pGameSpriteBatchManager.Add(SpriteBatch.Name.GameTexts);

            FontManager.Add(Font.Name.Player1ScoreTitle, pGameTexts_SpriteBatch, "SCORE <1>", Glyph.Name.Consolas36pt, 50, 825);
            FontManager.Add(Font.Name.Player2ScoreTitle, pGameTexts_SpriteBatch, "SCORE <2>", Glyph.Name.Consolas36pt, 650, 825);
            FontManager.Add(Font.Name.HighScoreTitle, pGameTexts_SpriteBatch, "HI-SCORE", Glyph.Name.Consolas36pt, 350, 825);
            FontManager.Add(Font.Name.Player1Score, pGameTexts_SpriteBatch, GameStateManager.GetGame().GetPlayer(Player.Name.Player1).GetPoints().ToString("D4"), Glyph.Name.Consolas36pt, 80, 775);
            FontManager.Add(Font.Name.Player2Score, pGameTexts_SpriteBatch, GameStateManager.GetGame().GetPlayer(Player.Name.Player2).GetPoints().ToString("D4"), Glyph.Name.Consolas36pt, 680, 775);
            FontManager.Add(Font.Name.HighScore, pGameTexts_SpriteBatch, GameStateManager.GetGame().GetHiScore().ToString("D4"), Glyph.Name.Consolas36pt, 380, 775);

            FontManager.Add(Font.Name.GameCredits, pGameTexts_SpriteBatch, "CREDIT " + GameStateManager.GetGame().GetNumCredits(), Glyph.Name.Consolas36pt, 650, 50);

            //---------------------------------------------------------------------------------------------------------
            // Load State-specific content
            //---------------------------------------------------------------------------------------------------------

            GameStateManager.GetState(GameStateManager.GameState.Attract).LoadContent();
            GameStateManager.GetState(GameStateManager.GameState.Player1).LoadContent();
            GameStateManager.GetState(GameStateManager.GameState.Player2).LoadContent();
            GameStateManager.GetState(GameStateManager.GameState.End).LoadContent();

            // Game State -- Initialize in Attract mode
            this.pGameState = GameStateManager.GetState(GameStateManager.GameState.Attract);
            this.AttachStateInputObservers();
        }

        //-----------------------------------------------------------------------------
        // Game::Update()
        //      Called once per frame, update data, tranformations, etc
        //      Use this function to control process order
        //      Input, AI, Physics, Animation, and Graphics
        //-----------------------------------------------------------------------------
        public override void Update()
        {
            this.pGameState.Update();
        }

        //-----------------------------------------------------------------------------
        // Game::Draw()
        //		This function is called once per frame
        //	    Use this for draw graphics to the screen.
        //      Only do rendering here
        //-----------------------------------------------------------------------------
        public override void Draw()
        {
            this.pGameSpriteBatchManager.Draw();
            this.pGameState.Draw();
        }

        //-----------------------------------------------------------------------------
        // Game::UnLoadContent()
        //       unload content (resources loaded above)
        //       unload all content that was loaded before the Engine Loop started
        //-----------------------------------------------------------------------------
        public override void UnLoadContent()
        {
            FontManager.Destroy();
            GlyphManager.Destroy();
            CollisionPairManager.Destroy();
            SpriteAnimationManager.Destroy();
            SpriteBoxManager.Destroy();
            SpriteManager.Destroy();
            ImageManager.Destroy();
            TextureManager.Destroy();
        }

        public bool IsTwoPlayerMode()
        {
            return this.twoPlayerMode;
        }

        public void SetTwoPlayerMode(bool isTwoPlayer)
        {
            this.twoPlayerMode = isTwoPlayer;
        }

        public int GetHiScore()
        {
            return this.hiScore;
        }

        public void SetHiScore(int newHiScore)
        {
            this.hiScore = newHiScore;
        }

        public int GetNumCredits()
        {
            return this.numCredits;
        }

        public Player GetPlayer(Player.Name playerName)
        {
            Player pPlayer = null;

            switch (playerName)
            {
                case Player.Name.Player1:
                    pPlayer = this.player1;
                    break;
                case Player.Name.Player2:
                    pPlayer = this.player2;
                    break;
                default:
                    // something is wrong
                    Debug.Assert(false);
                    break;
            }

            return pPlayer;
        }

        public void SetPlayer(Player.Name playerName, Player inPlayer)
        {
            Debug.Assert(inPlayer != null);

            switch (playerName)
            {
                case Player.Name.Player1:
                    this.player1 = inPlayer;
                    break;
                case Player.Name.Player2:
                    this.player2 = inPlayer;
                    break;
                default:
                    // something is wrong
                    Debug.Assert(false);
                    break;
            }

        }

        public void SetGameState(GameStateManager.GameState inGameState)
        {
            this.pGameState = GameStateManager.GetState(inGameState);
        }

        public SpriteBatchManager GetGameScopeSpriteBatchManager()
        {
            return this.pGameSpriteBatchManager;
        }

        public void SetStateChangeFlag(bool changeState)
        {
            this.pGameState.SetStateChangePending(changeState);
        }

        public SpriteBatchManager GetStateSpriteBatchManager()
        {
            return this.pGameState.GetSpriteBatchManager();
        }

        public GameObjectManager GetStateGameObjectManager()
        {
            return this.pGameState.GetGameObjectManager();
        }

        public CoreCannonManager GetStateCoreCannonManager()
        {
            return this.pGameState.GetCoreCannonManager();
        }

        public QueuedTimeEventManager GetStateQueuedTimeEventManager()
        {
            return this.pGameState.GetQueuedTimeEventManager();
        }

        public GhostManager GetStateGhostManager()
        {
            return this.pGameState.GetGhostManager();
        }

        public DelayedObjectManager GetStateDelayedObjectManager()
        {
            return this.pGameState.GetDelayedObjectManager();
        }

        public CompositeManager GetStateCompositeManager()
        {
            return this.pGameState.GetCompositeManager();
        }

        public void AttachStateInputObservers()
        {
            this.pGameState.AttachInputObservers();
        }

        public void DetachStateInputObservers()
        {
            this.pGameState.DetachInputObservers();
        }

        public float GetStateAlienGridSpeed()
        {
            return this.pGameState.GetCurrAlienGridSpeed();
        }

        public void SetStateAlienGridSpeed(float newSpeed)
        {
            this.pGameState.SetCurrAlienGridSpeed(newSpeed);
        }

        public float GetGameInitialAlienGridSpeed()
        {
            return this.pGameState.GetGameInitialGridSpeed();
        }

        public float GetStateLevelInitialAlienGridSpeed()
        {
            return this.pGameState.GetLevelInitialAlienGridSpeed();
        }

        public void SetStateLevelInitialAlienGridSpeed(float newSpeed)
        {
            this.pGameState.SetLevelInitialAlienGridSpeed(newSpeed);
        }

        public float GetGameInitialGridXPosition()
        {
            return this.pGameState.GetGameInitialGridXPosition();
        }

        public float GetGameInitialGridYPosition()
        {
            return this.pGameState.GetGameInitialGridYPosition();
        }

        public float GetStateLevelInitialAlienGridYPosition()
        {
            return this.pGameState.GetLevelInitialAlienGridYPos();
        }

        public void SetStateLevelInitialAlienGridYPosition(float newYPos)
        {
            this.pGameState.SetLevelInitialAlienGridYPos(newYPos);
        }

        public void SetResetLevelFlag(bool resetLevelFlag)
        {
            this.pGameState.SetLevelResetPending(resetLevelFlag);
        }

        public bool IsLevelResetPending(bool resetLevelFlag)
        {
            return this.pGameState.IsLevelResetPending();
        }
    }
}

