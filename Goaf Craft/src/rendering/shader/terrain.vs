#version 400 core

in vec3 position;
in vec2 texture_coordinate;

out vec2 pixel_texture_coordinate;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main(void){

	pixel_texture_coordinate = texture_coordinate;
	gl_Position = projection * view * transformation * vec4(position, 1.0);

}