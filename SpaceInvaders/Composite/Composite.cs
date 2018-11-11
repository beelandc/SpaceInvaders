using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class Composite : GameObject
    {
        protected CompositeName compositeName;
        protected DLink pHead;
        protected int numChildren;

        protected Composite(GameObject.Name gameName)
            : base(gameName, Sprite.Name.NullSprite)
        {
            this.numChildren = 0;
            this.pHead = null;
            this.containerType = Container.COMPOSITE;
        }

        protected Composite(GameObject.Name gameName, Sprite.Name spriteName)
            : base(gameName, spriteName)
        {
            this.numChildren = 0;
            this.pHead = null;
            this.containerType = Container.COMPOSITE;
        }


        public void Set(CompositeName name)
        {
            this.compositeName = name;
        }

        override public void Add(Component pComponent)
        {
            Debug.Assert(pComponent != null);
            DLink.AddToFront(ref this.pHead, pComponent);

            this.numChildren += 1;

            pComponent.pParent = this;
        }

        override public void Remove(Component pComponent)
        {
            Debug.Assert(pComponent != null);
            DLink.RemoveFromList(ref this.pHead, pComponent);

            this.numChildren -= 1;
        }

        public override void Print()
        {
            Debug.WriteLine(" GameObject Name: {0} ({1})", this.GetName(), this.GetHashCode());
            base.Print();

            DLink pNode = pHead;
            while (pNode != null)
            {
                pNode.Print();
                pNode = pNode.GetNext();
            }
        }

        public override void Update()
        {

            if (this.GetFirstChild() != null)
            {
                base.BaseUpdateBoundingBox();
                base.Update();
            }
           
        }

        public override void Move(float xDelta, float yDelta)
        {
            DLink pNode = this.pHead;

            while (pNode != null)    
            {
                // Get component
                Component pComponent = (Component)pNode;

                // Move component
                pComponent.Move(xDelta, yDelta);

                // Increment pointer along list
                pNode = pNode.GetNext();
            }

        }

        override public Component GetFirstChild()
        {
            DLink pNode = this.pHead;

            return (Component)pNode;
        }

        public int GetNumOfChildren()
        {
            return numChildren;
        }

        public Composite GetChildByIndex(int index)
        {
            Debug.Assert(index >= 0);
            Debug.Assert(index < numChildren);

            DLink pNode = this.pHead;
            int count = 0;

            while(count <= index)
            {
                if (count == index)
                {
                    return (Composite)pNode;  
                }

                pNode = pNode.GetNext();
                count++;
            }

            return null;
        }

        public CompositeName GetCompositeName()
        {
            return this.compositeName;
        }

        public void SetName(CompositeName name)
        {
            this.compositeName = name;
        }

        public DLink GetComponentList()
        {
            return this.pHead;
        }

        public enum CompositeName
        {
            AlienGrid,
            AlienCol1,
            AlienCol2,
            AlienCol3,
            AlienCol4,
            AlienCol5,
            AlienCol6,
            AlienCol7,
            AlienCol8,
            AlienCol9,
            AlienCol10,
            AlienCol11,
            MissileGroup,
            CoreCannonGroup,
            ShieldGroup,
            Uninitialized,
            BombRoot,
            FlyingSaucerRoot
        };

    }
}
