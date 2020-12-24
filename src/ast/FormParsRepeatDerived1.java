// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class FormParsRepeatDerived1 extends FormParsRepeat {

    private FormParsRepeat FormParsRepeat;
    private FormParDecl FormParDecl;

    public FormParsRepeatDerived1 (FormParsRepeat FormParsRepeat, FormParDecl FormParDecl) {
        this.FormParsRepeat=FormParsRepeat;
        if(FormParsRepeat!=null) FormParsRepeat.setParent(this);
        this.FormParDecl=FormParDecl;
        if(FormParDecl!=null) FormParDecl.setParent(this);
    }

    public FormParsRepeat getFormParsRepeat() {
        return FormParsRepeat;
    }

    public void setFormParsRepeat(FormParsRepeat FormParsRepeat) {
        this.FormParsRepeat=FormParsRepeat;
    }

    public FormParDecl getFormParDecl() {
        return FormParDecl;
    }

    public void setFormParDecl(FormParDecl FormParDecl) {
        this.FormParDecl=FormParDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsRepeat!=null) FormParsRepeat.accept(visitor);
        if(FormParDecl!=null) FormParDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.traverseTopDown(visitor);
        if(FormParDecl!=null) FormParDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsRepeat!=null) FormParsRepeat.traverseBottomUp(visitor);
        if(FormParDecl!=null) FormParDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsRepeatDerived1(\n");

        if(FormParsRepeat!=null)
            buffer.append(FormParsRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParDecl!=null)
            buffer.append(FormParDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsRepeatDerived1]");
        return buffer.toString();
    }
}
