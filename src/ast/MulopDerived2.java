// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class MulopDerived2 extends Mulop {

    public MulopDerived2 () {
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
        buffer.append("MulopDerived2(\n");

        buffer.append(tab);
        buffer.append(") [MulopDerived2]");
        return buffer.toString();
    }
}
