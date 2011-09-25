package sk.tomsik68.chimney;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {
	public static final File dataFile = new File("plugins"+File.separator+"Chimneys"+File.separator+"data.bin");
	public static <T extends Object> T load(){
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
			@SuppressWarnings("unchecked")
			T result = (T) ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {
		}
		return null;
	}
	public static <T extends Object> void save(T obj){
		try{
			if(!dataFile.exists()){
				new File("plugins\\Chimneys").mkdir();
				dataFile.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
