package net.beeland.spaceinvaders.image;

import net.beeland.spaceinvaders.manager.DLink;
import net.beeland.spaceinvaders.texture.Texture;
import org.jboss.logging.Logger;

/**
 * Image class representing a rectangular region within a texture
 * Used for sprite sheets where multiple images are packed into one texture
 */
public class Image extends DLink {
    
    private static final Logger LOG = Logger.getLogger(Image.class);
    
    // Image properties
    private String name;
    private Texture texture;
    private float x;      // X position in texture (normalized 0-1)
    private float y;      // Y position in texture (normalized 0-1)
    private float width;  // Width in texture (normalized 0-1)
    private float height; // Height in texture (normalized 0-1)
    
    // Pixel dimensions
    private int pixelX;
    private int pixelY;
    private int pixelWidth;
    private int pixelHeight;
    
    /**
     * Image name enum for identification
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
        UNINITIALIZED
    }
    
    /**
     * Constructor
     */
    public Image() {
        this.name = Name.UNINITIALIZED.toString();
        this.texture = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.width = 0.0f;
        this.height = 0.0f;
        this.pixelX = 0;
        this.pixelY = 0;
        this.pixelWidth = 0;
        this.pixelHeight = 0;
    }
    
    /**
     * Set the image properties using pixel coordinates
     * @param name Image name
     * @param texture Source texture
     * @param x X position in pixels
     * @param y Y position in pixels
     * @param width Width in pixels
     * @param height Height in pixels
     */
    public void set(Name name, Texture texture, int x, int y, int width, int height) {
        this.name = name.toString();
        this.texture = texture;
        this.pixelX = x;
        this.pixelY = y;
        this.pixelWidth = width;
        this.pixelHeight = height;
        
        // Calculate normalized texture coordinates
        // Flip Y coordinate to convert from top-left origin (image space) to bottom-left origin (OpenGL)
        if (texture != null) {
            float texWidth = (float) texture.getWidth();
            float texHeight = (float) texture.getHeight();
            
            this.x = x / texWidth;
            this.y = 1.0f - (y + height) / texHeight;  // Flip Y: bottom of glyph in OpenGL space
            this.width = width / texWidth;
            this.height = height / texHeight;
        }
        
        LOG.debug("Created image: " + name + " at (" + x + "," + y + ") size: " + 
                 width + "x" + height);
    }
    
    /**
     * Set the image to use the entire texture
     * @param name Image name
     * @param texture Source texture
     */
    public void set(Name name, Texture texture) {
        if (texture != null) {
            set(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
        }
    }
    
    /**
     * Get the texture
     * @return Source texture
     */
    public Texture getTexture() {
        return texture;
    }
    
    /**
     * Get image name
     * @return Image name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get normalized X coordinate
     * @return X coordinate (0-1)
     */
    public float getX() {
        return x;
    }
    
    /**
     * Get normalized Y coordinate
     * @return Y coordinate (0-1)
     */
    public float getY() {
        return y;
    }
    
    /**
     * Get normalized width
     * @return Width (0-1)
     */
    public float getWidth() {
        return width;
    }
    
    /**
     * Get normalized height
     * @return Height (0-1)
     */
    public float getHeight() {
        return height;
    }
    
    /**
     * Get pixel X coordinate
     * @return X in pixels
     */
    public int getPixelX() {
        return pixelX;
    }
    
    /**
     * Get pixel Y coordinate
     * @return Y in pixels
     */
    public int getPixelY() {
        return pixelY;
    }
    
    /**
     * Get pixel width
     * @return Width in pixels
     */
    public int getPixelWidth() {
        return pixelWidth;
    }
    
    /**
     * Get pixel height
     * @return Height in pixels
     */
    public int getPixelHeight() {
        return pixelHeight;
    }
    
    /**
     * Get texture coordinates for rendering
     * Returns array: [minX, minY, maxX, maxY]
     * @return Texture coordinate array
     */
    public float[] getTexCoords() {
        return new float[] {
            x,              // minX
            y,              // minY
            x + width,      // maxX
            y + height      // maxY
        };
    }
    
    @Override
    protected void wash() {
        // Reset image state when returned to pool
        this.name = Name.UNINITIALIZED.toString();
        this.texture = null;
        this.x = 0.0f;
        this.y = 0.0f;
        this.width = 0.0f;
        this.height = 0.0f;
        this.pixelX = 0;
        this.pixelY = 0;
        this.pixelWidth = 0;
        this.pixelHeight = 0;
    }
    
    @Override
    public void dump() {
        String texName = (texture != null) ? texture.getName() : "null";
        LOG.info("Image: " + name + " Texture: " + texName + 
                " Pos: (" + pixelX + "," + pixelY + ") Size: " + 
                pixelWidth + "x" + pixelHeight);
    }
}
