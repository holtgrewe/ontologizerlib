package de.ontologizer.immutable.ontology;

import de.ontologizer.immutable.graph.Edge;
import de.ontologizer.immutable.graph.ImmutableEdge;
import de.ontologizer.immutable.graph.ShallowCopyable;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermRelation;

/**
 * Implementation of an immutable {@link OntologyEdge}.
 * 
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class ImmutableOntologyEdge
		implements
			OntologyEdge,
			ShallowCopyable<ImmutableOntologyEdge> {

	private static final long serialVersionUID = 1L;

	private final Term source;
	private final Term dest;
	private final TermRelation termRelation;

	/**
	 * Static constructor method so {@link Term} does not have to be explicit.
	 * 
	 * @param source
	 *            {@link Term} to use for the edge foot vertex
	 * @param dest
	 *            {@link Term} to use for the edge tip vertex
	 * @param termRelation
	 *            {@link TermRelation} to use for the label
	 */
	public static ImmutableOntologyEdge construct(Term source, Term dest,
			TermRelation termRelation) {
		return new ImmutableOntologyEdge(source, dest, termRelation);
	}

	/**
	 * Construct <code>ImmutableEdge</code> with the given source and
	 * destination vertices.
	 *
	 * @param source
	 *            {@link Term} to use for the edge foot vertex
	 * @param dest
	 *            {@link Term} to use for the edge tip vertex
	 * @param termRelation
	 *            {@link TermRelation} to use for the label
	 */
	public ImmutableOntologyEdge(Term source, Term dest,
			TermRelation termRelation) {
		this.source = source;
		this.dest = dest;
		this.termRelation = termRelation;
	}

	@Override
	public Term getDest() {
		return dest;
	}

	@Override
	public Term getSource() {
		return source;
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public TermRelation getTermRelation() {
		return termRelation;
	}

	@Override
	public ImmutableOntologyEdge shallowCopy() {
		return ImmutableOntologyEdge.construct(source, dest, termRelation);
	}

	@Override
	public String toString() {
		return "ImmutableOntologyEdge [source=" + source + ", dest=" + dest
				+ ", termRelation=" + termRelation + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result
				+ ((termRelation == null) ? 0 : termRelation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableOntologyEdge other = (ImmutableOntologyEdge) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (termRelation != other.termRelation)
			return false;
		return true;
	}

	/**
	 * Static factory method for creating a {@link Factory}
	 * 
	 * @return Built <code>Factory</code> object.
	 */
	public static Factory factory() {
		return new Factory();
	}

	/**
	 * Factory for {@link ImmutableEdge}.
	 * 
	 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel
	 *         Holtgrewe</a>
	 */
	public static class Factory
			implements
				Edge.Factory<Term, ImmutableOntologyEdge> {

		@Override
		public ImmutableOntologyEdge construct(Term u, Term v) {
			return ImmutableOntologyEdge.construct(u, v, TermRelation.UNKOWN);
		}

	}
}
