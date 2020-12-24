// generated with ast extension for cup
// version 0.8
// 24/11/2020 18:22:28


package ast;

public class FactorMulopRepeatDerived1 extends FactorMulopRepeat {

    private FactorMulopRepeat FactorMulopRepeat;
    private Mulop Mulop;
    private Factor Factor;

    public FactorMulopRepeatDerived1 (FactorMulopRepeat FactorMulopRepeat, Mulop Mulop, Factor Factor) {
        this.FactorMulopRepeat=FactorMulopRepeat;
        if(FactorMulopRepeat!=null) FactorMulopRepeat.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public FactorMulopRepeat getFactorMulopRepeat() {
        return FactorMulopRepeat;
    }

    public void setFactorMulopRepeat(FactorMulopRepeat FactorMulopRepeat) {
        this.FactorMulopRepeat=FactorMulopRepeat;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorMulopRepeat!=null) FactorMulopRepeat.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorMulopRepeat!=null) FactorMulopRepeat.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorMulopRepeat!=null) FactorMulopRepeat.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorMulopRepeatDerived1(\n");

        if(FactorMulopRepeat!=null)
            buffer.append(FactorMulopRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorMulopRepeatDerived1]");
        return buffer.toString();
    }
}
