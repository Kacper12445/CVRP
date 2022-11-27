package Loader;

import lombok.Data;
@Data
public class CVRPModel {
    private Integer[][] coords;

    private Integer[][] demands;
    private int datasetSize;
    private int capacity;

    public CVRPModel() {

    }
    public CVRPModel(int datasetSize) {
        this.datasetSize = datasetSize;
        this.coords = new Integer[datasetSize][3];
        this.demands = new Integer[datasetSize][2];
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public void setDatasetSize(int datasetSize) {
        this.datasetSize = datasetSize;
    }
    public void insertNewPoint(int index, Integer[] coords, Integer[] demand) {
        this.coords[index] = coords;
        this.demands[index] = demand;
    }
}
