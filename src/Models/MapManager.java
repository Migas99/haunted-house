package Models;

import Graph.HauntedHouseGraph;
import LinkedList.ArrayUnorderedList;
import com.google.gson.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MapManager {

    private final String directory;
    private final File file;
    private final File[] files;

    public MapManager() {
        this.directory = "database/maps";
        this.file = new File(this.directory);
        this.files = this.file.listFiles();
    }

    public ArrayUnorderedList<String> getMaps() {
        ArrayUnorderedList<String> maps = new ArrayUnorderedList<>();

        for (int i = 0; i < this.files.length; i++) {
            maps.addToRear(this.files[i].getName().replace(".json", ""));
        }

        return maps;
    }

    /**
     * Method that loads a map from a json file and creates an equivalent
     * HauntedHouseGraph.
     *
     * @param map that should be loaded
     * @return the graph equivalent of the json file
     * @throws FileNotFoundException if the target map doesnt exist
     */
    public HauntedHouseGraph<String> loadMapFromJSON(String map) throws FileNotFoundException {
        HauntedHouseGraph<String> house = new HauntedHouseGraph<>();

        /**
         * Vamos buscar o nome do mapa e o número máximo de healthpoints
         */
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(new FileReader(this.directory + "/" + map + ".json")).getAsJsonObject();
        house.setMapName(object.get("nome").getAsString());
        house.setHealthPoints(object.get("pontos").getAsDouble());

        /**
         * Vamos buscar agora apenas os vértices que irão existir neste grafo
         */
        JsonArray mapa = object.get("mapa").getAsJsonArray();

        /**
         * Percorremos todas as rooms, encontramos o "vértice" e adicionámos ao
         * grafo como um vértice. Visto que o vértice "entrada" e "exterior"
         * apenas aparecem nas ligações, adicionamos no ambos, um no início e
         * outro no fim, respectivamente.
         */
        JsonObject room;
        house.addVertex("entrada");
        for (int i = 0; i < mapa.size(); i++) {
            room = mapa.get(i).getAsJsonObject();
            house.addVertex(room.get("aposento").getAsString());
        }
        house.addVertex("exterior");

        /**
         * Tendo todos os vértices, vamos agora criar as conecções
         */
        double cost = 0;
        JsonArray connections;
        String aposento;
        String connection;

        /**
         * Percorremos cada um dos vértices
         */
        for (int i = 0; i < mapa.size(); i++) {
            room = mapa.get(i).getAsJsonObject();
            cost = room.get("fantasma").getAsDouble();
            aposento = room.get("aposento").getAsString();
            connections = room.get("ligacoes").getAsJsonArray();

            /**
             * Percorremos as conecções respectivas a este vértice
             */
            for (int j = 0; j < connections.size(); j++) {
                connection = connections.get(j).getAsString();

                /**
                 * Se o connection for "entrada", significa que a ligação será
                 * inversa
                 */
                if ("entrada".equals(aposento)) {
                    house.addEdge(connection, aposento, 0);
                } else {
                    house.addEdge(aposento, connection, cost);
                }

            }
        }

        return house;
    }

}
