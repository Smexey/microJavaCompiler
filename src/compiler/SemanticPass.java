package compiler;

import org.apache.log4j.Logger;

import ast.ArrayBracketsDeclExists;
import ast.ArrayBracketsOptional;
import ast.ConstVarClassDecl;
import ast.MethodDecl;
import ast.MethodTypeAndName;
import ast.MethodTypeReturn;
import ast.MethodTypeReturnVoid;
import ast.Program;
import ast.ProgramName;
import ast.SyntaxNode;
import ast.Type;
import ast.VarDecl;
import ast.VarDeclIdentRepeat;
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

    public void visit(ConstVarClassDecl varDecl) {
        // typename + niz identifikatora;
        currDeclType = null;
    }

    public void visit(Type type) {
        Obj typeNode = Tab.find(type.getTypeName());
        if (typeNode == Tab.noObj) {
            reportError("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
            type.struct = Tab.noType;
        } else {
            if (Obj.Type == typeNode.getKind()) {
                type.struct = typeNode.getType();
            } else {
                reportError("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
                type.struct = Tab.noType;
            }
        }
        currDeclType = type.struct;
    }

    Obj currentMethod = null;

    public void visit(MethodTypeAndName meth) {
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
        // cuvam curr method jer potpis metode je retType name(paramList)
        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();
    }

    public boolean passed() {
        return !errorDetected;
    }
}
