package Graph;

import BinaryTree.LinkedHeap;
import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;

public class WeightDirectedMatrixGraph<T> extends DirectedMatrixGraph<T> implements WeightGraphADT<T> {

    /**
     * Creates an empty graph.
     */
    public WeightDirectedMatrixGraph() {
        super();
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int source = this.getIndex(vertex1);
        int destination = this.getIndex(vertex2);

        if (this.indexIsValid(source) && this.indexIsValid(destination)) {
            this.adjMatrix[source][destination] = weight;
        }
    }

    /**
     * Returns the lowest cost path between two vertexs.
     *
     * @param vertex1 vertex source
     * @param vertex2 vertex destination
     * @return the cost of the transaction
     * @throws EmptyCollectionException if the collection is empty
     * @throws PathNotFoundException if there's not a possible path between the
     * vertexs
     * @throws VertexNotFoundException if one or both of the vertexs are not
     * found
     */
    @Override
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException, PathNotFoundException, VertexNotFoundException {
        int source = this.getIndex(vertex1);
        int destination = this.getIndex(vertex2);

        if (this.indexIsValid(source) && this.indexIsValid(destination)) {
            return this.dijkstra(source, destination);
        } else {
            throw new VertexNotFoundException();
        }
    }

    /**
     * Method that returns the cost of the lowest cost path. It is calculated
     * with the help of Dijkstra Algorithm.
     *
     * @param source vertex source
     * @param destination vertex target
     * @return cost of the lowest cost path
     */
    private double dijkstra(int source, int destination) throws PathNotFoundException {
        double[] weight = new double[this.numVertices];
        boolean[] isVertexUsed = new boolean[this.numVertices];

        for (int i = 0; i < this.numVertices; i++) {
            weight[i] = Double.MAX_VALUE;
            isVertexUsed[i] = false;
        }

        weight[source] = 0;

        for (int i = 0; i < this.numVertices - 1; i++) {

            int cheapestVertex = this.minDistance(weight, isVertexUsed);
            isVertexUsed[cheapestVertex] = true;

            for (int j = 0; j < this.numVertices; j++) {
                if (!isVertexUsed[j] && this.adjMatrix[cheapestVertex][j] != -1 && weight[cheapestVertex] != Double.MAX_VALUE
                        && weight[cheapestVertex] + this.adjMatrix[cheapestVertex][j] < weight[j]) {
                    weight[j] = weight[cheapestVertex] + this.adjMatrix[cheapestVertex][j];
                }
            }
        }

        if (!isVertexUsed[destination]) {
            throw new PathNotFoundException();
        }

        return weight[destination];
    }
    
    /**
     * Returns a minimum spanning tree of the network.
     *
     * @return a minimum spanning tree of the network
     * @throws EmptyCollectionException if the collection is empty
     */
    public WeightDirectedMatrixGraph minimumSpanningTree() throws EmptyCollectionException {
        LinkedHeap<Double> minHeap = new LinkedHeap<>();
        WeightDirectedMatrixGraph<T> resultGraph = new WeightDirectedMatrixGraph<>();

        if (this.isEmpty() || !this.isConnected()) {
            return resultGraph;
        }

        resultGraph.adjMatrix = new double[this.numVertices][this.numVertices];
        for (int i = 0; i < this.numVertices; i++) {
            for (int j = 0; j < this.numVertices; j++) {
                resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        resultGraph.vertices = (T[]) (new Object[this.numVertices]);

        boolean[] visited = new boolean[this.numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        int[] edge = new int[2];
        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        /**
         * Add all edges, which are adjacent to the starting vertex, to the heap
         */
        for (int i = 0; i < this.numVertices; i++) {
            minHeap.addElement(adjMatrix[0][i]);
        }

        int index, x, y;
        double weight;
        while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {

            /**
             * Get the edge with the smallest weight that has exactly one vertex
             * already in the resultGraph
             */
            do {
                weight = (minHeap.removeMin());
                edge = this.getEdgeWithWeightOf(weight, visited);
            } while (!indexIsValid(edge[0]) || !indexIsValid(edge[1]));

            x = edge[0];
            y = edge[1];

            if (!visited[x]) {
                index = x;
            } else {
                index = y;
            }

            /**
             * Add the new edge and vertex to the resultGraph
             */
            resultGraph.vertices[index] = this.vertices[index];
            visited[index] = true;
            resultGraph.numVertices++;
            resultGraph.adjMatrix[x][y] = this.adjMatrix[x][y];
            resultGraph.adjMatrix[y][x] = this.adjMatrix[y][x];

            /**
             * Add all edges, that are adjacent to the newly added vertex, to
             * the heap
             */
            for (int i = 0; i < this.numVertices; i++) {
                if (!visited[i] && (this.adjMatrix[i][index] < Double.POSITIVE_INFINITY)) {
                    edge[0] = index;
                    edge[1] = i;
                    minHeap.addElement(this.adjMatrix[index][i]);
                }
            }
        }

        return resultGraph;
    }

    /**
     * Method that returns an array that contains the index of the vertexs wich
     * the edge connects.
     *
     * @param weight
     * @param visited array with visited vertexs
     * @return array with the vertexs wich the edge connects
     */
    private int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = new int[2];

        for (int i = 0; i < this.numVertices; i++) {
            if (visited[i]) {
                for (int j = 0; j < this.numVertices; j++) {
                    if (this.adjMatrix[i][j] == weight) {
                        edge[0] = i;
                        edge[1] = j;
                        return edge;
                    }
                }
            }
        }

        return edge;
    }

}
