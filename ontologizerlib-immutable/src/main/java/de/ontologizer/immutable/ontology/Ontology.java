package de.ontologizer.immutable.ontology;

import de.ontologizer.immutable.graph.DirectedGraph;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import ontologizer.ontology.ParentTermID;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;
import ontologizer.ontology.TermRelation;
import ontologizer.types.ByteString;

/**
 * Interface for representation of a full ontology.
 * 
 * <p>
 * In the ontologizerlib, an ontology is a set of terms with an imposed DAG graph structure. The
 * convention is that the interface takes {@link TermID}s for parameters but returns {@link Term}
 * objects. Any callbacks take {@link Term}s. Any other interfaces using the {@link ByteString} code
 * Java {@link String} representation of term IDs is implemented as decorators.
 * </p>
 * 
 * <p>
 * In <code>Ontology</code> graphs, vertices point to their children.
 * </p>
 * 
 * <p>
 * Only supports simple queries on terms and their relations. For anything going over more than one
 * edge, use an {@link TraversableOntology}.
 * </p>
 * 
 * @author Sebastian Bauer
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface Ontology<EdgeType extends OntologyEdge> extends TermMap, Serializable {

	/**
	 * Query for underlying {@link DirectedGraph}.
	 * 
	 * @return {@link DirectedGraph} of this <code>Ontology</code>.
	 */
	public DirectedGraph<Term, EdgeType> getGraph();

	/**
	 * Query for underlying {@link TermContainer}.
	 * 
	 * @return {@link TermContainer} of this <code>Ontology</code>.
	 */
	public TermContainer getTermContainer();

	/**
	 * Query for root {@link Term}.
	 * 
	 * @return The {@link Term} that is the root of the ontology.
	 */
	public Term getRootTerm();

	/**
	 * Query for all leaf terms.
	 * 
	 * @return {@link Collection} of leaf {@link Term}s.
	 */
	public Collection<Term> getLeafTerms();

	/**
	 * Query for all {@link Term}s in topological order.
	 * 
	 * @return {@link List} of {@link Term}s in topological order.
	 */
	public List<Term> getTermsInTopologicalOrder();

	/**
	 * Query for "is root" state of the given {@link TermID}
	 * 
	 * @param termId
	 *            {@link TermID} of the root
	 * @return whether or not <code>termId</code> is the root.
	 */
	public boolean isRootTerm(TermID termId);

	/**
	 * Query for "is artificial root" state of the given {@link TermID}
	 * 
	 * @param termId
	 *            {@link TermID} of the root
	 * @return whether or not <code>termId</code> is the root.
	 */
	public boolean isArtificialRoot(TermID termId);

	/**
	 * Return {@link Collection} of terms on the first level.
	 * 
	 * @return {@link Collection} of children
	 */
	public Collection<Term> getLevel1Terms();

	/**
	 * Return {@link Collection} of children for the given {@link TermID}
	 * 
	 * @param termId
	 *            {@link TermID} to query for
	 * @return {@link Collection} of children
	 */
	public Collection<Term> getChildTerms(TermID termId);

	/**
	 * Return {@link Collection} of parents for the given {@link TermID}
	 * 
	 * @param termId
	 *            {@link TermID} to query for
	 * @return {@link Collection} of parents
	 */
	public Collection<Term> getParentTerms(TermID termId);

	/**
	 * Query for relations between terms and their parents.
	 * 
	 * @param termId
	 *            {@link TermID} to query for.
	 * @return {@link Collection} of {@link ParentTermID}s describing the term-to-parent relations.
	 */
	public Collection<ParentTermID> getParentRelations(TermID termId);

	/**
	 * Get the relation that relates term to the parent or null.
	 *
	 * @param parent
	 *            selects the parent
	 * @param termId
	 *            selects the term
	 * @return the relation type of term and the parent term or null if no parent is not the parent
	 *         of term.
	 */
	public TermRelation getParentRelation(TermID termId, TermID parent);

	/**
	 * Query for siblings of the term identified by the given {@link TermID}.
	 * 
	 * @param termId
	 *            {@link TermID} to query for
	 * @return {@link Collection} of the {@link Term}'s siblings
	 */
	public Collection<TermID> getTermSiblings(TermID termId);

}
