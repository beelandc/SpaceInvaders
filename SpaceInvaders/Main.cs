using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class Program
    {
        static void Main(string[] args)
        {
            GameStateManager.Create();

            // Create the instance
            SpaceInvaders game = new SpaceInvaders();
            Debug.Assert(game != null);

            GameStateManager.SetGame(game);

            // Start the game
            game.Run();
        }
    }
}
