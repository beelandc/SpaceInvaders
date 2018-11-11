using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class Alien : Leaf
    {

        //----------------------------------------------------------------------
        // Constructors / Destructor
        //----------------------------------------------------------------------

        protected Alien(GameObject.Name gameName, Sprite.Name spriteName)
            : base(gameName, spriteName)
        {
            // Do nothing at this layer
        }

        ~Alien()
        {
            // Do nothing at this layer
        }

        //----------------------------------------------------------------------
        // Abstract Methods
        //----------------------------------------------------------------------
        // Strategy
        public abstract int GetPoints();

        //----------------------------------------------------------------------
        // Methods
        //----------------------------------------------------------------------
        public override void Accept(CollisionVisitor other)
        {
            other.VisitAlien(this);
        }

        override public void Move(float xDelta, float yDelta)
        {
            this.x = this.x + xDelta;
            this.y = this.y + yDelta;

        }
        
        public override void VisitShieldBrick(ShieldBrick sb)
        {
            //ShieldBrick vs Missile
            Debug.WriteLine("         collide:  {0} <-> {1}", sb.GetName(), this.name);

            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(sb, this);
            pColPair.NotifyListeners();
        }

        public enum Type
        {
            Squid,
            Crab,
            JellyFish,
            FlyingSaucer
        }
    }
}
