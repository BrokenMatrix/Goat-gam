#version 400 core

in vec2 pixel_texture_coordinate;
in float visibility;

out vec4 out_Color;

uniform sampler2D Texture;

#define sky_color vec4(0,0.15,0.3,1)

void main(void){

	vec4 t = texture(Texture, pixel_texture_coordinate);
	out_Color = vec4(mix(sky_color, t, visibility).xyz, t.w);
	//out_Color = vec4(pixel_texture_coordinate.xy, 0, 1);

}