package Graph;

import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
import LinkedList.ArrayUnorderedList;
import Queue.LinkedQueue;
import Stack.LinkedList;
import java.util.Iterator;

/**
 * Directed Matrix Graph
 *
 * @param <T>
 */
public class DirectedMatrixGraph<T> implements GraphADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices; // number of vertices in the graph
    protected double[][] adjMatrix; // adjacency matrix
    protected T[] vertices; // values of vertices

    /**
     * Creates an empty graph.
     */
    public DirectedMatrixGraph() {
        this.numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Adds a vertex to the graph, expanding the capacity of the graph if
     * necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    @Override
    public void addVertex(T vertex) {
        if (this.numVertices == this.vertices.length) {
            expandCapacity();
        }

        /* É verificado se este vértice já não se encontra adicionado */
        boolean notInTheGraph = true;
        for (int i = 0; i < this.numVertices; i++) {
            /* Se for igual, significa que este vértice já existe no grafo */
            if (this.vertices[i] == vertex) {
                notInTheGraph = false;
            }
        }

        /* Caso seja válido, é então adicionado */
        if (notInTheGraph) {
            this.vertices[this.numVertices] = vertex;
            for (int i = 0; i < this.numVertices; i++) {
                this.adjMatrix[this.numVertices][i] = -1;
                this.adjMatrix[i][this.numVertices] = -1;
            }

            this.numVertices++;
        }
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex the vertex to be removed
     */
    @Override
    public void removeVertex(T vertex) {
        int index = this.getIndex(vertex);

        if (this.indexIsValid(index)) {
            /* É eliminado o vértice a ser removido do array de vértices */
            for (int i = index; i < this.numVertices; i++) {
                this.vertices[i] = this.vertices[i + 1];
            }

            /**
             * É eliminado da matriz as conecções relativas ao vértice removido
             * Puxamos as linhas para "cima"
             */
            for (int i = index; i < this.numVertices - 1; i++) {
                for (int j = 0; j < this.numVertices; j++) {
                    this.adjMatrix[j][i] = this.adjMatrix[j][i + 1];
                }
            }

            /* Puxamos as colunas para a "esquerda" */
            for (int i = 0; i < this.numVertices; i++) {
                for (int j = index; j < this.numVertices - 1; j++) {
                    this.adjMatrix[j][i] = this.adjMatrix[j + 1][i];
                }
            }

            /* Preenchemos com -1  */
            for (int i = 0; i < this.numVertices; i++) {
                this.adjMatrix[this.numVertices - 1][i] = -1;
            }
            for (int i = 0; i < this.numVertices; i++) {
                this.adjMatrix[i][this.numVertices - 1] = -1;
            }

            this.numVertices--;
        }

    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        int source = this.getIndex(vertex1);
        int destination = this.getIndex(vertex2);

        if (this.indexIsValid(source) && this.indexIsValid(destination)) {
            this.adjMatrix[source][destination] = 0;
        }

    }

    /**
     * Removes an edge between two vertices of the graph.
     *
     * @param vertex1
     * @param vertex2
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int source = this.getIndex(vertex1);
        int destination = this.getIndex(vertex2);

        if (this.indexIsValid(source) && this.indexIsValid(destination)) {
            this.adjMatrix[source][destination] = -1;
        }
    }

    /**
     * Returns an iteratorShortestPath that performs a breadth first search
     * traversal starting at the given source.
     *
     * @param startIndex the source to begin the search from
     * @return an iteratorShortestPath that performs a breadth first traversal
     * @throws EmptyCollectionException if the collection is empty
     */
    @Override
    public Iterator<T> iteratorBFS(T startIndex) throws EmptyCollectionException {
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        int index = this.getIndex(startIndex);

        if (!indexIsValid(index)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[this.numVertices];
        for (int i = 0; i < this.numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(index);
        visited[index] = true;

        Integer dequeue;
        while (!traversalQueue.isEmpty()) {
            dequeue = traversalQueue.dequeue();
            resultList.addToRear(this.vertices[dequeue]);

            /**
             * Find all vertices adjacent to x that have not been visited and
             * queue them up
             */
            for (int i = 0; i < this.numVertices; i++) {
                if ((this.adjMatrix[dequeue][i] >= 0) && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        return resultList.iterator();
    }

    /**
     * Returns an iteratorShortestPath that performs a depth first search
     * traversal starting at the given source.
     *
     * @param startIndex the source to begin the search traversal from
     * @return an iteratorShortestPath that performs a depth first traversal
     * @throws EmptyCollectionException if the collection is empty
     */
    @Override
    public Iterator<T> iteratorDFS(T startIndex) throws EmptyCollectionException {
        LinkedList<Integer> traversalStack = new LinkedList<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        int index = this.getIndex(startIndex);

        if (!indexIsValid(index)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[this.numVertices];
        for (int i = 0; i < this.numVertices; i++) {
            visited[i] = false;
        }

        traversalStack.push(index);
        resultList.addToRear(this.vertices[index]);
        visited[index] = true;

        boolean found;
        Integer peek;
        while (!traversalStack.isEmpty()) {
            peek = traversalStack.peek();
            found = false;

            /**
             * Find a vertex adjacent to x that has not been visited and push it
             * on the stack
             */
            for (int i = 0; (i < this.numVertices) && !found; i++) {
                if ((this.adjMatrix[peek][i] >= 0) && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(this.vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }

            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }

        return resultList.iterator();
    }

    /**
     * Returns a iteratorShortestPath that has the shortest path from the
     * startVertex to the targetVertex
     *
     * @param startVertex
     * @param targetVertex
     * @return iterator with the shortest parth
     * @throws EmptyCollectionException if the collection is empty
     * @throws VertexNotFoundException if one or both of the vertexs are not
     * found
     * @throws PathNotFoundException if there's not a possible path between the
     * vertexs
     */
    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) throws EmptyCollectionException, VertexNotFoundException, PathNotFoundException {
        int source = this.getIndex(startVertex);
        int destination = this.getIndex(targetVertex);

        if (this.indexIsValid(source) && this.indexIsValid(destination)) {
            return this.dijkstra(source, destination);
        } else {
            throw new VertexNotFoundException();
        }
    }

    /**
     * Checks if the graph is empty.
     *
     * @return if the graph is empty
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Checks if the graph is connected. A graph is connected when from any
     * vertex you can reach any other vertex.
     *
     * @return if is connected
     * @throws EmptyCollectionException if the collection is empty
     */
    @Override
    public boolean isConnected() throws EmptyCollectionException {
        Iterator iterator;
        int count;

        /* É realizada uma travessia em profundidade para cada vértice */
        for (int i = 0; i < this.numVertices; i++) {

            /* É obtido o iterador da travessia */
            iterator = this.iteratorDFS(this.vertices[i]);
            count = 0;

            /* São adicionados todos os vértices do iterador à lista */
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }

            if (count != this.numVertices) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the number of vertices in this graph.
     *
     * @return number of vertices
     */
    @Override
    public int size() {
        return this.numVertices;
    }

    @Override
    public String toString() {
        String toString = "";

        for (int i = 0; i < this.numVertices; i++) {
            toString = toString + this.vertices[i];
            for (int j = 0; j < this.numVertices; j++) {
                toString = toString + " | " + this.adjMatrix[i][j];
            }
            toString = toString + " | \n";
        }

        return toString;
    }

    /**
     * Returns the source of the position in the array of vertexs given an
     * vertex.
     *
     * @param vertex
     * @return position of the vertex in the array of vertexs
     */
    protected int getIndex(T vertex) {
        for (int i = 0; i < this.numVertices; i++) {
            if (this.vertices[i].equals(vertex)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Check if the source value is valid
     *
     * @param index
     * @return if the source is valid or not
     */
    protected boolean indexIsValid(int index) {
        return index >= 0;
    }

    /**
     * Increases the capacity of the vertices array and matrix.
     */
    private void expandCapacity() {
        T[] expandCapacity = (T[]) (new Object[this.vertices.length + DEFAULT_CAPACITY]);

        for (int i = 0; i < this.vertices.length; i++) {
            if (this.vertices[i] != null) {
                expandCapacity[i] = this.vertices[i];
            }
        }

        this.vertices = expandCapacity;

        double[][] expandMatrixCapacity = new double[this.adjMatrix.length + DEFAULT_CAPACITY][this.adjMatrix[0].length + DEFAULT_CAPACITY];

        for (int i = 0; i < this.adjMatrix.length; i++) {
            for (int j = 0; j < this.adjMatrix[0].length; j++) {
                expandMatrixCapacity[i][j] = this.adjMatrix[i][j];
            }
        }

        this.adjMatrix = expandMatrixCapacity;
    }

    /**
     * Method that returns an iterator with the lowest cost path. It is
     * calculated with the help of Dijkstra Algorithm.
     *
     * @param source vertex source
     * @param destination vertex target
     * @return shortest path between the source vertex and target vertex
     */
    private Iterator dijkstra(int source, int destination) throws EmptyCollectionException, PathNotFoundException {
        double[] distances = new double[this.numVertices];
        boolean[] isVertexUsed = new boolean[this.numVertices];
        int[] previous = new int[this.numVertices];

        for (int i = 0; i < this.numVertices; i++) {
            distances[i] = Double.MAX_VALUE;
            isVertexUsed[i] = false;
            previous[i] = -1;
        }

        distances[source] = 0;

        for (int i = 0; i < this.numVertices - 1; i++) {

            int cheapestVertex = minDistance(distances, isVertexUsed);
            isVertexUsed[cheapestVertex] = true;

            for (int j = 0; j < this.numVertices; j++) {
                if (!isVertexUsed[j] && this.adjMatrix[cheapestVertex][j] != -1 && distances[cheapestVertex] != Double.MAX_VALUE
                        && distances[cheapestVertex] + this.adjMatrix[cheapestVertex][j] < distances[j]) {
                    distances[j] = distances[cheapestVertex] + this.adjMatrix[cheapestVertex][j];
                    previous[j] = cheapestVertex;
                }
            }
        }

        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        int target = destination;

        if (previous[target] != -1) {
            while (target != -1) {
                resultList.addToFront(this.vertices[target]);
                target = previous[target];
            }

        }

        if (resultList.isEmpty()) {
            throw new PathNotFoundException();
        }

        return resultList.iterator();
    }

    /**
     * Calculates the lowest cost path between vertices.
     *
     * @param distances distance between vertices
     * @param isVertexUsed vertices used
     * @return value of the lowest cost
     */
    protected int minDistance(double distances[], boolean isVertexUsed[]) {
        double minDistance = Double.MAX_VALUE;
        int minDistanceIndex = -1;

        for (int i = 0; i < this.numVertices; i++) {
            if (isVertexUsed[i] == false && distances[i] <= minDistance) {
                minDistance = distances[i];
                minDistanceIndex = i;
            }
        }

        return minDistanceIndex;
    }
}
