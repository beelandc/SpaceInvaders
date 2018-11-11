using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class InputManager
    {
        private static InputManager pInstance = null;
        private bool privSpaceKeyPrev;
        private bool privNKeyPrev;
        private bool privKey1Prev;
        private bool privKey2Prev;

        private InputSubject pSubjectArrowRight;
        private InputSubject pSubjectArrowLeft;
        private InputSubject pSubjectSpace;
        private InputSubject pSubjectKeyN;
        private InputSubject pSubjectKey1;
        private InputSubject pSubjectKey2;

        private InputManager()
        {
            this.pSubjectArrowLeft = new InputSubject();
            this.pSubjectArrowRight = new InputSubject();
            this.pSubjectSpace = new InputSubject();
            this.pSubjectKeyN = new InputSubject();
            this.pSubjectKey1 = new InputSubject();
            this.pSubjectKey2 = new InputSubject();

            this.privSpaceKeyPrev = false;
            this.privNKeyPrev = false;
            this.privKey1Prev = false;
            this.privKey2Prev = false;
        }

        private static InputManager privGetInstance()
        {
            if (pInstance == null)
            {
                pInstance = new InputManager();
            }
            Debug.Assert(pInstance != null);

            return pInstance;
        }

        public static InputSubject GetArrowRightSubject()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            return pInstance.pSubjectArrowRight;
        }

        public static InputSubject GetArrowLeftSubject()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            return pInstance.pSubjectArrowLeft;
        }

        public static InputSubject GetSpaceSubject()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            return pInstance.pSubjectSpace;
        }

        public static InputSubject GetKeyNSubject()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            return pInstance.pSubjectKeyN;
        }

        public static InputSubject GetKey1Subject()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            return pInstance.pSubjectKey1;
        }

        public static InputSubject GetKey2Subject()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            return pInstance.pSubjectKey2;
        }

        public static void Update()
        {
            InputManager pInstance = InputManager.privGetInstance();
            Debug.Assert(pInstance != null);

            // SpaceKey: (with key history) -----------------------------------------------------------
            bool spaceKeyCurr = Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_SPACE);
            if (spaceKeyCurr == true && pInstance.privSpaceKeyPrev == false)
            {
                pInstance.pSubjectSpace.Notify();
            }
            pInstance.privSpaceKeyPrev = spaceKeyCurr;

            // LeftKey: (no history) -----------------------------------------------------------
            if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ARROW_LEFT) == true)
            {
                pInstance.pSubjectArrowLeft.Notify();
            }

            // RightKey: (no history) -----------------------------------------------------------
            if (Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ARROW_RIGHT) == true)
            {
                pInstance.pSubjectArrowRight.Notify();
            }

            // 1 Key (with key history)
            bool Key1Curr = Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_1);
            if (Key1Curr == true && pInstance.privKey1Prev == false)
            {
                pInstance.pSubjectKey1.Notify();
            }
            pInstance.privKey1Prev = Key1Curr;

            // 2 Key (with key history)
            bool Key2Curr = Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_2);
            if (Key2Curr == true && pInstance.privKey2Prev == false)
            {
                pInstance.pSubjectKey2.Notify();
            }
            pInstance.privKey2Prev = Key2Curr;

            // Enter Key (with key history)
            bool nKeyCurr = Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_N);
            if (nKeyCurr == true && pInstance.privNKeyPrev == false)
            {
                pInstance.pSubjectKeyN.Notify();
            }
            pInstance.privNKeyPrev = nKeyCurr;

            //Debug.Print("EnterKey: {0}", Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_ENTER));
            //Debug.Print("KP_EnterKey: {0}", Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_KP_ENTER));
            //Debug.Print("N_Key: {0}", Azul.Input.GetKeyState(Azul.AZUL_KEY.KEY_N));

        }

    }
}
