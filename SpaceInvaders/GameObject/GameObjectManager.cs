using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class GameObjectManager : Manager
    {
        private GameObjectRef pGameObjectCompare;
        private NullGameObject pNullGameObject;

        //----------------------------------------------------------------------
        // Constructor / Destructor
        //----------------------------------------------------------------------
        public GameObjectManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            // make sure values are ressonable 
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            this.pGameObjectCompare = new GameObjectRef();

            this.pNullGameObject = new NullGameObject();
            this.pGameObjectCompare.SetGameObject(this.pNullGameObject);
        }

        ~GameObjectManager()
        {
            this.pGameObjectCompare = null;
            this.pNullGameObject = null;
        }

        public GameObjectRef Attach(GameObject pGameObject)
        {
            GameObjectRef pNode = (GameObjectRef) BaseAdd();
            Debug.Assert(pNode != null);

            pNode.Set(pGameObject);
            return pNode;
        }
        
        public GameObject Find(GameObject.Name name)
        {
            // Compare functions only compares two Nodes
            pGameObjectCompare.GetGameObject().SetName(name);

            GameObjectRef pNode = (GameObjectRef)BaseFind(pGameObjectCompare);
            if(pNode != null){
                return pNode.GetGameObject();
            }

            return null;
        }

        public void Detach(GameObject pNode)
        {
            Debug.Assert(pNode != null);
            pGameObjectCompare.SetGameObject(pNode);
            DLink pFoundNode = FindGameObjectByReference(pGameObjectCompare);

            pGameObjectCompare.SetGameObject(pNullGameObject);

            Debug.Assert(pFoundNode != null);
            BaseRemove(pFoundNode);
        }

        public void Update()
        {
            GameObjectRef pNode = (GameObjectRef)BaseGetActive();

            while (pNode != null)
            {
                
                if (pNode.GetGameObject() != null)
                {
                    // Update the node
                    pNode.GetGameObject().Update();
                }
                pNode = (GameObjectRef)pNode.GetNext();
            }

        }

        public void Print()
        {
            GameObject pCurrGameObject;

            Debug.WriteLine("******** GAME OBJECT MANAGER ****************");
            PrintStats();


            DLink pNodeIterator;
            int nodePos = 0;

            // Print Active List
            Debug.WriteLine("Active List:");

            if (pActive != null)
            {
                Debug.Write("  Node " + nodePos + " - ");
                pCurrGameObject = ((GameObjectRef)pActive).GetGameObject();
                if (pCurrGameObject != null)
                {
                    Debug.WriteLine(" GameObject Name: {0} ({1})", pCurrGameObject.GetName(), pCurrGameObject.GetHashCode());
                    pActive.Print();
                }
                pNodeIterator = pActive;
                while (pNodeIterator.HasNext())
                {
                    pNodeIterator = pNodeIterator.GetNext();
                    nodePos++;
                    Debug.Write("  Node " + nodePos + " - ");
                    pCurrGameObject = ((GameObjectRef)pNodeIterator).GetGameObject();
                    if (pCurrGameObject != null)
                    {
                        Debug.WriteLine(" GameObject Name: {0} ({1})", pCurrGameObject.GetName(), pCurrGameObject.GetHashCode());
                        pNodeIterator.Print();
                    }
                }
            }
            else
            {
                Debug.WriteLine("  *** Empty ***");
            }

            Debug.WriteLine("");

            // Print Reserve List
            nodePos = 0;
            Debug.WriteLine("Reserve List:");

            if (pReserve != null)
            {
                Debug.Write("  Node " + nodePos + " - ");
                pReserve.Print();
                pNodeIterator = pReserve;
                while (pNodeIterator.HasNext())
                {
                    pNodeIterator = pNodeIterator.GetNext();
                    nodePos++;
                    Debug.Write("  Node " + nodePos + " - ");
                    pNodeIterator.Print();
                }
            }
            else
            {
                Debug.WriteLine("  *** Empty ***");
            }

            Debug.WriteLine("");

        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new GameObjectRef();
            Debug.Assert(pNode != null);

            return pNode;
        }

        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            GameObjectRef pDataA = (GameObjectRef)pLinkA;
            GameObjectRef pDataB = (GameObjectRef)pLinkB;

            Boolean status = false;

            if (pDataA.GetGameObject().GetName() == pDataB.GetGameObject().GetName())
            {
                status = true;
            }

            return status;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            GameObjectRef pGameObjectRef = (GameObjectRef)pLink;
            pGameObjectRef.SetGameObject(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------

        private DLink FindGameObjectByReference(DLink pSearchGameObject)
        {

            // If no nodes in Active list
            if (pActive == null)
            {
                return null;
            }

            // Iterate over active list, starting at head
            DLink pSearchIterator = pActive;
            while (pSearchIterator != null)
            {
                // Compare current node to provided DLink node. If match, return node
                if (HashCodeCompare(pSearchIterator, pSearchGameObject))
                {
                    return pSearchIterator;
                }
                pSearchIterator = pSearchIterator.GetNext();
            }

            // If no match is found
            return null;

        }

        // Currently necessary for Detach() since GameObject.Name is not unique
        private Boolean HashCodeCompare(DLink pLinkA, DLink pLinkB)
        {
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            GameObjectRef pDataA = (GameObjectRef)pLinkA;
            GameObjectRef pDataB = (GameObjectRef)pLinkB;

            Boolean status = false;

            if (pDataA.GetGameObject().GetHashCode() == pDataB.GetGameObject().GetHashCode())
            {
                status = true;
            }

            return status;
        }

    }
}
