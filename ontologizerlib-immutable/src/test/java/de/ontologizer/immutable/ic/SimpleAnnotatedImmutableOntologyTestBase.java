package de.ontologizer.immutable.ic;

import de.ontologizer.immutable.ontology.ImmutableOntology;
import de.ontologizer.immutable.ontology.ImmutableTermContainer;
import java.util.ArrayList;
import java.util.List;
import ontologizer.ontology.ParentTermID;
import ontologizer.ontology.Prefix;
import ontologizer.ontology.Term;
import ontologizer.ontology.TermID;
import ontologizer.ontology.TermRelation;
import ontologizer.types.ByteString;
import org.junit.Before;

/**
 * Base class for JUnit test suits that provides a simple annotaed
 * {@link ImmutableOntology}.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class SimpleAnnotatedImmutableOntologyTestBase {

	protected ImmutableTermContainer termContainer;

	protected ImmutableOntology immutableOntology;

	protected List<LabeledTerm> labeledTerms;

	protected AnnotatedTermsInformationContentMapFactory factory;

	protected List<TermID> termIDs;

	@Before
	public void setUp() {
		// Construct
		//
		// XY:0 (root) ----- A .
		// XY:1 IS_A XY:0 -- A .
		// XY:2 IS_A XY:0 -- A B
		// XY:3 IS_A XY:2 -- A .
		// XY:4 IS_A XY:2 -- . B

		Prefix prefix = new Prefix("XY");

		termIDs = new ArrayList<TermID>();
		termIDs.add(new TermID(prefix, 0));
		termIDs.add(new TermID(prefix, 1));
		termIDs.add(new TermID(prefix, 2));
		termIDs.add(new TermID(prefix, 3));
		termIDs.add(new TermID(prefix, 4));

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Term(termIDs.get(0), new ByteString("name 0")));
		terms.add(new Term(termIDs.get(1), new ByteString("name 1"),
				new ParentTermID(termIDs.get(0), TermRelation.IS_A)));
		terms.add(new Term(termIDs.get(2), new ByteString("name 2"),
				new ParentTermID(termIDs.get(0), TermRelation.IS_A)));
		terms.add(new Term(termIDs.get(3), new ByteString("name 3"),
				new ParentTermID(termIDs.get(2), TermRelation.IS_A)));
		terms.add(new Term(termIDs.get(4), new ByteString("name 4"),
				new ParentTermID(termIDs.get(2), TermRelation.IS_A)));

		termContainer = new ImmutableTermContainer(terms,
				new ByteString("2010-01-01"), new ByteString("2.0"));

		immutableOntology = ImmutableOntology.constructFromTerms(termContainer);

		labeledTerms = new ArrayList<LabeledTerm>();
		labeledTerms.add(new LabeledTerm(terms.get(0), new TermLabel("A")));
		labeledTerms.add(new LabeledTerm(terms.get(1), new TermLabel("A")));
		labeledTerms.add(new LabeledTerm(terms.get(2), new TermLabel("A")));
		labeledTerms.add(new LabeledTerm(terms.get(3), new TermLabel("A")));
		labeledTerms.add(new LabeledTerm(terms.get(2), new TermLabel("B")));
		labeledTerms.add(new LabeledTerm(terms.get(4), new TermLabel("B")));

		factory = new AnnotatedTermsInformationContentMapFactory(
				immutableOntology, labeledTerms);
	}

	private static class LabeledTerm implements AnnotatedTerm {

		private final Term term;

		private final TermLabel termLabel;

		public LabeledTerm(Term term, TermLabel termLabel) {
			this.term = term;
			this.termLabel = termLabel;
		}

		public Term getTerm() {
			return term;
		}

		@Override
		public Label getLabel() {
			return termLabel;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((term == null) ? 0 : term.hashCode());
			result = prime * result
					+ ((termLabel == null) ? 0 : termLabel.hashCode());
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
			LabeledTerm other = (LabeledTerm) obj;
			if (term == null) {
				if (other.term != null)
					return false;
			} else if (!term.equals(other.term))
				return false;
			if (termLabel == null) {
				if (other.termLabel != null)
					return false;
			} else if (!termLabel.equals(other.termLabel))
				return false;
			return true;
		}

	}

	private static class TermLabel implements Label {

		private final String label;

		public TermLabel(String label) {
			super();
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		@Override
		public String toString() {
			return "TermLabel [label=" + label + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((label == null) ? 0 : label.hashCode());
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
			TermLabel other = (TermLabel) obj;
			if (label == null) {
				if (other.label != null)
					return false;
			} else if (!label.equals(other.label))
				return false;
			return true;
		}

	}

}
