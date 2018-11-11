using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CollisionObject
    {
        protected SpriteBox pCollisionSprite;
        protected CollisionRect pCollisionRect;

        public CollisionObject(SpriteProxy pSpriteProxy)
        {
            Debug.Assert(pSpriteProxy != null);

            // Create Collision Rect
            // Use the reference sprite to set size and shape
            Sprite pSprite = pSpriteProxy.GetSprite();
            Debug.Assert(pSprite != null);

            // Origin is in the UPPER RIGHT 
            this.pCollisionRect = new CollisionRect(pSprite.GetScreenRect());
            Debug.Assert(this.pCollisionRect != null);

            // Create the sprite
            this.pCollisionSprite = SpriteBoxManager.Add(SpriteBox.Name.Box, this.pCollisionRect.x, this.pCollisionRect.y, this.pCollisionRect.width, this.pCollisionRect.height );
            Debug.Assert(this.pCollisionSprite != null);
            this.pCollisionSprite.SetLineColor(1.0f, 0.0f, 0.0f);

        }

        public void UpdatePos(float x, float y)
        {
            this.pCollisionRect.x = x;
            this.pCollisionRect.y = y;

            this.pCollisionSprite.SetX(this.pCollisionRect.x);
            this.pCollisionSprite.SetY(this.pCollisionRect.y);

            this.pCollisionSprite.SetScreenRect(this.pCollisionRect.x, this.pCollisionRect.y, this.pCollisionRect.width, this.pCollisionRect.height);
            this.pCollisionSprite.Update();
        }

        public CollisionRect GetCollisionRect()
        {
            return this.pCollisionRect;
        }

        public SpriteBox GetCollisionSpriteBox()
        {
            return this.pCollisionSprite;
        }
        
    }
}