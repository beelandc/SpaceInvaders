using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class AddPlayerPointsObserver : CollisionObserver
    {
        private Player.Name playerName;
        private Font.Name pointsFont;
        private Alien pAlien;

        public AddPlayerPointsObserver(Player.Name inPlayerName)
        {
            this.playerName = inPlayerName;

            switch (inPlayerName)
            {
                case Player.Name.Player1:
                    pointsFont = Font.Name.Player1Score;
                    break;
                case Player.Name.Player2:
                    pointsFont = Font.Name.Player2Score;
                    break;
            }
        } 

        public override void Notify()
        {
            this.pAlien = (Alien)this.pSubject.pObjA;

            Player pPlayer = GameStateManager.GetGame().GetPlayer(playerName);

            // Add points
            pPlayer.AddPoints(pAlien.GetPoints());

            // Update Points display
            Font pPlayer1ScoreFont = FontManager.Find(pointsFont);
            Debug.Assert(pPlayer1ScoreFont != null);
            pPlayer1ScoreFont.UpdateMessage(pPlayer.GetPoints().ToString("D4"));
        }
    }
}
