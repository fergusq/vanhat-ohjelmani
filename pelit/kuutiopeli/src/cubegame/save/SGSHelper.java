package cubegame.save;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SGSHelper {
	
	String file;
	
	public SGSHelper(String filename) {
		file = filename;
	}
	
	public void writeByte(byte b, String name, String identifier) throws FileNotFoundException, IOException {
		GZIPOutputStream outGZIP = new GZIPOutputStream(new FileOutputStream(new File(file)));
		GZIPInputStream inGZIP = new GZIPInputStream(new FileInputStream(new File(file)));
		DataOutputStream out = new DataOutputStream(outGZIP);
		DataInputStream in = new DataInputStream(inGZIP);
		
		byte[] ident = identifier.getBytes();
		byte[] s = new byte[ident.length];
		int i = 0;
		boolean isSearch = true;
		boolean isRead = false;
		boolean found = false;
		
		while (isSearch || isRead){
			if (!isRead){
				if (in.readInt() == 10){
					isSearch = false;
					isRead = true;
				}
			}
			
			if (isRead){
				String rByte = in.readUTF();
				if (rByte.equals(identifier)){
					if (in.readUTF().equals(name)){
						
					}
				}
			}
		}
		
		if (found){
			
		}
	}
	
}
