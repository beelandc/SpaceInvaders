using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class Leaf : GameObject
    {

        protected Leaf(GameObject.Name gameName, Sprite.Name spriteName)
            : base(gameName, spriteName)
        {
            this.containerType = Container.LEAF;
        }

        override public void Add(Component c)
        {
            // This is a leaf so we do not want to impl this functionality
            Debug.Assert(false);
        }

        override public void Remove(Component c)
        {
            // This is a leaf so we do not want to impl this functionality
            Debug.Assert(false);
        }

        override public void Print()
        {
            Debug.WriteLine(" GameObject Name: {0} ({1})", this.GetName(), this.GetHashCode());
            base.Print();
        }

        override public void Move(float xDelta, float yDelta)
        {
            // Define in implementing class
            Debug.Assert(false);
        }

        override public Component GetFirstChild()
        {
            return null;
        }

        // TODO Remove?
        //  abstract public GameObject.Name GetName();

    }
}
