using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class MissileMovement : Command
    {
        private GameObject pGameObject;
        private float deltaY;
        private float currX = 0.0f;
        private float currY = 0.0f;
        private float spriteProxyHalfWidth = 0.0f;
        private Azul.Rect spriteProxyRect;

        public MissileMovement(GameObject.Name gameObjectName, float deltaY)
        {
            // Find the sprite that the movement is attached to
            this.pGameObject = GameStateManager.GetGame().GetStateGameObjectManager().Find(gameObjectName);
            Debug.Assert(this.pGameObject != null);

            this.deltaY = deltaY;
        }

        public void UpdateYDelta(float newDeltaY)
        {
            this.deltaY = newDeltaY;
        }

        public override void Execute(float deltaTime)
        {
            spriteProxyRect = this.pGameObject.GetSpriteProxy().GetSpriteScreenRect();
            spriteProxyHalfWidth = spriteProxyRect.width / 2.0f;

            this.currX = this.pGameObject.GetX();
            this.currY = this.pGameObject.GetY() + this.deltaY;

            // If past window top, reset
            if (this.currY > 896.0f)
            {
                // Set to Ship location + some Y 
                CoreCannon ship = (CoreCannon)GameStateManager.GetGame().GetStateGameObjectManager().Find(GameObject.Name.CoreCannon);
                this.currX = ship.GetX();
                this.currY = ship.GetY() + 20.0f;
            }
            
            // Update X, Y in Sprite
            this.pGameObject.SetX(this.currX);
            this.pGameObject.SetY(this.currY);

            // Add itself back to timer
            TimerManager.Add(TimeEvent.Name.MissileMovement, this, deltaTime);
        }

    }
}
