using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class GamePlayer2State : SpaceInvadersGameState
    {
        // SpriteBatches
        private SpriteBatch pCollisionBox_SpriteBatch;
        private SpriteBatch pWall_SpriteBatch;
        private SpriteBatch pAlien_SpriteBatch;
        private SpriteBatch pCoreCannon_SpriteBatch;
        private SpriteBatch pMissile_SpriteBatch;
        private SpriteBatch pBomb_SpriteBatch;
        private SpriteBatch pShield_SpriteBatch;
        private SpriteBatch pFlyingSaucer_SpriteBatch;
        private SpriteBatch pTexts_SpriteBatch;

        // Composites
        private Composite pAlienGrid;
        private Composite pWallGroup;

        private Azul.Color pGreenColor;

        private Random r = new Random();

        public GamePlayer2State(GameStateManager.GameState stateName)
            : base(stateName)
        {

        }

        public override void Handle(SpaceInvaders pGame)
        {
            bool isTwoPlayer = GameStateManager.GetGame().IsTwoPlayerMode();
            Player pPlayer2 = GameStateManager.GetGame().GetPlayer(Player.Name.Player2);

            // Two-Player Mode

            if (pPlayer2.GetNumLives() < 1)
            {
                // Update HiScore
                this.UpdateHiScore(pGame, pPlayer2.GetPoints());

                //Go to End state
                this.SwitchState(pGame, GameStateManager.GameState.End);

            } else
            {
                // Switch to player 1
                this.SwitchState(pGame, GameStateManager.GameState.Player1);
            }
            
        }

        //-----------------------------------------------------------------------------
        // Game::LoadContent()
        //		Allows you to load all content needed for your engine,
        //	    such as objects, graphics, etc.
        //-----------------------------------------------------------------------------
        public override void LoadContent()
        {
            // Set to current state for initialization
            GameStateManager.GetGame().SetGameState(GameStateManager.GameState.Player2);

            //---------------------------------------------------------------------------------------------------------
            // Initialize State-Scoped Managers
            //---------------------------------------------------------------------------------------------------------

            this.pSpriteBatchManager = new SpriteBatchManager(3, 1);
            this.pGameObjectManager = new GameObjectManager(300, 10);
            this.pDelayedObjectManager = new DelayedObjectManager();
            this.pQueuedTimeEventManager = new QueuedTimeEventManager(50, 5);
            this.pGhostManager = new GhostManager(50, 5);
            this.pCompositeManager = new CompositeManager(20, 2);

            //---------------------------------------------------------------------------------------------------------
            // Create Colors
            //---------------------------------------------------------------------------------------------------------

            pGreenColor = new Azul.Color(0.1137f, 0.8196f, 0.2667f, 1.0f);

            //---------------------------------------------------------------------------------------------------------
            // Create SpriteBatches
            //---------------------------------------------------------------------------------------------------------
            pCollisionBox_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.CollisionBox);
            pCollisionBox_SpriteBatch.SetDraw(false);

            pWall_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Wall);
            pAlien_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Alien);
            pCoreCannon_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.CoreCannon);
            pMissile_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Missile);
            pBomb_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Bomb);
            pShield_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Shield);
            pFlyingSaucer_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.FlyingSaucer);
            pTexts_SpriteBatch = this.pSpriteBatchManager.Add(SpriteBatch.Name.Player2Texts);

            //---------------------------------------------------------------------------------------------------------
            // Flying Saucer
            //---------------------------------------------------------------------------------------------------------

            FlyingSaucerRoot pFlyingSaucerRoot = new FlyingSaucerRoot(Composite.CompositeName.FlyingSaucerRoot, 0.0f, 0.0f);
            pFlyingSaucerRoot.ActivateSprite(pFlyingSaucer_SpriteBatch);
            pFlyingSaucerRoot.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            this.pGameObjectManager.Attach(pFlyingSaucerRoot);
            this.pCompositeManager.Attach(pFlyingSaucerRoot);

            DeployFlyingSaucerCommand pDeployFlyingSaucer = new DeployFlyingSaucerCommand();
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.DeployFlyingSaucer, pDeployFlyingSaucer, r.Next(30, 46));

            //---------------------------------------------------------------------------------------------------------
            // Core Cannon
            //---------------------------------------------------------------------------------------------------------

            CoreCannonGroup pCoreCannonGroup = new CoreCannonGroup(Composite.CompositeName.CoreCannonGroup, 0.0f, 0.0f);
            this.pGameObjectManager.Attach(pCoreCannonGroup);
            this.pCompositeManager.Attach(pCoreCannonGroup);
            this.pCoreCannonManager = new CoreCannonManager();
            this.pCoreCannonManager.ActivateCoreCannon();

            //---------------------------------------------------------------------------------------------------------
            // Sprite Animations
            //---------------------------------------------------------------------------------------------------------

            // Squid Alien
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.SpriteAnimation, SpriteAnimationManager.Find(SpriteAnimation.Name.SquidAlien), 0.8f);

            // Crab Alien
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.SpriteAnimation, SpriteAnimationManager.Find(SpriteAnimation.Name.CrabAlien), 0.8f);

            // JellyFish Alien
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.SpriteAnimation, SpriteAnimationManager.Find(SpriteAnimation.Name.JellyfishAlien), 0.8f);

            //---------------------------------------------------------------------------------------------------------
            // Bomb
            //---------------------------------------------------------------------------------------------------------

            BombRoot pBombRoot = new BombRoot(Composite.CompositeName.BombRoot, 0.0f, 0.0f);
            pBombRoot.ActivateSprite(pBomb_SpriteBatch);
            //pBombRoot.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            this.pCompositeManager.Attach(pBombRoot);
            this.pGameObjectManager.Attach(pBombRoot);

            // Add initial drop bomb events
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.DropBomb, new DropBombCommand(), 1.0f);
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.DropBomb, new DropBombCommand(), 3.0f);

            //---------------------------------------------------------------------------------------------------------
            // Missile
            //---------------------------------------------------------------------------------------------------------

            MissileGroup pMissileGroup = new MissileGroup(Composite.CompositeName.MissileGroup, 0.0f, 0.0f);
            pMissileGroup.ActivateSprite(pMissile_SpriteBatch);
            pMissileGroup.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            this.pGameObjectManager.Attach(pMissileGroup);
            this.pCompositeManager.Attach(pMissileGroup);

            //---------------------------------------------------------------------------------------------------------
            // Create Walls
            //---------------------------------------------------------------------------------------------------------

            // Wall Root
            pWallGroup = new WallGroup(GameObject.Name.ShieldGroup, Sprite.Name.NullSprite, 0.0f, 0.0f);
            pWallGroup.ActivateSprite(pWall_SpriteBatch);
            pWallGroup.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            LeftWall pLeftWall = new LeftWall(GameObject.Name.LeftWall, Sprite.Name.NullSprite, 20, 425, 15, 800);
            pLeftWall.ActivateSprite(pWall_SpriteBatch);
            pLeftWall.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            RightWall pRightWall = new RightWall(GameObject.Name.RightWall, Sprite.Name.NullSprite, 875, 425, 15, 800);
            pRightWall.ActivateSprite(pWall_SpriteBatch);
            pRightWall.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            TopWall pTopWall = new TopWall(GameObject.Name.TopWall, Sprite.Name.NullSprite, 450, 825, 870, 15);
            pTopWall.ActivateSprite(pWall_SpriteBatch);
            pTopWall.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            BottomWall pBottomWall = new BottomWall(GameObject.Name.BottomWall, Sprite.Name.NullSprite, 450, 55, 870, 15);
            pBottomWall.ActivateSprite(pWall_SpriteBatch);
            pBottomWall.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            LeftBumper pLeftBumper = new LeftBumper(GameObject.Name.LeftBumper, Sprite.Name.NullSprite, 45, 100, 30, 50);
            pLeftBumper.ActivateSprite(pWall_SpriteBatch);
            pLeftBumper.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            RightBumper pRightBumper = new RightBumper(GameObject.Name.RightBumper, Sprite.Name.NullSprite, 850, 100, 30, 50);
            pRightBumper.ActivateSprite(pWall_SpriteBatch);
            pRightBumper.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            pWallGroup.Add(pLeftWall);
            pWallGroup.Add(pRightWall);
            pWallGroup.Add(pTopWall);
            pWallGroup.Add(pBottomWall);
            pWallGroup.Add(pLeftBumper);
            pWallGroup.Add(pRightBumper);

            this.pCompositeManager.Attach(pWallGroup);
            this.pGameObjectManager.Attach(pWallGroup);
            this.pGameObjectManager.Attach(pLeftWall);
            this.pGameObjectManager.Attach(pRightWall);
            this.pGameObjectManager.Attach(pTopWall);
            this.pGameObjectManager.Attach(pBottomWall);
            this.pGameObjectManager.Attach(pLeftBumper);
            this.pGameObjectManager.Attach(pRightBumper);

            //---------------------------------------------------------------------------------------------------------
            // Shield 
            //---------------------------------------------------------------------------------------------------------

            //Shield Root
            ShieldGroup pShieldGroup = new ShieldGroup(Composite.CompositeName.ShieldGroup, 0.0f, 0.0f);
            pShieldGroup.ActivateSprite(pShield_SpriteBatch);
            pShieldGroup.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            GameObject pShield;
            pShield = ShieldFactory.CreateShield(110, 150);
            pShieldGroup.Add(pShield);
            pShield = ShieldFactory.CreateShield(310, 150);
            pShieldGroup.Add(pShield);
            pShield = ShieldFactory.CreateShield(510, 150);
            pShieldGroup.Add(pShield);
            pShield = ShieldFactory.CreateShield(710, 150);
            pShieldGroup.Add(pShield);

            this.pGameObjectManager.Attach(pShieldGroup);
            this.pCompositeManager.Attach(pShieldGroup);


            //---------------------------------------------------------------------------------------------------------
            // Create Alien Grid Composite
            //---------------------------------------------------------------------------------------------------------

            this.levelInitialAlienGridSpeed = SpaceInvadersGameState.gameInitialAlienGridSpeed;
            this.currAlienGridSpeed = SpaceInvadersGameState.gameInitialAlienGridSpeed;

            this.currLevelInitialAlienGridY = SpaceInvadersGameState.gameInitialAlienGridY;

            pAlienGrid = (Composite)AlienFactory.CreateAlienGrid(SpaceInvadersGameState.gameInitialAlienGridX, currLevelInitialAlienGridY);

            // Add Initial AlienGridMovementSound Event
            AlienGridMovementSound pAlienGridMovementSound = new AlienGridMovementSound();
            this.pQueuedTimeEventManager.Enqueue(TimeEvent.Name.AlienGridMovementSound, pAlienGridMovementSound, levelInitialAlienGridSpeed);

            //---------------------------------------------------------------------------------------------------------
            // Associate Collision Pairs 
            //---------------------------------------------------------------------------------------------------------

            // Alien vs Missile
            CollisionPair pAlienMissileColPair = CollisionPairManager.Add(CollisionPair.Name.Alien_Missile, pAlienGrid, pMissileGroup);
            Debug.Assert(pAlienMissileColPair != null);
            pAlienMissileColPair.Attach(new ShipReadyObserver());
            pAlienMissileColPair.Attach(new RemoveMissileObserver());
            pAlienMissileColPair.Attach(new RemoveAlienObserver());
            pAlienMissileColPair.Attach(new AddPlayerPointsObserver(Player.Name.Player2));
            pAlienMissileColPair.Attach(new InvaderKilledSoundObserver());

            // FlyingSaucer vs Missile
            CollisionPair pFlyingSaucerMissileColPair = CollisionPairManager.Add(CollisionPair.Name.FlyingSaucer_Missile, pFlyingSaucerRoot, pMissileGroup);
            Debug.Assert(pFlyingSaucerMissileColPair != null);
            pFlyingSaucerMissileColPair.Attach(new ShipReadyObserver());
            pFlyingSaucerMissileColPair.Attach(new RemoveMissileObserver());
            pFlyingSaucerMissileColPair.Attach(new RemoveFlyingSaucerObserver());
            pFlyingSaucerMissileColPair.Attach(new ExplosionSoundObserver());

            // FlyingSaucer vs Wall
            CollisionPair pFlyingSaucerWallColPair = CollisionPairManager.Add(CollisionPair.Name.FlyingSaucer_Wall, pFlyingSaucerRoot, pWallGroup);
            Debug.Assert(pFlyingSaucerWallColPair != null);
            pFlyingSaucerWallColPair.Attach(new RemoveFlyingSaucerObserver());

            // Alien vs Wall
            CollisionPair pAlienWallColPair = CollisionPairManager.Add(CollisionPair.Name.Alien_Wall, pAlienGrid, pWallGroup);
            Debug.Assert(pAlienWallColPair != null);
            pAlienWallColPair.Attach(new GridWallCollisionObserver());

            // Alien vs Shield
            CollisionPair pAlienShieldColPair = CollisionPairManager.Add(CollisionPair.Name.Alien_Shield, pAlienGrid, pShieldGroup);
            Debug.Assert(pAlienShieldColPair != null);
            pAlienShieldColPair.Attach(new RemoveShieldBrickObserver());

            // Missile vs Wall
            CollisionPair pMissileWallColPair = CollisionPairManager.Add(CollisionPair.Name.Missile_Wall, pMissileGroup, pWallGroup);
            Debug.Assert(pMissileWallColPair != null);
            pMissileWallColPair.Attach(new ShipReadyObserver());
            pMissileWallColPair.Attach(new RemoveMissileObserver());

            // Missile vs Shield
            CollisionPair pMissileShieldColPair = CollisionPairManager.Add(CollisionPair.Name.Missile_Shield, pMissileGroup, pShieldGroup);
            Debug.Assert(pMissileShieldColPair != null);
            pMissileShieldColPair.Attach(new ShipReadyObserver());
            pMissileShieldColPair.Attach(new RemoveMissileObserver());
            pMissileShieldColPair.Attach(new RemoveShieldBrickObserver());

            // Bomb vs Shield
            CollisionPair pBombShieldColPair = CollisionPairManager.Add(CollisionPair.Name.Bomb_Shield, pBombRoot, pShieldGroup);
            Debug.Assert(pBombShieldColPair != null);
            pBombShieldColPair.Attach(new RemoveBombObserver());
            pBombShieldColPair.Attach(new RemoveShieldBrickObserver());

            // Bomb vs Wall
            CollisionPair pBombWallColPair = CollisionPairManager.Add(CollisionPair.Name.Bomb_Wall, pBombRoot, pWallGroup);
            Debug.Assert(pBombWallColPair != null);
            pBombWallColPair.Attach(new RemoveBombObserver());

            // CoreCannon vs Bumpers
            CollisionPair pCoreCannonWallColPair = CollisionPairManager.Add(CollisionPair.Name.CoreCannon_Wall, pCoreCannonGroup, pWallGroup);
            Debug.Assert(pCoreCannonWallColPair != null);
            pCoreCannonWallColPair.Attach(new CoreCannonBumperCollisionObserver());

            // Bomb vs Missile
            CollisionPair pBombMissileColPair = CollisionPairManager.Add(CollisionPair.Name.Bomb_Missile, pBombRoot, pMissileGroup);
            Debug.Assert(pBombMissileColPair != null);
            pBombMissileColPair.Attach(new ShipReadyObserver());
            pBombMissileColPair.Attach(new RemoveBombObserver());
            pBombMissileColPair.Attach(new RemoveMissileObserver());

            // Bomb vs CoreCannon
            CollisionPair pBombShipColPair = CollisionPairManager.Add(CollisionPair.Name.Bomb_Ship, pBombRoot, pCoreCannonGroup);
            Debug.Assert(pBombShipColPair != null);
            pBombShipColPair.Attach(new PlayerDeathObserver(Player.Name.Player2));
            pBombShipColPair.Attach(new RemoveBombObserver());
            pBombShipColPair.Attach(new ExplosionSoundObserver());

            //---------------------------------------------------------------------------------------------------------
            // Fonts
            //---------------------------------------------------------------------------------------------------------
            FontManager.Add(Font.Name.Player2Lives, pTexts_SpriteBatch, "LIVES " + GameStateManager.GetGame().GetPlayer(Player.Name.Player2).GetNumLives().ToString(), Glyph.Name.Consolas36pt, 50, 50);

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

            // Fire off the timer event
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

            // Reset level if flag is true
            if (this.resetLevel)
            {
                ResetLevelCommand resetLevel = new ResetLevelCommand();
                resetLevel.Execute(1.0f);
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

            // TODO Remove -- Testing
            pInputSubject = InputManager.GetKeyNSubject();
            pInputSubject.Attach(new ChangeGameStateObserver());

            pInputSubject = InputManager.GetArrowRightSubject();
            pInputSubject.Attach(new MoveRightObserver());

            pInputSubject = InputManager.GetArrowLeftSubject();
            pInputSubject.Attach(new MoveLeftObserver());

            pInputSubject = InputManager.GetSpaceSubject();
            pInputSubject.Attach(new ShootObserver());

            pInputSubject = InputManager.GetKey1Subject();
            pInputSubject.Attach(new ToggleSpriteBatchDrawObserver(pCollisionBox_SpriteBatch));

            pInputSubject = InputManager.GetKey2Subject();
            pInputSubject.Attach(new ToggleSpriteBatchDrawObserver(pShield_SpriteBatch));
        }

        public override void DetachInputObservers()
        {
            InputSubject pInputSubject;
            pInputSubject = InputManager.GetArrowRightSubject();
            pInputSubject.DetachAllObservers();

            pInputSubject = InputManager.GetArrowLeftSubject();
            pInputSubject.DetachAllObservers();

            pInputSubject = InputManager.GetSpaceSubject();
            pInputSubject.DetachAllObservers();

            pInputSubject = InputManager.GetKey1Subject();
            pInputSubject.DetachAllObservers();

            pInputSubject = InputManager.GetKey2Subject();
            pInputSubject.DetachAllObservers();
        }

    }
}
