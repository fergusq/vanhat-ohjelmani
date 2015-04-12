package org.kaivos.stkc.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kaivos.parsertools.ParserTree;
import org.kaivos.sc.TokenScanner;
import org.kaivos.stg.error.SyntaxError;
import org.kaivos.stg.error.UnexpectedTokenSyntaxError;

public class StkCTree extends ParserTree {

	public static String nextI(TokenScanner s) throws UnexpectedTokenSyntaxError {
		String s2 = s.next();
		
		if (!s2.matches("[a-zA-Zöäå_].*")) {
			throw new UnexpectedTokenSyntaxError(s.file(), s.nextTokensLine()+1,s2, "<ident>", "'" + "<ident>" + "' excepted, got '" + s2 + "'");
		}
		
		switch (s2) {
		case "native":
		case "__from":
		case "if":
		case "else":
		case "while":
		case "do":
		case "for":
		case "goto":
		case "break":
		case "define":
		case "return":
		case "struct":
		case "typedef":
			throw new UnexpectedTokenSyntaxError(s.file(), s.nextTokensLine()+1,s2, "<ident>", "'" + "<ident>" + "' excepted, got '" + s2 + "'");

		default:
			break;
		}
		
		return s2;
	}
	
	/**
	 * Start = {
	 * 		FUNCTION*
	 * }
	 */
	public static class StartTree extends TreeNode {
		
		public ArrayList<TreeNode> functions = new ArrayList<TreeNode>();
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			while (!seek(s).equals("<EOF>")) {
				if (seek(s).equals("struct")) {
					StructTree t = new StructTree();
					t.parse(s);
					functions.add(t);
				} else if (seek(s).equals("typedef")) {
					accept("typedef", s);
					String type = parseDatatype(s, true);
					String name = nextI(s);
					accept(";", s);
					typedefs.put(name, type);
					types.add(name);
				} else {
					FunctionTree t = new FunctionTree();
					t.parse(s);
					functions.add(t);
				}
				continue;
			}
			accept("<EOF>", s);
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Datatype = {
	 * 		("void"|"number") "*"?
	 * }
	 */
	public static String parseDatatype(TokenScanner s, boolean allowNonPointerStructs) throws SyntaxError {
		if (seek(s).equals("char") || 
				seek(s).equals("int") || 
				seek(s).equals("float") || 
				seek(s).equals("void") || seek(s).startsWith("callable")){
			String base = accept(new String[] { "char", "int", "float", "void", "callable" }, s);
			if (base.equals("callable")) {
				base += " " + parseDatatype(s, false);
				base += "(";
				accept("(", s);
				if (!seek(s).equals(")")) while (true) {
					if (seek(s).equals("..")) {
						accept("..", s);
						accept(".", s);
						accept(")", s);
						break;
					}
					base += parseDatatype(s, false);
					if (accept(new String[]{")", ","}, s).equals(")")) break;
					else base += ", ";
				} else accept(")", s);
				base += ")";
			}
			while (seek(s).equals("*")) {
				base += "*";
				accept("*", s);
			}
			return base;
		} else {
			String base = null;
			if (seek(s).equals("struct")) {
				accept("struct", s);
				base = nextI(s);
			} else if (typedefs.containsKey(seek(s))) {
				base = typedefs.get(nextI(s));
				allowNonPointerStructs = true;
			} else {
				base = nextI(s);
			}
			if (!allowNonPointerStructs) {
				base += "*";
				accept("*", s);
			}
			while (seek(s).equals("*")) {
				base += "*";
				accept("*", s);
			}
			return base;
		}
		
	}

	
	public static Set<String> types = new HashSet<>();
	static {
		types.addAll(Arrays.asList("int", "char", "float", "void", "callable", "struct"));
	}
	public static Map<String, String> typedefs = new HashMap<String, String>();
	
	/**
	 * Function = {
	 * 		DATATYPE NAME PARAMETERS VARIABLES? LINE
	 * }
	 */
	public static class FunctionTree extends TreeNode {

		public String name = null, datatype = null, __from = "";
		
		public boolean moreParameters = false, _native = false;
		
