using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract public class FallStrategy
    {
        abstract public void Fall( Bomb pBomb );
        abstract public void Reset( float posY );
        
    }
}
