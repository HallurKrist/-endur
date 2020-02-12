import java.io.FileReader;

public class Program {
  public static void main( String[] args ) throws Exception
  {
    NanoLexer lexer = new NanoLexer(new FileReader(args[0]));
    System.out.println("i main");
    lexer.init();
    System.out.println("current lexeme is: "+lexer.getLexeme());
    System.out.println("next lexeme is: "+lexer.getNextLexeme());
    lexer.advance();
    System.out.println("current lexeme is: "+lexer.getLexeme());
    System.out.println("next lexeme is: "+lexer.getNextLexeme());

  }

  public static void Parse(int token) 
  {
    System.out.println("token-id er " + token);

  }


}