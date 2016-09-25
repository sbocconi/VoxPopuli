package nl.cwi.ins2.voxpopuli;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.FileOutputStream;

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
public class SMILMedia
{
    
    /*********************
     *  CONSTANTS *
     **********************/
    
// parameters for the smil file
    
    static private final int Width = 640;
    static private final int Height = 480;
    
    static private final String TitleFileName = "Sigla_225.rm";
    static private final int TitleWidth = 320;
    static private final int TitleHeight = 240;
    static private final String TitleBeginFrame = "00:00:00.000";
    static private final String TitleEndFrame = "00:00:03.000";
    static private final String TitleTransIn = null;
    static private final String TitleTransOut = null;
    
    // height for the caption area
    static private final int STHeight = 60;
    
    // height for the link area
    static private final int LinkHeight = 30;
    
    // parameters for the transition in SMIL
    static private final String TransDur = "0.5s";
    static private final String StartProgress = "0.0";
    static private final String EndProgress = "1.0";
    static private final String Direction = "forward";
    static private final String TransName = "trans";
    static private final String TransType = "fade";
    static private final String TransSubtype = "";
    static private final String Extension = ".rm";
    
    // Do we use Real extensions?
    private final boolean RealExt = false;
    
    static private final String RealTextServer =
            "http://media.cwi.nl/cocoon/cuypers/oai/text?Content=";
    
    static private final String VPImagesource = "Yellow.jpg";
    static private final String ATTACKImagesource = "Red.jpg";
    static private final String SUPPORTImagesource = "Green.jpg";
    static private final String LinkBG = "000000";
    
    static private final String RealTextFile = "RealText";
    //static private final String WebLocation = "C:/Documents and Settings/sbocconi/.netbeans/5.5/apache-tomcat-5.5.17_base/webapps/cocoon/VP/video/";
    
    // typography
    static private final int FontSize = 4;
    static private final String FgColor = "ffffff";
    static private final String BgColor = "000000";
    static private final String TextAlign = "center";
    static private final String TextFont = "Arial";
    
    /*********************
     *  CLASSES *
     **********************/
    // This is a par of all single sequences
    private class SMILMediaItem
    {
        SMILMediaElement[] VideoSegments;
        SMILMediaElement[] AudioSegments;
        SMILMediaElement[] StillSegments;
        SMILMediaElement[] TextSegments;
    }
    
    // an element like it would be present in a seq
    private class SMILMediaElement
    {
        private String Type;
        private int Priority;
        private String BeginFrame;
        private String EndFrame;
        private String FileName;
        private String Description;
        private String Language;
        private String TransIn;
        private String TransOut;
        private String Start;
        private String Stop;
        private String Link;
    }
    
    /*********************
     *  VARIABLES *
     **********************/
    
    //private ArrayList MediaArray = null; // List of Media to show
    private ArrayList SMILMediaArray = null; // List of Media to show
    
    private Util u;
    private Outputs P;
    
    private String VideoQuality = "";
    
    private String VideoLocation = "";
    private String StillLocation = "";
    private String AudioLocation = "";
    private String TextLocation = "";
    
    private String theSMILURL = null;
    
    private String EffTextLocation = "";
    
    private String Repository = "";
    
    private PrintWriter OutFile = null;
    
    private boolean CaptionToFile;
    
    /*********************
     *  FUNCTIONS *
     **********************/
    
    public SMILMedia( String Quality, String theVideoLocation,
            String theAudioLocation,
            String theStillLocation,
            String theTextLocation,
            String Rep, String OutFilename,
            boolean toFile, ByteArrayOutputStream ba,
            boolean captionToFile, Outputs p ) throws java.io.FileNotFoundException
    {
        
        u = new Util();
        P = new Outputs();
        Repository = new String( Rep );
        P = p;
        
        CaptionToFile = captionToFile;
        
        SetMediaLocation( Quality, theVideoLocation, theStillLocation,
                theAudioLocation, theTextLocation );
        
        SetOutputFile( OutFilename, toFile, ba );
        
    }
    
