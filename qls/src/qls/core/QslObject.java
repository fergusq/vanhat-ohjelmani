package qls.core;

public class QslObject {
	private QslObject parent  = null;
	       
	       /**
	        * void constructor
	        *
	        */
	       public QslObject()
	       {
	           //nothing ...
	       }
	       
	       /**
	        * Constructor with parent object
	        * @param parent
	        */
	       public QslObject (QslObject parent)
	       {
	           this.parent = parent;
	       }
	       
	       /**
	        * Get an object parent
	        * @return StticObject
	        */
	       public QslObject getParent()
	       {
	           return parent;
	       }
}
