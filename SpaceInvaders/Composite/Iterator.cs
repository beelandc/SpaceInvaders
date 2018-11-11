using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract public class Iterator
    {
        abstract public Component Next();
        abstract public bool IsDone();
        abstract public Component First();

        static public GameObject GetSiblingGameObject(GameObject pNode)
        {
            GameObject pGameObj = null;

            Component pComponent = ForwardIterator.GetSibling(pNode);

            if (pComponent != null)
            {
                pGameObj = (GameObject)pComponent;
            }

            return pGameObj;
        }

        static public GameObject GetChildGameObject(GameObject pNode)
        {
            GameObject pGameObj = null;

            // TRICKY... 
            Component pComponent = pNode;

            if (pComponent != null)
            {
                pComponent = ForwardIterator.GetChild(pComponent);
                pGameObj = (GameObject)pComponent;
            }

            return pGameObj;
        }

        //static public GameObject GetParentGameObject(GameObject pNode)
        //{
        //    GameObject pGameObj = null;
        //    Component pComponent = ForwardIterator.GetParent(pNode);
        //    pGameObj = (GameObject)pComponent;

        //    return pGameObj;
        //}
    }
}
