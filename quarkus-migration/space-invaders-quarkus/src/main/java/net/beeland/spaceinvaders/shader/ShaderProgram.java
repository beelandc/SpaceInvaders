package net.beeland.spaceinvaders.shader;

import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL33.*;

/**
 * ShaderProgram - Utility class for loading and compiling GLSL shaders
 * 
 * Handles shader compilation, linking, and error reporting
 */
public class ShaderProgram {
    
    private static final Logger LOG = Logger.getLogger(ShaderProgram.class);
    
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    
    /**
     * Constructor
     */
    public ShaderProgram() {
        this.programId = 0;
        this.vertexShaderId = 0;
        this.fragmentShaderId = 0;
    }
    
    /**
     * Create and compile shader program from resource files
     * 
     * @param vertexPath Path to vertex shader resource (e.g., "shaders/sprite.vert")
     * @param fragmentPath Path to fragment shader resource (e.g., "shaders/sprite.frag")
     * @return True if successful, false otherwise
     */
    public boolean createFromResources(String vertexPath, String fragmentPath) {
        try {
            // Load shader source code from resources
            String vertexSource = loadResourceAsString(vertexPath);
            String fragmentSource = loadResourceAsString(fragmentPath);
            
            if (vertexSource == null || fragmentSource == null) {
                LOG.error("Failed to load shader resources");
                return false;
            }
            
            return createFromSource(vertexSource, fragmentSource);
            
        } catch (Exception e) {
            LOG.error("Error loading shader resources: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Create and compile shader program from source strings
     * 
     * @param vertexSource Vertex shader source code
     * @param fragmentSource Fragment shader source code
     * @return True if successful, false otherwise
     */
    public boolean createFromSource(String vertexSource, String fragmentSource) {
        // Compile vertex shader
        vertexShaderId = compileShader(GL_VERTEX_SHADER, vertexSource);
        if (vertexShaderId == 0) {
            LOG.error("Failed to compile vertex shader");
            return false;
        }
        
        // Compile fragment shader
        fragmentShaderId = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShaderId == 0) {
            LOG.error("Failed to compile fragment shader");
            glDeleteShader(vertexShaderId);
            return false;
        }
        
        // Link shader program
        programId = linkProgram(vertexShaderId, fragmentShaderId);
        if (programId == 0) {
            LOG.error("Failed to link shader program");
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);
            return false;
        }
        
        LOG.info("Shader program created successfully: ID=" + programId);
        return true;
    }
    
    /**
     * Compile a shader
     * 
     * @param type Shader type (GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
     * @param source Shader source code
     * @return Shader ID, or 0 on failure
     */
    private int compileShader(int type, String source) {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            LOG.error("Failed to create shader object");
            return 0;
        }
        
        // Set shader source and compile
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);
        
        // Check compilation status
        int status = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        if (status == GL_FALSE) {
            String error = glGetShaderInfoLog(shaderId);
            String shaderType = (type == GL_VERTEX_SHADER) ? "vertex" : "fragment";
            LOG.error("Failed to compile " + shaderType + " shader:\n" + error);
            glDeleteShader(shaderId);
            return 0;
        }
        
        String shaderType = (type == GL_VERTEX_SHADER) ? "vertex" : "fragment";
        LOG.info("Compiled " + shaderType + " shader: ID=" + shaderId);
        return shaderId;
    }
    
    /**
     * Link shader program
     * 
     * @param vertexId Vertex shader ID
     * @param fragmentId Fragment shader ID
     * @return Program ID, or 0 on failure
     */
    private int linkProgram(int vertexId, int fragmentId) {
        int programId = glCreateProgram();
        if (programId == 0) {
            LOG.error("Failed to create shader program");
            return 0;
        }
        
        // Attach shaders
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        
        // Link program
        glLinkProgram(programId);
        
        // Check link status
        int status = glGetProgrami(programId, GL_LINK_STATUS);
        if (status == GL_FALSE) {
            String error = glGetProgramInfoLog(programId);
            LOG.error("Failed to link shader program:\n" + error);
            glDeleteProgram(programId);
            return 0;
        }
        
        // Validate program
        glValidateProgram(programId);
        status = glGetProgrami(programId, GL_VALIDATE_STATUS);
        if (status == GL_FALSE) {
            String error = glGetProgramInfoLog(programId);
            LOG.warn("Shader program validation warning:\n" + error);
        }
        
        // Detach shaders (they're now part of the program)
        glDetachShader(programId, vertexId);
        glDetachShader(programId, fragmentId);
        
        LOG.info("Linked shader program: ID=" + programId);
        return programId;
    }
    
    /**
     * Load a resource file as a string
     * 
     * @param resourcePath Path to resource
     * @return File contents as string, or null on error
     */
    private String loadResourceAsString(String resourcePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                LOG.error("Resource not found: " + resourcePath);
                return null;
            }
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
            
        } catch (Exception e) {
            LOG.error("Error reading resource: " + resourcePath, e);
            return null;
        }
    }
    
    /**
     * Use this shader program
     */
    public void use() {
        glUseProgram(programId);
    }
    
    /**
     * Stop using this shader program
     */
    public void unuse() {
        glUseProgram(0);
    }
    
    /**
     * Get the program ID
     * 
     * @return OpenGL program ID
     */
    public int getProgramId() {
        return programId;
    }
    
    /**
     * Get uniform location
     * 
     * @param name Uniform name
     * @return Uniform location, or -1 if not found
     */
    public int getUniformLocation(String name) {
        int location = glGetUniformLocation(programId, name);
        if (location == -1) {
            LOG.warn("Uniform not found: " + name);
        }
        return location;
    }
    
    /**
     * Set uniform integer value
     * 
     * @param name Uniform name
     * @param value Value to set
     */
    public void setUniform(String name, int value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            glUniform1i(location, value);
        }
    }
    
    /**
     * Set uniform float value
     * 
     * @param name Uniform name
     * @param value Value to set
     */
    public void setUniform(String name, float value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            glUniform1f(location, value);
        }
    }
    
    /**
     * Check if program is valid
     * 
     * @return True if program is created and linked
     */
    public boolean isValid() {
        return programId != 0;
    }
    
    /**
     * Cleanup shader resources
     */
    public void dispose() {
        if (vertexShaderId != 0) {
            glDeleteShader(vertexShaderId);
            vertexShaderId = 0;
        }
        
        if (fragmentShaderId != 0) {
            glDeleteShader(fragmentShaderId);
            fragmentShaderId = 0;
        }
        
        if (programId != 0) {
            glDeleteProgram(programId);
            LOG.info("Shader program disposed: ID=" + programId);
            programId = 0;
        }
    }
}
