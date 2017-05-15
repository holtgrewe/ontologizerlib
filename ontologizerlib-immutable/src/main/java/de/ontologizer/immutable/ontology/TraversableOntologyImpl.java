package de.ontologizer.immutable.ontology;

import de.ontologizer.immutable.graph.DirectedGraph;
import de.ontologizer.immutable.graph.NeighborSelector;
import de.ontologizer.immutable.graph.algorithms.BreadthFirstSearch;
import de.ontologizer.immutable.graph.algorithms.VertexVisitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import ontologizer.ontology.ParentTermID;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;
import ontologizer.ontology.TermRelation;

/**
 * Decorator for {@link Ontology} that allows the traversal of the ontology
 * adhering to the {@link TraversableOntology} interface.
 * 
 * <h3>Usage</h3>
 * 
 * <pre>
 * final Ontology ontology = // ...
 * final OntologyTraversalDecorator oTraversal =
 *     new OntologyTraversalDecorator(ontology);
 *
 * // ...
 *
 * oTraversal.walkToSource(new TermID("GO:", 123), new VertexVisitor() {
 *     &#64;Override
 *     public boolean visit(DirectedGraph&lt;Term&gt; g, Term v) {
 *     // ...
 *     return true;
 * });
 * </pre>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class TraversableOntologyImpl<EdgeType extends OntologyEdge> implements TraversableOntology<EdgeType> {

	private static final long serialVersionUID = 1L;

	/** Decorated {@link ImmutableOntology} to wrap. */
	private final Ontology<EdgeType> ontology;

	/**
	 * Construct decorator with underlying {@link ImmutableOntology}.
	 * 
	 * @param ontology
	 *            {@link ImmutableOntology} to decorate.
	 */
	public TraversableOntologyImpl(Ontology<EdgeType> ontology) {
		this.ontology = ontology;
	}

	@Override
	public Term get(TermID termId) {
		return ontology.get(termId);
	}

	@Override
	public int countTerms() {
		return ontology.countTerms();
	}

	@Override
	public int maximumTermID() {
		return ontology.maximumTermID();
	}

	@Override
	public Iterator<Term> iterator() {
		return ontology.iterator();
	}

	@Override
	public boolean doesPathExist(TermID sourceId, TermID destId) {
		// Some special cases because of the artificial root
		if (ontology.isRootTerm(destId)) {
			if (ontology.isRootTerm(sourceId)) {
				return true;
			}
			return false;
		}

		// We walk from the destination to the source against the graph
		// direction in BFS fashion.
		final boolean[] pathExists = new boolean[1]; // yay for Java lambdas...
		final Term source = ontology.get(sourceId);
		Term dest = ontology.get(destId);

		new BreadthFirstSearch<Term, EdgeType, DirectedGraph<Term, EdgeType>>().startFromReverse(ontology.getGraph(),
				dest, new VertexVisitor<Term, EdgeType>() {
					@Override
					public boolean visit(DirectedGraph<Term, EdgeType> g, Term v) {
						if (!v.equals(source)) {
							return true;
						}
						pathExists[0] = true;
						return false;
					}
				});

		return pathExists[0];
	}

	@Override
	public void walkToSource(TermID termId, VertexVisitor<Term, EdgeType> visitor,
			EnumSet<TermRelation> relationsToFollow) {
		new BreadthFirstSearch<Term, EdgeType, DirectedGraph<Term, EdgeType>>().startFrom(ontology.getGraph(),
				get(termId), new RelationsToFollowNeighborSelector(true, relationsToFollow), visitor);
	}

	@Override
	public void walkToSource(Collection<TermID> termIds, VertexVisitor<Term, EdgeType> visitor) {
		for (TermID termId : termIds) {
			walkToSource(termId, visitor);
		}
	}

	@Override
	public void walkToSource(TermID termId, VertexVisitor<Term, EdgeType> visitor) {
		walkToSource(termId, visitor, EnumSet.allOf(TermRelation.class));
	}

	@Override
	public void walkToSource(Collection<TermID> termIds, VertexVisitor<Term, EdgeType> visitor,
			EnumSet<TermRelation> relationsToFollow) {
		for (TermID termId : termIds) {
			walkToSinks(termId, visitor, relationsToFollow);
		}
	}

	@Override
	public void walkToSinks(TermID termId, VertexVisitor<Term, EdgeType> visitor,
			EnumSet<TermRelation> relationsToFollow) {
		new BreadthFirstSearch<Term, EdgeType, DirectedGraph<Term, EdgeType>>().startFrom(ontology.getGraph(),
				get(termId), new RelationsToFollowNeighborSelector(false, relationsToFollow), visitor);
	}

	@Override
	public void walkToSinks(TermID termId, VertexVisitor<Term, EdgeType> visitor) {
		walkToSinks(termId, visitor, EnumSet.allOf(TermRelation.class));
	}

	@Override
	public void walkToSinks(Collection<TermID> termIds, VertexVisitor<Term, EdgeType> visitor) {
		for (TermID termId : termIds) {
			walkToSinks(termId, visitor);
		}
	}

	@Override
	public void walkToSinks(Collection<TermID> termIds, VertexVisitor<Term, EdgeType> visitor,
			EnumSet<TermRelation> relationsToFollow) {
		for (TermID termId : termIds) {
			walkToSinks(termId, visitor, relationsToFollow);
		}
	}

	/**
	 * Helper class for selecting neighbors over a edges marked with specific
	 * {@link TermRelation}s only.
	 * 
	 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel
	 *         Holtgrewe</a>
	 */
	private final class RelationsToFollowNeighborSelector
			implements NeighborSelector<Term, EdgeType, DirectedGraph<Term, EdgeType>> {

		private final boolean reverse;
		private final EnumSet<TermRelation> rels;

		/**
		 * Construct with set of relation types to follow
		 * 
		 * @param reverse
		 *            whether or not to iterate edges in reverse order
		 * @param rels
		 *            relation types to follow in neighbor selections
		 */
		private RelationsToFollowNeighborSelector(boolean reverse, EnumSet<TermRelation> rels) {
			this.reverse = reverse;
			this.rels = rels;
		}

		@Override
		public Iterator<Term> selectNeighbors(Term v) {
			final Iterator<? extends OntologyEdge> inIter;
			if (reverse) {
				inIter = ontology.getGraph().inEdgeIterator(v);
			} else {
				inIter = ontology.getGraph().outEdgeIterator(v);
			}
			final ArrayList<Term> termsToConsider = new ArrayList<Term>();
			while (inIter.hasNext()) {
				OntologyEdge edge =
						(OntologyEdge) inIter.next(); /* Ugly cast */
				if (rels.contains(edge.getTermRelation())) {
					termsToConsider.add(edge.getSource());
				}
			}
			return termsToConsider.iterator();
		}
	}

	@Override
	public DirectedGraph<Term, EdgeType> getGraph() {
		return ontology.getGraph();
	}

	@Override
	public TermContainer getTermContainer() {
		return ontology.getTermContainer();
	}

	@Override
	public Collection<Term> getLeafTerms() {
		return ontology.getLeafTerms();
	}

	@Override
	public List<Term> getTermsInTopologicalOrder() {
		return ontology.getTermsInTopologicalOrder();
	}

	@Override
	public boolean isRootTerm(TermID termId) {
		return ontology.isRootTerm(termId);
	}

	@Override
	public boolean isArtificialRoot(TermID termId) {
		return ontology.isArtificialRoot(termId);
	}

	@Override
	public Collection<Term> getLevel1Terms() {
		return ontology.getLevel1Terms();
	}

	@Override
	public Collection<Term> getChildTerms(TermID termId) {
		return ontology.getChildTerms(termId);
	}

	@Override
	public Collection<Term> getParentTerms(TermID termId) {
		return ontology.getParentTerms(termId);
	}

	@Override
	public Collection<ParentTermID> getParentRelations(TermID termId) {
		return ontology.getParentRelations(termId);
	}

	@Override
	public TermRelation getParentRelation(TermID termId, TermID parent) {
		return ontology.getParentRelation(termId, parent);
	}

	@Override
	public Collection<TermID> getTermSiblings(TermID termId) {
		return ontology.getTermSiblings(termId);
	}

	@Override
	public Term getRootTerm() {
		return ontology.getRootTerm();
	}

}
