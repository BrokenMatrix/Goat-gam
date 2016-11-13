package game;

import game.entities.Player;
import game.tools.VertexObjectHelper;
import game.world.World;
import rendering.Renderer;
import rendering.Window;

public class Main {
	
	public static void main(String[] args){
		
		//Creating window
		Window.Create(24);
		//Initializing classes
		VertexObjectHelper.Create();
		VertexObjectHelper.Create();
		Renderer.Create();
		World.Create();
		Player.Create();
		
		//Game loop
		while(!Window.IsCloseRequested()){
			
			//Updating objects
			World.Update();
			Player.Update();
			
			//Rendering objects
			Renderer.Render();
			
			//Updating window
			Window.Update();
			
		}
		
		//Cleaning up classes and closing program
		Renderer.Destroy();
		VertexObjectHelper.Destroy();
		Window.Destroy();
		
	}
	
}
