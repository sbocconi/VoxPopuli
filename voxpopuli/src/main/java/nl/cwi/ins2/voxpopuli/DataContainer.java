package nl.cwi.ins2.voxpopuli;

import java.util.Hashtable;

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
public class DataContainer{


  public UserModel[] UserModelsArray = null; // List of possible User Models in the repository

  public Opinion[] OpinionsArray = null; // List of possible positions in the repository

  public Topic[] TopicsArray = null; // List of possible positions in the repository

  public Question[] QuestionsArray = null; // List of interviews with data

  public Hashtable Interviewees = null; // List of interviewees

  public Interview[] InterviewsArray = null; // List of interviews with topic and position

  public Hashtable Statements = null; // List of all statements

  public Hashtable Concepts = null; // List of all concepts

  public Hashtable Modifiers = null; // List of all modifiers

  public Hashtable Predicates = null; // List of all predicates

  public Hashtable Relations = null; // List of all links

  public Hashtable Videos = null; // List of all video segments

  public Hashtable Audios = null; // List of all audio segments

  public Hashtable Images = null; // List of all image segments

  public Hashtable Texts = null; // List of all text segments

  public DataContainer() throws Exception{

  }

  public void AddDataToMedia( String[] Ids, String Description, String InterviewId, String StatementId, boolean IsInterview ){

    if( Ids == null ){
      return;
    }
    VideoSegment a;
    AudioSegment b;
    StillSegment c;
    TextSegment d;

    for( int i = 0; i < Ids.length; i++ ){
      if( ( Videos != null ) && ( a = ( VideoSegment )Videos.get( Ids[i] ) ) != null ){
        a.AddStatementId( StatementId );
        a.AddDescription( Description );
        a.AddInterviewId( InterviewId );
        a.SetIsInterview(IsInterview);
      } else if( ( Audios != null ) && ( b = ( AudioSegment )Audios.get( Ids[i] ) ) != null ){
        b.AddStatementId( StatementId );
        b.AddDescription( Description );
        b.AddInterviewId( InterviewId );
        b.SetIsInterview(IsInterview);
      } else if( ( Images != null ) && ( c = ( StillSegment )Images.get( Ids[i] ) ) != null ){
        c.AddStatementId( StatementId );
        c.AddDescription( Description );
        c.AddInterviewId( InterviewId );
        c.SetIsInterview(IsInterview);
      } else if( ( Texts != null ) && ( d = ( TextSegment )Texts.get( Ids[i] ) ) != null ){
        d.AddStatementId( StatementId );
        d.AddDescription( Description );
        d.AddInterviewId( InterviewId );
        d.SetIsInterview(IsInterview);
      }
    }
  }

  public Segment getMedia( String Id ){

    if( Id == null ){
      return null;
    }
    Segment a;

    if( ( Videos != null ) && ( a = ( VideoSegment )Videos.get( Id ) ) != null ){
      return a;
    } else if( ( Audios != null ) && ( a = ( AudioSegment )Audios.get( Id ) ) != null ){
      return a;
    } else if( ( Images != null ) && ( a = ( StillSegment )Images.get( Id ) ) != null ){
      return a;
    } else if( ( Texts != null ) && ( a = ( TextSegment )Texts.get( Id ) ) != null ){
      return a;
    }
    return null;
  }

};