		public ArrayList<ParameterTree> parameters = new ArrayList<ParameterTree>();
		public ArrayList<ParameterTree> variables = new ArrayList<ParameterTree>();
		public ArrayList<LineTree> lines = new ArrayList<>();
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			if (seek(s).equals("native")) {
				accept("native", s);
				_native = true;
			}
			
			datatype = parseDatatype(s, false);
			
			name = nextI(s);
			
			{ accept("(", s);
				if (!seek(s).equals(")")){
					while (true) {
						
						if (seek(s).equals("..")) {
							accept("..", s);
							accept(".", s);
							moreParameters = true;
							accept(")", s);
							break;
						}
						
						ParameterTree t = new ParameterTree();
						t.parse(s);
						parameters.add(t);

						String str = accept(new String[] { ",", ")"}, s);

						if (str.equals(")"))
							break;
					}}
				else
					accept(")", s);
			}
			
			if (seek(s).equals(":")) {
				accept(":", s);
				{ accept("(", s);
				if (!seek(s).equals(")"))
					while (true) {
						ParameterTree t = new ParameterTree();
						t.parse(s);
						variables.add(t);

						String str = accept(new String[] { ",", ")" }, s);

						if (str.equals(")"))
							break;
					}
				else
					accept(")", s);
			}
			}

			if (_native) {
				accept("__from", s);
				if (seek(s).equals("<")) {
					__from = "<";
					do __from += next(s); while (!seek(s).equals(">"));
					__from += ">";
					accept(">", s);
				} else {
					__from = next(s);
				}
			}
			
			if (seek(s).equals(";") || _native) {
				lines = null;
				
				accept(";", s);
				
			} else {
				accept("{", s);
				
				while (!seek(s).equals("}")) {
					
					if (seek(s).equals("local")) {
						accept("local", s);
						ParameterTree t = new ParameterTree();
						t.parse(s);
						variables.add(t);
						accept(";", s);
						continue;
					} else if (types.contains(seek(s))) {
						ParameterTree t = new ParameterTree();
						t.parse(s);
						variables.add(t);
						accept(";", s);
						continue;
					}
					
					LineTree t = new LineTree();
					t.parse(s);
					lines.add(t);
				}
				
				accept("}", s);
			}
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Struct = {
	 * 		"struct" NAME ("{" VARIABLE_LIST "}")? ";"
	 * }
	 */
	public static class StructTree extends TreeNode {

		public String name = null;
		public HashMap<String, ParameterTree> variables = new HashMap<String, ParameterTree>();
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			accept("struct", s);
			
			name = nextI(s);
			
			if (seek(s).equals("{")) {
				accept("{", s);
				if (!seek(s).equals("}")) {
					int i = 0;
					while (true) {
						ParameterTree t = new ParameterTree();
						t.parse(s);
						t.index = i;
						variables.put(t.name, t);

						accept(";", s);
						
						if (seek(s).equals("}")) {
							accept("}", s);
							break;
						}
						
						i++;
					}}
				else
					accept("}", s);
			
			} else {
				variables = null;
			}
			accept(";", s);
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Parameter = {
	 * 		DATATYPE NAME
	 * }
	 */
	public static class ParameterTree extends TreeNode {

		public String name = null, datatype = null;
		public int index;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			datatype = parseDatatype(s, false);
			name = nextI(s);
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
		@Override
		public String toString() {
			return name + ": " + datatype;
		}
		
	}
	
	/**
	 * Line = {
	 * 		Expression ";"
	 * 		| While
	 * 		| If
	 * 		| "return" EXPRESSION
	 * 		| "break"
	 * 		| "define" LABEL ":"
	 *		| "goto" LABEL ";"
	 *		| "asm" "(" "\"" TOKENS "\"" ")" ";"
	 * }
	 */
	public static class LineTree extends TreeNode {

		public enum LineType {
			EXPRESSION,
			WHILE,
			DO_WHILE,
			FOR,
			IF,
			BLOCK,
			RETURN,
			BREAK,
			GOTO,
			LABEL,
			ASM
		}
		
