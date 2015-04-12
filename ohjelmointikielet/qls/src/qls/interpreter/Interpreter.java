package qls.interpreter;

import java.util.Iterator;
import java.util.LinkedList;

import qls.core.QslCmd;
import qls.core.QslFunc;
import qls.core.QslScope;
import qls.core.QslValue;
import qls.core.QslVariable;
import qls.core.VariableNotFoundException;
import qls.reflection.QslReflection;
import qls.syntaxtree.AdditiveExpression;
import qls.syntaxtree.CmdExpression;
import qls.syntaxtree.CreateNew;
import qls.syntaxtree.FieldDeclaration;
import qls.syntaxtree.FuncExpression;
import qls.syntaxtree.GetFrom;
import qls.syntaxtree.GlobalExpression;
import qls.syntaxtree.INode;
import qls.syntaxtree.IfExpression;
import qls.syntaxtree.JavaStaticMethods;
import qls.syntaxtree.Main;
import qls.syntaxtree.MathExpression;
import qls.syntaxtree.MultiplicativeExpression;
import qls.syntaxtree.NodeChoice;
import qls.syntaxtree.NodeListOptional;
import qls.syntaxtree.NodeSequence;
import qls.syntaxtree.NodeToken;
import qls.syntaxtree.Proceed;
import qls.syntaxtree.RelationalEqualityExpression;
import qls.syntaxtree.RelationalExpression;
import qls.syntaxtree.RelationalGreaterExpression;
import qls.syntaxtree.RelationalLessExpression;
import qls.syntaxtree.ReturnStatement;
import qls.syntaxtree.Start;
import qls.syntaxtree.StatementExpression;
import qls.syntaxtree.UnaryExpression;
import qls.syntaxtree.UnaryRelational;
import qls.syntaxtree.Use;
import qls.syntaxtree.VariableAssign;
import qls.syntaxtree.VariableDeclaration;
import qls.syntaxtree.VariableName;
import qls.syntaxtree.VariableValue;
import qls.syntaxtree.WhileExpression;

public class Interpreter implements Interpret{
	
	       public Object visit(Start node) throws Exception {
	           
	           /*
	            * first setup of imported packages and add it to QslReflection
	            * class for using it with reflection full class identifier like :
	            *      "java.lang.System"
	            * the elements f0 ... fn has generated automatically by JTB =)
	            */
	           
	           Iterator<INode> importedPackagesEnum = node.f0.elements();
	           while( importedPackagesEnum.hasNext() )
	           {
	               // adding required packages
	               NodeSequence ns = (NodeSequence) importedPackagesEnum.next();
	               QslReflection.pushPackage( this.visit( (Use) ns.elementAt( 0 ) , null).toString() );
	           }
	           
	           /*
	            *~ Now! press start to play, maybe if you not lost all your credits
	            *~ in article and configurations, you lost them all here =)
	            *
	            * okay, after importation we start interpreting a source code
	            * 
	            * testing if exists a code after "require
	            * require java lang.
	            * System:ou:println(  ). "first statement
	            * System:ou:println(  ). "second statement
	            */
	           
	           QslScope parent = new QslScope( null );
	           
	            if( node.f2.size() >  0)
	            {
	               //~ creating of parent scope
	                Iterator<INode> statement = node.f2.elements();
	                while( statement.hasNext() )
	                {
	                	GlobalExpression n = (GlobalExpression)statement.next();
	                	if (n.f0.choice instanceof FieldDeclaration) {
	                		this.visit( (FieldDeclaration) n.f0.choice, parent); 
	                	} else
	                	this.visit( (CreateNew) n.f0.choice, parent); 
	                }
	            }
	           
	            if( node.f4.size() >  0)
	            {
	               //~ creating of parent scope
	                Iterator<INode> statement = node.f4.elements();
	                while( statement.hasNext() )
	                {
	                	GlobalExpression n = (GlobalExpression)statement.next();
	                	if (n.f0.choice instanceof FieldDeclaration) {
	                		this.visit( (FieldDeclaration) n.f0.choice, parent); 
	                	} else
	                	this.visit( (CreateNew) n.f0.choice, parent); 
	                }
	            }
	         
	            
	            this.visit( node.f3 , parent); 
	            
	            return null;
	       }
	   
