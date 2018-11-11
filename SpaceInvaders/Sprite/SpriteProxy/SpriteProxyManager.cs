using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class SpriteProxyManager : Manager
    {
        private static SpriteProxyManager pInstance = null;
        private SpriteProxy pSpriteProxyCompare;

        //---------------------------------------------------------------------------------------------------------
        // Constructors / Destructor
        //---------------------------------------------------------------------------------------------------------
        private SpriteProxyManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pSpriteProxyCompare = new SpriteProxy();
        }

        ~SpriteProxyManager()
        {
            this.pSpriteProxyCompare = null;
            SpriteProxyManager.pInstance = null;
        }

        //----------------------------------------------------------------------
        // Static Methods
        //----------------------------------------------------------------------
        public static void Create(int reserveNum = 3, int reserveGrow = 1)
        {
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            Debug.Assert(pInstance == null);

            if (pInstance == null)
            {
                pInstance = new SpriteProxyManager(reserveNum, reserveGrow);
            }
        }

        public static void Destroy()
        {
            // Get the instance
            SpriteProxyManager pInstance = SpriteProxyManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.BaseDestroy();

            pInstance.pSpriteProxyCompare = null;
            SpriteProxyManager.pInstance = null;
        }

        public static SpriteProxy Add(SpriteProxy.Name proxyName, Sprite.Name spriteName)
        {
            SpriteProxyManager pInstance = SpriteProxyManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            SpriteProxy pNode = (SpriteProxy)pInstance.BaseAdd();
            Debug.Assert(pNode != null);

            pNode.Set(proxyName, spriteName);

            return pNode;
        }
        public static SpriteProxy Find(SpriteProxy.Name name)
        {
            SpriteProxyManager pInstance = SpriteProxyManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Use compare node to compare to search nodes
            pInstance.pSpriteProxyCompare.SetName(name);

            SpriteProxy pData = (SpriteProxy)pInstance.BaseFind(pInstance.pSpriteProxyCompare);
            return pData;
        }
        public static void Remove(Sprite pNode)
        {
            SpriteProxyManager pMan = SpriteProxyManager.PrivGetInstance();
            Debug.Assert(pMan != null);
            Debug.Assert(pNode != null);
            pMan.BaseRemove(pNode);
        }
        public static void Print()
        {
            SpriteProxyManager pInstance = SpriteProxyManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.WriteLine("******** SPRITE PROXY MANAGER ****************");
            pInstance.PrintStats();
            pInstance.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new SpriteProxy();
            Debug.Assert(pNode != null);

            return pNode;
        }
        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            SpriteProxy pDataA = (SpriteProxy)pLinkA;
            SpriteProxy pDataB = (SpriteProxy)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            SpriteProxy pSpriteProxy = (SpriteProxy)pLink;
            pSpriteProxy.SetSprite(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static SpriteProxyManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);

            return pInstance;
        }
        
    }
}
