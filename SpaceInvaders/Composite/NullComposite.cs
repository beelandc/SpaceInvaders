using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class NullComposite : Composite
    {
        public NullComposite()
            : base(GameObject.Name.Null_Object, Sprite.Name.NullSprite)
        {
            this.pHead = null;
            this.containerType = Container.COMPOSITE;
        }
        
        public override void Accept(CollisionVisitor other)
        {
            throw new NotImplementedException();
        }
    }
}
