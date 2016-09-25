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

import voxpopuli.Rule;
import java.util.ArrayList;
import java.util.Hashtable;
import voxpopuli.Person;
import java.util.Enumeration;

public class RuleInstance{
  public RuleInstance(){
    try{
      jbInit();
    } catch( Exception ex ){
      ex.printStackTrace();
    }
  }

  /*********************
   *  CONSTANTS *
   **********************/



  /*************************
   *  PSEUDO VARIABLES     *
   *  set once and forever *
   *  with error checking  *
   *************************/

  /*********************
   *  VARIABLES *
   **********************/

  ArrayList MediaArray;
  Hashtable StorySpace;

  DataContainer theDataContainer;
  Outputs theOutputs;
  Util u;
  String OutputFilename;

  /*********************
   *  FUNCTIONS *
   **********************/


  /********************* RULES **********************/



  private class NarTimeOn implements Rule{

    public int ApplyRule( StorySpace a, int NodeLevel ){
      return 100;
    }

    public int DoSelection( StorySpace a, int NodeLevel ){

      return 100;

    }

  }

  private class Dummy implements Rule{

    public int ApplyRule( StorySpace a, int NodeLevel ){
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ){

      return 0;

    }

  }

  private class NarTimeOff implements Rule{

    public int ApplyRule( StorySpace a, int NodeLevel ){
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ){

      return 0;

    }

  }

  private interface PersonSelect{

    String FieldString( Person a );
  }

  private interface SegmentSelect{

    String FieldString( Segment a );
  }

  private class SelectRace implements PersonSelect, Rule{

    String[] RacesA;
    String[] RacesB;
    boolean MAINCHAR = false;

