package generator;

import entity.Billboard;
import fileIO.MyFileWriter;

import java.util.ArrayList;

public class BillboardGenerator {
    private int billNum = 500;

    private ArrayList<Billboard> billboardList;

    public static void main(String[] arg) {
        BillboardGenerator billboardGenerator = new BillboardGenerator();
    }

    public BillboardGenerator() {
        billboardList = new ArrayList<>();
        createBill();
        output();
    }

    private void createBill() {
        for (int i = 0; i < billNum; i++) {
            int inf = (int) (i / 10 * (2 - Math.random())) + 1;
            int price = (int) Math.pow(inf, 0.6);
            Billboard billboard = new Billboard(String.valueOf(i), inf, price);
            billboardList.add(billboard);
        }
    }

    private void output() {
        String fileName = "./src/data/BillboardList.txt";
        MyFileWriter myFileWriter = new MyFileWriter(fileName);

        for (Billboard billboard : billboardList) {
            myFileWriter.writeToFile(billboard.getBillboardID() + "," + billboard.getInf() + "," + billboard.getPrice() + "\r\n");
        }

        myFileWriter.close();
    }

}
