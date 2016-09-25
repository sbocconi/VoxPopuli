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

public class MediaContainer{
  Hashtable VideoSegments;
  Hashtable AudioSegments;
  Hashtable StillSegments;
  Hashtable TextSegments;

  MediaContainer(){
    VideoSegments = new Hashtable();
    AudioSegments = new Hashtable();
    StillSegments = new Hashtable();
    TextSegments = new Hashtable();

  }

  MediaContainer( Hashtable VideoSegments, Hashtable AudioSegments, Hashtable StillSegments,
                  Hashtable TextSegments ){
    if( VideoSegments != null ){
      this.VideoSegments = new Hashtable( VideoSegments );
    } else{
      this.VideoSegments = new Hashtable();
    }
    if( AudioSegments != null ){
      this.AudioSegments = new Hashtable( AudioSegments );
    } else{
      this.AudioSegments = new Hashtable();
    }
    if( StillSegments != null ){
      this.StillSegments = new Hashtable( StillSegments );
    } else{
      this.StillSegments = new Hashtable();
    }
    if( TextSegments != null ){
      this.TextSegments = new Hashtable( TextSegments );
    } else{
      this.TextSegments = new Hashtable();
    }

  }

  MediaContainer( MediaContainer a ) throws Exception{
    if( a == null ){
      throw new Exception( "Object null in MediaContainer copy constructor " );
    } else{
      if( a.VideoSegments != null ){
        VideoSegments = new Hashtable( a.VideoSegments );
      }
      if( a.AudioSegments != null ){
        AudioSegments = new Hashtable( a.AudioSegments );
      }
      if( a.StillSegments != null ){
        StillSegments = new Hashtable( a.StillSegments );
      }
      if( a.TextSegments != null ){
        TextSegments = new Hashtable( a.TextSegments );
      }
    }
  }

  public boolean IsContained( String key ){
    if( VideoSegments.containsKey( key ) ||
        AudioSegments.containsKey( key ) ||
        StillSegments.containsKey( key ) ||
        TextSegments.containsKey( key ) ){
      return true;
    }
    return false;
  }

  public void clear(){
    VideoSegments.clear();
    AudioSegments.clear();
    StillSegments.clear();
    TextSegments.clear();
  }

  public void put( String key, DataContainer a ){
    if( a.Videos.containsKey( key ) ){
      VideoSegment b = ( VideoSegment )a.Videos.get( key );
      VideoSegments.put( key, b );
    } else if( a.Audios.containsKey( key ) ){
      AudioSegment b = ( AudioSegment )a.Audios.get( key );
      AudioSegments.put( key, b );
    } else if( a.Images.containsKey( key ) ){
      StillSegment b = ( StillSegment )a.Images.get( key );
      StillSegments.put( key, b );
    } else if( a.Texts.containsKey( key ) ){
      TextSegment b = ( TextSegment )a.Texts.get( key );
      TextSegments.put( key, b );
    }
  }

  public void put( Segment theSeg ){
    if( theSeg instanceof VideoSegment ){
      VideoSegments.put( theSeg.Id, theSeg );
    } else if( theSeg instanceof AudioSegment ){
      AudioSegments.put( theSeg.Id, theSeg );
    } else if( theSeg instanceof StillSegment ){
      StillSegments.put( theSeg.Id, theSeg );
    } else if( theSeg instanceof TextSegment ){
      TextSegments.put( theSeg.Id, theSeg );
    }
  }

  public void remove( Segment a ){
    if( VideoSegments.containsKey( a.Id ) ){
      VideoSegments.remove( a.Id );
    } else if( AudioSegments.containsKey( a.Id ) ){
      AudioSegments.remove( a.Id );
    } else if( TextSegments.containsKey( a.Id ) ){
      StillSegments.remove( a.Id );
    } else if( TextSegments.containsKey( a.Id ) ){
      TextSegments.remove( a.Id );
    }

  }

  public int size(){
    return VideoSegments.size() + AudioSegments.size() + StillSegments.size() + TextSegments.size();
  }
};
