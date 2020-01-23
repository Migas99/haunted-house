package Graph;

import Exceptions.VertexNotFoundException;
import Gameplay.Player;

public class HauntedHouseGraph<T> extends WeightDirectedMatrixGraph<T> {

    private int position;
    private final String difficulty;
    private final int healthPoints;

    public HauntedHouseGraph(Player player) throws VertexNotFoundException {
        this.difficulty = player.getDifficulty();
        this.healthPoints = player.getHealthPoints();
        this.position = this.getInitialPosition("Entrada");
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int getInitialPosition(String startVertex) throws VertexNotFoundException {
        for (int i = 0; i < this.numVertices; i++) {
            if (this.vertices[i] == startVertex) {
                return i;
            }
        }

        throw new VertexNotFoundException();
    }
}
