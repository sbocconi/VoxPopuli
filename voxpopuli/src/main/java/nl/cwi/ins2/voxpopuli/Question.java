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

public class Question{
  public String QuestionId;
  public String QuestionText;


  public Question( String a, String b ){
    if( a != null ){
      QuestionId = new String( a );
    } else{
      QuestionId = new String( "" );
    }
    if( b != null ){
      QuestionText = new String( b );
    } else{
      QuestionText = new String( "" );
    }
  }
}
