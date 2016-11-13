package game.tools;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MathHelper {

	//Defining random, X axis, and Y axis
	public static final Vector3f X = new Vector3f(1, 0, 0);
	public static final Vector3f Y = new Vector3f(0, 1, 0);
	public static final Random RANDOM = new Random(4237362089L);
	
	//Generates a simplex noise field
	public static float[][] GetNoise(OpenSimplexNoise generator, float smoothness, int SX, int SY){
		
		float[][] noise = new float[SX][SY];
		
		for(int x = 0; x < SX; x++){
			for(int y = 0; y < SY; y++){
				double val = generator.eval(x / smoothness, y / smoothness);
				val += generator.eval(x / smoothness / 2, y / smoothness / 2) / 2;
				val += generator.eval(x / smoothness / 4, y / smoothness / 4) / 4;
				noise[x][y] = (float) val;
			}
		}
		
		return noise;
		
	}
	
	//Gets interpolated 2D grid value
	public static float GetValue(float[][] grid, float x, float y){
		
		int X = (int) x;
		int Y = (int) y;
		float fX = x - X;
		float fY = y - Y;
//		float v1 = GetOppositeValue(grid, X, Y);
//		float v2 = GetOppositeValue(grid, X + 1, Y);
//		float v3 = GetOppositeValue(grid, X, Y + 1);
//		float v4 = GetOppositeValue(grid, X + 1, Y + 1);
		float v1 = grid[X][Y];
		float v2 = grid[X + 1][Y];
		float v3 = grid[X][Y + 1];
		float v4 = grid[X + 1][Y + 1];
		float i1 = Interpolate(v1, v2, fX);
		float i2 = Interpolate(v3, v4, fX);
		return Interpolate(i1, i2, fY);
		
	}
	
	//Gets interpolated value based on blend factor
	public static float Interpolate(float a, float b, float blend){
		
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
		
	}
	
	//Gets barry centric interpolating value
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
		
	}
	
	//Smoothes grid and returns self, smoothness variable effects how far away the sample points are chosen
	public static float[][] SmoothGrid(float[][] grid, int smoothness){
		
		float[][] smooth_grid = new float[grid.length][grid.length];
		
		for(int x = 0; x < grid.length; x++){
			for(int y = 0; y < grid.length; y++){
				smooth_grid[x][y] = Average9(grid, x, y, smoothness);
			}
		}
		
		return smooth_grid;
		
	}
	
	//Currently unused
//	public static float[][] SmoothGrid(float[][] grid, int smoothness, int iterations){
//		
//		float[][] smooth_grid = grid;
//		
//		for(int i = 0; i < iterations; i++){
//			smooth_grid = SmoothGrid(smooth_grid, smoothness);
//		}
//		
//		return smooth_grid;
//		
//	}
	
	//Returns the weighted average of 9 sample points centered at x, y. Smoothness variable defines how far away the surrounding 8 sample points are from the center
	private static float Average9(float[][] grid, int x, int y, int smoothness){
		
		float corners = (GetValue(grid, x - smoothness, y - smoothness) + GetValue(grid, x + smoothness, y + smoothness) + GetValue(grid, x + smoothness, y - smoothness) + GetValue(grid, x - smoothness, y + smoothness)) / 16f;
		float edges = (GetValue(grid, x + smoothness, y) + GetValue(grid, x - smoothness, y) + GetValue(grid, x, y + smoothness) + GetValue(grid, x, y - smoothness)) / 8f;
		float center = GetValue(grid, x, y) / 4f;
		return corners + edges + center;
		
	}
	
	//Returns 2D grid value if in bounds, else returns 0
	private static float GetValue(float[][] grid, int x, int y){
		
		int XN = x;
		int YN = y;
		if(x < 0){
			XN = 0;
		}else if(x >= grid.length){
			XN = grid.length - 1;
		}
		if(y < 0){
			YN = 0;
		}else if(y >= grid.length){
			YN = grid.length - 1;
		}
		return grid[XN][YN];
		
	}
	
//	private static float GetValue0(float[][] grid, int x, int y){
//		
//		if(x < 0 || x >= grid.length || y < 0 || y >= grid.length){
//			return 0;
//		}
//		return grid[x][y];
//		
//	}
	
	//Returns the distance in between two 3D points
	public static float distance(Vector3f v1, Vector3f v2){
		
		float dx = v1.x - v2.x;
		float dy = v1.y - v2.y;
		float dz = v1.z - v2.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		
	}

	//Returns the distance in between two 2D points
	public static float distance(Vector2f v1, Vector2f v2){
	
		float dx = v1.x - v2.x;
		float dy = v1.y - v2.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	
	}
	
	//Returns random value in between -1 and 1
	public static float nextFloat(Random random){
		
		return (random.nextFloat() * 2) - 1;
		
	}
	
	//Returns random value in between 0 and max
	public static float nextFloat(float max, Random random){
		
		return random.nextFloat() * max;
		
	}
	
	//Returns random value in between min and max
	public static float nextFloat(float min, float max, Random random){
		
		return (random.nextFloat() * (max-min)) + min;
		
	}
	
}
