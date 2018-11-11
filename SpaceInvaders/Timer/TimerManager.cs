using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class TimerManager : Manager
    {
        private static TimerManager pInstance = null;
        private TimeEvent pTimeEventCompare;
        protected float currTime;

        //----------------------------------------------------------------------
        // Constructor / Destructor
        //----------------------------------------------------------------------
        private TimerManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pTimeEventCompare = new TimeEvent();
        }

        ~TimerManager()
        {
            this.pTimeEventCompare = null;
            TimerManager.pInstance = null;
        }

        //----------------------------------------------------------------------
        // Static Methods
        //----------------------------------------------------------------------
        public static void Create(int reserveNum = 3, int reserveGrow = 1)
        {
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);
            Debug.Assert(pInstance == null);

            if (pInstance == null)
            {
                pInstance = new TimerManager(reserveNum, reserveGrow);
            }
        }

        public static TimeEvent Add(TimeEvent.Name eventName, Command pCommand, float deltaTimeToTrigger)
        {
            TimerManager pInstance = TimerManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Retrieve a node from Reserve list
            TimeEvent pNewNode = (TimeEvent)pInstance.BaseGetNodeFromReserve();
            Debug.Assert(pNewNode != null);

            Debug.Assert(pCommand != null);
            Debug.Assert(deltaTimeToTrigger >= 0.0f);

            // Set TimerEvent attributes
            pNewNode.Set(eventName, pCommand, deltaTimeToTrigger);

            // Add to Active List in sorted order 
            AddToActiveListInSortedOrder(pInstance, pNewNode);

            return pNewNode;
        }

        private static void AddToActiveListInSortedOrder(TimerManager pInstance, TimeEvent pNewNode)
        {
            TimeEvent pListPointer = (TimeEvent)pInstance.BaseGetActive();

            if ((pListPointer == null) || (pNewNode.GetTriggerTime() <= pListPointer.GetTriggerTime()))
            {
                // If list is empty, or if pNode.triggerTime <= head.triggerTime
                // make the node as head
                pInstance.BaseSetActiveHead(pNewNode);
            }
            else
            {
                while (pListPointer != null)
                {
                    // If next TimeEvent has a greater triggerTime than new node, insert into list
                    if (pListPointer.GetTriggerTime() >= pNewNode.GetTriggerTime())
                    {
                        AddToListBeforeNode(pNewNode, pListPointer);

                        // Break loop
                        break;
                    } else if (pListPointer.GetNext() == null)
                    {
                        AddToListAfterNode(pNewNode, pListPointer);
                        break;
                    }

                    pListPointer = (TimeEvent)pListPointer.GetNext();
                }
            }
        }

        private static void AddToListBeforeNode(TimeEvent pNewNode, TimeEvent pNextNode)
        {
            // Set new node pointers
            pNewNode.SetNext(pNextNode);
            pNewNode.SetPrev(pNextNode.GetPrev());

            if(pNextNode.GetPrev() != null)
            {
                // Set previous node pointer
                pNextNode.GetPrev().SetNext(pNewNode);
            }

            // Set next node pointer
            pNextNode.SetPrev(pNewNode);
        }

        private static void AddToListAfterNode(TimeEvent pNewNode, TimeEvent pPrevNode)
        {
            // Set new node pointers
            pNewNode.SetNext(pPrevNode.GetNext());
            pNewNode.SetPrev(pPrevNode);

            // Set next node pointer
            if(pPrevNode.GetNext() != null)
            {
                pPrevNode.GetNext().SetPrev(pNewNode);
            }

            // Set previous node pointer
            pPrevNode.SetNext(pNewNode);

        }

        public static TimeEvent Find(TimeEvent.Name name)
        {
            TimerManager pInstance = TimerManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Use compare node to compare to search nodes
            Debug.Assert(pInstance.pTimeEventCompare != null);
            pInstance.pTimeEventCompare.Clean();
            pInstance.pTimeEventCompare.SetName(name);

            TimeEvent pData = (TimeEvent)pInstance.BaseFind(pInstance.pTimeEventCompare);
            return pData;
        }

        public static void Remove(TimeEvent pNode)
        {
            TimerManager pInstance = TimerManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.Assert(pNode != null);
            pInstance.BaseRemove(pNode);
        }

        public static TimeEvent Pop()
        {
            TimerManager pInstance = TimerManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            DLink pNode = pInstance.pActive;

            if(pNode != null)
            {
                pInstance.BaseRemove(pNode);
            }

            return (TimeEvent) pNode;
        }

        public static void Print()
        {
            TimerManager pInstance = TimerManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.WriteLine("******** TIMER MANAGER ****************");
            pInstance.PrintStats();
            pInstance.PrintNodes();
        }

        public static void Update(float totalTime)
        {

            // Get the instance
            TimerManager pInstance = TimerManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // "Latch" the current time
            pInstance.currTime = totalTime;

            TimeEvent pEvent = (TimeEvent)pInstance.BaseGetActive();
            TimeEvent pNextEvent = null;

            // Walk the list to end OR currTime is greater than timeEvent 
            while (pEvent != null && (pInstance.currTime >= pEvent.GetTriggerTime()))
            {
                pNextEvent = (TimeEvent)pEvent.GetNext();

                if (pInstance.currTime >= pEvent.GetTriggerTime())
                {
                    // Process event
                    pEvent.Process();

                    // Remove from list
                    pInstance.BaseRemove(pEvent);
                }

                // Advance the pointer
                pEvent = pNextEvent;
            }
        }

        public static float GetCurrTime()
        {
            TimerManager pInstance = TimerManager.PrivGetInstance();
            return pInstance.currTime;
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new TimeEvent();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override bool DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            TimeEvent pTimeEventA = (TimeEvent)pLinkA;
            TimeEvent pTimeEventB = (TimeEvent)pLinkB;

            Boolean status = false;

            if (pTimeEventA.GetName() == pTimeEventB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            TimeEvent pTimeEvent = (TimeEvent)pLink;
            pTimeEvent.SetCommand(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static TimerManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);
            return pInstance;
        }
    }
}
