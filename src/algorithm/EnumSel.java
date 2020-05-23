package algorithm;

import entity.Billboard;
import entity.BillboardCombination;
import entity.BillboardSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        for(int i = 0; i < resultOne.billboardsInfoArray.size(); i ++){
            String content[] = resultOne.billboardsInfoArray.get(i).split(",");
            String billboardID = content[0];
            int billboardInf = Integer.parseInt(content[1]);
            int billboardPrice = Integer.parseInt(content[2]);
            Billboard billboard = new Billboard(billboardID, billboardInf, billboardPrice);
            resultOneList.add(billboard);
        }

        for(int i = 0; i < resultTwo.billboardsInfoArray.size(); i ++){
            String content[] = resultTwo.billboardsInfoArray.get(i).split(",");
            String billboardID = content[0];
            int billboardInf = Integer.parseInt(content[1]);
            int billboardPrice = Integer.parseInt(content[2]);
            Billboard billboard = new Billboard(billboardID, billboardInf, billboardPrice);
            resultTwoList.add(billboard);
        }

        // Compare H1 & H2
        // Return the sets with the highest influence under budget
        if(resultOne.inf > resultTwo.inf){
            resultList = resultOneList;
        }else{
            resultList = resultTwoList;
        }
    }

    /**
     * Phase One
     * Enumerate the billboards and get the set with the highest influence under budget
     * One set can have no greater than 2 elements
     **/
    public BillboardSet phaseOne(List<Billboard> billboardList){

        // Initiates
        BillboardSet billboardSet = new BillboardSet();

        // Max influence for one billboard per set
        for(int i = 0; i < billboardList.size(); i ++){
            if((billboardSet == null || billboardList.get(i).getInf() > billboardSet.inf)
                    && billboardList.get(i).getPrice() <= budget){
                BillboardSet selectedBoard = new BillboardSet();
                selectedBoard.addBoard(billboardList.get(i));
                // If the board has bigger influence under budget, assign to result
                billboardSet = selectedBoard;
            }
        }

        // Max influence for two billboards per set
        for(int i = 0; i < billboardList.size(); i ++){
            for(int j = i + 1; j < billboardList.size(); j ++){
                if(billboardList.get(i).getInf() + billboardList.get(j).getInf() > billboardSet.inf
                        && billboardList.get(i).getPrice() + billboardList.get(j).getPrice() <= budget){
                    BillboardSet maxInfBoards = new BillboardSet();
                    maxInfBoards.addBoard(billboardList.get(i));
                    maxInfBoards.addBoard(billboardList.get(j));
                    // If two billboards have bigger influence under budget, assign them to result instead
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
    public BillboardSet phaseTwo(List<Billboard> billboardList){

        // Initiates
        int totalMoney = budget;
        List<Billboard> billboardsListCopy = new ArrayList<>(billboardList);

        BillboardSet greedilyAddedResult = new BillboardSet();
        BillboardCombination maxInfComb1 = new BillboardCombination();
        BillboardSet stepOneResult = new BillboardSet();
        BillboardSet stepTwoResult = new BillboardSet();
        // Array to contain step two results in array format
        ArrayList<BillboardSet> stepTwoSetsArray = new ArrayList<>();
        ArrayList<BillboardCombination> combinationsList = new ArrayList<>();


        // Step One
        // Get all combination of three elements within budget
        // Create an object for each combination
        // Create an arraylist for all combinations
        for(int i = 0; i < billboardsListCopy.size(); i ++){
            for(int j = i + 1; j < billboardsListCopy.size(); j ++){
                for(int c = j + 1; c < billboardsListCopy.size(); c ++){
                    // if under budget, add to the combination && combination list
                    if(billboardsListCopy.get(i).getPrice()
                            + billboardsListCopy.get(j).getPrice()
                            + billboardsListCopy.get(c).getPrice() <= budget){
                        BillboardCombination combination = new BillboardCombination();
                        combination.board1 = i;
                        combination.board2 = j;
                        combination.board3 = c;
                        combination.combinationMoney = billboardsListCopy.get(i).getPrice()
                                + billboardsListCopy.get(j).getPrice()
                                + billboardsListCopy.get(c).getPrice();
                        combination.inf = billboardsListCopy.get(i).getInf()
                                + billboardsListCopy.get(j).getInf()
                                + billboardsListCopy.get(c).getInf();
                        // Add into an array list
                        combinationsList.add(combination);
                    }
                }
            }
        }

        // If the three combination under budget exists, assign to step two result
        if(combinationsList.size() != 0) {

            // Get the combination with the highest influence
            maxInfComb1 = Collections.max(combinationsList, Comparator.comparing(b -> b.inf));

            // Convert the max inf combination into billboard sets
            stepOneResult.addBoard(billboardsListCopy.get(maxInfComb1.board1));
            stepOneResult.addBoard(billboardsListCopy.get(maxInfComb1.board2));
            stepOneResult.addBoard(billboardsListCopy.get(maxInfComb1.board3));

            // Step Two
            // Greedily insert the next max influence board into each set until budget is consumed
            for (int i = 0; i < combinationsList.size(); i++) {
                BillboardCombination oneCombination = combinationsList.get(i);
                if (totalMoney - oneCombination.combinationMoney > 0) {
                    // Call helper method to insert more billboards (as a set) greedily
                    greedilyAddedResult = insertMore(oneCombination);
                    stepTwoSetsArray.add(greedilyAddedResult);
                    break;
                }
            }
            // Get the combination with the highest influence
            if(stepTwoSetsArray.size() != 0){
                stepTwoResult = Collections.max(stepTwoSetsArray, Comparator.comparing(b -> b.inf));
            }else{
                stepTwoResult = null;
            }
        } else{
            stepTwoResult = null;
        }


        // Step Three
        // Compare results from the two steps
        if( stepTwoResult == null || (stepOneResult.inf > stepTwoResult.inf) ){
            return stepOneResult;
        }else{
            return stepTwoResult;
        }
    }


    /**
     * Helper method for phase two, step two
     * insert the next max inf billboard into the combination sets
     * using greedySel
     **/
    public BillboardSet insertMore(BillboardCombination combinations){

        // Initiates
        int totalMoney = budget;
        List<Billboard> billboardListCopy = new ArrayList<Billboard>(billboardList);
        ArrayList<Billboard> billboardArrayList = (ArrayList) billboardListCopy;

        BillboardSet billboardSetBase = new BillboardSet();
        BillboardSet billboardSetResult = new BillboardSet();

        // Assign three boards from the combination lists
        Billboard board1 = billboardArrayList.get(combinations.board1);
        Billboard board2 = billboardArrayList.get(combinations.board2);
        Billboard board3 = billboardArrayList.get(combinations.board3);

        // Add the three boards to billboard set base
        billboardSetBase.addBoard(board1);
        billboardSetBase.addBoard(board2);
        billboardSetBase.addBoard(board3);

        // Remove the three boards from the total billboards list
        billboardArrayList.remove(board1);
        billboardArrayList.remove(board2);
        billboardArrayList.remove(board3);

        // Reduce budget as three boards are already selected
        totalMoney -= combinations.combinationMoney;

        // Use greedySel to insert the next marginal max inf boards until budget run out
        ArrayList<Billboard> insertSetResult = new ArrayList<>();
        GreedySel greedy = new GreedySel(totalMoney, billboardArrayList);
        greedy.generateSolution();
        insertSetResult = greedy.resultList;

        // Convert greedy results into billboard set
        for(int i = 0; i < insertSetResult.size(); i ++){
            billboardSetResult.addBoard(insertSetResult.get(i));
        }

        // Add the greedy results (set format) into base set (set format)
        billboardSetBase.addBoardSets(billboardSetResult);

        return billboardSetBase;
    }
}
