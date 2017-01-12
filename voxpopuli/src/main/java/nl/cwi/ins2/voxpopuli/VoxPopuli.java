package nl.cwi.ins2.voxpopuli;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.AccessDeniedException;
import java.util.*;

import java.util.Hashtable;

import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;

public class VoxPopuli{

  /*********************
   *  CLASSES *
   **********************/

  private class RhetType{
    private String Name;
    private int index;
    RhetType( String a, int b ){
      if( a != null ){
        this.Name = new String( a );
      } else{
        this.Name = new String( "" );
      }
      this.index = b;
    }

    RhetType( RhetType a ){
      if( a != null && a.Name != null ){
        this.Name = new String( a.Name );
      } else{
        this.Name = new String( "" );
      }
      this.index = a.index;
    }
  }

  private class LinkDescription{
    private RhetType SubjectChange;
    private RhetType ObjectChange;
    private RhetType ModifierChange;
    private RhetType PredicateChange;
    LinkDescription( RhetType a, RhetType b, RhetType c, RhetType d ){
      if( a != null ){
        SubjectChange = RhetActStrings[a.index];
      } else{
        SubjectChange = RhetActStrings[SELF];
      }
      if( b != null ){
        ModifierChange = RhetActStrings[b.index];
      } else{
        ModifierChange = RhetActStrings[SELF];
      }
      if( c != null ){
        PredicateChange = RhetActStrings[c.index];
      } else{
        PredicateChange = RhetActStrings[SELF];
      }
      if( d != null ){
        ObjectChange = RhetActStrings[d.index];
      } else{
        ObjectChange = RhetActStrings[SELF];
      }
    }

    LinkDescription( LinkDescription a ){
      SubjectChange = a.SubjectChange;
      ObjectChange = a.ObjectChange;
      ModifierChange = a.ModifierChange;
      PredicateChange = a.PredicateChange;
    }

    public boolean equals( LinkDescription a ){

      return SubjectChange.equals( a.SubjectChange ) &&
          ObjectChange.equals( a.ObjectChange ) &&
          ModifierChange.equals( a.ModifierChange ) &&
          PredicateChange.equals( a.PredicateChange );

    }
  }

  private class ArgumentationStructure{
    RhetType ToulminType;
    RhetType[] NotAllowedLinks;
    boolean ReplaceInterviewee;
    ArgumentationStructure( RhetType a, RhetType[] b, boolean c ) throws Exception{
      if( a == null || b == null ){
        throw new Exception( "Object null in VerbalStatement copy constructor " );
      }
      // Attention: copy because we use always the same
      this.ToulminType = a;
      this.NotAllowedLinks = new RhetType[b.length];
      for( int i = 0; i < b.length; i++ ){
        // Attention: copy because we use always the same
        this.NotAllowedLinks[i] = b[i];
      }
      this.ReplaceInterviewee = c;
    }
    /*
        ArgumentationStructure(ArgumentationStructure a){
          this.ToulminType = a.ToulminType;
          this.AllowedLinks = new RhetType[a.AllowedLinks.length];
          for( int i=0; i<a.AllowedLinks.length; i++ ){
            this.AllowedLinks[i] = a.AllowedLinks[i];
          }
          this.ReplaceInterviewee = a.ReplaceInterviewee;
        }
     */
  }

  private class WeightClass{
    String RelationName;
    int Weight;
    WeightClass( String a, int b ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in WeightClass copy constructor " );
      } else{
        RelationName = new String( a );
        Weight = b;
      }
    }
  }


  /* Data collected for the interface */

  // single video segment
  private class VideoSegment{
    String BeginFrame;
    String EndFrame;
    String FileName;
    String Description;
    String Language;
    String Quality;
    String TransIn;
    String TransOut;
    // Images to superimpose
    Hashtable ImageArray;
    VideoSegment(){};
    VideoSegment( VideoSegment a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in VideoSegment copy constructor " );
      } else{
        if( a.BeginFrame != null ){
          BeginFrame = new String( a.BeginFrame );
        }
        if( a.EndFrame != null ){
          EndFrame = new String( a.EndFrame );
        }
        if( a.FileName != null ){
          FileName = new String( a.FileName );
        }
        if( a.Description != null ){
          Description = new String( a.Description );
        }
        if( a.Language != null ){
          Language = new String( a.Language );
        }
        if( a.Quality != null ){
          Quality = new String( a.Quality );
        }
        if( a.TransIn != null ){
          TransIn = new String( a.TransIn );
        }
        if( a.TransOut != null ){
          TransOut = new String( a.TransOut );
        }
        if( a.ImageArray != null ){
          ImageArray = new Hashtable( a.ImageArray );
        }
      }
    }
  }

  private class MyArrayList extends ArrayList{
    MyArrayList(){
      super();
    }

    MyArrayList( MyArrayList a ){
      super( ( ArrayList )a );
    }

    public MyArrayList invert(){
      MyArrayList b = new MyArrayList();

      for( int i = this.size() - 1; i >= 0; i-- ){
        b.add( this.get( i ) );
      }
      return b;
    }

    public boolean contains( LinkDescription a ){
      for( int i = 0; i < this.size(); i++ ){
        LinkDescription b = ( LinkDescription )this.get( i );
        if( b.equals( a ) ){
          return true;
        }
      }
      return false;
    }

    public boolean contains( Link a ){
      for( int i = 0; i < this.size(); i++ ){
        Link b = ( Link )this.get( i );
        if( b.StatementId.equals( a.StatementId ) ){
          return true;
        }
      }
      return false;
    }

    public boolean contains( ConstructedStatement a ){
      for( int i = 0; i < this.size(); i++ ){
        ConstructedStatement b = ( ConstructedStatement )this.get( i );
        if( b.equals( a ) ){
          return true;
        }
      }
      return false;
    }

    public boolean contains( RhetType a ){
      for( int i = 0; i < this.size(); i++ ){
        LinkDescription b = ( LinkDescription )this.get( i );
        if( b.SubjectChange.equals( a ) ||
            b.ModifierChange.equals( a ) ||
            b.PredicateChange.equals( a ) ||
            b.ObjectChange.equals( a )
            ){
          return true;
        }
      }
      return false;
    }
  }

  private class Link{
    String StatementId;
    MyArrayList LinkType;
    Link(){};
    Link( VerbalStatement a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in Link copy constructor " );
      } else{
        this.StatementId = a.Id;
      }
    }

    Link( ConstructedStatement a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in Link copy constructor " );
      } else{
        this.LinkType = new MyArrayList( a.LinkType );
      }
    }
  }

  private class VerbalStatement{
    String Id;
    String Subject;
    String SubjectDescription;
    String Modifier;
    String ModifierDescription;
    String Predicate;
    String PredicateDescription;
    /*
        String Object;
        String ObjectDescription;
     */
    VideoSegment[] Segments;
    boolean Explicit;

    // This hash table contains object of type Link
    MyArrayList ConnectedStatements;
    ToulminNode ParentNode;
    Partecipant Claimer;
    int EthosRating;
    VerbalStatement(){};
    VerbalStatement( VerbalStatement a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in VerbalStatement copy constructor " );
      } else{
        if( a.Id != null ){
          this.Id = new String( a.Id );
        }
        if( a.Subject != null ){
          this.Subject = new String( a.Subject );
        }
        if( a.SubjectDescription != null ){
          this.SubjectDescription = new String( a.SubjectDescription );
        }
        if( a.Modifier != null ){
          this.Modifier = new String( a.Modifier );
        }
        if( a.ModifierDescription != null ){
          this.ModifierDescription = new String( a.ModifierDescription );
        }
        if( a.Predicate != null ){
          this.Predicate = new String( a.Predicate );
        }
        if( a.PredicateDescription != null ){
          this.PredicateDescription = new String( a.PredicateDescription );
        }
        /*
                if( a.Object != null ){
                  this.Object = new String( a.Object );
                }
                if( a.ObjectDescription != null ){
                  this.ObjectDescription = new String( a.ObjectDescription );
                }
         */
        if( a.Claimer != null ){
          this.Claimer = new Partecipant( a.Claimer );
        }
        this.Explicit = a.Explicit;
        if( a.Segments != null ){
          this.Segments = new VideoSegment[a.Segments.length];
          for( int i = 0; i < a.Segments.length; i++ ){
            this.Segments[i] = new VideoSegment( a.Segments[i] );
          }
        }
      }
    }
  };

  private class ConstructedStatement{
    String Subject;
    String SubjectDescription;
    String Modifier;
    String ModifierDescription;
    String Predicate;
    String PredicateDescription;
    /*
        String Object;
        String ObjectDescription;
     */
    MyArrayList LinkType;
    RhetType ToulminType;
    public boolean equals( ConstructedStatement a ){
      boolean result = true;

      if( ( a.Subject != null && !a.Subject.equals( this.Subject ) ) ||
          ( a.Modifier != null && !a.Modifier.equals( this.Modifier ) ) ||
          ( a.Predicate != null && !a.Predicate.equals( this.Predicate ) )
          ){
        result = false;
      }
      return result;

    }

    //ConstructedStatement(){};
    ConstructedStatement( VerbalStatement a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in ConstructedStatement copy constructor " );
      } else{

        if( a.Subject != null ){
          this.Subject = new String( a.Subject );
        }
        if( a.SubjectDescription != null ){
          this.SubjectDescription = new String( a.SubjectDescription );
        }
        if( a.Modifier != null ){
          this.Modifier = new String( a.Modifier );
        }
        if( a.ModifierDescription != null ){
          this.ModifierDescription = new String( a.ModifierDescription );
        }
        if( a.Predicate != null ){
          this.Predicate = new String( a.Predicate );
        }
        if( a.PredicateDescription != null ){
          this.PredicateDescription = new String( a.PredicateDescription );
        }
        /*
                if( a.Object != null ){
                  this.Object = new String( a.Object );
                }
                if( a.ObjectDescription != null ){
                  this.ObjectDescription = new String( a.ObjectDescription );
                }
         */
        if( a.ParentNode != null ){
          // The same is used everywhere
          this.ToulminType = a.ParentNode.ToulminType;
        }
        this.LinkType = new MyArrayList();
      }
    }

    ConstructedStatement( ConstructedStatement a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in ConstructedStatement copy constructor " );
      } else{
        if( a.Subject != null ){
          this.Subject = new String( a.Subject );
        }
        if( a.SubjectDescription != null ){
          this.SubjectDescription = new String( a.
                                                SubjectDescription );
        }
        if( a.Modifier != null ){
          this.Modifier = new String( a.Modifier );
        }
        if( a.ModifierDescription != null ){
          this.ModifierDescription = new String( a.
                                                 ModifierDescription );
        }
        if( a.Predicate != null ){
          this.Predicate = new String( a.Predicate );
        }
        if( a.PredicateDescription != null ){
          this.PredicateDescription = new String(
              a.PredicateDescription );
        }
        /*
                if( a.Object != null ){
                  this.Object = new String( a.Object );
                }
                if( a.ObjectDescription != null ){
                  this.ObjectDescription = new String( a.
                                                       ObjectDescription );
                }
         */
        if( a.LinkType == null ){
          this.LinkType = new MyArrayList();
        } else{
          this.LinkType = new MyArrayList( a.LinkType );
        }
      }
    }
  };

  private class ImageConcept{
    String Concept;
    String ConceptDescription;
    // These two are to be able to know where to place the pictures
    String BeginFrame;
    String EndFrame;
  }

  private class Image{
    String FileName;
    String BeginFrame;
    String EndFrame;
    String TransIn;
    String TransOut;
    String Description;
  }

  private class ToulminNode{
    RhetType ToulminType;
    VerbalStatement[] theStatements;
    boolean StatementsinAND;
    String LogicType;
    // Link to the containing interview
    Interview theInterview;
    ToulminNode(){};
    ToulminNode( ToulminNode a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in ToulminNode copy constructor " );
      } else{
        if( a.ToulminType != null ){
          // Copy because it is always the same structure
          this.ToulminType = a.ToulminType;
        }
        if( a.theStatements != null ){
          this.theStatements = new VerbalStatement[a.theStatements.length];
          for( int i = 0; i < a.theStatements.length; i++ ){
            this.theStatements[i] = new VerbalStatement( a.theStatements[i] );
          }
        }
        this.StatementsinAND = a.StatementsinAND;
        this.theInterview = a.theInterview;
        if( a.LogicType != null ){
          this.LogicType = new String( a.LogicType );
        } else{
          this.LogicType = new String( "" );
        }
      }
    }
  }

  private class Argument{
    ToulminNode[] Nodes;
    // Nested arguments
    Argument[] Arguments;
    // This type refers to the Toulmin role of the whole argument structure,
    // it does not apply at the top level, but only for every recursion.
    // This means that if this is a nested argument, this is its type.
    RhetType ToulminType;
    Argument(){};
    Argument( Argument a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in Argument copy constructor " );
      } else{
        if( a.Nodes != null ){
          this.Nodes = new ToulminNode[a.Nodes.length];
          for( int i = 0; i < a.Nodes.length; i++ ){
            this.Nodes[i] = new ToulminNode( a.Nodes[i] );
          }
        }
        if( a.Arguments != null ){
          this.Arguments = new Argument[a.Arguments.length];
          for( int i = 0; i < a.Arguments.length; i++ ){
            this.Arguments[i] = new Argument( a.Arguments[i] );
          }
        }
        if( a.ToulminType != null ){
          this.ToulminType = a.ToulminType;
        }
      }
    }
  }

  private class Partecipant{
    String Id;
    String Description;
    int EthosScore;
    Partecipant(){};
    Partecipant( Partecipant a ) throws Exception{
      if( a == null ){
        throw new Exception( "Object null in Partecipant copy constructor " );
      } else{
        if( a.Id != null ){
          this.Id = new String( a.Id );
        }
        if( a.Description != null ){
          this.Description = new String( a.Description );
        }
        this.EthosScore = a.EthosScore;
      }
    }
  }

  private class Question{
    String QuestionId;
    String QuestionText;
    public Question( String a, String b ){
      if( a != null ){
        QuestionId = new String( a );
      } else{
        QuestionId = new String( "" );
      }
      if( b != null ){
        QuestionText = new String( b );
      } else{
        QuestionText = new String( "" );
      }
    }
  }

  private class Interview{
    String Id;
    String Position;
    String Topic;
    // There can be more point made in an interview
    Argument[] Arguments;
    VideoSegment[] Segments;
    Partecipant Interviewee;
    Question thequestion;
  }

  /*********************
   *  CONSTANTS *
   **********************/

