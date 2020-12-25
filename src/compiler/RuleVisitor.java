package compiler;

import ast.MinusExists;
import ast.VisitorAdaptor;

public class RuleVisitor extends VisitorAdaptor {
    int minuscnt = 0;

    @Override
    public void visit(MinusExists minusExists) {
    }
}
