package rendering.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Shader {
	
	//YOU KNOW HOW SHADER WORK NOOB
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	private static final String PREFIX = "/rendering/shader/";
	private static final String VERTEX = ".vs";
	private static final String FRAGMENT = ".fs";
	
	public Shader(String name, boolean hasGeometryShader){
		
		vertexShaderID = loadShader(PREFIX + name + VERTEX, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(PREFIX + name + FRAGMENT, GL20.GL_FRAGMENT_SHADER);
//		if(hasGeometryShader){
//			geometryShaderID = loadShader(PREFIX + name + GEOMETRY, GL32.GL_GEOMETRY_SHADER);
//		}
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
//		if(hasGeometryShader){
//			GL20.glAttachShader(programID, geometryShaderID);
//		}
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
		
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName){
		
		return GL20.glGetUniformLocation(programID, uniformName);
		
	}
	
	public void start(){
		
		GL20.glUseProgram(programID);
		
	}
	
	public void stop(){
		
		GL20.glUseProgram(0);
		
	}
	
	public void cleanUp(){
		
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
//		if(geometryShaderID != 1337){
//			GL20.glDetachShader(programID, geometryShaderID);
//		}
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
//		if(geometryShaderID != 1337){
//			GL20.glDeleteShader(geometryShaderID);
//		}
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
		
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName){
		
		GL20.glBindAttribLocation(programID, attribute, variableName);
		
	}
	
	protected void loadFloat(int location, float value){
		
		GL20.glUniform1f(location, value);
		
	}
	
	protected void loadVector3(int location, Vector3f vector){
		
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
		
	}
	
	protected void loadBoolean(int location, boolean value){
		
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
		
	}
	
	protected void loadMatrix(int location, Matrix4f matrix){
		
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
		
	}
	
	private static int loadShader(String file, int type){
		
		StringBuilder shaderSource = new StringBuilder();
		try{
			InputStream in = Class.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		return shaderID;
		
	}
	
}
