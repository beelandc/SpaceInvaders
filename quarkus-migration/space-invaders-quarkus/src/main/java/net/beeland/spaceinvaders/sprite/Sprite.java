package net.beeland.spaceinvaders.sprite;

import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.manager.DLink;
import org.jboss.logging.Logger;

/**
 * Sprite class representing a renderable game object
 * Contains position, scale, rotation, and image reference
 */
public class Sprite extends DLink {
    
    private static final Logger LOG = Logger.getLogger(Sprite.class);
    
    // Sprite properties
    private String name;
    private Image image;
    private float x;
    private float y;
    private float scaleX;
    private float scaleY;
    private float angle;
    private float width;
    private float height;
    
    // Color tint (RGBA)
    private float red;
    private float green;
    private float blue;
    private float alpha;
    
    /**
     * Sprite name enum for identification
     */
    public enum Name {
        SQUID_ALIEN,
        CRAB_ALIEN,
        JELLYFISH_ALIEN,
        CORE_CANNON,
        MISSILE,
        BOMB,
        SHIELD_BRICK,
        UFO,
        EXPLOSION,
        BACKGROUND,
        UNINITIALIZED
    }
    
    /**
     * Constructor
     */
    public Sprite() {
        this.name = Name.UNINITIALIZED.toString();
        this.image = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.angle = 0.0f;
        this.width = 0.0f;
        this.height = 0.0f;
        
        // Default to white (no tint)
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    /**
     * Set the sprite properties
     * @param name Sprite name
     * @param image Image to render
     * @param x X position
     * @param y Y position
     */
    public void set(Name name, Image image, float x, float y) {
        this.name = name.toString();
        this.image = image;
        this.x = x;
        this.y = y;
        
        if (image != null) {
            this.width = image.getPixelWidth();
            this.height = image.getPixelHeight();
        }
        
        // Reset to defaults
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.angle = 0.0f;
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    /**
     * Set sprite position
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Set sprite scale
     * @param scaleX X scale factor
     * @param scaleY Y scale factor
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    /**
     * Set sprite rotation
     * @param angle Rotation angle in radians
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    /**
     * Set sprite color tint
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
     * Get sprite name
     * @return Sprite name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get sprite image
     * @return Image reference
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * Swap the sprite's image (for animations)
     * @param newImage New image to display
     */
    public void swapImage(Image newImage) {
        this.image = newImage;
        if (newImage != null) {
            this.width = newImage.getPixelWidth();
            this.height = newImage.getPixelHeight();
        }
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
     * Get sprite width
     * @return Width in pixels
     */
    public float getWidth() {
        return width * scaleX;
    }
    
    /**
     * Get sprite height
     * @return Height in pixels
     */
    public float getHeight() {
        return height * scaleY;
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
     * Render the sprite
     * This will be called by the sprite batch
     */
    public void render() {
        // Rendering is handled by SpriteBatch
        // This method is here for future direct rendering if needed
    }
    
    @Override
    protected void wash() {
        // Reset sprite state when returned to pool
        this.name = Name.UNINITIALIZED.toString();
        this.image = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.angle = 0.0f;
        this.width = 0.0f;
        this.height = 0.0f;
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    @Override
    public void dump() {
        String imageName = (image != null) ? image.getName() : "null";
        LOG.info("Sprite: " + name + " Image: " + imageName + 
                " Pos: (" + x + "," + y + ") Scale: (" + scaleX + "," + scaleY + 
                ") Angle: " + angle + " Color: (" + red + "," + green + "," + blue + "," + alpha + ")");
    }
}
