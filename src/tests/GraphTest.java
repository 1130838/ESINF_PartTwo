package tests;

import graphbase.Edge;
import graphbase.Graph;
import graphbase.Vertex;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 10/11/2015.
 */
public class GraphTest {
    // creates a new directed graph ( oriented )
    Graph<String, String> instance = new Graph<>(true) ;

    /**
     * This test pretends to test the number of Vertices in the instance of Graph.
     * When graph is created (see contructor) the  numberOfVertices and numberOfEdges should be 0.
     *
     * @throws Exception
     */
    @Test
    public void testNumVertices() throws Exception {
        System.out.println("## Test NumVertices() ##");
        System.out.println("when creating the graph, the number of Vertices should be 0");
        assertTrue(instance.numVertices() == 0);

        System.out.println("when inserting 1 vertice in the graph, the number of Vertices should be 1");
        Vertex vertex = new Vertex(1,"Vertice1");
        instance.insertVertex(vertex.getElement().toString());
        assertTrue(instance.numVertices() == 1);

    /*    System.out.println("when removing 1 vertice in the graph, the number of Vertices should be 0 again");
        instance.removeVertex(vertex);
        assertTrue(instance.numVertices() == 0);*/


    }

    @Test
    public void testVertices() throws Exception {

    }

    @Test
    public void testNumEdges() throws Exception {

    }

    @Test
    public void testEdges() throws Exception {

    }

    @Test
    public void testGetEdge() throws Exception {

    }

    @Test
    public void testEndVertices() throws Exception {

        Vertex<String,String> vert1=instance.insertVertex("A");
        Vertex<String,String> vert2=instance.insertVertex("B");
        Vertex<String,String> vert3=instance.insertVertex("C");

        Edge<String,String> edge1=instance.insertEdge("A", "B", "Edge1", 6);
        Edge<String,String> edge2=instance.insertEdge("A","C","Edge2",1);
        Edge<String,String> edge3=instance.insertEdge("B", "C", "Edge3", 4);

        /* model:

        (A) --> (B)
          \     |
           \    |
            v   v
             (C)

         */

        Vertex<String,String>[] verticesArray = instance.endVertices(edge1);

        System.out.println("The end vertices of edge1(A) should be 2 vertcies");

        int expected = 2;
        int result = verticesArray.length;

        assertEquals(expected,result);


        //---

        Vertex<String,String>[] verticesArray2 = instance.endVertices(edge2);
        System.out.println("The end vertices of edge2(B) should be 1 vertices");

        int expected2 = 1;
        int result2 = verticesArray2.length;

        assertEquals(expected,result);


        //---

        Vertex<String,String>[] verticesArray3 = instance.endVertices(edge3);
        System.out.println("The end vertices of edge3(C) should be 0 vertices");

        int expected3 = 0;
        int result3 = verticesArray2.length;

        assertEquals(expected,result);


    }

    @Test
    public void testOpposite() throws Exception {

        Vertex<String,String> vert1=instance.insertVertex("A");
        Vertex<String,String> vert2=instance.insertVertex("B");
        Vertex<String,String> vert3=instance.insertVertex("C");

        Edge<String,String> edge1=instance.insertEdge("A", "B", "Edge1", 6);
        Edge<String,String> edge2=instance.insertEdge("A","C","Edge2",1);
        Edge<String,String> edge3=instance.insertEdge("B", "C", "Edge3", 4);

        /* model:

        (A) --> (B)
          \     |
           \    |
            v   v
             (C)

         */

        System.out.println("opposite of Vertice A with edge 1 should be B");
        Vertex expected = vert2; // B
        Vertex result = instance.opposite(vert1, edge1);

        assertEquals(expected, result);

        //---------

        System.out.println("opposite of Vertice A with edge 2 should be C");
        Vertex expected1 = vert3; // C
        Vertex result1 = instance.opposite(vert1, edge2);

        assertEquals(expected1, result1);



    }

    @Test
    public void testOutDegree() throws Exception {

    }

