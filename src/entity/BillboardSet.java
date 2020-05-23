package entity;

import java.util.ArrayList;

/**
 * This class contains one or more billboards
 */
public class BillboardSet {

    public String billboardId = "";
    public int inf = 0;
    public int cost = 0;
    public int boardsNum = 0;
    public ArrayList<String> billboardsInfoArray;

    public BillboardSet(){
        billboardsInfoArray = new ArrayList<>();
    }

    public void addBoard(Billboard billboard){
        billboardsInfoArray.add(billboard.getBillboardID() + "," + billboard.getInf() + "," + billboard.getPrice());
        inf += billboard.getInf();
        cost += billboard.getPrice();
        boardsNum ++;
    }

    public void addBoardSets(BillboardSet oneSet){
        billboardsInfoArray.addAll(oneSet.billboardsInfoArray);
        inf += oneSet.inf;
        cost += oneSet.cost;
        boardsNum += oneSet.boardsNum;
    }

}
