using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ChangeGameStateObserver : InputObserver
    {
        public override void Notify()
        {
            GameStateManager.GetGame().SetStateChangeFlag(true);
            
        }
    }
}
