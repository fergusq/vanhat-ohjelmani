/* Generated by JTB 1.4.4 */
package qls.syntaxtree;

import qls.visitor.IRetArguVisitor;
import qls.visitor.IRetVisitor;
import qls.visitor.IVoidArguVisitor;
import qls.visitor.IVoidVisitor;

public interface INode extends java.io.Serializable {

  public <R, A> R accept(final IRetArguVisitor<R, A> vis, final A argu);

  public <R> R accept(final IRetVisitor<R> vis);

  public <A> void accept(final IVoidArguVisitor<A> vis, final A argu);

  public void accept(final IVoidVisitor vis);

}