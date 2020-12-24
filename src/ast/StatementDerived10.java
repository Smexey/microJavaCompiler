// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class StatementDerived10 extends Statement {

    private StatementRepeat StatementRepeat;

    public StatementDerived10 (StatementRepeat StatementRepeat) {
        this.StatementRepeat=StatementRepeat;
        if(StatementRepeat!=null) StatementRepeat.setParent(this);
    }

    public StatementRepeat getStatementRepeat() {
        return StatementRepeat;
    }

    public void setStatementRepeat(StatementRepeat StatementRepeat) {
        this.StatementRepeat=StatementRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementRepeat!=null) StatementRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementRepeat!=null) StatementRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementRepeat!=null) StatementRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDerived10(\n");

        if(StatementRepeat!=null)
            buffer.append(StatementRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDerived10]");
        return buffer.toString();
    }
}
