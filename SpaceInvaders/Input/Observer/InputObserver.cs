using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    abstract class InputObserver : DLink
    {

        abstract public void Notify();

        public InputSubject pSubject;
    }
}
