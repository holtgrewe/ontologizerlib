package de.ontologizer.immutable.ic;

import com.google.common.collect.ImmutableSet;
import de.ontologizer.immutable.ontology.ImmutableOntology;
import de.ontologizer.immutable.ontology.TermCollectorVisitor;
import de.ontologizer.immutable.ontology.TraversableImmutableOntology;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;

/**
 * Construct {@link InformationContentMap} from {@link Collection} of
 * {@link AnnotatedTerm}s.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class AnnotatedTermsInformationContentMapFactory
		implements
			InformationContentMapFactory {

	/**
	 * {@link AnnotatedTerm}s to construct IC from.
	 */
	private final Collection<AnnotatedTerm> annotatedTerms;

	/**
	 * Construct from {@link Collection} of {@link AnnotatedTerm}s.
	 * 
	 * @param ontology
	 *            {@link ImmutableOntology} for performing implicit assignment
	 *            of annotations to parents
	 * @param annotatedTerms
	 *            to use for computing the IC
	 */
	public AnnotatedTermsInformationContentMapFactory(
			ImmutableOntology ontology,
			Collection<? extends AnnotatedTerm> annotatedTerms) {
		this.annotatedTerms = performImplicitAssignment(ontology,
				annotatedTerms);
	}

	private Collection<AnnotatedTerm> performImplicitAssignment(
			ImmutableOntology ontology,
			Collection<? extends AnnotatedTerm> annotatedTerms) {
		final TraversableImmutableOntology traversableOntology = new TraversableImmutableOntology(
				ontology);

		// Build initial label assignments
		Map<TermID, Set<Label>> labelAssignment = new HashMap<TermID, Set<Label>>();
		for (AnnotatedTerm at : annotatedTerms) {
			if (!labelAssignment.containsKey(at.getTerm())) {
				labelAssignment.put(at.getTerm().getID(), new HashSet<Label>());
			}
			labelAssignment.get(at.getTerm().getID()).add(at.getLabel());
		}

		// Apply implicit annotations
		Map<TermID, Set<Label>> labelAssignment2 = new HashMap<TermID, Set<Label>>();
		for (TermID termID : labelAssignment.keySet()) {
			final TermCollectorVisitor visitor = new TermCollectorVisitor();
			traversableOntology.walkToSource(termID, visitor);
			for (Term visitedTerm : visitor.getTerms()) {
				if (!labelAssignment2.containsKey(visitedTerm.getID())) {
					labelAssignment2.put(visitedTerm.getID(),
							new HashSet<Label>());
				}
				labelAssignment2.get(visitedTerm.getID())
						.addAll(labelAssignment.get(termID));
			}
		}

		// Build resulting collection of {@link AnnotatedTerm}s
		List<AnnotatedTerm> result = new ArrayList<AnnotatedTerm>();
		for (Entry<TermID, Set<Label>> entry : labelAssignment2.entrySet()) {
			for (Label label : entry.getValue()) {
				result.add(new ImmutableAnnotatedTerm<Label>(
						ontology.get(entry.getKey()), label));
			}
		}
		return result;
	}

	@Override
	public InformationContentMap build() {
		System.err.println("Computing information content...");

		// First, build mapping from term to label
		Map<Term, Set<Label>> termToLabel = new HashMap<Term, Set<Label>>();
		for (AnnotatedTerm annotatedTerm : annotatedTerms) {
			final Term term = annotatedTerm.getTerm();
			if (!termToLabel.containsKey(term)) {
				termToLabel.put(term, new HashSet<Label>());
			}
			termToLabel.get(term).add(annotatedTerm.getLabel());
		}

		// From this, derive absolute frequencies for annotation of gene with
		// term
		Map<Term, Integer> termFreqAbs = new HashMap<>();
		for (Term term : termToLabel.keySet()) {
			termFreqAbs.put(term, termToLabel.get(term).size());
		}

		// Get total number of genes with annotation
		final int numGenes = ImmutableSet.copyOf(termToLabel.values()).size();

		// From this, we can easily compute the information content
		DefaultInformationContentMap result = new DefaultInformationContentMap();
		for (Entry<Term, Integer> e : termFreqAbs.entrySet()) {
			result.put(e.getKey().getID(),
					-Math.log(((double) e.getValue()) / numGenes));
		}
		return result;
	}

}
