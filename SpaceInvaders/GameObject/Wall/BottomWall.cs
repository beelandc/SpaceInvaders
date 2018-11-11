using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class BottomWall : Wall
    {
        public BottomWall(GameObject.Name name, Sprite.Name spriteName, float posX, float posY, float width, float height)
        : base(name, spriteName, Wall.Type.Left)
        {
            this.GetCollisionObject().GetCollisionRect().Set(posX, posY, width, height);

            this.x = posX;
            this.y = posY;

            this.SetCollisionObjectLineColor(1, 1, 0);
        }

        ~BottomWall()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitBottomWall(this);
        }

        public override void Update()
        {
            base.Update();
        }

        public override void VisitAlienGrid(AlienGrid a)
        {
            //Debug.WriteLine("collide: {0} with {1}", this, a);
            
            // AlienGrid vs BottomWall
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(a, this);
            pColPair.NotifyListeners();
        }

        public override void VisitBombRoot(BombRoot br)
        {
            //Debug.WriteLine("collide: {0} with {1}", br, this);

            // BombRoot vs BottomWall
            // go down a level in Missile Group.
            GameObject pGameObj = Iterator.GetChildGameObject(br);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitBomb(Bomb b)
        {
            //Debug.WriteLine("collide: {0} with {1}", b, this);

            // Bomb vs BottomWall
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(this, b);
            pColPair.NotifyListeners();
        }

    }
}
