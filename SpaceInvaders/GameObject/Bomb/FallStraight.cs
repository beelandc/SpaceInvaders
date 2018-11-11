using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class FallStraight : FallStrategy
    {
        public FallStraight()
        {
            this.oldPosY = 0.0f;
        }

        public override void Reset( float posY )
        {
            this.oldPosY = posY;
        }

        public override void Fall( Bomb pBomb )
        {
            Debug.Assert(pBomb != null);
            
            // Do nothing for this strategy
        }

        // Data
        private float oldPosY;
    }
}
