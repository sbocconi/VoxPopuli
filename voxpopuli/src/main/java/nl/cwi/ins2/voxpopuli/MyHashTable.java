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

import java.util.Hashtable;
import java.util.Enumeration;

class MyHashTable extends Hashtable{
  public MyHashTable(){
    super();
  }

  public MyHashTable( int size ){
    super( size );
  }

  public MyHashTable( Hashtable a ){
    super( a );
  }

  public boolean Contains( String key ){

    for( Enumeration e = this.elements(); e.hasMoreElements(); ){
      MediaItem anItem = ( MediaItem )e.nextElement();
      if( anItem.Contains( key ) ){
        return true;
      }
    }
    return false;
  }

  public boolean remove( MediaItem theItem ){

    for( Enumeration e = this.keys(); e.hasMoreElements(); ){
      Object key = e.nextElement();
      MediaItem anItem = ( MediaItem )this.get( key );

      if( theItem.VideoSegments != null ){
        for( int i = 0; i < theItem.VideoSegments.size(); i++ ){
          Segment Seg = ( Segment )theItem.VideoSegments.get( i );
          anItem.remove( Seg );
        }
      }

      if( theItem.AudioSegments != null ){
        for( int i = 0; i < theItem.AudioSegments.size(); i++ ){
          Segment Seg = ( Segment )theItem.AudioSegments.get( i );
          anItem.remove( Seg );
        }
      }

      if( theItem.StillSegments != null ){
        for( int i = 0; i < theItem.StillSegments.size(); i++ ){
          Segment Seg = ( Segment )theItem.StillSegments.get( i );
          anItem.remove( Seg );
        }
      }

      if( theItem.TextSegments != null ){
        for( int i = 0; i < theItem.TextSegments.size(); i++ ){
          Segment Seg = ( Segment )theItem.TextSegments.get( i );
          anItem.remove( Seg );
        }
      }

      if( anItem.isEmpty() ){
        this.remove( key );
      }
    }

    return false;
  }

  public void put( MediaItem theItem ) throws Exception{

    if( theItem.VideoSegments != null ){
      for( int i = 0; i < theItem.VideoSegments.size(); i++ ){
        Segment Seg = ( Segment )theItem.VideoSegments.get( i );
        this.Fill( Seg );
      }
    }

    if( theItem.AudioSegments != null ){
      for( int i = 0; i < theItem.AudioSegments.size(); i++ ){
        Segment Seg = ( Segment )theItem.AudioSegments.get( i );
        this.Fill( Seg );
      }
    }

    if( theItem.StillSegments != null ){
      for( int i = 0; i < theItem.StillSegments.size(); i++ ){
        Segment Seg = ( Segment )theItem.StillSegments.get( i );
        this.Fill( Seg );
      }
    }

    if( theItem.TextSegments != null ){
      for( int i = 0; i < theItem.TextSegments.size(); i++ ){
        Segment Seg = ( Segment )theItem.TextSegments.get( i );
        this.Fill( Seg );
      }
    }
  }

  public void Fill( Segment aSeg ) throws Exception{

    if( aSeg.InterviewId == null ||
        ( aSeg.IsInterview == false &&
          aSeg.StatementIds == null ) ){
      throw new Exception( "Interview ID null in segment " );
    }

    if( aSeg.IsInterview == true ){
      for( int i=0; i<aSeg.InterviewId.size(); i++){
        String Id = (String)aSeg.InterviewId.get(i);
        if( this.containsKey( Id ) ){
          MediaItem a = ( MediaItem )this.get( Id );
          a.add( aSeg );
        } else{
          MediaItem a = new MediaItem();
          a.add( aSeg );
          this.put( Id, a );
        }
      }
    }
    if( aSeg.StatementIds != null ){
      for( Enumeration ee = aSeg.StatementIds.elements(); ee.hasMoreElements(); ){
        String anID = ( String )ee.nextElement();
        if( anID != null ){
          if( this.containsKey( anID ) ){
            MediaItem a = ( MediaItem )this.get( anID );
            a.add( aSeg );
          } else{
            MediaItem a = new MediaItem();
            a.add( aSeg );
            this.put( anID, a );
          }
        }
      }
    }
  }
}
