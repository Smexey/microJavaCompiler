package compiler;

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
        Obj o = Tab.insert(Obj.Con, "$", cf.getConstValue().obj.getType());
        o.setLevel(0);
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
        Code.put(Code.const_1);
        Code.put(Code.add);
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

    public void visit(AssignmentNoErr t) {
        Code.store(t.getDesignator().obj);
    }

    public void visit(NewOperatorFactor t) {
        if (t.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
            int kind = t.getType().struct.getKind();
            Code.put(Code.newarray);

            if (kind == Struct.Char || kind == Struct.Bool) {
                Code.put(0);
            } else {
                Code.put(1);
            }

        } else {
            Code.put(Code.new_);
            Code.put(t.getType().struct.getNumberOfFields());
        }
    }

    public void visit(DesignatorIdent t) {

        Designator des = (Designator) t.getParent();
        // ubacujem ga svaki put osim ako je on poslednji terminal
        if (!(des.getParent() instanceof Assignment)) {
            Code.load(t.obj);
        } else {
            // jeste assign ali treba za evaluaciju
            if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists
                    || des.getSubDesignatorRepeat() instanceof SubDesignatorRepeatExists) {
                Code.load(t.obj);
            }
        }

    }

    public void visit(Designator t) {
        // TODO change logiku kad ubacim fieldove klasa
        if (t.getArrayBracketsOptional() instanceof ArrayBracketsExists && !(t.getParent() instanceof Assignment)) {
            Code.put(Code.aload);
        }
    }

}
