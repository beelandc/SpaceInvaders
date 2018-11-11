using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CollisionSubject
    {
        private CollisionObserver pHead;
        // TODO Make Private
        public GameObject pObjA;
        public GameObject pObjB;

        public CollisionSubject()
        {
            this.pObjB = null;
            this.pObjA = null;
            this.pHead = null;
        }

        ~CollisionSubject()
        {
            this.pObjB = null;
            this.pObjA = null;
            // ToDo
            // Need to walk and nuke the list
            this.pHead = null;
        }

        public void Attach(CollisionObserver observer)
        {
            // protection
            Debug.Assert(observer != null);

            observer.pSubject = this;

            // add to front
            if (pHead == null)
            {
                pHead = observer;
                observer.SetNext(null);
                observer.SetPrev(null);
            }
            else
            {
                observer.SetNext(pHead);
                pHead.SetPrev(observer);
                pHead = observer;
            }

        }


        public void Notify()
        {
            CollisionObserver pNode = this.pHead;

            while (pNode != null)
            {
                // Fire off listener
                pNode.Notify();

                pNode = (CollisionObserver)pNode.GetNext();
            }

        }

        public void Detach()
        {
            // TODO?
        }
        
    }
}
