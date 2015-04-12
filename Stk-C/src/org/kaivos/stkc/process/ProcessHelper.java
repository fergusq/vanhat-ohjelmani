package org.kaivos.stkc.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessHelper {

	public static String getErrors(Process p) throws IOException {
		String errors = "";
		
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		
		while (r.ready()) {
			errors += r.readLine() + "\n";
		}
		
		return errors;
	}
	
	public static String getOutput(Process p) throws IOException {
		String output = "";
		
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		while (r.ready()) {
			output += r.readLine() + "\n";
		}
		
		return output;
	}
	
	public static String getOutputAndErrors(Process p) throws IOException {
		return getOutput(p) + "\n" + getErrors(p);
	}
	
}
