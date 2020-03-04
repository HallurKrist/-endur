// A parser for NanoMorpho based on EBNF in grammar2.txt and ifexpr.txt

import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;

public class Parser
{
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

    private static NanoLexer lexer;

    public static void main(final String[] args) throws Exception
    {
        lexer = new NanoLexer(new FileReader(args[0])); //lesa inn skránna
        lexer.init(); // upphafstilla
        Object[] program = parse(); // keyra parse-erinn
        showProgram(program, 2);
    }

    public static void showProgram(Object[] tree, int indent)
    {
        for (Object obj : tree)
        {
            if (obj instanceof Object[])
            {
                Object[] o = (Object[]) obj;
                System.out.printf("%-" + indent + "s%s","", "[\n");
                showProgram(o, indent+2);
                System.out.printf("%-" + indent + "s%s","", "]\n");
            }
            else
                System.out.printf("%-" + indent + "s%s\n","", obj);
        }
    }

    private static String err(String tok)
    {
        int line = lexer.getLine() + 1;
        int column = lexer.getColumn() + 1;
        String pos = "\nError in line " + line + ", column " + column;
        String e = ".\nExpected " + tok + ". Next lexeme is \'" + lexer.getLexeme() + "\'.";
        return pos + e;
    }

    // The symbol table consists of the following two variables.
    private static int varCount;
    private static HashMap<String,Integer> varTable;

    // Adds a new variable to the symbol table.
    // Throws Error if the variable already exists.
    private static void addVar( String name )
    {
        if( varTable.get(name) != null )
            throw new Error("Variable "+name+" already exists, near line "+lexer.getLine());
        varTable.put(name,varCount++);
    }

    // Finds the location of an existing variable.
    // Throws Error if the variable does not exist.
    private static int findVar( String name )
    {
        Integer res = varTable.get(name);
        if( res == null ) {
            throw new Error("Variable "+name+" does not exist, near line "+lexer.getLine());
        }
        return res;
    }

    public static Object[] parse()

