package org.kaivos.sc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class ProcCompiler {

	enum ParseState {
		NONE,
		PROC1,
		PROC2,
		PROC3,
		CLASS1,
		CLASS2,
		CLASS3,
		CLASS4,
		INCLUDE,
		DEFINE
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 1) {
			System.out.println("Usage: java org.kaivos.sc.ProcCompiler <file>");
			return;
		}
		
		String filen = args[0];
		File file = new File(filen);
		TokenScanner s = null;
		
		s = new TokenScanner();
		BufferedReader r = new BufferedReader(new FileReader(file));
		String code_str = "", cstr = "";
		try {
			while ((cstr = r.readLine()) != null) {
				String tmp = cstr;
				if (cstr.indexOf("//", tmp.lastIndexOf('\n')) != -1) {
					tmp = cstr.substring(0, cstr.indexOf("//", tmp.lastIndexOf('\n')));
				}
				code_str += "\n" + tmp;
			}
		} catch (IOException e) {e.printStackTrace();
		}
		s.setSpecialTokens(new char[]{'=', '(', ',', ')'});
		s.init(code_str);
		
		ParseState state = ParseState.NONE;
		
		int params = 0;
		HashMap<String, Type> parameters = new HashMap<String, Type>(); 
		
		String funcName = null;
		String funcParams = null;
		String superName = null;
		
		System.out.println("0 1 #main pushp popr\n\n");
		
		System.out.println("%include lib/object.stk");
		
		/*System.out.println(";; CLASS Object");

		System.out.println("!Object.size		1");
		System.out.println("!Object.realsize	1");
		System.out.println("!Object.delete		1");

		System.out.println("Object.new:");
		System.out.println("	0 readm 1 add 0 printm 				; Varaa lisää muistia");
		System.out.println("	inits(1)");
		System.out.println("	0 readm secv(1) 				; asetetaan this");
		System.out.println("	0 readm dupc(0) &Object.size add add 0 printm	 		; varataan muistia");
		System.out.println("	#Object.delete dupc(1) &Object.delete add printm; lisätään metodit");
		System.out.println("	dupc(1)	popr					; palautetaan this");

		System.out.println("Object.delete:");
		System.out.println("	0 readm 1 sub 0 printm				; Vapautetaan muisti");
		System.out.println("	0 readm dupc(0) &Object.size add sub 0 printm		; Vapautetaan muisti");
		System.out.println("	popr");

		System.out.println(";; END OF CLASS Object");*/

		
		//System.out.println("%include lib/string.stk");
		/*System.out.println(";; CLASS String");

		System.out.println("!String.size		2");
		System.out.println("!String.realsize	2");
		System.out.println("!String.delete		1");
		System.out.println("!String.print		2");

		System.out.println("String.new:");
		System.out.println("	flip");
		System.out.println("	0 readm 1 add 0 printm 				; Varaa lisää muistia");
		System.out.println("	&String.size rotr");
		System.out.println("	strlp: 	rotl 1 add rotr");
		System.out.println("		dup 0 readm over add printm");
		System.out.println("		0 eq not");
		System.out.println("		#strlp exch goto");
		System.out.println("	over 0 readm printm");
		System.out.println("	inits(1)");
		System.out.println("	0 readm secv(1) 				; asetetaan this");
		System.out.println("	0 readm dupc(0) add 0 printm 			; varataan muistia");
		System.out.println("	#String.delete dupc(1) &String.delete add printm; lisätään metodit");
		System.out.println("	#String.print dupc(1) &String.print add printm; lisätään metodit");
		System.out.println("	dupc(1)	popr					; palautetaan this");


		System.out.println("String.delete:");
		System.out.println("	0 readm 1 sub 0 printm				; Vapautetaan muisti");
		System.out.println("	0 readm dupc(1) readm sub 0 printm		; Vapautetaan muisti");
		System.out.println("	popr");

		System.out.println("String.print:");
		System.out.println("	&String.size add");
		System.out.println("	inits(1)");
		System.out.println("	dupc(0) secv(1)");
		System.out.println("	loop2: 	dupc(1) 1 add dup secv(1) readm dup 1 print	; tulostaa merkin");
		System.out.println("		0 eq not					; jos merkki ei ole 0 ...");
		System.out.println("		#loop2 exch goto				; ... jatka loop2 labeliin");
		System.out.println("	popr");
		
		System.out.println(";; END OF CLASS String");*/
		
		while (s.hasNext()) {
			String str = s.next();
			str = str.trim();
			if (str.trim().replaceAll("\n", "") == "") {
				continue;
			}
			//System.out.println(str + ", " + state);
			
			if (state == ParseState.NONE) {
				if (str.equals("proc")) {
					state = ParseState.PROC1;
					continue;
				}
				if (str.equals("class")) {
					state = ParseState.CLASS1;
					continue;
				}
				if (str.equals("#include")) {
					state = ParseState.INCLUDE;
					continue;
				}
				if (str.equals("#define")) {
					state = ParseState.DEFINE;
					continue;
				}
			} else if (state == ParseState.PROC1) {
				funcName = str;
				state = ParseState.PROC2;
				continue;
			} else if (state == ParseState.PROC2) {
				if (str.equals("(") || str.equals(",")) {
					String type = s.next();
					String paramName = s.next();
					if (!paramName.equals(")")) {
						parameters.put(paramName, new Type(params++, type));
						state = ParseState.PROC2;
					} else {
						state = ParseState.PROC3;
					}
					
					continue;
				}
				state = ParseState.PROC3;
				continue;
			} else if (state == ParseState.PROC3) {
				if (str.startsWith("{") && str.endsWith("}")) {
					str = str.substring(1, str.length()-1);
					Function f = new Function();
					f.name = funcName;
					f.code = str;
					f.params = parameters;
					functions.put(funcName, f);
					//parseFunc(funcName, str, parameters);
				} else {
					System.err.println("; ERROR: Can't find body for proc " + funcName);
				}
				parameters = new HashMap<String, Type>(); 
				params = 0;
				state = ParseState.NONE;
				continue;
			} else if (state == ParseState.CLASS1) {
				funcName = str;
				state = ParseState.CLASS2;
				continue;
			} else if (state == ParseState.CLASS2) {
				if (str.equals("=")) {
					state = ParseState.CLASS3;
					continue;
				}
				superName = "Object";
				state = ParseState.CLASS4;
			} else if (state == ParseState.CLASS3) {
				superName = str;
				state = ParseState.CLASS4;
				continue;
			} 
			if (state == ParseState.CLASS4) {
				//System.out.println(str);
				if (str.startsWith("{") && str.endsWith("}")) {
					str = str.substring(1, str.length()-1);
					parseClass(funcName, superName, str);
				} else {
					System.err.println("; ERROR: Can't find body for proc " + funcName);
				}
				
				state = ParseState.NONE;
				continue;
			} else if (state == ParseState.INCLUDE) {
				System.out.println("\n%include " + str);
				state = ParseState.NONE;
				continue;
			} else if (state == ParseState.DEFINE) {
				System.out.println("\n%define " + str);
				state = ParseState.NONE;
				continue;
			} 
			
		}
		
		//System.out.println(functions);
		
		for (Entry<String, Function> e : functions.entrySet()) {
			if (e.getValue() != null) {
				parseFunc(e.getValue().name, e.getValue().code, e.getValue().params);
			}
		}
	}

	public static class Function{
		String name; 
		HashMap<String, Type> params; 
		String code; 
		String returnType;
		public String toString() {
			return "["+name+"= "+returnType+", "+params+", "+code;
		}
	};
	public static HashMap<String, Function> functions = new HashMap<String, Function>();
	public static class Type {
		int id; 
		String name; 
		public Type(int id, String name) {this.id = id; this.name = name;}
		
		public String toString() {
			
			return "[" + id + ", " + name + "]";
		}
	};
	public static class PClass {
		ArrayList<Type> attributes;
		ArrayList<Function> methods;
		String name;
		String superType;
	}
	public static HashMap<String, PClass> classes = new HashMap<String, PClass>();
	
	public static int dummyProcCounter = 0;
	public static int curw = 0;
	
	private static void parseFunc(String funcName, String str,
			HashMap<String, Type> parameters) {
		
		//System.out.println("; " + parameters);
		System.out.println(funcName + ":");
		System.out.println("	flip");
		
		HashMap<String, Type> localVars = new HashMap<String, Type>();
		
		int localVar = 0;
		
		int depth = 0;
		
		String[] lines = str.trim().split(";");
		TokenScanner s = new TokenScanner();
		
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.trim().startsWith("local array")) {
				String size = line.trim().split(" ")[1];
				String name = size.substring(0, size.indexOf('['));
				size = size.substring(size.indexOf('[')+1, size.indexOf(']'));
				int size2 = Integer.parseInt(size);
				for (int j = 0; j < size2; j++) localVars.put(name + "[" + j + "]", new Type(parameters.size() + localVar++, "int"));
				lines[i] = "";
			}
			else if (line.trim().startsWith("local")) {
				//System.out.println("; DEFINED " + line.trim().split(" ")[2].trim());
				localVars.put(line.trim().split(" ")[2].trim(), new Type(parameters.size() + localVar++, line.trim().split(" ")[1]));
				lines[i] = "";
			}
		}
		
		localVars.putAll(parameters);
		System.out.println("; " + localVars);
		
		System.out.println("	inits(" + (localVar) + ")");
		
		ArrayList<Integer> whiles = new ArrayList<Integer>();
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			line = line.trim();
			
			if (line.trim() == "") continue;
			
			System.out.print("\t");
			
			if (line.trim().startsWith("print ")) {
				if (localVars.containsKey(line.trim().split(" ")[1].trim()) ){
					System.out.println("dupc(" + localVars.get(line.trim().split(" ")[1].trim()).id + ") 0 print 10 1 print");
				} else if (parameters.containsKey(line.trim().split(" ")[1].trim()) ){
					System.out.println("dupc(" + parameters.get(line.trim().split(" ")[1].trim()).id + ") 0 print 10 1 print");
				} else {
					System.out.println("" + line.trim().split(" ")[1].trim() + " 0 print 10 1 print");
				}
				continue;
			}
			
			if (line.trim().startsWith("proceed [")) {
				line = line.substring(line.indexOf(' ')+1);
				while (true) {
					int sulku1 = line.indexOf('[');
					int sulku2 = line.indexOf(']');
					if (sulku1 == -1 || sulku2 == -1) break;
					while (sulku1 > sulku2) {
						sulku2 = line.indexOf(']', sulku2+1);
					}
					
					String subline = line.substring(sulku1, sulku2+1);
					String subline2 = subline.substring(1, subline.length()-1);
					subline2 = subline2.trim();
					String object = subline2.split(" ")[0];
					
				}
				System.out.println("&" + line.split(" ")[1] + " dupc("+localVars.get(line.split(" ")[2]).id+") add readm pushp ");
				continue;
			}
			
			if (line.trim().startsWith("proceed ")) {
				System.out.println("#" + line.trim().split(" ")[1].trim() + " pushp");
				continue;
			}
			
			if (line.trim().startsWith("call ")) {
				System.out.println("&" + line.split(" ")[1] + " dupc("+localVars.get(line.split(" ")[2]).id+") add readm pushp ");
				continue;
			}
			
			if (line.trim().startsWith("field ")) {
				System.out.println("&" + line.split(" ")[1] + " dupc("+localVars.get(line.split(" ")[2]).id+") add readm ");
				continue;
			}
			
			if (line.trim().startsWith("setfield ")) {
				System.out.println("&" + line.split(" ")[1] + " dupc("+localVars.get(line.split(" ")[2]).id+") add printm ");
				continue;
			}
			
			if (line.trim().startsWith("define ")) {
				System.out.println("user_" + line.trim().split(" ")[1].trim() + ":");
				continue;
			}
			
			if (line.trim().startsWith("goto ")) {
				System.out.println("#user_" + line.trim().split(" ")[1].trim() + " 1 goto");
				continue;
			}
			
			boolean do_while = false;
			
			if (line.trim().startsWith("break")) {
				System.out.println("#outwhile" + whiles.get(whiles.size()-1) + " 1 goto");
				continue;
			}
			
			if (line.trim().startsWith("while ")) {
				do_while = true;
				whiles.add(curw++);
				System.out.print("while" + whiles.get(whiles.size()-1) + ": ");
			}
			
			if (line.trim().startsWith("endwhile")) {
				System.out.println("#while" + whiles.get(whiles.size()-1) + " 1 goto outwhile" + whiles.get(whiles.size()-1) + ": ");
				whiles.remove(whiles.size()-1);
			}
			
			
			
			{
			    boolean bool = true;

			    //alert(str);

			    while (bool) {
			        int sulku1 = line.lastIndexOf("(");
			        int sulku2 = line.indexOf(")");
			        if (sulku1 == -1 || sulku2 == -1) break;
			        int i1 = sulku2;
			        while (sulku1 > sulku2) { sulku1 = line.indexOf("(", i1); i1--; }

			        i1 = 2;
			        String nimi = ""+line.charAt(sulku1-1);
			        //System.out.println("; LINE = " + line);
			        while (sulku1-i1 >= 0 && (line.charAt(sulku1-i1)+"").matches("[A-Za-z0-9\\.]")) { 
			        	nimi = line.charAt(sulku1-i1) + nimi; 
			        	i1++; 
			        }

			        ArrayList<String> parametrit = new ArrayList<String>(Arrays.asList(line.substring(sulku1+1, sulku2).split(",")));

			        if (parametrit.get(0).equals("")) parametrit.remove(0);
			        
			        String s2 = null;
			        
			        if (nimi.contains(".")) {
			        	if (nimi.split("\\.")[0].equals("")) {
			        		System.err.println("; SYNTAX ERROR NEAR TOKEN '" + nimi + "'");
			        	}
			        	String nimi2 = "INTERNAL_ERROR";
			        	String nimi3 = "INTERNAL_ERROR";
			        	String name2 = nimi.split("\\.")[0];
			        	String nimi4 = nimi.split("\\.")[1];
			        	if (localVars.containsKey(name2)) {
			        		nimi2 = localVars.get(name2).id + "";//nimi.split(" ")[1];
			        		nimi3 = localVars.get(name2).name;
			        	} else if (parameters.containsKey(name2)) {
			        		nimi2 = parameters.get(name2).id + "";//nimi.split(" ")[1];
			        		nimi3 = localVars.get(name2).name;
			        	} else {
			        		//System.err.println("; INTERNAL ERROR: CAN'T FIND " + name2);
			        		s2 = "dummy_value_TAKEN!#!#!#!";
			        		for (int i2 = 0; i2 < parametrit.size(); i2++) { // etsii ja tulostaa parametrit
			        			String s3 = parametrit.get(i2);
			        			if (s3.startsWith("\"")) { // parsii stringit
			        				if (s3.endsWith("\"")) {
			        					if (s3.trim().length() == 1) {
			        						System.err.println(";; SYNTAX ERROR NEAR '" + s3 + "'!");
			        					} else {
			        						s3 = s3.substring(1, s3.length()-1);
			        						parametrit.remove(i2--);
			        						for (int j = 0; j < s3.length(); j++) {
			        							
			        							int s3char = (int)s3.charAt(j);
			        							
			        							if (s3char == '\\') {
			        								switch (s3.charAt(j+1)) {
			        								case '0':
			        									s3char = '\0';
			        									j++;
			        									break;
			        								case 'n':
			        									s3char = '\n';
			        									j++;
			        									break;
			        								case '\\':
			        									s3char = '\\';
			        									j++;
			        									break;
			        								case 'r':
			        									s3char = '\r';
			        									j++;
			        									break;
			        								case '\"':
			        									s3char = '\"';
			        									j++;
			        									break;
			        								}
			        							}
			        							
			        							parametrit.add(i2+1, s3char+"");
			        						}
			        						if (!name2.equals("String")) {
			        							
			        						}
			        					}
			        				} else {
			        					System.err.println(";; SYNTAX ERROR NEAR '" + s3 + "'!");
			        				}
			        			}
			        			else if (!s3.startsWith("dummy_value_TAKEN!#!#!#!")) {
				        			s3 = s3.trim();
				        			if (localVars.containsKey(s3)) {
				        				s3 = "dupc(" + localVars.get(s3).id + ")";
				        			} else if (parameters.containsKey(s3)) {
				        				s3 = "dupc(" + parameters.get(s3).id + ")";
				        			}
				        			System.out.print(s3 + " ");
				        		}
				        	}
				        	
				        	System.out.println(parametrit.size() + " #" + nimi + " pushp");
				        	line = line.replace(nimi + line.substring(sulku1, sulku2+1), s2);
			        		continue;
			        	}
			        	
			        	parametrit.add(0, "dupc(" + nimi2 + ")");
			        	s2 = "dummy_value_TAKEN!#!#!#!";
			        	
			        	
			        	//System.out.println("; P2 = " + parametrit);
			        	
			        	for (String s3 : parametrit) {
			        		if (!s3.startsWith("dummy_value_TAKEN!#!#!#!")) {
			        			s3 = s3.trim();
			        			if (localVars.containsKey(s3)) {
			        				s3 = "dupc(" + localVars.get(s3).id + ")";
			        			} else if (parameters.containsKey(s3)) {
			        				s3 = "dupc(" + parameters.get(s3).id + ")";
			        			}
			        			System.out.print(s3 + " ");
			        		}
			        	}
			        	
			        	System.out.println(parametrit.size() + " dupc(" + nimi2 + ") &" + nimi3 + "." + nimi4 + " add readm pushp");
			        	
			        	//System.out.println("");
			        } else {
			        	System.err.println("; SYNTAX ERROR NEAR TOKEN '" + nimi + "'");
			        }
			        line = line.replace(nimi + line.substring(sulku1, sulku2+1), s2);

			        

			        //var par = "";
			        //for (var i2 = 0; i2 < parametrit.length; i2++) par = par + ";" + parametrit[i2];

			        //if (debug) document.getElementById("tulos").innerHTML+=("<small>call " + nimi + "(" + par + ")</small> = \""+s+"\"<br>");

			        //alert("funktio " + nimi + "(" + par + ")");
			    }
			}
			
			int varToModify = -1;
			
			if (localVars.containsKey(line.trim().split(" ")[0].trim()) || 
					localVars.containsKey(line.trim().split("=")[0].trim())) {
				if (line.split(" ").length <= 1) {
					if (line.indexOf('=') != -1 && (line.indexOf('=') == line.length()-1 || line.charAt(line.indexOf('=')+1) != '=')
							 && line.charAt(line.indexOf('=')-1) != '<'
							 && line.charAt(line.indexOf('=')-1) != '>') {
						varToModify = localVars.get(line.substring(0, line.indexOf('='))).id;
						line = line.substring(line.indexOf('=')+1);
					}
				}
				else if (line.trim().split(" ")[1].trim().equals("=")) {
					varToModify = localVars.get(line.trim().split(" ")[0].trim()).id;
					line = line.substring(line.indexOf('=')+1);
				}
			}
			
			line = line.trim();
			
			ArrayList<String> operators = new ArrayList<String>();
			
			s.setSpecialTokens(new char[]{'+', '-', '*', '/', '&', '|', '=', '!', '<', '>', ',', '(', ')'});
			try {
				s.init(line);
			} catch (StringIndexOutOfBoundsException ex) {
				System.out.println("Wrong line: " + line);
			}
			ArrayList<String> ops = s.getTokenList();
			
			
			for (int j = 0; j < ops.size(); j++) {
				String c = ops.get(j);
				if (c.equals("=") && j < ops.size()-2 && ops.get(j+1).equals("=")) {
					j++;
					c += "=";
				}
				if (c.equals("!") && j < ops.size()-2 && ops.get(j+1).equals("=")) {
					j++;
					c += "=";
				}
				if (c.equals("-") && j < ops.size()-2 && ops.get(j+1).equals(">")) {
					j++;
					c += ">";
				}
				
				if (isNumber(c)) {
					System.out.print(c + " ");
				} else if (localVars.containsKey(c)){
					System.out.print("dupc(" + localVars.get(c).id + ") ");
				} else if (parameters.containsKey(c)){
					System.out.print("dupc(" + parameters.get(c).id + ") ");
				} else if (functions.keySet().contains(c.trim())) {
					//System.out.println("; INFO: " + c);
					operators.add(c);
				}
				if (isOperator(c)) {
					if (!operators.isEmpty()) {
						if (((lA(c) && preOperator(c) <= preOperator(operators.get(operators.size()-1))) ||
								preOperator(c) < preOperator(operators.get(operators.size()-1))) &&
								!operators.get(operators.size()-1).equals("("))  {
							System.out.print(replaceOperators(operators.remove(operators.size()-1)) + " ");
						}
					}
							
					operators.add(c);
				}
				else if (isLParenthis(c)) {
					operators.add(c);
				}
				else if (isRParenthis(c)) {
					while (!isLParenthis(operators.get(operators.size()-1))) {
						System.out.print(replaceOperators(operators.remove(operators.size()-1)) + " ");
					}
					operators.remove(operators.size()-1);
					String token = operators.get(operators.size()-1);
					if (functions.keySet().contains(token)) {
						operators.remove(operators.size()-1);
						if (functions.get(token) != null) {
							System.out.print(functions.get(token).params.size() + " ");
							System.out.print("#" + token + " pushp ");
						}
						else System.out.print("#" + token + " pushr ");
					} else {
						//System.out.println();
						//System.out.println("; ERROR: CAN'T FIND " + token);
					}
				} else {
				}
				
			}
			while (!operators.isEmpty()) {
				System.out.print(replaceOperators(operators.remove(operators.size()-1)) + " ");
			}
			if (!do_while && varToModify != -1) System.out.println("secv(" + varToModify + ") ");
			else if (do_while) {
				System.out.println("not #outwhile" + whiles.get(whiles.size()-1) + " exch goto");
			}
			
		}
		
		System.out.println("	popr\n");
		
	}
	
	private static void parseClass(String className, String superT, String str) {
		
		System.out.println("\n;; CLASS " + className + " = " + superT);
		
		HashMap<String, Integer> fields = new HashMap<String, Integer>();
		HashMap<String, Type> methods = new HashMap<String, Type>();
		
		int globalField = 2;
		
		
		String[] lines = str.trim().split(";");
		TokenScanner s = new TokenScanner();
		s.setParseSpaces(false);
		s.setSpecialTokens(new char[]{';'});
		s.init(str);
		
		while (s.hasNext()) {
			String line = s.next();
			line = line.trim();
			line = line.replace('\t', ' ');
			if (line.startsWith("global array")) {
				String size = line.split(" ")[1];
				String name = size.substring(0, size.indexOf('['));
				size = size.substring(size.indexOf('[')+1, size.indexOf(']'));
				int size2 = Integer.parseInt(size);
				for (int j = 0; j < size2; j++) fields.put(name + "[" + j + "]", globalField++);
				
			}
			else if (line.startsWith("global proc")) {
				String name = className + "." + line.split(" ")[2];
				methods.put(name, new Type(globalField++, name));
				HashMap<String, Type> parameters = new HashMap<String, Type>();
				int param = 0;
				TokenScanner scannParams = new TokenScanner();
				scannParams.setSpecialTokens(new char[]{'(', ')', ','});
				scannParams.init(line);
				scannParams.next();
				scannParams.next();
				scannParams.next();
				while (true) {
					String s2 = scannParams.next();;
					if (s2.equals("(") || s2.equals(",")) {
						String s3 = scannParams.next();
						String s4 = scannParams.next();
						parameters.put(s4, new Type(param++, s3));
					} else {
						
						break;
					}
				}
				String equals = s.next();
				String code;
				if (equals.equals("=")) {
					equals = s.next();
					code = s.next();
				} else code = equals;
				
				if (code.startsWith("{") && code.endsWith("}")) {
					code = code.substring(1, code.length()-1);
				} else {
					System.err.println(";ERROR: Can't find body for " + name + " (" + code + ")");
				}
				Function f = new Function();
				f.name = name;
				f.code = code;
				f.params = parameters;
				f.returnType = equals;
				functions.put(name, f);
				//parseFunc(name, s.next(), paramters);
				
			} else if (line.startsWith("global reproc")) {
				String name = className + "." + line.split(" ")[2];
				methods.put(name, new Type(-1, superT + "." + line.split(" ")[2]));
				HashMap<String, Type> parameters = new HashMap<String, Type>();
				int param = 0;
				TokenScanner scannParams = new TokenScanner();
				scannParams.setSpecialTokens(new char[]{'(', ')', ','});
				scannParams.init(line);
				scannParams.next();
				scannParams.next();
				scannParams.next();
				while (true) {
					String s2 = scannParams.next();;
					if (s2.equals("(") || s2.equals(",")) {
						String s3 = scannParams.next();
						String s4 = scannParams.next();
						parameters.put(s4, new Type(param++, s3));
					} else {
						
						break;
					}
				}
				String equals = s.next();
				String code;
				if (equals.equals("=")) {
					equals = s.next();
					code = s.next();
				} else code = equals;
				
				if (code.startsWith("{") && code.endsWith("}")) {
					code = code.substring(1, code.length()-1);
				} else {
					System.err.println(";ERROR: Can't find body for " + name + " (" + code + ")");
				}
				Function f = new Function();
				f.name = name;
				f.code = code;
				f.params = parameters;
				f.returnType = equals;
				functions.put(name, f);
				//parseFunc(name, s.next(), paramters);
				
			} else if (line.startsWith("global")) {
				fields.put(line.split(" ")[1], globalField++);
				
			}
		}
		
		PClass pclass = new PClass();
		pclass.name = className;
		pclass.superType = superT;
		
		functions.put(className + ".new", null);
		functions.put(className + ".delete", null);
		
		System.out.println("!" + className + ".size		" + globalField);
		System.out.println("!" + className + ".realsize	" + globalField + " &" + superT + ".realsize add");
		
		System.out.println("\n; Methods");
		System.out.println("!" + className + ".delete	1");
		for (Entry<String, Type> e : methods.entrySet()) {
			if (e.getValue().id != -1)
				System.out.println("!" + e.getKey() + "	" + e.getValue().id);
			else {
				System.out.println("!" + e.getKey() + "	&" + e.getValue().name);
			}
		}
		
		System.out.println("\n; Fields");
		for (Entry<String, Integer> e : fields.entrySet()) {
			System.out.println("!" + className + "." + e.getKey() + "	" + e.getValue());
		}
		
		System.out.println("\n;Constructor");
		
		System.out.println(className + ".new:");
		System.out.println("	inits(1)");
		System.out.println("	dupc(0) &"+className+".size add 1 #"+superT+".new pushp secv(1)");
		//System.out.println("	0 readm &"+className+".size add 0 printm");
		System.out.println("	#"+className+".delete dupc(1) &"+className+".delete add printm");
		for (Entry<String, Type> method : methods.entrySet()) {
			System.out.println("	#"+method.getKey()+" dupc(1) &"+method.getValue().name+" add printm");
		}
		for (String field : fields.keySet()) {
			System.out.println("	0 dupc(1) &"+className+"."+field+" add printm");
		}
		System.out.println("	dupc(1) popr");
		
		System.out.println("\n;Destructor");
		System.out.println(className + ".delete:");
		System.out.println("	dupc(1) dupc(0) &"+className+".size add 2 #"+superT+".delete pushp				; Vapautetaan muisti (super)");
		//System.out.println("	0 readm &"+className+".size sub 0 printm		; Vapautetaan muisti ");
		System.out.println("	popr");
		
		System.out.println(";; END OF CLASS " + className + "\n");
		
		
	}
	
	private static boolean isLParenthis(String c) {
		if (c.equals("(")) return true;
		return false;
	}
	private static boolean isRParenthis(String c) {
		if (c.equals(")")) return true;
		return false;
	}
	
	private static boolean lA(String c) {
		if (c.equals("^")) return false;
		return true;
	}
	
	private static int preOperator(String c) {
		if (c.equals("&") || c.equals("|") || c.equals("AND") || c.equals("OR")) return 2;
		if (c.equals("==") || c.equals("!=") || c.equals("<") || c.equals(">")) return 3;
		if (c.equals("*") || c.equals("/") || c.equals("%")) return 5;
		if (c.equals("^")) return 5;
		if (c.equals("->")) return 6;
		return 4;
	}
	
	private static boolean isOperator(String c) {
		if (c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("==") || c.equals("!=") || c.equals("<")
				 || c.equals(">")) return true;
		return false;
	}

	private static boolean isNumber(String c) {
		try {
			int i = Integer.parseInt(c);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private static String replaceOperators(String s) {
		if (s.equals("+")) return "add";
		if (s.equals("-")) return "sub";
		if (s.equals("*")) return "mul";
		if (s.equals("/")) return "div";
		if (s.equals("==")) return "eq";
		if (s.equals("!=")) return "eq not";
		if (s.equals("<")) return "lt";
		if (s.equals(">")) return "gt";
		if (s.equals("^")) return "exp";
		if (s.equals("%")) return "mod";
		if (s.equals("&") || s.equals("AND")) return "and";
		if (s.equals("|") || s.equals("OR")) return "or";
		return s;
	}
	
}
