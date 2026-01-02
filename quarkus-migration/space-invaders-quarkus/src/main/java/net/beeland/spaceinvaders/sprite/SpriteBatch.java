package net.beeland.spaceinvaders.sprite;

import org.jboss.logging.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

/**
 * SpriteBatch - Efficient batched sprite rendering
 * 
 * Batches multiple sprite draw calls into a single OpenGL draw call
 * for improved performance. Uses dynamic vertex buffer with instancing.
 */
public class SpriteBatch {
    
    private static final Logger LOG = Logger.getLogger(SpriteBatch.class);
    
    // Maximum sprites per batch
    private static final int MAX_BATCH_SIZE = 1000;
    
    // Vertices per sprite (2 triangles = 6 vertices)
    private static final int VERTICES_PER_SPRITE = 6;
    
    // Floats per vertex (position: 2, texCoord: 2, color: 4)
    private static final int FLOATS_PER_VERTEX = 8;
    
    // Total floats per sprite
    private static final int FLOATS_PER_SPRITE = VERTICES_PER_SPRITE * FLOATS_PER_VERTEX;
    
    // OpenGL objects
    private int vao;
    private int vbo;
    private int shaderProgram;
    
    // Vertex data buffer
    private FloatBuffer vertexBuffer;
    
    // Batch state
    private int spriteCount;
    private boolean isDrawing;
    private int currentTextureId;  // Track current texture for batching
    
    // Projection matrix
    private Matrix4f projectionMatrix;
    
    /**
     * Constructor
     */
    public SpriteBatch() {
        this.spriteCount = 0;
        this.isDrawing = false;
        this.projectionMatrix = new Matrix4f();
        
        // Allocate vertex buffer
        this.vertexBuffer = BufferUtils.createFloatBuffer(MAX_BATCH_SIZE * FLOATS_PER_SPRITE);
    }
    
    /**
     * Initialize the sprite batch
     * Sets up OpenGL buffers and shader program
     * 
     * @param shaderProgram Compiled shader program ID
     * @param screenWidth Screen width in pixels
     * @param screenHeight Screen height in pixels
     */
    public void initialize(int shaderProgram, int screenWidth, int screenHeight) {
        this.shaderProgram = shaderProgram;
        
        // Create orthographic projection matrix
        // Origin at top-left, Y-axis points down (screen coordinates)
        projectionMatrix.setOrtho2D(0, screenWidth, screenHeight, 0);
        
        // Generate and bind VAO
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        
        // Generate and bind VBO
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        
        // Allocate buffer (dynamic draw for frequent updates)
        glBufferData(GL_ARRAY_BUFFER, (long) MAX_BATCH_SIZE * FLOATS_PER_SPRITE * Float.BYTES, GL_DYNAMIC_DRAW);
        
        // Set up vertex attributes
        // Position (location = 0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        
        // Texture coordinates (location = 1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
        
        // Color (location = 2)
        glVertexAttribPointer(2, 4, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 4 * Float.BYTES);
        glEnableVertexAttribArray(2);
        
        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        
        LOG.info("SpriteBatch initialized: VAO=" + vao + ", VBO=" + vbo + ", Shader=" + shaderProgram);
    }
    
    /**
     * Begin a batch rendering session
     */
    public void begin() {
        if (isDrawing) {
            LOG.error("SpriteBatch.begin() called while already drawing");
            return;
        }
        
        isDrawing = true;
        spriteCount = 0;
        currentTextureId = -1;  // Reset texture tracking
        vertexBuffer.clear();
    }
    
