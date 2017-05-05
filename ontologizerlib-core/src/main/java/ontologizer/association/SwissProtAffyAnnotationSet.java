package ontologizer.association;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ontologizer.ontology.TermID;
import ontologizer.types.ByteString;

/**
 * Set containing all swiss prot ids linked to affy ids and annotaions.
 *
 * @author Sebastian Bauer
 */
public class SwissProtAffyAnnotationSet implements Iterable<SwissProtAffyAnnotation>, Serializable
{
	private static final long serialVersionUID = 1L;

	private HashMap<ByteString,SwissProtAffyAnnotation> map;

	public SwissProtAffyAnnotationSet()
	{
		map = new HashMap<ByteString,SwissProtAffyAnnotation>();
	}

	/**
	 * Add a new swissprot id -&gt; affyID -&gt; goID mappping.
	 *
	 * @param swissProtID
	 * @param affyID
	 * @param goIDs
	 */
	public void add(ByteString swissProtID, ByteString affyID, List<TermID> goIDs)
	{
		SwissProtAffyAnnotation an = map.get(swissProtID);
		if (an == null)
		{
			an = new SwissProtAffyAnnotation(swissProtID);
			map.put(swissProtID,an);
		}
		an.addAffyID(affyID);
		for (TermID id : goIDs)
			an.addTermID(id);
	}

	public Iterator<SwissProtAffyAnnotation> iterator()
	{
		return map.values().iterator();
	}
}
