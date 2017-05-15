package de.ontologizer.immutable.ic;

import ontologizer.ontology.Term;

/**
 * Interface that term annotations should implement.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface AnnotatedTerm {

	/**
	 * Query for {@link Term} of annotation
	 * 
	 * @return {@link Term} of the annotation
	 */
	public Term getTerm();

	/**
	 * Query for {@link Label} of annotation
	 * 
	 * @return {@link Label} of the annotation
	 */
	public Label getLabel();

}
