using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Texture : DLink
    {
        private Name name;
        private Azul.Texture pAzulTexture;

        //---------------------------------------------------------------------------------------------------------
        // Static Data
        //---------------------------------------------------------------------------------------------------------
        static private Azul.Texture psDefaultAzulTexture = new Azul.Texture("RedGrid.tga", Azul.Texture_Filter.NEAREST, Azul.Texture_Filter.NEAREST);

        //---------------------------------------------------------------------------------------------------------
        // Constructor
        //---------------------------------------------------------------------------------------------------------
        public Texture()
            : base()
        {
            Debug.Assert(Texture.psDefaultAzulTexture != null);

            this.pAzulTexture = psDefaultAzulTexture;
            Debug.Assert(this.pAzulTexture != null);

            this.name = Name.Default;
        }

        public void Set(Name name, string pTextureName)
        {
            Debug.Assert(pTextureName != null);

            this.pAzulTexture = new Azul.Texture(pTextureName);
            Debug.Assert(pAzulTexture != null);

            this.name = name;
        }

        public void Set(Name name, Azul.Texture pAzulTexture)
        {
            Debug.Assert(pAzulTexture != null);

            this.pAzulTexture = pAzulTexture;

            this.name = name;
        }

        public void SetAzulTexture(Azul.Texture newTexture)
        {
            this.pAzulTexture = newTexture;
        }

        public Azul.Texture GetAzulTexture()
        {
            return pAzulTexture;
        }

        public override void Clean()
        {
            base.Clean();
            name = Name.Uninitialized;
            pAzulTexture = null;
        }

        public override void Print()
        {
            Debug.WriteLine("Texture ({0})", this.GetHashCode());
            Debug.WriteLine("    Name: {0}", this.name);

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
            Aliens,

            NullTexture,
            Uninitialized,
            Shield,
            Consolas20pt,
            Consolas36pt
        };

    }
}