		public LineType type;
		
		public WhileTree _while;
		public ExpressionTree expression;
		public DoWhileTree doWhile;
		public ForTree _for;
		public IfTree _if;
		public BlockTree block;
		public String labelOrCode;
		//public ReturnTree _return;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			String str = seek(s);

			if (str.equals("while")) {
				type = LineType.WHILE;
				_while = new WhileTree();
				_while.parse(s);
				
			} else if (str.equals("do")) {
				type = LineType.DO_WHILE;
				doWhile = new DoWhileTree();
				doWhile.parse(s);
				
				accept(";", s);
				
			} else if (str.equals("for")) {
				type = LineType.FOR;
				_for = new ForTree();
				_for.parse(s);
				
			} else if (str.equals("if")) {
				type = LineType.IF;
				_if = new IfTree();
				_if.parse(s);
				
			} else if (str.equals("return")) {
				type = LineType.RETURN;
				accept("return", s);
				expression = new ExpressionTree();
				expression.parse(s);
				
				accept(";", s);
				
			} else if (str.equals("break")) {
				type = LineType.BREAK;
				accept("break", s);
				
				accept(";", s);
				
			} else if (str.equals("goto")) {
				type = LineType.GOTO;
				accept("goto", s);
				labelOrCode = nextI(s);
				accept(";", s);
				
			} else if (str.equals("define")) {
				type = LineType.LABEL;
				accept("define", s);
				labelOrCode = nextI(s);
				accept(":", s);
				
			} else if (str.equals("asm")) {
				type = LineType.ASM;
				accept("asm", s);
				accept("(", s);
				String code = next(s);
				if (!code.startsWith("\"") || !code.endsWith("\"")) {
					throw new UnexpectedTokenSyntaxError(s.file(), s.line(), code, "\"", "Unexcepted token '" + code + "'!");
				}
				code = code.substring(1, code.length()-1);
				labelOrCode = code;
				accept(")", s);
				accept(";", s);
				
			} else if (str.equals("{"))
			{
				type = LineType.BLOCK;
				block = new BlockTree();
				block.parse(s);
			} else
			{
				type = LineType.EXPRESSION;
				expression = new ExpressionTree();
				expression.parse(s);
				
				accept(";", s);
			}
			
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}

	/**
	 * Block = {
	 * 		"{" LINE* "}"
	 * }
	 */
	public static class BlockTree extends TreeNode {

		public ArrayList<LineTree> lines = new ArrayList<LineTree>();
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			accept("{", s);
			while (!seek(s).equals("}")) {
				LineTree t = new LineTree();
				t.parse(s);
				lines.add(t);
			}
			accept("}", s);
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	
	/**
	 * While = {
	 * 		"while" "(" EXPRESSION ")" LINE
	 * }
	 */
	public static class WhileTree extends TreeNode {

		public ExpressionTree condition;
		public LineTree line;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			accept("while", s);
			
			accept("(", s);
			condition = new ExpressionTree();
			condition.parse(s);
			accept(")", s);
			
			line = new LineTree();
			line.parse(s);
			
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * DoWhile = {
	 * 		"do" LINE "while" "(" EXPRESSION ")"
	 * }
	 */
	public static class DoWhileTree extends TreeNode {

		public ExpressionTree condition;
		public LineTree line;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			accept("do", s);
			
			line = new LineTree();
			line.parse(s);
			
			accept("while", s);
			
			accept("(", s);
			condition = new ExpressionTree();
			condition.parse(s);
			accept(")", s);
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * For = {
	 * 		"for" "(" EXPRESSION ";" EXPRESSION ";" EXPRESSION ")" LINE
	 * }
	 */
	public static class ForTree extends TreeNode {

		public ExpressionTree assign;
		
		public ExpressionTree condition;
		public ExpressionTree increment;
		public LineTree line;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			accept("for", s);
			
			accept("(", s);
			assign = new ExpressionTree();
			assign.parse(s);
			accept(";", s);
			condition = new ExpressionTree();
			condition.parse(s);
			accept(";", s);
			increment = new ExpressionTree();
			increment.parse(s);
			accept(")", s);
			
