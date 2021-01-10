package compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

    boolean errorDetected = false;

    public boolean passed() {
        return !errorDetected;
    }

    Logger log = Logger.getLogger(getClass());
    Struct boolType = new Struct(Struct.Bool);

    SemanticPass() {
        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
        reportInfo("==================SEMANTICSTART==============", null);
    }

    public void reportError(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.error(msg.toString());
    }

    public void reportInfo(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    // Program
    public void visit(Program program) {
        Tab.chainLocalSymbols(program.getProgramName().obj);
        Tab.closeScope();
    }

    public void visit(ProgramName progName) {
        progName.obj = Tab.insert(Obj.Prog, progName.getProgramName(), Tab.noType);
        Tab.openScope();
    }

    // VAR
    boolean arrayBracketsDecl = false;

    public void visit(ArrayBracketsDeclExists arBrDeclExists) {
        arrayBracketsDecl = true;
    }

    Struct currDeclType = null;

    public void visit(VarDeclNoErr varDecl) {
        if (Tab.find(varDecl.getName()) == Tab.noObj) {
            if (arrayBracketsDecl) {
                Tab.insert(Obj.Var, varDecl.getName(), new Struct(Struct.Array, varDecl.getType().struct));
                arrayBracketsDecl = false;
            } else {
                Tab.insert(Obj.Var, varDecl.getName(), varDecl.getType().struct);
            }
        } else {
            reportError("var redefinition ", varDecl);
        }
    }

    public void visit(VarDeclIdentRepeatDerived1 varDeclRep) {
        if (Tab.find(varDeclRep.getName()) == Tab.noObj) {
            if (arrayBracketsDecl) {
                // array
                Tab.insert(Obj.Var, varDeclRep.getName(), new Struct(Struct.Array, currDeclType));
                arrayBracketsDecl = false;
            } else {
                Tab.insert(Obj.Var, varDeclRep.getName(), currDeclType);
            }
        } else {
            reportError("var redefinition ", varDeclRep);
        }
    }

    // CONST
    public void visit(ConstDecl constDecl) {
        if (Tab.find(constDecl.getName()) == Tab.noObj) {

            Obj o = constDecl.getConstValue().obj;
            Struct s = o.getType();
            if (currDeclType.assignableTo(s)) {
                Tab.insert(Obj.Con, constDecl.getName(), constDecl.getType().struct);
            } else
                reportError("const not assignable ", constDecl);
        } else {
            reportError("const redefinition ", constDecl);
        }
    }

    public void visit(ConstDeclRepeatDerived1 constDeclRep) {
        if (Tab.find(constDeclRep.getName()) == Tab.noObj) {

            Obj o = constDeclRep.getConstValue().obj;
            Struct s = o.getType();
            if (currDeclType.assignableTo(s)) {
                Tab.insert(Obj.Con, constDeclRep.getName(), currDeclType);
            } else
                reportError("const not assignable ", constDeclRep);

        } else
            reportError("const redefinition ", constDeclRep);
    }

    public void visit(ConstValueNum cv) {
        Obj o = new Obj(Obj.Con, "", Tab.intType);
        o.setAdr(cv.getN1());
        cv.obj = o;
    }

    public void visit(ConstValueChar cv) {
        Obj o = new Obj(Obj.Con, "", Tab.charType);
        o.setAdr(cv.getC1());
        cv.obj = o;
    }

    public void visit(ConstValueBool cv) {
        Obj o = new Obj(Obj.Con, "", this.boolType);
        o.setAdr(cv.getB1() ? 1 : 0);
        cv.obj = o;
    }

    // TYPE
    public void visit(Type type) {
        Obj typeNode = Tab.find(type.getTypeName());
        if (typeNode == Tab.noObj) {
            reportError("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
            type.struct = Tab.noType;
        } else {
            if (typeNode.getKind() == Obj.Type) {
                type.struct = typeNode.getType();
            } else {
                reportError("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
                type.struct = Tab.noType;
            }
        }
        currDeclType = type.struct;
    }

    public void visit(ConstVarClassDecl varDecl) {
        // typename + niz identifikatora;
        currDeclType = null;
    }

    // CLASS
    // TODO

    // METHOD
    Obj currentMethod = null;

    boolean returnFound = false;

    public void visit(MethodTypeAndName meth) {
        returnFound = false;
        Struct type = null;
        if (meth.getMethodType() instanceof MethodTypeReturn) {
            type = ((MethodTypeReturn) meth.getMethodType()).getType().struct;
        } else if (meth.getMethodType() instanceof MethodTypeReturnVoid) {
            type = Tab.noType;

        }
        currentMethod = Tab.insert(Obj.Meth, meth.getName(), type);

        Tab.openScope();
        reportInfo("methodStart: ", meth);
    }

    public void visit(MethodDecl meth) {
        if (!returnFound && currentMethod.getType() != Tab.noType) {
            reportError("no return " + currentMethod.getType().getKind() + "", meth);
        }
        // cuvam curr method jer potpis metode je retType name(paramList)
        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();
    }

    public void visit(FormParDeclNoErr var) {
        if (Tab.find(var.getName()) == Tab.noObj) {
            if (arrayBracketsDecl) {
                Tab.insert(Obj.Var, var.getName(), new Struct(Struct.Array, var.getType().struct));
                arrayBracketsDecl = false;
            } else {
                Tab.insert(Obj.Var, var.getName(), var.getType().struct);
            }
        } else {
            reportError("var redefinition ", var);
        }
    }

    public void visit(FormParsExists formPars) {
        currentMethod.setLevel(Tab.currentScope().getnVars());
    }

    // STATEMENT
    // DESIGNATOR
    public void visit(Designator des) {
        Obj obj = Tab.find(des.getName());
        if (obj != Tab.noObj) {
            des.obj = obj;
            // ako postoje [] menja mu se tip u element niza
            if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                des.obj = new Obj(Obj.Elem, "", obj.getType().getElemType());
            }
        } else
            reportError("designator not declared ", des);
    }

    public void visit(AssignmentNoErr as) {
        if (as.getExpr().struct.assignableTo(as.getDesignator().obj.getType())) {
            int kind = as.getDesignator().obj.getKind();
            if (kind == Obj.Var || kind == Obj.Fld || kind == Obj.Elem) {
                as.struct = as.getDesignator().obj.getType();
            } else
                reportError("not assignable to that desigantor", as);
        } else
            reportError("not assignable ", as);
    }

    public void visit(DesignatorStatementIncrement as) {
        int kind = as.getDesignator().obj.getKind();
        if (kind == Obj.Var || kind == Obj.Fld || kind == Obj.Elem) {
            if (as.getDesignator().obj.getType() == Tab.intType) {

            } else
                reportError("designator must be inttype", as);
        } else
            reportError("not assignable to that desigantor", as);
    }

    public void visit(DesignatorStatementDecrement as) {
        int kind = as.getDesignator().obj.getKind();
        if (kind == Obj.Var || kind == Obj.Fld || kind == Obj.Elem) {
            if (as.getDesignator().obj.getType() == Tab.intType) {

            } else
                reportError("designator must be inttype", as);
        } else
            reportError("not assignable to that desigantor", as);
    }

    // FUNCTCALL
    Stack<ArrayList<Struct>> actParsListStack = new Stack<>();

    public void visit(FunctCall functCall) {
        Obj fDes = functCall.getFunctDesignator().getDesignator().obj;
        ArrayList<Struct> actParsList = actParsListStack.pop();
        if (fDes.getKind() == Obj.Meth) {

            if (functCall.getActParsOptional() instanceof ActParsExists) {
                if (fDes.getLevel() != actParsList.size()) {
                    reportError("wrong param num", functCall);
                    return;
                }

                Iterator<Obj> itObj = fDes.getLocalSymbols().iterator();
                Iterator<Struct> itStr = actParsList.iterator();
                // u levelu se cuva br formpars
                System.out.println("level: " + fDes.getLevel());
                for (int i = 0; i < fDes.getLevel(); i++) {
                    Obj o = itObj.next();
                    Struct s = itStr.next();
                    if (!o.getType().assignableTo(s)) {
                        // ne radi
                        reportError("missmatch param: " + o.getType().getKind() + " " + s.getKind(), functCall);
                        return;
                    }
                }
            }
            functCall.struct = fDes.getType();
        } else {
            reportError("not a function", functCall);
        }
    }

    public void visit(FunctDesignator t) {
        actParsListStack.push(new ArrayList<Struct>());
    }

    public void visit(ActParsMultiple actPars) {
        System.out.println("act pars multiple: " + actPars.getExpr().struct.getKind());
        actParsListStack.peek().add(actPars.getExpr().struct);
    }

    public void visit(ActParsSingle actPars) {
        System.out.println("act pars single: " + actPars.getExpr().struct.getKind());
        actParsListStack.peek().add(actPars.getExpr().struct);
    }

    // EXPR && TERM && FACTOR
    public void visit(DesFactor df) {
        df.struct = df.getDesignator().obj.getType();
    }

    public void visit(FunctCallFactor df) {
        df.struct = df.getFunctCall().struct;
    }

    public void visit(ConstFactor df) {
        df.struct = df.getConstValue().obj.getType();
    }

    public void visit(NewOperatorFactor df) {
        df.struct = df.getType().struct;
    }

    public void visit(SubExprFactor df) {
        df.struct = df.getExpr().struct;
    }

    public void visit(NoTernExpr expr) {
        expr.struct = expr.getSmolExpr().struct;
    }

    public void visit(SmolExpr smExpr) {
        if (smExpr.getTermAddopRepeat() instanceof TermAddopRepeatEmpty
                || smExpr.getTerm().struct.compatibleWith(smExpr.getTermAddopRepeat().struct)) {
            smExpr.struct = smExpr.getTerm().struct;
        } else
            reportError("not compatible", smExpr);
    }

    public void visit(Term term) {
        if (term.getFactorMulopRepeat() instanceof FactorMulopRepeatEmpty
                || term.getFactor().struct.compatibleWith(term.getFactorMulopRepeat().struct)) {
            term.struct = term.getFactor().struct;
        } else
            reportError("not compatible", term);
    }

    public void visit(TermAddopRepeatExists t) {
        if (t.getTermAddopRepeat() instanceof TermAddopRepeatEmpty
                || t.getTerm().struct.compatibleWith(t.getTermAddopRepeat().struct)) {
            t.struct = t.getTerm().struct;
        } else
            reportError("not compatible", t);
    }

    public void visit(FactorMulopRepeatExists t) {
        if (t.getFactorMulopRepeat() instanceof FactorMulopRepeatEmpty
                || t.getFactor().struct.compatibleWith(t.getFactorMulopRepeat().struct)) {
            t.struct = t.getFactor().struct;
        } else
            reportError("not compatible", t);
    }

    // DOWHILE && SWITCH
    Stack<Set<Integer>> usedCaseValuesStack = new Stack<>();
    int doWhileDepth = 0;
    int switchDepth = 0;

    public void visit(StartDo t) {
        doWhileDepth++;
    }

    public void visit(StartSwitch t) {
        switchDepth++;
        usedCaseValuesStack.push(new HashSet<Integer>());
    }

    public void visit(DoWhileStatement t) {
        doWhileDepth--;
    }

    public void visit(SwitchStatement t) {
        switchDepth--;
        usedCaseValuesStack.pop();
    }

    public void visit(ContinueStatement t) {
        if (doWhileDepth > 0) {
            // BRAVO
        } else
            reportError("", t);
    }

    public void visit(BreakStatement t) {
        if (switchDepth > 0 || doWhileDepth > 0) {
            // BRAVO
        } else
            reportError("", t);
    }

    public void visit(CaseRepeatExists t) {
        if (!usedCaseValuesStack.peek().contains(t.getNum())) {
            usedCaseValuesStack.peek().add(t.getNum());
        } else
            reportError("already used this num", t);
    }
}