	       @Override
			public Object visit(Main node, QslScope scope, Object... objects)
					throws Exception {
	    	   
	    	   if( node.f1.nodes.size() >  0)
	            {
	               //~ creating of parent scope
	                QslScope parent = new QslScope( scope );
	                Iterator<INode> statement = node.f1.elements();
	                while( statement.hasNext() )
	                {
	                    this.visit( (StatementExpression)statement.next() , parent); 
	                }
	            }
	    	   
	    	   return null;
			}
	       
	       /**
	       * this method allow retrieving and transforming required packages
	       * to Java format 
	       */
	      public Object visit(Use node, QslScope scope, Object... objects) {
	          /*
	           * before begin: 
	           *      notice to all reserved keywords has ignored !
	           *      f0 : content a keyword we ignore it
	           */
	          StringBuilder builder = new StringBuilder();
	          Iterator<INode> element = node.f1.elements();
	          while( element.hasNext() )
	          {
	              builder.append( element.next() ) ;
	              if( element.hasNext() )
	              {
	                  builder.append( "." );
	              }
	          }
	          return builder;
	      }
	  
	      /**
	       * integers operation
	       */
	      public Object visit(MathExpression node, QslScope scope, Object... objects) throws Exception {
	          return this.visit(node.f0, scope, objects);
	      }
	  
	      /**
	       * additive operations
	       */
	      public Object visit(AdditiveExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          
	          QslValue value = (QslValue)this.visit(node.f0, scope, objects);
	          
	          Iterator<INode> e = node.f1.elements();
	          while( e.hasNext() )
	          {
	              NodeSequence ns = (NodeSequence) e.next();
	              NodeChoice nc = (NodeChoice) ns.elementAt(0);
	              
	              QslValue tmp = (QslValue) this.visit( (MultiplicativeExpression) ns.elementAt(1) , scope, objects);
	              if( nc.choice.toString().equals("&"))
	              {
	            	  tmp.setType(String.class);
	                  tmp.setValue( value.getValue() + tmp.getValue() );
	                  return tmp;
	              }	else if( nc.choice.toString().equals("+") && value.getType() == long.class )
	              {
	            	  tmp.setType(long.class);
	                  tmp.setValue( Long.parseLong(value.getValue()) + Long.parseLong(tmp.getValue()) );
	                  return tmp;
	              }
	              else if( nc.choice.toString().equals("-") && value.getType() == long.class)
	              {
	            	  tmp.setType(long.class);
	                  tmp.setValue( Long.parseLong(value.getValue()) - Long.parseLong(tmp.getValue()) );
	                  return tmp;
	              }
	          }
	          return value;
	      }
	  
	      /**
	       * multiplicative operation
	       */
	      public Object visit(MultiplicativeExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          
	          QslValue value = (QslValue)this.visit(node.f0, scope, objects);
	          
	          Iterator<INode> e = node.f1.elements();
	          while( e.hasNext() )
	          {
	              NodeSequence ns = (NodeSequence) e.next();
	              NodeChoice nc = (NodeChoice) ns.elementAt(0);
	              QslValue tmp = (QslValue) this.visit( (UnaryExpression) ns.elementAt(1) , scope, objects);
	              
	              if (tmp.getType() == long.class && value.getType() == long.class) {
	              
	              if( nc.choice.toString().equals("*") )
	              {
	                  tmp.setValue( Long.parseLong(value.getValue()) * Long.parseLong(tmp.getValue()) );
	                  return tmp;
	              }
	              else if( nc.choice.toString().equals("/") )
	              {
	                  tmp.setValue( Long.parseLong(value.getValue()) / Long.parseLong(tmp.getValue()) );
	                  return tmp;
	              }
	              else if( nc.choice.toString().equals("%") )
	              {
	                  tmp.setValue( Long.parseLong(value.getValue()) % Long.parseLong(tmp.getValue()) );
	                  return tmp;
	              }
	              
	              }
	          }
	          return value;
	      }
	  
