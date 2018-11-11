using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class RemoveFlyingSaucerObserver : CollisionObserver
    {
        private GameObject pAlien;
        private Random r = new Random();

        public RemoveFlyingSaucerObserver()
        {
            this.pAlien = null;

        }

        public RemoveFlyingSaucerObserver(RemoveFlyingSaucerObserver a)
        {
            this.pAlien = a.pAlien;
        }

        public override void Notify()
        {
            //Debug.WriteLine("RemoveFlyingSaucerObserver: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            // This cast will throw an exception if wrong
            this.pAlien = (FlyingSaucer)this.pSubject.pObjA;

            pAlien.GetCollisionObject().GetCollisionRect().Set(0, 0, 0, 0);
            pAlien.Update();

            if (!pAlien.IsMarkedForDeath())
            {
                pAlien.MarkForDeath();

                // Delay - remove object later
                RemoveFlyingSaucerObserver pObserver = new RemoveFlyingSaucerObserver(this);
                GameStateManager.GetGame().GetStateDelayedObjectManager().Attach(pObserver);
            }

            TimerManager.Add(TimeEvent.Name.DeployFlyingSaucer, new DeployFlyingSaucerCommand(), r.Next(30, 61));

        }

        public override void ExecuteDelayed()
        {
            // Let the gameObjects deal with this...

            if (this.pAlien != null)
            {
                this.pAlien.Remove(SpriteBatch.Name.FlyingSaucer, SpriteBatch.Name.CollisionBox);
            }

            // Remove from Root
            Composite pFlyingSaucerRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.FlyingSaucerRoot);
            pFlyingSaucerRoot.Remove(this.pAlien);

        }

    }
}
