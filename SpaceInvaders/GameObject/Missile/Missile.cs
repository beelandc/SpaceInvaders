using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Missile : Leaf
    {
        private bool enable;
        public float delta;

        public Missile(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;
            this.enable = false;
            this.delta = 10.0f;
        }

        ~Missile()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitMissile(this);
        }

        public override void VisitAlienGrid(AlienGrid ag)
        {
            // AlienGrid vs Missile
            //Debug.WriteLine("         collide:  {0} <-> {1}", ag.GetName(), this.name);

            // AlienGridColumn vs Missile
            GameObject pGameObj = Iterator.GetChildGameObject(ag);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitAlienGridColumn(AlienGridColumn agc)
        {
            // AlienGridColumn vs Missile
            //Debug.WriteLine("         collide:  {0} <-> {1}", agc.GetName(), this.name);

            // Alien vs Missile
            GameObject pGameObj = Iterator.GetChildGameObject(agc);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitAlien(Alien a)
        {
            //Alien vs Missile
            //Debug.WriteLine("         collide:  {0} <-> {1}", a.GetName(), this.name);

            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(a, this);
            pColPair.NotifyListeners();
 
        }

        public override void VisitShieldBrick(ShieldBrick sb)
        {
            //ShieldBrick vs Missile
            //Debug.WriteLine("         collide:  {0} <-> {1}", sb.GetName(), this.name);

            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(sb, this);
            pColPair.NotifyListeners();
        }

        public override void VisitBombRoot(BombRoot br)
        {
            // BombRoot vs Missile
            GameObject pGameObj = Iterator.GetChildGameObject(br);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitBomb(Bomb b)
        {
            // Bomb vs Missile
            
            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(this, b);
            pColPair.NotifyListeners();
        }

        public override void VisitFlyingSaucerRoot(FlyingSaucerRoot fsr)
        {
            // FlyingSaucerRoot vs Missile
            GameObject pGameObj = Iterator.GetChildGameObject(fsr);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitFlyingSaucer(FlyingSaucer fs)
        {
            // Notify Observers
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(fs, this);
            pColPair.NotifyListeners();
        }

        public override void Update()
        {
            base.Update();
            this.y += delta;
        }

        public void SetActive(bool state)
        {
            this.enable = state;
        }

    }
}