    public void SetSMILURL( String URL, String Stage, String Videolocation, String Audiolocation,
            String Stilllocation, String Textlocation, String Repository, String RDFLocation,
            String DomainNS, String Local, String Curlang, String Strategy,
            String Intercut, String Quality, String Caption, String[] Classes, String[][] parameters1,
            String[][] parameters2 ) throws Exception
    {
        
        StringBuffer theClasses = new StringBuffer();
        
        if( Classes != null )
        {
            for( int i = 0; i < Classes.length; i++ )
            {
                if( parameters1[i] != null )
                {
                    for( int j = 0; j < parameters1[i].length; j++ )
                    {
                        theClasses.append( "&amp;" + Classes[i] + "1=" + URLEncoder.encode( parameters1[i][j], "UTF-8" ) );
                    }
                }
                if( parameters2[i] != null )
                {
                    for( int j = 0; j < parameters2[i].length; j++ )
                    {
                        theClasses.append( "&amp;" + Classes[i] + "2=" + URLEncoder.encode( parameters2[i][j], "UTF-8" ) );
                    }
                }
            }
        }
        
        theSMILURL = new String(
                URL + "?" +
                "stage=" + URLEncoder.encode( Stage, "UTF-8" ) + "&amp;" +
                "videolocation=" + URLEncoder.encode( Videolocation, "UTF-8" ) + "&amp;" +
                "audiolocation=" + URLEncoder.encode( Audiolocation, "UTF-8" ) + "&amp;" +
                "stilllocation=" + URLEncoder.encode( Stilllocation, "UTF-8" ) + "&amp;" +
                "textlocation=" + URLEncoder.encode( Textlocation, "UTF-8" ) + "&amp;" +
                "repository=" + URLEncoder.encode( Repository, "UTF-8" ) + "&amp;" +
                "RDFLocation=" + URLEncoder.encode( RDFLocation, "UTF-8" ) + "&amp;" +
                "domainNS=" + URLEncoder.encode( DomainNS, "UTF-8" ) + "&amp;" +
                "local=" + URLEncoder.encode( Local, "UTF-8" ) + "&amp;" +
                "curlang=" + URLEncoder.encode( Curlang, "UTF-8" ) + "&amp;" +
                "intercut=" + URLEncoder.encode( Intercut, "UTF-8" ) + "&amp;" +
                "quality=" + URLEncoder.encode( Quality, "UTF-8" ) + "&amp;" +
                "caption=" + URLEncoder.encode( Caption, "UTF-8" ) +
                theClasses.toString()
                );
        
    }
    
    public void SetOutputFile( String OutFilename, boolean toFile,
            ByteArrayOutputStream ba )
    {
        try
        {
            if( toFile == true )
            {
                if( !OutFilename.equals( "out" ) )
                {
                    // Add the correct extension
                    OutFilename = new String( OutFilename + ".smil" );
                    
                    OutFile = new PrintWriter( new FileOutputStream( OutFilename ) );
                    // write it to standard out
                }
                else
                {
                    OutFile = new PrintWriter( System.out );
                }
            }
            else
            {
                OutFile = new PrintWriter( ba );
            }
            
        }
        catch( IOException e )
        {
            P.PrintLn( P.Err, "Error opening file " + e.toString() );
            e.printStackTrace(P.Err);
        }
        return ;
    }
    
    public boolean SetMediaLocation( String Quality, String theVideoLocation,
            String theStillLocation,
            String theAudioLocation,
            String theTextLocation )
    {
        VideoLocation = theVideoLocation;
        VideoQuality = Quality;
        
        if( theStillLocation == null )
        {
            StillLocation = new String( VideoLocation );
        }
        else
        {
            StillLocation = new String( theStillLocation );
        }
        
        if( theAudioLocation == null )
        {
            AudioLocation = new String( VideoLocation );
        }
        else
        {
            AudioLocation = new String( theAudioLocation );
        }
        
        if( theTextLocation == null )
        {
            TextLocation = new String( VideoLocation );
        }
        else
        {
            TextLocation = new String( theTextLocation );
        }
       
        EffTextLocation = new String( TextLocation.replaceFirst("http://media.cwi.nl/IWA","/export/data2/media/video/IWA"));
        return true;
    }
    
