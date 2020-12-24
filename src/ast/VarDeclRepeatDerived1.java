// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class VarDeclRepeatDerived1 extends VarDeclRepeat {

    private VarDeclRepeat VarDeclRepeat;
    private String I2;
    private OptArray OptArray;

    public VarDeclRepeatDerived1 (VarDeclRepeat VarDeclRepeat, String I2, OptArray OptArray) {
        this.VarDeclRepeat=VarDeclRepeat;
        if(VarDeclRepeat!=null) VarDeclRepeat.setParent(this);
        this.I2=I2;
        this.OptArray=OptArray;
        if(OptArray!=null) OptArray.setParent(this);
    }

    public VarDeclRepeat getVarDeclRepeat() {
        return VarDeclRepeat;
    }

    public void setVarDeclRepeat(VarDeclRepeat VarDeclRepeat) {
        this.VarDeclRepeat=VarDeclRepeat;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public OptArray getOptArray() {
        return OptArray;
    }

    public void setOptArray(OptArray OptArray) {
        this.OptArray=OptArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclRepeat!=null) VarDeclRepeat.accept(visitor);
        if(OptArray!=null) OptArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclRepeat!=null) VarDeclRepeat.traverseTopDown(visitor);
        if(OptArray!=null) OptArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclRepeat!=null) VarDeclRepeat.traverseBottomUp(visitor);
        if(OptArray!=null) OptArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclRepeatDerived1(\n");

        if(VarDeclRepeat!=null)
            buffer.append(VarDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptArray!=null)
            buffer.append(OptArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclRepeatDerived1]");
        return buffer.toString();
    }
}
