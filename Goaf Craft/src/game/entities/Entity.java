package game.entities;

import org.lwjgl.util.vector.Vector3f;

public class Entity {
	
	public int[] model;
	public int texture;
	public Vector3f scale;
	public boolean FacesCamera;
	
	public Entity(int[] model, int texture){
		
		this(model, texture, 1f);
		
	}
	
	public Entity FacesCamera(){
		
		FacesCamera = true;
		return this;
		
	}
	
	public Entity(int[] model, int texture, float scale){
		
		this.model = model;
		this.texture = texture;
		this.scale = new Vector3f(scale, scale, scale);
		
	}
	
}
