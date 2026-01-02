package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.collision.CollisionObject;
import net.beeland.spaceinvaders.collision.CollisionRect;
import net.beeland.spaceinvaders.composite.Component;
import net.beeland.spaceinvaders.sprite.SpriteProxy;

/**
 * GameObject is the abstract base class for all game entities.
 * It combines the Composite pattern (via Component) with collision detection
 * and sprite rendering capabilities.
 * 
 * Each GameObject has:
 * - A name for identification
 * - Position (x, y coordinates)
 * - A sprite proxy for rendering
 * - A collision object for collision detection
 * - A death flag for lifecycle management
 */
public abstract class GameObject extends Component {
    
    protected GameObjectName name;
    protected boolean markedForDeath;
    protected float x;
    protected float y;
    protected SpriteProxy spriteProxy;
    protected CollisionObject collisionObject;

    /**
     * Creates a game object with the specified name.
     * Sprite proxy must be set separately.
     *
     * @param gameName The name identifier for this game object
     */
    protected GameObject(GameObjectName gameName) {
        this.name = gameName;
        this.x = 0.0f;
        this.y = 0.0f;
        this.markedForDeath = false;
        this.spriteProxy = new SpriteProxy();
        this.collisionObject = new CollisionObject(this.spriteProxy);
    }

    /**
     * Updates the game object's state.
     * This includes updating sprite position and collision bounds.
     */
    public void update() {
        if (this.spriteProxy != null) {
            this.spriteProxy.setPosition(this.x, this.y);
        }

        if (this.collisionObject != null) {
            this.collisionObject.updatePos(this.x, this.y);
        }
    }

    /**
     * Gets the name of this game object.
     *
     * @return The game object name
     */
    public GameObjectName getName() {
        return this.name;
    }

    /**
     * Sets the name of this game object.
     *
     * @param name The new name
     */
    public void setName(GameObjectName name) {
        this.name = name;
    }

    /**
     * Gets the X coordinate.
     *
     * @return The X coordinate
     */
    public float getX() {
        return this.x;
    }

    /**
     * Sets the X coordinate.
     *
     * @param x The new X coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the Y coordinate.
     *
     * @return The Y coordinate
     */
    public float getY() {
        return this.y;
    }

    /**
     * Sets the Y coordinate.
     *
     * @param y The new Y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the position of this game object.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     */
    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the sprite proxy for this game object.
     *
     * @return The sprite proxy
     */
    public SpriteProxy getSpriteProxy() {
        return this.spriteProxy;
    }

    /**
     * Gets the collision object for this game object.
     *
     * @return The collision object
     */
    public CollisionObject getCollisionObject() {
        return this.collisionObject;
    }

    /**
     * Checks if this game object is marked for death.
     *
     * @return true if marked for death, false otherwise
     */
    public boolean isMarkedForDeath() {
        return this.markedForDeath;
    }

    /**
     * Marks this game object for death (to be removed in next update cycle).
     */
    public void markForDeath() {
        this.markedForDeath = true;
    }

    /**
     * Clears the death flag.
     */
    public void clearDeathFlag() {
        this.markedForDeath = false;
    }

    /**
     * Accepts a collision visitor for double-dispatch collision handling.
     * Subclasses must implement this to define collision behavior.
     *
     * @param other The collision visitor
     */
    public abstract void accept(CollisionVisitor other);

    /**
     * Visits another game object for collision handling.
     * Subclasses can override to define specific collision responses.
     *
     * @param other The game object being visited
     */
    public void visit(GameObject other) {
        // Default implementation - can be overridden by subclasses
    }

