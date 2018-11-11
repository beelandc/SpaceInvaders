using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteBaseRefManager : Manager
    {
        private SpriteBaseRef pSpriteBaseRefCompare;
        private SpriteBatch.Name name;

        public SpriteBaseRefManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            // initialize derived data here
            this.pSpriteBaseRefCompare = new SpriteBaseRef();
        }

        ~SpriteBaseRefManager()
        {
            this.name = SpriteBatch.Name.Uninitialized;
            this.pSpriteBaseRefCompare = null;
        }

        //----------------------------------------------------------------------
        // Methods
        //----------------------------------------------------------------------
        public void Destroy()
        {
            this.BaseDestroy();

            this.name = SpriteBatch.Name.Uninitialized;
            this.pSpriteBaseRefCompare = null;
        }

        public void Set(SpriteBatch.Name name, int reserveNum, int reserveGrow)
        {
            this.name = name;

            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            this.BaseSetReserve(reserveNum, reserveGrow);
        }

        public SpriteBaseRef Attach(Sprite.Name name)
        {
            SpriteBaseRef pNode = (SpriteBaseRef)this.BaseAdd();
            Debug.Assert(pNode != null);

            // Initialize SpriteBase Node
            pNode.Set(name);

            return pNode;
        }
        public SpriteBaseRef Attach(SpriteBox.Name name)
        {

            SpriteBaseRef pNode = (SpriteBaseRef)this.BaseAdd();
            Debug.Assert(pNode != null);

            // Initialize SpriteBase Node
            pNode.Set(name);

            return pNode;
        }

        public SpriteBaseRef Attach(SpriteProxy pNode)
        {
            SpriteBaseRef pSBNode = (SpriteBaseRef)this.BaseAdd();
            Debug.Assert(pSBNode != null);

            // Initialize SpriteBaseRef
            pSBNode.Set(pNode);

            return pSBNode;
        }

        public SpriteBaseRef Attach(SpriteBox pNode)
        {
            SpriteBaseRef pSBNode = (SpriteBaseRef)this.BaseAdd();
            Debug.Assert(pSBNode != null);

            // Initialize SpriteBaseRef
            pSBNode.Set(pNode);

            return pSBNode;
        }

        public SpriteBaseRef Attach(FontSprite pNode)
        {
            SpriteBaseRef pSBNode = (SpriteBaseRef)this.BaseAdd();
            Debug.Assert(pSBNode != null);

            // Initialize SpriteBaseRef
            pSBNode.Set(pNode);

            return pSBNode;
        }

        public void Detach(SpriteProxy pNode)
        {
            Debug.Assert(pNode != null);
            this.pSpriteBaseRefCompare.SetSpriteBase(pNode);
            DLink pFoundNode = this.BaseFind(this.pSpriteBaseRefCompare);

            Debug.Assert(pFoundNode != null);
            this.BaseRemove(pFoundNode);
        }

        public void Detach(SpriteBox pNode)
        {
            Debug.Assert(pNode != null);
            this.pSpriteBaseRefCompare.SetSpriteBase(pNode);
            DLink pFoundNode = this.BaseFind(this.pSpriteBaseRefCompare);

            Debug.Assert(pFoundNode != null);
            this.BaseRemove(pFoundNode);
        }

        public void Draw()
        {
            // walk through the list and render
            SpriteBaseRef pNode = (SpriteBaseRef)this.BaseGetActive();

            while (pNode != null)
            {
                // Assumes update() has been called on each sprite
                pNode.GetSpriteBase().Render();

                pNode = (SpriteBaseRef)pNode.GetNext();
            }
        }

        public void Remove(SpriteBaseRef pNode)
        {
            Debug.Assert(pNode != null);
            this.BaseRemove(pNode);
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new SpriteBaseRef();
            Debug.Assert(pNode != null);

            return pNode;
        }
        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            SpriteBaseRef pDataA = (SpriteBaseRef)pLinkA;
            SpriteBaseRef pDataB = (SpriteBaseRef)pLinkB;

            Boolean status = false;

            // TODO Add Comparison Functionality
            if (pDataB.GetSpriteBase().GetHashCode() == pDataA.GetSpriteBase().GetHashCode())
            {
                status = true;
            }
            else
            {
                status = false;
            }

            return status;
        }
        override protected void DerivedDestroyNode(DLink pLink)
        {
            SpriteBaseRef pSpriteBaseRef = (SpriteBaseRef)pLink;
            pSpriteBaseRef.SetSpriteBase(null);
        }

    }
}
