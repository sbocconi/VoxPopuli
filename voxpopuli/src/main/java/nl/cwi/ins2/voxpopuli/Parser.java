package nl.cwi.ins2.voxpopuli;

/**
 * <p>Title: Vox Populi</p>
 *
 * <p>Description: Automatic Generation of Biased Video Documentaries</p>
 *
 * <p>Copyright: Copyleft</p>
 *
 * <p>Company: CWI</p>
 *
 * @author Stefano Bocconi
 * @version 1.0
 */

import voxpopuli.Rule;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;

public class Parser{

  /*********************
   *  CONSTANTS        *
   *********************/

  final char NOSTATUS = 0;
  final char INAND = 1;
  final char INOR = 2;
  final char INBLOCK = 3;
  final char INRULE = 4;
  final char INPARAMETER1 = 5;
  final char INPARAMETER2 = 6;

  final char NOEVENT = 0;
  final char EOF = 1;
  final char STARTAND = 2;
  final char ENDAND = 3;
  final char STARTOR = 4;
  final char ENDOR = 5;
  final char STARTBLOCK = 6;
  final char ENDBLOCK = 7;
  final char TOKEN = 8;
  final char NEWLINE = 9;
  final char STARTRULE = 10;
  final char ENDRULE = 11;
  final char PARSEP = ENDRULE + 1;
  final char LAST = PARSEP + 1;

  final String sSTARTAND = "START_AND";
  final String sENDAND = "END_AND";
  final String sSTARTOR = "START_OR";
  final String sENDOR = "END_OR";
  final String sSTARTBLOCK = "START_BLOCK";
  final String sENDBLOCK = "END_BLOCK";
  final String sSTARTRULE = "START_RULE";
  final String sENDRULE = "END_RULE";
  final String sEOF = "EOF";
  final String sTOKEN = "TOKEN";
  final String sNEWLINE = "NEWLINE";
  final String sNOEVENT = "NOEVENT";
  final String sPARSEP = "PARSEP";

  final int ORDEPTH = 40;
  final int ORWIDTH = 10;
  /*************************
   *  PSEUDO VARIABLES     *
   *  set once and forever *
   *  with error checking  *
   *************************/

  String EventMessage[];

  /*********************
   *  CLASSES *
   **********************/
  private class NodeStack{

    int Level;
    int Row[];
    boolean bFirstNode;
    ScriptNode[] NodeToLink[];

    public NodeStack(){
      Level = 0;
      bFirstNode = true;

      NodeToLink = new ScriptNode[ORDEPTH][];
      Row = new int[ORDEPTH];
      for( int i = 0; i < ORDEPTH; i++ ){
        Row[i] = -1;
      }
      // Initialize the first layer
      NodeToLink[0] = new ScriptNode[ORWIDTH];
      Row[0] = 0;
    }

    public void IncreaseLevel() throws Exception{
      if( bFirstNode ){
        // We start with an OR: provide a dummy Node as
        // a starting point
        Rule r = aRuleInstance.FindRule("Dummy", null, null );
        NodeToLink[0][0] = new ScriptNode(ScriptNode.LEAF, r);
        bFirstNode = false;
      }
      // Avoid back to back OR blocks leaving one row empty
      if( NodeToLink[Level][0] == null ){
        // Still need to initialize Row
        Row[Level] = -1;
        return;
      }
      Level++;
      if( Level >= ORDEPTH ){
        throw new Exception( "Node Stack Level inconsistent " );
      }

      NodeToLink[Level] = new ScriptNode[ORWIDTH];
      Row[Level] = -1;
    }

    public void DecreaseLevel() throws Exception{
      if( Level <= 0 ){
        throw new Exception( "Node Stack Level inconsistent " );
      }
      for( int i = 0; i < ORWIDTH; i++ ){
        NodeToLink[Level][i] = null;
      }
      // Hoping in garbage collection ....
      NodeToLink[Level] = null;
      Row[Level] = -1;
      Level--;
    }

    public void IncreaseRow() throws Exception{
      Row[Level]++;
      if( Row[Level] >= ORWIDTH ){
        throw new Exception( "Node Stack Row inconsistent " );
      }

    }

    public void DecreaseRow() throws Exception{
      Row[Level]--;
      if( Row[Level] < -1 ){
        throw new Exception( "Node Stack Row inconsistent " );
      }

    }


