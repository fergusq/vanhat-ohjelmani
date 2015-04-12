package org.kaivos.stkc.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.kaivos.sc.TokenScanner;
import org.kaivos.stg.error.SyntaxError;
import org.kaivos.stkc.parser.StkCTree;
import org.kaivos.stkc.parser.StkCTree.Expression3Tree;
import org.kaivos.stkc.parser.StkCTree.ExpressionTree;
import org.kaivos.stkc.parser.StkCTree.FunctionTree;
import org.kaivos.stkc.parser.StkCTree.LineTree;
import org.kaivos.stkc.parser.StkCTree.LineTree.LineType;
import org.kaivos.stkc.parser.StkCTree.PrimaryTree;
import org.kaivos.stkc.parser.StkCTree.PrimaryTree.Operator;
import org.kaivos.stkc.parser.StkCTree.StructTree;

public class StkCCompiler {

	enum Datatype_enum {
		VOID,
		NUMBER,
		POINTER,
		STRUCT
	}
	
	static class Var {
		
		public Var(String n, int p, String t) {
			name = n;
			pos = p;
			type = t;
		}
		
		String name;
		int pos;
		String type;
		
		@Override
		public String toString() {
			return pos + "";
		}
	}
	
	Datatype_enum dt_do(String t, boolean allowStructs) throws CompilerError {
		switch (t) {
		case "void": return Datatype_enum.VOID;
		case "int": case "char": case "float": return Datatype_enum.NUMBER;
		default:
			if (allowStructs) {
				if (structs.containsKey(t)) {
					return Datatype_enum.STRUCT;
				}
			}
			if (t.endsWith("*")) {
				dt_do(t.substring(0, t.length()-1), true);
				return Datatype_enum.POINTER;
			}
			throw new CompilerError();
		}
	}
	
	Datatype_enum dt(String t) throws CompilerError {
		return dt_do(t, false);
	}
	
	boolean compatible(String a, String b) {
		if ((a.endsWith("*") && (b.equals("int") || b.equals("char") || b.equals("float"))) ||
				(b.endsWith("*") && (a.equals("int") || a.equals("char") || a.equals("float")))) {
			return true;
		} else if ((a.endsWith("*") && b.equals("void*")) ||
				(b.endsWith("*") && a.equals("void*"))) {
			return true;
		} else if ((a.equals("int") || a.equals("char") || a.equals("float")) && (b.equals("int") || b.equals("char") || b.equals("float")) ||
				((b.equals("int") || b.equals("char") || b.equals("float")) && (a.equals("int") || a.equals("char") || a.equals("float")))) {
			return true;
		} else {
			return a.equals(b);
		}
	}
	
	String changeTypes(String a, String b) {
		switch (a) {
		case "void":
			return b;
		case "int":
			switch (b) {
			case "float":
				return "float";
			case "int":
				return "int";
			case "char":
				return "int";
			default:
				if (b.endsWith("*")) return b;
				return a;
			}
		case "float":
			switch (b) {
			case "float":
				return "float";
			case "int":
				return "float";
			case "char":
				return "float";
			default:
				if (b.endsWith("*")) return b;
				return a;
			}
		case "char":
			switch (b) {
			case "float":
				return "float";
			case "int":
				return "int";
			case "char":
				return "char";
			default:
				if (b.endsWith("*")) return b;
				return a;
			}
		default:
			if (b.endsWith("*")) return b;
			return a;
		}
	}
	
	private PrintStream out;
	private StringBuffer natives = new StringBuffer();
	
	public HashMap<String, StkCTree.FunctionTree> functions;
	public HashMap<String, StkCTree.StructTree> structs;
	
	public String func;
	private StkCTree.FunctionTree func1;
	private HashMap<String, Var> vars;
	
	private int counter = 1;
	private int ncount = 0;
	
	public StkCCompiler() {
		out = System.out;
		functions = new HashMap<>();
		structs = new HashMap<>();
		vars = new HashMap<>();
	}
	
