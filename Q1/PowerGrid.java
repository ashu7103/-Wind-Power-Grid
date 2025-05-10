import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

class PowerLine {
    String cityA;
    String cityB;

    public PowerLine(String cityA, String cityB) {
        this.cityA = cityA;
        this.cityB = cityB;
    }
}

// Students can define new classes here

public class PowerGrid {
    int numCities;
    int numLines;
    String[] cityNames;
    PowerLine[] powerLines;
    private int counter;
    private int time_new;
    private int ceilLog;
    private HashMap<String, ArrayList<String>> graph; // done
    private HashMap<String, ArrayList<String>> graph2; // done   // it is graph without bridges
    private HashMap<String, ArrayList<String>> graph3; // done    // graph only with bridge
    private ArrayList<PowerLine> bridges;
    private HashMap<String, ArrayList<String>> ancestors;
    private HashMap<String, ArrayList<Integer>> ancestors_numbers;
    private HashMap<String, Integer> included;
    private HashMap<String, Integer> no_of_steps;
    private HashMap<String, Integer> lowest_no_of_steps;
    private HashMap<String, String> parent;
    private int time;
    private ArrayList<String> bridgenode;
    private HashMap<String, String> newname;
    private HashMap<String, Integer> t_in;
    private HashMap<String, Integer> t_out;
    private HashMap<String, Integer> heightarray;
    // Students can define private variables here

    public PowerGrid(String filename) throws Exception {
        bridgenode = new ArrayList<>();
        ceilLog = 0;
        time_new = 0;
        counter = 0;
        newname = new HashMap<>();
        included = new HashMap<>();
        graph = new HashMap<>();
        graph2 = new HashMap<>();
        graph3 = new HashMap<>();
        bridges = new ArrayList<>();
        no_of_steps = new HashMap<>();
        lowest_no_of_steps = new HashMap<>();
        parent = new HashMap<>();
        heightarray = new HashMap<>();
        time = 0;
        t_in = new HashMap<>();
        t_out = new HashMap<>();
        ancestors = new HashMap<>();
        ancestors_numbers = new HashMap<>();
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        numCities = Integer.parseInt(br.readLine());
        numLines = Integer.parseInt(br.readLine());
        cityNames = new String[numCities];
        for (int i = 0; i < numCities; i++) {
            cityNames[i] = br.readLine();
            graph.put(cityNames[i], new ArrayList<>());
            graph2.put(cityNames[i], new ArrayList<>());
        }
        powerLines = new PowerLine[numLines];
        for (int i = 0; i < numLines; i++) {
            String[] line = br.readLine().split(" ");
            powerLines[i] = new PowerLine(line[0], line[1]);
            graph.get(line[0]).add(line[1]);
            graph.get(line[1]).add(line[0]);
            graph2.get(line[0]).add(line[1]);
            graph2.get(line[1]).add(line[0]);
        }
        // TO be completed by students
    }