    public void AddNode( ScriptNode a, int link ){

      if( bFirstNode ){
        bFirstNode = false;
        FirstNode = a;
      }else if( NodeToLink[Level][Row[Level]] != null ){
        // We are putting a node in an already existing column
        NodeToLink[Level][Row[Level]].LinkType = link;
        NodeToLink[Level][Row[Level]].AddScriptNode( a );
      }else if( Level > 0 && Row[Level - 1] >= 0 ){
        // We are putting a node in a new column
        for(int i=0; i<=Row[Level - 1]; i++){
          NodeToLink[Level - 1][i].LinkType = link;
          NodeToLink[Level - 1][i].AddScriptNode( a );
        }
      }
      NodeToLink[Level][Row[Level]] = a;
    }

  }

  /*********************
   *  VARIABLES *
   **********************/
  int LineNumber = 0;
  StringBuffer StatusStack;
  NodeStack Nodes;
  ScriptNode FirstNode;
  RuleInstance aRuleInstance;
  boolean BlockEmpty;
  boolean OREmpty;

  String RuleFound;
  String[] ParA;
  String[] ParB;
  int link;

  /*********************
   *  FUNCTIONS *
   **********************/

  public Parser( RuleInstance a){
    EventMessage = new String[LAST];

    EventMessage[NOEVENT] = new String( sNOEVENT );
    EventMessage[EOF] = new String( sEOF );
    EventMessage[STARTAND] = new String( sSTARTAND );
    EventMessage[ENDAND] = new String( sENDAND );
    EventMessage[STARTOR] = new String( sSTARTOR );
    EventMessage[ENDOR] = new String( sENDOR );
    EventMessage[STARTBLOCK] = new String( sSTARTBLOCK );
    EventMessage[ENDBLOCK] = new String( sENDBLOCK );
    EventMessage[TOKEN] = new String( sTOKEN );
    EventMessage[NEWLINE] = new String( sNEWLINE );
    EventMessage[STARTRULE] = new String( sSTARTRULE );
    EventMessage[ENDRULE] = new String( sENDRULE );
    EventMessage[PARSEP] = new String( sPARSEP );

    StatusStack = new StringBuffer();
    StatusStack.append( NOSTATUS );
    Nodes = new NodeStack();
    aRuleInstance = a;

  }

  private boolean isInToken( int i ){
    return( i >= 'a' && i <= 'z' ) || ( i >= 'A' && i <= 'Z' ) || ( i >= '0' && i <= '9' ) || ( i == '_' ) ||
        ( i == '#' ) || ( i == '/' ) || ( i == '/' ) || ( i == '~' ) || ( i == ':' ) || ( i == '.' );
  }

  private boolean isNewLine( int i ){
    return( i == '\n' );
  }

  private boolean isBlank( int i ){
    return(
        ( i == ' ' ) ||
        ( i == '\t' )
        );
  }

  private boolean mustDisappear( int i ){
    return( i == '\r' );
  }

  private boolean isComment( int i ){
    return( i == '%' );
  }

  private boolean isParamSep( int i ){
    return( i == ',' );
  }


  private boolean isEndOfFile( int i ){
    return( i == -1 );
  }

  private boolean Scanner( BufferedReader /*FileReader*/ file ) throws IOException, Exception{

    boolean TokenStarted = false;
    StringBuffer Token = null;

    char c = 0;
    int i = 0;

    char Event = NOEVENT;

    LineNumber = 1;
    while( true ){

      i = file.read();

      // Skip comments till newline or EOF
      if( isComment( i ) ){
        while( !isEndOfFile( i = file.read() ) && !isNewLine( i ) ){
        }
      }

      if( isEndOfFile( i ) ){

        if( TokenStarted ){
          TokenStarted = false;
        }
        Event = EOF;
      } else if( mustDisappear( i ) ){

      } else if( isNewLine( i ) ){
        if( TokenStarted ){
          TokenStarted = false;
        }
        Event = NEWLINE;

      } else if( isParamSep( i ) ){
        if( TokenStarted ){
          TokenStarted = false;
        }
        Event = PARSEP;
      } else if( isInToken( i ) ){
        if( Token == null ){
          Token = new StringBuffer();
          TokenStarted = true;
        }
        c = ( char )i;
        Token.append( c );
      } else if( isBlank( i ) ){
        if( TokenStarted ){
          TokenStarted = false;
          Event = TOKEN;
        }
      } else{
        throw new Exception( "Unknown character " + i + "at line number " + LineNumber );
      }

      if( Event != NOEVENT ){
        String a = null;
        if( Token != null ){
          a = new String( Token.toString() );
        }
        Token = null;

        if( RuleStateMachine( Event, a ) ){
          break;
        }
        Event = NOEVENT;
      }

    }
    return true;
  }