			line = new LineTree();
			line.parse(s);
			
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * If = {
	 * 		"if" "(" EXPRESSION ")" LINE ("else" LINE)?
	 * }
	 */
	public static class IfTree extends TreeNode {

		public ExpressionTree condition;
		public LineTree line;
		public LineTree line_else;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			accept("if", s);
			
			accept("(", s);
			condition = new ExpressionTree();
			condition.parse(s);
			accept(")", s);
			
			line = new LineTree();
			line.parse(s);
			
			if (seek(s).equals("else")) {
				accept("else", s);
				line_else = new LineTree();
				line_else.parse(s);
			}
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Expression2 = {
	 * 		EXPRESSION3 (((("->"|"=>") EXPRESSION3)|"[" EXPRESSION "]")* (("."|"::") ("[" (EXPRESSION ("," EXPRESSION)*)? "]"|EXPRESSION) )?)?
	 * 		| EXPRESSION3
	 * }
	 */
	public static class Expression2Tree extends TreeNode {

		public enum Operator {
			NEXT,
			OPERATOR
		}
		public Operator operator = Operator.NEXT;
		public Expression3Tree first;
		
		public ArrayList<String> op = new ArrayList<>();
		public ArrayList<Expression3Tree> second = new ArrayList<>();
		
		public int line;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			first = new Expression3Tree();
			first.parse(s);
			
			line = s.line();
			
			String operator;
			label1: while (Arrays.asList(new String[]{"="}).contains((operator = seek(s)))){
				switch (operator) {
				case "=":
					op.add(next(s));
					this.operator = Operator.OPERATOR;
				
					Expression3Tree e = new Expression3Tree();
					e.operator = Expression3Tree.Operator.NEXT;
					PrimaryTree e4 = new PrimaryTree();
					e4.operator = PrimaryTree.Operator.EXPRESSION;
					e4.second = new ExpressionTree();
					e4.second.parse(s);
					e.first = e4;
					second.add(e);
					break label1;
				default:
					this.operator = Operator.NEXT;
					break label1;
				}
			}
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Expression2 = {
	 * 		EXPRESSION3 (((("->"|"=>") EXPRESSION3)|"[" EXPRESSION "]")* (("."|"::") ("[" (EXPRESSION ("," EXPRESSION)*)? "]"|EXPRESSION) )?)?
	 * 		| EXPRESSION3
	 * }
	 */
	public static class Expression3Tree extends TreeNode {

		public enum Operator {
			NEXT,
			OPERATOR
		}
		public Operator operator = Operator.NEXT;
		public PrimaryTree first;
		
		public ArrayList<String> op = new ArrayList<>();
		public ArrayList<PrimaryTree> second = new ArrayList<>();
		public ArrayList<ArrayList<ExpressionTree>> args = new ArrayList<>();
		
		public int line;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			first = new PrimaryTree();
			first.parse(s);
			
			line = s.line();
			
			String operator;
			label1: while (Arrays.asList(new String[]{"->", ".", "[", "("}).contains((operator = seek(s)))){
				switch (operator) {
				case "[":
					op.add(next(s));
					this.operator = Operator.OPERATOR;
				
					PrimaryTree e2 = new PrimaryTree();
					e2.operator = PrimaryTree.Operator.EXPRESSION;
					e2.second = new ExpressionTree();
					e2.second.parse(s);
					second.add(e2);
					
					accept("]", s);
					break;
				case "->":
					op.add(next(s));
					this.operator = Operator.OPERATOR;
				
					PrimaryTree e3 = new PrimaryTree();
					e3.operator = PrimaryTree.Operator.FIELD;
					e3.first = next(s);
					second.add(e3);
					break;
				case "(":
				{
					op.add("(");
					this.operator = Operator.OPERATOR;
					ArrayList<ExpressionTree> arguments;
					arguments = new ArrayList<>();
					accept("(", s);
					if (!seek(s).equals(")")) while (true) {
						ExpressionTree t = new ExpressionTree();
						t.parse(s);
						arguments.add(t);
						if (accept(new String[]{")", ","}, s).equals(")")) break;
					} else accept(")", s);
					args.add(arguments);
					second.add(null);
				}
					break;
				default:
					this.operator = Operator.NEXT;
					break label1;
				}
			}
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Expression1 = {
	 * 		EXPRESSION2 ("*"|"/"|"%"|"^") EXPRESSION2
	 * 		| EXPRESSION2
	 * }
	 */
	public static class Expression1Tree extends TreeNode {

