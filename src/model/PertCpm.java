package model;

import graphbase.Graph;
import graphbase.GraphAlgorithms;
import graphbase.Vertex;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedHashMap;

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

        this.startActivity = new FixedCostActivity("Start", ActivityType.FCA, "Start", 0, TimeUnit.day, 0, new ArrayList<>());
        this.finishActivity = new FixedCostActivity("Finish", ActivityType.FCA, "Finish", 0, TimeUnit.day, 0, new ArrayList<>());

        // always add in the initialization this 2 virtual vertices ( start and finish )
        addActivity(startActivity);
        addActivity(finishActivity);

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


    /**
     * Creates the connections (Edges) between the vertices with no preceding activities to the Start Vertice
     * and the create the connections with vertices with no outgoing Activites with the Finish Vertice.
     * The result will be the final graph with all the Vertices and Edges linked.
     */
    public void createGraph(){

        LinkedHashMap<String,Activity> activityMap = activityRecord.getMap();

        Iterator<Activity> iterator = activityMap.values().iterator();

        // se nao tiver preceding activites, ele liga eles todos com o Vertice Start
        while (iterator.hasNext()) {

            Activity activityTemp = iterator.next();
            if (activityTemp.getPreceding_activities().isEmpty()
                    && !GraphAlgorithms.BreadthFirstSearch(activityGraph,startActivity).contains(activityTemp) // se nao contem os visitados
                    ) {

                this.addLink(startActivity, activityTemp);
            }
            // se tiver precedentes ele vai chamar o addLink( que chama o AddEdge da classe generica )
            // que já introduz no mapa os Vertices ( se nao existirem ) e a ligacao entre eles
            else{

                for (String s : activityTemp.getPreceding_activities()) {
                    Activity precedingActivity = activityMap.get(s);
                    addLink(precedingActivity, activityTemp);
                }
            }
        }

        // se nao tiver outgoing activites, ele liga eles todos com o Vertice Finish
    Iterator<Vertex<Activity,Integer>> iterator2 = activityGraph.vertices().iterator();

        while(iterator2.hasNext()){

            Vertex<Activity,Integer> verticeTemp = iterator2.next();

            Activity activityTemp = verticeTemp.getElement();
            if (activityTemp != startActivity // to not connect Start with Finish
                    && activityTemp != finishActivity
                    && verticeTemp.getOutgoing().isEmpty()){
                addLink(activityTemp, finishActivity);
                System.out.println("test: added activitie " + activityTemp.getKey() + " to finish");
            }
        }

    }

}

