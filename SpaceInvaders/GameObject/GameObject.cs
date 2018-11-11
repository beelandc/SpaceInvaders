using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class GameObject : CollisionVisitor
    {
        protected GameObject.Name name;
        protected bool markedForDeath;
        protected float x;
        protected float y;
        protected SpriteProxy pSpriteProxy;
        protected CollisionObject pCollisionObject;

        //----------------------------------------------------------------------
        // Constructors / Destructor
        //----------------------------------------------------------------------

        protected GameObject(GameObject.Name gameName, Sprite.Name spriteName)
        {
            this.name = gameName;
            this.x = 0.0f;
            this.y = 0.0f;
            this.pSpriteProxy = new SpriteProxy(spriteName);
            this.pCollisionObject = new CollisionObject(this.pSpriteProxy);
        }

        ~GameObject()
        {
            this.name = GameObject.Name.Uninitialized;
            this.pSpriteProxy = null;
            this.pCollisionObject = null;
        }

        //----------------------------------------------------------------------
        // Methods
        //----------------------------------------------------------------------
        public virtual void Update()
        {
            Debug.Assert(this.pSpriteProxy != null);
            this.pSpriteProxy.SetX(this.x);
            this.pSpriteProxy.SetY(this.y);

            Debug.Assert(this.pCollisionObject != null);
            this.pCollisionObject.UpdatePos(this.x, this.y);

            SpriteBox pCollisionSpriteBox = this.pCollisionObject.GetCollisionSpriteBox();
            Debug.Assert(pCollisionSpriteBox != null);
            pCollisionSpriteBox.Update();
        }

        protected void BaseUpdateBoundingBox()
        {
            // Go to first child
            Component pComponent = (Component)this;
            GameObject pNode = (GameObject)ForwardIterator.GetChild(pComponent);
            Debug.Assert(pNode != null);

            CollisionRect ColTotal = this.pCollisionObject.GetCollisionRect();
            ColTotal.Set(pNode.GetCollisionObject().GetCollisionRect());

            // loop through sliblings
            while (pNode != null)
            {
                ColTotal.Union(pNode.GetCollisionObject().GetCollisionRect());

                // go to next sibling
                pComponent = ForwardIterator.GetSibling(pNode);

                if (pComponent != null)
                {
                    pNode = (GameObject)pComponent;
                }
                else
                {
                    pNode = null;
                }
            }

            this.x = this.GetCollisionObject().GetCollisionRect().x;
            this.y = this.GetCollisionObject().GetCollisionRect().y;

            //Debug.WriteLine("x:{0} y:{1} w:{2} h:{3}", ColTotal.x, ColTotal.y, ColTotal.width, ColTotal.height);
        }

        public void ActivateCollisionSprite(SpriteBatch pSpriteBatch)
        {
            Debug.Assert(pSpriteBatch != null);
            Debug.Assert(this.pCollisionObject != null);
            pSpriteBatch.Attach(this.pCollisionObject.GetCollisionSpriteBox());
        }
        public void ActivateSprite(SpriteBatch pSpriteBatch)
        {
            Debug.Assert(pSpriteBatch != null);
            pSpriteBatch.Attach(this.pSpriteProxy);
        }

        public GameObject.Name GetName()
        {
            return this.name;
        }

        public void SetName(Name newName)
        {
            this.name = newName;
        }

        public float GetX()
        {
            return this.x;
        }

        public void SetX(float x)
        {
            this.x = x;
        }

        public float GetY()
        {
            return this.y;
        }

        public void SetY(float y)
        {
            this.y = y;
        }

        public SpriteProxy GetSpriteProxy()
        {
            return this.pSpriteProxy;
        }

        public CollisionObject GetCollisionObject()
        {
            return this.pCollisionObject;
        }

        public void SetPos(float inX, float inY)
        {
            this.x = inX;
            this.y = inY;
        }

        public void SetCollisionObjectLineColor(float r, float g, float b)
        {
            this.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(r, g, b);
        }

        public bool IsMarkedForDeath()
        {
            return markedForDeath;
        }

        public void MarkForDeath()
        {
            markedForDeath = true;
        }

        public virtual void Remove(SpriteBatch.Name gameObjSpriteBatch, SpriteBatch.Name collisionBoxSpriteBatch)
        {
            // Very difficult at first... if you are messy, you will pay here!
            // Given a game object....

            // Since the Root object is being drawn
            // 1st set its size to zero
            this.pCollisionObject.GetCollisionRect().Set(0, 0, 0, 0);
            this.Update();

            // Update the parent
            GameObject pParent = (GameObject)this.pParent;
            if(pParent != null)
            {
                pParent.Update();
            }

            // Remove from SpriteBatch
            GameStateManager.GetGame().GetStateSpriteBatchManager().Find(gameObjSpriteBatch).Detach(pSpriteProxy);
            GameStateManager.GetGame().GetStateSpriteBatchManager().Find(collisionBoxSpriteBatch).Detach(pCollisionObject.GetCollisionSpriteBox());

            // Remove from GameObjectManager
            GameStateManager.GetGame().GetStateGameObjectManager().Detach(this);

            // Attach to GhostManager for later use
            GameStateManager.GetGame().GetStateGhostManager().Attach(this);

            // Reset death flag
            this.markedForDeath = false;

        }

        public enum Name
        {
            SquidAlien,
            JellyFishAlien,
            CrabAlien,
            FlyingSaucer,
            CoreCannon,

            AlienGrid,
            AlienGridColumn,

            Missile,
            MissileGroup,

            
            ShieldGroup,
            ShieldRoot,
            ShieldBrick,
            ShieldColumn,

            RightWall,
            LeftWall,
            TopWall,
            BottomWall,

            CoreCannonGroup,
            
            BombRoot,
            Bomb,

            Null_Object,
            Uninitialized,
            LeftBumper,
            RightBumper,
            FlyingSaucerRoot,
            FlyingSaucerBomb,
            ShieldBrick_LeftTop1,
            ShieldBrick_LeftTop0,
            ShieldBrick_LeftBottom,
            ShieldBrick_RightBottom,
            ShieldBrick_RightTop1,
            ShieldBrick_RightTop0,
        }

    }

}
