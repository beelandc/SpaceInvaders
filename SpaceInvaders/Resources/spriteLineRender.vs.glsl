#version 400 core

layout (location = 0) in vec4 position;

uniform mat4 world_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;
             
void main(void)
{
	// create the transformed vert
    gl_Position = proj_matrix * view_matrix * world_matrix * position;
}
