using System;
using System.Diagnostics;

namespace SpaceInvaders
{ 
    class SpriteBoxManager : Manager
    {

        private static SpriteBoxManager pInstance;
        private SpriteBox pSpriteBoxCompare;

        private static Azul.Color pDefaultColor = new Azul.Color(0.9019f, 0.0784f, 0.0784f, 1.0f);

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private SpriteBoxManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pSpriteBoxCompare = new SpriteBox();
        }

        ~SpriteBoxManager()
        {
            this.pSpriteBoxCompare = null;
            SpriteBoxManager.pInstance = null;
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
                pInstance = new SpriteBoxManager(reserveNum, reserveGrow);
            }
        }
        public static void Destroy()
        {
            SpriteBoxManager pInstance = SpriteBoxManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.BaseDestroy();

            pInstance.pSpriteBoxCompare = null;
            SpriteBoxManager.pInstance = null;
        }

        public static SpriteBox Add(SpriteBox.Name name, float x, float y, float width, float height)
        {
            SpriteBoxManager pMan = SpriteBoxManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            SpriteBox pSpriteBox = (SpriteBox)pMan.BaseAdd();
            Debug.Assert(pSpriteBox != null);

            // Initialize the date
            pSpriteBox.Set(name, x, y, width, height, pDefaultColor);
            return pSpriteBox;
        }

        public static SpriteBox Add(SpriteBox.Name name, float x, float y, float width, float height, Azul.Color color)
        {
            SpriteBoxManager pMan = SpriteBoxManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            SpriteBox pSpriteBox = (SpriteBox) pMan.BaseAdd();
            Debug.Assert(pSpriteBox != null);

            // Initialize the date
            pSpriteBox.Set(name, x, y, width, height, color);
            return pSpriteBox;
        }

        public static SpriteBox Find(SpriteBox.Name name)
        {
            SpriteBoxManager pMan = SpriteBoxManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            // Use compare node to compare to search nodes
            pMan.pSpriteBoxCompare.SetName(name);

            SpriteBox pData = (SpriteBox)pMan.BaseFind(pMan.pSpriteBoxCompare);
            return pData;
        }

        public static void Remove(SpriteBox pNode)
        {
            SpriteBoxManager pMan = SpriteBoxManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            Debug.Assert(pNode != null);
            pMan.BaseRemove(pNode);
        }

        public static void Print()
        {
            SpriteBoxManager pMan = SpriteBoxManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            Debug.WriteLine("******** SPRITEBOX MANAGER *****************");
            pMan.PrintStats();
            pMan.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        protected override bool DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            SpriteBox pDataA = (SpriteBox)pLinkA;
            SpriteBox pDataB = (SpriteBox)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new SpriteBox();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            SpriteBox pSpriteBox = (SpriteBox)pLink;
            pSpriteBox.SetLineColor(null);
            pSpriteBox.SetAzulSpriteBox(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static SpriteBoxManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);
            return pInstance;
        }
    }
}
