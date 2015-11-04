package model;

import java.util.List;

/**
 * Created by bruno.devesa on 19/10/2015.
 */
public abstract class Activity {

    private String key;

    private ActivityType type;
    private String description;
    private float duration;
    private TimeUnit time_unit;
    private List<String> preceding_activities;

    /**
     * Constructor os Activity Class
     * @param key key
     * @param type  type
     * @param description description
     * @param duration duration
     * @param time_unit time_unit
     * @param preceding_activities preceding_activities
     */
    public Activity(String key, ActivityType type, String description, float duration, TimeUnit time_unit, List<String> preceding_activities) {
        this.key = key;
        this.type = type;
        this.description = description;
        this.duration = duration;
        this.time_unit = time_unit;
        this.preceding_activities = preceding_activities;
    }

    /**
     * Get the key atribute
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * toString method
     * @return instance content
     */
    public String toString(){

        return "Type: " + type +  "\n" +
                "Description : " + description +  "\n" +
                "Duration : " + duration +  "\n" +
                "Time Unit: " + time_unit +  "\n" +
                "Preceding activities: " + preceding_activities + "\n" ;

    }


}
