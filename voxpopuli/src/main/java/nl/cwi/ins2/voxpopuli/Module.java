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


/*********************************** Module *****************************/
// This class represents a module that contains all the rules according to
// a particular discipline like Narrative, Argumentation

public class Module{

  /*********************
   *  CONSTANTS        *
   *********************/


  final int RANKSTACKSIZE = 20;

  /*********************
   *  VARIABLES *
   **********************/

  private String ModuleName;
  private ScriptNode StartNode; // First Rule

  private Scheduler parent;

  private ScriptNode CurrentNode;

  int NodeNr; // number of rules in a module
  private int NodeLevel;
  private int Results[];

  public Module( Scheduler a ){

    parent = a;
    NodeNr = 0;

    InizializeModule();
  }

  private Module(){};

  public void InizializeModule(){

    NodeLevel = 0;
    CurrentNode = StartNode;
    Results = new int[NodeNr];

    for( int i = 0; i < NodeNr; i++ ){
      Results[i] = 0;
    }

  }

  private int CheckLength( ScriptNode a ) throws Exception{
    int size = a.NextScriptNode.size();

    if( size == 0 ){
      if( a.LinkType != ScriptNode.LEAF ){
        throw new Exception( "Leaf attribute not correctly set" );
      }
      return 1; // myself
    } else if( a.LinkType == ScriptNode.LEAF ){
      throw new Exception( "Leaf attribute not correctly set" );
    }
    int max = 0, curr;
    for( int i = 0; i < size; i++ ){
      ScriptNode tmp = ( ScriptNode )a.NextScriptNode.get( i );
      curr = CheckLength( tmp );
      if( curr > max ){
        max = curr;
      }
    }
    return max + 1;
  }

  public void AddStartNode( ScriptNode a ) throws Exception{

    StartNode = a;
    NodeNr = CheckLength( StartNode );
    return;
  }

  public void RunModule( boolean first ) throws RuleException, Exception{

    parent.theStorySpace.StartTrans();
    if( first == true ){
      Results[NodeLevel] = CurrentNode.theRule.DoSelection( parent.theStorySpace, NodeLevel );
      first = false;
    } else{
      Results[NodeLevel] = CurrentNode.theRule.ApplyRule( parent.theStorySpace, NodeLevel );
    }
    if( parent.GoodBadness() ){
      throw new RuleException();

    } else{
      NodeLevel++;
      // Examine link to next node(s)
      switch( CurrentNode.LinkType ){
        case ScriptNode.AND:
          CurrentNode = ( ScriptNode )CurrentNode.NextScriptNode.get( 0 );
          RunModule( first );
          break;
        case ScriptNode.OR:

          // We try in order the different branches till the badness of one
          // is good enough to return, or if none is good enough we
          // take the best


          ScriptNode LocalCurrent = CurrentNode;
          int LocalLevel = NodeLevel;
          int SchedulerLevel = parent.CurrentIndex;

          int LocalRank = parent.theStorySpace.GetTrans();
          int length = LocalCurrent.NextScriptNode.size();
          int locmax;

          for( int i = 0; i < length; i++ ){
            CurrentNode = ( ScriptNode )LocalCurrent.NextScriptNode.get( i );
            NodeLevel = LocalLevel;
            parent.theStorySpace.SetTrans( LocalRank );
            parent.CurrentIndex = SchedulerLevel;

            RunModule( first );

            if( parent.IsMax() ){
              parent.theStorySpace.SaveSpace();

            }

          }


          break;
        case ScriptNode.LEAF:
          // Undo the increment because we are leaving the module
          NodeLevel--;
          parent.RunNextModule();
          break;
        default:
          throw new Exception( "Uknown link type " );

      }
    }
    return;
  }

  public int CalculateModuleScore(){

    int total = 0;

    for( int i = 0; i <= NodeLevel; i++ ){
      total = total + Results[i];
    }

    return total;
  }
};
