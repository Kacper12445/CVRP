package Algorithm.SimulatedAnnealing;

import Algorithm.Greedy.GreedyAlgorithm;
import CSV.SaverCSV;
import Loader.CVRPModel;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
public class SimulatedAnnealing {

    private int datasetSize;
    private CVRPModel model;

    private double cost;

    public SimulatedAnnealing(CVRPModel model) {
        this.model = model;
        this.datasetSize = model.getDatasetSize();
    }

    public Integer[] performAlgorithm() throws IOException {
        SaverCSV saverCSV = new SaverCSV("medium-t150.csv");
        int iteration = 0;
        int maxIteration = 100;
        int amountOfNeighbours = 100;
        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(0, this.getModel());
        greedyAlgorithm.searchForSolution();
//        Integer[] solution = randomlyInitialize();
        Integer[] solution = greedyAlgorithm.getPath();
        double bestScore = model.evaluateScore(solution);
        while (iteration < maxIteration){
            Integer[][] neighbours = generateNeighbours(amountOfNeighbours, solution);
            double temperature = 100.0;
            double alpha = 0.9;
            for (int i = 0; i < amountOfNeighbours; i++) {
                Integer[] adjacentPath = neighbours[i];
                double score = model.evaluateScore(adjacentPath);
                if(score < bestScore){
                    solution = adjacentPath;
                    bestScore = score;
                } else {
                    double probability = Math.exp((bestScore-score)/temperature);
                    if(probability > Math.random()){
                        solution = adjacentPath;
                        bestScore = score;
                    }
                }
                temperature *= alpha;
            }

            double[] scores = new double[neighbours.length];
            for (int i = 0; i < neighbours.length; i++) {
                scores[i] = model.evaluateScore(neighbours[i]);
            }
            iteration += 1;
            saverCSV.saveEpochToCSV(iteration, scores);
            System.out.println(bestScore);
        }
        saverCSV.writer.close();

        return solution;
    }

    public Integer[][] generateNeighbours(int amountOfNeighbours, Integer[] pattern) {
        List<Integer[]> listOfSwapCombinations = chooseSwapCombinations(amountOfNeighbours);
        Integer[][] neighbours = new Integer[amountOfNeighbours + 1][datasetSize];
        for (int i = 0; i < amountOfNeighbours; i++) {
            neighbours[i][0] = 0;
//            this.paths[i][datasetSize] = 0;
        }
        neighbours[0] = pattern;

        for (int i = 1; i < amountOfNeighbours + 1; i++) {
            neighbours[i] = copyPathAndSwap(neighbours[0], listOfSwapCombinations.get(i-1));
        }
        return neighbours;
    }


    private List<Integer[]> chooseSwapCombinations(int amountOfNeighbours) {
        Random generator = new Random();
        List<Integer[]> list = new ArrayList<>();
        while (list.size() < amountOfNeighbours) {
            Integer[] pair = generatePair(generator);
            Integer[] reversedPair = reversePair(pair);
            while (list.contains(pair) || list.contains(reversedPair)) {
                pair = generatePair(generator);
                reversedPair = reversePair(pair);
            }
            list.add(pair);
        }
        return list;
    }

    private Integer[] generatePair(Random generator) {
        Integer first = generator.nextInt(1, this.datasetSize);
        Integer second = generator.nextInt(1, this.datasetSize);
        while (first.equals(second)) {
            second = generator.nextInt(1, this.datasetSize);
        }
        return new Integer[]{first, second};
    }

    private Integer[] reversePair(Integer[] pair) {
        Integer[] reversedPair = new Integer[2];
        reversedPair[0] = pair[1];
        reversedPair[1] = pair[0];
        return reversedPair;
    }


    private Integer[] randomlyInitialize() {
        List<Integer> possibleNodes = new ArrayList<>();
        Random random = new Random();
        Integer[] path = new Integer[this.datasetSize + 1];
        path[0] = 0;
        int randomIndex;
        for (int i = 1; i < this.datasetSize; i++) {
            possibleNodes.add(i);
        }
        for (int i = 1; i < this.datasetSize; i++) {
            randomIndex = random.nextInt(possibleNodes.size());
            path[i] = possibleNodes.get(randomIndex);
            possibleNodes.remove(randomIndex);
        }
        return path;
    }

    private Integer[] copyPathAndSwap(Integer[] pattern, Integer[] combination) {
        Integer[] path = Arrays.copyOf(pattern, pattern.length);
        path[combination[0]] = pattern[combination[1]];
        path[combination[1]] = pattern[combination[0]];
        return path;
    }


}
