using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class ShieldRoot: Composite
    {
        public ShieldRoot(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;

        }

        ~ShieldRoot()
        {
        }

        public override void Accept(CollisionVisitor other)
        {
            // Important: at this point we have an Alien
            // Call the appropriate collision reaction            
            other.VisitShieldRoot(this);
        }

        public override void VisitMissileGroup(MissileGroup mg)
        {
            //Debug.WriteLine("collide: {0} with {1}", mg, this);

            // MissileGroup vs ShieldRoot
            // go down a level in Shield composite.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(mg, pGameObj);
        }

        public override void VisitBombRoot(BombRoot br)
        {
            //Debug.WriteLine("collide: {0} with {1}", br, this);

            // BombRoot vs ShieldRoot
            // go down a level in Shield composite.
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(br, pGameObj);
        }

        public override void VisitAlienGrid(AlienGrid ag)
        {
            //Debug.WriteLine("collide: {0} with {1}", ag, this);

            // AlienGrid vs ShieldRoot
            // go down a level in Shield composite
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(ag, pGameObj);
        }

    }
}
