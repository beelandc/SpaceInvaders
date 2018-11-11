using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class ForwardIterator : Iterator
    {
        public ForwardIterator(Component pStart)
        {
            Debug.Assert(pStart != null);
            Debug.Assert(pStart.containerType == Component.Container.COMPOSITE);

            this.pCurr = pStart;
            this.pRoot = pStart;
            this.bIsDone = false;
        }

        static public Component GetParent(Component pNode)
        {
            Debug.Assert(pNode != null);

            return pNode.pParent;

        }
        static public Component GetChild(Component pNode)
        {
            Debug.Assert(pNode != null);

            return pNode.GetFirstChild();
        }

        static public Component GetSibling(Component pNode)
        {
            Debug.Assert(pNode != null);
            return (Component)pNode.GetNext();
        }

        // Non-Recursive Depth-First Search Impl.
        // If has child, get first child
        // Else - If has sibling, get sibling
        // Else - Go up 
        //      If has sibling, get sibling
        //      Else - Go up
        private Component PrivNextStep(Component pNode, Component pParent, Component pSibling)
        {
            Component childOrSibling = ReturnChildOrSibling(pNode, pSibling);

            // If pNode has children, return first child
            // Else if pNode has siblings, return next sibling
            if (childOrSibling != null)
            {
                return childOrSibling;
            }
             else
            {
                // Go up a level and search for sibling
                while (pParent != null)
                {
                    pNode = pParent;

                    // If parent has sibling, return sibling
                    if (pNode.GetNext() != null)
                    {
                        return (Component)pNode.GetNext();
                    }
                    else
                    {
                        // Else go up another level
                        pParent = GetParent(pNode);
                    }
                }
            }

            // Is iteration complete?
            if ((pNode.containerType != Component.Container.LEAF) && (pParent == null))
            {
                pNode = null;
                this.bIsDone = true;
            }

            return pNode;
        }

        private Component ReturnChildOrSibling(Component pNode, Component pSibling)
        {
            Component pFirstChild = pNode.GetFirstChild();

            // If has children, return first child
            if (pFirstChild != null)
            {
                return pFirstChild;
            }
            // Else if node has sibling, return sibling
            else if (pSibling != null)
            {
                return pSibling;
            }

            // If node has neither child nor sibling
            return null;
        }

        override public Component First()
        {
            Debug.Assert(this.pRoot != null);
            
            this.pCurr = this.pRoot;

            // Debug.WriteLine("---> {0} ", this.pCurr.GetHashCode());
            return this.pCurr;
        }
        override public Component Next()
        {
            Debug.Assert(this.pCurr != null);

            Component pNode = this.pCurr;

            Component pChild = GetChild(pNode);
            Component pSibling = GetSibling(pNode);
            Component pParent = GetParent(pNode);

            // Start - Depth first iteration
            pNode = PrivNextStep(pNode, pParent, pSibling);

            this.pCurr = pNode;

            //if (this.pCurr != null)
            //{
            //    Debug.WriteLine("---> {0}", this.pCurr.GetHashCode());
            //}
            //else
            //{
            //    Debug.WriteLine("---> null");
            //}

            return this.pCurr;
        }

        override public bool IsDone()
        {
            return this.bIsDone;
        }

        private Component pCurr;
        private Component pRoot;
        private bool bIsDone;

    }
}