  private boolean RuleStateMachine( char ev2, String Token ) throws Exception{

    int ev1, pos;

    if( Token != null ){

      if( Token.equals( sSTARTRULE ) ){
        ev1 = STARTRULE;
      } else if( Token.equals( sENDRULE ) ){
        ev1 = ENDRULE;
      } else if( Token.equals( sSTARTAND ) ){
        ev1 = STARTAND;
      } else if( Token.equals( sENDAND ) ){
        ev1 = ENDAND;
      } else if( Token.equals( sSTARTOR ) ){
        ev1 = STARTOR;
      } else if( Token.equals( sENDOR ) ){
        ev1 = ENDOR;
      } else if( Token.equals( sSTARTBLOCK ) ){
        ev1 = STARTBLOCK;
      } else if( Token.equals( sENDBLOCK ) ){
        ev1 = ENDBLOCK;
      } else if( (ev2 == TOKEN) || (ev2 == NEWLINE) || (ev2 == EOF) ){
        ev1 = TOKEN;
      } else{
        throw new Exception( "Unconsistent Event generated " + Token + ", " + ev2 );
      }
    } else if( ev2 == NEWLINE || ev2 == EOF || ev2 == PARSEP){
      ev1 = ev2;
    } else{
      throw new Exception( "Unconsistent Event generated " + Token + ", " + ev2 );
    }

    boolean busy = true;

    while( busy ){
      busy = false;
      char StatusParsing = StatusStack.charAt( StatusStack.length() - 1 );
      switch( StatusParsing ){
        case NOSTATUS:
          switch( ev1 ){
            case NEWLINE:
            case EOF:


              // Handled by ev2, do nothing
              break;
            case NOEVENT:
            case ENDAND:
            case ENDOR:
            case ENDRULE:
            case ENDBLOCK:
            case STARTBLOCK:
            case PARSEP:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );
            case STARTAND:
              StatusStack.append( INRULE );
              StatusStack.append( INAND );
              break;
            case STARTOR:
              StatusStack.append( INRULE );
              busy = true;
              break;
            case STARTRULE:
              StatusStack.append( INRULE );
              break;
            case TOKEN:
              StatusStack.append( INRULE );
              busy = true;
              break;

            default:
              break;
          }
          break;
        case INAND:
          switch( ev1 ){
            case NOEVENT:
            case ENDOR:
            case STARTRULE:
            case EOF:
            case ENDRULE:
            case ENDBLOCK:
            case STARTBLOCK:
            case STARTAND:
            case PARSEP:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );
            case NEWLINE:

              // Handled later by ev2
              break;

            case ENDAND:
              int length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INAND because of the switch
              break;
            case STARTOR:
              StatusStack.append( INOR );
              Nodes.IncreaseLevel();
              OREmpty = true;
              break;

            case TOKEN:

              StatusStack.append( INPARAMETER1 );
              RuleFound = new String(Token);
              ParA = null;
              ParB = null;
              link = ScriptNode.AND;

              if( ev2 == NEWLINE  ){
                busy = true;
                ev1 = NEWLINE;
              }

              break;

            default:
              break;
          }

          break;

        case INPARAMETER1:
          switch( ev1 ){
            case NOEVENT:
            case EOF:
            case ENDAND:
            case ENDBLOCK:
            case STARTRULE:
            case ENDRULE:
            case STARTAND:
            case STARTOR:
            case ENDOR:
            case STARTBLOCK:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );

            case PARSEP:
              int length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INPARAMETER1
              StatusStack.append( INPARAMETER2 );

              break;

            case NEWLINE:
              length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INPARAMETER1

              Rule r = aRuleInstance.FindRule( RuleFound, ParA, null );
              ScriptNode Temp = new ScriptNode( ScriptNode.LEAF, r );
              Nodes.AddNode( Temp, link );


              // Handled also later by ev2
              break;

            case TOKEN:
              if( ParA == null ){
                ParA = new String[1];
                ParA[0] = new String(Token);
              }else{
                int size = ParA.length;
                String[] TempParA = new String[size + 1];
                for( int i=0; i<size; i++ ){
                  TempParA[i] = new String(ParA[i]);
                }
                TempParA[size] = new String(Token);
                ParA = TempParA;
              }
              if( ev2 == NEWLINE  ){
                busy = true;
                ev1 = NEWLINE;
              }

              break;

            default:
              break;
          }

          break;

        case INPARAMETER2:
          switch( ev1 ){
            case NOEVENT:
            case EOF:
            case ENDAND:
            case ENDBLOCK:
            case STARTRULE:
            case ENDRULE:
            case STARTAND:
            case STARTOR:
            case ENDOR:
            case STARTBLOCK:
            case PARSEP:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );


            case NEWLINE:
              int length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INPARAMETER2

