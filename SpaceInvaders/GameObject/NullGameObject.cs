using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class NullGameObject : Leaf
    {
        public NullGameObject()
            : base(GameObject.Name.Null_Object, Sprite.Name.NullSprite)
        {

        }

        ~NullGameObject()
        {

        }

        public override void Accept(CollisionVisitor other)
        {
            throw new NotImplementedException();
        }

        public override void Update()
        {
            // Do nothing
        }

    }
}
