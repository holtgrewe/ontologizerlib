package de.ontologizer.immutable.ic;

/**
 * Interface for classes contructing {@link InformationContentMap}
 * implementations.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface InformationContentMapFactory {

	/**
	 * Build {@link InformationContentMap}
	 * 
	 * @return constructed {@link InformationContentMap}
	 */
	public InformationContentMap build();

}
