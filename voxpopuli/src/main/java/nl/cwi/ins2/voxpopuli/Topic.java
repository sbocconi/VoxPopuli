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
public class Topic{

  public String TopicId;

  public String TopicText;

  public Topic( String a, String b ){
    if( a != null ){
      TopicId = new String( a );
    } else{
      TopicId = new String( "" );
    }
    if( b != null ){
      TopicText = new String( b );
    } else{
      TopicText = new String( "" );
    }
  }
}