		public enum Operator {
			NEXT,
			OPERATOR
		}
		public Operator operator = Operator.NEXT;
		public ArrayList<String> op = new ArrayList<>();
		public Expression2Tree first;
		public ArrayList<Expression2Tree> second = new ArrayList<>();
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			first = new Expression2Tree();
			first.parse(s);
			
			String operator;
			label1: while (Arrays.asList(new String[]{"*", "/", "%", "^", "&", "|"}).contains((operator = seek(s)))){
				switch (operator) {
				case "*":
				case "/":
				case "%":
				case "^":
				case "&":
				case "|":
					op.add(next(s));
					this.operator = Operator.OPERATOR;
				
					Expression2Tree e = new Expression2Tree();
					e.parse(s);
					second.add(e);
					break;
				default:
					this.operator = Operator.NEXT;
					break label1;
				}
			}
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Expression0 = {
	 * 		EXPRESSION1 ("+"|"-") EXPRESSION1
	 * 		| EXPRESSION1
	 * }
	 */
	public static class Expression0Tree extends TreeNode {

		public enum Operator {
			NEXT,
			OPERATOR
		}
		public Operator operator = Operator.NEXT;
		public ArrayList<String> op = new ArrayList<>();
		public Expression1Tree first;
		public ArrayList<Expression1Tree> second = new ArrayList<>();
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			first = new Expression1Tree();
			first.parse(s);
			
			parse_(s);
			
		}

		private void parse_(TokenScanner s) throws SyntaxError {
			String operator;
			label1: while (Arrays.asList(new String[]{"+", "-"}).contains((operator = seek(s)))){
				switch (operator) {
				case "+":
				case "-":
					op.add(next(s));
					this.operator = Operator.OPERATOR;
				
					Expression1Tree e = new Expression1Tree();
					e.parse(s);
					second.add(e);
					break;
				default:
					this.operator = Operator.NEXT;
					break label1;
				}
			}
		}
		
		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Expression = {
	 * 		| EXPRESSION_LOGIC ("&&"|"||") EXPRESSION_LOGIC
	 * 		| EXPRESSION_LOGIC
	 * }
	 */
	public static class ExpressionTree extends TreeNode {

		public enum Operator {
			NEXT,
			OPERATOR
		}
		public Operator operator = Operator.NEXT;
		public ArrayList<String> op = new ArrayList<>();
		public ExpressionLogicTree first;
		public ArrayList<ExpressionLogicTree> second = new ArrayList<>();
		
		@SuppressWarnings("unused")
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			first = new ExpressionLogicTree();
			first.parse(s);
			
			String operator;
			label1: while (Arrays.asList(new String[]{"&&", "||"}).contains((operator = seek(s)))){
			switch (operator) {
			case "&&":
			case "||":
				op.add(next(s));
				this.operator = Operator.OPERATOR;
				
				ExpressionLogicTree e = new ExpressionLogicTree();
				e.parse(s);
				second.add(e);
				break;
			default:
				this.operator = Operator.NEXT;
			
			}
			}
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * ExpressionLogic = {
	 * 		| EXPRESSION0 ("=="|"!="|"<"|"<="|">"|">=") EXPRESSION0
	 * 		| EXPRESSION0
	 * }
	 */
	public static class ExpressionLogicTree extends TreeNode {

		public enum Operator {
			NEXT,
			OPERATOR
		}
		public Operator operator = Operator.NEXT;
		public ArrayList<String> op = new ArrayList<>();
		public Expression0Tree first;
		public ArrayList<Expression0Tree> second = new ArrayList<>();
		
		@SuppressWarnings("unused")
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			first = new Expression0Tree();
			first.parse(s);
			
