using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class DelayedObjectManager
    {
        private CollisionObserver head;

        public DelayedObjectManager()
        {
            this.head = null;
        }

        public void Attach(CollisionObserver observer)
        {
            // protection
            Debug.Assert(observer != null);

            // add to front
            if (this.head == null)
            {
                this.head = observer;
                observer.SetNext(null);
                observer.SetPrev(null);
            }
            else
            {
                observer.SetNext(this.head);
                observer.SetPrev(null);
                this.head.SetPrev(observer);
                this.head = observer;
            }
        }
        private void PrivDetach(CollisionObserver node, ref CollisionObserver head)
        {
            // protection
            Debug.Assert(node != null);

            if (node.GetPrev() != null)
            {	// middle or last node
                node.GetPrev().SetNext(node.GetNext());
            }
            else
            {  // first
                head = (CollisionObserver)node.GetNext();
            }

            if (node.GetNext() != null)
            {	// middle node
                node.GetNext().SetPrev(node.GetPrev());
            }
        }
        public void Process()
        {
            CollisionObserver pNode = this.head;

            while (pNode != null)
            {
                // Fire off listener
                pNode.ExecuteDelayed();

                pNode = (CollisionObserver)pNode.GetNext();
            }


            // remove
            pNode = this.head;
            CollisionObserver pTmp = null;

            while (pNode != null)
            {
                pTmp = pNode;
                pNode = (CollisionObserver)pNode.GetNext();

                // remove
                this.PrivDetach(pTmp, ref this.head);
            }
        }
        
    }
}