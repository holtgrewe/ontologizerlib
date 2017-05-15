package de.ontologizer.immutable.io;

import de.ontologizer.immutable.ontology.Ontology;
import de.ontologizer.immutable.ontology.OntologyEdge;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import ontologizer.io.dot.AbstractDotAttributesProvider;
import ontologizer.io.dot.GODOTWriter;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;

/**
 * Writing of an ontology to the DOT format.
 * 
 * <pre>
 * Ontology ontology = // ...
 * 		DotOntologyWriter.fromPath("ontology.dot.gz").write(ontology);
 * </pre>
 * 
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class OntologyDotWriter<EdgeType extends OntologyEdge>
		implements
			OntologyWriter<EdgeType> {

	/** Where the data is written to. */
	private final WriterOutput writerOutput;

	/**
	 * Construct with {@link String} path to output file.
	 * 
	 * @param <EdgeType>
	 *            {@link OntologyEdge} sub type to use for the ontology's edge
	 *            type
	 * @param outputPath
	 *            Path to file to write to
	 * @return {@link OntologyDotWriter} constructed for the give
	 *         <code>outputPath</code>
	 */
	public static <EdgeType extends OntologyEdge> OntologyDotWriter<EdgeType> fromPath(
			String outputPath) {
		return fromFile(new File(outputPath));
	}

	/**
	 * Construct with output {@link File}.
	 * 
	 * @param <EdgeType>
	 *            {@link OntologyEdge} sub type to use for the ontology's edge
	 *            type
	 * @param outputFile
	 *            {@link File} to write to
	 * @return {@link OntologyDotWriter} constructed for the give
	 *         <code>outputFile</code>
	 */
	public static <EdgeType extends OntologyEdge> OntologyDotWriter<EdgeType> fromFile(
			File outputFile) {
		return new OntologyDotWriter<EdgeType>(
				new FileWriterOutput(outputFile));
	}

	/**
	 * Construct with {@link WriterOutput}.
	 * 
	 * @param writerOutput
	 *            {@link WriterOutput} to write to
	 */
	public OntologyDotWriter(WriterOutput writerOutput) {
		this.writerOutput = writerOutput;
	}

	@Override
	public void write(Ontology<EdgeType> ontology) {
		writerOutput.close(); // using old API...

		Set<TermID> termIDs = new HashSet<TermID>();
		for (Term term : ontology) {
			termIDs.add(term.getID());
		}
		ontologizer.ontology.TermContainer legacyTC = new ontologizer.ontology.TermContainer(
				ontology, ontology.getTermContainer().getFormatVersion(),
				ontology.getTermContainer().getDate());
		ontologizer.ontology.Ontology legacyOntology = ontologizer.ontology.Ontology
				.create(legacyTC);

		GODOTWriter.writeDOT(legacyOntology,
				new File(writerOutput.getFilename()),
				ontology.getRootTerm().getID(), termIDs,
				new AbstractDotAttributesProvider());
	}

}
