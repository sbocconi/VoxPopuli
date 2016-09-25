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

public class Person{
  public String Id;
  public String Description;
  public String HasAge;
  public String HasEducation;
  public String HasJob;
  public String HasOrigin;
  public String HasRace;
  public String HasReligion;
  public String HasGender;
  public int EthosScore;
  public Person(){};
  public Person( String Id, String Description, String HasAge, String HasEducation,
               String HasJob, String HasOrigin, String HasRace, String HasReligion, String HasGender ){
    if( Id != null ){
      this.Id = new String( Id );
    } else{
      this.Id = new String( "" );
    }
    if( Description != null ){
      this.Description = new String( Description );
    } else{
      this.Description = new String( "" );
    }
    if( HasAge != null ){
      this.HasAge = new String( HasAge );
    } else{
      this.HasAge = new String( "" );
    }
    if( HasEducation  != null ){
      this.HasEducation = new String( HasEducation );
    } else{
      this.HasEducation = new String( "" );
    }
    if( HasJob != null ){
      this.HasJob = new String( HasJob );
    } else{
      this.HasJob = new String( "" );
    }
    if( HasOrigin != null ){
      this.HasOrigin = new String( HasOrigin );
    } else{
      this.HasOrigin = new String( "" );
    }
    if( HasRace != null ){
      this.HasRace = new String( HasRace );
    } else{
      this.HasRace = new String( "" );
    }
    if( HasReligion != null ){
      this.HasReligion = new String( HasReligion );
    } else{
      this.HasReligion = new String( "" );
    }
    if( HasGender != null ){
      this.HasGender = new String( HasGender );
    } else{
      this.HasGender = new String( "" );
    }

  }

  public void SetEthosRating( int rating ){
    this.EthosScore = rating;
  }

  public Person( Person a ) throws Exception{
    if( a == null ){
      throw new Exception( "Object null in Interviewee copy constructor " );
    } else{
      if( a.Id != null ){
        this.Id = new String( a.Id );
      }
      if( a.Description != null ){
        this.Description = new String( a.Description );
      }
      this.EthosScore = a.EthosScore;
      if( HasAge != null ){
        this.HasAge = new String( HasAge );
      } else{
        this.HasAge = new String( "" );
      }
      if( a.HasEducation != null ){
        this.HasEducation = new String( a.HasEducation );
      } else{
        this.HasEducation = new String( "" );
      }
      if( a.HasJob != null ){
        this.HasJob = new String( a.HasJob );
      } else{
        this.HasJob = new String( "" );
      }
      if( a.HasOrigin != null ){
        this.HasOrigin = new String( a.HasOrigin );
      } else{
        this.HasOrigin = new String( "" );
      }
      if( a.HasRace != null ){
        this.HasRace = new String( a.HasRace );
      } else{
        this.HasRace = new String( "" );
      }
      if( a.HasReligion != null ){
        this.HasReligion = new String( a.HasReligion );
      } else{
        this.HasReligion = new String( "" );
      }
      if( a.HasGender != null ){
        this.HasGender = new String( a.HasGender );
      } else{
        this.HasGender = new String( "" );
      }
    }
  }
}
