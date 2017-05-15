package de.ontologizer.immutable.ic;

import java.util.Map;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;

/**
 * Interface for implementing mappings from {@link Term} to information content.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface InformationContentMap extends Map<TermID, Double> {

}
