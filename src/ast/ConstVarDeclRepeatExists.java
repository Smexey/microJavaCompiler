// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class ConstVarDeclRepeatExists extends ConstVarDeclRepeat {

    private ConstVarDeclRepeat ConstVarDeclRepeat;
    private ConstVarDecl ConstVarDecl;

    public ConstVarDeclRepeatExists (ConstVarDeclRepeat ConstVarDeclRepeat, ConstVarDecl ConstVarDecl) {
        this.ConstVarDeclRepeat=ConstVarDeclRepeat;
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.setParent(this);
        this.ConstVarDecl=ConstVarDecl;
        if(ConstVarDecl!=null) ConstVarDecl.setParent(this);
    }

    public ConstVarDeclRepeat getConstVarDeclRepeat() {
        return ConstVarDeclRepeat;
    }

    public void setConstVarDeclRepeat(ConstVarDeclRepeat ConstVarDeclRepeat) {
        this.ConstVarDeclRepeat=ConstVarDeclRepeat;
    }

    public ConstVarDecl getConstVarDecl() {
        return ConstVarDecl;
    }

    public void setConstVarDecl(ConstVarDecl ConstVarDecl) {
        this.ConstVarDecl=ConstVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.accept(visitor);
        if(ConstVarDecl!=null) ConstVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.traverseTopDown(visitor);
        if(ConstVarDecl!=null) ConstVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.traverseBottomUp(visitor);
        if(ConstVarDecl!=null) ConstVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstVarDeclRepeatExists(\n");

        if(ConstVarDeclRepeat!=null)
            buffer.append(ConstVarDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstVarDecl!=null)
            buffer.append(ConstVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstVarDeclRepeatExists]");
        return buffer.toString();
    }
}
