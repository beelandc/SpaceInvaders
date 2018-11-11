using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class GameObjectRef : DLink
    {
        private GameObject pGameObj;

        //----------------------------------------------------------------------
        // Constructors / Destructor
        //----------------------------------------------------------------------
        public GameObjectRef()
            : base()
        {
            this.pGameObj = null;
        }

        ~GameObjectRef()
        {
            this.pGameObj = null;
        }

        //----------------------------------------------------------------------
        // Methods
        //----------------------------------------------------------------------
        public void Set(GameObject pGameObject)
        {
            Debug.Assert(pGameObject != null);
            this.pGameObj = pGameObject;
        }

        public override void Clean()
        {
            this.pGameObj = null;
        }

        public Enum GetName()
        {
            return this.pGameObj.GetName();
        }

        internal GameObject GetGameObject()
        {
            return this.pGameObj;
        }

        public void SetGameObject(GameObject newGameObject)
        {
            this.pGameObj = newGameObject;
        }
    }
}
