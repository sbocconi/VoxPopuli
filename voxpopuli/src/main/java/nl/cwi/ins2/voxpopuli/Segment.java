package nl.cwi.ins2.voxpopuli;

import java.util.Hashtable;
import java.util.ArrayList;
import org.openrdf.model.Value;

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
public class Segment{
  String Id;
  Hashtable StatementIds;
  ArrayList InterviewId;
  String Description;
  String FileName;
  String Subject;
  String Quality;
  String Language;
  boolean IsInterview;
  double score;
  double proximity;
  boolean NeedPreTrans;
  boolean NeedPostTrans;

  public Segment( Value Id, String StatementId, String InterviewId, Value FileName,
                  Value Description, Value Language, Value Quality, Value Subject ){
    if( Id != null ){
      this.Id = new String( Id.toString() );
    }
    if( StatementId != null ){
      this.StatementIds = new Hashtable();
      this.StatementIds.put( StatementId, new String( StatementId ) );
    }
    if( InterviewId != null ){
      this.InterviewId = new ArrayList();
      this.InterviewId.add(InterviewId);
    }
    if( FileName != null ){
      this.FileName = new String( FileName.toString() );
    }
    if( Description != null ){
      this.Description = new String( Description.toString() );
    }
    if( Language != null ){
      this.Language = new String( Language.toString() );
    }
    if( Quality != null ){
      this.Quality = new String( Quality.toString() );
    }
    if( Subject != null ){
      this.Subject = new String( Subject.toString() );
    }

    this.score = 0;
    this.proximity = 0;
    this.NeedPreTrans = false;
    this.NeedPostTrans = false;


  }

  Segment( Segment a ) throws Exception{
  if( a == null ){
    throw new Exception( "Object null in Segment copy constructor " );
  } else{

    if( a.Id != null ){
      Id = new String( a.Id );
    }
    if( a.StatementIds != null ){
      StatementIds = new Hashtable( a.StatementIds );
    }
    if( a.InterviewId != null ){
      this.InterviewId = new ArrayList(a.InterviewId);
    }
    if( a.FileName != null ){
      FileName = new String( a.FileName );
    }
    if( a.Description != null ){
      Description = new String( a.Description );
    }
    if( a.Language != null ){
      Language = new String( a.Language );
    }
    if( a.Quality != null ){
      Quality = new String( a.Quality );
    }
    if( a.Subject != null ){
      Subject = new String( a.Subject );
    }
    this.IsInterview = a.IsInterview;

    this.score = a.score;

    this.NeedPreTrans = a.NeedPreTrans;

    this.NeedPostTrans = a.NeedPostTrans;

  }
}


  public void AddStatementId( String StatementId ){

    if( StatementId == null ){
      return;
    }
    if( StatementIds == null ){
      StatementIds = new Hashtable();
      StatementIds.put( StatementId, new String( StatementId ) );
    } else if( !StatementIds.containsKey( StatementId ) ){
      StatementIds.put( StatementId, new String( StatementId ) );
    }
  }

  public void AddInterviewId( String InterviewId ){

    if( InterviewId == null ){
      return;
    }

    if( this.InterviewId == null ){
      this.InterviewId = new ArrayList();
    }

    this.InterviewId.add(InterviewId);
  }

  public void AddDescription( String Description ){

    if( Description == null ){
      return;
    }
    if( this.Description == null ){
      this.Description = new String( "" );
    }
    this.Description = new String( this.Description + " " + Description );
  }

  public void SetIsInterview( boolean IsInterview ){

    if( IsInterview == true ){
      this.IsInterview = IsInterview;
    }else if( this.IsInterview == true ){

    }else{
      this.IsInterview = IsInterview;
    }

  }
  public void addScore( double add ){
    this.score = this.score + add;
  }

  public Segment New( ) throws Exception{
    if( this instanceof VideoSegment ){
      return new VideoSegment( ( VideoSegment )this );
    } else if( this instanceof AudioSegment ){
      return new AudioSegment( ( AudioSegment )this );
    } else if( this instanceof StillSegment ){
      return new StillSegment( ( StillSegment )this );
    } else if( this instanceof TextSegment ){
      return new TextSegment( ( TextSegment )this );
    }
    return null;
  }
}
