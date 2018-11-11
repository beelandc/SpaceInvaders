using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class TopWall : Wall
    {
        public TopWall(GameObject.Name name, Sprite.Name spriteName, float posX, float posY, float width, float height)
        : base(name, spriteName, Wall.Type.Left)
        {
            this.GetCollisionObject().GetCollisionRect().Set(posX, posY, width, height);

            this.x = posX;
            this.y = posY;

            this.SetCollisionObjectLineColor(1, 1, 0);
        }

        ~TopWall()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitTopWall(this);
        }

        public override void Update()
        {
            base.Update();
        }

        public override void VisitMissileGroup(MissileGroup mg)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // MissileGroup vs TopWall
            // go down a level in Missile Group.
            GameObject pGameObj = Iterator.GetChildGameObject(mg);
            CollisionPair.Collide(pGameObj, this);
        }

        // TODO Remove - Shouldn't need this one for TopWall
        public override void VisitMissile(Missile m)
        {
            // Missile vs TopWall
            //       Debug.WriteLine("collide: {0} with {1}", this, a);

            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(this, m);
            pColPair.NotifyListeners();
        }
        
    }
}