    /**
     * Updates the bounding box for composite game objects.
     * This method calculates the union of all child collision rectangles.
     */
    protected void baseUpdateBoundingBox() {
        // Get first child
        Component component = this;
        GameObject node = (GameObject) getChild(component);
        
        if (node == null) {
            return;
        }

        CollisionRect totalRect = this.collisionObject.getCollisionRect();
        totalRect.set(node.getCollisionObject().getCollisionRect());

        // Loop through siblings
        while (node != null) {
            totalRect.union(node.getCollisionObject().getCollisionRect());

            // Go to next sibling
            component = getSibling(node);
            node = (component != null) ? (GameObject) component : null;
        }

        this.x = this.collisionObject.getCollisionRect().x;
        this.y = this.collisionObject.getCollisionRect().y;
    }

    /**
     * Helper method to get the first child of a component.
     *
     * @param component The parent component
     * @return The first child, or null if none exists
     */
    private Component getChild(Component component) {
        return component.getFirstChild();
    }

    /**
     * Helper method to get the next sibling of a component.
     *
     * @param component The current component
     * @return The next sibling, or null if none exists
     */
    private Component getSibling(Component component) {
        return (Component) component.getNext();
    }

    /**
     * Washes/resets this game object for reuse in the object pool.
     */
    @Override
    protected void wash() {
        this.name = GameObjectName.UNINITIALIZED;
        this.x = 0.0f;
        this.y = 0.0f;
        this.markedForDeath = false;
        if (this.spriteProxy != null) {
            this.spriteProxy.wash();
        }
    }

    /**
     * Dumps debug information about this game object.
     */
    @Override
    public void dump() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return String.format("%s[name=%s, pos=(%.2f, %.2f), dead=%b]",
                getClass().getSimpleName(), name, x, y, markedForDeath);
    }

    /**
     * Enum defining all possible game object types.
     */
    public enum GameObjectName {
        // Aliens
        SQUID_ALIEN,
        JELLYFISH_ALIEN,
        CRAB_ALIEN,
        FLYING_SAUCER,
        FLYING_SAUCER_ROOT,
        
        // Player
        CORE_CANNON,
        CORE_CANNON_GROUP,
        
        // Alien structures
        ALIEN_GRID,
        ALIEN_GRID_COLUMN,
        
        // Projectiles
        MISSILE,
        MISSILE_GROUP,
        BOMB,
        BOMB_ROOT,
        FLYING_SAUCER_BOMB,
        
        // Shields
        SHIELD_GROUP,
        SHIELD_ROOT,
        SHIELD_BRICK,
        SHIELD_COLUMN,
        SHIELD_BRICK_LEFT_TOP_0,
        SHIELD_BRICK_LEFT_TOP_1,
        SHIELD_BRICK_LEFT_BOTTOM,
        SHIELD_BRICK_RIGHT_BOTTOM,
        SHIELD_BRICK_RIGHT_TOP_0,
        SHIELD_BRICK_RIGHT_TOP_1,
        
        // Walls
        RIGHT_WALL,
        LEFT_WALL,
        TOP_WALL,
        BOTTOM_WALL,
        LEFT_BUMPER,
        RIGHT_BUMPER,
        
        // Special
        NULL_OBJECT,
        UNINITIALIZED
    }

    /**
     * Interface for collision visitor pattern.
     * Allows double-dispatch for collision handling.
     */
    public interface CollisionVisitor {
        void visit(GameObject other);
        
        default void visitAlienGrid(AlienGrid alienGrid) {
            // Default implementation - can be overridden
        }
        
        default void visitAlienGridColumn(AlienGridColumn alienGridColumn) {
            // Default implementation - can be overridden
        }
        
        default void visitMissileGroup(MissileGroup missileGroup) {
            // Default implementation - can be overridden
        }
        
        default void visitCoreCannonGroup(CoreCannonGroup coreCannonGroup) {
            // Default implementation - can be overridden
        }
        
        default void visitShieldBrick(ShieldBrick shieldBrick) {
            // Default implementation - can be overridden
        }
        
        default void visitFlyingSaucer(FlyingSaucer flyingSaucer) {
            // Default implementation - can be overridden
        }
        
        default void visitFlyingSaucerRoot(net.beeland.spaceinvaders.composite.Composite flyingSaucerRoot) {
            // Default implementation - can be overridden
        }
    }
}