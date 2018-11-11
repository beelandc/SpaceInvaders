using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteManager : Manager
    {

        private static SpriteManager pInstance;
        private Sprite pSpriteCompare;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private SpriteManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            this.pSpriteCompare = new Sprite();
        }

        ~SpriteManager()
        {
            this.pSpriteCompare = null;
            SpriteManager.pInstance = null;
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
                pInstance = new SpriteManager(reserveNum, reserveGrow);

                // Add a null sprite
                SpriteManager.Add(Sprite.Name.NullSprite, Image.Name.NullImage, 0, 0, 0, 0);
            }
        }
        public static void Destroy()
        {
            SpriteManager pInstance = SpriteManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            pInstance.BaseDestroy();

            pInstance.pSpriteCompare = null;
            SpriteManager.pInstance = null;
        }

        public static Sprite Add(Sprite.Name name, Image.Name imageName, float x, float y, float width, float height)
        {
            SpriteManager pInstance = SpriteManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Sprite pSprite = (Sprite)pInstance.BaseAdd();
            Debug.Assert(pSprite != null);

            Image pImage = ImageManager.Find(imageName);
            Debug.Assert(pImage != null);

            // Initialize the sprite data
            pSprite.Set(name, pImage, x, y, width, height);
            return pSprite;
        }

        public static Sprite Add(Sprite.Name name, Image.Name imageName, float x, float y, float width, float height, Azul.Color color)
        {
            SpriteManager pInstance = SpriteManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Sprite pSprite = (Sprite)pInstance.BaseAdd();
            Debug.Assert(pSprite != null);

            Image pImage = ImageManager.Find(imageName);
            Debug.Assert(pImage != null);

            // Initialize the date
            pSprite.Set(name, pImage, x, y, width, height, color);
            return pSprite;
        }

        public static Sprite Find(Sprite.Name name)
        {
            SpriteManager pInstance = SpriteManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            // Use compare node to compare to search nodes
            pInstance.pSpriteCompare.SetName(name);

            Sprite pData = (Sprite)pInstance.BaseFind(pInstance.pSpriteCompare);
            return pData;
        }

        public static void Remove(Sprite pNode)
        {
            SpriteManager pInstance = SpriteManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.Assert(pNode != null);
            pInstance.BaseRemove(pNode);
        }

        public static void Print()
        {
            SpriteManager pInstance = SpriteManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.WriteLine("******** SPRITE MANAGER *****************");
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

            Sprite pDataA = (Sprite)pLinkA;
            Sprite pDataB = (Sprite)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }

            return status;
        }

        protected override DLink DerivedCreateNode()
        {
            DLink pNode = new Sprite();
            Debug.Assert(pNode != null);

            return pNode;
        }

        protected override void DerivedDestroyNode(DLink pLink)
        {
            Sprite pSprite = (Sprite)pLink;
            pSprite.SetAzulSprite(null);
            pSprite.SetImage(null);
            pSprite.SetScreenRect(null);
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static SpriteManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);
            return pInstance;
        }
        
    }
}
