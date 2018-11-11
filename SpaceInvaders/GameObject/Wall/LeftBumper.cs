using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class LeftBumper : Wall
    {
        public LeftBumper(GameObject.Name name, Sprite.Name spriteName, float posX, float posY, float width, float height)
            : base(name, spriteName, Wall.Type.Left)
        {
            this.GetCollisionObject().GetCollisionRect().Set(posX, posY, width, height);

            this.x = posX;
            this.y = posY;

            this.SetCollisionObjectLineColor(1, 1, 0);
        }

        ~LeftBumper()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitLeftBumper(this);
        }

        public override void Update()
        {
            base.Update();
        }

        public override void VisitCoreCannonGroup(CoreCannonGroup ccg)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // CoreCannonGroup vs LeftBumper
            // go down a level in CoreCannonGroup
            GameObject pGameObj = Iterator.GetChildGameObject(ccg);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitCoreCannon(CoreCannon cc)
        {
            // CoreCannon vs LeftBumper
            // Debug.WriteLine("collide: {0} with {1}", this, a);

            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(cc, this);
            pColPair.NotifyListeners();
        }
    }
}
