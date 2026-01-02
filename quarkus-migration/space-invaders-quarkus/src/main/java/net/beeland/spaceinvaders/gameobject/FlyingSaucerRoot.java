package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;

/**
 * FlyingSaucerRoot - Composite container for managing flying saucer
 * Part of the Composite pattern hierarchy
 * Typically contains a single FlyingSaucer instance
 */
public class FlyingSaucerRoot extends Composite {
    
    /**
     * Constructor with composite name and position
     * 
     * @param compositeName The composite name for this flying saucer root
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public FlyingSaucerRoot(CompositeName compositeName, float posX, float posY) {
        super(GameObject.GameObjectName.FLYING_SAUCER_ROOT);
        this.compositeName = compositeName;
        this.x = posX;
        this.y = posY;
    }
    
    /**
     * Constructor with just composite name
     * 
     * @param compositeName The composite name for this flying saucer root
     */
    public FlyingSaucerRoot(CompositeName compositeName) {
        super(GameObject.GameObjectName.FLYING_SAUCER_ROOT);
        this.compositeName = compositeName;
        this.x = 0.0f;
        this.y = 0.0f;
    }
    
    @Override
    public void accept(GameObject.CollisionVisitor other) {
        // Call the appropriate collision reaction
        other.visitFlyingSaucerRoot(this);
    }
    
    @Override
    public void update() {
        super.update();
        // Update is handled by Composite base class which updates all children
    }
    
    @Override
    public void dump() {
        System.out.println("FlyingSaucerRoot:");
        System.out.println("  Name: " + this.name);
        System.out.println("  Composite Name: " + this.compositeName);
        System.out.println("  Position: (" + this.x + ", " + this.y + ")");
        System.out.println("  Children: " + this.numChildren);
        // Use print() method from Composite to show children
        print();
    }
}