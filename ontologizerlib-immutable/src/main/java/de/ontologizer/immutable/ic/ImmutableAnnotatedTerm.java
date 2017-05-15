package de.ontologizer.immutable.ic;

import ontologizer.ontology.Term;

/**
 * Simple, immutable implementation of {@link AnnotatedTerm}
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class ImmutableAnnotatedTerm<LabelType extends Label>
		implements
			AnnotatedTerm {

	/** Annotated term. */
	private final Term term;

	/** Simple annotated label. */
	private final LabelType label;

	public ImmutableAnnotatedTerm(Term term, LabelType label) {
		this.term = term;
		this.label = label;
	}

	public Term getTerm() {
		return term;
	}

	public LabelType getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return "ImmutableAnnotatedTerm [term=" + term + ", label=" + label
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
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
		@SuppressWarnings("rawtypes")
		ImmutableAnnotatedTerm other = (ImmutableAnnotatedTerm) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

}
