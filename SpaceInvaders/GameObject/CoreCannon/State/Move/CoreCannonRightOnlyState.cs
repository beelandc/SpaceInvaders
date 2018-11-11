using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class CoreCannonRightOnlyState : CoreCannonMoveState
    {
        public override void Handle(CoreCannon pCoreCannon)
        {
            // Do Nothing - Observer will change state
        }

        public override void MoveRight(CoreCannon pCoreCannon)
        {
            pCoreCannon.SetX(pCoreCannon.GetX() + pCoreCannon.GetShipSpeed());
        }

        public override void MoveLeft(CoreCannon pCoreCannon)
        {
            // Do Nothing - Right Only
        }

    }

}