			String operator;
			label1: while (Arrays.asList(new String[]{"==", "<", "<=", ">", ">=", "!="}).contains((operator = seek(s)))){
			switch (operator) {
			case "==":
			case "<":
			case "<=":
			case ">":
			case ">=":
			case "!=":
				op.add(next(s));
				this.operator = Operator.OPERATOR;
				
				Expression0Tree e = new Expression0Tree();
				e.parse(s);
				second.add(e);
				break;
			default:
				this.operator = Operator.NEXT;
			
			}
			}
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
	
	/**
	 * Primary = {
	 * 		NUMBER
	 * 		| NAME "=" EXPRESSION
	 * 		| "local" NAME "=" EXPRESSION
	 * 		| NAME
	 * 		| "!" NAME
	 * 		| "++" NAME
	 * 		| "--" NAME
	 * 	 	| NAME "++"
	 * 		| NAME "--"
	 * 		| "(" EXPRESSION ")"
	 * 		| "{" (EXPRESSION ("," EXPRESSION)*)? "}"
	 * 		| "!" EXPRESSION 
	 * 		| "-" EXPRESSION
	 * 		| "$" NAME
	 * 		| "*" PRIMARY
	 * 		| STRING
	 * 		| "table" "{" (NAME "=" EXPRESSION ("," NAME "=" EXPRESSION)*)? "}"
	 * 		| NAME "(" ARGUMENTS ")"
	 * 		| "&" NAME
	 * 		| "sizeof" "(" NAME ")"
	 * }
	 */
	public static class PrimaryTree extends TreeNode {

		public enum Operator {
			NUMBER_VALUE,
			VARIABLE,
			EXPRESSION,
			PREFIX,
			POSTFIX,
			NOT,
			REVERSE,
			LIST,
			STRING,
			TABLE,
			//FUNCTION_CALL,
			POINTER, PARAM,
			FIELD,
			CHAR,
			REF, FREF,
			SIZEOF
		}
		public Operator operator;
		public String fix;
		public String first;
		public ExpressionTree second;
		public Expression3Tree expr;
		public ArrayList<ExpressionTree> arguments = new ArrayList<>();
		public ArrayList<String> names = new ArrayList<>();
		public int line;
		
