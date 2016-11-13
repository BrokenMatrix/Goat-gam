package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import game.world.Chunk;
import game.world.World;
import rendering.shader.TerrainShader;

public class TerrainRenderer {
	
	private static TerrainShader shader;
	
	public static void Create(Matrix4f projection){
		
		//Creating shader and loading projection matrix
		shader = new TerrainShader();
		shader.start();
		shader.loadProjection(projection);
		shader.stop();
		
	}
	
	public static void Render(Matrix4f view){
		
		//Starting shader and loading view matrix
		shader.start();
		shader.loadView(view);
		//Rendering chunks
		for(Chunk chunk : World.Visible){
			//Binding attributes
			GL30.glBindVertexArray(chunk.vao);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			
			//Binding texture, loading transformation matrix, and rending chunk
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, chunk.texture);
			shader.loadTransformation(chunk.transformation);
			GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.vertex_count, GL11.GL_UNSIGNED_INT, 0);
			
			//Disabling attributes
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		//Stopping shader
		shader.stop();
		
	}
	
	public static void Destroy(){
		
		//Cleaning up shader
		shader.cleanUp();
		
	}
	
}
