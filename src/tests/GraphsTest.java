package tests;

import graphs.Edge;
import graphs.IGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import example.DirectedGraph;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Runs a series of tests to verify a directed graph
 * structure using the IGraph<V> interface.
 *
 * DO NOT ALTER THIS FILE!
 *
 * @author Josh Archer
 * @version 1.0
 */
public class GraphsTest
{
    private static final int DEFAULT_WEIGHT = 1;
    private static final int TEST_WEIGHT = 15;
    private static String[] testVerts = {"A", "B", "C", "D", "E", "F", "G", "H",
                                  "I", "J", "K", "L"};
    private IGraph<String> graph;

    /**
     * Creates a new graph for each test.
     */
    @BeforeEach
    public void setup()
    {
        //instantiate the DirectedGraph class here...
        graph = new DirectedGraph<>();
    }

    private void addFewVertices()
    {
        for (String letter : testVerts)
        {
            graph.addVertex(letter);
        }
    }

    private void addFewEdges(int weight)
    {
        List<Edge<String>> edges = getTestEdges(weight);
        for (Edge<String> edge : edges)
        {
            graph.addEdge(edge.getSource(), edge.getDestination(), edge.getWeight());
        }
    }

    private List<Edge<String>> getTestEdges(int weight)
    {
        List<Edge<String>> edges = new ArrayList<>();

        //add edges between letters that are adjacent (A-B, B-C, ... , J-K, K-L)
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];
            edges.add(new Edge<>(source, destination, weight));
        }

        return edges;
    }

    /**
     * Verifies that vertices are added to the graph.
     */
    @Test
    public void addVertexTest()
    {
        addFewVertices();

        //verify size
        assertEquals(graph.vertexSize(), testVerts.length);

        //verify contains
        for (String letter : testVerts)
        {
            assertTrue(graph.containsVertex(letter));
        }
    }

    /**
     * Checks the return type of Graph.addVertex(), making sure
     * that duplicate vertices are not added to the graph.
     */
    @Test
    public void testDuplicateVertex()
    {
        addFewVertices();

        assertTrue(graph.addVertex("M"));
        assertFalse(graph.addVertex("M"));
    }

    /**
     * Verifies that edges can be added to the graph.
     */
    @Test
    public void addEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        int numEdges = testVerts.length - 1;

        //verify size
        assertEquals(graph.vertexSize(), testVerts.length);
        assertEquals(graph.edgeSize(), numEdges);

        //make sure all expected edges are present
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            assertTrue(graph.containsEdge(source, destination));
        }
    }

    /**
     * Checks whether adding an edge with a vertex not in the map is recognized
     * by the return type of the addEdge() method. (i.e. the method should return
     * true when the edge is added and false otherwise)
     */
    @Test
    public void addEdgeWithoutVertexTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //add an edge with both vertices
        assertTrue(graph.addEdge(testVerts[0], testVerts[testVerts.length - 1], DEFAULT_WEIGHT));

        //add edges with missing vertices
        assertFalse(graph.addEdge(testVerts[0], "M", DEFAULT_WEIGHT));
        assertFalse(graph.addEdge("M", testVerts[0], DEFAULT_WEIGHT));
        assertFalse(graph.addEdge("P", "M", DEFAULT_WEIGHT));
    }

    /**
     * Checks whether the graph reports the unsuccessful addition
     * of duplicate edges.
     */
    @Test
    public void addDuplicateEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        int sizeBefore = graph.vertexSize();
        assertNotEquals(0, sizeBefore);

        //add the duplicate and verify that the graph hasn't changed
        assertFalse(graph.addEdge(testVerts[0], testVerts[1], DEFAULT_WEIGHT));

        int sizeAfter = graph.vertexSize();
        assertEquals(sizeBefore, sizeAfter);
    }

    /**
     * Checks whether the graph recognizes missing vertices.
     */
    @Test
    public void missingVertexTest()
    {
        addFewVertices();
        assertEquals(testVerts.length, graph.vertexSize());

        //verify no false positives
        assertFalse(graph.containsVertex("M"));
    }

    /**
     * Checks whether the graph recognizes missing edges.
     */
    @Test
    public void missingEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        assertEquals(testVerts.length, graph.vertexSize());
        assertEquals(getTestEdges(DEFAULT_WEIGHT).size(), graph.edgeSize());

        //verify no false positives (both of these edges should not be in graph)
        assertFalse(graph.containsEdge("A", "A"));
        assertFalse(graph.containsEdge("A", "A"));

        //force a resize and then verify no false positives
        for (int i = 1; i <= 100; i++)
        {
            graph.addVertex(i + ""); //add "1", "2", ... , "100"
        }

        //verify that we can't find an edge with both missing vertices
        assertFalse(graph.containsEdge("101", "101"));

        //verify that we can't find an edge with one missing vertex
        assertFalse(graph.containsEdge("A", "50"));
        assertFalse(graph.containsEdge("50", "A"));
    }

    /**
     * Verifies that edges are added to the graph in a directed way.
     */
    @Test
    public void directednessTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //verify that pairs only work in one direction
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            assertTrue(graph.containsEdge(source, destination));
            assertFalse(graph.containsEdge(destination, source));
        }
    }

    /**
     * Verifies that Graph.vertices() returns all vertices added to the graph.
     */
    @Test
    public void vertexSetTest()
    {
        //empty vertex set
        assertEquals(0, graph.vertices().size());

        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        String[] vertices = graph.vertices().toArray(new String[0]);
        Arrays.sort(vertices);

        assertArrayEquals(testVerts, vertices);
    }

    /**
     * Verifies that Graph.edges() returns all edges added to the graph.
     */
    @Test
    public void edgeSetTest()
    {
        //empty edge set
        assertEquals(0, graph.edges().size());

        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure the elements in the edge set match
        Set<Edge<String>> edges = graph.edges();
        Set<Edge<String>> others = new HashSet<>();
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            others.add(new Edge<>(source, destination, DEFAULT_WEIGHT));
        }

        //does size match?
        assertEquals(others.size(), edges.size());

        //does each element match?
        for (Edge<String> edge : others)
        {
            assertTrue(edges.contains(edge));
        }
    }

    /**
     * Verifies that edges are not returned with null
     * values in the edge set.
     */
    @Test
    public void edgesNotNullTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        Set<Edge<String>> edges = graph.edges();
        for (Edge<String> edge : edges)
        {
            assertNotNull(edge.getSource());
            assertNotNull(edge.getDestination());
        }
    }

    /**
     * Verifies that edges are not returned with null
     * values after being removed from the graph
     * @param vertex the vertex to remove
     */
    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K"})
    public void edgesNotNullAfterRemoveTest(String vertex)
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        assertTrue(graph.removeVertex(vertex));

        Set<Edge<String>> edges = graph.edges();
        for (Edge<String> edge : edges)
        {
            assertNotNull(edge.getSource());
            assertNotNull(edge.getDestination());
        }
    }

    /**
     * Tests whether edges weights passed to Graph.addEdge() are stored with
     * the correct weights.
     */
    @Test
    public void badWeightTest()
    {
        addFewVertices();
        addFewEdges(TEST_WEIGHT);

        //verify weighted edges
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            assertEquals(TEST_WEIGHT, graph.edgeWeight(source, destination));
        }

        //make sure we can't add an invalid weight
        assertThrows(IllegalArgumentException.class,
                () -> graph.addEdge("A", "B", -DEFAULT_WEIGHT));
        assertThrows(IllegalArgumentException.class,
                () -> graph.addEdge("A", "B", 0));
    }

    /**
     * Verify that positive edge weights can be added to the
     * graph.
     */
    @Test
    public void weightTest()
    {
        addFewVertices();

        assertEquals(testVerts.length, graph.vertexSize());

        //this is a valid edge weight
        assertDoesNotThrow(() -> graph.addEdge("A", "B", 1));
    }

    /**
     * The vertices() method should not return a reference
     * to an internal data structure (breaking encapsulation...)
     */
    @Test
    public void verticesReferenceTest()
    {
        addFewVertices();

        Set<String> verts1 = graph.vertices();
        Set<String> verts2 = graph.vertices();

        assertNotSame(verts1, verts2);
    }

    /**
     * The edges() method should not return a reference
     * to an internal data structure (breaking encapsulation...)
     */
    @Test
    public void edgesReferenceTest()
    {
        addFewVertices();
        addFewEdges(TEST_WEIGHT);

        Set<Edge<String>> edges1 = graph.edges();
        Set<Edge<String>> edges2 = graph.edges();

        assertNotSame(edges1, edges2);
    }

    /**
     * Verifies that missing vertices cannot be removed from the graph.
     */
    @Test
    public void removeMissingVertexTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);
        assertEquals(testVerts.length, graph.vertexSize());
        assertEquals(getTestEdges(DEFAULT_WEIGHT).size(), graph.edgeSize());

        //make sure you cannot remove missing vertex elements
        assertFalse(graph.removeVertex("M"));
    }

    /**
     * Verifies that vertices can reliably be removed from the graph.
     *
     * @param vertex the vertex to remove
     */
    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C", "D", "E", "F", "G", "H",
                            "I", "J", "K"})
    public void removeVertexTest(String vertex)
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure you can remove vertices in graph
        assertTrue(graph.removeVertex(vertex));

        //verify related edges are gone
        List<Edge<String>> edges = getTestEdges(DEFAULT_WEIGHT);
        for (Edge<String> edge : edges)
        {
            if (edge.getDestination().equals(vertex) || edge.getSource().equals(vertex))
            {
                assertFalse(graph.containsEdge(edge.getSource(), edge.getDestination()));
                assertFalse(graph.containsEdge(edge.getDestination(), edge.getSource()));
            }
        }

        assertEquals(testVerts.length - 1, graph.vertexSize());
    }

    /**
     * Verifies that edges are removed when a vertex is removed from the graph.
     * @param vertex the vertex to remove
     */
    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K"})
    public void edgesGoneAfterRemoveTest(String vertex)
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure you can remove vertices in graph
        assertTrue(graph.removeVertex(vertex));

        Set<Edge<String>> edges = graph.edges();
        for (Edge<String> edge : edges)
        {
            assertNotEquals(edge.getSource(), vertex);
            assertNotEquals(edge.getDestination(), vertex);
        }

        assertEquals(testVerts.length - 1, graph.vertexSize());
    }

    /**
     * Verifies that all vertices (and related edges) can
     * be removed from the graph.
     */
    @Test
    public void removeAllVerticesTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //remove each vertex one by one
        for (String vertex : testVerts)
        {
            assertTrue(graph.removeVertex(vertex));
        }

        //no edges should be left
        for (Edge<String> edge : getTestEdges(DEFAULT_WEIGHT))
        {
            assertFalse(graph.containsEdge(edge.getSource(), edge.getDestination()));
        }

        //there should no longer be elements in the graph
        assertEquals(0, graph.vertexSize());
        assertEquals(0, graph.edgeSize());
    }

    /**
     * Verifies that both ends of an edge are removed when
     * a vertex is removed (both rows and cols in the matrix).
     */
    @Test
    public void removeVertexDirectedTest()
    {
        addFewVertices();

        //add a few edges
        List<Edge<String>> testEdges = List.of(
            new Edge<>("A", "B", DEFAULT_WEIGHT),
            new Edge<>("A", "C", DEFAULT_WEIGHT),
            new Edge<>("A", "D", DEFAULT_WEIGHT),
            new Edge<>("B", "A", DEFAULT_WEIGHT),
            new Edge<>("C", "A", DEFAULT_WEIGHT),
            new Edge<>("E", "A", DEFAULT_WEIGHT)
        );

        for (Edge<String> edge : testEdges)
        {
            graph.addEdge(edge.getSource(), edge.getDestination(), edge.getWeight());
        }

        //remove a vertex
        assertTrue(graph.removeVertex("A"));

        //verify that the edge is gone
        for (Edge<String> edge : testEdges)
        {
            assertFalse(graph.containsEdge(edge.getSource(), edge.getDestination()));
        }

        Set<Edge<String>> edgesInGraph = graph.edges();
        for (Edge<String> edge : testEdges)
        {
            assertFalse(edgesInGraph.contains(edge));
        }
    }

    /**
     * Verifies that edges can reliably be removed from the graph.
     */
    @Test
    public void removeEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure you cannot remove missing edge elements (one missing vertex, or both)
        assertFalse(graph.removeEdge("A", "M"));
        assertFalse(graph.removeEdge("M", "A"));
        assertFalse(graph.removeEdge("N", "M"));

        //make sure you can remove edges in graph
        Set<Edge<String>> edges = graph.edges();
        Edge<String> first = edges.iterator().next();

        assertTrue(graph.removeEdge(first.getSource(), first.getDestination()));
        assertEquals(testVerts.length - 2, graph.edgeSize());

        //add the edge back
        graph.addEdge(first.getSource(), first.getDestination(), DEFAULT_WEIGHT);

        //remove all edges in the graph
        for (Edge<String> edge : edges)
        {
            assertTrue(graph.removeEdge(edge.getSource(), edge.getDestination()));
        }

        assertEquals(0, graph.edgeSize());
    }

    /**
     * Tests whether the Graph.clear() method removes all vertices
     * and edges from the graph.
     */
    @Test
    public void clearTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        graph.clear();

        //verify size
        assertEquals(0, graph.vertexSize());
        assertEquals(0, graph.edgeSize());

        //verify sets are empty
        assertEquals(0, graph.vertices().size());
        assertEquals(0, graph.edges().size());
    }
}
