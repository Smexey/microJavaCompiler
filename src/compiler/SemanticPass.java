package compiler;

import java.nio.file.WatchEvent.Kind;
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
        reportInfo("==================SEMANTIC==============", null);
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
            Struct s = varDecl.getType().struct;

            if (arrayBracketsDecl) {
                s = new Struct(Struct.Array, varDecl.getType().struct);
                arrayBracketsDecl = false;
            }

            Obj o = null;

            if (currentClass != null) {
                o = new Obj(Obj.Fld, varDecl.getName(), s);
                currentClass.getType().getMembersTable().insertKey(o);
            } else {
                o = new Obj(Obj.Var, varDecl.getName(), s);
            }

            Tab.currentScope().addToLocals(o);

        } else {
            reportError("var redefinition ", varDecl);
        }
    }

    public void visit(VarDeclIdentRepeatDerived1 varDeclRep) {
        if (Tab.find(varDeclRep.getName()) == Tab.noObj) {
            Struct s = currDeclType;

            if (arrayBracketsDecl) {
                s = new Struct(Struct.Array, currDeclType);
                arrayBracketsDecl = false;
            }

            Obj o = null;

            if (currentClass != null) {
                o = new Obj(Obj.Fld, varDeclRep.getName(), s);
                currentClass.getType().getMembersTable().insertKey(o);
            } else {
                o = new Obj(Obj.Var, varDeclRep.getName(), s);
            }

            Tab.currentScope().addToLocals(o);

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
    Obj currentClass = null;

    public void visit(ClassName t) {
        // napravi novi tip
        currentClass = Tab.insert(Obj.Type, t.getName(), new Struct(Struct.Class));
        Tab.openScope();
    }

    public void visit(ClassDecl t) {
        // POPUNIM METODAMA NATKLASE AKO FALE
        if (currentClass.getType().getElemType() != null) {
            Iterator<Obj> it = currentClass.getType().getElemType().getMembers().iterator();
            while (it.hasNext()) {
                Obj o = it.next();
                // ako u trenutnom scopeu nema te metode
                if (o.getKind() == Obj.Meth && Tab.find(o.getName()) == Tab.noObj) {
                    Tab.currentScope().addToLocals(o);
                }
            }
        }

        Tab.chainLocalSymbols(currentClass.getType());
        Tab.closeScope();

        currentClass = null;
    }

    public void visit(SuperClassNoErr t) {
        Obj sup = Tab.find(t.getName());
        if (sup != Tab.noObj) {
            if (sup.getName() != currentClass.getName()) {
                // stavim da extenduje superklass
                currentClass.getType().setElementType(sup.getType());

                // kopiram fieldove
                Iterator<Obj> it = sup.getType().getMembers().iterator();
                while (it.hasNext()) {
                    Obj o = it.next();
                    if (o.getKind() == Obj.Fld)
                        Tab.insert(Obj.Fld, o.getName(), o.getType());
                }

            } else
                reportError("cant extend from self", t);
        } else
            reportError("no superclass found", t);
    }

    // METHOD
    Obj currentMethod = null;

    Struct returnFound = null;

    public void visit(MethodTypeAndName meth) {
        Struct type = null;
        if (meth.getMethodType() instanceof MethodTypeReturn) {
            type = ((MethodTypeReturn) meth.getMethodType()).getType().struct;
        } else if (meth.getMethodType() instanceof MethodTypeReturnVoid) {
            type = Tab.noType;
        }
        currentMethod = Tab.insert(Obj.Meth, meth.getName(), type);

        Tab.openScope();
        if (currentClass != null) {
            Tab.insert(Obj.Var, "this", currentClass.getType());
        }
        reportInfo("methodStart: ", meth);
    }

    public void visit(MethodDecl meth) {
        if (returnFound == null && currentMethod.getType() != Tab.noType) {
            reportError("no return found" + currentMethod.getType().getKind() + "", meth);
        }
        returnFound = null;

        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();
        currentMethod = null;
    }

    public void visit(ReturnExprStatement ret) {
        if (currentMethod != null) {
            if (ret.getExpr().struct == currentMethod.getType()) {

            } else
                reportError("return type missmatch", ret);
        } else
            reportError("cant return when not in method", ret);

        returnFound = ret.getExpr().struct;
    }

    public void visit(ReturnNoExprStatement ret) {
        if (currentMethod != null) {
            if (currentMethod.getType() == Tab.noType) {
                // BRAVO

            } else
                reportError("return type missmatch", ret);
        } else
            reportError("cant return when not in method", ret);
    }

    public void visit(FormParDeclNoErr var) {
        Obj v = Tab.find(var.getName());
        if (v == Tab.noObj || v.getKind() == Obj.Fld) {
            if (arrayBracketsDecl) {
                Tab.insert(Obj.Var, var.getName(), new Struct(Struct.Array, var.getType().struct));
                arrayBracketsDecl = false;
            } else {
                Tab.insert(Obj.Var, var.getName(), var.getType().struct);
            }
        } else {
            reportError("var redefinition formpar", var);
        }
    }

    public void visit(FormParsExists formPars) {
        if (currentClass == null)
            currentMethod.setLevel(Tab.currentScope().getnVars());
        else
            currentMethod.setLevel(Tab.currentScope().getnVars() - 1);
    }

    // STATEMENT
    // DESIGNATOR
    public void visit(Designator des) {
        Obj obj = Tab.find(des.getName());

        if (obj == Tab.noObj && currentClass != null) {
            Struct s = currentClass.getType();
            while (obj == Tab.noObj && s != null) {
                obj = s.getMembersTable().searchKey(des.getName());
                if (obj == null)
                    obj = Tab.noObj;
                s = s.getElemType();
            }
        }
        if (obj != Tab.noObj) {

            // ako postoje [] menja mu se tip u element niza
            if (des.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                ArrayBracketsExists arrBrackets = (ArrayBracketsExists) des.getArrayBracketsOptional();
                if (arrBrackets.getExpr().struct == Tab.intType) {
                    obj = new Obj(Obj.Elem, "", obj.getType().getElemType());
                } else
                    reportError("index mora biti intType", des);
            }

            SubDesignatorRepeat rep = des.getSubDesignatorRepeat();

            while (rep instanceof SubDesignatorRepeatExists) {
                SubDesignatorRepeatExists curr = (SubDesignatorRepeatExists) rep;

                Struct s = obj.getType();
                boolean found = false;
                while (s != null) {
                    Obj member = s.getMembersTable().searchKey(curr.getName());
                    // u definiciji klase smo

                    if (member != null) {
                        found = true;

                        obj = member;
                        break;
                    }
                    s = s.getElemType();
                }
                if (!found)
                    reportError("no such field: " + curr.getName(), curr);

                rep = curr.getSubDesignatorRepeat();
            }

        } else
            reportError("designator not declared in scope", des);

        des.obj = obj;
    }

    public void visit(AssignmentNoErr as) {
        if (as.getExpr().struct.getKind() == Struct.Class) {
            // check every supercls
            Struct s = as.getExpr().struct;
            boolean found = false;
            while (s != null) {
                if (s.assignableTo(as.getDesignator().obj.getType())) {
                    found = true;
                    break;
                }

                s = s.getElemType();
            }
            if (!found)
                reportError("not assignable to any superclass", as);
        }

        else {
            if (as.getExpr().struct.assignableTo(as.getDesignator().obj.getType())) {
                int kind = as.getDesignator().obj.getKind();
                if (kind == Obj.Var || kind == Obj.Fld || kind == Obj.Elem) {

                } else
                    reportError("not assignable to that desigantor", as);
            } else
                reportError("not assignable ", as);
        }
        as.struct = as.getDesignator().obj.getType();
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

                    // skip ako je this iz klasne metode
                    // ovo ne radi za metodu koja ima prvi parametar klasnog tipa imena this
                    if (o.getName() == "this" && o.getType().getKind() == Struct.Class && i == 0)
                        continue;

                    Struct s = itStr.next();
                    if (o.getType().getKind() == Struct.Class) {
                        // check every supercls
                        Struct spr = o.getType();
                        boolean found = false;
                        while (spr != null) {
                            if (spr.assignableTo(s)) {
                                found = true;
                                break;
                            }
                            s = s.getElemType();
                        }
                        if (!found)
                            reportError("missmatch param to any superclass" + o.getType().getKind() + " " + s.getKind(),
                                    functCall);
                    } else {
                        // neklasni tip
                        if (!o.getType().assignableTo(s)) {
                            reportError("missmatch param: " + o.getType().getKind() + " " + s.getKind(), functCall);
                        }
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
        // kopira tip
        if (df.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
            ArrayBracketsExists arrBr = (ArrayBracketsExists) df.getArrayBracketsOptional();
            if (arrBr.getExpr().struct == Tab.intType) {
                df.struct = new Struct(Struct.Array);
                df.struct.setElementType(df.getType().struct);
            } else
                reportError("new array size mora biti inttype", df);
        } else {
            // mora biti klasnog tipa
            Obj cls = Tab.find(df.getType().getTypeName());

            if (cls.getType().getKind() == Struct.Class) {
                // BRAVO
            } else
                reportError("cant new non class type", df);
            df.struct = cls.getType();
        }
    }

    public void visit(SubExprFactor df) {
        df.struct = df.getExpr().struct;
    }

    public void visit(NoTernExpr expr) {
        expr.struct = expr.getSmolExpr().struct;
    }

    public void visit(TernaryOperatorExpr expr) {
        if (expr.getSmolExpr1().struct == expr.getSmolExpr2().struct) {

        } else
            reportError("different types for truthy and falsy", expr);
        expr.struct = expr.getSmolExpr1().struct;
    }

    public void visit(SmolExpr smExpr) {
        if (smExpr.getTermAddopRepeat() instanceof TermAddopRepeatEmpty
                || smExpr.getTerm().struct.compatibleWith(smExpr.getTermAddopRepeat().struct)) {
            // BRAVO
        } else
            reportError("not compatible", smExpr);
        smExpr.struct = smExpr.getTerm().struct;
    }

    public void visit(Term term) {
        if (term.getFactorMulopRepeat() instanceof FactorMulopRepeatEmpty
                || term.getFactor().struct.compatibleWith(term.getFactorMulopRepeat().struct)) {
            // BRAVO
        } else
            reportError("not compatible", term);
        term.struct = term.getFactor().struct;
    }

    public void visit(TermAddopRepeatExists t) {
        if (t.getTermAddopRepeat() instanceof TermAddopRepeatEmpty
                || t.getTerm().struct.compatibleWith(t.getTermAddopRepeat().struct)) {
            // BRAVO
        } else
            reportError("not compatible", t);
        t.struct = t.getTerm().struct;
    }

    public void visit(FactorMulopRepeatExists t) {
        if (t.getFactorMulopRepeat() instanceof FactorMulopRepeatEmpty
                || t.getFactor().struct.compatibleWith(t.getFactorMulopRepeat().struct)) {
            // BRAVO
        } else
            reportError("not compatible", t);
        t.struct = t.getFactor().struct;
    }

    // DOWHILE && SWITCH && IF
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
        if (t.getCondition().struct == this.boolType) {
            // BRAVO
        } else
            reportError("dowhile has non boolean condition", t);
    }

    public void visit(SwitchStatement t) {
        switchDepth--;
        usedCaseValuesStack.pop();
        if (t.getExpr().struct == Tab.intType) {
            // BRAVO
        } else
            reportError("SwitchSTatement expr must be intType", t);
    }

    public void visit(IfStatement t) {
        if (t.getCondition().struct == this.boolType) {
            // BRAVO
        } else
            reportError("if has non boolean condition", t);
    }

    public void visit(ContinueStatement t) {
        if (doWhileDepth > 0) {
            // BRAVO
        } else
            reportError("cant continue from here", t);
    }

    public void visit(BreakStatement t) {
        if (switchDepth > 0 || doWhileDepth > 0) {
            // BRAVO
        } else
            reportError("cant break from here", t);
    }

    public void visit(CaseRepeatExists t) {
        if (!usedCaseValuesStack.peek().contains(t.getNum())) {
            usedCaseValuesStack.peek().add(t.getNum());
        } else
            reportError("already used this num", t);
    }

    // READ && PRINT
    public void visit(PrintStatement t) {
        int kind = t.getExpr().struct.getKind();
        if (kind == Tab.charType.getKind() || kind == Tab.intType.getKind() || kind == this.boolType.getKind()) {
            // BRAVO
        } else
            reportError("incompatible print type", t);
    }

    public void visit(ReadStatement t) {
        int kind = t.getDesignator().obj.getKind();
        if (kind == Obj.Var || kind == Obj.Fld || kind == Obj.Elem) {
            kind = t.getDesignator().obj.getType().getKind();
            if (kind == Tab.charType.getKind() || kind == Tab.intType.getKind() || kind == this.boolType.getKind()) {
                // BRAVO
            } else
                reportError("incompattible designator type for read", t);
        } else
            reportError("incompatible designator for read", t);
    }

    // CONDITION
    public void visit(ConditionNoErr t) {
        Struct s = t.getCondTerm().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondTermOrRepeat().struct) || t.getCondTermOrRepeat().struct == null)) {
            // BRAVO
        } else
            reportError("incompatible type for condition", t);
        t.struct = s;
    }

    public void visit(CondTermOrRepeatExists t) {
        Struct s = t.getCondTerm().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondTermOrRepeat().struct) || t.getCondTermOrRepeat().struct == null)) {
            // BRAVO
        } else
            reportError("incompatible type for condition", t);
        t.struct = s;
    }

    public void visit(CondTerm t) {
        Struct s = t.getCondFact().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondFactorAndRepeat().struct) || t.getCondFactorAndRepeat().struct == null)) {
            // BRAVO
        } else
            reportError("incompatible type for condition", t);
        t.struct = s;
    }

    public void visit(CondFact t) {
        // poredjenja expr
        Struct s = t.getExpr().struct;
        if (s == this.boolType) {
            // prosto je true jer je const?
            // t.struct = this.boolType;
        } else {
            if (t.getRelopExprOptional() instanceof RelopExprExists) {
                RelopExprExists relExpr2 = (RelopExprExists) t.getRelopExprOptional();
                if (relExpr2.getExpr().struct == s) {
                    // t.struct = this.boolType;
                } else
                    reportError("incompatible relation operands", t);
            } else
                reportError("incompatible type for condition", t);
        }
        t.struct = this.boolType;
    }

    public void visit(CondFactorAndRepeatExists t) {
        Struct s = t.getCondFact().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondFactorAndRepeat().struct) || t.getCondFactorAndRepeat().struct == null)) {
            // BRAVO
        } else
            reportError("incompatible type for condition", t);
        t.struct = s;
    }

}
