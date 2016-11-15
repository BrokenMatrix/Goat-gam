package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import game.entities.Entity;
import game.entities.EntityInstance;
import game.world.Chunk;
import game.world.World;

public class EntityRenderer {
	
	public static void Render(Matrix4f view){
		
		//Starting shader and loading view matrix
		Renderer.shader.start();
		Renderer.shader.loadView(view);
		//Rendering entities
		for(Chunk chunk : World.Visible){
			for(Entity entity : chunk.entities.keySet()){
				GL30.glBindVertexArray(entity.model[0]);
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.texture);
				
				for(EntityInstance instance : chunk.entities.get(entity)){
					if(entity.FacesCamera){
						instance.FaceCamera(view);
					}
					Renderer.shader.loadTransformation(instance.transformation);
					GL11.glDrawElements(GL11.GL_TRIANGLES, entity.model[1], GL11.GL_UNSIGNED_INT, 0);
				}
				
				GL20.glDisableVertexAttribArray(0);
				GL20.glDisableVertexAttribArray(1);
				GL30.glBindVertexArray(0);
			}
		}
		//Stopping shader
		Renderer.shader.stop();
		
	}
	
}