// parameters for the smil file
// dimension for the video area
  //static int Width = 320;
  //static int Height = 240;

  static int Width = 640;
  static int Height = 480;

  static String TitleFileName = "sigla.rm";
  static int TitleWidth = 320;
  static int TitleHeight = 240;
  static String TitleBeginFrame = "00:00:00.000";
  static String TitleEndFrame = "00:00:03.000";
  static String TitleTransIn = null;
  static String TitleTransOut = null;

  // height for the caption area
  static int STHeight = 60;

  // parameters for the transition in SMIL
  static String TransDur = "0.5s";
  static String StartProgress = "0.0";
  static String EndProgress = "1.0";
  static String Direction = "forward";
  static String TransName = "trans";
  static String TransType = "fade";
  static String TransSubtype = "";
  static String Extension = ".rm";

  // Do we use Real extensions?
  private boolean RealExt = true;

  static String RealTextServer =
      "http://media.cwi.nl:8080/cocoon/cuypers/oai/text?Content=";

//  static String RealVideoLocation = "http://media.cwi.nl/IWA/real/";
//  static String MediaLocation = "file:///net/aries/export/data1/IWA/real/";

  static String VideoLocation = "";
//  static String VideoLocation = "../real/";
//  static String VideoLocation = "http://media.cwi.nl:8080/IWA/real/";
  static final String RDFSfile = "VoxPopuli.rdfs";
  static final String RDFfile = "VoxPopuli.rdf";

  static String PictureLocation = "/ufs/sbocconi/links/IWA/figs/";
