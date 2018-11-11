using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class CoreCannonReadyState : CoreCannonMissileState
    {
        public override void Handle(CoreCannon pCoreCannon)
        {
            pCoreCannon.SetMissileState(CoreCannonManager.MissileState.MissileFlying);
        }

        public override void ShootMissile(CoreCannon pShip)
        {
            // Activate and position missile
            Missile pMissile = GameStateManager.GetGame().GetStateCoreCannonManager().ActivateMissile();
            pMissile.SetPos(pShip.GetX(), pShip.GetY() + 20);
            pMissile.SetActive(true);

            // Play shoot sound
            IrrKlang.ISound pSnd = SoundEngineManager.GetSoundEngine().Play2D("shoot.wav");

            // switch states
            this.Handle(pShip);
        }

    }
}
