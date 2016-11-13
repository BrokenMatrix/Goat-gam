#version 400 core

in vec2 pixel_texture_coordinate;
in float visibility;

out vec4 out_Color;

uniform sampler2D Texture;

#define sky_color vec4(0,0.15,0.3,1)

void main(void){

	out_Color = mix(sky_color, texture(Texture, pixel_texture_coordinate), visibility);

}