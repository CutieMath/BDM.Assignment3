package algorithm;

import entity.Billboard;
import entity.BillboardSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartSel {

    private ArrayList<ArrayList<Billboard>> clusterList;
    public ArrayList<Billboard> resultList; // this variable is used to store the billboard set of the solution. Do not change or remove it!
    private int budget; // the budget constraint

    public PartSel(int budget, ArrayList<ArrayList<Billboard>> clusterList) {
        this.budget = budget;
        this.clusterList = clusterList;
        this.resultList = new ArrayList<>();
    }

    public void generateSolution() {

        // Initiates and copies
        List<ArrayList<Billboard>> P = new ArrayList<ArrayList<Billboard>>(clusterList);
        int L = budget;
        int m = P.size();
        int i = 0;
        int l = 0;
        // construct two 2D array, add +1 to include index 0
        int[][] E = new int[m+1][L+1];
        int[][] I = new int[m+1][L+1];
        HashMap<String, ArrayList<Billboard>> E_s = new HashMap<>();
        HashMap<String, ArrayList<Billboard>> I_s = new HashMap<>();
        // Assign the starting value equal to 0 and null
        I[0][0] = 0;
        E[0][0] = 0;
        String Ezero = "00";
        String Izero = "00";
        E_s.put(Ezero, null);
        I_s.put(Izero, null);



        // from cluster 1 to m
        // from budget 1 to L
        for(i = 1; i <= m; i ++){

            // Assign the starting value to 0 and null
            E[i][0] = 0;
            String Es = String.valueOf(i) + "0";
            E_s.put(Es, null);

            for(l = 1; l <= L; l ++){

                // Invoke enumSel (Ci, l) to compute E[i][l]
                // Use P.get(i-1) because arraylist starts with 0
                EnumSel enumSel = new EnumSel(l, P.get(i-1));
                enumSel.generateSolution();
                ArrayList<Billboard> E_boards = new ArrayList<>();
                E_boards.addAll(enumSel.resultList);

                // 1. Assign results to E_s table
                String id = Integer.toString(i) + Integer.toString(l);
                E_s.put(id, E_boards);

                // 2. Assign influence to its dimension in E table
                int E_influence = 0;
                for(int count = 0; count < E_boards.size(); count ++) {
                    E_influence += E_boards.get(count).getInf();
                }
                E[i][l] = E_influence;

                // find the best q that can produce max influence
                // between current cluster and all the previous clusters
                I[0][l] = 0;
                String Is = "0"+ String.valueOf(l);
                I_s.put(Is, null);

                int maxLocalInf = 0;
                int q_result = 0;

                for(int q = 0; q <= l; q ++){
                    int localInf = I[i - 1][l - q] + E[i][q];
                    if(localInf > maxLocalInf){
                        maxLocalInf = localInf;
                        q_result = q;
                    }
                }
                I[i][l] = maxLocalInf;

                // Use the q_result that produces max influence
                // Construct hash table Is
                // Add in the boards that produces max local influence
                String IString = Integer.toString(i - 1) + Integer.toString(l - q_result);
                String EString = Integer.toString(i) + Integer.toString(q_result);
                ArrayList<Billboard> combination = new ArrayList<>();
                if(I_s.get(IString) == null && E_s.get(EString) == null){
                    Billboard empty = new Billboard("",0,0);
                    combination.add(empty);
                } else if(I_s.get(IString) == null && E_s.get(EString) != null) {
                    combination.addAll(E_s.get(EString));
                }else if(I_s.get(IString) != null && E_s.get(EString) == null){
                    combination.addAll(I_s.get(IString));
                }else {
                    combination.addAll(E_s.get(EString));
                    combination.addAll(I_s.get(IString));
                }
                I_s.put(id, combination);
            }
        }
        String mL_Id = Integer.toString(m) + Integer.toString(L);
        resultList = I_s.get(mL_Id);

    }// end of generateSolution
}// end of partSel