// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class ExprOptionalDerived2 extends ExprOptional {

    public ExprOptionalDerived2 () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprOptionalDerived2(\n");

        buffer.append(tab);
        buffer.append(") [ExprOptionalDerived2]");
        return buffer.toString();
    }
}
