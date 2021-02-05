package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ast.Program;
import java_cup.runtime.Symbol;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import util.DumpSymbolTableVisitorExt;
import util.Log4JUtils;

public class Test {

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(Test.class);
        File sourceCode;
        if (args.length > 0)
            sourceCode = new File(args[0]);
        else
            sourceCode = new File("testFiles/codeTest.mj");
        // semanticTestClass

        if (!sourceCode.exists()) {
            log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
            return;
        }

        log.info("Compiling source file: " + sourceCode.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
            Yylex lexer = new Yylex(br);
            Parser p = new Parser(lexer);
            Symbol s = p.parse(); // pocetak parsiranja
            Program prog = (Program) (s.value);

            Tab.init();
            SemanticPass semanticCheck = new SemanticPass();
            prog.traverseBottomUp(semanticCheck);
            Tab.dump(new DumpSymbolTableVisitorExt());

            log.info("===========================");
            log.info(prog.toString(""));
            log.info("===========================");

            if (semanticCheck.passed() && !p.errorDetected) {
                File objFile = new File("output/program.obj");
                CodeGenerator codeGenerator = new CodeGenerator(semanticCheck.getClsList());

                prog.traverseBottomUp(codeGenerator);
                Code.dataSize = semanticCheck.getNVars();
                Code.dataSize += codeGenerator.getTVFSize();
                Code.mainPc = codeGenerator.getMainPc();
                Code.write(new FileOutputStream(objFile));
                log.info("Parsiranje uspesno zavrseno!");
            }

        }
    }
}
