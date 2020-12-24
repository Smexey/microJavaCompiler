// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class ConditionDerived1 extends Condition {

    private CondTerm CondTerm;
    private CondTermOrRepeat CondTermOrRepeat;

    public ConditionDerived1 (CondTerm CondTerm, CondTermOrRepeat CondTermOrRepeat) {
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
        this.CondTermOrRepeat=CondTermOrRepeat;
        if(CondTermOrRepeat!=null) CondTermOrRepeat.setParent(this);
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public CondTermOrRepeat getCondTermOrRepeat() {
        return CondTermOrRepeat;
    }

    public void setCondTermOrRepeat(CondTermOrRepeat CondTermOrRepeat) {
        this.CondTermOrRepeat=CondTermOrRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTerm!=null) CondTerm.accept(visitor);
        if(CondTermOrRepeat!=null) CondTermOrRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
        if(CondTermOrRepeat!=null) CondTermOrRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        if(CondTermOrRepeat!=null) CondTermOrRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionDerived1(\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTermOrRepeat!=null)
            buffer.append(CondTermOrRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionDerived1]");
        return buffer.toString();
    }
}
