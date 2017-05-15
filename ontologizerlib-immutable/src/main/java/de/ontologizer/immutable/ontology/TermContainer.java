package de.ontologizer.immutable.ontology;

import ontologizer.ontology.Term;
import ontologizer.types.ByteString;

/**
 * Interface for OBO parse results ({@link Term}s plus some meta information).
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface TermContainer extends TermMap {

	/**
	 * Query for ontology file format version of stored terms.
	 * 
	 * @return {@link ByteString} with the format version.
	 */
	public ByteString getFormatVersion();

	/**
	 * Query for the date of the parsed file.
	 * 
	 * @return {@link ByteString} with the file's date.
	 */
	public ByteString getDate();

}
