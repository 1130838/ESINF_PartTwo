package tests;

import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by bruno.devesa on 21/10/2015.
 */
public class ActivityRecordTest {

    ActivityRecord instance = new ActivityRecord();
    ActivityRecord instance2 = new ActivityRecord();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    /**
     * Test pretends to validate the success of adding an activity in the the LinkedHashMap
     * Test developed to compare between a empty created LinkedHashMap map and the same map after
     * add a new Activity.
     * The new created map should have size = 0 before and size = 1 after
     */
    public void testAddActivity() throws Exception {

        System.out.println("addActivity Test");

        //create new Activity
        ArrayList<String> precedingActivities = new ArrayList<>();
        precedingActivities.add("actkeyprev1");
        precedingActivities.add("actkeyprev2");
        Activity activity1 = new FixedCostActivity("A", ActivityType.valueOf("FCA"), "Order Hardware platform", Float.parseFloat("4"), TimeUnit.valueOf("week"), Float.parseFloat("2500"),precedingActivities );

        //create new Hashmap map2 and put created activity inside map2
        LinkedHashMap<String, Activity> map2 = new LinkedHashMap<>();

        // validate size 0 in both expected and result
        int expect_before = 0;
        int result_before = map2.size();
        assertEquals(expect_before, result_before);

        // add a new activity
        map2.put(activity1.getKey(), activity1);

        // validate size 1 in both expected and result
        int expect_after = 1;
        int result_after = map2.size();
        assertEquals(expect_after, result_after);
    }

    @Test
    /**
     * Test pretends to validate the success of a created new activity.
     * Test developed to compare the activity hashmap(map1) values of a created activity and another map(map2) with a hardcoded hashmap value with the same activity.
     * The map1 content should be the same as the map2 content
     */
    public void testNewActivity() throws Exception {
        System.out.println("newActivity Test");

        // create new activity inside instance. This will create a instance hashMap with the new instance values
        instance.newActivity(new ArrayList<>(Arrays.asList("A", "FCA", "Order Hardware platform", "4", "week", "2500", "actkeyprev1", "actkeyprev2")));

        ArrayList<String> precedingActivities = new ArrayList<>();
        precedingActivities.add("actkeyprev1");
        precedingActivities.add("actkeyprev2");

        //create new Activity
        Activity activity1 = new FixedCostActivity("A", ActivityType.valueOf("FCA"), "Order Hardware platform", Float.parseFloat("4"), TimeUnit.valueOf("week"), Float.parseFloat("2500"),precedingActivities );

        //create new Hashmap map2 and put created activity inside map2
        LinkedHashMap<String, Activity> map2 = new LinkedHashMap<>();
        map2.put(activity1.getKey(), activity1);

        // iterate through map2 and validate if content of map 2 the same as the instance hashmap values.
        Set set = map2.entrySet();

        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry myEntry = (Map.Entry)iterator.next();

            String expected_value = myEntry.getValue().toString();
            String result_value = instance.getActivityByKey(activity1.getKey()).toString();

            assertEquals(expected_value, result_value);
        }
    }

    @Test
    /**
     * Test pretends to validade the success of creating Activities from a file source.
     * Test developed to compare the activity hashmap content of a imported file and another activity hashmap with the same activities.
     * The instance1 hashmap content of the imported file should be the same as the hashmap content of the instance2 as well
     * as both instance hashmaps should have the same size.
     */
    public void testCreateActivitiesFromFileData() throws Exception {

        System.out.println("CreateActivitiesFromFileData Test");
        String fileName = "import-test";

        instance.CreateActivitiesFromFileData(fileName);

        instance2.newActivity(new ArrayList<>(Arrays.asList("A", "FCA", "Order Hardware platform", "4", "week", "2500", "actkeyprev1", "actkeyprev2")));
        instance2.newActivity(new ArrayList<>(Arrays.asList("B", "VCA", "Order Software platform", "5", "week", "500", "50", "actkeyprev0", "actkeyprev1")));

        //instance1 and instance2 must have the same hashmap content
        Iterator<Map.Entry<String, Activity>> iterator = instance.getMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Activity> nextActivity = iterator.next();
            String expected = nextActivity.getValue().toString();
            String result = instance2.getActivityByKey(nextActivity.getKey()).toString();

            assertEquals(expected, result);
        }

        // both instance should have the same hashmap size
        int size_expected = instance.getMap().size();
        int size_result = instance2.getMap().size();

        assertEquals(size_expected, size_result);
    }
}