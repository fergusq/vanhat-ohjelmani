package qsharp.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Qcc {

	public static void main(String[] args) {
		BufferedReader r = null;
		try {
			teeKirjastot();
			r = new BufferedReader(new FileReader(new File(args[0])));
		
			String s;
			
			PrintWriter cppPrinter = new PrintWriter(new File(args[1]+".cpp"));
			
			PrintWriter w = new PrintWriter(new File(args[1]+"Strings.java"));
			w.print("public class " + args[1] + "Strings { \n");
			
			PrintWriter w2 = new PrintWriter(new File(args[1]+".java"));
			
			w2.println("import java.io.*;");
			
			w2.println("public class " + args[1] + 
					" { public static void main(String[] args) {" +
					"\nBufferedReader r = null;\ntry {\nr = " +
					"new BufferedReader(new FileReader(new File(args[0])));\n");
			
			w2.println("String node, s;\nnode=\"\";\n" +
					"while ((s = r.readLine()) != null && s != \"\")\n" +
					"{s = s.trim();String[] nodes = s.split(\" \");\n");
			
			w2.println("/*int i0 = -1;\nfor (String node1 : nodes) {\ni0++;\n*/" +
					"Start0 start = new Start0(nodes, 0); System.out.println(start.parse(s, 0));\n" +
					"start.parse1(nodes, s, 0);");
			
			//cppPrinter.println("#include <iostream>" TODO
			//		+"#include <string.h>"
			//		+"#include <strstream>"
			//		+"using namespace std;");
			
			while ((s = r.readLine()) != null && s != null) {
				int syvyys = -1;
				
				s =s.trim();
				int kommentti = s.indexOf("#");
				if (kommentti != -1) s = s.substring(0, kommentti);
				if (s.isEmpty()) continue;
				
				/*if (s.startsWith("token")) { TODO
					String[] lauseke = s.split(" ", 3);
					String name = lauseke[1];
					String value = lauseke[2];
					cppPrinter.println(name.toUpperCase() + "='" + value + "',");
					continue;
				}*/
				
				String[] lauseke = s.split(":=", 2);
				String nimi = lauseke[0].trim();
				
				PrintWriter w3 = new PrintWriter(new File(nimi+".java"));
				w3.print("public class " + nimi + " extends QccObj { \n");
				w3.print("String[] nodes;\n" +
						"int index;\n" +
						"public " + nimi + " (String[] n, int i) {name=\""+nimi+"\"; " +
								"nodes = n; index = i;} \n");
				
				String[] määrittelyt = lauseke[1].split("\\|");
				int i0 = 0;
				for (String määrittely : määrittelyt) {
					määrittely = määrittely.trim();
					PrintWriter w1 = new PrintWriter(new File(nimi+i0+".java"));
					w1.print("public class " + nimi + i0 + " extends QccObj { \n");
					w1.print("String[] nodes;\n" +
							"int index;\n" +
							"public " + nimi + i0 + " (String[] n, int i) {name = \""+nimi+i0+"\";" +
									" nodes = n; index = i; \n");
					String [] kohdat = määrittely.split(" ");
					int i1 = 0;
					StringBuilder s1 = new StringBuilder();
					for (String kohta : kohdat) {
						kohta = kohta.trim();
						if (kohta.matches("<[A-Z]*>")) {
							s1.append("public QccObj t"+i1+";\n");
							w1.print("t"+i1+" = new QccString(/*n[index+"+i1+"]*/\"[a-zA-Z+-9]*\");");
						} else
						if (kohta.matches("\".*\"")) {
							
							s1.append("public QccObj t"+i1+" = new QccString("+muutaRegexMerkit(kohta)+");\n");
							//w.print("public static final String " + 
							//		kohta.substring(1, kohta.length()-1).toUpperCase() + " = " + kohta + ";\n");
						} else
						if (kohta.matches("[a-zA-Z][a-zA-Z+-9]*")) {
							s1.append("public QccObj t"+i1+";\n");
							w1.append("t"+i1+" = new "+kohta+"(nodes, index+"+i1+");\n");
						} else {
							s1.append("public QccObj t"+i1+" = new QccString(\""+kohta+"\");\n"); // Regex-merkkejä
						}
						i1++;
					}
					w1.print("}\n");
					w1.print(s1.toString());
					w1.println("public boolean parse(String nodes1, int index) {");
					w1.println("String s1 = t0.toString();");
					for (int i = 1; i < i1; i++) {
						w1.println("s1+=\" *\"+t"+i+".toString();");
					}
					w1.println("System.out.println(s1); // TODO");
					w1.println("if (nodes1.matches(s1)) index++; else return false;");
					w1.println("return true;");
					w1.println("}");
					
					w1.println("public void parse1(String[] nodes0, String nodes1, int index) {");
					w1.println("if (!(t0 instanceof QccString) && nodes1.matches(t0.toString())) {t0.parse1(nodes0, nodes1, index);\n" +
							"System.out.println(\"->t"+0+"(\"+t0.name+\")\");}");
					for (int i = 1; i < i1; i++) {
						w1.println("if (!(t"+i+" instanceof QccString) && nodes1.matches(t"+i+".toString())) { t"+i+".parse1(nodes0, nodes1, index);\n" +
							"System.out.println(\"->t"+i+"(\"+t"+i+".name+\")\"); }");
					}
					//w1.print("else System.out.println(\"-><-|\");\n");
					w1.println("}");
					
					w1.println("public String toString() {");
					w1.println("String s1 = t0.toString();");
					for (int i = 1; i < i1; i++) {
						w1.println("s1 += \" *\" + t"+i+".toString();");
						
					}
					w1.println("return s1;");
					w1.println("}");
					
					w1.println("}");
					w1.flush();
					
					i0++;
				}
				
				w3.println("public String toString() {");
				w3.println("String s1 = \"(\";");
				for (int i = 0; i < i0; i++) {
					w3.println(nimi+i+" obj"+i+" = new "+nimi+i+"(nodes, index+"+i+");");
				}
				w3.println("s1 += obj0.toString();");
				for (int i = 1; i < i0; i++) {
					w3.println("s1 += \"|\"+obj"+i+".toString();");
				}
				w3.println("s1 += \")\";");
				w3.println("return s1;");
				w3.println("}");
				

				w3.println("public void parse1(String[] nodes0, String nodes1, int index) {");
				for (int i = 0; i < i0; i++) {
					w3.println("QccObj obj"+i+" = new "+nimi+i+"(nodes, index+"+i+");");
				}
				w3.println("if (!(obj0 instanceof QccString) && nodes1.matches(obj0.toString())) {obj0.parse1(nodes0, nodes1, index);\n" +
						"System.out.println(\"->obj"+0+"(\"+obj0.name+\")\");}");
				for (int i = 1; i < i0; i++) {
					w3.println("if (!(obj"+i+" instanceof QccString) && nodes1.matches(obj"+i+".toString())) { obj"+i+".parse1(nodes0, nodes1, index);\n" +
						"System.out.println(\"->obj"+i+"(\"+obj"+i+".name+\")\"); }");
				}
				//w3.print("else System.out.println(\"-><-|\");\n");
				w3.println("}");
				
				w3.print("}");
				w3.flush();
			}
			w.print("}");
			w.flush();
			
			w2.print("/*}*/}} catch (IOException e) {e.printStackTrace();}}}");
			w2.flush();
			
		} catch (IOException e) {e.printStackTrace();
		}
	}

	private static void teeKirjastot() throws FileNotFoundException {
		PrintWriter w = new PrintWriter(new File("QccString.java"));
		w.print("public class QccString extends QccObj{ \n");
		w.print("String str;\n");
		w.print("public QccString(String s) {str = s; name=\"\\\"\"+s+\"\\\"\";}\n");
		w.print("public void parse1(String[] nodes0, String nodes1, int index) {\n");
		w.print("System.out.println(\"->\"+str+\"<-|\");\n");
		w.print("}\n");
		w.print("public String toString() {return str;}\n");
		w.print("}\n");
		w.flush();
		
		w = new PrintWriter(new File("QccObj.java"));
		w.print("public abstract class QccObj{ \n");
		w.print("public String name = \"QccObj\";\n");
		w.print("public abstract void parse1(String[] nodes0, String nodes1, int index);\n");
		w.print("public abstract String toString();\n}");
		w.flush();
	}

	private static String muutaRegexMerkit(String kohta) {
		kohta = kohta.replaceAll("\\(", "\\(");
		kohta = kohta.replaceAll("\\)", "\\)");
		kohta = kohta.replaceAll("\\[", "\\[");
		kohta = kohta.replaceAll("\\]", "\\]");
		kohta = kohta.replaceAll("\\|", "\\|");
		kohta = kohta.replaceAll("\\*", "\\*");
		kohta = kohta.replaceAll("\\^", "\\^");
		kohta = kohta.replaceAll("\\/", "\\/");
		kohta = kohta.replaceAll("\\\\", "\\\\");
		kohta = kohta.replaceAll("\\.", "\\.");
		kohta = kohta.replaceAll("\\$", "\\$");
		kohta = kohta.replaceAll("\\?", "\\?");
		kohta = kohta.replaceAll("\\+", "\\+");
		return kohta;
	}
	
}
