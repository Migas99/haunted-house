package HauntedHouse;

import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
import Graph.WeightDirectedMatrixGraph;
import java.util.Iterator;

public class Start {

    public static void main(String[] args) throws EmptyCollectionException, VertexNotFoundException, PathNotFoundException {
        WeightDirectedMatrixGraph<String> house = new WeightDirectedMatrixGraph<>();

        house.addVertex("Cozinha");
        house.addVertex("Sala");
        house.addVertex("Saida");
        house.addVertex("Entrada");
        house.addVertex("Sotao");

        house.addEdge("Entrada", "Cozinha", 0);
        house.addEdge("Cozinha", "Sala", 0);
        house.addEdge("Sala", "Saida", 0);
       // house.addEdge("Entrada", "Saida", 0);

        Iterator iterator = house.iteratorShortestPath("Entrada", "Saida");
        
        while(iterator.hasNext()){
            System.out.println("Iterator: " + iterator.next());
        }
        
        System.out.println(house.toString());
        
    }
}
