using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ExplosionSoundObserver : CollisionObserver
    {
        public ExplosionSoundObserver()
        {
            // Do nothing at this layer
        }

        public override void Notify()
        {
            SoundEngineManager.GetSoundEngine().Play2D("explosion.wav");
        }
    }
}
