package de.ontologizer.immutable.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Constants for GO (gene ontology)
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class GoConstants {

	/** ID of artificial root after loading. */
	public static String ROOT_ID = "GO:0000000";

	/** Name of GO term "molecular function" */
	public static String MOLECULAR_FUNCTION_ID = "GO:0003674";

	/** Name of GO term "biological process" */
	public static String BIOLOGICAL_PROCESS_ID = "GO:0000004";

	/** Name of GO term "cellular component" */
	public static String CELLULAR_COMPONENT_ID = "GO:0022607";

	/** Name of GO term "molecular function" */
	public static String MOLECULAR_FUNCTION_NAME = "molecular function";

	/** Name of GO term "biological process" */
	public static String BIOLOGICAL_PROCESS_NAME = "biological process";

	/** Name of GO term "cellular component" */
	public static String CELLULAR_COMPONENT_NAME = "cellular component";

	/**
	 * Obtain copy of set of level 1 term IDs of GO.
	 * 
	 * @return {@link Set} with copy of level 1 term names.
	 */
	public static Set<String> getLevel1TermIDs() {
		return new HashSet<String>(Arrays.asList(MOLECULAR_FUNCTION_ID,
				BIOLOGICAL_PROCESS_ID, CELLULAR_COMPONENT_ID));
	}

	/**
	 * Obtain copy of set of level 1 term names of GO.
	 * 
	 * @return {@link Set} with copy of level 1 term names.
	 */
	public static Set<String> getLevel1TermNames() {
		return new HashSet<String>(Arrays.asList(MOLECULAR_FUNCTION_NAME,
				BIOLOGICAL_PROCESS_NAME, CELLULAR_COMPONENT_NAME));
	}

}
