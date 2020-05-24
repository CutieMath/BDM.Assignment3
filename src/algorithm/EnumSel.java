package algorithm;

import entity.Billboard;
import entity.BillboardCombination;
import entity.BillboardSet;

import java.util.*;

public class EnumSel {

    public ArrayList<Billboard> resultList; // this variable is used to store the billboard set of the solution. Do not change or remove it!
    private ArrayList<Billboard> billboardList;
    private int budget; // the budget constraint

    public EnumSel(int budget, ArrayList<Billboard> billboardList) {
        this.budget = budget;
        this.billboardList = billboardList;
        this.resultList = new ArrayList<>();
    }

    public void generateSolution() {

        // Get the results from methods
        // Initiates container
        List<Billboard> billboardListCopy1 = new ArrayList<>(billboardList);
        List<Billboard> billboardListCopy2 = new ArrayList<>(billboardList);
        BillboardSet resultOne = phaseOne(billboardListCopy1);
        BillboardSet resultTwo = phaseTwo(billboardListCopy2);

        // Get results from step one H1
        ArrayList<Billboard> resultOneList = new ArrayList<>();
        // Get results from step two H2
        ArrayList<Billboard> resultTwoList = new ArrayList<>();

        // Phase 3
        // convert result sets into billboards ArrayList
        for (int i = 0; i < resultOne.billboardsInfoArray.size(); i++) {
            String content[] = resultOne.billboardsInfoArray.get(i).split(",");
            String billboardID = content[0];
            int billboardInf = Integer.parseInt(content[1]);
            int billboardPrice = Integer.parseInt(content[2]);
            Billboard billboard = new Billboard(billboardID, billboardInf, billboardPrice);
            resultOneList.add(billboard);
        }

        // if second resultset is not empty
        if(resultTwo.inf != 0) {
            for (int i = 0; i < resultTwo.billboardsInfoArray.size(); i++) {
                String content[] = resultTwo.billboardsInfoArray.get(i).split(",");
                String billboardID = content[0];
                int billboardInf = Integer.parseInt(content[1]);
                int billboardPrice = Integer.parseInt(content[2]);
                Billboard billboard = new Billboard(billboardID, billboardInf, billboardPrice);
                resultTwoList.add(billboard);
            }
        }


        // Compare H1 & H2
        // Return the sets with the highest influence under budget
        if(resultTwo.inf != 0) {
            if (resultOne.inf > resultTwo.inf) {
                resultList = resultOneList;
            } else {
                resultList = resultTwoList;
            }
        }else{
            resultList = resultOneList;
        }

    }

    /**
     * Phase One
     * Enumerate the billboards and get the set with the highest influence under budget
     * One set can have no greater than 2 elements
     **/
    public BillboardSet phaseOne(List<Billboard> billboardList) {

        // Initiates
        BillboardSet billboardSet = new BillboardSet();

        // Max influence for one billboard per set
        for (int i = 0; i < billboardList.size(); i++) {
            // If the board has larger influence under budget, assign to result
            if ((billboardSet == null || billboardList.get(i).getInf() > billboardSet.inf)
                    && billboardList.get(i).getPrice() <= budget) {
                BillboardSet selectedBoard = new BillboardSet();
                selectedBoard.addBoard(billboardList.get(i));
                billboardSet = selectedBoard;
            }
        }

        // Max influence for two billboards per set
        for (int i = 0; i < billboardList.size(); i++) {
            for (int j = i + 1; j < billboardList.size(); j++) {
                // If two billboards have bigger influence under budget, assign them to result instead
                if (billboardList.get(i).getInf() + billboardList.get(j).getInf() > billboardSet.inf
                        && billboardList.get(i).getPrice() + billboardList.get(j).getPrice() <= budget) {
                    BillboardSet maxInfBoards = new BillboardSet();
                    maxInfBoards.addBoard(billboardList.get(i));
                    maxInfBoards.addBoard(billboardList.get(j));
                    billboardSet = maxInfBoards;
                }
            }
        }
        // Now the result should have max influence under budget set H1
        return billboardSet;
    }


