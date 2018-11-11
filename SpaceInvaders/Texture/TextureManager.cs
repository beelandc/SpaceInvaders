using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class TextureManager : Manager
    {
        private static TextureManager pInstance;
        private Texture pTextureCompare;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private TextureManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pTextureCompare = new Texture();
        }

        ~TextureManager()
        {
            this.pTextureCompare = null;
            TextureManager.pInstance = null;
        }

        //----------------------------------------------------------------------
        // Static Methods
        //----------------------------------------------------------------------
        public static void Create(int reserveNum = 3, int reserveGrow = 1)
        {
            // make sure values are ressonable 
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            // initialize the singleton here
            Debug.Assert(pInstance == null);

            // Do the initialization
            if (pInstance == null)
            {
                pInstance = new TextureManager(reserveNum, reserveGrow);

                // Default texture
                TextureManager.Add(Texture.Name.Default, "RedGrid.tga");

                // NullObject texture
                TextureManager.Add(Texture.Name.NullTexture, "RedGrid.tga");
            }
        }

        public static Texture Add(Texture.Name name, string pTextureName)
        {
            TextureManager pMan = TextureManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            Texture pTexture = (Texture)pMan.BaseAdd();
            Debug.Assert(pTexture != null);

            // Initialize the date
            pTexture.Set(name, pTextureName);
            return pTexture;
        }

        public static Texture Find(Texture.Name name)
        {
            TextureManager pMan = TextureManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            // Use compare node to compare to search nodes
            pMan.pTextureCompare.SetName(name);

            Texture pData = (Texture)pMan.BaseFind(pMan.pTextureCompare);
            return pData;
        }

        public static void Remove(Texture pNode)
        {
            TextureManager pMan = TextureManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            Debug.Assert(pNode != null);
            pMan.BaseRemove(pNode);
        }

        public static void Print()
        {
            TextureManager pMan = TextureManager.PrivGetInstance();
            Debug.Assert(pMan != null);

            Debug.WriteLine("******** TEXTURE MANAGER ****************");
            pMan.PrintStats();
            pMan.PrintNodes();
        }

        public static void Destroy()
        {
            TextureManager pInstance = TextureManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.BaseDestroy();

            pInstance.pTextureCompare = null;
            TextureManager.pInstance = null;
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        protected override bool DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            Texture pDataA = (Texture)pLinkA;
            Texture pDataB = (Texture)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new Texture();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            Texture pTexture = (Texture)pLink;
            pTexture.SetAzulTexture(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static TextureManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);
            return pInstance;
        }
    }
}
