package nl.cwi.ins2.voxpopuli;

/**
 * <p>Title: Vox Populi</p>
 *
 * <p>Description: Automatic Generation of Biased Video Documentaries</p>
 *
 * <p>Copyright: Copyleft: free to copy</p>
 *
 * <p>Company: CWI</p>
 *
 * @author Stefano Bocconi
 * @version 1.0
 */
public class Util{

  final static int BIGVALUE = 0xffff;
  final static int SMALLVALUE = -0xfffe;

  final static char[] SMALLSTRING = {'\u0000'};
  final static char[] BIGSTRING = {'\uffff'};

//  final static String SMALLSTRING = " ";
//  final static String BIGSTRING = "~~~~~~";

  
  public static int StrToInt( String s ){
    int n = 0;

    for( int i = 0; i < s.length(); i++ ){
      n = n * 10 + ( s.charAt( i ) - '0' );
    }
    return n;
  }

  public static long StrToLong( String s ){
    long n = 0;

    for( int i = 0; i < s.length(); i++ ){
      n = n * 10 + ( s.charAt( i ) - '0' );
    }
    return n;
  }

  public static int[] Order( double[] values, boolean crescent ){


    if( values != null ){
      int size = values.length;
      double[] copy = new double[size];
      int[] orderlist = new int[size];

      for( int j = 0; j < size; j++ ){
        copy[j] = values[j];
      }

      for( int j = 0; j < size; j++ ){

        if( crescent ){
          double min = BIGVALUE;
          for( int i = 0; i < size; i++ ){
            if( min > copy[i] ){
              min = copy[i];
              orderlist[j] = i;
            }
          }
          copy[orderlist[j]] = BIGVALUE;

        }else{
          double max = SMALLVALUE;
          for( int i = 0; i < size; i++ ){
            if( max < copy[i] ){
              max = copy[i];
              orderlist[j] = i;
            }
          }
          copy[orderlist[j]] = SMALLVALUE;
        }
      }

      return orderlist;
    }
    return null;
  }

  public static int[] Order( int[] values, boolean crescent ){


    if( values != null ){
      int size = values.length;
      double[] copy = new double[size];
      int[] orderlist = new int[size];

      for( int j = 0; j < size; j++ ){
        copy[j] = values[j];
      }

      for( int j = 0; j < size; j++ ){

        if( crescent ){
          double min = BIGVALUE;
          for( int i = 0; i < size; i++ ){
            if( min > copy[i] ){
              min = copy[i];
              orderlist[j] = i;
            }
          }
          copy[orderlist[j]] = BIGVALUE;

        }else{
          double max = SMALLVALUE;
          for( int i = 0; i < size; i++ ){
            if( max < copy[i] ){
              max = copy[i];
              orderlist[j] = i;
            }
          }
          copy[orderlist[j]] = SMALLVALUE;
        }
      }

      return orderlist;
    }
    return null;
  }


  public static int[] Order( String[] values, boolean crescent ){


    if( values != null ){
      int size = values.length;
      String[] copy = new String[size];
      int[] orderlist = new int[size];

      for( int j = 0; j < size; j++ ){
        copy[j] = new String( values[j] );
      }

      for( int j = 0; j < size; j++ ){

        if( crescent ){
          String min = new String( BIGSTRING );
          for( int i = 0; i < size; i++ ){
            if( min.compareTo( copy[i] ) > 0 ){
              min = new String( copy[i] );
              orderlist[j] = i;
            }
          }
          copy[orderlist[j]] = new String( BIGSTRING );

        }else{
          String max = new String( SMALLSTRING );
          for( int i = 0; i < size; i++ ){
            if( max.compareTo( copy[i] ) < 0 ){
              max = new String( copy[i] );
              orderlist[j] = i;
            }
          }
          copy[orderlist[j]] = new String( SMALLSTRING );
        }
      }

      return orderlist;
    }
    return null;
  }


  public static String ConvertToDate( long theDSec ){

    long Hours = theDSec / 36000;
    long Mins = ( theDSec - 36000 * Hours ) / 600;
    long Secs = ( theDSec - 36000 * Hours - Mins * 600 ) / 10;
    long DSecs = ( theDSec - 36000 * Hours - Mins * 600 - Secs * 10 );

    String SDate = ( ( Hours < 10 ) ? "0" + Hours : "" + Hours ) + ":" +
        ( ( Mins < 10 ) ? "0" + Mins : "" + Mins ) + ":" +
        ( ( Secs < 10 ) ? "0" + Secs : "" + Secs ) + "." + DSecs + "00";

    if( SDate.length() > 12 ){
      SDate.substring( 0, 12 );
    }
    return SDate;
  }

  public static String TimeDifference( String End, String Begin ){
    return( ConvertToDSec( End ) - ConvertToDSec( Begin ) ) / 10.0 + "";
  }

  public static long ConvertToDSec( String TimeString ){

    long DSecs = 0;

    try{
      long Hours = StrToInt( TimeString.substring( 0, 2 ) );
      long Mins = StrToInt( TimeString.substring( 3, 5 ) );
      long Secs = StrToInt( TimeString.substring( 6, 8 ) );
      long MSecs = StrToInt( TimeString.substring( 9, TimeString.length() ) );
      for( int i = 0; i < ( 3 - ( TimeString.length() - 9 ) ); i++ ){
        MSecs = MSecs * 10;
      }

      DSecs = MSecs / 100 + 10 * ( Secs + 60 * ( Mins + 60 * ( Hours ) ) );
    } catch( StringIndexOutOfBoundsException e ){
      System.err.println( "String not long enough: " + TimeString + ",error " +
                          e.toString() );
    }

    return DSecs;
  }

  public static int max( int a, int b ){
    return( a > b ? a : b );
  }

  public static int max( int a, int b, int c ){
    return( max( max( a, b ), c ) );
  }

  public static int min( int a, int b ){
    return( a < b ? a : b );
  }

  public static int min( int a, int b, int c ){
    return( min(min( a, b ), c ) );
  }

  public static long fact( long n ) throws Exception{
    if( n < 0 )throw new Exception( "Underflow error in factorial" );
    else if( n > 20 )throw new Exception( "Overflow error in factorial" );
    else if( n < 2 )return 1;
    else{
      long a = 2;
      for( long i = 3; i <= n; i++ ){
        a = a * i;
      }
      return a;
    }
  }

  public static boolean even( int a ){
    int b = a / 2;

    if( a == ( b * 2 ) ){
      return true;
    }
    return false;
  }
}

