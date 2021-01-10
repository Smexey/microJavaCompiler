package compiler;

import org.apache.log4j.Logger;

import ast.ArrayBracketsDeclExists;
import ast.ConstDecl;
import ast.ConstDeclRepeatDerived1;
import ast.ConstValue;
import ast.ConstValueBool;
import ast.ConstValueChar;
import ast.ConstValueNum;
import ast.ConstVarClassDecl;
import ast.MethodDecl;
import ast.MethodTypeAndName;
import ast.MethodTypeReturn;
import ast.MethodTypeReturnVoid;
import ast.Program;
import ast.ProgramName;
import ast.SyntaxNode;
import ast.Type;
import ast.VarDeclIdentRepeatDerived1;
import ast.VarDeclNoErr;
import ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

    boolean errorDetected = false;

    Logger log = Logger.getLogger(getClass());
    Struct boolType = new Struct(Struct.Bool);

    SemanticPass() {
        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
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
            // check assignability
            Tab.insert(Obj.Con, constDecl.getName(), constDecl.getType().struct);
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
            reportError("no return ", meth);
        }
        // cuvam curr method jer potpis metode je retType name(paramList)
        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();
    }

    public boolean passed() {
        return !errorDetected;
    }
}