		@Override
		public void parse(TokenScanner s) throws SyntaxError {
			
			line = s.line();
			
			if (seek(s).equals("(")) {
				accept("(", s);
				this.operator = Operator.EXPRESSION;
				second = new ExpressionTree();
				second.parse(s);
				accept(")", s);
				return;
			} else if (seek(s).equals("++") || seek(s).equals("--")) {
				operator = Operator.PREFIX;
				fix = accept(new String[]{"++", "--"}, s);
				first = nextI(s);
				return;
			} else if (seek(s).equals("!")) {
				operator = Operator.NOT;
				accept("!", s);
				expr = new Expression3Tree();
				expr.parse(s);
				return;
			} else if (seek(s).equals("-")) {
				operator = Operator.REVERSE;
				accept("-", s);
				expr = new Expression3Tree();
				expr.parse(s);
				return;
			} else if (seek(s).equals("{")) {	
				this.operator = Operator.LIST;
				
				accept("{", s);
				if (!seek(s).equals("}")) while (true) {
					ExpressionTree t = new ExpressionTree();
					t.parse(s);
					arguments.add(t);
					if (accept(new String[]{"}", ","}, s).equals("}")) break;
				} else accept("}", s);
				return;
				
			} else if (seek(s).equals("$")) { 
				this.operator = Operator.STRING;
				accept("$", s);
				first = nextI(s);
				return;
			} else if (seek(s).equals("&")) { 
				this.operator = Operator.REF;
				accept("&", s);
				first = nextI(s);
				return;
			} else if (seek(s).equals("&&")) { 
				this.operator = Operator.REF;
				accept("&&", s);
				first = nextI(s);
				return;
			} else if (seek(s).equals("*")) { 
				this.operator = Operator.POINTER;
				accept("*", s);
				expr = new Expression3Tree();
				expr.parse(s);
				return;
			} else if (seek(s).equals("parameter")) {
				operator = Operator.PARAM;
				accept("parameter", s);
				accept("(", s);
				second = new ExpressionTree();
				second.parse(s);
				accept(")", s);
				return;
			} 
			
			/*else if (seek(s).equals("[{")) {	
				operator = Operator.LIST;
				
				accept("[{", s);
				if (!seek(s).equals("}]")) while (true) {
					ExpressionTree t = new ExpressionTree();
					t.parse(s);
					arguments.add(t);
					if (accept(new String[]{"}]", ","}, s).equals("}]")) break;
				} else accept("}]", s);
				return;
			} else if (seek(s).equals("new")) {
				operator = Operator.NEW;
				accept("new", s);
				first = next(s);
				accept("(", s);
				if (!seek(s).equals(")")) while (true) {
					ExpressionTree t = new ExpressionTree();
					t.parse(s);
					arguments.add(t);
					if (accept(new String[]{")", ","}, s).equals(")")) break;
				} else accept(")", s);
				return;
			}*/
			
			first = next(s);
			
			String operator = seek(s);

			if (first.equals("sizeof")) {
				this.operator = Operator.SIZEOF;
				accept("(", s);
				first = nextI(s);
				accept(")", s);
				
			} else
			
			/*if (operator.equals("=")) {	
				this.operator = Operator.ASSIGN;
				
				accept("=", s);
				
				second = new ExpressionTree();
				second.parse(s);
			}  else if (operator.equals("[")) {	
				this.operator = Operator.ARRAY;
				
				accept("[", s);
				second = new ExpressionTree();
				second.parse(s);
				accept("]", s);
				
			} else if (operator.equals("[]")) {	
				this.operator = Operator.ARRAY;
				
				accept("[]", s);
				
			} else if (seek(s).equals("[{")) {	
				this.operator = Operator.LIST;
				
				accept("[{", s);
				if (!seek(s).equals("}]")) while (true) {
					ExpressionTree t = new ExpressionTree();
					t.parse(s);
					arguments.add(t);
					if (accept(new String[]{"}]", ","}, s).equals("}]")) break;
				} else accept("}]", s);
				return;
			} else*/ if (operator.equals("++") || operator.equals("--")) {
				this.operator = Operator.POSTFIX;
				fix = accept(new String[]{"++", "--"}, s);
				return;
			} else if (first.equals("table")) {	
				this.operator = Operator.TABLE;
				
				accept("{", s);
				if (!seek(s).equals("}")) while (true) {
					names.add(nextI(s));
					accept("=", s);
					ExpressionTree t = new ExpressionTree();
					t.parse(s);
					arguments.add(t);
					if (accept(new String[]{"}", ","}, s).equals("}")) break;
				} else accept("}", s);
				return;
				
			} else /*if (seek(s).equals("(")){
			
			{	
				this.operator = Operator.FUNCTION_CALL;
				{
					accept("(", s);
					if (!seek(s).equals(")")) while (true) {
						ExpressionTree t = new ExpressionTree();
						t.parse(s);
						arguments.add(t);
						if (accept(new String[]{")", ","}, s).equals(")")) break;
					} else accept(")", s);
				}
				
			}
			}
			else*/ {
				if (first.equals("<EOF>")) {
					throw new UnexpectedTokenSyntaxError(s.file(), s.line(), "<EOF>", "<ident>", "Unexcepted EOF");
				}
				if (first.startsWith("\"") && first.endsWith("\"")) {
					this.operator = Operator.STRING;
					first = first.substring(1, first.length()-1);
					return;
				}
				if (first.startsWith("'") && first.endsWith("'") && first.length() == 3) {
					this.operator = Operator.CHAR;
					first = first.substring(1, first.length()-1);
					return;
				}
				try {
					Double.parseDouble(first);
					this.operator = Operator.NUMBER_VALUE;
				} catch (NumberFormatException ex) {
					this.operator = Operator.VARIABLE;
				}
				
			}
			
		}

		@Override
		public String generate(String a) {
			return null;
		}
		
	}
}
