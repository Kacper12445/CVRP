import Algorithm.ACO.AntAlgorithm;
import Algorithm.Greedy.GreedyAlgorithm;
import Algorithm.Random.RandomAlgorithm;
import CSV.SaverCSV;
import Loader.Loader;

import java.io.IOException;
import java.util.Arrays;

import Loader.*;


public class Main {
    public static void main(String[] args) throws IOException {
        Loader loader = new Loader();
        CVRPModel model = loader.loadFileData("Medium - tai150d");
        GreedyAlgorithm greedy = new GreedyAlgorithm(0, model);
        greedy.searchForSolution();
//        SaverCSV saverCSV = new SaverCSV("randomTest.csv");
        double[] results = new double[10];

//        for (int j = 0; j < 10; j++) {
//            for (int i = 0; i < 10; i++) {
//                RandomAlgorithm random = new RandomAlgorithm(model);
//                random.searchSolution();
//                results[i] = random.getCost();
//            }
//            saverCSV.saveEpochToCSV(j, results);
//        }
        AntAlgorithm antColony = new AntAlgorithm(model);
        antColony.runACO();
        double best = Arrays.stream(antColony.bestScoresArray).average().getAsDouble();
        double worst = Arrays.stream(antColony.worstScoresArray).average().getAsDouble();
        System.out.println(Arrays.toString(antColony.bestScoresArray));
        System.out.println(Arrays.toString(antColony.worstScoresArray));
        System.out.println("BEST AVG: " + best);
        System.out.println("WORST AVG: " + worst);

//        saverCSV.closeFile();
        System.out.println("GREEDY COST: " + greedy.getCost());

//        submitLoader(model);


    }

    private static void submitLoader(CVRPModel model) {
        System.out.println("COORD DATA");
        for (Integer[] data: model.getCoords()) {
            System.out.printf("%d %d %d", data[0], data[1], data[2]);
            System.out.println(" ");
        }
        System.out.println(" ");
        System.out.println("DEMAND DATA");
        for (Integer[] data: model.getDemands()) {
            System.out.printf("%d %d", data[0], data[1]);
            System.out.println(" ");
        }
        System.out.println("CAPACITY " + model.getCapacity());
    }
}