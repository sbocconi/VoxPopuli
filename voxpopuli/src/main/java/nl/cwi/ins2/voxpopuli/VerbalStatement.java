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
// This class represents a statement read from the repository, differently
// from the constructed statements built by the engine
public class VerbalStatement{
  String Id;
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
  String[] theMediaItem;
  boolean Explicit;

  // This array contains object of type Link, i.e. Id of real statements
  Hashtable ConnectedStatements;
  ToulminNode ParentNode;
  String ClaimerId;
  int EthosRating;
  VerbalStatement(){};
  VerbalStatement( VerbalStatement a ) throws Exception{
    if( a == null ){
      throw new Exception( "Object null in VerbalStatement copy constructor " );
    } else{
      if( a.Id != null ){
        this.Id = new String( a.Id );
      }
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
      if( a.ClaimerId != null ){
        this.ClaimerId = new String( a.ClaimerId );
      }
      this.Explicit = a.Explicit;
      if( a.theMediaItem != null ){
        this.theMediaItem = new String[ a.theMediaItem.length ];
        for( int i=0; i<a.theMediaItem.length; i++ ){
          this.theMediaItem[i] = new String(a.theMediaItem[i]);
        }
      }
      if( a.ConnectedStatements != null ){
        this.ConnectedStatements = new Hashtable( a.ConnectedStatements );
      }
      if( a.ParentNode != null ){
        this.ParentNode = new ToulminNode( a.ParentNode );
      }

      this.EthosRating = a.EthosRating;

    }
  }
};
