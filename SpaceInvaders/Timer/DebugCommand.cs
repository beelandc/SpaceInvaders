using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class DebugCommand : Command
    {
        public override void Execute(float deltaTime)
        {
            Debug.Print("******************************************Debug TimerEvent: deltaTime = {0}", deltaTime);

            // Add itself back to timer
            TimerManager.Add(TimeEvent.Name.DebugCommand, this, deltaTime);
        }
    }
}