	      /**
	       * getting values
	       */
	      public Object visit(UnaryExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          /*
	           * We are allowed just operation in IN (integers) ;)
	           */
	          if( node.f0.choice instanceof NodeToken )
	          {
	        	  if (node.f0.choice.toString().startsWith("'") && node.f0.choice.toString().endsWith("'")) {
	        		  QslValue value = new QslValue();
		              value.setValue( node.f0.choice.toString().substring(1, node.f0.choice.toString().length()-1) );
		              value.setType( String.class ); // here ;)
		              return value;
	        	  }
	              QslValue value = new QslValue();
	              value.setValue( Long.parseLong( node.f0.choice.toString() ) );
	              value.setType( long.class ); // here ;)
	              return value;
	          } else if( node.f0.choice instanceof VariableValue )
	          {
	        	  Object o = this.visit((VariableValue)node.f0.choice, scope, objects);
	        	  if (o.toString().startsWith("'") && o.toString().endsWith("'")) {
	        		  QslValue value = new QslValue();
		              value.setValue( o.toString() );
		              value.setType( String.class ); // here ;)
		              return value;
	        	  }
	              QslValue value = new QslValue();
	              value.setValue( Long.parseLong( o.toString() ) );
	              value.setType( long.class ); // here ;)
	              return value;
	          }
	          else if( node.f0.choice instanceof VariableName )
	          {
	              String var = this.visit( (VariableName) node.f0.choice, scope, objects)
	                          .toString();
	              if( scope.existsChild( var ) )
	              {
	                  return scope.child( var ).getVariableValue();
	              }
	              else
	              {
	                  new VariableNotFoundException("The variable " + var + " not exists!" ).send();
	              }
	          }
	          else if( node.f0.choice instanceof NodeSequence )
	          {
	              NodeSequence ns = (NodeSequence) node.f0.choice ;
	              return this.visit( (MathExpression) ns.elementAt(1) , scope, objects) ;
	          }
	          return null;
	      }
	  
	      /**
	       * relational testing ...
	       */
	      public Object visit(RelationalExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          
	          return this.visit(node.f0, scope, objects);
	      }
	  
	      /**
	       * testing for equality " == "
	       */
	      public Object visit(RelationalEqualityExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          
	          Object obj = this.visit(node.f0, scope, objects);
	          if( node.f1.elements() != null && obj instanceof Long && node.f1.elements().hasNext())
	          {
	              NodeSequence ns = (NodeSequence) node.f1.elements().next();
	              Object tmp = this.visit( (RelationalGreaterExpression) ns.elementAt(1), scope, objects);
	              if( tmp instanceof Long)
	              {
	                  NodeChoice nc = (NodeChoice) ns.elementAt(0) ;
	                  if( nc.choice.toString().equals("==") )
	                  {
	                      obj = Long.parseLong( obj.toString() ) ==  Long.parseLong( tmp.toString() );
	                  }
	                  else if( nc.choice.toString().equals("!=") )
	                  {
	                      obj = Long.parseLong( obj.toString() ) !=  Long.parseLong( tmp.toString() );
	                  }
	              }
	          }
	          return obj;
	      }
	  
	      /**
	       * testing for greater value " > "
	       */
	      public Object visit(RelationalGreaterExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          
	          Object obj = this.visit(node.f0, scope, objects);
	          if( node.f1.elements() != null && obj instanceof Long && node.f1.elements().hasNext())
	          {
	              NodeSequence ns = (NodeSequence) node.f1.elements().next();
	              Object tmp = this.visit( (RelationalLessExpression) ns.elementAt(1), scope, objects);
	              if( tmp instanceof Long)
	              {
	                  NodeChoice nc = (NodeChoice) ns.elementAt(0) ;
	                  if( nc.choice.toString().equals(">") )
	                  {
	                      obj = Long.parseLong( obj.toString() ) >  Long.parseLong( tmp.toString() );
	                  }
	                  else if( nc.choice.toString().equals(">=") )
	                  {
	                      obj = Long.parseLong( obj.toString() ) >=  Long.parseLong( tmp.toString() );
	                  }
	              }
	          }
	          return obj;
	      }
	      
	      /**
	       * test for less value " < "
	       */
	      public Object visit(RelationalLessExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          Object obj = this.visit(node.f0, scope, objects);
	          if( node.f1.elements() != null && obj instanceof Long)
	          {
	              NodeSequence ns = (NodeSequence) node.f1.elements().next();
	              Object tmp = this.visit( (UnaryRelational) ns.elementAt(1), scope, objects);
	              if( tmp instanceof Long)
	              {
	                  NodeChoice nc = (NodeChoice) ns.elementAt(0) ;
	                  if( nc.choice.toString().equals("<") )
	                  {
	                      obj = Long.parseLong( obj.toString() ) <  Long.parseLong( tmp.toString() );
	                  }
	                  else if( nc.choice.toString().equals("<=") )
	                  {
	                      obj = Long.parseLong( obj.toString() ) <=  Long.parseLong( tmp.toString() );
	                  }
	              }
	          }
	          return obj;
	      }
	  
