using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class GhostManager : GameObjectManager
    {
        
        public GhostManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        { 

        }
        
        // Do I need to do anything else here?

    }
}
