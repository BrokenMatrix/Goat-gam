package rendering.shader;

import org.lwjgl.util.vector.Matrix4f;

public class TerrainShader extends Shader {
	
	//JUST A STANDARD SHADER CLASS
	
	private int location_transformation;
	private int location_projection;
	private int location_view;
	
	public TerrainShader(){
		
		super("terrain", true);
		
	}
	
	@Override
	protected void bindAttributes() {
		
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "color");
		
	}
	
	@Override
	protected void getAllUniformLocations() {
		
		location_transformation = super.getUniformLocation("transformation");
		location_projection = super.getUniformLocation("projection");
		location_view = super.getUniformLocation("view");
		
	}
	
	public void loadTransformation(Matrix4f matrix){
		
		super.loadMatrix(location_transformation, matrix);
		
	}
	
	public void loadProjection(Matrix4f matrix){
		
		super.loadMatrix(location_projection, matrix);
		
	}

	public void loadView(Matrix4f matrix){
	
		super.loadMatrix(location_view, matrix);
	
	}
	
}
