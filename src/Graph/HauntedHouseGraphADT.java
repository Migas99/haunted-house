package Graph;

import Exceptions.VertexNotFoundException;
import LinkedList.ArrayUnorderedList;

public interface HauntedHouseGraphADT<T> {

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
     * Change the position of the player to the defined position.
     *
     * @param nextPosition new position of the player
     * @return if the the transaction was a sucess
     * @throws VertexNotFoundException if the vertex target isnt found
     */
    public boolean changePosition(T nextPosition) throws VertexNotFoundException;

    /**
     * Returns the current position of the player.
     *
     * @return the current position of the player
     */
    public T getCurrentPosition();
    
    public void setStartPosition(T vertex);
    
    public void setEndPosition(T vertex);
    
    public ArrayUnorderedList<T> getAvailableDoors(T vertex);

}
