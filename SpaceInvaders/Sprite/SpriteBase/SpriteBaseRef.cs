using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteBaseRef : DLink
    {
        private SpriteBase pSpriteBase;

        public SpriteBaseRef()
            : base()
        {
            this.pSpriteBase = null;
        }

        ~SpriteBaseRef()
        {
            this.pSpriteBase = null;
        }

        public void Set(Sprite.Name name)
        {
            // Go find it
            this.pSpriteBase = SpriteManager.Find(name);
            Debug.Assert(this.pSpriteBase != null);
        }

        public void Set(SpriteBox.Name name)
        {
            this.pSpriteBase = SpriteBoxManager.Find(name);
            Debug.Assert(this.pSpriteBase != null);
        }

        public void Set(SpriteBase pNode)
        {
            Debug.Assert(pNode != null);
            this.pSpriteBase = pNode;
        }

        public SpriteBase GetSpriteBase()
        {
            return this.pSpriteBase;
        }

        public void SetSpriteBase(SpriteBase newSpriteBase)
        {
            this.pSpriteBase = newSpriteBase;
        }

        public override void Clean()
        {
            base.Clean();
            this.pSpriteBase = null;
        }
        
    }
}
