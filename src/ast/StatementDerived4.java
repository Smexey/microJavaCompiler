// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class StatementDerived4 extends Statement {

    private Expr Expr;
    private CaseRepeat CaseRepeat;

    public StatementDerived4 (Expr Expr, CaseRepeat CaseRepeat) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.CaseRepeat=CaseRepeat;
        if(CaseRepeat!=null) CaseRepeat.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public CaseRepeat getCaseRepeat() {
        return CaseRepeat;
    }

    public void setCaseRepeat(CaseRepeat CaseRepeat) {
        this.CaseRepeat=CaseRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(CaseRepeat!=null) CaseRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(CaseRepeat!=null) CaseRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(CaseRepeat!=null) CaseRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDerived4(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseRepeat!=null)
            buffer.append(CaseRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDerived4]");
        return buffer.toString();
    }
}
