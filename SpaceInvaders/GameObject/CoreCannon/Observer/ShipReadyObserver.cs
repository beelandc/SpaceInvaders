using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class ShipReadyObserver : CollisionObserver
    {
        public override void Notify()
        {
            CoreCannon pShip = GameStateManager.GetGame().GetStateCoreCannonManager().GetShip();
            pShip.SetMissileState(CoreCannonManager.MissileState.Ready);
        }

    }
}