    public SelectRace( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          RacesA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            RacesA[i] = new String( A[i] );
          }
        }
      }
      if( B != null ){
        int size = B.length;
        RacesB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          RacesB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( RacesA != null ){
          SelectinStory( StoryA, OldStoryA, RacesA, this );
        }
        if( RacesB != null ){
          SelectinStory( StoryB, OldStoryB, RacesB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, RacesB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    public String FieldString( Person a ){
      return a.HasRace;
    }

  }

  private class SelectAge implements PersonSelect, Rule{

    String[] AgesA;
    String[] AgesB;
    boolean MAINCHAR = false;

    public SelectAge( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          AgesA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            AgesA[i] = new String( A[i] );
          }
        }
      }
      if( B != null ){
        int size = B.length;
        AgesB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          AgesB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( AgesA != null ){
          SelectinStory( StoryA, OldStoryA, AgesA, this );
        }
        if( AgesB != null ){
          SelectinStory( StoryB, OldStoryB, AgesB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, AgesB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    public String FieldString( Person a ){
      return a.HasAge;
    }

  }

  private class FilmFeatures implements Rule{

  boolean MAINCHAR = false;

  public FilmFeatures( String[] A, String[] B ){


  }

  public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

    ArrayList[] LoopPlot = {
        a.GetPlot( 0 ), a.GetPlot( 1 ), a.GetPlot( 2 )};

    for( int plot = 0; plot < LoopPlot.length; plot++ ){

      ArrayList Plot = LoopPlot[ plot ];

      for( int i = 0; i < Plot.size(); i++ ){
        MediaItem item = ( MediaItem )Plot.get( i );
        if( item.size() == 0 ){
          return 0;
        }

        ArrayList[] LoopItem = {
            item.AudioSegments, item.StillSegments, item.TextSegments, item.VideoSegments};

        Person theLastItemPerson = null, thePerson = null, theOldPerson = null;

        for( int j = 0; j < LoopItem.length; j++ ){

          theOldPerson = theLastItemPerson;

          if( LoopItem[j] != null ){
            for( int z = 0; z < LoopItem[j].size(); z++ ){

              Segment theSeg = ( Segment )LoopItem[j].get( z );

              thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );

              if( theOldPerson!= null && thePerson.Id.equals( theOldPerson.Id ) ){
                theSeg.NeedPreTrans = true;
              }
              theOldPerson = thePerson;
            }
          }
          if( j == (LoopItem.length -1) ){
            theLastItemPerson = thePerson;
          }
        }
      }
    }
    return 100;
  }

  public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

    if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
      throw new Exception( "Plot not null in first selection " );
    }
    return this.ApplyRule( a, NodeLevel );
  }

}


  private class SelectEducation implements PersonSelect, Rule{

    String[] EducationsA;
    String[] EducationsB;
    boolean MAINCHAR = false;

    public SelectEducation( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          EducationsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            EducationsA[i] = new String( A[i] );
          }
        }
      }
      if( B != null ){
        int size = B.length;
        EducationsB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          EducationsB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      // Get the StorySpace
      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( EducationsA != null ){
          SelectinStory( StoryA, OldStoryA, EducationsA, this );
        }
        if( EducationsB != null ){
          SelectinStory( StoryB, OldStoryB, EducationsB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, EducationsB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    public String FieldString( Person a ){
      return a.HasEducation;
    }

  }

  private class SelectEmployment implements PersonSelect, Rule{

    String[] EmploymentsA;
    String[] EmploymentsB;
    boolean MAINCHAR = false;

    public SelectEmployment( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          EmploymentsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            EmploymentsA[i] = new String( A[i] );
          }
        }
      }
      if( B != null ){
        int size = B.length;
        EmploymentsB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          EmploymentsB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( EmploymentsA != null ){
          SelectinStory( StoryA, OldStoryA, EmploymentsA, this );
        }
        if( EmploymentsB != null ){
          SelectinStory( StoryB, OldStoryB, EmploymentsB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, EmploymentsB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    public String FieldString( Person a ){
      return a.HasJob;
    }

  }

  private class SelectGeoLocation implements PersonSelect, Rule{

    String[] GeoLocationsA;
    String[] GeoLocationsB;
    boolean MAINCHAR = false;

    public SelectGeoLocation( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          GeoLocationsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            GeoLocationsA[i] = new String( A[i] );
          }
        }
      }
      if( B != null ){
        int size = B.length;
        GeoLocationsB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          GeoLocationsB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( GeoLocationsA != null ){
          SelectinStory( StoryA, OldStoryA, GeoLocationsA, this );
        }
        if( GeoLocationsB != null ){
          SelectinStory( StoryB, OldStoryB, GeoLocationsB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, GeoLocationsB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    public String FieldString( Person a ){
      return a.HasOrigin;
    }

  }

  private class SelectReligion implements PersonSelect, Rule{

    String[] ReligionsA;
    String[] ReligionsB;
    boolean MAINCHAR = false;

    public SelectReligion( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          ReligionsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            ReligionsA[i] = new String( A[i] );
          }
        }
      }
      if( B != null ){
        int size = B.length;
        ReligionsB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          ReligionsB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( ReligionsA != null ){
          SelectinStory( StoryA, OldStoryA, ReligionsA, this );
        }
        if( ReligionsB != null ){
          SelectinStory( StoryB, OldStoryB, ReligionsB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, ReligionsB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    public String FieldString( Person a ){
      return a.HasOrigin;
    }
  }

  private class SelectGender implements PersonSelect, Rule{

    String[] GendersA;
    String[] GendersB;
    boolean MAINCHAR = false;

    public SelectGender( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        GendersA = new String[size];
        for( int i = 0; i < A.length; i++ ){
          GendersA[i] = new String( A[i] );
        }
      }
      if( B != null ){
        int size = B.length;
        GendersB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          GendersB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( GendersA != null ){
          SelectinStory( StoryA, OldStoryA, GendersA, this );
        }
        if( GendersB != null ){
          SelectinStory( StoryB, OldStoryB, GendersB, this );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectinStory( StoryA, OldStoryA, GendersB, this );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );

    }

    public String FieldString( Person a ){
      return a.HasGender;
    }
  }

  private class SelectConcepts implements Rule{

    String[] ConceptsA;
    String[] ConceptsB;
    boolean MAINCHAR = false;

    public SelectConcepts( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          ConceptsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            ConceptsA[i] = new String( A[i] );
          }
        }
      }

      if( B != null ){
        int size = B.length;
        ConceptsB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          ConceptsB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      // Get the StorySpace
      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( ConceptsA != null ){
          SelectConceptinStory( StoryA, OldStoryA, ConceptsA );
        }
        if( ConceptsB != null ){
          SelectConceptinStory( StoryB, OldStoryB, ConceptsB );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectConceptinStory( StoryA, OldStoryA, ConceptsB );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }

      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    private void StoryLoop( ArrayList theLoopArray, Hashtable theKeysHash, MediaItem item ){

      //Find statement espressing it
      if( theLoopArray != null ){
        outer_loop:
            for( int i = 0; i < theLoopArray.size(); i++ ){
          Segment theSeg = ( Segment )theLoopArray.get( i );
          if( theSeg.StatementIds == null ){
          } else{
            for( Enumeration ee = theSeg.StatementIds.elements(); ee.hasMoreElements(); ){
              // Get the statement ID
              String StatementId = ( String )ee.nextElement();
              VerbalStatement Statement = ( VerbalStatement )theDataContainer.Statements.get( StatementId );
              if( theKeysHash.containsKey( Statement.Subject ) ){
                item.add( theSeg );
                continue outer_loop;
              }
            }
          }
        }
      }
    }

    private boolean SelectConceptinStory( MyHashTable Story, MyHashTable OldStory,
                                          String[] Concept ) throws Exception{

      Hashtable Concepts = new Hashtable();

      if( Concept != null ){
        for( int i = 0; i < Concept.length; i++ ){
          Concepts.put( Concept[i], Concept[i] );
        }

        Story.clear();
        for( Enumeration e = OldStory.elements(); e.hasMoreElements(); ){
          MediaItem theItem = ( MediaItem )e.nextElement();
          MediaItem Add = new MediaItem();

          StoryLoop( theItem.VideoSegments, Concepts, Add );
          StoryLoop( theItem.AudioSegments, Concepts, Add );
          StoryLoop( theItem.StillSegments, Concepts, Add );
          StoryLoop( theItem.TextSegments, Concepts, Add );

          if( !Add.isEmpty() ){
            Story.put( Add );
          }
        }
      }
      return true;
    }

    /*
        // Expunge if ALL elements of the Plot do not fulfill the requirement
        private boolean SelectConceptinPlot( StorySpace a, Hashtable Expunged,
                                             String[] ConceptA, String[] ConceptB ) throws Exception{

          Hashtable ConceptsA = new Hashtable();
          Hashtable ConceptsB = new Hashtable();

          if( ConceptA != null || ConceptB != null ){
            if( ConceptA != null ){
              for( int i = 0; i < ConceptA.length; i++ ){
                ConceptsA.put( ConceptA[i], ConceptA[i] );
              }
            }
            if( ConceptB != null ){
              for( int i = 0; i < ConceptB.length; i++ ){
                ConceptsB.put( ConceptB[i], ConceptB[i] );
              }
            }

            ArrayList[] TempPlot = {
                new ArrayList( a.GetPlot( 0 ) ), new ArrayList( a.GetPlot( 1 ) )};

            Hashtable[] Concepts = {
                ConceptsA, ConceptsB};

            for( int index = 0; index < TempPlot.length; index++ ){
              external_loop:

                  for( int i = 0; i < TempPlot[index].size(); i++ ){

                MediaItem item = ( MediaItem )TempPlot[index].get( i );
                if( item.VideoSegments != null ){
                  for( int ii = 0; ii < item.VideoSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.VideoSegments.get( ii );
                    if( theSeg.StatementIds == null ){
                      continue;
                    }
                    for( Enumeration ee = theSeg.StatementIds.elements(); ee.hasMoreElements(); ){
                      // Get the statement ID
                      String StatementId = ( String )ee.nextElement();
                      VerbalStatement Statement = ( VerbalStatement )theDataContainer.Statements.get( StatementId );
                      if( ConceptsA.containsKey( Statement.Subject ) ){
                        continue external_loop;
                      }
                    }
                  }
                }

                if( item.AudioSegments != null ){
                  for( int ii = 0; ii < item.AudioSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.AudioSegments.get( ii );
                    if( theSeg.StatementIds == null ){
                      continue;
                    }
                    for( Enumeration ee = theSeg.StatementIds.elements(); ee.hasMoreElements(); ){
                      // Get the statement ID
                      String StatementId = ( String )ee.nextElement();
                      VerbalStatement Statement = ( VerbalStatement )theDataContainer.Statements.get( StatementId );
                      if( Concepts[index].containsKey( Statement.Subject ) ){
                        continue external_loop;
                      }
                    }
                  }
                }

                if( item.StillSegments != null ){
                  for( int ii = 0; ii < item.StillSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.StillSegments.get( ii );
                    if( theSeg.StatementIds == null ){
                      continue;
                    }
                    for( Enumeration ee = theSeg.StatementIds.elements(); ee.hasMoreElements(); ){
                      // Get the statement ID
                      String StatementId = ( String )ee.nextElement();
                      VerbalStatement Statement = ( VerbalStatement )theDataContainer.Statements.get( StatementId );
                      if( Concepts[index].containsKey( Statement.Subject ) ){
                        continue external_loop;
                      }
                    }
                  }
                }

                if( item.TextSegments != null ){
                  for( int ii = 0; ii < item.TextSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.TextSegments.get( ii );
                    if( theSeg.StatementIds == null ){
                      continue;
                    }
                    for( Enumeration ee = theSeg.StatementIds.elements(); ee.hasMoreElements(); ){
                      // Get the statement ID
                      String StatementId = ( String )ee.nextElement();
                      VerbalStatement Statement = ( VerbalStatement )theDataContainer.Statements.get( StatementId );
                      if( Concepts[index].containsKey( Statement.Subject ) ){
                        continue external_loop;
                      }
                    }
                  }
                }
                // if we get here it does not contain the concept
                a.ExpungeFromPlot( item );
              }
            }
          }
          return true;
        }
     */
  }

  private class SelectQuestions implements Rule{

    String[] QuestionsA;
    String[] QuestionsB;
    boolean MAINCHAR = false;

    public SelectQuestions( String[] A, String[] B ) throws Exception{

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
          if( B == null ){
            throw new Exception( "No question to select for MAINCHAR " );
          }
          size = B.length;
          QuestionsB = new String[size];
          for( int i = 0; i < B.length; i++ ){
            QuestionsB[i] = new String( B[i] );
          }
        } else{
          QuestionsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            QuestionsA[i] = new String( A[i] );
          }
        }
      }
    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable OldStoryA = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        OldStoryA = new MyHashTable( StoryA );

        if( QuestionsA != null ){
          SelectQuestioninStory( StoryA, OldStoryA, QuestionsA );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        OldStoryA = new MyHashTable( StoryA );
        SelectQuestioninStory( StoryA, OldStoryA, QuestionsB );
      }

      if( StoryA.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    private void StoryLoop( String ID, ArrayList theLoopArray, Hashtable theKeysHash,
                            MediaItem item )throws Exception{

      //Find statement espressing it
      if( theLoopArray != null ){
        outer_loop:
            for( int j = 0; j < theLoopArray.size(); j++ ){
          Segment theSeg = ( Segment )theLoopArray.get( j );
          if( theSeg.InterviewId == null || theSeg.IsInterview == false ){
            continue;
          }

          Interview theInterview = null;

          if( ID == null ){
            continue;
          }

          for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){

            if( theDataContainer.InterviewsArray[i].Id.equals( ID ) ){
              theInterview = ( Interview )theDataContainer.InterviewsArray[i];
              break;
            }
          }
          if( theInterview == null ){
            continue;
          }

          if( theKeysHash.containsKey( theInterview.thequestion.QuestionId ) ){
            item.add( theSeg );
            continue outer_loop;
          }
          // if we get here it does not contain the concept
        }
      }
    }

    private boolean SelectQuestioninStory( MyHashTable Story, MyHashTable OldStory,
                                           String[] Question ) throws Exception{

      Hashtable Questions = new Hashtable();

      if( Question != null ){
        for( int i = 0; i < Question.length; i++ ){
          Questions.put( Question[i], Question[i] );
        }

        Story.clear();
        for( Enumeration e = OldStory.elements(); e.hasMoreElements(); ){
          MediaItem theItem = ( MediaItem )e.nextElement();

          MediaItem Add = new MediaItem();

          StoryLoop( theItem.getInterviewId(), theItem.VideoSegments, Questions, Add );
          StoryLoop( theItem.getInterviewId(), theItem.AudioSegments, Questions, Add );
          StoryLoop( theItem.getInterviewId(), theItem.StillSegments, Questions, Add );
          StoryLoop( theItem.getInterviewId(), theItem.TextSegments, Questions, Add );

          if( !Add.isEmpty() ){
            Story.put( Add );
          }
        }

      }
      return true;
    }

    /*
        // Expunge if ALL elements of the Plot do not fulfill the requirement
        private boolean SelectQuestioninPlot( StorySpace a, Hashtable Expunged,
                                              String[] QuestionA, String[] QuestionB ) throws Exception{

          Hashtable QuestionsA = new Hashtable();
          Hashtable QuestionsB = new Hashtable();

          if( QuestionA != null || QuestionB != null ){
            if( QuestionA != null ){
              for( int i = 0; i < QuestionA.length; i++ ){
                QuestionsA.put( QuestionA[i], QuestionA[i] );
              }
            }
            if( QuestionB != null ){
              for( int i = 0; i < QuestionB.length; i++ ){
                QuestionsB.put( QuestionB[i], QuestionB[i] );
              }
            }

            ArrayList[] TempPlot = {
                new ArrayList( a.GetPlot( 0 ) ), new ArrayList( a.GetPlot( 1 ) )};

            Hashtable[] Questions = {
                QuestionsA, QuestionsB};

            for( int index = 0; index < TempPlot.length; index++ ){
              external_loop:

                  for( int i = 0; i < TempPlot[index].size(); i++ ){

                MediaItem item = ( MediaItem )TempPlot[index].get( i );

                ArrayList[] Loop = {item.VideoSegments, item.AudioSegments, item.StillSegments,
                    item.TextSegments};

                for( int loop=0; loop<Loop.length; loop++ ){
                  if( Loop[loop] != null ){

                    for( int ii = 0; ii < Loop[loop].size(); ii++ ){
                      Segment theSeg = ( Segment )Loop[loop].get( ii );
                      if( theSeg.InterviewId == null ){
                        continue;
                      }
                      Interview theInterview = null;

                      for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                        if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                          theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                          break;
                        }
                      }

                      if( theInterview == null ){
                        continue;
                      }

                      if( Questions[index].containsKey( theInterview.thequestion.QuestionId ) ){
                        continue external_loop;
                      }
                    }
                  }
                }

                // if we get here it does not contain the concept
                a.ExpungeFromPlot( item );
              }
            }
          }
          return true;
        }
     */
  }

  private class SelectPartecipants implements Rule{

    String[] PartecipantsA;
    String[] PartecipantsB;
    boolean MAINCHAR = false;

    public SelectPartecipants( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          PartecipantsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            PartecipantsA[i] = new String( A[i] );
          }
        }
      }

      if( B != null ){
        int size = B.length;
        PartecipantsB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          PartecipantsB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( PartecipantsA != null ){
          SelectPartecipantinStory( StoryA, OldStoryA, PartecipantsA );
        }
        if( PartecipantsB != null ){
          SelectPartecipantinStory( StoryB, OldStoryB, PartecipantsB );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        OldStoryA = new MyHashTable( StoryA );
        StoryB = new MyHashTable();
        SelectPartecipantinStory( StoryA, OldStoryA, PartecipantsB );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    private void StoryLoop( String ID, ArrayList theLoopArray, Hashtable theKeysHash,
                            MediaItem item )throws Exception{

      //Find statement espressing it
      if( theLoopArray != null ){
        outer_loop:
            for( int j = 0; j < theLoopArray.size(); j++ ){
          Segment theSeg = ( Segment )theLoopArray.get( j );
          if( theSeg.InterviewId == null ){
            continue;
          }

          Interview theInterview = null;

          if( ID == null ){
            continue;
          }

          for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
            if( theDataContainer.InterviewsArray[i].Id.equals( ID ) ){
              theInterview = ( Interview )theDataContainer.InterviewsArray[i];
              break;
            }
          }

          if( theInterview == null ){
            continue;
          }

          if( theKeysHash.containsKey( theInterview.theInterviewee ) ){

            item.add( theSeg );
            continue outer_loop;
          }
          // if we get here it does not contain the concept
        }
      }
    }

    private boolean SelectPartecipantinStory( MyHashTable Story, MyHashTable OldStory,
                                              String[] Partecipant ) throws Exception{

      Hashtable Partecipants = new Hashtable();

      if( Partecipant != null ){
        for( int i = 0; i < Partecipant.length; i++ ){
          Partecipants.put( Partecipant[i], Partecipant[i] );
        }

        Story.clear();
        for( Enumeration e = OldStory.elements(); e.hasMoreElements(); ){
          MediaItem theItem = ( MediaItem )e.nextElement();
          MediaItem Add = new MediaItem();

          StoryLoop( theItem.getInterviewId(), theItem.VideoSegments, Partecipants, Add );
          StoryLoop( theItem.getInterviewId(),theItem.AudioSegments, Partecipants, Add );
          StoryLoop( theItem.getInterviewId(),theItem.StillSegments, Partecipants, Add );
          StoryLoop( theItem.getInterviewId(),theItem.TextSegments, Partecipants, Add );

          if( !Add.isEmpty() ){
            Story.put( Add );
          }
        }

      }
      return true;
    }

    /*
        // Expunge if ALL elements of the Plot do not fulfill the requirement
        private boolean SelectPartecipantinPlot( StorySpace a, Hashtable Expunged,
                                                 String[] PartecipantA, String[] PartecipantB ) throws Exception{

          Hashtable PartecipantsA = new Hashtable();
          Hashtable PartecipantsB = new Hashtable();

          if( PartecipantA != null || PartecipantB != null ){
            if( PartecipantA != null ){
              for( int i = 0; i < PartecipantA.length; i++ ){
                PartecipantsA.put( PartecipantA[i], PartecipantA[i] );
              }
            }
            if( PartecipantB != null ){
              for( int i = 0; i < PartecipantB.length; i++ ){
                PartecipantsB.put( PartecipantB[i], PartecipantB[i] );
              }
            }

            ArrayList[] TempPlot = {
                new ArrayList( a.GetPlot( 0 ) ), new ArrayList( a.GetPlot( 1 ) )};

            Hashtable[] Partecipants = {
                PartecipantsA, PartecipantsB};

            for( int index = 0; index < TempPlot.length; index++ ){
              external_loop:

                  for( int i = 0; i < TempPlot[index].size(); i++ ){

                MediaItem item = ( MediaItem )TempPlot[index].get( i );
                if( item.VideoSegments != null ){
                  for( int ii = 0; ii < item.VideoSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.VideoSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( Partecipants[index].containsKey( theInterview.theInterviewee ) ){
                      continue external_loop;
                    }

                  }
                }

                if( item.AudioSegments != null ){
                  for( int ii = 0; ii < item.AudioSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.AudioSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( Partecipants[index].containsKey( theInterview.theInterviewee ) ){
                      continue external_loop;
                    }

                  }
                }

                if( item.StillSegments != null ){
                  for( int ii = 0; ii < item.StillSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.StillSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( Partecipants[index].containsKey( theInterview.theInterviewee ) ){
                      continue external_loop;
                    }

                  }
                }

                if( item.TextSegments != null ){
                  for( int ii = 0; ii < item.TextSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.TextSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( Partecipants[index].containsKey( theInterview.theInterviewee ) ){
                      continue external_loop;
                    }

                  }
                }
                // if we get here it does not contain the concept
                a.ExpungeFromPlot( item );
              }
            }
          }
          return true;
        }
     */
  }

  private class SelectInterview implements Rule{

    String[] InterviewA;
    String[] InterviewB;
    boolean MAINCHAR = false;

    public SelectInterview( String[] A, String[] B ){

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
        } else{
          InterviewA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            InterviewA[i] = new String( A[i] );
          }
        }
      }

      if( B != null ){
        int size = B.length;
        InterviewB = new String[size];
        for( int i = 0; i < B.length; i++ ){
          InterviewB[i] = new String( B[i] );
        }
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable StoryB = null;
      MyHashTable OldStoryA = null;
      MyHashTable OldStoryB = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        OldStoryA = new MyHashTable( StoryA );
        OldStoryB = new MyHashTable( StoryB );

        if( InterviewA != null ){
          SelectInterviewsinStory( StoryA, OldStoryA, InterviewA );
        }
        if( InterviewB != null ){
          SelectInterviewsinStory( StoryB, OldStoryB, InterviewB );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        StoryB = new MyHashTable();
        OldStoryA = new MyHashTable( StoryA );
        SelectInterviewsinStory( StoryA, OldStoryA, InterviewB );
      }

      if( StoryA.size() + StoryB.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    private boolean SelectInterviewsinStory( MyHashTable Story, MyHashTable OldStory,
                                             String[] Interview ) throws Exception{

      Hashtable Interviews = new Hashtable();

      if( Interview != null ){
        for( int i = 0; i < Interview.length; i++ ){
          Interviews.put( Interview[i], Interview[i] );
        }

        Story.clear();
        for( Enumeration e = OldStory.elements(); e.hasMoreElements(); ){
          MediaItem theItem = ( MediaItem )e.nextElement();

          MediaItem Add = new MediaItem();

          StoryLoop( theItem.VideoSegments, Interviews, Add );
          StoryLoop( theItem.AudioSegments, Interviews, Add );
          StoryLoop( theItem.StillSegments, Interviews, Add );
          StoryLoop( theItem.TextSegments, Interviews, Add );

          if( !Add.isEmpty() ){
            Story.put( Add );
          }
        }

      }
      return true;
    }

    private void StoryLoop( ArrayList theLoopArray, Hashtable theKeysHash,
                            MediaItem item )throws Exception{

      //Find segments belonging to the interview
      if( theLoopArray != null ){
        outer_loop:
            for( int j = 0; j < theLoopArray.size(); j++ ){
          Segment theSeg = ( Segment )theLoopArray.get( j );
          if( theSeg.InterviewId == null || theSeg.IsInterview == false ){
            continue;
          }

          String ID = item.getInterviewId();

          if( ID == null ){
            continue;
          }

          if( theKeysHash.containsKey( ID ) ){
            item.add( theSeg );
            continue outer_loop;
          }
          // if we get here it does not contain the concept
        }
      }
    }

  }

  // Rule to assemble from 2 storylines parties 2 Plots
  private class CreatePlot implements Rule{

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = a.GetCharacter( 1 );
      MyHashTable StoryB = a.GetCharacter( 2 );

      MediaItem item = null;

      MyHashTable[] Loop = {
          StoryA, StoryB};
      for( int i = 0; i < Loop.length; i++ ){

        for( Enumeration e = Loop[i].elements(); e.hasMoreElements(); ){
          MediaItem theItem = ( MediaItem )e.nextElement();
          item = new MediaItem( theItem );

          if( item != null ){
            if( i % 2 == 0 ){
              a.AddUniquetoPlot( item, 1 );
            } else{
              a.AddUniquetoPlot( item, 2 );
            }
          }
        }
      }

      if( a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      return this.ApplyRule( a, NodeLevel );
    }
  }

  private class EliminateCharacter implements Rule{

    String Character;

    public EliminateCharacter( String[] A ) throws Exception{

      if( A != null ){
        if( A.length != 1 ){
          throw new Exception( "Too many character to eliminate " );
        }
        Character = new String( A[0] );
      }
    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = a.GetCharacter( 1 );
      MyHashTable StoryB = a.GetCharacter( 2 );

      MyHashTable Story = null;

      if( Character.equals( "A" ) ){

        Story = StoryA;

      } else if( Character.equals( "B" ) ){

        Story = StoryB;

      } else{
        throw new Exception( "Unknown character to eliminate " );
      }

      Story.clear();

      if( StoryA.size() + StoryB.size() + a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }

      return this.ApplyRule( a, NodeLevel );
    }

  }

  private class SelectOpinions implements Rule{

    String[] OpinionsA;
    String[] OpinionsB;
    boolean MAINCHAR = false;

    public SelectOpinions( String[] A, String[] B ) throws Exception{

      if( A != null ){
        int size = A.length;
        if( size == 1 && A[0].equals( "MAINCHAR" ) ){
          MAINCHAR = true;
          if( B == null ){
            throw new Exception( "No opinion to select for MAINCHAR " );
          }
          size = B.length;
          OpinionsB = new String[size];
          for( int i = 0; i < B.length; i++ ){
            OpinionsB[i] = new String( B[i] );
          }
        } else{
          OpinionsA = new String[size];
          for( int i = 0; i < A.length; i++ ){
            OpinionsA[i] = new String( A[i] );
          }
        }
      }
    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      MyHashTable StoryA = null;
      MyHashTable OldStoryA = null;

      if( MAINCHAR == false ){
        StoryA = a.GetCharacter( 1 );
        OldStoryA = new MyHashTable( StoryA );

        if( OpinionsA != null ){
          SelectOpinioninStory( StoryA, OldStoryA, OpinionsA );
        }
      } else{
        StoryA = a.GetCharacter( 0 );
        OldStoryA = new MyHashTable( StoryA );
        SelectOpinioninStory( StoryA, OldStoryA, OpinionsB );
      }

      if( StoryA.size() > 0 ){
        return 100;
      }
      return 0;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }
      return this.ApplyRule( a, NodeLevel );
    }

    private void StoryLoop( String ID, ArrayList theLoopArray, Hashtable theKeysHash, MediaItem item )throws Exception{

      //Find statement espressing it
      if( theLoopArray != null ){
        outer_loop:
            for( int j = 0; j < theLoopArray.size(); j++ ){
          Segment theSeg = ( Segment )theLoopArray.get( j );
          if( theSeg.InterviewId == null || theSeg.IsInterview == false ){
            continue;
          }

          Interview theInterview = null;

          if( ID == null ){
            continue;
          }

          for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
            if( theDataContainer.InterviewsArray[i].Id.equals( ID ) ){
              theInterview = ( Interview )theDataContainer.InterviewsArray[i];
              break;
            }
          }
          if( theInterview == null ){
            continue;
          }

          if( theKeysHash.containsKey( theInterview.OpinionId ) ){
            item.add( theSeg );
            continue outer_loop;
          }
          // if we get here it does not contain the concept
        }
      }

    }

    private boolean SelectOpinioninStory( MyHashTable Story, MyHashTable OldStory,
                                          String[] Opinion ) throws Exception{

      Hashtable Opinions = new Hashtable();

      if( Opinion != null ){
        for( int i = 0; i < Opinion.length; i++ ){
          Opinions.put( Opinion[i], Opinion[i] );
        }

        Story.clear();

        for( Enumeration e = OldStory.elements(); e.hasMoreElements(); ){
          MediaItem theItem = ( MediaItem )e.nextElement();
          MediaItem Add = new MediaItem();

          StoryLoop( theItem.getInterviewId(), theItem.VideoSegments, Opinions, Add );
          StoryLoop( theItem.getInterviewId(), theItem.AudioSegments, Opinions, Add );
          StoryLoop( theItem.getInterviewId(), theItem.StillSegments, Opinions, Add );
          StoryLoop( theItem.getInterviewId(), theItem.TextSegments, Opinions, Add );

          if( !Add.isEmpty() ){
            Story.put( Add );
          }
        }

      }
      return true;
    }

    /*
        // Expunge if ALL elements of the Plot do not fulfill the requirement
        private boolean SelectOpinioninPlot( StorySpace a, Hashtable Expunged,
                                             String[] OpinionA, String[] OpinionB ) throws Exception{

          Hashtable OpinionsA = new Hashtable();
          Hashtable OpinionsB = new Hashtable();

          if( OpinionA != null || OpinionB != null ){
            if( OpinionA != null ){
              for( int i = 0; i < OpinionA.length; i++ ){
                OpinionsA.put( OpinionA[i], OpinionA[i] );
              }
            }
            if( OpinionB != null ){
              for( int i = 0; i < OpinionB.length; i++ ){
                OpinionsB.put( OpinionB[i], OpinionB[i] );
              }
            }

            ArrayList[] TempPlot = {
                new ArrayList( a.GetPlot( 0 ) ), new ArrayList( a.GetPlot( 1 ) )};

            Hashtable[] Opinions = {
                OpinionsA, OpinionsB};

            for( int index = 0; index < TempPlot.length; index++ ){

              external_loop:

                  for( int i = 0; i < TempPlot[index].size(); i++ ){

                MediaItem item = ( MediaItem )TempPlot[index].get( i );
                if( item.VideoSegments != null ){
                  for( int ii = 0; ii < item.VideoSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.VideoSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( ( theInterview.OpinionId != null ) &&
                        Opinions[index].containsKey( theInterview.OpinionId ) ){
                      continue external_loop;
                    }
                  }
                }

                if( item.AudioSegments != null ){
                  for( int ii = 0; ii < item.AudioSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.AudioSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( ( theInterview.OpinionId != null ) &&
                        Opinions[index].containsKey( theInterview.OpinionId ) ){
                      continue external_loop;
                    }
                  }
                }

                if( item.StillSegments != null ){
                  for( int ii = 0; ii < item.StillSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.StillSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( ( theInterview.OpinionId != null ) &&
                        Opinions[index].containsKey( theInterview.OpinionId ) ){
                      continue external_loop;
                    }

                  }
                }

                if( item.TextSegments != null ){
                  for( int ii = 0; ii < item.TextSegments.size(); ii++ ){
                    Segment theSeg = ( Segment )item.TextSegments.get( ii );
                    if( theSeg.InterviewId == null ){
                      continue;
                    }
                    Interview theInterview = null;

                    for( int j = 0; i < theDataContainer.InterviewsArray.length; j++ ){
                      if( theDataContainer.InterviewsArray[j].Id.equals( theSeg.InterviewId ) ){
                        theInterview = ( Interview )theDataContainer.InterviewsArray[j];
                        break;
                      }
                    }

                    if( theInterview == null ){
                      continue;
                    }

                    if( ( theInterview.OpinionId != null ) &&
                        Opinions[index].containsKey( theInterview.OpinionId ) ){
                      continue external_loop;
                    }
                  }
                }
                // if we get here it does not contain the concept
                a.ExpungeFromPlot( item );
              }
            }
          }
          return true;
        }
     */
  }

  private class CreateArg implements Rule{

    private String Strategy;
    private boolean Intercut;
    private boolean alternate;

    public CreateArg( String[] A ) throws Exception{

      if( A.length != 3 ){
        throw new Exception( "Not enough parameters: " + A.length );
      }
      Strategy = new String( A[0] );

      if( Strategy.equals( "attack" ) ||
          Strategy.equals( "support" ) ||
          Strategy.equals( "VP" ) ){

      } else{
        throw new Exception( "Unknown strategy " + Strategy );
      }

      if( A[1].equals( "true" ) ){
        Intercut = true;
      } else{
        Intercut = false;
      }

      if( A[2].equals( "true" ) && Intercut == true ){
        alternate = true;
      } else{
        alternate = false;
      }

    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      int dest1, dest2;
      // Get the StorySpace
      MyHashTable StoryA, StoryB;

      if( Strategy.equals( "VP" ) ){

        if( a.SizePlot( 0 ) != 0 ){
          throw new Exception( "Plot not empty in VP interview " );
        }

        dest1 = 0;
        StoryA = a.GetCharacter( 0 );
        dest2 = 1;
        StoryB = a.GetCharacter( 1 );
        CreateArgumentation( "support", StoryA, StoryB, a, dest1, dest2 );
        dest2 = 2;
        StoryB = a.GetCharacter( 2 );
        CreateArgumentation( "attack", StoryA, StoryB, a, dest1, dest2 );

      } else{

        StoryA = a.GetCharacter( 1 );
        StoryB = a.GetCharacter( 2 );
        if( StoryB.size() == 0 ){
          StoryB = StoryA;
        }

        CreateArgumentation( Strategy, StoryA, StoryB, a, 1, 2 );
      }

      int result = ( StoryA.size() + StoryB.size() + a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ? 100 :
                     0 );
      return result;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 0 ) + a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }

      // Get the StorySpace
      MyHashTable Story = a.GetCharacter( 1 );

      CreateArgumentation( Strategy, Story, Story, a, 1, 2 );

      int result = ( a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ? 100 : 0 );
      return result;
    }

    private void CreateArgumentation( String Role, MyHashTable StoryA,
                                      MyHashTable StoryB, StorySpace a, int dest1, int dest2 ) throws Exception{

      Hashtable HandledInterviews = new Hashtable();

      outer_loop:
          for( Enumeration e = StoryA.elements(); e.hasMoreElements(); ){
        MediaItem theItem = ( MediaItem )e.nextElement();

        //Find statement or interview containing it
        if( theItem.getStatementIds().size() == 0 && theItem.getInterviewId() == null ){
          continue;
        }
        if( theItem.getInterviewId() != null ){
          // This is an interview
          Interview theInterview = null;
          MediaItem InterviewB = null;
          String Id = new String( theItem.getInterviewId() );

          for( int j = 0; j < theDataContainer.InterviewsArray.length; j++ ){
            if( theDataContainer.InterviewsArray[j].Id.equals( Id ) ){
              theInterview = theDataContainer.InterviewsArray[j];
              break;
            }
          }
          if( theInterview == null ){
            throw new Exception( " Interview not found " );
          } else if( HandledInterviews.containsKey( theInterview.Id ) ){
            continue outer_loop;
          }
          HandledInterviews.put( theInterview.Id, theInterview );

          //If it has no Arguments we can not create an argumentation
          if( theInterview.Arguments != null ){

            if( Intercut == false ){
              InterviewB = new MediaItem();
            }

            argument:
                for( int k = 0; k < theInterview.Arguments.length; k++ ){
              Argument anArgument = theInterview.Arguments[k];

              if( anArgument.Nodes == null ){
                throw new Exception( " Argument with no Nodes " );
              }
              for( int z = 0; z < anArgument.Nodes.length; z++ ){
                ToulminNode aNode = anArgument.Nodes[z];
                if( aNode.theStatementIDs == null ){
                  throw new Exception( " Node with no statements " );
                }
                stat:
                    for( int zz = 0; zz < aNode.theStatementIDs.length; zz++ ){
                  VerbalStatement aStatement = ( VerbalStatement )theDataContainer.Statements.
                      get( aNode.theStatementIDs[zz] );
                  MediaItem[][] Statementitem;
                  Statementitem = CreateArgSeg( aStatement, Role, aNode.ToulminType, StoryB, true );

                  // insert only once in Main Character Plot
                  if( Statementitem[0] != null && !( dest1 == 0 && dest2 == 2 ) ){
                    for( int plind = 0; plind < Statementitem[0].length; plind++ ){
                      a.AddtoPlot( Statementitem[0][plind], dest1 );
                    }
                  }
                  if( Statementitem[1] != null ){
                    for( int plind = 0; plind < Statementitem[1].length; plind++ ){
                      if( Intercut == true ){
                        a.AddtoPlot( Statementitem[1][plind], dest2 );
                      } else{
                        InterviewB.add( Statementitem[1][plind] );
                      }
                    }
                  }
                }
              }
            } // We handle all arguments
            if( Intercut == false ){
              // add the interview only if we found segments to argument it
              if( a.AddtoPlot( InterviewB, dest2 ) == true && !( dest1 == 0 && dest2 == 2 ) ){
                // insert only once in Main Character Plot
                a.AddtoPlot( theItem, dest1 );
              }
            }
          } // Does it have arguments?
        } else{ // Not an interview
          Hashtable StatementIds = theItem.getStatementIds();
          for( Enumeration ee = StatementIds.elements(); ee.hasMoreElements(); ){
            // Get the statement ID
            String StatementId = ( String )ee.nextElement();
            VerbalStatement Statement = ( VerbalStatement )theDataContainer.Statements.get( StatementId );
            MediaItem[][] Statementitem;
            Statementitem = CreateArgSeg( Statement, Role, DataBuilder.ToulminStrings[0], StoryB, false );
            // insert only once in Main Character Plot
            if( Statementitem[0] != null && !( dest1 == 0 && dest2 == 2 ) ){
              for( int plind = 0; plind < Statementitem[0].length; plind++ ){
                a.AddtoPlot( Statementitem[0][plind], dest1 );
              }
            }
            if( Statementitem[1] != null ){
              for( int plind = 0; plind < Statementitem[1].length; plind++ ){
                a.AddtoPlot( Statementitem[1][plind], dest2 );
              }
            }
          }
        }
      }

      if( !Strategy.equals( "VP" ) ){
        StoryA.clear();
      }

      StoryB.clear();
      return;
    }

    private MediaItem[][] CreateArgSeg( VerbalStatement aStatement, String Role, NameIndex ToulminType,
                                        MyHashTable Story, boolean IsInterview ) throws Exception{

      MediaItem[][] Statementitem = new MediaItem[2][];

      MyHashTable MyStory = new MyHashTable( Story );

      String[][] FoundMedia = null;
      int[] distance = null;
      int index = -1;

      int max = 0; // max number of media in b statements

      if( aStatement.ConnectedStatements != null ){

        int size = aStatement.ConnectedStatements.size();
        FoundMedia = new String[size][];
        distance = new int[size];
        conn_stat:
            // Loop on the connected statement per role
            for( Enumeration ez = aStatement.ConnectedStatements.elements(); ez.hasMoreElements(); ){
          Link aLink = ( Link )ez.nextElement();
          index++;
          FoundMedia[index] = null;

          if( ( distance[index] = CheckRole( Role, ToulminType, aLink ) ) > 50 ){
            continue;
          } else{
            // This statement is the one linked to the one contained in the Argumentation
            VerbalStatement bStatement = ( VerbalStatement )theDataContainer.Statements.get(
                aLink.StatementId );
            if( aStatement.ClaimerId.equals( bStatement.ClaimerId ) ){
              continue;
            }
            if( aStatement.theMediaItem == null || bStatement.theMediaItem == null ){
              throw new Exception( "No media item for Statement " );
            }

            int len = bStatement.theMediaItem.length;
            if( len > max ){
              max = len;
            }
            FoundMedia[index] = new String[bStatement.theMediaItem.length];

            for( int ii = 0; ii < bStatement.theMediaItem.length; ii++ ){
              if( !MyStory.Contains( bStatement.theMediaItem[ii] ) ){
                FoundMedia[index] = null;
                continue conn_stat;
              }
              FoundMedia[index][ii] = new String( bStatement.theMediaItem[ii] );

            }
          }
        }

        int maxlength = u.min( aStatement.theMediaItem.length, max );

        // Order the segments
        boolean first = true;

        int[] min = u.Order( distance, true );
        for( int i = 0; i < min.length; i++ ){
          if( ( distance[min[i]] != -1 ) && ( FoundMedia[min[i]] != null ) ){

            if( ( first == true ) && ( ( Intercut == true ) || ( IsInterview == false ) ) ){
              if( alternate == true ){
                Statementitem[0] = new MediaItem[maxlength];
                for( int ii = 0; ii < maxlength; ii++ ){
                  Statementitem[0][ii] = new MediaItem();
                  Statementitem[0][ii].add( theDataContainer.getMedia( aStatement.theMediaItem[ii] ) );
                }
                if( aStatement.theMediaItem.length > maxlength ){
                  for( int ii = maxlength; ii < aStatement.theMediaItem.length; ii++ ){
                    Statementitem[0][maxlength - 1].add( theDataContainer.getMedia( aStatement.theMediaItem[ii] ) );
                  }
                }
              } else{
                Statementitem[0] = new MediaItem[1];
                Statementitem[0][0] = new MediaItem();
                for( int ii = 0; ii < aStatement.theMediaItem.length; ii++ ){
                  Statementitem[0][0].add( theDataContainer.getMedia( aStatement.theMediaItem[ii] ) );
                }
              }
              first = false;
            }

            if( alternate == true ){

              if( Statementitem[1] == null ){
                Statementitem[1] = new MediaItem[maxlength];
              }
              int length = u.min( maxlength, FoundMedia[min[i]].length );

              for( int ii = 0; ii < length; ii++ ){
                if( Statementitem[1][ii] == null ){
                  Statementitem[1][ii] = new MediaItem();
                }
                // this in case a segment plays different roles and proximity would be
                // overwritten
                Segment aSeg = theDataContainer.getMedia( FoundMedia[min[i]][ii] );
                Segment theSeg = aSeg.New();

                theSeg.proximity = distance[min[i]];
                Statementitem[1][ii].add( theSeg );
              }
              if( FoundMedia[min[i]].length > length ){
                if( Statementitem[1][length - 1] == null ){
                  Statementitem[1][length - 1] = new MediaItem();
                }
                for( int ii = length; ii < FoundMedia[min[i]].length; ii++ ){
                    // this in case a segment plays different roles and proximity would be
                    // overwritten
                    Segment theSeg = theDataContainer.getMedia( FoundMedia[min[i]][ii] );

                    theSeg.proximity = distance[min[i]];

                    Statementitem[1][length - 1].add( theSeg );
                }
              }
            } else{
              if( Statementitem[1] == null ){
                Statementitem[1] = new MediaItem[1];
                Statementitem[1][0] = new MediaItem();
              }
              for( int ii = 0; ii < FoundMedia[min[i]].length; ii++ ){
                  // this in case a segment plays different roles and proximity would be
                  // overwritten
                  Segment aSeg = theDataContainer.getMedia( FoundMedia[min[i]][ii] );
                  Segment theSeg = aSeg.New();
                  theSeg.proximity = distance[min[i]];
                  Statementitem[1][0].add( theSeg );
              }
            }
          }
        }
      }

      return Statementitem;
    }

  }

  private class Order implements Rule{

    private String Strategy;
    private String[] parameters;

    public Order( String[] A ) throws Exception{

      if( A.length == 0 ){
        throw new Exception( "Not enough parameters: " + A.length );
      }
      Strategy = new String( A[0] );

      parameters = new String[A.length - 1];

      for( int i = 1; i < A.length; i++ ){
        parameters[i-1] = new String( A[i] );
      }
    }

    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      // Get the StorySpace
      ArrayList PlotA = a.GetPlot( 1 );
      ArrayList PlotB = a.GetPlot( 2 );

      if( PlotB.size() == 0 ){
        PlotB = PlotA;
      }

      if( Strategy.equals( "quantity" ) ){
        Quantity( NodeLevel, PlotA, PlotB );
      } else if( Strategy.equals( "logosproximity" ) ){
        Proximity( NodeLevel, PlotA, PlotB );
      } else if( Strategy.equals( "ethos" ) ){
        Ethos( NodeLevel, PlotA, PlotB, parameters[0] );
      } else if( Strategy.equals( "pace" ) ){
        Pace( NodeLevel, PlotA, PlotB );
      } else if( Strategy.equals( "framing" ) ){
        Framing( NodeLevel, PlotA, PlotB );
      }else if( Strategy.equals( "doorder" ) ){
        DoOrder( PlotA, PlotB, parameters[0] );
      }

      int result = ( a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ? 100 : 0 );
      return result;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      throw new Exception( "Rule can not be called as first " );

    }

    private void EqualizePlots( ArrayList PlotA, ArrayList PlotB ){

      int sizeA = PlotA.size();
      int sizeB = PlotB.size();

      if( sizeA == sizeB ){

      } else if( sizeA > sizeB ){
        for( int i = 0; i < sizeA - sizeB; i++ ){
          MediaItem empty = new MediaItem();
          PlotB.add( empty );
        }
      } else{
        for( int i = 0; i < sizeB - sizeA; i++ ){
          MediaItem empty = new MediaItem();
          PlotA.add( empty );
        }
      }
    }

    private void Quantity( int NodeLevel, ArrayList PlotA, ArrayList PlotB ) throws Exception{

      // it starts from zero, we have to start from 1
      NodeLevel++;

      if( PlotA.size() != PlotB.size() ){
        EqualizePlots( PlotA, PlotB );
      }

      int size = PlotA.size();
      int outnumber;
      int min = u.BIGVALUE, max = u.SMALLVALUE;
      double values[] = new double[size];

      for( int i = 0; i < size; i++ ){
        MediaItem ItemA = ( MediaItem )PlotA.get( i );
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        outnumber = ItemA.size() - ItemB.size();

        if( outnumber < min ){
          min = outnumber;
        }

        if( outnumber > max ){
          max = outnumber;
        }

        values[i] = outnumber;

      }


      // scale it
      for( int i = 0; i < size; i++ ){
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        ItemB.addScore( ( (values[i] - min )/(max-min) ) /  NodeLevel );

      }


      return;
    }

    private void Ethos( int NodeLevel, ArrayList PlotA, ArrayList PlotB, String UserModel ) throws Exception{

      // it starts from zero, we have to start from 1
      NodeLevel++;

      if( PlotA.size() != PlotB.size() ){
        EqualizePlots( PlotA, PlotB );
      }

      UserModel user = null;
      for( int i = 0; i < theDataContainer.UserModelsArray.length; i++ ){
        user = theDataContainer.UserModelsArray[i];
        if( user.Id.equals( UserModel ) ){
          break;
        }
      }

      if( user == null ){
        throw new Exception( " User model not found " );
      }

      int size = PlotA.size();
      double outnumber;
      double min = u.BIGVALUE, max = u.SMALLVALUE;
      double values[] = new double[size];

      for( int i = 0; i < size; i++ ){
        MediaItem ItemA = ( MediaItem )PlotA.get( i );
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        outnumber = EvaluateEthos( ItemA, user ) - EvaluateEthos( ItemB, user );

        if( outnumber < min ){
          min = outnumber;
        }

        if( outnumber > max ){
          max = outnumber;
        }

        values[i] = outnumber;

      }

      // scale it
      for( int i = 0; i < size; i++ ){
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        ItemB.addScore( ( (values[i] - min )/(max-min) ) /  NodeLevel );

      }

      return;
    }

    private double EvaluateEthos( MediaItem item, UserModel user ){

      if( item.size() == 0 ){
        return 0;
      }

      ArrayList[] Loop = {
          item.VideoSegments, item.AudioSegments, item.StillSegments, item.TextSegments};

      double score = 0;

      for( int j = 0; j < Loop.length; j++ ){
        if( Loop[j] != null ){
          for( int i = 0; i < Loop[j].size(); i++ ){

            Segment theSeg = ( Segment )Loop[j].get( i );

            Person thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );

            String[] SocialArray = {
                thePerson.HasAge, thePerson.HasEducation, thePerson.HasJob,
                thePerson.HasOrigin, thePerson.HasRace,
                thePerson.HasReligion, thePerson.HasGender};

            for( int z = 0; z < SocialArray.length; z++ ){

              if( user.ExcludedFactors.contains( SocialArray[z] ) ){
                score = score + UserModel.excluded;
              } else if( user.ImportantFactors.contains( SocialArray[z] ) ){
                score = score + UserModel.important;
              } else if( user.VeryImportantFactors.contains( SocialArray[z] ) ){
                score = score + UserModel.veryImportant;
              }
            }
            theSeg.addScore( score );
          }
        }
      }
      score = score / item.size();

      return score;
    }

    private void Framing( int NodeLevel, ArrayList PlotA, ArrayList PlotB ) throws Exception{

      // it starts from zero, we have to start from 1
      NodeLevel++;

      if( PlotA.size() != PlotB.size() ){
        EqualizePlots( PlotA, PlotB );
      }


      int size = PlotA.size();
      double outnumber;
      double min = u.BIGVALUE, max = u.SMALLVALUE;
      double values[] = new double[size];

      for( int i = 0; i < size; i++ ){
        MediaItem ItemA = ( MediaItem )PlotA.get( i );
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        outnumber = EvaluateFraming( ItemB ) - EvaluateFraming( ItemA );

        if( outnumber < min ){
          min = outnumber;
        }

        if( outnumber > max ){
          max = outnumber;
        }

        values[i] = outnumber;

      }

      // scale it
      for( int i = 0; i < size; i++ ){
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        ItemB.addScore( ( (values[i] - min )/(max-min) ) /  NodeLevel );

      }

      return;
    }

    private double EvaluateFraming( MediaItem item ){

      if( item.size() == 0 ){
        return 0;
      }


      double score = 0, total = 0;


      if( item.VideoSegments != null ){
        for( int i = 0; i < item.VideoSegments.size(); i++ ){

          VideoSegment theSeg = ( VideoSegment )item.VideoSegments.get( i );
          String compare = new String(theSeg.StartFraming.substring(theSeg.StartFraming.lastIndexOf('#') + 1 ));

          if( compare.equals("ExtremeCloseUp") ){
            score = 0;
          }else if( compare.equals("CloseUp") ){
            score = 1;
          }else if( compare.equals("MediumCloseUp") ){
            score = 2;
          }else if( compare.equals("MidShot") ){
            score = 3;
          }else if( compare.equals("WideShot") ){
            score = 4;
          }else if( compare.equals("VeryWideShot") ){
            score = 5;
          }else if( compare.equals("ExtremeWideShot") ){
            score = 6;
          }else if( compare.equals("NoddyShot") ){
            score = 5;
          }else if( compare.equals("TwoShot") ){
            score = 5;
          }

          theSeg.addScore( score );

          total = total + score;
        }
      }

      total = total / item.size();

      return total;
    }

    private void Proximity( int NodeLevel, ArrayList PlotA, ArrayList PlotB ) throws Exception{

      if( PlotA.size() != PlotB.size() ){
        EqualizePlots( PlotA, PlotB );
      }

      double proximity;

      int size = PlotA.size();
      double min = u.BIGVALUE, max = u.SMALLVALUE;

      double values[] = new double[size];

      for( int i = 0; i < size; i++ ){

        MediaItem ItemA = ( MediaItem )PlotA.get( i );
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        if( ItemA.size() != 0 && ItemB.size() != 0 ){
          proximity = (  ( EvaluateProximity(ItemB) / ItemB.size() - EvaluateProximity(ItemA)/ ItemA.size() ) );
        } else{
          proximity = 10;
        }

        if( proximity < min ){
          min = proximity;
        }

        if( proximity > max ){
          max = proximity;
        }

        values[i] = proximity;

      }

      // scale it
      for( int i = 0; i < size; i++ ){
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        ItemB.addScore( ( (values[i] - min )/(max-min) ) /  NodeLevel);

      }

      return;
    }

    private double EvaluateProximity( MediaItem item ){

      int size = item.size();

      if( size == 0 ){
        return Util.BIGVALUE;
      }

      ArrayList[] Loop = {
          item.VideoSegments, item.AudioSegments, item.StillSegments, item.TextSegments};

      double score = 0;

      for( int j = 0; j < Loop.length; j++ ){
        if( Loop[j] != null ){
          for( int i = 0; i < Loop[j].size(); i++ ){

            Segment theSeg = ( Segment )Loop[j].get( i );

            score = score + theSeg.proximity;

          }
        }
      }

      score = score / size;

      return score;


    }

    private void Pace( int NodeLevel, ArrayList PlotA, ArrayList PlotB ) throws Exception{

      if( PlotA.size() != PlotB.size() ){
        EqualizePlots( PlotA, PlotB );
      }

      int size = PlotA.size();
      double min = u.BIGVALUE, max = u.SMALLVALUE;

      double values[] = new double[size];

      for( int i = 0; i < size; i++ ){

        MediaItem ItemA = ( MediaItem )PlotA.get( i );
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        double tempoA = 0;
        double tempoB = 0;

        if( ItemA.size() != 0 ){
          tempoA = EvaluatePace(ItemA);
        }

        if( ItemB.size() != 0 ){
          tempoB = EvaluatePace(ItemB);
        }

        values[i] = (tempoA+tempoB)/ 2;

        if( values[i] < min ){
          min = values[i];
        }

        if( values[i] > max ){
          max = values[i];
        }



      }


      // scale it
      for( int i = 0; i < size; i++ ){
        MediaItem ItemB = ( MediaItem )PlotB.get( i );

        ItemB.addScore( ( (values[i] - min )/(max-min) ) /  NodeLevel);

      }

      return;
    }

    private double EvaluatePace( MediaItem item ){

      int size = item.size();

      if( size == 0 ){
        return 0;
      }

      ArrayList[] Loop = {
          item.VideoSegments, item.AudioSegments};

      double score = 0;

      for( int j = 0; j < Loop.length; j++ ){
        if( Loop[j] != null ){
          for( int i = 0; i < Loop[j].size(); i++ ){

            Segment theSeg = ( Segment )Loop[j].get( i );

            score = score + (u.ConvertToDSec( ( ( VideoSegment )theSeg ).EndFrame) -
                             u.ConvertToDSec( ( ( VideoSegment )theSeg ).BeginFrame )
                );
          }
        }
      }

      score = score / size;

      return score;

    }

    private void DoOrder( ArrayList PlotA, ArrayList PlotB, String Direction ){

      if( PlotA.size() != PlotB.size() ){
        EqualizePlots( PlotA, PlotB );
      }

      int size = PlotA.size();
      double[] scores = new double[size];
      int[] order = new int[size];

      for( int i = 0; i < size; i++ ){
        MediaItem ItemB = ( MediaItem )PlotB.get( i );
        scores[i] = ItemB.score;
      }
      if( Direction.equals( "ASC" ) ){
        order = u.Order( scores, true );
      } else if( Direction.equals( "DES" ) ){
        order = u.Order( scores, false );
      }

      ArrayList TempPlotA = new ArrayList( PlotA );

      ArrayList TempPlotB = new ArrayList( PlotB );

      PlotA.clear();
      PlotB.clear();

      for( int i=0; i<size; i++ ){
        PlotA.add(i, TempPlotA.get( order[i] ));
        PlotB.add(i, TempPlotB.get( order[i] ));
      }
    }
  }

  private class SelectUnique implements Rule{

    boolean parallel = false;

    public SelectUnique( String[] A ) throws Exception{

      if( A != null && A.length == 1 && A[0].equals( "parallel" ) ){
        parallel = true;
      }
    }

    // This function requires two groups of video segments and
    // it creates a Plot putting what it can not use in Expunged
    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      ArrayList PlotA = a.GetPlot( 1 );
      ArrayList PlotB = a.GetPlot( 2 );

      SelectUniquePlot( PlotA, a );
      SelectUniquePlot( PlotB, a );

      int result = a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ? 100 : 0;
      return result;
    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 1 ) + a.SizePlot( 2 ) ) == 0 ){
        throw new Exception( "This rule can not be on top " );
      }

      return 0;

    }

    private void SelectUniquePlot( ArrayList Plot, StorySpace a ) throws Exception{

      Hashtable Keys = new Hashtable();

      for( int i = 0; i < Plot.size(); i++ ){

        MediaItem item = ( MediaItem )Plot.get( i );

        if( item.VideoSegments != null ){
          for( int j = 0; j < item.VideoSegments.size(); j++ ){

            Segment theSeg = ( Segment )item.VideoSegments.get( j );

            if( Keys.containsKey( theSeg.Id ) ){
              MediaItem Clashitem = ( MediaItem )Keys.get( theSeg.Id );
              if( item.VideoSegments.size() > 1 ){
                item.remove( theSeg );
                j--;

              } else if( Clashitem.VideoSegments.size() > 1 ){
                Clashitem.remove( theSeg );
                Keys.put( theSeg.Id, item );
              } else{
                if( parallel == true ){
                  // only in case of argumentation do something
                  a.ExpungeFromPlot( item );
                  i--;
                } else{
                  item.remove( theSeg );
                  j--;
                }
              }
            } else{
              Keys.put( theSeg.Id, item );
            }
          }
        }

      }
    }
  }

  /************************* GAZECLASH ******************************************/

  private class GazeClash implements Rule{

    boolean AToLeft = false;

    boolean BToLeft = !AToLeft;

    // This function requires two groups of video segments and
    // it creates a Plot putting what it can not use in Expunged
    public int ApplyRule( StorySpace a, int NodeLevel ) throws Exception{

      boolean XToLeft = AToLeft;
      for( int j = 1; j < 3; j++ ){
        for( int i = 0; i < a.GetPlot( j ).size(); i++ ){

          MediaItem item = ( MediaItem )a.GetPlot( j ).get( i );

          if( item.VideoSegments != null ){
            for( int ii = 0; ii < item.VideoSegments.size(); ii++ ){
              VideoSegment theVideo = ( VideoSegment )item.VideoSegments.get( ii );
              String Gaze = theVideo.Gaze.substring( DataBuilder.VoxPopuliNamespaces.length() );
              String StartFraming = theVideo.StartFraming.substring( DataBuilder.VoxPopuliNamespaces.length() );
              String EndFraming = theVideo.EndFraming.substring( DataBuilder.VoxPopuliNamespaces.length() );

              boolean noddy = false;
              if( StartFraming.equals( "Noddy Shot" ) ){
                noddy = true;
              }
              boolean result = ( !XToLeft && !noddy ) || ( XToLeft && noddy );
              if( Gaze.equals( "LeftCenter" ) || Gaze.equals( "Left" ) ){

                SwapGazeDirection( result, theVideo );

              } else if( Gaze.equals( "RightCenter" ) || Gaze.equals( "Right" ) ){

                SwapGazeDirection( !result, theVideo );

              } else{
                // nothing is done for center gaze
              }
            }
          }
        }
        XToLeft = BToLeft;
      }
      int result = a.SizePlot( 1 ) + a.SizePlot( 2 ) > 0 ? 100 : 0;
      return result;
    }

    private void SwapGazeDirection( boolean doit, Segment theSeg ){

      if( doit == true ){
        if( !theSeg.FileName.endsWith( "r" ) ){
          theSeg.FileName = new String( theSeg.FileName + "r" );
        } else{
          //theSeg.FileName = new String( theSeg.FileName.substring( 0, theSeg.FileName.length() - 1 ) );
        }
      } else{
        if( !theSeg.FileName.endsWith( "r" ) ){
        } else{
          theSeg.FileName = new String( theSeg.FileName.substring( 0, theSeg.FileName.length() - 1 ) );
        }

      }

    }

    public int DoSelection( StorySpace a, int NodeLevel ) throws Exception{

      if( ( a.SizePlot( 1 ) + a.SizePlot( 2 ) ) > 0 ){
        throw new Exception( "Plot not null in first selection " );
      }

      return this.ApplyRule( a, NodeLevel );

    }
  }

  /********************* END RULES **********************/

  /********************* FUNCTION FOR RULES **********************/

  private boolean SelectinStory( MyHashTable Story, MyHashTable OldStory,
                                 String[] Field, PersonSelect theRule ) throws Exception{

    Hashtable Fields = new Hashtable();

    if( Field != null ){
      for( int i = 0; i < Field.length; i++ ){
        Fields.put( Field[i], Field[i] );
      }

      Story.clear();

      for( Enumeration e = OldStory.elements(); e.hasMoreElements(); ){

        MediaItem theItem = ( MediaItem )e.nextElement();

        MediaItem theAddedItem = new MediaItem();

        ArrayList[] Loop = {
            theItem.VideoSegments, theItem.AudioSegments, theItem.StillSegments,
            theItem.TextSegments};

        for( int j = 0; j < Loop.length; j++ ){
          if( Loop[j] != null ){
            for( int i = 0; i < Loop[j].size(); i++ ){

              Segment theSeg = ( Segment )Loop[j].get( i );

              Person thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );
              if( ( thePerson != null ) && Fields.containsKey( theRule.FieldString( thePerson ) ) ){
                theAddedItem.add( theSeg );
              }
            }
          }
        }
        Story.put( theAddedItem );

      }

    }

    return true;
  }

  /*
    // Expunge if one element of the Plot does not fulfill the requirement
    private boolean SelectinPlot( StorySpace a, Hashtable Expunged,
                                  String[] FieldA, String[] FieldB, PersonSelect theRule ) throws Exception{

      Hashtable FieldsA = new Hashtable();
      Hashtable FieldsB = new Hashtable();

      if( FieldA != null || FieldB != null ){

        if( FieldA != null ){
          for( int i = 0; i < FieldA.length; i++ ){
            FieldsA.put( FieldA[i], FieldA[i] );
          }
        }
        if( FieldB != null ){
          for( int i = 0; i < FieldB.length; i++ ){
            FieldsB.put( FieldB[i], FieldB[i] );
          }
        }

        ArrayList[] TempPlot = {
            new ArrayList( a.GetPlot( 0 ) ), new ArrayList( a.GetPlot( 1 ) )};
        Hashtable[] Fields = {
            FieldsA, FieldsB};

        for( int index = 0; index < TempPlot.length; index++ ){
          external_loop:

              for( int i = 0; i < TempPlot[index].size(); i++ ){

            MediaItem item = ( MediaItem )TempPlot[index].get( i );
            if( item.VideoSegments != null ){
              for( int ii = 0; ii < item.VideoSegments.size(); ii++ ){
                Segment theSeg = ( Segment )item.VideoSegments.get( ii );
                Person thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );
                if( ( thePerson != null ) && Fields[index].containsKey( theRule.FieldString( thePerson ) ) ){
                } else{
                  a.ExpungeFromPlot( item );
                  continue external_loop;
                }
              }
            }

            if( item.AudioSegments != null ){
              for( int ii = 0; ii < item.AudioSegments.size(); ii++ ){
                Segment theSeg = ( Segment )item.AudioSegments.get( ii );
                Person thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );
                if( ( thePerson != null ) && Fields[index].containsKey( theRule.FieldString( thePerson ) ) ){
                } else{
                  a.ExpungeFromPlot( item );
                  continue external_loop;
                }
              }
            }

            if( item.StillSegments != null ){
              for( int ii = 0; ii < item.StillSegments.size(); ii++ ){
                Segment theSeg = ( Segment )item.StillSegments.get( ii );
                Person thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );
                if( ( thePerson != null ) && Fields[index].containsKey( theRule.FieldString( thePerson ) ) ){
                } else{
                  a.ExpungeFromPlot( item );
                  continue external_loop;
                }
              }
            }

            if( item.TextSegments != null ){
              for( int ii = 0; ii < item.TextSegments.size(); ii++ ){
                Segment theSeg = ( Segment )item.TextSegments.get( ii );
                Person thePerson = ( Person )theDataContainer.Interviewees.get( theSeg.Subject );
                if( ( thePerson != null ) && Fields[index].containsKey( theRule.FieldString( thePerson ) ) ){
                } else{
                  a.ExpungeFromPlot( item );
                  continue external_loop;
                }

              }
            }
          }
        }
      }
      return true;
    }

   */
  // This function implements the logic that determines whether 2 statements
  // contradict or support each others.
  private int CheckRole( String Strategy, NameIndex ToulminType, Link aLink ) throws Exception{

    int generalize = 0, associate = 0, specialize = 0, attack = 0, support = 0, self = 0;

    for( int i = 0; i < aLink.LinkType.size(); i++ ){
      NameIndex a = ( ( RelationDescription )aLink.LinkType.get( i ) ).Change;
      if( a == DataBuilder.RhetActStrings[DataBuilder.ATTACK] ){
        attack++;
      } else if( a == DataBuilder.RhetActStrings[DataBuilder.SUPPORT] ){
        support++;
      } else if( a == DataBuilder.RhetActStrings[DataBuilder.GENERALIZE] ){
        generalize++;
      } else if( a == DataBuilder.RhetActStrings[DataBuilder.ASSOCIATE] ){
        associate++;
      } else if( a == DataBuilder.RhetActStrings[DataBuilder.SELF] ){
        self++;
      } else if( a == DataBuilder.RhetActStrings[DataBuilder.SPECIALIZE] ){
        specialize++;
      } else{
        throw new Exception( "Unknown linked relation in CheckRole " + a.Name );
      }
    }
    int total = generalize + associate + specialize + attack + support + self;

    if( Strategy.equals( "attack" ) ){
      switch( ToulminType.index ){
        case DataBuilder.CLAIM:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.BACKING:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.CONCESSION:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.CONDITION:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.DATA:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.WARRANT:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.EXAMPLE:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        default:
          throw new Exception( "Unknown original Toulmin role in CheckRole " + ToulminType.Name );
      }
    } else if( Strategy.equals( "support" ) ){
      switch( ToulminType.index ){
        case DataBuilder.CLAIM:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.BACKING:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.CONCESSION:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.CONDITION:
          if( u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.DATA:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.WARRANT:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        case DataBuilder.EXAMPLE:
          if( !u.even( attack ) ){
            return 100;
          }
          break;
        default:
          throw new Exception( "Unknown original Toulmin role in CheckRole " + ToulminType.Name );
      }
    } else{
      throw new Exception( "Unknown Strategy in CheckRole " + Strategy );
    }

    return total;
  }

  /********************* END FUNCTION FOR RULES **********************/


  // This function finds the longest path of related statements
  private int GetMaxLength( VerbalStatement aStatement, ArrayList current ) throws
      Exception{

    int max = 0;
    //This hash table contains the longest path
    ArrayList MaxChain = null;

    if( !current.contains( aStatement.Id ) ){
      current.add( aStatement.Id );
    } else{
      return 0;
    }

    if( aStatement.ConnectedStatements != null ){
      int length = 0;
      //The Hashtable is used to explore the chain and gets every time filled in
      // with the iterative exploration, which if it yields a max length, gets copied
      // to the Max hashtables
      ArrayList local;
      VerbalStatement bStatement;

      for( Enumeration e = aStatement.ConnectedStatements.elements(); e.hasMoreElements(); ){

        Link aLink = ( Link )e.nextElement();
        if( current.contains( aLink.StatementId ) ){
          continue;
        }

        if( ( bStatement = ( VerbalStatement )StorySpace.get( aLink.StatementId ) ) == null ){
          throw new Exception( "Statement not found " );
        }

        // reset the iterative hash table for a new iteration
        local = new ArrayList( current );
        length = GetMaxLength( bStatement, local );
        if( length > max ){
          max = length;
          // New max found, reset the MaxChain hashtable
          theOutputs.PrintLn( theOutputs.Debug2, "Length of the chain " + max );
          MaxChain = new ArrayList( local );
        }
      }
    }

    if( MaxChain != null ){
      // Items will be copied again in the hash table
      current.clear();
      for( int i = 0; i < MaxChain.size(); i++ ){
        current.add( i, MaxChain.get( i ) );
      }
    }

    return( max + 1 );

  }

  private boolean LongestPath( String filename, SMILMedia theSMILMedia ) throws Exception{

    // We select the longest chain of statements, not very logical
    // but it gives an idea of how many connected statements there are
    theOutputs.PrintLn( theOutputs.Locator, "Calculating Longest Path " );
    int max = 0;
    ArrayList current = new ArrayList();
    ArrayList maxchain = null;
    for( Enumeration e = StorySpace.elements(); e.hasMoreElements(); ){
      VerbalStatement aStatement = ( VerbalStatement )e.nextElement();
      int i = GetMaxLength( aStatement, current );
      if( i != current.size() ){
        throw new Exception( "Size of Max Chain not correct " );
      }
      if( i > max ){
        max = i;
        theOutputs.PrintLn( theOutputs.Debug1, "Length of the chain " + i );
        maxchain = new ArrayList( current );
      }
      // reset the iterative hash table for a new iteration
      current = new ArrayList();
    }

    StatementsToMedia( maxchain, true, true );
    theSMILMedia.DoMontage( MediaArray );
    theSMILMedia.CreateSMILOutput( true );

    return true;
  }

  // This function looks in the anArgument to see if the structure can be "augmented"
  // with other segments. The segments that are allowed depend on the Strategy attack or support and
  // the new created structure is Replaced. dupStat says whether to allow duplicate statements in the augmented structure
  // and useSpk indicates whether to use the main speaker against
  // him/her self and keep indicates whether to keep the original segment or not. NodeOn indicates whether the
  // count should be based on the number of Nodes or Statements replaced
  public void AugmentStructure( String Strategy, boolean allowDupStat, boolean useSpk, boolean keep,
                                int[] augmented, Argument anArgument, Argument AugmentedArg ) throws Exception{

    if( anArgument.Nodes == null && anArgument.Arguments == null ){
      return;
    }
    if( anArgument.Nodes != null ){

      for( int i = 0; i < anArgument.Nodes.length; i++ ){
        ToulminNode aNode = anArgument.Nodes[i];
        boolean NewNode = true;
        if( aNode.theStatementIDs != null ){

          for( int ii = 0; ii < aNode.theStatementIDs.length; ii++ ){
            // This statement is the one contained in the Argumentation
            VerbalStatement aStatement = ( VerbalStatement )StorySpace.get( aNode.theStatementIDs[ii] );

            if( aStatement.ConnectedStatements != null ){

              for( Enumeration e = aStatement.ConnectedStatements.elements(); e.hasMoreElements(); ){

                Link aLink = ( Link )e.nextElement();
                // This statement is the one linked to the one contained in the Argumentation
                VerbalStatement bStatement = ( VerbalStatement )StorySpace.get(
                    aLink.StatementId );

                if( allowDupStat == false ){
                  // Check whether the statement is already present in the argument
                  // In this case we do not use it, but we could to show for ex. contraddictions
                  boolean present = false;
                  for( int index1 = 0; !present && ( index1 < AugmentedArg.Nodes.length ); index1++ ){
                    for( int index2 = 0; !present && ( index2 < AugmentedArg.Nodes[index1].theStatementIDs.length );
                         index2++ ){
                      if( AugmentedArg.Nodes[index1].theStatementIDs[index2].equals( bStatement.Id ) ){
                        present = true;
                      }
                    }
                  }
                  // The statement is already in the selected ones
                  if( present ){
                    continue;
                  }
                }

                // If the claimer is the same as the one of the main statement, do not count it
                // if flag is true
                if( ( useSpk == true ) &&
                    ( bStatement.ClaimerId.equals( aStatement.ClaimerId ) ) ){
                  continue;
                }
                // Check whether the segment fits the strategy
                if( CheckRole( Strategy, aNode.ToulminType, aLink ) < 100 ){
                  // This continue skips the increment below
                  continue;
                } else{

                  int index = -1;
                  VerbalStatement[] Temp = null;

                  // If the node in the original structure is new and we must not include the statement in the
                  // augmented structure, create a new node in the augmented one
                  if( !keep && NewNode ){
                    AugmentedArg.Nodes[i].theStatementIDs = new String[0];
                  }

                  Temp = new VerbalStatement[AugmentedArg.
                      Nodes[i].theStatementIDs.length];

                  for( int v = 0;
                       v < AugmentedArg.Nodes[i].theStatementIDs.length; v++ ){
                    // We copy the content before overwriting it.
                    Temp[v] = new VerbalStatement( ( VerbalStatement )StorySpace.get( AugmentedArg.Nodes[i].
                        theStatementIDs[v] ) );

                    // Look for the position of the statement to replace
                    if( keep && aStatement.Id.equals( Temp[v].Id ) ){
                      index = v;
                    }
                  }
                  if( keep && index == -1 ){
                    throw new Exception( "Statement to replace not found " );
                  }

                  // Reallocate the array to make place for the new statement
                  AugmentedArg.Nodes[i].theStatementIDs = new String[
                      AugmentedArg.Nodes[i].theStatementIDs.length + 1];

                  // We keep the original and add the replacement
                  int gap = 0;
                  for( int v = 0;
                       v < AugmentedArg.Nodes[i].theStatementIDs.length; v++ ){
                    // Add the statement after the original one
                    if( ( keep && ( v == ( index + 1 ) ) ) ||
                        // or at the end if we do not need to keep it
                        ( !keep && ( v == AugmentedArg.Nodes[i].theStatementIDs.length - 1 ) ) ){
                      AugmentedArg.Nodes[i].theStatementIDs[v] = new String( bStatement.Id );
                      gap = 1;
                      // otherwise we copy it from the original to the augmented structure
                    } else if( keep || ( ( !keep ) && ( v != AugmentedArg.Nodes[i].theStatementIDs.length - 1 ) ) ){
                      AugmentedArg.Nodes[i].theStatementIDs[v] = new
                          String( Temp[v - gap].Id );
                    } else{
                      throw new Exception( "Code should not get here " );
                    }
                  }
                }
              }
              if( NewNode ){
                augmented[1]++; //Nodes
                NewNode = false;
              }
              augmented[0]++; //Statements
            }
          }
        }
      }
    }

    if( anArgument.Arguments != null ){
      for( int i = 0; i < anArgument.Arguments.length; i++ ){
        AugmentStructure( Strategy, allowDupStat,
                          useSpk, keep, augmented, anArgument.Arguments[i], AugmentedArg.Arguments[i] );
      }
    }
    return;
  }

  public boolean Multiplevoices( String Strategy, boolean keep, boolean NodeOn, boolean original, String filename,
                                 SMILMedia theSMILMedia ) throws
      Exception{

    theOutputs.PrintLn( theOutputs.Locator, "Calculating Multiple Voices " + filename );

    int ReplacedRoles = 0, max = 0, index[] = {
        -1, -1};
    Argument Voices = null, Temp;
    boolean result = false;

    // Look for interview with different roles
    for( int i = 0; i < theDataContainer.InterviewsArray.length; i++ ){
      if( theDataContainer.InterviewsArray[i].Arguments != null ){
        for( int ii = 0; ii < theDataContainer.InterviewsArray[i].Arguments.length; ii++ ){
          // The following function looks whether the statements contained in the
          // Argument can be replaced and constructs a new argument with the replaced
          // statements.
          Temp = new Argument( theDataContainer.InterviewsArray[i].Arguments[ii] );
          int[] augmented = new int[2];
          AugmentStructure( Strategy, false, false, keep, augmented, theDataContainer.InterviewsArray[i].Arguments[ii],
                            Temp );

          ReplacedRoles = ( NodeOn == true ? augmented[1] : augmented[0] );
          if( ReplacedRoles > max ){
            max = ReplacedRoles;
            Voices = new Argument( Temp );
            index[0] = i;
            index[1] = ii;
          }
        }
      }
    }
    if( max > 0 ){

      if( original == true ){
        // Clear the array segment
        if( MediaArray != null ){
          MediaArray.clear();
        }
        result = OrderArgument( theDataContainer.InterviewsArray[index[0]].Arguments[index[1]], true );
        result = result && theSMILMedia.DoMontage( MediaArray );
        theSMILMedia.SetOutputFile( filename + "_ORIG", true, null );
        result = result && theSMILMedia.CreateSMILOutput( true );
      } else{
        result = true;
      }
      // Clear the array segment
      if( MediaArray != null ){
        MediaArray.clear();
      }

      result = result && OrderArgument( Voices, true );
      result = result && theSMILMedia.DoMontage( MediaArray );
      theSMILMedia.SetOutputFile( filename, true, null );
      result = result && theSMILMedia.CreateSMILOutput( true );
    }

    return result;
  }

  public boolean OrderArgument( Argument MultipleVoices, boolean do_order ) throws Exception{

    boolean result = false;
    int[] order = {
        DataBuilder.EXAMPLE, DataBuilder.DATA, DataBuilder.WARRANT, DataBuilder.BACKING,
        DataBuilder.CLAIM, DataBuilder.CONCESSION, DataBuilder.CONDITION};

    ArrayList ChainStatements = new ArrayList();

    theOutputs.PrintLn( theOutputs.Locator, "Ordering Arguments" );

    if( MultipleVoices == null ){
      return result;
    }
    if( do_order == true ){
      for( int i = 0; i < order.length; i++ ){

        if( MultipleVoices.Nodes != null ){
          for( int ii = 0; ii < MultipleVoices.Nodes.length; ii++ ){
            if( MultipleVoices.Nodes[ii].ToulminType.index == order[i] ){

              if( MultipleVoices.Nodes[ii].theStatementIDs == null ){
                continue;
              }

              for( int iii = 0; iii < MultipleVoices.Nodes[ii].theStatementIDs.length; iii++ ){
                ChainStatements.add( StorySpace.get( MultipleVoices.Nodes[ii].theStatementIDs[iii] ) );
              }
            }
          }
        }
        if( MultipleVoices.Arguments != null ){
          for( int ii = 0; ii < MultipleVoices.Arguments.length; ii++ ){
            if( MultipleVoices.Arguments[ii].ToulminType.index == order[i] ){
              result = OrderArgument( MultipleVoices.Arguments[ii], do_order );
              if( result == false ){
                return false;
              }
            }
          }
        }
      }
    } else{

      if( MultipleVoices.Nodes != null ){
        for( int ii = 0; ii < MultipleVoices.Nodes.length; ii++ ){
          if( MultipleVoices.Nodes[ii].theStatementIDs == null ){
            continue;
          }

          for( int iii = 0; iii < MultipleVoices.Nodes[ii].theStatementIDs.length; iii++ ){
            ChainStatements.add( StorySpace.get( MultipleVoices.Nodes[ii].theStatementIDs[iii] ) );
          }
        }
      }
      if( MultipleVoices.Arguments != null ){
        for( int ii = 0; ii < MultipleVoices.Arguments.length; ii++ ){
          result = OrderArgument( MultipleVoices.Arguments[ii], do_order );
          if( result == false ){
            return false;
          }
        }
      }
    }

    result = StatementsToMedia( ChainStatements, false, false );
    return result;
  }

  // This function creates an ordered sequence of segments so that segments are
  // responding to each other
  private boolean StatementsToMedia( ArrayList StatementsChain, boolean key, boolean clear ) throws Exception{

    int index = 0;
    VerbalStatement aStatement;
    String Msg;

    MediaArray = new ArrayList();

    if( StatementsChain == null ){
      return false;
    }

    for( int i = 0; i < StatementsChain.size(); i++ ){
      if( key == true ){
        if( ( aStatement = ( VerbalStatement )StorySpace.get( ( ( Link )StatementsChain.get( i ) ).StatementId ) ) == null ){
          throw new Exception( "Statement not found " );
        }
        Msg = new String( ( ( ( Link )StatementsChain.get( i ) ).LinkType ).PrintLinkList() );
      } else{
        aStatement = ( VerbalStatement )StatementsChain.get( i );
        Msg = new String( "" );
      }

      theDataContainer.AddDataToMedia( aStatement.theMediaItem, Msg, null, null, false );

      MediaItem a = new MediaItem( aStatement.theMediaItem, theDataContainer );
      MediaArray.add( index, a );
      index++;
    }

    return true;
  }

  /********************* END GENERAL RULES **********************/




  public RuleInstance( DataContainer b, Outputs p ){
    theDataContainer = b;
    StorySpace = new Hashtable( b.Statements );
    theOutputs = p;
    //theDataBuilder = b;
    u = new Util();
  }

  public Rule FindRule( String Name, String[] A, String[] B ) throws Exception{

    Rule r;

    if( Name.equals( "Dummy" ) ){
      r = new Dummy();
    } else if( Name.equals( "ON" ) ){
      r = new NarTimeOn();
    } else if( Name.equals( "EliminateCharacter" ) ){
      r = new EliminateCharacter( A );
    } else if( Name.equals( "SelectRace" ) ){
      r = new SelectRace( A, B );
    } else if( Name.equals( "SelectAge" ) ){
      r = new SelectAge( A, B );
    } else if( Name.equals( "SelectEducation" ) ){
      r = new SelectEducation( A, B );
    } else if( Name.equals( "SelectEmployment" ) ){
      r = new SelectEmployment( A, B );
    } else if( Name.equals( "SelectGeoLocation" ) ){
      r = new SelectGeoLocation( A, B );
    } else if( Name.equals( "SelectReligion" ) ){
      r = new SelectReligion( A, B );
    } else if( Name.equals( "SelectGender" ) ){
      r = new SelectGender( A, B );
    } else if( Name.equals( "SelectConcept" ) ){
      r = new SelectConcepts( A, B );
    } else if( Name.equals( "SelectQuestion" ) ){
      r = new SelectQuestions( A, B );
    } else if( Name.equals( "SelectPartecipant" ) ){
      r = new SelectPartecipants( A, B );
    } else if( Name.equals( "SelectInterview" ) ){
      r = new SelectInterview( A, B );
    } else if( Name.equals( "SelectOpinion" ) ){
      r = new SelectOpinions( A, B );
    } else if( Name.equals( "CreateArg" ) ){
      r = new CreateArg( A );
    } else if( Name.equals( "FilmFeatures" ) ){
      r = new FilmFeatures( A, B );
    } else if( Name.equals( "Order" ) ){
      r = new Order( A );
    } else if( Name.equals( "GazeClash" ) ){
      r = new GazeClash();
    } else if( Name.equals( "SelectUnique" ) ){
      r = new SelectUnique( A );
    } else if( Name.equals( "CreatePlot" ) ){
      r = new CreatePlot();
    } else if( Name.equals( "OFF" ) ){
      r = new NarTimeOff();
    } else{
      throw new Exception( "Unknown Rule Name " + Name );
    }
    return r;
  }

  private void jbInit() throws Exception{
  }

}
