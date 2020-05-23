package algorithm;

import entity.Billboard;
import entity.BillboardSet;

import java.util.*;

public class GreedySel {

    public ArrayList<Billboard> resultList; // this variable is used to store the billboard set of the solution. Do not change or remove it!
    private ArrayList<Billboard> billboardList;
    private int budget; // the budget constraint

    public GreedySel(int budget, ArrayList<Billboard> billboardList) {
        this.budget = budget;
        this.billboardList = billboardList;
        this.resultList = new ArrayList<>();
    }

    public void generateSolution() {

        int resultOneInf = 0;
        List<Billboard> billboardListCopy = new ArrayList<>(billboardList);
        List<Billboard> billboardListCopy2 = new ArrayList<>(billboardList);


        ArrayList<Billboard> resultOne = stepOne(billboardListCopy);
        ArrayList<Billboard> resultTwo = stepTwo(billboardListCopy2);


        // Step Three
        // Compare which set has maximum influence
        for(int i = 0; i < resultOne.size(); i ++){
            resultOneInf += resultOne.get(i).getInf();
        }

        if(resultTwo.size() != 0) {
            if (resultOneInf > resultTwo.get(0).getPrice()) {
                resultList = resultOne;
            } else {
                resultList = resultTwo;
            }
        }else{
            resultList = resultOne;
        }
    }


    /**
     * Step One
     * Find the billboard (bi) that has maximum marginal influence gain from billboard list (U)
     * Add (bi) into result set for step one (H1)
     * Repeat until budget (L) is consumed
     **/
    public ArrayList<Billboard> stepOne(List<Billboard> billboardList){

        // Initiates
        // copy the budget
        int money = budget;
        ArrayList<Billboard> setOne = new ArrayList<>();
        Billboard maxInfBoard;

        // repeat this step until budget (L) is consumed or the billboards are run out
        while(money > 0 && billboardList.size() != 0) {
            // find the marginal max influence board then check it's cost
            maxInfBoard = Collections.max(billboardList, Comparator.comparing(i -> i.getMarginalInf()));
            if(maxInfBoard.getPrice() <= money) {
                // add the board to step one result
                setOne.add(maxInfBoard);
                // remove the selected board and reduce budget as the board has been selected
                billboardList.remove(maxInfBoard);
                money -= maxInfBoard.getPrice();
            }else{
                billboardList.remove(maxInfBoard);
            }
        }
        return setOne;
    }


    /**
     * Step Two
     * Find a single billboard (bi) that has the most influence under budget
     **/
    public ArrayList<Billboard> stepTwo(List<Billboard> billboardList) {

        // Initiates
        Boolean run = true;
        ArrayList<Billboard> setTwo = new ArrayList<>();

        // repeat finding the max influence board until termination condition met (found the max influence board under budget)
        while (run == true && billboardList.size() != 0) {
            Billboard maxInfBoard = Collections.max(billboardList, Comparator.comparing(i -> i.getInf()));
            if (maxInfBoard.getPrice() <= budget) {
                setTwo.add(maxInfBoard);
                run = false;
            }else{
                billboardList.remove(maxInfBoard);
            }
        }
        return setTwo;
    }
}