	      /**
	       * method for getting a value to be tested
	       */
	      public Object visit(UnaryRelational node, QslScope scope,
	              Object... objects) throws Exception {
	          
	    	  if (node.f0.choice instanceof VariableName) {
	    		  String name = (String) this.visit((VariableName)node.f0.choice, scope, objects) ;
	    		  if (scope.existsChild( name )) {
	    			  QslVariable var = (QslVariable) scope.child( name ) ;
	    			  if (var.getVariableValue().getType() == long.class) {
	    				  return Long.parseLong(var.getVariableValue().getValue());
	    			  }
	    			  return var.getVariableValue().getValue();
	    		  }
	    		  
	    	  }
	    	  
	    	  if (((NodeToken)node.f0.choice).tokenImage.startsWith("'") && ((NodeToken)node.f0.choice).tokenImage.endsWith("'")) {
        		  QslValue value = new QslValue();
	              value.setValue( ((NodeToken)node.f0.choice).tokenImage.substring(1, ((NodeToken)node.f0.choice).tokenImage.length()-1) );
	              value.setType( String.class ); // here ;)
	              return value;
        	  }
	    	  
	    	  QslValue l = new QslValue();
	    	  l.setType(long.class);
	          l.setValue(Long.parseLong(((NodeToken)node.f0.choice).tokenImage));
	          return Long.parseLong(l.getValue());
	      }
	  
	      /**
	       * Qsl "if" condition
	       */
	      public Object visit(IfExpression node, QslScope scope, Object... objects) throws Exception {
	          
	          /*
	           * like variable declaration we ignore all keywords, for more information
	           * see interface Interpret.java or JTB grammar
	           */
	          QslScope ifScope = new QslScope( scope );
	          if( new Boolean(this.visit(node.f1, scope, objects).toString()) )
	          {
	              Iterator<INode> e = node.f3.elements();
	              while( e.hasNext() )
	              {
	                  this.visit( (StatementExpression)e.next() , ifScope, objects);
	              }
	          }
	          return null;
	      }
	  
	      /**
	       * Qsl "while" expression
	       */
	      public Object visit(WhileExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          /*
	           * like variable declaration we ignore all keywords, for more information
	           * see interface Interpret.java or JTB grammar
	           */
	          QslScope whileScope = new QslScope( scope );
	          while( new Boolean(this.visit(node.f1, scope, objects).toString()) )
	          {
	              Iterator<INode> e = node.f3.elements();
	              while( e.hasNext() )
	              {
	                  this.visit( (StatementExpression)e.next() , whileScope, objects);
	              }
	          }
	          return null;
	      }
	  
	      /**
	       * variable declaration and assignment
	       * @throws Exception 
	       */
	      public Object visit(VariableDeclaration node, QslScope scope,
	              Object... objects) throws Exception {
	          /*
	           * we ignore "def", "=" and "." keywords
	           */
	          
	          QslVariable var = new QslVariable();
	          var.setVariableName( this.visit( node.f1 , scope, objects).toString() ) ;
	          var.setVariableValue( (QslValue) this.visit(node.f3, scope, objects) );

	          
	          
	          /*
	           * we add a variable to current scope for variable life cycle
	           * and visibility.
	           */
	          scope.pushChild( var.getVariableName() , var );
	          return null;
	      }
	  
	      /**
	       * assigning a new value to variable
	       * @throws Exception 
	       */
	      public Object visit(VariableAssign node, QslScope scope, Object... objects) throws Exception {
	          String name = this.visit(node.f1, scope, objects).toString() ;
	          if( scope.existsChild( name ) )
	          {
	              QslVariable var = (QslVariable) scope.child( name ) ;
	              var.setVariableValue( (QslValue) this.visit(node.f3, scope, objects) );
	          }
	          return null;
	      }
	  
	      /**
	       * getting a variable name
	       */
	      public Object visit(VariableName node, QslScope scope, Object... objects) {
	          return node.f0.tokenImage;
	      }
	  
