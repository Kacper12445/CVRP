package Algorithm.ACO;

import CSV.SaverCSV;
import Loader.CVRPModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.Arrays;
public class AntAlgorithm {
    private final double c = 1;             // Initial value of trails at the start of the simulation
    private final double alpha = 1;
    private final double beta = 10;
    private final double evaporation = 0.5;   // Percent of how much the pheromone should left on trail
    private final double Q = 1;
    private final double antPerCity = 2;
    private final int maxIterations = 200;
    private final int numberOfCities;
    private final int numberOfAnts;
    private double[][] pheromoneMatrix;

    private final double[][] distanceGraph;
    private final double[] probabilities;
    private List<Ant> colony;
    private final Random random = new Random();
    private int currentIndex;
    private Integer[] bestTourOrder;
    private double bestTourLength;
    private double worstTourLength;
    private int runACONumber;
    private CVRPModel cvrpModel;
    SaverCSV saverCSV = null;
    double[] antsScores;
    public double[] bestScoresArray;
    public double[] worstScoresArray;

    public AntAlgorithm(CVRPModel cvrpModel, String fileSaveName, int runAlgorithmAmount) throws IOException {
        runACONumber = runAlgorithmAmount;
        bestScoresArray = new double[runACONumber];
        worstScoresArray = new double[runACONumber];
        this.cvrpModel = cvrpModel;

        colony = new ArrayList<>();
        distanceGraph = cvrpModel.getAdjacencyMatrix();
        saverCSV = new SaverCSV(fileSaveName + ".csv");

        numberOfCities = cvrpModel.getDatasetSize();
        pheromoneMatrix = new double[numberOfCities][numberOfCities];
        numberOfAnts = (int)(numberOfCities * antPerCity);
        probabilities = new double[numberOfCities];
        antsScores = new double[numberOfAnts];

        IntStream.range(0, numberOfAnts).forEach(i-> {
            colony.add(new Ant(numberOfCities));
        });
    }

    public void runACO() throws IOException {
        for(int i = 0; i < runACONumber; i++){
            searchForResult(i);
            bestScoresArray[i] = bestTourLength;
            worstScoresArray[i] = worstTourLength;
        }
    }

    public void searchForResult(int runAlgorithmNumber) throws IOException {
        setupAnts();
        setInitialPheromoneMatrix();

        for(int i = 0; i < maxIterations; i++){
            moveAnts();
            updatePheromoneMatrixValues();
            updateBestPath();
            if(runAlgorithmNumber < 2){
                saverCSV.saveEpochToCSV(i, antsScores);
            }
            Arrays.fill(antsScores, 0.0);
        }
//        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));
//        System.out.println("Worst tour length: " + (worstTourLength));
    }

    public void setupAnts(){
        List<Integer> selectedCities = new ArrayList<>();
            for (Ant ant : colony) {
                ant.resetVisitedCities();
                int randomCityNumber = random.nextInt(numberOfCities);
                if (selectedCities.contains(randomCityNumber) && selectedCities.size() < numberOfCities) {
                    while (selectedCities.contains(randomCityNumber)) {
                        randomCityNumber = random.nextInt(numberOfCities);
                    }
                }
                ant.visitCity(0, randomCityNumber);
                selectedCities.add(randomCityNumber);
            }
        currentIndex = 0;
    }

    public void moveAnts(){
        for(int i = currentIndex; i < numberOfCities - 1; i++){
            colony.forEach(ant -> ant.visitCity(currentIndex + 1, selectNextCity(ant)));
            currentIndex++;
        }
    }

    private int selectNextCity(Ant ant) {
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0.00;
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r && !ant.visited[i]) {
                return i;
            }
        }
        throw new RuntimeException("There are no other cities");
    }

    public void calculateProbabilities(Ant ant) {
        int currentCityNumber = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
                pheromone += Math.pow(pheromoneMatrix[currentCityNumber][l], alpha) * Math.pow(1.0 / distanceGraph[currentCityNumber][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(pheromoneMatrix[currentCityNumber][j], alpha) * Math.pow(1.0 / distanceGraph[currentCityNumber][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }
    private void vaporizePheromoneMatrix(){
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                this.pheromoneMatrix[i][j] *= evaporation;
            }
        }
    }

    private void updatePheromoneMatrixValues() {
        vaporizePheromoneMatrix();
        for (Ant a : colony) {
            double pheromoneValue = Q / cvrpModel.evaluateScore(a.trail);
            for (int i = 0; i < numberOfCities - 1; i++) {
                pheromoneMatrix[a.trail[i]][a.trail[i + 1]] += pheromoneValue;
            }
            pheromoneMatrix[a.trail[numberOfCities - 1]][a.trail[0]] += pheromoneValue;
        }
    }

    private void updateBestPath() throws IOException {
        double temp = 0.0;
        if (bestTourOrder == null) {
            bestTourOrder = colony.get(0).trail;
            bestTourLength = cvrpModel.evaluateScore(colony.get(0).trail);
            temp = bestTourLength;

        }
        for(int i =0; i < numberOfAnts; i++){
            double tripCost = cvrpModel.evaluateScore(colony.get(i).trail);
            antsScores[i] = tripCost;
            if (tripCost < temp) {
                temp = tripCost;
            }
        }

        for (Ant a : colony) {
            double currentScore = cvrpModel.evaluateScore(a.trail);
            if (currentScore < bestTourLength) {
                bestTourLength = currentScore;
                bestTourOrder = a.trail.clone();
            }
            if(currentScore > worstTourLength) {
                worstTourLength = currentScore;
            }
        }
    }

    private void setInitialPheromoneMatrix() {
        for(int i = 0 ; i < numberOfCities; i++){
            for(int j = 0; j <  numberOfCities; j++){
                pheromoneMatrix[i][j] = c;
            }
        }
    }

}