    /**
     * Draw a sprite proxy
     *
     * @param proxy Sprite proxy to draw
     */
    public void draw(SpriteProxy proxy) {
        if (!isDrawing) {
            LOG.error("SpriteBatch.draw() called outside begin/end");
            return;
        }
        
        if (proxy == null || proxy.getImage() == null) {
            return;
        }
        
        // Get texture ID from the image
        int textureId = proxy.getImage().getTexture().getTextureId();
        
        // Flush if texture changes or batch is full
        if ((currentTextureId != -1 && currentTextureId != textureId) || spriteCount >= MAX_BATCH_SIZE) {
            flush();
        }
        
        // Set current texture
        currentTextureId = textureId;
        
        // Get sprite properties
        float x = proxy.getX();
        float y = proxy.getY();
        float width = proxy.getWidth();
        float height = proxy.getHeight();
        float angle = proxy.getAngle();
        
        // Get texture coordinates from image
        float u0 = proxy.getImage().getX();
        float v0 = proxy.getImage().getY();
        float u1 = u0 + proxy.getImage().getWidth();
        float v1 = v0 + proxy.getImage().getHeight();
        
        // Get color
        float r = proxy.getRed();
        float g = proxy.getGreen();
        float b = proxy.getBlue();
        float a = proxy.getAlpha();
        
        // Calculate sprite corners (with rotation if needed)
        float x0, y0, x1, y1, x2, y2, x3, y3;
        
        if (angle != 0.0f) {
            // Rotate around center
            float centerX = x + width / 2.0f;
            float centerY = y + height / 2.0f;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            
            // Calculate rotated corners
            float dx0 = -width / 2.0f;
            float dy0 = -height / 2.0f;
            float dx1 = width / 2.0f;
            float dy1 = height / 2.0f;
            
            x0 = centerX + dx0 * cos - dy0 * sin;
            y0 = centerY + dx0 * sin + dy0 * cos;
            x1 = centerX + dx1 * cos - dy0 * sin;
            y1 = centerY + dx1 * sin + dy0 * cos;
            x2 = centerX + dx1 * cos - dy1 * sin;
            y2 = centerY + dx1 * sin + dy1 * cos;
            x3 = centerX + dx0 * cos - dy1 * sin;
            y3 = centerY + dx0 * sin + dy1 * cos;
        } else {
            // No rotation - simple rectangle
            x0 = x;
            y0 = y;
            x1 = x + width;
            y1 = y;
            x2 = x + width;
            y2 = y + height;
            x3 = x;
            y3 = y + height;
        }
        
        // Add vertices to buffer (2 triangles = 6 vertices)
        // Triangle 1: top-left, top-right, bottom-left
        addVertex(x0, y0, u0, v0, r, g, b, a);
        addVertex(x1, y1, u1, v0, r, g, b, a);
        addVertex(x3, y3, u0, v1, r, g, b, a);
        
        // Triangle 2: top-right, bottom-right, bottom-left
        addVertex(x1, y1, u1, v0, r, g, b, a);
        addVertex(x2, y2, u1, v1, r, g, b, a);
        addVertex(x3, y3, u0, v1, r, g, b, a);
        
        spriteCount++;
    }
    
    /**
     * Draw a texture region directly (for font rendering)
     *
     * @param textureId OpenGL texture ID
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @param u0 Texture U coordinate (left)
     * @param v0 Texture V coordinate (top)
     * @param u1 Texture U coordinate (right)
     * @param v1 Texture V coordinate (bottom)
     * @param r Red color component (0-1)
     * @param g Green color component (0-1)
     * @param b Blue color component (0-1)
     * @param a Alpha component (0-1)
     */
    public void drawTexture(int textureId, float x, float y, float width, float height,
                           float u0, float v0, float u1, float v1,
                           float r, float g, float b, float a) {
        if (!isDrawing) {
            LOG.error("SpriteBatch.drawTexture() called outside begin/end");
            return;
        }
        
        // Flush if texture changes or batch is full
        if ((currentTextureId != -1 && currentTextureId != textureId) || spriteCount >= MAX_BATCH_SIZE) {
            flush();
        }
        
        // Set current texture
        currentTextureId = textureId;
        
        // Calculate sprite corners (no rotation for fonts)
        // In OpenGL, Y increases upward, so y is the BOTTOM of the quad
        float x0 = x;
        float y0 = y + height;  // top-left (higher Y)
        float x1 = x + width;
        float y1 = y + height;  // top-right (higher Y)
        float x2 = x + width;
        float y2 = y;           // bottom-right (lower Y)
        float x3 = x;
        float y3 = y;           // bottom-left (lower Y)
        
        // Add vertices to buffer (2 triangles = 6 vertices)
        // Triangle 1: top-left, top-right, bottom-left
        addVertex(x0, y0, u0, v0, r, g, b, a);
        addVertex(x1, y1, u1, v0, r, g, b, a);
        addVertex(x3, y3, u0, v1, r, g, b, a);
        
        // Triangle 2: top-right, bottom-right, bottom-left
        addVertex(x1, y1, u1, v0, r, g, b, a);
        addVertex(x2, y2, u1, v1, r, g, b, a);
        addVertex(x3, y3, u0, v1, r, g, b, a);
        
        spriteCount++;
    }
    
