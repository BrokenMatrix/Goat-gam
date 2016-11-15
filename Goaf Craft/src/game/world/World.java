package game.world;

import java.util.ArrayList;
import java.util.List;

import game.entities.Player;
import game.tools.MathHelper;
import game.tools.MidpointDisplacement;
import game.tools.OpenSimplexNoise;

public class World {
	
	//Defining chunk count
	public static final int CHUNKS = 16;
	private static final int VIEW_DISTANCE = 8;
	private static final int VIEW_DISTANCE2 = VIEW_DISTANCE / 2;
	private static final int VIEW_DISTANCE21 = VIEW_DISTANCE2 + 1;
	
	public static Chunk[][] Chunks;
	public static List<Chunk> Visible;
	
	public static void Create(){
		
		//Generating island height map
		float[][] map = MidpointDisplacement.GetMap(Chunk.VERTEX_COUNT * CHUNKS, 1.1f, MathHelper.RANDOM);
		//Smoothing island height map
//		map = MathHelper.SmoothGrid(map, 1);
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
		Visible.clear();
		for(int x = -VIEW_DISTANCE2; x < VIEW_DISTANCE21; x++){
			for(int y = -VIEW_DISTANCE2; y < VIEW_DISTANCE21; y++){
				int CX = (int) Math.round((Player.Position.x - Chunk.SIZE2 - (x * Chunk.SIZE)) / Chunk.SIZE);
				int CZ = (int) Math.round((Player.Position.z - Chunk.SIZE2 - (y * Chunk.SIZE)) / Chunk.SIZE);
				if(!(CX < 0 || CZ < 0 || CX > CHUNKS - 1 || CZ > CHUNKS - 1)){
					Visible.add(Chunks[CX][CZ]);
				}
			}
		}
//		int CX = (int) (Player.Position.x / Chunk.SIZE);
//		int CZ = (int) (Player.Position.z / Chunk.SIZE);
//		for(int x = 0; x < CHUNKS; x++){
//			for(int y = 0; y < CHUNKS; y++){
//				Visible.add(Chunks[x][y]);
//			}
//		}
		
	}
	
}
