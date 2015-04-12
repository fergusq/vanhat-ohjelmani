package qsharp.lang;

import java.util.HashMap;

import qsharp.ui.MenuDialog;

public class QScope {

	QScope parent;
	
	public HashMap<String, Command> commands = new HashMap<String, Command>();
	public HashMap<String, Command> functions = new HashMap<String, Command>();
	public HashMap<String, String> stringVars = new HashMap<String, String>();
	public HashMap<String, Integer> intVars = new HashMap<String, Integer>();
	public HashMap<String, Double> doubleVars = new HashMap<String, Double>();
	public HashMap<String, MenuDialog> uiMenuVars = new HashMap<String, MenuDialog>();
	public HashMap<String, Command> arrayVars = new HashMap<String, Command>();
	
	public QScope(QScope parent) {
		if (parent == null);
		this.parent = parent;
	}
	
	public Command getCommand(String key) {
		if (parent != null) {
			Command c = commands.get(key);
			if (c == null) c = parent.getCommand(key);
			return c;
		}
		return commands.get(key);
	}
	
	public Command getFunction(String key) {
		if (parent != null) {
			Command c = functions.get(key);
			if (c == null) c = parent.getFunction(key);
			return c;
		}
		return functions.get(key);
	}
	
	public String getString(String key) {
		if (parent != null) {
			String c = stringVars.get(key);
			if (c == null) c = parent.getString(key);
			return c;
		}
		return stringVars.get(key);
	}
	
	public boolean setString(String key, String value) {
		String c = stringVars.get(key);
		if (c != null)  {
			stringVars.put(key, value);
		} else if (parent != null) {
			parent.setString(key, value);
		} else {
			return false;
		}
		return true;
	}
	
	public boolean containsString(String key) {
		return getString(key) != null;
	}
	
	public Integer getInt(String key) {
		if (parent != null) {
			Integer c = intVars.get(key);
			if (c == null) c = parent.getInt(key);
			return c;
		}
		return intVars.get(key);
	}
	
	public boolean setInt(String key, int value) {
		Integer c = intVars.get(key);
		if (c != null)  {
			intVars.put(key, value);
		} else if (parent != null) {
			parent.setInt(key, value);
		} else {
			return false;
		}
		return true;
	}
	
	public boolean containsInt(String key) {
		return getInt(key) != null;
	}
	
	public Double getDouble(String key) {
		if (parent != null) {
			Double c = doubleVars.get(key);
			if (c == null) c = parent.getDouble(key);
			return c;
		}
		return doubleVars.get(key);
	}
	
	public boolean containsDouble(String key) {
		return getDouble(key) != null;
	}
	
	public boolean setDouble(String key, double value) {
		Double c = doubleVars.get(key);
		if (c != null)  {
			doubleVars.put(key, value);
		} else if (parent != null) {
			parent.setDouble(key, value);
		} else {
			return false;
		}
		return true;
	}
	
	public MenuDialog getUiMenuVar(String key) {
		if (parent != null) {
			MenuDialog c = uiMenuVars.get(key);
			if (c == null) c = parent.getUiMenuVar(key);
			return c;
		}
		return uiMenuVars.get(key);
	}
	
	public boolean setUiMenuVar(String key, MenuDialog value) {
		MenuDialog c = uiMenuVars.get(key);
		if (c != null)  {
			uiMenuVars.put(key, value);
		} else if (parent != null) {
			parent.setUiMenuVar(key, value);
		} else {
			return false;
		}
		return true;
	}
	
	public Command getArray(String key) {
		if (parent != null) {
			Command c = arrayVars.get(key);
			if (c == null) c = parent.getArray(key);
			return c;
		}
		return arrayVars.get(key);
	}
	
}
