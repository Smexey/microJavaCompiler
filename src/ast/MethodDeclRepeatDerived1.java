// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class MethodDeclRepeatDerived1 extends MethodDeclRepeat {

    private MethodDeclRepeat MethodDeclRepeat;
    private MethodDecl MethodDecl;

    public MethodDeclRepeatDerived1 (MethodDeclRepeat MethodDeclRepeat, MethodDecl MethodDecl) {
        this.MethodDeclRepeat=MethodDeclRepeat;
        if(MethodDeclRepeat!=null) MethodDeclRepeat.setParent(this);
        this.MethodDecl=MethodDecl;
        if(MethodDecl!=null) MethodDecl.setParent(this);
    }

    public MethodDeclRepeat getMethodDeclRepeat() {
        return MethodDeclRepeat;
    }

    public void setMethodDeclRepeat(MethodDeclRepeat MethodDeclRepeat) {
        this.MethodDeclRepeat=MethodDeclRepeat;
    }

    public MethodDecl getMethodDecl() {
        return MethodDecl;
    }

    public void setMethodDecl(MethodDecl MethodDecl) {
        this.MethodDecl=MethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclRepeat!=null) MethodDeclRepeat.accept(visitor);
        if(MethodDecl!=null) MethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclRepeat!=null) MethodDeclRepeat.traverseTopDown(visitor);
        if(MethodDecl!=null) MethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclRepeat!=null) MethodDeclRepeat.traverseBottomUp(visitor);
        if(MethodDecl!=null) MethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclRepeatDerived1(\n");

        if(MethodDeclRepeat!=null)
            buffer.append(MethodDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDecl!=null)
            buffer.append(MethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclRepeatDerived1]");
        return buffer.toString();
    }
}
