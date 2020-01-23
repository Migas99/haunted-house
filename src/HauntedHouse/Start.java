package HauntedHouse;

import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
import Graph.WeightDirectedMatrixGraph;
import Models.MapManager;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Start {

    public static void main(String[] args) throws EmptyCollectionException, VertexNotFoundException, PathNotFoundException, FileNotFoundException {
        MapManager mapm = new MapManager();
        WeightDirectedMatrixGraph<String> house = mapm.loadMapFromJSON("sexta-feira 13");

        /*house.addVertex("Cozinha");
        house.addVertex("Sala");
        house.addVertex("Saida");
        house.addVertex("Entrada");
        house.addVertex("Sotao");

        house.addEdge("Entrada", "Cozinha", 0);
        house.addEdge("Cozinha", "Sala", 0);
        house.addEdge("Sala", "Saida", 0);*/
       // house.addEdge("Entrada", "Saida", 0);

        /*Iterator iterator = house.iteratorShortestPath("entrada", "exterior");
        double a = house.shortestPathWeight("entrada", "cozinha");
        
        while(iterator.hasNext()){
            System.out.println("Iterator: " + iterator.next());
        }
        System.out.println(a);*/
        
        System.out.println(house.toString());
        
    }
}
