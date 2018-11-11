using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class FlyingSaucerRoot : Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.FlyingSaucerRoot;

        public FlyingSaucerRoot(Composite.CompositeName inCompositeName, float posX, float posY)
            : base(pGameObject)
        {
            this.compositeName = inCompositeName;

            this.x = posX;
            this.y = posY;

            this.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(0, 0, 1);
        }

        public FlyingSaucerRoot(Composite.CompositeName compositeName)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
        }

        ~FlyingSaucerRoot()
        {
        }

        public override void Accept(CollisionVisitor other)
        {
            // Call the appropriate collision reaction            
            other.VisitFlyingSaucerRoot(this);
        }

    }
}
