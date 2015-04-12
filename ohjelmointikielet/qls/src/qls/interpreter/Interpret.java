package qls.interpreter;

import qls.core.QslScope;
import qls.syntaxtree.AdditiveExpression;
import qls.syntaxtree.CmdExpression;
import qls.syntaxtree.CreateNew;
import qls.syntaxtree.FieldDeclaration;
import qls.syntaxtree.FuncExpression;
import qls.syntaxtree.GetFrom;
import qls.syntaxtree.IfExpression;
import qls.syntaxtree.JavaStaticMethods;
import qls.syntaxtree.Main;
import qls.syntaxtree.MathExpression;
import qls.syntaxtree.MultiplicativeExpression;
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

public interface Interpret {
	/**
	           * f -> ( Require() "." )+
	           * f -> ( StatementExpression() )*
	        * @throws Exception 
	           */
	          public Object visit(Start node) throws Exception;
	   
	          /**
	           * f -> "require"
	           * f -> ( <IDENTIFIER> )+
	           */
	          public Object visit(Use node, QslScope scope, Object ... objects);
	   
	          /**
	           * f -> AdditiveExpression()
	        * @throws Exception 
	           */
	          public Object visit(MathExpression node, QslScope scope, Object ... objects) throws Exception;
	   
	          /**
	           * f -> MultiplicativeExpression()
	           * f -> ( ( "+" | "-" ) MultiplicativeExpression() )*
	        * @throws Exception 
	           */
	          public Object visit(AdditiveExpression node, QslScope scope, Object ... objects) throws Exception;
	   
	          /**
	           * f -> UnaryExpression()
	           * f -> ( ( "*" | "/" | "%" ) UnaryExpression() )*
	        * @throws Exception 
	           */
	          public Object visit(MultiplicativeExpression node, QslScope scope, Object ... objects) throws Exception;
	   
	          /**
	           * f -> "(" MathExpression() ")"
	           *       | <INTEGER_LITERAL>
	           *       | VariableName()
	        * @throws Exception 
	           */
	          public Object visit(UnaryExpression node, QslScope scope, Object ... objects) throws Exception;
	   
	          /**
	           * f -> RelationalEqualityExpression()
	        * @throws Exception 
	           */
	          public Object visit(RelationalExpression node, QslScope scope, Object ... objects) throws Exception;
	   
	          /**
	           * f -> RelationalGreaterExpression()
	           * f -> ( ( "==" | "!=" ) RelationalGreaterExpression() )*
	        * @throws Exception 
	           */
	          public Object visit(RelationalEqualityExpression node, QslScope scope, Object ... objects) throws Exception;
	   
	          /**
	           * f -> RelationalLessExpression()
	           * f -> ( ( ">" | ">=" ) RelationalLessExpression() )*
	        * @throws Exception 
	          */
	         public Object visit(RelationalGreaterExpression node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> UnaryRelational()
	          * f -> ( ( "<" | "<=" ) UnaryRelational() )*
	       * @throws Exception 
	          */
	         public Object visit(RelationalLessExpression node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> MathExpression()
	       * @throws Exception 
	          */
	         public Object visit(UnaryRelational node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> "if"
	          * f -> RelationalExprssion()
	          * f -> "do"
	          * f -> ( StatementExpression() )*
	          * f -> "stop"
	       * @throws Exception 
	          */
	         public Object visit(IfExpression node, QslScope scope, Object ... objects) throws Exception;
	         
	         /**
	          * f -> "if"
	          * f -> RelationalExprssion()
	          * f -> "do"
	          * f -> ( StatementExpression() )*
	          * f -> "stop"
	       * @throws Exception 
	          */
	         public Object visit(CmdExpression node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> "while"
	          * f -> RelationalExprssion()
	          * f -> "do"
	          * f -> ( StatementExpression() )*
	          * f -> "stop"
	       * @throws Exception 
	          */
	         public Object visit(WhileExpression node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> "def"
	          * f -> VariableName()
	          * f -> "="
	          * f -> MathExpression()
	          * f -> "."
	       * @throws Exception 
	          */
	         public Object visit(VariableDeclaration node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> VariableName()
	          * f -> "="
	          * f -> MathExpression()
	          * f -> "."
	       * @throws Exception 
	          */
	         public Object visit(VariableAssign node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> <IDENTIFIER>
	          */
	         public Object visit(VariableName node, QslScope scope, Object ... objects);
	  
	         /**
	          * f -> <IDENTIFIER>
	          * f -> ( ":" <IDENTIFIER> )+
	          * f -> "("
	          * f -> MathExpression()
	          * f -> ( "," MathExpression() )*
	          * f -> ")"
	          * f -> "."
	       * @throws Exception 
	          */
	         public Object visit(JavaStaticMethods node, QslScope scope, Object ... objects) throws Exception;
	  
	         /**
	          * f -> VariableDeclaration()
	          *       | VariableAssign()
	          *       | JavaStaticMethods()
	          *       | IfExpression()
	          *       | WhileExpression()
	       * @throws Exception 
	          */
	         public Object visit(StatementExpression node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(Proceed node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(Main node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(VariableValue node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(FieldDeclaration node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(CreateNew node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(ReturnStatement node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(FuncExpression node, QslScope scope, Object ... objects) throws Exception;
	         
	         public Object visit(GetFrom node, QslScope scope, Object ... objects) throws Exception;

}
