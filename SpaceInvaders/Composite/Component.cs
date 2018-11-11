using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class Component : DLink
    {
        // TODO Make protected and add getter/setter
        public Component pParent = null;
        public Component pReverse = null;
        public Container containerType = Container.Unknown;

        public abstract void Add(Component c);
        public abstract void Remove(Component c);
        public abstract void Move(float xDelta, float yDelta);
        public abstract Component GetFirstChild();

        public enum Container
        {
            LEAF,
            COMPOSITE,
            Unknown
        }

    }
}
