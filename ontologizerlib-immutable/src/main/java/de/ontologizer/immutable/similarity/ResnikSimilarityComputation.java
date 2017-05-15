package de.ontologizer.immutable.similarity;

import de.ontologizer.immutable.ic.InformationContentMap;
import de.ontologizer.immutable.ontology.ImmutableOntology;
import de.ontologizer.immutable.ontology.Ontology;
import ontologizer.ontology.Term;

/**
 * Implementation of Resnik similarity computation
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class ResnikSimilarityComputation extends AbstractSimilarityComputation {

	/**
	 * Construct computation object for Resnik's similarity measure.
	 *
	 * @param ontology
	 *            The {@link Ontology} to base the computation on
	 * @param informationContent
	 *            Label for each {@link Term} in <code>ontology</code> with the
	 *            information content
	 */
	public ResnikSimilarityComputation(ImmutableOntology ontology,
			InformationContentMap informationContent) {
		super(new ResnikOneToOneSimilarityComputation(ontology,
				informationContent));
	}

}
