package HauntedHouse;

import Exceptions.EmptyCollectionException;
import LinkedList.ArrayUnorderedList;
import LinkedList.DoubleLinkedOrderedList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class ClassificationManager<T> implements ClassificationManagerADT<T> {

    private final String directory;

    /**
     * Construtor que inicializa a variàvel directory, que indica onde o nosso
     * ficheiro classifications se encontra
     */
    public ClassificationManager() {
        this.directory = "database/classifications";
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
    public void addNewClassification(String playerName, String mapName, ArrayUnorderedList<T> pathTaken, double healthPoints, int level) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        JsonObject object;
        JsonObject player = new JsonObject();
        JsonArray path = new JsonArray();
        Iterator iterator = pathTaken.iterator();

        /**
         * Tentamos obter primeiro o conteúdo do ficheiro. Caso este se encontre
         * vazio, é então criado o objecto classifications
         */
        try {
            object = parser.parse(new FileReader(this.directory + "/" + mapName + ".json")).getAsJsonObject();
        } catch (IllegalStateException | FileNotFoundException e) {
            object = new JsonObject();
            object.add("classifications", new JsonArray());
        }

        /**
         * Obtemos as classificações dos jogadores, dentro do JsonObject
         * classifications e adicionamos a nova classificação
         */
        JsonArray players = object.get("classifications").getAsJsonArray();

        player.addProperty("Player", playerName);
        player.addProperty("Map", mapName);
        player.addProperty("Difficulty", this.getDifficulty(level));

        while (iterator.hasNext()) {
            path.add((String) iterator.next());
        }

        player.add("Path", path);
        player.addProperty("HealthPoints", healthPoints);

        players.add(player);

        JsonObject jsonFile = new JsonObject();
        jsonFile.add("classifications", players);

        /**
         * No fim, damos override no ficheiro todo das classificações
         */
        try (Writer writer = new FileWriter(this.directory + "/" + mapName + ".json")) {
            gson.toJson(jsonFile, writer);
            writer.flush();
        }
    }

    /**
     * Returns the classification table.
     *
     * @param mapName name of the map
     * @param level level the player played
     * @return classification table
     * @throws FileNotFoundException if the file classifications is not found
     * @throws EmptyCollectionException if the collection is empty
     */
    @Override
    public ArrayUnorderedList<ArrayUnorderedList<String>> getClassificationTable(String mapName, int level) throws FileNotFoundException, EmptyCollectionException {
        ArrayUnorderedList<ArrayUnorderedList<String>> classificationTable = new ArrayUnorderedList<>();
        ArrayUnorderedList<String> playerInfo;
        DoubleLinkedOrderedList<Double> scores = new DoubleLinkedOrderedList<>();
        ArrayUnorderedList<Integer> alreadyInTheTable = new ArrayUnorderedList<>();
        String difficulty = this.getDifficulty(level);

        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(new FileReader(this.directory + "/" + mapName + ".json")).getAsJsonObject();
        JsonArray players = object.get("classifications").getAsJsonArray();
        JsonObject player;
        JsonArray path;
        String pathtaken;

        /**
         * Obtemos todas as classificações e adicionámos a uma lista que irá os
         * ordenar por ordem crescente
         */
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i).getAsJsonObject();
            if (difficulty.equals(player.get("Difficulty").getAsString())) {
                scores.add(player.get("HealthPoints").getAsDouble());
            }
        }

        /**
         * Enquanto o tamanho da lista alreadyInTheTable for diferente do número
         * de players
         */
        boolean done = false;
        int playersNumber = scores.size();
        int i = 0;
        while (!done) {
            player = players.get(i).getAsJsonObject();

            /**
             * Verificamos se este jogador já não foi "tomado em conta", e
             * verificamos se a pontuação dele é equivalente à maior pontuação
             * que se encontra atualmente na lista que contêm todas as
             * pontuações e também verificamos se a dificuldade em que ele jogou
             * é a mesma da dificuldade da tabela que queremos apresentar
             */
            if (!alreadyInTheTable.contains(i) && scores.last() == player.get("HealthPoints").getAsDouble() && difficulty.equals(player.get("Difficulty").getAsString())) {
                alreadyInTheTable.addToRear(i);
                scores.removeLast();
                playerInfo = new ArrayUnorderedList<>();
                playerInfo.addToRear(player.get("Player").getAsString());
                playerInfo.addToRear(player.get("Map").getAsString());
                playerInfo.addToRear(player.get("Difficulty").getAsString());

                path = player.get("Path").getAsJsonArray();
                pathtaken = "Path Taken: [ ";
                for (int j = 0; j < path.size(); j++) {
                    if (j == path.size() - 1) {
                        pathtaken = pathtaken + path.get(j).getAsString();
                    } else {
                        pathtaken = pathtaken + path.get(j).getAsString() + " | ";
                    }
                }
                pathtaken = pathtaken + " ]";

                playerInfo.addToRear(pathtaken);
                playerInfo.addToRear(player.get("HealthPoints").getAsString());
                classificationTable.addToRear(playerInfo);
                i = 0;

                /**
                 * Caso as duas listas tenham o mesmo tamanho, encerramos o
                 * ciclo while
                 */
                if (playersNumber == alreadyInTheTable.size()) {
                    done = true;
                }

            } else {
                i++;
            }

        }

        return classificationTable;
    }

    /**
     * Returns the classification table.
     *
     * @return classification table
     * @throws FileNotFoundException if the player file is not found
     */
    @Override
    public String getClassificationTableInString() throws FileNotFoundException {
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
            classificationTable = classificationTable + "Path: [ ";
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
     * Converts the integer level into a String that represents its difficulty
     *
     * @param level integer level
     * @return difficulty in string
     */
    private String getDifficulty(int level) {
        String difficulty;

        switch (level) {
            case 1:
                difficulty = "Easy";
                break;
            case 2:
                difficulty = "Normal";
                break;
            default:
                difficulty = "Hard";
                break;
        }

        return difficulty;
    }
}
