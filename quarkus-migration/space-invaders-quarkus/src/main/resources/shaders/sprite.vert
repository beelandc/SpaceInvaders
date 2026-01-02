#version 330 core

// Vertex shader for sprite rendering

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_texCoord;
layout (location = 2) in vec4 a_color;

out vec2 v_texCoord;
out vec4 v_color;

uniform mat4 u_projection;

void main() {
    // Transform position to clip space
    gl_Position = u_projection * vec4(a_position, 0.0, 1.0);
    
    // Pass texture coordinates and color to fragment shader
    v_texCoord = a_texCoord;
    v_color = a_color;
}
