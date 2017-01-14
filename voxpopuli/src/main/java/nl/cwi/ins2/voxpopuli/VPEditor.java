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

package nl.cwi.ins2.voxpopuli;

import java.io.File;
import java.io.FileFilter;

public class VPEditor {
	/*********************
	 * CONSTANTS *
	 *********************/

	private static final String suffix = ".rule";

	/*************************
	 * PSEUDO VARIABLES * set once and forever * with error checking *
	 *************************/

	/*********************
	 * VARIABLES *
	 **********************/

	/*********************
	 * CLASSES *
	 **********************/

	/*********************
	 * FUNCTIONS *
	 **********************/

	private static void Usage() throws Exception {
		System.out.println(
				"Usage: <local=true|false> <Sesame server|Local Directory> <Repository name> <nameSpace> <videolocation>\n"
						+ "<badness> <rulefile> <outputfile>");
		throw new Exception("Program not called correctly");
	}

	public VPEditor() {

	}

	public static void main(String[] args) {

		try {

			if (args.length != 8) {
				Usage();
			}

			final String prefix = args[6];

			Outputs P = new Outputs();
			P.SetOutputStreams(null, null, null, null, null, null, null, System.out, System.err);

			DataContainer theDataContainer = new DataContainer();
			DataBuilder theDataBuilder = new DataBuilder(args[0].equals("true"), args[1], args[2], args[3],
					theDataContainer, P);

			theDataBuilder.SetObject(true, true);
			RuleInstance aRuleInstance = new RuleInstance(theDataContainer, P);

			StorySpace theStory = new StorySpace(theDataContainer.Videos, theDataContainer.Audios,
					theDataContainer.Images, theDataContainer.Texts);

			int goodness = Util.StrToInt(args[5]);

			try {
				Scheduler theScheduler = new Scheduler(theStory, P, aRuleInstance);

				// This filter only returns rdf files
				FileFilter fileFilter = new FileFilter() {
					public boolean accept(File file) {
						return file.isFile() && file.toString().matches(".*" + prefix + "[0-9][0-9]*" + suffix);
					}
				};

				// Load RDF files
				File myDir = new File("./");

				File[] files = myDir.listFiles(fileFilter);

				if (files == null) {
					throw new java.io.IOException("No files in local directory");
				}

				for (int i = 0; i < files.length; i++) {

					Parser R = new Parser(aRuleInstance);

					ScriptNode Node = R.ParseRulesFile(args[6] + i + suffix);

					Module theModule = new Module(theScheduler);
					theScheduler.AddModule(theModule);
					theModule.AddStartNode(Node);
				}

				theScheduler.InitGoodness(goodness);

				if (theScheduler.RunScheduler()) {
					P.PrintLn(P.ResultOut, "Solution found ");
				} else {
					P.PrintLn(P.ResultOut, "Solution NOT found ");
				}

				SMILMedia theSMILMedia = new SMILMedia("_100", args[4], null, null, null, args[2], args[7], true, null,
						true, P);

				theSMILMedia.SetSMILURL("url", "two", "videolocation", "", "stilllocation", "textlocation",
						"repository", "RDFLocation", "domainNS", "" + "local", "44", "VP", "intercut", "quality",
						"caption", null, null, null);

				try {
					if (theSMILMedia.DoMontage(theStory.GetLastPlot()) && theSMILMedia.CreateSMILOutput(true)) {
						System.out.println("Output generated ");
					} else {
						System.out.println("Output NOT generated ");
					}
				} catch (Exception e) {
					System.out.println("Output NOT generated " + e.toString());
					e.printStackTrace(System.out);
				}
			} catch (Exception e) {
				System.out.println("TestRule failed " + e.toString());
				e.printStackTrace(System.out);
			}

		} catch (Exception ex) {
			System.out.println("Brodo happened ");
			ex.printStackTrace(System.out);
		}
	}
}