//  static String PictureLocation = "http://media.cwi.nl:8080/IWA/real/";

  // typography
  static int FontSize = 4;
  static String FgColor = "ffffff";
  static String BgColor = "000000";
  static String TextAlign = "center";

  // These values has to be -1 and 1 because of Heider's triangle
  static final int Attack = -1;
  static final int Support = 1;
  static final int Positive = 1;
  static final int Negative = -1;

  // These are just constants
  static final int ATTACK = 0;
  static final int SUPPORT = ATTACK + 1;
  static final int GENERALIZE = SUPPORT + 1;
  static final int ASSOCIATE = GENERALIZE + 1;
  static final int SELF = ASSOCIATE + 1;
  static final int SPECIALIZE = SELF + 1;

  static final int CLAIM = 0;
  static final int BACKING = CLAIM + 1;
  static final int CONCESSION = BACKING + 1;
  static final int CONDITION = CONCESSION + 1;
  static final int DATA = CONDITION + 1;
  static final int WARRANT = DATA + 1;
  static final int EXAMPLE = WARRANT + 1;

  // Order is important !!!!!!
  private final RhetType[] RhetActStrings = {
      new RhetType( "Attack", ATTACK ), new RhetType( "Support", SUPPORT ), new RhetType( "Generalize", GENERALIZE ),
      new RhetType( "Associate", ASSOCIATE ), new RhetType( "Self", SELF ), new RhetType( "Specialize", SPECIALIZE )
  };

  // Order is important !!!!!!
  private final RhetType[] ToulminStrings = {
      new RhetType( "Claim", CLAIM ), new RhetType( "Backing", BACKING ), new RhetType( "Concession", CONCESSION ),
      new RhetType( "Condition", CONDITION ), new RhetType( "Data", DATA ), new RhetType( "Warrant", WARRANT ),
      new RhetType( "Example", EXAMPLE )
  };

  static String Namespaces =
      "VoxPopuli = <!http://www.cwi.nl/~media/ns/IWA/VoxPopuli.rdfs#>, " +
      "MediaClipping = <!http://www.w3.org/2001/SMIL20/MediaClipping#>, " +
      "BasicMedia = <!http://www.w3.org/2001/SMIL20/BasicMedia#>, " +
      "rdfs = <!http://www.w3.org/2000/01/rdf-schema#>, " +
      "rdf = <!http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

  /*********************
   *  VARIABLES *
   **********************/
  private Repository theRepository;
  private ArgumentationStructure[] StrictAttack = null;
  private ArgumentationStructure[] StrictSupport = null;
  private ArgumentationStructure[] GeneralAttack = null;
  private ArgumentationStructure[] GeneralSupport = null;

  private WeightClass[] Weights = null;

  private String[] ContrTopicArray = null; // List of possible controversial topics

  private Question[] QuestionArray = null; // List of interviews with topic and position

  private Interview[] InterviewArray = null; // List of interviews with topic and position

  //private int Position;               // Position of the current interview in the Array
  private Hashtable Statements = null; // List of all statements

  private Hashtable AnswerConceptArray = null; // List of Concepts supporting or opposing

  private ArrayList SegmentArray = null; // List of Segments to show

  private Hashtable SegmentImagesArray = null; // List of Segments with Images to show

  // DEBUG LEVELS

  private PrintStream Query = null;
  private PrintStream Debug1 = null;
  private PrintStream Debug2 = null;
  private PrintStream ResultOut = null;
  private PrintStream Locator = null;
  private PrintStream Log1 = null;
  private PrintStream Log2 = null;
  private PrintStream Err = null;

  /*********************
   *  FUNCTIONS *
   **********************/

  private static int StrToInt( String s ){
    int n = 0;

    for( int i = 0; i < s.length(); i++ ){
      n = n * 10 + ( s.charAt( i ) - '0' );

    }
    return n;
  }

  public static void main( String[] args ){

//    int len = Array.getLength(args);
//    int len = args.length;

    try{

      if( ( args[5].equals( "L" ) && args.length != 7 ) ||
          ( args[5].equals( "M" ) && args.length != 11 ) ||
          ( args[5].equals( "C" ) && args.length != 7 ) ||
          ( args[5].equals( "A" ) && args.length != 8 ) ){
        Usage();
      }

      VoxPopuli voxpopuli = new VoxPopuli( args[0], args[1], args[2], args[3], args[4] );

      voxpopuli.SetDataStructure();

//setting the debug streams

      voxpopuli.Query = new PrintStream( new FileOutputStream( new File( "/dev/null" ) ) );
      //voxpopuli.Query = System.err;

      //voxpopuli.Debug1 = new PrintStream(new FileOutputStream(new File("/dev/null")));
      voxpopuli.Debug1 = System.err;

      voxpopuli.Debug2 = new PrintStream( new FileOutputStream( new File( "/dev/null" ) ) );
      //voxpopuli.Debug2 = System.err;

      //voxpopuli.ResultOut = new PrintStream(new FileOutputStream(new File("/dev/null")));
      voxpopuli.ResultOut = System.out;

      //voxpopuli.Locator = new PrintStream(new FileOutputStream(new File("/dev/null")));
      voxpopuli.Locator = System.err;

      //voxpopuli.Log1 = new PrintStream(new FileOutputStream(new File("/dev/null")));
      voxpopuli.Log1 = System.err;

      voxpopuli.Log2 = new PrintStream( new FileOutputStream( new File( "/dev/null" ) ) );
      //voxpopuli.Log2 = System.err;


      //voxpopuli.Err = new PrintStream(new FileOutputStream(new File("/dev/null")));
      voxpopuli.Err = System.err;

      /*
             // All the controversial topics have been read now and the user should be
             // able to choose one. For the moment we pick the first since there is only
             // one, but there must be a user interface here.

             voxpopuli.DummySelectTopic();
             voxpopuli.GetInterviewByTopics(voxpopuli.ContrTopicArray[0]);
             // We retrieved all interviews with the topic, now there must be a selection
             // of the one we want to use. We call DummySelectInterviews()
             voxpopuli.DummySelectInterviews();

             String UserType = new String( "Pacifist" );

             //voxpopuli.PathosProcessInterview(UserType, position, Attack);

       */

      if( args[5].equals( "C" ) ){
        boolean Result;
        if( args[6].equals( "A" ) ){
          Result = voxpopuli.CheckConcepts( true );
        } else if( args[6].equals( "NA" ) ){
          Result = voxpopuli.CheckConcepts( false );
        } else{
          throw new Exception( "Incorrect option for Check Concepts " );
        }
        if( Result == true ){
          voxpopuli.ResultOut.println( "Concepts are OK " );
        } else{
          voxpopuli.ResultOut.println( "Concepts are not OK " );
        }
      } else{

        if( !voxpopuli.ReadTopics() ){
          throw new Exception( "Error reading topics" );
        }

        voxpopuli.GetAllInterviews();

        // This loop reads all interviews and builds the data structure
        for( int i = 0; i < Array.getLength( voxpopuli.InterviewArray ); i++ ){
          voxpopuli.ReadInterviewData( i );
        }

        // This loop links all related statements together
        for( int i = 0; i < Array.getLength( voxpopuli.InterviewArray ); i++ ){

          voxpopuli.Locator.println( "Processing Interview " + i );
          voxpopuli.ProcessInterview( i );
        }

        // The following prints all the statements with connections
        voxpopuli.PrintStatements();

        String theArgumentation;
        boolean keep = true;
        boolean NodeOn = true;
        boolean orig = false;
        String filename = null;

        if( args[5].equals( "A" ) ){
          // Automatic for all options
          String[] ArgArr = {
              "GA", "SA", "GS", "SS"
          };
          boolean[] BoolArr = {
              true, false};

          filename = args[6] + "_" + args[5] + "_" + "L";
          voxpopuli.Locator.println( "Calculating Longest Path " );
          //voxpopuli.LongestPath( filename );

          // Do we have to generate an original file as well?
          if( args[7].equals( "O" ) ){
            orig = true;
          } else if( args[7].equals( "NO" ) ){
            orig = false;
          } else{
            throw new Exception( "Wrong option for original " );
          }
          for( int i = 0; i < ArgArr.length; i++ ){
            for( int ii = 0; ii < BoolArr.length; ii++ ){
              for( int iii = 0; iii < BoolArr.length; iii++ ){
                filename = args[6] + "_" + args[5] + "_" + ArgArr[i] + "_" +
                    ( BoolArr[ii] == true ? "K" : "NK" ) + "_" + ( BoolArr[iii] == true ? "N" : "S" );

                voxpopuli.Locator.println( "Calculating Multiple Voices " + filename );
                voxpopuli.Multiplevoices( ArgArr[i], BoolArr[ii], BoolArr[iii], orig, filename );

              }
            }
          }
        } else{
          if( args[5].equals( "L" ) ){
            filename = args[6] + "_" + args[5];
            // Calculate the longest path
            voxpopuli.Locator.println( "Calculating Longest Path " );
            //voxpopuli.LongestPath( filename );

          } else if( args[5].equals( "M" ) ){
            // Type of argumentation
            theArgumentation = new String( args[8] );

            // Do we have to generate an original file as well?
            if( args[7].equals( "O" ) ){
              orig = true;
            } else if( args[7].equals( "NO" ) ){
              orig = false;
            } else{
              throw new Exception( "Wrong option for original " );
            }

            if( args[9].equals( "K" ) ){
              // Keep statement
              keep = true;
            } else if( args[9].equals( "NK" ) ){
              // Do not keep statetement
              keep = false;
            } else{
              throw new Exception( "Unknown sub-option for strategy " );
            }
            if( args[10].equals( "N" ) ){
              // Node Based
              NodeOn = true;
            } else if( args[10].equals( "S" ) ){
              // Statement based
              NodeOn = false;
            } else{
              throw new Exception( "Unknown sub-option for strategy " );
            }

            filename = args[6] + "_" + args[5] + "_" + args[8] + "_" + args[9] + "_" + args[10];
            voxpopuli.Locator.println( "Calculating Multiple Voices " + filename );
            voxpopuli.Multiplevoices( theArgumentation, keep, NodeOn, orig, filename );

          } else{
            throw new Exception( "Unknown option for strategy " );
          }
        }
      }
    } catch( Exception e ){
      e.printStackTrace();
      System.err.println( "Error:  " + e.toString() );
    }
  }

  private boolean PrintStatements(){

    for( Enumeration e = Statements.elements(); e.hasMoreElements(); ){
      VerbalStatement aStatement = ( VerbalStatement )e.nextElement();
      Log1.println( "Statement " + aStatement.Id + " " + aStatement.SubjectDescription + " " +
                    aStatement.ModifierDescription + " " +
                    aStatement.PredicateDescription );
      if( aStatement.ConnectedStatements != null ){
        PrintConnectedStatement( aStatement.ConnectedStatements, true );
      } else{
        Log1.println( "\tNo Link " );
      }
    }
    return true;
  }

  private boolean LongestPath( String filename ) throws Exception{

    // We select the longest chain of statements, not very logical
    // but it gives an idea of how many connected statements there are
    int max = 0;
    ArrayList current = new ArrayList();
    ArrayList maxchain = null;
    for( Enumeration e = Statements.elements(); e.hasMoreElements(); ){
      VerbalStatement aStatement = ( VerbalStatement )e.nextElement();
      int i = GetMaxLength( aStatement, current );
      if( i != current.size() ){
        throw new Exception( "Size of Max Chain not correct " );
      }
      if( i > max ){
        max = i;
        Debug1.println( "Length of the chain " + i );
        maxchain = new ArrayList( current );
      }
      // reset the iterative hash table for a new iteration
      current = new ArrayList();
    }

    CreateSegmentArray( maxchain, true, true );
    CreateSMILOutput( filename );

    return true;
  }

  private boolean Multiplevoices( String theArg, boolean keep, boolean NodeOn, boolean original, String filename ) throws
      Exception{

    int ReplacedRoles = 0, max = 0, index[] = {
         -1, -1};
    Argument Voices = null, Temp;
    boolean result = false;
    ArgumentationStructure[] theArgumentation = null;

    if( theArg.equals( "GA" ) ){
      // General Attack
      theArgumentation = GeneralAttack;
    } else if( theArg.equals( "SA" ) ){
      // Strict Attack
      theArgumentation = StrictAttack;
    } else if( theArg.equals( "GS" ) ){
      // General Support
      theArgumentation = GeneralSupport;
    } else if( theArg.equals( "SS" ) ){
      // Strict Support
      theArgumentation = StrictSupport;
    } else{
      throw new Exception( "Unknown sub-option for strategy " );
    }

    // Look for interview with different roles
    for( int i = 0; i < Array.getLength( InterviewArray ); i++ ){
      if( InterviewArray[i].Arguments != null ){
        for( int ii = 0; ii < InterviewArray[i].Arguments.length; ii++ ){
          // The following function looks whether the statements contained in the
          // Argument can be replaced and constructs a new argument with the replaced
          // statements.
          Temp = new Argument( InterviewArray[i].Arguments[ii] );
          ReplacedRoles = ReplaceableRoles( InterviewArray[i].Arguments[ii], true, theArgumentation, Temp, keep, NodeOn );

          if( ReplacedRoles > max ){
            max = ReplacedRoles;
            Voices = new Argument( Temp );
            index[0] = i;
            index[1] = ii;
          }
        }
      }
    }
    if( max > 0 ){

      if( original == true ){
        // Clear the array segment
        CreateSegmentArray( null, false, true );
        result = OrderArgument( InterviewArray[index[0]].Arguments[index[1]], true );
        result = result && CreateSMILOutput( filename + "_ORIG" );
      } else{
        result = true;
      }
      // Clear the array segment
      CreateSegmentArray( null, false, true );
      result = result && OrderArgument( Voices, true );
      result = result && CreateSMILOutput( filename );
    }

    return result;
  }

  private int ReplaceableRoles( Argument anArgument, boolean filter, ArgumentationStructure[] theArgumentation,
                                Argument Replaced, boolean keep, boolean NodeOn ) throws Exception{

    int NrReplaceableRoles = 0, NrReplaceableNodes = 0;
    ArgumentationStructure a = null;
    boolean expunge = false;

    if( anArgument.Nodes == null && anArgument.Arguments == null ){
      return NrReplaceableRoles;
    }
    if( anArgument.Nodes != null ){

      for( int i = 0; i < anArgument.Nodes.length; i++ ){
        ToulminNode aNode = anArgument.Nodes[i];
        boolean NewNode = true;
        if( aNode.theStatements != null ){

          // Look for the name of the Toulmin role to apply the rule
          a = null;
          for( int iii = 0; iii < theArgumentation.length; iii++ ){
            if( theArgumentation[iii].ToulminType.index == aNode.ToulminType.index ){
              a = theArgumentation[iii];
              break;
            }
          }
          if( a == null ){
            throw new Exception( "Toulmin Node type not known " + aNode.ToulminType );
          }

          for( int ii = 0; ii < aNode.theStatements.length; ii++ ){
            // This statement is the one contained in the Argumentation
            VerbalStatement aStatement = aNode.theStatements[ii];

            if( aStatement.ConnectedStatements != null ){

              for( int j = 0; j < aStatement.ConnectedStatements.size(); j++ ){
                Link aLink = ( Link )aStatement.ConnectedStatements.get( j );
                // This statement is the one linked to the one contained in the Argumentation
                VerbalStatement bStatement = ( VerbalStatement )Statements.get(
                    aLink.StatementId );
                boolean present = false;
                for( int index1 = 0; !present && ( index1 < Replaced.Nodes.length ); index1++ ){
                  for( int index2 = 0; !present && ( index2 < Replaced.Nodes[index1].theStatements.length ); index2++ ){
                    if( Replaced.Nodes[index1].theStatements[index2].Id.equals( bStatement.Id ) ){
                      present = true;
                    }
                  }
                }
                // The statement is already in the selected ones
                if( present ){
                  continue;
                }

                // Only do filtering if flag is true, otherwise count the statement
                // as replaceable
                if( filter == true ){

                  // If the claimer is the same as the one of the main statement, do not count it
                  // if flag is true
                  if( ( a.ReplaceInterviewee == true ) &&
                      ( bStatement.Claimer.Id.equals( aStatement.Claimer.Id ) ) ){
                    continue;
                  }
                  expunge = false;
                  for( int iv = 0; iv < a.NotAllowedLinks.length; iv++ ){
                    if( aLink.LinkType.contains( a.NotAllowedLinks[iv] ) ){
                      expunge = true;
                      break;
                    }
                  }
                  if( expunge == true ){
                    // This continue skips the increment below
                    continue;
                  } else{

                    int index = -1;
                    VerbalStatement[] Temp = null;
                    if( !keep && NewNode ){
                      Replaced.Nodes[i].theStatements = new VerbalStatement[0];
                    }

                    Temp = new VerbalStatement[Replaced.
                        Nodes[i].theStatements.length];

                    for( int v = 0;
                         v < Replaced.Nodes[i].theStatements.length; v++ ){
                      // We copy the content before overwriting it.
                      Temp[v] = new VerbalStatement( Replaced.Nodes[i].
                          theStatements[v] );

                      // Look for the position of the statement to replace
                      if( keep && aStatement.Id.equals( Temp[v].Id ) ){
                        index = v;
                      }
                    }
                    if( keep && index == -1 ){
                      throw new Exception( "Statement to replace not found " );
                    }

                    // Reallocate the array to make place for the new statement
                    Replaced.Nodes[i].theStatements = new VerbalStatement[
                        Replaced.Nodes[i].theStatements.length + 1];

                    // We keep the original and add the replacement
                    int gap = 0;
                    for( int v = 0;
                         v < Replaced.Nodes[i].theStatements.length; v++ ){
                      // Add the statement after the original one
                      if( ( keep && ( v == ( index + 1 ) ) ) ||
                          ( !keep && ( v == Replaced.Nodes[i].theStatements.length - 1 ) ) ){
                        Replaced.Nodes[i].theStatements[v] = new
                            VerbalStatement( bStatement );
                        gap = 1;
                      } else if( keep || ( ( !keep ) && ( v != Replaced.Nodes[i].theStatements.length - 1 ) ) ){
                        Replaced.Nodes[i].theStatements[v] = new
                            VerbalStatement( Temp[v - gap] );
                      } else{
                        throw new Exception( "Code should not get here " );
                      }
                    }
                  }
                }
                if( NewNode ){
                  NrReplaceableNodes++;
                  NewNode = false;
                }
                NrReplaceableRoles++;
              }
              // Here we could order the statements
              // Replaced.Nodes[i].theStatements
            }
          }
        }
      }
    }
    if( anArgument.Arguments != null ){
      for( int i = 0; i < anArgument.Arguments.length; i++ ){
        NrReplaceableRoles = NrReplaceableRoles + ReplaceableRoles( anArgument.Arguments[i], filter,
            theArgumentation, Replaced.Arguments[i], keep, NodeOn );
      }
    }
    if( NodeOn ){
      return NrReplaceableNodes;
    }
    return NrReplaceableRoles;
  }

  // This function creates an ordered sequence of segments so that segments are
  // responding to each other
  private boolean CreateSegmentArray( ArrayList StatementsChain, boolean key, boolean clear ) throws Exception{

    int index = 0;
    VerbalStatement aStatement;
    String Msg = "";

    if( SegmentArray == null ){
      SegmentArray = new ArrayList();
    } else if( clear == true ){
      SegmentArray.clear();
    }
    if( StatementsChain == null ){
      return false;
    }

    for( int i = 0; i < StatementsChain.size(); i++ ){
      if( key == true ){
        if( ( aStatement = ( VerbalStatement )Statements.get( ( ( Link )StatementsChain.get( i ) ).StatementId ) ) == null ){
          throw new Exception( "Statement not found " );
        }
        Msg = new String( PrintLinkList( ( ( Link )StatementsChain.get( i ) ).LinkType ) );
      } else{
        aStatement = ( VerbalStatement )StatementsChain.get( i );
      }

      for( int ii = 0; ii < aStatement.Segments.length; ii++ ){
        aStatement.Segments[ii].Description = new String( aStatement.Segments[ii].Description + Msg );
        SegmentArray.add( index, aStatement.Segments[ii] );
        index++;
      }
    }
    return true;
  }

  /*
   // This was an attempt to save the structure to file,
   // but all classes have to implement the interface
   // java.io.Serializable and I do not have control on
   // sesame classes unless I modify the sources ...
     private void Save() throws Exception{
    ObjectOutputStream OUTStream = null;
    try {
      FileOutputStream file = new FileOutputStream("VoxPopuli");
      OUTStream = new ObjectOutputStream(file);
      OUTStream.writeObject(Statements);
      OUTStream.flush();
    }catch (IOException e) {
      throw new Exception("Error saving to file " + e.toString());
    }
    finally {
      if( OUTStream != null ){
        OUTStream.close();
      }
    }
     }
     private boolean Read() throws Exception{
    ObjectOutputStream INStream;
    try {
      FileOutputStream file = new FileInputStream("c:\\userInfo.ser");
      INStream = new ObjectInputStream(file);
      INStream.writeObject(user);
      INStream.flush();
    }catch (java.io.IOException IOE) {
      labelOutput.setText("IOException");
    }
    finally {
      out.close();
    }
     }
   */

  public VoxPopuli( String ServerString, String RepositoryString,
                    String theNameSpaceString, String local, String RDFDirectory ) throws Exception{

    try{
      // This is required by Sesame
      System.setProperty( "org.xml.sax.driver",
                          "org.apache.xerces.parsers.SAXParser" );

      SetRepository( local, ServerString, RepositoryString, theNameSpaceString, RDFDirectory );

    } catch( Exception e ){
      throw new Exception( "Error in constructing VoxPopuli " + e.toString() );
    }

  }

  private void SetDataStructure() throws Exception{

    Weights = new WeightClass[]{
        new WeightClass( "veryImportantFactor", 3 ),
        new WeightClass( "importantFactor", 2 ),
        new WeightClass( "excludedFactor", -1 ),
    };

    // Sanity Check on Data Structure
    for( int i = 0; i < RhetActStrings.length; i++ ){
      if( RhetActStrings[i].index != i ){
        throw new Exception( "RhetActStrings not correct " );
      }
    }
    for( int i = 0; i < ToulminStrings.length; i++ ){
      if( ToulminStrings[i].index != i ){
        throw new Exception( "ToulminStrings not correct " );
      }
    }

    // ATTACK SUPPORT GENERALIZE ASSOCIATE
    // CLAIM BACKING CONCESSION CONDITION DATA WARRANT

    StrictSupport = new ArgumentationStructure[]{
        new ArgumentationStructure( ToulminStrings[CLAIM], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[BACKING], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[CONCESSION], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[CONDITION], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[DATA], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[WARRANT], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[EXAMPLE], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true )
    };

    GeneralSupport = new ArgumentationStructure[]{
        new ArgumentationStructure( ToulminStrings[CLAIM], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[BACKING], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[CONCESSION], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[ASSOCIATE],
                                    RhetActStrings[SELF]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[CONDITION], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[ASSOCIATE],
                                    RhetActStrings[SELF]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[DATA], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[WARRANT], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[EXAMPLE], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false )
    };

    StrictAttack = new ArgumentationStructure[]{
        new ArgumentationStructure( ToulminStrings[CLAIM], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[BACKING], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[CONCESSION], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[CONDITION], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[DATA], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[WARRANT], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true ),
        new ArgumentationStructure( ToulminStrings[EXAMPLE], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , true )
    };

    GeneralAttack = new ArgumentationStructure[]{
        new ArgumentationStructure( ToulminStrings[CLAIM], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[ASSOCIATE],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[SELF]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[BACKING], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[ASSOCIATE],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[SELF]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[CONCESSION], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[CONDITION], new RhetType[]{RhetActStrings[ATTACK],
                                    RhetActStrings[ASSOCIATE]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[DATA], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[ASSOCIATE],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[SELF]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[WARRANT], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[ASSOCIATE],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[SELF]}
                                    , false ),
        new ArgumentationStructure( ToulminStrings[EXAMPLE], new RhetType[]{RhetActStrings[SUPPORT],
                                    RhetActStrings[SELF],
                                    RhetActStrings[GENERALIZE],
                                    RhetActStrings[SPECIALIZE],
                                    RhetActStrings[ASSOCIATE]}
                                    , false )
    };
  }

  private static void Usage() throws Exception{
    System.out.println(
        "Usage: <Sesame server> <Repository name> <nameSpace> <local=true/false> <path-to-RDF(S)file>\n" +
        "<main-option> <outputfile> <strategy-options-to-be-documented>" );
    throw new Exception( "Program not called correctly" );
  }

  private boolean SetRepository( String local, String ServerString, String RepositoryString,
                                 String theNameSpaceString, String RDFDirectory ) throws Exception{
    boolean Found = false;

    theRepository = new Repository();

    /*
      FIRST
        File sysConfFile = new File("./IWA.conf");
        LocalService service = Sesame.getService(sysConfFile);
        RepositoryList a = service.getRepositoryList();
        theRepository.Client = service.getRepository("IWA");
     SECOND
        RepositoryConfig repConfig = new RepositoryConfig("IWA", "Interview with America");
        SailConfig syncSail = new SailConfig("org.openrdf.sesame.sailimpl.sync.SyncRdfSchemaRepository");
        SailConfig memSail = new RdfSchemaRepositoryConfig();
        repConfig.addSail(syncSail);
        repConfig.addSail(memSail);
        repConfig.setWorldReadable(true);
        repConfig.setWorldWriteable(true);
        LocalService service = Sesame.getService();
        theRepository.Client = service.createRepository(repConfig);
     */

    try{
      if( local.equals( "true" ) ){

        RepositoryConfig repConfig = new RepositoryConfig( RepositoryString );

        SailConfig syncSail = new SailConfig( "org.openrdf.sesame.sailimpl.sync.SyncRdfSchemaRepository" );

        SailConfig memSail = new RdfSchemaRepositoryConfig();

        repConfig.addSail( syncSail );
        repConfig.addSail( memSail );

        repConfig.setWorldReadable( true );
        repConfig.setWorldWriteable( true );
        LocalService service = Sesame.getService();
        theRepository.Client = service.createRepository( repConfig );

        File myRDFData = new File( RDFDirectory + RDFSfile );

        boolean verifyData = true;
        AdminListener myListener = new StdOutAdminListener();
        theRepository.Client.addData( myRDFData, theNameSpaceString, RDFFormat.RDFXML, verifyData, myListener );

        myRDFData = new java.io.File( RDFDirectory + RDFfile );
        theRepository.Client.addData( myRDFData, theNameSpaceString, RDFFormat.RDFXML, verifyData, myListener );

        theRepository.SesameURL = new String( "In Memory" );

      } else{
        // Set the URL of the Sesame server and create the client to talk to it
        java.net.URL sesameServerURL = new java.net.URL( ServerString );
        SesameService service = Sesame.getService( sesameServerURL );
        //service.login("sbocconi", "Apriti");
        //RepositoryList a = service.getRepositoryList();
        theRepository.Client = service.getRepository( RepositoryString );
        theRepository.SesameURL = new String( ServerString );
      }

      theRepository.RepositoryId = new String( RepositoryString );
      theRepository.NameSpace = new String( theNameSpaceString );
      Found = true;

    } catch( java.net.MalformedURLException e ){
      throw new Exception( "Wrong URL" + e.toString() );
    } catch( java.io.IOException e ){
      throw new Exception( "Problem in accessing repository " + e.toString() );
    }

    return Found;
  }

  private boolean CreateSMILOutput( String OutFilename ) throws Exception{
    PrintWriter OutFile = null;
    try{
      if( !OutFilename.equals( "out" ) ){
        // Add the correct extension
        OutFilename = new String( OutFilename + ".smil" );

        OutFile = new PrintWriter( new FileOutputStream( OutFilename ) );
        // write it to standard out
      } else{
        OutFile = new PrintWriter( System.out );
      }
    } catch( IOException e ){
      Err.print( "Error opening file " + e.toString() );
    }

    OutFile.print(
        "<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>\n" +
        "<smil   xmlns=\"http://www.w3.org/2001/SMIL20/Language\" " +
        ( RealExt == true ?
          "xmlns:rn=\"http://features.real.com/2001/SMIL20/Extensions\">\n" : "" ) +
        "<head>\n" +
        "<layout>\n" +
        "<root-layout width=\"" + Width + "\" height=\"" + ( Height + STHeight ) +
        "\"/>\n" +
        "<region id=\"video-region\" left=\"0\" top=\"0\" width=\"" + Width +
        "\" height=\"" + Height + "\"/>\n" +
        "<region id=\"title-region\" left=\"" + ( Width - TitleWidth ) / 2 + "\" top=\"" + ( Height - TitleHeight ) / 2 +
        "\" width=\"" + TitleWidth + "\" height=\"" + TitleHeight + "\"/>\n" +
        "<region id=\"subtitle-region\" left=\"0\" top=\"" + Height +
        "\" width=\"" + Width +
        "\" height=\"" + STHeight + "\"/>\n" +
        "</layout>\n" +
        "<transition id=\"" + TransName + "\" type=\"" + TransType + "\"" +
        ( !( TransSubtype.equals( "" ) ) ? " subtype=\"" + TransSubtype + "\"" : "" ) +
        " dur=\"" + TransDur + "\"" +
        " startProgress=\"" + StartProgress + "\"" +
        " endProgress=\"" + EndProgress + "\"" +
        " direction=\"" + Direction + "\"" +
        "/>" +
        "</head>\n" +
        "<body>\n" +
        "<seq>\n" );

    // The title (workaround RealOne problem
    if( TitleFileName != null ){
      OutFile.print( "<video region=\"title-region\" src=\"" + VideoLocation +
                     TitleFileName +
                     "\" " +
                     "clipBegin=\"" + TitleBeginFrame + "\" " +
                     "clipEnd=\"" + TitleEndFrame + "\" " +
                     "clip-begin=\"" + TitleBeginFrame +
                     "\" " +
                     "clip-end=\"" + TitleEndFrame + "\" " +
                     ( TitleTransIn != null ? "transIn=\"" + TitleTransIn + "\" " : "" ) +
                     ( TitleTransOut != null ? "transOut=\"" + TitleTransOut + "\" " : "" ) +
                     "fill=\"transition\" />\n" );
    }

    if( !CreateSegmentMontage( OutFile ) ){
      throw new Exception( "Error creating SMIL File Segment Montage " );
    }

    if( !CreateSegmentSequence( OutFile ) ){
      throw new Exception( "Error creating SMIL File Segment Sequence " );
    }

    OutFile.print( "</seq>\n" + "</body>\n" + "</smil>\n" );
    OutFile.flush();

    return true;
  }

  private boolean CreateSegmentSequence( PrintWriter OutFile ){

    if( SegmentArray == null ){
      return true;
    }

    for( int i = 0; i < SegmentArray.size(); i++ ){

      String STDuration = TimeDifference( ( ( VideoSegment )SegmentArray.get( i ) ).EndFrame,
                                          ( ( VideoSegment )SegmentArray.get( i ) ).BeginFrame );

      OutFile.print( "<par>\n" );

//    OutFile.print("<text id=\"subtitle" + i + "\" region=\"subtitle-region\" begin=\"0\" src=\"" +
      OutFile.print( "<text region=\"subtitle-region\" src=\"" +
                     RealTextServer + ( ( VideoSegment )SegmentArray.get( i ) ).Description.replaceAll( " ", "%20" ) +
                     "&amp;width=" + Width +
                     "&amp;height=" + STHeight + "&amp;duration=" + STDuration +
                     "&amp;fontsize=" + FontSize + "&amp;fgcolor=%23" + FgColor +
                     "&amp;bgcolor=%23" + BgColor + "&amp;text-align=" +
                     TextAlign + "&amp;ext=.rt\" " +
                     "begin=\"0\" dur=\"" + STDuration + "\" " +
                     ( ( ( VideoSegment )SegmentArray.get( i ) ).TransIn != null ?
                       "transIn=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).TransIn + "\" " : "" ) +
                     ( ( ( VideoSegment )SegmentArray.get( i ) ).TransOut != null ?
                       "transOut=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).TransOut + "\" " : "" ) +
                     ">\n" +
                     "<param name=\"vAlign\" value=\"" + TextAlign + "\"/>\n" +
                     "</text>\n" );

//    OutFile.print("<video id=\"video" + i + "\" region=\"video\" src=\"" + aSegment.Filename +
      OutFile.print( "<video region=\"video-region\" src=\"" + VideoLocation +
                     ( ( VideoSegment )SegmentArray.get( i ) ).FileName + Extension +
                     "\" " +
                     "clipBegin=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).BeginFrame + "\" " +
                     "clipEnd=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).EndFrame + "\" " +
                     "clip-begin=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).BeginFrame +
                     "\" " +
                     "clip-end=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).EndFrame + "\" " +
                     ( ( ( VideoSegment )SegmentArray.get( i ) ).TransIn != null ?
                       "transIn=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).TransIn + "\" " : "" ) +
                     ( ( ( VideoSegment )SegmentArray.get( i ) ).TransOut != null ?
                       "transOut=\"" + ( ( VideoSegment )SegmentArray.get( i ) ).TransOut + "\" " : "" ) +
                     " />\n" );

      OutFile.print( "</par>\n" );
    }

    return true;
  }

  private boolean CreateSegmentMontage( PrintWriter OutFile ){

    if( SegmentImagesArray == null ){
      return true;
    }
    for( Enumeration eSegment = SegmentImagesArray.elements();
         eSegment.hasMoreElements(); ){
      VideoSegment aSegment = ( VideoSegment )eSegment.nextElement();
      String STDuration = TimeDifference( aSegment.EndFrame, aSegment.BeginFrame );

      OutFile.print( "<par>\n" );

      //OutFile.print("<text id=\"subtitle" + i + "\" region=\"subtitle-region\" begin=\"0\" src=\"" +
      OutFile.print( "<text region=\"subtitle-region\" begin=\"0\" src=\"" +
                     RealTextServer + aSegment.Description.replaceAll( " ", "%20" ) +
                     "&amp;width=" + Width +
                     "&amp;height=" + STHeight + "&amp;duration=" + STDuration +
                     "&amp;fontsize=" + FontSize + "&amp;fgcolor=%23" + FgColor +
                     "&amp;bgcolor=%23" + BgColor + "&amp;text-align=" +
                     TextAlign + "&amp;ext=.rt\" " +
                     "transIn=\"" + aSegment.TransIn + "\" " +
                     "transOut=\"" + aSegment.TransOut + "\" " +
                     "dur=\"" + STDuration + "\">\n" +
                     "<param name=\"vAlign\" value=\"" + TextAlign + "\"/>\n" +
                     "</text>\n" );

      //OutFile.print("<video id=\"video" + i + "\" region=\"video\" src=\"" + aSegment.Filename +
      OutFile.print( "<video region=\"video-region\" src=\"" + VideoLocation +
                     aSegment.FileName +
                     "\" " +
                     "clipBegin=\"" + aSegment.BeginFrame + "\" " +
                     "clipEnd=\"" + aSegment.EndFrame + "\" " +
                     "clip-begin=\"" + aSegment.BeginFrame +
                     "\" " +
                     "clip-end=\"" + aSegment.EndFrame + "\" " +
                     "transIn=\"" + aSegment.TransIn + "\" " +
                     "transOut=\"" + aSegment.TransOut + "\" " +
                     ( aSegment.ImageArray.size() > 0 ? "fill=\"transition\" " :
                       "" ) +
                     " />\n" );

      if( aSegment.ImageArray != null ){
        for( Enumeration eImage = aSegment.ImageArray.elements();
             eImage.hasMoreElements(); ){
          Image theImage = ( Image )eImage.nextElement();
          String ImageDuration = TimeDifference( theImage.EndFrame,
                                                 theImage.BeginFrame );

          String ImageStart = TimeDifference( theImage.BeginFrame,
                                              aSegment.BeginFrame );

          //OutFile.print("<img id=\"img" + (z + i*z) + "\" region=\"video\" src=\"" +
          OutFile.print( "<img region=\"video\" src=\"" +
                         PictureLocation + theImage.FileName + "\" " +
                         "begin=\"" + ImageStart + "s\" " +
                         "dur=\"" + ImageDuration + "s\" " +
                         "transIn=\"" + theImage.TransIn + "\" " +
                         "transOut=\"" + theImage.TransOut + "\" " +
                         " />\n" );
          /*
           (!eImage.hasMoreElements() ? "fill=\"hold\" " :
                                     "fill=\"transition\" ") +
           */
        }
      }

      OutFile.print( "</par>\n" );
    }

    return true;
  }

  private String TimeDifference( String End, String Begin ){
    return( ConvertToDSec( End ) - ConvertToDSec( Begin ) ) / 10.0 + "";
  }

  private long ConvertToDSec( String TimeString ){

    String a;
    long DSecs = 0;

    try{
      a = TimeString.substring( 0, 1 );
      long Hours = StrToInt( TimeString.substring( 0, 2 ) );
      long Mins = StrToInt( TimeString.substring( 3, 5 ) );
      long Secs = StrToInt( TimeString.substring( 6, 8 ) );
      DSecs = StrToInt( TimeString.substring( 9, 10 ) );

      DSecs = DSecs + 10 * ( Secs + 60 * ( Mins + 60 * ( Hours ) ) );
    } catch( StringIndexOutOfBoundsException e ){
      Err.print( "String not long enough: " + TimeString + ",error " +
                 e.toString() );
    }

    return DSecs;
  }

  private String ConvertToDate( long theDSec ){

    long Hours = theDSec / 36000;
    long Mins = ( theDSec - 36000 * Hours ) / 600;
    long Secs = ( theDSec - 36000 * Hours - Mins * 600 ) / 10;
    long DSecs = ( theDSec - 36000 * Hours - Mins * 600 - Secs * 10 );

    String SDate = ( ( Hours < 10 ) ? "0" + Hours : "" + Hours ) + ":" +
        ( ( Mins < 10 ) ? "0" + Mins : "" + Mins ) + ":" +
        ( ( Secs < 10 ) ? "0" + Secs : "" + Secs ) + "." + DSecs;
    return SDate;
  }

  private boolean ReadTopics(){
    QueryResultsTable Results;
    boolean result = false;

    //
    String queryString =
        "select ControversialTopic " +
        "from " +
        "{ControversialTopic} <rdf:type> {<VoxPopuli:ControversialTopic>} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );
    try{
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      ContrTopicArray = new String[Results.getRowCount()];

      for( int i = 0; i < Results.getRowCount(); i++ ){
        String Res = new String( Results.getValue( i, 0 ).toString() );
        Debug2.println( "Query Result: " + Res );
        ContrTopicArray[i] = new String( Res );
      }

      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( QueryEvaluationException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( IOException e ){
      Err.println( "Error retrieving Topics " + e.toString() );
    }

    return result;
  }

  private boolean RetrieveQuestions(){
    QueryResultsTable Results;
    boolean result = false;

    // We read all the interview that have the specified topic
    String queryString =
        "select Questions, QLabel " +
        "from " +
        "{Question} <rdf:type> {<voxpopuli:Question>}, " +
        "{Question} <rdfs:label> {QLabel} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );
    try{
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      QuestionArray = new Question[Results.getRowCount()];

      for( int i = 0; i < Results.getRowCount(); i++ ){
        Question aQuestion = new Question( Results.getValue( i, 0 ).toString(),
                                           Results.getValue( i, 1 ).toString() );
        QuestionArray[i] = aQuestion;
        Query.println( "Query Result: " + aQuestion.QuestionId + " " +
                       aQuestion.QuestionText );
      }

      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( QueryEvaluationException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( IOException e ){
      Err.println( "Error retrieving Topics " + e.toString() );
    }

    return result;
  }

  private boolean GetInterviewByTopics( String Topic ){
    QueryResultsTable Results;
    boolean result = false;

    // We read all the interview that have the specified topic
    String queryString =
        "select Interview, ProCon, Interviewee, Description " +
        "from " +
        "{Interview} <VoxPopuli:partecipant> {Interviewee}; " +
        "<VoxPopuli:opinion> {X} <VoxPopuli:topic> {<!" + Topic +
        ">}, " +
        "{X} <serql:directType> {ProCon}, " +
        "{Interviewee} <VoxPopuli:description> {Description}, " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );
    try{
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      InterviewArray = new Interview[Results.getRowCount()];

      for( int i = 0; i < Results.getRowCount(); i++ ){
        Interview aSelectedInterview = new Interview();
        aSelectedInterview.Id = new String( Results.getValue( i, 0 ).toString() );
        aSelectedInterview.Position = new String( Results.getValue( i, 1 ).
                                                  toString() );
        aSelectedInterview.Topic = new String( Topic );
        aSelectedInterview.Interviewee = new Partecipant();
        aSelectedInterview.Interviewee.Id = new String( Results.getValue( i, 2 ).toString() );
        aSelectedInterview.Interviewee.Description = new String( Results.getValue( i, 3 ).
            toString() );
        Query.println( "Query Result: " + aSelectedInterview.Id + " " +
                       aSelectedInterview.Position + " " + Topic );
        InterviewArray[i] = aSelectedInterview;
      }

      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( QueryEvaluationException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( IOException e ){
      Err.println( "Error retrieving Topics " + e.toString() );
    }

    return result;
  }

  private boolean GetAllInterviews(){
    QueryResultsTable Results;
    boolean result = false;

    // We read all the interviews
    String queryString =
        "select Interview, ProCon, Topic, Question, QuestionText, Interviewee, Description " +
        "from " +
        "{Interview} <serql:directType> {<VoxPopuli:InterviewSegment>}; " +
        // This is because Interview segment is considered by Sesame
        // (because of common properties) the same class as VerbalStatement
        //"{Interview} <VoxPopuli:partecipant> {}, " +
        "<VoxPopuli:hasQuestion> {Question}; " +
        "<VoxPopuli:partecipant> {Interviewee}, " +
        "{Interviewee} <VoxPopuli:description> {Description}, " +
        "{Question} <VoxPopuli:text> {QuestionText}, " +
        "[{Interview} <VoxPopuli:opinion> {X} <VoxPopuli:topic> {Topic}, " +
        "{X} <serql:directType> {ProCon}] " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );
    try{
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      InterviewArray = new Interview[Results.getRowCount()];

      for( int i = 0; i < Results.getRowCount(); i++ ){
        Interview aSelectedInterview = new Interview();
        aSelectedInterview.Id = new String( Results.getValue( i, 0 ).toString() );
        aSelectedInterview.Position = new String( Results.getValue( i, 1 ) != null ?
                                                  Results.getValue( i, 1 ).
                                                  toString() : "" );
        aSelectedInterview.Topic = new String( Results.getValue( i, 2 ) != null ?
                                               Results.getValue( i, 2 ).toString() :
                                               "" );
        aSelectedInterview.thequestion = new Question( ( Results.getValue( i, 3 ) != null ?
            Results.getValue( i, 3 ).toString() : "" ),
            ( Results.getValue( i, 4 ) != null ?
              Results.getValue( i, 4 ).toString() : "" ) );

        aSelectedInterview.Interviewee = new Partecipant();
        aSelectedInterview.Interviewee.Id = new String( Results.getValue( i, 5 ).toString() );
        aSelectedInterview.Interviewee.Description = new String( Results.getValue( i, 6 ).toString() );

        Query.println( "Query Result: " + aSelectedInterview.Id + " " +
                       aSelectedInterview.Position + " " +
                       aSelectedInterview.Topic + " " + aSelectedInterview.Interviewee.Description );
        InterviewArray[i] = aSelectedInterview;
      }

      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( QueryEvaluationException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( IOException e ){
      Err.println( "Error retrieving Topics " + e.toString() );
    }

    return result;
  }

  private boolean ProcessInterview( int position ) throws
      Exception{
    boolean result = false;

    // Descend in the interview statements
    Interview CurrentInterview = InterviewArray[position];

    // The following function is recursive and that is because the data structure is
    // also recursive after a certain point; this is why we first have to call this function
    // to descend in the data structure
    result = TraverseInterviewStatememts( CurrentInterview.Arguments );

    return result;
  }

  // This function finds the longest path of related statements
  private int GetMaxLength( VerbalStatement aStatement, ArrayList current ) throws
      Exception{

    int max = 0;
    //This hash table contains the longest path
    ArrayList MaxChain = null;

    if( !current.contains( aStatement.Id ) ){
      current.add( aStatement.Id );
    } else{
      return 0;
    }

    if( aStatement.ConnectedStatements != null ){
      int length = 0;
      //The Hashtable is used to explore the chain and gets every time filled in
      // with the iterative exploration, which if it yields a max length, gets copied
      // to the Max hashtables
      ArrayList local;
      VerbalStatement bStatement;
      for( int j = 0; j < aStatement.ConnectedStatements.size(); j++ ){
        Link aLink = ( Link )aStatement.ConnectedStatements.get( j );
        if( current.contains( aLink.StatementId ) ){
          continue;
        }

        if( ( bStatement = ( VerbalStatement )Statements.get( aLink.StatementId ) ) == null ){
          throw new Exception( "Statement not found " );
        }

        // reset the iterative hash table for a new iteration
        local = new ArrayList( current );
        length = GetMaxLength( bStatement, local );
        if( length > max ){
          max = length;
          // New max found, reset the MaxChain hashtable
          Debug2.println( "Length of the chain " + max );
          MaxChain = new ArrayList( local );
        }
      }
    }

    if( MaxChain != null ){
      // Items will be copied again in the hash table
      current.clear();
      for( int i = 0; i < MaxChain.size(); i++ ){
        current.add( i, MaxChain.get( i ) );
      }
    }

    return( max + 1 );

  }

  private boolean TraverseInterviewStatememts( Argument[] Arguments ) throws
      Exception{

    // This function has two parts, a processing part and a recursive part
    if( Arguments != null ){
      for( int i = 0; i < Arguments.length; i++ ){
        if( Arguments[i].Nodes != null ){
          for( int ii = 0; ii < Arguments[i].Nodes.length; ii++ ){
            if( Arguments[i].Nodes[ii].theStatements != null ){
              for( int iii = 0;
                   iii < Arguments[i].Nodes[ii].theStatements.length; iii++ ){
                // Processing part
                ConnectStatements( Arguments[i].Nodes[ii].theStatements[iii] );
              }
            }
          }
        }
        // Recursive part
        if( Arguments[i].Arguments != null ){
          TraverseInterviewStatememts( Arguments[i].Arguments );

        }
      }
    }
    return true;
  }

  private boolean ConnectStatements( VerbalStatement theStatement ) throws Exception{
    // starting with 100 supporting/counter statements
    int Dimension = 100, size = 0;
    boolean result = false;
    RhetType[] actions = {
        RhetActStrings[SUPPORT],
        //RhetActStrings[ASSOCIATE],
        RhetActStrings[GENERALIZE],
        RhetActStrings[SPECIALIZE],
        RhetActStrings[ATTACK]
    };

    // Initialize the hash table
    MyArrayList INStatements = new MyArrayList();

    // Construct the first statement
    ConstructedStatement aStatement = new ConstructedStatement( theStatement );
    aStatement.LinkType = new MyArrayList();
    aStatement.LinkType.add( new LinkDescription( null, null, null, null ) );

    INStatements.add( aStatement );
    int curmax = -1;
    int max = 0;
    int counter = 0;
    while( max > curmax && counter < 2 ){
      curmax = max;
      for( int i = 0; i < actions.length; i++ ){

        // Augment the set of statements that can be contrasted or supported by
        // using support, generalization and specialization
        if( !OneStepStatementManipulation( INStatements, actions[i].index ) ){
          throw new Exception( "Error expanding Statement with " + actions[i] );
        }

        size = INStatements.size();
        if( size <= max ){
          Log2.println( "No Increase with " + actions[i].Name );
        } else{
          Log2.println( "Increase with " + actions[i].Name + " of " + ( size - max ) );
          max = size;
        }
      }
      counter++;
    }

    // Link the statements from the IN because OUT is cleared
    if( !LinkStatements( theStatement, INStatements ) ){
      throw new Exception( "Error linking Statements " );
    }

    Log1.println( "Length constructed statements " + INStatements.size() );
    if( theStatement.ConnectedStatements != null ){
      Log1.println( "Length connected statements: " + theStatement.ConnectedStatements.size() );
      PrintConnectedStatement( theStatement.ConnectedStatements, true );
    } else{
      Log1.println( "ZERO Length of connected statements " );
      PrintConnectedStatement( INStatements, false );
    }

    return true;
  }

  private boolean LinkStatements( VerbalStatement theStatement, MyArrayList StatementArray ) throws
      Exception{
    // here we do the actual linking, this has become a separate function so that
    // it is not needed to repeat the code twice

    VerbalStatement StatementToLink;

    // The hash table contains statements that only have the s/m/p/o filled in
    // We still have to see if they exist or not, and we query the repository to see if they do
    // which should be the fastest method (or the easiest) instead of looking in
    // the hash table of all statements
    for( int i = 0; i < StatementArray.size(); i++ ){
      ConstructedStatement aStatement = ( ConstructedStatement )StatementArray.get( i );

      int TotalAND = ( aStatement.Subject.equals( "" ) ? 1 : 0 ) +
          ( aStatement.Modifier.equals( "" ) ? 1 : 0 ) +
          ( aStatement.Predicate.equals( "" ) ? 1 : 0 ) /* +
                     ( aStatement.Object.equals( "" ) ? 1 : 0 )*/
          ;

      String whereclause;
      if( TotalAND > 0 ){
        whereclause = " where " +
            ( aStatement.Subject.equals( "" ) ? "Subject = NULL " : "" ) +
            ( ( aStatement.Subject.equals( "" ) && TotalAND-- > 1 ) ? " and " : " " ) +
            ( aStatement.Modifier.equals( "" ) ? "Modifier = NULL " : "" ) +
            ( ( aStatement.Modifier.equals( "" ) && TotalAND-- > 1 ) ? " and " : " " ) +
            ( aStatement.Predicate.equals( "" ) ? "Predicate = NULL " : "" ) +
            ( ( aStatement.Predicate.equals( "" ) && TotalAND-- > 1 ) ? " and " : " " ) /*+
                         ( aStatement.Object.equals( "" ) ? "Object = NULL " : "" )*/
            ;
      } else{
        whereclause = "";
      }

      String queryString = "select Id  " +
          "from " +
          "{Id} <rdf:type> {<VoxPopuli:VerbalStatement>} " +
          ( !aStatement.Subject.equals( "" ) ?
            ", {Id} <VoxPopuli:subject>  {<!" + aStatement.Subject + ">}" :
            ", [{Id} <VideoGen:subject> {Subject}]" ) +
          ( !aStatement.Modifier.equals( "" ) ?
            ", {Id} <VoxPopuli:modifier> {<!" + aStatement.Modifier + ">}" :
            ", [{Id} <VoxPopuli:modifier> {Modifier}]" ) +
          ( !aStatement.Predicate.equals( "" ) ?
            ", {Id} <VoxPopuli:predicate> {<!" + aStatement.Predicate + ">}" :
            ", [{Id} <VoxPopuli:predicate> {Predicate}]" ) +
          /*
                    ( !aStatement.Object.equals( "" ) ?
                      ", {Id} <VoxPopuli:object> {<!" + aStatement.Object + ">}" :
                      ", [{Id} <VoxPopuli:object> {Object}]" ) +
           */
          whereclause +
          " using namespace " + Namespaces;

      Query.println( "Query: " + queryString );

      QueryResultsTable Results = null;
      try{

        Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );
        for( int j = 0; j < Results.getRowCount(); j++ ){
          // Apparently such a statement exists, so it MUST be in the hash table
          // StatementToLink will be a fully filled in statement
          StatementToLink = null;
          if( ( StatementToLink = ( VerbalStatement )Statements.get( Results.getValue( j,
              0 ).toString() ) ) == null ){
            throw new Exception( "Missing statement in Hash Table " +
                                 Results.getValue( j, 0 ).toString() );
          }
          // only link if the statements are not the same
          if( !StatementToLink.Id.equals( theStatement.Id ) ){
            // Allocate the related statements structure
            if( theStatement.ConnectedStatements == null ){
              theStatement.ConnectedStatements = new MyArrayList();
            }
            if( StatementToLink.ConnectedStatements == null ){
              StatementToLink.ConnectedStatements = new MyArrayList();
            }
            Log2.println( "URRA, SI LINKA " + PrintLinkList( aStatement.LinkType ) + ": " + theStatement.Id +
                          " " + StatementToLink.Id );
            // Link the statements, using the Id from the RDF instance, which
            // should be unique
            // First link the one we have to the one we found
            Link a = new Link();
            a.StatementId = new String( StatementToLink.Id );
            a.LinkType = new MyArrayList( aStatement.LinkType );

            Object c;
            if( theStatement.ConnectedStatements.contains( a ) ){
            } else{
              theStatement.ConnectedStatements.add( a );
            }
            // Then link the one we found to the one we have
            Link b = new Link();
            b.StatementId = new String( theStatement.Id );
            b.LinkType = new MyArrayList( aStatement.LinkType.invert() );
            if( StatementToLink.ConnectedStatements.contains( b ) ){
            } else{
              StatementToLink.ConnectedStatements.add( b );
            }
          }
        }
      } catch( AccessDeniedException ex ){
        throw new Exception( "Exception in Linking Statements " + ex.toString() );
      } catch( QueryEvaluationException ex ){
        throw new Exception( "Exception in Linking Statements " + ex.toString() );
      } catch( MalformedQueryException ex ){
        throw new Exception( "Exception in Linking Statements " + ex.toString() );
      } catch( IOException ex ){
        throw new Exception( "Exception in Linking Statements " + ex.toString() );
      }
    }

    return true;
  }

  private boolean OneStepStatementManipulation( MyArrayList INStatements, int action ) throws
      Exception{
    // This function implements the basic logic of supporting a statement
    // and produces a list (hash table) of possible pro statements

    QueryResultsTable Results;
    String Relation;

    switch( action ){
      case ATTACK:
        Relation = "<VoxPopuli:logicalopposite>";
        break;
      case SUPPORT:
        Relation = "<VoxPopuli:logicalsimilar>";
        break;
      case GENERALIZE:
        Relation = "<VoxPopuli:generalization>";
        break;
      case SPECIALIZE:
        Relation = "<VoxPopuli:specialization>";
        break;
      case ASSOCIATE:
        Relation = "<VoxPopuli:association>";
        break;
      default:
        throw new Exception( "Unknown action to handle statements " );
    }

    boolean result = false;
    String Message = RhetActStrings[action].Name + " ";
    int size = INStatements.size();

    try{
      for( int i = 0; i < size; i++ ){

        ConstructedStatement theStatement = ( ConstructedStatement )INStatements.get( i );
        ConstructedStatement aStatement;

        if( action == SUPPORT ){

          if( !theStatement.LinkType.contains( new LinkDescription( null, null, null, null ) ) ){

            aStatement = new ConstructedStatement( theStatement );

            aStatement.LinkType.add( new LinkDescription( null, null, null, null ) );

            Log2.println( "Adding statement to support itself: " +
                          aStatement.SubjectDescription + " " +
                          aStatement.ModifierDescription + " " +
                          aStatement.PredicateDescription /*+ " " +
                                                 aStatement.ObjectDescription */);

            // This should avoid duplications of equal statements
            if( !INStatements.contains( aStatement ) ){
              INStatements.add( aStatement );
            }
          }
        }

        if( !theStatement.Subject.equals( "" ) ){
          String queryString = "select Subject, SubjectDescription " +
              "from " +
              "{<!" + theStatement.Subject + ">} " + Relation + " {Subject}; " +
              Relation +
              " {Subject} <VoxPopuli:partDescription> {SubjectDescription} " +
              "using namespace " + Namespaces;

          Query.println( "Query: " + queryString );

          Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
              queryString );

          int limit = Results.getRowCount();

          for( int j = 0; j < limit; j++ ){

            aStatement = new ConstructedStatement( theStatement );
            aStatement.LinkType = new MyArrayList( theStatement.LinkType );
            aStatement.LinkType.add( new LinkDescription( RhetActStrings[action], null, null, null ) );

            aStatement.Subject = new String( Results.getValue( j, 0 ) != null ?
                                             Results.getValue( j, 0 ).toString() :
                                             "" );
            aStatement.SubjectDescription = new String( Results.getValue( j, 1 ) != null ?
                Results.getValue( j, 1 ).toString() : "" );

            Log2.println( "Constructing " + Message + "Subject: " +
                          aStatement.SubjectDescription + " " +
                          aStatement.ModifierDescription + " " +
                          aStatement.PredicateDescription /*+ " " +
                                                                  aStatement.ObjectDescription */);

            // This should avoid duplications of equal statements
            if( !INStatements.contains( aStatement ) ){
              INStatements.add( aStatement );
            }
          }
        }
        if( theStatement.Modifier.equals( "" ) ){

          throw new Exception( " Modifier can not be null " );

        } else{

          String queryString = "select Modifier, ModifierDescription " +
              "from " +
              "{<!" + theStatement.Modifier + ">} " +
              Relation + " {Modifier}; " +
              Relation +
              " {Modifier} <VoxPopuli:partDescription> {ModifierDescription} " +
              "using namespace " + Namespaces;

          Query.println( "Query: " + queryString );

          Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
              queryString );

          int limit = Results.getRowCount();

          for( int j = 0; j < limit; j++ ){

            if( Results.getValue( j, 0 ) != null ){
              aStatement = new ConstructedStatement( theStatement );

              aStatement.LinkType = new MyArrayList( theStatement.LinkType );
              aStatement.LinkType.add( new LinkDescription( null, RhetActStrings[action], null, null ) );

              aStatement.Modifier = new String( Results.getValue( j, 0 ).toString() );
              aStatement.ModifierDescription = new String( Results.getValue( j, 1 ).
                  toString() );

              // This should avoid duplications of equal statements
              if( !INStatements.contains( aStatement ) ){
                INStatements.add( aStatement );
              }
            }
          }
        }
        if( !theStatement.Predicate.equals( "" ) ){

          String queryString = "select Predicate, PredicateDescription " +
              "from " +
              "{<!" + theStatement.Predicate + ">} " +
              Relation + " {Predicate}; " +
              Relation +
              " {Predicate} <VoxPopuli:partDescription> {PredicateDescription} " +
              "using namespace " + Namespaces;

          Query.println( "Query: " + queryString );

          Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
              queryString );

          int limit = Results.getRowCount();

          for( int j = 0; j < limit; j++ ){
            aStatement = new ConstructedStatement( theStatement );

            aStatement.LinkType = new MyArrayList( theStatement.LinkType );
            aStatement.LinkType.add( new LinkDescription( null, null, RhetActStrings[action], null ) );

            aStatement.Predicate = new String( Results.getValue( j, 0 ) != null ?
                                               Results.getValue( j, 0 ).toString() :
                                               "" );
            aStatement.PredicateDescription = new String( Results.getValue( j, 1 ) != null ?
                Results.getValue( j, 1 ).toString() : "" );

            Log2.println( "Constructing " + Message + "Predicate: " +
                          aStatement.SubjectDescription + " " +
                          aStatement.ModifierDescription + " " +
                          aStatement.PredicateDescription /* + " " +
                                                 aStatement.ObjectDescription */);

            // This should avoid duplications of equal statements
            if( !INStatements.contains( aStatement ) ){
              INStatements.add( aStatement );
            }
          }
        }
        /*
                if( !theStatement.Object.equals( "" ) ){
                  String queryString = "select Object, ObjectDescription " +
                      "from " +
                      "{<!" + theStatement.Object + ">} " +
                      Relation + " {Object}; " +
                      Relation +
                      " {Object} <VoxPopuli:partDescription> {ObjectDescription} " +
                      "using namespace " + Namespaces;

                  Query.println( "Query: " + queryString );

                  Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
                      queryString );

                  int limit = Results.getRowCount();

                  for( int j = 0; j < limit; j++ ){
                    aStatement = new ConstructedStatement( theStatement );

                    aStatement.LinkType = new MyArrayList( theStatement.LinkType );
                    aStatement.LinkType.add( new LinkDescription(null, null, null, RhetActStrings[action]) );

                    aStatement.Object = new String( Results.getValue( j, 0 ) != null ?
                                                    Results.getValue( j, 0 ).toString() :
                                                    "" );
                    aStatement.ObjectDescription = new String( Results.getValue( j, 1 ) != null ?
                        Results.getValue( j, 1 ).toString() : "" );

                    Log.println( "Constructing " + Message + "Object: " +
                                        aStatement.SubjectDescription + " " +
                                        aStatement.ModifierDescription + " " +
                                        aStatement.PredicateDescription + " " +
                                        aStatement.ObjectDescription );

                    // This should avoid duplications of equal statements
                    OUTStatements.put( aStatement.Subject + aStatement.Modifier +
                                       aStatement.Predicate + aStatement.Object,
                                       aStatement );

                  }
                }*/
      }
      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( Exception e ){
      Err.println( "Error supporting statements with Logos" + e.toString() );
    }

    return result;
  }

  private boolean ReadInterviewData( int position ) throws Exception{
    boolean result = false;

    if(
        ( ( InterviewArray[position].Segments = ReadMedia( InterviewArray[
        position].Id, "Q: " + InterviewArray[position].thequestion.QuestionText ) ) != null ) &&
        ( ( InterviewArray[position].Arguments = ReadRhetoric( InterviewArray[
        position].Id,
        "rhetoricalForm", position ) ) != null )
        ){
      result = true;
    }
    return result;
  }

  private VideoSegment[] ReadMedia( String Id, String Description ) throws Exception{

    QueryResultsTable Results;
    VideoSegment[] theVideoSegments = null;

    // Read media related information
    String queryString =
        "select Src, BeginFrame, EndFrame, MediaDescription, Language, Quality " +
        "from " +
        "{<!" + Id + ">} " +
        "<VoxPopuli:hasMedia> {} <MediaClipping:beginFrame> {BeginFrame}; " +
        "<MediaClipping:endFrame> {EndFrame}; " +
        "[<VoxPopuli:mediaDescription> {MediaDescription}]; " +
        "<BasicMedia:src> {Src}; " +
        "<VoxPopuli:language> {Language}; " +
        "<VoxPopuli:quality> {Quality} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );

    Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
        queryString );

    int l = Results.getRowCount();

    if( l == 0 ){
      throw new Exception( "No media associated with Video Segment " );
    }
    theVideoSegments = new VideoSegment[l];

    for( int i = 0; i < l; i++ ){
      theVideoSegments[i] = new VideoSegment();
      theVideoSegments[i].FileName = new String( Results.getValue( i, 0 ).
                                                 toString() );
      theVideoSegments[i].BeginFrame = new String( Results.getValue( i, 1 ).
          toString() );
      theVideoSegments[i].EndFrame = new String( Results.getValue( i, 2 ).
                                                 toString() );
      theVideoSegments[i].Description = new String( Results.getValue( i, 3 ) != null ?
          Results.getValue( i, 3 ).toString() + Description : Description );

      theVideoSegments[i].Language = new String( Results.getValue( i, 4 ) != null ?
                                                 Results.getValue( i, 4 ).toString() :
                                                 "" );
      theVideoSegments[i].Quality = new String( Results.getValue( i, 5 ) != null ?
                                                Results.getValue( i, 5 ).toString() :
                                                "" );
    }

    return theVideoSegments;
  }

  // Read rhetorical information.
  private Argument[] ReadRhetoric( String Id, String Relation, int position ) throws
      Exception{

    Argument[] Arguments = null;

    // Relation is a parameter so that this function can be called
    // recursively, once with "rhetoricalForm" and with "nestedRhetoricalForm"
    String queryString = "select Rhetoric  " +
        "from " +
        "{<!" + Id + ">} " + "<VoxPopuli:" + Relation + "> {Rhetoric} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );

    QueryResultsTable Results = theRepository.Client.performTableQuery(
        QueryLanguage.SERQL,
        queryString );

    int l = Results.getRowCount();

    if( l != 0 ){
      Arguments = new Argument[l];

      // There can be more Toulmin structures in one interview
      // Different nodes belong to each structure
      for( int i = 0; i < l; i++ ){

        Arguments[i] = new Argument();
        queryString = "select Role from " +
            "{<!" + Results.getValue( i, 0 ).toString() + ">} " +
            "<VoxPopuli:toulminRole> {Role} " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryString );

        QueryResultsTable RoleResults = theRepository.Client.performTableQuery(
            QueryLanguage.SERQL,
            queryString );

        int ll = RoleResults.getRowCount();

        if( ll == 1 ){

          Arguments[i].ToulminType = null;
          String Temp = RoleResults.getValue( 0, 0 ).toString();
          Temp = new String( Temp.substring( Temp.lastIndexOf( '#' ) + 1 ) );
          for( int j = 0; j < ToulminStrings.length; j++ ){
            if( Temp.equals( ToulminStrings[j].Name ) ){
              Arguments[i].ToulminType = ToulminStrings[j];
              break;
            }
          }
          if( Arguments[i].ToulminType == null ){
            throw new Exception( "Unknown Toulmin Type " );
          }
        } else if( Relation.equals( "nestedRhetoricalForm" ) ){
          throw new Exception( "Missing Toulmin Type in Nested Argument" );
        }

        Arguments[i].Nodes = ReadToulminData( Results.getValue( i, 0 ).toString(),
                                              position );

        // Node can also be nested Toulmin structures
        Arguments[i].Arguments = ReadRhetoric( Results.getValue( i, 0 ).toString(),
                                               "nestedRhetoricalForm", position );

      }
    }

    return Arguments;
  }

  private ToulminNode[] ReadToulminData( String Id, int position ) throws
      Exception{

    ToulminNode[] Nodes = null;

    String queryString = "select Node, NodeType " +
        "from " + "{<!" + Id + ">} " +
        "<VoxPopuli:toulminType> {Node}, " +
        "{Node} <serql:directType> {NodeType} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );

    QueryResultsTable NodeResults = theRepository.Client.performTableQuery(
        QueryLanguage.SERQL, queryString );

    int l = NodeResults.getRowCount();

    // If there is something we read the node
    if( l != 0 ){

      Nodes = new ToulminNode[l];

      for( int i = 0; i < l; i++ ){

        Nodes[i] = new ToulminNode();
        Nodes[i].ToulminType = null;
        String Temp = new String( NodeResults.getValue( i, 1 ).toString() );
        Temp = new String( Temp.substring( Temp.lastIndexOf( '#' ) + 1 ) );
        for( int j = 0; j < ToulminStrings.length; j++ ){
          if( Temp.equals( ToulminStrings[j].Name ) ){
            Nodes[i].ToulminType = ToulminStrings[j];
            break;
          }
        }
        if( Nodes[i].ToulminType == null ){
          throw new Exception( "Unknown Toulmin Type " );
        }
        Nodes[i].theInterview = InterviewArray[position];

        //Read the statements per node
        queryString = "select Statements  " +
            "from " + "{<!" + NodeResults.getValue( i, 0 ).toString() + ">} " +
            "<VoxPopuli:statements> {Statements} " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryString );
        QueryResultsTable StatementsResults = theRepository.Client.
            performTableQuery( QueryLanguage.SERQL, queryString );

        int ll = StatementsResults.getRowCount();

        // There must be statement in a Toulmin Node
        if( ll == 0 ){
          throw new Exception( "Toulmin node with no Statements " );
        }

        Nodes[i].theStatements = new VerbalStatement[ll];

        for( int ii = 0; ii < ll; ii++ ){
          Nodes[i].theStatements[ii] = new VerbalStatement();
          Nodes[i].theStatements[ii] = ReadStatements( StatementsResults.
              getValue( ii, 0 ).toString(), Nodes[i] );

          // Put the statement in the hash table to retrieve it more easily
          if( Statements == null ){
            Statements = new Hashtable();
          }
          Statements.put( Nodes[i].theStatements[ii].Id,
                          Nodes[i].theStatements[ii] );
        }

        // Read the relation between statements (OR or AND) and the
        // type of link to the rest of the Toulmin structure
        queryString = "select StatementRel, LogicType  " +
            "from " + "{<!" + NodeResults.getValue( i, 0 ).toString() + ">} " +
            "<VoxPopuli:stmRelation> {StatementRel}; " +
            "<VoxPopuli:logicType> {LogicType} " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryString );
        QueryResultsTable NodeDataResults = theRepository.Client.
            performTableQuery( QueryLanguage.SERQL, queryString );

        if( NodeDataResults.getRowCount() != 0 ){
          if( NodeDataResults.getValue( 0, 0 ) != null ){
            Nodes[i].StatementsinAND = NodeDataResults.getValue( 0, 0 ).toString().
                equals( "AND" );
          } else{
            Nodes[i].StatementsinAND = false;
          }
          if( NodeDataResults.getValue( 0, 1 ) != null ){
            Nodes[i].LogicType = new String( NodeDataResults.getValue( 0, 1 ).
                                             toString() );
          } else{
            Nodes[i].LogicType = new String( "" );
          }
        }
      }
    }

    return Nodes;
  }

  private VerbalStatement ReadStatements( String Id, ToulminNode Node ) throws
      Exception{

    VerbalStatement aStatement = null;

    // This function can be reused whenever statements need to be read

    String queryString =
        "select Subject, Modifier, Predicate, " +
        "SubjectDescription, PredicateDescription, ModifierDescription, Explicit, " +
        "Interviewee, Description " +
        "from " +
        "{<!" + Id + ">} " + " <VoxPopuli:subject> {Subject}; " +
        "<VoxPopuli:explicit> {Explicit}; " +
        "<VoxPopuli:claimer> {Interviewee}; " +
        "[<VoxPopuli:modifier> {Modifier}]; " +
        "[<VoxPopuli:predicate> {Predicate}]; " +
        "[<VoxPopuli:subject> {Subject} <VoxPopuli:partDescription> {SubjectDescription}]; " +
        "[<VoxPopuli:modifier> {Modifier} <VoxPopuli:partDescription> {ModifierDescription}]; " +
        "[<VoxPopuli:predicate> {Predicate} <VoxPopuli:partDescription> {PredicateDescription}], " +
        "{Interviewee} <VoxPopuli:description> {Description} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery(
          QueryLanguage.SERQL, queryString );

      aStatement = new VerbalStatement();

      // This will be used as a key
      aStatement.Id = new String( Id );

      aStatement.Subject = new String( Results.getValue( 0, 0 ) != null ?
                                       Results.getValue( 0, 0 ).toString() :
                                       "" );
      if( Results.getValue( 0, 6 ) != null ){
        aStatement.Explicit = Results.getValue( 0, 6 ).toString().equals( "true" ) ?
            true : false;
      }

      // Set the parent node to know what kind of Toulmin node this statement
      // belongs to
      aStatement.ParentNode = Node;

      aStatement.SubjectDescription = new String( Results.getValue( 0, 3 ) != null ?
                                                  Results.getValue( 0, 3 ).
                                                  toString() : "" );
      aStatement.Modifier = new String( Results.getValue( 0, 1 ) != null ?
                                        Results.getValue( 0, 1 ).toString() :
                                        "" );
      aStatement.ModifierDescription = new String( Results.getValue( 0, 5 ) != null ?
          Results.getValue( 0, 5 ).
          toString() : "" );
      aStatement.Predicate = new String( Results.getValue( 0, 2 ) != null ?
                                         Results.getValue( 0, 2 ).toString() :
                                         "" );
      aStatement.PredicateDescription = new String( Results.getValue( 0, 4 ) != null ?
          Results.getValue( 0, 4 ).toString() : "" );
      /*
            aStatement.Object = new String( Results.getValue( 0, 3 ) != null ?
                                            Results.getValue( 0, 3 ).toString() : "" );
            aStatement.ObjectDescription = new String( Results.getValue( 0, 7 ) != null ?
                                                       Results.getValue( 0, 7 ).
                                                       toString() : "" );
       */
      aStatement.Claimer = new Partecipant();
      aStatement.Claimer.Id = new String( Results.getValue( 0, 7 ).toString() );
      aStatement.Claimer.Description = new String( Results.getValue( 0, 8 ).toString() );

      String Temp = "S: " +
          ( !aStatement.SubjectDescription.equals( "" ) ?
            aStatement.SubjectDescription + " " : "" ) +
          ( !aStatement.ModifierDescription.equals( "" ) ?
            aStatement.ModifierDescription + " " : "" ) +
          ( !aStatement.PredicateDescription.equals( "" ) ?
            aStatement.PredicateDescription + " " : "" ) +
          /*          ( !aStatement.ObjectDescription.equals( "" ) ?
                      aStatement.ObjectDescription + " " : "" ) +
           */
          "T: " + aStatement.ParentNode.ToulminType.Name + " " +
          "C: " + aStatement.Claimer.Description;

      Query.println( "Query Result: " + Temp );

      // Read the segments
      aStatement.Segments = ReadMedia( Id, Temp );
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( IOException e ){
      Err.println( "Error retrieving Topics " + e.toString() );
    }

    return aStatement;
  }

  private boolean PathosProcessInterview( String UserType, int position,
                                          int attitude ) throws Exception{
    boolean result = false;

    /* if (!PathosHandleStatement(UserType, attitude)) {
       throw new Exception("Error creating attack statements");
     }
     if (!SelectImages(UserType, position)) {
       throw new Exception("Error selecting video segments");
     }
     if (!AssignImages()) {
       throw new Exception("Error assigning video segments");
     }
     */
    return result;
  }

  private boolean PathosHandleStatement( String UserTYpe, int attitude,
                                         VerbalStatement AttackedStatement ){
    QueryResultsTable Results;
    int index = 0, link;
    boolean result = false;

    // assuming up to 100 counter statements
    if( AnswerConceptArray == null ){
      AnswerConceptArray = new Hashtable( 100 );
    }

    try{
      String Relation = "ERROR";
      String queryString;

      // Determine what kind of link the user has with the subject
      // This is used to calculate 1 side of Heider's triangle

      if( !AttackedStatement.Subject.equals( "" ) ){
        if(
            // modifier has positive meaning
            ( AttackedStatement.ModifierDescription.equals( "" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "best" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "good" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not_bad" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not_worst" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "same" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "only" ) ) ){
          link = Positive;
        } else if(
            // modifier has negative meaning
            ( AttackedStatement.ModifierDescription.equals( "bad" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not best" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not good" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not only" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "not same" ) ) ||
            ( AttackedStatement.ModifierDescription.equals( "worst" ) ) ){
          link = Negative;
        } else{
          throw new Exception( "Modifier not defined " );
        }
        /*
                if( !AttackedStatement.Object.equals( "" ) ){

                  // Check whether we are linked to a negative (for the user) object
                  queryString =
                      "select Point, PointDescription " +
                      "from " +
                      "{} <rdf:type> {<VoxPopuli:UserModel>}; " +
                      "<VoxPopuli:userType> {Value}; " +
                      "<VoxPopuli:negativeReaction> {Point} <VoxPopuli:partDescription> {PointDescription} " +
                      "where Value = \"" + UserTYpe + "\" and " +
                      "Point = <!" + AttackedStatement.Object + "> " +
                      "using namespace " + Namespaces;

                  Query.println( "Query: " + queryString );

                  Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
                      queryString );

                  if( Results.getRowCount() == 1 ){
                    link = -link;
                  }
                }
         */
        if( ( link * attitude ) == Negative ){
          Relation = new String( "<VoxPopuli:negativeReaction>" );
        } else{
          Relation = new String( "<VoxPopuli:positiveReaction>" );
        }

        queryString =
            "select Point, PointDescription " +
            "from " +
            "{} <rdf:type> {<VoxPopuli:UserModel>}; " +
            "<VoxPopuli:userType> {Value}; " +
            Relation +
            " {Point} <VoxPopuli:partDescription> {PointDescription}, " +
            "{<!" + AttackedStatement.Subject +
            ">} <VoxPopuli:association> {Point} " +
            "where Value = \"" + UserTYpe + "\" " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryString );

        Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        int limit = Results.getRowCount();

        for( int j = 0; j < limit; j++ ){

          ImageConcept aImageConcept = new ImageConcept();
          aImageConcept.BeginFrame = new String( AttackedStatement.Segments[0].
                                                 BeginFrame );
          aImageConcept.EndFrame = new String( AttackedStatement.Segments[0].
                                               EndFrame );

          aImageConcept.Concept = new String( Results.getValue( j, 0 ) != null ?
                                              Results.getValue( j, 0 ).toString() :
                                              "" );
          aImageConcept.ConceptDescription = new String( Results.getValue( j, 1 ) != null ?
              Results.getValue( j, 1 ).toString() :
              "" );

          Log2.println( "Constructing Image " +
                        ( ( ( link * attitude ) == Negative ) ? "Con" : "Pro" ) +
                        " Concept: " +
                        aImageConcept.ConceptDescription );

          AnswerConceptArray.put( "" + index++, aImageConcept );
        }
      }

      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( Exception e ){
      Err.println( "Error handling statements with Pathos " + e.toString() );
    }

    return result;
  }

  private int GiveEthosRating( String UserType, String Interviewee ){

    int Rating = 0;
    QueryResultsTable Results;
    String queryString;

    try{
      for( int i = 0; i < 3; i++ ){

        queryString =
            "select Factor " +
            "from " +
            "{} <rdf:type> {<VoxPopuli:UserModel>}; " +
            "<VoxPopuli:userType> {Value};" +
            "<VoxPopuli:" + Weights[i].RelationName + "> {Factor}, " +
            "{<!" + Interviewee + ">} P {Factor} " +
            "where Value = \"" + UserType + "\" " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryString );

        Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        Rating = Rating + Results.getRowCount() * Weights[i].Weight;
      }

    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( Exception e ){
      Err.println( "Error retrieving Segments from Statements " +
                   e.toString() );
    }

    return Rating;
  }

  private boolean SelectImages( String UserType, int NrInterview ){
    QueryResultsTable Results;
    String queryString;
    boolean result = false;
    VideoSegment aSegment = null;
    Image aImage = null;
    int index = 0;

    // assuming for the moment only one interview gets the images
    SegmentImagesArray = new Hashtable( 1 );

    try{

      queryString =
          "select BeginFrame, EndFrame, FileName, Description " +
          "from " +
          "{<!" + InterviewArray[NrInterview].Id + ">} " +
          "<MediaClipping:beginFrame> {BeginFrame}; " +
          "<MediaClipping:endFrame> {EndFrame}; " +
          "<MediaClipping:src> {FileName}; " +
          "<VoxPopuli:hasQuestion> {} <rdfs:label> {Description} " +
          "using namespace " + Namespaces;

      Query.println( "Query: " + queryString );
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      aSegment = new VideoSegment();
      aSegment.BeginFrame = new String( Results.getValue( 0, 0 ).toString() );
      aSegment.EndFrame = new String( Results.getValue( 0, 1 ).toString() );
      aSegment.FileName = new String( Results.getValue( 0, 2 ).toString() );
      aSegment.Description = new String( Results.getValue( 0, 3 ).toString() );
      aSegment.TransIn = TransName;
      aSegment.TransOut = TransName;
      // assuming max 100 pictures
      aSegment.ImageArray = new Hashtable( 100 );

      for( Enumeration eImgCon = AnswerConceptArray.elements();
           eImgCon.hasMoreElements(); ){
        ImageConcept aImageConcept = ( ImageConcept )eImgCon.nextElement();

        queryString =
            "select FileName, Description " +
            "from " +
            "{} <VoxPopuli:express>  {<!" + aImageConcept.Concept + ">}; " +
            "<MediaClipping:src> {FileName}; " +
            "<VoxPopuli:imageDescription> {Description} " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryString );

        Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        for( int j = 0; j < Results.getRowCount(); j++ ){
          aImage = new Image();
          aImage.BeginFrame = new String( aImageConcept.BeginFrame );
          aImage.EndFrame = new String( aImageConcept.EndFrame );
          aImage.FileName = new String( Results.getValue( j, 0 ).toString() );
          aImage.Description = new String( Results.getValue( j, 1 ).toString() );
          aImage.TransIn = TransName;
          aImage.TransOut = TransName;
          aSegment.ImageArray.put( "" + index++, aImage );
        }
      }
      // We only have one interview to add images to
      SegmentImagesArray.put( "0", aSegment );
      result = true;
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( Exception e ){
      Err.println( "Error retrieving Segments from Statements " +
                   e.toString() );
    }

    return result;
  }

  private boolean AssignImages(){
    // This function assigns 1 picture per segment trying to distribute
    // the picture so that the maximum number of segments get one and
    // a picture is never shown twice
    for( Enumeration eSeg = SegmentImagesArray.elements(); eSeg.hasMoreElements(); ){
      VideoSegment aSegment = ( VideoSegment )eSeg.nextElement();
      Hashtable SubSegwithImg = new Hashtable( 10 );

      // make a structure containing per segment (begin end frame) the pictures
      // that can be associated. This structure is a double hash table that is
      // indexed by begin-end frame and filename respectively; in this way no duplicate
      // are allowed
      if( aSegment.ImageArray != null ){
        for( Enumeration eImg = aSegment.ImageArray.elements();
             eImg.hasMoreElements(); ){
          Image aImage = ( Image )eImg.nextElement();
          Hashtable hImgtoCmp = ( Hashtable )SubSegwithImg.get( aImage.BeginFrame +
              aImage.EndFrame );
          if( ( hImgtoCmp == null ) || ( !hImgtoCmp.containsKey( aImage.FileName ) ) ){
            if( hImgtoCmp == null ){
              hImgtoCmp = new Hashtable( 10 );
            }
            hImgtoCmp.put( aImage.FileName, aImage );
            SubSegwithImg.put( aImage.BeginFrame + aImage.EndFrame, hImgtoCmp );
          }
        }
        aSegment.ImageArray.clear();
        // Now select the picture
        boolean stay = true;
        while( stay ){
          stay = false;
          boolean action = true;
          while( action ){
            // This loop assigns subsegments with only one picture
            action = false;
            for( Enumeration eSubSeg = SubSegwithImg.elements();
                 eSubSeg.hasMoreElements(); ){
              Hashtable ImginSeg = ( Hashtable )eSubSeg.nextElement();
              if( ImginSeg.size() == 1 ){
                Enumeration eImg = ImginSeg.elements();
                Image theImage = ( Image )eImg.nextElement();
                aSegment.ImageArray.put( theImage.BeginFrame, theImage );
                // This clears also the hash table in SubSegwithImg, so
                // the get returns a pointer
                ImginSeg.clear();
                // Eliminate the picture from the rest of the segments
                Eliminate( SubSegwithImg, theImage );
                action = true;
                break;
              }
            }
          }
          for( Enumeration eSubSeg = SubSegwithImg.elements();
               eSubSeg.hasMoreElements(); ){
            // This loop assigns subsegments with more than one picture
            Hashtable ImginSeg = ( Hashtable )eSubSeg.nextElement();
            if( ImginSeg.size() > 0 ){
              Enumeration eImg = ImginSeg.elements();
              Image theImage = ( Image )eImg.nextElement();
              aSegment.ImageArray.put( theImage.BeginFrame, theImage );
              // This clears also the hash table in SubSegwithImg, so
              // the get returns a pointer
              ImginSeg.clear();
              Eliminate( SubSegwithImg, theImage );
              action = true;
              stay = true;
              break;
            }
          }
        }
      }
    }
    return true;
  }

  private boolean Eliminate( Hashtable a, Image i ){
    for( Enumeration e = a.elements(); e.hasMoreElements(); ){
      Hashtable hi = ( Hashtable )e.nextElement();
      if( ( hi != null ) && hi.containsKey( i.FileName ) ){
        hi.remove( i.FileName );
      }
    }
    return true;
  }

  private boolean DummySelectTopic(){
    return false;
  }

  private boolean DummySelectInterviews(){
    return false;
  }

  private String PrintLinkList( ArrayList a ){
    String Msg = new String( "" );
    if( a != null ){
      Msg = new String( " Link:" );
      for( int i = 0; i < a.size(); i++ ){
        LinkDescription Desc = ( LinkDescription )a.get( i );
        String NewMsg = "" +
            ( Desc.SubjectChange.index != SELF ? "Subject: " + Desc.SubjectChange.Name + " " : "" ) +
            ( Desc.ModifierChange.index != SELF ? "Modifier: " + Desc.ModifierChange.Name + " " : "" ) +
            ( Desc.PredicateChange.index != SELF ? "Predicate: " + Desc.PredicateChange.Name + " " : "" ) +
            ( Desc.ObjectChange.index != SELF ? "Object: " + Desc.ObjectChange.Name : "" );
        if( NewMsg.equals( "" ) ){
          NewMsg = "ALL SELF";
        }
        Msg = new String( Msg + " " + NewMsg );
      }
    }
    return Msg;
  }

  private boolean PrintConnectedStatement( MyArrayList StatementsArray, boolean key ){

    // The key is used to understand if the Hashtable contains the statements
    // or the keys to them
    VerbalStatement vStatement;
    ConstructedStatement cStatement;
    String Msg = "";

    if( StatementsArray == null ){
      return false;
    }
    for( int i = 0; i < StatementsArray.size(); i++ ){

      if( key == true ){
        Link a = ( Link )StatementsArray.get( i );
        vStatement = ( VerbalStatement )Statements.get( a.StatementId );
        if( a.LinkType != null ){
          Msg = PrintLinkList( a.LinkType );
        }

        Log1.println( "Linked Statement: " + vStatement.SubjectDescription + " " +
                      vStatement.ModifierDescription + " " +
                      vStatement.PredicateDescription + " " /*+
                                                  vStatement.ObjectDescription */+ Msg );

      } else{
        cStatement = ( ConstructedStatement )StatementsArray.get( i );
        if( cStatement.LinkType != null ){
          Msg = PrintLinkList( cStatement.LinkType );
        }
        Log1.println( "Constructed Statement: " + cStatement.SubjectDescription + " " +
                      cStatement.ModifierDescription + " " +
                      cStatement.PredicateDescription + " " /*+
                                                  cStatement.ObjectDescription */+ Msg );

      }
    }
    return true;
  }

  private boolean OrderArgument( Argument MultipleVoices, boolean do_order ) throws Exception{

    boolean result = false;
    int[] order = {
        EXAMPLE, DATA, WARRANT, BACKING, CLAIM, CONCESSION, CONDITION};
    ArrayList ChainStatements = new ArrayList();

    if( MultipleVoices == null ){
      return result;
    }
    if( do_order == true ){
      for( int i = 0; i < order.length; i++ ){

        if( MultipleVoices.Nodes != null ){
          for( int ii = 0; ii < MultipleVoices.Nodes.length; ii++ ){
            if( MultipleVoices.Nodes[ii].ToulminType.index == order[i] ){

              if( MultipleVoices.Nodes[ii].theStatements == null ){
                continue;
              }

              for( int iii = 0; iii < MultipleVoices.Nodes[ii].theStatements.length; iii++ ){
                ChainStatements.add( MultipleVoices.Nodes[ii].theStatements[iii] );
              }
            }
          }
        }
        if( MultipleVoices.Arguments != null ){
          for( int ii = 0; ii < MultipleVoices.Arguments.length; ii++ ){
            if( MultipleVoices.Arguments[ii].ToulminType.index == order[i] ){
              result = OrderArgument( MultipleVoices.Arguments[ii], do_order );
              if( result == false ){
                return false;
              }
            }
          }
        }
      }
    } else{

      if( MultipleVoices.Nodes != null ){
        for( int ii = 0; ii < MultipleVoices.Nodes.length; ii++ ){
          if( MultipleVoices.Nodes[ii].theStatements == null ){
            continue;
          }

          for( int iii = 0; iii < MultipleVoices.Nodes[ii].theStatements.length; iii++ ){
            ChainStatements.add( MultipleVoices.Nodes[ii].theStatements[iii] );
          }
        }
      }
      if( MultipleVoices.Arguments != null ){
        for( int ii = 0; ii < MultipleVoices.Arguments.length; ii++ ){
          result = OrderArgument( MultipleVoices.Arguments[ii], do_order );
          if( result == false ){
            return false;
          }
        }
      }
    }

    result = CreateSegmentArray( ChainStatements, false, false );
    return result;
  }

  // This function finds checks that there are no paths between concepts that should not be linked
  private boolean CheckConcepts( boolean UseAssociation ) throws Exception{

    QueryResultsTable Results;

    boolean result = true;

    // We read all the concepts
    String queryConcept =
        "select Concept, ConceptDescription " +
        "from " +
        "{Concept} <rdf:type> {<VoxPopuli:RhetoricalStatementPart>}, " +
        "{Concept} <VoxPopuli:partDescription> {ConceptDescription} " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryConcept );

    String Concept, ConceptDescription;

    try{
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryConcept );

      for( int i = 0; i < Results.getRowCount(); i++ ){

        Concept = new String( Results.getValue( i, 0 ).toString() );
        ConceptDescription = new String( Results.getValue( i, 1 ).toString() );

        if( !CheckReciprocalLinks( Concept, ConceptDescription ) ){
          result = false;
        }

      }

      for( int i = 0; i < Results.getRowCount(); i++ ){

        Concept = new String( Results.getValue( i, 0 ).toString() );
        ConceptDescription = new String( Results.getValue( i, 1 ).toString() );
        ArrayList Opposite = new ArrayList();
        ArrayList Visited = new ArrayList();
        ArrayList VisitedDescription = new ArrayList();

        String queryOpposite =
            "select Concept " +
            "from " +
            "{<!" + Concept + ">} <VoxPopuli:logicalopposite> {Concept} " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryOpposite );

        QueryResultsTable OppositeResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryOpposite );

        for( int ii = 0; ii < OppositeResults.getRowCount(); ii++ ){
          if( !Opposite.contains( OppositeResults.getValue( ii, 0 ).toString() ) ){
            Opposite.add( OppositeResults.getValue( ii, 0 ).toString() );
          } else{}
        }

        if( !CheckConceptLinks( Concept, ConceptDescription, Opposite, Visited, VisitedDescription, UseAssociation ) ){
          result = false;
          int size = VisitedDescription.size();
          ResultOut.println( "Concept linked to Opposite: " + ConceptDescription );
          ResultOut.println( "Opposite Concept: " + VisitedDescription.get( size - 1 ) );
          for( int j = 1; j < size - 1; j++ ){
            ResultOut.println( "Linking Concept: " + VisitedDescription.get( j ) );
          }
        }
      }
    } catch( MalformedQueryException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      Err.println( "Error in Query " + e.toString() );
    } catch( IOException e ){
      Err.println( "Error retrieving Topics " + e.toString() );
    }

    return result;
  }

  // This function finds checks that there are no paths between concepts that should not be linked
  private boolean CheckConceptLinks( String Concept, String ConceptDescription, ArrayList Opposite,
                                     ArrayList Visited, ArrayList VisitedDescription, boolean UseAssociation ) throws
      Exception{

    if( Visited.contains( Concept ) ){
      return true;
    } else{
      Visited.add( Concept );
      VisitedDescription.add( ConceptDescription );
    }

    String queryRelated =
        "select Concept, ConceptDescription " +
        "from " +
        "{<!" + Concept + ">} X {Concept}, " +
        "{Concept} <VoxPopuli:partDescription> {ConceptDescription} " +
        "where " +
        "X = <VoxPopuli:logicalsimilar> OR " +
        ( UseAssociation == true ? "X = <VoxPopuli:association> OR " : "" ) +
        "X = <VoxPopuli:generalization> " +
        "using namespace " + Namespaces;

    Query.println( "Query: " + queryRelated );

    QueryResultsTable RelatedResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
        queryRelated );

    for( int ii = 0; ii < RelatedResults.getRowCount(); ii++ ){
      String NextConcept = new String( RelatedResults.getValue( ii, 0 ).toString() );
      String NextConceptDescription = new String( RelatedResults.getValue( ii, 1 ).toString() );
      if( Opposite.contains( NextConcept ) ){
        // Add the causing concept on top of the list
        Visited.add( NextConcept );
        VisitedDescription.add( NextConceptDescription );
        return false;
      }
      ArrayList Temp = new ArrayList( Visited );
      ArrayList TempDesc = new ArrayList( VisitedDescription );
      if( !CheckConceptLinks( NextConcept, NextConceptDescription, Opposite, Temp, TempDesc, UseAssociation ) ){
        Visited.clear();
        VisitedDescription.clear();
        for( int iii = 0; iii < Temp.size(); iii++ ){
          Visited.add( Temp.get( iii ) );
          VisitedDescription.add( TempDesc.get( iii ) );
        }
        return false;
      }
    }

    return true;
  }

  // This function checks that relations between concepts are simmetrical
  private boolean CheckReciprocalLinks( String Concept, String ConceptDescription ) throws
      Exception{

    boolean result = true;

    String[] Relation = {
        "logicalsimilar", "logicalopposite", "association", "generalization"};

    for( int i = 0; i < Relation.length; i++ ){

      String queryRelated =
          "select RelConcept, RelConceptDescription " +
          "from " +
          "{<!" + Concept + ">} <VoxPopuli:" + Relation[i] + "> {RelConcept}, " +
          "{RelConcept} <VoxPopuli:partDescription> {RelConceptDescription} " +
          "using namespace " + Namespaces;

      Query.println( "Query: " + queryRelated );

      QueryResultsTable RelatedResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryRelated );

      for( int ii = 0; ii < RelatedResults.getRowCount(); ii++ ){
        String RelatedConcept = new String( RelatedResults.getValue( ii, 0 ).toString() );
        String RelatedConceptDescription = new String( RelatedResults.getValue( ii, 1 ).toString() );
        String InvRel;
        if( Relation[i].equals( "generalization" ) ){
          InvRel = "specialization";
        } else{
          InvRel = Relation[i];
        }

        String queryBackRelated =
            "select X " +
            "from " +
            "{<!" + RelatedConcept + ">} <VoxPopuli:" + InvRel + "> {X} " +
            "where " +
            "X = <!" + Concept + "> " +
            "using namespace " + Namespaces;

        Query.println( "Query: " + queryBackRelated );

        QueryResultsTable BackRelatedResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryBackRelated );

        if( BackRelatedResults.getRowCount() != 1 ){

          ResultOut.println( "Concept: " + ConceptDescription + " is not back related by relation: " + InvRel +
                             " to concept: " +
                             RelatedConceptDescription );
          result = false;
        }
      }
    }

    return true;
  }

}
