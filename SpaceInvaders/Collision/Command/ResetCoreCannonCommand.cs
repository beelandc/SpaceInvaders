using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ResetCoreCannonCommand : Command
    {
        public override void Execute(float deltaTime)
        {
            GhostManager pGhostManager = GameStateManager.GetGame().GetStateGhostManager();
            GameObjectManager pGameObjectManager = GameStateManager.GetGame().GetStateGameObjectManager();
            SpriteBatchManager pSpriteBatchManager = GameStateManager.GetGame().GetStateSpriteBatchManager();
            

            Composite pCoreCannonGroup = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.CoreCannonGroup);
            GameObject pGameObj = pGhostManager.Find(GameObject.Name.CoreCannon);

            // Remove game object from ghost manager
            pGhostManager.Detach(pGameObj);

            // Reset position
            pGameObj.SetX(200);
            pGameObj.SetY(100);

            // Reset Collision Object
            Azul.Rect pSpriteProxyScreenRect = pGameObj.GetSpriteProxy().GetSpriteScreenRect();
            CollisionObject pCollisionObject = pGameObj.GetCollisionObject();
            pCollisionObject.GetCollisionRect().Set(pSpriteProxyScreenRect);
            pCollisionObject.GetCollisionSpriteBox().Set(SpriteBox.Name.Box, 200, 100, pSpriteProxyScreenRect.width, pSpriteProxyScreenRect.height);

            // Add to GameObjectManager
            Debug.Assert(pGameObj != null);
            pGameObjectManager.Attach(pGameObj);

            // Add to Composite
            pCoreCannonGroup.Add(pGameObj);

            // Attach to SpriteBatch
            pGameObj.ActivateSprite(pSpriteBatchManager.Find(SpriteBatch.Name.CoreCannon));
            pGameObj.ActivateCollisionSprite(pSpriteBatchManager.Find(SpriteBatch.Name.CollisionBox));
        }
    }
}
