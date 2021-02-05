package compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;

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
    Struct arrType = new Struct(Struct.Array);

    SemanticPass() {
        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
        reportInfo("==================SEMANTIC==============", null);
    }

    public void reportError(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder("Semantic err: ");
        msg.append(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.error(msg.toString());
        System.err.println(msg.toString());
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
    Struct currDeclType = null;

    public void visit(ConstVarClassDecl varDecl) {
        // typename + niz identifikatora;
        currDeclType = null;
    }

    private int nVars = 0;

    public int getNVars() {
        return this.nVars;
    }

    private int level = 0;

    private boolean insertVar(Struct s, String name, boolean arrayBracketsDecl) {
        boolean ret = false;
        Obj o = null;
        if (Tab.currentScope.findSymbol(name) == null) {
            if (arrayBracketsDecl) {
                s = new Struct(Struct.Array, s);
                arrayBracketsDecl = false;
            }

            if (currentClass != null) {
                o = new Obj(Obj.Fld, name, s);
                currentClass.getType().getMembersTable().insertKey(o);
            } else {
                this.nVars++;
                o = new Obj(Obj.Var, name, s);
            }
            ret = true;
        } else {
            o = new Obj(Obj.Var, name, Tab.noType);
        }
        o.setLevel(level);
        Tab.currentScope().addToLocals(o);
        return ret;
    }

    public void visit(VarDeclNoErr varDecl) {
        if (!insertVar(varDecl.getType().struct, varDecl.getName(),
                varDecl.getArrayBracketsDeclOptional() instanceof ArrayBracketsDeclExists))
            reportError("var redefinition ", varDecl);
    }

    public void visit(VarDeclIdentRepeatNoErr varDeclRep) {
        if (!insertVar(currDeclType, varDeclRep.getName(),
                varDeclRep.getArrayBracketsDeclOptional() instanceof ArrayBracketsDeclExists))
            reportError("var redefinition ", varDeclRep);
    }

    // CONST
    public void visit(ConstDecl constDecl) {
        if (Tab.find(constDecl.getName()) == Tab.noObj) {

            Obj o = constDecl.getConstValue().obj;
            Struct s = o.getType();
            if (currDeclType.assignableTo(s)) {
                Obj t = Tab.insert(Obj.Con, constDecl.getName(), constDecl.getType().struct);
                t.setAdr(constDecl.getConstValue().obj.getAdr());
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
                Obj t = Tab.insert(Obj.Con, constDeclRep.getName(), currDeclType);
                t.setAdr(constDeclRep.getConstValue().obj.getAdr());
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
                reportError("" + type.getTypeName() + " ne predstavlja tip ", type);
                type.struct = Tab.noType;
            }
        }
        currDeclType = type.struct;
    }

    // CLASS
    Obj currentClass = null;

    public void visit(ClassName t) {
        // napravi novi tip mora novi struct zbog poredjenja structova kasnije?
        currentClass = Tab.insert(Obj.Type, t.getName(), new Struct(Struct.Class));
        Tab.openScope();
        Tab.insert(Obj.Fld, "tvf", Tab.nullType);
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
        if (sup != Tab.noObj && sup.getType().getKind() == Struct.Class) {
            if (sup.getName() != currentClass.getName()) {
                // stavim da extenduje superklass
                currentClass.getType().setElementType(sup.getType());

                // kopiram fieldove
                Iterator<Obj> it = sup.getType().getMembers().iterator();
                while (it.hasNext()) {
                    Obj o = it.next();
                    if (o.getKind() == Obj.Fld)
                        Tab.currentScope().addToLocals(o);// doda po referenci jel dobro?
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
        meth.obj = currentMethod = Tab.insert(Obj.Meth, meth.getName(), type);
        Tab.openScope();

        level++;

        if (currentClass != null) {
            Tab.insert(Obj.Var, "this", currentClass.getType());
        }
    }

    public void visit(MethodDecl meth) {
        if (returnFound == null && currentMethod.getType() != Tab.noType) {
            reportError("no return found" + currentMethod.getType().getKind() + "", meth);
        }
        returnFound = null;

        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();
        currentMethod = null;
        level--;
    }

    public void visit(ReturnExprStatement ret) {
        if (currentMethod != null) {
            if (ret.getExpr().struct == currentMethod.getType()) {
                // BRAVO
            } else
                reportError("return type missmatch", ret);
        } else
            reportError("cant return when not in method", ret);
        // pokupice notype iz expr
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
        Obj v = Tab.currentScope.findSymbol(var.getName());
        if (v == Tab.noObj || v == null) {
            if (var.getArrayBracketsDeclOptional() instanceof ArrayBracketsDeclExists) {
                Tab.insert(Obj.Var, var.getName(), new Struct(Struct.Array, var.getType().struct));
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
            currentMethod.setLevel(Tab.currentScope().getnVars() - 1);// zbog this
    }

    // STATEMENT
    // DESIGNATOR
    public void visit(DesignatorIdent des) {
        des.obj = Tab.find(des.getName());
        if (des.getParent() instanceof SubDesignatorRepeatExists) {
            // trazi u scopeu klase
            SubDesignatorRepeatExists subdes = (SubDesignatorRepeatExists) des.getParent();

        }
    }

    public void visit(Designator des) {
        Obj obj = des.getDesignatorIdent().obj;

        if (obj == Tab.noObj && currentClass != null) {
            Struct s = currentClass.getType();
            while (obj == Tab.noObj && s != null) {
                obj = s.getMembersTable().searchKey(des.getDesignatorIdent().getName());
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
                    Obj member = s.getMembersTable().searchKey(curr.getDesignatorIdent().getName());
                    // u definiciji klase smo

                    if (member != null) {
                        found = true;

                        // setuje obj DesignatorID za codgen
                        curr.getDesignatorIdent().obj = member;

                        // if array
                        if (curr.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
                            obj = new Obj(Obj.Elem, "", member.getType().getElemType());
                        } else {
                            obj = member;
                        }
                        break;
                    }
                    s = s.getElemType();
                }
                if (!found)
                    reportError("no such field: " + curr.getDesignatorIdent().getName(), curr);

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
        // (fDes.getType().getKind() == Struct.Class) Designator je klasnog tipa
        ArrayList<Struct> actParsList = actParsListStack.pop();
        if (fDes.getKind() == Obj.Meth) {

            if (functCall.getActParsOptional() instanceof ActParsExists) {
                if (fDes.getLevel() != actParsList.size()) {
                    reportError("wrong param num", functCall);
                    return;
                }

                Iterator<Obj> localSymIt = fDes.getLocalSymbols().iterator();
                Iterator<Struct> actParsIt = actParsList.iterator();

                // u levelu se cuva br formpars
                for (int i = 0; i < fDes.getLevel(); i++) {
                    Obj o = localSymIt.next();

                    // skip ako je this iz klasne metode
                    // ovo ne radi za necls metodu koja ima prvi parametar klasnog tipa imena this
                    // TODO popraviti
                    if (o.getName() == "this" && o.getType().getKind() == Struct.Class && i == 0)
                        continue;

                    Struct s = actParsIt.next();
                    // proveri da li je neko od natklasa assignable zbog polimorfizma
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
                }
            }
            functCall.struct = fDes.getType();
        } else
            reportError("not a function", functCall);
    }

    public void visit(FunctDesignator t) {
        actParsListStack.push(new ArrayList<Struct>());
    }

    public void visit(ActParsMultiple actPars) {
        actParsListStack.peek().add(actPars.getExpr().struct);
    }

    public void visit(ActParsSingle actPars) {
        actParsListStack.peek().add(actPars.getExpr().struct);
    }

    // EXPR && TERM && FACTOR
    public void visit(DesFactor df) {
        df.struct = df.getDesignator().obj.getType();
    }

    public void visit(FunctCallFactor t) {
        Obj o = t.getFunctCall().getFunctDesignator().getDesignator().obj;
        if (o.getType() == Tab.noType) {
            reportError("cant use funct with no return inside as a factor "
                    + t.getFunctCall().getFunctDesignator().getDesignator().getDesignatorIdent().getName(), t);
        }
        t.struct = t.getFunctCall().struct;
    }

    public void visit(ConstFactor df) {
        df.struct = df.getConstValue().obj.getType();
    }

    public void visit(NewOperatorFactor df) {
        // kopira tip svakako zbog errora
        df.struct = df.getType().struct;

        if (df.getArrayBracketsOptional() instanceof ArrayBracketsExists) {
            ArrayBracketsExists arrBr = (ArrayBracketsExists) df.getArrayBracketsOptional();

            if (arrBr.getExpr().struct == Tab.intType) {
                df.struct = this.arrType;
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
            // BRAVO
        } else
            reportError("different types for truthy and falsy", expr);
        expr.struct = expr.getSmolExpr1().struct;
    }

    public void visit(SmolExpr smExpr) {
        // ako nema desnog operanda ili ako su kompatibilni
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
        if (!usedCaseValuesStack.peek().contains(t.getCaseNum().getNum())) {
            usedCaseValuesStack.peek().add(t.getCaseNum().getNum());
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
            // ako je struct null onda je instanceof empty
            // BRAVO
        } else
            reportError("incompatible type for condition1", t);
        t.struct = s;
    }

    public void visit(CondTermOrRepeatExists t) {
        Struct s = t.getCondTerm().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondTermOrRepeat().struct) || t.getCondTermOrRepeat().struct == null)) {
            // ako je struct null onda je instanceof empty
            // BRAVO
        } else
            reportError("incompatible type for condition2", t);
        t.struct = s;
    }

    public void visit(CondTerm t) {
        Struct s = t.getCondFact().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondFactorAndRepeat().struct) || t.getCondFactorAndRepeat().struct == null)) {
            // ako je struct null onda je instanceof empty
            // BRAVO
        } else
            reportError("incompatible type for condition3", t);
        t.struct = s;
    }

    public void visit(CondFact t) {
        // poredjenja expr
        Struct s = t.getExpr().struct;

        if (s != this.boolType && t.getRelopExprOptional() instanceof RelopExprExists) {
            RelopExprExists relExpr2 = (RelopExprExists) t.getRelopExprOptional();
            if (relExpr2.getExpr().struct == s) {
                // BRAVO
            } else
                reportError("incompatible relation operands", t);
        } else {
            if (s != this.boolType)
                reportError("incompatible type for condition4", t);
        }

        t.struct = this.boolType;
    }

    public void visit(CondFactorAndRepeatExists t) {
        Struct s = t.getCondFact().struct;
        if (s == this.boolType
                && (s.compatibleWith(t.getCondFactorAndRepeat().struct) || t.getCondFactorAndRepeat().struct == null)) {
            // ako je struct null onda je instanceof empty
            // BRAVO
        } else
            reportError("incompatible type for condition5", t);
        t.struct = s;
    }
}
