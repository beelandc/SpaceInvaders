using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CoreCannonManager
    {

        // Active
        private CoreCannon pCoreCannon;
        private Missile pMissile;

        // Missile States (Reference)
        private CoreCannonReadyState pReadyState;
        private CoreCannonMissileFlyingState pMissileFlyingState;
        private CoreCannonEndState pEndState;

        // Move States (Reference)
        private CoreCannonFreeState pFreeState;
        private CoreCannonLeftOnlyState pLeftOnlyState;
        private CoreCannonRightOnlyState pRightOnlyState;

        public enum MissileState
        {
            Ready,
            MissileFlying,
            End
        }

        public enum MoveState
        {
            Free,
            LeftOnly,
            RightOnly
        }

        public CoreCannonManager()
        {
            // Store the states
            this.pReadyState = new CoreCannonReadyState();
            this.pMissileFlyingState = new CoreCannonMissileFlyingState();
            this.pEndState = new CoreCannonEndState();

            this.pFreeState = new CoreCannonFreeState();
            this.pLeftOnlyState = new CoreCannonLeftOnlyState();
            this.pRightOnlyState = new CoreCannonRightOnlyState();

            // set active
            this.pCoreCannon = null;
            this.pMissile = null;

        }

        public CoreCannon GetShip()
        {
           Debug.Assert(this.pCoreCannon != null);

            return this.pCoreCannon;
        }

        public CoreCannonMissileState GetMissileState(MissileState state)
        {

            CoreCannonMissileState pShipMissileState = null;

            switch (state)
            {
                case CoreCannonManager.MissileState.Ready:
                    pShipMissileState = this.pReadyState;
                    break;

                case CoreCannonManager.MissileState.MissileFlying:
                    pShipMissileState = this.pMissileFlyingState;
                    break;

                case CoreCannonManager.MissileState.End:
                    pShipMissileState = this.pEndState;
                    break;
                default:
                    Debug.Assert(false);
                    break;
            }

            return pShipMissileState;
        }

        public CoreCannonMoveState GetMoveState(MoveState state)
        {
            CoreCannonMoveState pShipMoveState = null;

            switch (state)
            {
                case CoreCannonManager.MoveState.Free:
                    pShipMoveState = this.pFreeState;
                    break;

                case CoreCannonManager.MoveState.LeftOnly:
                    pShipMoveState = this.pLeftOnlyState;
                    break;

                case CoreCannonManager.MoveState.RightOnly:
                    pShipMoveState = this.pRightOnlyState;
                    break;
                default:
                    Debug.Assert(false);
                    break;
            }

            return pShipMoveState;
        }

        public Missile GetMissile()
        {
            Debug.Assert(this.pMissile != null);

            return this.pMissile;
        }

        public Missile ActivateMissile()
        {
            // copy over safe copy
            Missile pMissile = new Missile(GameObject.Name.Missile, Sprite.Name.Missile, 400, 100);
            this.pMissile = pMissile;

            // Attach to GameObjectManager - {update and collisions}
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pMissile);

            // Attached to SpriteBatches
            SpriteBatch pMissile_SpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.Missile);
            SpriteBatch pCollisionBox_SpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox);
            
            pMissile.ActivateSprite(pMissile_SpriteBatch);
            pMissile.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            // Attach the missile to the missile root
            GameObject pMissileGroup = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.MissileGroup);
            Debug.Assert(pMissileGroup != null);

            // Add to Composite
            pMissileGroup.Add(this.pMissile);

            return this.pMissile;
        }


        public CoreCannon ActivateCoreCannon()
        {
            // copy over safe copy
            CoreCannon pCoreCannon = new CoreCannon(GameObject.Name.CoreCannon, Sprite.Name.CoreCannon, 200, 100);
            this.pCoreCannon = pCoreCannon;

            // Attach to GameObjectManager - {update and collisions}
            GameStateManager.GetGame().GetStateGameObjectManager().Attach(pCoreCannon);

            // Attach the sprite to the correct sprite batch
            SpriteBatch pCoreCannon_SpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CoreCannon);
            SpriteBatch pCollisionBox_SpriteBatch = GameStateManager.GetGame().GetStateSpriteBatchManager().Find(SpriteBatch.Name.CollisionBox);

            pCoreCannon.ActivateSprite(pCoreCannon_SpriteBatch);
            pCoreCannon.ActivateCollisionSprite(pCollisionBox_SpriteBatch);

            // Attach the Core Cannon to the CoreCannonGroup
            GameObject pCoreCannonGroup = GameStateManager.GetGame().GetStateGameObjectManager().Find(GameObject.Name.CoreCannonGroup);
            Debug.Assert(pCoreCannonGroup != null);

            // Add to Composite
            pCoreCannonGroup.Add(this.pCoreCannon);

            this.pCoreCannon.SetMissileState(CoreCannonManager.MissileState.Ready);
            this.pCoreCannon.SetMoveState(CoreCannonManager.MoveState.Free);

            return this.pCoreCannon;
        }
        
    }
}