    // This function transforms an array of media in an array of layered media
    // for SMIL
    public boolean DoMontage( ArrayList MediaArray ) throws Exception
    {
        
        boolean result = true;
        long AudioLength = 0;
        long VideoLength = 0;
        long DLength = 0;
        
        P.PrintLn( P.Locator, "Doing Montage" );
        
        if( MediaArray == null )
        {
            return false;
        }
        
        SMILMediaArray = new ArrayList();
        
        for( int i = 0; i < MediaArray.size(); i++ )
        {
            
            DLength = 0;
            AudioLength = 0;
            VideoLength = 0;
            
            MediaItem a = ( MediaItem )MediaArray.get( i );
            // There must be a dynamic media here otherwise we
            // can not estabilish the duration
            if( a.AudioSegments == null && a.VideoSegments == null )
            {
                //new Exception( "No dynamic media in Media Item " );
                //return false;
                continue;
            }
            if( a.AudioSegments != null )
            {
                for( int ii = 0; ii < a.AudioSegments.size(); ii++ )
                {
                    AudioLength = AudioLength + ( u.ConvertToDSec( ( ( AudioSegment )a.AudioSegments.get( ii ) ).EndFrame ) -
                            u.ConvertToDSec( ( ( AudioSegment )a.AudioSegments.get( ii ) ).BeginFrame ) );
                }
            }
            if( a.VideoSegments != null )
            {
                for( int ii = 0; ii < a.VideoSegments.size(); ii++ )
                {
                    VideoLength = VideoLength + ( u.ConvertToDSec( ( ( VideoSegment )a.VideoSegments.get( ii ) ).EndFrame ) -
                            u.ConvertToDSec( ( ( VideoSegment )a.VideoSegments.get( ii ) ).BeginFrame ) );
                }
            }
            DLength = ( AudioLength > VideoLength ? AudioLength : VideoLength );
            
            SMILMediaItem MediaList = new SMILMediaItem();
            
            if( a.TextSegments != null )
            {
                MediaList.TextSegments = new SMILMediaElement[a.TextSegments.size()];
                for( int iii = 0; iii < a.TextSegments.size(); iii++ )
                {
                    TextSegment item = ( ( TextSegment )a.TextSegments.get( iii ) );
                    MediaList.TextSegments[iii] = new SMILMediaElement();
                    MediaList.TextSegments[iii].Type = new String( "text" );
                    MediaList.TextSegments[iii].Priority = 3;
                    MediaList.TextSegments[iii].Start = new String( u.ConvertToDate( DLength / a.TextSegments.size() * iii ) );
                    
                    MediaList.TextSegments[iii].Stop = new String( u.ConvertToDate( DLength / a.TextSegments.size() * ( iii + 1 ) ) );
                    
                    MediaList.TextSegments[iii].Language = new String( item.Language );
                    MediaList.TextSegments[iii].FileName = new String( TextLocation + item.FileName );
                    MediaList.TextSegments[iii].Description = new String( item.Description );
                    if( item.NeedPreTrans == true )
                    {
                        MediaList.TextSegments[iii].TransIn = new String(TransName);
                    }
                    else
                    {
                        MediaList.TextSegments[iii].TransIn = null;
                    }
                    MediaList.TextSegments[iii].TransOut = null;
                    
                }
            }
            if( a.StillSegments != null )
            {
                MediaList.StillSegments = new SMILMediaElement[a.StillSegments.size()];
                for( int iii = 0; iii < a.StillSegments.size(); iii++ )
                {
                    StillSegment item = ( ( StillSegment )a.StillSegments.get( iii ) );
                    MediaList.StillSegments[iii] = new SMILMediaElement();
                    MediaList.StillSegments[iii].Type = new String( "img" );
                    MediaList.StillSegments[iii].Priority = 2;
                    MediaList.StillSegments[iii].Start = new String( u.ConvertToDate( DLength / a.StillSegments.size() * iii ) );
                    
                    MediaList.StillSegments[iii].Stop = new String( u.ConvertToDate( DLength / a.StillSegments.size() * ( iii + 1 ) ) );
                    
                    MediaList.StillSegments[iii].Language = new String( item.Language );
                    MediaList.StillSegments[iii].FileName = new String( StillLocation + item.FileName );
                    MediaList.StillSegments[iii].Description = new String( item.Description );
                    if( item.NeedPreTrans == true )
                    {
                        MediaList.StillSegments[iii].TransIn = new String(TransName);
                    }
                    else
                    {
                        MediaList.StillSegments[iii].TransIn = null;
                    }
                    MediaList.StillSegments[iii].TransOut = null;
                    
                }
            }
            if( a.AudioSegments != null )
            {
                MediaList.AudioSegments = new SMILMediaElement[a.AudioSegments.size()];
                for( int iii = 0; iii < a.AudioSegments.size(); iii++ )
                {
                    AudioSegment item = ( ( AudioSegment )a.AudioSegments.get( iii ) );
                    MediaList.AudioSegments[iii] = new SMILMediaElement();
                    MediaList.AudioSegments[iii].Type = new String( "audio" );
                    MediaList.AudioSegments[iii].Priority = 1;
                    if( iii == 0 )
                    {
                        MediaList.AudioSegments[iii].Start = new String( "00:00:00.000" );
                    }
                    else
                    {
                        MediaList.AudioSegments[iii].Start = new String( MediaList.AudioSegments[iii - 1].Stop );
                    }
                    long DStop = u.ConvertToDSec( ( ( AudioSegment )a.AudioSegments.get( iii ) ).EndFrame ) -
                            u.ConvertToDSec( ( ( AudioSegment )a.AudioSegments.get( iii ) ).BeginFrame ) +
                            u.ConvertToDSec( MediaList.AudioSegments[iii].Start );
                    MediaList.AudioSegments[iii].Stop = new String( u.ConvertToDate( DStop ) );
                    
                    MediaList.AudioSegments[iii].Language = new String( item.Language );
                    MediaList.AudioSegments[iii].FileName = new String( AudioLocation + item.FileName );
                    MediaList.AudioSegments[iii].Description = new String( item.Description );
                    MediaList.AudioSegments[iii].BeginFrame = new String( item.BeginFrame );
                    MediaList.AudioSegments[iii].EndFrame = new String( item.EndFrame );
                    if( item.NeedPreTrans == true )
                    {
                        MediaList.AudioSegments[iii].TransIn = new String( TransName );
                    }
                    else
                    {
                        MediaList.AudioSegments[iii].TransIn = null;
                    }
                    MediaList.AudioSegments[iii].TransOut = null;
                }
            }
            if( a.VideoSegments != null )
            {
                MediaList.VideoSegments = new SMILMediaElement[a.VideoSegments.size()];
                for( int iii = 0; iii < a.VideoSegments.size(); iii++ )
                {
                    VideoSegment item = ( ( VideoSegment )a.VideoSegments.get( iii ) );
                    MediaList.VideoSegments[iii] = new SMILMediaElement();
                    MediaList.VideoSegments[iii].Type = new String( "video" );
                    MediaList.VideoSegments[iii].Priority = 0;
                    
                    if( iii == 0 )
                    {
                        MediaList.VideoSegments[iii].Start = new String( "00:00:00.000" );
                    }
                    else
                    {
                        MediaList.VideoSegments[iii].Start = new String( MediaList.VideoSegments[iii - 1].Stop );
                    }
                    
                    long DStop = u.ConvertToDSec( ( ( VideoSegment )a.VideoSegments.get( iii ) ).EndFrame ) -
                            u.ConvertToDSec( ( ( VideoSegment )a.VideoSegments.get( iii ) ).BeginFrame ) +
                            u.ConvertToDSec( MediaList.VideoSegments[iii].Start );
                    
                    MediaList.VideoSegments[iii].Stop = new String( u.ConvertToDate( DStop ) );
                    
                    MediaList.VideoSegments[iii].Language = new String( item.Language );
                    MediaList.VideoSegments[iii].FileName = new String( VideoLocation + item.FileName );
                    MediaList.VideoSegments[iii].Description = new String( item.Description );
                    MediaList.VideoSegments[iii].BeginFrame = new String( item.BeginFrame );
                    MediaList.VideoSegments[iii].EndFrame = new String( item.EndFrame );
                    if( item.NeedPreTrans == true )
                    {
                        MediaList.VideoSegments[iii].TransIn = new String( TransName );
                    }
                    else
                    {
                        MediaList.VideoSegments[iii].TransIn = null;
                    }
                    MediaList.VideoSegments[iii].TransOut = null;
                    if( item.InterviewId != null && theSMILURL != null )
                    {
                        MediaList.VideoSegments[iii].Link = new String(
//                URLEncoder.encode( theSMILURL + "&amp;Interviews=" + item.InterviewId, "UTF-8" ) );
                                theSMILURL + "&amp;" + "Interviews=" + URLEncoder.encode( (String)item.InterviewId.get(0), "UTF-8" ) );
                    }
                    else
                    {
                        MediaList.VideoSegments[iii].Link = null;
                    }
                }
            }
            SMILMediaArray.add( MediaList );
        }
        
        return result;
    }
    
