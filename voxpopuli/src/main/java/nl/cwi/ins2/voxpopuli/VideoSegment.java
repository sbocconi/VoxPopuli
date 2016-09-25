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

import org.openrdf.model.Value;

// single video segment
public class VideoSegment extends Segment{
  String BeginFrame;
  String EndFrame;

  String StartFraming;
  String EndFraming;
  String Gaze;
  String Motion;

  String Location;
  String Place;
  String Time;

  VideoSegment( Value Id, String StatementId, String InterviewId, Value BeginFrame, Value EndFrame,
                Value FileName, Value Description, Value Language, Value Quality, Value Subject,
                Value StartFraming, Value EndFraming, Value Gaze, Value Motion, Value Location,
                Value Place, Value Time ){

    super( Id, StatementId, InterviewId, FileName, Description, Language, Quality, Subject );

    if( BeginFrame != null ){
      this.BeginFrame = new String( BeginFrame.toString() );
    }
    if( EndFrame != null ){
      this.EndFrame = new String( EndFrame.toString() );
    }
    if( StartFraming != null ){
      this.StartFraming = new String( StartFraming.toString() );
    }
    if( EndFraming != null ){
      this.EndFraming = new String( EndFraming.toString() );
    }
    if( Gaze != null ){
      this.Gaze = new String( Gaze.toString() );
    }
    if( Motion != null ){
      this.Motion = new String( Motion.toString() );
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

  VideoSegment( VideoSegment a ) throws Exception{
    super( a );
    if( a.BeginFrame != null ){
      BeginFrame = new String( a.BeginFrame );
    }
    if( a.EndFrame != null ){
      EndFrame = new String( a.EndFrame );
    }
    if( a.StartFraming != null ){
      StartFraming = new String( a.StartFraming );
    }
    if( a.EndFraming != null ){
      EndFraming = new String( a.EndFraming );
    }
    if( a.Gaze != null ){
      Gaze = new String( a.Gaze );
    }
    if( a.Motion != null ){
      Motion = new String( a.Motion );
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
