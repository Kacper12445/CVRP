package CSV;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SaverCSV {
    private FileWriter writer;
    public SaverCSV(String filename) throws IOException {
        File newFile = new File(filename);
        newFile.createNewFile();
        this.writer = new FileWriter(filename);
        this.writer.write("Epoch,Best,Worst,Avg,Std\n");
    }

    public void saveEpochToCSV(int epoch, double[] populationCosts) throws IOException {
        double best = Arrays.stream(populationCosts).min().getAsDouble();
        double worst = Arrays.stream(populationCosts).max().getAsDouble();
        double avg = Arrays.stream(populationCosts).average().getAsDouble();
        double std = this.calculateStd(populationCosts);
//        System.out.println(best);
//        System.out.println(worst);
//        System.out.println(avg);
//        System.out.println(std);
        this.writer.write(epoch + "," + best + "," + worst + "," + avg + "," + std + "\n");
    }

    private double calculateStd(double[] array) {
        double sum = 0.0;
        double standard_deviation = 0.0;
        int arrayLength = array.length;
        for(double temp : array) {
            sum += temp;
        }
        double mean = sum / arrayLength;
        for(double temp: array) {
            standard_deviation += Math.pow(temp - mean, 2);
        }
        return Math.sqrt(standard_deviation / arrayLength);
    }

    public void closeFile() throws IOException {
        this.writer.close();
    }
}
