package cubegame.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

public class Logger {
	StringBuffer messages = new StringBuffer();
	
	private String getTime(){
		return Calendar.getInstance().get(Calendar.YEAR) + "-" + 
		(1+Calendar.getInstance().get(Calendar.MONTH)) + "-" + 
		Calendar.getInstance().get(Calendar.DATE) + " " + 
		Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + 
		Calendar.getInstance().get(Calendar.MINUTE);
	}
	private String getDate(){
		return Calendar.getInstance().get(Calendar.YEAR) + "-" + 
		(1+Calendar.getInstance().get(Calendar.MONTH)) + "-" + 
		Calendar.getInstance().get(Calendar.DATE);
	}
	
	public void log(String msg){
		String logmsg = getTime() + " [INFO] " + 
		msg;
		
		System.out.println(logmsg);
		messages.append(logmsg+"\n");
	}
	
	public void warn(String msg){
		String logmsg = getTime() + " [WARNING] " + 
		msg;
		
		System.out.println(logmsg);
		messages.append(logmsg+"\n");
	}
	
	public void save() throws FileNotFoundException{
		PrintWriter out = new PrintWriter(new File("logs/log_" + getDate()));
		out.println(messages.toString());
		out.close();
	}
}