    /**
     * Phase two
     * Partition the original billboards with each set has 3 billboards
     * Then greedily insert the next max influence board for each set until budget is consumed
     **/
    public BillboardSet phaseTwo(List<Billboard> billboardList) {

        ArrayList<BillboardSet> resultArray = new ArrayList<>();
        BillboardSet phaseTwoResult = new BillboardSet();

        // Step One:
        // Select tau + 1 boards
        // Add each combination into an array
        ArrayList<BillboardCombination> combinationsArrayList = new ArrayList<>();

        for (int i = 0; i < billboardList.size(); i++) {
            for (int j = i + 1; j < billboardList.size(); j++) {
                for (int c = j + 1; c < billboardList.size(); c++) {
                    if (billboardList.get(i).getPrice()
                            + billboardList.get(j).getPrice()
                            + billboardList.get(c).getPrice() <= budget) {
                        BillboardCombination combination = new BillboardCombination();
                        combination.board1 = i;
                        combination.board2 = j;
                        combination.board3 = c;
                        combination.combinationMoney = billboardList.get(i).getPrice()
                                + billboardList.get(j).getPrice()
                                + billboardList.get(c).getPrice();
                        combination.inf = billboardList.get(i).getInf()
                                + billboardList.get(j).getInf()
                                + billboardList.get(c).getInf();
                        // Add into an arrayList
                        combinationsArrayList.add(combination);
                    }
                }
            }
        }

        // Step two
        // Greedily insert more billboards into the result from step one
        for (int i = 0; i < combinationsArrayList.size(); i++) {
            BillboardCombination oneCombination = combinationsArrayList.get(i);
            if (oneCombination.combinationMoney < budget) {
                // Call helper method to insert more billboards greedily
                BillboardSet greedyResult = new BillboardSet();
                greedyResult = insertMore(oneCombination);
                // add result into arraylist
                resultArray.add(greedyResult);
            }
        }

        // get the set with highest influence
        if (resultArray.size() != 0) {
            phaseTwoResult = Collections.max(resultArray, Comparator.comparing(b -> b.inf));
        }
        return phaseTwoResult;
    }


    /**
     * Helper method for phase two
     * insert the next max inf billboard into the combination sets
     * use greedySel
     **/
    public BillboardSet insertMore(BillboardCombination combinations) {

        // Initiates
        int totalMoney = budget;
        ArrayList<Billboard> billboardListCopy = (ArrayList<Billboard>) billboardList.clone();
        BillboardSet resultSet = new BillboardSet();

        // Assign three boards from the combination
        Billboard board1 = billboardListCopy.get(combinations.board1);
        Billboard board2 = billboardListCopy.get(combinations.board2);
        Billboard board3 = billboardListCopy.get(combinations.board3);

        // Add them to the result set
        resultSet.addBoard(board1);
        resultSet.addBoard(board2);
        resultSet.addBoard(board3);

        // Remove them from total billboardList
        billboardListCopy.remove(board1);
        billboardListCopy.remove(board2);
        billboardListCopy.remove(board3);

        // Reduce budget as the boards are already selected
        totalMoney -= combinations.combinationMoney;

        // Use greedySel to insert more
        ArrayList<Billboard> greedyResult = new ArrayList<>();
        GreedySel greedy = new GreedySel(totalMoney, billboardListCopy);
        greedy.generateSolution();
        greedyResult.addAll(greedy.resultList);

        if (greedyResult.size() == 0) {
            // if greedyResult returns null, return the same billboard set
            return resultSet;
        } else {
            // else add the greedy result into billboard set and return
            for (int i = 0; i < greedyResult.size(); i++) {
                resultSet.addBoard(greedyResult.get(i));
            }
            return resultSet;
        }

    }// end of InsertMore

}// end of EnumSel