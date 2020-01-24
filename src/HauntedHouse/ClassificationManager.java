package HauntedHouse;

import LinkedList.ArrayUnorderedList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClassificationManager<T> implements ClassificationManagerADT<T> {

    private final String directory;
    private final File classificationsFile;

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
        this.classificationsFile = new File(this.directory);
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
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        ArrayUnorderedList<T> list;
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(new FileReader(this.directory));
        String json = obj.toString();
        list = new Gson().fromJson(json, new TypeToken<ArrayUnorderedList<T>>() {
        }.getType());

        FileWriter file = new FileWriter(this.directory);
        gson.toJson(list, file);
        file.flush();

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

    /**
     * Método responsável por por carregar dados do ficheiro json para um
     * arrayList
     *
     * @param path - path do ficheiro
     * @throws FileNotFoundException
     */
    private ArrayUnorderedList<T> fromFileToList(String path) throws FileNotFoundException {
        ArrayUnorderedList<T> list;
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(new FileReader(path));
        String json = obj.toString();
        list = new Gson().fromJson(json, new TypeToken<ArrayUnorderedList<T>>() {
        }.getType());

        return list;
    }

}
