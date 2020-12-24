// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class StatementDerived9 extends Statement {

    private Expr Expr;
    private PrintWidthOptional PrintWidthOptional;

    public StatementDerived9 (Expr Expr, PrintWidthOptional PrintWidthOptional) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintWidthOptional=PrintWidthOptional;
        if(PrintWidthOptional!=null) PrintWidthOptional.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintWidthOptional getPrintWidthOptional() {
        return PrintWidthOptional;
    }

    public void setPrintWidthOptional(PrintWidthOptional PrintWidthOptional) {
        this.PrintWidthOptional=PrintWidthOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintWidthOptional!=null) PrintWidthOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintWidthOptional!=null) PrintWidthOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintWidthOptional!=null) PrintWidthOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDerived9(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintWidthOptional!=null)
            buffer.append(PrintWidthOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDerived9]");
        return buffer.toString();
    }
}
