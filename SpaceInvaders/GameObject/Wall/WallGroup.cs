using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class WallGroup : Composite
    {
        public WallGroup(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;

            this.GetCollisionObject().GetCollisionSpriteBox().SetLineColor(1, 1, 1);
        }

        ~WallGroup()
        {

        }

        public override void Accept(CollisionVisitor other)
        {           
            other.VisitWallGroup(this);
        }

        public override void VisitAlienGrid(AlienGrid a)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);
            
            // AlienGrid vs WallGroup
            // go down a level in Wall Group.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(a, pGameObj);
        }

        public override void VisitMissileGroup(MissileGroup mg)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // MissileGroup vs WallGroup
            // go down a level in Wall Group.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(mg, pGameObj);
        }

        public override void VisitBombRoot(BombRoot br)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // BombRoot vs WallGroup
            // go down a level in Wall Group.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(br, pGameObj);
        }

        public override void VisitCoreCannonGroup(CoreCannonGroup ccg)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // CoreCannonGroup vs WallGroup
            // go down a level in Wall Group.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(ccg, pGameObj);
        }

        public override void VisitFlyingSaucerRoot(FlyingSaucerRoot fsr)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // BombRoot vs WallGroup
            // go down a level in Wall Group.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(fsr, pGameObj);
        }

    }
}
