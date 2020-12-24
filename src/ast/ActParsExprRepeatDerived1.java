// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class ActParsExprRepeatDerived1 extends ActParsExprRepeat {

    private ActParsExprRepeat ActParsExprRepeat;
    private Expr Expr;

    public ActParsExprRepeatDerived1 (ActParsExprRepeat ActParsExprRepeat, Expr Expr) {
        this.ActParsExprRepeat=ActParsExprRepeat;
        if(ActParsExprRepeat!=null) ActParsExprRepeat.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ActParsExprRepeat getActParsExprRepeat() {
        return ActParsExprRepeat;
    }

    public void setActParsExprRepeat(ActParsExprRepeat ActParsExprRepeat) {
        this.ActParsExprRepeat=ActParsExprRepeat;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParsExprRepeat!=null) ActParsExprRepeat.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsExprRepeat!=null) ActParsExprRepeat.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsExprRepeat!=null) ActParsExprRepeat.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsExprRepeatDerived1(\n");

        if(ActParsExprRepeat!=null)
            buffer.append(ActParsExprRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsExprRepeatDerived1]");
        return buffer.toString();
    }
}
