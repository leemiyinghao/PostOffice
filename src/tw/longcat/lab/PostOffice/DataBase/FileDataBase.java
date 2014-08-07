package tw.longcat.lab.PostOffice.DataBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class FileDataBase {
	private File dbFile;
	
	HashMap<String, String> dbContext;
	public FileDataBase(String fileName){
		dbFile = new File(fileName);
		dbContext = new HashMap<String, String>();
		load();
	}
	public boolean load(){
		if(dbFile.exists()){
			BufferedReader br = null;
			String line;
			try {
				br = new BufferedReader(new FileReader(dbFile));
				while((line = br.readLine()) != null){
					String[] arr = line.split(":");
					if(arr.length!=2){
						br.close();
						throw new LineErrorException();
					}
					dbContext.put(arr[0], arr[1]);
				}
				br.close();
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			} catch (LineErrorException e) {
				return false;
			}
			return true;
		}
		if(initFile()){
			dbContext.clear();
			return true;
		}else{
			return false;
		}
	}
	public boolean initFile(){
		try {
			dbFile.createNewFile();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	public boolean write(){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(dbFile));
			String text = "";
			for(Entry<String, String> entry : dbContext.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    text += key + ":" + value + "\n";
			}
			bw.write(text);
			bw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	public String getValue(String key){
		return dbContext.get(key);
	}
	public String[] searchInnerValue(String innerValue){
		for(Entry<String, String> entry : dbContext.entrySet()) {
			if(entry.getValue().contains(innerValue+",")){
				String[] temp = {entry.getKey(),entry.getValue()};
				return temp;
			}
		}
		return null;
	}
	public boolean setValue(String key,String value){
		dbContext.put(key, value);
		write();
		load();
		return true;
	}
	public boolean clearRow(String key){
		dbContext.remove(key);
		write();
		load();
		return true;
	}
	public boolean hasKey(String key){
		return dbContext.containsKey(key);
	}
	public boolean hasValue(String value){
		return dbContext.containsKey(value);
	}
}
