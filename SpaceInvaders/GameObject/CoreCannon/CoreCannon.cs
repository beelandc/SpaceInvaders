using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CoreCannon : Leaf
    {
        public float coreCannonSpeed;
        private CoreCannonMissileState pMissileState;
        private CoreCannonMoveState pMoveState;

        public CoreCannon(GameObject.Name name, Sprite.Name spriteName, float posX, float posY)
            : base(name, spriteName)
        {
            this.x = posX;
            this.y = posY;

            this.coreCannonSpeed = 3.0f;
            this.pMissileState = null;
            this.pMoveState = null;
        }

        ~CoreCannon()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            other.VisitCoreCannon(this);
        }

        public override void Update()
        {
            base.Update();
        }

        public void MoveRight()
        {
            if(pMoveState.GetType() == typeof(CoreCannonRightOnlyState))
            {
                this.SetMoveState(CoreCannonManager.MoveState.Free);
            }
            this.pMoveState.MoveRight(this);
        }

        public void MoveLeft()
        {
            if (pMoveState.GetType() == typeof(CoreCannonLeftOnlyState))
            {
                this.SetMoveState(CoreCannonManager.MoveState.Free);
            }
            this.pMoveState.MoveLeft(this);
        }

        public void ShootMissile()
        {
            this.pMissileState.ShootMissile(this);
        }

        public void SetMissileState(CoreCannonManager.MissileState inState)
        {
            this.pMissileState = GameStateManager.GetGame().GetStateCoreCannonManager().GetMissileState(inState);
        }

        public void SetMoveState(CoreCannonManager.MoveState inState)
        {
            this.pMoveState = GameStateManager.GetGame().GetStateCoreCannonManager().GetMoveState(inState);
        }

        public float GetShipSpeed()
        {
            return this.coreCannonSpeed;
        }

        public override void VisitBombRoot(BombRoot br)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // BombRoot vs CoreCannon
            // go down a level in BombRoot composite
            GameObject pGameObj = Iterator.GetChildGameObject(br);
            CollisionPair.Collide(pGameObj, this);
        }

        public override void VisitBomb(Bomb b)
        {
            // Debug.WriteLine("collide: {0} with {1}", a, this);

            // Bomb vs CoreCannon
            CollisionPair pColPair = CollisionPairManager.GetActiveColPair();
            Debug.Assert(pColPair != null);
            pColPair.SetCollision(b, this);
            pColPair.NotifyListeners();
        }

    }
}
