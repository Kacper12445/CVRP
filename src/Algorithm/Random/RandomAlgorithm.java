package Algorithm.Random;

import Loader.CVRPModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class RandomAlgorithm {
    private int datasetSize;
    private CVRPModel model;
    private Integer[] path;
    private double cost;

    public RandomAlgorithm(CVRPModel model) {
        this.model = model;
        this.datasetSize = model.getDatasetSize();
        this.path = new Integer[datasetSize + 1];
        this.path[0] = 0;
    }

    public void searchSolution() {
        List<Integer> possibleNodes = new ArrayList<>();
        Random random = new Random();
        int randomIndex;
        for (int i = 1; i < this.datasetSize; i++) {
            possibleNodes.add(i);
        }
        for (int i = 1; i < this.datasetSize; i++) {
            randomIndex = random.nextInt(possibleNodes.size());
            this.path[i] = possibleNodes.get(randomIndex);
            possibleNodes.remove(randomIndex);
        }
        this.path[this.datasetSize] = 0;
        this.cost = this.model.evaluateScore(this.path);
    }
}
