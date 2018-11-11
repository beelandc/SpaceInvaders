using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class SpriteBatchManager : Manager
    {
        private SpriteBatch poNodeCompare;

        //----------------------------------------------------------------------
        // Constructor
        //----------------------------------------------------------------------
        public SpriteBatchManager(int reserveNum = 3, int reserveGrow = 1)
        : base(reserveNum, reserveGrow)
        {
            // make sure values are ressonable 
            Debug.Assert(reserveNum > 0);
            Debug.Assert(reserveGrow > 0);

            this.poNodeCompare = new SpriteBatch();
        }

        ~SpriteBatchManager()
        {
            this.poNodeCompare = null;
        }

        public SpriteBatch Add(SpriteBatch.Name name, int reserveNum = 3, int reserveGrow = 1)
        {
            SpriteBatch pNode = (SpriteBatch)BaseAdd();
            Debug.Assert(pNode != null);

            pNode.Set(name, reserveNum, reserveGrow);
            return pNode;
        }

        public void Draw()
        {
            // walk through the list and render
            SpriteBatch pSpriteBatch = (SpriteBatch)BaseGetActive();

            while (pSpriteBatch != null)
            {

                if (pSpriteBatch.GetDraw())
                {
                    SpriteBaseRefManager pSpriteBaseRefManager = pSpriteBatch.GetSpriteBaseRefManager();
                    Debug.Assert(pSpriteBaseRefManager != null);

                    // Assumes that update() has been called on each sprite
                    pSpriteBaseRefManager.Draw();
                }

                pSpriteBatch = (SpriteBatch)pSpriteBatch.GetNext();
            }

        }

        public SpriteBatch Find(SpriteBatch.Name name)
        {
            // Use compare node to compare to search nodes
            this.poNodeCompare.SetName(name);

            SpriteBatch pData = (SpriteBatch)BaseFind(poNodeCompare);
            return pData;
        }

        public void Remove(SpriteBatch pNode)
        {
            Debug.Assert(pNode != null);
            BaseRemove(pNode);
        }

        public void Print()
        {
            Debug.WriteLine("******** SPRITE BATCH MANAGER ***********");
            PrintStats();
            PrintNodes();
        }

        //----------------------------------------------------------------------
        // Override Abstract methods
        //----------------------------------------------------------------------
        override protected DLink DerivedCreateNode()
        {
            DLink pNode = new SpriteBatch();
            Debug.Assert(pNode != null);

            return pNode;
        }
        override protected Boolean DerivedCompare(DLink pLinkA, DLink pLinkB)
        {
            // This is used in baseFind() 
            Debug.Assert(pLinkA != null);
            Debug.Assert(pLinkB != null);

            SpriteBatch pDataA = (SpriteBatch)pLinkA;
            SpriteBatch pDataB = (SpriteBatch)pLinkB;

            Boolean status = false;

            if (pDataA.GetName() == pDataB.GetName())
            {
                status = true;
            }
            
            return status;
        }
        override protected void DerivedDestroyNode(DLink pLink)
        {
            SpriteBatch pNode = (SpriteBatch)pLink;
            Debug.Assert(pNode != null);
            pNode.Destroy();
        }

    }
}
