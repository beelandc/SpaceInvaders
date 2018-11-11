using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class CoreCannonLeftOnlyState : CoreCannonMoveState
    {
        public override void Handle(CoreCannon pCoreCannon)
        {
            // Do Nothing - Observer will change state
        }

        public override void MoveRight(CoreCannon pCoreCannon)
        {
            // Do Nothing - Left only
        }

        public override void MoveLeft(CoreCannon pCoreCannon)
        {
            pCoreCannon.SetX(pCoreCannon.GetX() - pCoreCannon.GetShipSpeed());
        }
    }
}
