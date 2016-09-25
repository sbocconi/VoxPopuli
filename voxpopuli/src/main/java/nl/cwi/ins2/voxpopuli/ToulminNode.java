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

public class ToulminNode{
  NameIndex ToulminType;
  // This contains only the IDs of the statements. The statements are in the hash table
  String[] theStatementIDs;
  boolean StatementsinAND;
  String LogicType; //Not used for the moment
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
      if( a.theStatementIDs != null ){
        this.theStatementIDs = new String[a.theStatementIDs.length];
        for( int i = 0; i < a.theStatementIDs.length; i++ ){
          this.theStatementIDs[i] = new String( a.theStatementIDs[i] );
        }
      }
      this.StatementsinAND = a.StatementsinAND;
      // Pointer because they all are in an array and do not get modified (hopefully)
      this.theInterview = a.theInterview;
      if( a.LogicType != null ){
        this.LogicType = new String( a.LogicType );
      } else{
        this.LogicType = new String( "" );
      }
    }
  }
}
