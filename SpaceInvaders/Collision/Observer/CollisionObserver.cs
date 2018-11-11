using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract public class CollisionObserver : DLink
    {
        public abstract void Notify();

        public virtual void ExecuteDelayed()
        {
            // default implementation
        }

        public CollisionSubject pSubject;
    }
}
