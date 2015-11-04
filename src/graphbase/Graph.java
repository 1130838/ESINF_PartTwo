
package graphbase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author DEI-ESINF
 * @param <V>
 * @param <E>
 */

public class Graph<V,E> implements GraphInterface<V,E> {
    
    private int numVert;
    private int numEdge;
    private boolean isDirected;
    private ArrayList<Vertex<V,E>> listVert;  //Vertice list
    
    // Constructs an empty graph (either undirected or directed)
    public Graph(boolean directed) { 
        numVert=0; 
        numEdge=0;
        isDirected=directed;
        listVert = new ArrayList<>();
    }
      
    public int numVertices(){ return numVert; }
      
    public Iterable<Vertex<V,E>> vertices() { return listVert; }
    
    public int numEdges(){ return numEdge; }
    
    public Iterable<Edge<V,E>> edges() { 
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Edge<V,E> getEdge(Vertex<V,E> vorig, Vertex<V,E> vdest){
       
        if (listVert.contains(vorig) && listVert.contains(vdest))
           return vorig.getOutgoing().get(vdest);

        return null;
    }
    
    public Vertex<V,E>[] endVertices(Edge<V,E> e){ 
   
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Vertex<V,E> opposite(Vertex<V,E> vert, Edge<V,E> e){
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int outDegree(Vertex<V,E> v){
 
        if (listVert.contains(v))
            return v.getOutgoing().size();
        else
            return -1;
    }
     
    public int inDegree(Vertex<V,E> v){
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    public Iterable<Edge<V,E>> outgoingEdges(Vertex<V,E> v){
 
        if (!listVert.contains(v))
            return null;
        
        ArrayList<Edge<V,E>> edges = new ArrayList<>();
        
        Map<Vertex<V,E>,Edge<V,E>>  map = v.getOutgoing();          
        Iterator<Map.Entry<Vertex<V,E>,Edge<V,E>>> it = map.entrySet().iterator();
        while(it.hasNext()) 
            edges.add(it.next().getValue());
       
        return edges ;
    }
    
    public Iterable<Edge<V,E>> incomingEdges(Vertex<V,E> v){
 
        throw new UnsupportedOperationException("Not supported yet.");
    }
            
    public Vertex<V,E> insertVertex(V vInf){
         
        Vertex<V,E> vert = getVertex(vInf);
        if (vert == null){
            Vertex<V,E> newvert = new Vertex<>(numVert,vInf);
            listVert.add(newvert);
            numVert++;
            return newvert;
        }
        return vert;  
    }
    
    public Edge<V,E> insertEdge(V vOrig, V vDest, E eInf, double eWeight){
        
        Vertex<V,E> vorig = getVertex(vOrig) ;
        if (vorig == null)
            vorig = insertVertex(vOrig);
        
        Vertex<V,E> vdest = getVertex(vDest) ;
        if (vdest == null)
             vdest = insertVertex(vDest);
        
        if (getEdge(vorig,vdest) == null) {               
            Edge<V,E> newedge = new Edge<>(eInf,eWeight,vorig,vdest);
            vorig.getOutgoing().put(vdest,newedge);
            numEdge++;
             
            //if graph is not direct insert other edge in the opposite direction 
            if (!isDirected)
                if (getEdge(vdest,vorig) == null) { 
                    Edge<V,E> otheredge = new Edge<>(eInf,eWeight,vdest,vorig);
                    vdest.getOutgoing().put(vorig,otheredge);
                    numEdge++;
                }       
            
            return newedge ;
        }
        return null ;
    }
    
    public void removeVertex(V vInf){
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void removeEdge(Edge<V,E> edge) {
        
        Vertex<V,E>[] endpoints = endVertices(edge);
        
        Vertex<V,E> vorig = endpoints[0] ;
        Vertex<V,E> vdest = endpoints[1] ;
        
        if (vorig != null && vdest != null) 
            if (edge.equals(getEdge(vorig,vdest))){
               vorig.getOutgoing().remove(vdest);
               numEdge--;
            } 
    }
           
    public Vertex<V,E> getVertex(V vInf){
        
        for (Vertex<V,E> vert : this.listVert)  
            if (vInf.equals(vert.getElement()))
                return vert;
    
        return null;
    }
    
    public Vertex<V,E> getVertex(int vKey){
   
        if (vKey < listVert.size())
            return listVert.get(vKey);
        
        return null;
    }
    
    //Returns a clone of the graph 
    public Graph<V,E> clone() {
        
        Graph<V,E> newObject = new Graph<>(this.isDirected);

        newObject.numVert = this.numVert;
        
        newObject.listVert = new ArrayList<>();        
        for (Vertex<V,E> v : this.listVert)   
            newObject.listVert.add(new Vertex<V,E>(v.getKey(), v.getElement()));
        
        for (Vertex<V,E> v1 : this.listVert) 
            for (Edge<V,E> e : this.outgoingEdges(v1))  
                if (e != null){
                   Vertex<V,E> v2=this.opposite(v1,e);
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

        if (!(oth instanceof Graph<?,?>))
            return false;

        Graph<?, ?> other = (Graph<?, ?>) oth;

        if (numVert != other.numVert || numEdge != other.numEdge) 
            return false;

        //graph must have same vertices
        boolean eqvertex;
        for (Vertex<V,E> v1 : this.vertices()){
            eqvertex=false;
            for (Vertex<?,?> v2 : other.vertices())
                if (v1.equals(v2))
                    eqvertex=true;
            
            if (!eqvertex)
              return false;
        }
        
         //graph must have same edges
        boolean eqedge;
        for (Edge<V,E> e1 : this.edges()){
            eqedge=false;
            for (Edge<?,?> e2 : other.edges())
                if (e1.equals(e2))
                    eqedge=true;
            
            if (!eqedge)
              return false;
        }

        return true;
    }

    
    //string representation
    @Override
    public String toString() {
        String s="" ;
        if (numVert == 0) {
            s = "\nGraph not defined!!";
        } 
        else {
            s = "Graph: "+ numVert + " vertices, " + numEdge + " edges\n";
            for (Vertex<V,E> vert : listVert) {
                s += vert + "\n" ;
                if (!vert.getOutgoing().isEmpty()){
                   for (Map.Entry<Vertex<V,E>, Edge<V,E>> entry : vert.getOutgoing().entrySet()){
                       s += entry.getValue() + "\n" ;
                   }
                }
                else
                   s += "\n" ; 
            }
        }
        return s ;
    }
    
}
