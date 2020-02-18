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
            if (lexer.getToken() != 44) {

                // error, expected (
                return;
            }
            // Svigi hefur opnast.
            lexer.advance();

            if (name) {
                lexer.advance();
                while (lexer.getToken() == 44) {
                    // 44 er ','
                    lexer.advance();
                    if (lexer.getToken()) {
                        // error expected name
                        return;
                    }
                    lexer.advance();
                }
            }
            if (lexer.getToken() != 41) {
                // error, expected )
                return;
            }
            // Svigi hefur lokast og við á taka að "{"
            lexer.advance();

            if (lexer.getToken() != 123) {
                // error expected {
                return;
            }
            // Hornklofi hefur opnast.
            lexer.advance();

            while (lexer.getToken() == VAR) {
                lexer.advance();
                if (!decl()) {
                    // error, expected declaration
                    return;
                }
                lexer.advance();
            }
            // Búið er að lesa {decl, ';'}
            // Næst ætti að koma {expr, ';'} , }

            if (!expr()) {
                // error expected expression
                return;
            }






            /*
            if (name())
            {
                lexer.advance();
                if (lexer.getToken() == 40)
                { //token 40 er "("
                    lexer.advance();
                    // XXX
                    while (name())
                    {
                        lexer.advance();
                        if (lexer.getToken() == 44)
                        { //token 44 er ","
                            lexer.advance();
                            if (name())
                            {
                                lexer.advance();
                            } else
                            {
                                //TODO: stop program, expected name after ","
                            }
                        }
                    }
                    if (lexer.getToken() == 41)
                    { //token 41 er ")"
                    // Svigi hefur lokast og hornklofi á að opnast
                        lexer.advance();
                        if (lexer.getToken() == 123)
                        { //token 123 er "{"
                            lexer.advance();
                            // XXX
                            while (decl())
                            {
                                lexer.advance();
                                if (lexer.getToken() == 59)
                                { //token 59 er ";"
                                    lexer.advance();
                                } else
                                {
                                    //TODO: stop program, expected ";"
                                }
                            }
                            // XXX
                            while (expr())
                            {
                                lexer.advance();
                                if (lexer.getToken() == 59)
                                { //token 59 er ";"
                                    lexer.advance();
                                } else
                                {
                                    //TODO: stop program, expected ";"
                                }
                            }
                            if (lexer.getToken() == 125)
                            { //token 125 er "}"
                                lexer.advance();
                            } else
                            {
                                //TODO: stop program, expected "}"
                            }
                        } else
                        {
                            //TODO: stop program, expected "{"
                        }
                    } else
                    {
                        //TODO: stop program, expected close param
                    }
                } else
                {
                    //TODO: stop program, expected open param
                }
            } else
            {
                System.out.println("Expected function name");
            }
            */



        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static boolean name()
    {
        if (lexer.getToken() == NAME)
        {
            throw new Error("Expected name. ")
                } else
        {
            return false;
        }
    
        return false;
    }

    private static boolean decl()
    {
        //lexer.advance();
        if (!name()) {
            throw new Error("Expected name. Token is " + lexer.getToken());
        }
        while (lexer.getToken == 44) {
            // 44 er ','
            lexer.advance();
            if (lexer.getToken() != NAME) {

            }
            lexer.advance();
        }
        if (lexer.getToken() != 59) {
            // error, expected ';'
            return false;
        }
        return true;
    }

    private static boolean expr()
    {
        System.out.println("in expr");
        if (lexer.getToken() == RETURN)
        {
            lexer.advance();
            return expr();
        } else if (name())
        {
            lexer.advance();
            if (lexer.getToken() == 61)
            { //token 61 er "="
                lexer.advance();
                return expr();
            }
        } else
        {
            orexpr();
        }
        return false;

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

    private static boolean ifexpr()
    {
        System.out.println("in ifexpr");
        try
        {
            if (lexer.getToken() == 1001)
            {
                lexer.advance();
                if (lexer.getToken() == 40)
                {
                    lexer.advance();
                    if (expr())
                    {
                        lexer.advance();
                        if (lexer.getToken() == 41)
                        {
                            lexer.advance();
                            if (body())
                            {
                                lexer.advance();
                                if (elsepart())
                                {
                                    return true;
                                } else
                                {
                                    return false;
                                }
                            } else
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }

                } else
                {
                    return false;
                }
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

    private static boolean elsepart()
    {
        System.out.println("in elsepart");
        try
        {
            if (lexer.getToken() == 1007)
            {
                lexer.advance();
                if (body())
                {
                    return true;
                } else
                {
                    return false;
                }
            } else if (lexer.getToken() == 1006)
            {
                lexer.advance();
                if (lexer.getToken() == 40)
                {
                    lexer.advance();
                    if (expr())
                    {
                        lexer.advance();
                        if (lexer.getToken() == 41)
                        {
                            lexer.advance();
                            if (body())
                            {
                                lexer.advance();
                                if (elsepart())
                                {
                                    return true;
                                } else
                                {
                                    return false;
                                }
                            } else
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return false;
                }
            } else
            {
                return true;
            }

        } catch (Exception e)
        {
            //TODO: handle exception
        }
        return true;
    }

    private static boolean body()
    {
        System.out.println("in body");
        try
        {
            if (lexer.getToken() == 123)
            {
                lexer.advance();
                if (expr())
                {
                    lexer.advance();
                    if (lexer.getToken() == 59)
                    {
                        lexer.advance();
                        if (lexer.getToken() == 125)
                        {
                            return true;
                        } else
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return false;
                }
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
}
