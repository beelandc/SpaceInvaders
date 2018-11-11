using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class AlienGridMovementSound : Command
    {
        private int currSoundIndex;
        private const String pFastInvader1 = "fastInvader1.wav";
        private const String pFastInvader2 = "fastInvader2.wav";
        private const String pFastInvader3 = "fastInvader3.wav";
        private const String pFastInvader4 = "fastInvader4.wav";

        public AlienGridMovementSound()
        {
            this.currSoundIndex = 0;
        }

        public override void Execute(float deltaTime)
        {
            String pCurrSound;

            // Select sound
            switch (currSoundIndex)
            {
                case 0:
                    pCurrSound = pFastInvader1;
                    break;

                case 1:
                    pCurrSound = pFastInvader2;
                    break;

                case 2:
                    pCurrSound = pFastInvader3;
                    break;

                case 3:
                    pCurrSound = pFastInvader4;
                    break;

                default:
                    // something is wrong
                    Debug.Assert(false);
                    pCurrSound = "";
                    break;
            }

            // Play current sound
            IrrKlang.ISound pSnd = SoundEngineManager.GetSoundEngine().Play2D(pCurrSound);

            // Increment sound index
            if (currSoundIndex == 3)
            {
                this.currSoundIndex = 0;
            }
            else
            {
                this.currSoundIndex += 1;
            }

            // If there is still at least one alien
             if(GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.AlienGrid).GetFirstChild() != null)
            {
                Debug.Print("Adding GridMovementSound ({0}) back to timer - Delta: {1}", this.GetHashCode(), GameStateManager.GetGame().GetStateAlienGridSpeed());

                // Add itself back to timer
                TimerManager.Add(TimeEvent.Name.AlienGridMovementSound, this, GameStateManager.GetGame().GetStateAlienGridSpeed());
            }
            
        }
    }
}
