/* Generated by JTB 1.4.4 */
package qls.syntaxtree;

import qls.visitor.IRetArguVisitor;
import qls.visitor.IRetVisitor;
import qls.visitor.IVoidArguVisitor;
import qls.visitor.IVoidVisitor;

public interface INodeList extends INode {

  public void addNode(final INode n);

  public INode elementAt(int i);

  public java.util.Iterator<INode> elements();

  public int size();

  public <R, A> R accept(final IRetArguVisitor<R, A> vis, final A argu);

  public <R> R accept(final IRetVisitor<R> vis);

  public <A> void accept(final IVoidArguVisitor<A> vis, final A argu);

  public void accept(final IVoidVisitor vis);

}