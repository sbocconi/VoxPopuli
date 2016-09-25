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
public class MyArrayList extends ArrayList{
  MyArrayList(){
    super();
  }

  MyArrayList( int i ){
    super(i);
  }

  MyArrayList( MyArrayList a ){
    super( ( ArrayList )a );
  }

  public MyArrayList invert(){
    MyArrayList b = new MyArrayList();

    for( int i = this.size() - 1; i >= 0; i-- ){
      b.add( this.get( i ) );
    }
    return b;
  }

  /*
      public boolean contains( LinkDescription a ){
        for( int i = 0; i < this.size(); i++ ){
          LinkDescription b = ( LinkDescription )this.get( i );
          if( b.equals( a ) ){
            return true;
          }
        }
        return false;
      }
   */
  public boolean contains( Link a ){
    for( int i = 0; i < this.size(); i++ ){
      Link b = ( Link )this.get( i );
      if( b.StatementId.equals( a.StatementId ) ){
        return true;
      }
    }
    return false;
  }

  public boolean contains( ConstructedStatement a ){
    for( int i = 0; i < this.size(); i++ ){
      ConstructedStatement b = ( ConstructedStatement )this.get( i );
      if( b.equals( a ) ){
        return true;
      }
    }
    return false;
  }
  public String PrintLinkList( ){
    String Msg = new String( "" );
    if( this != null ){
      Msg = new String( " Link:" );
      for( int i = 0; i < this.size(); i++ ){
        RelationDescription Desc = ( RelationDescription )this.get( i );
        String NewMsg = Desc.PartChanged + " " + Desc.Change.Name;
        Msg = new String( Msg + " " + NewMsg );
      }
    }
    return Msg;
  }

  /*
      public boolean contains( NameIndex a ){
        for( int i = 0; i < this.size(); i++ ){
          LinkDescription b = ( LinkDescription )this.get( i );
          if( b.PartChanged.equals( a ) ||
              b.ModifierChange.equals( a ) ||
              b.PredicateChange.equals( a ) ||
              b.ObjectChange.equals( a )
              ){
            return true;
          }
        }
        return false;
      }
   */
}
