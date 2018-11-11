using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ShootObserver : InputObserver
    {

        public ShootObserver()
        {
        }

        public override void Notify()
        {
            //Debug.WriteLine("Shoot Observer");
            CoreCannon pShip = GameStateManager.GetGame().GetStateCoreCannonManager().GetShip();
            pShip.ShootMissile();

        }
    }
}