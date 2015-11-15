package tests;

import graphbase.Graph;
import graphbase.Vertex;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 06/11/2015.
 */
public class PertCpmTest {

    // creating a activityRecord instance and 3 hardcoded Activities for test purposes
    ActivityRecord activityRecord = new ActivityRecord();
    VariableCostActivity vca1 = new VariableCostActivity("A", ActivityType.VCA, "ActivityOneVCA", 1, TimeUnit.day, 30, 112, null);
    FixedCostActivity fca1 = new FixedCostActivity("B", ActivityType.FCA, "ActivityTwoFCA", 4, TimeUnit.day, 2500, null);
    FixedCostActivity fca2 = new FixedCostActivity("C", ActivityType.FCA, "ActivityThreeFCA", 2, TimeUnit.week, 1250, new ArrayList<>(Arrays.asList("B")));
    //(fca will have fca1 as a preceding activitie:)

    /*

    Start ---- A -----Finish
           \            /
            \ B --- C /
     */


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * This test pretends to validate the add of an activity in the PertCPM instance (in the PertCPM graph).
     * the PertCPM instance is initialized in the constructor with 2 vertices (start and finish).
     * Thus, when another another vertice is added, the number of vertices should be 3 (2+1).
     *
     * @throws Exception
     */
    @Test
    public void testAddActivity() throws Exception {
        System.out.println("## AddActivity Test ##");
        PertCpm instance = new PertCpm(activityRecord);

        instance.addActivity(vca1);

        int expected = 3; // 3 = start +  finish + vca1
        int result = instance.getActivityGraph().numVertices();
        assertEquals(expected, result);

        // ---------

        // add another one (activitie). the number of vertices should be 4
        instance.addActivity(fca1);
        int expected2 = 4; // 4 = start +  finish + vca1 + fca1
        int result2 = instance.getActivityGraph().numVertices();
        assertEquals(expected2, result2);

    }


