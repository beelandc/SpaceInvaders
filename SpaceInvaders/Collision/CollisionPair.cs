using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CollisionPair : DLink
    {
        // TODO Correct visibility
        public CollisionPair.Name name;
        public GameObject treeA;
        public GameObject treeB;
        public CollisionSubject pSubject;
        
        public CollisionPair()
            : base()
        {
            this.treeA = null;
            this.treeB = null;
            this.name = CollisionPair.Name.Not_Initialized;

            this.pSubject = new CollisionSubject();
            Debug.Assert(this.pSubject != null);
        }

        ~CollisionPair()
        {
            this.treeA = null;
            this.treeB = null;
            this.name = CollisionPair.Name.Not_Initialized;

            this.pSubject = null;
        }

        public void Set(CollisionPair.Name colpairName, GameObject pTreeRootA, GameObject pTreeRootB)
        {
            Debug.Assert(pTreeRootA != null);
            Debug.Assert(pTreeRootB != null);

            this.treeA = pTreeRootA;
            this.treeB = pTreeRootB;
            this.name = colpairName;
           

        }
        public void Clear()
        {
            this.treeA = null;
            this.treeB = null;
            this.name = CollisionPair.Name.Not_Initialized;

            this.pSubject = null;
        }

        public CollisionPair.Name GetName()
        {
            return this.name;
        }

        public void Process()
        {
            Collide(this.treeA, this.treeB);
        }

        static public void Collide(GameObject pSafeTreeA, GameObject pSafeTreeB)
        {
            // A vs B
            GameObject pNodeA = pSafeTreeA;
            GameObject pNodeB = pSafeTreeB;

            while (pNodeA != null)
            {
                pNodeB = pSafeTreeB;

                while (pNodeB != null)
                {
                    // Get rectangles
                    CollisionRect rectA = pNodeA.GetCollisionObject().GetCollisionRect();
                    CollisionRect rectB = pNodeB.GetCollisionObject().GetCollisionRect();

                    if (CollisionRect.Intersect(rectA, rectB))
                    {
                        pNodeA.Accept(pNodeB);
                        //break;
                    }

                    pNodeB = Iterator.GetSiblingGameObject(pNodeB);
                }

                pNodeA = Iterator.GetSiblingGameObject(pNodeA);
            }
        }
        
        public void SetName(CollisionPair.Name inName)
        {
            this.name = inName;
        }

        public void Attach(CollisionObserver observer)
        {
            this.pSubject.Attach(observer);
        }

        public void NotifyListeners()
        {
            this.pSubject.Notify();
        }

        public void SetCollision(GameObject pObjA, GameObject pObjB)
        {
            Debug.Assert(pObjA != null);
            Debug.Assert(pObjB != null);

            // GameObject pAlien = AlienCategory.GetAlien(objA, objB);
            this.pSubject.pObjA = pObjA;
            this.pSubject.pObjB = pObjB;
        }

        public override void Print()
        {
            // TODO Implement
            base.Print();
        }

        public enum Name
        {
            Alien_Missile,
            Alien_Wall,

            NullObject,
            Not_Initialized,
            Missile_Wall,
            Missile_Shield,
            Alien_Shield,
            Bomb_Shield,
            Bomb_Wall,
            CoreCannon_Wall,
            Bomb_Missile,
            FlyingSaucer_Missile,
            FlyingSaucer_Wall,
            Bomb_Ship
        }

    }
}