    @Test
    public void testInDegree() throws Exception {

        Vertex<String,String> vert1=instance.insertVertex("A");
        Vertex<String,String> vert2=instance.insertVertex("B");
        Vertex<String,String> vert3=instance.insertVertex("C");

        Edge<String,String> edge1=instance.insertEdge("A", "B", "Edge1", 6);
        Edge<String,String> edge2=instance.insertEdge("A","C","Edge2",1);
        Edge<String,String> edge3=instance.insertEdge("B", "C", "Edge3", 4);

        /* model:

        (A) --> (B)
          \     |
           \    |
            v   v
             (C)

         */

        System.out.println("degree of Vertice vert1(A) should be 0");
        int expected = 0;
        int result = instance.inDegree(vert1);

        assertEquals(expected, result);

        // -----------

        System.out.println("degree of Vertice vert2(B) should be 1");
        int expected2 = 1;
        int result2 = instance.inDegree(vert2);

        assertEquals(expected2, result2);

        // -----------

        System.out.println("degree of Vertice vert3(C) should be 2");
        int expected3 = 2;
        int result3 = instance.inDegree(vert3);

        assertEquals(expected3, result3);

    }

    @Test
    public void testOutgoingEdges() throws Exception {

    }

    @Test
    public void testIncomingEdges() throws Exception {

        Vertex<String,String> vert1=instance.insertVertex("A");
        Vertex<String,String> vert2=instance.insertVertex("B");
        Vertex<String,String> vert3=instance.insertVertex("C");

        Edge<String,String> edge1=instance.insertEdge("A", "B", "Edge1", 6);
        Edge<String,String> edge2=instance.insertEdge("A","C","Edge2",1);
        Edge<String,String> edge4=instance.insertEdge("B","C","Edge3",4);

        /* model:

        (A) --> (B)
          \     |
           \    |
            v   v
             (C)

         */

        Iterator<Edge<String,String>> iteratorEdgeC = instance.incomingEdges(vert3).iterator();

        System.out.println("incoming edges to vert 3(C) should be 2");

        int expected = 2;
        int result = 0;
        while(iteratorEdgeC.hasNext()){
            result++;
            iteratorEdgeC.next();
        }

        assertEquals(expected,result);

        // ------------------------

        Iterator<Edge<String,String>> iteratorEdgeA = instance.incomingEdges(vert1).iterator();
        System.out.println("incoming edges to vert 1(A) should be 0");

        int expected2 = 0;
        int result2 = 0;

        while(iteratorEdgeA.hasNext()){
            result2++;
            iteratorEdgeA.next();
        }

        assertEquals(expected2,result2);

        // ------------------------

        Iterator<Edge<String,String>> iteratorEdgeB = instance.incomingEdges(vert2).iterator();
        System.out.println("incoming edges to vert 2(B) should be 1");

        int expected3 = 1;
        int result3 = 0;
        while(iteratorEdgeB.hasNext()){
            result3++;
            iteratorEdgeB.next();
        }

        assertEquals(expected3,result3);

    }

    @Test
    public void testInsertVertex() throws Exception {

    }

    @Test
    public void testInsertEdge() throws Exception {

    }

    @Test
    public void testRemoveVertex() throws Exception {

        Vertex<String,String> vert1=instance.insertVertex("A");
        Vertex<String,String> vert2=instance.insertVertex("B");
        Vertex<String,String> vert3=instance.insertVertex("C");

        Edge<String,String> edge1=instance.insertEdge("A", "B", "Edge1", 6);
        Edge<String,String> edge2=instance.insertEdge("A","C","Edge2",1);
        Edge<String,String> edge4=instance.insertEdge("B","C","Edge3",4);

        /* model:

        (A) --> (B)
          \     |
           \    |
            v   v
             (C)

         */

        System.out.println("removing Vertex C, the number of Vertices should be 2 and number of edges should be 1");

        instance.removeVertex(vert3.getElement());

        int expectedVertices = 2;
        int resultVertices = instance.numVertices();



        assertEquals(expectedVertices, resultVertices);

        int expectedEdges = 1;
        int resultEdges = instance.numEdges();

        assertEquals(expectedEdges,resultEdges);



    }

    @Test
    public void testRemoveEdge() throws Exception {

    }
}