package de.ontologizer.immutable.ontology;

import de.ontologizer.immutable.graph.DirectedGraph;
import de.ontologizer.immutable.graph.algorithms.VertexVisitor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ontologizer.ontology.Term;

/**
 * Helper class for collecting vertices up to the root.
 */
public class TermCollectorVisitor
		implements
			VertexVisitor<Term, ImmutableOntologyEdge> {

	private final Set<Term> terms = new HashSet<Term>();

	@Override
	public boolean visit(DirectedGraph<Term, ImmutableOntologyEdge> g, Term v) {
		terms.add(v);
		return true;
	}

	/**
	 * Call after using visitor to obtain list of {@link Term}s
	 * 
	 * @return {@link List} of {@link Term}s.
	 */
	public Set<Term> getTerms() {
		return terms;
	}

}