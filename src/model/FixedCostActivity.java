package model;

import java.util.ArrayList;

/**
 * Created by bruno.devesa on 19/10/2015.
 */
public class FixedCostActivity extends Activity {

    float cost;

    /**
     * Constructor of the FixedCostActivity
     * @param key key
     * @param type type
     * @param description description
     * @param duration duration
     * @param time_unit time_unit
     * @param cost cost
     * @param preceding_activities preceding_activities
     */
    public FixedCostActivity(String key, ActivityType type, String description, float duration, TimeUnit time_unit, float cost, ArrayList<String> preceding_activities) {
        super(key, type, description, duration, time_unit, preceding_activities);
        this.cost = cost;
    }

    /**
     * toString method to return the instance content
     * @return String
     */
    public String toString(){
        return super.toString()+"Cost: " + cost + "\n";
    }


}
