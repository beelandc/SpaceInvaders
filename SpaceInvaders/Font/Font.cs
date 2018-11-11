using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class Font: DLink
    {
        public Name name;
        public FontSprite pFontSprite;
        static private String pNullString = "null";

        public enum Name
        {
            TestMessage,
            TestOneOff,
            Player1Score,
            Player2Score,
            HighScore,
            GameCredits,
            Attract_Play,
            Attract_Title,
            Attract_ScoreAdvanceTable,
            Attract_FlyingSaucerPoints,
            Attract_SquidPoints,
            Attract_CrabPoints,
            Attract_JellyfishPoints,

            NullObject,
            Uninitialized,
            Player1ScoreTitle,
            Player2ScoreTitle,
            HighScoreTitle,
            End_Game,
            End_GameOver,
            End_Instructions,
            Attract_Instruction_1P,
            Attract_Instruction_2P,
            Player1Lives,
            Player2Lives
        };

        public Font()
            : base()
        {
            this.name = Name.Uninitialized;
            this.pFontSprite = new FontSprite();
            this.pFontSprite.Set(Font.Name.NullObject, pNullString, Glyph.Name.NullObject, 0.0f, 0.0f);
        }

        ~Font()
        {
            this.name = Name.Uninitialized;
            this.pFontSprite = null;
        }

        public void UpdateMessage(String pMessage)
        {
            Debug.Assert(pMessage != null);
            Debug.Assert(this.pFontSprite != null);
            this.pFontSprite.UpdateMessage(pMessage);
        }

        public void Set(Font.Name name, String pMessage, Glyph.Name glyphName, float xStart, float yStart)
        {
            Debug.Assert(pMessage != null);

            this.name = name;
            this.pFontSprite.Set(name, pMessage, glyphName, xStart, yStart);
        }

        public override void Clean()
        {
            base.Clean();

            this.name = Name.Uninitialized;
            if(pFontSprite != null)
            {
                this.pFontSprite.Set(Font.Name.NullObject, pNullString, Glyph.Name.NullObject, 0.0f, 0.0f);
            }
        }

        public override void Print()
        {
            base.Print();
        }

    }
}
