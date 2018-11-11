using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class CrabAlien : Alien
    {
        int crabPoints = 20;

        public CrabAlien(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;
        }

        ~CrabAlien()
        {

        }

        public override int GetPoints()
        {
            return crabPoints;
        }

        public override void Update()
        {
            base.Update();
        }
    }
}
