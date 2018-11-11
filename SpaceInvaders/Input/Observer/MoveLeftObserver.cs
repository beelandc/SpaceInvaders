using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class MoveLeftObserver : InputObserver
    {
        public override void Notify()
        {
            //Debug.WriteLine("Move Left");
            CoreCannon pCoreCannon = GameStateManager.GetGame().GetStateCoreCannonManager().GetShip();
            pCoreCannon.MoveLeft();
        }
    }
}
