package nl.cwi.ins2.voxpopuli;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;

public class DataBuilder{

  /*********************
   *  CLASSES *
   **********************/


  // Sesame repositories
  private class Repository{
    public String local;
    public String SesameURL;
    public String RepositoryId;
    public String NameSpace;
    public SesameRepository Client;

    public Repository(){
      SesameURL = new String( "null" );
      RepositoryId = new String( "null" );
      NameSpace = new String( "null" );
      local = new String( "null" );
    }
  }

  /* Data collected from the repository */






  /*********************
   *  CONSTANTS *
   **********************/

  // typography for the Statistic file
  static private final String SEPARATOR = ";";

  // These are just constants
  static public final int ATTACK = 0;
  static public final int SUPPORT = ATTACK + 1;
  static public final int GENERALIZE = SUPPORT + 1;
  static public final int SPECIALIZE = GENERALIZE + 1;
  static public final int ASSOCIATE = SPECIALIZE + 1;
  static public final int SELF = ASSOCIATE + 1;

  static public final int CLAIM = 0;
  static public final int BACKING = CLAIM + 1;
  static public final int CONCESSION = BACKING + 1;
  static public final int CONDITION = CONCESSION + 1;
  static public final int DATA = CONDITION + 1;
  static public final int WARRANT = DATA + 1;
  static public final int EXAMPLE = WARRANT + 1;

  // Data structure to associate message and int constant for logging
  // the int value is the index, thus
  // Order is important !!!!!!
  public static final NameIndex[] RhetActStrings = {
      new NameIndex( "Attack", ATTACK ), new NameIndex( "Support", SUPPORT ), new NameIndex( "Generalize", GENERALIZE ),
      new NameIndex( "Specialize", SPECIALIZE ), new NameIndex( "Associate", ASSOCIATE ), new NameIndex( "Self", SELF )
  };

  // Order is important !!!!!!
  public static final NameIndex[] ToulminStrings = {
      new NameIndex( "Claim", CLAIM ), new NameIndex( "Backing", BACKING ), new NameIndex( "Concession", CONCESSION ),
      new NameIndex( "Condition", CONDITION ), new NameIndex( "Data", DATA ), new NameIndex( "Warrant", WARRANT ),
      new NameIndex( "Example", EXAMPLE )
  };

  private NameIndex[] ConnectActions = {
      RhetActStrings[SUPPORT],
      //RhetActStrings[ASSOCIATE],
      RhetActStrings[GENERALIZE],
      RhetActStrings[ATTACK],
      RhetActStrings[SPECIALIZE]
  };

  // Number of iterations when connecting the statements
  static private int IterationsNr = 3;

  static public final String VoxPopuliNamespaces = "http://www.cwi.nl/~media/ns/VP/VoxPopuli.rdfs#";
  static private final String VoxPopuliString = "VP";
  static private final String VoxPopuliSesameNamespaces = "VoxPopuli = <" + VoxPopuliNamespaces + ">";

  static private final String FixedNamespaces =
      VoxPopuliSesameNamespaces + ", " +
      "MediaClipping = <http://www.w3.org/2001/SMIL20/MediaClipping#>, " +
      "BasicMedia = <http://www.w3.org/2001/SMIL20/BasicMedia#>, " +
      "rdfs = <http://www.w3.org/2000/01/rdf-schema#>, " +
      "rdf = <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

  /*************************
   *  PSEUDO VARIABLES     *
   *  set once and forever *
   *  with error checking  *
   *************************/



  /*********************
   *  VARIABLES *
   **********************/

  Outputs P; // used to print
  private DataContainer Data; // all data for the repository

  private Repository theRepository;

  private String Namespaces = null;

  private boolean DataBuilt;
  private boolean SemGraphBuilt;

  /*********************
   *  FUNCTIONS *
   **********************/


  public DataBuilder( String local, String RDFLocation, String RepositoryString,
                      String theNameSpaceString, DataContainer a, Outputs p ) throws Exception{

    ( new Outputs() ).PrintTemp( System.err, "Object " + this.hashCode() + " created " );

    P = p;

    Data = a;

    try{
      // This is required by Sesame
      System.setProperty( "org.xml.sax.driver",
                          "org.apache.xerces.parsers.SAXParser" );

      SetRepository( local, RDFLocation, RepositoryString, theNameSpaceString );

    } catch( Exception e ){
      e.printStackTrace();
      throw new Exception( "Error in constructing DataBuilder " + e.toString() );
    }

    SetDataStructure();

    DataBuilt = false;
    SemGraphBuilt = false;

  }

  public boolean SetIterations( int iterations ){

    IterationsNr = iterations;

    return true;
  }

  private boolean RDFUpToDate( String local, String RDFLocation, String RepositoryString,
                               String theNameSpaceString ){
    if( theRepository.local.equals( local ) &&
        theRepository.NameSpace.equals( theNameSpaceString ) &&
        theRepository.SesameURL.equals( RDFLocation ) &&
        theRepository.RepositoryId.equals( RepositoryString ) ){
      return true;
    }

    return false;
  }

  // This function reads the data and constructs the Semantic Graph
  public boolean SetObject( boolean buildData, boolean buildSG ) throws Exception{

    boolean result = true;

    if( buildSG == true ){
      buildData = true;
    }

    try{
      if( buildData == true ){
        result = ReadRepository();
        if( result == true ){
          DataBuilt = true;
        }
      }

      if( ( result == true ) && ( buildSG == true ) ){
        if( ( result = ProcessStatements() ) == true ){
          DataBuilt = true;
          SemGraphBuilt = true;
        }
      }
    } catch( Exception e ){
      e.printStackTrace();
      throw new Exception( "Error in constructing DataBuilder " + e.toString() );
    }

    return result;

  }

  // This function has a double role, reread the data structure if that is changed and
  // rebuild the Semantic Graph
  public boolean ReSetObject( String local, String RDFLocation, String RepositoryString,
                              String theNameSpaceString, boolean buildData, boolean buildSG ) throws Exception{

    boolean result = true;

    try{
      if( !RDFUpToDate( local, RDFLocation, RepositoryString, theNameSpaceString ) ){

        DataBuilt = false;
        SemGraphBuilt = false;

        P.PrintLn( P.Locator, "(Re)Setting repository " );
        if( !SetRepository( local, RDFLocation, RepositoryString, theNameSpaceString ) ){
          return false;
        }
      }
      result = SetObject( buildData, buildSG );
    } catch( Exception e ){
      e.printStackTrace();
      throw new Exception( "Error in constructing DataBuilder " + e.toString() );
    }

    return result;

  }

  public boolean DataOK( String local, String RDFLocation, String RepositoryString,
                         String theNameSpaceString, boolean SemGraphReq ){
    if( RDFUpToDate( local, RDFLocation, RepositoryString, theNameSpaceString ) ){
      if( DataBuilt == true ){
        if( SemGraphReq == true ){
          if( SemGraphBuilt == true ){
            return true;
          }
        } else{
          return true;
        }
      }
    }
    return false;
  }

  protected void finalize() throws Throwable{
    ( new Outputs() ).PrintTemp( System.err, "Object " + this.hashCode() + " finalized " );
  }

  public boolean PrintStatements(){

    for( Enumeration e = Data.Statements.elements(); e.hasMoreElements(); ){
      VerbalStatement aStatement = ( VerbalStatement )e.nextElement();
      P.Print( P.StatementLog, "Statement " + aStatement.Id + " " + aStatement.SubjectDescription + " " +
               aStatement.ModifierDescription + " " +
               aStatement.PredicateDescription );
      if( aStatement.ConnectedStatements != null ){
        PrintConnectedStatement( aStatement.ConnectedStatements, true );
      } else{
        P.PrintLn( P.StatementLog, "\tNo Link " );
      }
    }
    return true;
  }

  public QueryResultsTable PerformQuery( String queryString ){

    QueryResultsTable a = null;
    String b = new String( queryString + " using namespace " + Namespaces );

    try{

      a = theRepository.Client.performTableQuery( QueryLanguage.SERQL, b );

    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( QueryEvaluationException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error checking Videos" + e.toString() );
    }
    return a;
  }

  // This function tests the length of the video segments
  // and whether they belong to an interview or Statement
  public boolean TestVideo( int minsec, int maxsec ){

    boolean result = true;
    Util u = new Util();
    long dur;

    // We read all the videos
    String queryString1 =
        "select Start, Stop, Lab, File, X, Y, TypeY from " +
        "{X} rdf:type {VoxPopuli:Video}; " +
        "MediaClipping:beginFrame {Start}; " +
        "MediaClipping:endFrame {Stop}; " +
        "rdfs:label {Lab}; " +
        "BasicMedia:src {File}, " +
        "[{Y} VoxPopuli:hasMedia {X}, " +
        "{Y} serql:directType {TypeY}] " +

        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString1 );
    try{
      QueryResultsTable Results1 = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString1 );

      for( int i = 0; i < Results1.getRowCount(); i++ ){

        dur = ( u.ConvertToDSec( Results1.getValue( i, 1 ).toString() ) -
                u.ConvertToDSec( Results1.getValue( i, 0 ).toString() ) ) / 10;
        if( ( dur > maxsec ) || ( dur < minsec ) ){
          P.PrintLn( P.ResultOut, "Video too " +
                     ( dur > maxsec ? "long: " : "short: " ) +
                     Results1.getValue( i, 4 ).toString() + " - " +
                     Results1.getValue( i, 2 ).toString() + " dur " + dur + " - " +
                     ( Results1.getValue( i, 5 ) != null ?
                       "Video contained in: " + Results1.getValue( i, 5 ).toString() +
                       " of type " + Results1.getValue( i, 6 ).toString().substring( VoxPopuliNamespaces.length() ) :
                       " Video NOT contained anywhere" )
              );
        }
      }
      String queryString2 =
          "select Orp, Lab, Y from " +
          "{Orp} rdf:type {VoxPopuli:Video}, " +
          "{Orp} rdfs:label {Lab}, " +
          "[{Y} VoxPopuli:hasMedia {Orp}] " +
          "where " +
          "Y = null " +
          "using namespace " + Namespaces;

      P.PrintLn( P.Query, "Query: " + queryString2 );

      QueryResultsTable Results2 = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString2 );

