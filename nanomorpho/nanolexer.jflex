/**
	JFlex scanner example based on a scanner for NanoLisp.
	Author: Snorri Agnarsson, 2017-2020

	This stand-alone scanner/lexical analyzer can be built and run using:
		java -jar JFlex-full-1.7.0.jar nanolexer.jflex
		javac NanoLexer.java
		java NanoLexer inputfile > outputfile
	Also, the program 'make' can be used with the proper 'makefile':
		make test
 */

import java.io.*;

%%

%public
%class NanoLexer
%unicode
%byaccj

%{

// This part becomes a verbatim part of the program text inside
// the class, NanoLexer.java, that is generated.

// Definitions of tokens:
final static int ERROR = -1;
final static int IF = 1001;
final static int DEFINE = 1002;
final static int NAME = 1003;
final static int LITERAL = 1004;
final static int OPNAME = 1005;
final static int ELSIF = 1006;
final static int ELSE = 1007;
final static int VAR = 1008;
final static int WHILE = 1009;
final static int RETURN = 1010;

final static int AND = 1011;
final static int OR = 1012;
final static int NOT = 1013;

final static int OP1 = 1101; 
final static int OP2 = 1102;
final static int OP3 = 1103;
final static int OP4 = 1104;
final static int OP5 = 1105;
final static int OP6 = 1106;
final static int OP7 = 1107;

// A variable that will contain lexemes as they are recognized:
private static String lexeme;
private static int t1, t2;
private static String l1, l2;

// This runs the scanner:
public static void main( String[] args ) throws Exception
{
     Program prog = new Program();
//      prog.Parse("sumthin");
	NanoLexer lexer = new NanoLexer(new FileReader(args[0]));
	int token = lexer.yylex();
	while( token!=0 )
	{
                prog.Parse(token);
		System.out.println(""+token+": \'"+lexeme+"\'");
		token = lexer.yylex();
	}
     
}

/*
public static Nanolexer init()
{
        return new Nanolexer();
}        

public int advance()
{
        return 0;
}

public int getToken()
{
        return 0;
}

public int getNext()
{
        return 0;
}

public String getLexeme()
{
        return "";
}
*/

%}

  /* Reglulegar skilgreiningar */

  /* Regular definitions */

_DIGIT=[0-9]
_FLOAT={_DIGIT}+\.{_DIGIT}+([eE][+-]?{_DIGIT}+)?
_INT={_DIGIT}+
_STRING=\"([^\"\\]|\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|(\\[0-3][0-7][0-7])|\\[0-7][0-7]|\\[0-7])*\"
_CHAR=\'([^\'\\]|\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|(\\[0-3][0-7][0-7])|(\\[0-7][0-7])|(\\[0-7]))\'
_DELIM=[(){},;=]
_NAME=([:letter:]|{_DIGIT}|[_])+
_BOOLEAN=['!'|'&&'|'||']
_OPNAME=([\+\-*/!%=><\:\^\~&|?])

%%

  /* Lesgreiningarreglur */
  /* Scanning rules */
"&&" {
           lexeme = yytext();
           return AND;
}

"||" {
           lexeme = yytext();
           return OR;
}

"!" {
           lexeme = yytext();
           return NOT;
}

{_DELIM} {
	lexeme = yytext();
	return yycharat(0);
}

{_STRING} | {_FLOAT} | {_CHAR} | {_INT} | null | true | false {
	lexeme = yytext();
	return LITERAL;
}

"if" {
	lexeme = yytext();
	return IF;
}

"elsif" {
        lexeme = yytext();
        return ELSIF;
}

"else" {
       lexeme = yytext();
       return ELSE;
}

"var" {
	lexeme = yytext();
	return VAR;
}

"while" {
        lexeme = yytext();
        return WHILE;
}

"return" {
         lexeme = yytext();
         return RETURN;
}

{_OPNAME} {
          lexeme = yytext();
          int token = -1;
          switch(lexeme)
          {
                case "*": case "/": case "%":
                    token = 1107;
                    break;
                case "+": case "-":
                     token = 1106;
                     break;
                case "<": case ">": case "!": case "=":
                     token = 1105;
                     break;
                case "&":
                     token = 1104;
                     break;
                case "|":
                     token = 1103;
                     break;
                case ":":
                     token = 1102;
                     break;
                case "?": case "~": case "^":
                     token = 1101;
                     break;
           }
          return token;
}

{_NAME} {
	lexeme = yytext();
	return NAME;
}

";;;".*$ {
}

[ \t\r\n\f] {
}

. {
	lexeme = yytext();
	return ERROR;
}


