import java.io.FileReader;

public class Program 
{
  private static NanoLexer lexer;

  public static void main(final String[] args) throws Exception 
  {

    lexer = new NanoLexer(new FileReader(args[0])); //lesa inn skránna
    lexer.init(); // upphafstilla
    parse(); // keyra parse-erinn

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
      if (name()) 
      {
        lexer.advance();
        if (lexer.getToken() == 40) 
        { //token 40 er "("
          lexer.advance();
          if (name()) 
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
            lexer.advance();
            if (lexer.getToken() == 123) 
            { //token 123 er "{"
              lexer.advance();
              if (decl()) 
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

              if (expr()) 
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

      


    } catch (Exception e) 
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private static boolean name() 
  {
    try 
    {
      if (lexer.getToken() == 1003) 
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

  private static boolean decl() 
  {
    try 
    {
      if (lexer.getToken() == 1008) 
      {
        lexer.advance();
        if (name()) 
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
          return true;
        } else 
        {
          //TODO: stop program, expected name after var
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

  private static boolean expr() 
  {
    try 
    {
      System.out.println("in expr");
      if (lexer.getToken() == 1010) 
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
    } catch (Exception e) 
    {
      //TODO: handle exception
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

  private static boolean smallexpr() 
  {
    System.out.println("in smallexpr");
    try 
    {
      if (lexer.getToken() == 1003) 
      {
        lexer.advance();
        if (lexer.getNextToken() == 40) 
        {
          lexer.advance();
          if (!(lexer.getToken() == 41)) 
          {
            if (expr()) 
            {
              lexer.advance();
              if (lexer.getToken() == 44) 
              {
                return expr();
              } else if (lexer.getToken() == 41) 
              {
                return true;
              } else 
              {
                return false;
              }
            }
            return false;
          }
          if (lexer.getToken() == 41) 
          {
            return true;
          }
        }
        return true;
      } else if (opname()) 
      {
        lexer.advance();
        return smallexpr();
      } else if (lexer.getToken() == 1004) 
      {
        return true;
      } else if (lexer.getToken() == 40) 
      {
        lexer.advance();
        if (expr()) 
        {
          lexer.advance();
          if (lexer.getToken() == 41) 
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
      } else if (ifexpr()) 
      {
        return true;
      } else if (lexer.getToken() == 1009) 
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
      }
    } catch (Exception e) 
    {
      //TODO: handle exception
    }
    return false;
  }

  private static boolean opname() 
  {
    System.out.println("in opname");
    try 
    {
      if (lexer.getToken() == (1101 | 1102 | 1103 | 1104 | 1105 | 1106 | 1107)) 
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