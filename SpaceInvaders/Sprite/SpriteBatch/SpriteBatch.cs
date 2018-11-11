using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteBatch : DLink
    {
        private Name name;
        private SpriteBaseRefManager pSBRefManager;
        private bool draw = true;

        public enum Name
        {
            Alien,
            CoreCannon,
            CollisionBox,
            Uninitialized,
            Missile,
            Wall,
            Shield,
            Bomb,
            FlyingSaucer,
            GameTexts,
            AttractTexts,
            Attract_Aliens,
            Player1Texts,
            EndTexts,
            Player2Texts
        }

        public SpriteBatch()
            : base()
        {
            this.name = SpriteBatch.Name.Uninitialized;
            this.draw = true;

            this.pSBRefManager = new SpriteBaseRefManager();
            Debug.Assert(this.pSBRefManager != null);
        }

        ~SpriteBatch()
        {
            this.name = Name.Uninitialized;
            this.pSBRefManager = null;
        }

        public void Destroy()
        {
            Debug.Assert(this.pSBRefManager != null);
            this.pSBRefManager.Destroy();
        }

        public void Set(SpriteBatch.Name name, int reserveNum = 3, int reserveGrow = 1)
        {
            this.name = name;
            this.pSBRefManager.Set(name, reserveNum, reserveGrow);
        }

        public SpriteBaseRef Attach(Sprite.Name name)
        {
            SpriteBaseRef pNode = this.pSBRefManager.Attach(name);
            return pNode;
        }

        public SpriteBaseRef Attach(SpriteBox.Name name)
        {
            SpriteBaseRef pNode = this.pSBRefManager.Attach(name);
            return pNode;
        }

        public SpriteBaseRef Attach(SpriteProxy pNode)
        {
            Debug.Assert(this.pSBRefManager != null);
            SpriteBaseRef pSBRef = this.pSBRefManager.Attach(pNode);
            return pSBRef;
        }

        public SpriteBaseRef Attach(SpriteBox pNode)
        {
            Debug.Assert(this.pSBRefManager != null);
            SpriteBaseRef pSBRef = this.pSBRefManager.Attach(pNode);
            return pSBRef;
        }

        public SpriteBaseRef Attach(FontSprite pNode)
        {
            Debug.Assert(this.pSBRefManager != null);
            SpriteBaseRef pSBRef = this.pSBRefManager.Attach(pNode);
            return pSBRef;
        }

        public void Detach(SpriteProxy pNode)
        {
            Debug.Assert(pNode != null);
            this.pSBRefManager.Detach(pNode);
        }

        public void Detach(SpriteBox pNode)
        {
            Debug.Assert(pNode != null);
            this.pSBRefManager.Detach(pNode);
        }

        public void SetName(SpriteBatch.Name inName)
        {
            this.name = inName;
        }

        public SpriteBatch.Name GetName()
        {
            return this.name;
        }

        public SpriteBaseRefManager GetSpriteBaseRefManager()
        {
            return this.pSBRefManager;
        }

        public void SetDraw(bool inDraw)
        {
            this.draw = inDraw;
        }

        public bool GetDraw()
        {
            return draw;
        }

        public new void Print()
        {
            Debug.WriteLine("******** SPRITE BATCH - SpriteBaseRef Manager *****************");
            this.pSBRefManager.PrintStats();
            this.pSBRefManager.PrintNodes();
        }

    }
}
