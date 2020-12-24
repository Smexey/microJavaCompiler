// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class FormParsRepeatDerived2 extends FormParsRepeat {

    public FormParsRepeatDerived2 () {
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
        buffer.append("FormParsRepeatDerived2(\n");

        buffer.append(tab);
        buffer.append(") [FormParsRepeatDerived2]");
        return buffer.toString();
    }
}
