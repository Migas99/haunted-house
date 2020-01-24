package HauntedHouse;

import LinkedList.ArrayUnorderedList;

public interface ClassificationManagerADT<T> {
    
    /**
     * 
     * @return 
     */
    public String getClassificationTable();
    
    /**
     * 
     * @param playerName
     * @param mapName
     * @param pathTaken
     * @param healthPoints 
     */
    public void addNewClassification(String playerName, String mapName, ArrayUnorderedList<T> pathTaken, double healthPoints);
}
