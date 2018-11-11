using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class QueuedTimeEvent : DLink
    {
        private TimeEvent.Name timeEventName;
        private Command pCommand;
        private float commandTimeDelta;

        public QueuedTimeEvent()
        {
            timeEventName = TimeEvent.Name.Uninitialized;
            pCommand = null;
            commandTimeDelta = -1.0f;
        }

        public QueuedTimeEvent(TimeEvent.Name inEventName, Command inCommand, float inTimeDelta)
        {
            Debug.Assert(inCommand != null);
            Debug.Assert(inTimeDelta > 0);

            this.timeEventName = inEventName;
            this.pCommand = inCommand;
            this.commandTimeDelta = inTimeDelta;
        }

        public void Set(TimeEvent.Name inEventName, Command inCommand, float inTimeDelta)
        {
            Debug.Assert(inCommand != null);
            Debug.Assert(inTimeDelta > 0);

            this.timeEventName = inEventName;
            this.pCommand = inCommand;
            this.commandTimeDelta = inTimeDelta;
        }

        public TimeEvent.Name GetTimeEventName()
        {
            return this.timeEventName;
        }

        public Command GetCommand()
        {
            return this.pCommand;
        }

        public float GetTimeEventDelta()
        {
            return this.commandTimeDelta;
        }

    }
}