	      /**
	       * method for executing a static Java methods
	       * @throws Exception 
	       */
	      public Object visit(JavaStaticMethods node, QslScope scope,
	              Object... objects) throws Exception {
	          
	          /*
	           * Okay, firstly we need to test existence of class and fields or method
	           * after, we get a value for arguments, finally we invoke a static Java Method 
	           */
	          
	    	//f0 is methid name 
	    	  
	          //f6 is class name 
	          String identifier = QslReflection.fullIdentifier( node.f5.tokenImage ) ;
	          if( identifier != null )
	          {
	              // making a class object
	              Object currentObject = QslReflection.makeObject ( identifier );
	              if( currentObject != null ){
	                  Iterator<INode> e = node.f6.elements();
	                  //~ getting a last field object
	                  while( e.hasNext() )
	                  {
	                      NodeSequence ns = (NodeSequence) e.next(); 
	                      if( QslReflection.existsField( currentObject , ns.elementAt(1).toString()  ) )
	                      {
	                          currentObject = QslReflection.getFieldObject( currentObject , ns.elementAt(1).toString() );
	                      }
	                      else
	                      {
	                          
	                          break;
	                      }
	                  }
	                  
	                  LinkedList<QslValue> params = new LinkedList<QslValue>();
	                  if (node.f2.present()) {
	                	  NodeSequence l = ((NodeSequence)node.f2.node);
	                	  params.add( (QslValue) this.visit((VariableValue)l.elementAt(0), scope, objects) );
	                	  Iterator<INode> eVal = ((NodeListOptional)l.elementAt(1)).elements();
	                  
	                	  while( eVal.hasNext() )
                      		{
	                		  NodeSequence nsVal = (NodeSequence) eVal.next();
	                		  params.add( (QslValue) this.visit( (VariableValue) nsVal.elementAt(1) , scope, objects) );
                      		}
	                  }
	                  
                      
                      
                      //~ test and invoking
                      if( QslReflection.existsSubroutine( currentObject , node.f0.tokenImage , params.toArray( new QslValue[]{} )) )
                      {
                          return QslReflection.invokeStaticSubroutine( currentObject , node.f0.tokenImage , params.toArray( new QslValue[]{} )) ;
                      }
	              }
	          }
	          
	          return null;
	      }
	  
	      /**
	       * Statement is a core of interpreting Qsl source code
	       * @throws Exception 
	       */
	      public Object visit(StatementExpression node, QslScope scope,
	              Object... objects) throws Exception {
	          /*
	           * Statement expression do *NOTHING* =) just redirecting
	           * to right statement!
	           * >> redirecting? what you mean here man, we're not in Web or JavaScript ?
	           * yes, redirecting because here we don't allowed to execute any statement
	           * just returning a result from right statement =)!
	           * 
	           * NOTE::   you're free to do what you like here just for this article we're
	           *          not allowed to executing any statement for creating an easy and
	           *          clear code source.
	           * 
	           * and remember in JTB file we have
	           *  
	                  VariableDeclaration()
	                  | LOOKAHEAD() VariableAssign()
	                  | JavaStaticMethods()
	                  | IfExpression()    
	                  | WhileExpression()
	           *
	           *
	           */
	          
	          if( node.f0.choice instanceof VariableDeclaration )
	          {
	              return this.visit( (VariableDeclaration) node.f0.choice, scope, objects);
	          }
	          else if( node.f0.choice instanceof VariableAssign)
	          {
	              return this.visit( (VariableAssign) node.f0.choice, scope, objects);
	          }
	          else if( node.f0.choice instanceof JavaStaticMethods)
	          {
	              return this.visit( (JavaStaticMethods) node.f0.choice, scope, objects);
	          }
	          else if( node.f0.choice instanceof IfExpression)
	          {
	              return this.visit( (IfExpression) node.f0.choice, scope, objects);
	          }
	          else if( node.f0.choice instanceof WhileExpression)
	          {
	              return this.visit( (WhileExpression) node.f0.choice, scope, objects);
	          }
	          else if( node.f0.choice instanceof CmdExpression )
	          {
	              return this.visit( (CmdExpression) node.f0.choice, scope, objects);
	          }
	          else if( node.f0.choice instanceof Proceed )
	          {
	              return this.visit( (Proceed) node.f0.choice, scope, objects);
	          }
	          return null;
	      }

		@Override
		public Object visit(CmdExpression node, QslScope scope,
				Object... objects) throws Exception {
			
			 /*
	           * we ignore "create", "new" and "." keywords
	           */
	          
	          QslCmd var = new QslCmd();
	          var.setVariableName( this.visit( node.f1 , scope, objects).toString() ) ;
	          
	          Iterator<INode> e = node.f2.elements();
	          StatementExpression[] arr = new StatementExpression[node.f2.nodes.size()];
	          int i = 0;
              while( e.hasNext() )
              {
                  arr[i] = (StatementExpression)e.next();
                  i++;
              }
	          var.setCmd(arr);
	          
	          /*
	           * we add a variable to current scope for variable life cycle
	           * and visibility.
	           */
	          scope.pushChild( var.getVariableName() , var );
	          return null;
		}
		
