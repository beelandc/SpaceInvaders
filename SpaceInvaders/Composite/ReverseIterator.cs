using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class ReverseIterator : Iterator
    {
        public ReverseIterator(Component pStart)
        {
            Debug.Assert(pStart != null);
            Debug.Assert(pStart.containerType == Component.Container.COMPOSITE);

            // TODO - REFACTOR THIS
            // Horrible HACK need to clean up.. but its late
            ForwardIterator pForward = new ForwardIterator(pStart);

            this.pRoot = pStart;
            this.pCurr = this.pRoot;

            Component pPrevNode = this.pRoot;

            // Initialize the reverse pointer
            Component pNode = pForward.First();

            while (!pForward.IsDone())
            {
                // Squirrel away
                pPrevNode = pNode;

                // Advance
                pNode = pForward.Next();

                if (pNode != null)
                {
                    pNode.pReverse = pPrevNode;
                }
            }

            pRoot.pReverse = pPrevNode;

        }

        override public Component First()
        {
            Debug.Assert(this.pRoot != null);

            this.pCurr = this.pRoot.pReverse;

            // Avoids infinite loop
            this.pRoot.pReverse = null;

            return this.pCurr;
        }

        override public Component Next()
        {
            Debug.Assert(this.pCurr != null);

            this.pCurr = this.pCurr.pReverse;
            return this.pCurr;
        }

        override public bool IsDone()
        {
            return (this.pCurr == null);
        }

        private Component pRoot;
        private Component pCurr;

    }
}
