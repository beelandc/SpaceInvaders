using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class MoveRightObserver : InputObserver
    {
        public override void Notify()
        {
            //Debug.WriteLine("Move Right");
            CoreCannon pCoreCannon = GameStateManager.GetGame().GetStateCoreCannonManager().GetShip();
            pCoreCannon.MoveRight();
        }
    }
}
