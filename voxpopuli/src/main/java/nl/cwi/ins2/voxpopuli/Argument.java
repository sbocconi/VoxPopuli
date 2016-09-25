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

public class Argument{
  public ToulminNode[] Nodes;
  // Nested arguments
  public Argument[] Arguments;
  // This type refers to the Toulmin role of the whole argument structure,
  // it does not apply at the top level, but only for every recursion.
  // This means that if this is a nested argument, this is its type.
  public NameIndex ToulminType;
  public Argument(){};
  public Argument( Argument a ) throws Exception{
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