    /**
     * Add a vertex to the buffer
     */
    private void addVertex(float x, float y, float u, float v, float r, float g, float b, float a) {
        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.put(u);
        vertexBuffer.put(v);
        vertexBuffer.put(r);
        vertexBuffer.put(g);
        vertexBuffer.put(b);
        vertexBuffer.put(a);
    }
    
    /**
     * End the batch and render all sprites
     */
    public void end() {
        if (!isDrawing) {
            LOG.error("SpriteBatch.end() called without begin");
            return;
        }
        
        flush();
        isDrawing = false;
    }
    
    /**
     * Flush the current batch to the GPU
     */
    private void flush() {
        if (spriteCount == 0) {
            return;
        }
        
        LOG.debug("SpriteBatch.flush() - Rendering " + spriteCount + " sprites with texture ID: " + currentTextureId);
        
        // Prepare buffer for reading
        vertexBuffer.flip();
        
        LOG.debug("  Buffer position: " + vertexBuffer.position() + ", limit: " + vertexBuffer.limit() + ", capacity: " + vertexBuffer.capacity());
        
        // Use shader program
        glUseProgram(shaderProgram);
        
        // Set projection matrix uniform
        int projectionLoc = glGetUniformLocation(shaderProgram, "u_projection");
        if (projectionLoc != -1) {
            FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
            projectionMatrix.get(matrixBuffer);
            glUniformMatrix4fv(projectionLoc, false, matrixBuffer);
            LOG.debug("  Set projection matrix uniform at location: " + projectionLoc);
        } else {
            LOG.warn("  Projection matrix uniform 'u_projection' not found in shader!");
        }
        
        // Bind the current texture
        if (currentTextureId != -1) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, currentTextureId);
            
            // Set texture uniform
            int textureLoc = glGetUniformLocation(shaderProgram, "u_texture");
            if (textureLoc != -1) {
                glUniform1i(textureLoc, 0);  // Use texture unit 0
                LOG.debug("  Bound texture " + currentTextureId + " to unit 0, uniform location: " + textureLoc);
            } else {
                LOG.warn("  Texture uniform 'u_texture' not found in shader!");
            }
        }
        
        // Enable blending for transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        // Bind VAO and VBO
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        
        // Upload vertex data
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer);
        LOG.debug("  Uploaded " + vertexBuffer.limit() + " floats to VBO");
        
        // Draw
        int vertexCount = spriteCount * VERTICES_PER_SPRITE;
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        LOG.debug("  glDrawArrays(GL_TRIANGLES, 0, " + vertexCount + ")");
        
        // Check for OpenGL errors
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            LOG.error("  OpenGL error after draw: " + error);
        }
        
        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
        glUseProgram(0);
        glDisable(GL_BLEND);
        
        // Reset for next batch
        vertexBuffer.clear();
        spriteCount = 0;
        
        LOG.debug("SpriteBatch.flush() completed");
    }
    
    /**
     * Update projection matrix (e.g., on window resize)
     * 
     * @param screenWidth New screen width
     * @param screenHeight New screen height
     */
    public void updateProjection(int screenWidth, int screenHeight) {
        projectionMatrix.setOrtho2D(0, screenWidth, screenHeight, 0);
        LOG.info("SpriteBatch projection updated: " + screenWidth + "x" + screenHeight);
    }
    
    /**
     * Get the current sprite count in the batch
     * 
     * @return Number of sprites in current batch
     */
    public int getSpriteCount() {
        return spriteCount;
    }
    
    /**
     * Check if currently drawing
     * 
     * @return True if between begin() and end()
     */
    public boolean isDrawing() {
        return isDrawing;
    }
    
    /**
     * Cleanup OpenGL resources
     */
    public void dispose() {
        if (vbo != 0) {
            glDeleteBuffers(vbo);
            vbo = 0;
        }
        
        if (vao != 0) {
            glDeleteVertexArrays(vao);
            vao = 0;
        }
        
        LOG.info("SpriteBatch disposed");
    }
}
