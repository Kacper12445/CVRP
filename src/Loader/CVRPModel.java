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
        this.adjacencyMatrix = adjacencyMatrix;
    }
}
