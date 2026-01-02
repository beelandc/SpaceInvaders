package net.beeland.spaceinvaders.gameobject;

/**
 * SquidAlien - Top row alien, worth 30 points
 */
public class SquidAlien extends Alien {
    
    private static final int SQUID_POINTS = 30;
    
    /**
     * Constructor
     *
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public SquidAlien(float posX, float posY) {
        super(GameObject.GameObjectName.SQUID_ALIEN);
        this.x = posX;
        this.y = posY;
    }
    
    @Override
    public int getPoints() {
        return SQUID_POINTS;
    }
    
    @Override
    public Type getType() {
        return Type.SQUID;
    }
    
    @Override
    public void update() {
        super.update();
    }
}