import java.io.FileReader;

public class Program {
  private static NanoLexer lexer;

  public static void main(final String[] args) throws Exception {

    lexer = new NanoLexer(new FileReader(args[0]));
    lexer.init();

    parse();

  }

  public static void parse() {
    try {
      function();
      // while (lexer.getToken() != 0) {
      //   System.out.println("call function again");
      //   function();
      // }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void function() {
    
    try {
      // System.out.println("in function()");
      // System.out.println("this lexeme: " + lexer.getLexeme());
      // System.out.println("next lexeme: " + lexer.getNextLexeme());
      // System.out.println("this token: " + lexer.getToken());
      // System.out.println("next token: " + lexer.getNextToken());
      // lexer.advance();

      if (name()) {
        lexer.advance();
        if (lexer.getToken() == 40) { //token 40 er "("
          lexer.advance();
          if (name()) {
            lexer.advance();
            if (lexer.getToken() == 44) { //token 44 er ","
              lexer.advance();
              if (name()) {
                lexer.advance();
              } else {
                //TODO: stop program, expected name after ","
              }
            }
          } 

          if (lexer.getToken() == 41) { //token 41 er ")"
            lexer.advance();
            if (lexer.getToken() == 123) { //token 123 er "{"
              lexer.advance();
              if (decl()) {
                lexer.advance();
                if (lexer.getToken() == 59) { //token 59 er ";"
                  lexer.advance();
                } else {
                  //TODO: stop program, expected ";"
                }
              }

              if (expr()) {
                lexer.advance();
                if (lexer.getToken() == 59) { //token 59 er ";"
                  lexer.advance();
                } else {
                  //TODO: stop program, expected ";"
                }
              }

              if (lexer.getToken() == 125) { //token 125 er "}"
                lexer.advance();
              } else {
                //TODO: stop program, expected "}"
              }

            } else {
              //TODO: stop program, expected "{"
            }

          } else {
            //TODO: stop program, expected close param
          }


        } else {
          //TODO: stop program, expected open param
        }
      } else {
        //TODO: stop program, expected name
      }

      


    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private static boolean name() {
    try {
      if (lexer.getToken() == 1003) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      //TODO: handle exception
    }
    System.out.println("not try");
    return false;
  }

  private static boolean decl() {
    try {
      if (lexer.getToken() == 1008) {
        lexer.advance();
        if (name()) {
          lexer.advance();
          if (lexer.getToken() == 44) { //token 44 er ","
            lexer.advance();
            if (name()) {
              lexer.advance();
            } else {
              //TODO: stop program, expected name after ","
            }
          }
          return true;
        } else {
          //TODO: stop program, expected name after var
        }
      } else {
        return false;
      }
    } catch (Exception e) {
      //TODO: handle exception
    }
    return false;
  }

  private static boolean expr() {
    return true;
  }

  private static void ifexpr() {

  }

  private static void body() {

  }


}