#version 400 core

// Not allowed in 400, only 420
// layout (binding = 0) uniform sampler2D tex_object;

uniform sampler2D tex_object;
uniform vec4 input_Color;

in VS_OUT
{
    vec2 textCoordinate;
	mat4 uv_mat;
} fs_in;

out vec4 color;

void main(void)
{
	// correct the UV coordinates, originally its (0,0 - 1,1)
	// scale and translate to the correct sub image on the texture
	vec4 uv_vec;
	uv_vec.xy =  fs_in.textCoordinate.xy;
	uv_vec.z = 0.0f;
	uv_vec.w = 1.0f;

	vec4 uv_corrected = fs_in.uv_mat * uv_vec;

	// Now interpolated to the correct texel
	color = input_Color * texture(tex_object, uv_corrected.xy );
}
