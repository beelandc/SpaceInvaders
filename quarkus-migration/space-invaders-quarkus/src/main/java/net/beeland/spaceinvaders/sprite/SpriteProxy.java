package net.beeland.spaceinvaders.sprite;

import net.beeland.spaceinvaders.image.Image;
import org.jboss.logging.Logger;

/**
 * SpriteProxy - Lightweight proxy for Sprite instances
 * Implements Proxy pattern for efficient sprite management
 * 
 * Multiple proxies can share the same underlying Sprite data,
 * each with unique position and state
 */
public class SpriteProxy {
    
    private static final Logger LOG = Logger.getLogger(SpriteProxy.class);
    
    // Reference to the real sprite (shared data)
    private Sprite realSprite;
    
    // Unique proxy properties (not shared)
    private float x;
    private float y;
    private float scaleX;
    private float scaleY;
    private float angle;
    
    // Color tint (unique per proxy)
    private float red;
    private float green;
    private float blue;
    private float alpha;
    
    /**
     * Constructor
     */
    public SpriteProxy() {
        this.realSprite = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.angle = 0.0f;
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    /**
     * Set the real sprite reference
     * @param sprite The sprite to proxy
     */
    public void set(Sprite sprite) {
        this.realSprite = sprite;
        
        // Initialize with sprite's default values
        if (sprite != null) {
            this.x = sprite.getX();
            this.y = sprite.getY();
            this.scaleX = sprite.getScaleX();
            this.scaleY = sprite.getScaleY();
            this.angle = sprite.getAngle();
            this.red = sprite.getRed();
            this.green = sprite.getGreen();
            this.blue = sprite.getBlue();
            this.alpha = sprite.getAlpha();
        }
    }
    
    /**
     * Set position
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Set scale
     * @param scaleX X scale factor
     * @param scaleY Y scale factor
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    /**
     * Set rotation angle
     * @param angle Angle in radians
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    /**
     * Set color tint
     * @param red Red component (0-1)
     * @param green Green component (0-1)
     * @param blue Blue component (0-1)
     * @param alpha Alpha component (0-1)
     */
    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    /**
     * Get the real sprite
     * @return Sprite reference
     */
    public Sprite getRealSprite() {
        return realSprite;
    }
    
    /**
     * Get sprite image (delegated to real sprite)
     * @return Image reference
     */
    public Image getImage() {
        return (realSprite != null) ? realSprite.getImage() : null;
    }
    
    /**
     * Get X position
     * @return X coordinate
     */
    public float getX() {
        return x;
    }
    
    /**
     * Get Y position
     * @return Y coordinate
     */
    public float getY() {
        return y;
    }
    
    /**
     * Get X scale
     * @return X scale factor
     */
    public float getScaleX() {
        return scaleX;
    }
    
    /**
     * Get Y scale
     * @return Y scale factor
     */
    public float getScaleY() {
        return scaleY;
    }
    
    /**
     * Get rotation angle
     * @return Angle in radians
     */
    public float getAngle() {
        return angle;
    }
    
    /**
     * Get width (from real sprite, scaled)
     * @return Width in pixels
     */
    public float getWidth() {
        if (realSprite != null) {
            Image image = realSprite.getImage();
            if (image != null) {
                return image.getPixelWidth() * scaleX;
            }
        }
        return 0.0f;
    }
    
    /**
     * Get height (from real sprite, scaled)
     * @return Height in pixels
     */
    public float getHeight() {
        if (realSprite != null) {
            Image image = realSprite.getImage();
            if (image != null) {
                return image.getPixelHeight() * scaleY;
            }
        }
        return 0.0f;
    }
    
    /**
     * Get red color component
     * @return Red (0-1)
     */
    public float getRed() {
        return red;
    }
    
    /**
     * Get green color component
     * @return Green (0-1)
     */
    public float getGreen() {
        return green;
    }
    
    /**
     * Get blue color component
     * @return Blue (0-1)
     */
    public float getBlue() {
        return blue;
    }
    
    /**
     * Get alpha component
     * @return Alpha (0-1)
     */
    public float getAlpha() {
        return alpha;
    }
    
    /**
     * Render the sprite proxy
     * Pushes proxy-specific values to real sprite before rendering
     */
    public void render() {
        if (realSprite != null) {
            // Push proxy values to real sprite
            realSprite.setPosition(x, y);
            realSprite.setScale(scaleX, scaleY);
            realSprite.setAngle(angle);
            realSprite.setColor(red, green, blue, alpha);
            
            // Render through real sprite
            realSprite.render();
        }
    }
    
    /**
     * Reset the proxy
     */
    public void wash() {
        this.realSprite = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.angle = 0.0f;
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    /**
     * Dump proxy information
     */
    public void dump() {
        String spriteName = (realSprite != null) ? realSprite.getName() : "null";
        LOG.info("SpriteProxy -> Sprite: " + spriteName + 
                " Pos: (" + x + "," + y + ") Scale: (" + scaleX + "," + scaleY + 
                ") Angle: " + angle + " Color: (" + red + "," + green + "," + blue + "," + alpha + ")");
    }
}
