package game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import game.tools.MathHelper;
import game.world.Chunk;
import game.world.World;
import rendering.Window;

public class Player {
	
	//Defining movement variables
	private static final float SPEED = 2.5f;
	private static final float RUNNING_SPEED = 12.4f / SPEED;
	private static final float GRAVITY = -9.8f;
	private static final float JUMP_FORCE = 6f;
	
	public static Matrix4f ViewMatrix;
	public static Vector3f Position;
	
	private static float Speed;
	private static Vector3f NegativePosition;
	private static Vector3f Velocity;
	private static float Pitch;
	private static float Yaw;
	
	public static void Create(){
		
		//Initializing view matrix, position, negative position, and velocity variables
		ViewMatrix = new Matrix4f();
		Position = new Vector3f(512 / 2, 40, 1024 / 2);
		NegativePosition = new Vector3f();
		Velocity = new Vector3f();
		
	}
	
	public static void Update(){
		
		//Checking input to get speed
		Speed = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			Speed = SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			Speed = -SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			Speed *= RUNNING_SPEED;
		}
		
		//Checking mouse input for rotation
		if(Mouse.isGrabbed()){
			Yaw -= Mouse.getDX() / 30f;
			Pitch += Mouse.getDY() / 30f;
		}
		
		//Moving player based on speed
		float distance = Speed * Window.GetDelta();
		Position.x -= distance * Math.sin(Math.toRadians(Yaw));
		Position.z -= distance * Math.cos(Math.toRadians(Yaw));
		
		//Getting current chunk
		int CX = (int) (Position.x / Chunk.SIZE);
		int CZ = (int) (Position.z / Chunk.SIZE);
		if(CX < 0){
			CX = 0;
		}
		if(CX > World.CHUNKS - 1){
			CX = World.CHUNKS - 1;
		}
		if(CZ < 0){
			CZ = 0;
		}
		if(CZ > World.CHUNKS - 1){
			CZ = World.CHUNKS - 1;
		}
		//Getting height of current chunk
		float height = World.Chunks[CX][CZ].getHeightOfTerrain(Position.x, Position.z) + 1.175f;
		//Applying gravity
		Velocity.y += GRAVITY * Window.GetDelta();
		//Checking input for pausing velocity for debugging
		if(!Keyboard.isKeyDown(Keyboard.KEY_1)){
			//Applying velocity
			Position.x += Velocity.x * Window.GetDelta();
			Position.y += Velocity.y * Window.GetDelta();
			Position.z += Velocity.z * Window.GetDelta();
		}
		//Checking for terrain collision
		if(Position.y < height){
			Position.y = height;
			Velocity.y = 0;
			//Checking input for jumping
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				Velocity.y += JUMP_FORCE;
			}
		}
		
		//Checking input for mouse grabbing
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB)){
			Mouse.setGrabbed(false);
		}else{
			Mouse.setGrabbed(true);
		}
		
		//Calculating view matrix
		ViewMatrix.setIdentity();
		NegativePosition.x = -Position.x;
		NegativePosition.y = -Position.y;
		NegativePosition.z = -Position.z;
		ViewMatrix.rotate((float) Math.toRadians(-Pitch), MathHelper.X);
		ViewMatrix.rotate((float) Math.toRadians(-Yaw), MathHelper.Y);
		ViewMatrix.translate(NegativePosition);
		
	}
	
}
