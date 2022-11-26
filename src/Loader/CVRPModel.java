package Loader;

public class CVRPModel {
    private int[][] coords;
    private int[][] demands;
    private int datasetSize;
    private int capacity;

    public CVRPModel() {

    }
    public CVRPModel(int datasetSize) {
        this.datasetSize = datasetSize;
        this.coords = new int[datasetSize][3];
        this.demands = new int[datasetSize][2];
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public void setDatasetSize(int datasetSize) {
        this.datasetSize = datasetSize;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void insertNewPoint(int index, int[] coords, int[] demand) {
        this.coords[index] = coords;
        this.demands[index] = demand;
    }

    public int[][] getCoords() {
        return coords;
    }

    public void setCoords(int[][] coords) {
        this.coords = coords;
    }

    public int[][] getDemands() {
        return demands;
    }

    public void setDemands(int[][] demands) {
        this.demands = demands;
    }
}
