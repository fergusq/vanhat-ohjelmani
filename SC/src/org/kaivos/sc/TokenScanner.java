package org.kaivos.sc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class TokenScanner {
	
	private ArrayList<String> tokens;
	private char[] sTokens;
	private int next;
	private int line;
	public boolean parseSpaces = true;
	
	public TokenScanner() {
		
		next = 0;
		line = 0;
	}
	
	public void setSpecialTokens(char[] tokens) {
		this.sTokens = tokens;
	}
	
	public void setParseSpaces(boolean b) {
		parseSpaces = b;
	}
	
	public boolean hasNext() {
		return next <= tokens.size()-1;
	}
	
	public String next() {
		String nextS = tokens.get(next++);
		while (nextS.replace(" ",	 " ").equals("\n")) {
			line++;
			nextS = tokens.get(next++);
		}
		return nextS;
	}
	
	public int line() {
		return line;
	}
	
	
	public ArrayList<String> getTokenList() {
		return tokens;
	}
	
	public void init(String code) throws StringIndexOutOfBoundsException {
		line = 0;
		this.tokens = parseCode(code);
		next = 0;
	}
	
	private ArrayList<String> parseCode(String rawcode) throws StringIndexOutOfBoundsException {
		ArrayList<String> code = new ArrayList<String>();
		
		int char1 = 0;
		
		String text = "";
		
		while (char1 < rawcode.length()) {
			char char2 = rawcode.charAt(char1);
			
			if (search(sTokens, char2) > -1) {
				if (!text.isEmpty()) {
					code.add(text);
					text = "";
				}
				code.add("" + char2);
				
			}
			else switch (char2) {
			case '"':
				if (!text.isEmpty()) {
					code.add(text);
					text = "";
				}
				String text2 = rawcode.substring(char1, rawcode.indexOf('"', char1+1)+1);
				
				
				
				code.add(text2);
				
				char1 = rawcode.indexOf('"', char1+1);
				break;
			case '{':
				if (!text.isEmpty()) {
					code.add(text);
					text = "";
				}
				int fromS = char1+1;
				int depth = 0;
				
				while (true) {
					char char3 = rawcode.charAt(fromS++);
					if (char3 == '{') depth++;
					if (char3 == '}') depth--;
					if (depth == -1) break;
				}
				
				String text3 = rawcode.substring(char1, fromS);
				
				if (text3.contains("\n")) {
				}
				
				code.add(text3);
				
				char1 = fromS+1;
				break;
			case '\n':
				code.add("\n");
			case '\t':
			case ' ':
				if (parseSpaces) {
					if (!text.isEmpty()) {
						code.add(text);
						text = "";
					}
					break;
				}
				
			default:
				text+=char2;
				break;
			}
			char1++;
			//if (char2 == '~') {
			//	break;
			//}
		}
		if (!text.isEmpty()) {
			code.add(text);
			text = "";
		}
		/*for (Object o : code) {
			System.out.println(o);
		}*/
		return code;
		
	}

	private int search(char[] sTokens2, char char2) {
		for (int i = 0; i < sTokens2.length; i++) {
			if (sTokens2[i] == char2) {
				return i;
			}
		}
		return -1;
	}
}
