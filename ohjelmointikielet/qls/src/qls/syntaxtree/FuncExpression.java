/* Generated by JTB 1.4.4 */
package qls.syntaxtree;

import qls.visitor.*;

public class FuncExpression implements INode {

  public NodeToken f0;

  public VariableName f1;

  public NodeListOptional f2;

  public ReturnStatement f3;

  public NodeToken f4;

  public NodeToken f5;

  private static final long serialVersionUID = 144L;

  public FuncExpression(final NodeToken n0, final VariableName n1, final NodeListOptional n2, final ReturnStatement n3, final NodeToken n4, final NodeToken n5) {
    f0 = n0;
    f1 = n1;
    f2 = n2;
    f3 = n3;
    f4 = n4;
    f5 = n5;
  }

  public FuncExpression(final VariableName n0, final NodeListOptional n1, final ReturnStatement n2) {
    f0 = new NodeToken("function");
    f1 = n0;
    f2 = n1;
    f3 = n2;
    f4 = new NodeToken(".");
    f5 = new NodeToken("end");
  }

  public <R, A> R accept(final IRetArguVisitor<R, A> vis, final A argu) {
    return vis.visit(this, argu);
  }

  public <R> R accept(final IRetVisitor<R> vis) {
    return vis.visit(this);
  }

  public <A> void accept(final IVoidArguVisitor<A> vis, final A argu) {
    vis.visit(this, argu);
  }

  public void accept(final IVoidVisitor vis) {
    vis.visit(this);
  }

}
