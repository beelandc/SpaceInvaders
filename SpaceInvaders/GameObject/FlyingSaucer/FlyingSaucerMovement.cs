using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class FlyingSaucerMovement : SpriteMovement
    {

        public FlyingSaucerMovement(GameObject.Name gameObjectName, float deltaX, float deltaY)
            : base(gameObjectName, deltaX, deltaY)
        {
        }

        public override void Execute(float deltaTime)
        {
            Composite pFlyingSaucerRoot = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.FlyingSaucerRoot);
            IrrKlang.ISoundEngine pSoundEngine = SoundEngineManager.GetSoundEngine();

            // If flying saucer is active
            if(pFlyingSaucerRoot.GetFirstChild() != null)
            {
                // Check to see if sound is currently playing
                if (!pSoundEngine.IsCurrentlyPlaying("ufo_lowpitch.wav"))
                {
                    IrrKlang.ISound pSnd = pSoundEngine.Play2D("ufo_lowpitch.wav");
                }

                base.Execute(deltaTime);
            }
        }

    }
}
