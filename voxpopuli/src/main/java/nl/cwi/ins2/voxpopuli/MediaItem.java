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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class MediaItem{

  ArrayList<Segment> VideoSegments;
  ArrayList<Segment> AudioSegments;
  ArrayList<Segment> StillSegments;
  ArrayList<Segment> TextSegments;

//  static final int SCORE = 0;
//  static final int PROXIMITY = 1;
//  static final int QUANTITY = 2;

  double score = 0;



//  double quantity = 0;

  public void AddDescription( String a, boolean replace ){
    if( a == null ){
      a = new String( "" );
    }
    if( this.VideoSegments != null ){
      for( int i = 0; i < VideoSegments.size(); i++ ){
        if( ( ( VideoSegment )VideoSegments.get( i ) ).Description != null && replace == false ){
          ( ( VideoSegment )VideoSegments.get( i ) ).Description = new String( ( ( VideoSegment )VideoSegments.get( i ) ).
              Description + a );
        } else{
          ( ( VideoSegment )VideoSegments.get( i ) ).Description = new String( a );
        }
      }
    }
    if( this.AudioSegments != null ){
      for( int i = 0; i < AudioSegments.size(); i++ ){
        if( ( ( AudioSegment )AudioSegments.get( i ) ).Description != null && replace == false ){
          ( ( AudioSegment )AudioSegments.get( i ) ).Description = new String( ( ( AudioSegment )AudioSegments.get( i ) ).
              Description + a );
        } else{
          ( ( AudioSegment )AudioSegments.get( i ) ).Description = new String( a );
        }
      }
    }
    if( this.StillSegments != null ){
      for( int i = 0; i < StillSegments.size(); i++ ){
        if( ( ( StillSegment )StillSegments.get( i ) ).Description != null && replace == false ){
          ( ( StillSegment )StillSegments.get( i ) ).Description = new String( ( ( StillSegment )StillSegments.get( i ) ).
              Description + a );
        } else{
          ( ( StillSegment )StillSegments.get( i ) ).Description = new String( a );
        }
      }
    }
    if( this.TextSegments != null ){
      for( int i = 0; i < TextSegments.size(); i++ ){
        if( ( ( TextSegment )TextSegments.get( i ) ).Description != null && replace == false ){
          ( ( TextSegment )TextSegments.get( i ) ).Description = new String( ( ( TextSegment )TextSegments.get( i ) ).
              Description + a );
        } else{
          ( ( TextSegment )TextSegments.get( i ) ).Description = new String( a );
        }
      }
    }
  }

  MediaItem(){

    this.VideoSegments = new ArrayList<Segment>();
    this.AudioSegments = new ArrayList<Segment>();
    this.StillSegments = new ArrayList<Segment>();
    this.TextSegments = new ArrayList<Segment>();
    this.score = 0;
  }

  public MediaItem( String[] Ids, DataContainer Container ){
    if( Ids == null ){
      return;
    }

    for( int i = 0; i < Ids.length; i++ ){
      add( Ids[i], Container );
    }
  }

  public MediaItem( String Id, DataContainer Container ){
    add( Id, Container );
  }

  public void add( String Id, DataContainer Container ){
    if( Id == null ){
      return;
    }

    VideoSegment a;
    AudioSegment b;
    StillSegment c;
    TextSegment d;

    if( ( a = ( VideoSegment )Container.Videos.get( Id ) ) != null ){
      if( VideoSegments == null ){
        VideoSegments = new ArrayList<Segment>();
      }
      if( !VideoSegments.contains( a ) ){
        VideoSegments.add( a );
      }

    } else if( ( b = ( AudioSegment )Container.Audios.get( Id ) ) != null ){
      if( AudioSegments == null ){
        AudioSegments = new ArrayList<Segment>();
      }
      if( !AudioSegments.contains( a ) ){
        AudioSegments.add( a );
      }

    } else if( ( c = ( StillSegment )Container.Images.get( Id ) ) != null ){
      if( StillSegments == null ){
        StillSegments = new ArrayList<Segment>();
      }
      if( !StillSegments.contains( a ) ){
        StillSegments.add( a );
      }

    } else if( ( d = ( TextSegment )Container.Texts.get( Id ) ) != null ){
      if( TextSegments == null ){
        TextSegments = new ArrayList<Segment>();
      }
      if( !TextSegments.contains( a ) ){
        TextSegments.add( a );
      }
    }

  }

  MediaItem( MediaItem a ) throws Exception{
    if( a == null ){
      throw new Exception( "Object null in MediaItem copy constructor " );
    } else{
      if( a.VideoSegments != null ){
        VideoSegments = new ArrayList<Segment>( a.VideoSegments );
      }
      if( a.AudioSegments != null ){
        AudioSegments = new ArrayList<Segment>( a.AudioSegments );
      }
      if( a.StillSegments != null ){
        StillSegments = new ArrayList<Segment>( a.StillSegments );
      }
      if( a.TextSegments != null ){
        TextSegments = new ArrayList<Segment>( a.TextSegments );
      }
      this.score = a.score;
    }
  }

  public boolean Contains( Segment theSeg ){

    if( theSeg instanceof VideoSegment ){
      if( VideoSegments != null ){
        for( int i = 0; i < VideoSegments.size(); i++ ){
          Segment aSeg = ( Segment )VideoSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            return true;
          }
        }
      }
    } else if( theSeg instanceof AudioSegment ){
      if( AudioSegments != null ){
        for( int i = 0; i < AudioSegments.size(); i++ ){
          Segment aSeg = ( Segment )AudioSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            return true;
          }
        }
      }
    } else if( theSeg instanceof StillSegment ){
      if( StillSegments != null ){
        for( int i = 0; i < StillSegments.size(); i++ ){
          Segment aSeg = ( Segment )StillSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            return true;
          }
        }
      }
    } else if( theSeg instanceof TextSegment ){
      if( TextSegments != null ){
        for( int i = 0; i < TextSegments.size(); i++ ){
          Segment aSeg = ( Segment )TextSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            return true;
          }
        }
      }
    }
    return false;

  }

  public boolean Contains( String ID ){

    if( VideoSegments != null ){
      for( int i = 0; i < VideoSegments.size(); i++ ){
        Segment aSeg = ( Segment )VideoSegments.get( i );
        if( aSeg.Id.equals( ID ) ){
          return true;
        }
      }
    }
    if( AudioSegments != null ){
      for( int i = 0; i < AudioSegments.size(); i++ ){
        Segment aSeg = ( Segment )AudioSegments.get( i );
        if( aSeg.Id.equals( ID ) ){
          return true;
        }
      }
    }
    if( StillSegments != null ){
      for( int i = 0; i < StillSegments.size(); i++ ){
        Segment aSeg = ( Segment )StillSegments.get( i );
        if( aSeg.Id.equals( ID ) ){
          return true;
        }
      }
    }
    if( TextSegments != null ){
      for( int i = 0; i < TextSegments.size(); i++ ){
        Segment aSeg = ( Segment )TextSegments.get( i );
        if( aSeg.Id.equals( ID ) ){
          return true;
        }
      }
    }

    return false;

  }

  public boolean remove( Segment theSeg ){

    if( theSeg instanceof VideoSegment ){
      if( VideoSegments != null ){
        for( int i = 0; i < VideoSegments.size(); i++ ){
          Segment aSeg = ( Segment )VideoSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            VideoSegments.remove( i );
            return true;
          }
        }
      }
    } else if( theSeg instanceof AudioSegment ){
      if( AudioSegments != null ){
        for( int i = 0; i < AudioSegments.size(); i++ ){
          Segment aSeg = ( Segment )AudioSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            AudioSegments.remove( i );
            return true;
          }
        }
      }
    } else if( theSeg instanceof StillSegment ){
      if( StillSegments != null ){
        for( int i = 0; i < StillSegments.size(); i++ ){
          Segment aSeg = ( Segment )StillSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            StillSegments.remove( i );
            return true;
          }
        }
      }
    } else if( theSeg instanceof TextSegment ){
      if( TextSegments != null ){
        for( int i = 0; i < TextSegments.size(); i++ ){
          Segment aSeg = ( Segment )TextSegments.get( i );
          if( aSeg.Id.equals( theSeg.Id ) ){
            TextSegments.remove( i );
            return true;
          }
        }
      }
    }
    return false;

  }

  public boolean add( Segment theSeg ){

    if( theSeg instanceof VideoSegment ){
      if( VideoSegments != null && (VideoSegments.contains(theSeg) == false) ){
        VideoSegments.add( theSeg );
        return true;
      }
    } else if( theSeg instanceof AudioSegment ){
      if( AudioSegments != null && (AudioSegments.contains(theSeg) == false) ){
        AudioSegments.add( theSeg );
        return true;
      }
    } else if( theSeg instanceof StillSegment ){
      if( StillSegments != null && (StillSegments.contains(theSeg) == false) ){
        StillSegments.add( theSeg );
        return true;
      }
    } else if( theSeg instanceof TextSegment ){
      if( TextSegments != null && (TextSegments.contains(theSeg) == false) ){
        StillSegments.add( theSeg );
        return true;
      }
    }
    return false;

  }

  public void add( MediaItem a ) throws Exception{
    if( a == null ){
      return;
    } else{
      if( a.VideoSegments != null ){
        for( int i = 0; i < a.VideoSegments.size(); i++ ){
          VideoSegments.add( a.VideoSegments.get( i ) );
        }
      }
      if( a.AudioSegments != null ){
        for( int i = 0; i < a.AudioSegments.size(); i++ ){
          AudioSegments.add( a.AudioSegments.get( i ) );
        }
      }
      if( a.StillSegments != null ){
        for( int i = 0; i < a.StillSegments.size(); i++ ){
          StillSegments.add( a.StillSegments.get( i ) );
        }
      }
      if( a.TextSegments != null ){
        for( int i = 0; i < a.TextSegments.size(); i++ ){
          TextSegments.add( a.TextSegments.get( i ) );
        }
      }
    }
  }

  public boolean isEmpty(){
    return( this.VideoSegments.isEmpty() &&
            this.AudioSegments.isEmpty() &&
            this.StillSegments.isEmpty() &&
            this.TextSegments.isEmpty() );
  }

  public int size(){
    return( this.VideoSegments.size() +
            this.AudioSegments.size() +
            this.StillSegments.size() +
            this.TextSegments.size() );

  }

  public String getInterviewId() throws Exception{

    boolean first = true;

    ArrayList<String> result = new ArrayList<String>();
    String ID = null;

    first = FindInterviewId( first, this.VideoSegments, result );
    first = FindInterviewId( first, this.AudioSegments, result );
    first = FindInterviewId( first, this.StillSegments, result );
    first = FindInterviewId( first, this.TextSegments, result );

    if( result.size() == 1 ){
      ID = new String( (String)result.get(0));
      return ID;
    }
    return null;
  }

  private boolean FindInterviewId( boolean first, ArrayList<Segment> Items, ArrayList<String> result ) throws Exception{

    if( Items != null ){
      for( int i = 0; i < Items.size(); i++ ){
        Segment theSeg = Items.get( i );
        if( first ){
          if( theSeg.IsInterview ){
            for( int j=0; j<theSeg.InterviewId.size(); j++ ){
              result.add(theSeg.InterviewId.get(j));
            }
          } else{
            result.clear();
          }
        } else{
          if( theSeg.IsInterview ){
            for( int j=0; j<result.size(); j++ ){
              String item = (String) result.get(j);
              if( !theSeg.InterviewId.contains( item ) ){
                result.remove(j);
                j--;
              }
            }

          }else{
            result.clear();
          }
        }
        first = false;
      }
    }
    return first;
  }

  public Hashtable<String, String> getStatementIds() throws Exception{

    Hashtable<String, String> Temp = null;

    Temp = FindStatementsId( this.VideoSegments, true, Temp );
    Temp = FindStatementsId( this.AudioSegments, false, Temp );
    Temp = FindStatementsId( this.StillSegments, false, Temp );
    Temp = FindStatementsId( this.TextSegments, false, Temp );

    return Temp;
  }

  private Hashtable<String, String> FindStatementsId( ArrayList<Segment> Items, boolean first, Hashtable<String, String> IDs ){

    Hashtable<String, String> Temp;

    if( IDs == null ){
      Temp = new Hashtable<String, String>();
    } else{
      Temp = new Hashtable<String, String>( IDs );
    }

    if( Items != null ){
      for( int i = 0; i < Items.size(); i++ ){
        Segment aSeg = ( Segment )Items.get( i );

        if( aSeg.StatementIds != null ){
          for( Enumeration<?> ee = aSeg.StatementIds.elements(); ee.hasMoreElements(); ){
            String anID = ( String )ee.nextElement();
            if( anID != null ){
              if( first ){
                Temp.put( anID, anID );
                // If the Seg has more statements, it will be present in more MediaItems
                first = false;
              } else if( !Temp.containsKey( anID ) ){
                Temp.clear();
              }
            }
          }
        }
      }
    }

    return Temp;
  }

  public double getScore(  ){

    return this.score;
  }

  public void addScore( double add ){
    this.score = this.score + add;
  }

  public void setScore( double add ){
    this.score = add;
  }

}
