package model;

import graphbase.Graph;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;

/**
 * Created by bruno.devesa on 04/11/2015.
 */
public class PertCpm {

    private Graph<Activity, Integer> activityGraph;
    private ActivityRecord activityRecord;
    private ArrayList<ArrayList<Float>> matrix;
    private boolean updated;
    FixedCostActivity startActivity;
    FixedCostActivity finishActivity;

    public PertCpm(ActivityRecord activityRecord) {
        this.activityGraph = new Graph(true);
        this.activityRecord = activityRecord;
        this.matrix = new ArrayList<>();
        this.updated = false;

        this.startActivity = new FixedCostActivity("Start", ActivityType.FCA, "Start", 0, TimeUnit.day, 0, null);
        this.finishActivity = new FixedCostActivity("Finish", ActivityType.FCA, "Finish", 0, TimeUnit.day, 0, null);

        // always add in the initialization this 2 virtual vertices ( start and finish )
        this.addActivity(startActivity);
        this.addActivity(finishActivity);

    }

    public Graph<Activity, Integer> getActivityGraph() {
        return activityGraph;
    }

    public ActivityRecord getActivityRecord() {
        return activityRecord;
    }

    public ArrayList<ArrayList<Float>> getMatrix() {
        return matrix;
    }

    public void addActivity(Activity activity) {
        activityGraph.insertVertex(activity);
        this.updated = false;
    }

    public void addLink(Activity activity1, Activity activity2) {
        activityGraph.insertEdge(activity1, activity2, null, 0);
        this.updated = false;
    }

}

