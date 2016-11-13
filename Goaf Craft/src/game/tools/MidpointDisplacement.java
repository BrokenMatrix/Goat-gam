package game.tools;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

public class MidpointDisplacement {
	
	//Generates fractal noise island map using midpoint displacement algorithm
	//TODO: FURTHER COMMENTING NEEDED
	public static float[][] GetMap(int size, float smoothness, Random generator){
		
		float[][] map = new float[size+1][size+1];
		
		int step = size / 32;
		float sum;
		int count;
		float roughness = 1;
		for(int i = 0; i < size + 1; i += step * 2){
			for(int j = 0; j < size + 1; j += step * 2){
				if(MathHelper.distance(new Vector2f(i,j), new Vector2f(size/2,size/2)) < 100){
					map[i][j] = MathHelper.nextFloat(roughness*15, generator);
				}else{
					map[i][j] = MathHelper.nextFloat(roughness*-15, generator);
				}
			}
		}
		
		
		while(step > 0){
			for(int x = step; x < size + 1; x += 2 * step){
				for(int y = step; y < size + 1; y += 2 * step){
					sum = map[x - step][y-step] +
							map[x-step][y+step] +
							map[x+step][y-step] +
							map[x+step][y+step];
					map[x][y] = sum / 4 + MathHelper.nextFloat(-roughness, roughness, generator);
				}
			}
			
			for(int x = 0; x < size + 1; x += step) {
				for(int y = step * (1 - (x/step)%2); y < size + 1; y += 2 * step){
					sum = 0;
					count = 0;
					if(x - step >= 0){
						sum += map[x - step][y];
						count++;
					}
					if(x + step < size + 1){
						sum += map[x + step][y];
						count++;
					}
					if(y - step >= 0){
						sum += map[x][y - step];
						count++;
					}
					if(y + step < size + 1){
						sum += map[x][y + step];
						count++;
					}
					if(count > 0) map[x][y] = sum / count + MathHelper.nextFloat(-roughness, roughness, generator);
					else map[x][y] = 0;
				}
			}
			roughness /= smoothness;
			step /= 2;
		}
		
		return map;
		
	}
	
}
