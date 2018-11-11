using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class CoreCannonMissileFlyingState : CoreCannonMissileState
    {
        public override void Handle(CoreCannon pShip)
        {
            // Do Nothing - Observer will change state
        }

        public override void ShootMissile(CoreCannon pShip)
        {
            // Do Nothing
        }

    }
}
