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

    public static class GlobalVarConstCounter extends CounterVisitor {
        // TRAVERSE BOTTOM UP
        int inClass = 0;

        public void visit(ClassName t) {
            inClass = 1;
        }

        public void visit(ClassDecl t) {
            inClass = 0;
        }

        public void visit(VarDeclNoErr fp) {
            cnt += 1 - inClass;
        }

        public void visit(VarDeclIdentRepeatNoErr fp) {
            cnt += 1 - inClass;
        }

        public void visit(ConstDecl fp) {
            cnt += 1 - inClass;
        }

        public void visit(ConstDeclRepeatExists fp) {
            cnt += 1 - inClass;
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
