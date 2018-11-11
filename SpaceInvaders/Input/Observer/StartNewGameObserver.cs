using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class StartNewGameObserver : ChangeGameStateObserver
    {
        private bool twoPlayerMode;

        public StartNewGameObserver(bool isTwoPlayer)
        {
            this.twoPlayerMode = isTwoPlayer;
        }

        public override void Notify()
        {
            GameStateManager.GetGame().SetTwoPlayerMode(twoPlayerMode);
            base.Notify();
        }
    }
}
