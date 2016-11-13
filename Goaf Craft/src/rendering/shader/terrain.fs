#version 400 core

in vec2 pixel_texture_coordinate;

out vec4 out_Color;

uniform sampler2D Texture;

void main(void){

	out_Color = texture(Texture, pixel_texture_coordinate);

}