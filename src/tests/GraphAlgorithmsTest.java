package tests;

import graphbase.Graph;
import graphbase.GraphAlgorithms;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 15/11/2015.
 */
public class GraphAlgorithmsTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBreadthFirstSearch() throws Exception {

    }

    @Test
    public void testAllPaths() throws Exception {

        Graph<String, String> testGraph = new Graph<>(true);

        testGraph.insertVertex("Start");
        testGraph.insertVertex("A");
        testGraph.insertVertex("B");
        testGraph.insertVertex("C");
        testGraph.insertVertex("Finish");

        testGraph.insertEdge("Start", "A", "StartA", 1);
        testGraph.insertEdge("A", "Finish", "AFinish", 2);
        testGraph.insertEdge("Start", "B", "StartB", 3);
        testGraph.insertEdge("B", "C", "BC", 4);
        testGraph.insertEdge("C", "Finish", "CFinish", 5);

        /*

        Start ----> A -----> Finish
           \            ^
            \         /
            v       /
             B --> C

         */


        System.out.println("## allPaths Test ##");

        ArrayList<Deque<String>> allPathsListResult = GraphAlgorithms.allPaths(testGraph, "Start", "Finish");

        ArrayList<ArrayList<String>> allPathsListExpected = new ArrayList<ArrayList<String>>();

        ArrayList<String> path1 = new ArrayList<>();

        path1.add("Start");
        path1.add("A");
        path1.add("Finish");
        allPathsListExpected.add(path1);

        ArrayList<String> path2 = new ArrayList<>();
        path2.add("Start");
        path2.add("B");
        path2.add("C");
        path2.add("Finish");
        allPathsListExpected.add(path2);

        System.out.println("\n--- all possible paths  (" + allPathsListResult.size() + ") ------");

        ArrayList<ArrayList<String>> resultVerticesPath = new ArrayList<>();
        ArrayList<String> resultPath1 = new ArrayList<>();

        for (int i = 0; i < allPathsListResult.size(); i++) {
            System.out.println("## Path " + (i + 1) + " ##");
            Iterator it = allPathsListResult.get(i).iterator();
            while (it.hasNext()) {
                String vertex = (String) it.next();
                System.out.println(vertex);
                resultPath1.add(vertex);
            }

            resultVerticesPath.add((ArrayList<String>) resultPath1.clone()); // must be clone or else the reterence will be cleared
            resultPath1.clear();
            System.out.println("----------------------");
        }

        // testing the paths
        for (int i = 0; i < allPathsListExpected.size(); i++) {
            for (int j = 0; j < allPathsListExpected.get(i).size(); j++) {
            assertEquals(allPathsListExpected.get(i).get(j), resultVerticesPath.get(i).get(j));
            }
        }

        // testing the size of all possible paths
        int expectedSize = 2;
        int resultSize = allPathsListResult.size();
        assertEquals(expectedSize, resultSize);
    }


    @Test
    public void testShortestPath() throws Exception {

    }
}