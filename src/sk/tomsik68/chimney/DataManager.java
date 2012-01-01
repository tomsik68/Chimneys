/*    This file is part of Chimneys.

    Chimneys is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Chimneys is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Chimneys.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.chimney;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {
	public static final File dataFile = new File("plugins" + File.separator + "Chimneys" + File.separator + "data.bin");

	public static <T extends Object> T load() {
		if (!dataFile.exists()) {
			System.out.println("[Chimneys] Data File not found.");
			return null;
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
			@SuppressWarnings("unchecked")
			T result = (T) ois.readObject();
			ois.close();
			save(result);
			return result;
		} catch (Exception e) {
			System.out.println("= CHIMNEYS: LOADING ERROR =");
			e.printStackTrace();
			System.out.println("= END ERROR=");
		}
		return null;
	}

	public static <T extends Object> void save(T chimneys) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
			oos.writeObject(chimneys);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			System.out.println("= CHIMNEYS: SAVING ERROR =");
			e.printStackTrace();
			System.out.println("= END ERROR=");
		}
	}

}
