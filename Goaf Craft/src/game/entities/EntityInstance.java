package game.entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import game.tools.MathHelper;

public class EntityInstance {
	
	public Entity entity;
	public Matrix4f transformation;
	private Vector3f position;
	
	public EntityInstance(Entity entity, Vector3f position){
		
		this.entity = entity;
		this.transformation = new Matrix4f();
		this.transformation.translate(position);
		this.transformation.scale(entity.scale);
		this.position = position;
		
	}
	
	public void FaceCamera(Matrix4f view){
		
		transformation.setIdentity();
		transformation.translate(position);
		transformation.m00 = view.m00;
		transformation.m01 = view.m10;
		transformation.m02 = view.m20;
		transformation.m10 = view.m01;
		transformation.m11 = view.m11;
		transformation.m12 = view.m21;
		transformation.m20 = view.m02;
		transformation.m21 = view.m12;
		transformation.m22 = view.m22;
		transformation.rotate((float) Math.toRadians(-90), MathHelper.Y);
		transformation.scale(entity.scale);
		
	}
	
}
