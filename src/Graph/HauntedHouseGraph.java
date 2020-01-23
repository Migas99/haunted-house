package Graph;

import Exceptions.VertexNotFoundException;
import Gameplay.Player;

public class HauntedHouseGraph<T> extends WeightDirectedMatrixGraph<T> {

    private T position;
    private final String difficulty;
    private final int healthPoints;

    public HauntedHouseGraph(Player player) throws VertexNotFoundException {
        this.difficulty = player.getDifficulty();
        this.healthPoints = player.getHealthPoints();
        this.position = this.getInitialPosition("Entrada");
    }

    public boolean changePosition(T position, T nextPosition){
        return true;
    }
    
    public T getPosition() {
        return position;
    }

    public void setPosition(T position) {
        this.position = position;
    }

    private T getInitialPosition(String startVertex) throws VertexNotFoundException {
        for (int i = 0; i < this.numVertices; i++) {
            if (this.vertices[i] == startVertex) {
                return this.vertices[i];
            }
        }

        throw new VertexNotFoundException();
    }
}
