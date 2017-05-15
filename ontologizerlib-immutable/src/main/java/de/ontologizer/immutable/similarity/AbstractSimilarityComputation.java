package de.ontologizer.immutable.similarity;

import java.util.Collection;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;

/**
 * Base class for generic similarity computation between ontology {@link Term}s
 * following the Phenomizer score computation.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
abstract class AbstractSimilarityComputation {

	/** Implementation of pairwise term similarity. */
	protected final OneToOneSimilarityComputation pairwiseImpl;

	/**
	 * Construct and set pairwise implementation.
	 *
	 * @param pairwiseImpl
	 *            Implementation object for one-to-one similarity computation.
	 */
	protected AbstractSimilarityComputation(
			OneToOneSimilarityComputation pairwiseImpl) {
		this.pairwiseImpl = pairwiseImpl;
	}

	/**
	 * Compute similarity score between two terms <code>t1</code> and
	 * <code>t2</code>.
	 *
	 * @param t1
	 *            First {@link Term} to use
	 * @param t2
	 *            Second {@link Term} to use
	 * @return Resulting pairwise score
	 */
	public double computeScore(TermID t1, TermID t2) {
		return pairwiseImpl.computeScore(t1, t2);
	}

	/**
	 * Compute asymmetric similarity score between two collections of Terms
	 * <code>query</code> and <code>target</code>.
	 *
	 * @param query
	 *            Query collection of {@link Term}s to use
	 * @param target
	 *            Target collection of {@link Term}s to use
	 * @return asymmetric similarity score
	 */
	public double computeScore(Collection<TermID> query,
			Collection<TermID> target) {
		final OneToOneSimilarityComputation pairwise = pairwiseImpl;
		double sum = 0;

		for (TermID q : query) {
			double maxValue = 0.0;
			for (TermID t : target) {
				maxValue = Math.max(maxValue, pairwise.computeScore(q, t));
			}
			sum += maxValue;
		}

		return sum / query.size();
	}

	/**
	 * Compute symmetric similarity score between two collections of Terms
	 * <code>query</code> and <code>target</code>.
	 *
	 * @param query
	 *            Query collection of {@link Term}s to use
	 * @param target
	 *            Target collection of {@link Term}s to use
	 * @return asymmetric similarity score
	 */
	public double computeSymmetricScore(Collection<TermID> query,
			Collection<TermID> target) {
		return 0.5
				* (computeScore(query, target) + computeScore(target, query));
	}

}
