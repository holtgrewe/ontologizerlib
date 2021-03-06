package ontologizer.ontology;

import static ontologizer.ontology.OBOKeywords.TERM_KEYWORDS;

import java.io.PrintStream;
import java.util.Iterator;

import sonumina.math.graph.AbstractGraph.DotAttributesProvider;
import sonumina.math.graph.DirectedGraph;
import sonumina.math.graph.Edge;

public class OBOByteLineScannerGenerator
{
	class StringEdge extends Edge<Integer>
	{
		private String l;

		public StringEdge(Integer source, Integer dest, String l)
		{
			super(source, dest);

			this.l = l;
		}

		public String getL() {
			return l;
		}
	}

	/**
	 * Writes selection - action code to the given out.
	 *
	 * @param out where to put the output
	 * @param current the index of the current node
	 * @param tree the tree
	 * @param indentLevel the level of code intend
	 * @param depth the current node depth within the tree
	 * @param pos the position within the tree
	 * @param name
	 */
	private void writeCode(PrintStream out, Integer current, DirectedGraph<Integer> tree, int indentLevel, int depth, int pos, String name)
	{
		boolean first = true;
		Iterator<Edge<Integer>> iter = tree.getOutEdges(current);

		while (iter.hasNext())
		{
			StringEdge se = (StringEdge)iter.next();

			for (int i=0;i<indentLevel;i++)
				out.print("\t");

			if (!first) out.print("else ");

			out.print("if (");

			if (depth != 0)
			{
				for (int i=0;i<se.l.length();i++)
				{
					int plus = pos+i-1;

					out.print(String.format("toLower(buf[keyStart%s]) == %d && ",plus!=0?String.format(" + %d", plus):"",se.l.getBytes()[i]));
				}
				out.println(String.format("true) /* %s */",se.l));
			} else
			{
				out.println(String.format("keyLen==%d)",se.l.getBytes()[0]));
			}

			for (int i=0;i<indentLevel;i++)
				out.print("\t");
			out.println("{");
			writeCode(out,se.getDest(),tree,indentLevel+1,depth+1,pos + se.l.length(),name + se.l);

			for (int i=0;i<indentLevel;i++)
				out.print("\t");
			out.println("}");

			first = false;
		}
		if (first)
		{
			/* We are at a leaf */
			for (int i=0;i<indentLevel;i++)
				out.print("\t");
			out.println(String.format("parse_%s(buf, valueStart, valueLen);",name.substring(1)));
		}
	}


	/**
	 * Try to collapse the given tree at the given node.
	 *
	 * @param current
	 * @param tree
	 */
	private void collapse(Integer current, DirectedGraph<Integer> tree)
	{
		int currentOutDegree = tree.getOutDegree(current);
		if (currentOutDegree > 1)
		{
			Iterator<Edge<Integer>> iter = tree.getOutEdges(current);
			while (iter.hasNext())
				collapse(iter.next().getDest(),tree);
		} else if (currentOutDegree == 1)
		{
			StringEdge e = (StringEdge)tree.getOutEdges(current).next();
			Integer next = e.getDest();
			int nextOutDegree = tree.getOutDegree(next);
			if (nextOutDegree == 1)
			{
				StringEdge ne = (StringEdge)tree.getOutEdges(next).next();
				Integer nextnext = ne.getDest();
				e.l += ne.l;

				/* Move all out edges of next next to next */
				Iterator<Edge<Integer>> nextnextIter = tree.getOutEdges(nextnext);
				while (nextnextIter.hasNext())
				{
					StringEdge se = (StringEdge)nextnextIter.next();
					tree.addEdge(new StringEdge(next,se.getDest(),se.l));
				}

				/* Finally, remove next next */
				tree.removeVertex(nextnext);

				collapse(current,tree);
			} else
			{
				collapse(next,tree);
			}
		}
	}


	/**
	 * Try to identify the neighbor of the current node.
	 *
	 * @param tree
	 * @param current
	 * @param c
	 * @return the neighbor or null if it is non-existent.
	 */
	private Integer followEdge(final DirectedGraph<Integer> tree, Integer current, byte c)
	{
		Integer next = null;
		Iterator<Edge<Integer>> iter = tree.getOutEdges(current);
		while (iter.hasNext())
		{
			StringEdge se = (StringEdge)iter.next();
			if (se.l.getBytes()[0] == c)
			{
				next = se.getDest();
				break;
			}
		}
		return next;
	}

	private int currentVertexIndex = 1;

	/**
	 * Insert the a new edge to the given byte into the tree if it is
	 * not already present.
	 *
	 * @param tree
	 * @param current
	 * @param c
	 * @return the node to which the edge points to.
	 */
	private Integer insertEdge(final DirectedGraph<Integer> tree, Integer current, byte c)
	{
		Integer next = followEdge(tree, current, c);
		if (next == null)
		{
			next = new Integer(currentVertexIndex++);
			tree.addVertex(next);
			StringEdge se = new StringEdge(current, next, ((char)c)+"");
			tree.addEdge(se);
		}
		return next;
	}


	/**
	 * Generate Java code for if clauses.
	 */
	public void generateKeywordIfClauses()
	{
		final DirectedGraph<Integer> tree = new DirectedGraph<Integer>();

		Integer root = new Integer(0);
		tree.addVertex(root);

		for (byte [] keyword : TERM_KEYWORDS)
		{
			/* First level is the length of the keyword */
			Integer current = insertEdge(tree, root, (byte)keyword.length);

			for (int j=0;j<keyword.length;j++)
			{
				byte c = keyword[j];
				current = insertEdge(tree, current, c);
			}
		}

		/* Collapse */
		collapse(root,tree);

		System.out.println("private void readTermValue(byte[] buf, int keyStart, int keyLen, int valueStart, int valueLen)");
		System.out.println("{");
		writeCode(System.out, root,tree,1,0,0,"");
		System.out.println("}");

		tree.writeDOT(new PrintStream(System.out), new DotAttributesProvider<Integer>()
				{
					@Override
					public String getDotEdgeAttributes(Integer src, Integer dest) {

						return "label=\"" + ((StringEdge)tree.getEdge(src, dest)).getL() +  "\"";
					}
				});
	}

	public static void main(String[] args)
	{
		new OBOByteLineScannerGenerator().generateKeywordIfClauses();
	}
}
