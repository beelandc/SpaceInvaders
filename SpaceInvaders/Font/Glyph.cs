using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Glyph: DLink
    {
        public Name name;
        public int key;
        private Azul.Rect pSubRect;
        private Texture pTexture;

        public enum Name
        {
            Consolas20pt,

            NullObject,
            Uninitialized,
            Consolas36pt
        }

        public Glyph()
            : base()
        {
            this.name = Name.Uninitialized;
            this.pTexture = null;
            this.pSubRect = new Azul.Rect();
            this.key = 0;
        }

        ~Glyph()
        {
            this.name = Name.Uninitialized;
            this.pSubRect = null;
            this.pTexture = null;
        }

        public void Set(Glyph.Name name, int key, Texture.Name textName, float x, float y, float width, float height)
        {
            Debug.Assert(this.pSubRect != null);
            this.name = name;

            this.pTexture = TextureManager.Find(textName);
            Debug.Assert(this.pTexture != null);

            this.pSubRect.Set(x, y, width, height);

            this.key = key;

        }

        public void Wash()
        {
            this.name = Name.Uninitialized;
            this.pTexture = null;
            this.pSubRect.Set(0,0,1,1);
            this.key = 0;
        }

        public override void Print()
        {
            base.Print();

            // Data:
            Debug.WriteLine("\t\tname: {0} ({1})", this.name, this.GetHashCode());
            Debug.WriteLine("\t\t\tkey: {0}", this.key);
            if (this.pTexture != null)
            {
                Debug.WriteLine("\t\t   pTexture: {0}", this.pTexture.GetName());
            }
            else
            {
                Debug.WriteLine("\t\t   pTexture: null");
            }
            Debug.WriteLine("\t\t      pRect: {0}, {1}, {2}, {3}", this.pSubRect.x, this.pSubRect.y, this.pSubRect.width, this.pSubRect.height);
        }

        public Azul.Rect GetAzulSubRect()
        {
            Debug.Assert(this.pSubRect != null);
            return this.pSubRect;
        }

        public Azul.Texture GetAzulTexture()
        {
            Debug.Assert(this.pTexture != null);
            return this.pTexture.GetAzulTexture();
        }

    }
}
