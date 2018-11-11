using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CollisionPairManager : Manager
    {
        private static CollisionPairManager pInstance = null;
        private CollisionPair poNodeCompare;
        private CollisionPair pActiveColPair;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private CollisionPairManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pActiveColPair = null;
            this.poNodeCompare = new CollisionPair();
        }

        ~CollisionPairManager()
        {
            this.pActiveColPair = null;
            this.poNodeCompare = null;
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
                pInstance = new CollisionPairManager(reserveNum, reserveGrow);
            }

        }



        public static void Destroy()
        {
            // Get the instance

        }

        public static CollisionPair Add(CollisionPair.Name colpairName, GameObject treeRootA, GameObject treeRootB)
        {
            // Get the instance
            CollisionPairManager pInstance = CollisionPairManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Go to Man, get a node from reserve, add to active, return it
            CollisionPair pColPair = (CollisionPair)pInstance.BaseAdd();
            Debug.Assert(pColPair != null);

            // Initialize Image
            pColPair.Set(colpairName, treeRootA, treeRootB);

            return pColPair;
        }

        public static void Process()
        {
            // get the singleton
            CollisionPairManager pInstance = CollisionPairManager.PrivGetInstance();

            CollisionPair pColPair = (CollisionPair)pInstance.BaseGetActive();

            while (pColPair != null)
            {
                // set the current active
                pInstance.pActiveColPair = pColPair;

                // do the check for a single pair
                pColPair.Process();

                // advance to next
                pColPair = (CollisionPair)pColPair.GetNext();
            }
        }

        public static CollisionPair Find(CollisionPair.Name name)
        {
            CollisionPairManager pInstance = CollisionPairManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Compare functions only compares two Nodes

            // So:  Use the Compare Node - as a reference
            //      use in the Compare() function
            pInstance.poNodeCompare.SetName(name);

            CollisionPair pData = (CollisionPair)pInstance.BaseFind(pInstance.poNodeCompare);
            return pData;
        }
        public static void Remove(CollisionPair pNode)
        {
            CollisionPairManager pInstance = CollisionPairManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.Assert(pNode != null);
            pInstance.BaseRemove(pNode);
        }

        public static CollisionPair GetActiveColPair()
        {
            CollisionPairManager pInstance = CollisionPairManager.PrivGetInstance();

            return pInstance.pActiveColPair;
        }

        public static void Print()
        {
            CollisionPairManager pInstance = CollisionPairManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new CollisionPair();
            Debug.Assert(pNode != null);

            return pNode;
        }
        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            CollisionPair pDataA = (CollisionPair)pLinkA;
            CollisionPair pDataB = (CollisionPair)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            throw new NotImplementedException();
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static CollisionPairManager PrivGetInstance()
        {
            // Safety - this forces users to call Create() first before using class
            Debug.Assert(pInstance != null);

            return pInstance;
        }
        
    }
}
