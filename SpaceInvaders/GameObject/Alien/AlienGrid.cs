using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class AlienGrid : Composite
    {
        private static GameObject.Name pGameObject = GameObject.Name.AlienGrid;
        private bool switchXDirection = false;
        private bool directionChangePending = false;

        private float xDelta = 0.0f;
        private float yDelta = 0.0f;
        private float prevXDelta = -1.0f;

        public AlienGrid(Composite.CompositeName compositeName, float inXDelta, float inYDelta)
            : base(pGameObject)
        {
            this.compositeName = compositeName;
            this.xDelta = inXDelta;
            this.yDelta = inYDelta;
        }
        
        public override void Accept(CollisionVisitor other)
        {
            other.VisitAlienGrid(this);
        }

        public override void VisitMissileGroup(MissileGroup m)
        {
            // AlienGrid vs MissileGroup
            Debug.WriteLine("         collide:  {0} <-> {1}", m.GetName(), this.name);

            // MissileGroup vs Columns
            GameObject pGameObj = Iterator.GetChildGameObject(this);
            CollisionPair.Collide(m, pGameObj);
        }

        public override void Move(float dummyX, float dummyY)
        {
            
            DLink pNode = this.pHead;

            // #1 - Initial movement since wall collision - Move down
            if (switchXDirection && !directionChangePending)
            {
                this.SetXDelta(0.0f);
                this.SetYDelta(-25.0f);
                this.directionChangePending = true;
            }

            while (pNode != null)
            {
                // Get component
                Component pComponent = (Component)pNode;

                // Move component
                pComponent.Move(this.xDelta, this.yDelta);

                pNode = pNode.GetNext();
            }
            
            // #3 - Finally, Grid has moved off wall, reset direction change flag
            if (directionChangePending && !switchXDirection)
            {
                this.directionChangePending = false;
            }

            // #2 - Movement down complete, switch X direction 
            if (switchXDirection == true)
            {
                // Reset deltas to horizontal movement in opposite direction
                this.yDelta = 0.0f;
                this.xDelta = (this.prevXDelta * -1.0f);
                this.switchXDirection = false;
            }
            

        }

        public void SetSwitchXDirection(bool inSwitchDirectionBool)
        {
            this.switchXDirection = inSwitchDirectionBool;
        }

        public bool GetSwitchXDirection()
        {
            return this.switchXDirection;
        }

        public void SetXDelta(float inXDelta)
        {
            this.prevXDelta = this.xDelta;
            this.xDelta = inXDelta;
        }

        public void SetYDelta(float inYDelta)
        {
            this.yDelta = inYDelta;
        }

        public float GetPrevXDelta()
        {
            return this.prevXDelta;
        }

        public void SetPrevXDelta(float inPrevXDelta)
        {
            this.prevXDelta = inPrevXDelta;
        }

        public bool IsDirectionChangePending()
        {
            return this.directionChangePending;
        }
    }
}
