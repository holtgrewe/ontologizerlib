package de.ontologizer.immutable.similarity;

import ontologizer.ontology.TermID;

/**
 * Interface for computing similarity scores between two terms.
 *
 * <p>
 * Based on this, similarity can be defined between two sets of terms as
 * implemented in {@link AbstractSimilarityComputation} and sub classes.
 * </p>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
interface OneToOneSimilarityComputation {

	/**
	 * Compute similarity score between two terms.
	 *
	 * @param t1
	 *            first term ID
	 * @param t2
	 *            second term ID
	 * @return Similarity score between the two terms
	 */
	public double computeScore(TermID t1, TermID t2);

}
