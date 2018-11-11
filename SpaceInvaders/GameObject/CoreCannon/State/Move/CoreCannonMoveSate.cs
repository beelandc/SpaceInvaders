using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract public class CoreCannonMoveState
    {
        // state()
        public abstract void Handle(CoreCannon pCoreCannon);

        // strategy()
        public abstract void MoveRight(CoreCannon pCoreCannon);
        public abstract void MoveLeft(CoreCannon pCoreCannon);

    }
}
