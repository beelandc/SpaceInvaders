using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class RemoveMissileObserver : CollisionObserver
    {
        private GameObject pMissile;

        public RemoveMissileObserver()
        {
            this.pMissile = null;
        }

        public RemoveMissileObserver(RemoveMissileObserver m)
        {
            Debug.Assert(m.pMissile != null);
            this.pMissile = m.pMissile;
        }

        public override void Notify()
        {
            //Debug.WriteLine("RemoveMissileObserver: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            if(this.pSubject.pObjA.GetType() == typeof(Missile))
            {
                this.pMissile = (Missile)this.pSubject.pObjA;
            } else
            {
                this.pMissile = (Missile)this.pSubject.pObjB;
            }
            

            //Debug.WriteLine("MissileRemoveObserver: --> delete missile {0}", pMissile);

            if (!pMissile.IsMarkedForDeath())
            {
                pMissile.MarkForDeath();

                // Delay - remove object later
                RemoveMissileObserver pObserver = new RemoveMissileObserver(this);
                GameStateManager.GetGame().GetStateDelayedObjectManager().Attach(pObserver);
            }


        }

        public override void ExecuteDelayed()
        {
            // Let the gameObject deal with this... 
            this.pMissile.Remove(SpriteBatch.Name.Missile, SpriteBatch.Name.CollisionBox);

            // Remove from MissileGroup
            Composite pMissileGroup = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.MissileGroup);
            pMissileGroup.Remove(this.pMissile);
        }

    }
}
