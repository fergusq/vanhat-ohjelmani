/* Generated by JTB 1.4.4 */
package qls.visitor;

import qls.syntaxtree.*;
import java.util.*;

public class DepthFirstRetVisitor<R> implements IRetVisitor<R> {


  public R visit(final NodeChoice n) {
    final R nRes = n.choice.accept(this);
    return nRes;
  }

  public R visit(final NodeList n) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      final R sRes = e.next().accept(this);
    }
    return nRes;
  }

  public R visit(final NodeListOptional n) {
    if (n.present()) {
      R nRes = null;
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        @SuppressWarnings("unused")
        R sRes = e.next().accept(this);
        }
      return nRes;
    } else
      return null;
  }

  public R visit(final NodeOptional n) {
    if (n.present()) {
      final R nRes = n.node.accept(this);
      return nRes;
    } else
    return null;
  }

  public R visit(final NodeSequence n) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      R subRet = e.next().accept(this);
    }
    return nRes;
  }

  public R visit(final NodeToken n) {
    R nRes = null;
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return nRes;
  }

  public R visit(final Start n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    return nRes;
  }

  public R visit(final GlobalExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final CreateNew n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final Use n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final Main n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final StatementExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final FieldDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final VariableDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final VariableAssign n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final VariableValue n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final GetFrom n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final VariableName n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final Proceed n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final JavaStaticMethods n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    n.f7.accept(this);
    return nRes;
  }

  public R visit(final IfExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final WhileExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final CmdExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    return nRes;
  }

  public R visit(final FuncExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    return nRes;
  }

  public R visit(final ReturnStatement n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final MathExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final AdditiveExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final MultiplicativeExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final UnaryExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final RelationalExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final RelationalEqualityExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final RelationalGreaterExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final RelationalLessExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final UnaryRelational n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final Identifier n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final MyInteger n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

}