using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class SpriteBase : DLink
    {

        protected float x;
        protected float y;
        protected float sx;
        protected float sy;
        protected float angle;

        public SpriteBase()
            : base()
        {
            this.x = 0.0f;
            this.y = 0.0f;
            this.sx = 1.0f;
            this.sy = 1.0f;
            this.angle = 0.0f;
        }

        ~SpriteBase()
        {
            // Nothing to do for now
        }

        public virtual float GetX()
        {
            return this.x;
        }

        public virtual void SetX(float newX)
        {
            this.x = newX;
        }

        public virtual float GetY()
        {
            return this.y;
        }

        public virtual void SetY(float newY)
        {
            this.y = newY;
        }

        public virtual float GetSX()
        {
            return this.sx;
        }

        public virtual void SetSX(float newSX)
        {
            this.sx = newSX;
        }

        public virtual float GetSY()
        {
            return this.sy;
        }

        public virtual void SetSY(float newSY)
        {
            this.sy = newSY;
        }

        public void SetAngle(float newAngle)
        {
            this.angle = newAngle;
        }

        abstract public void Update();
        abstract public void Render();

    }
}
