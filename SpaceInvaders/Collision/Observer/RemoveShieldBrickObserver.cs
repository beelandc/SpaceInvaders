using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class RemoveShieldBrickObserver : CollisionObserver
    {
        private GameObject pShieldBrick;
        private GameObject pColComposite;
        private GameObject pShieldComposite;

        public RemoveShieldBrickObserver()
        {
            this.pShieldBrick = null;
            this.pColComposite = null;
            this.pShieldComposite = null;
        }

        public RemoveShieldBrickObserver(RemoveShieldBrickObserver sb)
        {
            this.pShieldBrick = sb.pShieldBrick;
            this.pColComposite = sb.pColComposite;
            this.pShieldComposite = sb.pShieldComposite;
        }

        // TODO Clean this up
        public override void Notify()
        {
            //Debug.WriteLine("RemoveShieldBrickObserver: {0} {1}", this.pSubject.pObjA, this.pSubject.pObjB);

            // This cast will throw an exception if wrong
            this.pShieldBrick = (ShieldBrick)this.pSubject.pObjA;
            this.pColComposite = null;
            this.pShieldComposite = null;

            // Remove Shield from composite
            Composite pShieldGrid = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.ShieldGroup);
            ForwardIterator pFwdIter = new ForwardIterator(pShieldGrid);

            Component pNode = pFwdIter.First();
            while (!pFwdIter.IsDone())
            {
                if (pNode.containerType == Component.Container.LEAF)
                {
                    // If Component is target shield brick
                    if (pNode.GetHashCode() == this.pShieldBrick.GetHashCode())
                    {
                        // Remove shield brick from column
                        pNode.pParent.Remove(pNode);

                        // Get references to parent composites
                        Component pShieldColComposite = pNode.pParent;
                        Component pShieldComposite = null;

                        if (pShieldColComposite != null)
                        {
                            pShieldComposite = pShieldColComposite.pParent;
                        }

                        // If column is now empty, delete column
                        if (pShieldColComposite.GetFirstChild() == null)
                        {
                            // Delete empty column from parent composite
                            if (pShieldColComposite.pParent != null)
                            {
                                pShieldColComposite.pParent.Remove(pShieldColComposite);
                            }

                            // Mark empty column for death
                            if (!((GameObject)pShieldColComposite).IsMarkedForDeath())
                            {
                                this.pColComposite = (GameObject)pShieldColComposite;
                                this.pColComposite.MarkForDeath();
                            }

                        }

                        // If Shield is now empty, delete column
                        if (pShieldComposite.GetFirstChild() == null)
                        {
                            // Delete empty shield from parent composite
                            if (pShieldComposite.pParent != null)
                            {
                                pShieldComposite.pParent.Remove(pShieldComposite);
                            }

                            // Mark empty shield for death
                            if (!((GameObject)pShieldComposite).IsMarkedForDeath())
                            {
                                this.pShieldComposite = (GameObject)pShieldComposite;
                                this.pShieldComposite.MarkForDeath();
                            }

                        }

                        break;
                    }
                }

                pNode = pFwdIter.Next();
            }

            if (!pShieldBrick.IsMarkedForDeath())
            {
                pShieldBrick.MarkForDeath();

                // Delay - remove object later
                RemoveShieldBrickObserver pObserver = new RemoveShieldBrickObserver(this);
                GameStateManager.GetGame().GetStateDelayedObjectManager().Attach(pObserver);
            }


        }

        public override void ExecuteDelayed()
        {
            // Let the gameObjects deal with this...

            if (this.pShieldBrick != null)
            {
                this.pShieldBrick.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
            }

            if (this.pColComposite != null)
            {
                this.pColComposite.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
            }

            if (this.pShieldComposite != null)
            {
                this.pShieldComposite.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
            }

            // Remove root if all deleted
            Composite pShieldGroup = GameStateManager.GetGame().GetStateCompositeManager().Find(Composite.CompositeName.ShieldGroup);
            if (pShieldGroup.GetFirstChild() == null && !pShieldGroup.IsMarkedForDeath())
            {
                pShieldGroup.MarkForDeath();
                pShieldGroup.Remove(SpriteBatch.Name.Shield, SpriteBatch.Name.CollisionBox);
            }
        }
    }
}