    public boolean CreateSMILOutput( boolean Captions ) throws
            Exception
    {
        
        P.PrintLn( P.Locator, "Creating SMIL file" );
        
        OutFile.print(
                "<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>\n" +
                "<smil   xmlns=\"http://www.w3.org/2001/SMIL20/Language\" " +
                ( RealExt == true ?
                    "xmlns:rn=\"http://features.real.com/2001/SMIL20/Extensions\">\n" : ">\n" ) +
                "<head>\n" +
                "<layout>\n" +
                "<regPoint id=\"middle\" top=\"50%\" left=\"50%\" regAlign=\"center\" />\n" +
                "<root-layout width=\"" + Width + "\" height=\"" + (Captions? ( Height + STHeight + LinkHeight ):
                    ( Height + LinkHeight )) +
                "\"/>\n" );
        
        if( !VideoQuality.equals( "_20" ) )
        {
            OutFile.print( "<region id=\"media-region0\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"0\" fit=\"meet\"/>\n" +
                    "<region id=\"media-region1\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"1\" fit=\"meet\"/>\n" +
                    "<region id=\"media-region2\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"2\" fit=\"meet\"/>\n" +
                    "<region id=\"media-region3\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"3\" fit=\"meet\"/>\n" );
            
        }
        else
        {
            OutFile.print( "<region id=\"media-region0\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"0\" />\n" +
                    "<region id=\"media-region1\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"1\" />\n" +
                    "<region id=\"media-region2\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"2\" />\n" +
                    "<region id=\"media-region3\" left=\"0\" top=\"0\" width=\"" + Width +
                    "\" height=\"" + Height + "\" z-index=\"3\" />\n" );
            
        }
        OutFile.print(
                "<region id=\"title-region\" left=\"" + ( Width - TitleWidth ) / 2 + "\" top=\"" + ( Height - TitleHeight ) / 2 +
                "\" width=\"" + TitleWidth + "\" height=\"" + TitleHeight + "\"/>\n" +
                ( Captions ?
                    "<region id=\"caption-region0\" left=\"0\" top=\"" + Height +
                "\" width=\"" + Width +
                "\" height=\"" + STHeight + "\" z-index=\"0\"/>\n" +
                "<region id=\"caption-region1\" left=\"1\" top=\"" + Height +
                "\" width=\"" + Width +
                "\" height=\"" + STHeight + "\" z-index=\"1\"/>\n" +
                "<region id=\"caption-region2\" left=\"2\" top=\"" + Height +
                "\" width=\"" + Width +
                "\" height=\"" + STHeight + "\" z-index=\"2\"/>\n" +
                "<region id=\"caption-region3\" left=\"3\" top=\"" + Height +
                "\" width=\"" + Width +
                "\" height=\"" + STHeight + "\" z-index=\"3\"/>\n"
                : "" ) +
                "<region id=\"link-region0\" backgroundColor=\"#" + LinkBG + "\" left=\"0\" top=\"" +
                (Captions? ( Height + STHeight ):  Height )  +
                "\" width=\"" + Width / 3 + "\" height=\"" + LinkHeight + "\" z-index=\"0\"/>\n" +
                "<region id=\"link-region1\" backgroundColor=\"#" + LinkBG + "\" left=\"" + Width * 1 / 3 + "\" top=\"" +
                (Captions? ( Height + STHeight ):  Height )  +
                "\" width=\"" + Width / 3 + "\" height=\"" + LinkHeight + "\" z-index=\"0\"/>\n" +
                "<region id=\"link-region2\" backgroundColor=\"#" + LinkBG + "\" left=\"" + Width * 2 / 3 + "\" top=\"" +
                (Captions? ( Height + STHeight ):  Height )  +
                "\" width=\"" + Width / 3 + "\" height=\"" + LinkHeight + "\" z-index=\"0\"/>\n" +
                "</layout>\n" +
                "<transition id=\"" + TransName + "\" type=\"" + TransType + "\"" +
                ( !( TransSubtype.equals( "" ) ) ? " subtype=\"" + TransSubtype + "\"" : "" ) +
                " dur=\"" + TransDur + "\"" +
                " startProgress=\"" + StartProgress + "\"" +
                " endProgress=\"" + EndProgress + "\"" +
                " direction=\"" + Direction + "\"" +
                "/>" +
                "</head>\n" +
                "<body>\n" +
                "<seq>\n" );
        // The title (workaround RealOne problem)
        if( TitleFileName != null && Repository.equals( "IWA" ) )
        {
            OutFile.print( "<video region=\"title-region\" src=\"" + VideoLocation +
                    TitleFileName +
                    "\" " +
                    "clipBegin=\"" + TitleBeginFrame + "\" " +
                    "clipEnd=\"" + TitleEndFrame + "\" " +
                    "clip-begin=\"" + TitleBeginFrame +
                    "\" " +
                    "clip-end=\"" + TitleEndFrame + "\" " +
                    ( TitleTransIn != null ? "transIn=\"" + TitleTransIn + "\" " : "" ) +
                    ( TitleTransOut != null ? "transOut=\"" + TitleTransOut + "\" " : "" ) +
                    "fill=\"transition\" />\n" );
        }
        
        if( !CreateSegmentSequence( Captions, OutFile ) )
        {
            throw new Exception( "Error creating SMIL File Segment Sequence " );
        }
        
        OutFile.print( "</seq>\n" + "</body>\n" + "</smil>\n" );
        OutFile.flush();
        
        return true;
    }
    
