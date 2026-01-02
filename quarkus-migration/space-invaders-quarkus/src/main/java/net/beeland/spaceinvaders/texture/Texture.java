package net.beeland.spaceinvaders.texture;

import net.beeland.spaceinvaders.manager.DLink;
import org.jboss.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Texture class for loading and managing OpenGL textures
 * Represents a single texture loaded from an image file
 */
public class Texture extends DLink {
    
    private static final Logger LOG = Logger.getLogger(Texture.class);
    
    // Texture properties
    private String name;
    private int textureId;
    private int width;
    private int height;
    private int channels;
    
    /**
     * Texture name enum for identification
     */
    public enum Name {
        ALIENS,
        SHIELD,
        BIRDS,
        HOT_PINK,
        RED_GRID,
        STITCH,
        CONSOLAS_20PT,
        CONSOLAS_36PT,
        UNINITIALIZED
    }
    
    /**
     * Constructor
     */
    public Texture() {
        this.name = Name.UNINITIALIZED.toString();
        this.textureId = 0;
        this.width = 0;
        this.height = 0;
        this.channels = 0;
    }
    
    /**
     * Set the texture properties
     * @param name Texture name
     * @param filePath Path to texture file
     */
    public void set(Name name, String filePath) {
        this.name = name.toString();
        loadTexture(filePath);
    }
    
    /**
     * Load texture from resource using STB Image
     * @param resourcePath Path to image resource (e.g., "graphics/Consolas36pt.tga")
     */
    private void loadTexture(String resourcePath) {
        ByteBuffer imageData = null;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Load resource as InputStream
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            
            // Read InputStream into ByteBuffer
            imageData = readInputStreamToByteBuffer(inputStream);
            
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);
            
            // TGA files can have either top-left or bottom-left origin
            // PNG files always have top-left origin
            // For consistency, we'll flip TGA files to match PNG (top-left origin)
            // Then our Image.java Y-flip will work correctly for both formats
            boolean isTga = resourcePath.toLowerCase().endsWith(".tga");
            STBImage.stbi_set_flip_vertically_on_load(isTga);  // Flip TGA to top-left, leave PNG as-is
            
            // Load image from memory buffer
            ByteBuffer imageBuffer = STBImage.stbi_load_from_memory(imageData, widthBuffer, heightBuffer,
                                                        channelsBuffer, 4); // Force RGBA
            
            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture: " + resourcePath +
                                         " - " + STBImage.stbi_failure_reason());
            }
            
            this.width = widthBuffer.get(0);
            this.height = heightBuffer.get(0);
            this.channels = 4; // Forced RGBA
            
            // Generate OpenGL texture
            textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            
            // Set texture parameters
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            
            // Upload texture data
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
            
            // Generate mipmaps
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            
            // Free image buffer
            STBImage.stbi_image_free(imageBuffer);
            
            LOG.info("Loaded texture: " + name + " (" + width + "x" + height + ") ID: " + textureId);
            
        } catch (Exception e) {
            LOG.error("Error loading texture: " + resourcePath, e);
            throw new RuntimeException("Failed to load texture: " + resourcePath, e);
        } finally {
            // Free the image data buffer
            if (imageData != null) {
                MemoryUtil.memFree(imageData);
            }
        }
    }
    
    /**
     * Read an InputStream into a ByteBuffer for STB Image
     * @param inputStream Input stream to read
     * @return ByteBuffer containing the data
     */
    private ByteBuffer readInputStreamToByteBuffer(InputStream inputStream) throws Exception {
        try (ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            // Start with a reasonable buffer size
            ByteBuffer buffer = MemoryUtil.memAlloc(8192);
            
            while (channel.read(buffer) != -1) {
                // If buffer is full, expand it
                if (buffer.remaining() == 0) {
                    ByteBuffer newBuffer = MemoryUtil.memAlloc(buffer.capacity() * 2);
                    buffer.flip();
                    newBuffer.put(buffer);
                    MemoryUtil.memFree(buffer);
                    buffer = newBuffer;
                }
            }
            
            buffer.flip();
            return buffer;
        }
    }
    
    /**
     * Create a procedural texture (for testing or simple graphics)
     * @param name Texture name
     * @param width Width in pixels
     * @param height Height in pixels
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     * @param alpha Alpha component (0-255)
     */
    public void createProcedural(Name name, int width, int height, int red, int green, int blue, int alpha) {
        this.name = name.toString();
        this.width = width;
        this.height = height;
        this.channels = 4;
        
        // Create pixel data (RGBA)
        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(width * height * 4);
        for (int i = 0; i < width * height; i++) {
            imageBuffer.put((byte) red);
            imageBuffer.put((byte) green);
            imageBuffer.put((byte) blue);
            imageBuffer.put((byte) alpha);
        }
        imageBuffer.flip();
        
        // Generate OpenGL texture
        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        // Set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        
        // Upload texture data
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                        GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
        
        LOG.info("Created procedural texture: " + name + " (" + width + "x" + height + ") ID: " + textureId);
    }
    
    /**
     * Bind this texture for rendering
     */
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }
    
    /**
     * Unbind texture
     */
    public static void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    /**
     * Get texture ID
     * @return OpenGL texture ID
     */
    public int getTextureId() {
        return textureId;
    }
    
    /**
     * Get texture name
     * @return Texture name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get texture width
     * @return Width in pixels
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Get texture height
     * @return Height in pixels
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Cleanup texture resources
     */
    public void cleanup() {
        if (textureId != 0) {
            GL11.glDeleteTextures(textureId);
            textureId = 0;
        }
    }
    
    @Override
    protected void wash() {
        // Reset texture state when returned to pool
        this.name = Name.UNINITIALIZED.toString();
        // Note: We don't delete the OpenGL texture here as it may be reused
    }
    
    @Override
    public void dump() {
        LOG.info("Texture: " + name + " ID: " + textureId + 
                " Size: " + width + "x" + height);
    }
}
