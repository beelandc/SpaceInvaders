using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ResetLevelCommand : Command
    {
        

        public override void Execute(float deltaTime)
        {
            SpaceInvaders pGame = GameStateManager.GetGame();

            pGame.SetResetLevelFlag(false);

            // Ghost any remaining Shields
            ShieldGroup pShieldGroup = (ShieldGroup)GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.ShieldGroup);
            ShieldFactory.DestroyShields(pShieldGroup);

            // Ghost any remaining Aliens (Only necessary for Game reset)
            AlienGrid pAlienGrid = (AlienGrid)GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.AlienGrid);
            AlienFactory.DestroyAlienGrid(pAlienGrid);

            // Clear TimeEvents
            TimeEvent tEvent = TimerManager.Pop();
            while(tEvent != null)
            {
                // Kill any remaining TimerEvents
                tEvent = TimerManager.Pop();
            }

            // Recreate shields
            GameObject pShield;
            pShield = ShieldFactory.RecreateShield(110, 150);
            pShieldGroup.Add(pShield);
            pShield = ShieldFactory.RecreateShield(310, 150);
            pShieldGroup.Add(pShield);
            pShield = ShieldFactory.RecreateShield(510, 150);
            pShieldGroup.Add(pShield);
            pShield = ShieldFactory.RecreateShield(710, 150);
            pShieldGroup.Add(pShield);

            // Adjust Starting Speed
            float newStartingSpeed = pGame.GetStateLevelInitialAlienGridSpeed() - 0.05f;
            pGame.SetStateLevelInitialAlienGridSpeed(newStartingSpeed);
            pGame.SetStateAlienGridSpeed(newStartingSpeed);

            // Adjust Starting Position
            float newStartingYPosition = pGame.GetStateLevelInitialAlienGridYPosition() - 40.0f;
            pGame.SetStateLevelInitialAlienGridYPosition(newStartingYPosition);

            // Recreate AlienGrid
            AlienFactory.RecreateAlienGrid(pAlienGrid, newStartingSpeed, GameStateManager.GetGame().GetGameInitialGridXPosition(), newStartingYPosition);

            pGame.GetStateGameObjectManager().Print();
            pGame.GetStateSpriteBatchManager().Print();

            // Queue up Time Events
            QueuedTimeEventManager qtem = pGame.GetStateQueuedTimeEventManager();

            qtem.Enqueue(TimeEvent.Name.AlienGridMovementSound, new AlienGridMovementSound(), newStartingSpeed);
            qtem.Enqueue(TimeEvent.Name.SpriteAnimation, SpriteAnimationManager.Find(SpriteAnimation.Name.SquidAlien), newStartingSpeed);
            qtem.Enqueue(TimeEvent.Name.SpriteAnimation, SpriteAnimationManager.Find(SpriteAnimation.Name.CrabAlien), newStartingSpeed);
            qtem.Enqueue(TimeEvent.Name.SpriteAnimation, SpriteAnimationManager.Find(SpriteAnimation.Name.JellyfishAlien), newStartingSpeed);

            // Load Time Events into TimerManager
            QueuedTimeEvent qte = pGame.GetStateQueuedTimeEventManager().Dequeue();
            while (qte != null)
            {
                TimerManager.Add(qte.GetTimeEventName(), qte.GetCommand(), qte.GetTimeEventDelta());
                qte = pGame.GetStateQueuedTimeEventManager().Dequeue();
            }

        }
    }
}
