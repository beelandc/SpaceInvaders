using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class SpriteAnimationManager : Manager
    {
        private static SpriteAnimationManager pInstance = null;
        private SpriteAnimation poNodeCompare;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private SpriteAnimationManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.poNodeCompare = new SpriteAnimation();
        }

        //----------------------------------------------------------------------
        // Static Methods
        //----------------------------------------------------------------------
        public static void Create(int reserveNum = 3, int reserveGrow = 1)
        {
            // make sure values are ressonable 
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            // initialize the singleton here
            Debug.Assert(pInstance == null);

            // Do the initialization
            if (pInstance == null)
            {
                pInstance = new SpriteAnimationManager(reserveNum, reserveGrow);
            }


        }
        public static void Destroy()
        {
            SpriteAnimationManager pInstance = SpriteAnimationManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.BaseDestroy();

            pInstance.poNodeCompare = null;
            SpriteAnimationManager.pInstance = null;

        }

        public static SpriteAnimation Add(SpriteAnimation.Name animationName, Sprite.Name spriteName)
        {
            SpriteAnimationManager pMan = SpriteAnimationManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            SpriteAnimation pNode = (SpriteAnimation)pMan.BaseAdd();
            Debug.Assert(pNode != null);

            pNode.Set(animationName, spriteName);
            return pNode;
        }

        public static SpriteAnimation Find(SpriteAnimation.Name name)
        {
            SpriteAnimationManager pInstance = SpriteAnimationManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Use compare node to compare to search nodes
            pInstance.poNodeCompare.SetName(name);

            SpriteAnimation pData = (SpriteAnimation)pInstance.BaseFind(pInstance.poNodeCompare);
            return pData;
        }

        public static void Remove(SpriteAnimation pNode)
        {
            SpriteAnimationManager pInstance = SpriteAnimationManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.Assert(pNode != null);
            pInstance.BaseRemove(pNode);
        }

        public static void Print()
        {
            SpriteAnimationManager pInstance = SpriteAnimationManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.WriteLine("******** SPRITE ANIMATION MANAGER ***********");
            pInstance.PrintStats();
            pInstance.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new SpriteAnimation();
            Debug.Assert(pNode != null);

            return pNode;
        }
        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            SpriteAnimation pDataA = (SpriteAnimation)pLinkA;
            SpriteAnimation pDataB = (SpriteAnimation)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }
        override protected void DerivedDestroyNode(DLink pLink)
        {
            SpriteAnimation pNode = (SpriteAnimation)pLink;
            Debug.Assert(pNode != null);
            pNode.Destroy();
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static SpriteAnimationManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);

            return pInstance;
        }

    }
}