      for( int ii = 0; ii < Results2.getRowCount(); ii++ ){
        P.PrintLn( P.ResultOut, "Video not contained anywhere: " + Results2.getValue( ii, 0 ).toString() +
                   " - " + Results2.getValue( ii, 1 ).toString() );
      }

    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( QueryEvaluationException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error checking Videos" + e.toString() );
    }

    return result;
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
      OUTStream.writeObject(Data.Statements);
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


  private void SetDataStructure() throws Exception{


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

  }

  private boolean SetRepository( String local, String RDFLocation, String RepositoryString,
                                 String theNameSpaceString ) throws Exception{
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

        theRepository.local = new String( "true" );

        RepositoryConfig repConfig = new RepositoryConfig( RepositoryString );

        SailConfig syncSail = new SailConfig( "org.openrdf.sesame.sailimpl.sync.SyncRdfSchemaRepository" );

        SailConfig memSail = new RdfSchemaRepositoryConfig();

        repConfig.addSail( syncSail );
        repConfig.addSail( memSail );

        repConfig.setWorldReadable( true );
        repConfig.setWorldWriteable( true );
        LocalService service = Sesame.getService();
        theRepository.Client = service.createRepository( repConfig );

        // This filter only returns rdf files
        FileFilter fileFilter = new FileFilter(){
          public boolean accept( File file ){
            return file.isFile() &&
                file.toString().endsWith( "rdf" );
          }
        };

        // Load RDF files
        File myRDFDir = new File( RDFLocation + RepositoryString );

        File[] RDFfiles = myRDFDir.listFiles( fileFilter );

        if( RDFfiles == null ){
          throw new java.io.IOException( "No RDF files in " + RDFLocation + RepositoryString );
        }

        boolean verifyData = true;
        AdminListener myListener = new StdOutAdminListener();

        for( int i = 0; i < RDFfiles.length; i++ ){
          theRepository.Client.addData( RDFfiles[i], theNameSpaceString, RDFFormat.RDFXML, verifyData, myListener );
        }

        // This filter only returns rdfs files
        fileFilter = new FileFilter(){
          public boolean accept( File file ){
            return file.isFile() &&
                file.toString().endsWith( "rdfs" );
          }
        };

        File myRDFSDir = new File( RDFLocation + VoxPopuliString );
        File[] RDFSfiles = myRDFSDir.listFiles( fileFilter );

        if( RDFSfiles == null ){
          throw new java.io.IOException( "No RDFS files in " + RDFLocation + VoxPopuliString );
        }

        for( int i = 0; i < RDFSfiles.length; i++ ){
          theRepository.Client.addData( RDFSfiles[i], VoxPopuliNamespaces, RDFFormat.RDFXML, verifyData, myListener );
        }

      } else{
        theRepository.local = new String( "false" );

        // Set the URL of the Sesame server and create the client to talk to it
        java.net.URL sesameServerURL = new java.net.URL( RDFLocation );
        SesameService service = Sesame.getService( sesameServerURL );
        //service.login("sbocconi", "Apriti");
        //RepositoryList a = service.getRepositoryList();
        theRepository.Client = service.getRepository( RepositoryString );
      }

      theRepository.RepositoryId = new String( RepositoryString );
      theRepository.NameSpace = new String( theNameSpaceString );
      theRepository.SesameURL = new String( RDFLocation );

      Namespaces = new String( "Instances = <" + theNameSpaceString + ">, " +
                               FixedNamespaces );

