package nl.cwi.ins2.voxpopuli;

import java.io.PrintStream;
import java.util.Calendar;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

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

public class Outputs{

  final private String DevNull = "/dev/null";
//  final private String DevNull = "nul";

  private class MyPrintStream extends PrintStream{

    MyPrintStream( OutputStream out ){
      super( out );
    }

    MyPrintStream( PrintStream prt ){
      super( prt );
    }

    public void println( String a ){

      String day, month;
      Calendar rightNow = Calendar.getInstance();

      switch( rightNow.get( Calendar.DAY_OF_WEEK ) ){
        case Calendar.SUNDAY:
          day = "Sun";
          break;
        case Calendar.MONDAY:
          day = "Mon";
          break;
        case Calendar.TUESDAY:
          day = "Tue";
          break;
        case Calendar.WEDNESDAY:
          day = "Wed";
          break;
        case Calendar.THURSDAY:
          day = "Thu";
          break;
        case Calendar.FRIDAY:
          day = "Fri";
          break;
        case Calendar.SATURDAY:
          day = "Sat";
          break;
        default:
          day = "";
          break;
      }

      String time = new String( "Time: " +

                                ( rightNow.get( Calendar.HOUR_OF_DAY ) > 9 ?
                                  "" : "0" ) + rightNow.get( Calendar.HOUR_OF_DAY ) + ":" +
                                ( rightNow.get( Calendar.MINUTE ) > 9 ?
                                  "" : "0" ) + rightNow.get( Calendar.MINUTE ) + ":" +
                                ( rightNow.get( Calendar.SECOND ) > 9 ?
                                  "" : "0" ) + rightNow.get( Calendar.SECOND ) + ", " +
                                day + " " +
                                ( rightNow.get( Calendar.DAY_OF_MONTH ) > 9 ?
                                  "" : "0" ) + rightNow.get( Calendar.DAY_OF_MONTH ) + " " +
                                ( rightNow.get( Calendar.MONTH ) > 8 ?
                                  "" : "0" ) + ( rightNow.get( Calendar.MONTH ) + 1 ) + " " +
                                rightNow.get( Calendar.YEAR ) + " - "
          );

      String b = new String( time + a );

      super.println( b );
    }

  }

  public void SetOutputStreams( PrintStream aQuery, PrintStream aDebug1, PrintStream aDebug2,
                                PrintStream aLog1, PrintStream aStatementLog, PrintStream aStatementStat,
                                PrintStream aLocator, PrintStream aResultOut, PrintStream aErr ) throws
      FileNotFoundException{

    if( aQuery != null ){
      Query = new MyPrintStream( aQuery );
    } else{
      Query = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aDebug1 != null ){
      Debug1 = new MyPrintStream( aDebug1 );
    } else{
      Debug1 = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aDebug2 != null ){
      Debug2 = new MyPrintStream( aDebug2 );
    } else{
      Debug2 = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aLog1 != null ){
      Log1 = new MyPrintStream( aLog1 );
    } else{
      Log1 = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aStatementLog != null ){
      StatementLog = new MyPrintStream( aStatementLog );
    } else{
      StatementLog = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aStatementStat != null ){
      StatementStat = new PrintStream( aStatementStat );
    } else{
      StatementStat = new PrintStream( new FileOutputStream( new File( DevNull ) ) );
    }

    if( aLocator != null ){
      Locator = new MyPrintStream( aLocator );
    } else{
      Locator = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aResultOut != null ){
      ResultOut = new MyPrintStream( aResultOut );
    } else{
      ResultOut = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }
    if( aErr != null ){
      Err = new MyPrintStream( aErr );
    } else{
      Err = new MyPrintStream( new FileOutputStream( new File( DevNull ) ) );
    }

  }

  public MyPrintStream Query = null;
  public MyPrintStream Debug1 = null;
  public MyPrintStream Debug2 = null;
  public MyPrintStream ResultOut = null;
  public MyPrintStream Locator = null;
  public MyPrintStream Log1 = null;
  public MyPrintStream StatementLog = null;
  public PrintStream StatementStat = null;
  public MyPrintStream Err = null;

  public Outputs() throws FileNotFoundException{
    SetOutputStreams( null, null, null, null, null, null, null, null, null );
  }

  public void PrintTemp( PrintStream prt, String msg ){
    MyPrintStream a = new MyPrintStream( prt );
    a.println( msg );
  }

  public void PrintLn( MyPrintStream a, String msg ){
    a.println( msg );
  }

  public void Print( MyPrintStream a, String msg ){
    a.print( msg );
  }

  public void PrintLn( PrintStream a, String msg ){
    a.println( msg );
  }

  public void PrintLn( PrintStream a ){
    a.println();
  }

  public void Print( PrintStream a, String msg ){
    a.print( msg );
  }

}
