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
        String[] fileNames = { "Difficult - X-n599-k92", "Difficult - X-n801-k40", "V_Difficult - Leuven1-3000N, 25Q"};
        for(String fileName : fileNames){
            Loader loader = new Loader();
            CVRPModel model = loader.loadFileData(fileName);
//            GreedyAlgorithm greedy = new GreedyAlgorithm(0, model);
//            greedy.searchForSolution();
            int runAlgTimes;
            if(!fileName.equals("V_Difficult - Leuven1-3000N, 25Q")){
                runAlgTimes = 10;
            }else{
                runAlgTimes = 4;
            }
            System.out.println("CURRENT FILE RUN:  " + fileName);
            AntAlgorithm antColony = new AntAlgorithm(model, fileName +" -- ResultFile", runAlgTimes);
            antColony.runACO();
            double best = Arrays.stream(antColony.bestScoresArray).average().getAsDouble();
            double worst = Arrays.stream(antColony.worstScoresArray).average().getAsDouble();
            System.out.println(Arrays.toString(antColony.bestScoresArray));
            System.out.println(Arrays.toString(antColony.worstScoresArray));
            System.out.println("BEST AVG: " + best);
            System.out.println("WORST AVG: " + worst);

//        saverCSV.closeFile();
//            System.out.println("GREEDY COST: " + greedy.getCost());

//        submitLoader(model);
        }



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