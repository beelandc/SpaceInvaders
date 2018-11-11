using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract class SLink
    {
        protected SLink pNext;

        protected SLink()
        {
            this.pNext = null;
        }

        public static void AddToFront(ref SLink pHead, SLink pNode)
        {
            Debug.Assert(pNode != null);

            // If list is empty
            if (pHead == null)
            {
                // Add as list head
                pHead = pNode;
                pNode.pNext = null;
            }
            else
            {
                // Push to front
                pNode.pNext = pHead;
                pHead = pNode;
            }

            Debug.Assert(pHead != null);
        }

        public SLink GetNext()
        {
            return this.pNext;
        }

        public void SetNext(SLink newNext)
        {
            this.pNext = newNext;
        }

    }
}
