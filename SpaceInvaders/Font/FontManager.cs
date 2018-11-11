using System;
using System.Xml;
using System.Diagnostics;

namespace SpaceInvaders
{
    class FontManager : Manager
    {
        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        private FontManager(int reserveNum = 3, int reserveGrow = 1)
            : base(reserveNum, reserveGrow)
        {
            this.pRefNode = new Font();
        }

        ~FontManager()
        {
            this.pRefNode = null;
            FontManager.pInstance = null;
        }

        //----------------------------------------------------------------------
        // Static Manager methods can be implemented with base methods 
        // Can implement/specialize more or less methods your choice
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
                pInstance = new FontManager(reserveNum, reserveGrow);
            }
        }
        public static void Destroy()
        {
            // Get the instance
            FontManager pInstance = FontManager.PrivGetInstance();
            pInstance.BaseDestroy();
        }
        public static Font Add(Font.Name name, SpriteBatch pSpriteBatch, String pMessage, Glyph.Name glyphName, float xStart, float yStart)
        {
            FontManager pInstance = FontManager.PrivGetInstance();

            Font pNode = (Font)pInstance.BaseAdd();
            Debug.Assert(pNode != null);

            pNode.Set(name, pMessage, glyphName, xStart, yStart);

            // Add to sprite batch
            Debug.Assert(pSpriteBatch != null);
            Debug.Assert(pNode.pFontSprite != null);
            pSpriteBatch.Attach(pNode.pFontSprite);

            return pNode;
        }

        public static void AddXml(Glyph.Name glyphName, String assetName,Texture.Name textName)
        {
            GlyphManager.AddXml(glyphName, assetName, textName);
        }

        public static void Remove(Glyph pNode)
        {
            Debug.Assert(pNode != null);
            FontManager pInstance = FontManager.PrivGetInstance();
            pInstance.BaseRemove(pNode);
        }
        public static Font Find(Font.Name name)
        {
            FontManager pInstance = FontManager.PrivGetInstance();

            // Compare functions only compares two Nodes
            pInstance.pRefNode.name = name;

            Font pData = (Font)pInstance.BaseFind(pInstance.pRefNode);
            return pData;
        }


        public static void Print()
        {
            FontManager pInstance = FontManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.WriteLine("------ Font Manager ------");
            pInstance.PrintStats();
            pInstance.PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            Font pDataA = (Font)pLinkA;
            Font pDataB = (Font)pLinkB;

            Boolean status = false;

            if (pDataA.name == pDataB.name )
            {
                status = true;
            }

            return status;
        }
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new Font();
            Debug.Assert(pNode != null);
            return pNode;
        }

        override protected void DerivedDestroyNode(DLink pLink)
        {
            // TODO
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static FontManager PrivGetInstance()
        {
            // Safety - this forces users to call Create() first before using class
            Debug.Assert(pInstance != null);

            return pInstance;
        }

        //----------------------------------------------------------------------
        // Data
        //----------------------------------------------------------------------
        private static FontManager pInstance = null;
        private Font pRefNode;
    }
}
