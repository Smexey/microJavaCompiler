// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class TermDerived1 extends Term {

    private Factor Factor;
    private FactorMulopRepeat FactorMulopRepeat;

    public TermDerived1 (Factor Factor, FactorMulopRepeat FactorMulopRepeat) {
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
        this.FactorMulopRepeat=FactorMulopRepeat;
        if(FactorMulopRepeat!=null) FactorMulopRepeat.setParent(this);
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public FactorMulopRepeat getFactorMulopRepeat() {
        return FactorMulopRepeat;
    }

    public void setFactorMulopRepeat(FactorMulopRepeat FactorMulopRepeat) {
        this.FactorMulopRepeat=FactorMulopRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Factor!=null) Factor.accept(visitor);
        if(FactorMulopRepeat!=null) FactorMulopRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
        if(FactorMulopRepeat!=null) FactorMulopRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        if(FactorMulopRepeat!=null) FactorMulopRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermDerived1(\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorMulopRepeat!=null)
            buffer.append(FactorMulopRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermDerived1]");
        return buffer.toString();
    }
}
