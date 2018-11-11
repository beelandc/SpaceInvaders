using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class CompositeRef : DLink
    {
        private Composite pComposite;

        //----------------------------------------------------------------------
        // Constructors / Destructor
        //----------------------------------------------------------------------
        public CompositeRef()
            : base()
        {
            this.pComposite = null;
        }

        ~CompositeRef()
        {
            this.pComposite = null;
        }

        //----------------------------------------------------------------------
        // Methods
        //----------------------------------------------------------------------
        public void Set(Composite pNewComposite)
        {
            Debug.Assert(pNewComposite != null);
            this.pComposite = pNewComposite;
        }

        public override void Clean()
        {
            this.pComposite = null;
        }

        public Enum GetName()
        {
            return this.pComposite.GetName();
        }

        public Composite GetComposite()
        {
            return this.pComposite;
        }

        public void SetComposite(Composite newComposite)
        {
            this.pComposite = newComposite;
        }

    }
}
