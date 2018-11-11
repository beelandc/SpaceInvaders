using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteProxy : SpriteBase
    {
        private SpriteProxy.Name name;
        private new float x;
        private new float y;
        private new float sx;
        private new float sy;
        private Sprite pSprite;

        //---------------------------------------------------------------------------------------------------------
        // Constructors / Destructor
        //---------------------------------------------------------------------------------------------------------
        public SpriteProxy()
            : base()
        {
            this.name = SpriteProxy.Name.Uninitialized;

            this.x = 0.0f;
            this.y = 0.0f;
            this.sx = 1.0f;
            this.sy = 1.0f;

            this.pSprite = null;
        }

        public SpriteProxy(Sprite.Name name)
        {
            this.name = SpriteProxy.Name.Proxy;

            this.x = 0.0f;
            this.y = 0.0f;
            this.sx = 1.0f;
            this.sy = 1.0f;

            this.pSprite = SpriteManager.Find(name);
            Debug.Assert(this.pSprite != null);
        }

        ~SpriteProxy()
        {
            this.pSprite = null;
            this.name = SpriteProxy.Name.Uninitialized;
        }

        //---------------------------------------------------------------------------------------------------------
        // Methods
        //---------------------------------------------------------------------------------------------------------
        public void Set(SpriteProxy.Name proxyName, Sprite.Name spriteName)
        {
            this.name = proxyName;

            this.x = 0.0f;
            this.y = 0.0f;

            this.pSprite = SpriteManager.Find(spriteName);
            Debug.Assert(this.pSprite != null);
        }

        public override void Update()
        {
            // push the data from proxy to Real GameSprite
            this.PrivPushToReal();
            this.pSprite.Update();
        }

        public override void Clean()
        {
            this.x = 0.0f;
            this.y = 0.0f;
            this.sx = 1.0f;
            this.sy = 1.0f;
            this.name = Name.Uninitialized;
            this.pSprite = null;

            base.Clean();
        }

        private void PrivPushToReal()
        {
            // push the data from proxy to Real GameSprite
            Debug.Assert(this.pSprite != null);

            this.pSprite.SetX(this.x);
            this.pSprite.SetY(this.y);
            this.pSprite.SetSX(this.sx);
            this.pSprite.SetSY(this.sy);
        }

        public override void Render()
        {
            // Move the values over to Real GameSprite
            this.PrivPushToReal();

            // Update and draw real sprite 
            this.pSprite.Update();
            this.pSprite.Render();
        }

        public void SetName(Name inName)
        {
            this.name = inName;
        }

        public Name GetName()
        {
            return this.name;
        }

        public override float GetX()
        {
            return this.x;
        }

        public override void SetX(float newX)
        {
            this.x = newX;
        }

        public override float GetY()
        {
            return this.y;
        } 

        public override void SetY(float newY)
        {
            this.y = newY;
        }

        public override void SetSX(float newSX)
        {
            this.sx = newSX;
        }

        public override void SetSY(float newSY)
        {
            this.sy = newSY;
        }

        public override float GetSX()
        {
            return this.sx;
        }

        public override float GetSY()
        {
            return this.sy;
        }


        public Sprite GetSprite()
        {
            return this.pSprite;
        }

        public void SetSprite(Sprite newSprite)
        {
            this.pSprite = newSprite;
        }

        public Azul.Rect GetSpriteScreenRect()
        {
            return this.pSprite.GetScreenRect();
        }

        public enum Name
        {
            // TODO Add reasonable names here
            Proxy,
            Uninitialized,
            FlyingSaucer1,
            JellyfishAlien1,
            CrabAlien1,
            SquidAlien1
        }
    }

}
