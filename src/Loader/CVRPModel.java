package Loader;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CVRPModel {
    private Integer[][] coords;

    private Integer[][] demands;
    private int datasetSize;
    private int capacity;
    private double[][] adjacencyMatrix;
    private double score;

    public CVRPModel(int datasetSize) {
        this.datasetSize = datasetSize;
        this.coords = new Integer[datasetSize][3];
        this.demands = new Integer[datasetSize][2];
    }

    public void insertNewPoint(int index, Integer[] coords, Integer[] demand) {
        this.coords[index] = coords;
        this.demands[index] = demand;
    }

    private double evaluateDistance(Integer[] point1, Integer[] point2){
        return Math.sqrt(Math.pow((point1[0] - point2[0]), 2)
                + Math.pow((point1[1] - point2[1]), 2));
    }

    public void createAdjacencyMatrix(){
        double[][] adjacencyMatrix = new double[this.datasetSize][this.datasetSize];
        for (int i = 0; i < this.datasetSize; i++) {
            for (int j = 0; j < this.datasetSize; j++) {
                adjacencyMatrix[i][j] = evaluateDistance(this.coords[i], this.coords[j]);
            }
        }
        this.setAdjacencyMatrix(adjacencyMatrix);
    }

    public double evaluateScore(Integer[] path){
        int currentLoad = 0; //first location is depot and after whole course truck returns to depot
        double score = 0.0; //distance to be counted

        for (int i = 0; i < this.datasetSize; i++) {
            if(i + 1 < this.datasetSize){
                int newLoad = this.demands[path[i+1]][1];
                if(currentLoad + newLoad > this.capacity){
                    score += adjacencyMatrix[path[i]][0] + adjacencyMatrix[path[i+1]][0];
                    currentLoad = newLoad;
                }else {
                    score += adjacencyMatrix[path[i]][path[i+1]];
                    currentLoad += newLoad;
                }
            }else {
                //last location -> return to depot
                score += adjacencyMatrix[path[i]][0];
            }
        }
        this.score = score;
        return score;
    }
}
