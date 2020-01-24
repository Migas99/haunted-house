package HauntedHouse;

import LinkedList.ArrayUnorderedList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClassificationManager<T> implements ClassificationManagerADT<T> {

    private String directory;

    @Expose
    private String playerName;
    @Expose
    private String mapName;
    @Expose
    private ArrayUnorderedList<T> pathTaken;
    @Expose
    private double healthPoints;

    public ClassificationManager() {
        this.directory = "database/classifications.json";
    }

    public ClassificationManager(String playerName, String mapName, ArrayUnorderedList<T> pathTaken, double healthPoints) {
        this.playerName = playerName;
        this.mapName = mapName;
        this.pathTaken = pathTaken;
        this.healthPoints = healthPoints;
    }

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
    @Override
    public void addNewClassification(String playerName, String mapName, ArrayUnorderedList<T> pathTaken, double healthPoints) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        FileWriter file = new FileWriter(this.directory, true);
        ClassificationManager classification = new ClassificationManager(playerName, mapName, pathTaken, healthPoints);
        gson.toJson(this, file);

    }

    /**
     * Returns the classification table.
     *
     * @return classification table
     * @throws FileNotFoundException if the player file is not found
     */
    @Override
    public String getClassificationTable() throws FileNotFoundException {
        String classificationTable = "--- Classifications Table ---\n";

        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(new FileReader(this.directory)).getAsJsonObject();
        JsonArray players = object.get("classifications").getAsJsonArray();

        JsonObject player;
        JsonArray path;

        for (int i = 0; i < players.size(); i++) {
            classificationTable = classificationTable + "-----------------------------\n";
            player = players.get(i).getAsJsonObject();
            classificationTable = classificationTable + "Player: " + player.get("Player").getAsString() + "\n";
            classificationTable = classificationTable + "Map: " + player.get("Map").getAsString() + "\n";

            path = player.get("Path").getAsJsonArray();
            classificationTable = classificationTable + "Path: [";
            for (int j = 0; j < path.size(); j++) {
                classificationTable = classificationTable + path.get(i).getAsString() + " ";
            }
            classificationTable = classificationTable + "]\n";

            classificationTable = classificationTable + "HealthPoints: " + player.get("HealthPoints").getAsDouble() + "\n";
            classificationTable = classificationTable + "-----------------------------\n";
        }

        return classificationTable;
    }

}
