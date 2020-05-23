package generator;

import entity.Billboard;
import fileIO.MyFileReader;
import fileIO.MyFileWriter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class ClusterGenerator {
    private int clusterNum =30;

    private ArrayList<Billboard> billboardList;
    private ArrayList<ArrayList<Billboard>> clusterList;

    public static void main(String[] arg) {
        ClusterGenerator clusterGenerator = new ClusterGenerator();
    }

    private ClusterGenerator() {
        billboardList = getBillboards();
        clusterList = getCluster();
        writeFile();
    }

    private void writeFile() {
        MyFileWriter myFileWriter = new MyFileWriter("./src/data/Dataset3.txt");
        for (ArrayList<Billboard> billboards : clusterList) {
            String billboardList = "";
            for (Billboard billboard : billboards) {
                billboardList += billboard.getBillboardID() + ",";
            }
            billboardList += "\r\n";
            myFileWriter.writeToFile(billboardList);
        }

    }

    private ArrayList<ArrayList<Billboard>> getCluster() {
        Collections.shuffle(billboardList);
        ArrayList<ArrayList<Billboard>> clusterList = new ArrayList<>();
        for (int i = 0; i < clusterNum; i++) {
            clusterList.add(new ArrayList<>());
        }
        int index = 0;
        while (index < billboardList.size()) {
            for (int i = 0; i < clusterNum; i++) {
                if (index >= billboardList.size())
                    break;
                clusterList.get(i).add(billboardList.get(index++));
            }
        }
        return clusterList;
    }

    private ArrayList<Billboard> getBillboards() {
        String fileName = "./src/data/BillboardList.txt";
        MyFileReader myFileReader = new MyFileReader(fileName);
        String line = myFileReader.getNextLine();
        ArrayList<Billboard> billboardList = new ArrayList<>();
        while (line != null) {
            String[] content = line.split(",");
            Billboard billboard = new Billboard(content[0], Integer.valueOf(content[1]), Integer.valueOf(content[2]));
            billboardList.add(billboard);
            line = myFileReader.getNextLine();
        }
        return billboardList;
    }
}
