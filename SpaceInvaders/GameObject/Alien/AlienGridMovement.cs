using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class AlienGridMovement : Command
    {
        private Component pComponent;

        public AlienGridMovement(Component component)
        {
            this.pComponent = component;
            Debug.Assert(this.pComponent != null);
        }

        public AlienGridMovement(Composite.CompositeName compositeName)
        {
            // Find the composite that the movement is attached to
            this.pComponent = GameStateManager.GetGame().GetStateCompositeManager().Find(compositeName);
            Debug.Assert(this.pComponent != null);
        }

        public override void Execute(float deltaTime)
        {
            // Update X, Y in Component
            // Movement Deltas are now being handled in AlienGrid
            this.pComponent.Move(0.0f, 0.0f);

            Debug.Print("Adding AlienGridMovement ({0}) back to timer - Delta: {1}", this.GetHashCode(), GameStateManager.GetGame().GetStateAlienGridSpeed());

            // Add itself back to timer
            TimerManager.Add(TimeEvent.Name.AlienGridMovementSound, this, GameStateManager.GetGame().GetStateAlienGridSpeed());
        }
    }
}
