package rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Window {
	
	//NO COMMENTS BECAUSE YOU KNOW HOW ALL OF THIS WORKS
	
	private static long LastFrameTime;
    private static float Delta;
    private static int FPS;
	
	public static void Create(int fps){
		
		ContextAttribs attribs = new ContextAttribs(3, 2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		try {
			Display.create(new PixelFormat(), attribs);
		    Display.setTitle("Zachium Gay Adventure");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		LastFrameTime = GetCurrentTime();
		fps = FPS;
		
	}
	
	public static float GetDelta(){
		
		return Delta;
		
	}
	
	private static long GetCurrentTime(){
		
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
        
    }
	
	public static void Update(){
		
		long currentFrameTime = GetCurrentTime();
		Delta = (currentFrameTime - LastFrameTime) / 1000f;
		LastFrameTime = currentFrameTime;
		Display.sync(FPS);
		Display.update();
		
	}
	
	public static boolean IsCloseRequested(){
		
		return Display.isCloseRequested();
		
	}
	
	public static void Destroy(){
		
		Display.destroy();
		System.exit(-1);
		
	}
	
}
