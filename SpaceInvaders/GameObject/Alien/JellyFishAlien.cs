using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class JellyfishAlien : Alien
    {
        int jellyfishPoints = 10;

        public JellyfishAlien(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;
        }

        ~JellyfishAlien()
        {

        }

        public override int GetPoints()
        {
            return jellyfishPoints;
        }

        public override void Update()
        {
            base.Update();
        }

    }
}
