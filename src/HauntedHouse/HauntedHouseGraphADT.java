package HauntedHouse;

import Exceptions.EdgeNotFoundException;
import Exceptions.VertexNotFoundException;
import Graph.WeightGraphADT;
import LinkedList.ArrayUnorderedList;
import java.io.FileNotFoundException;

public interface HauntedHouseGraphADT<T> extends WeightGraphADT<T> {

    /**
     * Change the position of the player to the defined position.
     *
     * @param nextPosition new position of the player
     * @return if the new position has a ghost
     * @throws VertexNotFoundException if the vertex target isnt found
     * @throws EdgeNotFoundException if its impossible to change position to the
     * given vertex
     */
    public boolean changePosition(T nextPosition) throws VertexNotFoundException, EdgeNotFoundException;

    /**
     * Returns the current position of the player.
     *
     * @return the current position of the player
     */
    public T getCurrentPosition();

    /**
     *
     * @param vertex actual position of the player
     * @return list of locations connected to actual position
     * @throws VertexNotFoundException if the vertex target isnt found
     */
    public ArrayUnorderedList<T> getAvailableDoors(T vertex) throws VertexNotFoundException;

    /**
     * Checks if the actual position of the player is the outside, wich means
     * that the player completed is the game.
     *
     * @return if the player completed the game or not
     */
    public boolean isComplete();

    /**
     * Checks if the player still has health points.
     *
     * @return if the player health points if bigger than 0
     */
    public boolean isAlive();

    /**
     * Method responsible for changing the amount of HP lost when the player
     * enters a room with a ghost, depending on the level of difficulty.
     */
    public void setDifficulty();

    /**
     * Sets in wich vertex the player starts the game.
     *
     * @param vertex where the player starts.
     */
    public void setStartPosition(T vertex);

    /**
     * Sets in wich vrtex the player ends the game.
     *
     * @param vertex where the player ends.
     */
    public void setEndPosition(T vertex);

    /**
     * Method responsible for adding a new classification to the database.
     */
    public void addNewClassification();

    /**
     * Returns the classification table.
     *
     * @return classification table
     * @throws FileNotFoundException if the classifications file is not found
     */
    public ArrayUnorderedList<ArrayUnorderedList<String>> getClassificationTable() throws FileNotFoundException;

}
