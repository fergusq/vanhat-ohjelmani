/* Generated by JTB 1.4.4 */
package qls.visitor;

import qls.syntaxtree.*;

public interface IRetArguVisitor<R, A> {

  public R visit(final NodeList n, final A argu);

  public R visit(final NodeListOptional n, final A argu);

  public R visit(final NodeOptional n, final A argu);

  public R visit(final NodeSequence n, final A argu);

  public R visit(final NodeToken n, final A argu);

  public R visit(final Start n, final A argu);

  public R visit(final GlobalExpression n, final A argu);

  public R visit(final CreateNew n, final A argu);

  public R visit(final Use n, final A argu);

  public R visit(final Main n, final A argu);

  public R visit(final StatementExpression n, final A argu);

  public R visit(final FieldDeclaration n, final A argu);

  public R visit(final VariableDeclaration n, final A argu);

  public R visit(final VariableAssign n, final A argu);

  public R visit(final VariableValue n, final A argu);

  public R visit(final GetFrom n, final A argu);

  public R visit(final VariableName n, final A argu);

  public R visit(final Proceed n, final A argu);

  public R visit(final JavaStaticMethods n, final A argu);

  public R visit(final IfExpression n, final A argu);

  public R visit(final WhileExpression n, final A argu);

  public R visit(final CmdExpression n, final A argu);

  public R visit(final FuncExpression n, final A argu);

  public R visit(final ReturnStatement n, final A argu);

  public R visit(final MathExpression n, final A argu);

  public R visit(final AdditiveExpression n, final A argu);

  public R visit(final MultiplicativeExpression n, final A argu);

  public R visit(final UnaryExpression n, final A argu);

  public R visit(final RelationalExpression n, final A argu);

  public R visit(final RelationalEqualityExpression n, final A argu);

  public R visit(final RelationalGreaterExpression n, final A argu);

  public R visit(final RelationalLessExpression n, final A argu);

  public R visit(final UnaryRelational n, final A argu);

  public R visit(final Identifier n, final A argu);

  public R visit(final MyInteger n, final A argu);

}
