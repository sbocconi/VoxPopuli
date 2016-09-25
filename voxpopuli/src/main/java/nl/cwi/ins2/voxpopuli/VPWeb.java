package nl.cwi.ins2.voxpopuli;


//CLASS TO TEST THE WEB INTERFACE

public class VPWeb{

  Outputs P;

  ArrayList MediaArray;

// This function builds the argument pro or against the selected
// opinion and person expressing it
  private String CreateRhetoric( SMILMedia theSMILMedia, RuleInstance theRuleInstance, DataContainer theDataContainer,
                                 DataBuilder theDataBuilder, String[] Opinions, String[] Interviewees, String Original,
                                 String Strategy, String Captions ){

    P.PrintLn( P.Locator, "Creating a Rhetoric " );

    // For the moment I assume that if no (or more than one) opinion(s)
    // or interviewee(s) are selected, no sequence is generated.
    // This is not necessary,
    // because the system could pick up an interview arbitrarily
    if( Opinions == null || Opinions.length != 1 ||
        Interviewees == null || Interviewees.length != 1 ){
      return null;
    }

    if( MediaArray == null ){
      MediaArray = new ArrayList();
    } else{
      MediaArray.clear();
      P.PrintLn( P.Log1, "MediaArray cleared" );
    }

    P.PrintLn( P.Debug1, "Captions: " + Captions );
    P.PrintLn( P.Debug1, "Original: " + Original );

    try{

      if( Strategy.equals( "attack" ) ){
      } else if( Strategy.equals( "support" ) ){
      } else{
        throw new Exception( "Unknown sub-option for strategy " );
      }

      String queryString =
          "select Interview, PosDesc, PartDesc  " +
          "from " +
          "{Interview} rdf:type {VoxPopuli:InterviewSegment}, " +
          "{Interview} VoxPopuli:opinion {<" + Opinions[0] + ">}, " +
          "{Interview} VoxPopuli:partecipant {<" + Interviewees[0] + ">}, " +
          "{<" + Interviewees[0] + ">} VoxPopuli:description {PartDesc}, " +
          "{<" + Opinions[0] + ">} rdfs:label {PosDesc} ";

      P.PrintLn( P.Query, "Query: " + queryString );

      QueryResultsTable Results;

      if( ( Results = theDataBuilder.PerformQuery( queryString ) ) == null ){
        return "";
      }

      // count instances found
      int count = 0;

      if( Original.equals( "on" ) ){
        for( int i = 0; i < Results.getRowCount(); i++ ){

          MediaItem MediaItems = null;
          P.PrintLn( P.Query, "Query Result: " + Results.getValue( i, 0 ).toString() );

          //FIX ME
          //MediaItems = theDataBuilder.ReadMedia( Results.getValue( i, 0 ).toString(), null, null, null );

          MediaItems.AddDescription( Results.getValue( i, 2 ).toString() + "\n" +
                                     Results.getValue( i, 1 ).toString(), true );
          MediaArray.add( MediaItems );

          count++;

        }
      } else{

        //Sem Graph built?
        theDataBuilder.SetObject( true, true );

        for( int i = 0; i < Results.getRowCount(); i++ ){

          P.PrintLn( P.Query, "Query Result: " + Results.getValue( i, 0 ).toString() );

          for( int ii = 0; ii < theDataContainer.InterviewsArray.length; ii++ ){
            if( theDataContainer.InterviewsArray[ii].Id.equals( Results.getValue( i, 0 ).toString() ) ){
              if( theDataContainer.InterviewsArray[ii].Arguments != null ){
                for( int iii = 0; iii < theDataContainer.InterviewsArray[ii].Arguments.length; iii++ ){
                  Argument Voices = new Argument( theDataContainer.InterviewsArray[ii].Arguments[iii] );
                  int[] augmented = new int[2];
                  theRuleInstance.AugmentStructure( Strategy, false, false, true, augmented,
                      theDataContainer.InterviewsArray[ii].Arguments[iii],
                      Voices );
                  // irrelevant here
                  theRuleInstance.OrderArgument( Voices, true );
                  count++;
                }
              }
            }
          }
        }
      }
      if( count > 0 && theSMILMedia.DoMontage( MediaArray ) ){

        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        theSMILMedia.CreateSMILOutput( Captions.equals( "on" ) );
        return ba.toString();
      }

    } catch( IOException e ){
      P.PrintLn( P.Err, "Error retrieving Sequences " + e.toString() );
    } catch( Exception e ){
      P.PrintLn( P.Err, "Error retrieving Media " + e.toString() );
    }

    return null;
  }

