package DefaultTesting;

import Exceptions.EmptyCollectionException;
import Exceptions.PathNotFoundException;
import Exceptions.VertexNotFoundException;
import HauntedHouse.ClassificationManager;
import HauntedHouse.HauntedHouseGraph;
import HauntedHouse.MapManager;
import LinkedList.ArrayUnorderedList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class Start {

    public static void main(String[] args) throws EmptyCollectionException, VertexNotFoundException, PathNotFoundException, FileNotFoundException, IOException {
        MapManager mapm = new MapManager();
        HauntedHouseGraph<String> house = mapm.loadMapFromJSON("sexta-feira 13");

        /*Iterator iterator = house.iteratorShortestPath("entrada", "exterior");
        double a = house.shortestPathWeight("entrada", "exterior");
        
        while(iterator.hasNext()){
            System.out.println("Iterator: " + iterator.next());
        }
        System.out.println(a);
        
        System.out.println(house.toString());*/
       /* ClassificationManager<String> classificationM = new ClassificationManager();
        ArrayUnorderedList<String> list = new ArrayUnorderedList<>();
       classificationM.addNewClassification("Miguel", "sexta-feira 13", list, 55, 2);
       classificationM.addNewClassification("Pedro", "sexta-feira 13", list, 95, 2);
       classificationM.addNewClassification("Joao", "sexta-feira 13", list, 55, 2);
       classificationM.addNewClassification("Luis", "sexta-feira 13", list, 25, 3);
       //classificationM.addNewClassification("Luis", "sexta-feira 13", list, 25, 1);
        //System.out.println(classificationM.getClassificationTableInString());

        ArrayUnorderedList<ArrayUnorderedList<String>> ok = classificationM.getClassificationTable("sexta-feira 13", 2);
        ArrayUnorderedList<String> kek;

        Iterator it1 = ok.iterator();
        while (it1.hasNext()) {
            kek = (ArrayUnorderedList<String>) it1.next();
            Iterator it2 = kek.iterator();
            while (it2.hasNext()) {
                System.out.print(it2.next() + " ");
            }
            System.out.println();
        }
        
        System.out.println(house.getMapPreview());*/
       System.out.println(house.toString());
       house.removeVertex("cozinha");
       System.out.println(house.toString());
    }
}
