package compiler;

import ast.*;

public class CounterVisitor extends VisitorAdaptor {

    protected int cnt = 0;

    public int getCnt() {
        return cnt;
    }

    public static class FormParamCounter extends CounterVisitor {
        public void visit(FormParDeclNoErr fp) {
            cnt++;
        }
    }

    public static class VarDeclCounter extends CounterVisitor {
        public void visit(VarDeclNoErr fp) {
            cnt++;
        }

        public void visit(VarDeclIdentRepeatNoErr fp) {
            cnt++;
        }
    }
}
