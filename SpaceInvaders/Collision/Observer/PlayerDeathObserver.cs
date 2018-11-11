using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class PlayerDeathObserver : CollisionObserver
    {
        private Player.Name playerName;
        private Font.Name livesFont;

        public PlayerDeathObserver(Player.Name inPlayerName)
        {
            this.playerName = inPlayerName;

            switch (inPlayerName)
            {
                case Player.Name.Player1:
                    livesFont = Font.Name.Player1Lives;
                    break;
                case Player.Name.Player2:
                    livesFont = Font.Name.Player2Lives;
                    break;
            }
        }

        public PlayerDeathObserver(PlayerDeathObserver pdo)
        {
            this.playerName = pdo.playerName;
            this.livesFont = pdo.livesFont;
        }

        public override void Notify()
        {
            Debug.WriteLine("PlayerDeathObserver: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            Player pPlayer = GameStateManager.GetGame().GetPlayer(playerName);

            // Decrement life count
            pPlayer.LoseLife();

            // Update Life count display
            Font pPlayerLives = FontManager.Find(livesFont);
            Debug.Assert(pPlayerLives != null);
            pPlayerLives.UpdateMessage("LIVES " + pPlayer.GetNumLives().ToString());

            // Remove CoreCannon
            GameObject pCoreCannon = GameStateManager.GetGame().GetStateGameObjectManager().Find(GameObject.Name.CoreCannon);
            if (!pCoreCannon.IsMarkedForDeath())
            {
                pCoreCannon.MarkForDeath();

                // Delay - remove object later
                PlayerDeathObserver pObserver = new PlayerDeathObserver(this);
                GameStateManager.GetGame().GetStateDelayedObjectManager().Attach(pObserver);
            }

            // Set State Change Flag
            GameStateManager.GetGame().SetStateChangeFlag(true);
        }

        public override void ExecuteDelayed()
        {
            // Let the gameObjects deal with this...

            GameObject pCoreCannon = GameStateManager.GetGame().GetStateGameObjectManager().Find(GameObject.Name.CoreCannon);

            // Remove from composite
            Composite pCoreCannonGroup = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.CoreCannonGroup);
            pCoreCannonGroup.Remove(pCoreCannon);

            if (pCoreCannon != null)
            {
                pCoreCannon.Remove(SpriteBatch.Name.CoreCannon, SpriteBatch.Name.CollisionBox);
            }

            // Add Timer Event to restore CoreCannon
            TimerManager.Add(TimeEvent.Name.ResetCoreCannon, new ResetCoreCannonCommand(), 1.0f);
        }

    }
}
