using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    
    class ToggleSpriteBatchDrawObserver : InputObserver
    {
        SpriteBatch pSpriteBatch;

        public ToggleSpriteBatchDrawObserver(SpriteBatch inSpriteBatch)
        {
            Debug.Assert(inSpriteBatch != null);
            this.pSpriteBatch = inSpriteBatch;
        }

        public override void Notify()
        {
            Debug.Print("Toggle Sprite Batch Observer **********************************************");
            pSpriteBatch.SetDraw(!pSpriteBatch.GetDraw());
        }
    }
}
