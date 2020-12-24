// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class Program implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String programName;
    private ConstVarDeclRepeat ConstVarDeclRepeat;
    private MethodDeclRepeat MethodDeclRepeat;

    public Program (String programName, ConstVarDeclRepeat ConstVarDeclRepeat, MethodDeclRepeat MethodDeclRepeat) {
        this.programName=programName;
        this.ConstVarDeclRepeat=ConstVarDeclRepeat;
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.setParent(this);
        this.MethodDeclRepeat=MethodDeclRepeat;
        if(MethodDeclRepeat!=null) MethodDeclRepeat.setParent(this);
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName=programName;
    }

    public ConstVarDeclRepeat getConstVarDeclRepeat() {
        return ConstVarDeclRepeat;
    }

    public void setConstVarDeclRepeat(ConstVarDeclRepeat ConstVarDeclRepeat) {
        this.ConstVarDeclRepeat=ConstVarDeclRepeat;
    }

    public MethodDeclRepeat getMethodDeclRepeat() {
        return MethodDeclRepeat;
    }

    public void setMethodDeclRepeat(MethodDeclRepeat MethodDeclRepeat) {
        this.MethodDeclRepeat=MethodDeclRepeat;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.accept(visitor);
        if(MethodDeclRepeat!=null) MethodDeclRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.traverseTopDown(visitor);
        if(MethodDeclRepeat!=null) MethodDeclRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstVarDeclRepeat!=null) ConstVarDeclRepeat.traverseBottomUp(visitor);
        if(MethodDeclRepeat!=null) MethodDeclRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Program(\n");

        buffer.append(" "+tab+programName);
        buffer.append("\n");

        if(ConstVarDeclRepeat!=null)
            buffer.append(ConstVarDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclRepeat!=null)
            buffer.append(MethodDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Program]");
        return buffer.toString();
    }
}
