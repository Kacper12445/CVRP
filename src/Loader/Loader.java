package Loader;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class Loader {
    private final String nordCoordSection = "NODE_COORD_SECTION";
    private final String demandSection = "DEMAND_SECTION";
    private final String depotSection = "DEPOT_SECTION";
    private final String endOfFile = "EOF";
    private final String capacity = "CAPACITY";
    private final String dimension = "DIMENSION";

    public CVRPModel loadFileData(String fileName) throws IOException {
        String filePath  = new File("./DataSets/"+ fileName).getCanonicalPath();
        File fileToRead = new File(filePath + ".vrp");
        CVRPModel model = new CVRPModel();

        Hashtable<String, Integer[][]> section_dict = new Hashtable<>();

        Scanner fileScanner = new Scanner(fileToRead);
        while(fileScanner.hasNextLine()){
            String line = fileScanner.nextLine().trim();
            if (line.split(" ")[0].equals(this.dimension)) {
                String[] splitLine = line.split("\\s+");
                int datasetLength = Integer.parseInt(splitLine[splitLine.length - 1]);
                model = new CVRPModel(datasetLength);
                section_dict.put(this.nordCoordSection, model.getCoords());
                section_dict.put(this.demandSection, model.getDemands());
            }
            if (line.split(" ")[0].equals(this.capacity)) {
                String[] splitLine = line.split("\\s+");
                model.setCapacity(Integer.parseInt(splitLine[splitLine.length - 1]));
            }
            String currentSection = line.equals(this.nordCoordSection) ? this.nordCoordSection : line.equals(this.demandSection) ? this.demandSection : this.endOfFile;
            String secondSection = currentSection.equals(this.nordCoordSection) ? this.demandSection : this.nordCoordSection;
            if(!currentSection.equals(this.endOfFile)){
                while(fileScanner.hasNextLine()){
                    String nextLine = fileScanner.nextLine().trim();
                    if(nextLine.equals(this.depotSection)) return model;
                    if(nextLine.equals(secondSection)) currentSection = secondSection;
                    if(!nextLine.isBlank() && !nextLine.equals(secondSection)){
                        String[] formattedLine = nextLine.trim().split("\\s+");
                        Integer[] readyLine = new Integer[formattedLine.length];
                        for(int i = 0; i < formattedLine.length; i++){
                            readyLine[i] = Integer.parseInt(formattedLine[i]);
                        }
                        section_dict.get(currentSection)[readyLine[0] - 1] = readyLine;
                    }
                }
            }
        }
        fileScanner.close();
        model.createAdjacencyMatrix();
        return model;
    }
}
