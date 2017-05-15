package de.ontologizer.immutable.ic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnnotatedTermsInformationContentMapFactoryTest
		extends
			SimpleAnnotatedImmutableOntologyTestBase {

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void test() {
		InformationContentMap contentMap = factory.build();

		Assert.assertEquals(0.4055, contentMap.get(termIDs.get(0)), 0.001);
		Assert.assertEquals(1.099, contentMap.get(termIDs.get(1)), 0.001);
		Assert.assertEquals(0.4055, contentMap.get(termIDs.get(2)), 0.001);
		Assert.assertEquals(1.099, contentMap.get(termIDs.get(3)), 0.001);
		Assert.assertEquals(1.099, contentMap.get(termIDs.get(4)), 0.001);
	}

}
