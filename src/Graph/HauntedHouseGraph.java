package Graph;

import Exceptions.VertexNotFoundException;
import Models.Player;

public class HauntedHouseGraph<T> extends WeightDirectedMatrixGraph<T> implements HauntedHouseGraphADT<T> {

    private final T startPosition;
    private final T endPosition;
    private T position;
    private double healthPoints;

    public HauntedHouseGraph(Player player) throws VertexNotFoundException {
        this.healthPoints = player.getHealthPoints();
        this.startPosition = this.getPosition("Entrada");
        this.endPosition = this.getPosition("Exterior");
        this.position = this.startPosition;
        this.setDifficulty(player.getDifficulty());
    }

    @Override
    public boolean isComplete() {
        return this.position == this.endPosition;
    }

    @Override
    public boolean isAlive() {
        return this.healthPoints > 0;
    }

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

    @Override
    public T getCurrentPosition() {
        return this.position;
    }

    private T getPosition(String startVertex) throws VertexNotFoundException {
        for (int i = 0; i < this.numVertices; i++) {
            if (this.vertices[i] == startVertex) {
                return this.vertices[i];
            }
        }

        throw new VertexNotFoundException();
    }

    private void setDifficulty(int level) {
        for (int i = 0; i < this.numVertices; i++) {
            for (int j = 0; j < this.numVertices; j++) {
                if (this.adjMatrix[i][j] > 0) {
                    this.adjMatrix[i][j] = this.adjMatrix[i][j] * level;
                }
            }
        }
    }

}
