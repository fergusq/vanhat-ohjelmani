package org.kaivos.sc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class SCompiler {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Usage: java org.kaivos.sc.SCompiler <file> <target>");
			return;
		}
		
		String filen = args[0];
		File file = new File(filen);
		TokenScanner s = null;
		
		ArrayList<String> ppKeys = new ArrayList<String>();
		ArrayList<String> ppValues =  new ArrayList<String>();
		
		BufferedReader r = new BufferedReader(new FileReader(file));
		String code_str = "", code_str2 = "", cstr = "";

		code_str2 = parseIncludes(r);
		r = new BufferedReader(new StringReader(code_str2));
		while ((cstr = r.readLine()) != null) {
			String tmp = cstr;
			if (cstr.indexOf(';', tmp.lastIndexOf('\"')) != -1) {
				tmp = cstr.substring(0, cstr.indexOf(';', tmp.lastIndexOf('\"')));
			}
			if (tmp.startsWith("!")) {
				int space = tmp.indexOf(' ');
				if (space == -1 || space > tmp.indexOf('\t') && tmp.indexOf('\t') > 0) space = tmp.indexOf('\t');
				String name = "&" + tmp.substring(1, space).trim();
				String value = " " + tmp.substring(space).trim() + " ";
				ppKeys.add(name);
				ppValues.add(value);
				code_str += "\n";
			}
			else code_str += "\n" + tmp;
		}
		
		Collections.reverse(ppKeys);
		Collections.reverse(ppValues);
		
		for (int in1 = 0; in1 < ppKeys.size(); in1++) {
			//System.out.println("'" + ppKeys.get(in1) + "' = '" +  ppValues.get(in1) + "'");
			code_str = code_str.replace(((String) ppKeys.get(in1)).trim(), (String) ppValues.get(in1));
		}
		
		int lindex = 0;
		int gvars = 0;
		boolean parse = true;
		
		while (true) {
			int pos = -1; //code_str.indexOf('\"', lindex);
			if (parse) {
				
				int breakc = 0;
				int nextPP = code_str.indexOf(OPCODE.pp_op[OPCODE.PP_INITS], lindex);
				if (nextPP == -1) breakc++;
				if (nextPP != -1 && (nextPP < pos || pos == -1)) {
					
					int s1 = code_str.indexOf('(', nextPP);
					if (s1 == -1) {
						System.err.println("Syntax error near '" + OPCODE.pp_op[OPCODE.PP_INITS] + "'. '(' excepted!"); 
						break;
					}
					int s2 = code_str.indexOf(')', s1);
					if (s2 == -1) {
						System.err.println("Syntax error near '" + OPCODE.pp_op[OPCODE.PP_INITS] + "'. ')' excepted!"); 
						break;
					}
					String params = code_str.substring(s1+1, s2);
					params = params.replace(" ", "");
					int nro = Integer.parseInt(params);
					String s3 = "";
					for (int i = 0; i < nro; i++) {gvars++; s3 += " 0 ";};
					code_str = code_str.replace(code_str.substring(nextPP, s2+1), s3);
					continue;
				}
				nextPP = code_str.indexOf(OPCODE.pp_op[OPCODE.PP_DUPC], lindex);
				
				if (nextPP == -1) breakc++;
				if (nextPP != -1 && (nextPP < pos || pos == -1)) {
					
					int s1 = code_str.indexOf('(', nextPP);
					if (s1 == -1) {
						System.err.println("Syntax error near '" + OPCODE.pp_op[OPCODE.PP_DUPC] + "'. '(' excepted!"); 
						break;
					}
					int s2 = code_str.indexOf(')', s1);
					if (s2 == -1) {
						System.err.println("Syntax error near '" + OPCODE.pp_op[OPCODE.PP_DUPC] + "'. ')' excepted!"); 
						break;
					}
					String params = code_str.substring(s1+1, s2);
					params = params.replace(" ", "");
					int nro = Integer.parseInt(params)+1;
					String s3 = " ";
					for (int i = 0; i < nro; i++) {s3 += "rotl ";};
					s3 += "dup ";
					for (int i = 0; i < nro; i++) {s3 += "exch rotr ";};
					code_str = code_str.replace(code_str.substring(nextPP, s2+1), s3);
					continue;
				}
				nextPP = code_str.indexOf(OPCODE.pp_op[OPCODE.PP_SECV], lindex);
				if (nextPP == -1) breakc++;
				if (nextPP != -1 && (nextPP < pos || pos == -1)) {
					int s1 = code_str.indexOf('(', nextPP);
					if (s1 == -1) {
						System.err.println("Syntax error near '" + OPCODE.pp_op[OPCODE.PP_SECV] + "'. '(' excepted!"); 
						break;
					}
					int s2 = code_str.indexOf(')', s1);
					if (s2 == -1) {
						System.err.println("Syntax error near '" + OPCODE.pp_op[OPCODE.PP_SECV] + "'. ')' excepted!"); 
						break;
					}
					String params = code_str.substring(s1+1, s2);
					params = params.replace(" ", "");
					int nro = Integer.parseInt(params/*.split(",")[0]*/)+1;
					//int nro1 = Integer.parseInt(params.split(",")[1]);
					String s3 = " ";//" " + nro1 + " ";
					for (int i = 0; i < nro; i++) {s3 += "rotl exch ";};
					s3 += "exch pop ";
					for (int i = 0; i < nro; i++) {s3 += "rotr ";};
					code_str = code_str.replace(code_str.substring(nextPP, s2+1), s3);
					
					continue;
				}
				if (breakc >= 3) {
					break;
				}
			}
			
			if (pos != -1) {
				//parse = !parse;
				//lindex = pos+1;
				
			} else break;
		}
		
		//System.out.println(code_str);
		
		s = new TokenScanner();
		s.setSpecialTokens(new char[]{});
		s.init(code_str);
		
		String filen2 = args[1];
		File file2 = new File(filen2);
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(file2));
		} catch (FileNotFoundException e) {e.printStackTrace(); return;
		}
		
		out.write(new byte[]{'S', 'B', 'C'});
		
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
		
		int pc = 0;
		
		while (s.hasNext()) { // labelit
			String str = s.next();
			if (str.endsWith(":")) {
				String lbl = str.substring(0, str.length()-1);
				if (labels.containsKey(lbl)) {
					System.err.println("Token "+pc+": Label '" + lbl + "' is already defined!");
				}
				else labels.put(lbl, pc);
			} else if (str.startsWith("%!")) {
				System.out.println(str + " pc = " + pc);
			} else
			if (str.endsWith("\"")) {
				if (str.startsWith("\"")) {
					String ch1 = str.substring(1, str.length()-1);
					if (ch1.length() == 0) {
						System.err.println("Syntax error near '" + str + "'. Invalid string!"); 
						//out.write(OPCODE.NOP);
					} else {
						for (int i = 0; i < ch1.length(); i++) {
							pc++;
						}
						pc--;
					}
					
				} else {
					System.err.println("Syntax error near '" + str + "'. Missing \"!");
					//out.write(OPCODE.NOP);
				}
			} else if (str.startsWith("\"")) {
				System.err.println("Syntax error near '" + str + "'. Missing \"!");
				//out.write(OPCODE.NOP);
			}
			
			pc++;
		}
		
		s = new TokenScanner();
		s.setSpecialTokens(new char[]{});
		s.init(code_str);
		
		pc = -1;
		
		while (s.hasNext()) { // instructionit
			pc++;
			String string = s.next();
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.NOP])) {
				out.write(OPCODE.NOP);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PUSH])) {
				out.write(OPCODE.PUSH);
				out.write(0);
			} else
				if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PUSH_INT])) {
					out.write(OPCODE.PUSH_INT);
					out.writeInt(0);
				} else if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PUSH_DOUBLE])) {
					out.write(OPCODE.PUSH_DOUBLE);
					out.writeDouble(0);
				} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.POP])) {
				out.write(OPCODE.POP);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.DUP])) {
				out.write(OPCODE.DUP);
			} else
			/*if (string.equalsIgnoreCase(OPCODE.op[OPCODE.DROP])) {
				out.write(OPCODE.DROP);
			} else*/
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.REVERSE]) || string.equalsIgnoreCase("flip")) {
				out.write(OPCODE.REVERSE);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.EXCH])) {
				out.write(OPCODE.EXCH);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.OVER])) {
				out.write(OPCODE.OVER);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.ROTR])) {
				out.write(OPCODE.ROTR);
			} else
				if (string.equalsIgnoreCase(OPCODE.op[OPCODE.ROTL])) {
					out.write(OPCODE.ROTL);
				} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.ADD])) {
				out.write(OPCODE.ADD);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.SUB])) {
				out.write(OPCODE.SUB);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.MUL])) {
				out.write(OPCODE.MUL);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.DIV])) {
				out.write(OPCODE.DIV);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.EQ])) {
				out.write(OPCODE.EQ);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.GT])) {
				out.write(OPCODE.GT);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.LT])) {
				out.write(OPCODE.LT);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.AND])) {
				out.write(OPCODE.AND);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.OR])) {
				out.write(OPCODE.OR);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.XOR])) {
				out.write(OPCODE.XOR);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.NOT])) {
				out.write(OPCODE.NOT);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PRINT])) {
				out.write(OPCODE.PRINT);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.READ])) {
				out.write(OPCODE.READ);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PRINTM])) {
				out.write(OPCODE.PRINTM);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.READM])) {
				out.write(OPCODE.READM);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PUSHR])) {
				out.write(OPCODE.PUSHR);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.POPR])) {
				out.write(OPCODE.POPR);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.PUSHP])) {
				out.write(OPCODE.PUSHP);
			} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.POPP])) {
				out.write(OPCODE.POPP);
			} else
			if (string.startsWith("#")) {
				if (labels.containsKey(string.substring(1))) {
					if (labels.get(string.substring(1)) < 255) {
						out.write(OPCODE.PUSH);
						//System.out.println("Redirected label '" + string + "' to token " + labels.get(string.substring(1)));
						
						out.write(labels.get(string.substring(1)));
					} else {
						out.write(OPCODE.PUSH_INT);
						//System.out.println("Redirected label '" + string + "' to token " + labels.get(string.substring(1)));
						
						out.writeInt(labels.get(string.substring(1)));
					}
					
					
				} else {
					System.err.println("Line " + s.line() + ", Token "+pc+": No label '" + string.substring(0) + "'!");
					out.write(OPCODE.NOP);
				}
				
			} else
			if (string.endsWith(":")) {
				out.write(OPCODE.NOP);
			} else
			if (string.startsWith("%!")) {
				out.write(OPCODE.NOP);
			} else
			if (string.endsWith("'")) {
				if (string.startsWith("'")) {
					String ch1 = string.substring(1, string.length()-1);
					if (ch1.length() > 1) {
						System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'. Invalid character!"); 
						out.write(OPCODE.NOP);
					} else {
						int ch = ch1.codePointAt(0);
						if (ch > 255) {
							out.write(OPCODE.PUSH_INT);
							out.writeInt(ch);
						} else {
							out.write(OPCODE.PUSH);
							out.write(ch);
						}
					}
					
				} else {
					System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'. Missing '!");
					out.write(OPCODE.NOP);
				}
			} else if (string.startsWith("'")) {
				System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'. Missing \"!");
				out.write(OPCODE.NOP);
			} else
				if (string.endsWith("\"")) {
					if (string.startsWith("\"")) {
						String ch1 = string.substring(1, string.length()-1);
						if (ch1.length() == 0) {
							System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'. Invalid string!"); 
							out.write(OPCODE.NOP);
						} else {
							for (int i = 0; i < ch1.length(); i++) {
								int ch = ch1.codePointAt(i);
								if (ch > 255) {
									out.write(OPCODE.PUSH_INT);
									out.writeInt(ch);
								} else {
									out.write(OPCODE.PUSH);
									out.write(ch);
								}
							}
							
						}
						
					} else {
						System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'. Missing \"!");
						out.write(OPCODE.NOP);
					}
				} else if (string.startsWith("\"")) {
					System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'. Missing \"!");
					out.write(OPCODE.NOP);
				} else
			if (string.equalsIgnoreCase(OPCODE.op[OPCODE.GOTO])) {
				out.write(OPCODE.GOTO);
			} else {
				try {
					int ch = Integer.parseInt(string);
					if (ch > 255 || ch < 0) {
						out.write(OPCODE.PUSH_INT);
						out.writeInt(ch);
					} else {
						out.write(OPCODE.PUSH);
						out.write(ch);
					}
					
				} catch (NumberFormatException ex) {
					try {
						double ch = Double.parseDouble(string);
						out.write(OPCODE.PUSH_DOUBLE);
						out.writeDouble(ch);
						
						
					} catch (NumberFormatException ex2) {
						
						System.err.println("Line " + s.line() + ", Token "+pc+": Syntax error near '" + string + "'!");
						out.write(OPCODE.NOP);
					}
				}
			}
		}
		//s.close();
		out.flush();
		out.close();
		

	}

	private static ArrayList<String> includes = new ArrayList<String>();
	
	private static String parseIncludes(BufferedReader r) throws FileNotFoundException, IOException {
		
		String code_str = "", code_str2 = "", cstr = "";

		while ((cstr = r.readLine()) != null) {
			String tmp = cstr;
			if (cstr.indexOf(';', tmp.lastIndexOf('\"')) != -1) {
				tmp = cstr.substring(0, cstr.indexOf(';', tmp.lastIndexOf('\"')));
			}
			if (tmp.startsWith("%include ")) {
				String[] params = tmp.split(" ");
				String fileStr = params[1];
				try {
				BufferedReader r2 = new BufferedReader(new FileReader(fileStr));
				String importedStr = "", tmpStr2 = "";
				boolean containsIncludes = false;
				while ((tmpStr2 = r2.readLine()) != null) {
					if (tmpStr2.trim().startsWith("%include ")) {
						String[] params2 = tmpStr2.split(" ");
						String fileStr2 = params2[1];
						if (!includes.contains(fileStr2))
							{
							containsIncludes = true;
							includes.add(fileStr2);
							}
					}
					importedStr += "\n" + tmpStr2;
				}
				if (containsIncludes) importedStr = parseIncludes(new BufferedReader(new StringReader(importedStr)));
				code_str2 += importedStr;
				} catch (FileNotFoundException ex) {
					System.err.println("[WARNING] Can't find " + ex.getMessage() + "");
				}
				
			}
			else code_str2 += "\n" + tmp;
		}
		return code_str2;
	}

}