	public void compile(StkCTree.StartTree tree) throws CompilerError {
		out.println("#__init pushr popr\n");
		out.println("__init:\n" +
				"	40 0 printm 1 1 printm 0 2 printm 10000 3 printm\n" +
				"	#__main pushr\n" +
				"	popr\n");
		out.println("__main:\n" +
				"	0 #main pushp\n" +
				"	popr\n");
		
		for (StkCTree.TreeNode t1 : tree.functions) {
			if (t1 instanceof FunctionTree) {
				StkCTree.FunctionTree f = (FunctionTree) t1;
				if (functions.containsKey(f.name)) {
					if (f.lines != null && functions.get(f.name).lines != null) {
						System.err.println("; E: " + f.name
								+ " already defined!");
						throw new CompilerError();
					}
					{
						boolean error = false;
						if (functions.get(f.name).parameters.size() == f.parameters.size())
							for (int i = 0; i < functions.get(f.name).parameters.size(); i++) {
								if (!functions.get(f.name).parameters.get(i).datatype.equals(f.parameters.get(i).datatype))
									error = true;
							}
						else {
							error = true;
						}
						if (!f.datatype.equals(functions.get(f.name).datatype)) error = true;
						if (error) {
							System.err.println("; E: \""
									+ f.datatype + " "
									+ f.name + "(" + f.parameters + ")\""
									+ " already defined as \""
									+ functions.get(f.name).datatype + " " 
									+ f.name + "("
									+ functions.get(f.name).parameters + ")\"!");
							throw new CompilerError();
						}
					}
					if (f.lines != null && !f._native)
						compileFunction(f);
				} else {
					functions.put(f.name, f);
					if (f.lines != null)
						compileFunction(f);
					else if (f._native) compileNativeFunction(f);
				}
			} else if (t1 instanceof StructTree) {
				StructTree f = (StructTree) t1;
				if (f.variables != null && 
						structs.get(f.name) != null && 
						structs.get(f.name).variables != null) {
					System.err.println("; E: " + f.name
							+ " already defined!");
					throw new CompilerError();
				}
				structs.put(f.name, f);
			}
		}
		
		natives.append("int functions(){return " + ncount + ";}\n");
		
		if (ncount > 0) try (PrintWriter w = new PrintWriter(new File("___natives.c"))){
			w.println(natives.toString());
			w.flush();
			//w2.println("gcc -c -fPIC ___natives.c -o ___natives.o");
			//w2.println("gcc -shared -Wl,-soname,libnatives.so.1 -o libnatives.so.1.0.1  ___natives.o");
			//w2.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void compileFunction(StkCTree.FunctionTree tree) throws CompilerError {
		out.println(tree.name + ":");//; " + tree.datatype + ": " + tree.parameters);
		//out.println("	inits(" + tree.variables.size() + ")");//; " + tree.variables);
		out.print("	"); for (int i = 0; i < tree.variables.size(); i++) out.print("0 "); out.println();
		out.println("	flip");
		
		vars.clear();
		
		for (int i = 0; i < tree.variables.size(); i++) {
			vars.put(tree.variables.get(i).name, new Var(tree.variables.get(i).name, i, (tree.variables.get(i).datatype)));
		}
		
		for (int i = 0; i < tree.parameters.size(); i++) {
			vars.put(tree.parameters.get(i).name, new Var(tree.parameters.get(i).name, tree.variables.size()+i, (tree.parameters.get(i).datatype)));
		}
		
		func = tree.name;
		func1 = tree;
		
		for (LineTree tree1 : tree.lines) {
			compileLine(tree1);
		}
		
		out.println("	popr\n");
	}
	
	public void compileNativeFunction(StkCTree.FunctionTree tree) throws CompilerError {
		out.println(tree.name + ":");//; " + tree.datatype + ": " + tree.parameters);
		out.println("	flip");
		

		ArrayList<Var> vars = new ArrayList<>();
		for (int i = 0; i < tree.parameters.size(); i++) {
			vars.add(new Var(tree.parameters.get(i).name, i, (tree.parameters.get(i).datatype)));
		}
		
		func = tree.name;
		func1 = tree;
		
		natives.append("#include " + tree.__from + "\n");
		
		int i = 0;
		
		for (Var var : vars) {
			if (var.pos != i) {
				// ERROR
				natives.append("/* ERROR */\n");
			}
			
			out.printf("\t" + i + " overf " + ncount + " callf pop\n");
			
			natives.append(var.type + " _" + func + "_" + var.name + ";\n");
			natives.append("double func" + ncount++ + "(double d){ /* "+var.name+" */\n" +
					"\t" + " _" + func + "_" + var.name + " = ("+var.type+")d;\n\treturn 0;\n}\n");
			
			i++;
		}
		
		out.printf("\t0 " + ncount + " callf\n");
		
		natives.append("double func" + ncount++ + "(double d){ /* "+func+" */\n" +
				"\t" + (tree.datatype.equals("void")?"":("return (double)")) + func + "(");
		
		i = 0;
		
		for (Var var : vars) {
			if (var.pos != i) {
				// ERROR
				natives.append("/* ERROR */\n");
			}
			
			natives.append((i!=0?", ":"") + "(" + var.type + ")_" + func + "_" + var.name);
			
			i++;
		}
		natives.append(");\n");
		if (tree.datatype.equals("void")) natives.append("\treturn 0;\n}\n");
		else natives.append("}\n");
		
		out.println("	popr\n");
	}

	public void compileLine(LineTree tree) throws CompilerError {
		if (tree.type == LineType.BLOCK) {
			for (LineTree tree1 : tree.block.lines) {
				compileLine(tree1);
			}
		}
		if (tree.type == LineType.RETURN) {
			out.print("	");
			compileExpression(tree.expression);
			out.println("popr");
		}
		if (tree.type == LineType.EXPRESSION) {
			out.print("	");
			compileExpression(tree.expression);
			out.println("pop");
		}
		if (tree.type == LineType.WHILE) {
			int loopId = counter++;
			out.println("	" + func + "__while" + loopId + ":");
			
			out.print("	");
			compileExpression(tree._while.condition);
			out.println("not #" + func+"__while" + loopId + "_exit exch goto");
			
			compileLine(tree._while.line);
			
			out.println("	#"+func+"__while" + loopId + " 1 goto");
			out.println("	"+func+"__while" + loopId + "_exit:");
		}
		if (tree.type == LineType.DO_WHILE) {
			int loopId = counter++;
			out.println("	" + func + "__dowhile" + loopId + ":");
			
			compileLine(tree.doWhile.line);
			
			out.print("	");
			compileExpression(tree.doWhile.condition);
			out.println("not #" + func+"__dowhile" + loopId + "_exit exch goto");
			
			out.println("	#"+func+"__dowhile" + loopId + " 1 goto");
			out.println("	"+func+"__dowhile" + loopId + "_exit:");
		}
		if (tree.type == LineType.FOR) {
			int loopId = counter++;
			out.print("	"); compileExpression(tree._for.assign); out.println("pop");
			out.println("	" + func + "__for" + loopId + ":");
			
			out.print("	");
			compileExpression(tree._for.condition);
			out.println("not #" + func+"__for" + loopId + "_exit exch goto");
			
			compileLine(tree._for.line);
			
			out.print("	"); compileExpression(tree._for.increment); out.println("pop");
			
			out.println("	#"+func+"__for" + loopId + " 1 goto");
			out.println("	"+func+"__for" + loopId + "_exit:");
		}
		if (tree.type == LineType.IF) {
			int loopId = counter++;
			out.println("	" + func + "__if" + loopId + ":");
			
			out.print("	");
			compileExpression(tree._if.condition);
			out.println("not #" + func+"__if" + loopId + "_else exch goto");
			
			compileLine(tree._if.line);

			out.println("	#" + func+"__if" + loopId + "_exit 1 goto");
			
			out.println("	"+func+"__if" + loopId + "_else:");
			
			if (tree._if.line_else != null) {
				compileLine(tree._if.line_else);
			}
			
			out.println("	"+func+"__if" + loopId + "_exit:");
		}
		
		if (tree.type == LineType.LABEL) {
			out.println("	" + func + "__label_" + tree.labelOrCode + ":");
		}
		
		if (tree.type == LineType.GOTO) {
			out.println("	#" + func + "__label_" + tree.labelOrCode + " 1 goto");
		}
		
		if (tree.type == LineType.ASM) {
			String code = tree.labelOrCode;
			String newCode = "";
			
			boolean varMode = false;
			String var = "";
			
			for (int i = 0; i < code.length(); i++) {
				char c = code.charAt(i);
				
				if (varMode) {
					if (c == ' ') {
						varMode = false;
						
						if (vars.containsKey(var)) {
							newCode += vars.get(var);
						} else {
							System.err.println("; E: ("+func+") Variable " + var + " not defined!");
							throw new CompilerError();
						}
						
						var = "";
						newCode += ' ';
					}
					else if (c == ':') {
						varMode = false;
						newCode += ':';
					}
					else {
						var += c;
					}
				}
				
				else if (c == ':') varMode = true;
				else newCode += c;
			}
			out.println("	" + newCode);
		}
	}

	public String compileExpression(ExpressionTree tree) throws CompilerError {
		switch (tree.operator) {
		case OPERATOR:
			StkCTree.ExpressionLogicTree first = tree.first;
			compileExpressionLogic(first);
			for (int i = 0; i < tree.op.size(); i++) {
				compileExpressionLogic(tree.second.get(i));
				switch (tree.op.get(i)) {
				case "||":
					out.print("or ");
					break;
				case "&&":
					out.print("and ");
					break;

				default:
					break;
				}
			}
			return "int";

		default:
			return compileExpressionLogic(tree.first);
		}
	}
	
	public String compileExpressionLogic(StkCTree.ExpressionLogicTree tree) throws CompilerError {
		switch (tree.operator) {
		case OPERATOR:
			StkCTree.Expression0Tree first = tree.first;
			compileExpression0(first);
			for (int i = 0; i < tree.op.size(); i++) {
				compileExpression0(tree.second.get(i));
				switch (tree.op.get(i)) {
				case "<":
					out.print("lt ");
					break;
				case ">":
					out.print("gt ");
					break;
					
				case "<=":
					out.print("exch gt ");
					break;

				case "=>":
					out.print("exch lt ");
					break;
					
				case "==":
					out.print("eq ");
					break;
					
				case "!=":
					out.print("eq not ");
					break;
					
				default:
					break;
				}
			}
			return "int";

		default:
			return compileExpression0(tree.first);
		}
	}
	
	public String compileExpression0(StkCTree.Expression0Tree tree) throws CompilerError {
		switch (tree.operator) {
		case OPERATOR:
			String lT;
			StkCTree.Expression1Tree first = tree.first;
			lT = compileExpression1(first);
			for (int i = 0; i < tree.op.size(); i++) {
				String nT = compileExpression1(tree.second.get(i));
				if (!compatible(lT, nT)) {
					System.err.println("; E: (" + func + ") Incompatible types " + lT + " and " + nT);
					throw new CompilerError();
				}
				lT = changeTypes(lT, nT);
				switch (tree.op.get(i)) {
				case "+":
					out.print("add ");
					break;
				case "-":
					out.print("sub ");
					break;
					
				default:
					break;
				}
			}
			return lT;

		default:
			return compileExpression1(tree.first);
		}
	}
	
	public String compileExpression1(StkCTree.Expression1Tree tree) throws CompilerError {
		switch (tree.operator) {
		case OPERATOR:
			String lT;
			StkCTree.Expression2Tree first = tree.first;
			lT = compileExpression2(first);
			for (int i = 0; i < tree.op.size(); i++) {
				String nT = compileExpression2(tree.second.get(i));
				if (!compatible(lT, nT)) {
					System.err.println("; E: (" + func + ") Incompatible types " + lT + " and " + nT);
					throw new CompilerError();
				}
				lT = changeTypes(lT, nT);
				switch (tree.op.get(i)) {
				case "*":
					out.print("mul ");
					break;
				case "/":
					out.print("div ");
					break;
				case "%":
					out.print("mod ");
					break;
				case "^":
					out.print("xor ");
					break;
				case "&":
					out.print("and ");
					break;
				case "|":
					out.print("or ");
					break;
					
				default:
					break;
				}
			}
			return lT;

		default:
			return compileExpression2(tree.first);
		}
	}
	
	public String compileExpression2(StkCTree.Expression2Tree tree) throws CompilerError {
		switch (tree.operator) {
		case OPERATOR:
			StkCTree.Expression3Tree first = tree.first;
			String lDt = "";
			for (int i = 0; i < tree.op.size(); i++) {
				String cDt = compileExpression3(tree.second.get(i), false);
				switch (tree.op.get(i)) {
				case "=":
					Expression3Tree lvalue1 = i == 0 ? first : tree.second.get(i-1);
					if (lvalue1.operator == Expression3Tree.Operator.NEXT)
					{
						PrimaryTree lvalue = lvalue1.first;
						if (lvalue.operator == Operator.VARIABLE) {
							if (!vars.containsKey(lvalue.first)) {
								System.err.println("; E: (" + func + ") "+lvalue.first+" not resolved");
								throw new CompilerError();
							}
							lDt = vars.get(lvalue.first).type;
							if (!compatible(lDt, cDt)) {
								System.err.println("; W: ("+func+") Type mismatch: cannot convert from " + cDt + " to " + lDt);
							}
							out.print(vars.get(lvalue.first) + " dupt ");
						} else if (lvalue.operator == Operator.POINTER) {
							out.print("dup ");
							lDt = compileExpression3(lvalue.expr, false);
							
							if (lDt.endsWith("*")) lDt = lDt.substring(0, lDt.length()-1);
							
							if (!compatible(lDt, cDt)) {
								System.err.println("; W: ("+func+") Type mismatch: cannot convert from " + cDt + " to " + lDt);
							}
							
							out.print("printm ");
						} else {
							System.err.println("; E: (" + func + ") The left-hand side of an assignment must be a variable.");
							throw new CompilerError();
						}
					} else {
						compileExpression3(lvalue1, true);
						out.print("printm 0 ");
					}
					break;
				default:
					break;
				}
				lDt = cDt;
			}
			return lDt;

		default:
			return compileExpression3(tree.first, false);
		}
	}
	
	public String compileExpression3(StkCTree.Expression3Tree tree, boolean write) throws CompilerError {
		switch (tree.operator) {
		case OPERATOR:
			StkCTree.PrimaryTree first = tree.first;
			String lDt = compilePrimary(first);
			for (int i = 0; i < tree.op.size(); i++) {
				String cDt = "";//i == 0 ? compilePrimary(tree.second.get(i)) : lDt;
				switch (tree.op.get(i)) {
				case "[":
					cDt = compilePrimary(tree.second.get(i));
					if(!cDt.equals("int")) {
						System.err.println("; W: ("+func+") Type mismatch: cannot convert from " + cDt + " to int ");
					}
					out.print("add ");
					if (!write || i != tree.op.size()-1) out.print("readm ");
					lDt = lDt.endsWith("*")?lDt.substring(0, lDt.length()-1):lDt;
					break;
				case "->":{
					PrimaryTree rvalue = tree.second.get(i);
					if (rvalue.operator == Operator.FIELD) {
						if (!lDt.endsWith("*")) {
							System.err.println("; E: ("+func+") " + lDt + " is not a pointer!");
							throw new CompilerError();
						}
						lDt = lDt.substring(0, lDt.length()-1);
						if (!structs.containsKey(lDt)) {
							System.err.println("; E: ("+func+") " + lDt + " cannot be resolved to a struct");
							throw new CompilerError();
						}
						if (structs.get(lDt).variables.containsKey(rvalue.first)) {
							
							
							if (structs.get(lDt).variables.get(rvalue.first).index != 0) out.print(structs.get(lDt).variables.get(rvalue.first).index + " add ");
							if (!write || i != tree.op.size()-1) out.print("readm ");
							lDt = structs.get(lDt).variables.get(rvalue.first).datatype;
						} else {
							System.err.println("; E: ("+func+") " + rvalue.first + " cannot be resolved or is not a field of " + lDt);
							throw new CompilerError();
						}
						
					} else {
						System.err.println("; E: ("+func+") Internal Compiler Error");
						throw new CompilerError();
					}
				}break;
				case "(":{
					if (!lDt.startsWith("callable"))
					{
						System.err.println("; E: ("+func+") Can't call non-function type " + lDt);
						throw new CompilerError();
					}
						{
							TokenScanner t1 = new TokenScanner();
							t1.setSpecialTokens(new char[] { ';', '<', '>', '(', ')', ',', ':', '+',
									'-', '*', '/', '%', '=', '&', '|', '{', '}', '.', '!', '[',
									']', '$' });
							t1.setBSpecialTokens(new String[] { "->", "=>", "==", "!=", "&&", "||",
									"<=", ">=", "++", "--", "::", ".." });
							t1.setComments(false);
							t1.setPrep(false);
							t1.init(lDt);
							t1.next();
							String returnType = null; ArrayList<String> parameters = new ArrayList<>();
							boolean moreParameters = false;
							try {
								returnType = StkCTree.parseDatatype(t1, false);
								t1.next();
								if (!t1.seek().equals(")")) while (t1.hasNext()) {
									if (t1.seek().equals("..")) {
										t1.next();
										t1.next();
										moreParameters = true;
										t1.next();
										break;
									} else
									parameters.add(StkCTree.parseDatatype(t1, false));
									if (t1.next().equals(")")) break;
								} else t1.next();
							} catch (SyntaxError e) {
								System.err.println("Error when parsing type '" + lDt + "'");
								e.printStackTrace();
							}
							ArrayList<ExpressionTree> arguments = tree.args.get(i);
							boolean error = false;
							String args = "[";
							for (int j = 0; j < Math.min(tree.args.get(i).size(), 
									parameters.size()); j++) {
								StkCTree.ExpressionTree ex = arguments.get(j);
								String dt = compileExpression(ex); out.print("exch ");
								if (!compatible(dt, parameters.get(j))) {
									error = true;
								}
								args += dt;
								if (j < arguments.size()-1) args += ", ";
							}
							args += "]";
							if (moreParameters) {
								if (arguments.size() < parameters.size()) error = true;
								else for (int j = parameters.size(); j < arguments.size(); j++) {
									StkCTree.ExpressionTree ex = arguments.get(j);
									String dt = compileExpression(ex); out.print("exch ");
									args += dt;
									if (j < arguments.size()-1) args += ", ";
								}
							} else if (arguments.size() != parameters.size()) error = true;
							if (error) {
								System.err.println("; W: ("+func+") Function " + "("+
										parameters
										+") is not applicable for the arguments " + args);
							}
							out.print(arguments.size() + " exch pushp ");
							lDt = returnType;
						}
					
				}break;
				default:
					break;
				}
				
			}
			return lDt;

		default:
			return compilePrimary(tree.first);
		}
	}
	
	public String compilePrimary(StkCTree.PrimaryTree tree) throws CompilerError {
		switch (tree.operator) {
		case VARIABLE:
			if (!vars.containsKey(tree.first)) {
				if (!functions.containsKey(tree.first)) {
					System.err.println("; E: ("+func+") Function or variable " + tree.first + " not resolved");
					throw new CompilerError();
				}
				out.print("#" + tree.first + " ");
				String paramString = functions.get(tree.first).datatype + "(";
				for (int i = 0; i < functions.get(tree.first).parameters.size(); i++) 
				{
					if (i != 0) paramString += ", ";
					paramString += functions.get(tree.first).parameters.get(i).datatype;
				}
				if (functions.get(tree.first).moreParameters) {
					paramString += ", ...";
				}
				paramString += ")";
				return "callable " + paramString;
			}
			out.print(vars.get(tree.first) + " overf ");
			return vars.get(tree.first).type;
		case NUMBER_VALUE:
			out.print(tree.first + " ");
			try{Integer.parseInt(tree.first);}catch(Exception ex) {return "float";}
			return "int";
		//case FUNCTION_CALL:

			//break;
		case POINTER:
			String t = compileExpression3(tree.expr, false);
			if (dt(t) != Datatype_enum.NUMBER && dt(t) != Datatype_enum.POINTER) throw new CompilerError();
			out.print("readm ");
			return t.endsWith("*") ? t.substring(0, t.length()-1) : t;
		case EXPRESSION:
			return compileExpression(tree.second);
		case PREFIX:
			if (!vars.containsKey(tree.first)) {
				System.err.println("; E: ("+func+") Variable " + tree.first + " not defined!");
				throw new CompilerError();
			}
			if (tree.fix.equals("++")) out.print(vars.get(tree.first) + " overf 1 add " + vars.get(tree.first) + " dupt ");
			if (tree.fix.equals("--")) out.print(vars.get(tree.first) + " overf 1 add " + vars.get(tree.first) + " dupt ");
			return vars.get(tree.first).type;
		case POSTFIX:
			if (!vars.containsKey(tree.first)) {
				System.err.println("; E: ("+func+") Variable " + tree.first + " not defined!");
				throw new CompilerError();
			}
			if (tree.fix.equals("++")) out.print(vars.get(tree.first) + " overf 1 add dup " + vars.get(tree.first) + " dupt pop ");
			if (tree.fix.equals("--")) out.print(vars.get(tree.first) + " overf 1 add dup " + vars.get(tree.first) + " dupt pop ");
			return vars.get(tree.first).type;
		case REVERSE:
			String t2 = compileExpression(tree.second);
			out.print("dup 2 mul sub ");
			return t2;
		case NOT:
			String t3 = compileExpression(tree.second);
			out.print("not ");
			return t3;
		case PARAM:
			out.print("	"); compileExpression(tree.second);
			out.print(func1.variables.size() + " add overf ");
			return "void";
		case STRING:
			for (int i = 0; i < tree.first.length(); i++) {
				out.print((int)tree.first.charAt(i) + " ");
			}
			out.print("0 ");
			out.print((tree.first.length()+1) + " #stralloc pushp ");
			return "char*";
		case CHAR:
			out.print((int)tree.first.charAt(0) + " ");
			return "char";
		case FIELD:
			return "-";
		case REF:
			if (!vars.containsKey(tree.first)) {
				System.err.println("; E: ("+func+") Variable " + tree.first + " not resolved");
				throw new CompilerError();
			}
			out.print(vars.get(tree.first) + " ref ");
			return vars.get(tree.first).type + "*";
		case SIZEOF:
			switch (tree.first) {
			case "int":
			case "float":
			case "char":
			case "void":
				out.print("1 ");
				break;

			default:
				if (tree.first.endsWith("*")) out.print("1 ");
				else if (structs.containsKey(tree.first)) out.print(structs.get(tree.first).variables.values().size() + " ");
				else {
					System.err.println("; E: (" + func + ") " + tree.first + " not resolved");
					throw new CompilerError();
				}
				break;
			}
			return "int";
		default:
			break;
		}
		throw new CompilerError();
	}
	
}
