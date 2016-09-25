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

public class UserModel{


  final static int veryImportant = 3;
  final static int important = 2;
  final static int excluded = -1;

  static int ExcludedFactorsTable = 0;
  static int ImportantFactorsTable = 1;
  static int VeryImportantFactorsTable = 2;

  static int NegativeReactionTable = 3;
  static int PositiveReactionTable = 4;

  String Id;
  String UserType;
  ArrayList ExcludedFactors;
  ArrayList ImportantFactors;
  ArrayList VeryImportantFactors;

  ArrayList NegativeReaction;
  ArrayList PositiveReaction;

  public UserModel( String Id, String usertype){

    this.Id = new String( Id );

    if( usertype != null ){
      this.UserType = new String( usertype );
    }

    this.ExcludedFactors = new ArrayList();
    this.ImportantFactors = new ArrayList();
    this.VeryImportantFactors = new ArrayList();
    this.NegativeReaction = new ArrayList();
    this.PositiveReaction = new ArrayList();

  }

  public boolean AddFactor( int table, String Factor ){

    if( Factor == null ){
      return false;
    }

    ArrayList[] Pool = {this.ExcludedFactors, this.ImportantFactors, this.VeryImportantFactors,
        this.NegativeReaction, this.PositiveReaction};

    Pool[table].add(Factor);

    return true;
  }

}
