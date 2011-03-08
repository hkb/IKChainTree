package misc;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Toolbox {
	public static void writeToFile(String contents, String fName, boolean append){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(fName, append));
			out.write(contents);
			out.close();
		}catch (Exception e){//Catch exception if any
			e.printStackTrace();
		}

	}

	public static String readFromFile(String fName){
		try{
			StringBuilder sb = new StringBuilder();

			BufferedReader in = new BufferedReader(new FileReader(fName));
			String line;
			while( (line=in.readLine())!=null ){
				sb.append(line);
				sb.append('\n');
			}

			return sb.toString();
		}catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}

	public static String readFromInternalFile(String fName) throws Error{ 
		try{
			StringBuilder sb = new StringBuilder();
			InputStream input = Toolbox.class.getResourceAsStream("/"+fName);
			if(input==null) throw new Error("No such resource");
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			String line;
			while( (line=in.readLine())!=null ){
				sb.append(line);
				sb.append('\n');
			}

			return sb.toString();
		}catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}

	
	
	public static void writeSerializedFile(Serializable obj, String fName){
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fName));
				oos.writeObject(obj);
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public static Object readSerializedFile(String fName){
		Object o = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fName));
			o = ois.readObject();
			ois.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		return o;
	}
	
	
	
}
