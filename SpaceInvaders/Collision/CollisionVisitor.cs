using System;
using System.Diagnostics;


namespace SpaceInvaders
{

    abstract public class CollisionVisitor : Component
    {

        // TODO Define and implement Visit Functions

        public virtual void VisitAlienGrid(AlienGrid ag)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by AlienGrid not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitAlienGridColumn(AlienGridColumn agc)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by AlienGridColumn not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitAlien(Alien a)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by Alien not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitMissile(Missile m)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by Missile not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitMissileGroup(MissileGroup m)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by MissileGroup not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitNullGameObject(NullGameObject n)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by NullGameObject not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitWallGroup(WallGroup wg)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by WallGroup not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitRightWall(RightWall rw)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by RightWall not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitLeftWall(LeftWall lw)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by LeftWall not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitBottomWall(BottomWall bw)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by BottomWall not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitTopWall(TopWall tw)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by LeftWall not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitLeftBumper(LeftBumper lb)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by LeftBumper not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitRightBumper(RightBumper rb)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by RightBumper not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitCoreCannonGroup(CoreCannonGroup ccg)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by CoreCannonGroup not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitCoreCannon(CoreCannon cc)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by CoreCannon not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitShieldBrick(ShieldBrick sb)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by ShieldBrick not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitShieldColumn(ShieldColumn sc)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by ShieldColumn not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitShieldRoot(ShieldRoot sr)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by ShieldRoot not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitShieldGroup(ShieldGroup sg)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by ShieldGroup not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitBomb(Bomb b)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by Bomb not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitBombRoot(BombRoot br)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by BombRoot not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitFlyingSaucerRoot(FlyingSaucerRoot fsr)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by FlyingSaucerRoot not implemented");
            Debug.Assert(false);
        }

        public virtual void VisitFlyingSaucer(FlyingSaucer fs)
        {
            // no differed to subcass
            Debug.WriteLine("Visit by FlyingSaucer not implemented");
            Debug.Assert(false);
        }


        abstract public void Accept(CollisionVisitor other);
    }

}
