package compiler;
import java_cup.runtime.Symbol;

%%
%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol newSymbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}

	// ukljucivanje informacije o poziciji tokena
	private Symbol newSymbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT


%eofval{
	return newSymbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

//terminali iz mjparser
//skripta da ih updatuje???

"continue"  { return newSymbol(sym.CONTINUE, yytext());}
"void" 		{ return newSymbol(sym.VOID, yytext()); }
"+" 		{ return newSymbol(sym.ADD, yytext()); }
"-" 		{ return newSymbol(sym.SUB, yytext()); }
"*" 		{ return newSymbol(sym.MUL, yytext()); }
"/" 		{ return newSymbol(sym.DIV, yytext()); }
"%" 		{ return newSymbol(sym.MOD, yytext()); }
"==" 		{ return newSymbol(sym.EQUALS, yytext()); }
"!=" 		{ return newSymbol(sym.NEQUALS, yytext()); }
">" 		{ return newSymbol(sym.GRT, yytext()); }
">=" 		{ return newSymbol(sym.GRTEQUAL, yytext()); }
"<" 		{ return newSymbol(sym.LESS, yytext()); }
"<=" 		{ return newSymbol(sym.LESSEQUAL, yytext()); }
"&&" 		{ return newSymbol(sym.AND, yytext()); }
"||" 		{ return newSymbol(sym.OR, yytext()); }
"++" 		{ return newSymbol(sym.INC, yytext()); }
"--" 		{ return newSymbol(sym.DECR, yytext()); }
"[" 		{ return newSymbol(sym.LBRACKET, yytext()); }
"]" 		{ return newSymbol(sym.RBRACKET, yytext()); }
"=" 		{ return newSymbol(sym.EQUAL, yytext()); }
";" 		{ return newSymbol(sym.SEMICOLN, yytext()); }
":" 		{ return newSymbol(sym.DDOTS, yytext()); }
"," 		{ return newSymbol(sym.COMMA, yytext()); }
"." 		{ return newSymbol(sym.DOT, yytext()); }
"(" 		{ return newSymbol(sym.LPAREN, yytext()); }
")" 		{ return newSymbol(sym.RPAREN, yytext()); }
"{" 		{ return newSymbol(sym.LBRACE, yytext()); }
"}"			{ return newSymbol(sym.RBRACE, yytext()); }
"return" 	{ return newSymbol(sym.RETURN, yytext()); }
"program"   { return newSymbol(sym.PROGRAM, yytext()); }
"break"     { return newSymbol(sym.BREAK, yytext());}
"print" 	{ return newSymbol(sym.PRINT, yytext()); }
"else"     	{ return newSymbol(sym.ELSE, yytext());}
"const"     { return newSymbol(sym.CONST, yytext());}
"if"     	{ return newSymbol(sym.IF, yytext());}
"new"     	{ return newSymbol(sym.NEW, yytext());}
"read"     	{ return newSymbol(sym.READ, yytext());}
"switch"    { return newSymbol(sym.SWITCH, yytext());}
"case"     	{ return newSymbol(sym.CASE, yytext());}
"class" 	{ return newSymbol(sym.CLASS, yytext()); }
"extends" 	{ return newSymbol(sym.EXTENDS, yytext()); }
"do" 		{ return newSymbol(sym.DO, yytext()); }
"while" 	{ return newSymbol(sym.WHILE, yytext()); }
"?" 		{ return newSymbol(sym.QMARK, yytext()); }

<YYINITIAL> "//" { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }
<COMMENT> .      { yybegin(COMMENT); }


[0-9]+  { return newSymbol(sym.NUM, new Integer (yytext())); }
(true)|(false) {return newSymbol (sym.BOOLEAN, new Boolean(yytext())); }
(\'.\') {return newSymbol (sym.CHAR, yytext().charAt(1)); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return newSymbol (sym.IDENT, yytext()); }


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }






