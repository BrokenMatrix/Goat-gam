package game.world;

import java.util.ArrayList;
import java.util.List;

import game.tools.MathHelper;
import game.tools.MidpointDisplacement;
import game.tools.OpenSimplexNoise;

public class World {
	
	//Defining chunk count
	public static final int CHUNKS = 16;
	
	public static Chunk[][] Chunks;
	public static List<Chunk> Visible;
	
	public static void Create(){
		
		//Generating island height map
		float[][] map = MidpointDisplacement.GetMap(Chunk.VERTEX_COUNT * CHUNKS, 1.1f, MathHelper.RANDOM);
		//Smoothing island height map
		map = MathHelper.SmoothGrid(map, 1);
		map = MathHelper.SmoothGrid(map, 2);
		map = MathHelper.SmoothGrid(map, 4);
		//Generating 2 simplex noise maps
		float[][] noise = MathHelper.GetNoise(new OpenSimplexNoise(4237362089L), 5f, Chunk.VERTEX_COUNT * CHUNKS + 1, Chunk.VERTEX_COUNT * CHUNKS + 1);
		float[][] noise2 = MathHelper.GetNoise(new OpenSimplexNoise(1337L), 20f, Chunk.VERTEX_COUNT * CHUNKS + 1, Chunk.VERTEX_COUNT * CHUNKS + 1);
		
		//Generating chunks
		Chunks = new Chunk[CHUNKS][CHUNKS];
		for(int x = 0; x < CHUNKS; x++){
			for(int y = 0; y < CHUNKS; y++){
				Chunks[x][y] = new Chunk(x, y, map, noise, noise2);
			}
		}
		//Initializing visible chunks list
		Visible = new ArrayList<Chunk>();
		
	}
	
	public static void Update(){
		
		//Calculating visible chunks
		//TODO: ACTUALLY F***ING CALCULATE THEM!!!
		Visible.clear();
		for(int x = 0; x < CHUNKS; x++){
			for(int y = 0; y < CHUNKS; y++){
				Visible.add(Chunks[x][y]);
			}
		}
		
	}
	
}
