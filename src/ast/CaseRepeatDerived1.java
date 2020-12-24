// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class CaseRepeatDerived1 extends CaseRepeat {

    private CaseRepeat CaseRepeat;
    private Integer N2;
    private StatementRepeat StatementRepeat;

    public CaseRepeatDerived1 (CaseRepeat CaseRepeat, Integer N2, StatementRepeat StatementRepeat) {
        this.CaseRepeat=CaseRepeat;
        if(CaseRepeat!=null) CaseRepeat.setParent(this);
        this.N2=N2;
        this.StatementRepeat=StatementRepeat;
        if(StatementRepeat!=null) StatementRepeat.setParent(this);
    }

    public CaseRepeat getCaseRepeat() {
        return CaseRepeat;
    }

    public void setCaseRepeat(CaseRepeat CaseRepeat) {
        this.CaseRepeat=CaseRepeat;
    }

    public Integer getN2() {
        return N2;
    }

    public void setN2(Integer N2) {
        this.N2=N2;
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
        if(CaseRepeat!=null) CaseRepeat.accept(visitor);
        if(StatementRepeat!=null) StatementRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CaseRepeat!=null) CaseRepeat.traverseTopDown(visitor);
        if(StatementRepeat!=null) StatementRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CaseRepeat!=null) CaseRepeat.traverseBottomUp(visitor);
        if(StatementRepeat!=null) StatementRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CaseRepeatDerived1(\n");

        if(CaseRepeat!=null)
            buffer.append(CaseRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+N2);
        buffer.append("\n");

        if(StatementRepeat!=null)
            buffer.append(StatementRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CaseRepeatDerived1]");
        return buffer.toString();
    }
}
