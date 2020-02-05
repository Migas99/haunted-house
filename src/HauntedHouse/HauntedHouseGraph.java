package HauntedHouse;

import Exceptions.EdgeNotFoundException;
import Exceptions.EmptyCollectionException;
import Exceptions.VertexNotFoundException;
import Graph.WeightDirectedMatrixGraph;
import LinkedList.ArrayUnorderedList;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HauntedHouseGraph<T> extends WeightDirectedMatrixGraph<T> implements HauntedHouseGraphADT<T> {

    private String playerName;
    private String mapName;
    private double healthPoints;
    private int level;
    private T endPosition;
    private T position;
    private final ArrayUnorderedList<T> pathTaken;
    private final ClassificationManager<T> classification;

    public HauntedHouseGraph() {
        super();
        this.playerName = null;
        this.pathTaken = new ArrayUnorderedList<>();
        this.classification = new ClassificationManager<>();
    }

    /**
     * Change the position of the playerName to the defined position.
     *
     * @param nextPosition new position of the playerName
     * @return if the new position has a ghost
     * @throws VertexNotFoundException if the vertex target isnt found
     * @throws EdgeNotFoundException if its impossible to change position to the
     * given vertex
     */
    @Override
    public boolean changePosition(T nextPosition) throws VertexNotFoundException, EdgeNotFoundException {
        int positionIndex = this.getIndex(this.position);
        int nextPositionIndex = this.getIndex(nextPosition);

        if (this.indexIsValid(nextPositionIndex)) {
            if (this.adjMatrix[positionIndex][nextPositionIndex] != null) {
                this.position = nextPosition;
                this.pathTaken.addToRear(this.position);
                this.healthPoints = this.healthPoints - this.adjMatrix[positionIndex][nextPositionIndex];
                System.out.println(this.adjMatrix[positionIndex][nextPositionIndex]);
                return this.adjMatrix[positionIndex][nextPositionIndex] > 0;
            } else {
                throw new EdgeNotFoundException();
            }
        } else {
            throw new VertexNotFoundException();
        }
    }

    /**
     * Returns the current position of the playerName.
     *
     * @return the current position of the playerName
     */
    @Override
    public T getCurrentPosition() {
        return this.position;
    }

    /**
     *
     * @param vertex actual position of the player
     * @return list of locations connected to actual position
     * @throws VertexNotFoundException if the vertex target isnt found
     */
    @Override
    public ArrayUnorderedList<T> getAvailableDoors(T vertex) throws VertexNotFoundException {
        ArrayUnorderedList<T> options = new ArrayUnorderedList<>();
        int vertexIndex = this.getIndex(vertex);

        if (this.indexIsValid(vertexIndex)) {
            for (int i = 0; i < this.numVertices; i++) {
                if (this.adjMatrix[vertexIndex][i] != null && this.vertices[i] != vertex) {
                    options.addToRear(this.vertices[i]);
                }
            }
        } else {
            throw new VertexNotFoundException();
        }

        return options;
    }

    /**
     * Checks if the actual position of the playerName is the outside, wich
     * means that the playerName completed is the game.
     *
     * @return if the playerName completed the game or not
     */
    @Override
    public boolean isComplete() {
        return this.position == this.endPosition;
    }

    /**
     * Checks if the playerName still has health points.
     *
     * @return if the playerName health points if bigger than 0
     */
    @Override
    public boolean isAlive() {
        return this.healthPoints > 0;
    }

    /**
     * Method responsible for changing the amount of HP lost when the player
     * enters a room with a ghost, depending on the level of difficulty.
     *
     */
    @Override
    public void setDifficulty() {
        for (int i = 0; i < this.numVertices; i++) {
            for (int j = 0; j < this.numVertices; j++) {
                if (this.adjMatrix[i][j] != null) {
                    this.adjMatrix[i][j] = this.adjMatrix[i][j] * this.level;
                }
            }
        }
    }

    /**
     * Sets in wich vertex the player starts the game.
     *
     * @param vertex where the player starts.
     */
    @Override
    public void setStartPosition(T vertex) {
        this.position = vertex;
        this.pathTaken.addToRear(this.position);
    }

    /**
     * Sets in wich vrtex the player ends the game.
     *
     * @param vertex where the player ends.
     */
    @Override
    public void setEndPosition(T vertex) {
        this.endPosition = vertex;
    }

    public T getEndPosition() {
        return this.endPosition;
    }

    /**
     * Method responsible for adding a new classification to the database.
     */
    @Override
    public void addNewClassification() {
        if (this.playerName != null) {
            try {
                this.classification.addNewClassification(this.playerName, this.mapName, this.pathTaken, this.healthPoints, this.level);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Returns the classification table.
     *
     * @return classification table
     * @throws FileNotFoundException if the classifications file is not found
     */
    @Override
    public ArrayUnorderedList<ArrayUnorderedList<String>> getClassificationTable() throws FileNotFoundException {
        ArrayUnorderedList<ArrayUnorderedList<String>> table = null;

        try {
            table = this.classification.getClassificationTable(this.mapName, this.level);
        } catch (EmptyCollectionException e) {
            System.err.println(e);
        }

        return table;
    }

    /**
     * Method responsible to create the map preview.
     *
     * @return a string that is the map preview
     */
    @Override
    public String getMapPreview() {
        ArrayUnorderedList<Integer> hasGhost = new ArrayUnorderedList<>();
        String mapPreview = "<html><p> Como interpretar: '[Divisão]' -> '[Conexão]'.</p>";

        if (this.level != 3) {
            mapPreview = mapPreview + "<p>Se tiver uma '*' significa que essa divisão tem fantasma!</p>";
        }

        /**
         * Percorremos cada vértice e se o valor deste for diferente de
         * "exterior" (porque o exterior não terá conexões)
         */
        for (int i = 0; i < this.numVertices; i++) {
            if (this.vertices[i] != "exterior") {

                /**
                 * Verificamos se esta divisão tem fantasma, para adicionarmos
                 * ou não o indicador '*'
                 */
                if (hasGhost.contains(i)) {
                    mapPreview = mapPreview + "<p>[" + this.vertices[i] + "*] -> ";
                } else {
                    mapPreview = mapPreview + "<p>[" + this.vertices[i] + "] -> ";
                }

                /**
                 * Percorremos as conexões do vértice
                 */
                for (int j = 0; j < this.numVertices; j++) {
                    if (this.adjMatrix[i][j] != null && i != j) {

                        /**
                         * Caso tenha fantasma, e o nível seja diferente de 3,
                         * ou seja, o nível 3 (Hard) não permite a visualização
                         * de que divisões tem fantasma
                         */
                        if (this.adjMatrix[i][j] > 0 && this.level != 3) {
                            mapPreview = mapPreview + "[" + this.vertices[j] + "*]";
                            hasGhost.addToRear(j);
                        } else {
                            mapPreview = mapPreview + "[" + this.vertices[j] + "]";
                        }
                    }
                }

                mapPreview = mapPreview + "</p>";
            }
        }

        mapPreview = mapPreview + "</html>";
        return mapPreview;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return this.mapName;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
    }

    public double getHealthPoints() {
        return this.healthPoints;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
    
    public ArrayUnorderedList<T> getPathTaken(){
        return this.pathTaken;
    }
}
