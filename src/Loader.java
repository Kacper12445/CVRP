import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Loader {
    String nordCoordSection = "NODE_COORD_SECTION";
    String demandSection = "DEMAND_SECTION";
    String depotSection = "DEPOT_SECTION";
    String endOfFile = "EOF";
    ArrayList<int[]> storedCoordData = new ArrayList<>();
    ArrayList<int[]> storedDemandData = new ArrayList<>();

    public void loadFileData(String fileName) throws IOException {
        String filePath  = new File("./DataSets/"+ fileName).getCanonicalPath();
        File fileToRead = new File(filePath + ".vrp");

        Hashtable<String, ArrayList<int[]>> section_dict = new Hashtable<>();
        section_dict.put(this.nordCoordSection, this.storedCoordData);
        section_dict.put(this.demandSection, this.storedDemandData);

        Scanner fileScanner = new Scanner(fileToRead);
        while(fileScanner.hasNextLine()){
            String line = fileScanner.nextLine();
            String currentSection = line.equals(this.nordCoordSection) ? this.nordCoordSection : line.equals(this.demandSection) ? this.demandSection : this.endOfFile;
            String secondSection = currentSection.equals(this.nordCoordSection) ? this.demandSection : this.nordCoordSection;
            if(!currentSection.equals(this.endOfFile)){
                while(fileScanner.hasNextLine()){
                    String nextLine = fileScanner.nextLine();
                    if(nextLine.equals(this.depotSection)) return;
                    if(nextLine.equals(secondSection)) currentSection = secondSection;
                    if(!nextLine.isBlank() && !nextLine.equals(secondSection)){
                        String[] formattedLine = nextLine.trim().split("\\s+");
                        int[] readyLine = new int[formattedLine.length];
                        for(int i = 0; i < formattedLine.length; i++){
                            readyLine[i] = Integer.parseInt(formattedLine[i]);
                        }
                        section_dict.get(currentSection).add(readyLine);
                    };
                }
            }
        }
        fileScanner.close();
    }
}
