using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class MissileGroup : Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.MissileGroup;

        public MissileGroup(Composite.CompositeName inCompositeName, float posX, float posY)
            : base(pGameObject)
        {
            this.compositeName = inCompositeName;

            this.x = posX;
            this.y = posY;

            this.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(0, 0, 1);
        }

        public MissileGroup(Composite.CompositeName compositeName)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitMissileGroup(this);
        }

        public override void VisitAlienGrid(AlienGrid ag)
        {
            // AlienGrid vs MissileGroup
            //Debug.WriteLine("         collide:  {0} <-> {1}", ag.GetName(), this.name);

            // AlienGrid vs Missile
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(ag, pGameObj);
        }

        public override void VisitShieldBrick(ShieldBrick sb)
        {
            // ShieldBrick vs Missile
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(sb, pGameObj);
        }

        public override void VisitBombRoot(BombRoot br)
        {
            // BombRoot vs MissileGroup
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(br, pGameObj);
        }

        public override void VisitFlyingSaucerRoot(FlyingSaucerRoot fsr)
        {
            // FlyingSaucerRoot vs MissileGroup
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(fsr, pGameObj);
        }
    }
}
