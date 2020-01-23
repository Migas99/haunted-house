package Graph;

import Exceptions.EmptyCollectionException;

/**
 * WeightGraphADT defines the interface to a network.
 *
 * @param <T>
 */
public interface WeightGraphADT<T> extends GraphADT<T> {

    /**
     * Inserts an edge between two vertices of this graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @param weight the weight
     */
    public void addEdge(T vertex1, T vertex2, double weight);

    /**
     * Returns the weight of the shortest path in this network.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return the weight of the shortest path in this network
     * @throws EmptyCollectionException if the collection is empty
     */
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException;
}
