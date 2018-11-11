using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class FlyingSaucerBomb : Bomb
    {
        public FlyingSaucerBomb(GameObject.Name name, Sprite.Name spriteName, FallStrategy strategy, float posX, float posY)
            : base(name, spriteName, strategy, posX, posY)
        {
            // Do nothing at this layer
        }
    }
}
