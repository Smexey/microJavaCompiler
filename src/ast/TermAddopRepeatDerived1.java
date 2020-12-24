// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class TermAddopRepeatDerived1 extends TermAddopRepeat {

    private TermAddopRepeat TermAddopRepeat;
    private Addop Addop;
    private Term Term;

    public TermAddopRepeatDerived1 (TermAddopRepeat TermAddopRepeat, Addop Addop, Term Term) {
        this.TermAddopRepeat=TermAddopRepeat;
        if(TermAddopRepeat!=null) TermAddopRepeat.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public TermAddopRepeat getTermAddopRepeat() {
        return TermAddopRepeat;
    }

    public void setTermAddopRepeat(TermAddopRepeat TermAddopRepeat) {
        this.TermAddopRepeat=TermAddopRepeat;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermAddopRepeat!=null) TermAddopRepeat.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermAddopRepeat!=null) TermAddopRepeat.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermAddopRepeat!=null) TermAddopRepeat.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermAddopRepeatDerived1(\n");

        if(TermAddopRepeat!=null)
            buffer.append(TermAddopRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermAddopRepeatDerived1]");
        return buffer.toString();
    }
}
