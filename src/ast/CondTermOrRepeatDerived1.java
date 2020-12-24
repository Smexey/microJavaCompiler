// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class CondTermOrRepeatDerived1 extends CondTermOrRepeat {

    private CondTermOrRepeat CondTermOrRepeat;
    private CondTerm CondTerm;

    public CondTermOrRepeatDerived1 (CondTermOrRepeat CondTermOrRepeat, CondTerm CondTerm) {
        this.CondTermOrRepeat=CondTermOrRepeat;
        if(CondTermOrRepeat!=null) CondTermOrRepeat.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public CondTermOrRepeat getCondTermOrRepeat() {
        return CondTermOrRepeat;
    }

    public void setCondTermOrRepeat(CondTermOrRepeat CondTermOrRepeat) {
        this.CondTermOrRepeat=CondTermOrRepeat;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTermOrRepeat!=null) CondTermOrRepeat.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTermOrRepeat!=null) CondTermOrRepeat.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTermOrRepeat!=null) CondTermOrRepeat.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTermOrRepeatDerived1(\n");

        if(CondTermOrRepeat!=null)
            buffer.append(CondTermOrRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTermOrRepeatDerived1]");
        return buffer.toString();
    }
}