    public ArrayList<PowerLine> criticalLines() {
        /*
         * Implement an efficient algorithm to compute the critical transmission lines
         * in the power grid.

         * Expected running time: O(m + n), where n is the number of cities and m is the
         * number of transmission lines.
         */
        if (counter == 0) {
            counter++;
            // findBridges(graph);
            dfs(cityNames[0]);
        }
        return bridges;
    }
    private void dfs(String vertex) {
        no_of_steps.put(vertex, time);
        lowest_no_of_steps.put(vertex, time);
        time++;
        for (String neighbor : graph.get(vertex)) {
            if (no_of_steps.containsKey(neighbor) == false) {
                parent.put(neighbor, vertex);
                dfs(neighbor);
                lowest_no_of_steps.put(vertex, Math.min(lowest_no_of_steps.get(vertex), lowest_no_of_steps.get(neighbor)));
                if (lowest_no_of_steps.get(neighbor) > no_of_steps.get(vertex)) {
                    if (included.containsKey(vertex) == false) {
                        bridgenode.add(vertex);
                        included.put(vertex, 0);
                    }
                    if (included.containsKey(neighbor) == false) {
                        bridgenode.add(neighbor);
                        included.put(neighbor, 0);
                    }
                    PowerLine bridge = new PowerLine(vertex, neighbor);
                    bridges.add(bridge);
                    if (graph2.containsKey(vertex) == true) {
                        graph2.get(vertex).remove(neighbor);
                        if (graph2.get(vertex).size() == 0)
                            graph2.remove(vertex);
                    }
                    if (graph2.containsKey(neighbor) == true) {
                        graph2.get(neighbor).remove(vertex);
                        if (graph2.get(neighbor).size() == 0)
                            graph2.remove(neighbor);
                    }
                }
                // else{
                //     PowerLine bridge = new PowerLine(vertex,neighbor);
                //     if(graph3.containsKey(vertex)==true){
                //         graph3.get(vertex).remove(neighbor);
                //         if(graph3.get(vertex).size()==0) graph3.remove(vertex);
                //     }
                //     if(graph3.containsKey(neighbor)==true){
                //         graph3.get(neighbor).remove(vertex);
                //         if(graph3.get(neighbor).size()==0) graph3.remove(neighbor);
                //     }
                // }
            } else if (!neighbor.equals(parent.get(vertex))) {
                lowest_no_of_steps.put(vertex, Math.min(lowest_no_of_steps.get(vertex), no_of_steps.get(neighbor)));
            }
        }
    }
    private ArrayList<ArrayList<String>> getConnectedComponents(HashMap<String, ArrayList<String>> graph) {
        HashMap<String, Boolean> visited = new HashMap<>();
        ArrayList<ArrayList<String>> components = new ArrayList<>();
        for (String vertex : graph.keySet()) {
            if (!visited.containsKey(vertex)) {
                ArrayList<String> component = new ArrayList<>();
                dfs(vertex, graph, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void dfs(String vertex, HashMap<String, ArrayList<String>> graph, HashMap<String, Boolean> visited, ArrayList<String> component) {
        visited.put(vertex, true);
        component.add(vertex);
        ArrayList<String> neighbors = graph.get(vertex);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.containsKey(neighbor)) {
                    dfs(neighbor, graph, visited, component);
                }
            }
        }
    }
    public void preprocessImportantLines() {
        /*
         * Implement an efficient algorithm to preprocess the power grid and build
         * required data structures which you will use for the numImportantLines()
         * method. This function is called once before calling the numImportantLines()
         * method. You might want to define new classes and data structures for this
         * method.

         * Expected running time: O(n * logn), where n is the number of cities.
         */
        criticalLines();
        ArrayList<ArrayList<String>> allConnectedComponents = getConnectedComponents(graph2);
        for (int j = 0; j < allConnectedComponents.size(); j++) {
            for (int i = 0; i < allConnectedComponents.get(j).size(); i++) {
                String s = "keshri" + String.valueOf(j);
                newname.put(allConnectedComponents.get(j).get(i), s);
            }
        }
        if (bridges.size() == 0)
            return;
        for (int i = 0; i < bridgenode.size(); i++) graph3.put(bridgenode.get(i), new ArrayList<>());
        for (int i = 0; i < bridges.size(); i++) {
            graph3.get(bridges.get(i).cityA).add(bridges.get(i).cityB);
            graph3.get(bridges.get(i).cityB).add(bridges.get(i).cityA);
        }
        HashMap<String, ArrayList<String>> graph3_new = new HashMap<>();
        ArrayList<String> Newcities = new ArrayList<>();
        for (String iter : graph3.keySet()) {
            if (newname.containsKey(iter) == true) {
                ArrayList<String> nnn = new ArrayList<>();
                HashMap<String, Integer> duplicates = new HashMap<>();
                for (String iter2 : graph3.get(iter)) {
                    if (duplicates.containsKey(iter2) == false) {
                        if (newname.containsKey(iter2) == true) {
                            nnn.add(newname.get(iter2));
                            duplicates.put(iter2, 1);
                        } else {
                            nnn.add(iter2);
                            duplicates.put(iter2, 1);
                        }
                    }
                }
                Newcities.add(newname.get(iter));
                String hhh = newname.get(iter);
                ArrayList<String> lll = graph3_new.get(hhh);
                if (lll == null)
                    graph3_new.put(newname.get(iter), nnn);
                else {
                    for (String ssss : lll) {
                        if (nnn.contains(ssss) == false)
                            nnn.add(ssss);
                    }
                    graph3_new.put(newname.get(iter), nnn);
                }
            } else {
                ArrayList<String> nnn = new ArrayList<>();
                HashMap<String, Integer> duplicates = new HashMap<>();
                for (String iter2 : graph3.get(iter)) {
                    if (duplicates.containsKey(iter2) == false) {
                        if (newname.containsKey(iter2) == true) {
                            nnn.add(newname.get(iter2));
                            duplicates.put(iter2, 1);
                        } else {
                            nnn.add(iter2);
                            duplicates.put(iter2, 1);
                        }
                    }
                }
                Newcities.add(iter);
                graph3_new.put(iter, nnn);
            }
        }
        int n = Newcities.size();
        ceilLog = (int) Math.ceil(Math.log(n) / Math.log(2));
        for (String name : graph3_new.keySet()) {
            ArrayList<String> lstt = new ArrayList<>();
            for (int i = 0; i < ceilLog; i++) lstt.add(null);
            ancestors.put(name, lstt);
        }
        HashMap<String, String> parennttt = new HashMap<>();
        dfs_new(Newcities.get(0), Newcities.get(0), ceilLog, graph3_new, parennttt, 1);
        for (String name : Newcities) {
            ArrayList<String> lsttt = ancestors.get(name);
            ArrayList<Integer> length = new ArrayList<>();
            // length.add(1);
            if (lsttt.get(0).equals(name)) {
                length.add(0);
            } else {
                length.add(1);
            }
            for (int i = 1; i < lsttt.size(); i++) {
                if (lsttt.get(i).equals(lsttt.get(i - 1)))
                    length.add(length.get(i - 1));
                else
                    length.add((int) Math.pow(2, i));
            }
            ancestors_numbers.put(name, length);
        }
        return;
    }
    private void dfs_new(String child, String paren, int ceillog, HashMap<String, ArrayList<String>> graph3_new, HashMap<String, String> parennttt, int height) {
        parennttt.put(child, paren);
        heightarray.put(child, height);
        t_in.put(child, time_new + 1);
        time_new++;
        ancestors.get(child).set(0, paren);
        for (int i = 1; i < ceillog; i++) {
            ancestors.get(child).set(i, ancestors.get(ancestors.get(child).get(i - 1)).get(i - 1));
        }
        for (String iter : graph3_new.get(child)) {
            if (iter.equals(paren) == false) {
                dfs_new(iter, child, ceillog, graph3_new, parennttt, height + 1);
            }
        }
        t_out.put(child, time_new + 1);
        time_new++;
    }
    private boolean is_ancestor(String u, String v) {
        return t_in.get(u) <= t_in.get(v) && t_out.get(u) >= t_out.get(v);
    }
    public int numImportantLines(String cityA, String cityB) {
        /*
         * Implement an efficient algorithm to compute the number of important
         * transmission lines between two cities. Calls to numImportantLines will be
         * made only after calling the preprocessImportantLines() method once.

         * Expected running time: O(logn), where n is the number of cities.
         */
        String cityAnew = newname.containsKey(cityA) == true ? newname.get(cityA) : cityA;
        String cityBnew = newname.containsKey(cityB) == true ? newname.get(cityB) : cityB;
        int ans = 0;
        if (cityAnew.equals(cityBnew))
            return 0;
        if (is_ancestor(cityAnew, cityBnew)) {
            ans = heightarray.get(cityBnew) - heightarray.get(cityAnew);
        } else if (is_ancestor(cityBnew, cityAnew)) {
            ans = heightarray.get(cityAnew) - heightarray.get(cityBnew);
        } else {
            String ccc = cityAnew;
            for (int i = ceilLog - 1; i >= 0; i--) {
                // System.out.println(i);
                if (is_ancestor(ancestors.get(ccc).get(i), cityBnew) == false) {
                    ccc = ancestors.get(ccc).get(i);
                }
            }
            ccc = ancestors.get(ccc).get(0);
            ans = heightarray.get(cityAnew) + heightarray.get(cityBnew) - 2 * heightarray.get(ccc);
        }
        return ans;
    }
    // public static void main(String[] args) {

    // }
}