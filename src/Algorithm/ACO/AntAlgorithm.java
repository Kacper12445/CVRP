package Algorithm.ACO;

import CSV.SaverCSV;
import Loader.CVRPModel;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.Arrays;
public class AntAlgorithm {
    private final double c = 1.0;             // Initial value of trails at the start of the simulation
    private final double alpha = 1;           // Pheromone importance
    private final double beta = 5;            // Distance priority (should be greater than alpha for best results)
    private final double evaporation = 0.5;   // Percent of how much the pheromone is evaporating in every iteration
    private final double Q = 500;              // Total amount of pheromone left on the trail by each Ant
    private final double antPerCity = 0.8;      // How many ant we will use per city
    private final int maxIterations = 1000;
    private final int numberOfCities;
    private final int numberOfAnts;
    private double[][] pheromoneMatrix;

    private final double[][] graph;
    private final double[] probabilities;
    private List<Ant> ants;
    private final Random random = new Random();
    private int currentIndex;
    private Integer[] bestTourOrder;
    private double bestTourLength;
    private double worstTourLength;
    private CVRPModel cvrpModel;
    SaverCSV saverCSV = null;
    double[] populationCost;

    public AntAlgorithm(CVRPModel cvrpModel) throws IOException {
        this.cvrpModel = cvrpModel;
        ants = new ArrayList<>();
        graph = cvrpModel.getAdjacencyMatrix();
        saverCSV = new SaverCSV("test.csv");

        numberOfCities = cvrpModel.getDatasetSize();
        pheromoneMatrix = new double[numberOfCities][numberOfCities];
        numberOfAnts = (int)(numberOfCities * antPerCity);
        probabilities = new double[numberOfCities];
        populationCost = new double[numberOfAnts];
//        IntStream.range(0, numberOfAnts).forEach(i-> {
        IntStream.range(0, numberOfAnts).forEach(i-> {
            ants.add(new Ant(numberOfCities));
        });
    }

    public void runACO() {
        IntStream.rangeClosed(1, 1)
                .forEach(i -> {
                    System.out.println("Attempt #" + i);
                    try {
                        solve();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void solve() throws IOException {
        setupAnts();
        setInitialPheromoneMatrix();

        for(int i = 0; i < maxIterations; i++){
            moveAnts();
            updatePheromoneMatrixValues();
            updateBestPath();
            saverCSV.saveEpochToCSV(i+1, populationCost);
            populationCost = new double[numberOfAnts];
        }
        saverCSV.closeFile();
        System.out.println("Best tour length: " + (bestTourLength));
        System.out.println("Worst tour length: " + (worstTourLength));
//        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));
    }

//    Dlaczego macierz
    public void setupAnts(){
        for (int i = 0; i < numberOfAnts; i++) {
            for (Ant ant : ants) {
                ant.resetVisitedCities();
                ant.visitCity(0, random.nextInt(numberOfCities));
            }
        }
        currentIndex = 0;
    }

    public void moveAnts(){
        for(int i = currentIndex; i < numberOfCities - 1; i++){
            ants.forEach(ant -> ant.visitCity(currentIndex + 1, selectNextCity(ant)));
            currentIndex++;
        }
    }

    private int selectNextCity(Ant ant) {
//        int t = random.nextInt(numberOfCities - currentIndex);
//        if (random.nextDouble() < randomFactor) {
//            OptionalInt cityIndex = IntStream.range(0, numberOfCities)
//                    .filter(i -> i == t && !ant.visited(i))
//                    .findFirst();
//            if (cityIndex.isPresent()) {
//                return cityIndex.getAsInt();
//            }
//        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
//        System.out.println("Probabilities: " + Arrays.toString(probabilities));
//        System.out.println("RANDOM: " + r);
//        double sum = 0.0;

//        Arrays.sort(probabilities);
//        for(int i = 0; i < probabilities.length; i++){
//            sum = sum + probabilities[i];
//            System.out.println("SUMA: " + sum);
//        }
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
//                System.out.println("WYBRANE i: " + i);
//                System.out.println("TOTAL WYBRANEGO: " + total);
//                System.out.println("WYBRANE prob: " + probabilities[i]);
                return i;
            }
        }
//        System.out.println("================================================");

        throw new RuntimeException("There are no other cities");
    }

    public void calculateProbabilities(Ant ant) {
        int currentCityNumber = ant.trail[currentIndex];
//        System.out.println("Current index: "+ currentIndex);
//        System.out.println("I: " + currentCityNumber);
//        System.out.println(Arrays.toString(ant.trail));
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
//                Mianownik wzoru na prawdopodobienstwo
                pheromone += Math.pow(pheromoneMatrix[currentCityNumber][l], alpha) * Math.pow(1.0 / graph[currentCityNumber][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
//                licznik wzoru na prawdopodobiestwo
                double numerator = Math.pow(pheromoneMatrix[currentCityNumber][j], alpha) * Math.pow(1.0 / graph[currentCityNumber][j], beta);
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
        for (Ant a : ants) {
            double contribution = Q / cvrpModel.evaluateScore(a.trail);
            for (int i = 0; i < numberOfCities - 1; i++) {
                pheromoneMatrix[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            pheromoneMatrix[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    private void updateBestPath() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = cvrpModel.evaluateScore(ants.get(0).trail);
        }
//        for (Ant a : ants) {
        for(int i = 0; i < numberOfAnts; i++){
            double currentScore = cvrpModel.evaluateScore(ants.get(i).trail);
            populationCost[i] = currentScore;
            if (currentScore < bestTourLength) {
                bestTourLength = currentScore;
                bestTourOrder = ants.get(i).trail.clone();
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
