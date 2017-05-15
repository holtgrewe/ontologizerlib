package de.ontologizer.immutable.io;

import de.ontologizer.immutable.ontology.Ontology;
import java.io.InputStream;

/**
 * Interface for {@link Ontology} reading data source.
 * 
 * @author Sebastian Bauer
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface ReaderInput {

	/**
	 * Query for {@link InputStream} to read from.
	 * 
	 * @return the wrapped input stream
	 */
	public InputStream inputStream();

	/**
	 * Close the associated input streams.
	 */
	public void close();

	/**
	 * Query for size of input stream.
	 * 
	 * @return The size of the contents of the input stream or -1 if this
	 *         information is not available.
	 */
	public int getSize();

	/**
	 * Query for position in input stream.
	 * 
	 * @return the current position of the input.
	 */
	public int getPosition();

	/**
	 * Query for file name.
	 * 
	 * @return the filename associated to the input or <code>null</code> if no
	 *         filename is associated.
	 */
	public String getFilename();

}
