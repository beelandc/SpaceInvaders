using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Wall : Leaf
    {
        protected Wall.Type type;

        protected Wall(GameObject.Name name, Sprite.Name spriteName, Wall.Type type)
            : base(name, spriteName)
        {
            this.type = type;
        }

        ~Wall()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            throw new NotImplementedException();
        }

        public Wall.Type GetCategoryType()
        {
            return this.type;
        }

        public override void VisitBombRoot(BombRoot br)
        {
            // Do nothing here -- Override in Bottom Wall class for bomb <--> bottom wall collision
        }

        public override void VisitBomb(Bomb b)
        {
            // Do nothing here -- Override in Bottom Wall class for bomb <--> bottom wall collision
        }

        public override void VisitAlienGrid(AlienGrid ag)
        {
            // Do nothing here -- Override in Bottom Wall class for AlienGrid <--> bottom wall collision
        }

        public override void VisitFlyingSaucerRoot(FlyingSaucerRoot fsr)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // FlyingSaucerRoot vs Wall
            // go down a level in Flying Saucer composite.
            GameObject pGameObj = Iterator.GetChildGameObject(fsr);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitFlyingSaucer(FlyingSaucer fs)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // FlyingSaucer vs Wall
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(fs, this);
            pColPair.NotifyListeners();
        }

        public enum Type
        {
            WallGroup,
            Right,
            Left,
            Bottom,
            Top,
            Unitialized
        }

    }
}
