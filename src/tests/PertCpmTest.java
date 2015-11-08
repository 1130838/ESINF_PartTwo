package tests;

import graphbase.Vertex;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 06/11/2015.
 */
public class PertCpmTest {

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
     * the PertCPM instance is unitialized in the contructor with 2 vertices (start and finish).
     * When another vertice is added, the number of vertices should be 3.
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
     *
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


            // 5 = Start(1)--- vca1(2)--- fca1(3) ---- fca2(4) ---- Finish(5)

        }

    /**
     *
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

}