    @Test
    public void testAddLink() throws Exception {

        System.out.println("## AddLink Test ##");
        PertCpm instance = new PertCpm(activityRecord);

        activityRecord.addActivity(vca1);
        activityRecord.addActivity(fca1);

        instance.addLink(vca1, fca1);

        int expected = 1; // vca1 ---(1)--- fca1
        int result = instance.getActivityGraph().numEdges();
        assertEquals(expected, result);

        // add one more Activity and edge

        activityRecord.addActivity(fca2);

        instance.addLink(fca1, fca2);
        int expected2 = 2; // 2 =  vca1---(1)--- fca1 --- (2) ---- fca2
        int result2 = instance.getActivityGraph().numEdges();
        assertEquals(expected2, result2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateGraph() throws Exception {

        System.out.println("## createGraph Test with NO preceding activities ##");

        PertCpm instance = new PertCpm(activityRecord);
        activityRecord.addActivity(vca1);
        activityRecord.addActivity(fca1);

        instance.createGraph();
            /*

            Start ---- A -----Finish
                 \            /
                  \         /
                   \   B  /
            */

        int expectedNumEdges = 4; // 4 = Start --(1)--- vca1---(2)--- fca1 --- (3) ---- Finish
        int resultNUmEdges = instance.getActivityGraph().numEdges();

        assertEquals(expectedNumEdges, resultNUmEdges);

        int expectedNumVertices = 4; // 4 = Start(1)--- vca1(2)--- fca1(3) ----  Finish(4)
        int resultNumVertices = instance.getActivityGraph().numVertices();

        assertEquals(expectedNumVertices, resultNumVertices);


    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateGraph2() throws Exception {

        System.out.println("## createGraph Test with preceding activities ##");

        PertCpm instance = new PertCpm(activityRecord);
        activityRecord.addActivity(fca1);
        activityRecord.addActivity(fca2);

        instance.createGraph();
            /*

            Start ---fca1(B)    Finish
                      |        /
                      |      /
                    fca2(C)
            */

        int expectedNumEdges = 3;
        int resultNUmEdges = instance.getActivityGraph().numEdges();

        assertEquals(expectedNumEdges, resultNUmEdges);

        int expectedNumVertices = 4;
        int resultNumVertices = instance.getActivityGraph().numVertices();

        assertEquals(expectedNumVertices, resultNumVertices);

    }


    /**
     * @throws Exception
     */
    @Test
    public void testValidateGraph1() throws Exception {

        System.out.println("## ValidateGraph1 Test with importing file and with no cycles ##");

        /*

        file content activities-test :
        A,VCA,High level analysis,1,week,30,112
        B,FCA,Order Hardware platform,4,week,2500
        C,FCA,Installation and commissioning of hardware,2,week,1250,B
        D,VCA,Detailed analysis of core modules,3,week,30,162,A

         */


               /*

            Start --- > vca(A) ---- > vca(D )---->   Finish
                  \                                ^
                   \                             /
                    v                          /
                      fca1(B)-------- > vca(C)
            */

        System.out.println("Graph with no cycles should be validated ( return true )");
        ActivityRecord activityRecordFromFile = new ActivityRecord();
        activityRecordFromFile.CreateActivitiesFromFileData("activities-test"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFile);

        instance.createGraph();

        boolean expected1 = true;
        boolean result1 = instance.validateGraph();
        assertEquals(expected1, result1);

        int expectedNumEdges = 6;
        int resultNUmEdges = instance.getActivityGraph().numEdges();
        assertEquals(expectedNumEdges, resultNUmEdges);

        int expectedNumVertex = 6;
        int resultNumVertex = instance.getActivityGraph().numVertices();
        assertEquals(expectedNumVertex, resultNumVertex);


    }


    @Test
    public void testValidateGraph2() throws Exception {

        System.out.println("## ValidateGraph2 Test with importing file and with cycles ##");

        /*

        file content activities-test :
        A,VCA,High level analysis,1,week,30,112
        B,FCA,Order Hardware platform,4,week,2500
        C,FCA,Installation and commissioning of hardware,2,week,1250,B,E
        D,VCA,Detailed analysis of core modules,3,week,30,162,A
        E,VCA,testing cycles,3,week,30,162,C

         */


               /*

            Start --- > vca(A) ---- > vca(D )----> Finish
                  \                             ^
                   \                          /
                    v                       /
                      fca2(B)-------- > vca(C)
                        |
                        |
                        |
                       (E)--------> (F)       (cycle between (E,F,G)
                        ^            /
                         \         /
                          \      /
                           \   v
                            (G)

            */


        System.out.println("Graph with no cycles should NOT be validated ( return false )");
        ActivityRecord activityRecordFromFileWithCycles = new ActivityRecord();
        activityRecordFromFileWithCycles.CreateActivitiesFromFileData("activities-test-cycles"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFileWithCycles);

        instance.createGraph();

        boolean expected2 = false;
        boolean result2 = instance.createGraph();

        assertEquals(expected2, result2);


        int expectedNumEdges = 10;
        int resultNUmEdges = instance.getActivityGraph().numEdges();
        assertEquals(expectedNumEdges, resultNUmEdges);

        int expectedNumVertex = 9;
        int resultNumVertex = instance.getActivityGraph().numVertices();
        assertEquals(expectedNumVertex, resultNumVertex);

    }

    @Test
    public void testAllPaths() throws Exception {
        System.out.println("## allPaths Test ##");

            /*

        file content activities-test :
        A,VCA,High level analysis,1,week,30,112
        B,FCA,Order Hardware platform,4,week,2500
        C,FCA,Installation and commissioning of hardware,2,week,1250,B
        D,VCA,Detailed analysis of core modules,3,week,30,162,A

         */

               /*

            Start --- > vca(A) ---- > vca(D )---->   Finish
                  \                                ^
                   \                             /
                    v                          /
                      fca1(B)-------- > vca(C)
            */


        ActivityRecord activityRecordFromFile = new ActivityRecord();
        activityRecordFromFile.CreateActivitiesFromFileData("activities-test"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFile);

        instance.createGraph();

        ArrayList<Deque<Activity>> resultAllPaths = instance.allPaths();

        System.out.println("\n--- all possible paths  (" + resultAllPaths.size() + ")-------");

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


        ArrayList<ArrayList<Activity>> resultVerticesPath = new ArrayList<>();
        ArrayList<String> resultPathTemp = new ArrayList<>();

        for (int i = 0; i < resultAllPaths.size(); i++) {
            System.out.println("## Path " + (i + 1) + " ##");
            Iterator it = resultAllPaths.get(i).iterator();
            while (it.hasNext()) {
                Activity vertex = (Activity) it.next();
                System.out.println(vertex.getKey());
                resultPathTemp.add(vertex.getKey());
            }

            resultVerticesPath.add((ArrayList<Activity>) resultPathTemp.clone()); // must be clone or else the reference will be cleared
            resultPathTemp.clear();
            System.out.println("----------------------");
        }

        // testing the paths
        for (int i = 0; i < resultVerticesPath.size(); i++) {
            for (int j = 0; j < resultVerticesPath.get(i).size(); j++) {
                assertEquals(resultVerticesPath.get(i).get(j), resultVerticesPath.get(i).get(j));
            }
        }

        // testing the size of all possible paths
        int expectedSize = 2;
        int resultSize = resultVerticesPath.size();
        assertEquals(expectedSize, resultSize);


    }



    @Test
    public void testAllPathsFromFile() throws Exception {
        System.out.println("## allPaths from File Test ##");

            ActivityRecord activityRecordFromFile = new ActivityRecord();
        activityRecordFromFile.CreateActivitiesFromFileData("activities-example"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFile);

        instance.createGraph();

        ArrayList<Deque<Activity>> resultAllPaths = instance.allPaths();

        System.out.println("\n--- all possible paths  (" + resultAllPaths.size() + ")-------");

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


        ArrayList<ArrayList<Activity>> resultVerticesPath = new ArrayList<>();
        ArrayList<String> resultPathTemp = new ArrayList<>();

        for (int i = 0; i < resultAllPaths.size(); i++) {
            System.out.println("## Path " + (i + 1) + " ##");
            Iterator it = resultAllPaths.get(i).iterator();
            while (it.hasNext()) {
                Activity vertex = (Activity) it.next();
                System.out.print(vertex.getKey() + " ");
                resultPathTemp.add(vertex.getKey());
            }

            resultVerticesPath.add((ArrayList<Activity>) resultPathTemp.clone()); // must be clone or else the reference will be cleared
            resultPathTemp.clear();
            System.out.println("");
        }


        // testing the size of all possible paths
        int expectedSize = 11;
        int resultSize = resultVerticesPath.size();
        assertEquals(expectedSize, resultSize);

    }


    @Test
    public void createParametersMatrix() throws Exception {
        System.out.println("## createParametersMatrix Test ##");

        ActivityRecord activityRecordFromFile = new ActivityRecord();
        activityRecordFromFile.CreateActivitiesFromFileData("activities-example"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFile);

        instance.createGraph();

        Graph activityGraph = new Graph(true);

        activityGraph = instance.getActivityGraph();

        float[][] resultMatrix = new float[14][];   //matrix[0]=ES, matrix[1]=EF, matrix[2]=LS, matrix[3]=LF, matrix[4]=Slack.

        int numVertices = activityGraph.numVertices();
        resultMatrix[0] = new float[numVertices]; // ES : Early Start
        resultMatrix[1] = new float[numVertices]; // EF : Early Finish
        resultMatrix[2] = new float[numVertices]; // LS : Latest Start
        resultMatrix[3] = new float[numVertices]; // LF : Latest Finish
        resultMatrix[4] = new float[numVertices]; // Slack


        resultMatrix = instance.createParametersMatrix();

        System.out.println("\n -- Table with imported vertices and its parameters --- \n");
        System.out.println("             ES | EF | LS | LF | Slack");
        for (int i = 2; i < numVertices; i++) { // starts from 2 because 0 is start and 1 is Finish
            System.out.print("Vertice " + instance.getActivityGraph().getVertex(i).getElement().getKey() + " : " + resultMatrix[0][i] + "  " + resultMatrix[1][i] + "  " + resultMatrix[2][i] + "  " + resultMatrix[3][i] + "  " + resultMatrix[4][i]);
            System.out.println("");
        }


        // Test the first vertice A parameters values
        float[][] expectedMatrix = new float[5][1];
        expectedMatrix[0][0] = (float) 0.0;
        expectedMatrix[1][0] = (float) 1.0;
        expectedMatrix[2][0] = (float) 2.0;
        expectedMatrix[3][0] = (float) 3.0;
        expectedMatrix[4][0] = (float) 2.0;

        assertEquals(expectedMatrix[0][0], resultMatrix[0][2], 0);
        assertEquals(expectedMatrix[1][0], resultMatrix[1][2], 0);
        assertEquals(expectedMatrix[2][0], resultMatrix[2][2], 0);
        assertEquals(expectedMatrix[3][0], resultMatrix[3][2], 0);
        assertEquals(expectedMatrix[4][0], resultMatrix[4][2], 0);

        // Test the last vertice L parameters values
        float[][] expectedMatrix2 = new float[5][1];
        expectedMatrix2[0][0] = (float) 14.0;
        expectedMatrix2[1][0] = (float) 16.0;
        expectedMatrix2[2][0] = (float) 14.0;
        expectedMatrix2[3][0] = (float) 16.0;
        expectedMatrix2[4][0] = (float) 0.0;

        assertEquals(expectedMatrix2[0][0], resultMatrix[0][13], 0); // position 14,array index 13 (last index)
        assertEquals(expectedMatrix2[1][0], resultMatrix[1][13], 0);
        assertEquals(expectedMatrix2[2][0], resultMatrix[2][13], 0);
        assertEquals(expectedMatrix2[3][0], resultMatrix[3][13], 0);
        assertEquals(expectedMatrix2[4][0], resultMatrix[4][13], 0);

    }


    @Test
    public void testActivityByCompletion() throws Exception {

        System.out.println("## activityByCompletion Test ##");

        ActivityRecord activityRecordFromFile = new ActivityRecord();
        activityRecordFromFile.CreateActivitiesFromFileData("activities-example"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFile);

        instance.createGraph();

        ArrayList<Activity> result = instance.activitiesByCompletion();

        System.out.println("-- Activities finished by completion ---");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("result : " + result.get(i).getKey());
        }

        ArrayList<String> expected = new ArrayList<>();

        expected.add("A");
        expected.add("B");
        expected.add("D");
        expected.add("C");
        expected.add("E");
        expected.add("F");
        expected.add("H");
        expected.add("G");
        expected.add("I");
        expected.add("J");
        expected.add("K");
        expected.add("L");

        // test the keys of the completed activities
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i).getKey());

        }

