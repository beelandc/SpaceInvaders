using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class Manager
    {

        protected DLink pActive;
        protected DLink pReserve;

        private int growDelta;
        private int totalNodeNum = 0;
        private int activeNodeNum = 0;
        private int reserveNodeNum = 0;

        public Manager(int initialNumReserved = 5, int growDelta = 2)
        {
            Debug.Assert(initialNumReserved >= 0);
            Debug.Assert(growDelta >= 0);

            this.growDelta = growDelta;
            AddToReservedList(initialNumReserved);

        }

        ~Manager()
        {
            this.pActive = null;
            this.pReserve = null;
        }

        //----------------------------------------------------------------------
        // Abstract methods - the "contract" Derived class must implement
        //----------------------------------------------------------------------
        abstract protected DLink DerivedCreateNode();
        abstract protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB);
        abstract protected void DerivedDestroyNode(DLink pLink);

        //----------------------------------------------------------------------
        // Base methods - called in Derived class but lives in Base
        //----------------------------------------------------------------------

        public DLink BaseGetActive()
        {
            return this.pActive;
        }

        public void BaseSetActiveHead(DLink pNodeToActivate)
        {
            DLink.AddToFront(ref pActive, pNodeToActivate);
        }

        protected void BaseSetReserve(int reserveNum, int reserveGrow)
        {
            this.growDelta = reserveGrow;

            if (reserveNum > this.reserveNodeNum)
            {
                // Preload the reserve
                this.AddToReservedList(reserveNum - this.reserveNodeNum);
            }
        }

        /// <summary>
        ///   Moves a node from reserve to active and then returns reference for client to modify as desired  
        /// </summary>
        /// <returns>DLink that was added to active list</returns>
        protected DLink BaseAdd()
        {
            DLink pNodeToActivate = BaseGetNodeFromReserve();

            // Add to front of Active List
            DLink.AddToFront(ref pActive, pNodeToActivate);

            // Stats
            this.reserveNodeNum--;
            this.activeNodeNum++;

            return pNodeToActivate;
        }

        protected DLink BaseGetNodeFromReserve()
        {
            // If pReserve is empty, refill with growDelta
            if (pReserve == null)
            {
                AddToReservedList(this.growDelta);
            }

            // Get and remove head of pReserve (as pNode)
            Debug.Assert(pReserve != null);
            DLink pNodeToActivate = pReserve;

            // Set new head of Reserve
            pReserve = pReserve.GetNext();

            // If new head is not null, clear its previous link to newly activated node
            if (pReserve != null)
            {
                pReserve.SetPrev(null);
            }

            // "Clean" values of pNode
            pNodeToActivate.Clean();

            return pNodeToActivate;
        }

        /// <summary>
        /// Finds first node that matches the DLink node passed in as pNodeTarget. Returns null if no matching node is found. 
        /// </summary>
        /// <param name="sd">Instance of DLink that will be compared against Active list nodes using derivedCompare()</param>
        /// <returns></returns>
        protected DLink BaseFind(DLink pNodeTarget)
        {
            // If no nodes in Active list
            if (pActive == null)
            {
                return null;
            }

            // Iterate over active list, starting at head
            DLink pSearchIterator = pActive;
            while (pSearchIterator != null)
            {
                // Compare current node to provided DLink node. If match, return node
                if (DerivedCompare(pSearchIterator, pNodeTarget))
                {
                    return pSearchIterator;
                }
                pSearchIterator = pSearchIterator.GetNext();
            }

            // If no match is found
            return null;
        }

        /// <summary>
        /// Moves a node from active to reserve and cleans list references for node 
        /// </summary>
        /// <param name="pNode"></param>
        protected void BaseRemove(DLink pNode)
        {
            Debug.Assert(pNode != null);

            // Update list pointers to remove pNode from list
            DLink.RemoveFromList(ref pActive, pNode);

            // Remove list references in removed node
            pNode.ClearLinks();

            // Return pNode to pReserve list
            DLink.AddToFront(ref pReserve, pNode);

            // stats update
            this.activeNodeNum--;
            this.reserveNodeNum++;

        }

        protected void BaseDestroy()
        {

            // Active List
            ClearList(this.pActive);
            this.activeNodeNum = 0;

            // Reserve List
            ClearList(this.pReserve);
            this.reserveNodeNum = 0;

        }

        private void ClearList(DLink listHead)
        {
            DLink pNode;
            DLink pTmpNode;

            pNode = listHead;
            while (pNode != null)
            {
                // Walk through the list
                pTmpNode = pNode;
                pNode = pNode.GetNext();

                // Clear and remove node
                Debug.Assert(pTmpNode != null);
                this.DerivedDestroyNode(pTmpNode);
                DLink.RemoveFromList(ref this.pActive, pTmpNode);
                pTmpNode = null;

                // Decrement total count
                this.totalNodeNum--;
            }

        }

        /// <summary>
        /// Adds additional nodes to reserved list
        /// </summary>
        /// <param name="numNodes">Number of nodes to ad to reserved list</param>
        private void AddToReservedList(int numNodes)
        {
            Debug.Assert(numNodes > 0);

            DLink pNewNode;
            for (int i = 0; i < numNodes; i++)
            {
                pNewNode = this.DerivedCreateNode();
                Debug.Assert(pNewNode != null);
                DLink.AddToFront(ref pReserve, pNewNode);
            }

            this.totalNodeNum += numNodes;
            this.reserveNodeNum += numNodes;
        }

        public int GetGrowDelta()
        {
            return this.growDelta;
        }

        public void SetGrowDelta(int newGrowDelta)
        {
            this.growDelta = newGrowDelta;
        }

        public void PrintStats()
        {
            Debug.WriteLine("**** Stats: *****************************");
            Debug.WriteLine("Active List Node Count: " + activeNodeNum);
            Debug.WriteLine("Reserve List Node Count: " + reserveNodeNum);
            Debug.WriteLine("Total Node Count: " + totalNodeNum);
            Debug.WriteLine("Growth Number: " + growDelta);
        }

        public void PrintNodes()
        {
            DLink pNodeIterator;
            int nodePos = 0;

            // Print Active List
            Debug.WriteLine("Active List:");

            if (pActive != null)
            {
                Debug.Write("  Node " + nodePos + " - ");
                pActive.Print();
                pNodeIterator = pActive;
                while (pNodeIterator.HasNext())
                {
                    pNodeIterator = pNodeIterator.GetNext();
                    nodePos++;
                    Debug.Write("  Node " + nodePos + " - ");
                    pNodeIterator.Print();
                }
            }
            else
            {
                Debug.WriteLine("  *** Empty ***");
            }

            Debug.WriteLine("");

            // Print Reserve List
            nodePos = 0;
            Debug.WriteLine("Reserve List:");

            if (pReserve != null)
            {
                Debug.Write("  Node " + nodePos + " - ");
                pReserve.Print();
                pNodeIterator = pReserve;
                while (pNodeIterator.HasNext())
                {
                    pNodeIterator = pNodeIterator.GetNext();
                    nodePos++;
                    Debug.Write("  Node " + nodePos + " - ");
                    pNodeIterator.Print();
                }
            }
            else
            {
                Debug.WriteLine("  *** Empty ***");
            }

            Debug.WriteLine("");
        }

    }
}
