using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class BombRoot: Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.BombRoot;

        public BombRoot(Composite.CompositeName inCompositeName, float posX, float posY)
            : base(pGameObject)
        {
            this.compositeName = inCompositeName;

            this.x = posX;
            this.y = posY;

            this.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(0, 0, 1);
        }

        public BombRoot(Composite.CompositeName compositeName)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
        }

        ~BombRoot()
        {
        }

        public override void Accept(CollisionVisitor other)
        {
            // Call the appropriate collision reaction            
            other.VisitBombRoot(this);
        }

    }
}
