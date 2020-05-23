package fileIO;

import entity.Billboard;
import java.util.ArrayList;

public class ResultOutput {

    private ArrayList<String> result;
    private String fileName;

    public ResultOutput() {
        result = new ArrayList<String>();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void record() {
        writeFile(fileName);
    }

    public void addResult(ArrayList<Billboard> billboardList) {
        int budget = 0;
        int influence = 0;

        for (Billboard billboard : billboardList) {
            budget += billboard.getPrice();
            influence += billboard.getInf();
        }
        result.add("budget:" + budget + "\r\n");
        result.add("influence:" + influence + "\r\n");
        for(int i = 0; i < billboardList.size(); i ++){
            result.add(billboardList.get(i).getBillboardID() + " | " +  String.valueOf(billboardList.get(i).getInf()) + " | " + String.valueOf(billboardList.get(i).getPrice() + "\n"));
        }

    }

    public void addRunningTime(long time) {
        result.add("Running time (ms):" + String.valueOf(time));
    }

    private void writeFile(String fineName) {
        MyFileWriter myFileWriter = new MyFileWriter("./src/result/" + fineName);
        for (String content : result) {
            myFileWriter.writeToFile(content);
        }
        myFileWriter.close();
    }
}
