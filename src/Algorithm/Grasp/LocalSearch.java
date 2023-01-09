package Algorithm.Grasp;

import Loader.CVRPModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

public class LocalSearch {

    double[][] costMatrix;
    int neighborsNumber;
    int searchSteps;
    private CVRPModel model;

    public LocalSearch(double[][] costMatrix, int neighborsNumber, int searchSteps, CVRPModel model) {
        this.costMatrix = costMatrix;
        this.neighborsNumber = neighborsNumber;
        this.searchSteps = searchSteps;
        this.model = model;
    }

    public Integer[] search(Integer[] currentSolution) {
        Integer[][] bestSolutions = new Integer[this.searchSteps][currentSolution.length];
        for (int i = 0; i < this.searchSteps; i++) {
            bestSolutions[i] = this.generateNeighborhood(currentSolution);
        }
        return Arrays
                .stream(bestSolutions)
                .min(Comparator.comparing(model::evaluateScore))
                .orElseThrow(NoSuchElementException::new);
    }

    private Integer[] generateNeighborhood(Integer[] currentPaths) {
        Integer[][] neighbors = swapGeneration(currentPaths);
        return Arrays
                .stream(neighbors)
                .min(Comparator.comparing(model::evaluateScore))
                .orElseThrow(NoSuchElementException::new);
    }

    private Integer[][] swapGeneration(Integer[] path) {
        Integer[][] neighborhood = new Integer[neighborsNumber][path.length];
        Random random = new Random();
        int firstIndex = 0;
        int secondIndex = 0;
        int secondIndexValue;
        for (int i = 0; i < neighborsNumber; i++) {
            firstIndex = random.nextInt(path.length - 1) + 1;
            do {
                secondIndex = random.nextInt(path.length - 1) + 1;
            } while (secondIndex == firstIndex);
            neighborhood[i] = copyArray(path);
            secondIndexValue = neighborhood[i][secondIndex];
            neighborhood[i][secondIndex] = neighborhood[i][firstIndex];
            neighborhood[i][firstIndex] = secondIndexValue;
        }
        return neighborhood;
    }

    private Integer[] copyArray(Integer[] arrayToCopy) {
        Integer[] newArray = new Integer[arrayToCopy.length];
        newArray[0] = 0;
        for (int i = 1; i < arrayToCopy.length; i++) {
            newArray[i] = arrayToCopy[i];
        }
        return newArray;
    }

}
