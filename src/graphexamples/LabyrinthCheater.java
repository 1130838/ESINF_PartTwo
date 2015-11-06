
package graphexamples;

import graph.AdjacencyMatrixGraph;
import graphbase.Vertex;

import java.util.LinkedList;
import java.util.Objects;

/**
 * A class to represent a labyrinth map with rooms, doors and exits
 * Methods discover the nearest exit and the path to it
 * Stores a graph of private Room vertex and Door edges
 *
 * @author DEI-ESINF
 * 
 */
public class LabyrinthCheater {

    public AdjacencyMatrixGraph<Room, Door> map;

    public LabyrinthCheater(){
        map = new AdjacencyMatrixGraph<Room, Door>();
    }


    private class Room{

        private String name;
        private boolean hasExit;

        public Room(String name, boolean hasExit) {
            this.name = name;
            this.hasExit = hasExit;

        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }

        // necessário fazer override do método equals para saber se os Rooms têm as mesma referencia
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Room other = (Room) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }


    }



    private class Door{

    }



    /**
     * Inserts a new room in the map
     * @param city
     * @return false if city exists in the map
     */
    public boolean insertRoom(String name, boolean hasExit){

        return map.insertVertex(new Room(name, hasExit));
    }

    /**
     * Inserts a new door in the map
     * fails if room does not exist or door already exists
     * @param from room
     * @param to room
     * @return false if a room does not exist or door already exists between rooms
     */
    public boolean insertDoor(String from, String to){

        return  map.insertEdge(new Room(from, false), new Room(to, false), new Door());
    }

    /**
     * Returns a list of rooms which are reachable from one room
     * @param room 
     * @return list of room names or null if room does not exist
     */
    public Iterable<String> roomsInReach(String room){

        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the nearest room with an exit
     * @param room from room
     * @return room name or null if from room does not exist or there is no reachable exit
     */
    public String nearestExit(String room){
        throw new UnsupportedOperationException("Not supported yet.");	
    }

    /**
     * Returns the shortest path to the nearest room with an exit
     * @param room from room
     * @return list of room names or null if from room does not exist or there is no reachable exit
     */
    public LinkedList<String> pathToExit(String from){
        throw new UnsupportedOperationException("Not supported yet.");	
    }


}

