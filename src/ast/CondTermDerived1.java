// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class CondTermDerived1 extends CondTerm {

    private CondFact CondFact;
    private CondFactorAndRepeat CondFactorAndRepeat;

    public CondTermDerived1 (CondFact CondFact, CondFactorAndRepeat CondFactorAndRepeat) {
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
        this.CondFactorAndRepeat=CondFactorAndRepeat;
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.setParent(this);
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public CondFactorAndRepeat getCondFactorAndRepeat() {
        return CondFactorAndRepeat;
    }

    public void setCondFactorAndRepeat(CondFactorAndRepeat CondFactorAndRepeat) {
        this.CondFactorAndRepeat=CondFactorAndRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondFact!=null) CondFact.accept(visitor);
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTermDerived1(\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFactorAndRepeat!=null)
            buffer.append(CondFactorAndRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTermDerived1]");
        return buffer.toString();
    }
}
