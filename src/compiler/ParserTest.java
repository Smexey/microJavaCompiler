package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ast.Program;
import java_cup.runtime.Symbol;

import util.Log4JUtils;

public class ParserTest {

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(ParserTest.class);
        File sourceCode;
        if (args.length > 2)
            sourceCode = new File(args[1]);
        else
            sourceCode = new File("testFiles/parserErrorRecoveryTest.mj");

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

            log.info("===========================");
            log.info(prog.toString(""));
            log.info("===========================");

        }
    }
}
