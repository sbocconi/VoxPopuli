package nl.cwi.ins2.voxpopuli;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: Vox Populi</p>
 *
 * <p>Description: Automatic Generation of Biased Video Documentaries</p>
 *
 * <p>Copyright: Copyleft: free to copy</p>
 *
 * <p>Company: CWI</p>
 *
 * @author Stefano Bocconi
 * @version 1.0
 */

/*********************************** Scheduler *****************************/
// This class represents the stack and the methods to make it and run it

public class Scheduler{

  /*********************
   *  CONSTANTS        *
   *********************/


  /*************************
   *  PSEUDO VARIABLES     *
   *  set once and forever *
   *  with error checking  *
   *************************/


  /*********************
   *  VARIABLES *
   **********************/
  ArrayList Stack;
  int CurrentIndex = 0;
  int Goodness = 0;      // the maximum goodness
  int Threshold = 0;
  int MaxScore;

  StorySpace theStorySpace;
  RuleInstance theRuleInstance;
  private Logger myLogger;


  /*********************
   *  CLASSES *
   **********************/


  /*********************
   *  FUNCTIONS *
   **********************/
  public Scheduler(StorySpace a, RuleInstance aRuleInstance) throws Exception{
    theStorySpace = a;
    theRuleInstance = aRuleInstance;
	myLogger = LoggerFactory.getLogger(this.getClass());
  }

  public void InitGoodness( int thr ) throws Exception{

    if( Stack == null ){
      throw new Exception( "Stack not initialized in Scheduler " );
    }
    // How good the result must be
    Threshold = thr;
    // Calculate optimal goodness
    int ssize = Stack.size();
    Goodness = 0;

    // Calculate the maximum goodness
    for( int i = 0; i < ssize; i++ ){
      Goodness = Goodness +
          ( ( ( Module )( Stack.get( i ) ) ).NodeNr*100 * ( int )java.lang.Math.pow( 2.0, ( double )( ssize - i - 1 ) ) );
    }
  }

  public boolean RunScheduler() throws Exception{

    boolean EditOK = false;
    MaxScore  = -10000000;;

    if( Stack == null ){
      throw new Exception( "Stack not initialized in Scheduler " );
    }

    try{

      CurrentIndex = 0;

      RunNextModule();

    } catch( RuleException e ){
      EditOK = true;
    }
    // Restore in case we saved a space because we did not get any
    // Exception meaning the result was good enough
    if( EditOK == false ){
      theStorySpace.RestoreSpace();
    }

/*    // Finalize the plot in case we did not create one
    Rule r = theRuleInstance.FindRule("CreatePlot", null,null);
    r.ApplyRule(theStorySpace, 0);
*/
    return EditOK;

  }

  public void RunNextModule() throws RuleException, Exception{

    if( Stack == null ){
      throw new Exception( "Stack not initialized in Scheduler " );
    }

    if( CurrentIndex < Stack.size() ){
      Module CurrentModule = ( Module )Stack.get( CurrentIndex++ );
      CurrentModule.InizializeModule();
      CurrentModule.RunModule(CurrentIndex==1); // because we incremented it already

    }
    return;
  }

  private int CalculateScore(){
    long localgoodness = 0;
    int ssize = Stack.size();

    for( int i = 0; i < CurrentIndex /*ssize*/; i++ ){
      localgoodness = localgoodness +
          ( ( ( Module )( Stack.get( i ) ) ).CalculateModuleScore() * ( int )java.lang.Math.pow( 2.0, ( double )( ssize - i - 1 ) ) );
    }
    if( localgoodness > Goodness ){
      localgoodness = Goodness;
    }
    double perc = ( ( ( double )localgoodness ) / ( ( double )Goodness ) );

    return ( int )( ( perc ) * 100 );
  }

  public boolean GoodBadness(){
    int local = CalculateScore();
    myLogger.info("Local Score " + local + " Target " + Threshold);
    if( local >= Threshold ){
      return true;
    }
    return false;
  }

  public boolean IsMax(){

    int locmax = CalculateScore();

    if( locmax > MaxScore ){
      MaxScore = locmax;
      return true;
    }
    return false;
  }


  public boolean AddModule( Module a ){
    if( Stack == null ){
      Stack = new ArrayList();
    }

    Stack.add( a );

    return true;
  }

};
