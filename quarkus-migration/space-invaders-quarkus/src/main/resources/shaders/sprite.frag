#version 330 core

// Fragment shader for sprite rendering

in vec2 v_texCoord;
in vec4 v_color;

out vec4 FragColor;

uniform sampler2D u_texture;

void main() {
    // Sample texture and multiply by vertex color (for tinting)
    vec4 texColor = texture(u_texture, v_texCoord);
    FragColor = texColor * v_color;
    
    // Discard fully transparent pixels
    if (FragColor.a < 0.01) {
        discard;
    }
}
