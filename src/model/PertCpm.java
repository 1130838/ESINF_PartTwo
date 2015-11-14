package model;

import graphbase.Edge;
import graphbase.Graph;
import graphbase.GraphAlgorithms;
import graphbase.Vertex;

import java.util.*;

/**
 * Created by bruno.devesa on 04/11/2015.
 */
public class PertCpm {

    private Graph<Activity, Integer> activityGraph;
    private ActivityRecord activityRecord;
    private float[][] matrix;   //matrix[0]=ES, matrix[1]=EF, matrix[2]=LS, matrix[3]=LF, matrix[4]=Slack.
    private boolean updated;
    FixedCostActivity startActivity;
    FixedCostActivity finishActivity;

    public PertCpm(ActivityRecord activityRecord) {
        this.activityGraph = new Graph(true);
        this.activityRecord = activityRecord;
        this.matrix = new float[5][];
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

   /* public ArrayList<ArrayList<Float>> getMatrix() {
        return matrix;
    }*/

    public float[][] getMatrix() {
        return matrix;
    }


    public boolean isUpdated() {
        return updated;
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
    public boolean createGraph() {

        LinkedHashMap<String, Activity> activityMap = activityRecord.getMap();

        Iterator<Activity> iterator = activityMap.values().iterator();

        // se nao tiver preceding activites, ele liga eles todos com o Vertice Start
        while (iterator.hasNext()) {

            Activity activityTemp = iterator.next();
            if (activityTemp.getPreceding_activities().isEmpty()
                    && !GraphAlgorithms.BreadthFirstSearch(activityGraph, startActivity).contains(activityTemp) // se nao contem os visitados
                    ) {

                this.addLink(startActivity, activityTemp);
            }
            // se tiver precedentes ele vai chamar o addLink( que chama o AddEdge da classe generica )
            // que já introduz no mapa os Vertices ( se nao existirem ) e a ligacao entre eles
            else {

                for (String s : activityTemp.getPreceding_activities()) {
                    Activity precedingActivity = activityMap.get(s);
                    addLink(precedingActivity, activityTemp);
                }
            }
        }

        // se nao tiver outgoing activites, ele liga eles todos com o Vertice Finish
        Iterator<Vertex<Activity, Integer>> iterator2 = activityGraph.vertices().iterator();

        while (iterator2.hasNext()) {

            Vertex<Activity, Integer> verticeTemp = iterator2.next();

            Activity activityTemp = verticeTemp.getElement();
            if (activityTemp != finishActivity // to not connect Start with Finish
                    && verticeTemp.getOutgoing().isEmpty() // if does not have outgoing vertices
                    ) {
                addLink(activityTemp, finishActivity);
                System.out.println("test: added activity " + activityTemp.getKey() + " to finish");
            }
        }

        if (validateGraph()) {
            return true;
        }
        return false;
    }

    public boolean validateGraph() {

        if (!hasACycle()) {
            return true;
        }
        return false;
    }

    //if i can reach a vertex through of one of its outgoingVertices then there is a cycle
    public boolean hasACycle() {
        Deque<Activity> pathReturned;
        for (Vertex<Activity, Integer> verticeActivity : activityGraph.vertices()) { // vertices return list of all vertices
            for (Vertex<Activity, Integer> vertActivity_Out : verticeActivity.getOutgoing().keySet()) {
                pathReturned = GraphAlgorithms.BreadthFirstSearch(activityGraph, vertActivity_Out.getElement());
                if (pathReturned.contains(verticeActivity.getElement())) {
                    return true;
                }
            }
        }
        return false;
    }


    private void calculateEarliest() {

        Deque<Vertex<Activity, Integer>> dequeAux = new LinkedList<>();
        boolean[] visited = new boolean[activityGraph.numVertices()];  //default initializ.: false
        float biggestEFfromPreviousVertex;
        boolean previousIsCalculated;

        Vertex<Activity, Integer> vOrig = activityGraph.getVertex(startActivity);
        dequeAux.add(vOrig);
        int vKey = vOrig.getKey();
        visited[vKey] = true;

        while (!dequeAux.isEmpty()) {
            vOrig = dequeAux.remove();
            for (Edge<Activity, Integer> edge : activityGraph.outgoingEdges(vOrig)) {
                Vertex<Activity, Integer> vAdj = activityGraph.opposite(vOrig, edge);
                vKey = vAdj.getKey();
                if (!visited[vKey]) {
                    Iterator incomingIterator = activityGraph.incomingEdges(vAdj).iterator();
                    biggestEFfromPreviousVertex = 0;
                    previousIsCalculated = true;
                    while (incomingIterator.hasNext()) {
                        Edge<Activity, Integer> currentEdge = (Edge<Activity, Integer>) incomingIterator.next();
                        if (visited[currentEdge.getVOrig().getKey()]) {
                            if (matrix[1][currentEdge.getVOrig().getKey()] > biggestEFfromPreviousVertex) {
                                biggestEFfromPreviousVertex = matrix[1][currentEdge.getVOrig().getKey()];
                            }
                        } else {
                            previousIsCalculated = false;
                            break;
                        }
                    }
                    if (previousIsCalculated) {
                        matrix[0][vAdj.getKey()] = biggestEFfromPreviousVertex;
                        float durationFromTextFile = vAdj.getElement().getDuration();
                        float durationInWeeks = vAdj.getElement().getTime_unit().getValue();
                        matrix[1][vAdj.getKey()] = biggestEFfromPreviousVertex + (durationFromTextFile * durationInWeeks);
                        visited[vKey] = true;
                    }
                    dequeAux.add(vAdj);
                }
            }
        }
    }


    private void calculateLatest() {

        Deque<Vertex<Activity, Integer>> dequeAux = new LinkedList<>();
        boolean[] visited = new boolean[activityGraph.numVertices()];  //default initializ.: false
        float smallestLS_FromPreviousVertex;
        boolean previousIsCalculated;

        Vertex<Activity, Integer> vOrig = activityGraph.getVertex(finishActivity);
        dequeAux.add(vOrig);
        int vKey = vOrig.getKey();
        visited[vKey] = true;
        matrix[2][vOrig.getKey()] = matrix[0][vOrig.getKey()];
        matrix[3][vOrig.getKey()] = matrix[1][vOrig.getKey()];

        while (!dequeAux.isEmpty()) {
            vOrig = dequeAux.remove();
            for (Edge<Activity, Integer> edge : activityGraph.incomingEdges(vOrig)) {
                Vertex<Activity, Integer> vAdj = activityGraph.opposite(vOrig, edge);
                vKey = vAdj.getKey();
                if (!visited[vKey]) {
                    Iterator outgoingIterator = activityGraph.outgoingEdges(vAdj).iterator();
                    smallestLS_FromPreviousVertex = Float.MAX_VALUE;
                    previousIsCalculated = true;
                    while (outgoingIterator.hasNext()) {
                        Edge<Activity, Integer> currentEdge = (Edge<Activity, Integer>) outgoingIterator.next();
                        if (visited[currentEdge.getVDest().getKey()]) {
                            if (matrix[2][currentEdge.getVDest().getKey()] < smallestLS_FromPreviousVertex) {
                                smallestLS_FromPreviousVertex = matrix[2][currentEdge.getVDest().getKey()];
                            }
                        } else {
                            previousIsCalculated = false;
                            break;
                        }
                    }
                    if (previousIsCalculated) {
                        matrix[3][vAdj.getKey()] = smallestLS_FromPreviousVertex;
                        matrix[2][vAdj.getKey()] = smallestLS_FromPreviousVertex - (vAdj.getElement().getDuration() * vAdj.getElement().getTime_unit().getValue());
                        visited[vKey] = true;
                    }
                    dequeAux.add(vAdj);
                }
            }
        }
    }


    private void calculateSlack() {

        for (int i = 0; i < matrix[4].length; i++) {
            matrix[4][i] = matrix[3][i] - matrix[1][i];
        }
    }


    public float[][] updateMatrix() {

        if (!isUpdated()) {
            matrix[0] = new float[activityGraph.numVertices()];
            matrix[1] = new float[activityGraph.numVertices()];
            matrix[2] = new float[activityGraph.numVertices()];
            matrix[3] = new float[activityGraph.numVertices()];
            matrix[4] = new float[activityGraph.numVertices()];

            calculateEarliest();
            calculateLatest();
            calculateSlack();
            this.updated = true;
            return getMatrix();
        }
        return getMatrix();
    }


    public ArrayList<Integer> allPaths(ArrayList<Deque<Activity>> paths) {

        ArrayList<Deque<Activity>> allPaths = GraphAlgorithms.allPaths(activityGraph, startActivity, finishActivity);
        paths.addAll(allPaths);

        //convert ArrayList<Deque<Activity>> in  ArrayList<Integer>
        ArrayList<Integer> pathsArrayList = new ArrayList<>();

        for (int i = 0; i < paths.size(); i++) {
            pathsArrayList.add(i, paths.get(i).size() - 2);
        }

        return pathsArrayList;
    }


    public ArrayList<Activity> activitiesByCompletion() {

        if (!isUpdated()) {
            updateMatrix();
        }

        ArrayList<Activity> completedActivitiesList = new ArrayList<>();
        for (Vertex<Activity, Integer> vert : activityGraph.vertices()) {
            boolean flag = true;
            if (vert.getKey() != 0 && vert.getKey() != 1) {
                if (completedActivitiesList.isEmpty()) {
                    completedActivitiesList.add(vert.getElement());
                } else {
                    for (int i = 0; i < completedActivitiesList.size() && flag; i++) {
                        if (matrix[1][vert.getKey()] < matrix[1][activityGraph.getVertex(completedActivitiesList.get(i)).getKey()]) {
                            completedActivitiesList.add(i, vert.getElement());
                            flag = false;
                        }
                    }
                    if (flag) {
                        completedActivitiesList.add(vert.getElement());
                    }
                }
            }
        }
        return completedActivitiesList;

    }


    public ArrayList<Deque<Activity>> criticalPaths() {

        updateMatrix();

        ArrayList<Deque<Activity>> criticalPathsList = new ArrayList<>();
        Deque<Activity> path = new LinkedList<>();
        boolean[] visited = new boolean[activityGraph.numVertices()];
        Vertex<Activity, Integer> vOrig = activityGraph.getVertex(startActivity);
        Vertex<Activity, Integer> vDest = activityGraph.getVertex(finishActivity);

        criticalPaths(vOrig, vDest, visited, path, criticalPathsList);

        return criticalPathsList;
    }


    private void criticalPaths(Vertex<Activity, Integer> vOrig, Vertex<Activity, Integer> vDest,
                               boolean[] visited, Deque<Activity> path, ArrayList<Deque<Activity>> paths) {

        visited[vOrig.getKey()] = true;
        path.add(vOrig.getElement());

        if (vOrig.getKey() == vDest.getKey()) {

            LinkedList<Activity> auxPathList = new LinkedList();
            auxPathList.addAll(path);
            auxPathList.removeFirst();
            auxPathList.removeLast();
            paths.add(auxPathList);

        } else {
            for (Vertex<Activity, Integer> vertex : vOrig.getOutgoing().keySet()) {
                if (visited[vertex.getKey()] == false && matrix[4][vertex.getKey()] == 0) {   // if not visited and if slack == 0
                    criticalPaths(vertex, vDest, visited, path, paths);
                }
            }
        }
        visited[vOrig.getKey()] = false;
        path.removeLast();

    }


}



