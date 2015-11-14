package model;

/**
 * Created by bruno.devesa on 19/10/2015.
 */
public enum TimeUnit {

    hour(1/(7*24)), day(1/7), week(1), month(4), year(48);
    public int value;

    private TimeUnit(int value) {
        this.value = value;

    }

    public int getValue(){
        return this.value;
    }

}
