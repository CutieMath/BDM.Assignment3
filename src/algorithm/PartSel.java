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
        int i = 0;
        int l = 0;
        int m = P.size();
        int[][] E = new int[m + 1][L + 1];
        int[][] I = new int[m + 1][L + 1];
        HashMap<String, ArrayList<Billboard>> E_s = new HashMap<>();
        HashMap<String, ArrayList<Billboard>> I_s = new HashMap<>();
        I[0][0] = 0;
        E[0][0] = 0;



        // from cluster 1 to m
        // from budget 1 to L
        for(i = 1; i < m; i ++){
            E[i][0] = 0;
            for(l = 1; l < L; l ++){
                // Invoke enumSel (Ci, l) to compute E[i][l]
                EnumSel enumSel = new EnumSel(l, P.get(i));
                enumSel.generateSolution();
                ArrayList<Billboard> E_boards = new ArrayList<>(enumSel.resultList);

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
                I[i][0] = 0;
                int maxLocalInf = 0;
                int q_result = 0;

                for(int q = 0; q <= l; q ++){
                    int localInf = I[i - 1][l - q] + E[i][q];
                    if(localInf > maxLocalInf){
                        q_result = q;
                        maxLocalInf = localInf;
                    }
                }
                I[i][l] = maxLocalInf;

                // Use the q_result that produces max influence
                // Construct hash table Is
                // Add in the boards that produces max local influence
                String IString = Integer.toString(i - 1) + Integer.toString(l - q_result);
                String EString = Integer.toString(i) + Integer.toString(q_result);
                ArrayList<Billboard> combination = new ArrayList<>();
                if(E_s.get(EString) != null && I_s.get(IString) != null) {
                    combination.addAll(E_s.get(EString));
                    combination.addAll(I_s.get(IString));
                }
                I_s.put(id, combination);
            }
        }

        String mL_Id = Integer.toString(m+1) + Integer.toString(L+1);
        resultList = I_s.get(mL_Id);

    }// end of generateSolution
}// end of partSel