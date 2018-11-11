using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class RightWall : Wall
    {
        public RightWall(GameObject.Name name, Sprite.Name spriteName, float posX, float posY, float width, float height)
            : base(name, spriteName, Wall.Type.Right)
        {
            this.GetCollisionObject().GetCollisionRect().Set(posX, posY, width, height);

            this.x = posX;
            this.y = posY;

            this.SetCollisionObjectLineColor(1, 1, 0);
        }

        ~RightWall()
        {

        }

        public override void Accept(CollisionVisitor other)
        {          
            other.VisitRightWall(this);
        }

        public override void Update()
        {
            base.Update();
        }

        public override void VisitAlienGrid(AlienGrid a)
        {
            // AlienGrid vs RightWall
            // Debug.WriteLine("collide: {0} with {1}", this, a);

            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(a, this);
            pColPair.NotifyListeners();
        }

    }
}
