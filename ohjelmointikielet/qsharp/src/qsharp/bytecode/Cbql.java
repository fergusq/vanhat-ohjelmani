package qsharp.bytecode;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Cbql {

	public ArrayList<DBNode> database = new ArrayList<DBNode>();
	
	public static final int LOAD = 8;
	public static final int JMP= 16;
	public static final int IN = 4;
	public static final int OUT = 2;
	public static final int IFEQ = 32;
	public static final int IFNE = 64;
	public static final int ADD = 42;
	public static final int SUB = 80;
	public static final int MOV = 6;
	
	public static final int REG_A = 1;
	public static final int REG_B = 3;
	public static final int REG_C = 5;
	public static final int REG_D = 7;
	
	public static final int REG_E = 9;
	public static final int REG_F = 11;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cbql l = new Cbql();
		l.compile(args[0]);

	}

	private void compile(String file) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(new File(file)));
		
			String line = "";
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			while((line = r.readLine()) != null) {
				String[] params = line.trim().split(" ");
				if (line.startsWith("db")) {
					database.add(new DBNode(Integer.parseInt(params[1].trim()), 
							parseString(line.substring(params[0].length() + params[1].length()))));
				} else if (line.startsWith(".load")) {
					out.write(LOAD);
					if (params[1].trim().matches("[0-9]*")) out.write(Integer.parseInt(params[1].trim()));
					else {
						loadReg(out, params[1]);
					}
				} else if (line.startsWith(".jmp")) {
					out.write(JMP);
					if (params[1].trim().matches("[0-9]*")) out.write(Integer.parseInt(params[1].trim()));
					else {
						loadReg(out, params[1]);
					}
				} 
				else if (line.startsWith(".mov")) {
					out.write(MOV);
					if (params[1].trim().matches("[0-9]*")) out.write(Integer.parseInt(params[1].trim()));
					else {
						loadReg(out, params[1]);
					}
					loadReg(out, params[2]);
				}
			}
		} catch (Exception e) {
		}
		
	}

	private void loadReg(ByteArrayOutputStream out2, String string) {
		if (string.trim().equals("a")) {
			out2.write(REG_A);
		}
		if (string.trim().equals("b")) {
			out2.write(REG_B);
		}
		if (string.trim().equals("c")) {
			out2.write(REG_C);
		}
		if (string.trim().equals("d")) {
			out2.write(REG_D);
		}
		if (string.trim().equals("e")) {
			out2.write(REG_E);
		}
		if (string.trim().equals("f")) {
			out2.write(REG_F);
		}
		
	}

	private String parseString(String str) {
		str = str.trim();
		if (str.startsWith("\"") && str.endsWith("\"")) {
			str = str.substring(1, str.length()-2);
		}
		return str;
	}

}
