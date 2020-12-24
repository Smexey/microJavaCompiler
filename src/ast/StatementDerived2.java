// generated with ast extension for cup
// version 0.8
// 24/11/2020 19:56:13


package ast;

public class StatementDerived2 extends Statement {

    private Condition Condition;
    private Statement Statement;
    private ElseStatementOptional ElseStatementOptional;

    public StatementDerived2 (Condition Condition, Statement Statement, ElseStatementOptional ElseStatementOptional) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseStatementOptional=ElseStatementOptional;
        if(ElseStatementOptional!=null) ElseStatementOptional.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ElseStatementOptional getElseStatementOptional() {
        return ElseStatementOptional;
    }

    public void setElseStatementOptional(ElseStatementOptional ElseStatementOptional) {
        this.ElseStatementOptional=ElseStatementOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseStatementOptional!=null) ElseStatementOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseStatementOptional!=null) ElseStatementOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseStatementOptional!=null) ElseStatementOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDerived2(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseStatementOptional!=null)
            buffer.append(ElseStatementOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDerived2]");
        return buffer.toString();
    }
}
