using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract public class CoreCannonMissileState
    {
        // state()
        public abstract void Handle(CoreCannon pCoreCannon);

        // strategy()
        public abstract void ShootMissile(CoreCannon pCoreCannon);

    }
}