    {
        Vector<Object> program = new Vector<Object>();
        try
        {
            varTable = new HashMap<String, Integer>();
            program.add(function());
            while (lexer.getToken() != 0)
            {
                varTable = new HashMap<String, Integer>();
                varCount = 0;
                program.add(function());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return program.toArray();
    }

    // EBNF: function = NAME, '(', [ NAME, { ',', NAME } ] ')'
    //                             '{', { decl, ';' }, { expr, ';' }, '}';
    // Returns: [name, argcount, varcount, exprs]
    private static Object function()
    {
        try
        {
            if (lexer.getToken() != NAME) {
                throw new Error(err("name"));
            }
            //
            String name = lexer.getLexeme();
            lexer.advance();
            if (lexer.getToken() != '(') {
                throw new Error(err("("));
            }
            // Svigi hefur opnast.
            lexer.advance();

            int argCount = 0;
            if (lexer.getToken() == NAME) {
                addVar(lexer.getLexeme());
                argCount++;
                lexer.advance();
                while (lexer.getToken() == ',') {
                    lexer.advance();
                    if (lexer.getToken() != NAME)
                        throw new Error(err("name"));
                    addVar(lexer.getLexeme());
                    argCount++;
                    lexer.advance();
                }
            }

            if (lexer.getToken() != ')') {
                throw new Error(err(")"));
            }
            // Svigi hefur lokast og við á að taka "{".
            lexer.advance();

            if (lexer.getToken() != '{') {
                throw new Error(err("{"));
            }
            // Hornklofi hefur opnast.
            lexer.advance();
            int varCountBefore = varCount;
            while (lexer.getToken() == VAR) {
                decl();
                if (lexer.getToken() != ';') {
                    throw new Error(err(";"));
                }
                lexer.advance();
            }
            // Búið er að lesa {decl, ';'}
            // Næst ætti að koma {expr, ';'} , '}'

            Vector<Object> expressions = new Vector<Object>();
            while (lexer.getToken() != '}') {
                expressions.add(expr());
                if (lexer.getToken() != ';')
                    throw new Error(err(";"));
                lexer.advance();
            }

            if (lexer.getToken() != '}')
                throw new Error(err("}"));
            lexer.advance();

            return new Object[]{name,
                                argCount,
                                varCount -varCountBefore,
                                expressions.toArray()};
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    // decl ='var', NAME, { ',', NAME };
    private static void decl() throws Exception
    {
        lexer.advance();
        if (lexer.getToken() != NAME) {
            throw new Error(err("name"));
        }
        addVar(lexer.getLexeme());
        lexer.advance();
        while (lexer.getToken() == ',') {
            // 44 er ','
            lexer.advance();
            if (lexer.getToken() != NAME) {
                throw new Error(err("name"));
            }
            addVar(lexer.getLexeme());
            lexer.advance();
        }
    }

    // EBNF: expr = 'return', expr
    //            | NAME, '=', expr
    //            | orexpr;
    // Returns: Object[] of valid expressions.
    //          Return arrays corresponding to above EBNF are
    //            ["RETURN", expr]
    //            ["STORE", pos, expr]
    //            orexpr()
    //          where pos is the location of NAME in the symbol table
    private static Object[] expr() throws Exception
    {
        int tok = lexer.getToken();
        if (tok == RETURN)
        {
            Object[] ret  = new Object[2];
            ret[0] = new String("RETURN");
            lexer.advance();
            ret[1] = expr();
            return ret;
        }
        if (tok == NAME && lexer.getNextToken() == '=')
        {
            Object[] store = new Object[3];
            store[0] = new String("STORE");
            int pos = findVar(lexer.getLexeme());
            store[1] = pos;
            lexer.advance();
            lexer.advance();
            store[2] = expr();
            return store;
        }
        return orexpr();
    }

    // EBNF: orexpr = andexpr, [ '||', orexpr ];
    // Returns: andexpr()
    //            or
    //          ["OR", andexpr(), orexpr()]
    private static Object[] orexpr() throws Exception
    {
        Object[] and = andexpr();
        if (lexer.getToken() == OR){
            Object[] or = new Object[3];
            or[0] = new String("OR");
            or[1] = and;
            lexer.advance();
            or[2] = orexpr();
            return or;
        }
        return and;
    }

    // EBNF: andexpr = notexpr, [ '&&', andexpr ];
    // Returns: notexpr()
    //            or
    //          ["AND", notexpr(), andexpr()]
    private static Object[] andexpr() throws Exception
    {
        Object[] not = notexpr();
        if (lexer.getToken() == AND){
            //Object[] and = new Object[3];
            //and[0] = new String("AND");
            //and[1] = not;
            lexer.advance();
            //and[2] = andexpr();
            // return and;
            return new Object[]{"AND", not, andexpr()};
        }
        return not;
    }

    // EBNF: notexpr = '!', notexpr | binopexpr1;
    // Returns: ["NOT", notexpr()]
    //            or
    //          binopexpr()
    private static Object[] notexpr() throws Exception
    {
        if (lexer.getToken() == NOT){
            //Object[] not = new Object[2];
            //not[0] = new String("NOT");
            lexer.advance();
            //not[1] = notexpr();
            //return not;
            return new Object[]{"NOT", notexpr()};
        }
        return binopexpr(1);
    }

    // Usage: Object[] code = binopexpr(k);
    // Pre:   The lexer is positioned at the beginning
    //        of a binopexpr with priority k.
    //        1 <= k <= 8.
    //        Note that a binopexpr with priority 8 is
    //        considered to be a smallexpr.
    // Post:  The compiler has advanced over the
    //        binopexpr with priority k and code now
    //        contains the intermediate code for that
    //        expression.
    // Note:  If an error occurs we may either write
    //        an error message and stop the program or
    //        we might throw an Error("...") and let
    //        the catcher of the Error write the error
    //        message.
    public static Object[] binopexpr( int k ) throws Exception
    {
        if( k == 8 ) return smallexpr();

        Object[] res = binopexpr(k+1);

        // Handle right associative binary operators
        if( k == 2 )
        {
            if( !isOp(lexer.getToken(),k) ) return res;
            String name = lexer.getLexeme();
            lexer.advance();
            Object[] right = binopexpr(k);
            return new Object[]{"CALL",name,new Object[]{res,right}};
        }

        // Handle left associative binary operators
        while( isOp(lexer.getToken(),k) )
        {
            String name = lexer.getLexeme();
            lexer.advance();
            Object[] right = binopexpr(k+1);
            res = new Object[]{"CALL",name,new Object[]{res,right}};
        }
        return res;
    }

    // Usage: boolean b = isOp(tok,k);
    // Pre:   tok is a token, 1<=k<=7.
    // Post:  b is true if and only if tok
    //        is a token which can be a
    //        binary operation of priority
    //        k.
    public static boolean isOp( int tok, int k )
    {
        switch( tok )
        {
        case OP1: return k==1;
        case OP2: return k==2;
        case OP3: return k==3;
        case OP4: return k==4;
        case OP5: return k==5;
        case OP6: return k==6;
        case OP7: return k==7;
        default:  return false;
        }
    }

    // EBNF: smallexpr = NAME
    //                 | NAME, '(', [ expr, { ',', expr } ], ')'
    //                 | opname, smallexpr
    //                 | LITERAL
    //                 | '(', expr, ')'
    //                 | ifexpr
    //                 | 'while', '(', expr, ')', body;
    // Returns: ["CALL", name, [expr(), expr(), ..., expr()]]
    //          [FETCH, pos]
    //          [TODO]
    //          ["LITERAL", literal]
    //          expr()
    //          ifexpr()
    //          ["WHILE", expr(), body()]
    private static Object[] smallexpr() throws Exception
    {
        if (lexer.getToken() == NAME && lexer.getNextToken() == '(')
        {
            String name = lexer.getLexeme();
            lexer.advance();
            lexer.advance();
            Vector<Object> expressions = new Vector<Object>();
            if (lexer.getToken() != ')') {
                expressions.add(expr());
                while (lexer.getToken() == ',') {
                    lexer.advance();
                    expressions.add(expr());
                }
            }
            if (lexer.getToken() != ')')
                throw new Error(err(")"));
            lexer.advance();
            return new Object[] {"CALL", name, expressions.toArray()};
        }

        if (lexer.getToken() == NAME)
        {
            String name = lexer.getLexeme();
            lexer.advance();
            return new Object[]{"FETCH", findVar(name)};
        }

        if  (1100 < lexer.getToken() && lexer.getToken() < 1108)
        {
            // TODO handle operators
            //opname();
            //smallexpr();
            String op = lexer.getLexeme();
            lexer.advance();
            return new Object[]{"CALL", op, smallexpr()};
        }

        if (lexer.getToken() == LITERAL)
        {
            String literal = lexer.getLexeme();
            lexer.advance();
            return new Object[]{"LITERAL", literal};
        }

        if (lexer.getToken() == '(')
        {
            lexer.advance();
            Object[] expression = expr();
            if (lexer.getToken() != ')')
                throw new Error(err(")"));
            lexer.advance();
            return expression;
        }

        if (lexer.getToken() == IF)
        {
            return ifexpr();
        }

        if (lexer.getToken() == WHILE)
        {
            lexer.advance();
            if (lexer.getToken() != '(')
                throw new Error(err("("));
            lexer.advance();
            Object[] expression = expr();
            if (lexer.getToken() != ')')
                throw new Error(err(")"));
            lexer.advance();
            return new Object[]{"WHILE", expression, body()};
        }

        throw new Error(err("smallexpr"));
    }

    // TODO: handle operators in smallexpr and delete function
    private static void opname() throws Exception
    {
        if (1100 < lexer.getToken() && lexer.getToken() < 1108)
        {
            lexer.advance();
        } else
        {
            err("operator");
        }
    }

    // EBNF: ifexpr = 'if', '(', expr, ')', body, elsepart;
    // Returns: ["IF", expr(), body(), elsepart()]
    private static Object[] ifexpr() throws Exception
    {
        lexer.advance();
        if (lexer.getToken() != '(')
            throw new Error(err("("));
        lexer.advance();
        // Búið er að lesa yfir if', '(',

        Object[] condition = expr();
        if (lexer.getToken() != ')')
            throw new Error(err(")"));
        lexer.advance();
        // Búið er að lesa yfir 'if', '(', expr, ')',

        return new Object[]{"IF", condition, body(), elsepart()};
    }

    // EBNF: elsepart = /* nothing */
    //               | 'else', body
    //               | 'elsif', '(', expr, ')', body, elsepart;
    // Returns:     body()
    //          or  ["IF", expr(), body(), elsepart()]
    //          or  null
    private static Object[] elsepart() throws Exception
    {
        int tok = lexer.getToken();
        if (tok == ELSE)
        {
            lexer.advance();
            return body();
        }
        if (tok == ELSIF)
        {
            lexer.advance();
            if (lexer.getToken() != '(')
                throw new Error(err("("));
            lexer.advance();
            Object[] condition =expr();
            if (lexer.getToken() != ')')
                throw new Error(err(")"));
            lexer.advance();
            return new Object[]{"IF", condition, body(), elsepart()};
        }

        // tok is neither ELSE nor ELSIF
        return null;
    }

    // body = '{', { expr, ';' }, '}';
    private static Object[] body() throws Exception
    {
        if (lexer.getToken() != '{')
            throw new Error(err("{"));
        lexer.advance();
        if (lexer.getToken() == '}')
        {
            lexer.advance();
            return null;
        }
        Vector<Object> expressions = new Vector<Object>();
        while (lexer.getToken() != '}')
        {
            expressions.add(expr());
            if (lexer.getToken() != ';')
                throw new Error(err(";"));
            lexer.advance();
        }
        lexer.advance();
        return new Object[]{"BODY", expressions.toArray()};
    }
}
