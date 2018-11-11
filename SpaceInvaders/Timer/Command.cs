using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public abstract class Command : DLink
    {
        abstract public void Execute(float deltaTime);
    }
}
