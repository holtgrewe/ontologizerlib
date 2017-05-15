package de.ontologizer.immutable.ic;

import java.util.HashMap;
import java.util.Map;
import ontologizer.ontology.TermID;

/**
 * Default implementation for {@link InformationContentMap}.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class DefaultInformationContentMap extends HashMap<TermID, Double>
		implements
			InformationContentMap {

	private static final long serialVersionUID = 1L;

	public DefaultInformationContentMap() {
		super();
	}

	public DefaultInformationContentMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public DefaultInformationContentMap(int initialCapacity) {
		super(initialCapacity);
	}

	public DefaultInformationContentMap(
			Map<? extends TermID, ? extends Double> m) {
		super(m);
	}

}
