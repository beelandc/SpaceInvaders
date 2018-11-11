#version 400 core

// Not allowed in 400, only 420
// layout (binding = 0) uniform sampler2D tex_object;

uniform vec4 input_Color;

out vec4 color;

void main(void)
{
	color = input_Color;
}

