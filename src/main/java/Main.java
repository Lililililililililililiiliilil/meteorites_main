import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {
    public static void main(String[] args) throws Exception {
        Boundary AUS_boundary = new Boundary(ContinentsCoords.getAUS_coord());
        Boundary SA_boundary = new Boundary(ContinentsCoords.getSA_coord());
        Boundary NA1_boundary = new Boundary(ContinentsCoords.getNA1_coord());
        Boundary NA2_boundary = new Boundary(ContinentsCoords.getNA2_coord());
        Boundary EU_boundary = new Boundary(ContinentsCoords.getEU_coord());
        Boundary AFR_boundary = new Boundary(ContinentsCoords.getAFR_coord());
        Boundary AS1_boundary = new Boundary(ContinentsCoords.getAS1_coord());
        Boundary AS2_boundary = new Boundary(ContinentsCoords.getAS2_coord());
        Boundary ANT_boundary = new Boundary(ContinentsCoords.getANT_coord());


        ArrayList<Meteorite> meteorites = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("data_meteorite.txt"));
        String temp;
        int i = 0;
        Meteorite mt = new Meteorite();
        while ((temp = reader.readLine()) != null) {
            if (i % 10 == 0) {
                mt.setName(temp);
            }
            if (i % 10 == 1) {
                mt.setId(Integer.parseInt(temp));
            }
            if (i % 10 == 2) {
                mt.setType(temp);
            }
            if (i % 10 == 3) {
                mt.setMclass(temp);
            }
            if (i % 10 == 4) {
                mt.setWeight(Double.parseDouble(temp));
            }
            if (i % 10 == 5) {
                mt.setIsFall(temp);
            }
            if (i % 10 == 6) {
                mt.setYear(Integer.parseInt(temp));
            }
            if (i % 10 == 7) {
                mt.setLatitude(Double.parseDouble(temp));
            }
            if (i % 10 == 8) {
                mt.setLongitude(Double.parseDouble(temp));
            }
            if (i % 10 == 9) {
                mt.setCoordinates(temp);
                meteorites.add(new Meteorite(mt));


            }
            i += 1;
        }


        ArrayList<Meteorite> AUS = new ArrayList<>();
        ArrayList<Meteorite> EU = new ArrayList<>();
        ArrayList<Meteorite> AFR = new ArrayList<>();
        ArrayList<Meteorite> NA = new ArrayList<>();
        ArrayList<Meteorite> SA = new ArrayList<>();
        ArrayList<Meteorite> AS = new ArrayList<>();
        ArrayList<Meteorite> ANT = new ArrayList<>();
        ArrayList<Meteorite> OC = new ArrayList<>();


        for (Meteorite meteor : meteorites) {
            if (AUS_boundary.contains(meteor.getNew_coord())) {
                AUS.add(meteor);
            } else if (SA_boundary.contains(meteor.getNew_coord())) {
                SA.add(meteor);
            } else if (NA1_boundary.contains(meteor.getNew_coord()) || NA2_boundary.contains(meteor.getNew_coord())) {
                NA.add(meteor);
            } else if (EU_boundary.contains(meteor.getNew_coord())) {
                EU.add(meteor);
            } else if (AFR_boundary.contains(meteor.getNew_coord())) {
                AFR.add(meteor);
            } else if (AS1_boundary.contains(meteor.getNew_coord()) || AS2_boundary.contains(meteor.getNew_coord())) {
                AS.add(meteor);
            } else if (ANT_boundary.contains(meteor.getNew_coord())) {
                ANT.add(meteor);
            } else {
                OC.add(meteor);
            }
        }

        ArrayList<ArrayList<Meteorite>> continents_list = new ArrayList<ArrayList<Meteorite>>();

        Collections.addAll(continents_list, AUS, SA, NA, EU, AFR, AS, ANT, OC);

        JSONArray conts = new JSONArray();

        int temp_i = 0;

        ArrayList<String> cont_names = new ArrayList<>();

        Collections.addAll(cont_names, "Australia", "South America", "North America", "Europe", "Africa", "Asia", "Antarctica", "Oceania");

        for (ArrayList<Meteorite> cont : continents_list) {
            if (cont.size() != 0) {
                JSONObject continent_name = new JSONObject();
                continent_name.put("Continent: ", cont_names.get(temp_i));

                long fallen = cont.stream().filter(x -> x.isFall.equals("Fell")).count();
                OptionalDouble avg = cont.stream().filter(x -> x.weight > 0).mapToDouble(x -> x.weight).average();
                OptionalDouble min_weight = cont.stream().filter(x -> x.weight > 0).mapToDouble(x -> x.weight).min();
                OptionalDouble max_weight = cont.stream().mapToDouble(x -> x.weight).max();
                Meteorite min_weight_meteorite = cont.stream().filter(x -> x.weight.equals(min_weight.getAsDouble())).collect(Collectors.toCollection(ArrayList::new)).get(0);
                Meteorite max_weight_meteorite = cont.stream().filter(x -> x.weight.equals(max_weight.getAsDouble())).collect(Collectors.toCollection(ArrayList::new)).get(0);
                int total_meteorites = cont.size();

                JSONObject data = new JSONObject();
                data.put("total meteorites: ", total_meteorites);
                JSONObject data1 = new JSONObject();
                data1.put("fallen ratio: ", fallen / total_meteorites);
                JSONObject data2 = new JSONObject();
                data2.put("avg weight: ", avg.getAsDouble());
                JSONObject data3 = new JSONObject();
                data3.put("name with min weight: ", min_weight_meteorite.getName());
                JSONObject data4 = new JSONObject();
                data4.put("year of min weight: ", min_weight_meteorite.getYear());
                JSONObject data5 = new JSONObject();
                data5.put("weight of min weight: ", min_weight_meteorite.getWeight());
                JSONObject data6 = new JSONObject();
                data6.put("name with max weight: ", max_weight_meteorite.getName());
                JSONObject data7 = new JSONObject();
                data7.put("year of max weight: ", max_weight_meteorite.getYear());
                JSONObject data8 = new JSONObject();
                data8.put("weight of max weight: ", max_weight_meteorite.getWeight());
                conts.add(continent_name);
                conts.add(data);
                conts.add(data1);
                conts.add(data2);
                conts.add(data3);
                conts.add(data4);
                conts.add(data5);
                conts.add(data6);
                conts.add(data7);
                conts.add(data8);


            } else {
                JSONObject continent_name = new JSONObject();
                continent_name.put("Continent: ", cont_names.get(temp_i));
                JSONObject data = new JSONObject();
                data.put("total meteorites: ", 0);
                JSONObject data1 = new JSONObject();
                data1.put("fallen ratio: ", 0);
                JSONObject data2 = new JSONObject();
                data2.put("avg weight: ", 0);
                JSONObject data3 = new JSONObject();
                data3.put("name with min weight: ", "unknown");
                JSONObject data4 = new JSONObject();
                data4.put("year of min weight: ", "unknown");
                JSONObject data5 = new JSONObject();
                data5.put("weight of min weight: ", "unknown");
                JSONObject data6 = new JSONObject();
                data6.put("name with max weight: ", "unknown");
                JSONObject data7 = new JSONObject();
                data7.put("year of max weight: ", "unknown");
                JSONObject data8 = new JSONObject();
                data8.put("weight of max weight: ", "unknown");
                conts.add(continent_name);
                conts.add(data);
                conts.add(data1);
                conts.add(data2);
                conts.add(data3);
                conts.add(data4);
                conts.add(data5);
                conts.add(data6);
                conts.add(data7);
                conts.add(data8);
            }
            temp_i += 1;
        }
        Files.writeString(Paths.get("ans.json"), conts.toJSONString() + "\n", StandardOpenOption.APPEND);


        Map<Integer, String> continent_count = new HashMap<Integer, String>();
        continent_count.put(AUS.size(), "Australia");
        continent_count.put(NA.size(), "North America");
        continent_count.put(SA.size(), "South America");
        continent_count.put(AS.size(), "Asia");
        continent_count.put(EU.size(), "Europe");
        continent_count.put(OC.size(), "Oceania");
        continent_count.put(AFR.size(), "Africa");
        continent_count.put(ANT.size(), "Antarctica");

        List<Map.Entry<Integer, String>> result =
                continent_count.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey(Collections.reverseOrder()))
                        .collect(Collectors.toList());

        System.out.println(result);
    }
}


