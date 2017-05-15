package de.ontologizer.immutable.similarity;

import de.ontologizer.immutable.ic.InformationContentMap;
import de.ontologizer.immutable.ontology.ImmutableOntology;
import de.ontologizer.immutable.ontology.Ontology;
import de.ontologizer.immutable.ontology.TermCollectorVisitor;
import de.ontologizer.immutable.ontology.TraversableImmutableOntology;
import java.util.HashSet;
import java.util.Set;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;

/**
 * Implementation of Resnik's similarity measure between two terms.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
class ResnikOneToOneSimilarityComputation
		implements
			OneToOneSimilarityComputation {

	/** The ontology to base the similarity computation on. */
	private final ImmutableOntology ontology;

	/**
	 * Decorator that allows easy traversing of {@link ImmutableOntology}
	 * objects.
	 */
	private final TraversableImmutableOntology traversableOntology;

	/**
	 * Information content for each term in the {@link #ontology} as required by
	 * Resnik's similarity measure.
	 */
	private final InformationContentMap informationContent;

	/**
	 * Construct computation object for Resnik's similarity measure.
	 *
	 * @param ontology
	 *            The {@link Ontology} to base the computation on
	 * @param ontologyFast
	 *            The slimmed-down version to use for faster access
	 * @param informationContent
	 *            Label for each {@link Term} in <code>ontology</code> with the
	 *            information content
	 */
	public ResnikOneToOneSimilarityComputation(ImmutableOntology ontology,
			InformationContentMap informationContent) {
		this.ontology = ontology;
		this.traversableOntology = new TraversableImmutableOntology(
				this.ontology);
		this.informationContent = informationContent;
	}

	@Override
	public double computeScore(TermID t1, TermID t2) {
		// Compute all ancestors for both t1 and t2
		final TermCollectorVisitor c1 = new TermCollectorVisitor();
		final TermCollectorVisitor c2 = new TermCollectorVisitor();
		traversableOntology.walkToSource(t1, c1);
		traversableOntology.walkToSource(t2, c2);

		// Obtain common ancestors
		final Set<Term> commonAncestors = new HashSet<Term>(c1.getTerms());
		commonAncestors.retainAll(c2.getTerms());

		// Compute largest IC of all common ancestors
		double result = 0;
		for (Term term : commonAncestors) {
			result = Math.max(result, informationContent.get(term.getID()));
		}
		return result;
	}

}
