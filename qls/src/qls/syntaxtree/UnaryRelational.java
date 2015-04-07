/* Generated by JTB 1.4.4 */
package qls.syntaxtree;

import qls.visitor.*;

public class UnaryRelational implements INode {

  public NodeChoice f0;

  private static final long serialVersionUID = 144L;

  public UnaryRelational(final NodeChoice n0) {
    f0 = n0;
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
