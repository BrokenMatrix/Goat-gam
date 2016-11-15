package game.world;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import game.entities.Entities;
import game.entities.Entity;
import game.entities.EntityInstance;
import game.tools.MathHelper;
import game.tools.TextureLoader;
import game.tools.VertexObjectHelper;

public class Chunk {
	
	//Defining all sizes for terrain components
	public static final float SIZE = 64;
	public static final float SIZE2 = SIZE / 2;
	public static final int VERTEX_COUNT = 16;
	private static final int VERTEX_COUNT_MINUS_ONE = VERTEX_COUNT - 1;
	private static final int TEXTURE_SIZE = 64;
	private static final float TEXTURE_RATIO = (float) TEXTURE_SIZE / (float) VERTEX_COUNT_MINUS_ONE;
	
	//Defining terrain colors
	private static final int GRASS_LESS_LIGHT = new Color(88, 186, 10).getRGB();
	private static final int GRASS_LIGHT = new Color(98, 176, 10).getRGB();
	private static final int GRASS_EXTRA_LIGHT = new Color(128, 196, 10).getRGB();
	
	private static final int GRASS_LESS_DARK = new Color(58, 156, 10).getRGB();
	private static final int GRASS_DARK = new Color(58, 136, 10).getRGB();
	private static final int GRASS_EXTRA_DARK = new Color(58, 106, 10).getRGB();
	
	private static final int SAND = new Color(239, 209, 76).getRGB();
	private static final int SAND_LIGHT = new Color(249, 219, 86).getRGB();
	
	private static final int WATER = new Color(0, 18, 73).getRGB();
	
	public int vao;
	public int vertex_count;
	public int texture;
	public Matrix4f transformation;
	public HashMap<Entity, List<EntityInstance>> entities;
	
	private float[][] heights;
	private float x,z;
	
	public Chunk(int X, int Z, float[][] map, float[][] noise, float[][] noise2){
		
		//Creating transformation matrix
		this.transformation = new Matrix4f();
		Matrix4f.translate(new Vector3f(X * SIZE, 0f, Z * SIZE), transformation, transformation);
		//Generating chunk geometry
		int[] info = GenerateChunk(X * VERTEX_COUNT_MINUS_ONE, Z * VERTEX_COUNT_MINUS_ONE, X * VERTEX_COUNT, Z * VERTEX_COUNT, map);
		this.vao = info[0];
		this.vertex_count = info[1];
		//Generating chunk texture
		this.texture = GenerateTexture(map, noise, noise2, X * VERTEX_COUNT_MINUS_ONE, Z * VERTEX_COUNT_MINUS_ONE);
		//Initializing terrain location
		x = X * SIZE;
		z = Z * SIZE;
		//Initializing entity map
		entities = new HashMap<Entity, List<EntityInstance>>();
		GenerateEntities(X, Z);
		
	}
	
	private void GenerateEntities(int X, int Z){
		
		entities.put(Entities.bisp_star, new ArrayList<EntityInstance>());
		entities.put(Entities.effle_stalk, new ArrayList<EntityInstance>());
		Entity[] objects = new Entity[]{Entities.bisp_star, Entities.effle_stalk};
		for(int i = 0; i < MathHelper.RANDOM.nextInt(10*objects.length); i++){
			float x = X * SIZE + MathHelper.nextFloat(SIZE, MathHelper.RANDOM);
			float z = Z * SIZE + MathHelper.nextFloat(SIZE, MathHelper.RANDOM);
			float y = getHeightOfTerrain(x, z);
			if(y > 2){
				Entity entity = objects[MathHelper.RANDOM.nextInt(objects.length)];
				entities.get(entity).add(new EntityInstance(entity, new Vector3f(x, y, z)));
			}
		}
		
	}
	
	private int GenerateTexture(float[][] map, float[][] noise, float[][] noise2, int X, int Y){
		
		//Creating buffered image
		BufferedImage texture = new BufferedImage(TEXTURE_SIZE, TEXTURE_SIZE, BufferedImage.TYPE_INT_ARGB);
		
		//Generating texture
		for(int x = 0; x < TEXTURE_SIZE; x++){
			for(int y = 0; y < TEXTURE_SIZE; y++){
				//Getting height map variables
				float height = MathHelper.GetValue(map, x / TEXTURE_RATIO + X, y / TEXTURE_RATIO + Y);
				float noise_height = 1f - Math.abs(MathHelper.GetValue(noise, x / TEXTURE_RATIO + X, y / TEXTURE_RATIO + Y));
				float noise_height2 = 1f - Math.abs(MathHelper.GetValue(noise2, x / TEXTURE_RATIO + X, y / TEXTURE_RATIO + Y));
				//Calculating color
				int color = WATER;
				if(height > 2){
					if(noise_height > 0.8f && noise_height2 > 0.6f){
						color = GRASS_LESS_DARK;
					}else if(noise_height > 0.5f && noise_height2 > 0.5f){
						color = GRASS_DARK;
					}else if(noise_height > 0.3f && noise_height2 > 0.4f){
						color = GRASS_EXTRA_DARK;
					}else if(noise_height > 0.8f && noise_height2 > 0.3f){
						color = GRASS_LESS_LIGHT;
					}else if(noise_height > 0.5 && noise_height2 > 0.1f){
						color = GRASS_EXTRA_LIGHT;
					}else{
						color = GRASS_LIGHT;
					}
				}else if(height > 0){
					color = SAND;
					if(noise_height > 0.8f){
						color = SAND_LIGHT;
					}
				}
				//Filling in pixel
				texture.setRGB(x, y, color);
			}
		}
		
		return TextureLoader.LoadImage(texture);
		
	}
	
	private int[] GenerateChunk(int X, int Z, int X1, int Z1, float[][] map){
		
		//Initializing variables
		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3];
		float[] texture_coordinates = new float[count * 2];
		int[] indices = new int[6 * VERTEX_COUNT_MINUS_ONE * VERTEX_COUNT_MINUS_ONE];
		int index = 0;
		//Calculating geometry
		for(int i = 0; i < VERTEX_COUNT; i++){
			for(int j = 0; j < VERTEX_COUNT; j++){
				//Getting height map value and limiting to 0
				float height = map[j + X][i + Z];
				if(map[j + X][i + Z] < 0){
					height = 0;
				}
				heights[j][i] = height;
				
				//Calculating Vertices
				vertices[index * 3] = (float)j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[index * 3 + 1] = height;
				vertices[index * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				
				//Calculating texture coordinates
				texture_coordinates[index * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				texture_coordinates[index * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				
				index++;
			}
		}
		//Calculating indices
		index = 0;
		for(int z = 0; z < VERTEX_COUNT - 1; z++){
			for(int x = 0; x < VERTEX_COUNT - 1; x++){
				int UL = (z * VERTEX_COUNT) + x;
				int UR = UL + 1;
				int LL = ((z + 1) * VERTEX_COUNT) + x;
				int LR = LL + 1;
				indices[index++] = UL;
				indices[index++] = LL;
				indices[index++] = UR;
				indices[index++] = UR;
				indices[index++] = LL;
				indices[index++] = LR;
			}
		}
		return VertexObjectHelper.LoadToVAO(vertices, texture_coordinates, indices);
		
	}
	
	public float getHeightOfTerrain(float x, float z){
		
		float terrainX = x - this.x;
		float terrainZ = z - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0){
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = MathHelper.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0,heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = MathHelper.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
		
	}
	
	public float getLocalHeightOfTerrain(float x, float z){
		
		float terrainX = x;
		float terrainZ = z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = MathHelper.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0,heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = MathHelper.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
		
	}
	
}