		@Override
		public Object visit(FuncExpression node, QslScope scope,
				Object... objects) throws Exception {
			
			 /*
	           * we ignore "create", "new" and "." keywords
	           */
	          
	          QslFunc var = new QslFunc(node.f3);
	          var.setVariableName( this.visit( node.f1 , scope, objects).toString() ) ;
	          
	          Iterator<INode> e = node.f2.elements();
	          StatementExpression[] arr = new StatementExpression[node.f2.nodes.size()];
	          int i = 0;
              while( e.hasNext() )
              {
                  arr[i] = (StatementExpression)e.next();
                  i++;
              }
	          var.setCmd(arr);
	          
	          /*
	           * we add a variable to current scope for variable life cycle
	           * and visibility.
	           */
	          scope.pushChild( var.getVariableName() , var );
	          return null;
		}

		@Override
		public Object visit(Proceed node, QslScope scope, Object... objects)
				throws Exception {
			
			String name = this.visit(node.f1, scope, objects).toString() ;
			
			QslScope s = new QslScope(scope);
	          if( s.existsChild( name ) )
	          {
	              QslCmd var = (QslCmd) scope.child( name ) ;
	              for (int i = 0; i < var.getCmd().length; i++) {
	            	  this.visit(var.getCmd()[i], s, objects);
	              }
	          } else {
                  new VariableNotFoundException("The cmd " + name + " not exists!" ).send();
              }
	          return null;
		}

		@Override
		public Object visit(VariableValue node, QslScope scope,
				Object... objects) throws Exception {
			if( node.f0.choice instanceof MathExpression )
	          {
	              return this.visit( (MathExpression) node.f0.choice, scope, objects);
	          } else if( node.f0.choice instanceof GetFrom )
	          {
	              return this.visit( (GetFrom) node.f0.choice, scope, objects);
	          } else {
	        	  QslValue v = new QslValue();
	        	  v.setType(String.class);
	        	  v.setValue(node.f0.choice.toString().substring(1, node.f0.choice.toString().length()-1));
	        	  return v;
	          }
		}

		@Override
		public Object visit(FieldDeclaration node, QslScope scope,
				Object... objects) throws Exception {
				/*
	           * we ignore "global", "to" and "." keywords
	           */
	          
	          QslVariable var = new QslVariable();
	          var.setVariableName( this.visit( node.f1 , scope, objects).toString() ) ;
	          var.setVariableValue( (QslValue) this.visit(node.f3, scope, objects) );

	          
	          
	          /*
	           * we add a field to current scope for field life cycle
	           * and visibility.
	           */
	          scope.pushChild( var.getVariableName() , var );
	          return null;
		}

		@Override
		public Object visit(CreateNew node, QslScope scope, Object... objects)
				throws Exception {
			if (node.f2.choice instanceof CmdExpression) {
				return this.visit( (CmdExpression) node.f2.choice, scope); 
        	}
			if (node.f2.choice instanceof FuncExpression) {
				return this.visit( (FuncExpression) node.f2.choice, scope); 
        	} 
        	return null;
		}

		@Override
		public Object visit(ReturnStatement node, QslScope scope,
				Object... objects) throws Exception {
			NodeSequence ns = (NodeSequence) node.f0.choice;
			
			if (ns.elementAt(1) instanceof JavaStaticMethods) 
				return this.visit((JavaStaticMethods) ns.elementAt(1), scope, objects);
			
			if (ns.elementAt(1) instanceof VariableValue) 
				return this.visit((VariableValue) ns.elementAt(1), scope, objects);
			
			return this.visit((StatementExpression) node.f0.choice, scope, objects);
		}

		@Override
		public Object visit(GetFrom node, QslScope scope, Object... objects)
				throws Exception {
			String name = this.visit(node.f1, scope, objects).toString() ;
			
			QslScope s = new QslScope(scope);
	          if( s.existsChild( name ) )
	          {
	              QslFunc var = (QslFunc) scope.child( name ) ;
	              for (int i = 0; i < var.getCmd().length; i++) {
	            	  this.visit(var.getCmd()[i], s, objects);
	              }
	              return this.visit(var.getRet(), s, objects);
	          } else {
                  new VariableNotFoundException("The function " + name + " not exists!" ).send();
              }
	          return null;
		}

}
