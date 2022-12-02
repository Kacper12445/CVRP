package Algorithm.Greedy;

import Loader.CVRPModel;
import lombok.Data;

@Data
public class GreedyAlgorithm {
    private Integer[] path;
    private double cost;
    private double[][] costMatrix;

    private CVRPModel cvrpModel;

    public GreedyAlgorithm(int startPoint, CVRPModel cvrpModel) {
        this.path = new Integer[cvrpModel.getDatasetSize()];
        this.path[0] = startPoint;
        this.costMatrix = new double[cvrpModel.getDatasetSize()][cvrpModel.getDatasetSize()];
        this.cvrpModel = cvrpModel;
        this.cost = 0;
    }

    public void searchForSolution() {
        deepCopyCostMatrix(cvrpModel.getAdjacencyMatrix());
        double minCost = Double.MAX_VALUE;
        int minCostIndex = -1;
        double edgeCost;
        for (int i = 0; i < this.path.length - 1; i++) {
            for (int j = 0; j < this.path.length; j++) {
                edgeCost = this.costMatrix[this.path[i]][j];
                if (minCost > edgeCost && edgeCost != 0) {
                    minCost = edgeCost;
                    minCostIndex = j;
                }
            }
            for (int j = 0; j < this.costMatrix.length; j++) {
                this.costMatrix[j][this.path[i]] = 0;
            }
            this.path[i + 1] = minCostIndex;
            minCost = Double.MAX_VALUE;
            minCostIndex = -1;
        }
        this.cost = cvrpModel.evaluateScore(this.path);
    }

    private void deepCopyCostMatrix(double[][] costMatrix) {
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix.length; j++) {
                this.costMatrix[i][j] = costMatrix[i][j];
            }
        }
    }
}
