// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class ActParsDerived1 extends ActPars {

    private Expr Expr;
    private ActParsExprRepeat ActParsExprRepeat;

    public ActParsDerived1 (Expr Expr, ActParsExprRepeat ActParsExprRepeat) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.ActParsExprRepeat=ActParsExprRepeat;
        if(ActParsExprRepeat!=null) ActParsExprRepeat.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public ActParsExprRepeat getActParsExprRepeat() {
        return ActParsExprRepeat;
    }

    public void setActParsExprRepeat(ActParsExprRepeat ActParsExprRepeat) {
        this.ActParsExprRepeat=ActParsExprRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(ActParsExprRepeat!=null) ActParsExprRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(ActParsExprRepeat!=null) ActParsExprRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(ActParsExprRepeat!=null) ActParsExprRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsDerived1(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsExprRepeat!=null)
            buffer.append(ActParsExprRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsDerived1]");
        return buffer.toString();
    }
}
