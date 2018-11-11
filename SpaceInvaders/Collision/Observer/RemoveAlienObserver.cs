using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class RemoveAlienObserver : CollisionObserver
    {
        private GameObject pAlien;
        private GameObject pComposite;

        public RemoveAlienObserver()
        {
            this.pAlien = null;
            this.pComposite = null;
        }

        public RemoveAlienObserver(RemoveAlienObserver a)
        {
            this.pAlien = a.pAlien;
            this.pComposite = a.pComposite;
        }

        public override void Notify()
        {
            // Delete missile
            //Debug.WriteLine("RemoveAlienObserver: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            // This cast will throw an exception if wrong
            this.pAlien = (Alien)this.pSubject.pObjA;
            this.pComposite = null;

            // Remove Alien from composite
            Composite pAlienGrid = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.AlienGrid);
            ForwardIterator pFwdIter = new ForwardIterator(pAlienGrid);

            Component pNode = pFwdIter.First();
            while (!pFwdIter.IsDone())
            {
                if (pNode.containerType == Component.Container.LEAF)
                {
                    // If Component is target alien
                    if (pNode.GetHashCode() == this.pAlien.GetHashCode())
                    {
                        // Remove Alien from column
                        pNode.pParent.Remove(pNode);

                        Component parentComposite = pNode.pParent;
                        // If column is now empty, delete column
                        if (parentComposite.GetFirstChild() == null)
                        {
                            // Delete empty column from parent composite
                            if (parentComposite.pParent != null)
                            {
                                parentComposite.pParent.Remove(parentComposite);
                            }

                            if (!((GameObject) parentComposite).IsMarkedForDeath())
                            {
                                // Delay - remove object later
                                this.pComposite = (GameObject)parentComposite;
                                this.pComposite.MarkForDeath();
                            }

                        }

                        break;
                    }
                }

                pNode = pFwdIter.Next();
            }

            if (!pAlien.IsMarkedForDeath())
            {
                pAlien.MarkForDeath();

                // Delay - remove object later
                RemoveAlienObserver pObserver = new RemoveAlienObserver(this);
                GameStateManager.GetGame().GetStateDelayedObjectManager().Attach(pObserver);
            }

            // Faster grid speed (shorter delta)
            float newSpeed = GameStateManager.GetGame().GetStateAlienGridSpeed() - 0.013f;

            if(newSpeed > 0.0f)
            {
                // Increase Alien Grid Speed
                GameStateManager.GetGame().SetStateAlienGridSpeed(newSpeed);
            }
            

        }

        public override void ExecuteDelayed()
        {
            // Let the gameObjects deal with this...
            
            if(this.pAlien != null)
            {
                this.pAlien.Remove(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox);
            }

            if(this.pComposite != null)
            {
                this.pComposite.Remove(SpriteBatch.Name.Alien, SpriteBatch.Name.CollisionBox);
            }

            // Reset level if all deleted
            Composite pAlienGrid = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.AlienGrid);
            if(pAlienGrid.GetFirstChild() == null)
            {
                GameStateManager.GetGame().SetResetLevelFlag(true);
            }
        }

    }
}
