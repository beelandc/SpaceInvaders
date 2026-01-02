package net.beeland.spaceinvaders.gameobject;

/**
 * CrabAlien - Middle rows alien, worth 20 points
 */
public class CrabAlien extends Alien {
    
    private static final int CRAB_POINTS = 20;
    
    /**
     * Constructor
     *
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public CrabAlien(float posX, float posY) {
        super(GameObject.GameObjectName.CRAB_ALIEN);
        this.x = posX;
        this.y = posY;
    }
    
    @Override
    public int getPoints() {
        return CRAB_POINTS;
    }
    
    @Override
    public Type getType() {
        return Type.CRAB;
    }
    
    @Override
    public void update() {
        super.update();
    }
}