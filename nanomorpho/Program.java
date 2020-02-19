import java.io.FileReader;

public class Program
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
        parse(); // keyra parse-erinn

    }

    private static String err(String tok)
    {
        String e = "Expected " + tok + " . Next token is " + lexer.getToken();
        return e;
    }

    public static void parse()
    {
        try
        {
            function();
            while (lexer.getToken() != 0)
            {
                System.out.println("call function again");
                function();
            }
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void function()
    {
        try
        {
            if (lexer.getToken() != NAME) {
                throw new Error(err("name"));
            }
            lexer.advance();
            if (lexer.getToken() != 40) {
                throw new Error(err("("));
            }
            // Svigi hefur opnast.
            lexer.advance();

            if (lexer.getToken == NAME) {
                lexer.advance();
                while (lexer.getToken == 44) {
                    // 44 er ','
                    lexer.advance();
                    if (lexer.getToken() != NAME) {
                        throw new Error(err("name"));
                    }
                    lexer.advance();
                }
            }

            if (lexer.getToken != 41) {
                throw new Error(err(")"));
            }
            // Svigi hefur lokast og við á taka að "{".
            lexer.advance();

            if (lexer.getToken != 123) {
                throw new Error(err("{"));
            }
            // Hornklofi hefur opnast.
            lexer.advance();

            while (lexer.getToken() == VAR) {
                decl();
                if (lexer.getToken() != 59) {
                    throw new Error(err(";"));
                }
                lexer.advance();
            }
            // Búið er að lesa {decl, ';'}
            // Næst ætti að koma {expr, ';'} , '}'

            expr();
            if (lexer.getToken() != 125)
                throw new Error("}");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

        // decl ='var', NAME, { ',', NAME }
    private static void decl() throws Exception
    {
        lexer.advance();
        if (lexer.getToken() != NAME) {
            throw new Error(err("name"));
        }
        lexer.advance();
        while (lexer.getToken == 44) {
            // 44 er ','
            lexer.advance();
            if (lexer.getToken() != NAME) {
                throw new Error(err("name"));
            }
            lexer.advance();
        }
    }

    // expr = 'return', expr
    //      | NAME, '=', expr
    //      | orexpr
    private static void expr() throws Exception
    {
        System.out.println("in expr");
        int tok = lexer.getToken();
        if (tok == RETURN)
        {
            lexer.advance();
            expr();
            return;
        }
        if (tok == NAME)
        {
            lexer.advance();
            if (lexer.getToken() != 61)
                    throw new Error("=");
            lexer.advance();
            expr();
            return;
        }
        orexpr();
    }

    private static boolean orexpr()
    {
        System.out.println("in orexpr");
        try
        {
            if (andexpr())
            {
                lexer.advance();
                if (lexer.getToken() == 1012)
                {
                    lexer.advance();
                    return orexpr();
                }
                return true;
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean andexpr()
    {
        System.out.println("in andexpr");
        try
        {
            if (notexpr())
            {
                lexer.advance();
                if (lexer.getToken() == 1011)
                {
                    return andexpr();
                }
                return true;
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean notexpr()
    {
        System.out.println("in notexpr");
        try
        {
            if (lexer.getToken() == 1013)
            {
                lexer.advance();
                if (notexpr())
                {
                    return true;
                } else
                {
                    return false;
                }
            } else if (binopexpr1())
            {
                return true;
            } else
            {
                return false;
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr1()
    {
        System.out.println("in binopexpr1");
        try
        {
            if (binopexpr2())
            {
                lexer.advance();
                if (lexer.getToken() == 1101)
                {
                    lexer.advance();
                    if (binopexpr2())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr2()
    {
        System.out.println("in binopexpr2");
        try
        {
            if (binopexpr3())
            {
                lexer.advance();
                if (lexer.getToken() == 1102)
                {
                    lexer.advance();
                    if (binopexpr3())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr3()
    {
        System.out.println("in binopexpr3");
        try
        {
            if (binopexpr4())
            {
                lexer.advance();
                if (lexer.getToken() == 1103)
                {
                    lexer.advance();
                    if (binopexpr4())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr4()
    {
        System.out.println("in binopexpr4");
        try
        {
            if (binopexpr5())
            {
                lexer.advance();
                if (lexer.getToken() == 1104)
                {
                    lexer.advance();
                    if (binopexpr5())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr5()
    {
        System.out.println("in binopexpr5");
        try
        {
            if (binopexpr6())
            {
                lexer.advance();
                if (lexer.getToken() == 1105)
                {
                    lexer.advance();
                    if (binopexpr6())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr6()
    {
        System.out.println("in binopexpr6");
        try
        {
            if (binopexpr7())
            {
                lexer.advance();
                if (lexer.getToken() == 1106)
                {
                    lexer.advance();
                    if (binopexpr7())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static boolean binopexpr7()
    {
        System.out.println("in binopexpr7");
        try
        {
            if (smallexpr())
            {
                lexer.advance();
                if (lexer.getToken() == 1107)
                {
                    lexer.advance();
                    if (smallexpr())
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return false;
    }

    private static void smallexpr() throws Exception
    {
      System.out.println("in smallexpr");

      if (lexer.getToken() == NAME)
      {
        if (lexer.getNextToken() == 40)
        {
          lexer.advance();
          lexer.advance();
          if (lexer.getToken() != 41) {
            expr();
            while (lexer.getToken() == 44) {
              lexer.advance();
              expr();
            }
          }
          if (lexer.getToken() == 41)
          {
            lexer.advance();
          } else
          {
            throw new Error(err(")"));
          }
        } else
        {
          lexer.advance();
        }
      } else if (1100 < lexer.getToken() && lexer.getToken() < 1108)
      {
        opname();
        smallexpr();
      } else if (lexer.getToken() == LITERAL)
      {
        lexer.advance();
      } else if (lexer.getToken() == 40)
      {
        lexer.advance();
        expr();
        if (lexer.getToken() == 41)
        {
          lexer.advance();
        } else
        {
          throw new Error(err(")"));
        }
      } else if (lexer.getToken() == IF)
      {
        ifexpr();
      } else if (lexer.getToken() == WHILE)
      {
        lexer.advance();
        if (lexer.getToken() != 40)
        {
          throw new Error(err("("));
        }
        lexer.advance();
        expr();
        if (lexer.getToken() != 41)
        {
          throw new Error(err(")"));
        }
        lexer.advance();
        body();
      } else
      {
        throw new Error(err("smallexpr"));
      }
    }

    private static void opname() throws Exception
    {
      System.out.println("in opname");

      if (1100 < lexer.getToken() && lexer.getToken() < 1108)
      {
        lexer.advance();
      } else
      {
          err("operator");
      }
    }

    // ifexpr = 'if', '(', expr, ')', body, elsepart;
    private static boolean ifexpr() throws Exception
    {
        System.out.println("in ifexpr");

        lexer.advance();
        if (lexer.getToken != 40)
            throw new Error("(");
        lexer.advance();
        // Búið er að lesa yfir if', '(',

        expr();
        if (lexer.getToken != 41)
            throw new Error(")");
        lexer.advance();
        // Búið er að lesa yfir 'if', '(', expr, ')',

        body();
        elsepart();
        // Búið er að lesa yfir 'if', '(', expr, ')', body, elsepart;
    }

    // elsepart = /* nothing */
    //          | 'else', body
    //          | 'elsif', '(', expr, ')', body, elsepart
    //          ;
    private static void elsepart() throws Exception
    {
        System.out.println("in elsepart");

        int tok = lexer.getToken();
        if (tok == ELSE)
        {
            lexer.advance();
            body();
            return;
        }
        if (tok == ELSIF)
        {
            lexer.advance();
            if (lexer.getToken() != 40)
                throw new Error("(");
            lexer.advance();
            expr();
            if (lexer.getToken() != 41)
                throw new Error(")");
            lexer.advance();
            body();
            elsepart();
        }
        // if tok is neither ELSE nor ELSEIF, do nothing.
    }

    // body = '{', { expr, ';' }, '}';
    private static void body() throws Exception
    {
        System.out.println("in body");

        if (lexer.getToken() != 123)
            throw new Error("{");
        lexer.advance();
        while (lexer.getToken() != 125)
        {
            expr();
            if (lexer.getToken() != 59)
                throw new Error(";");
            lexer.advance();
        }
        lexer.advance();
    }

}
