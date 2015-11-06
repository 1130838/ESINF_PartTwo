package model;

/**
 * Created by bruno.devesa on 19/10/2015.
 */
public enum TimeUnit {

    hour(24), day(1), week(7), month(30), year(365);
    public int value;

    private TimeUnit(int value) {
        this.value = value;

    }

    public int getValue(){
        return this.value;

    }


}
