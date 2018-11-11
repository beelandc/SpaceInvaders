using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class SpriteMovement : Command
    {
        private GameObject pGameObject;
        private float deltaX;
        private float deltaY;
        private float currX = 0.0f;
        private float currY = 0.0f;
        private float spriteProxyHalfWidth = 0.0f;
        private Azul.Rect spriteProxyRect;

        public SpriteMovement(GameObject.Name gameObjectName, float deltaX, float deltaY)
        {
            // Find the sprite that the movement is attached to
            this.pGameObject = GameStateManager.GetGame().GetStateGameObjectManager().Find(gameObjectName);
            Debug.Assert(this.pGameObject != null);

            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        public void UpdateXDelta(float newDeltaX)
        {
            this.deltaX = newDeltaX;
        }

        public void UpdateYDelta(float newDeltaY)
        {
            this.deltaY = newDeltaY;
        }

        public override void Execute(float deltaTime)
        {
            // TODO add parameters for border edges [Left, Right, Top, Bottom] for when to reset movement
            // TODO Probably need to create sub-commands for different types of movement?

            spriteProxyRect = this.pGameObject.GetSpriteProxy().GetSpriteScreenRect();
            spriteProxyHalfWidth = spriteProxyRect.width / 2.0f;

            // + ( (deltaX > 0.0f) ? spriteProxyHalfWidth : (-1.0f * spriteProxyHalfWidth))
            this.currX = this.pGameObject.GetX() + this.deltaX;
            this.currY = this.pGameObject.GetY() + this.deltaY;

            // If past window edge, change directions
            if ((this.currX + spriteProxyHalfWidth) > 896.0f)
            {
                deltaX = (-1.0f * deltaX);
                currX = 896.0f - spriteProxyHalfWidth;
            } else if ((this.currX + -(spriteProxyHalfWidth)) < 0.0f)
            {
                deltaX = (-1.0f * deltaX);
                currX = 0.0f + spriteProxyHalfWidth;
            }

            // Update X, Y in Sprite
            this.pGameObject.SetX(this.currX);
            this.pGameObject.SetY(this.currY);

            // Add itself back to timer
            TimerManager.Add(TimeEvent.Name.SpriteMovement, this, deltaTime);
        }

    }
}
