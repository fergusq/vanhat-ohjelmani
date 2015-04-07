package qsharp.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class QParser {

	/** Literal token values. */
	  static String[] tokenImage = {
	    "<EOF>",
	    "\" \"",
	    "\"\\t\"",
	    "\"\\n\"",
	    "\"\\r\"",
	    "<token of kind 5>",
	    "<token of kind 6>",
	    "\"use\"",
	    "\"if\"",
	    "\"then\"",
	    "\"endif\"",
	    "\"while\"",
	    "\"endwhile\"",
	    "\"do\"",
	    "\"enddo\"",
	    "\"create\"",
	    "\"new\"",
	    "\"endcreate\"",
	    "\"var\"",
	    "\"set\"",
	    "\"to\"",
	    "\".\"",
	    "\"?\"",
	    "\"!\"",
	    "\"$\"",
	    "\"#\"",
	    "\"=\"",
	    "\">\"",
	    "\"<\"",
	    "\">=\"",
	    "\"<=\"",
	    "\"|\"",
	    "\"+\"",
	    "\"-\"",
	    "\"*\"",
	    "\"/\"",
	    "\"%\"",
	    "[ \"1\"-\"9\" ] ([ \"0\"-\"9\" ])*",
	    "<DECIMAL_LITERAL>",
	    "<HEX_LITERAL>",
	    "<OCTAL_LITERAL>",
	    "[[ \"_\", \"a\"-\"z\", \"A\"-\"Z\" ], [ \"0\"-\"9\" ]]",
	    "[ \"_\", \"a\"-\"z\", \"A\"-\"Z\" ]",
	    "[ \"0\"-\"9\" ]",
	    "\"(\"",
	    "\",\"",
	    "\")\"",
	    "\":\"",
	    "\"==\"",
	    "\"print\"",
	  };
	
	/** End of File. */
	static String EOF = tokenImage[0];
	  /** RegularExpression Id. */
	  static String USE = tokenImage[7];
	  /** RegularExpression Id. */
	  static String IF = tokenImage[8];
	  /** RegularExpression Id. */
	  static String THEN = tokenImage[9];
	  /** RegularExpression Id. */
	  static String ENDIF = tokenImage[10];
	  /** RegularExpression Id. */
	  static String WHILE = tokenImage[11];
	  /** RegularExpression Id. */
	  static String ENDWHILE = tokenImage[12];
	  /** RegularExpression Id. */
	  static String DO = tokenImage[13];
	  /** RegularExpression Id. */
	  static String ENDDO = tokenImage[14];
	  /** RegularExpression Id. */
	  static String CREATE = tokenImage[15];
	  /** RegularExpression Id. */
	  static String NEW = tokenImage[16];
	  /** RegularExpression Id. */
	  static String ENDCREATE = tokenImage[17];
	  /** RegularExpression Id. */
	  static String VAR = tokenImage[18];
	  /** RegularExpression Id. */
	  static String SET = tokenImage[19];
	  /** RegularExpression Id. */
	  static String TO = tokenImage[20];
	  /** RegularExpression Id. */
	  static String DOT = tokenImage[21];
	  /** RegularExpression Id. */
	  static String QMARK = tokenImage[22];
	  /** RegularExpression Id. */
	  static String SMARK = tokenImage[23];
	  /** RegularExpression Id. */
	  static String DOLLAR = tokenImage[24];
	  /** RegularExpression Id. */
	  static String SHARP = tokenImage[25];
	  /** RegularExpression Id. */
	  static String EQ = tokenImage[26];
	  /** RegularExpression Id. */
	  static String GT = tokenImage[27];
	  /** RegularExpression Id. */
	  static String LT = tokenImage[28];
	  /** RegularExpression Id. */
	  static String GE = tokenImage[29];
	  /** RegularExpression Id. */
	  static String LE = tokenImage[30];
	  /** RegularExpression Id. */
	  static String NE = tokenImage[31];
	  /** RegularExpression Id. */
	  static String PLUS = tokenImage[32];
	  /** RegularExpression Id. */
	  static String MINUS = tokenImage[33];
	  /** RegularExpression Id. */
	  static String MUL = tokenImage[34];
	  /** RegularExpression Id. */
	  static String DIV = tokenImage[35];
	  /** RegularExpression Id. */
	  static String MOD = tokenImage[36];
	  /** RegularExpression Id. */
	  static String INTEGER_LITERAL = tokenImage[37];
	  /** RegularExpression Id. */
	  static String DECIMAL_LITERAL = tokenImage[38];
	  /** RegularExpression Id. */
	  static String HEX_LITERAL = tokenImage[39];
	  /** RegularExpression Id. */
	  static String OCTAL_LITERAL = tokenImage[40];
	  /** RegularExpression Id. */
	  static String IDENTIFIER = tokenImage[41];
	  /** RegularExpression Id. */
	  static String LETTER = tokenImage[42];
	  /** RegularExpression Id. */
	  static String DIGIT = tokenImage[43];
	  /** RegularExpression Id. */
	  static String PARAM_START = tokenImage[44];
	  /** RegularExpression Id. */
	  static String PARAM_MORE = tokenImage[45];
	  /** RegularExpression Id. */
	  static String PARAM_END = tokenImage[46];
	  
	  static String PRINT = tokenImage[49];

	  /** Lexical state. */
	  int DEFAULT = 0;
	  
	  static int maxDepth = 15;

	private static int depth;
	  
	  
	public static final String parseImportUse() {
		return USE + "("+parsePackage()+")+ ";
	}
	
	private static final String parseSetUse() {
		return SET + "("+parseField()+") " + mathExpression();
	}

	private static final String parseIf() {
		depth++;
		 return IF + relationalExpression() + THEN + "(" + statementExpression() + ") *" + ENDIF;
	}
	
	public static String statementExpression() {
		  return parseCreateUse() +
		  "|" + parseSetUse() +
		  "|" + parsePrintUse() +
		  "|" + parseFunctionUse() +
		  (depth > maxDepth ? "" : ("|" + parseIf())) +
		  (depth > maxDepth ? "" : ("|" + parseLoop()));
	}

	private static String parsePrintUse() {
		return PRINT + mathExpression();
	}

	private static String parseLoop() {
		return parseDo() +
		  "|" + parseWhile();
	}

	private static String parseDo() {
		depth++;
		return DO + "(" + statementExpression() + ") *" + ENDDO;
	}

	private static String parseWhile() {
		depth++;
		return WHILE + relationalExpression() + THEN + "(" + statementExpression() + ") *" + ENDWHILE;
	}

	private static String parseCreateUse() {
		return CREATE + NEW + parseType() + parseField() + "(" + TO + mathExpression() + ")?";
	}

	private static String parseType() {
		return "(" + "vararray" +
		  "|" + "int" +
		  "|" + "string" +
		  "|" + "cmd" +
		  "|" + "function" + ")";
	}

	private static String relationalExpression() {
		return relationalEqualityExpression();
	}

	private static String relationalEqualityExpression() {
		return relationalGreaterExpression() + "((\"==\" | \"|\")" + relationalGreaterExpression() + ")*";
	}

	private static String relationalGreaterExpression() {
		 return relationalLessExpression() + "((\">\" | \">=\")" + relationalLessExpression() + ")*";
	}

	private static String relationalLessExpression() {
		return unaryRelational() + "((\"<\" | \"<=\")" + unaryRelational() + ")*";
	}

	private static String unaryRelational() {
		return INTEGER_LITERAL + "|" + parseField();
	}

	public static final String parseFunctionUse() {
		return "("+parsePackage()+")+ " + "("+parseField()+")+ " + parseFunction() + "(" + PARAM_START + mathExpression()+"(" + PARAM_MORE + mathExpression() + ")*" + PARAM_END + ")";
	}

	private static String mathExpression() {
		return multiplicativeExpression() +
		  "((\"+\"| \"-\") " +
		    multiplicativeExpression()
		   + ")*";
	}

	private static String multiplicativeExpression() {
		
		return unaryExpression() + "((\"*\"| \"/\"| \"%\")" + unaryExpression()+ ")*";
	}

	private static String unaryExpression() {
		depth++;
		return (depth > maxDepth ? "" : ("\"(\" " + mathExpression() + " \")\" | ")) + INTEGER_LITERAL + " | " + parseField();
	}

	private static String parseFunction() {
		return QMARK + IDENTIFIER;
	}

	private static String parseField() {
		return DOLLAR + IDENTIFIER;
	}

	private static String parsePackage() {
		return SMARK + IDENTIFIER;
	}
	
	public static void main(String[] args) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(new File(args[0])));
		} catch (FileNotFoundException e) {e.printStackTrace();
		}
		
		try {
		
			String report = "";
			int i = 0;
		while (true) {
			depth = 0;
			String s = r.readLine();
			s = s.trim();
			i++;
			if (s.toLowerCase().startsWith("if") && !s.toLowerCase().endsWith("endif")) {
				while (!(s += " "+r.readLine()).toLowerCase().trim().endsWith("endif")) {i++;}
			}
			if (s.toLowerCase().startsWith("do") && !s.toLowerCase().endsWith("enddo")) {
				while (!(s += " "+r.readLine()).toLowerCase().trim().endsWith("enddo")) {i++;}
			}
			if (s.toLowerCase().startsWith("while") && !s.toLowerCase().endsWith("endwhile")) {
				while (!(s += " "+r.readLine()).toLowerCase().trim().endsWith("endwhile")) {i++;}
			}
			if (s.toLowerCase().startsWith("create new cmd") && !s.toLowerCase().endsWith("endcreate")) {
				while (!(s += " "+r.readLine()).toLowerCase().trim().endsWith("endcreate")) {i++;}
			}
			boolean ok = false;
			if (s.toLowerCase().matches(statementExpression())) ok = true;
			if (!ok) {
				report += "\nSyntax error on line " + i;
				report += "\n	" + s;
				break;
			}
		}
		
		System.out.println(report);
		
		} catch (Exception e) {
			
		}
	}
	
}
