using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class SpriteAnimation : Command
    {
        private Name name;
        private Sprite pSprite;
        private SLink pFirstImage;
        private SLink pCurrImage;

        public SpriteAnimation()
            : base()
        {
            this.name = SpriteAnimation.Name.Uninitialized;
            this.pSprite = null;
            this.pFirstImage = null;
            this.pCurrImage = null;
        }

        public SpriteAnimation(SpriteAnimation.Name animationName, Sprite.Name spriteName)
        {
            // list
            this.pFirstImage = null;
            this.pCurrImage = null;

            Set(animationName, spriteName);
        }

        public void Set(SpriteAnimation.Name animationName, Sprite.Name spriteName)
        {
            this.name = animationName;
            
            // Find the sprite that the animation is attached to
            this.pSprite = SpriteManager.Find(spriteName);
            Debug.Assert(this.pSprite != null);
        }

        public void Attach(Image.Name imageName)
        {
            // Get the image
            Image pImage = ImageManager.Find(imageName);
            Debug.Assert(pImage != null);

            // Create a new reference container
            ImageRef pImageRef = new ImageRef(pImage);
            Debug.Assert(pImageRef != null);

            // Attach to the Sprite Animation
            SLink.AddToFront(ref this.pFirstImage, pImageRef);

            // Set as current image
            this.pCurrImage = pImageRef;
        }

        public override void Execute(float deltaTime)
        {
            // Advance to next image 
              ImageRef pImageRef = (ImageRef)this.pCurrImage.GetNext();

            // if at end of list, set to first
            if (pImageRef == null)
            {
                pImageRef = (ImageRef)pFirstImage;
            }

            // Set as current image
            this.pCurrImage = pImageRef;

            // Update image in Sprite
            this.pSprite.SwapImage(pImageRef.GetImage());

            Debug.Print("Adding SpriteAnimation ({0}) back to timer - Delta: {1}", this.GetHashCode(), GameStateManager.GetGame().GetStateAlienGridSpeed());

            // Add itself back to timer
            TimerManager.Add(TimeEvent.Name.SpriteAnimation, this, GameStateManager.GetGame().GetStateAlienGridSpeed());
            
        }

        public void SetName(SpriteAnimation.Name name)
        {
            this.name = name;
        }

        public void Destroy()
        {
            pCurrImage = null;
            pSprite = null;
            ClearImageList(pFirstImage);
            pFirstImage = null;
        }

        private void ClearImageList(SLink listHead)
        {
            SLink pNode;
            SLink pTmpNode;

            pNode = listHead;
            while (pNode != null)
            {
                // Walk through the list
                pTmpNode = pNode;
                pNode = pNode.GetNext();

                // Clear and remove node
                Debug.Assert(pTmpNode != null);
                DestroyImageRef((ImageRef)pTmpNode);
                pTmpNode = null;
            }
        }

        private void DestroyImageRef(ImageRef pLink)
        {
            ImageRef pImageRef = (ImageRef)pLink;
            pImageRef.SetImage(null);
            pImageRef.SetNext(null);
        }

        public Name GetName()
        {
            return this.name;
        }

        public enum Name
        {
            CrabAlien,
            SquidAlien,
            JellyfishAlien,
            Uninitialized
        };
    }
}
