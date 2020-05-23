import algorithm.EnumSel;
import algorithm.GreedySel;
import algorithm.PartSel;
import entity.Billboard;
import fileIO.ClusterInput;
import fileIO.MyFileReader;
import fileIO.ResultOutput;

import java.util.ArrayList;

public class Main {

    public static void main(String[] arg) {
        int budget;
        ArrayList<ArrayList<String>> lists = getFilePath();
        ArrayList<ArrayList<Billboard>> clusterList;
        ClusterInput clusterInput = new ClusterInput();

        //GreedySel
        for (String filePath : lists.get(0)) {
            clusterList = clusterInput.generateCluster(filePath);
            budget = clusterInput.getBudget();
            GreedySel greedySel = new GreedySel(budget, clusterList.get(0));
            long startTime = System.currentTimeMillis();
            greedySel.generateSolution();
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            ArrayList<Billboard> resultSet = greedySel.resultList;
            recordResult(filePath, time, resultSet);
        }

        //EnumSel
        for (String filePath : lists.get(1)) {
            clusterList = clusterInput.generateCluster(filePath);
            budget = clusterInput.getBudget();
            EnumSel enumSel = new EnumSel(budget, clusterList.get(0));
            long startTime = System.currentTimeMillis();
            enumSel.generateSolution();
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            ArrayList<Billboard> resultSet = enumSel.resultList;
            recordResult(filePath, time, resultSet);
        }

        //PartSel
        for (String filePath : lists.get(2)) {
            clusterList = clusterInput.generateCluster(filePath);
            budget = clusterInput.getBudget();
            PartSel partSel = new PartSel(budget, clusterList);
            long startTime = System.currentTimeMillis();
            partSel.generateSolution();
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            ArrayList<Billboard> resultSet = partSel.resultList;
            recordResult(filePath, time, resultSet);
        }
    }

    private static ArrayList<ArrayList<String>> getFilePath() {
        String fileName = "./src/data/FileList.txt";
        MyFileReader myFileReader = new MyFileReader(fileName);
        String line = myFileReader.getNextLine();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        String algName = new String();
        while (line != null) {
            String[] text = line.split("/");
            if (!algName.equals("") && !algName.equals(text[0])) {
                lists.add(list);
                list = new ArrayList<>();
            }
            algName = text[0];
            list.add(line);
            line = myFileReader.getNextLine();
        }
        lists.add(list);
        return lists;
    }

    /**
     * This function is used to record the final result, which is used to evaluate your score.
     * Do not CHANGE or REMOVE this function!
     */
    private static void recordResult(String fileName, long time, ArrayList<Billboard> resultList) {
        ResultOutput resultOutput = new ResultOutput();
        resultOutput.setFileName(fileName);
        resultOutput.addResult(resultList);
        resultOutput.addRunningTime(time);
        resultOutput.record();
    }
}