  public String[][] RetrieveOpinions( DataContainer theDataContainer, String[] Interviewees ){

    String[][] Opinions;

    if( Interviewees == null ){
      int size = theDataContainer.OpinionsArray.length;

      Opinions = new String[2][size];

      for( int i = 0; i < size; i++ ){

        Opinions[0][i] = new String( theDataContainer.OpinionsArray[i].OpinionId );
        Opinions[1][i] = new String( theDataContainer.OpinionsArray[i].OpinionText );

      }
    }else{
      Hashtable TempInterviewees = new Hashtable();

      for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
        for( int j = 0; j < Interviewees.length; j++ ){
          if( theDataContainer.InterviewsArray[i].theInterviewee.equals( Interviewees[j] ) ){
            String[] a = new String[2];
            a[0] = new String( theDataContainer.InterviewsArray[i].OpinionId );
            if( !a[0].equals("") ){
              for( int z = 0; z < theDataContainer.OpinionsArray.length; z++ ){
                if( theDataContainer.OpinionsArray[z].OpinionId.equals( a[0] ) ){
                  a[1] = new String( theDataContainer.OpinionsArray[z].OpinionText );
                }
              }
              TempInterviewees.put( a[0], a );
            }
          }
        }
      }
      int size = TempInterviewees.size();
      Opinions = new String[2][size];
      int index = 0;
      for( Enumeration e = TempInterviewees.elements(); e.hasMoreElements(); ){
        String[] a = ( String[] )e.nextElement();

        Opinions[0][index] = new String( a[0] );
        Opinions[1][index] = new String( a[1] );
        index++;
      }
    }

