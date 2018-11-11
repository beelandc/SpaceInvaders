using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class ShieldBrick : ShieldCategory
    {
        public ShieldBrick(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName, ShieldCategory.Type.Brick)
        {
            this.x = posX;
            this.y = posY;

            this.SetCollisionObjectLineColor(1.0f, 1.0f, 1.0f);
        }

        ~ShieldBrick()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            // Important: at this point we have an Alien
            // Call the appropriate collision reaction            
            other.VisitShieldBrick(this);
        }

        public override void VisitMissileGroup(MissileGroup mg)
        {
            //Debug.WriteLine("collide: {0} with {1}", mg, this);

            // MissileGroup vs ShieldBrick
            // go down a level in Missile Group.
            GameObject pGameObj = Iterator.GetChildGameObject(mg);
            CollisionPair.Collide(this, pGameObj);
        }

        public override void VisitBombRoot(BombRoot br)
        {
            //Debug.WriteLine("collide: {0} with {1}", br, this);

            // BombRoot vs Shield Brick
            // go down a level in Bomb composite.
            GameObject pGameObj = Iterator.GetChildGameObject(br);

            CollisionPair.Collide(this, pGameObj);

        }

        public override void VisitAlienGrid(AlienGrid ag)
        {
            //Debug.WriteLine("collide: {0} with {1}", ag, this);

            // AlienGrid vs Shield Brick
            // go down a level in AlienGrid composite.
            GameObject pGameObj = Iterator.GetChildGameObject(ag);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitAlienGridColumn(AlienGridColumn agc)
        {

            //Debug.WriteLine("         collide:  {0} <-> {1}", agc.GetName(), this.name);

            // AlienGridColumn vs ShieldBrick
            GameObject pGameObj = Iterator.GetChildGameObject(agc);
            CollisionPair.Collide(this, pGameObj);
        }
    }
}
