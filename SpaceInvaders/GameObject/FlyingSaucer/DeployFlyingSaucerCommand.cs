using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class DeployFlyingSaucerCommand : Command
    {
        Random r = new Random();

        public DeployFlyingSaucerCommand()
        {
        } 

        public override void Execute(float deltaTime)
        {

            Composite pFlyingSaucerRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.FlyingSaucerRoot);

            // If there is not already a flying saucer on screen
            if (pFlyingSaucerRoot.GetFirstChild() == null)
            {
                // Identify random starting location (left or right)
                // 0 == Left, 1 == Right
                int randomStart = r.Next(0, 2);

                float xPos = 65 + (randomStart * 770.0f);
                float xDelta = (randomStart == 0) ? 10.0f : -10.0f;
                float yPos = 735.0f;

                FlyingSaucer pFlyingSaucer = new FlyingSaucer(GameObject.Name.FlyingSaucer, Sprite.Name.FlyingSaucer, xPos, yPos);
                GameStateManager.GetGame().GetStateGameObjectManager().Attach(pFlyingSaucer);
                GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.FlyingSaucerRoot).Add(pFlyingSaucer);
                pFlyingSaucer.ActivateSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.FlyingSaucer));
                pFlyingSaucer.ActivateCollisionSprite(GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox));

                FlyingSaucerMovement pFlyingSaucerMovement = new FlyingSaucerMovement(GameObject.Name.FlyingSaucer, xDelta, 0.0f);
                TimerManager.Add(TimeEvent.Name.SpriteMovement, pFlyingSaucerMovement, 0.1f);
                TimerManager.Add(TimeEvent.Name.DropBomb, new DropFlyingSaucerBombCommand(), (float)r.NextDouble());
            }

        }
    }
}