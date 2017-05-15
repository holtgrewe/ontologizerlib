package de.ontologizer.immutable.graph;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Implementation of an immutable directed graph.
 *
 * <h3>Construction</h3>
 *
 * <p>
 * You can use the functions
 * {@link #construct(Collection, Collection, boolean)},
 * {@link #construct(Collection, Collection)},
 * {@link #construct(Collection, boolean)}, or {@link #construct(Collection)}
 * for constructing the graph from a set of edges (and optionally a set of
 * vertices). Alternative, you can use the {@link Builder} class for iterative
 * construction.
 * </p>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class ImmutableDirectedGraph<VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>>
		implements
			DirectedGraph<VertexType, EdgeType>,
			ShallowCopyable<ImmutableDirectedGraph<VertexType, EdgeType>> {

	private static final long serialVersionUID = 1L;

	private ImmutableMap<VertexType, VertexEdgeList<VertexType, EdgeType>> edgeLists;

	/**
	 * Construct and return a {@link Builder} helper object.
	 *
	 * @return Freshly constructed {@link Builder} object.
	 */
	public static <VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>> Builder<VertexType, EdgeType> builder(
			Edge.Factory<VertexType, EdgeType> edgeFactory) {
		return new Builder<VertexType, EdgeType>(edgeFactory);
	}

	/**
	 * Construct a new {@link ImmutableDirectedGraph} from a collection of
	 * vertices and edges.
	 *
	 * @param<VertexType> Type
	 *                        to use for graph vertices
	 * @param<EdgeType> Type
	 *                      to use for graph edges
	 * 
	 * @param vertices
	 *            {@link Collection} of <code>Vertex</code> objects to use for
	 *            construction
	 * @param edges
	 *            {@link Collection} of <code>Edge</code> objects to use for
	 *            construction
	 * @param checkCompatibility
	 *            whether or not to check vertex and edge list to be compatible
	 * @return the built {@link ImmutableDirectedGraph}
	 */
	public static <VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>> ImmutableDirectedGraph<VertexType, EdgeType> construct(
			Collection<VertexType> vertices, Collection<EdgeType> edges,
			boolean checkCompatibility) {
		// Check compatibility if asked for
		if (checkCompatibility) {
			checkCompatibility(vertices, edges);
		}
		// Create copy of immutable edges
		List<EdgeType> immutableEdges = new ArrayList<>();
		for (EdgeType edge : edges) {
			immutableEdges.add(edge.shallowCopy());
		}
		return new ImmutableDirectedGraph<VertexType, EdgeType>(vertices,
				immutableEdges);
	}

	/**
	 * Construct a new {@link ImmutableDirectedGraph} from a collection edges.
	 *
	 * <p>
	 * The edge list is automatically inferred from the edges' vertices.
	 * </p>
	 *
	 * @param edges
	 *            {@link Collection} of <code>Edge</code> objects to use for
	 *            construction
	 * @param checkCompatibility
	 *            whether or not to check vertex and edge list to be compatible
	 * @return the built {@link ImmutableDirectedGraph}
	 */
	public static <VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>> ImmutableDirectedGraph<VertexType, EdgeType> construct(
			Collection<EdgeType> edges, boolean checkCompatibility) {
		// Collect the vertices in the same order as in edges
		List<VertexType> vertices = new ArrayList<VertexType>();
		Set<VertexType> vertexSet = new HashSet<VertexType>();
		for (Edge<VertexType> edge : edges) {
			if (!vertexSet.contains(edge.getSource())) {
				vertexSet.add(edge.getSource());
				vertices.add(edge.getSource());
			}
			if (!vertexSet.contains(edge.getDest())) {
				vertexSet.add(edge.getDest());
				vertices.add(edge.getDest());
			}
		}
		// Forward to implementation
		return construct(vertices, edges, checkCompatibility);
	}

	/**
	 * Construct a new {@link ImmutableDirectedGraph} from a collection of
	 * vertices and edges.
	 *
	 * <p>
	 * This is just a forward to <code>construct(vertices, edges, false);</code>
	 * </p>
	 */
	public static <VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>> ImmutableDirectedGraph<VertexType, EdgeType> construct(
			Collection<VertexType> vertices, Collection<EdgeType> edges) {
		return construct(vertices, edges, false);
	}

	/**
	 * Construct a new {@link ImmutableDirectedGraph} from a collection of
	 * edges.
	 *
	 * <p>
	 * This is just a forward to <code>construct(edges, false);</code>
	 * </p>
	 */
	public static <VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>> ImmutableDirectedGraph<VertexType, EdgeType> construct(
			Collection<EdgeType> edges) {
		return construct(edges, false);
	}

	/**
	 * This constructor is used internally for constructing via the static
	 * <code>create</code> functions.
	 *
	 * @param vertices
	 *            to use for constructing the graph with
	 * @param edges
	 *            to use for constructing the graph with.
	 */
	private ImmutableDirectedGraph(Collection<VertexType> vertices,
			Collection<EdgeType> edges) {
		// Construct mapping from vertex to builder
		Map<VertexType, VertexEdgeList.Builder<VertexType, EdgeType>> builders = new HashMap<VertexType, VertexEdgeList.Builder<VertexType, EdgeType>>();
		for (VertexType v : vertices) {
			builders.put(v, VertexEdgeList.<VertexType, EdgeType>builder());
		}
		// Fill edge builders
		for (EdgeType e : edges) {
			builders.get(e.getSource()).addOutEdge(e);
			builders.get(e.getDest()).addInEdge(e);
		}
		// Fill ImmutableMap builder and construct
		com.google.common.collect.ImmutableMap.Builder<VertexType, VertexEdgeList<VertexType, EdgeType>> builder = ImmutableMap
				.<VertexType, VertexEdgeList<VertexType, EdgeType>>builder();
		for (Entry<VertexType, VertexEdgeList.Builder<VertexType, EdgeType>> e : builders
				.entrySet()) {
			builder.put(e.getKey(), e.getValue().build());
		}
		this.edgeLists = builder.build();
	}

	/**
	 * Check compatibility of <code>vertices</code> and <code>edges</code>,
	 * i.e., there must not be a vertex in <code>edges</code> that is not
	 * present in <code>vertices</code>.
	 *
	 * @raises VerticesAndEdgesIncompatibleException in case of
	 *         incompatibilities.
	 */
	private static <Vertex> void checkCompatibility(Collection<Vertex> vertices,
			Collection<? extends Edge<Vertex>> edges) {
		Set<Vertex> vertexSet = new HashSet<>(vertices);
		for (Edge<Vertex> edge : edges) {
			if (!vertexSet.contains(edge.getSource())) {
				throw new VerticesAndEdgesIncompatibleException(
						"Unknown source edge in edge " + edge);
			}
			if (!vertexSet.contains(edge.getDest())) {
				throw new VerticesAndEdgesIncompatibleException(
						"Unknown dest edge in edge " + edge);
			}
			if (edge.getSource() == edge.getDest()) {
				throw new VerticesAndEdgesIncompatibleException(
						"Self-loop edge " + edge);
			}
		}

		// TODO: ensure the graph is simple?
	}

	@Override
	public ImmutableDirectedGraph<VertexType, EdgeType> shallowCopy() {
		return subGraph(edgeLists.keySet());
	}

	@Override
	public boolean containsVertex(VertexType v) {
		return edgeLists.containsKey(v);
	}

	@Override
	public int countVertices() {
		return edgeLists.size();
	}

	@Override
	public Iterator<VertexType> vertexIterator() {
		return edgeLists.keySet().iterator();
	}

	@Override
	public Collection<VertexType> getVertices() {
		return edgeLists.keySet();
	}

	@Override
	public boolean containsEdge(VertexType source, VertexType dest) {
		return getEdge(source, dest) != null;
	}

	@Override
	public EdgeType getEdge(VertexType source, VertexType dest) {
		final VertexEdgeList<VertexType, EdgeType> edgeList = edgeLists
				.get(source);
		for (EdgeType e : edgeList.getOutEdges()) {
			if (e.getSource().equals(source) && e.getDest().equals(dest)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public int countEdges() {
		int result = 0;
		for (VertexEdgeList<VertexType, EdgeType> lst : edgeLists.values()) {
			result += lst.getInEdges().size();
		}
		return result;
	}

	@Override
	public Iterator<EdgeType> edgeIterator() {

		return new Iterator<EdgeType>() {
			UnmodifiableIterator<VertexEdgeList<VertexType, EdgeType>> outerIt = edgeLists
					.values().iterator();
			private UnmodifiableIterator<EdgeType> innerIt = null;

			@Override
			public boolean hasNext() {
				if (innerIt != null && innerIt.hasNext()) {
					return true;
				} else {
					while (innerIt == null || !innerIt.hasNext()) {
						if (!outerIt.hasNext()) {
							return false;
						} else {
							innerIt = outerIt.next().getOutEdges().iterator();
							if (innerIt.hasNext()) {
								return true;
							}
						}
					}
					return innerIt.hasNext();
				}
			}

			@Override
			public EdgeType next() {
				return innerIt.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int getInDegree(VertexType v) {
		return edgeLists.get(v).getInEdges().size();
	}

	@Override
	public Iterator<EdgeType> inEdgeIterator(VertexType v) {
		return edgeLists.get(v).getInEdges().iterator();
	}

	@Override
	public int getOutDegree(VertexType v) {
		return edgeLists.get(v).getOutEdges().size();
	}

	@Override
	public Iterator<EdgeType> outEdgeIterator(VertexType v) {
		return edgeLists.get(v).getOutEdges().iterator();
	}

	@Override
	public Iterator<VertexType> parentVertexIterator(VertexType v) {
		final Iterator<? extends Edge<VertexType>> iter = outEdgeIterator(v);

		return new Iterator<VertexType>() {
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public VertexType next() {
				return iter.next().getDest();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Iterator<VertexType> childVertexIterator(VertexType v) {
		final Iterator<? extends Edge<VertexType>> iter = inEdgeIterator(v);

		return new Iterator<VertexType>() {
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public VertexType next() {
				return iter.next().getSource();
			}

			@Override
			public void remove() {
			}
		};
	}

	@Override
	public ImmutableDirectedGraph<VertexType, EdgeType> subGraph(
			Collection<VertexType> vs) {
		Set<VertexType> argVertexSet = ImmutableSet.copyOf(vs);

		// Create subset of vertices and edges
		Set<VertexType> vertexSubset = new HashSet<VertexType>();
		Iterator<VertexType> vIt = vertexIterator();
		while (vIt.hasNext()) {
			final VertexType v = vIt.next();
			if (argVertexSet.contains(v)) {
				vertexSubset.add(v);
			}
		}
		Set<EdgeType> edgeSubset = new HashSet<EdgeType>();
		Iterator<EdgeType> eIt = edgeIterator();
		while (eIt.hasNext()) {
			EdgeType e = eIt.next();
			if (argVertexSet.contains(e.getSource())
					&& argVertexSet.contains(e.getDest())) {
				edgeSubset.add(e.shallowCopy());
			}
		}

		// Construct sub graph
		return construct(vertexSubset, edgeSubset);
	}

	@Override
	public String toString() {
		return "ImmutableDirectedGraph [edgeLists=" + edgeLists + "]";
	}

	/**
	 * Helper class for iteratively constructing immutable directed graphs.
	 *
	 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel
	 *         Holtgrewe</a>
	 */
	public static class Builder<VertexType, EdgeType extends Edge<VertexType> & ShallowCopyable<EdgeType>> {

		private final List<VertexType> vertices = new ArrayList<VertexType>();
		private final List<EdgeType> edges = new ArrayList<EdgeType>();
		private final Edge.Factory<VertexType, EdgeType> edgeFactory;

		/**
		 * Construct with {@link Edge.Factory} for constructing
		 * appropriately-typed {@link Edge}s.
		 * 
		 * @param edgeFactory
		 *            The {@link Edge.Factory} to use
		 */
		public Builder(Edge.Factory<VertexType, EdgeType> edgeFactory) {
			this.edgeFactory = edgeFactory;
		}

		/**
		 * Add <code>Vertex</code> to add to the builder.
		 *
		 * @param v
		 *            <code>Vertex</code> to add to the builder.
		 */
		public void addVertex(VertexType v) {
			vertices.add(v);
		}

		/**
		 * Add {@link Collection} of <code>Vertex</code> objects to the builder.
		 *
		 * @param vs
		 *            <code>Vertex</code> objects to add to the builder.
		 */
		public void addVertices(Collection<VertexType> vs) {
			for (VertexType v : vs) {
				vertices.add(v);
			}
		}

		/**
		 * Add {@link Edge} to the builder.
		 *
		 * @param e
		 *            {@link Edge} to add to the builder.
		 */
		public void addEdge(EdgeType e) {
			edges.add(e);
		}

		/**
		 * Construct and add new {@link ImmutableEdge} between
		 * <code>source</code> and <code>dest</code>, any label/weights are set
		 * to default values.
		 *
		 * @param source
		 *            Source vertex for the directed edge
		 * @param dest
		 *            Destination vertex for the directed edge
		 */
		public void addEdge(VertexType source, VertexType dest) {
			edges.add(edgeFactory.construct(source, dest));
		}

		/**
		 * Build and return new {@link ImmutableDirectedGraph}.
		 *
		 * @param checkConsistency
		 *            whether or not to check consistency of vertex and edge
		 *            list.
		 * @return freshly built {@link ImmutableDirectedGraph}
		 */
		public ImmutableDirectedGraph<VertexType, EdgeType> build(
				boolean checkConsistency) {
			return ImmutableDirectedGraph.construct(vertices, edges,
					checkConsistency);
		}

		/**
		 * Build and return new {@link ImmutableDirectedGraph}.
		 *
		 * <p>
		 * This is a forward to <code>build(false)</code>.
		 * </p>
		 */
		public ImmutableDirectedGraph<VertexType, EdgeType> build() {
			return build(false);
		}

	}

}
