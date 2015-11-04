package tests;

import graphbase.Vertex;
import graphexamples.LabyrinthCheater;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 04/11/2015.
 */
public class LabyrinthCheaterTest {

    public LabyrinthCheater map = new LabyrinthCheater();

    @Before
    public void setUp() throws Exception {


        // insert vertices from A to E with no exits
        for(char c = 'A'; c < 'E'; c++)
            map.insertRoom(String.valueOf(c), false);

        // vertice E has a exit
        map.insertRoom("E", true);

        // insert vertices from F to J with no exits
        for(char c = 'F'; c < 'J'; c++)
            map.insertRoom(String.valueOf(c), false);

        // vertice J has a exit
        map.insertRoom("J", true);

        // vertice K has a exit
        map.insertRoom("K", true);

        // insert vertices from L to S with no exits
        for(char c = 'L'; c < 'S'; c++)
            map.insertRoom(String.valueOf(c), false);

        // vertice E has a exit
        map.insertRoom("S", true);

        // vertice E has a exit
        map.insertRoom("T", false);

        // insert doors
        map.insertDoor("A", "B");
        map.insertDoor("B", "G");
        map.insertDoor("G", "H");
        map.insertDoor("H", "C");
        map.insertDoor("C", "D");
        map.insertDoor("D", "E");
        map.insertDoor("D", "I");
        map.insertDoor("A", "F");
        map.insertDoor("K", "L");
        map.insertDoor("I", "J");
        map.insertDoor("F", "K");
        map.insertDoor("L", "Q");
        map.insertDoor("L", "M");
        map.insertDoor("M", "N");
        map.insertDoor("N", "S");

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test pretends to validate the insertion of a Vertice.
     * Since it«s not allowed to repeat the key of map (Vertice), by inserting a new
     * Vertice in the temporary created map the method (boolean return) should return true
     * and by inserting a existing Vertice the method should return false.
     * @throws Exception
     */
    @Test
    public void testInsertRoom() throws Exception {
        System.out.println("Test insert room");


        LabyrinthCheater tempMap = new LabyrinthCheater();

        System.out.println("Inserting a new Vertice - should return true");
        assertTrue(tempMap.insertRoom("A", false));

        System.out.println("Inserting a existing Vertice - should return false");
       // assertFalse(tempMap.map.checkVertex(v));
    }

    @Test
    public void testInsertDoor() throws Exception {
      //  fail("Not implemented yet.");
    }

    @Test
    public void testRoomsInReach() throws Exception {
       // fail("Not implemented yet.");
    }

    @Test
    public void testNearestExit() throws Exception {
       // fail("Not implemented yet.");
    }

    @Test
    public void testPathToExit() throws Exception {
       // fail("Not implemented yet.");
    }
}