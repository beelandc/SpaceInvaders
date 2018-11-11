using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Image : DLink
    {
        private Name name;
        private Texture pTexture;
        private Azul.Rect pTextureRect;

        //---------------------------------------------------------------------------------------------------------
        // Constructor
        //---------------------------------------------------------------------------------------------------------

        public Image()
        : base()
        {
            this.pTextureRect = new Azul.Rect();
            Debug.Assert(this.pTextureRect != null);

            this.Clean();
        }

        ~Image()
        {
            this.name = Name.Uninitialized;
            this.pTexture = null;
            this.pTextureRect = null;
        }

        public void Set(Name name, Texture texture, float x, float y, float width, float height)
        {
            this.name = name;

            this.pTexture = texture;
            //Debug.Assert(this.texture != null);

            this.pTextureRect = new Azul.Rect(x, y, width, height);
            Debug.Assert(pTextureRect != null);
        }

        public Azul.Texture GetAzulTexture()
        {
            return this.pTexture.GetAzulTexture();
        }

        public void SetTexture(Texture newTexture)
        {
            this.pTexture = newTexture;
        }

        public Azul.Rect GetTextureRect()
        {
            Debug.Assert(this.pTextureRect != null);
            return this.pTextureRect;
        }

        public void SetTextureRect(Azul.Rect newRect)
        {
            this.pTextureRect = newRect;
        }

        public override void Clean()
        {
            base.Clean();
            this.name = Name.Uninitialized;

            // TODO Refactor once we have defaults for these
            if (this.pTexture != null)
            {
                this.pTexture.Clean();
            }
            if(this.pTextureRect != null){
                this.pTextureRect.Clear();
            }
        }

        public override void Print()
        {
            Debug.WriteLine("Image ({0})", this.GetHashCode());
            Debug.WriteLine("    Name: {0}", this.name);
            // TODO What else to print here?
            //Debug.WriteLine("    X: {0}", this.x);
            base.Print();
        }

        public void SetName(Name newName)
        {
            this.name = newName;
        }

        public Name GetName()
        {
            return name;
        }

        public enum Name
        {
            Default,
            OpenCrabAlien,
            ClosedCrabAlien,
            OpenSquidAlien,
            ClosedSquidAlien,
            OpenJellyfishAlien,
            ClosedJellyfishAlien,
            FlyingSaucer,
            CoreCannon,
            Missle,

            NullImage,
            Uninitialized,
            Brick,
            BrickLeft_Top0,
            BrickLeft_Top1,
            BrickLeft_Bottom,
            BrickRight_Top0,
            BrickRight_Top1,
            BrickRight_Bottom,
            BombStraight,
            BombZigZag,
            BombCross
        };

    }

}
