using System;
using System.Diagnostics;


namespace SpaceInvaders
{
    class InputSubject
    {
        private DLink head;

        public void Attach(InputObserver observer)
        {
            // protection
            Debug.Assert(observer != null);

            observer.pSubject = this;

            // add to front
            if (head == null)
            {
                head = observer;
                observer.SetNext(null);
                observer.SetPrev(null);
            }
            else
            {
                observer.SetNext(head);
                observer.SetPrev(null);
                head.SetPrev(observer);
                head = observer;
            }
        }


        public void Notify()
        {
            InputObserver pNode = (InputObserver)this.head;

            while (pNode != null)
            {
                // Fire off listener
                pNode.Notify();

                pNode = (InputObserver)pNode.GetNext();
            }
        }

        public void DetachAllObservers()
        {
            DLink pNode = this.head;
            DLink pTmpNode;

            while (pNode != null)
            {
                // Walk through the list
                pTmpNode = pNode;
                pNode = pNode.GetNext();

                // Clear and remove node
                Debug.Assert(pTmpNode != null);
                DLink.RemoveFromList(ref this.head, pTmpNode);
                pTmpNode = null;
            }
        }

    }
}
