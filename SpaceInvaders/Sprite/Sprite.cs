using System;
using System.Diagnostics;
using Azul;

namespace SpaceInvaders
{
    public class Sprite : SpriteBase
    {
        protected Name name;
        protected Azul.Sprite pAzulSprite;
        protected Image pImage;
        protected Azul.Rect pScreenRect;

        //---------------------------------------------------------------------------------------------------------
        // Constructor
        //---------------------------------------------------------------------------------------------------------

        public Sprite()
            : base()
        {
            this.Clean();
        }

        ~Sprite()
        {
            this.name = Name.Uninitialized;
            this.pImage = null;
            this.pAzulSprite = null;
            this.pScreenRect = null;

            // TODO do I want color in Sprite?
            //this.pAzulColor = null;
        }

        public void Set(Name name, Image image, float x, float y, float width, float height)
        {
            this.name = name;
            this.pImage = image;
            this.pScreenRect = new Azul.Rect(x, y, width, height);
            Debug.Assert(pScreenRect != null);

            this.x = x;
            this.y = y;

            // Configure Azul Sprite
            this.pAzulSprite = createAzulSprite(image);
        }

        public void Set(Name name, Image image, float x, float y, float width, float height, Azul.Color color)
        {
            Set(name, image, x, y, width, height);

            // Configure Azul Sprite
            this.pAzulSprite.SwapColor(color);
        }

        private Azul.Sprite createAzulSprite(Image image)
        {
            return new Azul.Sprite(image.GetAzulTexture(), image.GetTextureRect(), this.pScreenRect);
        }

        public void SwapImage(Image newImage)
        {
            Debug.Assert(newImage != null);
            pImage = newImage;
            pAzulSprite.SwapTexture(newImage.GetAzulTexture());
            pAzulSprite.SwapTextureRect(newImage.GetTextureRect());
        }

        public void SwapImage(Image newImage, float sx, float sy, float width, float height)
        {
            Debug.Assert(newImage != null);
            pImage = newImage;
            pAzulSprite.SwapTexture(newImage.GetAzulTexture());
            pAzulSprite.SwapTextureRect(newImage.GetTextureRect());
            UpdateScreenRect(sx, sy, width, height);

            pAzulSprite.SwapScreenRect(this.pScreenRect);
        }

        public void SwapColor(Azul.Color color)
        {
            pAzulSprite.SwapColor(color);
        }

        public void UpdateScreenRect(float x, float y, float width, float height)
        {
            this.pScreenRect.x = x;
            this.pScreenRect.y = y;
            this.pScreenRect.width = width;
            this.pScreenRect.height = height;
            this.pAzulSprite.SwapScreenRect(this.pScreenRect);
            this.pAzulSprite.Update();
        }

        public Azul.Rect GetScreenRect()
        {
            return this.pScreenRect;
        }

        public override void Update()
        {
            this.pAzulSprite.sx = this.sx;
            this.pAzulSprite.sy = this.sy;
            this.pAzulSprite.x = this.x;
            this.pAzulSprite.y = this.y;
            this.pAzulSprite.angle = this.angle;

            this.pAzulSprite.Update();
        }

        public override void Render()
        {
            this.pAzulSprite.Render();
        }

        public override void Clean()
        {
            base.Clean();

            // TODO Refactor once we have defaults for these
            this.pImage = null;
            this.name = Sprite.Name.Uninitialized;
            this.pAzulSprite = null;

            this.x = 0.0f;
            this.y = 0.0f;
            this.sx = 1.0f;
            this.sy = 1.0f;
            this.angle = 0.0f;
        }

        public void SetName(Name newName)
        {
            this.name = newName;
        }

        public Name GetName()
        {
            return name;
        }

        public void SetAzulSprite(Azul.Sprite newSprite)
        {
            this.pAzulSprite = newSprite;
        }

        public void SetScreenRect(Azul.Rect newRect)
        {
            this.pScreenRect = newRect;
        }

        public void SetImage(Image newImage)
        {
            this.pImage = newImage;
        }

        public override void Print()
        {
            Debug.WriteLine("Sprite ({0})", this.GetHashCode());
            Debug.WriteLine("    Name: {0}", this.name);
            // TODO What else to print here?
            //Debug.WriteLine("    X: {0}", this.x);

            base.Print();
        }

        public enum Name
        {
            CrabAlien,
            SquidAlien,
            JellyfishAlien,
            FlyingSaucer,
            CoreCannon,
            Missile,
            
            NullSprite,
            Uninitialized,
            Brick,
            Brick_LeftTop1,
            Brick_LeftTop0,
            Brick_LeftBottom,
            Brick_RightTop1,
            Brick_RightBottom,
            Brick_RightTop0,
            BombCross,
            BombStraight,
            BombZigZag,
            GiantCrabAlien
        };
    }
}
