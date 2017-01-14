package nl.cwi.ins2.voxpopuli;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RDFRepository {

	static public final String VoxPopuliNamespaces = "http://www.cwi.nl/~media/ns/VP/VoxPopuli.rdfs#";
	static private final String VoxPopuliString = "VP";
	static private final String VoxPopuliSesameNamespaces = "VoxPopuli = <" + VoxPopuliNamespaces + ">";

	static private final String FixedNamespaces = VoxPopuliSesameNamespaces + ", "
			+ "MediaClipping = <http://www.w3.org/2001/SMIL20/MediaClipping#>, "
			+ "BasicMedia = <http://www.w3.org/2001/SMIL20/BasicMedia#>, "
			+ "rdfs = <http://www.w3.org/2000/01/rdf-schema#>, "
			+ "rdf = <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

	private Repository repo;

	String theNameSpaceString;
	
	private Logger myLogger;

	public RDFRepository(boolean isLocal, String RDFLocation, String RepositoryString, String someNameSpaceString)
			throws IOException {

		myLogger = LoggerFactory.getLogger(this.getClass());
		
		theNameSpaceString = someNameSpaceString;

		if (isLocal) {

			repo = new SailRepository(new MemoryStore());
			repo.initialize();

			try (RepositoryConnection con = repo.getConnection()) {

				// This filter only returns rdf files
				FileFilter fileFilter = new FileFilter() {
					public boolean accept(File file) {
						return file.isFile() && file.toString().endsWith("rdf");
					}
				};

				String location = RDFLocation + File.separator + RepositoryString;
				// Load RDF files
				File myRDFDir = new File(location);

				File[] RDFfiles = myRDFDir.listFiles(fileFilter);

				if (RDFfiles == null) {
					throw new java.io.IOException(
							"No RDF files in " + location);
				}

				for (int i = 0; i < RDFfiles.length; i++) {
					con.add(RDFfiles[i], theNameSpaceString, RDFFormat.RDFXML);
				}

				// This filter only returns rdfs files
				fileFilter = new FileFilter() {
					public boolean accept(File file) {
						return file.isFile() && file.toString().endsWith("rdfs");
					}
				};

				File myRDFSDir = new File(location);
				File[] RDFSfiles = myRDFSDir.listFiles(fileFilter);

				if (RDFSfiles == null) {
					throw new java.io.IOException("No RDFS files in " + RDFLocation + RepositoryString);
				}

				for (int i = 0; i < RDFSfiles.length; i++) {
					con.add(RDFSfiles[i], VoxPopuliNamespaces, RDFFormat.RDFXML);

				}
			} catch (RDF4JException e) {
				// handle exception. This catch-clause is
				// optional since RDF4JException is an unchecked exception
			}

		} else {
			repo = new SPARQLRepository(RDFLocation);
			repo.initialize();
		}

	}

	public List<BindingSet> executeQuery(String query) {

		List<BindingSet> results;

		try (RepositoryConnection conn = repo.getConnection()) {
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SERQL,
					query + " using namespace " + FixedNamespaces);

			try (TupleQueryResult result = tupleQuery.evaluate()) {
				results = QueryResults.asList(result);
			}
		}

		return results;
	}

	public static void main(String[] args) {
		String queryString1 = "select Start, Stop, Lab, File, X, Y, TypeY from " + "{X} rdf:type {VoxPopuli:Video}; "
				+ "MediaClipping:beginFrame {Start}; " + "MediaClipping:endFrame {Stop}; " + "rdfs:label {Lab}; "
				+ "BasicMedia:src {File}, " + "[{Y} VoxPopuli:hasMedia {X}, " + "{Y} serql:directType {TypeY}] ";

		try {
			RDFRepository a = new RDFRepository(true, "../RDF", "IWA", "ullo");
			List<BindingSet> Results = a.executeQuery(queryString1);
			Iterator<BindingSet> i = Results.iterator();

			long dur, maxsec = 30, minsec = 4;
			while (i.hasNext()) {
				BindingSet solution = i.next();

				dur = (Util.ConvertToDSec(solution.getValue("Stop").toString())
						- Util.ConvertToDSec(solution.getValue("Start").toString())) / 10;
				if ((dur > maxsec) || (dur < minsec)) {
					System.out.println("Video too " + (dur > maxsec ? "long: " : "short: ")
							+ solution.getValue("X").toString() + " - " + solution.getValue("Lab").toString() + " dur "
							+ dur + " - "
							+ (solution.getValue("Y") != null ? "Video contained in: "
									+ solution.getValue("Y").toString() + " of type " + solution.getValue("TypeY")
											.toString().substring(RDFRepository.VoxPopuliNamespaces.length())
									: " Video NOT contained anywhere"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}