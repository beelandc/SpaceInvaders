using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Bomb : Leaf
    {
        public float delta;
        private FallStrategy pStrategy;

        public Bomb(GameObject.Name name, Sprite.Name spriteName, FallStrategy strategy, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;
            this.delta = 4.0f;

            Debug.Assert(strategy != null);
            this.pStrategy = strategy;

            this.pStrategy.Reset(this.y);

            this.SetCollisionObjectLineColor(1, 1, 0);
        }


        public void Reset()
        {
            this.y = 700.0f;
            this.pStrategy.Reset(this.y);
        }

        public override void Update()
        {
            base.Update();
            this.y -= delta;

            // Strategy
            this.pStrategy.Fall(this);
        }

        public float GetBoundingBoxHeight()
        {
            return this.pCollisionObject.GetCollisionRect().height;
        }

        ~Bomb()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            // Call the appropriate collision reaction            
            other.VisitBomb(this);
        }

        public void MultiplyScale(float sx, float sy)
        {
            Debug.Assert(this.pSpriteProxy != null);

            this.pSpriteProxy.SetSX(this.pSpriteProxy.GetSX() * sx);
            this.pSpriteProxy.SetSY(this.pSpriteProxy.GetSY() * sy);
        }

        public override void VisitShieldBrick(ShieldBrick sb)
        {
            //ShieldBrick vs Bomb
            //Debug.WriteLine("         collide:  {0} <-> {1}", sb.GetName(), this.name);

            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(sb, this);
            pColPair.NotifyListeners();
        }

        public override void VisitBottomWall(BottomWall w)
        {
            //ShieldBrick vs Bomb
            //Debug.WriteLine("         collide:  {0} <-> {1}", w.GetName(), this.name);

            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(w, this);
            pColPair.NotifyListeners();
        }

    }
}