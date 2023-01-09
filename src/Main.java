import Algorithm.Grasp.Grasp;
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
        CVRPModel model = loader.loadFileData("Difficult - X-n801-k40");

        Grasp grasp = new Grasp(model.getDatasetSize(), 300, 30, model, 0.001);
        grasp.runAlgorithm(300);
        System.out.println("GRASP:");
        System.out.println(Arrays.toString(grasp.getBestSolution()));
        System.out.println(model.evaluateScore(grasp.getBestSolution()));

        GreedyAlgorithm greedy = new GreedyAlgorithm(0, model);
        greedy.searchForSolution();
        System.out.println("\nGreedy:");
        System.out.println(Arrays.toString(greedy.getPath()));
        System.out.println(model.evaluateScore(greedy.getPath()));

        SaverCSV saverCSV = new SaverCSV("randomTest.csv");
        double[] results = new double[10];
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                RandomAlgorithm random = new RandomAlgorithm(model);
                random.searchSolution();
                results[i] = random.getCost();
            }
            saverCSV.saveEpochToCSV(j, results);
        }
        saverCSV.closeFile();
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