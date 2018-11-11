using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    public class TimeEvent : DLink
    {
        private Name name;
        private Command pCommand;
        private float triggerTime;
        private float deltaTime;

        public TimeEvent()
            : base()
        {
            this.name = TimeEvent.Name.Uninitialized;
            this.pCommand = null;
            this.triggerTime = 0.0f;
            this.deltaTime = 0.0f;
        }

        ~TimeEvent()
        {
            this.name = Name.Uninitialized;
            this.pCommand = null;
        }

        public void Set(TimeEvent.Name eventName, Command pCommand, float deltaTimeToTrigger)
        {
            Debug.Assert(pCommand != null);

            this.name = eventName;
            this.pCommand = pCommand;
            this.deltaTime = deltaTimeToTrigger;

            // set the trigger time
            this.triggerTime = TimerManager.GetCurrTime() + deltaTimeToTrigger;
        }

        public void Process()
        {
            Debug.Assert(this.pCommand != null);
            this.pCommand.Execute(deltaTime);
        }

        public override void Clean()
        {
            base.Clean();
            this.name = TimeEvent.Name.Uninitialized;
            this.pCommand = null;
            this.triggerTime = 0.0f;
            this.deltaTime = 0.0f;
        }

        public override void Print()
        {
            Debug.WriteLine("   Name: {0} ({1})", this.name, this.GetHashCode());
            
            Debug.WriteLine("      Command: {0}", this.pCommand);
            Debug.WriteLine("   Event Name: {0}", this.name);
            Debug.WriteLine(" Trigger Time: {0}", this.triggerTime);
            Debug.WriteLine("   Delta Time: {0}", this.deltaTime);

            // TODO Consolidate like below
            //string next = (this.pNext != null) ? this.pNext : second_expression;

            /*
            if (this.pNext == null)
            {
                Debug.WriteLine("      next: null");
            }
            else
            {
                Image pTmp = (Image)this.pNext;
                Debug.WriteLine("      next: {0} ({1})", pTmp.name, pTmp.GetHashCode());
            }

            if (this.pPrev == null)
            {
                Debug.WriteLine("      prev: null");
            }
            else
            {
                Image pTmp = (Image)this.pPrev;
                Debug.WriteLine("      prev: {0} ({1})", pTmp.name, pTmp.GetHashCode());
            }
            */

            base.Print();
        }

        public void SetName(Name newName)
        {
            this.name = newName;
        }

        public Name GetName()
        {
            return name;
        }

        public Command GetCommand()
        {
            return this.pCommand;
        }

        public float GetDeltaTime()
        {
            return this.deltaTime;
        }

        public void SetTriggerTime(float triggerTime)
        {
            this.triggerTime = triggerTime;
        }

        public float GetTriggerTime()
        {
            return triggerTime;
        }
        
        public void SetCommand(Command newCommand)
        {
            this.pCommand = newCommand;
        }

        public enum Name
        {
            SpriteAnimation,
            SpriteMovement,
            MissileMovement,
            AlienGridMovementSound,
            DropBomb,

            DebugCommand,
            Uninitialized,
            DeployFlyingSaucer,
            ResetLevel,
            AlienGridMovement,
            ResetCoreCannon,
        }
    }
}
