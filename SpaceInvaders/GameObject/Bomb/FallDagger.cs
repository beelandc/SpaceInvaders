using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class FallDagger : FallStrategy
    {
        public FallDagger()
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
            
            float targetY = oldPosY - 1.0f * pBomb.GetBoundingBoxHeight();

            if( pBomb.GetY() < targetY )
            {
                pBomb.MultiplyScale(1.0f, -1.0f);
                oldPosY = targetY;
            }
        }

        // Data
        private float oldPosY;
    }
}
