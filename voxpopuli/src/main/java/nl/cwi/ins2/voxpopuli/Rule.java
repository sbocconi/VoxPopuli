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

public interface Rule{

  int ApplyRule( StorySpace a, int NodeLevel ) throws Exception;

  int DoSelection( StorySpace a, int NodeLevel ) throws Exception;

};

