using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class FlyingSaucer : Alien
    {
        Random r = new Random();
        int lowPoints = 50;
        int medPoints = 100;
        int hiPoints = 150;

        public FlyingSaucer(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;
        }

        ~FlyingSaucer()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitFlyingSaucer(this);
        }

        public override int GetPoints()
        {
            int choice = r.Next(0, 3);
            int points = 0;

            // Randomly select low, med, or hi points
            switch (choice)
            {
                case 0:
                    points = lowPoints;
                    break;
                case 1:
                    points = medPoints;
                    break;
                case 2:
                    points = hiPoints;
                    break;
            }

            return points;
        }

        public override void Update()
        {
            base.Update();
        }
    }
}
