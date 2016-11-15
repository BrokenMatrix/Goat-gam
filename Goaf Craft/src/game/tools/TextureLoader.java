package game.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureLoader {
	
	//Defining resource folder location
	private static final String RESOURCE_LOCATION = "res/";
	//Defining bytes per pixel as 4 because we are using RGBA format
	private static final int BYTES_PER_PIXEL = 4;
	
	private static List<Integer> textures;
	
	public static void create(){
		
		//Initializing textures array
		textures = new ArrayList<Integer>();
		
	}
	
	//Loads a file image into and OpenGL sampler
	public static int LoadImage(String location){
		
		try {
			return LoadImage(ImageIO.read(new File(RESOURCE_LOCATION + location + ".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1337;
		
	}
	
	//Loads a BufferedImage image into and OpenGL sampler
	public static int LoadImage(BufferedImage image){
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getWidth(), pixels, 0, image.getWidth());
		ByteBuffer pixel_buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				int pixel = pixels[y * image.getWidth() + x];
				pixel_buffer.put((byte) ((pixel >> 16) & 0xFF));
				pixel_buffer.put((byte) ((pixel >> 8) & 0xFF));
				pixel_buffer.put((byte) (pixel & 0xFF));
				pixel_buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		pixel_buffer.flip();
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixel_buffer);
		return textureID;
		
	}
	
	public static void destroy(){
		
		//Deleting all textures from OpenGL memory
		for(int texture : textures){
			GL11.glDeleteTextures(texture);
		}
		
	}
	
}
