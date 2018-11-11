using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class CoreCannonBumperCollisionObserver : CollisionObserver
    {
        public override void Notify()
        {
            //Debug.WriteLine("    CoreCannon_Wall_Observer: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            CoreCannon pCoreCannon = (CoreCannon)this.pSubject.pObjA;
            Wall pWall = (Wall)this.pSubject.pObjB;

            if (pWall.GetType() == typeof(LeftBumper))
            {
                pCoreCannon.SetMoveState(CoreCannonManager.MoveState.RightOnly);
            }
            else if (pWall.GetType() == typeof(RightBumper))
            {
                pCoreCannon.SetMoveState(CoreCannonManager.MoveState.LeftOnly);
            } else
            {
                Debug.Print("Unexpected Wall type in CoreCannon_Wall_Observer");
                Debug.Assert(false);
            }

            
        }
    }
}
