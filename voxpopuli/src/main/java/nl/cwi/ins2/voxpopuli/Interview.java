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

public class Interview{
  public String Id;
  public String OpinionId;
  // There can be more point made in an interview
  public Argument[] Arguments;
  public String MediaItems[];
  public String theInterviewee;
  public Question thequestion;
}
