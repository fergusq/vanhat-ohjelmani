package qls.core;

import java.util.Hashtable;

public class QslScope  extends QslObject{
	private Hashtable <String,QslVariable> childs = new Hashtable<String,QslVariable>();
	   
	       /**
	        * Constructor for setting up parent
	        * @param parent
	        */
	       public QslScope( QslObject parent )
	       {
	           super(parent);
	       }
	       
	       /**
	        * return a childs hash
	        * @return
	        */
	       public Hashtable <String,QslVariable> getChilds()
	       {
	           return childs;
	       }
	       
	       /**
	        * Add a child (variable) to scope for variable lifecycle and variable visibility
	        * 
	        * @param name
	        * @param child
	        * @return boolean
	        */
	       public boolean pushChild(String name, QslVariable child)
	       {
	           return this.childs.put( name , child) != null;
	       }
	       
	       /**
	        * Return a child by name, fetching finding in current scope and parents 
	        * 
	        * @param name
	        * @return StticVariable
	        */
	       public QslVariable child(String name)
	       {
	           return child ( name , this);
	       }
	       
	       /**
	        * ;) closure for finding a variable by name
	        * 
	        * @param name
	        * @param scope
	        * @return StticVariable
	        */
	       private QslVariable child(String name, QslScope scope)
	       {
	           if( scope.getChilds().containsKey( name ) )
	           {
	               return scope.getChilds().get( name );
	           }
	           else if( scope.getParent() != null && scope.getParent() instanceof QslScope )
	           {
	               return child(name, (QslScope)scope.getParent() );
	           }
	           return null;
	       }
	       
	       /**
	        * Check existens of variables
	        * @param name
	        * @return boolean
	        */
	       public boolean existsChild(String name){
	          return existsChild(name, this);
	      }
	      
	      /**
	       * ;) closure for finding a variable by name
	       * 
	       * @param name
	       * @param scope
	       * @return boolean
	       */
	      private boolean existsChild(String name, QslScope scope)
	      {
	          if( scope.getChilds().containsKey( name ) )
	          {
	              return true;
	          }
	          else if( scope.getParent() != null && scope.getParent() instanceof QslScope )
	          {
	              return existsChild(name, (QslScope) scope.getParent());
	          }
	          return false;
	      }
}
