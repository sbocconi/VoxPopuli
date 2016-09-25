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

// This class contains the Id of the statement being linked to
// and the chain of link operation made to get from the original statement
// to the linked one
public class Link{
  String StatementId;

  //This array contains objects of type LinkDescription
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

