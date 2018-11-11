using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class AlienGridColumn : Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.AlienGridColumn;

        public AlienGridColumn(Composite.CompositeName compositeName)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitAlienGridColumn(this);
        }

        public override void VisitShieldBrick(ShieldBrick sb)
        {
            // AlienGridColumn vs ShieldBrick
            Debug.WriteLine("         collide:  {0} <-> {1}", sb.GetName(), this.name);

            // Alien vs ShieldBrick
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(this, pGameObj);
        }
    }
}
