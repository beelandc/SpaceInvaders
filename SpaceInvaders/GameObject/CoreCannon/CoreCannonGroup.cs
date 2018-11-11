using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CoreCannonGroup : Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.CoreCannonGroup;

        public CoreCannonGroup(Composite.CompositeName compositeName, float posX, float posY)
            : base(pGameObject)
        {
            this.x = posX;
            this.y = posY;

            this.compositeName = compositeName;
            this.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(0, 0, 1);
        }

        public CoreCannonGroup(Composite.CompositeName compositeName)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitCoreCannonGroup(this);
        }

        public override void VisitBombRoot(BombRoot br)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // BombRoot vs CoreCannonGroup
            // go down a level in CoreCannon composite
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(br, pGameObj);
        }

    }
}
