package nl.cwi.ins2.voxpopuli;

import java.util.ArrayList;

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

public class RelationDescription{
  String PartChanged;
  NameIndex Change;
  String FromPartId;
  String FromPartDes;
  String ToPartId;
  String ToPartDes;
  String Relation;
  double PSharedscore;
  int HITscore;
  double NSharedscore;
  int MISSscore;

  RelationDescription(){
    PartChanged = new String( "none" );
    Change = DataBuilder.RhetActStrings[DataBuilder.SELF];
    FromPartId = new String( "" );
    FromPartDes = new String( "Self" );
    ToPartId = new String( "" );
    ToPartDes = new String( "Self" );
    Relation = new String( "" );
    PSharedscore = 0.0;
    NSharedscore = 0.0;
    HITscore = 0;
    MISSscore = 0;
  }

  RelationDescription( String a, NameIndex b, String c, String d, String e, String f, String g ) throws Exception{
    if( a != null && b != null && c != null && d != null && e != null && f != null && g != null ){
      PartChanged = new String( a );
      Change = DataBuilder.RhetActStrings[b.index];
      FromPartId = new String( c );
      FromPartDes = new String( d );
      ToPartId = new String( e );
      ToPartDes = new String( f );
      Relation = new String( g );
      PSharedscore = 0.0;
      NSharedscore = 0.0;
      HITscore = 0;
      MISSscore = 0;

    } else{
      throw new Exception( "Object null in RelationDescription constructor " );
    }
  }
  /*
      LinkDescription( LinkDescription a ){
        PartChanged = new String(a.PartChanged);
        Change = a.Change;
        FromPartId = new String(a.FromPartId);
        ToPartId = new String(a.ToPartId);

      }

      public boolean equals( LinkDescription a ){

        return PartChanged.equals( a.PartChanged ) &&
            Change.equals( a.Change );

      }
   */
};

