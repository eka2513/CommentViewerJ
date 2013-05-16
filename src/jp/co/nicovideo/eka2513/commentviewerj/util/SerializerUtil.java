package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializerUtil<T> {

	public void save(String filename, T object ) {
		try {
			FileOutputStream outFile = new FileOutputStream(filename);
			ObjectOutputStream outObject = new ObjectOutputStream(outFile);
			outObject.writeObject(object);
			outObject.close();
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public T load(String filename) {
		try {
			FileInputStream inFile = new FileInputStream(filename);
			ObjectInputStream inObject = new ObjectInputStream(inFile);
			@SuppressWarnings("unchecked")
			T object = (T) inObject.readObject();
			inObject.close();
			inFile.close();
			return object;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
