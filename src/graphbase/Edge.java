
package graphbase;

/**
 * 
 * @author DEI-ESINF
 * @param <V>
 * @param <E>
 */

public class Edge<V,E> implements Comparable {
    
    private E element;           // Edge information
    private double weight;       // Edge weight
    private Vertex<V,E> vOrig;  // vertex origin
    private Vertex<V,E> vDest;  // vertex destination
    
    public Edge() {
        element = null; weight= 0.0; vOrig=null; vDest=null; } 
    
    public Edge(E eInf, double ew, Vertex<V,E> vo, Vertex<V,E> vd) {
        element = eInf; weight= ew; vOrig=vo; vDest=vd;} 
  
    public E getElement() { return element; }	 
    public void setElement(E eInf) { element = eInf; }
    
    public double getWeight() { return weight; }	 
    public void setWeight(double ew) { weight= ew; }
    
    public Vertex<V,E> getVOrig() { return vOrig; }	 
    public void setVOrig(Vertex<V,E> vo) { vOrig= vo; }
    
    public Vertex<V,E> getVDest() { return vDest; }	 
    public void setVDest(Vertex<V,E> vd) { vDest= vd; }
    
    public Vertex<V,E>[] getEndpoints() { 
        Vertex<V,E>[] endverts = new Vertex[2];
        endverts[0]=vOrig; endverts[1]=vDest;
        return endverts; 
    }
           
    @Override
    public boolean equals(Object otherObj) {
        
        if (this == otherObj){
            return true;
        }
        if (otherObj == null || this.getClass() != otherObj.getClass()){
            return false;
        }
        Edge<V,E> otherEdge = (Edge<V,E>) otherObj;
        
        return (this.weight == otherEdge.weight && 
                this.element != null && otherEdge.element != null &&
                this.element.equals(otherEdge.element));
    }
    
    @Override
    public int compareTo(Object otherObject) {
        
        Edge<V,E> other = (Edge<V,E>) otherObject ;
        if (this.weight < other.weight)  return -1;
        if (this.weight == other.weight) return 0;
        return 1;
    }
       
    @Override
    public String toString() {
        String st="";
        if (element != null)
           st= "\t- (" + element + ")";
        else
            st= "\t- "; 
            
        if (weight != 0)
            st += weight +" - " +vDest.getElement();
        else
            st += vDest.getElement();

        return st;
    }

}
