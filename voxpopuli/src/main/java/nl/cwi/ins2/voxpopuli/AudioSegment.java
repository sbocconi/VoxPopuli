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

// single audio segment

import org.eclipse.rdf4j.model.Value;

public class AudioSegment extends Segment{
  String BeginFrame;
  String EndFrame;

  String Location;
  String Place;
  String Time;

  AudioSegment( Value Id, String StatementId, String InterviewId, Value BeginFrame, Value EndFrame,
                Value FileName, Value Description, Value Language, Value Quality, Value Subject,
                Value Location, Value Place, Value Time ){

    super( Id, StatementId, InterviewId, FileName, Description, Language, Quality, Subject );
    if( BeginFrame != null ){
      this.BeginFrame = new String( BeginFrame.toString() );
    }
    if( EndFrame != null ){
      this.EndFrame = new String( EndFrame.toString() );
    }

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

  AudioSegment( AudioSegment a ) throws Exception{
    super( a );
    if( a.BeginFrame != null ){
      BeginFrame = new String( a.BeginFrame );
    }
    if( a.EndFrame != null ){
      EndFrame = new String( a.EndFrame );
    }
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
