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

// single text
import org.openrdf.model.Value;

public class TextSegment extends Segment{


  TextSegment( Value Id, String StatementId, String InterviewId, Value FileName, Value Description,
               Value Language, Value Quality, Value Subject ){
    super(Id, StatementId, InterviewId, FileName, Description, Language, Quality, Subject);

  }

  TextSegment( TextSegment a ) throws Exception{
    super(a);
  }
}
