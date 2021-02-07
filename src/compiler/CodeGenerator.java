package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import ast.*;
import compiler.CounterVisitor.FormParamCounter;
import compiler.CounterVisitor.GlobalVarConstCounter;
import compiler.CounterVisitor.VarDeclCounter;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;
    private Program rootProgram = null;
    List<Obj> clsList = null;
    int tvfSize = 0;

    public int getTVFSize() {
        return tvfSize;
    }

    Set<Obj> globalFunctions;

    CodeGenerator(List<Obj> clsList, Set<Obj> globalFunctions) {
        this.clsList = clsList;
        this.globalFunctions = globalFunctions;
    }

    // moraju se porediti preko kind a ne referencom
    Struct boolType = new Struct(Struct.Bool);
    Struct arrType = new Struct(Struct.Array);

    public int getMainPc() {
        return this.mainPc;
    }

    public void visit(ProgramName t) {
        // init metode
        rootProgram = (Program) t.getParent();
        Obj chrObj = Tab.find("chr");
        chrObj.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1); // args
        Code.put(1);
        Code.put(Code.load_n);// load0
        Code.put(Code.exit);
        Code.put(Code.return_);

        Obj ordObj = Tab.find("ord");
        ordObj.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1); // args
        Code.put(1);
        Code.put(Code.load_n); // load0
        Code.put(Code.exit);
        Code.put(Code.return_);

        Obj lenObj = Tab.find("len");
        lenObj.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1); // args
        Code.put(1);
        Code.put(Code.load_n);// load0
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    int level = 0;

    public void visit(PrintStatement print) {
        // expr vec na steku
        dbg = print.getLine();
        if (print.getExpr().struct == Tab.intType) {
            Code.loadConst(5);// sirina
            Code.put(Code.print);
        } else {
            Code.loadConst(1);
            Code.put(Code.bprint);
        }
    }

    public void visit(ReadStatement rd) {
        // expr vec na steku
        if (rd.getDesignator().obj.getType() == Tab.intType) {
            Code.put(Code.read);
        } else {
            Code.put(Code.bread);
        }
        Code.store(rd.getDesignator().obj);
    }

    public void visit(ConstFactor cf) {
        // ConstValueNum, ConstValueChar, ConstValueBool
        Obj o = new Obj(Obj.Con, "$", cf.getConstValue().obj.getType(), 0, 0);
        o.setAdr(cf.getConstValue().obj.getAdr());
        Code.load(o);
    }

    private Map<String, Integer> tvfStartAdr = new HashMap<>();

    private void generateTVF() {
        GlobalVarConstCounter varCounter = new GlobalVarConstCounter();
        rootProgram.getConstVarClassDeclRepeat().traverseBottomUp(varCounter);
        int cnt = varCounter.getCnt();
        System.err.println("cnt = " + cnt);
        // racunam da su svi 4?
        // mozda uzimam tip? sta ja znam?

        for (Obj cls : clsList) {
            tvfStartAdr.put(cls.getName(), cnt);

            for (Obj member : cls.getType().getMembers()) {
                if (member.getKind() == Obj.Meth) {
                    // generate tvf entry
                    for (char c : member.getName().toCharArray()) {
                        Code.loadConst(c);
                        Code.put(Code.putstatic);
                        Code.put2(cnt++);
                    }
                    // end name string
                    Code.loadConst(-1);
                    Code.put(Code.putstatic);
                    Code.put2(cnt++);

                    // put adr
                    Code.loadConst(member.getAdr());
                    Code.put(Code.putstatic);
                    Code.put2(cnt++);

                }
            }
            // end class methods
            Code.loadConst(-2);
            Code.put(Code.putstatic);
            Code.put2(cnt++);
        }

        tvfSize = cnt;

    }

    public void visit(MethodTypeAndName t) {
        if ("main".equalsIgnoreCase(t.getName())) {
            this.mainPc = Code.pc; // na kraju setujem nazad ovaj pc
            // Code.mainPc = Code.pc; zasto ovo nisam uradio pardoniram

            // mainpc je sada iznad tvf jer mora da se inituje
            generateTVF();
        }
        t.obj.setAdr(Code.pc);
        SyntaxNode parent = t.getParent();

        VarDeclCounter varCnter = new VarDeclCounter();
        parent.traverseTopDown(varCnter);

        FormParamCounter paramCnter = new FormParamCounter();
        parent.traverseTopDown(paramCnter);

        Code.put(Code.enter);

        // ako je cls onda ima this
        int lvl = 0;
        if (!globalFunctions.contains(t.obj)) {
            lvl = 1;
        }

        Code.put(paramCnter.getCnt() + lvl);
        Code.put(paramCnter.getCnt() + lvl + varCnter.getCnt());
    }

    public void visit(MethodDecl t) {
        // impliocitni return
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(FunctCall t) {
        currentlyCalledFunctions.pop();

        Obj o = t.getFunctDesignator().getDesignator().obj;
        int offset = o.getAdr() - Code.pc;

        // provera dal je metoda unutar klase

        if (!globalFunctions.contains(o)) {

            // radi se invoke virtual

            Code.put(Code.getfield);
            Code.put2(0);

            Code.put(Code.invokevirtual);
            for (char c : o.getName().toCharArray()) {
                Code.put4(c);
            }
            Code.put4(-1);
            // Code.put(Code.putstatic);
            // Code.put2(cnt++);

        } else {
            Code.put(Code.call);
            Code.put2(offset);
        }

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
        Code.putJump(Code.pc + 4);
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

        if (lastCases.peek() != -1)
            Code.fixup(lastCases.peek());
        lastCases.pop();
        Code.put(Code.pop);
        fixup(breakLists.peek());
        breakLists.pop();
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

    int ternEndIfAdr = -1;
    int ternElseAdr = -1;

    public void visit(SmolExpr t) {
        if (t.getParent() instanceof TernaryOperatorExpr) {
            TernaryOperatorExpr parent = (TernaryOperatorExpr) t.getParent();
            if (parent.getSmolExpr() == t) {
                Code.put(Code.const_n + 0);
                Code.putFalseJump(Code.ne, 0);
                ternElseAdr = Code.pc - 2;
            } else if (parent.getSmolExpr1() == t) {
                Code.putJump(0);
                ternEndIfAdr = Code.pc - 2;
            } else if (parent.getSmolExpr2() == t) {
                Code.fixup(ternEndIfAdr);
            }
        }
    }

    public void visit(TernaryDdots t) {
        Code.fixup(ternElseAdr);
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

    // CLASS
    boolean inClass = false;

    public void visit(ClassName t) {
        inClass = true;
    }

    public void visit(ClassDecl t) {
        inClass = false;
    }

    // FunctDesignator
    Stack<Obj> currentlyCalledFunctions = new Stack<>();

    public void visit(FunctDesignator t) {
        Obj o = t.getDesignator().obj;
        currentlyCalledFunctions.push(o);
    }

    public void visit(ActPar t) {
        if (!globalFunctions.contains(currentlyCalledFunctions.peek())) {
            Code.put(Code.dup_x1);
            Code.put(Code.pop);
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

            // set tvf to the duped addr
            Code.put(Code.dup);
            String tvfName = t.getType().getTypeName();
            Code.loadConst(tvfStartAdr.get(tvfName));
            Code.put(Code.putfield);
            Code.put2(0);

        }
    }

    int dbg = 0;

    public void visit(DesignatorIdent t) {
        dbg = t.getLine();

        if (t.getParent() instanceof Designator) {
            Designator des = (Designator) t.getParent();// moze biti i subdesrepeatexists

            if (inClass) {
                if (des.obj.getKind() == Obj.Fld && !t.obj.getName().equals("this")) {
                    // tipa je field a nema this ispred sebe i nije sam on this
                    Code.put(Code.load_n + 0);
                }

                if (des.obj.getKind() == Obj.Meth && !(globalFunctions.contains(des.obj))) {
                    // unutrasnja metoda pozvana unutar klase nema this ispred sebe
                    Code.put(Code.load_n + 0);
                    Code.put(Code.dup);
                }
            }

            // nisam poslednji u nizu svakako se loadujem
            if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists
                    || des.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                Code.load(t.obj);
            } else {
                // poslednji sam u nizu

                // trazim root Designator
                SyntaxNode x = t.getParent();
                while (!(x instanceof Designator))
                    x = x.getParent();

                // ako je ovo sve bila funkcija ne ucitavam ime funkc
                if (!(x.getParent() instanceof FunctDesignator)) {
                    if (!(x.getParent() instanceof Assignment) && !(x.getParent() instanceof ReadStatement)) {
                        Code.load(t.obj);
                    } else {
                        // ako jeste read ili assign ali ima arraybrackets posle
                        if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                            Code.load(t.obj);
                        }
                    }
                }
            }
        } else {
            SubDesignatorRepeatExists des = (SubDesignatorRepeatExists) t.getParent();// moze biti i subdesrepeatexists

            // nisam poslednji u nizu svakako se loadujem
            if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists
                    || des.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                Code.load(t.obj);
            } else {
                // poslednji sam u nizu

                // trazim root Designator
                SyntaxNode x = t.getParent();
                while (!(x instanceof Designator))
                    x = x.getParent();

                // ako je ovo sve bila funkcija ne ucitavam ime funkc
                if (!(x.getParent() instanceof FunctDesignator)) {
                    if (!(x.getParent() instanceof Assignment) && !(x.getParent() instanceof ReadStatement)) {

                        if (x.getParent() instanceof DesignatorStatementIncrement
                                || x.getParent() instanceof DesignatorStatementDecrement) {
                            Code.put(Code.dup);
                        }
                        Code.load(t.obj);
                    } else {
                        // ako jeste read ili assign ali ima arraybrackets posle
                        if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                            Code.load(t.obj);
                        }
                    }
                } else {
                    // ako nije bila globalna funkcija onda dupujem this
                    if (!globalFunctions.contains(t.obj)) {
                        Code.put(Code.dup);
                    }
                }
            }
        }

    }

    public void visit(ArrayBracketsExists t) {
        if (t.getParent() instanceof NewOperatorFactor) {
            return;
        }

        if (t.getParent() instanceof Designator) {
            Designator des = (Designator) t.getParent();

            if (des.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                Code.put(Code.aload);
            } else {
                // poslednji sam ali array
                // vidim dal sam assignment
                SyntaxNode x = t.getParent();
                while (!(x instanceof Designator))
                    x = x.getParent();

                if (!(x.getParent() instanceof FunctDesignator)) { // nema niza funkcija svakako?
                    if (!(x.getParent() instanceof Assignment) && !(x.getParent() instanceof ReadStatement)) {

                        if (x.getParent() instanceof DesignatorStatementIncrement
                                || x.getParent() instanceof DesignatorStatementDecrement) {
                            Code.put(Code.dup2);
                        }
                        if (des.obj.getType() == Tab.charType || des.obj.getType().getKind() == boolType.getKind()) {
                            Code.put(Code.baload);
                        } else {
                            Code.put(Code.aload);
                        }
                    }
                }
            }
        } else {
            // kopija iznad
            SubDesignatorRepeatExists des = (SubDesignatorRepeatExists) t.getParent();
            if (des.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                Code.put(Code.aload);

            } else {
                // poslednji sam ali array
                // vidim dal sam assignment
                SyntaxNode x = t.getParent();
                while (!(x instanceof Designator))
                    x = x.getParent();

                if (!(x.getParent() instanceof FunctDesignator)) { // nema niza funkcija svakako?
                    if (!(x.getParent() instanceof Assignment) && !(x.getParent() instanceof ReadStatement)) {

                        if (x.getParent() instanceof DesignatorStatementIncrement
                                || x.getParent() instanceof DesignatorStatementDecrement) {
                            Code.put(Code.dup2);
                        }
                        if (des.getDesignatorIdent().obj.getType().getElemType() == Tab.charType
                                || des.getDesignatorIdent().obj.getType().getElemType().getKind() == boolType
                                        .getKind()) {
                            Code.put(Code.baload);
                        } else {
                            Code.put(Code.aload);
                        }
                    }
                }
            }
        }

    }

}
