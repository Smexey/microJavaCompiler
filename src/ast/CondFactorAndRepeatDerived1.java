// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class CondFactorAndRepeatDerived1 extends CondFactorAndRepeat {

    private CondFactorAndRepeat CondFactorAndRepeat;
    private CondFact CondFact;

    public CondFactorAndRepeatDerived1 (CondFactorAndRepeat CondFactorAndRepeat, CondFact CondFact) {
        this.CondFactorAndRepeat=CondFactorAndRepeat;
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
    }

    public CondFactorAndRepeat getCondFactorAndRepeat() {
        return CondFactorAndRepeat;
    }

    public void setCondFactorAndRepeat(CondFactorAndRepeat CondFactorAndRepeat) {
        this.CondFactorAndRepeat=CondFactorAndRepeat;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFactorAndRepeat!=null) CondFactorAndRepeat.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactorAndRepeatDerived1(\n");

        if(CondFactorAndRepeat!=null)
            buffer.append(CondFactorAndRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactorAndRepeatDerived1]");
        return buffer.toString();
    }
}
