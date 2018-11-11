using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ShieldFactory
    {
        private SpriteBatch pSpriteBatch;
        private SpriteBatch pCollisionSpriteBatch;
        private Composite pTree;

        public ShieldFactory(SpriteBatch.Name spriteBatchName, SpriteBatch.Name collisionSpriteBatch, Composite pTree)
        {
            this.pSpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(spriteBatchName);
            Debug.Assert(this.pSpriteBatch != null);

            this.pCollisionSpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(collisionSpriteBatch);
            Debug.Assert(this.pCollisionSpriteBatch != null);

            Debug.Assert(pTree != null);
            this.pTree = pTree;
        }

        internal static void DestroyShields(ShieldGroup pShieldGroup)
        {
            // Iterate through shields
            ShieldRoot pShieldRoot = (ShieldRoot)pShieldGroup.GetFirstChild();
            while (pShieldRoot != null)
            {
                // Iterate through shield columns
                ShieldColumn pShieldCol = (ShieldColumn)pShieldRoot.GetFirstChild();
                while (pShieldCol != null)
                {
                    // Iterate through shield bricks
                    ShieldBrick pShieldBrick = (ShieldBrick)pShieldCol.GetFirstChild();
                    while (pShieldBrick != null)
                    {
                        // Remove ShieldBrick from composite
                        pShieldCol.Remove(pShieldBrick);

                        // Kill ShieldRoot
                        pShieldBrick.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);

                        // Get next ShieldColumn
                        pShieldBrick = (ShieldBrick)pShieldCol.GetFirstChild();
                    }

                    // Remove ShieldColumn from composite
                    pShieldRoot.Remove(pShieldCol);

                    // Kill ShieldRoot
                    pShieldCol.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);

                    // Get next ShieldColumn
                    pShieldCol = (ShieldColumn)pShieldRoot.GetFirstChild();
                }


                // Remove ShieldRoot from composite
                pShieldGroup.Remove(pShieldRoot);

                // Kill ShieldRoot
                pShieldRoot.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);

                // Get next ShieldRoot
                pShieldRoot = (ShieldRoot)pShieldGroup.GetFirstChild();
            }
        }

        public void SetParent( GameObject pParentNode )
        {
            // OK being null
            Debug.Assert(pParentNode != null);
            this.pTree = (Composite)pParentNode;
        }

        ~ShieldFactory()
        {
            this.pSpriteBatch = null;
        }

        public GameObject Create(ShieldCategory.Type type, GameObject.Name gameName, float posX = 0.0f, float posY = 0.0f)
        {
            GameObject pShield = null;

            switch (type)
            {
                case ShieldCategory.Type.Brick:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick, posX, posY);
                    break;

                case ShieldCategory.Type.LeftTop1:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick_LeftTop1, posX, posY);
                    break;

                case ShieldCategory.Type.LeftTop0:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick_LeftTop0, posX, posY);
                    break;

                case ShieldCategory.Type.LeftBottom:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick_LeftBottom, posX, posY);
                    break;

                case ShieldCategory.Type.RightTop1:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick_RightTop1, posX, posY);
                    break;

                case ShieldCategory.Type.RightTop0:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick_RightTop0, posX, posY);
                    break;

                case ShieldCategory.Type.RightBottom:
                    pShield = new ShieldBrick(gameName, Sprite.Name.Brick_RightBottom,posX, posY);
                    break;

                case ShieldCategory.Type.Root:
                    pShield = new ShieldRoot(gameName, Sprite.Name.NullSprite,  posX, posY);
                    pShield.SetCollisionObjectLineColor(0.0f, 0.0f, 1.0f);
                    break;

                case ShieldCategory.Type.Column:
                    pShield = new ShieldColumn(gameName, Sprite.Name.NullSprite, posX, posY);
                    pShield.SetCollisionObjectLineColor(1.0f, 0.0f, 0.0f);
                    break;

                default:
                    // something is wrong
                    Debug.Assert(false);
                    break;
            }

            // Add to GameObjectManager
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pShield);

            // add to the tree
            this.pTree.Add(pShield);

            // Attached to Group
            pShield.ActivateSprite(this.pSpriteBatch);
            pShield.ActivateCollisionSprite(this.pCollisionSpriteBatch);

            return pShield;
        }

        public static GameObject CreateShield(float start_x = 0.0f, float start_y = 0.0f)
        {
            Composite pShieldRoot = (Composite)new ShieldRoot(GameObject.Name.ShieldRoot, Sprite.Name.NullSprite, 0.0f, 0.0f);
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pShieldRoot);
            pShieldRoot.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Shield));
            pShieldRoot.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));

            ShieldFactory SF = new ShieldFactory(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox, pShieldRoot);

            // load by column

            GameObject pColumn;

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            float off_x = 0;
            float brickWidth = 12.0f;
            float brickHeight = 6.0f;

            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 2 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.LeftTop1, GameObject.Name.ShieldBrick_LeftTop1, start_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.LeftTop0, GameObject.Name.ShieldBrick_LeftTop0, start_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Create(ShieldCategory.Type.LeftBottom, GameObject.Name.ShieldBrick_LeftBottom, start_x + off_x, start_y + 2 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Create(ShieldCategory.Type.RightBottom, GameObject.Name.ShieldBrick_RightBottom, start_x + off_x, start_y + 2 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 0 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 1 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Create(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 0 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 1 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Create(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Create(ShieldCategory.Type.RightTop1, GameObject.Name.ShieldBrick_RightTop1, start_x + off_x, start_y + 8 * brickHeight);
            SF.Create(ShieldCategory.Type.RightTop0, GameObject.Name.ShieldBrick_RightTop0, start_x + off_x, start_y + 9 * brickHeight);

            return pShieldRoot;
        }

        public static GameObject RecreateShield(float start_x = 0.0f, float start_y = 0.0f)
        {
            GhostManager pGhostManager = GameStateManager.GetGame().GetStateGhostManager();
            GameObjectManager pGameObjectManager = GameStateManager.GetGame().GetStateGameObjectManager();

            Composite pShieldRoot = (Composite)pGhostManager.Find(GameObject.Name.ShieldRoot);
            pGhostManager.Detach(pShieldRoot);

            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pShieldRoot);
            pShieldRoot.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Shield));
            pShieldRoot.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));

            ShieldFactory SF = new ShieldFactory(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox, pShieldRoot);

            // load by column

            GameObject pColumn;

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            float off_x = 0;
            float brickWidth = 12.0f;
            float brickHeight = 6.0f;

            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 2 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.LeftTop1, GameObject.Name.ShieldBrick, start_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.LeftTop0, GameObject.Name.ShieldBrick, start_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Recreate(ShieldCategory.Type.LeftBottom, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Recreate(ShieldCategory.Type.RightBottom, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 0 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 1 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            SF.SetParent(pShieldRoot);
            pColumn = SF.Recreate(ShieldCategory.Type.Column, GameObject.Name.ShieldColumn);

            SF.SetParent(pColumn);

            off_x += brickWidth;
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 0 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 1 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 2 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 3 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 4 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 5 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 6 * brickHeight);
            SF.Recreate(ShieldCategory.Type.Brick, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 7 * brickHeight);
            SF.Recreate(ShieldCategory.Type.RightTop1, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 8 * brickHeight);
            SF.Recreate(ShieldCategory.Type.RightTop0, GameObject.Name.ShieldBrick, start_x + off_x, start_y + 9 * brickHeight);

            return pShieldRoot;
        }

        public GameObject Recreate(ShieldCategory.Type type, GameObject.Name gameName, float posX = 0.0f, float posY = 0.0f)
        {
            GhostManager pGhostManager = GameStateManager.GetGame().GetStateGhostManager();
            GameObjectManager pGameObjectManager = GameStateManager.GetGame().GetStateGameObjectManager();

            GameObject pShield = null;
            switch (type)
            {
                case ShieldCategory.Type.Brick:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick);
                    break;

                case ShieldCategory.Type.LeftTop1:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick_LeftTop1);
                    break;

                case ShieldCategory.Type.LeftTop0:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick_LeftTop0);
                    break;

                case ShieldCategory.Type.LeftBottom:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick_LeftBottom);
                    break;

                case ShieldCategory.Type.RightTop1:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick_RightTop1);
                    break;

                case ShieldCategory.Type.RightTop0:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick_RightTop0);
                    break;

                case ShieldCategory.Type.RightBottom:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldBrick_RightBottom);
                    break;

                case ShieldCategory.Type.Root:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldRoot);
                    pShield.SetCollisionObjectLineColor(0.0f, 0.0f, 1.0f);
                    break;

                case ShieldCategory.Type.Column:
                    pShield = pGhostManager.Find(GameObject.Name.ShieldColumn);
                    pShield.SetCollisionObjectLineColor(1.0f, 0.0f, 0.0f);
                    break;

                default:
                    // something is wrong
                    Debug.Assert(false);
                    break;
            }

            // Remove game object from ghost manager
            pGhostManager.Detach(pShield);

            // Reset position
            pShield.SetX(posX);
            pShield.SetY(posY);

            // Reset Collision Object
            Azul.Rect pSpriteProxyScreenRect = pShield.GetSpriteProxy().GetSpriteScreenRect();
            CollisionObject pCollisionObject = pShield.GetCollisionObject();
            pCollisionObject.GetCollisionRect().Set(pSpriteProxyScreenRect);
            pCollisionObject.GetCollisionSpriteBox().Set(SpriteBox.Name.Box, posX, posY, pSpriteProxyScreenRect.width, pSpriteProxyScreenRect.height);

            // Add to GameObjectManager
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pShield);

            // add to the tree
            this.pTree.Add(pShield);

            // Attached to Group
            pShield.ActivateSprite(this.pSpriteBatch);
            pShield.ActivateCollisionSprite(this.pCollisionSpriteBatch);

            return pShield;
        }

    }
}
