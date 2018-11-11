using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class SquidAlien : Alien
    {
        int squidPoints = 30;

        public SquidAlien(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;
        }

        ~SquidAlien()
        {

        }

        public override int GetPoints()
        {
            return squidPoints;
        }

        public override void Update()
        {
            base.Update();
        }
    }
}
