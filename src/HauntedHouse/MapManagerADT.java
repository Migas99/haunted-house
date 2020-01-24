package HauntedHouse;

import LinkedList.ArrayUnorderedList;
import java.io.FileNotFoundException;

public interface MapManagerADT {

    /**
     * Method that obtains the names of all the maps in our database. ´
     *
     * @return UnorderedList that contains of the name of every map in our
     * database.
     */
    public ArrayUnorderedList<String> getMaps();

    /**
     * Method that loads a map from a json file and creates an equivalent
     * HauntedHouseGraph.
     *
     * @param map that should be loaded
     * @return the graph equivalent of the json file
     * @throws FileNotFoundException if the target map doesnt exist
     */
    public HauntedHouseGraph<String> loadMapFromJSON(String map) throws FileNotFoundException;
}