    private boolean CreateSegmentSequence( boolean Captions, PrintWriter OutFile )
    {
        
        P.PrintLn( P.Locator, "Creating SMIL sequence" );
        
        if( SMILMediaArray == null )
        {
            return true;
        }
        
        for( int i = 0; i < SMILMediaArray.size(); i++ )
        {
            
            OutFile.print( "<par>\n" );
            SMILMediaItem a = ( SMILMediaItem )SMILMediaArray.get( i );
            
            if( a.TextSegments != null && a.TextSegments.length > 0 )
            {
                
                if( a.TextSegments.length > 1 )
                {
                    OutFile.print( "<seq>\n" );
                }
                for( int ii = 0; ii < a.TextSegments.length; ii++ )
                {
                    SMILMediaElement b = a.TextSegments[ii];
                    
                    String STDuration = u.TimeDifference( b.Stop, b.Start );
                    OutFile.print( "<par>\n" );
                    if( Captions )
                    {
                        try
                        {
                            
                            OutFile.print( "<text region=\"caption-region" + b.Priority + "\" src=\"" +
                                    SourceText( b.Description, STDuration, CaptionToFile ) + "\" " +
                                    "begin=\"0\" dur=\"" + STDuration + "\" " +
                                    ( b.TransIn != null ? "transIn=\"" + b.TransIn + "\" " : "" ) +
                                    ( b.TransOut != null ? "transOut=\"" + b.TransOut + "\" " : "" ) +
                                    ">\n" +
                                    "<param name=\"vAlign\" value=\"" + TextAlign + "\"/>\n" +
                                    "</text>\n" );
                        }
                        catch( UnsupportedEncodingException e )
                        {
                            P.PrintLn( P.Err, "Error URL encoding string  " + e.toString() );
                        }
                    }
                    OutFile.print( "<" + b.Type + " region=\"media-region" + b.Priority + "\" src=\"" +
                            b.FileName + "\" " +
                            "begin=\"0\" " +
                            "dur=\"" + STDuration + "\" " +
                            ( b.TransIn != null ?
                                "transIn=\"" + b.TransIn + "\" " : "" ) +
                            ( b.TransOut != null ?
                                "transOut=\"" + b.TransOut + "\" " : "" ) +
                            " regPoint=\"middle\"" +
                            " />\n" );
                    OutFile.print( "</par>\n" );
                }
                if( a.TextSegments.length > 1 )
                {
                    OutFile.print( "</seq>\n" );
                }
            }
            if( a.StillSegments != null && a.StillSegments.length > 0 )
            {
                
                if( a.StillSegments.length > 1 )
                {
                    OutFile.print( "<seq>\n" );
                }
                
                for( int ii = 0; ii < a.StillSegments.length; ii++ )
                {
                    SMILMediaElement b = a.StillSegments[ii];
                    
                    String STDuration = u.TimeDifference( b.Stop, b.Start );
                    
                    OutFile.print( "<par>\n" );
                    if( Captions )
                    {
                        try
                        {
                            OutFile.print( "<text region=\"caption-region" + b.Priority + "\" src=\"" +
                                    SourceText( b.Description, STDuration, CaptionToFile ) + "\" " +
                                    "begin=\"0\" dur=\"" + STDuration + "\" " +
                                    ( b.TransIn != null ? "transIn=\"" + b.TransIn + "\" " : "" ) +
                                    ( b.TransOut != null ? "transOut=\"" + b.TransOut + "\" " : "" ) +
                                    ">\n" +
                                    "<param name=\"vAlign\" value=\"" + TextAlign + "\"/>\n" +
                                    "</text>\n" );
                        }
                        catch( UnsupportedEncodingException e )
                        {
                            P.PrintLn( P.Err, "Error URL encoding string  " + e.toString() );
                        }
                    }
                    OutFile.print( "<" + b.Type + " region=\"media-region" + b.Priority + "\" src=\"" +
                            b.FileName + "\" " +
                            "begin=\"0\" " +
                            "dur=\"" + STDuration + "\" " +
                            ( b.TransIn != null ?
                                "transIn=\"" + b.TransIn + "\" " : "" ) +
                            ( b.TransOut != null ?
                                "transOut=\"" + b.TransOut + "\" " : "" ) +
                            " regPoint=\"middle\"" +
                            " />\n" );
                    OutFile.print( "</par>\n" );
                }
                if( a.StillSegments.length > 1 )
                {
                    OutFile.print( "</seq>\n" );
                }
            }
            if( a.AudioSegments != null && a.AudioSegments.length > 0 )
            {
                
                if( a.AudioSegments.length > 1 )
                {
                    OutFile.print( "<seq>\n" );
                }
                
                for( int ii = 0; ii < a.AudioSegments.length; ii++ )
                {
                    SMILMediaElement b = a.AudioSegments[ii];
                    
                    String STDuration = u.TimeDifference( b.Stop, b.Start );
                    
                    OutFile.print( "<par>\n" );
                    if( Captions )
                    {
                        try
                        {
                            OutFile.print( "<text region=\"caption-region" + b.Priority + "\" src=\"" +
                                    SourceText( b.Description, STDuration, CaptionToFile ) + "\"" +
                                    "begin=\"0\" dur=\"" + STDuration + "\" " +
                                    ( b.TransIn != null ? "transIn=\"" + b.TransIn + "\" " : "" ) +
                                    ( b.TransOut != null ? "transOut=\"" + b.TransOut + "\" " : "" ) +
                                    ">\n" +
                                    "<param name=\"vAlign\" value=\"" + TextAlign + "\"/>\n" +
                                    "</text>\n" );
                        }
                        catch( UnsupportedEncodingException e )
                        {
                            P.PrintLn( P.Err, "Error URL encoding string  " + e.toString() );
                        }
                    }
                    OutFile.print( "<" + b.Type + " region=\"media-region" + b.Priority + "\" src=\"" +
                            b.FileName + "\" " +
                            "clipBegin=\"" + b.BeginFrame + "\" " +
                            "clipEnd=\"" + b.EndFrame + "\" " +
                            "clip-begin=\"" + b.BeginFrame +
                            "\" " +
                            "clip-end=\"" + b.EndFrame + "\" " +
                            "begin=\"0\" " +
                            "dur=\"" + STDuration + "\" " +
                            ( b.TransIn != null ?
                                "transIn=\"" + b.TransIn + "\" " : "" ) +
                            ( b.TransOut != null ?
                                "transOut=\"" + b.TransOut + "\" " : "" ) +
                            " />\n" );
                    OutFile.print( "<par>\n" );
                }
                if( a.AudioSegments.length > 1 )
                {
                    OutFile.print( "</seq>\n" );
                }
            }
            if( a.VideoSegments != null && a.VideoSegments.length > 0 )
            {
                
                if( a.VideoSegments.length > 1 )
                {
                    OutFile.print( "<seq>\n" );
                }
                
                for( int ii = 0; ii < a.VideoSegments.length; ii++ )
                {
                    SMILMediaElement b = a.VideoSegments[ii];
                    
                    String STDuration = u.TimeDifference( b.Stop, b.Start );
                    
                    OutFile.print( "<par>\n" );
                    if( Captions )
                    {
                        try
                        {
                            OutFile.print( "<text region=\"caption-region" + b.Priority + "\" src=\"" +
                                    SourceText( b.Description, STDuration, CaptionToFile ) + "\" " +
                                    "begin=\"0\" dur=\"" + STDuration + "\" " +
                                    ( b.TransIn != null ? "transIn=\"" + b.TransIn + "\" " : "" ) +
                                    ( b.TransOut != null ? "transOut=\"" + b.TransOut + "\" " : "" ) +
                                    ">\n" +
                                    "<param name=\"vAlign\" value=\"" + TextAlign + "\"/>\n" +
                                    "</text>\n" );
                        }
                        catch( UnsupportedEncodingException e )
                        {
                            P.PrintLn( P.Err, "Error URL encoding string  " + e.toString() );
                        }
                    }
                    OutFile.print( "<" + b.Type + " region=\"media-region" + b.Priority + "\" src=\"" +
                            b.FileName + VideoQuality + Extension + "\" " +
                            "clipBegin=\"" + b.BeginFrame + "\" " +
                            "clipEnd=\"" + b.EndFrame + "\" " +
                            "clip-begin=\"" + b.BeginFrame +
                            "\" " +
                            "clip-end=\"" + b.EndFrame + "\" " +
                            "begin=\"0\" " +
                            "dur=\"" + STDuration + "\" " +
                            ( b.TransIn != null ?
                                "transIn=\"" + b.TransIn + "\" " : "" ) +
                            ( b.TransOut != null ?
                                "transOut=\"" + b.TransOut + "\" " : "" ) +
                            " regPoint=\"middle\"" +
                            "/>\n" );
                    
                    if( b.Link != null )
                    {
                        OutFile.print( "<a href=\"" + b.Link + "&amp;strategy=VP\" >\n" );
                        
                        OutFile.print( "<img region=\"link-region0\" src=\"" +
                                StillLocation + VPImagesource + "\" " +
                                "begin=\"0\" " +
                                "dur=\"" + STDuration + "\" " +
                                ( b.TransIn != null ?
                                    "transIn=\"" + b.TransIn + "\" " : "" ) +
                                ( b.TransOut != null ?
                                    "transOut=\"" + b.TransOut + "\" " : "" ) +
                                " regPoint=\"middle\"" +
                                " />\n" );
                        OutFile.print( "</a>\n" );
                        OutFile.print( "<a href=\"" + b.Link + "&amp;strategy=attack\" >\n" );
                        
                        OutFile.print( "<img region=\"link-region1\" src=\"" +
                                StillLocation + ATTACKImagesource + "\" " +
                                "begin=\"0\" " +
                                "dur=\"" + STDuration + "\" " +
                                ( b.TransIn != null ?
                                    "transIn=\"" + b.TransIn + "\" " : "" ) +
                                ( b.TransOut != null ?
                                    "transOut=\"" + b.TransOut + "\" " : "" ) +
                                " regPoint=\"middle\"" +
                                " />\n" );
                        OutFile.print( "</a>\n" );
                        OutFile.print( "<a href=\"" + b.Link + "&amp;strategy=support\" >\n" );
                        
                        OutFile.print( "<img region=\"link-region2\" src=\"" +
                                StillLocation + SUPPORTImagesource + "\" " +
                                "begin=\"0\" " +
                                "dur=\"" + STDuration + "\" " +
                                ( b.TransIn != null ?
                                    "transIn=\"" + b.TransIn + "\" " : "" ) +
                                ( b.TransOut != null ?
                                    "transOut=\"" + b.TransOut + "\" " : "" ) +
                                " regPoint=\"middle\"" +
                                " />\n" );
                        OutFile.print( "</a>\n" );
                        
                    }
                    
                    OutFile.print( "</par>\n" );
                }
                if( a.VideoSegments.length > 1 )
                {
                    OutFile.print( "</seq>\n" );
                }
            }
            OutFile.print( "</par>\n" );
        }
        
        return true;
    }
    
