// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class ConstDeclRepeatDerived1 extends ConstDeclRepeat {

    private ConstDeclRepeat ConstDeclRepeat;
    private String I2;
    private ConstValue ConstValue;

    public ConstDeclRepeatDerived1 (ConstDeclRepeat ConstDeclRepeat, String I2, ConstValue ConstValue) {
        this.ConstDeclRepeat=ConstDeclRepeat;
        if(ConstDeclRepeat!=null) ConstDeclRepeat.setParent(this);
        this.I2=I2;
        this.ConstValue=ConstValue;
        if(ConstValue!=null) ConstValue.setParent(this);
    }

    public ConstDeclRepeat getConstDeclRepeat() {
        return ConstDeclRepeat;
    }

    public void setConstDeclRepeat(ConstDeclRepeat ConstDeclRepeat) {
        this.ConstDeclRepeat=ConstDeclRepeat;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public ConstValue getConstValue() {
        return ConstValue;
    }

    public void setConstValue(ConstValue ConstValue) {
        this.ConstValue=ConstValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclRepeat!=null) ConstDeclRepeat.accept(visitor);
        if(ConstValue!=null) ConstValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.traverseTopDown(visitor);
        if(ConstValue!=null) ConstValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclRepeat!=null) ConstDeclRepeat.traverseBottomUp(visitor);
        if(ConstValue!=null) ConstValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclRepeatDerived1(\n");

        if(ConstDeclRepeat!=null)
            buffer.append(ConstDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(ConstValue!=null)
            buffer.append(ConstValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclRepeatDerived1]");
        return buffer.toString();
    }
}
