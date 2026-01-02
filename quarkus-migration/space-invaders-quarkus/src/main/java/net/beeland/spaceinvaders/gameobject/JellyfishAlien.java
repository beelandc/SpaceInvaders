package net.beeland.spaceinvaders.gameobject;

/**
 * JellyfishAlien - Bottom rows alien, worth 10 points
 */
public class JellyfishAlien extends Alien {
    
    private static final int JELLYFISH_POINTS = 10;
    
    /**
     * Constructor
     *
     * @param posX Initial X position
     * @param posY Initial Y position
     */
    public JellyfishAlien(float posX, float posY) {
        super(GameObject.GameObjectName.JELLYFISH_ALIEN);
        this.x = posX;
        this.y = posY;
    }
    
    @Override
    public int getPoints() {
        return JELLYFISH_POINTS;
    }
    
    @Override
    public Type getType() {
        return Type.JELLYFISH;
    }
    
    @Override
    public void update() {
        super.update();
    }
}