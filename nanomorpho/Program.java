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

            if (lexer.getToken() == NAME) {
                lexer.advance();
                while (lexer.getToken() == 44) {
                    // 44 er ','
                    lexer.advance();
                    if (lexer.getToken() != NAME) {
                        throw new Error(err("name"));
                    }
                    lexer.advance();
                }
            }

            if (lexer.getToken() != 41) {
                throw new Error(err(")"));
            }
            // Svigi hefur lokast og við á taka að "{".
            lexer.advance();

            if (lexer.getToken() != 123) {
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
        while (lexer.getToken() == 44) {
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

    private static void orexpr() throws Exception
    {
        System.out.println("in orexpr");
        andexpr();
        lexer.advance();
        if (lexer.getToken() == OR){
            lexer.advance();
            orexpr();
        }
    }

    private static void andexpr() throws Exception
    {
        System.out.println("in andexpr");
        notexpr();
        lexer.advance();
        if (lexer.getToken() == AND){
            lexer.advance();
            andexpr();
        }
    }

    private static void notexpr() throws Exception
    {
        System.out.println("in notexpr");
        if (lexer.getToken() == NOT){
            lexer.advance();
            notexpr();
            return;
        }
        binopexpr1();
    }


    private static void binopexpr1() throws Exception
    {
        binopexpr2();
        while(lexer.getToken() == OP1){
            lexer.advance();
            binopexpr2();
        }
    }

    private static void binopexpr2() throws Exception
    {
        binopexpr3();
        if(lexer.getToken() == OP2){
            lexer.advance();
            binopexpr2();
        }
    }

    private static void binopexpr3() throws Exception
    {
        binopexpr4();
        while(lexer.getToken() == OP3){
            lexer.advance();
            binopexpr4();
        }
    }

    private static void binopexpr4() throws Exception
    {
        binopexpr5();
        while(lexer.getToken() == OP4){
            lexer.advance();
            binopexpr5();
        }
    }

    private static void binopexpr5() throws Exception
    {
        binopexpr6();
        while(lexer.getToken() == OP5){
            lexer.advance();
            binopexpr6();
        }
    }

    private static void binopexpr6() throws Exception
    {
        binopexpr7();
        while(lexer.getToken() == OP6){
            lexer.advance();
            binopexpr7();
        }
    }

    private static void binopexpr7() throws Exception
    {
        smallexpr();
        while(lexer.getToken() == OP7){
            lexer.advance();
            smallexpr();
        }
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
    private static void ifexpr() throws Exception
    {
        System.out.println("in ifexpr");

        lexer.advance();
        if (lexer.getToken() != 40)
            throw new Error("(");
        lexer.advance();
        // Búið er að lesa yfir if', '(',

        expr();
        if (lexer.getToken() != 41)
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
