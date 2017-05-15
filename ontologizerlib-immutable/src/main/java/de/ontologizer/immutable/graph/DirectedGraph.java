package de.ontologizer.immutable.graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Interface for directed graph classes with read access.
 *
 * <p>
 * All graphs in OntologizerLib are simple, having two edges in the same
 * direction between two vertices are not allowed.
 * </p>
 *
 * <p>
 * Vertices must implement <code>equals()</code> and <code>hashValue()</code>.
 * Vertices with the same value (by <code>equals</code>) are not allowed.
 * </p>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface DirectedGraph<VertexType, EdgeType extends Edge<? extends VertexType>>
		extends
			Serializable {

	/**
	 * Query whether the given vertex is contained in the graph.
	 *
	 * @param v
	 *            <code>Vertex</code> to query for
	 * @return whether <code>v</code> is contained in the graph.
	 */
	public boolean containsVertex(VertexType v);

	/**
	 * Return number of vertices in the graph.
	 *
	 * @return Number of vertices in the graph.
	 */
	public int countVertices();

	/**
	 * Query for {@link Collection} of vertices.
	 *
	 * @return {@link Collection} of vertices.
	 */
	public Collection<VertexType> getVertices();

	/**
	 * Build and return iterator over all vertices in the graph.
	 *
	 * @return {@link Iterator} over all vertices.
	 */
	public Iterator<VertexType> vertexIterator();

	/**
	 * Query for an edge being in in the graph.
	 *
	 * @param source
	 *            source <code>Vertex</code> of edge to query for
	 * @param dest
	 *            destination <code>Vertex</code> of edge to query for
	 * @return <code>true</code> if the edge could be found, <code>false</code>
	 *         otherwise
	 */
	public boolean containsEdge(VertexType source, VertexType dest);

	/**
	 * Query for the edge between <code>source</code> and <code>dest</code>.
	 *
	 * @param source
	 *            source <code>Vertex</code> of edge to query for
	 * @param dest
	 *            destination <code>Vertex</code> of the edge to query for
	 * @return {@link Edge} between <code>source</code> and <code>dest</code>,
	 *         <code>null</code> if no such edge could be found
	 */
	public EdgeType getEdge(VertexType source, VertexType dest);

	/**
	 * Query for number of edges.
	 *
	 * @return number of edges
	 */
	public int countEdges();

	/**
	 * Build and return {@link Iterator} over all edges.
	 *
	 * @return {@link Iterator} over all edges in the graph.
	 */
	public Iterator<EdgeType> edgeIterator();

	/**
	 * Query for in-degree of <code>Vertex</code>.
	 *
	 * @param v
	 *            <code>Vertex</code> to use for the query.
	 * @return number of incoming edges into <code>v</code>.
	 */
	public int getInDegree(VertexType v);

	/**
	 * Construct and return in-edge iterator.
	 *
	 * @param v
	 *            <code>VertexType</code> to use for query
	 * @return In-edge iterator for <code>v</code>
	 */
	public Iterator<EdgeType> inEdgeIterator(VertexType v);

	/**
	 * Query for out-degree of <code>Vertex</code>.
	 *
	 * @param v
	 *            <code>Vertex</code> to use for the query.
	 * @return number of outgoing edges from <code>v</code>.
	 */
	public int getOutDegree(VertexType v);

	/**
	 * Construct and return out-edge iterator.
	 *
	 * @param v
	 *            Vertex to get out edge iterator for
	 * @return Out-edge iterator for <code>v</code>
	 */
	public Iterator<EdgeType> outEdgeIterator(VertexType v);

	/**
	 * Build and return {@link Iterator} of vertices connected by incoming
	 * edges.
	 *
	 * @param v
	 *            <code>Vertex</code> to use for the query.
	 * @return {@link Iterator} over all vertices connected by incoming edges.
	 */
	public Iterator<VertexType> parentVertexIterator(VertexType v);

	/**
	 * Build and return {@link Iterator} of vertices connected by outgoing
	 * edges.
	 *
	 * @param v
	 *            <code>Vertex</code> to use for the query.
	 * @return {@link Iterator} over all vertices connected by outgoing edges.
	 */
	public Iterator<VertexType> childVertexIterator(VertexType v);

	/**
	 * Build and return <code>DirectedGraph</code> by restricting the vertex set
	 * to <code>vertices</code>.
	 *
	 * @param vertices
	 *            {@link Collection} of <code>Vertex</code> objects to limit to.
	 * @return <code>DirectedGraph</code> induced by restricting the vertex set
	 *         to <code>vertices</code>.
	 */
	public DirectedGraph<VertexType, EdgeType> subGraph(
			Collection<VertexType> vertices);

}
