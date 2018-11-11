using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class SoundEngineManager
    {
        private static SoundEngineManager pInstance;
        private IrrKlang.ISoundEngine pSoundEngine = new IrrKlang.ISoundEngine();

        private SoundEngineManager()
        {
            this.pSoundEngine.SoundVolume = 0.2f;
        }

        ~SoundEngineManager()
        {
            this.pSoundEngine = null;
            SoundEngineManager.pInstance = null;
        }

        //----------------------------------------------------------------------
        // Static Methods
        //----------------------------------------------------------------------
        public static void Create()
        {
            // Do the initialization
            if (pInstance == null)
            {
                pInstance = new SoundEngineManager();
            }
        }

        public static IrrKlang.ISoundEngine GetSoundEngine()
        {
            SoundEngineManager pInstance = SoundEngineManager.PrivGetInstance();
            Debug.Assert(pInstance != null);

            Debug.Assert(pInstance.pSoundEngine != null);
            return pInstance.pSoundEngine;
        }

        //----------------------------------------------------------------------
        // Private methods
        //----------------------------------------------------------------------
        private static SoundEngineManager PrivGetInstance()
        {
            Debug.Assert(pInstance != null);
            return pInstance;
        }

    }
}
