using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class DLink
    {
        protected DLink pNext;
        protected DLink pPrev;

        protected DLink()
        {
            this.Clean();
        }

        public DLink GetNext()
        {
            return this.pNext;
        }

        public void SetNext(DLink pNewNext)
        {
            this.pNext = pNewNext;
        }

        public DLink GetPrev()
        {
            return this.pPrev;
        }

        public void SetPrev(DLink pNewPrev)
        {
            this.pPrev = pNewPrev;
        }

        public Boolean HasNext()
        {
            return (pNext != null);
        }

        public virtual void Print()
        {
            Debug.WriteLine("    Next (Hashcode): " + ((pNext != null) ? pNext.GetHashCode().ToString() : "Null"));
            Debug.WriteLine("    Prev (Hashcode): " + ((pPrev != null) ? pPrev.GetHashCode().ToString() : "Null"));
        }

        public override String ToString()
        {
            return (this.GetType() + " - " + this.GetHashCode());
        }

        public virtual void Clean()
        {
            this.pNext = null;
            this.pPrev = null;
        }

        public void ClearLinks()
        {
            this.pNext = null;
            this.pPrev = null;
        }

        //----------------------------------------------------------------------
        // Static methods
        //----------------------------------------------------------------------

        public static void AddToFront(ref DLink pCurrentHead, DLink pNodeToAdd)
        {
            // Set current node as next of new node
            pNodeToAdd.SetNext(pCurrentHead);

            // Set new node as prev to current node
            if (pCurrentHead != null)
            {
                pCurrentHead.SetPrev(pNodeToAdd);
            }

            // Set new node as head of list
            pCurrentHead = pNodeToAdd;

        }

        public static void RemoveFromList(ref DLink pCurrentHead, DLink pNodeToRemove)
        {
            Debug.Assert(pNodeToRemove != null);

            DLink pTmpNode = pNodeToRemove;

            // If pNode is head of list
            if (pCurrentHead == pNodeToRemove)
            {
                pCurrentHead = pCurrentHead.GetNext();

                if(pCurrentHead != null)
                {
                    // Set prev to null - it's the new list head
                    pCurrentHead.SetPrev(null);
                }
            }
            else
            {
                if(pTmpNode.GetPrev() != null)
                {
                    // Set the previous node's next to pNode's next
                    pTmpNode.GetPrev().SetNext(pNodeToRemove.GetNext());
                }

                if (pTmpNode.GetNext() != null)
                {
                    // Set the next node's prev to pNodes prev
                    pTmpNode.GetNext().SetPrev(pTmpNode.GetPrev());
                }
            }

        }

    }
}