        // test the size of both ArrayLists
        assertEquals(expected.size(), result.size());

    }

    @Test
    public void testCriticaPaths() throws Exception {
        System.out.println("## criticalPaths Test ##");

        ActivityRecord activityRecordFromFile = new ActivityRecord();
        activityRecordFromFile.CreateActivitiesFromFileData("activities-example"); // importing graph from file

        PertCpm instance = new PertCpm(activityRecordFromFile);

        instance.createGraph();

        ArrayList<Deque<Activity>> resultCriticalPath = instance.criticalPaths();


        System.out.println("\n--- All critical paths  (" + resultCriticalPath.size() + ")-------");

        for (int i = 0; i < resultCriticalPath.size(); i++) {
            System.out.println("## Critical Path " + (i + 1) + " ##");
            Iterator it = resultCriticalPath.get(i).iterator();
            while (it.hasNext()) {
                Activity activity = (Activity) it.next();
                System.out.println(activity.getKey());
            }
            System.out.println("----------------------");
        }


        // all possible Vertices of critical path
        ArrayList<String> expectedCriticalPath = new ArrayList<>();

        expectedCriticalPath.add("B");
        expectedCriticalPath.add("C");
        expectedCriticalPath.add("F");
        expectedCriticalPath.add("G");
        expectedCriticalPath.add("I");
        expectedCriticalPath.add("K");
        expectedCriticalPath.add("L");

        // all possible vertices must be contained in the resultCriticalPaths
        for (int i = 0; i < resultCriticalPath.size(); i++) {
            for (int j = 0; j < expectedCriticalPath.size(); j++) {

                if (resultCriticalPath.get(i).contains(expectedCriticalPath.get(j))) {
                    assertTrue(true);
                } else {
                    assertFalse(false);
                }
            }
        }
    }


}