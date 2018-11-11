using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Player
    {
        private Name name;
        private int numLives;
        private int points;

        public Player(Player.Name name)
        {
            this.name = name;
            this.numLives = 3;
            this.points = 0;
        }

        public void AddPoints(int newPoints)
        {
            this.points += newPoints;
        }

        public int GetPoints()
        {
            return this.points; 
        }

        public int GetNumLives()
        {
            return this.numLives;
        }

        public void AddLife()
        {
            this.numLives += 1;
        }

        public void LoseLife()
        {
            this.numLives -= 1;
        }

        public Name GetName()
        {
            return this.name;
        }

        public enum Name
        {
            Player1,
            Player2,
            Uninitialized
        }
        
    }
}
