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
public class ConstructedStatement{
  String Subject;
  String SubjectDescription;
  String Modifier;
  String ModifierDescription;
  String Predicate;
  String PredicateDescription;
  /*
      String Object;
      String ObjectDescription;
   */

  // This array contains object of type LinkDescription, i.e. how this constructed statement
  // is connected to the original
  MyArrayList LinkType;
  NameIndex ToulminType;
  public boolean equals( ConstructedStatement a ){
    boolean result = true;

    if( ( a.Subject != null && !a.Subject.equals( this.Subject ) ) ||
        ( a.Modifier != null && !a.Modifier.equals( this.Modifier ) ) ||
        ( a.Predicate != null && !a.Predicate.equals( this.Predicate ) )
        ){
      result = false;
    }
    return result;

  }

  //ConstructedStatement(){};
  ConstructedStatement( VerbalStatement a ) throws Exception{
    if( a == null ){
      throw new Exception( "Object null in ConstructedStatement copy constructor " );
    } else{

      if( a.Subject != null ){
        this.Subject = new String( a.Subject );
      }
      if( a.SubjectDescription != null ){
        this.SubjectDescription = new String( a.SubjectDescription );
      }
      if( a.Modifier != null ){
        this.Modifier = new String( a.Modifier );
      }
      if( a.ModifierDescription != null ){
        this.ModifierDescription = new String( a.ModifierDescription );
      }
      if( a.Predicate != null ){
        this.Predicate = new String( a.Predicate );
      }
      if( a.PredicateDescription != null ){
        this.PredicateDescription = new String( a.PredicateDescription );
      }
      /*
              if( a.Object != null ){
                this.Object = new String( a.Object );
              }
              if( a.ObjectDescription != null ){
                this.ObjectDescription = new String( a.ObjectDescription );
              }
       */
      if( a.ParentNode != null ){
        // The same is used everywhere
        this.ToulminType = a.ParentNode.ToulminType;
      }
      this.LinkType = new MyArrayList();
    }
  }

  ConstructedStatement( ConstructedStatement a ) throws Exception{
    if( a == null ){
      throw new Exception( "Object null in ConstructedStatement copy constructor " );
    } else{
      if( a.Subject != null ){
        this.Subject = new String( a.Subject );
      }
      if( a.SubjectDescription != null ){
        this.SubjectDescription = new String( a.
                                              SubjectDescription );
      }
      if( a.Modifier != null ){
        this.Modifier = new String( a.Modifier );
      }
      if( a.ModifierDescription != null ){
        this.ModifierDescription = new String( a.
                                               ModifierDescription );
      }
      if( a.Predicate != null ){
        this.Predicate = new String( a.Predicate );
      }
      if( a.PredicateDescription != null ){
        this.PredicateDescription = new String(
            a.PredicateDescription );
      }
      /*
              if( a.Object != null ){
                this.Object = new String( a.Object );
              }
              if( a.ObjectDescription != null ){
                this.ObjectDescription = new String( a.
                                                     ObjectDescription );
              }
       */
      if( a.LinkType == null ){
        this.LinkType = new MyArrayList();
      } else{
        this.LinkType = new MyArrayList( a.LinkType );
      }
    }
  }
};
