// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class ConstDeclDerived1 extends ConstDecl {

    private Type Type;
    private String I2;
    private ConstValue ConstValue;
    private ConstDeclRepeat ConstDeclRepeat;

    public ConstDeclDerived1 (Type Type, String I2, ConstValue ConstValue, ConstDeclRepeat ConstDeclRepeat) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.ConstValue=ConstValue;
        if(ConstValue!=null) ConstValue.setParent(this);
        this.ConstDeclRepeat=ConstDeclRepeat;
        if(ConstDeclRepeat!=null) ConstDeclRepeat.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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

    public ConstDeclRepeat getConstDeclRepeat() {
        return ConstDeclRepeat;
    }

    public void setConstDeclRepeat(ConstDeclRepeat ConstDeclRepeat) {
        this.ConstDeclRepeat=ConstDeclRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstValue!=null) ConstValue.accept(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstValue!=null) ConstValue.traverseTopDown(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstValue!=null) ConstValue.traverseBottomUp(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclDerived1(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
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

        if(ConstDeclRepeat!=null)
            buffer.append(ConstDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclDerived1]");
        return buffer.toString();
    }
}
