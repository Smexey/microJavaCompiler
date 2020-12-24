// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class PrintWidthOptionalDerived1 extends PrintWidthOptional {

    private Integer N1;

    public PrintWidthOptionalDerived1 (Integer N1) {
        this.N1=N1;
    }

    public Integer getN1() {
        return N1;
    }

    public void setN1(Integer N1) {
        this.N1=N1;
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
        buffer.append("PrintWidthOptionalDerived1(\n");

        buffer.append(" "+tab+N1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintWidthOptionalDerived1]");
        return buffer.toString();
    }
}
