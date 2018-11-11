using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class ShieldGroup : Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.ShieldGroup;

        public ShieldGroup(Composite.CompositeName compositeName, float posX, float posY)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
            this.x = posX;
            this.y = posY;
        }

        ~ShieldGroup()
        {
        }

        public override void Accept(CollisionVisitor other)
        {
            // Call the appropriate collision reaction            
            other.VisitShieldGroup(this);
        }

        public override void VisitMissileGroup(MissileGroup mg)
        {
            //Debug.WriteLine("collide: {0} with {1}", mg, this);

            // MissileGroup vs ShieldGroup
            // go down a level in Shield composite
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(mg, pGameObj);
        }

        public override void VisitBombRoot(BombRoot br)
        {
            //Debug.WriteLine("collide: {0} with {1}", br, this);

            // BombRoot vs ShieldGroup
            // go down a level in Shield composite
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(br, pGameObj);
        }

        public override void VisitAlienGrid(AlienGrid ag)
        {
            //Debug.WriteLine("collide: {0} with {1}", ag, this);

            // AlienGrid vs ShieldGroup
            // go down a level in Shield composite
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(ag, pGameObj);
        }
    }
}