              Rule r = aRuleInstance.FindRule( RuleFound, ParA, ParB );
              ScriptNode Temp = new ScriptNode( ScriptNode.LEAF, r );
              Nodes.AddNode( Temp, link );

              // Handled later by ev2
              break;

            case TOKEN:
              if( ParB == null ){
                ParB = new String[1];
                ParB[0] = new String(Token);
              }else{
                int size = ParB.length;
                String[] TempParB = new String[size + 1];
                for( int i=0; i<size; i++ ){
                  TempParB[i] = new String(ParB[i]);
                }
                TempParB[size] = new String(Token);
                ParB = TempParB;
              }

              if( ev2 == NEWLINE  ){
                busy = true;
                ev1 = NEWLINE;
              }

              break;

            default:
              break;
          }
          break;

        case INOR:
          switch( ev1 ){
            case NOEVENT:
            case EOF:
            case ENDAND:
            case ENDBLOCK:
            case STARTRULE:
            case ENDRULE:
            case STARTAND:
            case TOKEN:
            case STARTOR:
            case PARSEP:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );

            case NEWLINE:

              // Handled later by ev2
              break;

            case ENDOR:

              int length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INOR because of the switch

              if( OREmpty ){
                Nodes.DecreaseLevel();
              }else{
                Nodes.IncreaseLevel();
                Nodes.IncreaseRow();
              }
              break;

            case STARTBLOCK:
              StatusStack.append( INBLOCK );
              Nodes.IncreaseRow();
              BlockEmpty = true;
              break;

            default:
              break;
          }

          break;
        case INBLOCK:
          switch( ev1 ){
            case NOEVENT:
            case EOF:
            case STARTBLOCK:
            case STARTRULE:
            case ENDRULE:
            case ENDAND:
            case ENDOR:
            case STARTOR:
            case STARTAND:
            case PARSEP:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );

            case NEWLINE:

              // Handled later by ev2
              break;

            case ENDBLOCK:
              int length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INBLOCK because of the switch
              if( BlockEmpty ){
                Nodes.DecreaseRow();
              }

              break;

            case TOKEN:

              StatusStack.append( INPARAMETER1 );
              RuleFound = new String(Token);
              ParA = null;
              ParB = null;

              link = ScriptNode.OR;

              if( BlockEmpty ){
                link = ScriptNode.OR;
                BlockEmpty = false;
              }else{
                link = ScriptNode.AND;
              }
              OREmpty = false;

              if( ev2 == NEWLINE  ){
                busy = true;
                ev1 = NEWLINE;
              }

              break;

            default:
              break;
          }

          break;
        case INRULE:
          switch( ev1 ){
            case NOEVENT:
            case EOF:
            case ENDOR:
            case STARTBLOCK:
            case ENDBLOCK:
            case ENDAND:
            case STARTRULE:
            case PARSEP:

              // This should not happen
              throw new Exception( "Event " + EventMessage[ev1] + " not allowed in Status " +
                                   (int)StatusParsing + " line number " + LineNumber );

            case NEWLINE:

              // Handled later by ev2
              break;
            case STARTOR:
              StatusStack.append( INOR );
              Nodes.IncreaseLevel();
              OREmpty = true;

              break;
            case STARTAND:
              StatusStack.append( INAND );
              break;
            case TOKEN:

              // Default is AND
              StatusStack.append( INAND );
              busy = true;
              break;

            case ENDRULE:
              int length = StatusStack.length();
              StatusStack.deleteCharAt( length - 1 ); // This must be INRULE because of the switch
              break;

            default:
              break;
          }

          break;
        default:
          break;

      }
    }
    if( ev2 == NEWLINE ){
      LineNumber++;
    } else if( ev2 == EOF ){
      return true;
    }

    return false;
  }


  public ScriptNode ParseRulesFile( String ModuleFileName ) throws Exception{

    File ModuleFile = new File( ModuleFileName );

    BufferedReader fr = new BufferedReader(new FileReader( ModuleFile ));



    try{
      Scanner( fr );
    } catch( IOException e ){
      System.out.println( "IO Error during scanning " + e.toString() );
    } catch( Exception e ){
      System.out.println( "Error during scanning " + e.toString() );
    }

    return FirstNode;
  }

  public ScriptNode ParseRulesString( String theRules ) throws Exception{


    StringReader Rules = new StringReader(theRules);
    BufferedReader fr = new BufferedReader( Rules );



  try{
    Scanner( fr );
  } catch( IOException e ){
    System.out.println( "IO Error during scanning " + e.toString() );
  } catch( Exception e ){
    System.out.println( "Error during scanning " + e.toString() );
  }

  return FirstNode;
}


}
