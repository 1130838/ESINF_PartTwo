package tests;

import utils.FileImport;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 19/10/2015.
 */
public class FileImportTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    /**
     * Test pretends to validate the success of importing a file.
     * Test developed creating a valid data structure with a content equal to the imported file.
     * The content of the created structure and its size should be the same as the returned content of method.
     */
    public void testImportDataFromFile() throws Exception {

        System.out.println("ImportDataFromFile Test");
        String fileName = "import-test";

        String line1[] = {"A", "FCA", "Order Hardware platform", "4", "week", "2500", "actkeyprev1", "actkeyprev2"};
        String line2[] = {"B", "VCA", "Order Software platform", "5", "week", "500","50","actkeyprev0", "actkeyprev1" };

        ArrayList<ArrayList<String>> expResult = new ArrayList<>();

        //add line 1 and add to expResult
        ArrayList<String> ExpectedListline1 = new ArrayList<>();
        //elements
        for (int i = 0; i < line1.length; i++) {
            ExpectedListline1.add(line1[i]);
        }
        expResult.add(ExpectedListline1);

        //add line 2 and add to expResult
        ArrayList<String> ExpectedListline2 = new ArrayList<>();
        //elements
        for (int i = 0; i < line2.length; i++) {
            ExpectedListline2.add(line2[i]);
        }
        expResult.add(ExpectedListline2);

        ArrayList<ArrayList<String>> result = FileImport.importDataFromFile(fileName);

        // test if the contente is the same
        assertEquals(expResult, result);
        // test if the number of lines are the same
        assertEquals(expResult.size(), result.size());




    }
}