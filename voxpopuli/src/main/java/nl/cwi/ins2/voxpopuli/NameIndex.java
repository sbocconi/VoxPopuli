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

public class NameIndex{
  String Name;
  int index;
  NameIndex( String a, int b ){
    if( a != null ){
      this.Name = new String( a );
    } else{
      this.Name = new String( "" );
    }
    this.index = b;
  }

  NameIndex( NameIndex a ){
    if( a != null && a.Name != null ){
      this.Name = new String( a.Name );
    } else{
      this.Name = new String( "" );
    }
    this.index = a.index;
  }
}
