using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class InvaderKilledSoundObserver : CollisionObserver
    {
        public InvaderKilledSoundObserver()
        {
            // Do nothing at this layer
        }

        public override void Notify()
        {
            IrrKlang.ISound pSnd = SoundEngineManager.GetSoundEngine().Play2D("invaderkilled.wav");
        }
    }
}
