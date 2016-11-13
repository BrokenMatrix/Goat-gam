package rendering;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import game.entities.Player;

public class Renderer {
	
	//Defining projection matrix variables
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	public static void Create(){
		
		//Creating projection matrix and initializing other renderers
		Matrix4f projection = CreateProjectionMatrix();
		TerrainRenderer.Create(projection);
		
	}
	
	public static void Render(){
		
		//Enabling depth test, clearing buffers, and clearing screen
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0.15f, 0.3f, 1);
		
		//Rendering everything
		TerrainRenderer.Render(Player.ViewMatrix);
		
	}
	
	public static void Destroy(){
		
		//Cleaning up renderers
		TerrainRenderer.Destroy();
		
	}
	
	//Creates a projection matirx
	private static Matrix4f CreateProjectionMatrix(){
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1f / Math.tan(Math.toRadians((FOV / 2f)))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
		
	}
	
}
