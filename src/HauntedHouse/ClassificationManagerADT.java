package HauntedHouse;

import Exceptions.EmptyCollectionException;
import LinkedList.ArrayUnorderedList;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ClassificationManagerADT<T> {

    /**
     * Method responsible for adding a new classification to the database.
     *
     * @param playerName the name of the player
     * @param mapName the map
     * @param pathTaken the path that the player took
     * @param healthPoints how many healthpoints the player ended with
     * @throws IOException if a problem happens while reading or writing to the
     * file
     */
    public void addNewClassification(String playerName, String mapName, ArrayUnorderedList<T> pathTaken, double healthPoints) throws IOException;

    /**
     * Returns the classification table.
     *
     * @return classification table
     * @throws FileNotFoundException if the classification file is not found
     * @throws EmptyCollectionException if the collection is empty
     */
    public ArrayUnorderedList<ArrayUnorderedList<String>> getClassificationTable() throws FileNotFoundException, EmptyCollectionException;

    /**
     * Returns the classification table in String format.
     *
     * @return classification table
     * @throws FileNotFoundException if the classification file is not found
     */
    public String getClassificationTableInString() throws FileNotFoundException;

}