    return Opinions;
  }

  public String[][] RetrieveQuestions( DataContainer theDataContainer, String[] Interviewees ){

    String[][] Questions = null;

    if( Interviewees == null ){

      int size = theDataContainer.QuestionsArray.length;
      Questions = new String[2][size];

      for( int i = 0; i < size; i++ ){

        Questions[0][i] = new String( theDataContainer.QuestionsArray[i].QuestionId );
        Questions[1][i] = new String( theDataContainer.QuestionsArray[i].QuestionText );

      }

    } else{

      Hashtable TempQuestions = new Hashtable();
      for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
        for( int j = 0; j < Interviewees.length; j++ ){
          if( theDataContainer.InterviewsArray[i].theInterviewee.equals( Interviewees[j] ) ){
            String[] a = new String[2];
            a[0] = new String( theDataContainer.InterviewsArray[i].thequestion.QuestionId );
            a[1] = new String( theDataContainer.InterviewsArray[i].thequestion.QuestionText );
            TempQuestions.put( a[0], a );
          }
        }
      }
      int size = TempQuestions.size();
      Questions = new String[2][size];
      int index = 0;
      for( Enumeration e = TempQuestions.elements(); e.hasMoreElements(); ){
        String[] a = ( String[] )e.nextElement();

        Questions[0][index] = new String( a[0] );
        Questions[1][index] = new String( a[1] );
        index++;
      }
    }
    return Questions;
  }

  public String[][] RetrieveInterviewees( DataContainer theDataContainer,
                                          String[] Questions, String[] Opinions ) throws Exception{

    String[][] Interviewees = null;

    if( Questions == null && Opinions == null ){

      int size = theDataContainer.Interviewees.size();
      Interviewees = new String[2][size];

      int index = 0;
      for( Enumeration e = theDataContainer.Interviewees.elements(); e.hasMoreElements(); ){

        Person theInterviewee = ( Person )e.nextElement();
        Interviewees[0][index] = new String( theInterviewee.Id );
        Interviewees[1][index] = new String( theInterviewee.Description );
        index++;

      }

    } else{

      if( Opinions == null ){

        Hashtable TempInterviewees = new Hashtable();

        for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
          for( int j = 0; j < Questions.length; j++ ){
            if( theDataContainer.InterviewsArray[i].thequestion.QuestionId.equals( Questions[j] ) ){
              String[] a = new String[2];
              a[0] = new String( theDataContainer.InterviewsArray[i].theInterviewee );
              a[1] = new String( ( ( Person )theDataContainer.Interviewees.get( a[0] ) ).Description );
              TempInterviewees.put( a[0], a );
            }
          }
        }
        int size = TempInterviewees.size();
        Interviewees = new String[2][size];
        int index = 0;
        for( Enumeration e = TempInterviewees.elements(); e.hasMoreElements(); ){
          String[] a = ( String[] )e.nextElement();

          Interviewees[0][index] = new String( a[0] );
          Interviewees[1][index] = new String( a[1] );
          index++;
        }
      } else if( Questions == null ){
        Hashtable TempInterviewees = new Hashtable();

        for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
          for( int j = 0; j < Opinions.length; j++ ){
            if( theDataContainer.InterviewsArray[i].OpinionId.equals( Opinions[j] ) ){
              String[] a = new String[2];
              a[0] = new String( theDataContainer.InterviewsArray[i].theInterviewee );
              a[1] = new String( ( ( Person )theDataContainer.Interviewees.get( a[0] ) ).Description );
              TempInterviewees.put( a[0], a );
            }
          }
        }
        int size = TempInterviewees.size();
        Interviewees = new String[2][size];
        int index = 0;
        for( Enumeration e = TempInterviewees.elements(); e.hasMoreElements(); ){
          String[] a = ( String[] )e.nextElement();

          Interviewees[0][index] = new String( a[0] );
          Interviewees[1][index] = new String( a[1] );
          index++;
        }

      } else{
        throw new Exception( "Opinions and Questions not null at the same time " );
      }
    }

    return Interviewees;
  }

  public String[][] RetrieveSocClass( DataBuilder theDataBuilder ){

    QueryResultsTable Results;

    // We read all the questions with Interviewees[]
    String queryString =
        "select X, Y " +
        "from " +
        "{X} serql:directSubClassOf {VoxPopuli:SocialAnnotation}, " +
        "{X} rdfs:label {Y} ";

    P.PrintLn( P.Query, "Query: " + queryString );

    if( ( Results = theDataBuilder.PerformQuery( queryString ) ) == null ){
      return null;
    }

    String[][] theClasses = new String[2][Results.getRowCount()];

    for( int i = 0; i < Results.getRowCount(); i++ ){
      theClasses[0][i] = new String( Results.getValue( i, 0 ).toString() );
      theClasses[1][i] = new String( Results.getValue( i, 1 ).toString() );

      P.PrintLn( P.Query, "Query Result: " + theClasses[1][i] );
    }

    return theClasses;
  }

  public String[][] RetrieveSubSocClass( DataBuilder theDataBuilder, String Class ){

    QueryResultsTable Results;
    Class = new String( Class.substring( DataBuilder.VoxPopuliNamespaces.length() ) );

    // We read all the questions with Interviewees[]
    String queryString =
        "select X, Y " +
        "from " +
        "{X} serql:directSubClassOf {VoxPopuli:" + Class + "}, " +
        "{X} rdfs:label {Y} ";

    P.PrintLn( P.Query, "Query: " + queryString );

    if( ( Results = theDataBuilder.PerformQuery( queryString ) ) == null ){
      return null;
    }

    String[][] theSubClasses = new String[2][Results.getRowCount()];

    for( int i = 0; i < Results.getRowCount(); i++ ){
      theSubClasses[0][i] = new String( Results.getValue( i, 0 ).toString() );
      theSubClasses[1][i] = new String( Results.getValue( i, 1 ).toString() );

      P.PrintLn( P.Query, "Query Result: " + theSubClasses[1][i] );
    }

    return theSubClasses;
  }

  public String[][] RetrieveConcepts( DataBuilder theDataBuilder ){

    QueryResultsTable Results;

    // We read all the questions with Interviewees[]
    String queryString =
        "select X, Y " +
        "from " +
        "{X} rdf:type {VoxPopuli:Concept}, " +
        "{X} rdfs:label {Y} ";

    P.PrintLn( P.Query, "Query: " + queryString );

    if( ( Results = theDataBuilder.PerformQuery( queryString ) ) == null ){
      return null;
    }

    String[][] theConcepts = new String[2][Results.getRowCount()];

    for( int i = 0; i < Results.getRowCount(); i++ ){
      theConcepts[0][i] = new String( Results.getValue( i, 0 ).toString() );
      theConcepts[1][i] = new String( Results.getValue( i, 1 ).toString() );

      P.PrintLn( P.Query, "Query Result: " + theConcepts[1][i] );
    }

    return theConcepts;
  }

  public String SetRules( String[][] Classes, String[][] parameters1, String[][] parameters2,
                          String[] concepts1, String[] concepts2, String[] questions,
                          String[] interviewees, String[] opinions, String[] interviews,
                          String strategy, String intercut ) throws Exception{

    final int NONE = 0;
    final int ATTACK = 1;
    final int SUPPORT = 2;
    final int VP = 3;

    int whatstrategy;

    StringBuffer msg = new StringBuffer();

    if( strategy != null ){
      if( strategy.equals( "none" ) ){
        whatstrategy = NONE;
      } else if( strategy.equals( "attack" ) ){
        whatstrategy = ATTACK;
      } else if( strategy.equals( "support" ) ){
        whatstrategy = SUPPORT;
      } else if( strategy.equals( "VP" ) ){
        whatstrategy = VP;
      } else{
        throw new Exception( "Unknown strategy " );
      }
    } else{
      whatstrategy = NONE;
    }

    msg.append( "START_RULE\n" );
    msg.append( "START_AND\n" );

    for( int i = 0; i < Classes[0].length; i++ ){
      P.PrintLn( P.Debug1, "Class: " + Classes[0][i] );
      if( parameters1[i] != null ){
        msg.append( "Select" + Classes[1][i] );
        for( int j = 0; j < parameters1[i].length; j++ ){
          P.PrintLn( P.Debug1, "Selection 1: " + parameters1[i][j] );
          msg.append( " " + parameters1[i][j] );
        }
        if( parameters2[i] != null ){
          msg.append( " , " );
          for( int j = 0; j < parameters2[i].length; j++ ){
            P.PrintLn( P.Debug1, "Selection 2: " + parameters2[i][j] );
            msg.append( " " + parameters2[i][j] );
          }
        }
      } else if( parameters2[i] != null ){
        msg.append( "Select" + Classes[1][i] );
        msg.append( " , " );
        for( int j = 0; j < parameters2[i].length; j++ ){
          P.PrintLn( P.Debug1, "Selection 2 wo 1: " + parameters2[i][j] );
          msg.append( " " + parameters2[i][j] );
        }
      }
      msg.append( "\n" );
    }

    if( concepts1 != null ){
      msg.append( "SelectConcept " );
      if( whatstrategy == VP ){
        msg.append( " MAINCHAR , " );
      }
      for( int i = 0; i < concepts1.length; i++ ){
        P.PrintLn( P.Debug1, "Concept 1: " + concepts1[i] );
        msg.append( " " + concepts1[i] );
      }
      if( concepts2 != null ){
        msg.append( " , " );
        for( int i = 0; i < concepts2.length; i++ ){
          P.PrintLn( P.Debug1, "Concept 2: " + concepts2[i] );
          msg.append( " " + concepts2[i] );
        }
      }
    } else if( concepts2 != null ){
      msg.append( "SelectConcept " );
      msg.append( " , " );
      for( int i = 0; i < concepts2.length; i++ ){
        P.PrintLn( P.Debug1, "Concept 2 wo 1: " + concepts2[i] );
        msg.append( " " + concepts2[i] );
      }
    }
    msg.append( "\n" );

    if( interviews == null ){
      if( questions != null ){
        msg.append( "SelectQuestion " );
        if( whatstrategy == VP ){
          msg.append( " MAINCHAR , " );
        }

        for( int i = 0; i < questions.length; i++ ){
          P.PrintLn( P.Debug1, "Question: " + questions[i] );
          msg.append( " " + questions[i] );
        }
      }
      msg.append( "\n" );

      if( interviewees != null ){
        msg.append( "SelectPartecipant " );
        if( whatstrategy == VP ){
          msg.append( " MAINCHAR , " );
        }

        for( int i = 0; i < interviewees.length; i++ ){
          P.PrintLn( P.Debug1, "Interviewee: " + interviewees[i] );
          msg.append( " " + interviewees[i] );
        }
      }
      msg.append( "\n" );

      if( opinions != null ){
        msg.append( "SelectOpinion " );
        if( whatstrategy == VP ){
          msg.append( " MAINCHAR , " );
        }

        for( int i = 0; i < opinions.length; i++ ){
          P.PrintLn( P.Debug1, "Opinion: " + opinions[i] );
          msg.append( " " + opinions[i] );
        }
      }
      msg.append( "\n" );
    } else{
      msg.append( "SelectInterview " );
      if( whatstrategy == VP ){
        msg.append( " MAINCHAR , " );
      }

      for( int i = 0; i < interviews.length; i++ ){
        P.PrintLn( P.Debug1, "Interview: " + interviews[i] );
        msg.append( " " + interviews[i] );
      }
      msg.append( "\n" );
    }

    if( whatstrategy == NONE ){
      msg.append( "EliminateCharacter B\n" );
    } else if( whatstrategy == ATTACK || whatstrategy == SUPPORT ){
      if( whatstrategy == SUPPORT ){
        msg.append( "CreateArg support " );
      } else{
        msg.append( "CreateArg attack " );
      }
      if( intercut.equals( "false" ) ){
        msg.append( "false " );
      } else{
        msg.append( "true " );
      }
      msg.append( "true " );
      msg.append( "\n" );
      msg.append( "GazeClash " );
      msg.append( "\n" );
      msg.append( "SelectUnique parallel" );
      msg.append( "\n" );

    } else if( whatstrategy == VP ){
      msg.append( "CreateArg VP " );
      if( intercut.equals( "false" ) ){
        msg.append( "false " );
      } else{
        msg.append( "true " );
      }
      msg.append( "true " );
      msg.append( "\n" );
      msg.append( "GazeClash " );
      msg.append( "\n" );
      msg.append( "SelectUnique " );
    }

    msg.append( "\n" );

    msg.append( "CreatePlot\n" );
    msg.append( "\n" );

    msg.append( "FilmFeatures\n" );
    msg.append( "\n" );

    msg.append( "END_AND\n" );
    msg.append( "END_RULE\n" );

    return msg.toString();

  }

  public String SetRules( String[] questions, String[] partecipants ){

    StringBuffer msg = new StringBuffer();

    msg.append( "START_RULE\n" );
    msg.append( "START_AND\n" );
    msg.append( "EliminateCharacter B\n" );

    if( questions != null ){
      msg.append( "SelectQuestion" );
      for( int i = 0; i < questions.length; i++ ){
        P.PrintLn( P.Debug1, "Question: " + questions[i] );
        msg.append( " " + questions[i] );
      }
    }
    msg.append( "\n" );

    if( partecipants != null ){
      msg.append( "SelectPartecipant" );
      for( int i = 0; i < partecipants.length; i++ ){
        P.PrintLn( P.Debug1, "Partecipant: " + partecipants[i] );
        msg.append( " " + partecipants[i] );
      }
    }
    msg.append( "\n" );

    msg.append( "CreatePlot\n" );
    msg.append( "\n" );

    msg.append( "FilmFeatures\n" );
    msg.append( "\n" );

    msg.append( "END_AND\n" );
    msg.append( "END_RULE\n" );

    return msg.toString();

  }

  /*
    public boolean CreateSequence( String[] Questions, String[] Interviewees, String Captions ){

      P.PrintLn( P.Locator, "Creating a Sequence " );

      if( MediaArray == null ){
        MediaArray = new ArrayList();
      } else{
        MediaArray.clear();
        P.PrintLn( P.Log1, "MediaArray cleared" );
      }

      P.PrintLn( P.Debug1, "Captions: " + Captions );

      if( Interviewees == null || Interviewees.length == 0 ||
          Questions == null || Questions.length == 0 ){
      } else{
        // We read all the interviews with Interviewees[] and Questions[]
        for( int part = 0; part < Interviewees.length; part++ ){
          for( int ques = 0; ques < Questions.length; ques++ ){

            String queryString =
                "select Interview, QuestDesc, PartDesc " +
                "from " +
                "{Interview} <rdf:type> {<VoxPopuli:InterviewSegment>}, " +
                "{Interview} <VoxPopuli:hasQuestion> {<" + Questions[ques] + ">}, " +
                "{Interview} <VoxPopuli:partecipant> {<" + Interviewees[part] + ">}, " +
                "{<" + Interviewees[part] + ">} <VoxPopuli:description> {PartDesc}, " +
                "{<" + Questions[ques] + ">} <VoxPopuli:text> {QuestDesc} ";

            P.PrintLn( P.Query, "Query: " + queryString );

            //try{

              QueryResultsTable Results;

              Results = theDataBuilder.PerformQuery( queryString );

              for( int i = 0; i < Results.getRowCount(); i++ ){

                MediaItem MediaItems = null;
                P.PrintLn( P.Query, "Query Result: " + Results.getValue( i, 0 ).toString() );

                // FIX ME
                // MediaItems = theDataBuilder.ReadMedia( Results.getValue( i, 0 ).toString(), null, null, null );

                MediaItems.AddDescription( Results.getValue( i, 2 ).toString() + "\n" +
                                           Results.getValue( i, 1 ).toString(), true );
                MediaArray.add( MediaItems );

              }
            //} catch( IOException e ){
             // P.PrintLn( P.Err, "Error retrieving Sequences " + e.toString() );
            //} catch( Exception e ){
             // P.PrintLn( P.Err, "Error retrieving Media " + e.toString() );
            //}
          }
        }
      }
      try{
        if( MediaArray.size() > 0 ){
          if( theSMILMedia.DoMontage( MediaArray ) ){

            theSMILMedia.CreateSMILOutput( Captions.equals( "on" ) );
            return true;
          }
          return false;
        }
      } catch( Exception e ){
        P.PrintLn( P.Err, "Error retrieving Media " + e.toString() );
      }

      return false;
    }
   */
  public VPWeb( Outputs p ){

    P = p;
  }

  private static void Usage() throws Exception{
    System.out.println(
        "Usage: <local=true|false> <Sesame server|Local Directory> <Repository name> <nameSpace> <videolocation>\n" );
    throw new Exception( "Program not called correctly" );
  }

  public static void main( String[] args ){

//    int len = Array.getLength(args);
//    int len = args.length;

    try{

      if( args.length < 5 ){
        Usage();
      }

      String local = args[0];
      String RDFLocation = args[1];
      String repository = args[2];
      String domainNS = args[3];

      String theVideoLocation = args[4];

      DataContainer theDataContainer = new DataContainer();
      Outputs P = new Outputs();

      DataBuilder theDataBuilder = new DataBuilder( local, RDFLocation, repository, domainNS, theDataContainer, P );
      // This reads all interviews and builds the data structure
      theDataBuilder.SetObject( true, false );

      //RuleInstance R = new RuleInstance( theDataContainer, P );

      //setting the debug streams
      P.SetOutputStreams( null, null, null, null, null, null, null, System.out, System.err );

      VPWeb Webby = new VPWeb( P );

      if( repository.equals( "VJ" ) ){
        String[] questions = {
            "http://www.cwi.nl/~media/ns/VP/VJ.rdf#VJ_Instance_20013"};
        String[] VJ = {
            "http://www.cwi.nl/~media/ns/VP/VJ.rdf#VJ_Instance_20037"};
        String[][] Questions = Webby.RetrieveQuestions( theDataContainer, VJ );

        for( int i = 0; i < Questions[0].length; i++ ){
          System.out.println( "Question: " + Questions[1][i] );
        }

        String[][] Interviewees = Webby.RetrieveInterviewees( theDataContainer, questions, null );

        for( int i = 0; i < Interviewees[0].length; i++ ){
          System.out.println( "Interviewee: " + Interviewees[1][i] );
        }

      } else{

        String[][] SocClasses = Webby.RetrieveSocClass( theDataBuilder );

        for( int i = 0; i < SocClasses[0].length; i++ ){
          System.out.println( "Class: " + SocClasses[1][i] );
          String[][] SubSocClasses = Webby.RetrieveSubSocClass( theDataBuilder, SocClasses[0][i] );
          for( int j = 0; j < SubSocClasses[0].length; j++ ){
            System.out.println( "SubClass: " + SubSocClasses[1][j] );
          }
        }

        String[][] Concepts = Webby.RetrieveConcepts( theDataBuilder );
        for( int i = 0; i < Concepts[0].length; i++ ){
          System.out.println( "Concept: " + Concepts[1][i] );
        }

        String[][] Questions = Webby.RetrieveQuestions( theDataContainer, null );

        for( int i = 0; i < Questions[0].length; i++ ){
          System.out.println( "Question: " + Questions[1][i] );
        }

        String[] opinions = {"http://www.cwi.nl/~media/ns/IWA/IWA.rdf#IWA_00218"};
        String[][] Opinions = Webby.RetrieveOpinions( theDataContainer,  opinions );

        for( int i = 0; i < Opinions[0].length; i++ ){
          System.out.println( "Opinion: " + Opinions[1][i] );
        }

        if( theDataBuilder.DataOK( local, RDFLocation, repository, domainNS, true ) ){
          System.out.println( "Data OK " );
        }
      }

    } catch( Exception e ){
      e.printStackTrace();
      System.err.println( "Error:  " + e.toString() );
    }
  }
}
