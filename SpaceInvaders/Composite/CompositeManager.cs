using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CompositeManager : Manager
    {

        private CompositeRef pCompositeCompare;
        private NullComposite pNullComposite;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        public CompositeManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            // make sure values are ressonable 
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            this.pCompositeCompare = new CompositeRef();
            this.pNullComposite = new NullComposite();
            this.pCompositeCompare.SetComposite(this.pNullComposite);
        }

        ~CompositeManager()
        {
            this.pCompositeCompare = null;
        }

        //----------------------------------------------------------------------
        // Static Methods
        //----------------------------------------------------------------------
        public void Destroy()
        {
            this.BaseDestroy();

            this.pCompositeCompare = null;
        }

        public CompositeRef Attach(Composite pComposite)
        {
            CompositeRef pNode = (CompositeRef)this.BaseAdd();
            Debug.Assert(pNode != null);

            pNode.Set(pComposite);
            return pNode;
        }
        
        public Composite Find(Composite.CompositeName name)
        {
            // Use compare node to compare to search nodes
            this.pCompositeCompare.GetComposite().SetName(name);

            CompositeRef pNode = (CompositeRef)this.BaseFind(this.pCompositeCompare);
            Debug.Assert(pNode != null);

            return pNode.GetComposite();
        }

        public void Remove(Composite pNode)
        {
            Debug.Assert(pNode != null);
            this.BaseRemove(pNode);
        }

        public void Print()
        {
            Debug.WriteLine("******** COMPOSITE MANAGER *****************");
            this.PrintStats();
            this.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        protected override bool DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            CompositeRef pDataA = (CompositeRef)pLinkA;
            CompositeRef pDataB = (CompositeRef)pLinkB;

            Boolean status = false;

            if (pDataA.GetComposite().GetCompositeName() == pDataB.GetComposite().GetCompositeName())
            {
                status = true;
            }

            return status;
        }

        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new CompositeRef();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            // Only worry about composites here - Leaf nodes are owned by different managers
            if (pLink.GetType() == Type.GetType("SpaceInvaders.Composite"))
            {
                Composite pNode = (Composite)pLink;
                Debug.Assert(pNode != null);
                ClearList(pNode.GetComponentList());
            }             
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------

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
                DLink.RemoveFromList(ref listHead, pTmpNode);
                pTmpNode = null;
                
            }

        }
    }

}
