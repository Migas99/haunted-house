package Graph;

import Exceptions.VertexNotFoundException;
import LinkedList.ArrayUnorderedList;

public class HauntedHouseGraph<T> extends WeightDirectedMatrixGraph<T> implements HauntedHouseGraphADT<T> {

    private String playerName;
    private String mapName;
    private double healthPoints;
    private T endPosition;
    private T position;

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
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
     * Change the position of the playerName to the defined position.
     *
     * @param nextPosition new position of the playerName
     * @return if the the transaction was a sucess
     * @throws VertexNotFoundException if the vertex target isnt found
     */
    @Override
    public boolean changePosition(T nextPosition) throws VertexNotFoundException {
        int positionIndex = this.getIndex(this.position);
        int nextPositionIndex = this.getIndex(nextPosition);

        if (this.indexIsValid(positionIndex) && this.indexIsValid(nextPositionIndex)) {
            if (this.adjMatrix[positionIndex][nextPositionIndex] >= 0) {
                this.position = nextPosition;
                this.healthPoints = this.healthPoints - this.adjMatrix[positionIndex][nextPositionIndex];
                return true;
            } else {
                return false;
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
     *
     * @param level
     */
    public void setDifficulty(int level) {
        for (int i = 0; i < this.numVertices; i++) {
            for (int j = 0; j < this.numVertices; j++) {
                if (this.adjMatrix[i][j] > 0) {
                    this.adjMatrix[i][j] = this.adjMatrix[i][j] * level;
                }
            }
        }
    }

    @Override
    public void setStartPosition(T vertex) {
        this.position = vertex;
    }

    @Override
    public void setEndPosition(T vertex) {
        this.endPosition = vertex;
    }

    @Override
    public ArrayUnorderedList<T> getAvailableDoors(T vertex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
