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

public class Opinion{
  public String OpinionId;
  public String OpinionText;

  public String PositionId;
  public String ContrTopicId;

  public Opinion( String a, String b, String c, String d ){
    if( a != null ){
      OpinionId = new String( a );
    } else{
      OpinionId = new String( "" );
    }
    if( b != null ){
      OpinionText = new String( b );
    } else{
      OpinionText = new String( "" );
    }
    if( c != null ){
      PositionId = new String( c );
    } else{
      PositionId = new String( "" );
    }
    if( d != null ){
      ContrTopicId = new String( b );
    } else{
      ContrTopicId = new String( "" );
    }

  }
}

