package Algorithm.Grasp;

import Loader.CVRPModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
public class Grasp {

    private double[][] costMatrix;
    private float[] volumeMatrix;
    private LocalSearch localSearch;
    private CVRPModel model;
    private double alpha;

    private Integer[] bestSolution;

    public Grasp(int nodesCount, int neighborsNumber, int searchSteps, CVRPModel model, double alpha) {
        this.costMatrix = model.getAdjacencyMatrix();
        this.localSearch = new LocalSearch(this.costMatrix, neighborsNumber, searchSteps, model);
        this.bestSolution = new Integer[nodesCount];
        this.model = model;
        this.alpha = alpha;
    }

    public void runAlgorithm(int maxIteration) {
        Integer[] currentSolution;
        Integer[] localFoundSolution;
        for (int i = 0; i < maxIteration; i++) {
            currentSolution = ConstructGreedyRandomizedSolution();
            localFoundSolution = this.localSearch.search(currentSolution);
            if (model.evaluateScore(localFoundSolution) < model.evaluateScore(currentSolution)) {
                currentSolution = copyArray(localFoundSolution);
            }
            if (this.bestSolution[0] == null) {
                this.bestSolution = copyArray(currentSolution);
            } else {
                if (model.evaluateScore(currentSolution) < model.evaluateScore(this.bestSolution)) {
                    this.bestSolution = copyArray(currentSolution);
                }
            }
        }
    }

    private Integer[] ConstructGreedyRandomizedSolution() {
        int startNode = 0;
        int i = 1;
        int selectedCandidate;
        List<Integer> rcl;
        int[] candidatesList = this.initializeSetOfCandidateNodes();
        double[] incrementalCostList = evaluateIncrementalCost(candidatesList, startNode);
        Integer[] solution = new Integer[model.getDatasetSize()];
        solution[0] = 0;

        while (candidatesList.length > 1) {
            rcl = buildRCL(candidatesList, incrementalCostList, alpha);
            selectedCandidate = rcl.get(new Random().nextInt(rcl.size()));
            solution[i] = selectedCandidate;
            candidatesList = updateCandidatesList(candidatesList, selectedCandidate);
            incrementalCostList = evaluateIncrementalCost(candidatesList, selectedCandidate);
            i++;
        }
        solution[solution.length - 1] = candidatesList[0];

        return solution;
    }

    private int[] updateCandidatesList(int[] candidatesList, int candidateToRemove) {
        int[] updatedCandidatesList = new int[candidatesList.length - 1];
        int indexToRemovePassed = 0;
        for (int i = 0; i < candidatesList.length; i++) {
            if (candidatesList[i] != candidateToRemove && i <= candidatesList.length - 1) {
                updatedCandidatesList[i - indexToRemovePassed] = candidatesList[i];
            } else {
                indexToRemovePassed = 1;
            }
        }
        return updatedCandidatesList;
    }

    private int[] initializeSetOfCandidateNodes() {
        int[] candidateNodesSet = new int[this.costMatrix.length - 1];
        for (int i = 1; i < this.costMatrix.length; i++) {
            candidateNodesSet[i-1] = i;
        }
        return candidateNodesSet;
    }

    private double[] evaluateIncrementalCost(int[] candidatesList, int currentNode) {
        double[] incrementalCostList = new double[candidatesList.length];
        for (int i = 0; i < candidatesList.length; i++) {
            incrementalCostList[i] = this.costMatrix[currentNode][candidatesList[i]];
        }
        return incrementalCostList;
    }
    private List<Integer> buildRCL(int[] candidatesList, double[] incrementalCostList, double alpha) {
        double minCost = Arrays.stream(incrementalCostList).min().getAsDouble();
        double maxCost = Arrays.stream(incrementalCostList).max().getAsDouble();
        List<Integer> rcl = new ArrayList<>();
        for (int i = 0; i < candidatesList.length; i++) {
            if (incrementalCostList[i] <= minCost + alpha * (maxCost - minCost)) {
                rcl.add(candidatesList[i]);
            }
        }
        return rcl;
    }

    private Integer[] copyArray(Integer[] arrayToCopy) {
        Integer[] newArray = new Integer[arrayToCopy.length];
        for (int i = 0; i < arrayToCopy.length; i++) {
            newArray[i] = arrayToCopy[i];
        }
        return newArray;
    }

}
