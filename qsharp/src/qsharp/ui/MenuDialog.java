package qsharp.ui;

import jcurses.util.Protocol;
import jcurses.util.Rectangle;
import jcurses.widgets.DefaultLayoutManager;
import jcurses.widgets.Dialog;
import jcurses.widgets.GridLayoutManager;
import jcurses.widgets.Label;
import jcurses.widgets.MenuList;
import jcurses.widgets.WidgetsConstants;
import jcurses.event.ItemEvent;
import jcurses.event.ItemListener;
import jcurses.system.Toolkit;

/**
*  This class implements a popup menu window. Such windows can be used 
* for example to implemene menu bars ( currently not cantained in the library ).
* A popup menu window gives a user the possibility to select and ivoke an item from
* a list and is than closed. Separator items can be used as by <code>MenuList</code>
* described. 
*/
public class MenuDialog implements WidgetsConstants, ItemListener {
	
	private MenuList _menuList = new MenuList();
	private Label _label;
	Dialog _peer = null;
	
	private int _x = 0;
	private int _y = 0;
	String _title = null;
	private String text;
	
	private int _selectedIndex = -1;
	private String _selectedItem = null;
	private boolean showText;
	
    /**
    *  The constructor
    * 
    * @param x the x coordinate of the dialog window's top left corner
    * @param y the y coordinate of the dialog window's top left corner
    * @param title window's title
    * @param text Text in label
    */
	public MenuDialog(String title, String text) {
		_title = title;
		this.text = text;
		_label = new Label(text);
		showText = true;
		
	}
	
	/**
	    *  The constructor
	    * 
	    * @param x the x coordinate of the dialog window's top left corner
	    * @param y the y coordinate of the dialog window's top left corner
	    * @param title window's title
	    */
	public MenuDialog(String title) {
		_title = title;
		showText = false;
		
	}
	
	/**
    * Makes the window visible. Blocks, until the window is closed.
	* 
	*/
	public void show() {
		if (showText){
			int width = 0;
			String[] sa = text.split("\n");
			if (sa != null && sa.length != 0) for (int i = 0; i < sa.length; i++) if (sa[i].length()> width) width = sa[i].length();
			else width += text.length();
			int labelWidth = width;
			width += 4;
			int height = text.split("\n").length + 6 + _menuList.getItemsCount();
		
			_peer = new Dialog((Toolkit.getScreenWidth()-width)/2,(Toolkit.getScreenHeight()-height)/2,width,
								 height+1,
								 true,_title);
			DefaultLayoutManager manager1 = new DefaultLayoutManager();
			
			_peer.getRootPanel().setLayoutManager(manager1);
			manager1.addWidget(_label,1,0,labelWidth,text.split("\n").length+2,ALIGNMENT_CENTER,ALIGNMENT_CENTER);
				manager1.addWidget(_menuList,width/2-3,text.split("\n").length+3,getPreferredSize().getWidth(),getPreferredSize().getHeight(),ALIGNMENT_CENTER,ALIGNMENT_CENTER);
			_menuList.addListener(this);
			//_menuList.setTitle(_title);
			_peer.show(); 
		} else {
			int width = getPreferredSize().getWidth();
			int height = getPreferredSize().getHeight();
				
			_peer = new Dialog((Toolkit.getScreenWidth()-width)/2,(Toolkit.getScreenHeight()-height)/2,width,
									height,
									false,_title);
			GridLayoutManager manager1 = new GridLayoutManager(1,1);
			_peer.getRootPanel().setLayoutManager(manager1);
			manager1.addWidget(_menuList,0,0,1,1,ALIGNMENT_CENTER,ALIGNMENT_CENTER);
			_menuList.addListener(this);
			_menuList.setTitle(_title);
			_peer.show();
			
		}
	}
	
	protected Rectangle getPreferredSize() {
		return new Rectangle(getMaxItemLength()+2,_menuList.getItemsCount()+2);
	}
	
	
	private int getMaxItemLength() {
		int result = 0;
		for (int i=0; i<_menuList.getItemsCount(); i++) {
			int length = getItemRepresentation((getItem(i))).length();
			result = (length > result)?length:result;
		}
		
		return result;
	}
	
	private static final String SEPARATOR = "\u0000\u0000\u0000\u0000";
	private static final String SEPARATOR_STRING = "";
	
	protected String getItemRepresentation(String item) {
		if (item == SEPARATOR) {
			return SEPARATOR_STRING;
		} else {
			return item;
		}
	}
	
    /**
    *  Adds a separator item at the specified position
    * 
    * @param index position
    */
	public void addSeparator(int index) {
		_menuList.addSeparator(index);
	}
	
	
    /**
    *  Adds a separator at the end of the list.
    */
	public void addSeparator() {
		_menuList.addSeparator();
	}
	
	
    /**
    *  Adds an item at the specified position
    * 
    * @param item item to add
    * @param pos position
    */
	public void add(int pos, String item) {
		_menuList.add(pos, item);
	}
	
	
    /**
    *  Adds an item at the end of the list.
    */
	public void add(String item) {
		_menuList.add(item);
	}
	
    
	/**
	*  @return the number of items
	*/
	public int getItemsCount() {
		return _menuList.getItemsCount();
	}
	
	
    /**
    *  @return the item at the specified position
    */
	public String getItem(int index) {
		return (String)_menuList.getItem(index);
	}
	
	
	
    /**
    *  Removes the item at the specified position
    * 
    * @param pos position
    */
	public void remove(int pos) {
		_menuList.remove(pos);
	}
	
	 /**
    * Removes the first ocuurence of the specified item
    * 
    * @param item item to be removed
    * 
    */
	public void remove(String item) {
		_menuList.remove(item);
	}
	
	
	public void stateChanged(ItemEvent e) {
		_selectedIndex = e.getId();
		_selectedItem = (String) e.getItem();
		_menuList.removeListener(this);
		_peer.close();
		
	}
	
	/**
    *  Returns the last selected index. Should be invoked after the return of the <code>show</code>
    * to get the result
    * 
    * @return last selected index
    */
	public int getSelectedIndex() {
		return _selectedIndex;
	}
	
	/**
    *  Returns the last selected item. Should be invoked after the return of the <code>show</code>
    * to get the result
    * 
    * @return last selected index
    */
	public String getSelectedItem() {
		return _selectedItem;
	}
	
	
	

}
