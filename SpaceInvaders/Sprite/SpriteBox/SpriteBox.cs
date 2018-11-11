using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteBox : SpriteBase
    {
        private Name name;
        private Azul.SpriteBox pAzulSpriteBox;
        private Azul.Color pLineColor;

        static private Azul.Rect psTmpRect = new Azul.Rect();
        static private Azul.Color psTmpColor = new Azul.Color(1, 1, 1);

        //---------------------------------------------------------------------------------------------------------
        // Constructor
        //---------------------------------------------------------------------------------------------------------
        public SpriteBox()
            : base()
        {
            Debug.Assert(SpriteBox.psTmpRect != null);
            SpriteBox.psTmpRect.Set(0, 0, 1, 1);
            Debug.Assert(SpriteBox.psTmpColor != null);
            SpriteBox.psTmpColor.Set(1, 1, 1);

            this.pAzulSpriteBox = new Azul.SpriteBox(psTmpRect, psTmpColor);
            Debug.Assert(this.pAzulSpriteBox != null);

            this.pLineColor = new Azul.Color(1, 1, 1);
            Debug.Assert(this.pLineColor != null);

            this.name = Name.Default;
        }

        ~SpriteBox()
        {
            this.name = Name.Uninitialized;
            this.pLineColor = null;
            this.pAzulSpriteBox = null;

        }

        public void SetScreenRect(float x, float y, float width, float height)
        {
            this.Set(this.name, x, y, width, height);
        }

        public void Set(Name name, float x, float y, float width, float height)
        {
            // Default sx, sy to 1.0f
            Set(name, x, y, 1.0f, 1.0f, width, height, this.pLineColor);
        }

        public void Set(Name name, float x, float y, float width, float height, Azul.Color color)
        {
            // Default sx, sy to 1.0f
            Set(name, x, y, 1.0f, 1.0f, width, height, color);
        }

        public void Set(Name name, float x, float y, float sx, float sy, float width, float height, Azul.Color color)
        {
            Debug.Assert(this.pAzulSpriteBox != null);
            Debug.Assert(this.pLineColor != null);

            Debug.Assert(psTmpRect != null);
            SpriteBox.psTmpRect.Set(x, y, width, height);

            this.name = name;

            if (color == null)
            {
                this.pLineColor.Set(1, 1, 1);
            }
            else
            {
                this.pLineColor.Set(color);
            }

            this.pAzulSpriteBox.Swap(psTmpRect, this.pLineColor);
            Debug.Assert(this.pAzulSpriteBox != null);

            this.x = pAzulSpriteBox.x;
            this.y = pAzulSpriteBox.y;
            this.sx = pAzulSpriteBox.sx;
            this.sy = pAzulSpriteBox.sy;
            this.angle = pAzulSpriteBox.angle;
        }

        public void SwapColor(Azul.Color newColor)
        {
            Debug.Assert(newColor != null);
            this.pAzulSpriteBox.SwapColor(newColor);
        }

        public override void Update()
        {
            // TODO Should this method should be the only place where the Azul.SpriteBox gets updated?

            this.pAzulSpriteBox.sx = this.sx;
            this.pAzulSpriteBox.sy = this.sy;
            this.pAzulSpriteBox.x = this.x;
            this.pAzulSpriteBox.y = this.y;
            this.pAzulSpriteBox.angle = this.angle;

            this.pAzulSpriteBox.Update();
        }

        public override void Render()
        {
            this.pAzulSpriteBox.Render();
        }

        public Azul.SpriteBox GetAzulSpriteBox()
        {
            return pAzulSpriteBox;
        }

        public void SetName(Name newName)
        {
            this.name = newName;
        }

        public Name GetName()
        {
            return name;
        }

        public void SetLineColor(Azul.Color newColor)
        {
            this.pLineColor = newColor;
        }

        public void SetLineColor(float red, float green, float blue, float alpha = 1.0f)
        {
            Debug.Assert(this.pLineColor != null);
            this.pLineColor.Set(red, green, blue, alpha);
        }

        public void SetAzulSpriteBox(Azul.SpriteBox newSpriteBox)
        {
            this.pAzulSpriteBox = newSpriteBox;
        }

        public override void Print()
        {
            Debug.WriteLine("SpriteBox ({0})", this.GetHashCode());
            Debug.WriteLine("    Name: {0}", this.name);
            // TODO What else to print here?
            //Debug.WriteLine("    X: {0}", this.x);

            base.Print();
        }

        public enum Name
        {
            Default,
            Uninitialized,
            Box
        };
    }

}
