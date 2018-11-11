using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class GridWallCollisionObserver : CollisionObserver
    {
        public override void Notify()
        {
            //Debug.WriteLine("    Grid_Wall_Observer: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            AlienGrid pAlienGrid = (AlienGrid)this.pSubject.pObjA;

            // If AlienGrid is not already changing directions
            if (!pAlienGrid.GetSwitchXDirection() && !pAlienGrid.IsDirectionChangePending())
            {
                // Tell AlienGrid to move down and change directions
                pAlienGrid.SetSwitchXDirection(true);
            }
            
        }
    }
}
