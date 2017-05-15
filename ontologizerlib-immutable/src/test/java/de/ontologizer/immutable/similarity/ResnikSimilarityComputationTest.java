package de.ontologizer.immutable.similarity;

import de.ontologizer.immutable.ic.InformationContentMap;
import de.ontologizer.immutable.ic.SimpleAnnotatedImmutableOntologyTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResnikSimilarityComputationTest
		extends
			SimpleAnnotatedImmutableOntologyTestBase {

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void test() {
		InformationContentMap contentMap = factory.build();
		ResnikSimilarityComputation computation = new ResnikSimilarityComputation(
				immutableOntology, contentMap);

		Assert.assertEquals(0.4055,
				computation.computeScore(termIDs.get(1), termIDs.get(2)),
				0.001);
		Assert.assertEquals(0.4055,
				computation.computeScore(termIDs.get(1), termIDs.get(3)),
				0.001);
		Assert.assertEquals(0.4055,
				computation.computeScore(termIDs.get(1), termIDs.get(4)),
				0.001);
		Assert.assertEquals(0.4055,
				computation.computeScore(termIDs.get(3), termIDs.get(4)),
				0.001);
	}

}