      Found = true;

    } catch( java.net.MalformedURLException e ){
      throw new Exception( "Wrong URL" + e.toString() );
    } catch( java.io.IOException e ){
      throw new Exception( "Problem in accessing repository " + e.toString() );
    }

    return Found;
  }

  // This function reads the data structure for all interviews
  private boolean ReadRepository(){

    if( DataBuilt == true ){
      return true;
    }

    P.PrintLn( P.Locator, "Reading Data" );

    QueryResultsTable Results;
    boolean result = true;

    try{

      ReadInterviewees();

      ReadUserModel();

      ReadQuestions();

      ReadOpinions();

      ReadTopics();

      ReadMedia();

      ReadStatements();

      ReadConcepts();

      ReadModifiers();

      ReadPredicates();

      // We read all the interviews
      String queryString =
          "select Interview, Opinion, Question, QuestionText, Interviewee " +
          "from " +
          "{Interview} serql:directType {VoxPopuli:InterviewSegment}; " +
          // This is because Interview segment is considered by Sesame
          // (because of common properties) the same class as VerbalStatement
          //"{Interview} VoxPopuli:partecipant {}, " +
          "VoxPopuli:hasQuestion {Question}; " +
          "VoxPopuli:partecipant {Interviewee}, " +
          "{Question} VoxPopuli:text {QuestionText}, " +
          "[{Interview} VoxPopuli:opinion {Opinion}] " +
          "using namespace " + Namespaces;

      P.PrintLn( P.Query, "Query: " + queryString );

      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      Data.InterviewsArray = new Interview[Results.getRowCount()];

      for( int i = 0; result && ( i < Results.getRowCount() ); i++ ){
        Interview aSelectedInterview = new Interview();
        aSelectedInterview.Id = new String( Results.getValue( i, 0 ).toString() );
        aSelectedInterview.OpinionId = new String( Results.getValue( i, 1 ) != null ?
            Results.getValue( i, 1 ).
            toString() : "" );
        aSelectedInterview.thequestion = new Question( ( Results.getValue( i, 2 ) != null ?
            Results.getValue( i, 2 ).toString() : "" ),
            ( Results.getValue( i, 3 ) != null ?
              Results.getValue( i, 3 ).toString() : "" )
            );

        aSelectedInterview.theInterviewee = new String( Results.getValue( i, 4 ).toString() );

        P.PrintLn( P.Query, "Query Result: " + aSelectedInterview.Id + " " +
                 aSelectedInterview.OpinionId + " " +
                 aSelectedInterview.theInterviewee );

        Data.InterviewsArray[i] = aSelectedInterview;

        // This reads the interviews data structure
        Data.InterviewsArray[i].MediaItems = ReadMediaID( Data.InterviewsArray[i].Id );

        Data.AddDataToMedia( Data.InterviewsArray[i].MediaItems, "Q: " + Data.InterviewsArray[i].thequestion.QuestionText,
                             Data.InterviewsArray[i].Id, null, true );
        Data.InterviewsArray[i].Arguments = ReadRhetoric( Data.InterviewsArray[i].Id, "rhetoricalForm", i );

      }

    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
      result = false;
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
      result = false;
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Interviews " + e.toString() );
      result = false;
    } catch( Exception e ){
      P.PrintLn( P.Err, "Error in reading Interviews data " + e.toString() );
      e.printStackTrace();
      result = false;
    }

    return result;
  }

  private void ReadStatements() throws
      Exception{

    if( Data.Statements == null ){
      Data.Statements = new Hashtable();
    }
    VerbalStatement aStatement;

    // This function can be reused whenever statements need to be read

    String queryString =
        "select X, Subject, Modifier, Predicate, " +
        "SubjectDescription, PredicateDescription, ModifierDescription, Explicit, " +
        "Interviewee " +
        "from " +
        "{X} VoxPopuli:subject {Subject}; " +
        "VoxPopuli:explicit {Explicit}; " +
        "VoxPopuli:claimer {Interviewee}; " +
        "[VoxPopuli:modifier {Modifier}]; " +
        "[VoxPopuli:predicate {Predicate}]; " +
        "[VoxPopuli:subject {Subject} VoxPopuli:partDescription {SubjectDescription}]; " +
        "[VoxPopuli:modifier {Modifier} VoxPopuli:partDescription {ModifierDescription}]; " +
        "[VoxPopuli:predicate {Predicate} VoxPopuli:partDescription {PredicateDescription}], " +
        "{Interviewee} VoxPopuli:description {Description} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery(
          QueryLanguage.SERQL, queryString );

      for( int i = 0; i < Results.getRowCount(); i++ ){
        aStatement = new VerbalStatement();

        // This will be used as a key
        aStatement.Id = new String( Results.getValue( i, 0 ).toString() );

        aStatement.Subject = new String( Results.getValue( i, 1 ) != null ?
                                         Results.getValue( i, 1 ).toString() :
                                         "" );
        if( Results.getValue( i, 7 ) != null ){
          aStatement.Explicit = Results.getValue( i, 7 ).toString().equals( "true" ) ?
              true : false;
        }

        aStatement.SubjectDescription = new String( Results.getValue( i, 4 ) != null ?
            Results.getValue( i, 4 ).
            toString() : "" );
        aStatement.Modifier = new String( Results.getValue( i, 2 ) != null ?
                                          Results.getValue( i, 2 ).toString() :
                                          "" );
        aStatement.ModifierDescription = new String( Results.getValue( i, 6 ) != null ?
            Results.getValue( i, 6 ).
            toString() : "" );
        aStatement.Predicate = new String( Results.getValue( i, 3 ) != null ?
                                           Results.getValue( i, 3 ).toString() :
                                           "" );
        aStatement.PredicateDescription = new String( Results.getValue( i, 5 ) != null ?
            Results.getValue( i, 5 ).toString() : "" );
        /*
              aStatement.Object = new String( Results.getValue( 0, 3 ) != null ?
                                              Results.getValue( 0, 3 ).toString() : "" );
              aStatement.ObjectDescription = new String( Results.getValue( 0, 7 ) != null ?
                                                         Results.getValue( 0, 7 ).
                                                         toString() : "" );
         */
        aStatement.ClaimerId = new String( Results.getValue( i, 8 ).toString() );

        String Temp = "S: " +
            ( !aStatement.SubjectDescription.equals( "" ) ?
              aStatement.SubjectDescription + " " : "" ) +
            ( !aStatement.ModifierDescription.equals( "" ) ?
              aStatement.ModifierDescription + " " : "" ) +
            ( !aStatement.PredicateDescription.equals( "" ) ?
              aStatement.PredicateDescription + " " : "" );
        /*          ( !aStatement.ObjectDescription.equals( "" ) ?
                    aStatement.ObjectDescription + " " : "" ) +

                     "T: " + aStatement.ParentNode.ToulminType.Name + " " +
                     "C: " + aStatement.Claimer.Description;*/

        P.PrintLn( P.Query, "Query Result: " + Temp );

        // Read the segments
        aStatement.theMediaItem = ReadMediaID( aStatement.Id );

        Data.AddDataToMedia( aStatement.theMediaItem, Temp, null, aStatement.Id, false );

        Data.Statements.put( aStatement.Id, aStatement );
      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return ;
  }

  public void SuggestLinks() throws Exception{

    ConnectActions = new NameIndex[1];
    ConnectActions[0] = new NameIndex( RhetActStrings[ASSOCIATE] );

    this.SetIterations( 3 );

    ReadConcepts();

    ReadModifiers();

    ReadPredicates();

    ReadStatements();

    CreateRelations();

    PrintHeader(); // for the stats file

    NetworkStatements();

    // Link the statements
    if( !LinksEvaluation() ){
      P.PrintLn( P.Err, "Link Evaluation failed " );
    }

  }

  private void NetworkStatements() throws Exception{

    P.PrintLn( P.Locator, "Network Statements " );

    Hashtable[] Connected = new Hashtable[IterationsNr + 1];

    Hashtable[] DataHash = new Hashtable[3];
    DataHash[0] = Data.Concepts;
    DataHash[1] = Data.Modifiers;
    DataHash[2] = Data.Predicates;

    int counter = 0;
    for( Enumeration enu = Data.Statements.elements(); enu.hasMoreElements(); ){

      VerbalStatement vStatement = ( VerbalStatement )enu.nextElement();

      P.Print( P.StatementStat, counter + SEPARATOR );
      P.Print( P.StatementStat, vStatement.SubjectDescription + " " +
               vStatement.ModifierDescription + " " +
               vStatement.PredicateDescription +
               SEPARATOR );

      ConstructedStatement cStatement = new ConstructedStatement( vStatement );

      Connected[0] = new Hashtable( 1 );

      Connected[0].put( cStatement.Subject + cStatement.Modifier + cStatement.Predicate, cStatement );

      for( int index = 1; index < IterationsNr + 1; index++ ){

        Connected[index] = new Hashtable( Data.Predicates.size() * Data.Modifiers.size() * Data.Concepts.size() );

        OneStepLinking( index, DataHash, Connected );

      }

      // Print increment in Stat file
      for( int index = 1; index < IterationsNr + 1; index++ ){
        P.Print( P.StatementStat, ( Connected[index].size() - Connected[index - 1].size() ) + SEPARATOR );
      }
      // Add the self relation
      // This is done otherwise all relations chains have self has the start
      cStatement.LinkType = new MyArrayList( 1 );
      cStatement.LinkType.add( new RelationDescription() );

      // Re add the statement to the list overwriting the previous one
      // NO NEED, WE HAVE THE POINTER
      //Connected[0].put( cStatement.Subject + cStatement.Modifier + cStatement.Predicate, cStatement );

      int size = 0; // we will have at least one statement
      for( int i = 0; i < IterationsNr + 1; i++ ){
        size = size + Connected[i].size();
      }

      P.Print( P.StatementStat, size + SEPARATOR );

      if( !LinkStatements( vStatement, Connected ) ){
        throw new Exception( "Error linking Statements " );
      }

      if( vStatement.ConnectedStatements != null ){
        P.PrintLn( P.StatementLog, "Length connected statements: " + vStatement.ConnectedStatements.size() );
        P.PrintLn( P.StatementStat, vStatement.ConnectedStatements.size() + "" );
      } else{
        P.PrintLn( P.StatementLog, "ZERO Length of connected statements " );
        P.PrintLn( P.StatementStat, "0" );
      }

      counter++;
    }

    return;

  }

  private void OneStepLinking( int index, Hashtable[] DataHash, Hashtable[] Connected ) throws Exception{

    Hashtable[] Loop = new Hashtable[index];

    // Avoid elaborating statements at iteration i that were
    // generated at iteration i-1
    for( int i = 0; i < index; i++ ){
      Loop[i] = new Hashtable( Connected[i] );
    }

    for( int i = 0; i < index; i++ ){

      for( Enumeration e = Loop[i].elements(); e.hasMoreElements(); ){

        ConstructedStatement theStatement = ( ConstructedStatement )e.nextElement();
        ConstructedStatement aStatement;

        for( Enumeration dataenum = DataHash[index - 1].elements(); dataenum.hasMoreElements(); ){

          String[] aEntity = ( String[] )dataenum.nextElement();

          String a = null, b = null, c = null, d = null, part = null, Id = null;

          if( index == 1 ){
            a = theStatement.Subject;
            b = theStatement.SubjectDescription;
            part = "Subject";
          } else if( index == 2 ){
            a = theStatement.Modifier;
            b = theStatement.ModifierDescription;
            part = "Modifier";
          } else if( index == 3 ){
            a = theStatement.Predicate;
            b = theStatement.PredicateDescription;
            part = "Predicate";
          }

          if( aEntity[0].equals( a ) ){
            continue;
          }

          aStatement = new ConstructedStatement( theStatement );

          if( index == 1 ){
            aStatement.Subject = new String( aEntity[0] );
            aStatement.SubjectDescription = new String( aEntity[1] );
            c = aStatement.Subject;
            d = aStatement.SubjectDescription;
            Id = "SUBJECT";
          } else if( index == 2 ){
            aStatement.Modifier = new String( aEntity[0] );
            aStatement.ModifierDescription = new String( aEntity[1] );
            c = aStatement.Modifier;
            d = aStatement.ModifierDescription;
            Id = "MODIFIER";
          } else if( index == 3 ){
            aStatement.Predicate = new String( aEntity[0] );
            aStatement.PredicateDescription = new String( aEntity[1] );
            c = aStatement.Predicate;
            d = aStatement.PredicateDescription;
            Id = "PREDICATE";
          }

          // Here we add the new link because every statement in the IN list
          // is relative to the original statement
          RelationDescription aRelationDescription = new RelationDescription( part,
              RhetActStrings[ASSOCIATE], a, b, c, d, Id );

          aStatement.LinkType.add( aRelationDescription );

          if( !Data.Relations.containsKey( aRelationDescription.FromPartId + aRelationDescription.ToPartId ) ){
            throw new Exception( "Link not present " );
          }

          // put it in the connected statements
          Connected[i + 1].put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );

        }
      }
    }

  }

  private void CreateRelations() throws
      Exception{

    P.PrintLn( P.Locator, "Creating Relations" );

    if( Data.Relations == null ){
      int i = Data.Concepts.size() * Data.Concepts.size();
      int j = Data.Modifiers.size() * Data.Modifiers.size();
      int z = Data.Predicates.size() * Data.Predicates.size();

      Data.Relations = new Hashtable( i + j + z + 1 );
    }
    String[] Entity1, Entity2;

    for( Enumeration e = Data.Concepts.elements(); e.hasMoreElements(); ){
      Entity1 = ( String[] )e.nextElement();

      for( Enumeration ee = Data.Concepts.elements(); ee.hasMoreElements(); ){
        Entity2 = ( String[] )ee.nextElement();

        if( !Entity1[0].equals( Entity2[0] ) ){
          RelationDescription a = new RelationDescription( "Subject",
              RhetActStrings[ASSOCIATE],
              Entity1[0],
              Entity1[1],
              Entity2[0],
              Entity2[1],
              "SUBJECT" );
          Data.Relations.put( a.FromPartId + a.ToPartId, a );
        }
      }
    }

    for( Enumeration e = Data.Modifiers.elements(); e.hasMoreElements(); ){
      Entity1 = ( String[] )e.nextElement();

      for( Enumeration ee = Data.Modifiers.elements(); ee.hasMoreElements(); ){
        Entity2 = ( String[] )ee.nextElement();

        if( !Entity1[0].equals( Entity2[0] ) ){
          RelationDescription a = new RelationDescription( "Modifier",
              RhetActStrings[ASSOCIATE],
              Entity1[0],
              Entity1[1],
              Entity2[0],
              Entity2[1],
              "MODIFIER" );
          Data.Relations.put( a.FromPartId + a.ToPartId, a );
        }
      }
    }

    for( Enumeration e = Data.Predicates.elements(); e.hasMoreElements(); ){
      Entity1 = ( String[] )e.nextElement();

      for( Enumeration ee = Data.Predicates.elements(); ee.hasMoreElements(); ){
        Entity2 = ( String[] )ee.nextElement();

        if( !Entity1[0].equals( Entity2[0] ) ){
          RelationDescription a = new RelationDescription( "Predicate",
              RhetActStrings[ASSOCIATE],
              Entity1[0],
              Entity1[1],
              Entity2[0],
              Entity2[1],
              "PREDICATE" );
          Data.Relations.put( a.FromPartId + a.ToPartId, a );
        }
      }
    }

    //Add the self link (distance zero)
    RelationDescription a = new RelationDescription();
    Data.Relations.put( a.FromPartId + a.ToPartId, a );

    return;
  }

  private boolean ProcessStatements() throws
      Exception{

    boolean result = true;

    if( ( DataBuilt == true ) && ( SemGraphBuilt == true ) ){
      return true;
    }

    if( ( Data.Statements == null ) ||
        ( Data.Statements.size() == 0 ) ||
        ( DataBuilt == false )
        ){
      return false;
    }

    P.PrintLn( P.Locator, "Process Statements" );

    // Read all relations
    String queryString = "select Conc1, Des1, Conc2, Des2, X " +
        "from " +
        "{Conc1} X {Conc2}, " +
        "{Conc1} rdf:type {VoxPopuli:RhetoricalStatementPart}, " +
        "{Conc1} VoxPopuli:partDescription {Des1}, " +
        "{Conc2} rdf:type {VoxPopuli:RhetoricalStatementPart}, " +
        "{Conc2} VoxPopuli:partDescription {Des2} " +
        "where " +
        "X=VoxPopuli:generalization or " +
        "X=VoxPopuli:specialization or " +
        "X=VoxPopuli:opposite or " +
        "X=VoxPopuli:similar " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{

      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      int results = Results.getRowCount();

      Data.Relations = new Hashtable( results + 1 );

      for( int j = 0; j < results; j++ ){

        RelationDescription a = new RelationDescription( "NotUsed", RhetActStrings[SELF],
            Results.getValue( j, 0 ).toString(),
            Results.getValue( j, 1 ).toString(),
            Results.getValue( j, 2 ).toString(),
            Results.getValue( j, 3 ).toString(),
            Results.getValue( j, 4 ).toString() );
        Data.Relations.put( a.FromPartId + a.ToPartId, a );
      }

      //Add the self link (distance zero)
      RelationDescription a = new RelationDescription();
      Data.Relations.put( a.FromPartId + a.ToPartId, a );

    } catch( AccessDeniedException ex ){
      throw new Exception( "Exception in Linking Statements " + ex.toString() );
    } catch( QueryEvaluationException ex ){
      throw new Exception( "Exception in Linking Statements " + ex.toString() );
    } catch( MalformedQueryException ex ){
      throw new Exception( "Exception in Linking Statements " + ex.toString() );
    } catch( IOException ex ){
      throw new Exception( "Exception in Linking Statements " + ex.toString() );
    }

    // Print statistics for linked statements
    PrintHeader();

    // This loop links all related statements together
    int counter = 0;
    for( Enumeration e = Data.Statements.elements(); result && e.hasMoreElements(); ){
      VerbalStatement aStatement = ( VerbalStatement )e.nextElement();

      P.Print( P.StatementStat, counter + SEPARATOR );
      P.Print( P.StatementStat, aStatement.SubjectDescription + " " +
               aStatement.ModifierDescription + " " +
               aStatement.PredicateDescription +
               SEPARATOR );

      P.PrintLn( P.Locator, "Processing Statement " + counter++ );

      result = ConnectStatements( aStatement );
    }

    P.PrintLn( P.Locator, "Evaluating links " );

    if( !LinksEvaluation() ){
      P.PrintLn( P.Err, "Link Evaluation failed " );
    }

    return result;
  }

  private void PrintHeader(){
    // Build statistics for linked statements
    P.Print( P.StatementStat, "Stat Nr" + SEPARATOR );
    P.Print( P.StatementStat, "Stat" + SEPARATOR );
    for( int j = 0; j < IterationsNr; j++ ){

      for( int i = 0; i < ConnectActions.length; i++ ){
        P.Print( P.StatementStat, ConnectActions[i].Name + ( j + 1 ) + SEPARATOR );
      }
    }
    P.Print( P.StatementStat, "Tot Gen" + SEPARATOR );

    // We take into account also the original statement
    P.Print( P.StatementStat, "SELF " + SEPARATOR );
    P.Print( P.StatementStat, "Red SELF " + SEPARATOR );

    for( int i = 1; i < IterationsNr + 1; i++ ){
      P.Print( P.StatementStat, "LL = " + i + SEPARATOR );
      P.Print( P.StatementStat, "Red LL = " + i + SEPARATOR );
    }

    P.PrintLn( P.StatementStat, "Tot Retr" );

  }

  private boolean LinksEvaluation() throws Exception{

    int size = Data.Relations.size(), count = 0;

    if( Data.Relations == null || size == 0 ){
      return false;
    }

    P.PrintLn( P.StatementStat );
    P.Print( P.StatementStat, "From" + SEPARATOR );
    P.Print( P.StatementStat, "Rel" + SEPARATOR );
    P.Print( P.StatementStat, "To" + SEPARATOR );
    P.Print( P.StatementStat, "Pos Score" + SEPARATOR );
    P.Print( P.StatementStat, "Neg Score" + SEPARATOR );
    P.Print( P.StatementStat, "Hit Score" + SEPARATOR );
    P.PrintLn( P.StatementStat, "Miss Score" );

    for( Enumeration e = Data.Relations.elements(); e.hasMoreElements(); ){
      count++;

      RelationDescription aRelDes = ( RelationDescription )e.nextElement();
      String msg = aRelDes.FromPartDes + SEPARATOR +
          aRelDes.Relation + SEPARATOR +
          aRelDes.ToPartDes + SEPARATOR +
          aRelDes.PSharedscore + SEPARATOR +
          aRelDes.NSharedscore + SEPARATOR +
          aRelDes.HITscore + SEPARATOR +
          aRelDes.MISSscore;

      P.PrintLn( P.StatementStat, msg );

    }

    return true;
  }

  private boolean ConnectStatements( VerbalStatement theStatement ) throws Exception{

    // Initialize the hash table
    Hashtable Hashtables[] = new Hashtable[IterationsNr + 1];
    for( int i = 0; i < IterationsNr + 1; i++ ){
      Hashtables[i] = new Hashtable();
    }

    // Construct the first statement
    ConstructedStatement aStatement = new ConstructedStatement( theStatement );

    Hashtables[0].put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );

    for( int i = 0; i < IterationsNr; i++ ){

      // Augment the set of statements that can be contrasted or supported by
      // using support, opposite, generalization and specialization
      OneStepStatementManipulation( Hashtables[i], Hashtables[i + 1], ( i > 0 ? Hashtables[i - 1] : null ) );
    }

    // Now add the correct relation type (SELF) to the original statement
    // to avoid every linked statement to have SELF (I think)
    aStatement.LinkType = new MyArrayList( 1 );
    aStatement.LinkType.add( new RelationDescription() );

    // Re add the statement to the list overwriting the previous one
    // NO NEED, WE HAVE THE POINTER
    //Hashtables[0].put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );

    int size = 0; // we will have at least one statement
    for( int i = 0; i < IterationsNr + 1; i++ ){
      size = size + Hashtables[i].size();
      PrintConnectedStatement( Hashtables[i], false );
    }

    P.PrintLn( P.StatementLog, "Length constructed statements " + size );
    P.Print( P.StatementStat, size + SEPARATOR );

    // Link the statements
    if( !LinkStatements( theStatement, Hashtables ) ){
      throw new Exception( "Error linking Statements " );
    }

    if( theStatement.ConnectedStatements != null ){
      P.PrintLn( P.StatementLog, "Length connected statements: " + theStatement.ConnectedStatements.size() );
      P.PrintLn( P.StatementStat, theStatement.ConnectedStatements.size() + "" );
      PrintConnectedStatement( theStatement.ConnectedStatements, true );
    } else{
      P.PrintLn( P.StatementLog, "ZERO Length of connected statements " );
      P.PrintLn( P.StatementStat, "0" );
    }

    return true;
  }

  // here we do the actual linking: theStatement is the starting statement, StatementArray
  // are all the derived statements
  private boolean LinkStatements( VerbalStatement theStatement, Hashtable[] StatementsHashArray ) throws
      Exception{

    // The statement to be linked to, this is verbally the same as one instance
    // of a constructed statement contained in StatementArray
    VerbalStatement VerbStatementToLink;

    int lnkArrSize = IterationsNr + 1;

    // The following variable is used to hold the length of the link chain
    // when a hit is found
    int[] LinkLength = new int[lnkArrSize];
    int[] RedundLinkLength = new int[lnkArrSize];

    for( int i = 0; i < lnkArrSize; i++ ){
      LinkLength[i] = 0;
      RedundLinkLength[i] = 0;
    }

    // We try to find statements based on the relation distance from the
    // given one, so first the same (dist=0) and then on.
    for( int i = 0; i < IterationsNr + 1; i++ ){

      // The array contains statements that only have the s/m/p filled in
      // We still have to see if they exist or not, and we query the repository to see if they do
      // which should be the fastest method (or the easiest) instead of looking in
      // the hash table of all statements
      for( Enumeration e = StatementsHashArray[i].elements(); e.hasMoreElements(); ){

        ConstructedStatement aConstructedStatement = ( ConstructedStatement )e.nextElement();

        int linksz = aConstructedStatement.LinkType.size();
        if( ( i != 0 ) && !( linksz == i ) ){
          throw new Exception( "Link length not correct, " + linksz + " should be " + ( i + 1 ) );
        }

        String queryString = "select Id  " +
            "from " +
            "{Id} rdf:type {VoxPopuli:VerbalStatement} " +
            ", {Id} VoxPopuli:subject  {<" + aConstructedStatement.Subject + ">}" +
            ", {Id} VoxPopuli:modifier {<" + aConstructedStatement.Modifier + ">}" +
            ", {Id} VoxPopuli:predicate {<" + aConstructedStatement.Predicate + ">}" +
            " using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        QueryResultsTable Results = null;
        try{

          Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
              queryString );
          int results = Results.getRowCount();

          if( results == 0 ){
            // Here we calculate the penalty for the relations for not having found a statement
            for( int lnkcnt = 0; lnkcnt < linksz; lnkcnt++ ){
              RelationDescription a = ( RelationDescription )aConstructedStatement.LinkType.get( lnkcnt );

              if( !( Data.Relations.containsKey( a.FromPartId + a.ToPartId ) ) ){
                throw new Exception( "Link not present " );
              }
              RelationDescription b = ( RelationDescription )Data.Relations.get( a.FromPartId + a.ToPartId );
              b.NSharedscore = b.NSharedscore - ( 1. / linksz );
              b.MISSscore = b.MISSscore - 1;
              // This should overwrite the old value (with the old score)
              Data.Relations.put( b.FromPartId + b.ToPartId, b );
            }
          } else{
            for( int j = 0; j < results; j++ ){
              // Apparently such a statement exists, so it MUST be in the hash table
              // VerbStatementToLink will be a fully filled in statement
              VerbStatementToLink = null;
              if( ( VerbStatementToLink = ( VerbalStatement )Data.Statements.get( Results.getValue( j,
                  0 ).toString() ) ) == null ){
                throw new Exception( "Missing statement in Hash Table " +
                                     Results.getValue( j, 0 ).toString() );
              }
              // only link if the statements are not the same
              if( !VerbStatementToLink.Id.equals( theStatement.Id ) ){

                // Here we calculate the reward for the links for having found a statement
                for( int relcnt = 0; relcnt < linksz; relcnt++ ){
                  RelationDescription a = ( RelationDescription )aConstructedStatement.LinkType.get( relcnt );
                  if( !( Data.Relations.containsKey( a.FromPartId + a.ToPartId ) ) ){
                    throw new Exception( "Link not present " );
                  }
                  RelationDescription b = ( RelationDescription )Data.Relations.get( a.FromPartId + a.ToPartId );
                  b.PSharedscore = b.PSharedscore + ( 1. / linksz );
                  b.HITscore = b.HITscore + 1;
                  // This should overwrite the old value (with the old score)
                  Data.Relations.put( b.FromPartId + b.ToPartId, b );
                }
                // Allocate the related statements structure
                if( theStatement.ConnectedStatements == null ){
                  theStatement.ConnectedStatements = new Hashtable();
                }

                // We increase the counter even if the statement might be already linked,
                // just to see if there are loops
                RedundLinkLength[i]++;

                // Link the statements, using the Id from the RDF instance, which
                // should be unique
                // We link the one we have to the one we found

                if( !theStatement.ConnectedStatements.containsKey( VerbStatementToLink.Id ) ){

                  P.PrintLn( P.StatementLog, "Linking " + theStatement.Id + " to " + VerbStatementToLink.Id +
                           " with " + aConstructedStatement.LinkType.PrintLinkList() );
                  Link a = new Link();
                  a.StatementId = new String( VerbStatementToLink.Id );
                  a.LinkType = new MyArrayList( aConstructedStatement.LinkType );
                  theStatement.ConnectedStatements.put( a.StatementId, a );

                  // increase the counter of the corresponding link length
                  LinkLength[i]++;
                }
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
    }
    for( int i = 0; i < LinkLength.length; i++ ){
      P.Print( P.StatementStat, LinkLength[i] + SEPARATOR );
      P.Print( P.StatementStat, RedundLinkLength[i] - LinkLength[i] + SEPARATOR );
    }

    return true;
  }

  private void OneStepStatementManipulation( Hashtable INStats, Hashtable OUTStats, Hashtable ExisStats ) throws
      Exception{
    // This function implements the basic logic of supporting a statement
    // and produces a list (hash table) of possible pro statements

    QueryResultsTable Results;
    String Relation;
    boolean result = true;
    int oldsize = 0;

    for( int i = 0; i < ConnectActions.length; i++ ){

      int action = ConnectActions[i].index;

      switch( action ){
        case ATTACK:
          Relation = "VoxPopuli:opposite";
          break;
        case SUPPORT:
          Relation = "VoxPopuli:similar";
          break;
        case GENERALIZE:
          Relation = "VoxPopuli:generalization";
          break;
        case SPECIALIZE:
          Relation = "VoxPopuli:specialization";
          break;
        case ASSOCIATE:
          Relation = "VoxPopuli:association";
          break;
        default:
          throw new Exception( "Unknown action to handle statements " );
      }

      String Message = RhetActStrings[action].Name + " ";

      try{
        for( Enumeration e = INStats.elements(); e.hasMoreElements(); ){

          ConstructedStatement theStatement = ( ConstructedStatement )e.nextElement();
          ConstructedStatement aStatement;

          if( !theStatement.Subject.equals( "" ) ){
            String queryString = "select Subject, SubjectDescription " +
                "from " +
                "{<" + theStatement.Subject + ">} " + Relation + " {Subject}; " +
                Relation +
                " {Subject} VoxPopuli:partDescription {SubjectDescription} " +
                "using namespace " + Namespaces;

            P.PrintLn( P.Query, "Query: " + queryString );

            Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
                queryString );

            int limit = Results.getRowCount();

            for( int j = 0; j < limit; j++ ){

              aStatement = new ConstructedStatement( theStatement );

              aStatement.Subject = new String( Results.getValue( j, 0 ) != null ?
                                               Results.getValue( j, 0 ).toString() :
                                               "" );
              aStatement.SubjectDescription = new String( Results.getValue( j, 1 ) != null ?
                  Results.getValue( j, 1 ).toString() : "" );

              // Here we add the new link because every statement in the IN list
              // is relative to the original statement
              RelationDescription aRelationDescription = new RelationDescription( "Subject", RhetActStrings[action],
                  theStatement.Subject,
                  theStatement.SubjectDescription,
                  aStatement.Subject,
                  aStatement.SubjectDescription,
                  Relation );
              aStatement.LinkType.add( aRelationDescription );

              if( !Data.Relations.containsKey( aRelationDescription.FromPartId + aRelationDescription.ToPartId ) ){
                throw new Exception( "Link not present " );
              }

              P.PrintLn( P.Log1, "Constructing " + Message + "Subject: " +
                         aStatement.SubjectDescription + " " +
                         aStatement.ModifierDescription + " " +
                         aStatement.PredicateDescription /*+ " " + aStatement.ObjectDescription */ );

              // Prevent going back to the original statement in one step
              if( ExisStats == null ){
                OUTStats.put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );
              } else if( !ExisStats.contains( aStatement.Subject + aStatement.Modifier + aStatement.Predicate ) ){
                OUTStats.put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );
              }
            }
          }
          if( theStatement.Modifier.equals( "" ) ){

            throw new Exception( " Modifier can not be null " );

          } else{

            String queryString = "select Modifier, ModifierDescription " +
                "from " +
                "{<" + theStatement.Modifier + ">} " +
                Relation + " {Modifier}; " +
                Relation +
                " {Modifier} VoxPopuli:partDescription {ModifierDescription} " +
                "using namespace " + Namespaces;

            P.PrintLn( P.Query, "Query: " + queryString );

            Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
                queryString );

            int limit = Results.getRowCount();

            for( int j = 0; j < limit; j++ ){

              if( Results.getValue( j, 0 ) != null ){
                aStatement = new ConstructedStatement( theStatement );

                aStatement.Modifier = new String( Results.getValue( j, 0 ).toString() );
                aStatement.ModifierDescription = new String( Results.getValue( j, 1 ).
                    toString() );

                // Here we add the new link because every statement in the IN list
                // is relative to the original statement
                RelationDescription aRelationDescription = new RelationDescription( "Modifier", RhetActStrings[action],
                    theStatement.Modifier,
                    theStatement.ModifierDescription,
                    aStatement.Modifier,
                    aStatement.ModifierDescription,
                    Relation );

                aStatement.LinkType.add( aRelationDescription );

                if( !Data.Relations.containsKey( aRelationDescription.FromPartId + aRelationDescription.ToPartId ) ){
                  throw new Exception( "Link not present " );
                }

                // Prevent going back to the original statement in one step
                if( ExisStats == null ){
                  OUTStats.put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );
                } else if( !ExisStats.contains( aStatement.Subject + aStatement.Modifier + aStatement.Predicate ) ){
                  OUTStats.put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );
                }
              }
            }
          }

          if( !theStatement.Predicate.equals( "" ) ){

            String queryString = "select Predicate, PredicateDescription " +
                "from " +
                "{<" + theStatement.Predicate + ">} " +
                Relation + " {Predicate}; " +
                Relation +
                " {Predicate} VoxPopuli:partDescription {PredicateDescription} " +
                "using namespace " + Namespaces;

            P.PrintLn( P.Query, "Query: " + queryString );

            Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
                queryString );

            int limit = Results.getRowCount();

            for( int j = 0; j < limit; j++ ){
              aStatement = new ConstructedStatement( theStatement );

              aStatement.Predicate = new String( Results.getValue( j, 0 ) != null ?
                                                 Results.getValue( j, 0 ).toString() :
                                                 "" );
              aStatement.PredicateDescription = new String( Results.getValue( j, 1 ) != null ?
                  Results.getValue( j, 1 ).toString() : "" );

              // Here we add the new link because every statement in the IN list
              // is relative to the original statement
              RelationDescription aRelationDescription = new RelationDescription( "Predicate", RhetActStrings[action],
                  theStatement.Predicate,
                  theStatement.PredicateDescription,
                  aStatement.Predicate,
                  aStatement.PredicateDescription,
                  Relation );

              aStatement.LinkType.add( aRelationDescription );

              if( !Data.Relations.containsKey( aRelationDescription.FromPartId + aRelationDescription.ToPartId ) ){
                throw new Exception( "Link not present " );
              }

              P.PrintLn( P.Log1, "Constructing " + Message + "Predicate: " +
                         aStatement.SubjectDescription + " " +
                         aStatement.ModifierDescription + " " +
                         aStatement.PredicateDescription /* + " " + aStatement.ObjectDescription */ );

              // Prevent going back to the original statement in one step
              if( ExisStats == null ){
                OUTStats.put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );
              } else if( !ExisStats.contains( aStatement.Subject + aStatement.Modifier + aStatement.Predicate ) ){
                OUTStats.put( aStatement.Subject + aStatement.Modifier + aStatement.Predicate, aStatement );
              }
            }
          }
          /*
                  if( !theStatement.Object.equals( "" ) ){
                    String queryString = "select Object, ObjectDescription " +
                        "from " +
                        "{<" + theStatement.Object + ">} " +
                        Relation + " {Object}; " +
                        Relation +
                        " {Object} <VoxPopuli:partDescription> {ObjectDescription} " +
                        "using namespace " + Namespaces;

                    P.PrintLn(P.Query, "Query: " + queryString );

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
      } catch( MalformedQueryException e ){
        result = false;
        P.PrintLn( P.Err, "Error in Query " + e.toString() );
      } catch( AccessDeniedException e ){
        result = false;
        P.PrintLn( P.Err, "Error in Query " + e.toString() );
      } catch( Exception e ){
        result = false;
        P.PrintLn( P.Err, "Error in manipulationg the statements " + e.toString() );
      }
      if( !result ){
        throw new Exception( "Error expanding Statements with " + ConnectActions[i].Name );
      }
      int size = OUTStats.size();
      P.Print( P.StatementStat, size - oldsize + SEPARATOR );
      if( size - oldsize > 0 ){
        P.PrintLn( P.StatementLog, "Increase with " + ConnectActions[i].Name + " of " + ( size - oldsize ) );
      } else{
        P.PrintLn( P.StatementLog, "No Increase with " + ConnectActions[i].Name );
      }
      oldsize = size;
    }
  }

  private void ReadMedia() throws Exception{

    // Read media related information
    ReadVideoSegments();

    ReadAudioSegments();

    ReadStillSegments();

    ReadTextSegments();

    return;
  }

  private void ReadVideoSegments() throws Exception{

    QueryResultsTable Results;

    // Read media related information
    String queryString =
        "select Src, BeginFrame, EndFrame, MediaDescription, Language, Quality, " +
        "StartFraming, EndFraming, Gaze, Motion, Time, Location, Place, Subject, X " +
        "from " +
        "{X} serql:directType {VoxPopuli:Video}, " +
        "{X} MediaClipping:beginFrame {BeginFrame}; " +
        "MediaClipping:endFrame {EndFrame}; " +
        "[VoxPopuli:mediaDescription {MediaDescription}]; " +
        "BasicMedia:src {Src}; " +
        "VoxPopuli:language {Language}; " +
        "[VoxPopuli:hasStartFraming {StartFraming}]; " +
        "[VoxPopuli:hasEndFraming {EndFraming}]; " +
        "[VoxPopuli:hasGaze {Gaze}]; " +
        "[VoxPopuli:hasMotion {Motion}]; " +
        "[VoxPopuli:atTime {Time}]; " +
        "[VoxPopuli:hasLocation {Location}]; " +
        "[VoxPopuli:takesPlace {Place}]; " +
        "[VoxPopuli:hasSubject {Subject}]; " +
        "VoxPopuli:quality {Quality} " +

        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
        queryString );

    int l = Results.getRowCount();

    if( l == 0 ){
      return;
    }

    for( int i = 0; i < l; i++ ){
      VideoSegment theVideoSegment = new VideoSegment( Results.getValue( i, 14 ), ( String )null, ( String )null,
          Results.getValue( i, 1 ),
          Results.getValue( i, 2 ),
          Results.getValue( i, 0 ),
          Results.getValue( i, 3 ),
          Results.getValue( i, 4 ),
          Results.getValue( i, 5 ),
          Results.getValue( i, 13 ),
          Results.getValue( i, 6 ),
          Results.getValue( i, 7 ),
          Results.getValue( i, 8 ),
          Results.getValue( i, 9 ),
          Results.getValue( i, 11 ),
          Results.getValue( i, 12 ),
          Results.getValue( i, 10 ) );

      if( Data.Videos == null ){
        Data.Videos = new Hashtable();
      }
      Data.Videos.put( theVideoSegment.Id, theVideoSegment );

    }
    return;
  }

  private String[] ReadMediaID( String Id ){

    QueryResultsTable Results;
    String ResultList[] = null;


    // Read media related information
    String queryString =
        "select X " +
        "from " +
        "{<" + Id + ">} VoxPopuli:hasMedia {X} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      int l = Results.getRowCount();

      if( l == 0 ){
        return null;
      }

      String theMedias[] = new String[l];
      String theOrderedMedias[] = new String[l];

      for( int i = 0; i < l; i++ ){
        theMedias[i] = new String( Results.getValue( i, 0 ).toString() );
        Segment theSeg = (Segment) Data.Videos.get(theMedias[i]);
        if( theSeg instanceof VideoSegment ){
          VideoSegment theVideo = (VideoSegment) theSeg;
          theOrderedMedias[i] = new String( theVideo.FileName + theVideo.BeginFrame + theVideo.EndFrame );
        }else{
          theOrderedMedias[i] = new String(Util.SMALLSTRING);
        }
      }
      Util u = new Util();
      int[] order = u.Order( theOrderedMedias, true );
      ResultList = new String[l];

      for( int i=0; i<l; i++ ){
        ResultList[i] = new String( theMedias[ order[i] ] );
      }


    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
      ResultList = null;
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
      ResultList = null;
    } catch( Exception e ){
      P.PrintLn( P.Err, "Error in manipulationg the statements " + e.toString() );
      ResultList = null;
    }

    return ResultList;

  }

  private void ReadAudioSegments() throws Exception{

    QueryResultsTable Results;

    // Read media related information
    String queryString =
        "select Src, BeginFrame, EndFrame, MediaDescription, Language, Quality, " +
        "Time, Location, Place, Subject, X " +
        "from " +
        "{X} serql:directType {VoxPopuli:Audio}, " +
        "{X} MediaClipping:beginFrame {BeginFrame}; " +
        "MediaClipping:endFrame {EndFrame}; " +
        "[VoxPopuli:mediaDescription {MediaDescription}]; " +
        "BasicMedia:src {Src}; " +
        "VoxPopuli:language {Language}; " +
        "VoxPopuli:atTime {Time}; " +
        "VoxPopuli:hasLocation {Location}; " +
        "VoxPopuli:takesPlace {Place}; " +
        "VoxPopuli:hasSubject {Subject}; " +
        "VoxPopuli:quality {Quality} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
        queryString );

    int l = Results.getRowCount();

    if( l == 0 ){
      return;
    }

    for( int i = 0; i < l; i++ ){
      AudioSegment theAudioSegment = new AudioSegment( Results.getValue( i, 10 ), null, null,
          Results.getValue( i, 1 ),
          Results.getValue( i, 2 ),
          Results.getValue( i, 0 ),
          Results.getValue( i, 3 ),
          Results.getValue( i, 4 ),
          Results.getValue( i, 5 ),
          Results.getValue( i, 9 ),
          Results.getValue( i, 7 ),
          Results.getValue( i, 8 ),
          Results.getValue( i, 6 )
          );

      AudioSegment a;
      if( Data.Audios == null ){
        Data.Audios = new Hashtable();
      }
      Data.Audios.put( theAudioSegment.Id, theAudioSegment );

    }
    return;
  }

  private void ReadStillSegments() throws Exception{

    QueryResultsTable Results;

    // Read media related information
    String queryString =
        "select Src, MediaDescription, Language, Quality, " +
        "Time, Location, Place, Subject, X " +
        "from " +
        "{X} serql:directType {VoxPopuli:Image}, " +
        "{X} BasicMedia:src {Src}; " +
        "[VoxPopuli:mediaDescription {MediaDescription}]; " +
        "VoxPopuli:language {Language}; " +
        "[VoxPopuli:atTime {Time}]; " +
        "[VoxPopuli:hasLocation {Location}]; " +
        "[VoxPopuli:takesPlace {Place}]; " +
        "[VoxPopuli:hasSubject {Subject}]; " +
        "VoxPopuli:quality {Quality} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
        queryString );

    int l = Results.getRowCount();

    if( l == 0 ){
      return;
    }

    for( int i = 0; i < l; i++ ){
      StillSegment theStillSegment = new StillSegment( Results.getValue( i, 8 ), null, null,
          Results.getValue( i, 0 ),
          Results.getValue( i, 1 ),
          Results.getValue( i, 2 ),
          Results.getValue( i, 3 ),
          Results.getValue( i, 7 ),
          Results.getValue( i, 5 ),
          Results.getValue( i, 6 ),
          Results.getValue( i, 4 )
          );

      if( Data.Images == null ){
        Data.Images = new Hashtable();
      }
      Data.Images.put( theStillSegment.Id, theStillSegment );

    }

    return;
  }

  private void ReadTextSegments() throws Exception{

    QueryResultsTable Results;

    // Read media related information
    String queryString =
        "select Src, MediaDescription, Language, Quality, Subject, X " +
        "from " +
        "{X} serql:directType {VoxPopuli:Text}, " +
        "{X} BasicMedia:src {Src}; " +
        "[VoxPopuli:mediaDescription {MediaDescription}]; " +
        "VoxPopuli:language {Language}; " +
        "VoxPopuli:hasSubject {Subject}; " +
        "VoxPopuli:quality {Quality} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
        queryString );

    int l = Results.getRowCount();

    if( l == 0 ){
      return;
    }

    for( int i = 0; i < l; i++ ){

      TextSegment theTextSegment = new TextSegment( Results.getValue( i, 5 ), null, null,
          Results.getValue( i, 0 ),
          Results.getValue( i, 1 ),
          Results.getValue( i, 2 ),
          Results.getValue( i, 3 ),
          Results.getValue( i, 4 )

          );

      if( Data.Texts == null ){
        Data.Texts = new Hashtable();
      }
      Data.Texts.put( theTextSegment.Id, theTextSegment );

    }

    return;
  }

  // Read rhetorical information.
  private Argument[] ReadRhetoric( String Id, String Relation, int position ) throws
      Exception{

    Argument[] Arguments = null;

    // Relation is a parameter so that this function can be called
    // recursively, once with "rhetoricalForm" and with "nestedRhetoricalForm"
    String queryString = "select Rhetoric  " +
        "from " +
        "{<" + Id + ">} " + "VoxPopuli:" + Relation + " {Rhetoric} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

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
            "{<" + Results.getValue( i, 0 ).toString() + ">} " +
            "VoxPopuli:toulminRole {Role} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

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
        "from " + "{<" + Id + ">} " +
        "VoxPopuli:toulminType {Node}, " +
        "{Node} serql:directType {NodeType} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

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
        Nodes[i].theInterview = Data.InterviewsArray[position];

        //Read the statements per node
        queryString = "select Statements  " +
            "from " + "{<" + NodeResults.getValue( i, 0 ).toString() + ">} " +
            "VoxPopuli:statements {Statements} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );
        QueryResultsTable StatementsResults = theRepository.Client.
            performTableQuery( QueryLanguage.SERQL, queryString );

        int ll = StatementsResults.getRowCount();

        // There must be statement in a Toulmin Node
        if( ll == 0 ){
          throw new Exception( "Toulmin node with no Statements " );
        }

        Nodes[i].theStatementIDs = new String[ll];

        for( int ii = 0; ii < ll; ii++ ){
          Nodes[i].theStatementIDs[ii] = new String( StatementsResults.getValue( ii, 0 ).toString() );

          VerbalStatement a;
          if( ( a = ( VerbalStatement )Data.Statements.get( Nodes[i].theStatementIDs[ii] ) ) != null ){

            // Set the parent node to know what kind of Toulmin node this statement
            // belongs to
            a.ParentNode = Nodes[i];
            // Add the interview data to the media segment, no need for the statement
            // because we have already done it
            Data.AddDataToMedia( a.theMediaItem, "T: " + a.ParentNode.ToulminType.Name,
                                 Data.InterviewsArray[position].Id, null, false );

          }
        }

        // Read the relation between statements (OR or AND) and the
        // type of link to the rest of the Toulmin structure
        queryString = "select StatementRel, LogicType  " +
            "from " + "{<" + NodeResults.getValue( i, 0 ).toString() + ">} " +
            "VoxPopuli:stmRelation {StatementRel}; " +
            "VoxPopuli:logicType {LogicType} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );
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
        }else if( ll > 1 ){
          // there are more statements but no relations between them
          // assume AND
          Nodes[i].StatementsinAND = true;
        }
      }
    }

    return Nodes;
  }

  // Read all the interviewees

  private void ReadInterviewees() throws
      Exception{

    String queryString =
        "select Interviewee, Description, HasAge, HasEducation, HasJob, HasOrigin, HasRace, HasReligion, HasGender " +
        "from " +
        "{Interviewee} serql:directType {VoxPopuli:InterviewedPerson}; " +
        "VoxPopuli:description {Description}; " +
        "[VoxPopuli:hasAge {HasAge}]; " +
        "[VoxPopuli:hasEducation {HasEducation}]; " +
        "[VoxPopuli:hasJob {HasJob}]; " +
        "[VoxPopuli:hasOrigin {HasOrigin}]; " +
        "[VoxPopuli:hasRace {HasRace}]; " +
        "[VoxPopuli:hasReligion {HasReligion}]; " +
        "[VoxPopuli:hasGender {HasGender}] " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.Interviewees = new Hashtable( count );

      for( int i = 0; i < count; i++ ){

        Person thePerson = new Person( Results.getValue( i, 0 ).toString(),
                                       ( Results.getValue( i, 1 ) != null ? Results.getValue( i, 1 ).toString() : null ),
                                       ( Results.getValue( i, 2 ) != null ? Results.getValue( i, 2 ).toString() : null ),
                                       ( Results.getValue( i, 3 ) != null ? Results.getValue( i, 3 ).toString() : null ),
                                       ( Results.getValue( i, 4 ) != null ? Results.getValue( i, 4 ).toString() : null ),
                                       ( Results.getValue( i, 5 ) != null ? Results.getValue( i, 5 ).toString() : null ),
                                       ( Results.getValue( i, 6 ) != null ? Results.getValue( i, 6 ).toString() : null ),
                                       ( Results.getValue( i, 7 ) != null ? Results.getValue( i, 7 ).toString() : null ),
                                       ( Results.getValue( i, 8 ) != null ? Results.getValue( i, 8 ).toString() : null )
            );


        Data.Interviewees.put( thePerson.Id, thePerson );

      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return ;
  }

  // Read all the interviewees

  private void ReadUserModel() throws
      Exception{


    try{

      String queryString =
          "select ID, UserType " +
          "from " +
          "{ID} serql:directType {VoxPopuli:UserModel}; " +
          "VoxPopuli:userType {UserType} " +
          "using namespace " + Namespaces;

      P.PrintLn( P.Query, "Query: " + queryString );

      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryString );

      int count = Results.getRowCount();

      Data.UserModelsArray = new UserModel[count];

      for( int i = 0; i < count; i++ ){
        UserModel aUserModel = new UserModel( Results.getValue( i, 0 ).toString(),
                                              ( Results.getValue( i, 1 ) != null ) ?
                                              Results.getValue( i, 1 ).toString() : null );

        queryString =
            "select Factor " +
            "from " +
            "{<" + aUserModel.Id + ">} serql:directType {VoxPopuli:UserModel}; " +
            "VoxPopuli:excludedFactor {Factor} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        QueryResultsTable FactorResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        for( int j = 0; j < FactorResults.getRowCount(); j++ ){

          aUserModel.AddFactor( UserModel.ExcludedFactorsTable, FactorResults.getValue(j,0).toString() );
        }

        queryString =
            "select Factor " +
            "from " +
            "{<" + aUserModel.Id + ">} serql:directType {VoxPopuli:UserModel}; " +
            "VoxPopuli:importantFactor {Factor} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        FactorResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        for( int j = 0; j < FactorResults.getRowCount(); j++ ){

          aUserModel.AddFactor( UserModel.ImportantFactorsTable, FactorResults.getValue(j,0).toString() );
        }

        queryString =
            "select Factor " +
            "from " +
            "{<" + aUserModel.Id + ">} serql:directType {VoxPopuli:UserModel}; " +
            "VoxPopuli:veryImportantFactor {Factor} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        FactorResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        for( int j = 0; j < FactorResults.getRowCount(); j++ ){

          aUserModel.AddFactor( UserModel.VeryImportantFactorsTable, FactorResults.getValue(j,0).toString() );
        }

        queryString =
            "select Factor " +
            "from " +
            "{<" + aUserModel.Id + ">} serql:directType {VoxPopuli:UserModel}; " +
            "VoxPopuli:negativeReaction {Factor} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        FactorResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        for( int j = 0; j < FactorResults.getRowCount(); j++ ){

          aUserModel.AddFactor( UserModel.NegativeReactionTable, FactorResults.getValue(j,0).toString() );
        }

        queryString =
            "select Factor " +
            "from " +
            "{<" + aUserModel.Id + ">} serql:directType {VoxPopuli:UserModel}; " +
            "VoxPopuli:positiveReaction {Factor} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        FactorResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        for( int j = 0; j < FactorResults.getRowCount(); j++ ){

          aUserModel.AddFactor( UserModel.PositiveReactionTable, FactorResults.getValue(j,0).toString() );
        }

        Data.UserModelsArray[i] = aUserModel;

      }

    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return ;
  }
  private void ReadQuestions() throws
      Exception{

    String queryString =
        "select Question, Description, Order " +
        "from " +
        "{Question} serql:directType {VoxPopuli:Question}; " +
        "VoxPopuli:text {Description}; " +
        "[VoxPopuli:order {Order}] " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.QuestionsArray = new Question[count];

      int[] indexes = new int[count];
      Util u = new Util();

      for( int i = 0; i < count; i++ ){

        indexes[i] = ( Results.getValue( i, 2 ) != null ? u.StrToInt( Results.getValue( i, 2 ).toString() ) : -1 );
      }

      int[] indexorder = new int[count];
      indexorder = u.Order( indexes, true );

      for( int i = 0; i < count; i++ ){

        Data.QuestionsArray[i] = new Question( Results.getValue( indexorder[i], 0 ).toString(),
                                               ( Results.getValue( indexorder[i], 1 ) != null ?
                                                 Results.getValue( indexorder[i], 1 ).toString() : null ) );
      }

    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return ;
  }

  private void ReadOpinions() throws
      Exception{

    String queryString =
        "select Opinion, ContrTopic, Description2, Position, Description1 " +
        "from " +
        "{Opinion} serql:directType {VoxPopuli:Opinion}, " +
        "{Opinion} VoxPopuli:position {Position} VoxPopuli:posDescription {Description1}, " +
        "{Opinion} VoxPopuli:topic {ContrTopic} VoxPopuli:contrTopicDescription {Description2} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.OpinionsArray = new Opinion[count];

      for( int i = 0; i < count; i++ ){

        String Description = new String( ( Results.getValue( i, 2 ) != null ? Results.getValue( i, 2 ).toString() : null ) +
                                         " - " +
                                         ( Results.getValue( i, 4 ) != null ? Results.getValue( i, 4 ).toString() : null ) );

        Data.OpinionsArray[i] = new Opinion( Results.getValue( i, 0 ).toString(), Description,
                                             Results.getValue( i, 3 ).toString(), Results.getValue( i, 1 ).toString() );

      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return ;
  }

  private void ReadTopics() throws
      Exception{

    String queryString =
        "select Topic, Description " +
        "from " +
        "{Topic} serql:directType {VoxPopuli:ControversialTopic}; " +
        "VoxPopuli:description {Description} " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.TopicsArray = new Topic[count];

      for( int i = 0; i < count; i++ ){

        Data.TopicsArray[i] = new Topic( Results.getValue( i, 0 ).toString(),
                                         ( Results.getValue( i, 1 ) != null ? Results.getValue( i, 1 ).toString() : null )
            );

      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return ;
  }

  private void ReadConcepts() throws
      Exception{

    String queryString = new String( "select Conc1, Des1 " +
                                     "from " +
                                     "{Conc1} rdf:type {VoxPopuli:Concept}, " +
                                     "{Conc1} VoxPopuli:partDescription {Des1} " +
                                     "using namespace " + Namespaces );

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.Concepts = new Hashtable( count );

      for( int i = 0; i < count; i++ ){

        String[] Entity = new String[2];

        Entity[0] = new String( Results.getValue( i, 0 ).toString() );
        Entity[1] = new String( Results.getValue( i, 1 ).toString() );

        Data.Concepts.put( Entity[0], Entity );

      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Concepts " + e.toString() );
    }

    return ;
  }

  private void ReadModifiers() throws
      Exception{

    String queryString = new String( "select Conc1, Des1 " +
                                     "from " +
                                     "{Conc1} rdf:type {VoxPopuli:Modifier}, " +
                                     "{Conc1} VoxPopuli:partDescription {Des1} " +
                                     "using namespace " + Namespaces );

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.Modifiers = new Hashtable( count );

      for( int i = 0; i < count; i++ ){

        String[] Entity = new String[2];

        Entity[0] = new String( Results.getValue( i, 0 ).toString() );
        Entity[1] = new String( Results.getValue( i, 1 ).toString() );

        Data.Modifiers.put( Entity[0], Entity );

      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Modifiers " + e.toString() );
    }

    return ;
  }

  private void ReadPredicates() throws
      Exception{

    String queryString = new String( "select Conc1, Des1 " +
                                     "from " +
                                     "{Conc1} rdf:type {VoxPopuli:Predicate}, " +
                                     "{Conc1} VoxPopuli:partDescription {Des1} " +
                                     "using namespace " + Namespaces );

    P.PrintLn( P.Query, "Query: " + queryString );

    try{
      QueryResultsTable Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL, queryString );

      int count = Results.getRowCount();

      Data.Predicates = new Hashtable( count );

      for( int i = 0; i < count; i++ ){

        String[] Entity = new String[2];

        Entity[0] = new String( Results.getValue( i, 0 ).toString() );
        Entity[1] = new String( Results.getValue( i, 1 ).toString() );

        Data.Predicates.put( Entity[0], Entity );

      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Predicates " + e.toString() );
    }

    return ;
  }

/*
  private int GiveEthosRating( String UserType, String Interviewee ){

    int Rating = 0;
    QueryResultsTable Results;
    String queryString;

    try{
      for( int i = 0; i < 3; i++ ){

        queryString =
            "select Factor " +
            "from " +
            "{} rdf:type {VoxPopuli:UserModel}; " +
            "VoxPopuli:userType {Value};" +
            "VoxPopuli:" + Weights[i].RelationName + " {Factor}, " +
            "{<" + Interviewee + ">} P {Factor} " +
            "where Value = \"" + UserType + "\" " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryString );

        Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryString );

        Rating = Rating + Results.getRowCount() * Weights[i].Weight;
      }

    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( Exception e ){
      P.PrintLn( P.Err, "Error retrieving Segments from Statements " +
                 e.toString() );
    }

    return Rating;
  }

*/

  private boolean PrintConnectedStatement( Hashtable StatementsArray, boolean key ){

    // The key is used to understand if the array contains the statements
    // or the keys to them

    String Msg = "";

    if( StatementsArray == null ){
      return false;
    }
    for( Enumeration e = StatementsArray.elements(); e.hasMoreElements(); ){

      if( key == true ){
        Link a = ( Link )e.nextElement();

        VerbalStatement vStatement = ( VerbalStatement )Data.Statements.get( a.StatementId );
        if( a.LinkType != null ){
          Msg = a.LinkType.PrintLinkList();
        }

        P.PrintLn( P.StatementLog, "Linked Statement: " + vStatement.SubjectDescription + " " +
                 vStatement.ModifierDescription + " " +
                 vStatement.PredicateDescription + " " /*+
                                                        vStatement.ObjectDescription */+ Msg );

      } else{

        ConstructedStatement cStatement = ( ConstructedStatement )e.nextElement();

        if( cStatement.LinkType != null ){
          Msg = cStatement.LinkType.PrintLinkList();
        }
        P.PrintLn( P.StatementLog, "Constructed Statement: " + cStatement.SubjectDescription + " " +
                 cStatement.ModifierDescription + " " +
                 cStatement.PredicateDescription + " " /*+
                                                        cStatement.ObjectDescription */+ Msg );

      }
    }
    return true;
  }

  // This function checks that:
  //     -terms are one-step-linked only by one relation
  //     -the links are reciprocal (either transitive or class-subclass like)
  //     -there are no paths between concepts that should not be linked
  public boolean CheckConcepts( boolean UseAssociation ) throws Exception{

    QueryResultsTable Results;

    boolean result = true;

    P.PrintLn( P.Locator, "Checking concepts " );

    String Concept, ConceptDescription;

    try{
      // This query reads all the concepts related by more than one relation
      // and it should give an empty result set
      String queryConcept =
          "select distinct U1, X, Y, U2 " +
          "from " +
          "{U1} X {U2}, " +
          "{U1} Y {U2}, " +
          "{U1} rdf:type {VoxPopuli:RhetoricalStatementPart}, " +
          "{U2} rdf:type {VoxPopuli:RhetoricalStatementPart} " +
          "where " +
          "(X=VoxPopuli:generalization or " +
          "X=VoxPopuli:specialization or " +
          "X=VoxPopuli:opposite or " +
          "X=VoxPopuli:similar) " +
          "and " +
          "(Y=VoxPopuli:generalization or " +
          "Y=VoxPopuli:specialization or " +
          "Y=VoxPopuli:opposite or " +
          "Y=VoxPopuli:similar) and " +
          "X!=Y " +
          "using namespace " + Namespaces;

      P.PrintLn( P.Query, "Query: " + queryConcept );

      P.PrintLn( P.Locator, "Checking concepts linked by more then one relation " );

      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryConcept );

      if( Results.getRowCount() > 0 ){
        P.PrintLn( P.ResultOut, "There are concepts linked by more then one relation " );
        for( int j = 0; j < Results.getRowCount(); j++ ){
          P.PrintLn( P.ResultOut, "Concept: " + Results.getValue( j, 0 ).toString() +
                     " Relation: " + Results.getValue( j, 1 ).toString() +
                     " Concept: " + Results.getValue( j, 2 ).toString() );
        }
        result = false;
      }

      // We read all the concepts
      queryConcept =
          "select Concept, ConceptDescription " +
          "from " +
          "{Concept} rdf:type {VoxPopuli:RhetoricalStatementPart}, " +
          "{Concept} VoxPopuli:partDescription {ConceptDescription} " +
          "using namespace " + Namespaces;

      P.PrintLn( P.Query, "Query: " + queryConcept );

      Results = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
          queryConcept );

      P.PrintLn( P.Locator, "Checking reciprocal links " );
      for( int i = 0; i < Results.getRowCount(); i++ ){

        Concept = new String( Results.getValue( i, 0 ).toString() );
        ConceptDescription = new String( Results.getValue( i, 1 ).toString() );

        if( !CheckReciprocalLinks( Concept, ConceptDescription ) ){
          result = false;
        }
      }

      P.PrintLn( P.Locator, "Checking opposite/similar links " );

      for( int i = 0; i < Results.getRowCount(); i++ ){

        Concept = new String( Results.getValue( i, 0 ).toString() );
        ConceptDescription = new String( Results.getValue( i, 1 ).toString() );
        ArrayList Opposite = new ArrayList();
        ArrayList Visited = new ArrayList();
        ArrayList VisitedDescription = new ArrayList();

        String queryOpposite =
            "select Concept " +
            "from " +
            "{<" + Concept + ">} VoxPopuli:opposite {Concept} " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryOpposite );

        QueryResultsTable OppositeResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryOpposite );

        for( int ii = 0; ii < OppositeResults.getRowCount(); ii++ ){
          if( !Opposite.contains( OppositeResults.getValue( ii, 0 ).toString() ) ){
            Opposite.add( OppositeResults.getValue( ii, 0 ).toString() );
          } else{}
        }
        P.PrintLn( P.Locator, "Checking opposite/similar links for concept " + Concept );

        if( !CheckConceptLinks( Concept, ConceptDescription, Opposite, Visited, VisitedDescription, UseAssociation ) ){
          result = false;
          int size = VisitedDescription.size();
          P.PrintLn( P.ResultOut, "Concept linked to Opposite: " + ConceptDescription );
          P.PrintLn( P.ResultOut, "Opposite Concept: " + VisitedDescription.get( size - 1 ) );
          for( int j = 1; j < size - 1; j++ ){
            P.PrintLn( P.ResultOut, "Linking Concept: " + VisitedDescription.get( j ) );
          }
        }
      }
    } catch( MalformedQueryException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( AccessDeniedException e ){
      P.PrintLn( P.Err, "Error in Query " + e.toString() );
    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Topics " + e.toString() );
    }

    return result;
  }

  // This function checks that there are no paths between concepts that should not be linked
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
        "{<" + Concept + ">} X {Concept}, " +
        "{Concept} VoxPopuli:partDescription {ConceptDescription} " +
        "where " +
        "X = VoxPopuli:similar OR " +
        ( UseAssociation == true ? "X = VoxPopuli:association OR " : "" ) +
        "X = VoxPopuli:generalization " +
        "using namespace " + Namespaces;

    P.PrintLn( P.Query, "Query: " + queryRelated );

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
        "similar", "opposite", "association", "generalization"};

    for( int i = 0; i < Relation.length; i++ ){

      String queryRelated =
          "select RelConcept, RelConceptDescription " +
          "from " +
          "{<" + Concept + ">} VoxPopuli:" + Relation[i] + " {RelConcept}, " +
          "{RelConcept} VoxPopuli:partDescription {RelConceptDescription} " +
          "using namespace " + Namespaces;

      P.PrintLn( P.Query, "Query: " + queryRelated );

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
            "{<" + RelatedConcept + ">} VoxPopuli:" + InvRel + " {X} " +
            "where " +
            "X = <" + Concept + "> " +
            "using namespace " + Namespaces;

        P.PrintLn( P.Query, "Query: " + queryBackRelated );

        QueryResultsTable BackRelatedResults = theRepository.Client.performTableQuery( QueryLanguage.SERQL,
            queryBackRelated );

        if( BackRelatedResults.getRowCount() != 1 ){

          P.PrintLn( P.ResultOut, "Concept: " + ConceptDescription + " is not back related by relation: " + InvRel +
                     " to concept: " +
                     RelatedConceptDescription );
          result = false;
        }
      }
    }

    return true;
  }

}
