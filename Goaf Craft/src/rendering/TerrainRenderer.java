package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import game.world.Chunk;
import game.world.World;

public class TerrainRenderer {
	
	public static void Render(Matrix4f view){
		
		//Starting shader and loading view matrix
		Renderer.shader.start();
		Renderer.shader.loadView(view);
		//Rendering chunks
		for(Chunk chunk : World.Visible){
			//Binding attributes
			GL30.glBindVertexArray(chunk.vao);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			
			//Binding texture, loading transformation matrix, and rending chunk
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, chunk.texture);
			Renderer.shader.loadTransformation(chunk.transformation);
			GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.vertex_count, GL11.GL_UNSIGNED_INT, 0);
			
			//Disabling attributes
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		//Stopping shader
		Renderer.shader.stop();
		
	}
	
}
