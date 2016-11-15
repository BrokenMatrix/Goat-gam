package game.entities;

import game.tools.ModelLoader;
import game.tools.TextureLoader;

public class Entities {
	
	public static Entity bisp_star;
	public static Entity effle_stalk;
	
	public static void Create(){
		
		int[] quad = ModelLoader.loadOBJ("Quad");
		
		bisp_star = new Entity(quad, TextureLoader.LoadImage("bisp_star"), 0.5f).FacesCamera();
		effle_stalk = new Entity(quad, TextureLoader.LoadImage("effle_stalk"), 0.3f).FacesCamera();
		
	}
	
}
