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
import org.eclipse.rdf4j.model.Value;

// single image
public class StillSegment extends Segment{

  String Location;
  String Place;
  String Time;

  StillSegment( Value Id, String StatementId, String InterviewId, Value FileName, Value Description,
                Value Language, Value Quality, Value Subject, Value Location, Value Place,
                Value Time ){
    super( Id, StatementId, InterviewId, FileName, Description, Language, Quality, Subject );

    if( Location != null ){
      this.Location = new String( Location.toString() );
    }
    if( Place != null ){
      this.Place = new String( Place.toString() );
    }
    if( Time != null ){
      this.Time = new String( Time.toString() );
    }
  }

  StillSegment( StillSegment a ) throws Exception{
    super( a );

    if( a.Location != null ){
      Location = new String( a.Location );
    }
    if( a.Place != null ){
      Place = new String( a.Place );
    }
    if( a.Time != null ){
      Time = new String( a.Time );
    }
  }
}
