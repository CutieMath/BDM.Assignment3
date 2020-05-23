package fileIO;

import entity.Billboard;

import java.util.ArrayList;
import java.util.HashMap;

public class ClusterInput {

    private HashMap<String, Billboard> billboardTree;
    private ArrayList<Billboard> billboardList;
    private int budget;

    public ClusterInput() {
        billboardTree = new HashMap<String, Billboard>();
        billboardList = new ArrayList<Billboard>();
        generateBillboards();
    }

    public ArrayList<ArrayList<Billboard>> generateCluster(String fileName) {
        int index = 0;
        fileName = "./src/data/" + fileName;
        MyFileReader myFileReader = new MyFileReader(fileName);
        ArrayList<ArrayList<Billboard>> clusterList = new ArrayList<>();
        String line = myFileReader.getNextLine();
        if (line != null)
            this.budget = Integer.valueOf(line);
        line = myFileReader.getNextLine();
        while (line != null) {
            String[] content = line.split(",");
            clusterList.add(new ArrayList<Billboard>());
            for (String billboardID : content) {
                if (billboardID != null) {
                    Billboard billboard = billboardTree.get(billboardID);
                    clusterList.get(index).add(billboard);
                }
            }
            index++;
            line = myFileReader.getNextLine();
        }
        return clusterList;
    }

    private void generateBillboards() {
        String fileName = FilePath.BILLBOARD_FILE_PATH;
        MyFileReader myFileReader = new MyFileReader(fileName);
        String line = myFileReader.getNextLine();
        while (line != null) {
            String[] content = line.split(",");
            Billboard billboard = new Billboard(content[0], Integer.parseInt(content[1]), Integer.parseInt(content[2]));
            billboardList.add(billboard);
            billboardTree.put(billboard.getBillboardID(), billboard);
            line = myFileReader.getNextLine();
        }
    }

    public ArrayList<Billboard> getBillboardList() {
        return this.billboardList;
    }

    public int getBudget() {
        return this.budget;
    }
}
