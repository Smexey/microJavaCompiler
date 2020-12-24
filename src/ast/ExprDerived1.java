// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class ExprDerived1 extends Expr {

    private MinusOptional MinusOptional;
    private Term Term;
    private TermAddopRepeat TermAddopRepeat;

    public ExprDerived1 (MinusOptional MinusOptional, Term Term, TermAddopRepeat TermAddopRepeat) {
        this.MinusOptional=MinusOptional;
        if(MinusOptional!=null) MinusOptional.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.TermAddopRepeat=TermAddopRepeat;
        if(TermAddopRepeat!=null) TermAddopRepeat.setParent(this);
    }

    public MinusOptional getMinusOptional() {
        return MinusOptional;
    }

    public void setMinusOptional(MinusOptional MinusOptional) {
        this.MinusOptional=MinusOptional;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public TermAddopRepeat getTermAddopRepeat() {
        return TermAddopRepeat;
    }

    public void setTermAddopRepeat(TermAddopRepeat TermAddopRepeat) {
        this.TermAddopRepeat=TermAddopRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MinusOptional!=null) MinusOptional.accept(visitor);
        if(Term!=null) Term.accept(visitor);
        if(TermAddopRepeat!=null) TermAddopRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MinusOptional!=null) MinusOptional.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(TermAddopRepeat!=null) TermAddopRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MinusOptional!=null) MinusOptional.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(TermAddopRepeat!=null) TermAddopRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDerived1(\n");

        if(MinusOptional!=null)
            buffer.append(MinusOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TermAddopRepeat!=null)
            buffer.append(TermAddopRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDerived1]");
        return buffer.toString();
    }
}
