using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class QueuedTimeEventManager : Manager
    {
        public QueuedTimeEventManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            // Do nothing at this layer
        }

        ~QueuedTimeEventManager()
        {

        }

        public void Enqueue(TimeEvent.Name inEventName, Command inCommand, float inTimeDelta)
        {
            QueuedTimeEvent pQueuedTimeEvent = (QueuedTimeEvent) BaseAdd();
            Debug.Assert(pQueuedTimeEvent != null);

            // Initialize QTE
            pQueuedTimeEvent.Set(inEventName, inCommand, inTimeDelta);

        }

        public QueuedTimeEvent Dequeue()
        {
            QueuedTimeEvent pNode = (QueuedTimeEvent)this.pActive;
            
            if(pNode != null)
            {
                BaseRemove(pNode);
            }

            // TODO Will this algorithm wipe the QTE before it is returned?
            // If so, may need to make a copy before wiping and return that?

            return pNode;
        }

        public void Print()
        {
            Debug.WriteLine("******** QUEUED TIME EVENT MANAGER ****************");
            PrintStats();
            PrintNodes();
        }

        protected override bool DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            throw new NotImplementedException();
        }

        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new QueuedTimeEvent();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            QueuedTimeEvent pQueuedTimeEvent = (QueuedTimeEvent)pLink;
            pQueuedTimeEvent.Set(TimeEvent.Name.Uninitialized, null, -1.0f);
        }
    }
}
