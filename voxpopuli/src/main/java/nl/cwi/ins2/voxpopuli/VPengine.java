package nl.cwi.ins2.voxpopuli;

public class VPengine{

  private static void Usage() throws Exception{
  System.out.println(
      "Usage: <local=true|false> <Sesame server|Local Directory> <Repository name> <nameSpace> <videolocation>\n" +
      "<main-option [LMCSRVA]> <options-depending-on-main-option>" );
  throw new Exception( "Program not called correctly" );
}



  public static void main( String[] args ){

//    int len = Array.getLength(args);
//    int len = args.length;

    try{


      //System.out.println(args[5]);
      if( !( args[5].equals( "L" ) ) &&
          !( args[5].equals( "M" ) ) &&
          !( args[5].equals( "C" ) ) &&
          !( args[5].equals( "R" ) ) &&
          !( args[5].equals( "V" ) ) &&
          !( args[5].equals( "S" ) ) &&
          !( args[5].equals( "A" ) )
       ){
        Usage();
      }

      if( ( args[5].equals( "L" ) && args.length != 7 ) ||
          ( args[5].equals( "M" ) && args.length != 11 ) ||
          ( args[5].equals( "C" ) && args.length != 7 ) ||
          ( args[5].equals( "R" ) && args.length != 7 ) ||
          ( args[5].equals( "V" ) && args.length != 8 ) ||
          ( args[5].equals( "S" ) && args.length != 6 ) ||
          ( args[5].equals( "A" ) && args.length != 8 ) ){
        Usage();
      }

      DataContainer theDataContainer = new DataContainer();
      Outputs theOutputs = new Outputs();

      DataBuilder theDataBuilder = new DataBuilder( args[0], args[1], args[2], args[3], theDataContainer, theOutputs);






      if( args[5].equals( "V" ) ){
        theOutputs.SetOutputStreams( null, null, null, null, null, null, null, System.out, null );
        Util u = new Util();
        theDataBuilder.TestVideo( u.StrToInt( args[6] ), u.StrToInt( args[7] ) );

      }else{
        PrintStream Stat = new PrintStream( new FileOutputStream( "a.csv" ) );
        //setting the debug streams
        theOutputs.SetOutputStreams( null, null, null, null, System.out, Stat, null, null, null );
        //theOutputs.SetOutputStreams( null, null, null, null, null, Stat, null, null, null );

        /*
               String UserType = new String( "Pacifist" );

               //voxpopuli.PathosProcessInterview(UserType, position, Attack);

         */

        if( args[5].equals( "C" ) ){
          boolean Result;
          if( args[6].equals( "A" ) ){
            Result = theDataBuilder.CheckConcepts( true );
          } else if( args[6].equals( "NA" ) ){
            Result = theDataBuilder.CheckConcepts( false );
          } else{
            throw new Exception( "Incorrect option for Check Concepts " );
          }
          if( Result == true ){
            System.out.println( "Concepts are OK " );
          } else{
            System.out.println( "Concepts are not OK " );
          }
        }else if( args[5].equals( "S" ) ){
          theDataBuilder.SuggestLinks();
        }else{

          if( args[5].equals( "R" ) ){
            Util u = new Util();
            int it = u.StrToInt( args[6] );

            theDataBuilder.SetIterations( it );
          }

          // This reads all interviews and builds the data structure
          theDataBuilder.SetObject( true, true );

          // The following prints all the statements with connections
          theDataBuilder.PrintStatements();


          if( args[5].equals( "R" ) ){
            // Skip the SMIL generation, we are only interested in the
            // Semantic Graph generation

          } else{

            // Now we create the rules
            RuleInstance theRuleInstance = new RuleInstance( theDataContainer, theOutputs );

            String theArgumentation;
            boolean keep = true;
            boolean NodeOn = true;
            boolean orig = false;
            String filename = null;

            if( args[5].equals( "A" ) ){
              // Automatic for all options
              String[] ArgArr = {
                  "attack", "support"
              };
              boolean[] BoolArr = {
                  true, false};

              filename = args[6] + "_" + args[5] + "_" + "L";

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
                    SMILMedia theSMILMedia = new SMILMedia("_20", args[4], null,null,null, args[2],
                        filename, true, null, true, theOutputs);
                    theRuleInstance.Multiplevoices( ArgArr[i], BoolArr[ii], BoolArr[iii], orig, filename, theSMILMedia );

                  }
                }
              }
            } else{
              if( args[5].equals( "L" ) ){
                filename = args[6] + "_" + args[5];
                // Calculate the longest path
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
                  throw new Exception( "Wrong option for original " + args[7] );
                }

                if( args[9].equals( "K" ) ){
                  // Keep statement
                  keep = true;
                } else if( args[9].equals( "NK" ) ){
                  // Do not keep statetement
                  keep = false;
                } else{
                  throw new Exception( "Unknown sub-option for strategy " + args[9] );
                }
                if( args[10].equals( "N" ) ){
                  // Node Based
                  NodeOn = true;
                } else if( args[10].equals( "S" ) ){
                  // Statement based
                  NodeOn = false;
                } else{
                  throw new Exception( "Unknown sub-option for strategy " + args[10] );
                }

                filename = args[6] + "_" + args[5] + "_" + args[8] + "_" + args[9] + "_" + args[10];
                SMILMedia theSMILMedia = new SMILMedia("_20", args[4], null,null,null, args[2],
                    filename, true, null, true, theOutputs);
                theRuleInstance.Multiplevoices( theArgumentation, keep, NodeOn, orig, filename, theSMILMedia );

              } else{
                throw new Exception( "Unknown option for strategy " + args[5] );
              }
            }
          }
        }
      }
    } catch( Exception e ){
      e.printStackTrace();
      System.err.println( "Error:  " + e.toString() );
    }
  }
}
