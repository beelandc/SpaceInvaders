using System;
using System.Diagnostics;

namespace SpaceInvaders
{

    class ImageManager : Manager
    {

        private static ImageManager pInstance;
        private Image pImageCompare;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private ImageManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pImageCompare = new Image();
        }

        ~ImageManager()
        {
            this.pImageCompare = null;
            ImageManager.pInstance = null;
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
                pInstance = new ImageManager(reserveNum, reserveGrow);

                // Add a null Image into the Manager
                ImageManager.Add(Image.Name.NullImage, Texture.Name.NullTexture, 0, 0, 128, 128);

                // Default image
                ImageManager.Add(Image.Name.Default, Texture.Name.Default, 0, 0, 128, 128);
            }
        }
        public static void Destroy()
        {
            ImageManager pInstance = ImageManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.BaseDestroy();

            pInstance.pImageCompare = null;
            ImageManager.pInstance = null;
        }

        public static Image Add(Image.Name name, Texture.Name textureName, float x, float y, float width, float height)
        {
            ImageManager pInstance = ImageManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Image pImage = (Image)pInstance.BaseAdd();
            Debug.Assert(pImage != null);

            Texture pTexture = TextureManager.Find(textureName);
            Debug.Assert(pTexture != null);

            // Initialize the date
            pImage.Set(name, pTexture, x, y, width, height);
            return pImage;
        }

        public static Image Find(Image.Name name)
        {
            ImageManager pInstance = ImageManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Use compare node to compare to search nodes
            pInstance.pImageCompare.SetName(name);

            Image pData = (Image)pInstance.BaseFind(pInstance.pImageCompare);
            return pData;
        }

        public static void Remove(Image pNode)
        {
            ImageManager pInstance = ImageManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.Assert(pNode != null);
            pInstance.BaseRemove(pNode);
        }

        public static void Print()
        {
            ImageManager pInstance = ImageManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.WriteLine("******** IMAGE MANAGER ******************");
            pInstance.PrintStats();
            pInstance.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        protected override bool DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            Image pDataA = (Image)pLinkA;
            Image pDataB = (Image)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new Image();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            Image pImage = (Image)pLink;
            pImage.SetTexture(null);
            pImage.SetTextureRect(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static ImageManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);
            return pInstance;
        }
    }
}
