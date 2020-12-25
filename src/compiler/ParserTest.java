package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ast.SyntaxNode;
import java_cup.runtime.Symbol;

import util.Log4JUtils;

public class ParserTest {

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(ParserTest.class);
        if (args.length < 2) {
            log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");
            return;
        }

        File sourceCode = new File(args[0]);
        if (!sourceCode.exists()) {
            log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
            return;
        }

        log.info("Compiling source file: " + sourceCode.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
            Yylex lexer = new Yylex(br);
            Parser p = new Parser(lexer);
            Symbol s = p.parse(); // pocetak parsiranja
            SyntaxNode prog = (SyntaxNode) (s.value);

            Tab.init(); // Universe scope
            SemanticPass semanticCheck = new SemanticPass();
            prog.traverseBottomUp(semanticCheck);

            log.info("Print calls = " + semanticCheck.printCallCount);
            Tab.dump();

            if (!p.errorDetected && semanticCheck.passed()) {
                File objFile = new File(args[1]);
                log.info("Generating bytecode file: " + objFile.getAbsolutePath());
                if (objFile.exists())
                    objFile.delete();

                // Code generation...
                CodeGenerator codeGenerator = new CodeGenerator();
                prog.traverseBottomUp(codeGenerator);
                Code.dataSize = semanticCheck.nVars;
                Code.mainPc = codeGenerator.getMainPc();
                Code.write(new FileOutputStream(objFile));
                log.info("Parsiranje uspesno zavrseno!");
            } else {
                log.error("Parsiranje NIJE uspesno zavrseno!");
            }
        }
    }
}
