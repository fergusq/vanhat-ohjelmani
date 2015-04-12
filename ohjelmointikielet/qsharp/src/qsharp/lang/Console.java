package qsharp.lang;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import qsharp.ui.MenuDialog;
import qsharp.ui.YesNoDialog;
import sun.misc.IOUtils;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Message;
import jcurses.widgets.Window;

public class Console {

	public static enum Type{
		Cmd,
		Integer,
		Function,
		String,
		VarArray,
		Double
	}
	
	private static boolean explicit = true;
	private static boolean auto_gc = false;
	private static boolean debug_mode = false;
	private static boolean debug_running = false;
	private static ArrayList<Integer> debug_breakpoints = new ArrayList<Integer>();
	
	/*private static HashMap<String, Command> commands = new HashMap<String, Command>();
	private static HashMap<String, Command> functions = new HashMap<String, Command>();
	private static HashMap<String, String> stringVars = new HashMap<String, String>();
	private static HashMap<String, Integer> intVars = new HashMap<String, Integer>();
	private static HashMap<String, Double> doubleVars = new HashMap<String, Double>();
	private static HashMap<String, MenuDialog> uiMenuVars = new HashMap<String, MenuDialog>();
	private static HashMap<String, Command> arrayVars = new HashMap<String, Command>();*/
	
	private static QScope scope;
	
	private static boolean runFile = false;
	private static boolean varBuild = false;
	private static boolean loopDoBuild = false;
	private static boolean loopWhileBuild = false;
	private static String whileBoolean = "";
	private static boolean varArrayBuild = false;
	private static boolean funcBuild = false;
	private static boolean cmdBuild = false;
	
	
	private static Scanner userIn = null;
	private static Scanner fileIn = null;
	private static String[] args1;
	private static boolean runningCom;
	private static boolean runningLoop;
	private static int comLine;
	private static int loopLine;
	private static boolean uiInited = false;
	private static int uiMaxX;
	private static int uiMaxY;
	
	private static int curLine = 0;
	
