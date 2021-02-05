package compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.*;
import compiler.CounterVisitor.FormParamCounter;
import compiler.CounterVisitor.VarDeclCounter;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;

    // moraju se porediti preko kind a ne referencom
    Struct boolType = new Struct(Struct.Bool);
    Struct arrType = new Struct(Struct.Array);

    public int getMainPc() {
        return this.mainPc;
    }

    int level = 0;

    public void visit(PrintStatement print) {
        // expr vec na steku
        if (print.getExpr().struct == Tab.intType) {
            Code.loadConst(5);// sirina
            Code.put(Code.print);
        } else {
            Code.loadConst(1);
            Code.put(Code.bprint);
        }
    }

    public void visit(ConstFactor cf) {
        // ConstValueNum, ConstValueChar, ConstValueBool
        Obj o = new Obj(Obj.Con, "$", cf.getConstValue().obj.getType(), 0, 0);
        o.setAdr(cf.getConstValue().obj.getAdr());
        Code.load(o);
    }

    public void visit(MethodTypeAndName t) {
        if ("main".equalsIgnoreCase(t.getName())) {
            this.mainPc = Code.pc; // na kraju setujem nazad ovaj pc
            // Code.mainPc = Code.pc; zasto ovo nisam uradio pardoniram
        }
        t.obj.setAdr(Code.pc);
        SyntaxNode parent = t.getParent();

        VarDeclCounter varCnter = new VarDeclCounter();
        parent.traverseTopDown(varCnter);

        FormParamCounter paramCnter = new FormParamCounter();
        parent.traverseTopDown(paramCnter);

        Code.put(Code.enter);
        Code.put(paramCnter.getCnt());
        Code.put(paramCnter.getCnt() + varCnter.getCnt());
    }

    public void visit(MethodDecl t) {
        // impliocitni return
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(FunctCall t) {
        Obj o = t.getFunctDesignator().getDesignator().obj;
        int offset = o.getAdr() - Code.pc;

        Code.put(Code.call);
        Code.put2(offset);
    }

    public void visit(DesignatorStatementFunctCall t) {
        if (t.getFunctCall().getFunctDesignator().getDesignator().obj.getType() != Tab.noType) {
            // nije void a ne treba nam rezultat
            Code.put(Code.pop);
        }
    }

    public void visit(ReturnExprStatement t) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(ReturnNoExprStatement t) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(TermAddopRepeatExists t) {
        if (t.getAddop() instanceof OperatorAdd) {
            Code.put(Code.add);
        } else if (t.getAddop() instanceof OperatorSub) {
            Code.put(Code.sub);
        }
    }

    public void visit(Term t) {
        if (t.getParent() instanceof SmolExpr) {
            SmolExpr e = (SmolExpr) t.getParent();
            if (e.getMinusOptional() instanceof MinusExists) {
                Code.put(Code.neg);
            }
        }
    }

    public void visit(DesignatorStatementIncrement t) {
        // ako je arr imacu vrednost indexiranog na stacku
        Code.put(Code.const_1);
        Code.put(Code.add);
        Code.store(t.getDesignator().obj);
    }

    public void visit(DesignatorStatementDecrement t) {
        // ako je arr imacu vrednost indexiranog na stacku
        Code.put(Code.const_1);
        Code.put(Code.sub);
        Code.store(t.getDesignator().obj);
    }

    public void visit(FactorMulopRepeatExists t) {
        if (t.getMulop() instanceof OperatorMul) {
            Code.put(Code.mul);
        } else if (t.getMulop() instanceof OperatorDiv) {
            Code.put(Code.div);
        } else if (t.getMulop() instanceof OperatorMod) {
            Code.put(Code.rem);
        }
    }

    // CONDITION
    int adr = -1;

    Stack<List<Integer>> elseLists = new Stack<>();
    Stack<List<Integer>> thenLists = new Stack<>();
    Stack<Integer> endIfs = new Stack<>();

    public static int CONTROLDOSTATE = 0, CONTROLIFSTATE = 1;
    Stack<Integer> controlStates = new Stack<>();
    Stack<Integer> startDos = new Stack<>();

    public void visit(StartIf t) {
        controlStates.push(CONTROLIFSTATE);
        elseLists.push(new ArrayList<>());
        thenLists.push(new ArrayList<>());
    }

    Stack<List<Integer>> breakLists = new Stack<>();
    Stack<List<Integer>> continueLists = new Stack<>();

    public void visit(StartDo t) {
        controlStates.push(CONTROLDOSTATE);

        breakLists.push(new ArrayList<>());
        continueLists.push(new ArrayList<>());
        startDos.push(Code.pc);
    }

    public void visit(StartSwitch t) {
        breakLists.push(new ArrayList<>());
        lastCases.push(-1);
    }

    Stack<Integer> lastCases = new Stack<>();

    public void visit(CaseStart t) {
        if (lastCases.peek() != -1)
            Code.fixup(lastCases.pop());
        else {
            lastCases.pop();
        }
        Code.put(Code.dup);
    }

    public void visit(CaseEnd t) {
        // Code.fixup(lastCase);
        Code.putFalseJump(Code.eq, 0);// eq jer se obrce
        lastCases.push(Code.pc - 2);
        Code.put(Code.pop);

        if (prevCaseEnd != -1)
            Code.fixup(prevCaseEnd);
    }

    public void visit(CaseNum cf) {
        // ConstValueNum, ConstValueChar, ConstValueBool
        Obj o = new Obj(Obj.Con, "$", Tab.intType, 0, 0);
        o.setAdr(cf.getNum());
        Code.load(o);
    }

    int prevCaseEnd = -1;

    public void visit(CaseRepeatExists t) {
        Code.putJump(Code.pc + 3);
        prevCaseEnd = Code.pc - 2;
    }

    public void fixup(List<Integer> adrL) {
        for (Integer adr : adrL) {
            Code.fixup(adr);
        }
        adrL.clear();
    }

    public void visit(ThenStatement t) {

        IfStatement parent = (IfStatement) t.getParent();
        if (parent.getElseStatementOptional() instanceof ElseStatementExists) {
            Code.putJump(0);
            endIfs.push(Code.pc - 2);
        }

    }

    public void visit(ElseStart t) {
        fixup(elseLists.peek());
        elseLists.pop();
    }

    public void visit(ElseStatementEmpty t) {
        fixup(elseLists.peek());
        elseLists.pop();
    }

    public void visit(ElseStatementExists t) {
        Code.fixup(endIfs.pop());
    }

    public void visit(OrStart t) {
        Code.putJump(0);
        fixup(elseLists.peek());
        thenLists.peek().add(Code.pc - 2);
    }

    public void visit(ConditionNoErr t) {
        if (controlStates.peek() == CONTROLIFSTATE) {
            fixup(thenLists.peek());
            thenLists.pop();
        } else {
            Code.putJump(startDos.pop());
            fixup(breakLists.peek());
            breakLists.pop();

        }
        controlStates.pop();
    }

    public void visit(SwitchStatement t) {
        fixup(breakLists.peek());
        breakLists.pop();

        if (lastCases.peek() != -1)
            Code.fixup(lastCases.peek());
        lastCases.pop();
    }

    public void visit(StartWhile t) {
        fixup(continueLists.peek());
        continueLists.pop();
    }

    public void visit(BreakStatement t) {
        Code.putJump(0);
        breakLists.peek().add(Code.pc - 2);
    }

    public void visit(ContinueStatement t) {
        Code.putJump(0);
        continueLists.peek().add(Code.pc - 2);
    }

    public void visit(CondFact t) {
        if (t.getRelopExprOptional() instanceof RelopExprExists) {
            RelopExprExists relopExpr = (RelopExprExists) t.getRelopExprOptional();
            if (relopExpr.getRelop() instanceof CmpEq) {
                Code.putFalseJump(Code.eq, 0);
            } else if (relopExpr.getRelop() instanceof CmpNeq) {
                Code.putFalseJump(Code.ne, 0);
            } else if (relopExpr.getRelop() instanceof CmpGrt) {
                Code.putFalseJump(Code.gt, 0);
            } else if (relopExpr.getRelop() instanceof CmpGrtEq) {
                Code.putFalseJump(Code.ge, 0);
            } else if (relopExpr.getRelop() instanceof CmpLess) {
                Code.putFalseJump(Code.lt, 0);
            } else if (relopExpr.getRelop() instanceof CmpLessEq) {
                Code.putFalseJump(Code.le, 0);
            }
            if (controlStates.peek() == CONTROLIFSTATE) {
                elseLists.peek().add(Code.pc - 2);
            } else if (controlStates.peek() == CONTROLDOSTATE) {
                breakLists.peek().add(Code.pc - 2);
            }
        } else {
            // ima samo expr unutar ifa
            // cmp sa 0?
            Code.put(Code.const_n + 0);
            Code.putFalseJump(Code.ne, 0);

            if (controlStates.peek() == CONTROLIFSTATE) {
                elseLists.peek().add(Code.pc - 2);
            } else if (controlStates.peek() == CONTROLDOSTATE) {
                breakLists.peek().add(Code.pc - 2);
            }

        }
    }

    // DESIGNATOR
    public void visit(AssignmentNoErr t) {
        Code.store(t.getDesignator().obj);
    }

    public void visit(NewOperatorFactor t) {
        if (t.getArrayBracketsOptional() instanceof ArrayBracketsExists) {

            Code.put(Code.newarray);

            int kind = t.getType().struct.getKind();
            if (kind == Struct.Char || kind == Struct.Bool) {
                Code.put(0);
            } else {
                Code.put(1);
            }
        } else {
            // klasni tip
            Code.put(Code.new_);
            Code.put2(t.getType().struct.getNumberOfFields() * 4);
        }
    }

    public void visit(DesignatorIdent t) {
        if (t.getParent() instanceof Designator) {
            Designator des = (Designator) t.getParent();
            // ako je rvalue ubacuje se
            if (!(des.getParent() instanceof Assignment)) {
                // da se ne loaduje ime funkcije
                if (!(des.getParent() instanceof FunctDesignator))
                    Code.load(t.obj);
            } else {
                // jeste assign ali jos nije apsolutna adresa izracunata
                if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists
                        || des.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                    Code.load(t.obj);
                }
            }
        } else {
            // instanceof SubDesignatorRepeat
            SubDesignatorRepeatExists subdes = (SubDesignatorRepeatExists) t.getParent();
            // stavljam svom parentu arrload jer parent nije poslednji

            if (subdes.getParent() instanceof SubDesignatorRepeatExists) {
                SubDesignatorRepeatExists myParent = (SubDesignatorRepeatExists) subdes.getParent();
                if (myParent.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                    Code.put(Code.aload);
                }
            }

            if (subdes.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                // ima neko posle mene -> svakako je load
                Code.load(t.obj);
            } else {
                // nema niko posle mene -> da li sam u assignmentu?
                SyntaxNode x = t.getParent();
                while (!(x instanceof Designator))
                    x = x.getParent();

                if (!(x.getParent() instanceof Assignment)) {
                    Code.load(t.obj);
                }

                if ((x.getParent() instanceof Assignment)
                        && subdes.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                    Code.load(t.obj);
                }
            }
        }
    }

    public void visit(Designator t) {
        // najdubljem trebam da vidim jel array i nije u assignmentu

        SubDesignatorRepeatExists subdes = null;
        if (t.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
            subdes = (SubDesignatorRepeatExists) t.getSubDesignatorRepeat();
            while (subdes.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                subdes = (SubDesignatorRepeatExists) subdes.getSubDesignatorRepeat();
            }
        }

        if (subdes != null) {
            // ako ima neki subdes
            if (subdes.getArrayBracketsOptional() instanceof ArrayBracketsExists
                    && !(t.getParent() instanceof Assignment)) {
                // ako je array tip i ako nije designator u koji se upisuje (lvalue)
                // TODO ne radi
                if (t.getParent() instanceof DesignatorStatementIncrement
                        || t.getParent() instanceof DesignatorStatementDecrement) {
                    Code.put(Code.dup2);
                }
                Code.put(Code.aload);
            }
        } else {
            // ako je designator jedini
            if (t.getArrayBracketsOptional() instanceof ArrayBracketsExists && !(t.getParent() instanceof Assignment)) {
                // ako je array tip i ako nije designator u koji se upisuje (lvalue)

                if (t.getParent() instanceof DesignatorStatementIncrement
                        || t.getParent() instanceof DesignatorStatementDecrement) {
                    Code.put(Code.dup2);
                }
                Code.put(Code.aload);
            }

        }
    }

}
