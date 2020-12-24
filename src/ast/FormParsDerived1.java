// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class FormParsDerived1 extends FormPars {

    private FormParDecl FormParDecl;
    private FormParsRepeat FormParsRepeat;

    public FormParsDerived1 (FormParDecl FormParDecl, FormParsRepeat FormParsRepeat) {
        this.FormParDecl=FormParDecl;
        if(FormParDecl!=null) FormParDecl.setParent(this);
        this.FormParsRepeat=FormParsRepeat;
        if(FormParsRepeat!=null) FormParsRepeat.setParent(this);
    }

    public FormParDecl getFormParDecl() {
        return FormParDecl;
    }

    public void setFormParDecl(FormParDecl FormParDecl) {
        this.FormParDecl=FormParDecl;
    }

    public FormParsRepeat getFormParsRepeat() {
        return FormParsRepeat;
    }

    public void setFormParsRepeat(FormParsRepeat FormParsRepeat) {
        this.FormParsRepeat=FormParsRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParDecl!=null) FormParDecl.accept(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParDecl!=null) FormParDecl.traverseTopDown(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParDecl!=null) FormParDecl.traverseBottomUp(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDerived1(\n");

        if(FormParDecl!=null)
            buffer.append(FormParDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsRepeat!=null)
            buffer.append(FormParsRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDerived1]");
        return buffer.toString();
    }
}
