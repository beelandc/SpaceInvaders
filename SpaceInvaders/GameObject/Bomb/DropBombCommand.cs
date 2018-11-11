using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class DropBombCommand : Command
    {
        Random r = new Random();

        public override void Execute(float deltaTime)
        {
            Composite pAlienGrid = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.AlienGrid);

            // If Alien Grid has at least one column
            if (pAlienGrid.GetFirstChild() != null)
            {
                Composite pBombRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.BombRoot);

                // Identify random Column + starting location
                int randomColIndex = r.Next(0, pAlienGrid.GetNumOfChildren());
                Composite pAlienCol = pAlienGrid.GetChildByIndex(randomColIndex);
                Azul.Rect pAlienColCollisionRect = pAlienCol.GetCollisionObject().GetCollisionRect();

                float xPos = (pAlienColCollisionRect.x + (pAlienColCollisionRect.width / 2.0f));
                float yPos = (pAlienColCollisionRect.y - (pAlienColCollisionRect.height / 2.0f));

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
                Bomb pBomb = new Bomb(GameObject.Name.Bomb, (Sprite.Name)bombType, bombStrategy, xPos, yPos);

                // Attach to BombRoot, SpriteBatches, and any related managers
                GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.BombRoot).Add(pBomb);
                GameStateManager.GetGame().GetStateGameObjectManager().Attach(pBomb);
                pBomb.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));
                pBomb.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Bomb));

            }

        }

    }
}
