#version 400 core

in vec3 position;
in vec2 texture_coordinate;

out vec2 pixel_texture_coordinate;
out float visibility;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

const float density = 0.007;
const float gradient = 3.0;

void main(void){

	pixel_texture_coordinate = texture_coordinate;
	vec4 position_relative_to_cam = view * transformation * vec4(position, 1.0);
	gl_Position = projection * position_relative_to_cam;
	
	float distance = length(position_relative_to_cam);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);

}