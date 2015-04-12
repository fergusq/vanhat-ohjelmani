package org.kaivos.stkc.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.kaivos.lib.ArgumentParser;
import org.kaivos.sc.TokenScanner;
import org.kaivos.stg.error.SyntaxError;
import org.kaivos.stg.error.UnexpectedTokenSyntaxError;
import org.kaivos.stkc.compiler.CompilerError;
import org.kaivos.stkc.compiler.StkCCompiler;
import org.kaivos.stkc.parser.StkCTree.StartTree;

public class StkCParser {
	public static void main(String[] args) {

		ArgumentParser a = new ArgumentParser(args);
		
		BufferedReader in = null;
		
		
		if (a.getFlag("i") != null) {
			in = new BufferedReader(new InputStreamReader(System.in));
		} else {
			try {
				in = new BufferedReader(new FileReader(new File(a.lastText())));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return;
			}
		}
		
		String textIn = "";
		try {
			while (in.ready())
				textIn += in.readLine() + "\n";
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		TokenScanner s = new TokenScanner();
		s.setSpecialTokens(new char[] { ';', '<', '>', '(', ')', ',', ':', '+',
				'-', '*', '/', '%', '=', '&', '|', '{', '}', '.', '!', '[',
				']', '$' });
		s.setBSpecialTokens(new String[] { "->", "=>", "==", "!=", "&&", "||",
				"<=", ">=", "++", "--", "::", ".." });
		s.setComments(true);
		s.setPrep(true);
		s.init(textIn);
		// System.out.println(s.getTokenList());
		StkCTree.StartTree tree = new StartTree();

		try {
			tree.parse(s);
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			StkCCompiler compiler = new StkCCompiler();
			try {
				compiler.compile(tree);
			} catch (CompilerError e) {
				System.err.println("virhe 1");
			} catch (Exception e) {
				System.err.println("; E: ("+compiler.func+") Internal Compiler Exception");
				e.printStackTrace();
			}
		} catch (UnexpectedTokenSyntaxError e) {

			if (e.getExceptedArray() == null) {
				System.err.println("[" + e.getFile() + ":" + e.getLine()
						+ "] Syntax error on token '" + e.getToken()
						+ "', excepted '" + e.getExcepted() + "'");
				 e.printStackTrace();
			} else {
				System.err.println("[" + e.getFile() + ":" + e.getLine()
						+ "] Syntax error on token '" + e.getToken()
						+ "', excepted one of:");
				for (String token : e.getExceptedArray()) {
					System.err.println("[Line " + e.getLine() + "] \t\t'"
							+ token + "'");
				}
			}

			System.err.println("[Line " + e.getLine() + "] Line: '"
					+ s.getLine(e.getLine() - 1).trim() + "'");
		} catch (SyntaxError e) {

			System.err.println("[Line " + e.getLine() + "] " + e.getMessage());
			System.err.println("[Line " + e.getLine() + "] \t"
					+ s.getLine(e.getLine() - 1).trim());
		} catch (StackOverflowError e) {
			System.err.println("Stack overflow exception!");
		}

		{
			// SveCodeGenerator.CGStartTree gen = new
			// SveCodeGenerator.CGStartTree(tree);
			// System.out.println(gen.generate(""));
		}

	}

	/*private static void openRealtimeInterpreter() throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		PrintStream o = System.out;

		SveInterpreter inter = new SveInterpreter();

		o.println("Sve 1.0 Realtime Interpreter - (c) 2012 Iikka Hauhio - All rights reserved");

		String line = "";
		boolean addMode = false;

		o.print("> ");
		while (true) {
			if (!addMode)
				line = r.readLine();
			else
				line += "\n" + r.readLine();
			if (line.equals("exit"))
				break;
			TokenScanner s = new TokenScanner();
			s.setSpecialTokens(new char[] { ';', '<', '>', '(', ')', ',', ':',
					'+', '-', '*', '/', '%', '=', '&', '|', '{', '}', '.', '!',
					'[', ']', '$' });
			s.setBSpecialTokens(new String[] { "->", "=>", "==", "!=", "&&",
					"||", "<=", ">=", "++", "--", "::" });
			s.setComments(true);

			try {
				s.init(line);
				// System.out.println(s.getTokenList());
				SveTree.StartTree tree = new StartTree();

				tree.parse(s);
				inter.interpret(tree);
				addMode = false;
			} catch (UnexceptedTokenSyntaxError e) {

				if (e.getToken().equals("<EOF>")) {
					addMode = true;
					o.print(">> ");
					continue;
				} else
					addMode = false;

				if (e.getExceptedArray() == null) {
					System.err.println("[Line " + e.getLine()
							+ "] Syntax error on token '" + e.getToken()
							+ "', excepted '" + e.getExcepted() + "'");
					// e.printStackTrace();
				} else {
					System.err.println("[Line " + e.getLine()
							+ "] Syntax error on token '" + e.getToken()
							+ "', excepted one of:");
					for (String token : e.getExceptedArray()) {
						System.err.println("[Line " + e.getLine() + "] \t\t'"
								+ token + "'");
					}
				}

				System.err.println("[Line " + e.getLine() + "] Line: '"
						+ s.getLine(e.getLine()).trim() + "'");
			} catch (SyntaxError e) {

				System.err.println("[Line " + e.getLine() + "] "
						+ e.getMessage());
				System.err.println("[Line " + e.getLine() + "] \t"
						+ s.getLine(e.getLine()).trim());
			} catch (StackOverflowError e) {
				System.err.println("Stack overflow exception!");
			} catch (SveVariableNotFoundException e) {
				System.err.println("[RUNTIME] " + e.getMessage());
			} catch (SveRuntimeException e) {
				System.err.println("[RUNTIME] " + e.getMessage());
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}

			o.print("> ");
		}
	}

	public static StkCTree.StartTree parse(String file) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File(file)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}

		String textIn = "";
		try {
			while (in.ready())
				textIn += in.readLine() + "\n";
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		TokenScanner s = new TokenScanner();
		s.setSpecialTokens(new char[] { ';', '<', '>', '(', ')', ',', ':', '+',
				'-', '*', '/', '%', '=', '&', '|', '{', '}', '.', '!', '[',
				']', '$' });
		s.setBSpecialTokens(new String[] { "->", "=>", "==", "!=", "&&", "||",
				"<=", ">=", "++", "--", "::" });
		s.setComments(true);
		s.init(textIn);
		// System.out.println(s.getTokenList());
		StkCTree.StartTree tree = new StartTree();

		try {
			tree.parse(s);
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return tree;
		} catch (UnexceptedTokenSyntaxError e) {

			if (e.getExceptedArray() == null) {
				System.err.println("[Line " + e.getLine()
						+ "] Syntax error on token '" + e.getToken()
						+ "', excepted '" + e.getExcepted() + "'");
				// e.printStackTrace();
			} else {
				System.err.println("[Line " + e.getLine()
						+ "] Syntax error on token '" + e.getToken()
						+ "', excepted one of:");
				for (String token : e.getExceptedArray()) {
					System.err.println("[Line " + e.getLine() + "] \t\t'"
							+ token + "'");
				}
			}

			System.err.println("[Line " + e.getLine() + "] Line: '"
					+ s.getLine(e.getLine() - 1).trim() + "'");
		} catch (SyntaxError e) {

			System.err.println("[Line " + e.getLine() + "] " + e.getMessage());
			System.err.println("[Line " + e.getLine() + "] \t"
					+ s.getLine(e.getLine() - 1).trim());
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static StkCTree.StartTree parseText(String txt) throws SyntaxError {

		TokenScanner s = new TokenScanner();
		s.setSpecialTokens(new char[] { ';', '<', '>', '(', ')', ',', ':', '+',
				'-', '*', '/', '%', '=', '&', '|', '{', '}', '.', '!', '[',
				']', '$' });
		s.setBSpecialTokens(new String[] { "->", "=>", "==", "!=", "&&", "||",
				"<=", ">=", "++", "--", "::" });
		s.setComments(true);
		s.init(txt);
		// System.out.println(s.getTokenList());
		StkCTree.StartTree tree = new StartTree();

		tree.parse(s);
		return tree;

	}*/
}
