/* Generated by JTB 1.4.4 */
package qls.syntaxtree;

import qls.visitor.*;

public class GetFrom implements INode {

  public NodeToken f0;

  public VariableName f1;

  private static final long serialVersionUID = 144L;

  public GetFrom(final NodeToken n0, final VariableName n1) {
    f0 = n0;
    f1 = n1;
  }

  public GetFrom(final VariableName n0) {
    f0 = new NodeToken("getfrom");
    f1 = n0;
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
