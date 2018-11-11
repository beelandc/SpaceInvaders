using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class RemoveBombObserver : CollisionObserver
    {
        private GameObject pBomb;
        private Random r = new Random();

        public RemoveBombObserver()
        {
            this.pBomb = null;
        }

        public RemoveBombObserver(RemoveBombObserver m)
        {
            Debug.Assert(m.pBomb != null);
            this.pBomb = m.pBomb;
        }

        public override void Notify()
        {
            //Debug.WriteLine("RemoveBombObserver: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            if ((this.pSubject.pObjA.GetType() == typeof(Bomb)) 
                || (this.pSubject.pObjA.GetType() == typeof(FlyingSaucerBomb)))
            {
                this.pBomb = (Bomb)this.pSubject.pObjA;
            }
            else
            {
                this.pBomb = (Bomb)this.pSubject.pObjB;
            }
            

            if (!pBomb.IsMarkedForDeath())
            {
                pBomb.MarkForDeath();

                // Delay - remove object later
                RemoveBombObserver pObserver = new RemoveBombObserver(this);
                GameStateManager.GetGame().GetStateDelayedObjectManager().Attach(pObserver);
            }
            
        }

        public override void ExecuteDelayed()
        {
            // Let the gameObject deal with this... 
            this.pBomb.Remove(SpriteBatch.Name.Bomb, SpriteBatch.Name.CollisionBox);

            // Remove from BombRoot
            Composite pBombRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.BombRoot);
            pBombRoot.Remove(this.pBomb);

            // Add a time event to drop next bomb
            if (pBomb.GetType() == typeof(FlyingSaucerBomb))
            {
                TimerManager.Add(TimeEvent.Name.DropBomb, new DropFlyingSaucerBombCommand(), 1.5f);
            }
            else
            {
                // Rate-limit bombs -- TODO refactor
                if (GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.BombRoot).GetNumOfChildren() < 6)
                {
                    TimerManager.Add(TimeEvent.Name.DropBomb, new DropBombCommand(), 0.2f);
                }
            }
        }

    }
}
