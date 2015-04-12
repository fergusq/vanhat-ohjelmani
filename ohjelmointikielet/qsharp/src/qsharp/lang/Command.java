package qsharp.lang;

import java.util.ArrayList;
import java.util.HashMap;

import qsharp.lang.Console.Type;

public class Command {
	
	public String[] lines;
	private ArrayList<String> lines2;
	public HashMap<String, Type> params = new HashMap<String, Type>();
	
	public Command(String[] lines){
		this.lines = lines;
	}
	
	public Command(int size){
		this.lines = new String[size];
	}
	
	public Command() {
		lines = null;
		lines2 = new ArrayList<String>();
	}
	
	public String getLine(int l) {
		return lines2.get(l);
	}
	
	public void setLine(int l, String s) {
		lines2.remove(l);
		lines2.add(l, s);
	}
	
	public void addLine(int l, String s) {
		lines2.add(l, s);
	}
	
	public void addLine(String s) {
		lines2.add(s);
	}

	public int length() {
		if (lines == null) return lines2.size();
		return lines.length;
	}
	
}
