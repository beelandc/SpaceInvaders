using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class AlienFactory
    {
        SpriteBatch pAlienSpriteBatch;
        SpriteBatch pCollisionBoxSpriteBatch;
        Composite pComposite;

        public AlienFactory(SpriteBatch.Name alienSpriteBatchName, SpriteBatch.Name collisionBoxSpriteBatchName, Composite alienComposite)
        {
            this.pAlienSpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(alienSpriteBatchName);
            Debug.Assert(this.pAlienSpriteBatch != null);

            this.pCollisionBoxSpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(collisionBoxSpriteBatchName);
            Debug.Assert(this.pCollisionBoxSpriteBatch != null);

            Debug.Assert(alienComposite != null);
            this.pComposite = alienComposite;

        }

        internal static void DestroyAlienGrid(AlienGrid pAlienGrid)
        {
            // Iterate through AlienGridColumns
            AlienGridColumn pGridColumn = (AlienGridColumn)pAlienGrid.GetFirstChild();
            while (pGridColumn != null)
            {
                
                    // Iterate through shield bricks
                    Alien pAlien = (Alien)pGridColumn.GetFirstChild();
                    while (pAlien != null)
                    {
                        // Remove Alien from composite
                        pGridColumn.Remove(pAlien);

                        // Kill Alien
                        pAlien.Remove(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox);

                        // Get next Alien
                        pAlien = (Alien)pGridColumn.GetFirstChild();
                    }

                // Remove AlienGridColumn from composite
                pAlienGrid.Remove(pGridColumn);

                // Kill AlienGridColumn
                pGridColumn.Remove(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox);

                // Get next AlienGridColumn
                pGridColumn = (AlienGridColumn)pAlienGrid.GetFirstChild();
            }
        }

        ~AlienFactory()
        {
            this.pAlienSpriteBatch = null;
            this.pComposite = null;
        }

        public void Create(Alien.Type type, float posX, float posY)
        {
            GameObject pGameObj = null; 

            switch (type)
            {
                case Alien.Type.Crab:
                    pGameObj = new CrabAlien(GameObject.Name.CrabAlien, Sprite.Name.CrabAlien, posX, posY);
                    break;

                case Alien.Type.FlyingSaucer:
                    pGameObj = new FlyingSaucer(GameObject.Name.FlyingSaucer, Sprite.Name.FlyingSaucer, posX, posY);
                    break;

                case Alien.Type.JellyFish:
                    pGameObj = new JellyfishAlien(GameObject.Name.JellyFishAlien, Sprite.Name.JellyfishAlien, posX, posY);
                    break;

                case Alien.Type.Squid:
                    pGameObj = new SquidAlien(GameObject.Name.SquidAlien, Sprite.Name.SquidAlien, posX, posY);
                    break;

                default:
                    // something is wrong
                    Debug.Assert(false);
                    break;
            }

            // Add to GameObjectManager
            Debug.Assert(pGameObj != null);
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pGameObj);

            // Add to Composite
            pComposite.Add(pGameObj);

            // Attach to SpriteBatch
            pGameObj.ActivateSprite(this.pAlienSpriteBatch);
            pGameObj.ActivateCollisionSprite(this.pCollisionBoxSpriteBatch);
        }

        public void Recreate(Alien.Type type, float posX, float posY)
        {
            GameObject pGameObj = null;
            GhostManager pGhostManager = GameStateManager.GetGame().GetStateGhostManager();
            GameObjectManager pGameObjectManager = GameStateManager.GetGame().GetStateGameObjectManager();

            switch (type)
            {
                case Alien.Type.Crab:
                    pGameObj = pGhostManager.Find(GameObject.Name.CrabAlien);
                    break;

                case Alien.Type.FlyingSaucer:
                    pGameObj = pGhostManager.Find(GameObject.Name.FlyingSaucer);
                    break;

                case Alien.Type.JellyFish:
                    pGameObj = pGhostManager.Find(GameObject.Name.JellyFishAlien);
                    break;

                case Alien.Type.Squid:
                    pGameObj = pGhostManager.Find(GameObject.Name.SquidAlien);
                    break;

                default:
                    // something is wrong
                    Debug.Assert(false);
                    break;
            }

            // Remove game object from ghost manager
            pGhostManager.Detach(pGameObj);

            // Reset position
            pGameObj.SetX(posX);
            pGameObj.SetY(posY);

            // Reset Collision Object
            Azul.Rect pSpriteProxyScreenRect = pGameObj.GetSpriteProxy().GetSpriteScreenRect();
            CollisionObject pCollisionObject = pGameObj.GetCollisionObject();
            pCollisionObject.GetCollisionRect().Set(pSpriteProxyScreenRect);
            pCollisionObject.GetCollisionSpriteBox().Set(SpriteBox.Name.Box, posX, posY, pSpriteProxyScreenRect.width, pSpriteProxyScreenRect.height);


            // Add to GameObjectManager
            Debug.Assert(pGameObj != null);
            pGameObjectManager.Attach(pGameObj);

            // Add to Composite
            pComposite.Add(pGameObj);

            // Attach to SpriteBatch
            pGameObj.ActivateSprite(this.pAlienSpriteBatch);
            pGameObj.ActivateCollisionSprite(this.pCollisionBoxSpriteBatch);
        }

        public static GameObject CreateAlienGrid(float start_x = 0.0f, float start_y = 0.0f)
        {
            // Create Composite objects 
            Composite pAlienGrid = new AlienGrid(Composite.CompositeName.AlienGrid, 20.0f, 0.0f);
            pAlienGrid.SetCollisionObjectLineColor(1.0f, 1.0f, 0.0f);
            pAlienGrid.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Alien));
            pAlienGrid.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));
            GameStateManager.GetGame().GetStateCompositeManager().Attach(pAlienGrid);
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pAlienGrid);

            Composite pCol;
            for( int i = 0; i < 11; i++ )
            {
                pCol = new AlienGridColumn(Composite.CompositeName.AlienCol1 + i);
                GameStateManager.GetGame().GetStateGameObjectManager().Attach(pCol);

                // Generate Aliens & Attach to Column
                GenerateNewAlienGridColumn(pCol, (start_x + (60.0f * i)), start_y);

                // Append Columns to AlienGrid
                pAlienGrid.Add(pCol);
            }

            // Add Initial AlienGridMovement Event
            AlienGridMovement pAlienGridMovement = new AlienGridMovement(pAlienGrid);
            GameStateManager.GetGame().GetStateQueuedTimeEventManager().Enqueue(TimeEvent.Name.AlienGridMovement, pAlienGridMovement, GameStateManager.GetGame().GetStateLevelInitialAlienGridSpeed());

            return pAlienGrid;
        }

        private static void GenerateNewAlienGridColumn(Composite pCol, float x, float yStart, float yDelta = -40.0f)
        {
            pCol.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Alien));
            pCol.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));
            pCol.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(1.0f, 1.0f, 1.0f);

            AlienFactory pAlienFactory = new AlienFactory(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox, pCol);
            pAlienFactory.Create(Alien.Type.Squid, x, yStart);
            pAlienFactory.Create(Alien.Type.Crab, x, yStart + (yDelta));
            pAlienFactory.Create(Alien.Type.Crab, x, yStart + (yDelta * 2));
            pAlienFactory.Create(Alien.Type.JellyFish, x, yStart + (yDelta * 3));
            pAlienFactory.Create(Alien.Type.JellyFish, x, yStart + (yDelta * 4));
        }

        public static GameObject RecreateAlienGrid(AlienGrid pAlienGrid, float startingGridSpeed, float start_x = 0.0f, float start_y = 0.0f)
        {
            GhostManager pGhostManager = GameStateManager.GetGame().GetStateGhostManager();
            GameObjectManager pGameObjectManager = GameStateManager.GetGame().GetStateGameObjectManager();

            // Attach Composite objects 
            Composite pCol;
            for (int i = 0; i < 11; i++)
            {
                pCol = (Composite)pGhostManager.Find(GameObject.Name.AlienGridColumn);
                pGhostManager.Detach(pCol);

                pGameObjectManager.Attach(pCol);

                // Recreate Aliens & Attach to Column
                RecreateAlienGridColumn(pCol, (start_x + (60.0f * i)), start_y);

                // Append Columns to AlienGrid
                pAlienGrid.Add(pCol);
            }

            // Add Initial AlienGridMovement Event
            AlienGridMovement pAlienGridMovement = new AlienGridMovement(pAlienGrid);
            GameStateManager.GetGame().GetStateQueuedTimeEventManager().Enqueue(TimeEvent.Name.AlienGridMovement, pAlienGridMovement, startingGridSpeed);

            return pAlienGrid;
        }

        private static void RecreateAlienGridColumn(Composite pCol, float x, float yStart, float yDelta = -40.0f)
        {
            GhostManager pGhostManager = GameStateManager.GetGame().GetStateGhostManager();
            GameObjectManager pGameObjectManager = GameStateManager.GetGame().GetStateGameObjectManager();
            SpriteBatchManager pSpriteBatchManager = GameStateManager.GetGame().GetStateSpriteBatchManager();

            pCol.ActivateSprite(pSpriteBatchManager.Find(SpriteBatch.Name.Alien));
            pCol.ActivateCollisionSprite(pSpriteBatchManager.Find(SpriteBatch.Name.CollisionBox));
            pCol.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(1.0f, 1.0f, 1.0f);

            AlienFactory pAlienFactory = new AlienFactory(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox, pCol);
            pAlienFactory.Recreate(Alien.Type.Squid, x, yStart);
            pAlienFactory.Recreate(Alien.Type.Crab, x, yStart + (yDelta));
            pAlienFactory.Recreate(Alien.Type.Crab, x, yStart + (yDelta * 2));
            pAlienFactory.Recreate(Alien.Type.JellyFish, x, yStart + (yDelta * 3));
            pAlienFactory.Recreate(Alien.Type.JellyFish, x, yStart + (yDelta * 4));
        }

    }

}