    private String SourceText( String Description, String Duration, boolean toFile ) throws UnsupportedEncodingException
    {
        
        String Msg = null;
        
        try
        {
            if( toFile == true )
            {
                
                // Name of the file
                String Path = new String( EffTextLocation + RealTextFile + GenerateSuffix( Description, Duration ) + ".rt" );
                
                P.PrintLn( P.Debug1, "Path to file " + Path );
                
                Msg = new String( TextLocation + RealTextFile + GenerateSuffix( Description, Duration ) + ".rt" );
                
                PrintWriter RealFile = new PrintWriter( new FileOutputStream( Path ) );
                
                RealFile.print(
                        "<window  duration=\"" + Duration + "\" bgcolor=\"#" + BgColor + "\" width=\"" + Width + "\" height=\"" +
                        STHeight + "\"> \n" +
                        "<font size=\"" + FontSize + "\" color=\"#" + FgColor + "\" face=\"" + TextFont + "\"> \n" +
                        "<center>\n" +
                        Description + "\n" +
                        "</center>\n" +
                        "</font> \n" +
                        "</window> " );
                
                RealFile.flush();
                
            }
            else
            {
                Msg = new String( RealTextServer + URLEncoder.encode( Description, "UTF-8" ) +
                        "&amp;width=" + Width +
                        "&amp;height=" + STHeight + "&amp;duration=" + Duration +
                        "&amp;fontsize=" + FontSize + "&amp;fgcolor=%23" + FgColor +
                        "&amp;bgcolor=%23" + BgColor + "&amp;text-align=" +
                        TextAlign + "&amp;ext=.rt" );
            }
        }
        catch( IOException e )
        {
            P.PrintLn( P.Err, "Error opening file " + e.toString() );
            e.printStackTrace(P.Err);
        }
        
        return Msg;
        
    }
    
    private String GenerateSuffix( String Description, String Duration )
    {
        
        String Msg = new String( Description + Duration );
        Msg = new String( Msg.replaceAll( " |:|\\.|\\?|/", "_" ) );
        
        return Msg;
        
    }
}
