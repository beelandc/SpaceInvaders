using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class DropFlyingSaucerBombCommand : Command
    {
        Random r = new Random();

        public override void Execute(float deltaTime)
        {
            Composite pFlyingSaucerRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.FlyingSaucerRoot);

            // If Flying Saucer is on screen
            if (pFlyingSaucerRoot.GetFirstChild() != null)
            {
                Composite pBombRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.BombRoot);

                // Identify random Column + starting location
                int randomColIndex = r.Next(0, pFlyingSaucerRoot.GetNumOfChildren());
                FlyingSaucer pFlyingSaucer = (FlyingSaucer) pFlyingSaucerRoot.GetFirstChild();
                Azul.Rect pFlyingSaucerColCollisionRect = pFlyingSaucer.GetCollisionObject().GetCollisionRect();

                float xPos = (pFlyingSaucerColCollisionRect.x + (pFlyingSaucerColCollisionRect.width / 2.0f));
                float yPos = (pFlyingSaucerColCollisionRect.y - (pFlyingSaucerColCollisionRect.height / 2.0f));

                // Identify random Bomb Type and create bomb
                FallStrategy bombStrategy = null;
                int bombType = r.Next(0, 3);

                if (bombType == 0)
                {
                    bombType = (int)Sprite.Name.BombStraight;
                    bombStrategy = new FallStraight();
                }
                else if (bombType == 1)
                {
                    bombType = (int)Sprite.Name.BombZigZag;
                    bombStrategy = new FallZigZag();
                }
                else
                {
                    bombType = (int)Sprite.Name.BombCross;
                    bombStrategy = new FallDagger();
                }

                // TODO refactor to use Object Pool
                FlyingSaucerBomb pBomb = new FlyingSaucerBomb(GameObject.Name.FlyingSaucerBomb, (Sprite.Name)bombType, bombStrategy, xPos, yPos);

                // Attach to BombRoot, SpriteBatches, and any related managers
                GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.BombRoot).Add(pBomb);
                GameStateManager.GetGame().GetStateGameObjectManager().Attach(pBomb);
                pBomb.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));
                pBomb.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Bomb));
            }
        }
    }
}
