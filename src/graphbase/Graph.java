
package graphbase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @param <V>
 * @param <E>
 * @author DEI-ESINF
 */

public class Graph<V, E> implements GraphInterface<V, E> {

    private int numberOfVertices;
    private int numberOfEdges;
    private boolean isDirected;
    private ArrayList<Vertex<V, E>> verticesList;  //Vertice list

    // Constructs an empty graph (either undirected or directed)
    public Graph(boolean directed) {
        numberOfVertices = 0;
        numberOfEdges = 0;
        isDirected = directed;
        verticesList = new ArrayList<>();
    }

    public int numVertices() {
        return numberOfVertices;
    }

    public Iterable<Vertex<V, E>> vertices() {
        return verticesList;
    }

    public int numEdges() {
        return numberOfEdges;
    }


    public Iterable<Edge<V, E>> edges() {

        ArrayList<Edge<V, E>> allBranches = new ArrayList<>();

        for (Vertex<V, E> vertex : verticesList) {
            Map<Vertex<V, E>, Edge<V, E>> allBranchesOfThisVertice = vertex.getOutgoing();
            Iterator iterator = allBranchesOfThisVertice.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) iterator.next(); //o entry faz parte do Map, para guardar o elemento que estmaos a percorrer (Key+value)
                allBranches.add((Edge<V, E>) thisEntry.getValue());

            }
        }
        return allBranches;
        // throw new UnsupportedOperationException("Not supported yet.");
    }


    public Edge<V, E> getEdge(Vertex<V, E> vorig, Vertex<V, E> vdest) {

        if (verticesList.contains(vorig) && verticesList.contains(vdest))
            return vorig.getOutgoing().get(vdest);

        return null;
    }

    public Vertex<V, E>[] endVertices(Edge<V, E> e) {

        //test if branch exist
        boolean test = false;
        for (Vertex<V, E> vertice : verticesList) {
            if (vertice.getOutgoing().containsValue(e)) {
                test = true;
            }
        }
        // if exists,  create an array, fill it and return it
        if (test) {
            Vertex<V, E>[] origDest = new Vertex[2];

            origDest[0] = e.getVOrig();
            origDest[1] = e.getVDest();

            return origDest;
        }
        return null;

      //  throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vertex<V, E> opposite(Vertex<V, E> vert, Edge<V, E> e) {
        if (verticesList.contains(vert)) {
            if (e.getVOrig() == vert) {
                return e.getVDest(); //if its source, returns destiny
            } else if (e.getVDest() == vert) {
                return e.getVOrig();////if its destiny, returns source
            }
            return null;        //this means that the branch do NOT connect to that Vertice(vOrig)
        }
        return null;            //this means that vertice not belongs to graph

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public int outDegree(Vertex<V, E> v) {

        if (verticesList.contains(v))
            return v.getOutgoing().size();
        else
            return -1;
    }

    public int inDegree(Vertex<V, E> v) {

        int exitLevel = 0;
        if (v.getElement() != null) {
            if (verticesList.contains(v)) {
                for (Vertex<V, E> vertice : verticesList) {
                    if (vertice.getOutgoing().containsKey(v)) {
                        exitLevel++;
                    }
                }
            }
            return exitLevel;
        }
        return -1;

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<Edge<V, E>> outgoingEdges(Vertex<V, E> v) {

        if (!verticesList.contains(v))
            return null;

        ArrayList<Edge<V, E>> edges = new ArrayList<>();

        Map<Vertex<V, E>, Edge<V, E>> map = v.getOutgoing();
        Iterator<Map.Entry<Vertex<V, E>, Edge<V, E>>> it = map.entrySet().iterator();
        while (it.hasNext())
            edges.add(it.next().getValue());

        return edges;
    }

    public Iterable<Edge<V, E>> incomingEdges(Vertex<V, E> v) {

        ArrayList<Edge<V, E>> allBranchesOfVertice = new ArrayList();

        if (verticesList.contains(v)) {
            for (Vertex<V, E> vertice : verticesList) {
                if (vertice.getOutgoing().containsKey(v)) {

                    allBranchesOfVertice.add(vertice.getOutgoing().get(v));
                }
            }
        } else {
            return null;
        }
        return allBranchesOfVertice;

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    // este nem faz sentido porque o Vertice já é adicionado na insertEdge()
    public Vertex<V, E> insertVertex(V vInf) {

        Vertex<V, E> vert = getVertex(vInf);
        if (vert == null) {
            Vertex<V, E> newvert = new Vertex<>(numberOfVertices, vInf);
            verticesList.add(newvert);
            numberOfVertices++;
            return newvert;
        }
        return vert;
    }

    // apenas com o parametros Edge(V1, V2), ele cria os Vertices ( os que nao ja existirem ) e cria a ligação entre eles.
    public Edge<V, E> insertEdge(V vOrig, V vDest, E eInf, double eWeight) {

        Vertex<V, E> vorig = getVertex(vOrig);
        if (vorig == null)
            vorig = insertVertex(vOrig);

        Vertex<V, E> vdest = getVertex(vDest);
        if (vdest == null)
            vdest = insertVertex(vDest);

        if (getEdge(vorig, vdest) == null) {
            Edge<V, E> newedge = new Edge<>(eInf, eWeight, vorig, vdest);
            vorig.getOutgoing().put(vdest, newedge);
            numberOfEdges++;

            //if graph is not direct insert other edge in the opposite direction 
            if (!isDirected)
                if (getEdge(vdest, vorig) == null) {
                    Edge<V, E> otheredge = new Edge<>(eInf, eWeight, vdest, vorig);
                    vdest.getOutgoing().put(vorig, otheredge);
                    numberOfEdges++;
                }

            return newedge;
        }
        return null;
    }

    public void removeVertex(V vInf) {

        Vertex<V, E> verticeCurrent = getVertex(vInf);

        int verticePosition = verticesList.indexOf(verticeCurrent);

        //remove the edges where the V vInf is outgoing vertex
        if (verticesList.contains(verticeCurrent)) {
            for (Vertex<V, E> vertice : verticesList) {
                if (vertice.getOutgoing().containsKey(verticeCurrent)) {
                    removeEdge(vertice.getOutgoing().get(verticeCurrent));//remove the edge associated
                    vertice.getOutgoing().remove(verticeCurrent);        //remove the key from outgoing map in vert
                }
            }

            numberOfEdges = numberOfEdges - verticeCurrent.getOutgoing().size();

            //remove vertex from graph
            verticesList.remove(verticeCurrent);

            //refresh current vertexes keys
            for (int i = verticePosition; i < verticesList.size(); i++) {
                verticesList.get(i).setKey(i);
            }

            // decrease numberof Vertices
            numberOfVertices--;

        }

       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeEdge(Edge<V, E> edge) {

        Vertex<V, E>[] endpoints = endVertices(edge);

        Vertex<V, E> vorig = endpoints[0];
        Vertex<V, E> vdest = endpoints[1];

        if (vorig != null && vdest != null)
            if (edge.equals(getEdge(vorig, vdest))) {
                vorig.getOutgoing().remove(vdest);
                numberOfEdges--;
            }
    }

    public Vertex<V, E> getVertex(V vInf) {

        for (Vertex<V, E> vert : this.verticesList)
            if (vInf.equals(vert.getElement()))
                return vert;

        return null;
    }

    public Vertex<V, E> getVertex(int vKey) {

        if (vKey < verticesList.size())
            return verticesList.get(vKey);

        return null;
    }

    //Returns a clone of the graph 
    public Graph<V, E> clone() {

        Graph<V, E> newObject = new Graph<>(this.isDirected);

        newObject.numberOfVertices = this.numberOfVertices;

        newObject.verticesList = new ArrayList<>();
        for (Vertex<V, E> v : this.verticesList)
            newObject.verticesList.add(new Vertex<V, E>(v.getKey(), v.getElement()));

        for (Vertex<V, E> v1 : this.verticesList)
            for (Edge<V, E> e : this.outgoingEdges(v1))
                if (e != null) {
                    Vertex<V, E> v2 = this.opposite(v1, e);
                    newObject.insertEdge(v1.getElement(), v2.getElement(),
                            e.getElement(), e.getWeight());
                }
        return newObject;
    }

    /* equals implementation
     * @param the other graph to test for equality
     * @return true if both objects represent the same graph
     */
    public boolean equals(Object oth) {

        if (oth == null) return false;

        if (this == oth) return true;

        if (!(oth instanceof Graph<?, ?>))
            return false;

        Graph<?, ?> other = (Graph<?, ?>) oth;

        if (numberOfVertices != other.numberOfVertices || numberOfEdges != other.numberOfEdges)
            return false;

        //graph must have same vertices
        boolean eqvertex;
        for (Vertex<V, E> v1 : this.vertices()) {
            eqvertex = false;
            for (Vertex<?, ?> v2 : other.vertices())
                if (v1.equals(v2))
                    eqvertex = true;

            if (!eqvertex)
                return false;
        }

        //graph must have same edges
        boolean eqedge;
        for (Edge<V, E> e1 : this.edges()) {
            eqedge = false;
            for (Edge<?, ?> e2 : other.edges())
                if (e1.equals(e2))
                    eqedge = true;

            if (!eqedge)
                return false;
        }

        return true;
    }


    //string representation
    @Override
    public String toString() {
        String s = "";
        if (numberOfVertices == 0) {
            s = "\nGraph not defined!!";
        } else {
            s = "Graph: " + numberOfVertices + " vertices, " + numberOfEdges + " edges\n";
            for (Vertex<V, E> vert : verticesList) {
                s += vert + "\n";
                if (!vert.getOutgoing().isEmpty()) {
                    for (Map.Entry<Vertex<V, E>, Edge<V, E>> entry : vert.getOutgoing().entrySet()) {
                        s += entry.getValue() + "\n";
                    }
                } else
                    s += "\n";
            }
        }
        return s;
    }

}
