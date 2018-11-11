using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class InputTest
    {
        public static void KeyboardTest()
        {
	        // Quick and dirty test, if these work the rest do.
	        // ---> try a,s,d,<SPACE> keys
	        String a = "";
            String b = "";
            String c = "";
            String d = "";

	        if (Azul.Input.GetKeyState( Azul.AZUL_KEY.KEY_ARROW_RIGHT))
	        {
		        a = " RIGHT_ARROW";
	        }

	        if (Azul.Input.GetKeyState( Azul.AZUL_KEY.KEY_ARROW_LEFT))
	        {
		        b = " LEFT_ARROW";
	        }

	        if (Azul.Input.GetKeyState( Azul.AZUL_KEY.KEY_Q))
	        {
		        c = " Q";
	        }

	        if (Azul.Input.GetKeyState( Azul.AZUL_KEY.KEY_SPACE))
	        {
		      d = " <SPACE>";
	        }

	        //String total = a + b + c + d;
            Console.WriteLine("Key: {0}{1}{2}{3} ",a,b,c,d);
	
        }

        public static void MouseTest()
        {

            // Quick and dirty test, if these work the rest do.
            // --> try move the mouse inside the window, click right, click left
            String a = "";
            String b = "";

            float xPos = 0.0f;
            float yPos = 0.0f;

            // get mouse position
            Azul.Input.GetCursor( ref xPos, ref yPos);

            // read mouse buttons
            if (Azul.Input.GetKeyState( Azul.AZUL_MOUSE.BUTTON_RIGHT ))
            {
                a = " <right>";
            }

            if (Azul.Input.GetKeyState( Azul.AZUL_MOUSE.BUTTON_LEFT ))
            {
                b = " <left>";
            }

            Console.WriteLine("({0},{1}): {2} {3} ", xPos, yPos, a, b);
        }


    }
}
