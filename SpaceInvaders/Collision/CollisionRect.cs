using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CollisionRect : Azul.Rect
    {
        public CollisionRect(float x, float y, float width, float height)
            : base(x, y, width, height)
        {
        }
        public CollisionRect(Azul.Rect pRect)
            : base(pRect)
        {
        }
        public CollisionRect(CollisionRect pRect)
            : base(pRect)
        {
        }
        public CollisionRect()
            : base()
        {
        }

        static public bool Intersect(CollisionRect ColRectA, CollisionRect ColRectB)
        {
            bool status = false;

            float A_minx = ColRectA.x - ColRectA.width / 2;
            float A_maxx = ColRectA.x + ColRectA.width / 2;
            float A_miny = ColRectA.y - ColRectA.height / 2;
            float A_maxy = ColRectA.y + ColRectA.height / 2;

            float B_minx = ColRectB.x - ColRectB.width / 2;
            float B_maxx = ColRectB.x + ColRectB.width / 2;
            float B_miny = ColRectB.y - ColRectB.height / 2;
            float B_maxy = ColRectB.y + ColRectB.height / 2;

            // Trivial reject
            if ((B_maxx < A_minx) || (B_minx > A_maxx) || (B_maxy < A_miny) || (B_miny > A_maxy))
            {
                status = false;
            }
            else
            {
                status = true;
            }


            return status;
        }

        public void Union(CollisionRect ColRect)
        {
            float minX;
            float minY;
            float maxX;
            float maxY;


            if ((this.x - this.width / 2) < (ColRect.x - ColRect.width / 2))
            {
                minX = (this.x - this.width / 2);
            }
            else
            {
                minX = (ColRect.x - ColRect.width / 2);
            }

            if ((this.x + this.width / 2) > (ColRect.x + ColRect.width / 2))
            {
                maxX = (this.x + this.width / 2);
            }
            else
            {
                maxX = (ColRect.x + ColRect.width / 2);
            }

            if ((this.y + this.height / 2) > (ColRect.y + ColRect.height / 2))
            {
                maxY = (this.y + this.height / 2);
            }
            else
            {
                maxY = (ColRect.y + ColRect.height / 2);
            }

            if ((this.y - this.height / 2) < (ColRect.y - ColRect.height / 2))
            {
                minY = (this.y - this.height / 2);
            }
            else
            {
                minY = (ColRect.y - ColRect.height / 2);
            }

            this.width = (maxX - minX);
            this.height = (maxY - minY);
            this.x = minX + this.width / 2;
            this.y = minY + this.height / 2;
        }

    }
}