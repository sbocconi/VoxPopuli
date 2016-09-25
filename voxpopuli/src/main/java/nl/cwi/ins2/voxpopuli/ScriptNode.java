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

/*********************************** ScriptNode *****************************/
// This class represents a single rule in a module

import java.util.ArrayList;
import voxpopuli.Rule;

public class ScriptNode{

  /*********************
   *  CONSTANTS        *
   *********************/


  final static char FIRST = 0;
  final static char AND = 0;
  final static char OR = 1;
  final static char LEAF = 2;
  final static char LAST = LEAF;

  /*********************
   *  VARIABLES *
   **********************/



  int LinkType; // how is this node connected to the next one
  Rule theRule; // the rule to execute
  ArrayList NextScriptNode;
  int Level;

  public ScriptNode( int l, Rule r){

    theRule = r;
    LinkType = l;
    NextScriptNode = new ArrayList();
    Level = 0;
  }

/*  public ScriptNode( ScriptNode a){


  }
*/

  public boolean SetLinkType(char c){
    if( c<FIRST || c>LAST ){
      return false;
    }
    LinkType = c;
    return true;
  }

  public void AddScriptNode( ScriptNode a ){
    this.NextScriptNode.add( a );
    a.Level = this.Level + 1;
  }
};
