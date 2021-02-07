# Docs
## Build
Buildovanje se radi preko vscode build taskova
default build task `generateCode` generise lexer, parser, i syntaxTree

clean zahteva [rm](https://gcc.gnu.org/ "GNU Home")

## Compile
`launch.json` sadrzi launch configuraciju za compiler u kome se podesavaju input argumenti za main i u kome je redirectovan output konzole u logs folder

## Run
Pokretanje se radi preko vscode taskova `dbguJvm` i `runuJvm`, a dissassemble preko `dissassemble`


## Code

#### CounterVisitor.java
prebraja topdown pojave argumenata, uzeto sa laba i generalno beskorisno
#### CodeGenerator.java
Visitorska klasa koja radi generisanje bytecode-a
#### SemanticPass.java
Visitorska klasa koja prolazi i generise semanticko stablo i tipove objektnih cvorova
#### DumpSymbolTableVisitorExt.java
Implementacija dump visitora koji ispisuje sve slucajeve za dati zadatak
#### Test.java
Driver kod koji generise syntax stablo i pokrece prolaze svih visitora
## Test
`test300.mj`, `test301.mj`, `test302.mj`, `test303.mj` su javni testovi

`parserErrorRecoveryTest.mj` ispisuje sve greske greske od kojih se radi oporavak

`semanticTest.mj` testira semantiku svih pokrivenih slucajeva

`tst.mj` testingGrounds 

`codeTest*.mj` su testovi za generisanje koda po nivoima
## Notes
`Log4JUtils.findLoggerConfigFile` trazi config file van classpatha odnosno u config folderu, ovo je uradjeno jer config folder ne bi smeo da bude deo classpatha