	public static void main(String[] args){
		scope = new QScope(null);
		
		scope.commands.put("help", new Command(new String[]{
				"print \"proceed\"",
				"print \"print\"",
				"print \"create new ... endcreate\"",
				}));
		print("Q# Console - " + Info.devPhase + " Version " + Info.version);
		print("");
		
		scope.stringVars.put("$enter", "\n");
		scope.stringVars.put("$empty", "");
		scope.stringVars.put("$centerdot", "·");
		
		try {
			args1 = args;
			userIn = new Scanner(System.in);
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-F")){
					fileIn = new Scanner(new FileInputStream(new File(args[i + 1])));
					runFile = true;
				}
				if (args[i].equals("-d")){
					debug_mode = true;
					print("Debug mode (type help to get help)"); // Aktivoi debug-tilan
				}
			}
		} catch (Exception e) {
			print("Q# Console has crashed! Error:");
			e.printStackTrace();
		}
		int lN = 0;
		try {
		
		String rawFunc = new String();
		String funcName = "";
		String rawCmd = new String();
		String cmdName = "";
		String varName = "";
		Command rawArray = new Command();
		Command rawLoop = new Command();
		HashMap<String, Type> param = null;
		while (runFile ? fileIn.hasNext() : userIn.hasNext()){
			
			// DEBUG-KOMENNOT ALKAVAT
			
			if (debug_mode && ((debug_running && debug_breakpoints.contains(curLine) || !debug_running))) {
				boolean typing = true;
				debug_running = false;
				while (typing){ // Debug-komennot
					type("> ");
					String command = userIn.nextLine();
					if (command.toLowerCase().startsWith("help")) {
						if (command.indexOf(" ") == -1) {
							print("help			- Shows help");
							print("vars			- Prints vars");
							print("runline			- Runs line");
							print("run			- Runs program");
							print("breakpoint		- Sets breakpoint");
							print("next			- Goes to next line");
							continue;
						}
						String parameter = command.substring(command.indexOf(" ")).trim();
						if (parameter.equalsIgnoreCase("help")) {
							print("Help for command help");
							print("Shows help for typed command");
						}
						if (parameter.equalsIgnoreCase("vars")) {
							print("Help for command vars");
							print("Shows all variables");
						}
						if (parameter.equalsIgnoreCase("runline")) {
							print("Help for command runline");
							print("Runs specified Q# command");
						}
						if (parameter.equalsIgnoreCase("run")) {
							print("Help for command run");
							print("Runs programs to next breakpoint");
						}
						if (parameter.equalsIgnoreCase("breakpoint")) {
							print("Help for command breakpoint");
							print("Adds new breakpoint");
						}
						if (parameter.equalsIgnoreCase("next")) {
							print("Help for command next");
							print("Runs programs to next line");
						}
					}
					if (command.toLowerCase().equalsIgnoreCase("vars")) {
						for (String key : scope.intVars.keySet()) {
							String space = ". . . . . . . . . . . . ";
							for (char c : key.toCharArray()) {
								space = space.substring(1);
							}
							print("int " + key+" "+space+"= " + scope.intVars.get(key));
						}
						for (String key : scope.stringVars.keySet()) {
							String space = " . . . . . . . . . . ";
							for (char c : key.toCharArray()) {
								space = space.substring(1);
							}
							print("string " + key+" "+space+"= " + scope.stringVars.get(key));
						}
						for (String key : scope.commands.keySet()) {
							print("cmd " + key);
						}
						for (String key : scope.functions.keySet()) {
							print("function " + key);
						}
						for (String key : scope.uiMenuVars.keySet()) {
							print("uimenu " + key);
						}
					}
					if (command.toLowerCase().startsWith("runline ")) {
						String parameter = command.substring(command.indexOf(" ")).trim();
						runLine(parameter);
					}
					if (command.toLowerCase().equalsIgnoreCase("next")) {
						typing = false;
					}
					if (command.toLowerCase().equalsIgnoreCase("run")) {
						typing = false;
						debug_running = true;
					}
					if (command.toLowerCase().startsWith("breakpoint ")) {
						String parameter = command.substring(command.indexOf(" ")).trim();
						debug_breakpoints.add(Integer.parseInt(parameter));
					}
				}
				
			}
			
			// DEBUG-KOMENNOT LOPPUVAT
			
			String line = runFile ? fileIn.nextLine() : userIn.nextLine();
			line.replaceAll("\\n", "\n");
			
			lN++;
			
			while (line.endsWith(">>")) { // parsii useat rivit
				line = line.substring(0, line.length()-2) + (runFile ? fileIn.nextLine() : userIn.nextLine());
				lN++;
			}
			
			line = line.trim();
			
			curLine = lN;
			
			if (varArrayBuild){
				if (line.trim().equalsIgnoreCase("endcreate")) {
					varArrayBuild = false;
					scope.arrayVars.put(varName, rawArray);
					rawArray = new Command();
					varName = "";
				}else {
					rawArray.addLine(line);
					continue;
				}
			}
			
			if (loopDoBuild){
				if (line.trim().equalsIgnoreCase("enddo")) {
					loopDoBuild = false;
					runDoLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(line);
					continue;
				}
			}
			
			if (loopWhileBuild){
				if (line.trim().equalsIgnoreCase("endwhile")) {
					loopWhileBuild = false;
					runWhileLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(line);
					continue;
				}
			}
			
			if (funcBuild){
				if (line.trim().equalsIgnoreCase("endcreate")) {
					funcBuild = false;
					
					if (param != null) {
						Command c = new Command(rawFunc.split("LINEENDEr£r£r£r£r£r£r£"));
						c.params = param;
						scope.functions.put(funcName, c);
					} else {
						scope.functions.put(funcName, new Command(rawFunc.split("LINEENDEr£r£r£r£r£r£r£")));
					}
					rawFunc = "";
					funcName = "";
					param = null;
				} else {
					rawFunc += line + "LINEENDEr£r£r£r£r£r£r£";
					continue;
				}
			}
			if (cmdBuild){
				if (line.trim().equalsIgnoreCase("endcreate")) {
					cmdBuild = false;
					scope.commands.put(cmdName, new Command(rawCmd.split("LINEENDEr£r£r£r£r£r£r£")));
					rawCmd = "";
					cmdName = "";
				} else {
					rawCmd += line + "LINEENDEr£r£r£r£r£r£r£";
					continue;
				}
			}
			if (line.toLowerCase().startsWith("proceed ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				Command com = scope.getCommand(cmd);
				if (com != null){
					runCommand(com);
				}
			} else if (line.toLowerCase().startsWith("option explicit ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				cmd = cmd.substring(cmd.indexOf(" ")).trim();
				boolean b = cmd.equalsIgnoreCase("true");
				explicit = b;
			} else if (line.toLowerCase().startsWith("option gc ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				cmd = cmd.substring(cmd.indexOf(" ")).trim();
				boolean b = cmd.equalsIgnoreCase("true");
				auto_gc = b;
			} else if (line.toLowerCase().startsWith("use ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				readLib(cmd);
			} else if (line.toLowerCase().startsWith("do ")) {
				loopDoBuild = true;
			} else if (line.toLowerCase().startsWith("while ")) {
				loopWhileBuild = true;
				String cmd = line.substring(line.indexOf(" ")).trim();
				whileBoolean = cmd;
			} 
			else if (line.toLowerCase().startsWith("create new ")){
				String[] raw = line.split(" ");
				String type = raw[2];
				Type t = parseType(type);
				if (t == Type.String){
					String s3 = "";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.stringVars.put(raw[3], s3);
					varBuild = true;
				}
				if (t == Type.Integer){
					String s3 = "0";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.intVars.put(raw[3], Integer.parseInt(s3));
					varBuild = true;
				}
				if (t == Type.Double){
					String s3 = "0.0";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.doubleVars.put(raw[3], Double.parseDouble(s3));
					varBuild = true;
				}
				if (t == Type.Function){
					funcBuild = true;
					funcName = raw[3];
					if (raw.length > 4) {
						String params = "";
						for (int i = 4; i < raw.length; i++) {
							params += raw[i].trim() + " ";
						}
						if (params.matches(PARAMETERS)) {
							param = new HashMap<String, Type>();
							params = params.substring("withparams".length());
							params = params.trim();
							params = params.substring(1);
							params = params.trim();
							boolean more = true;
							while (more) {
								String rawType = params.substring(0, params.indexOf(' ')).trim();
								params = params.substring(params.indexOf(' ')).trim();
								Type type2 = parseType(rawType);
								if (params.indexOf(',') == -1) {
									String name2 = params.substring(0, params.indexOf(']')).trim();
									param.put(name2, type2);
									break;
								}
								String name2 = params.substring(0, params.indexOf(',')).trim();
								params = params.substring(params.indexOf(',')+1).trim();
								param.put(name2, type2);
								more = !(params.startsWith("]"));
							}
						}
					}
				}
				if (t == Type.Cmd){
					cmdBuild = true;
					cmdName = raw[3];
					if (raw.length > 4) {
						String params = "";
						for (int i = 4; i < raw.length; i++) {
							params += raw[i].trim() + " ";
						}
						if (params.matches(PARAMETERS)) {
							param = new HashMap<String, Type>();
							params = params.substring("withparams".length());
							params = params.trim();
							params = params.substring(1);
							params = params.trim();
							boolean more = true;
							while (more) {
								String rawType = params.substring(0, params.indexOf(' ')).trim();
								params = params.substring(params.indexOf(' ')).trim();
								Type type2 = parseType(rawType);
								if (params.indexOf(',') == -1) {
									String name2 = params.substring(0, params.indexOf(']')).trim();
									param.put(name2, type2);
									break;
								}
								String name2 = params.substring(0, params.indexOf(',')).trim();
								params = params.substring(params.indexOf(',')+1).trim();
								param.put(name2, type2);
								more = !(params.startsWith("]"));
							}
						}
					}
				}
				if (t == Type.VarArray){
					varArrayBuild = true;
					varName = raw[3];
				}
			} else if (line.toLowerCase().startsWith("endcreate")) {
				/*if (!varBuild){
					stringVars.clear();
					intVars.clear();
					if (funcBuild){
						
					}
				}*/
				varBuild = false;
			} else if (line.toLowerCase().startsWith("goto")) {
				
		} else {
				runCommand(new Command(new String[]{line}));
			}
		}
		
		} catch (NumberFormatException ex) {
			String msg = ex.getMessage().substring(19, ex.getMessage().length()-1);
			if (msg.startsWith("$")) {
				throwException(new VariableNotFoundException("Cannot find variable: " + msg), lN, ex);
			}
			if (msg.startsWith("?")) {
				throwException(new VariableNotFoundException("Cannot find function: " + msg), lN, ex);
			}
			throwException(new ConsoleException("Q# Console has crashed!"), lN, ex);
		} catch (Exception ex) {
			throwException(new ConsoleException("Q# Console has crashed!"), lN, ex);
		} 
	}

	private static void readLib(String cmd1) {
		int lN = 0;
		try {
		
		String rawFunc = new String();
		String funcName = "";
		String rawCmd = new String();
		String cmdName = "";
		String varName = "";
		Command rawArray = new Command();
		Command rawLoop = new Command();
		HashMap<String, Type> param = null;
		Scanner libIn = new Scanner(new FileInputStream(new File(cmd1)));
		while (libIn.hasNext()){
			
			String line = libIn.nextLine();
			line.replaceAll("\\n", "\n");
			
			lN++;
			
			while (line.endsWith(">>")) { // parsii useat rivit
				line = line.substring(0, line.length()-2) + (libIn.nextLine());
				lN++;
			}
			
			line = line.trim();
			
			curLine = lN;
			
			if (varArrayBuild){
				if (line.trim().equalsIgnoreCase("endcreate")) {
					varArrayBuild = false;
					scope.arrayVars.put(varName, rawArray);
					rawArray = new Command();
					varName = "";
				}else {
					rawArray.addLine(line);
					continue;
				}
			}
			
			if (loopDoBuild){
				if (line.trim().equalsIgnoreCase("enddo")) {
					loopDoBuild = false;
					runDoLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(line);
					continue;
				}
			}
			
			if (loopWhileBuild){
				if (line.trim().equalsIgnoreCase("endwhile")) {
					loopWhileBuild = false;
					runWhileLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(line);
					continue;
				}
			}
			
			if (funcBuild){
				if (line.trim().equalsIgnoreCase("endcreate")) {
					funcBuild = false;
					
					if (param != null) {
						Command c = new Command(rawFunc.split("LINEENDEr£r£r£r£r£r£r£"));
						c.params = param;
						scope.functions.put(funcName, c);
					} else {
						scope.functions.put(funcName, new Command(rawFunc.split("LINEENDEr£r£r£r£r£r£r£")));
					}
					rawFunc = "";
					funcName = "";
					param = null;
				} else {
					rawFunc += line + "LINEENDEr£r£r£r£r£r£r£";
					continue;
				}
			}
			if (cmdBuild){
				if (line.trim().equalsIgnoreCase("endcreate")) {
					cmdBuild = false;
					scope.commands.put(cmdName, new Command(rawCmd.split("LINEENDEr£r£r£r£r£r£r£")));
					rawCmd = "";
					cmdName = "";
				} else {
					rawCmd += line + "LINEENDEr£r£r£r£r£r£r£";
					continue;
				}
			}
			if (line.toLowerCase().startsWith("proceed ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				Command com = scope.getCommand(cmd);
				if (com != null){
					runCommand(com);
				}
			} else if (line.toLowerCase().startsWith("option explicit ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				cmd = cmd.substring(cmd.indexOf(" ")).trim();
				boolean b = cmd.equalsIgnoreCase("true");
				explicit = b;
			} else if (line.toLowerCase().startsWith("option gc ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				cmd = cmd.substring(cmd.indexOf(" ")).trim();
				boolean b = cmd.equalsIgnoreCase("true");
				auto_gc = b;
			} else if (line.toLowerCase().startsWith("use ")){
				String cmd = line.substring(line.indexOf(" ")).trim();
				readLib(cmd);
			} else if (line.toLowerCase().startsWith("do ")) {
				loopDoBuild = true;
			} else if (line.toLowerCase().startsWith("while ")) {
				loopWhileBuild = true;
				String cmd = line.substring(line.indexOf(" ")).trim();
				whileBoolean = cmd;
			} 
			else if (line.toLowerCase().startsWith("create new ")){
				String[] raw = line.split(" ");
				String type = raw[2];
				Type t = parseType(type);
				if (t == Type.String){
					String s3 = "";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.stringVars.put(raw[3], s3);
					varBuild = true;
				}
				if (t == Type.Integer){
					String s3 = "0";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.intVars.put(raw[3], Integer.parseInt(s3));
					varBuild = true;
				}
				if (t == Type.Function){
					funcBuild = true;
					funcName = raw[3];
					if (raw.length > 4) {
						String params = "";
						for (int i = 4; i < raw.length; i++) {
							params += raw[i].trim() + " ";
						}
						if (params.matches(PARAMETERS)) {
							param = new HashMap<String, Type>();
							params = params.substring("withparams".length());
							params = params.trim();
							params = params.substring(1);
							params = params.trim();
							boolean more = true;
							while (more) {
								String rawType = params.substring(0, params.indexOf(' ')).trim();
								params = params.substring(params.indexOf(' ')).trim();
								Type type2 = parseType(rawType);
								if (params.indexOf(',') == -1) {
									String name2 = params.substring(0, params.indexOf(']')).trim();
									param.put(name2, type2);
									break;
								}
								String name2 = params.substring(0, params.indexOf(',')).trim();
								params = params.substring(params.indexOf(',')+1).trim();
								param.put(name2, type2);
								more = !(params.startsWith("]"));
							}
						}
					}
				}
				if (t == Type.Cmd){
					cmdBuild = true;
					cmdName = raw[3];
					if (raw.length > 4) {
						String params = "";
						for (int i = 4; i < raw.length; i++) {
							params += raw[i].trim() + " ";
						}
						if (params.matches(PARAMETERS)) {
							param = new HashMap<String, Type>();
							params = params.substring("withparams".length());
							params = params.trim();
							params = params.substring(1);
							params = params.trim();
							boolean more = true;
							while (more) {
								String rawType = params.substring(0, params.indexOf(' ')).trim();
								params = params.substring(params.indexOf(' ')).trim();
								Type type2 = parseType(rawType);
								if (params.indexOf(',') == -1) {
									String name2 = params.substring(0, params.indexOf(']')).trim();
									param.put(name2, type2);
									break;
								}
								String name2 = params.substring(0, params.indexOf(',')).trim();
								params = params.substring(params.indexOf(',')+1).trim();
								param.put(name2, type2);
								more = !(params.startsWith("]"));
							}
						}
					}
				}
				if (t == Type.VarArray){
					varArrayBuild = true;
					varName = raw[3];
				}
			} else if (line.toLowerCase().startsWith("endcreate")) {
				/*if (!varBuild){
					stringVars.clear();
					intVars.clear();
					if (funcBuild){
						
					}
				}*/
				varBuild = false;
			} else if (line.toLowerCase().startsWith("goto")) {
				
		}
		}
		} catch (NumberFormatException ex) {
				String msg = ex.getMessage().substring(19, ex.getMessage().length()-1);
				if (msg.startsWith("$")) {
					throwException(new VariableNotFoundException(cmd1 + ": Cannot find variable: " + msg), lN, ex);
				}
				if (msg.startsWith("?")) {
					throwException(new VariableNotFoundException(cmd1 + ": Cannot find function: " + msg), lN, ex);
				}
				throwException(new ConsoleException(cmd1 + ": Q# Console has crashed!"), lN, ex);
			} catch (Exception ex) {
				throwException(new ConsoleException(cmd1 + ": Q# Console has crashed!"), lN, ex);
			} 
		
	}

	private static void throwException(QSharpException ex, int line, Exception e) { // Käsittelee poikkeuksen
		print("Unhandled exception on line " + line + " (comLine " + comLine + ")");
		print("Thrown exception code: " + ex.code);
		print("Exception message: " + ex.getReason());
		//if (e == null) print("No java exception thrown.");
		print("Check exception information log.");
		try {
			PrintWriter writer = new PrintWriter(new File("error.log"));
			
			writer.print("Unhandled exceptin on line " + line + "\n");
			writer.print("Thrown exception code: " + ex.code + "\n");
			writer.print("Exception message: " + ex.getReason() + "\n");
			if (e != null) {
				writer.print("Java exception information: " + "\n");
				e.printStackTrace(writer);
			} else {
				writer.print("No java exception thrown.");
			}
			writer.flush();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		System.exit(1);
	}

	private static void runDoLoop(Command cmd) { // suorittaa do-loopin
		scope = new QScope(scope);
		int old = loopLine;
		runningLoop = true;
		loopLine = 0;
		Command rawLoop = new Command();
		for (loopLine = 0; runningLoop; loopLine++){
			
			if (loopLine >= cmd.length()) loopLine = 0;
			
			String com = cmd.getLine(loopLine);
			
			if (loopDoBuild){
				if (com.trim().equalsIgnoreCase("enddo")) {
					loopDoBuild = false;
					runDoLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(com);
					continue;
				}
			}
			
			/*if (loopWhileBuild){
				if (com.trim().equalsIgnoreCase("endwhile")) {
					varArrayBuild = false;
					arrayVars.put(varName, rawArray);
					rawArray = new Command();
				}else {
					rawArray.addLine(com);
					continue;
				}
			}*/
			
			if (com.toLowerCase().startsWith("do")) {
				loopDoBuild = true;
			} else if (com.toLowerCase().startsWith("while ")) {
			//	loopWhileBuild = true;
			} else runLine(com);

		}
		if (scope.parent != null) scope = scope.parent;
		loopLine = old;
	}
	
	private static void runWhileLoop(Command cmd) { // Suorittaa while-loopin
		scope = new QScope(scope);
		int old = loopLine;
		runningLoop = true;
		loopLine = 0;
		Command rawLoop = new Command();
		for (loopLine = 0; runningLoop; loopLine++){
			
			runningLoop = getBoolean(whileBoolean);
			
			if (loopLine >= cmd.length()) loopLine = 0;
			
			String com = cmd.getLine(loopLine);
			
			if (loopWhileBuild){
				if (com.trim().equalsIgnoreCase("endwhile")) {
					loopWhileBuild = false;
					runWhileLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(com);
					continue;
				}
			}
			
			if (com.toLowerCase().startsWith("do")) {
			//	loopDoBuild = true;
			} else if (com.toLowerCase().startsWith("while ")) {
				loopWhileBuild = true;
			} else runLine(com);

		}
		if (scope.parent != null) scope = scope.parent;
		loopLine = old;
	}

	private static Type parseType(String raw) { // parsii tyypin
		if (raw.toLowerCase().equals("cmd")){
			return Type.Cmd;
		} else if (raw.toLowerCase().equals("function")){
			return Type.Function;
		} else if (raw.toLowerCase().equals("int")){
			return Type.Integer;
		} else if (raw.toLowerCase().equals("string")){
			return Type.String;
		} else if (raw.toLowerCase().equals("vararray")){
			return Type.VarArray;
		} else if (raw.toLowerCase().equals("double")) {
			return Type.Double;
		}
		return null;
	}

	private static void runCommand(Command cmd) { // Suorittaa komennon
		scope = new QScope(scope);
		int old = comLine;
		runningCom = true;
		comLine = 0;
		Command rawLoop = new Command();
		ArrayList<String> vars = new ArrayList<String>();
		for (comLine = 0; comLine < cmd.lines.length && runningCom; comLine++){
			String com = cmd.lines[comLine];
			
			if (loopDoBuild){
				if (com.trim().equalsIgnoreCase("enddo")) {
					loopDoBuild = false;
					runDoLoop(rawLoop);
					rawLoop = new Command();
				}else {
					rawLoop.addLine(com);
					continue;
				}
			}
			
			 if (com.toLowerCase().startsWith("create new ")){
					String[] raw = com.split(" ");
					String type = raw[2];
					Type t = parseType(type);
					if (t == Type.String){
						String s3 = "";
						if (raw.length > 5) {
							if (raw[4].equals("to")) {
								s3 = raw[5];
							}
						}
						scope.stringVars.put(raw[3], s3);
						vars.add(raw[3]);
					}
					if (t == Type.Integer){
						String s3 = "0";
						if (raw.length > 5) {
							if (raw[4].equals("to")) {
								s3 = raw[5];
							}
						}
						scope.intVars.put(raw[3], Integer.parseInt(s3));
						vars.add(raw[3]);
					}
					if (t == Type.Double){
						String s3 = "0.0";
						if (raw.length > 5) {
							if (raw[4].equals("to")) {
								s3 = raw[5];
							}
						}
						scope.doubleVars.put(raw[3], Double.parseDouble(s3));
						vars.add(raw[3]);
					}
			} else
			if (com.toLowerCase().startsWith("do")) {
				loopDoBuild = true;
			} else if (com.toLowerCase().startsWith("while ")) {
			//	loopWhileBuild = true;
			} else runLine(com);
		}
		if (auto_gc) { // Tuhoaa muuttujat
			/*for (String name : vars) {
				if (intVars.containsKey(name)) intVars.remove(name);
				if (stringVars.containsKey(name)) stringVars.remove(name);
			}*/
		}
		
		if (scope.parent != null) scope = scope.parent;
		comLine = old;
	}
	
	private static String runFunction(Command cmd, String ... params) { // Suorittaa funktion
		scope = new QScope(scope);
		for (int i = 0; i < params.length; i++) {
			String name = (String) cmd.params.keySet().toArray()[i]; // Etsii parametrit
			Type t = cmd.params.get(name);
			switch (t) {
			case Integer:
				scope.intVars.put("$" + name, Integer.parseInt(params[params.length-1-i])); // Parametrit käännetään toisinpäin
				break;
			case Double:
				scope.doubleVars.put("$" + name, Double.parseDouble(params[params.length-1-i])); // Parametrit käännetään toisinpäin
				break;
			case String:
				scope.stringVars.put("$" + name, (String) params[params.length-1-i]); // Parametrit käännetään toisinpäin
				break;
			default:
				break;
			}
			
		}
		String ret = null;
		ArrayList<String> vars = new ArrayList<String>();
		for (int i = 0; i < cmd.lines.length; i++){
			String com = cmd.lines[i];
			if (com.toLowerCase().startsWith("create new ")){
				String[] raw = com.split(" ");
				String type = raw[2];
				Type t = parseType(type);
				if (t == Type.String){
					String s3 = "";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.stringVars.put(raw[3], s3);
					vars.add(raw[3]);
				}
				if (t == Type.Integer){
					String s3 = "0";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.intVars.put(raw[3], Integer.parseInt(s3));
					vars.add(raw[3]);
				}
				if (t == Type.Double){
					String s3 = "0.0";
					if (raw.length > 5) {
						if (raw[4].equals("to")) {
							s3 = raw[5];
						}
					}
					scope.doubleVars.put(raw[3], Double.parseDouble(s3));
					vars.add(raw[3]);
				}
			}
			String k = runLine(com);
			if (k != null) {
				ret = k;
			}
		}
		
		if (auto_gc) { // Tuhoaa muuttujat
			/*for (String name : vars) {
				if (intVars.containsKey(name)) intVars.remove(name);
				if (stringVars.containsKey(name)) stringVars.remove(name);
				if (doubleVars.containsKey(name)) doubleVars.remove(name);
			}*/
		}
		
		if (scope.parent != null) scope = scope.parent;
		
		/*for (int i = 0; i < params.length; i++) {
			String name = (String) cmd.params.keySet().toArray()[i]; // tuhoaa parametrit
			Type t = cmd.params.get(name);
			switch (t) {
			case Integer:
				scope.intVars.remove("$" + name);
				break;
			case Double:
				scope.doubleVars.remove("$" + name);
				break;
			case String:
				scope.stringVars.remove("$" + name);
				break;
			default:
				break;
			}
			
		}*/ // TODO Testaa
		
		return ret;
	}

	private static String runLine(String com){ // suorittaa komennon
		if (com.toLowerCase().startsWith("proceed ")){
			String cmd = com.substring(com.indexOf(" ")).trim();
			Command command = scope.getCommand(cmd);
			if (command != null){
				runCommand(command);
			}
		} else if (com.toLowerCase().startsWith("delete ")){ // tuhoaa muuttujan
			String cmd = com.substring(com.indexOf(" ")).trim();
			if (scope.containsInt(cmd)){
				scope.intVars.remove(cmd);
			}
			if (scope.containsString(cmd)){
				scope.stringVars.remove(cmd);
			}
			if (scope.arrayVars.keySet().contains(cmd)){ // TODO
				scope.arrayVars.remove(cmd);
			}
			if (scope.uiMenuVars.keySet().contains(cmd)){
				scope.uiMenuVars.remove(cmd);
			}
		} else if (com.toLowerCase().startsWith("block")) {
			scope = new QScope(scope);
		} else if (com.toLowerCase().startsWith("endblock")) {
			if (scope.parent != null) scope = scope.parent;
		} else
		if (com.toLowerCase().startsWith("print ")){
			String param = getParam(com.substring(com.indexOf(" ")).trim());
			if (!uiInited) print(param);
			if (uiInited) printLineToUI(param);
		} else if (com.toLowerCase().startsWith("type ")){
			String param = getParam(com.substring(com.indexOf(" ")).trim());
			if (!uiInited) type(param);
			if (uiInited) printLineToUI(param);
		} else if (com.toLowerCase().startsWith("echo ")){
			String param = getParam(com.substring(com.indexOf(" ")).trim());
			type(param);
		} else if (com.toLowerCase().startsWith("endcreate")){
			runningCom = false;
		} else
		if (com.toLowerCase().startsWith("goto ")){
			String param = getParam(com.substring(com.indexOf(" ")).trim());
			goTo(param);
		} else
		if (com.toLowerCase().startsWith("if ")){
			boolean param = getBoolean(com.substring(com.indexOf(" "), com.toLowerCase().indexOf("then")-1).trim());
			if (param){
				runLine(com.substring(com.toLowerCase().indexOf("then")+4).trim());
			}
		} else
		if (com.toLowerCase().startsWith("set ")){
			String[] params = com.split(" ");
			
			if (params[1].equals("new")) {
				Type t = parseType(params[2]);
				if (t == Type.String){
					scope.stringVars.put(params[3], "");
				}
				if (t == Type.Integer){
					scope.intVars.put(params[3], 0);
				}
				if (t == Type.VarArray){
					Command arr = new Command();
					if (params.length == 6) {
						for (int i = 0; i < Integer.parseInt(params[3]); i++) arr.addLine("");
						if (params[4].toLowerCase().equals("endnew"))
							scope.arrayVars.put(params[5], arr);
					} else {
						scope.arrayVars.put(params[4], arr);
					}
					
				}
			}
			
			String strVar = scope.getString(params[1]);
			Integer ivar = scope.getInt(params[1]) != null ? scope.getInt(params[1]) : 0;
			Double dvar = scope.getDouble(params[1]) != null ? scope.getDouble(params[1]) : -1;
			
			if (strVar == null && scope.getInt(params[1]) == null){
				if (params[1].equals("getfrom") && params.length > 4) {
					if (scope.getArray(params[2]) != null) {
						Command arr = scope.getArray(params[2]);
						arr.setLine(Integer.parseInt(params[3]), params[4]);
						scope.arrayVars.put(params[2], arr);
					}
				}
			}
			else if (strVar != null && ivar == 0 && dvar == -1){
				scope.setString(params[1], getParam(com.substring(com.indexOf(" ", com.indexOf(" ")+1)).trim()));
			} else if (strVar == null && ivar != null && dvar == -1){
				String s = getParam(com.substring(com.indexOf(" ", com.indexOf(" ")+1)).trim());
				scope.setInt(params[1], new Integer((s != null ? (int)Double.parseDouble(s)+"" : "0")));
			}
			else if (strVar == null && ivar == 0 && dvar != null){
				String s = getParam(com.substring(com.indexOf(" ", com.indexOf(" ")+1)).trim());
				scope.setDouble(params[1], new Double(s != null ? s : "0"));
			}
		} else
		if (com.toLowerCase().startsWith("return ")){
			String param = getParam(com.substring(com.indexOf(" ")).trim());
			return param;
		} else if (com.toLowerCase().startsWith("uidraw ")){
			String[] params = com.substring(com.indexOf(' ')).trim().split("; ");
			int x = Integer.parseInt(getParam(params[0]));
			int y = Integer.parseInt(getParam(params[1]));
			uiMaxX = x;
			if (y > uiMaxY) uiMaxY = y;
			String c = getParam(params[2]);
			short d = CharColor.BLACK;
			short f = CharColor.BLACK;
			if (params.length == 5){
				switch (Integer.parseInt(params[3])){
				case 0x00:
					break;
				case 0x01:
					d = CharColor.RED;
					break;
				case 0x02:
					d = CharColor.GREEN;
					break;
				case 0x03:
					d = CharColor.YELLOW;
					break;
				case 0x04:
					d = CharColor.BLUE;
					break;
				case 0x05:
					d = CharColor.MAGENTA;
					break;
				case 0x06:
					d = CharColor.CYAN;
					break;
				case 0x07:
					d = CharColor.WHITE;
					break;
				}
				switch (Integer.parseInt(params[4])){
				case 0x00:
					break;
				case 0x01:
					f = CharColor.RED;
					break;
				case 0x02:
					f = CharColor.GREEN;
					break;
				case 0x03:
					f = CharColor.YELLOW;
					break;
				case 0x04:
					f = CharColor.BLUE;
					break;
				case 0x05:
					f = CharColor.MAGENTA;
					break;
				case 0x06:
					f = CharColor.CYAN;
					break;
				case 0x07:
					f = CharColor.WHITE;
					break;
				}
			}
			Toolkit.startPainting();
			Toolkit.printString(c, x, y, new CharColor(f, d, CharColor.REVERSE));
			Toolkit.endPainting();
		} else if (com.toLowerCase().startsWith("uipaint ")){
			String[] params = com.substring(com.indexOf(' ')).trim().split("; ");
			int x = Integer.parseInt(getParam(params[0]));
			int y = Integer.parseInt(getParam(params[1]));
			int c = Integer.parseInt(getParam(params[2]));
			uiMaxX = x;
			if (y > uiMaxY) uiMaxY = y;
			Toolkit.startPainting();
			short chr = CharColor.BLACK;
			switch (c){
			case 0x00:
				break;
			case 0x01:
				chr = CharColor.RED;
				break;
			case 0x02:
				chr = CharColor.GREEN;
				break;
			case 0x03:
				chr = CharColor.YELLOW;
				break;
			case 0x04:
				chr = CharColor.BLUE;
				break;
			case 0x05:
				chr = CharColor.MAGENTA;
				break;
			case 0x06:
				chr = CharColor.CYAN;
				break;
			case 0x07:
				chr = CharColor.WHITE;
				break;
			}
			Toolkit.drawRectangle(x, y, 1, 1, new CharColor(chr, CharColor.BLACK, CharColor.REVERSE));
			Toolkit.endPainting();
		} else if (com.toLowerCase().startsWith("uimsg ")){
			String[] params = com.substring(com.indexOf(' ')).trim().split("; ");
			if (params.length == 3){
				String name = getParam(params[0]);
				String text = getParam(params[1]);
				String butn = getParam(params[2]);
				new Message(name, text, butn).show();
			} else {
				String param = com.substring(com.indexOf(' ')).trim();
				String x = getParam(param);
			
				new Message("Q# Console", x, "OK").show();
			}
			
		} else if (com.toLowerCase().startsWith("uiclear")){
			Toolkit.clearScreen(new CharColor(CharColor.BLACK, CharColor.WHITE, CharColor.REVERSE));
			uiMaxX = 0;
			uiMaxY = 0;
		} else if (com.toLowerCase().startsWith("uiwindow")){
			String[] params = com.substring(com.indexOf(' ')).trim().split("; ");
			if (params.length == 3){
				String sx = getParam(params[0]);
				String sy = getParam(params[1]);
				String name = getParam(params[2]);
				new Window(Integer.parseInt(sx), Integer.parseInt(sy), true, name).show();
			} else {
				String param = com.substring(com.indexOf(' ')).trim();
				String x = getParam(param);
			
				new Window(4, 4, true, x).show();
			}
		} else if (com.toLowerCase().startsWith("uinewmenu ")){
			String cmd = com.substring(com.indexOf(" ")).trim();
			String name = cmd.substring(0, cmd.indexOf(" ")).trim();
			String[] params = cmd.substring(cmd.indexOf(" ")).trim().split("; ");
			if (params.length == 2){
				String title = getParam(params[0]);
				String text = getParam(params[1]);
				MenuDialog menu = new MenuDialog(title, text);
				scope.uiMenuVars.put(name, menu);
			} else {
				String title = getParam(cmd.substring(cmd.indexOf(" ")).trim());
				MenuDialog menu = new MenuDialog(title);
				scope.uiMenuVars.put(name, menu);
			}
			
		} else if (com.toLowerCase().startsWith("uimenu ")){
			String params = com.substring(com.indexOf(" ")).trim();
			String name = params.substring(0, params.indexOf(" ")).trim();
			String cmd = params.substring(params.indexOf(" ")).trim();
			MenuDialog n = scope.getUiMenuVar(name);
			if (cmd.toLowerCase().startsWith("add")){
				String param = cmd.substring(cmd.indexOf(" ")).trim();
				n.add(getParam(param));
			}
			if (cmd.toLowerCase().startsWith("show")){
				n.show();
			}
		} else if (com.toLowerCase().startsWith("initui")){
			uiInited = true;
			Toolkit.init();
		} else if (com.toLowerCase().startsWith("exitui")){
			uiInited = false;
			Toolkit.clearScreen(new CharColor(CharColor.BLACK, CharColor.WHITE, CharColor.REVERSE));
			Toolkit.shutdown();
		} else if (com.toLowerCase().equals("wait press")){
			Toolkit.readCharacter();
		} else if (com.toLowerCase().equals("end")){
			Toolkit.shutdown();
			System.exit(0);
		} //else print("Unknow command: " + com);
		return null;
	}
	
	private static void printLineToUI(String param) { // Printtaa ui-tilassa rivin
		Toolkit.startPainting();
		uiMaxY++;
		Toolkit.printString(param, uiMaxX, uiMaxY, new CharColor(CharColor.BLACK, CharColor.WHITE, CharColor.REVERSE));
		Toolkit.endPainting();
	}

	private static void goTo(String param) { // parsii goto-käskyn
		
		if (param.toLowerCase().equals("enddo")) {
			runningLoop = false;
			return;
		}
		
		int line = Integer.parseInt(param);
		/*fileIn.close();
		try {
			userIn = new Scanner(System.in);
			if (args1.length == 2 && args1[0].equals("-F")){
				fileIn = new Scanner(new FileInputStream(new File(args1[1])));
				runFile = true;
			}
		} catch (Exception e) {
			print("Q# Console has crashed! Error:");
			e.printStackTrace();
		}
		for (int i = 0; i < line; i++){
			fileIn.nextLine();
		}*/
		comLine = line;
		
	}

	private static boolean getBoolean(String s) { // Parsii booleanin
		s = s.replaceAll(" ", "");
		if (s.matches(".*<.*")){
			String var1 = getParam(s.substring(0, s.indexOf("<")));
			String var2 = getParam(s.substring(s.indexOf("<")+1));
			double nm1 = Double.parseDouble(var1);
			double nm2 = Double.parseDouble(var2);
			return nm1 < nm2;
		}
		if (s.matches(".*>.*")){
			String var1 = getParam(s.substring(0, s.indexOf(">")));
			String var2 = getParam(s.substring(s.indexOf(">")+1));
			double nm1 = Double.parseDouble(var1);
			double nm2 = Double.parseDouble(var2);
			return nm1 > nm2;
		}
		if (s.matches(".*=.*")){
			String var1 = getParam(s.substring(0, s.indexOf("=")));
			String var2 = getParam(s.substring(s.indexOf("=")+1));
			try {
				double nm1 = Double.parseDouble(var1);
				double nm2 = Double.parseDouble(var2);
				return nm1 == nm2;
			} catch (NumberFormatException ex){
				return var1.equals(var2);
			}
			
		}
		if (s.matches(".*|.*")){
			String var1 = getParam(s.substring(0, s.indexOf("|")));
			String var2 = getParam(s.substring(s.indexOf("|")+1));
			try {
				double nm1 = Double.parseDouble(var1);
				double nm2 = Double.parseDouble(var2);
				return nm1 != nm2;
			} catch (NumberFormatException ex){
				return !var1.equals(var2);
			}
		}
		return false;
	}

	private static String getParam(String com) { // Etsii minkätahansa arvon (muuttujasta, funktiosta, arraysta, yms)
		String strParam = com.startsWith("\"") ? com.substring(1, com.lastIndexOf('"')) : null;
		if (strParam == null || strParam.equals("")){
			if (com.toLowerCase().startsWith("getfrom ")){
				String[] params = com.split(" ");
				return getFrom(params[1], params[2]);
			} else if (com.toLowerCase().startsWith("$")){
				String[] params = com.split(" ");
				String var = scope.getString(params[0]);
				MenuDialog var2 = scope.getUiMenuVar(params[0]);
				if (var != null){
					return var;
				}
				if (var2 != null){
					return var2.toString();
				}
				if (scope.getInt(params[0]) == null) {
					if (scope.getDouble(params[0]) != null) {
						double ivar = scope.getDouble(params[0]);
						return ivar+"";
					}
					throwException(new VariableNotFoundException("Cannot find variable: " + params[0]), curLine, null);
				} else {
					int ivar = scope.getInt(params[0]);
					return ivar+"";
				}
				
			} else if (com.toLowerCase().startsWith("?")){
				String[] params = com.split(" ");
				Command c = scope.getFunction(params[0]);
				String[] fParams = new String[c.params.size()];
				for (int i = 0; i < fParams.length; i++) {
					String param = "";
					if (params[1].startsWith("\"")){
						if (params[1].endsWith("\"")) {
							param += params[1].substring(1, params[1].length()-1);
						} else {
							for (int j = 0;;j++) {
								if (params[j+2].endsWith("\"")){
									param += params[j+2].substring(0, params[j+2].length());
									break;
								} else {
									param += params[j+2];
								}
							}
						}
						fParams[i] = param;
					} else {
						String[] fp = com.split(";");
						fp[0] = fp[0].substring(params[0].length()+1);
						fParams[i] = getParam(fp[i].trim());
					}
				}
				
				return runFunction(c, fParams);
			} else if (com.toLowerCase().startsWith("!")){
				throwException(new QSharpException("Not supported yet!"), curLine, null);
			} else if (com.toLowerCase().startsWith("x#")){
				String s = com.substring(2);
				return Integer.decode("#"+s)+"";
			} else if (com.toLowerCase().startsWith("[")){
				if (com.indexOf(']') < 0) {
					throwException(new QSharpException("Parse exception: Missing ']'."), curLine, null);
				}
				String formula = com.substring(1, com.lastIndexOf(']'));
				String answer1 = calculateSingleFormula(formula)+"";
				if (answer1.endsWith(".0")) answer1 = answer1.substring(0, answer1.length()-2);
				return answer1;
			} else if (com.toLowerCase().startsWith("(")){
				String formula = com.substring(1, com.lastIndexOf(')'));
				return concatStrings(formula)+"";
			} else if (com.toLowerCase().startsWith("execute ")){
				String[] params = com.split(" ");
				String rnd = params[1].startsWith("\"") ? params[1].substring(1, params[1].lastIndexOf('"')) : getParam(params[1]);
				InputStream is;
				try {
					is = Runtime.getRuntime().exec(rnd).getInputStream();
					return new BufferedReader(new InputStreamReader(is)).readLine();
				} catch (IOException e) {throwException(new QSharpException(""), curLine, e);}
			} else if (com.toLowerCase().trim().equals("input")){
				return uiInited ? getInput() : userIn.nextLine();
			} else if (com.toLowerCase().trim().startsWith("rnd ")){
				String[] params = com.split(" ");
				int rnd = Integer.parseInt(params[1]);
				return (new Random()).nextInt(rnd)+"";
			} else if (com.toLowerCase().startsWith("uimenu ")){
				String params = com.substring(com.indexOf(" ")).trim();
				String name = params.substring(0, params.indexOf(" ")).trim();
				String cmd = params.substring(params.indexOf(" ")).trim();
				MenuDialog n = scope.getUiMenuVar(name);
				if (cmd.toLowerCase().startsWith("index")){
					return n.getSelectedIndex()+"";
				}
				if (cmd.toLowerCase().startsWith("text")){
					return n.getSelectedItem();
				}
			} else if (com.toLowerCase().startsWith("uiyesno ")){
				String[] params = com.substring(com.indexOf(' ')).trim().split("; ");
				if (params.length == 4){
					String name = getParam(params[0]);
					String text = getParam(params[1]);
					String yes = getParam(params[2]);
					String no = getParam(params[3]);
					YesNoDialog d = new YesNoDialog(name, text, yes, no);
					d.show();
					return d.getSelectedItem();
				} else {
					String param = com.substring(com.indexOf(' ')).trim();
					String x = getParam(param);
				
					new Message("Q Console", x, "OK").show();
				}
				
			}  
		} else {
			return strParam;
		}
		com = com.trim();
		return com.contains(" ") ? com.substring(0, com.lastIndexOf(' ')) : com;
	}

	private static String getInput() { // Ottaa user-inputin UI-tilassa
		Toolkit.startPainting();
		InputChar c = Toolkit.readCharacter();
		String text = "";
		
		uiMaxY++;
		
		while (c != null) {
		    if ((c.isSpecialCode() && c.getCode() != '\n') && !text.isEmpty()) {
		    	String s = "";
		    	
		    	for (int i = 0; i < text.length(); i++) s+=" ";
		    	
		    	Toolkit.printString("? " + s, uiMaxX, uiMaxY, new CharColor(CharColor.BLACK, CharColor.WHITE));
		    	text = text.substring(0, text.length()-1);
		    	Toolkit.printString("? " + text, uiMaxX, uiMaxY, new CharColor(CharColor.BLACK, CharColor.WHITE));
		    }
		    else {
		    	//if (c.getCharacter() == 0) continue;
		    	//if (c.getCharacter() == 54) continue;
		    	//if (c.getCharacter() == 42) continue;
		    	//if (c.getCharacter() == 0) continue; //LCONTROL
		    	//if (c.getCharacter() == 0) continue; //RCONTROL
		    	//if (c.getCharacter() == 56) continue;
		    	//if (c.getCharacter() == 184) continue;
		    	if (c.getCode() == '\n') break;
		    	if (!c.isSpecialCode()) text += c.getCharacter();
		    	Toolkit.printString("? " + text, uiMaxX, uiMaxY, new CharColor(CharColor.BLACK, CharColor.WHITE));
		    	c = Toolkit.readCharacter();
		    }
		}
		Toolkit.endPainting();
		return text;
	}

	private static String concatStrings(String formula) { // liittää yhteen 2 stringiä, esim ("ab" + "c")
		formula = formula.replace('+', '+');
		formula = formula.replaceAll(" \\+ ", "+");
		String[] strs = formula.split("\\+");
		String answer = "";
		for (int i = 0; i < strs.length; i++){
			String str = getParam(strs[i]);
			answer += str;
		}
		return answer;
	}

	private static double calculateSingleFormula(String formula){ // parsii laskutoimituksen
		
		    boolean bool = true;

		    //alert(str);

		    while (bool) {
		        int sulku1 = formula.lastIndexOf("[");
		        int sulku2 = formula.indexOf("]");
		        if (sulku1 == -1 || sulku2 == -1) break;
		        int i1 = 1;
		        while (sulku1 > sulku2) { sulku2 = formula.indexOf("]", i1); i1++; }

		        i1 = 2;
		        //String nimi = ""+formula.charAt(sulku1-1);
		        //while ((""+formula.charAt(sulku1-i1)).matches("[A-Za-z]")) { 
		        //	nimi = formula.charAt(sulku1-i1) + nimi; i1++; }

		        String[] parametrit = formula.substring(sulku1+1, sulku2).split(",");

		        String s = ""+calculateSingleFormula(formula.substring(sulku1+1, sulku2)); 

		        formula = formula.replace(formula.substring(sulku1, sulku2+1), s);
		    }
		
		ArrayList<String> fml = new ArrayList<String>();
		String [] raw = formula.split(" ");
		
		for (int i = 0; i < raw.length; i++){
			String j = raw[i];
			if (!j.matches("(\\d){1,"+Integer.MAX_VALUE+"}(\\.\\d){0,64}") && !j.contains("+") 
					&& !j.contains("-") 
					&& !j.contains("*") 
					&& !j.contains("/") 
					&& !j.contains("^") 
					&& !j.contains("\\")) {
				if (!explicit) {
					j = "0";
					if (scope.containsInt(raw[i])) j = scope.getInt(raw[i])+"";
					else if (scope.containsDouble(raw[i])) j = scope.getDouble(raw[i])+"";
					else if (scope.containsString(raw[i])) j = scope.getString(raw[i])+"";
				} else {
					if (scope.containsInt(raw[i])) j = scope.getInt(raw[i])+"";
					else if (scope.containsString(raw[i])) j = scope.getString(raw[i])+"";
					else if (scope.containsDouble(raw[i])) j = scope.getDouble(raw[i])+"";
					else if (j.startsWith("$")) throwException(new VariableNotFoundException("Cannot find variable: " + j), curLine, null);
					else throwException(new ParseException("Bad integer: " + j), curLine, null);
				}
			}
			//if (intVars.containsKey(raw[i])) j = intVars.get(raw[i])+"";
			//if (stringVars.containsKey(raw[i])) j = stringVars.get(raw[i])+"";
			fml.add(j);
		}
		
		return Double.parseDouble(parseFormula(formula, fml).get(0));
		
	}
	
	private static ArrayList<String> parseFormula(String formula, ArrayList<String> fml){ // Suorittaa laskutoimituksen
		boolean doDot = false;
		boolean doDot2 = false;
		
		if (fml.contains("*")){
			doDot = true;
		}
		if (fml.contains("/")){
			doDot = true;
		}
		
		if (fml.contains("^")){
			doDot2 = true;
		}
		
		if (fml.contains("\\")){
			doDot2 = true;
		}
		
		for (int i = 0; i < fml.size()-1; i++){
			if (fml.size() < 3) break;
			if (i % 2 != 0) continue;
			
			double n1 = Double.parseDouble(fml.get(i));
			double n2 = Double.parseDouble(fml.get(i+2));
			String operator = fml.get(i+1);
			if (operator.equals("^")){
				fml.set(i, (Math.pow(n1, n2))+"");
				fml.remove(i+1);
				fml.remove(i+1);
				return parseFormula(formula, fml);
			}
			if (operator.equals("\\")){
				fml.set(i, (n1 % n2)+"");
				fml.remove(i+1);
				fml.remove(i+1);
				return parseFormula(formula, fml);
			}
			if (!doDot2) {
				if (operator.equals("*")){
					fml.set(i, (n1 * n2)+"");
					fml.remove(i+1);
					fml.remove(i+1);
					return parseFormula(formula, fml);
				}
				if (operator.equals("/")){
					if (n2 == 0 && explicit) {
						throwException(new ArithmeticException("Divide by zero"), curLine, null);
					} else if (n2 == 0) n2 = 1;
					fml.set(i, (n1 / n2)+"");
					fml.remove(i+1);
					fml.remove(i+1);
					return parseFormula(formula, fml);
				}
				if (!doDot){
					if (operator.equals("+")){
						fml.set(i, (n1 + n2)+"");
						fml.remove(i+1);
						fml.remove(i+1);
						return parseFormula(formula, fml);
					}
					if (operator.equals("-")){
						fml.set(i, (n1 - n2)+"");
						fml.remove(i+1);
						fml.remove(i+1);
						return parseFormula(formula, fml);
					}
				}
			}
		}
		return fml;
	}
	
	private static String getFrom(String var, String id) { // Etsii varArraysta arvon
		int id2 = Integer.parseInt(id);
		String s = "";
		Command c = scope.getArray(var);
		s = c.getLine(id2);
		
		if (s.startsWith("\"")) s = s.substring(1, s.length()-1);
		
		return s;
	}

	private static void print(String string) { // Print - komento
		System.out.println(string);
	}
	private static void type(String string) { // Type - komento (ilman \n:ää)
		System.out.print(string);
	}
	
	public static String IDENTIFIER = "([_,\\$,a-z,A-Z]|[0-9])+";
	
	public static String STRING_LITERAL = "\"\\\"\"((~[\"\\\"\", \"\\\\\"])"+
			  "| (\"\\\\\"([\"n\", \"t\", \"b\", \"r\", \"f\", \"\\\\\", \"'\"]"+
			           "| [\"0\"-\"7\"]([\"0\"-\"7\"])?"+
			           "| [\"0\"-\"3\"][\"0\"-\"7\"][\"0\"-\"7\"])))*\"\\\"\"";
	
	public static String TYPE = "(int|" +
			"string|" +
			"double|" +
			"vararray|" +
			"cmd|" +
			"function)"; 
	
	public static String PARAMETERS = "withparams(\\s)?" + // Withparams regex
			"\\[(\\s)?" +
			TYPE + "(\\s)?" +
			IDENTIFIER + "(\\s)?" +
			"(" +
			"\\,(\\s)?" +
			TYPE + "(\\s)?" +
			IDENTIFIER + "" +
			")*(\\s)?" +
			"\\](\\s)?";

}
